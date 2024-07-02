package com.example.appdev.ui.goals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.entities.GoalEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateGoalViewModel(application: Application) : AndroidViewModel(application) {
    private val goalDao = GoalSaverDatabase.getDatabase(application).goalDao()

    fun createGoal(title: String, description: String, dueDate: String, price: String, monthlySavings: String) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val date = sdf.parse(dueDate)

        if (date != null) {
            val goal = GoalEntity(
                user_id = 1, // Example user_id
                title = title,
                description = description,
                category = "", // Add category if necessary
                target_amount = price,
                current_amount = "0",
                due_date = date
            )

            viewModelScope.launch(Dispatchers.IO) {
                goalDao.insert(goal)
            }
        }
    }
}
