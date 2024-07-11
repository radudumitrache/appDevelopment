package com.example.appdev.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appdev.database.entities.FriendEntity

@Dao
interface FriendDao {
    @Insert
    fun addFriend(friend: FriendEntity)

    @Query("SELECT * FROM Friend WHERE user_id = :userId")
    fun getFriends(userId: Int): List<FriendEntity>

    @Query("DELETE FROM Friend WHERE user_id = :userId AND friend_id = :friendId")
    fun removeFriend(userId: Int, friendId: Int)
}
