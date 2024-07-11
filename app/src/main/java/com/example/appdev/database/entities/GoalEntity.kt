package com.example.appdev.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Goal",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["user_id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)])
data class GoalEntity (
    @PrimaryKey(autoGenerate = true) val goal_id : Int = 0,
    val user_id : Int,
    val title : String,
    val description : String,
    val target_amount : Double,
    val current_amount : Double,
    val due_date : Date
)
