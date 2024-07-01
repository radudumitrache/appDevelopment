package com.example.appdev.database.daos
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appdev.database.entities.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user:UserEntity)
    @Query("SELECT * FROM user WHERE user_id = :id")
    suspend fun getUserById(id: Int): UserEntity?
    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUserByEmail(email:String) :UserEntity?
    @Query("DELETE FROM User")
    suspend fun deleteAll()
}