package com.example.appdev.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appdev.database.entities.CardEntity
import com.example.appdev.database.entities.GoalEntity
import com.example.appdev.database.entities.UserEntity

@Dao
interface CardDao {
    @Insert
    fun insert(card: CardEntity)

    @Query("SELECT * FROM Card WHERE user_id = :user_id")
    fun getCardsOfUser(user_id: Int): List<CardEntity>

    @Query("SELECT * FROM Card WHERE card_id = :card_id")
    fun getCardOfId(card_id: Int): CardEntity

    @Query("UPDATE Card SET amount_on_card = :newAmount WHERE card_id = :cardId")
    fun updateCardAmount(cardId: Int, newAmount: Float)

    @Query("DELETE FROM Card WHERE card_id=:card_id")
    fun deleteGoalWithId(card_id: Int)
}