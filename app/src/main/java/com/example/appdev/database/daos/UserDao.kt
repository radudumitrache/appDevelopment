package com.example.appdev.database.daos
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appdev.database.entities.UserEntity
import androidx.room.OnConflictStrategy
@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM User")
    suspend fun getAllUsers(): List<UserEntity>
}