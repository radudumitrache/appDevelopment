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
            Friend("Friend 3")
        )

    }

    class Friend(val name: String)

}
