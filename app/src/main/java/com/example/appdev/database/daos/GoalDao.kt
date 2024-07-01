package com.example.appdev.database.daos
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appdev.database.entities.GoalEntity
import com.example.appdev.database.entities.UserEntity
@Dao
interface GoalDao {
    @Insert
    suspend fun insert(goal : GoalEntity)
    @Query("SELECT * FROM Goal WHERE user_id = :user_id ")
    suspend fun getGoalsOfUser(user_id : Int)
}