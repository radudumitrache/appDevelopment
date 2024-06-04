package com.example.appdev.ui.goals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GoalsViewModel : ViewModel() {

    private val _goalTitle = MutableLiveData<String>()
    val goalTitle: LiveData<String> get() = _goalTitle

    private val _goalDescription = MutableLiveData<String>()
    val goalDescription: LiveData<String> get() = _goalDescription

    private val _dueDate = MutableLiveData<String>()
    val dueDate: LiveData<String> get() = _dueDate

    private val _amountSaved = MutableLiveData<String>()
    val amountSaved: LiveData<String> get() = _amountSaved

    private val _remainingAmount = MutableLiveData<String>()
    val remainingAmount: LiveData<String> get() = _remainingAmount

    private val _costTitle = MutableLiveData<String>()
    val costTitle: LiveData<String> get() = _costTitle

    private val _amount = MutableLiveData<String>()
    val amount: LiveData<String> get() = _amount

    init {
        // Initial dummy data
        _goalTitle.value = "Save for a car"
        _goalDescription.value = "Save money for a new car"
        _dueDate.value = "31/12/2024"
        _amountSaved.value = "$3000"
        _remainingAmount.value = "$2000"
        _costTitle.value = "Insurance"
        _amount.value = "$500"
    }
}
