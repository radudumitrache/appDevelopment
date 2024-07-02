package com.example.appdev

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.entities.UserEntity
import com.example.appdev.databinding.ActivityMainBinding
import com.example.appdev.util.CheckInternetConnection
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_account, R.id.navigation_transactions, R.id.navigation_dashboard, R.id.navigation_exchange, R.id.navigation_goals
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        var newUser : UserEntity = UserEntity(email="ceva", password = "ceva", profession = "ceva", age = "ceva", monthly_salary = 11.2f, preffered_currency = "dollar", user_id = 0)
        GoalSaverDatabase.getDatabase(this).userDao().insert(newUser)
    }
}