package com.example.shoppingapp.screen.mainScreenPages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.shoppingapp.R
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppingapp.AppStyle.AppStyle
import com.example.shoppingapp.Routes
import okhttp3.Route

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    val tabs = listOf("Products", "History", "Likes")
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = modifier.fillMaxSize()) {
        // Profile Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppStyle.colors.darkBlule )
                //.padding(top = 32.dp, bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    // Avatar
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground), // Replace with your drawable
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // Followers / Following
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
                    ) {

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "128",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text("Products", color = Color.LightGray, fontSize = 14.sp)
                        }
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


                Spacer(modifier = Modifier.height(0.dp))

                // Name + Button
                Row (
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                    // Name & Username
                    Text(
                        "Profile Name",
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
        SecondaryTabRow (
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = AppStyle.colors.darkBlule,
            indicator = { TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(selectedTab, matchContentSize = false),
                color = AppStyle.colors.darkBlule
            )},
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
            0 -> VibesTab()
            1 -> MediaTab()
            2 -> LikesTab()
        }
    }
}

@Composable
fun VibesTab() {
    // Example posts
    val posts = listOf(
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus.",
        "Hello, Lorem ipsum dolor sit amet consectetur adipiscing.",
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus ex sapien vitae pellentesque sem.",
        "Lorem ipsum dolor sit amet consectetur adipiscing elit quisque."
    )
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
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
fun MediaTab() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Example media item
        Card(
            modifier = Modifier.size(120.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            // Replace with actual media
            Image(
                painter = painterResource(id = R.drawable.adaptivecarticon_foreground), // Replace with your media drawable
                contentDescription = "Media",
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Media content goes here...")
    }
}

@Composable
fun LikesTab() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painterResource(R.drawable.adaptivecarticon_foreground) , contentDescription = null, tint = Color.Red, modifier = Modifier.size(48.dp))
        Text("Liked posts will appear here.")
    }
}