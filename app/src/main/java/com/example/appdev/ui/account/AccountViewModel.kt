package com.example.appdev.ui.account

import androidx.lifecycle.ViewModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.entities.UserEntity

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = GoalSaverDatabase.getDatabase(application).userDao()
    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity> get() = _user

    fun loadUser(userId: Int) {
        val user = userDao.getUserById(userId)
        _user.value = user
    }

    fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
        _user.value = user
    }
}