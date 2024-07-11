package com.example.appdev.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appdev.database.entities.FriendRequestEntity

@Dao
interface FriendRequestDao {
    @Insert
    fun sendFriendRequest(request: FriendRequestEntity)

    @Query("SELECT * FROM FriendRequest WHERE receiver_id = :userId AND status = 'pending'")
    fun getPendingRequests(userId: Int): List<FriendRequestEntity>

    @Query("UPDATE FriendRequest SET status = :status WHERE request_id = :requestId")
    fun updateRequestStatus(requestId: Int, status: String)
}
