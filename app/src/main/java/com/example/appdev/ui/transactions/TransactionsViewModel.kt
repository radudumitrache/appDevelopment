package com.example.appdev.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

    data class Transaction(val amount: Int, val description: String, val date: String)
}
