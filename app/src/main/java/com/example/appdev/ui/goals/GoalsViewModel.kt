package com.example.appdev.ui.goals

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appdev.MainActivity
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
        if (MainActivity.logged_user != null)
        {
            val userId = MainActivity.logged_user!!.user_id // Replace with actual user ID
            val goalEntities = goalDao.getGoalsOfUser(userId)
            val goalsWithCosts = goalEntities.map { goal ->
                val relatedCosts = recurringCostDao.selectRecurringCostsByGoal(goal.goal_id)
                GoalDetails(
                    goalId = goal.goal_id,
                    title = goal.title,
                    description = goal.description,
                    dueDate = dateFormat.format(goal.due_date),
                    amount = goal.current_amount,
                    remainingAmount = goal.target_amount,
                    relatedCosts = relatedCosts.map {
                        RelatedCost(it.cost_id, it.title, it.amount.toDouble(), it.frequency == "recurring")
                    }.toMutableList()
                )
            }
            _goals.value = goalsWithCosts
        }



    }

    fun addGoal(goal: GoalDetails) {
        if (MainActivity.logged_user !=null)
        {
            val goalEntity = GoalEntity(
                user_id = MainActivity.logged_user!!.user_id, // Replace with actual user ID
                title = goal.title,
                description = goal.description,
                target_amount = goal.amount,
                current_amount = 0.0,
                due_date = dateFormat.parse(goal.dueDate)!!
            )
            goalDao.insert(goalEntity)
            loadGoals()
        }

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
        recurringCostDao.deleteCost(relatedCost.costId)
    }

    fun deleteGoal(goalId: Int) {
        goalDao.deleteGoal(goalId)
        recurringCostDao.deleteCostsByGoal(goalId)
        loadGoals()
    }

    fun calculateBudgetImpact(averageMonthlySavings: Double): Pair<Double, Int> {
        val totalRemainingAmount = _goals.value?.sumOf { it.remainingAmount } ?: 0.00
        val monthsToGoal = predictMonthsToGoal()
        val totalSavingsNeeded = totalRemainingAmount - (monthsToGoal * averageMonthlySavings)

        val moneyLeft = if (totalSavingsNeeded > 0) {
            -totalSavingsNeeded
        } else {
            -totalSavingsNeeded
        }

        return Pair(moneyLeft, monthsToGoal)
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

        Log.d("GoalsViewModel", "Total remaining amount: $totalRemainingAmount")
        Log.d("GoalsViewModel", "Total savings needed: $totalSavingsNeeded")

        if (totalSavingsNeeded >= totalRemainingAmount) {
            Log.d("GoalsViewModel", "All goals are viable.")
            return listOf("All goals are viable.")
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
        val goalId: Int,
        val title: String,
        val description: String,
        val dueDate: String,
        val amount: Double,
        val remainingAmount: Double,
        val relatedCosts: MutableList<RelatedCost> = mutableListOf()
    )

    data class RelatedCost(
        val costId: Int,
        val title: String,
        val amount: Double,
        val isRecurring: Boolean
    )

    companion object {
        private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }
}
