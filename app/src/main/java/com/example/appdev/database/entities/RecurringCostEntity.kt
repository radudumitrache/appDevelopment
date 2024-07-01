package com.example.appdev.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "RecurringCost",
    foreignKeys = [
        ForeignKey(entity = GoalEntity::class, parentColumns = ["goal_id"], childColumns = ["goal_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = UserEntity::class, parentColumns = ["user_id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)
    ]
    )
data class RecurringCostEntity (
    @PrimaryKey(autoGenerate = true) val cost_id: Int = 0,
    val goal_id : Int,
    val user_id : Int,
    val title : String,
    val amount : Float,
    val currency : String,
    val frequency : String
)