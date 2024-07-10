package com.example.appdev.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appdev.database.entities.GoalEntity

@Dao
interface GoalDao {
    @Insert
    fun insert(goal: GoalEntity)

    @Query("SELECT * FROM Goal WHERE user_id = :user_id")
    fun getGoalsOfUser(user_id: Int): List<GoalEntity>
}
