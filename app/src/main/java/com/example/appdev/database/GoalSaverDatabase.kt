package com.example.appdev.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appdev.database.daos.*
import com.example.appdev.database.entities.*
import com.example.appdev.util.Converters

@Database(
    entities = [
        UserEntity::class,
        TransactionsEntity::class,
        GoalEntity::class,
        RecurringCostEntity::class,
        CardEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GoalSaverDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionsDao
    abstract fun goalDao(): GoalDao
    abstract fun recurringCostDao(): RecurringCostDao
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var INSTANCE: GoalSaverDatabase? = null

        fun getDatabase(context: Context): GoalSaverDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GoalSaverDatabase::class.java,
                    "goal_saver_database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
