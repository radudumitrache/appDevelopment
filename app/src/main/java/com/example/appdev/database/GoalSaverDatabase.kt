package com.example.appdev.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appdev.database.daos.GoalDao
import com.example.appdev.database.daos.TransactionsDao
import com.example.appdev.database.daos.UserDao
import com.example.appdev.database.entities.GoalEntity
import com.example.appdev.database.entities.TransactionsEntity
import com.example.appdev.database.entities.UserEntity
import com.example.appdev.util.Converters
import androidx.room.Room
import android.content.Context
import com.example.appdev.database.daos.RecurringCostDao
import com.example.appdev.database.entities.RecurringCostEntity

@Database(entities = [UserEntity::class,TransactionsEntity::class,GoalEntity::class,RecurringCostEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GoalSaverDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun transactionDao() : TransactionsDao
    abstract fun goalDao() : GoalDao
    abstract fun recurringCostDao() : RecurringCostDao
    companion object {
        @Volatile
        private var INSTANCE: GoalSaverDatabase? = null
        fun getDatabase(context: Context): GoalSaverDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GoalSaverDatabase::class.java,
                    "goal_saver_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }

    }
}