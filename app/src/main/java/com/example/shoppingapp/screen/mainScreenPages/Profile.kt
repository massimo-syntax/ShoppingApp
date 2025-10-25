package com.example.shoppingapp.screen.mainScreenPages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
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
import com.example.shoppingapp.data.model.UserSelectedProduct
import com.example.shoppingapp.repository.RemoteProductsRepository
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.viewmodel.ProductsViewModel
import com.example.shoppingapp.viewmodel.ProfileVIewModel
import com.example.shoppingapp.viewmodel.UserSelectedViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

@Composable
fun ProfilePage( modifier: Modifier = Modifier , userSelectedViewModel: UserSelectedViewModel = viewModel(), profileVIewModel: ProfileVIewModel = viewModel()) {

    val tabs = listOf("Products", "History", "Likes")
    var selectedTab by remember { mutableIntStateOf(0) }


    var profile by remember { profileVIewModel.profile }


    var favs = userSelectedViewModel.uiState.collectAsState()

    var myProducts by remember {mutableStateOf<List<UserSelectedProduct>>(emptyList())}

    val roomRepo = SelectedProductsRepository(LocalContext.current)

    LaunchedEffect(Unit) {
        // get profile
        profileVIewModel.getProfile()
        // get favs
        userSelectedViewModel.getAllFavs(roomRepo)
        // get my products
        val _myProducts = mutableListOf<UserSelectedProduct>()
        Firebase.firestore.collection("products").whereEqualTo("userId", Firebase.auth.currentUser!!.uid )
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    val product = UserSelectedProduct(
                        id = it["id"].toString(),
                        title = it["title"].toString(),
                        description = it["description"].toString(),
                        mainPicture = it["images"].toString().split(',').first() ,
                        price = it["price"].toString(),
                    )
                    _myProducts.add(product)
                }
                myProducts = _myProducts.toList()
            }
        //



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
                                .background(Color.White),
                        )
                    else AsyncImage(
                        model = profile!!.image,
                        contentDescription = "profile picture",
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.FillBounds,
                        )


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
                            Routes.navController.navigate(Routes.newProduct)
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
            0 -> ProductsTab(myProducts)
            1 -> HistoryTab()
            2 -> FavoriteProductsTab(favs.value.list)
        }
    }
}

@Composable
fun ProductsTab(myProducts: List<UserSelectedProduct> = emptyList()) {

    LazyColumn {
        items(myProducts){ product ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp)) {
                    AsyncImage(
                        model = product.mainPicture,
                        contentDescription = product.title,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(product.title, fontWeight = FontWeight.Bold)
                        Text(product.description)
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
fun FavoriteProductsTab(favs: List<UserSelectedProduct> = emptyList() ) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (favs.isEmpty()) {
            Icon(
                painterResource(R.drawable.icon_favorite),
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