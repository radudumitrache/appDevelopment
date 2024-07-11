package com.example.appdev.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Friend",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["user_id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = UserEntity::class, parentColumns = ["user_id"], childColumns = ["friend_id"], onDelete = ForeignKey.CASCADE)
    ]
)
data class FriendEntity(
    @PrimaryKey(autoGenerate = true) val friendship_id: Int = 0,
    val user_id: Int,
    val friend_id: Int,
    val name: String
)
