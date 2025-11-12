package com.example.shoppingapp.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.R
import com.example.shoppingapp.components.SimpleStandardTopBar
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.screen.mainScreenPages.Cart
import com.example.shoppingapp.screen.mainScreenPages.Home
import com.example.shoppingapp.screen.mainScreenPages.ProfilePage
import com.example.shoppingapp.screen.mainScreenPages.RemoteProducts
import com.example.shoppingapp.viewmodel.ProfileViewModel


enum class Pages(val str: String) {
    ONE("Products"),
    TWO("Featured"),
    CART("Cart"),
    FOUR("Profile")
}

@OptIn(ExperimentalMaterial3Api::class)
private val MyRippleConfiguration =
    RippleConfiguration(color = AppStyle.colors.lightBlue, rippleAlpha = RippleAlpha(0f,0f,0f,0f,))

@Composable
fun MainScreen(page:Pages = Pages.ONE , profileViewModel: ProfileViewModel = viewModel()){

    var selectedPage by rememberSaveable { mutableStateOf(page) }
    var cart by rememberSaveable { mutableIntStateOf(0) }

    val context = LocalContext.current
    val profile by profileViewModel.profile

    LaunchedEffect(Unit) {
        // count of cart for cart badge
        cart = SelectedProductsRepository(context).getCart().size
        // to check notifications of ratings, or give directly to profile page once..
        profileViewModel.getProfile()
    }

    // as callback for pages that inteactively update the cart badge
    fun updateBadge(n:Int){
        cart = n
    }

    fun colorNavbarItem(page:Pages):Color{
        return if (selectedPage == page) AppStyle.colors.lightBlue
        else AppStyle.colors.middleBlue
    }

    // page name, icon, on the navbar
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
                Pages.ONE -> SimpleStandardTopBar(Pages.ONE.str , false , profile?.ratings?.size ?: 0)
                Pages.TWO -> SimpleStandardTopBar(Pages.TWO.str , false , profile?.ratings?.size ?: 0)
                Pages.CART -> SimpleStandardTopBar(Pages.CART.str , false ,profile?.ratings?.size ?: 0)
                Pages.FOUR -> SimpleStandardTopBar(Pages.FOUR.str , true , profile?.ratings?.size ?: 0 )
            }
        },
        bottomBar = {
                NavigationBar(
                    containerColor = AppStyle.colors.darkBlue,
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
                                    BadgedBox(
                                        badge = {
                                            if(it.first == Pages.CART && cart > 0 )
                                                Badge(
                                                    containerColor = Color.White,
                                                    contentColor = AppStyle.colors.darkBlue
                                                ) {
                                                    Text(cart.toString())
                                                }
                                        }
                                    ){
                                        Icon(
                                            painter = painterResource(it.second),
                                            contentDescription = it.first.str,
                                            modifier = Modifier.requiredSize(26.dp),
                                            tint = {
                                                colorNavbarItem(it.first)
                                            }
                                        )
                                    }


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
            updateBadge = { updateBadge(it) },
            profileViewModel = profileViewModel
        )
    }


}

@Composable
fun MainScreenContent(modifier: Modifier = Modifier, page: Pages, updateBadge:(n:Int)->Unit , profileViewModel: ProfileViewModel ){

    when(page){
        Pages.ONE -> Home(modifier)
        Pages.TWO -> RemoteProducts(modifier, updateBadge)
        Pages.CART -> Cart(modifier, updateBadge)
        Pages.FOUR -> ProfilePage(modifier, profileViewModel = profileViewModel)
    }

}

