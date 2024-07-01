package com.example.appdev.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class TransactionsViewModel : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    init {
        // Initial dummy data
        _transactions.value = listOf(
            Transaction(-30, "Groceries", "11/11/24"),
            Transaction(50, "Salary", "11/11/24")
        )
    }

    // Function to add a new transaction
    fun addTransaction(transaction: Transaction) {
        val currentList = _transactions.value ?: emptyList()
        _transactions.value = currentList + transaction
    }

    // Function to calculate total earnings
    fun calculateTotalEarnings(): Int {
        return _transactions.value?.filter { it.amount > 0 }?.sumOf { it.amount } ?: 0
    }

    // Function to calculate total spent
    fun calculateTotalSpent(): Int {
        return _transactions.value?.filter { it.amount < 0 }?.sumOf { it.amount } ?: 0
    }

    // Function to calculate total saved
    fun calculateTotalSaved(): Int {
        return calculateTotalEarnings() + calculateTotalSpent() // since spent amounts are negative
    }

    fun readCsvFile(filePath: String) {
        var bufferedReader: BufferedReader? = null
        val amounts = mutableListOf<Double>()
        val types = mutableListOf<String>()
        val dates = mutableListOf<String>()

        try {
            bufferedReader = BufferedReader(FileReader(filePath))

            // Reading the header line
            val headerLine = bufferedReader.readLine()
            val headers = headerLine.split(",")

            // Indices for Amount and Type
            val amountIndex = headers.indexOf("Amount")
            val dateIndex = headers.indexOf("Started Date")
            val descriptionIndex = headers.indexOf("Description")

            if (amountIndex == -1 || descriptionIndex == -1 || dateIndex == -1) {
                println("CSV does not contain required headers.")
                return
            }

            // Reading the rest of the lines
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                val values = line.split(",")
                val amount = values[amountIndex].toDouble().toInt()
                val date = values[dateIndex]
                val type = values[descriptionIndex]

                addTransaction(Transaction(amount, type, date))

                line = bufferedReader.readLine()
            }



        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            bufferedReader?.close()
        }
    }

    data class Transaction(val amount: Int, val description: String, val date: String)
}
