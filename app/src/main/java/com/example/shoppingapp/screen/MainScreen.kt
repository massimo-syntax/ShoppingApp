package com.example.shoppingapp.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.captionBarPadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    CART("Cart"),
    FOUR("Profile")
}

@OptIn(ExperimentalMaterial3Api::class)
private val MyRippleConfiguration =
    RippleConfiguration(color = Color.Red, rippleAlpha = RippleAlpha(0f,0f,0f,0f,))

@Composable
fun MainScreen(){

    var selectedPage by rememberSaveable { mutableStateOf(Pages.ONE) }
    var cart by rememberSaveable { mutableStateOf(0) }


    fun colorNavbarItem(page:Pages):Color{
        return if (selectedPage == page) Color.White
        else Color.DarkGray
    }

    val navbarItems: List<Pair<Pages, Int>> = listOf(
        Pair(
            Pages.ONE,
            R.drawable.icon_map
        ),
        Pair(
            Pages.TWO,
            R.drawable.icon_sell
        ),        Pair(
            Pages.CART,
            R.drawable.icon_cart_garden
        ),        Pair(
            Pages.FOUR,
            R.drawable.icon_profile
        ),

    )

    Scaffold(
        topBar = {
            when(selectedPage){
                Pages.ONE -> SimpleStandardTopBar(Pages.ONE.str , false)
                Pages.TWO -> SimpleStandardTopBar(Pages.TWO.str , false)
                Pages.CART -> SimpleStandardTopBar(Pages.CART.str , false)
                Pages.FOUR -> SimpleStandardTopBar(Pages.FOUR.str , true)
            }
        },
        bottomBar = {
                NavigationBar(
                    containerColor = AppStyle.colors.darkBlule,
                    contentColor = Color.White,
                    //modifier = Modifier.border(5.dp, Color.White).clip(RoundedCornerShape(6.dp))
                ) {
                    navbarItems.forEach {
                        CompositionLocalProvider(LocalRippleConfiguration provides null) {
                            NavigationBarItem(
                                selected = false,// or the ripple is still there also with CompositionLocalProvider(LocalRippleConfiguration provides null) or provides the transparent ripple all 0f //  selectedPage == it.first,
                                onClick = { selectedPage = it.first },
                                colors = NavigationBarItemColors(
                                    selectedIconColor = colorNavbarItem(it.first),
                                    selectedTextColor = colorNavbarItem(it.first),
                                    selectedIndicatorColor = Color.Transparent,
                                    unselectedIconColor = colorNavbarItem(it.first),
                                    unselectedTextColor = colorNavbarItem(it.first),
                                    disabledIconColor = colorNavbarItem(it.first),
                                    disabledTextColor = colorNavbarItem(it.first)
                                ),
                                icon = {
                                        Icon(
                                            painter = painterResource(it.second),
                                            contentDescription = it.first.str,
                                            modifier = Modifier.requiredSize(26.dp),
                                            tint = {
                                                 colorNavbarItem(it.first)
                                            }
                                        )

                                },
                                alwaysShowLabel = true,
                                label = {
                                    Text(
                                        text = it.first.str,
                                        color = colorNavbarItem(it.first),
                                    )
                                }
                            )
                        }
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
        Pages.CART -> RemoteProductsScreen(modifier)
        Pages.FOUR -> ProfilePage(modifier)
    }

}

