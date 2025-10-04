package com.example.shoppingapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.screen.AuthScreen
import com.example.shoppingapp.screen.LoginScreen
import com.example.shoppingapp.screen.SignupScreen

@Composable
fun Navigation(modifier: Modifier = Modifier){

    val navController = rememberNavController()

    NavHost(navController = navController , startDestination = Routes.auth) {

        composable(Routes.auth) {
            AuthScreen(modifier , navController)
        }

        composable(Routes.login) {
            LoginScreen(modifier)
        }

        composable(Routes.signup) {
            SignupScreen(modifier)
        }


        //RemoteProductsScreen(modifier)

    }

}

object Routes{
    val auth = "auth"
    val login = "login"
    val signup = "signup"
}