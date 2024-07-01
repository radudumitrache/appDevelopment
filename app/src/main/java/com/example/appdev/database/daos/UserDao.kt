package com.example.appdev.database.daos
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appdev.database.entities.UserEntity
import androidx.room.OnConflictStrategy
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user:UserEntity)
    @Query("SELECT * FROM User WHERE user_id = :id")
    suspend fun getUserById(id: Int): UserEntity?
    @Query("SELECT * FROM User WHERE email = :email")
    suspend fun getUserByEmail(email:String) :UserEntity?
    @Query("DELETE FROM User")
    suspend fun deleteAll()
}