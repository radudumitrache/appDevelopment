package com.example.appdev.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Transaction",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["user_id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)])
data class TransactionsEntity (
    @PrimaryKey(autoGenerate = true) val transaction_id : Int = 0,
    val user_id: Int,
    val type : Char,
    val amount : Float,
    val currency : String,
    val date : Date,
    val isRecurring : Boolean,
    val description : String
)