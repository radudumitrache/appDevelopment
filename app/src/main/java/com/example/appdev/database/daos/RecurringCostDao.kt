package com.example.appdev.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appdev.database.entities.RecurringCostEntity

@Dao
interface RecurringCostDao {
    @Insert
    fun insert(recurringCost: RecurringCostEntity)

    @Query("SELECT * FROM RecurringCost WHERE goal_id = :goalId")
    fun selectRecurringCostsByGoal(goalId: Int): List<RecurringCostEntity>

    @Query("DELETE FROM RecurringCost WHERE cost_id = :costId")
    fun deleteCost(costId: Int)

    @Query("DELETE FROM RecurringCost WHERE goal_id = :goalId")
    fun deleteCostsByGoal(goalId: Int)
}
