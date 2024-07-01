package com.example.appdev.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appdev.database.daos.GoalDao
import com.example.appdev.database.daos.TransactionsDao
import com.example.appdev.database.daos.UserDao
import com.example.appdev.database.entities.GoalEntity
import com.example.appdev.database.entities.TransactionsEntity
import com.example.appdev.database.entities.UserEntity

@Database(entities = [UserEntity::class,TransactionsEntity::class,GoalEntity::class], version = 1)
abstract class GoalSaverDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun transactionsDao() : TransactionsDao
    abstract fun goalDao() : GoalDao
}