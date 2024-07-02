package com.example.appdev.ui.goals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GoalsViewModel : ViewModel() {

    private val _goals = MutableLiveData<List<GoalDetails>>()
    val goals: LiveData<List<GoalDetails>> get() = _goals

    private val _relatedCosts = MutableLiveData<List<RelatedCost>>()
    val relatedCosts: LiveData<List<RelatedCost>> get() = _relatedCosts

    init {
        // Initialize with dummy data
        _goals.value = listOf(
            GoalDetails(
                "New Car",
                "Save money for a new car",
                "31/12/2024",
                3000.0,
                2500.0,
                500.0 // Monthly savings
            )
        )

        _relatedCosts.value = listOf(
            RelatedCost("Insurance", 400.0, false),
            RelatedCost("Tires", 400.0, false)
        )
    }

    fun addGoal(goal: GoalDetails) {
        val currentGoals = _goals.value?.toMutableList() ?: mutableListOf()
        currentGoals.add(goal)
        _goals.value = currentGoals
    }

    fun addRelatedCost(relatedCost: RelatedCost) {
        val list = _relatedCosts.value ?: emptyList()
        _relatedCosts.value = list + relatedCost
    }

    fun calculateBudgetImpact(): Double {
        val totalRelatedCost = _relatedCosts.value?.sumOf { it.amount } ?: 0.0
        val goals = _goals.value ?: return 0.0
        return goals.sumOf { it.remainingAmount } - totalRelatedCost
    }

    fun predictMonthsToGoal(): Int {
        val goals = _goals.value ?: return 0
        val totalMonthlySavings = goals.sumOf { it.monthlySavings }
        if (totalMonthlySavings <= 0) return Int.MAX_VALUE // To avoid division by zero
        return (goals.sumOf { it.amount - it.remainingAmount } / totalMonthlySavings).toInt()
    }

    data class GoalDetails(
        val title: String,
        val description: String,
        val dueDate: String,
        val amount: Double,
        val remainingAmount: Double,
        val monthlySavings: Double
    )

    data class RelatedCost(val title: String, val amount: Double, val isRecurring: Boolean)
}
