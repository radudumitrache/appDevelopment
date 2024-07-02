package com.example.appdev.ui.goals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.entities.GoalEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class GoalsViewModel(application: Application) : AndroidViewModel(application) {
    private val goalDao = GoalSaverDatabase.getDatabase(application).goalDao()
    private val _goals = MutableLiveData<List<GoalEntity>>()
    val goals: LiveData<List<GoalEntity>> get() = _goals

    private val _relatedCosts = MutableLiveData<List<RelatedCost>>()
    val relatedCosts: LiveData<List<RelatedCost>> get() = _relatedCosts

    init {
        loadGoals()
        _relatedCosts.value = listOf(
            RelatedCost("Insurance", 400.0, false),
            RelatedCost("Tires", 400.0, false)
        )
    }

    private fun loadGoals() {
        viewModelScope.launch(Dispatchers.IO) {
            val userGoals = goalDao.getGoalsOfUser(1) // Example user_id
            _goals.postValue(userGoals)
        }
    }

    fun addGoal(goal: GoalDetails) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val date = sdf.parse(goal.dueDate)

        if (date != null) {
            val goalEntity = GoalEntity(
                user_id = 1, // Example user_id
                title = goal.title,
                description = goal.description,
                category = "", // Add category if necessary
                target_amount = goal.targetAmount.toString(),
                current_amount = goal.currentAmount.toString(),
                due_date = date
            )

            viewModelScope.launch(Dispatchers.IO) {
                goalDao.insert(goalEntity)
                loadGoals()
            }
        }
    }

    fun addRelatedCost(relatedCost: RelatedCost) {
        val currentCosts = _relatedCosts.value?.toMutableList() ?: mutableListOf()
        currentCosts.add(relatedCost)
        _relatedCosts.value = currentCosts
    }

    fun calculateBudgetImpact(): Double {
        val totalRelatedCost = _relatedCosts.value?.sumOf { it.amount } ?: 0.0
        val goals = _goals.value ?: return 0.0
        return goals.sumOf { it.target_amount.toDouble() - it.current_amount.toDouble() } - totalRelatedCost
    }

    fun predictMonthsToGoal(): Int {
        val goals = _goals.value ?: return 0
        val totalMonthlySavings = goals.sumOf { it.target_amount.toDouble() - it.current_amount.toDouble() }
        if (totalMonthlySavings <= 0) return Int.MAX_VALUE // To avoid division by zero
        return (totalMonthlySavings / goals.sumOf { it.target_amount.toDouble() }).toInt()
    }

    data class GoalDetails(
        val title: String,
        val description: String,
        val dueDate: String,
        val targetAmount: Double,
        val currentAmount: Double,
        val monthlySavings: Double
    )

    data class RelatedCost(val title: String, val amount: Double, val isRecurring: Boolean)
}
