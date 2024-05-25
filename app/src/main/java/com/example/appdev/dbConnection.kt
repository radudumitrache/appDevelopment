package com.example.appdev

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.concurrent.thread

class DatabaseConnectionTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Run database connection test in a separate thread
        thread {
            testDatabaseConnection()
        }
    }

    private fun testDatabaseConnection() {
        val connectionString = "jdbc:sqlserver://appdevelopmentnhl.database.windows.net:1433;" +
                "database=appdevelopmentnhl;" +
                "user=superadmin@appdevelopmentnhl;" +
                "password=ApXCOzKMIm1yVeJ94opdPvPl;" +
                "encrypt=true;" +
                "trustServerCertificate=false;" +
                "hostNameInCertificate=*.database.windows.net;" +
                "loginTimeout=30;"

        var connection: Connection? = null

        try {
            // Establish the connection
            connection = DriverManager.getConnection(connectionString)

            // Log success message
            if (connection != null) {
                Log.d("DatabaseConnectionTest", "Connection to Azure SQL Database established successfully!")
            } else {
                Log.d("DatabaseConnectionTest", "Failed to establish connection!")
            }
        } catch (e: SQLException) {
            Log.e("DatabaseConnectionTest", "SQLException: ${e.message}")
        } finally {
            // Close the connection
            try {
                connection?.close()
            } catch (e: SQLException) {
                Log.e("DatabaseConnectionTest", "SQLException while closing: ${e.message}")
            }
        }
    }
}