package com.example.appdev.ui.goals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.entities.GoalEntity
import com.example.appdev.database.entities.RecurringCostEntity
import java.text.SimpleDateFormat
import java.util.*

class GoalsViewModel(application: Application) : AndroidViewModel(application) {

    private val goalDao = GoalSaverDatabase.getDatabase(application).goalDao()
    private val recurringCostDao = GoalSaverDatabase.getDatabase(application).recurringCostDao()

    private val _goals = MutableLiveData<List<GoalDetails>>()
    val goals: LiveData<List<GoalDetails>> get() = _goals

    init {
        loadGoals()
    }

    private fun loadGoals() {
        val userId = 1 // Replace with actual user ID
        val goalEntities = goalDao.getGoalsOfUser(userId)
        val goalsWithCosts = goalEntities.map { goal ->
            val relatedCosts = recurringCostDao.selectRecurringCostsByGoal(goal.goal_id)
            GoalDetails(
                title = goal.title,
                description = goal.description,
                dueDate = goal.due_date.toString(),
                amount = goal.current_amount,
                remainingAmount = goal.target_amount,
                relatedCosts = relatedCosts.map {
                    RelatedCost(it.title, it.amount.toDouble(), it.frequency == "recurring")
                }.toMutableList()
            )
        }
        _goals.value = goalsWithCosts
    }

    fun addGoal(goal: GoalDetails) {
        val goalEntity = GoalEntity(
            user_id = 1, // Replace with actual user ID
            title = goal.title,
            description = goal.description,
            target_amount = goal.amount,
            current_amount = 0.0,
            due_date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(goal.dueDate)!!
        )
        goalDao.insert(goalEntity)
        loadGoals()
    }

    fun addRelatedCost(goalTitle: String, relatedCost: RelatedCost) {
        val currentGoals = _goals.value?.toMutableList() ?: mutableListOf()
        val goal = currentGoals.find { it.title == goalTitle }
        goal?.relatedCosts?.add(relatedCost)
        _goals.value = currentGoals
    }

    fun removeRelatedCost(goalTitle: String, relatedCost: RelatedCost) {
        val currentGoals = _goals.value?.toMutableList() ?: mutableListOf()
        val goal = currentGoals.find { it.title == goalTitle }
        goal?.relatedCosts?.remove(relatedCost)
        _goals.value = currentGoals
    }

    fun calculateBudgetImpact(averageMonthlySavings: Double): Double {
        val totalRemainingAmount = _goals.value?.sumOf { it.remainingAmount } ?: 0.0
        val monthsToGoal = predictMonthsToGoal()
        return (totalRemainingAmount - (monthsToGoal * averageMonthlySavings)).coerceAtLeast(0.0)
    }

    fun predictMonthsToGoal(): Int {
        val goals = _goals.value ?: return 0
        val latestDueDate = goals.maxOfOrNull { parseDate(it.dueDate) } ?: return 0
        val monthsUntilDueDate = calculateMonthsUntilDate(latestDueDate)
        return monthsUntilDueDate
    }

    private fun parseDate(dateStr: String): Date {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.parse(dateStr) ?: Date()
    }

    private fun calculateMonthsUntilDate(dueDate: Date): Int {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        calendar.time = dueDate
        val dueYear = calendar.get(Calendar.YEAR)
        val dueMonth = calendar.get(Calendar.MONTH)
        return (dueYear - currentYear) * 12 + (dueMonth - currentMonth)
    }

    fun checkGoalsViability(averageMonthlySavings: Double): List<String> {
        val goals = _goals.value ?: return emptyList()
        val totalRemainingAmount = goals.sumOf { it.remainingAmount }
        val monthsToGoal = predictMonthsToGoal()
        val totalSavingsNeeded = monthsToGoal * averageMonthlySavings

        if (totalSavingsNeeded >= totalRemainingAmount) {
            return emptyList() // All goals are viable
        }

        // Identify which goals to delete
        val goalsSortedByPriority = goals.sortedByDescending { it.remainingAmount }
        val nonViableGoals = mutableListOf<String>()
        var accumulatedSavings = 0.0

        for (goal in goalsSortedByPriority) {
            accumulatedSavings += goal.remainingAmount
            if (accumulatedSavings > totalSavingsNeeded) {
                break
            }
            nonViableGoals.add(goal.title)
        }

        return nonViableGoals
    }

    data class GoalDetails(
        val title: String,
        val description: String,
        val dueDate: String,
        val amount: Double,
        val remainingAmount: Double,
        val relatedCosts: MutableList<RelatedCost> = mutableListOf()
    )

    data class RelatedCost(
        val title: String,
        val amount: Double,
        val isRecurring: Boolean
    )
}
