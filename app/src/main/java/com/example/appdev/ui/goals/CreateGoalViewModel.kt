package com.example.appdev.ui.goals

import androidx.lifecycle.ViewModel

class CreateGoalViewModel : ViewModel() {
    var goalTitle: String = ""
    var goalDescription: String = ""
    var dueDate: String = ""
    var price1: String = ""

    fun createGoal(title: String, description: String, date: String, price2: String) {
        goalTitle = title
        goalDescription = description
        dueDate = date
        price1 = price2
        // add to db
    }
}

