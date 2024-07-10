package com.example.appdev.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Transaction",
    foreignKeys = [
        ForeignKey(entity = CardEntity::class, parentColumns = ["card_id"], childColumns = ["card_id"], onDelete = ForeignKey.CASCADE)])
data class TransactionsEntity (
    @PrimaryKey(autoGenerate = true) val transaction_id : Int = 0,
    val card_id: Int,
    val type : Char,
    val amount : Float,
    val date : Date,
    val isRecurring : Boolean,
    val description : String
)