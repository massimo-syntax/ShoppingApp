package com.example.shoppingapp.screen

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

enum class Pages{ ONE, TWO, THREE, FOUR }

@Composable
fun MainScreen(modifier: Modifier = Modifier){

    var selectedPage by remember { mutableStateOf(Pages.ONE) }

    fun colorNavbarItem(page:Pages):Color{
        return if (selectedPage == page) Color.Black
        else Color.LightGray
    }

    Scaffold(
        topBar = { when(selectedPage) {
                        Pages.ONE -> TopBarHome()
                        Pages.TWO -> null
                        Pages.THREE -> TopBarHouse()
                        Pages.FOUR -> null
                    }
                 },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(top = 20.dp)
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
                    label = {Text("page1")}
                )
            }

        }
    ) { innerPadding ->
        MainScreenContent(modifier.padding(innerPadding) , selectedPage)
    }


}

@Composable
fun MainScreenContent(modifier: Modifier = Modifier, page: Pages){

    when(page){
        Pages.ONE -> Text("hello there")
        Pages.TWO -> Text("hello there 2")
        Pages.THREE -> RemoteProductsScreen()
        Pages.FOUR -> Text("hello there last")
    }

}

