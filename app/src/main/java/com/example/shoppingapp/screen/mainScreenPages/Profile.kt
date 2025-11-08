package com.example.shoppingapp.screen.mainScreenPages

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.Routes
import com.example.shoppingapp.data.model.Contact
import com.example.shoppingapp.data.model.Product
import com.example.shoppingapp.data.model.UserSelectedProduct
import com.example.shoppingapp.repository.SelectedProductsRepository
import com.example.shoppingapp.viewmodel.MessagesViewModel
import com.example.shoppingapp.viewmodel.ProfileViewModel
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
    profileViewModel: ProfileViewModel = viewModel(),
    messagesViewModel: MessagesViewModel = viewModel(),
) {

    // state for tabs
    val tabs = listOf("Products", "Messages", "Favorites")
    var selectedTab by remember { mutableIntStateOf(0) }

    // profile injected

    // my profile
    var profile by remember { profileViewModel.profile }

    // tabs content
    val favs = userSelectedViewModel.uiStateFavs.collectAsState()
    var myProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    val contacts by remember { messagesViewModel.contacts }

    // repo for favs
    val roomRepo = SelectedProductsRepository(LocalContext.current)

    LaunchedEffect(Unit) {
        // get profile
        // profileViewModel.getProfile()
        // get favs
        userSelectedViewModel.getAllFavs(roomRepo)
        // get my products, for products tab
        val _myProducts = mutableListOf<Product>()
        Firebase.firestore.collection("products")
            .whereEqualTo("userId", Firebase.auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    val p = it.toObject(Product::class.java)
                    _myProducts.add(p!!)
                }
                myProducts = _myProducts.toList()
            }
        // wait for profile
        while (profile == null) delay(100)
        // get contacts
        val conversations = profile!!.chat
        messagesViewModel.getContacts(conversations)
    }

    // change status bar icon color hence background of header is dark, only in this composable
    val view = LocalView.current
    DisposableEffect(true) {
        if (!view.isInEditMode) {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
        onDispose {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    // UI
    Column(modifier = modifier.fillMaxSize()) {
        // Profile Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppStyle.colors.darkBlule),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (profile == null || profile?.image!!.isEmpty())
                // Avatar
                    Image(
                        painter = painterResource(R.drawable.icon_profile), // Replace with your drawable
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                    )
                else
                    AsyncImage(
                        model = profile!!.image,
                        contentDescription = "profile picture",
                        modifier = Modifier
                            .size(120.dp)
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
                        onClick = {
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
                            var products = ""
                            if (myProducts.isNotEmpty())
                                products = myProducts.size.toString()
                            else
                                products = "0"
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
                            var favCount = ""
                            if (favs.value.list.isNotEmpty())
                                favCount = favs.value.list.size.toString()
                            else
                                favCount = "0"

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
                    if (profile != null) profile!!.name else "",
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
            Spacer(Modifier.height(10.dp))
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
            2 -> FavoriteProductsTab(favs.value.list, roomRepo)
        }
    }
}

@Composable
fun ProductsTab(myProducts: List<Product> = emptyList()) {

    LazyColumn {
        items(myProducts) { product ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp)) {
                    AsyncImage(
                        model = product.images.split(",").first(),
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
fun ProductItem(
    product: Product
) {
    val heightSize = 64.dp
    Surface(
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            AsyncImage(
                model = product.images.split(',').first(),
                contentDescription = product.title,
                modifier = Modifier
                    .size(heightSize)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(14.dp))
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(1f),
            ) {
                // username
                Text(
                    product.title,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = AppStyle.colors.middleBlue,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(Modifier.height(6.dp))
                // message text
                Text(
                    product.description,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = AppStyle.colors.darkBlule,
                        fontWeight = FontWeight.Normal
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

            }

            Column(
                Modifier
                    .fillMaxHeight()
                    .height(heightSize),
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        // delete product
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.adaptivecarticon_foreground),
                        contentDescription = "delete form favorites",
                        modifier = Modifier.size(24.dp),
                        tint = AppStyle.colors.green
                    )
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
                items(contacts) {
                    ContactItem(it)
                }
            }
        }
    }
}

@Composable
fun ContactItem(contact: Contact) {

    val height = 64.dp
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable {
                Routes.navController.navigate(Routes.chat + "/" + contact.userId)
            }
    ) {
        // elevation for image
        Surface(
            shadowElevation = 4.dp,
            shape = CircleShape
        ){
            AsyncImage(
                model = contact.image,
                contentDescription = contact.userName,
                modifier = Modifier
                    .size(height)
                    .clip(CircleShape)
                    .background(Color.White),
                contentScale = ContentScale.FillBounds,
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(
            modifier = Modifier.fillMaxHeight().height(height),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // title + firest message
            Column{
                // title
                Text(
                    contact.userName,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = AppStyle.colors.middleBlue,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Spacer(Modifier.height(8.dp))
                // description
                Text(
                    contact.lastMessage,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = AppStyle.colors.darkBlule,
                        fontWeight = FontWeight.Normal
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            // datetime
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // datetime
                val sdf = SimpleDateFormat("HH:mm dd/MM", Locale.getDefault())
                val date = Date(contact.lastMessageDatetime)
                val text = sdf.format(date)
                Text(
                    text,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    }
}

@Composable
fun FavoriteProductsTab(
    favs: List<UserSelectedProduct> = emptyList(),
    roomRepo: SelectedProductsRepository
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
                    FavoritedItem(it, roomRepo)
                }
            }
        }
    }
}

@Composable
fun FavoritedItem(
    favItem: UserSelectedProduct,
    roomRepo: SelectedProductsRepository,
    viewModel: UserSelectedViewModel = viewModel()
) {
    val heightSize = 64.dp
    Surface(
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 10.dp, top = 12.dp,bottom=12.dp, end = 4.dp)
        ) {
            AsyncImage(
                model = favItem.mainPicture,
                contentDescription = favItem.title,
                modifier = Modifier
                    .size(heightSize)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(14.dp))
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(1f),
            ) {
                // title
                Text(
                    favItem.title,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = AppStyle.colors.middleBlue,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Clip
                )
                Spacer(Modifier.height(6.dp))
                // description
                Text(
                    favItem.description,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = AppStyle.colors.darkBlule,
                        fontWeight = FontWeight.Normal
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

            }

            Column(
                Modifier
                    .fillMaxHeight()
                    .height(heightSize),
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        viewModel.deleteFromFav(roomRepo, favItem.id)
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_favorite),
                        contentDescription = "delete form favorites",
                        modifier = Modifier.size(24.dp),
                        tint = AppStyle.colors.red
                    )
                }
            }

        }

    }


}