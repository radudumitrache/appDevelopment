package com.example.appdev.ui.goals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.appdev.MainActivity
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.entities.GoalEntity
import java.text.SimpleDateFormat
import java.util.*

class CreateGoalViewModel(application: Application) : AndroidViewModel(application) {

    private val goalDao = GoalSaverDatabase.getDatabase(application).goalDao()

    var goalTitle: String = ""
    var goalDescription: String = ""
    var dueDate: String = ""
    var price: String = ""

    fun createGoal(title: String, description: String, date: String, price: String) {
        goalTitle = title
        goalDescription = description
        dueDate = date
        this.price = price

        val goalEntity = GoalEntity(
            user_id = MainActivity.logged_user!!.user_id,
            title = title,
            description = description,
            target_amount = price.toDouble(),
            current_amount = 0.0,
            due_date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(date)!!
        )
        goalDao.insert(goalEntity)

    }
}
