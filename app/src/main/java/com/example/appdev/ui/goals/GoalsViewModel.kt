package com.example.appdev.ui.goals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GoalsViewModel : ViewModel() {

    private val _goalDetails = MutableLiveData<GoalDetails>()
    val goalDetails: LiveData<GoalDetails> get() = _goalDetails

    private val _relatedCosts = MutableLiveData<List<RelatedCost>>()
    val relatedCosts: LiveData<List<RelatedCost>> get() = _relatedCosts

    init {
        _goalDetails.value = GoalDetails(
            "New Car",
            "Save money for a new car",
            "31/12/2024",
            "$3000",
            "$2500")

        _relatedCosts.value = listOf(
            RelatedCost("Insurance", 400.0),
            RelatedCost("Tires", 400.0),
        )
    }

    fun addRelatedCost(relatedCost: RelatedCost) {
        val list = _relatedCosts.value ?: emptyList()
        _relatedCosts.value = list + relatedCost
    }

    data class GoalDetails(val title: String, val description: String, val dueDate: String, val amount: String, val remainingAmount: String)
    data class RelatedCost(val title: String, val amount: Double)
}
