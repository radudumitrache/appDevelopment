package com.example.appdev.ui.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModel : ViewModel() {

    private val _friends = MutableLiveData<List<Friend>>()
    val friends: LiveData<List<Friend>> get() = _friends

    init {
        // Initialize with dummy data
        _friends.value = listOf(
            Friend("Friend 1"),
            Friend("Friend 2"),
            Friend("Friend 3"),
            Friend("Friend 4"),
            Friend("Friend 5"),
            Friend("Friend 6"),
            Friend("Friend 7"),
            Friend("Friend 777"),
            Friend("Friend 8"),
            Friend("Friend 9"),
            Friend("Friend 10"),
            Friend("Friend 11"),
            Friend("Friend 12"),
        )

    }

    class Friend(val name: String)

    fun removeFriend(friend: Friend) {
        _friends.value = _friends.value?.filter { it != friend }
    }

}
