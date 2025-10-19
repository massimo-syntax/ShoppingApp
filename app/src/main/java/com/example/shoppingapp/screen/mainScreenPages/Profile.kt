package com.example.shoppingapp.screen.mainScreenPages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.shoppingapp.R
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.Routes
import com.example.shoppingapp.data.model.UiProductWithFieldsFromRoom
import com.example.shoppingapp.data.remote.RemoteProduct
import com.example.shoppingapp.repository.RemoteProductsRepository
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.viewmodel.ProfileVIewModel

@Composable
fun ProfilePage( modifier: Modifier = Modifier , profileVIewModel: ProfileVIewModel = viewModel() ) {

    val tabs = listOf("Products", "History", "Likes")
    var selectedTab by remember { mutableIntStateOf(0) }


    var profile by remember { profileVIewModel.profile }

    LaunchedEffect(Unit) {
        profileVIewModel.getProfile()
    }



    Column(modifier = modifier.fillMaxSize()) {
        // Profile Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppStyle.colors.darkBlule)
            //.padding(top = 32.dp, bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if(profile == null || profile?.image!!.isEmpty())
                        // Avatar
                        Image(
                            painter = painterResource(R.drawable.icon_profile), // Replace with your drawable
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    else AsyncImage(profile!!.image,"profile picture")

                    Spacer(modifier = Modifier.height(4.dp))

                    // Followers / Following
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        IconButton(
                            onClick ={
                                Routes.navController.navigate(Routes.UploadSinglePicture)
                            }
                        ) {
                            Icon(
                                painterResource(R.drawable.icon_image),
                                contentDescription = "icon edit image",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Row {

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "128",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text("Products", color = Color.LightGray, fontSize = 14.sp)
                            }
                            Spacer(Modifier.width(8.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "562",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text("Favourited", color = Color.LightGray, fontSize = 14.sp)
                            }
                        }

                    }

                }


                Spacer(modifier = Modifier.height(0.dp))

                // Name + Button
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Name & Username
                    Text(
                        if(profile!=null)profile!!.name else "",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                    )
                    OutlinedButton(
                        onClick = {
                            Routes.navController.navigate(Routes.UploadSinglePicture)
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = AppStyle.colors.darkBlule
                        ),
                        shape = RoundedCornerShape(40)
                    ) {
                        Text("Add Product", fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }

        // Tabs
        SecondaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = AppStyle.colors.darkBlule,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(selectedTab, matchContentSize = false),
                    color = AppStyle.colors.darkBlule
                )
            },
            divider = { HorizontalDivider(color = AppStyle.colors.darkBlule) },
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(tab) },
                    /*icon = {
                        when (index) {
                            0 -> Icon(painterResource(R.drawable.adaptivecarticon_foreground), contentDescription = null)
                            1 -> Icon(painterResource(R.drawable.ic_launcher_foreground), contentDescription = null)
                            2 -> Icon(painterResource(R.drawable.ic_launcher_background), contentDescription = null)
                        }
                    } */
                )
            }
        }
        // Tab Content
        when (selectedTab) {
            0 -> ProductsTab()
            1 -> HistoryTab()
            2 -> FavoriteProductsTab()
        }
    }
}

@Composable
fun ProductsTab() {
    // Example posts
    val posts = listOf(
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus.",
        "Hello, Lorem ipsum dolor sit amet consectetur adipiscing.",
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus ex sapien vitae pellentesque sem.",
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque."
    )
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        posts.forEach { post ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.adaptivecarticon_foreground), // Replace with your drawable
                        contentDescription = null,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Profile Name", fontWeight = FontWeight.Bold)
                        Text(post)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryTab(
    items: List<String> = emptyList()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (items.isEmpty()) {
            Card(
                modifier = Modifier.size(120.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_sell), // Replace with your media drawable
                    contentDescription = "icon sell",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("you didnt buy or sell anything yet")
        } else {
            // @TODO show list of items
        }
    }
}

@Composable
fun FavoriteProductsTab() {

    val roomRepo = SelectedProductsRepository(LocalContext.current)
    val remoteRepo = RemoteProductsRepository()

    var favs by remember { mutableStateOf<List<UiProductWithFieldsFromRoom>>(emptyList()) }

    LaunchedEffect(Unit) {
        val favRemoteProducts = roomRepo.getFavs()
        val remoteFavourites = mutableListOf<UiProductWithFieldsFromRoom>()

        favRemoteProducts.forEach {
            val remoteProduct = remoteRepo.getProduct(it.productId)
            // just to have a type to display for now
            // later there will be also products in firebase
            val p = UiProductWithFieldsFromRoom(
                remoteProduct.id,
                remoteProduct.title,
                remoteProduct.description,
                remoteProduct.image, // now is just 1, remote product has just 1
                remoteProduct.price,
                // cart or fav nothing here
            )
            remoteFavourites.add(p)
        }
        favs = remoteFavourites.toList()
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (favs.isEmpty()) {
            Icon(
                painterResource(R.drawable.favorite),
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.size(48.dp)
            )
            Text("No avorites yet.")
        } else {
            LazyColumn {
                items(favs) {
                    Text(it.title)
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}