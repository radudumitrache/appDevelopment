package com.example.appdev.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appdev.database.entities.RecurringCostEntity
@Dao
interface RecurringCostDao {
    @Insert
    fun insert(recurringCost : RecurringCostEntity)

    @Query("SELECT * FROM RecurringCost WHERE goal_id = :goalId")
    fun select_recurring_costs_by_goal(goalId : Int)  : List<RecurringCostEntity>

    @Query("DELETE FROM RecurringCost where cost_id = :cost_id")
    fun delete_cost(cost_id : Int)
}