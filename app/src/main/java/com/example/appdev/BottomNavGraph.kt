package com.example.appdev


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Profile.route) {
            // Route to "home" screen
        }
        composable(route = BottomBarScreen.Home.route) {
            // Route to "profile" screen
        }
        composable(route = BottomBarScreen.Goals.route) {
            // Route to "goals" screen
        }
    }
}