package com.example.appdev.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "FriendRequest",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["user_id"], childColumns = ["sender_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = UserEntity::class, parentColumns = ["user_id"], childColumns = ["receiver_id"], onDelete = ForeignKey.CASCADE)
    ]
)
data class FriendRequestEntity(
    @PrimaryKey(autoGenerate = true) val request_id: Int = 0,
    val sender_id: Int,
    val receiver_id: Int,
    val sender_email: String,
    val status: String
)
