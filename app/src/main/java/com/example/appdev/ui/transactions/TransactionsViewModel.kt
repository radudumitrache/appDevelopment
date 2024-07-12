package com.example.appdev.ui.transactions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.appdev.MainActivity
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.daos.TransactionsDao
import com.example.appdev.database.entities.TransactionsEntity
import com.example.appdev.ui.dashboard.DashboardFragment
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class TransactionsViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionsDao: TransactionsDao
    private val _transactions = MutableLiveData<List<TransactionsEntity>>()
    private val logged_user = MainActivity.logged_user
    val transactions: LiveData<List<TransactionsEntity>> get() = _transactions
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    init {
        val db = Room.databaseBuilder(
            application, GoalSaverDatabase::class.java, "goal_saver_database"
        ).allowMainThreadQueries().build()

        transactionsDao = db.transactionDao()

        AverageSavingsCalculator.initialize(application.applicationContext)
        loadAllTransactions()
    }

    private fun loadAllTransactions() {
        if (logged_user != null) {
            _transactions.value = transactionsDao.getTransactionsByUserId(logged_user.user_id)
        }
    }

    fun addTransaction(transaction: TransactionsEntity) {
        transactionsDao.insert(transaction)
        loadAllTransactions()
    }

    fun deleteTransaction(transactionId: Int) {
        val cardId = transactionsDao.getTransactionByID(transactionId).card_id
        transactionsDao.deleteTransaction(transactionId)
        loadAllTransactions()
    }

    fun calculateTotalEarnings(): Float {
        return _transactions.value?.filter { it.amount > 0 }?.sumOf { it.amount.toDouble() }
            ?.toFloat() ?: 0f
    }

    fun calculateTotalSpent(): Float {
        return _transactions.value?.filter { it.amount < 0 }?.sumOf { it.amount.toDouble() }
            ?.toFloat() ?: 0f
    }

    fun calculateTotalSaved(): Float {
        return calculateTotalEarnings() + calculateTotalSpent() // since spent amounts are negative
    }

    fun readCsvFile(filePath: String, cardId: Int) {
        var bufferedReader: BufferedReader? = null
        try {
            bufferedReader = BufferedReader(FileReader(filePath))
            val headerLine = bufferedReader.readLine()
            val headers = headerLine.split(",")

            val amountIndex = headers.indexOf("Amount")
            val dateIndex = headers.indexOf("Started Date")
            val descriptionIndex = headers.indexOf("Description")

            if (amountIndex == -1 || descriptionIndex == -1 || dateIndex == -1) {
                println("CSV does not contain required headers.")
                return
            }

            var line: String? = bufferedReader.readLine()
            while (line != null) {
                val values = line.split(",")
                val amount = values[amountIndex].toFloat()
                val dateString = values[dateIndex].trim()
                val date = try {
                    dateFormat.parse(dateString)
                        ?: throw IllegalArgumentException("Invalid date format")
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                if (date != null) {
                    val description = values[descriptionIndex]
                    if (logged_user != null) {
                        val transaction = TransactionsEntity(
                            card_id = cardId, // Use the selected card ID
                            type = if (amount >= 0) '+' else '-',
                            amount = amount,
                            date = date,
                            isRecurring = false,
                            description = description
                        )
                        val card = GoalSaverDatabase.getDatabase(getApplication()).cardDao()
                            .getCardOfId(cardId)
                        if (transaction.type == '+') {
                            val amountToChange = card.amount_on_card + amount
                            GoalSaverDatabase.getDatabase(getApplication()).cardDao()
                                .updateCardAmount(cardId, amountToChange)
                        } else {
                            val amountToChange = card.amount_on_card + amount
                            GoalSaverDatabase.getDatabase(getApplication()).cardDao()
                                .updateCardAmount(cardId, amountToChange)
                        }
                        addTransaction(transaction)
                    }
                }

                line = bufferedReader.readLine()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            bufferedReader?.close()
        }
    }
}
