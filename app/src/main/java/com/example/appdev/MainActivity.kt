package com.example.appdev

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.appdev.database.GoalSaverDatabase
import com.example.appdev.database.entities.UserEntity
import com.example.appdev.databinding.ActivityMainBinding
import com.example.appdev.ui.map.MapDialogFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var database: GoalSaverDatabase
        var logged_user : UserEntity? = null
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logged_user = intent.getParcelableExtra("USER", UserEntity::class.java)

        if (logged_user != null) {
            Toast.makeText(this, "Welcome ${logged_user!!.email}", Toast.LENGTH_SHORT).show()
        }
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

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar_menu, menu)
        val item = menu?.findItem(R.id.action_map)
        item?.icon?.setTint(getColor(R.color.white))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_map -> {
                showMapDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showMapDialog() {
        val mapDialogFragment = MapDialogFragment()
        mapDialogFragment.show(supportFragmentManager, "MapDialogFragment")
    }
}
