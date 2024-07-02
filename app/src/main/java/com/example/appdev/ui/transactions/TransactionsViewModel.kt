package com.example.appdev.ui.transactions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.daos.TransactionsDao
import com.example.appdev.database.entities.TransactionsEntity
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.Date

class TransactionsViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionsDao: TransactionsDao
    private val _transactions = MutableLiveData<List<TransactionsEntity>>()
    val transactions: LiveData<List<TransactionsEntity>> get() = _transactions

    init {
        // Create an instance of the database
        val db = Room.databaseBuilder(
            application,
            GoalSaverDatabase::class.java, "goal_saver_database"
        ).allowMainThreadQueries().build()

        transactionsDao = db.transactionDao()
        loadTransactions()
    }

    private fun loadTransactions() {
        _transactions.value = transactionsDao.getTransactionsByUser(0) // Example user_id
    }

    fun addTransaction(transaction: TransactionsEntity) {
        transactionsDao.insert(transaction)
        loadTransactions()
    }

    fun deleteTransaction(transactionId: Int) {
        transactionsDao.deleteTransaction(transactionId)
        loadTransactions()
    }

    fun calculateTotalEarnings(): Float {
        return _transactions.value?.filter { it.amount > 0 }?.sumOf { it.amount.toDouble() }?.toFloat() ?: 0f
    }

    fun calculateTotalSpent(): Float {
        return _transactions.value?.filter { it.amount < 0 }?.sumOf { it.amount.toDouble() }?.toFloat() ?: 0f
    }

    fun calculateTotalSaved(): Float {
        return calculateTotalEarnings() + calculateTotalSpent() // since spent amounts are negative
    }

    fun readCsvFile(filePath: String) {
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
                val date = Date(values[dateIndex]) // Assuming date is in a parseable format
                val description = values[descriptionIndex]

                val transaction = TransactionsEntity(
                    user_id = 0, // Example user_id
                    type = if (amount >= 0) '+' else '-',
                    amount = amount,
                    currency = "USD", // Example currency
                    date = date,
                    isRecurring = false, // Example value
                    description = description
                )
                addTransaction(transaction)
                line = bufferedReader.readLine()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            bufferedReader?.close()
        }
    }
}
