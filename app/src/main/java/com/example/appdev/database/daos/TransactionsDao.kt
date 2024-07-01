package com.example.appdev.database.daos
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appdev.database.entities.TransactionsEntity

@Dao
interface TransactionsDao {
    @Insert
    fun insert(transaction : TransactionsEntity)
    @Query("SELECT * FROM `Transaction` WHERE user_id = :user_id")
    fun getTransactionsByUser(user_id :Int) : List<TransactionsEntity>
    @Query("SELECT * FROM `Transaction` WHERE user_id = :user_id AND type = :transaction_type")
    fun getTransactionsByUserType(user_id: Int,transaction_type:String) : List<TransactionsEntity>
    @Query("DELETE FROM `Transaction` WHERE transaction_id = :transaction_id")
    fun deleteTransaction(transaction_id: Int)
}