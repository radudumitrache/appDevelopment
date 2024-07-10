package com.example.appdev.database.daos
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appdev.database.entities.UserEntity
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface UserDao {

    @Insert
    fun insert(user: UserEntity)

    @Query("SELECT * FROM User")
    fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM User WHERE email = :email")
    fun getUserByEmail(email :String): UserEntity?
    @Query("SELECT * From User WHERE user_id = :id")
    fun getUserById(id : Int) : UserEntity
    @Update
    fun updateUser(user: UserEntity)
}