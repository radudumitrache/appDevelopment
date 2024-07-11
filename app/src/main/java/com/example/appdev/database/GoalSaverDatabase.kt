package com.example.appdev.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appdev.database.daos.*
import com.example.appdev.database.entities.*
import com.example.appdev.util.Converters
import com.example.appdev.database.daos.UserDao
import com.example.appdev.database.daos.FriendDao
import com.example.appdev.database.daos.FriendRequestDao
import com.example.appdev.database.entities.FriendEntity
import com.example.appdev.database.entities.FriendRequestEntity
import com.example.appdev.database.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TransactionsEntity::class,
        GoalEntity::class,
        RecurringCostEntity::class,
        CardEntity::class,
        FriendEntity::class,
        FriendRequestEntity::class,
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
    abstract fun friendDao(): FriendDao
    abstract fun friendRequestDao(): FriendRequestDao

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
