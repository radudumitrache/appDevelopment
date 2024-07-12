package com.example.appdev.database.daos
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appdev.database.entities.TransactionsEntity

@Dao
interface TransactionsDao {
    @Insert
    fun insert(transaction: TransactionsEntity)

    @Query("SELECT * FROM `Transaction` WHERE card_id = :card_id")
    fun getTransactionsByCard(card_id: Int): List<TransactionsEntity>

    @Query("SELECT * FROM `Transaction` WHERE card_id = :card_id AND type = :transaction_type")
    fun getTransactionsByUserType(card_id: Int, transaction_type: String): List<TransactionsEntity>

    @Query("SELECT * FROM `Transaction` WHERE card_id = :card_id AND isRecurring = :isRecurring")
    fun getRecurringTransactionsOfCard(card_id: Int, isRecurring: Int): List<TransactionsEntity>

    @Query("SELECT * FROM `Transaction` WHERE transaction_id = :transaction_id")
    fun getTransactionByID(transaction_id: Int): TransactionsEntity

    @Query("""
        SELECT t.*
        FROM `Transaction` t
        INNER JOIN Card c ON t.card_id = c.card_id
        WHERE c.user_id = :userId
    """)
    fun getTransactionsByUserId(userId: Int): List<TransactionsEntity>

    @Query("SELECT * FROM `Transaction`")
    fun getAllTransactions(): List<TransactionsEntity>

    @Query("DELETE FROM `Transaction` WHERE transaction_id = :transaction_id")
    fun deleteTransaction(transaction_id: Int)
}