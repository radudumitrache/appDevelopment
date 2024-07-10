package com.example.appdev.database.entities
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Card", foreignKeys = [
    ForeignKey(entity = UserEntity::class, parentColumns = ["user_id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)
])
data class CardEntity (
    @PrimaryKey(autoGenerate = true) val card_id : Int = 0,
    val user_id : Int,
    val name_on_card : Int,
    val currency_type : String,
    val bank_name : String,
    val amount_on_card : Float
)
