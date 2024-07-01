package com.example.appdev.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserEntity (
        @PrimaryKey(autoGenerate = true) val user_id: Int = 0,
        val email: String,
        val password: String,
        val profession: String,
        val age: String,
        val monthly_salary: Float,
        val preffered_currency: String
)