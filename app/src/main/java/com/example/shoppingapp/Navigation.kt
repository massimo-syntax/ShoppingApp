package com.example.shoppingapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.data.model.UiCart
import com.example.shoppingapp.screen.AuthScreen
import com.example.shoppingapp.screen.CategoryScreen
import com.example.shoppingapp.screen.CheckoutPage
import com.example.shoppingapp.screen.LoginScreen
import com.example.shoppingapp.screen.MainScreen
import com.example.shoppingapp.screen.MessagesScreen
import com.example.shoppingapp.screen.NewProductUpload
import com.example.shoppingapp.screen.Pages
import com.example.shoppingapp.screen.ProductScreenJsonApi
import com.example.shoppingapp.screen.ProductScreenUploaded
import com.example.shoppingapp.screen.RatingsScreen
import com.example.shoppingapp.screen.SignupScreen
import com.example.shoppingapp.screen.UploadSinglePictureScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun Navigation(){

    // redirect to main screen if user is already/still logged in

    val login:Boolean = Firebase.auth.currentUser != null
    val startDestination = if(login) Routes.main+"/"+Pages.ONE.str else Routes.auth

    val navController = rememberNavController()
    Routes.navController = navController

    NavHost(navController = navController , startDestination = startDestination ) {

        composable(Routes.auth) {
            AuthScreen(navController)
        }
        composable(Routes.login) {
            LoginScreen(navController)
        }
        composable(Routes.signup) {
            SignupScreen(navController)
        }
        composable(Routes.main+"/{page}") {  navArgs ->
            val arg = navArgs.arguments?.getString("page")
            val page = when(arg){
                "Profile" -> Pages.FOUR
                else -> {Pages.ONE}
            }
            MainScreen(page)
        }
        composable(Routes.uploadSinglePicture) {
            UploadSinglePictureScreen()
        }
        composable(Routes.productJsonApi + "/{id}") { navBackStack ->
            // Extracting the argument
            val id = navBackStack.arguments?.getString("id") ?: "0"
            ProductScreenJsonApi(id)
        }
        composable(Routes.newProduct) {
            NewProductUpload()
        }
        composable(Routes.productUploaded + "/{id}") { navBackStack ->
            // Extracting the argument
            val id = navBackStack.arguments?.getString("id") ?: "0"
            ProductScreenUploaded(id)
        }
        composable(Routes.category + "/{${Routes.category}}") { navArgs ->
            // get category name from argument named CATEGORY
            val argCategory = navArgs.arguments?.getString(Routes.category)
            // find right category name
            val category: Category = Category.entries.first { it.enam == argCategory }
            CategoryScreen(category)
        }
        composable(Routes.chat + "/{id}" ){ navArgs ->
            val id = navArgs.arguments?.getString("id") ?: "id_not_fonund: navigation"
            MessagesScreen(idReceiver = id)
        }
        composable(Routes.checkout + "/{total}"){ navArgs ->
            val total = navArgs.arguments?.getString("total")!!.toFloat()
            CheckoutPage(total)
        }
        composable(Routes.ratings){
            RatingsScreen()
        }


    }

}

object Routes{
    lateinit var navController: NavController
    val auth = "AUTH"
    val login = "LOGIN"
    val signup = "SIGNUP"
    val main = "MAIN"
    val productJsonApi = "PRODUCT_REMOTE"
    val productUploaded = "PRODUCT_DB"
    val newProduct = "NEW_PRODUCT"
    val uploadSinglePicture = "UPLOAD_SINGLE_PICTURE"

    val category = "CATEGORY"
    val chat = "CHAT"
    val checkout = "CHECKOUT"
    var checkoutPayload : Set<UiCart> = emptySet()

    val ratings = "RATINGS"
}