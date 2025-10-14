package com.example.shoppingapp.screen

import android.graphics.drawable.Icon
import android.graphics.pdf.PdfDocument
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
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
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.components.SimpleStandardTopBar
import com.example.shoppingapp.screen.mainScreenPages.Home
import com.example.shoppingapp.screen.mainScreenPages.ProfilePage

enum class Pages(val str: String) {
    ONE("Products"),
    TWO("Featured"),
    THREE("Cart"),
    FOUR("Profile")
}

@Composable
fun MainScreen(){

    var selectedPage by remember { mutableStateOf(Pages.ONE) }

    fun colorNavbarItem(page:Pages):Color{
        return if (selectedPage == page) Color.White
        else Color.LightGray
    }

    val navbarItems: List<Pair<Pages, Int>> = listOf(
        Pair(
            Pages.ONE,
            R.drawable.ic_launcher_foreground
        ),
        Pair(
            Pages.TWO,
            R.drawable.ic_launcher_foreground
        ),        Pair(
            Pages.THREE,
            R.drawable.ic_launcher_foreground
        ),        Pair(
            Pages.FOUR,
            R.drawable.ic_launcher_foreground
        ),

    )

    Scaffold(
        topBar = {
            when(selectedPage){
                Pages.ONE -> SimpleStandardTopBar(Pages.ONE.str , true)
                Pages.TWO -> SimpleStandardTopBar(Pages.TWO.str , true)
                Pages.THREE -> SimpleStandardTopBar(Pages.THREE.str , true)
                Pages.FOUR -> SimpleStandardTopBar(Pages.FOUR.str , true)
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = AppStyle.colors.darkBlule,
                contentColor = Color.White,
                //modifier = Modifier.border(2.dp,Color.Red)
            ){
                navbarItems.forEach {
                    NavigationBarItem(
                        selected = selectedPage == it.first,
                        onClick = { selectedPage = it.first},
                        icon = {
                            Icon(
                                painter = painterResource(it.second),
                                contentDescription = it.first.str,
                                modifier = Modifier.height(32.dp),
                                tint = {
                                    colorNavbarItem(it.first)
                                }

                            )
                        },
                        label = {
                            Text(
                                text = it.first.str,
                                color = colorNavbarItem(it.first)
                                )
                        }
                    )
                }


            }



        }
    ) { innerPadding ->
        MainScreenContent(
            modifier= Modifier.padding(innerPadding),
            page = selectedPage,
        )
    }


}

@Composable
fun MainScreenContent(modifier: Modifier = Modifier, page: Pages){

    when(page){
        Pages.ONE -> Home(modifier)
        Pages.TWO -> Text("hello there 2")
        Pages.THREE -> RemoteProductsScreen(modifier)
        Pages.FOUR -> ProfilePage(modifier)
    }

}

