package com.example.appdev.ui.goals

import androidx.lifecycle.ViewModel

class CreateGoalViewModel : ViewModel() {
    var goalTitle: String = ""
    var goalDescription: String = ""
    var dueDate: String = ""
    var price: String = ""
    var monthlySavings: String = ""

    fun createGoal(title: String, description: String, date: String, price: String, monthlySavings: String) {
        goalTitle = title
        goalDescription = description
        dueDate = date
        this.price = price
        this.monthlySavings = monthlySavings
        // Add to db or handle further logic
    }
}