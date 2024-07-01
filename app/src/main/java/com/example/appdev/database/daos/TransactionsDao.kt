package com.example.appdev.database.daos
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appdev.database.entities.TransactionsEntity

@Dao
interface TransactionsDao {
    @Insert
    suspend fun insert(transaction : TransactionsEntity)
    @Query("SELECT * FROM `Transaction` WHERE user_id = :user_id")
    suspend fun getTransactionsByUser(user_id :Int)
    @Query("SELECT * FROM `Transaction` WHERE user_id = :user_id AND type = :transaction_type")
    suspend fun getTransactionsByUserType(user_id: Int,transaction_type:String)
    @Query("DELETE FROM `Transaction` WHERE transaction_id = :transaction_id")
    suspend fun deleteTransaction(transaction_id: Int)
}