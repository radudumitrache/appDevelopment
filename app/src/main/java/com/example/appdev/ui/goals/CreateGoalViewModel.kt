package com.example.appdev.ui.goals

import androidx.lifecycle.ViewModel

class CreateGoalViewModel : ViewModel() {
    var goalTitle: String = ""
    var goalDescription: String = ""
    var dueDate: String = ""
    var price: String = ""

    fun createGoal(title: String, description: String, date: String, price: String) {
        goalTitle = title
        goalDescription = description
        dueDate = date
        this.price = price
        // Add to db or handle further logic
    }
}
