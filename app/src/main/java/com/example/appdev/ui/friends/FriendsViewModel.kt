package com.example.appdev.ui.friends

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appdev.MainActivity
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.daos.FriendDao
import com.example.appdev.database.daos.FriendRequestDao
import com.example.appdev.database.entities.FriendEntity
import com.example.appdev.database.entities.FriendRequestEntity

class FriendsViewModel : ViewModel() {

    private lateinit var friendDao: FriendDao
    private lateinit var friendRequestDao: FriendRequestDao

    private val _friends = MutableLiveData<List<FriendEntity>>()
    val friends: LiveData<List<FriendEntity>> get() = _friends

    private val _friendRequests = MutableLiveData<List<FriendRequestEntity>>()
    val friendRequests: LiveData<List<FriendRequestEntity>> get() = _friendRequests

    private fun initializeDatabase(context: Context) {
        val db = GoalSaverDatabase.getDatabase(context)
        friendDao = db.friendDao()
        friendRequestDao = db.friendRequestDao()
    }

    fun loadFriends(context: Context) {
        initializeDatabase(context)
        val userId = MainActivity.logged_user!!.user_id // Assuming user_id is available in MainActivity
        _friends.value = friendDao.getFriends(userId)
    }

    fun loadFriendRequests(context: Context) {
        initializeDatabase(context)
        val userId = MainActivity.logged_user!!.user_id // Assuming user_id is available in MainActivity
        _friendRequests.value = friendRequestDao.getPendingRequests(userId)
    }

    fun sendFriendRequest(context: Context, email: String) {
        initializeDatabase(context)
        val senderId = MainActivity.logged_user!!.user_id // Assuming user_id is available in MainActivity
        val receiver = GoalSaverDatabase.getDatabase(context).userDao().getUserByEmail(email)
        if (receiver != null) {
            val request = FriendRequestEntity(sender_id = senderId, receiver_id = receiver.user_id, status = "pending")
            friendRequestDao.sendFriendRequest(request)
            loadFriendRequests(context)
        }
    }

    fun acceptFriendRequest(context: Context, request: FriendRequestEntity) {
        initializeDatabase(context)
        friendRequestDao.updateRequestStatus(request.request_id, "accepted")
        val senderEmail = GoalSaverDatabase.getDatabase(context).userDao().getUserById(request.sender_id).email
        val receiverEmail = GoalSaverDatabase.getDatabase(context).userDao().getUserById(request.receiver_id).email
        val friend1 = FriendEntity(user_id = request.sender_id, friend_id = request.receiver_id, name = receiverEmail)
        val friend2 = FriendEntity(user_id = request.receiver_id, friend_id = request.sender_id, name = senderEmail)
        friendDao.addFriend(friend1)
        friendDao.addFriend(friend2)
        loadFriends(context)
        loadFriendRequests(context)
    }

    fun declineFriendRequest(context: Context, request: FriendRequestEntity) {
        initializeDatabase(context)
        friendRequestDao.updateRequestStatus(request.request_id, "declined")
        loadFriendRequests(context)
    }

    fun removeFriend(context: Context, friend: FriendEntity) {
        initializeDatabase(context)
        friendDao.removeFriend(friend.user_id, friend.friend_id)
        loadFriends(context)
    }
}
