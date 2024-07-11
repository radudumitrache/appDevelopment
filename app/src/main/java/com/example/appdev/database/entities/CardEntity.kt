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
    val first_digits_of_card : String,
    val expiry_date : Date,
    val name_on_card : String,
    val currency_type : String,
    val bank_name : String,
    var amount_on_card : Float
)
