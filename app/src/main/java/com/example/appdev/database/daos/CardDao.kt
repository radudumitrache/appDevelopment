package com.example.appdev.database.daos
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appdev.database.entities.CardEntity
import com.example.appdev.database.entities.GoalEntity
import com.example.appdev.database.entities.UserEntity
interface CardDao {
    @Insert
    fun insert(card : CardEntity)

    @Query("SELECT * FROM Card WHERE user_id = :user_id")
    fun getCardsOfUser(user_id : Int) : List<CardEntity>

    @Query("SELECT * FROM Card WHERE card_id = :card_id")
    fun getCardOfId(card_id : Int) : CardEntity

    @Query("DELETE FROM Card WHERE card_id=:card_id")
    fun deleteGoalWithId(card_id : Int)
}