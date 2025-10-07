package com.example.shoppingapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.screen.AuthScreen
import com.example.shoppingapp.screen.LoginScreen
import com.example.shoppingapp.screen.MainScreen
import com.example.shoppingapp.screen.SignupScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun Navigation(modifier: Modifier = Modifier){

    // rediirect to main screen if user is already/still logged in

    val login:Boolean = Firebase.auth.currentUser != null
    val startDestination = if(login) Routes.main else Routes.auth

    val navController = rememberNavController()

    NavHost(navController = navController , startDestination = startDestination) {
        composable(Routes.auth) {
            AuthScreen(modifier , navController)
        }
        composable(Routes.login) {
            LoginScreen(modifier, navController)
        }
        composable(Routes.signup) {
            SignupScreen(modifier , navController)
        }
        composable(Routes.main) {
            MainScreen(modifier)
        }
        //RemoteProductsScreen(modifier)
    }

}

object Routes{
    val auth = "AUTH"
    val login = "LOGIN"
    val signup = "SIGNUP"
    val main = "MAIN"
}