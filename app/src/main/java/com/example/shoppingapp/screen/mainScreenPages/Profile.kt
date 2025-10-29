package com.example.shoppingapp.screen.mainScreenPages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AppBarRow
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
import com.example.shoppingapp.data.local.Fav
import com.example.shoppingapp.data.model.Contact
import com.example.shoppingapp.data.model.Product
import com.example.shoppingapp.data.model.UserSelectedProduct
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.viewmodel.MessagesViewModel
import com.example.shoppingapp.viewmodel.ProfileVIewModel
import com.example.shoppingapp.viewmodel.UserSelectedViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    userSelectedViewModel: UserSelectedViewModel = viewModel(),
    profileViewModel: ProfileVIewModel = viewModel(),
    messagesViewModel: MessagesViewModel = viewModel()
) {

    val tabs = listOf("Products", "Messages", "Favorites")
    var selectedTab by remember { mutableIntStateOf(0) }

    var profile by remember { profileViewModel.profile }

    val favs = userSelectedViewModel.uiStateFavs.collectAsState()

    var myProducts by remember {mutableStateOf<List<UserSelectedProduct>>(emptyList())}

    val contacts by remember { messagesViewModel.contacts }

    val roomRepo = SelectedProductsRepository(LocalContext.current)

    LaunchedEffect(Unit) {
        // get profile
        profileViewModel.getProfile()
        // get favs
        userSelectedViewModel.getAllFavs(roomRepo)
        // get my products, for products tab
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

        // wait for profile
        while(profile == null) delay(100)
        // get contacts
        val conversations = profile!!.chat
        messagesViewModel.getContacts(conversations)

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

                    // products - favs
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Upload new image button
                        IconButton(
                            onClick ={
                                Routes.navController.navigate(Routes.uploadSinglePicture)
                            }
                        ) {
                            Icon(
                                painterResource(R.drawable.icon_image),
                                contentDescription = "icon edit image",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        // stats : products , favs
                        Row {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                var products = "0"
                                if(myProducts.isNotEmpty()) products = myProducts.size.toString()
                                Text(
                                    products,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text("Products", color = Color.LightGray, fontSize = 14.sp)
                            }
                            Spacer(Modifier.width(8.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                var favCount = "0"
                                if( favs.value.list.isNotEmpty() ) favCount = favs.value.list.size.toString()
                                Text(
                                    favCount,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text("Favorited", color = Color.LightGray, fontSize = 14.sp)
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
            1 -> Chat(contacts)
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
fun Chat(
    contacts: List<Contact> = emptyList(),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (contacts.isEmpty()) {
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

            LazyColumn {
                items(contacts){
                    ContactItem(it)
                    HorizontalDivider(Modifier.height(2.dp), color = AppStyle.colors.middleBlue)
                }
            }
        }
    }
}

@Composable
fun ContactItem(contact:Contact){

    Column (
        modifier = Modifier
            .padding(6.dp)
            .clickable{
                Routes.navController.navigate(Routes.chat+"/"+contact.userId+"/"+contact.userName)
            }
    ) {
        Row {
            AsyncImage(
                model = contact.image,
                contentDescription = contact.userName,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(contact.userName, fontWeight = FontWeight.Bold)
                Text(contact.lastMessage)
            }
        }
        Row {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = Date(contact.lastMessageDatetime)
            val text = sdf.format(date)
            Text(text, color = AppStyle.colors.lightBlue)
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
            Text("No favorites yet.")
        } else {
            LazyColumn {
                items(favs) {
                    FavoritedItem(it)
                }
            }
        }
    }
}
@Composable
fun FavoritedItem(favItem: UserSelectedProduct){

    Row{
        AsyncImage(
            model = favItem.mainPicture,
            contentDescription = favItem.title,
            modifier = Modifier.size(64.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White)
        )
        Text(favItem.title, modifier = Modifier.weight(1f))

        Column (
            Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {

                }
            ){
                Icon(
                    painter = painterResource(R.drawable.icon_favorite),
                    contentDescription = "delete form favorites",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Red
                )
            }
        }

    }


}