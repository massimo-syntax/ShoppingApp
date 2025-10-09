package com.example.shoppingapp.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.shoppingapp.R
import com.example.shoppingapp.components.TopBarHome
import com.example.shoppingapp.components.TopBarHouse
import com.example.shoppingapp.screen.mainScreenPages.Home
import java.lang.reflect.TypeVariable

enum class Pages{ ONE, TWO, THREE, FOUR }

@Composable
fun MainScreen(){

    var selectedPage by remember { mutableStateOf(Pages.ONE) }
    val variable  = rememberTextFieldState()

    fun colorNavbarItem(page:Pages):Color{
        return if (selectedPage == page) Color.Black
        else Color.LightGray
    }

    Scaffold(
        topBar = {
            when(selectedPage){
                Pages.ONE -> TopBarHouse(variable)
                Pages.TWO -> null
                Pages.THREE -> null
                Pages.FOUR -> null
            }
        },

        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.border(2.dp,Color.Red)
            ){
                NavigationBarItem(
                    selected = false,
                    onClick = { selectedPage = Pages.ONE},
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = "cart",
                            modifier = Modifier.height(36.dp),
                            tint = colorNavbarItem(Pages.ONE)
                        )
                    },
                    label = {Text("page1")}
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {selectedPage = Pages.TWO},
                    modifier = Modifier.height(80.dp),
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = "cart",
                            modifier = Modifier.height(36.dp),
                            tint = colorNavbarItem(Pages.TWO)
                        )
                    },
                    label = {Text("page1")}
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {selectedPage = Pages.THREE},
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = "cart",
                            modifier = Modifier.height(36.dp),
                            tint = colorNavbarItem(Pages.THREE)
                        )
                    },
                    label = {
                        Text(
                            text ="page1",
                            color = colorNavbarItem(Pages.THREE)
                        )
                    }

                )
                NavigationBarItem(
                    selected = false,
                    onClick = {selectedPage = Pages.FOUR},
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = "cart",
                            modifier = Modifier.height(36.dp),
                            tint = colorNavbarItem(Pages.FOUR)
                        )
                    },
                    label = {Text(text = variable.text.toString())}
                )
            }

        }
    ) { innerPadding ->
        MainScreenContent(
            modifier= Modifier.padding(innerPadding),
            selectedPage,
            variable
            )
    }


}

@Composable
fun MainScreenContent(modifier: Modifier = Modifier, page: Pages, variable: TextFieldState){

    when(page){
        Pages.ONE -> Home(modifier, variable)
        Pages.TWO -> Text("hello there 2")
        Pages.THREE -> RemoteProductsScreen()
        Pages.FOUR -> Text("hello there last")
    }

}

