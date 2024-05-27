package com.example.appdev

import android.app.DownloadManager.Query
import android.os.AsyncTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.Async
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

class datatabaseConnector (
    private val server:String,
    private val database:String,
    private val user:String,
    private val password:String
) {
    private val jdbcUrl = "jdbc:sqlserver://$server;databaseName=$database;encrypt=true;trustServerCertificate=false;loginTimeout=30;"
    init {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    }
    suspend fun query(query: String): ResultSet? {
        return withContext(Dispatchers.IO) {
            try {
                val connection: Connection = DriverManager.getConnection(jdbcUrl, user, password)
                val statement = connection.createStatement()
                statement.executeQuery(query)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }



}
