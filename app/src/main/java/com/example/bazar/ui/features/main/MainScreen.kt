package com.example.bazar.ui.features.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bazar.R
import com.example.bazar.ui.theme.MainAppTheme
import com.example.bazar.ui.theme.cardViewBackGround
import com.example.bazar.ui.theme.shapes

@Composable
fun MainScreen() {
    val scroller = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroller)
            .padding(bottom = 16.dp)
    ) {

        TopToolBar()
        CategoryBar()
        ProductSubject()
        ProductSubject()
        BigPicture()
        ProductSubject()

        ProductSubject()


    }
}

@Composable
fun ProductSubject() {
    Column(modifier = Modifier.padding(top = 32.dp)) {
        Text(
            text = "Popular Destinations",
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.headlineMedium
        )
        ProductBar()
    }
}

@Composable
fun ProductBar() {
    LazyRow(
        modifier = Modifier.padding(top = 16.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(10) {

            ProductItem()
        }
    }
}

@Composable
fun ProductItem() {
    Card(
        modifier = Modifier
            .clickable {}
            .padding(start = 16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        shape = shapes.medium
    ) {
        Column() {
            Image(
                modifier = Modifier.size(200.dp),
                contentDescription = null,
                contentScale = ContentScale.Crop, painter = painterResource(R.drawable.img_intro)
            )
        }
        Column(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 8.dp),
        ) {
            Text(
                text = "Diamond Woman Watch",
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp),
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = "86000 toman",
                style = TextStyle(fontSize = 13.sp),
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = "156 sold.",
                style = TextStyle(fontSize = 12.sp),
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp)
            )


        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopToolBar() {
    TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White), title = {
        Text(text = "Duni Bazzar")
    }, actions = {
        IconButton(onClick = {

        }) {
            Icon(Icons.Default.ShoppingCart, null)
        }
        IconButton(onClick = {

        }) {
            Icon(Icons.Default.Person, null)
        }
    })
}

@Composable
fun CategoryBar() {
    LazyRow(Modifier.padding(top = 16.dp), contentPadding = PaddingValues(end = 16.dp)) {
        items(10) {
            CategoryItems()
        }
    }
}

@Composable
fun BigPicture() {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp).padding(top = 32.dp, start = 16.dp, end = 16.dp).clip(shapes.medium).clickable{},
        painter = painterResource(R.drawable.img_intro),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun CategoryItems(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clickable {}
            .padding(start = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Surface(shape = shapes.medium, color = cardViewBackGround) {
            Image(
                modifier = modifier.padding(16.dp),
                painter = painterResource(R.drawable.ic_cat_backpack), contentDescription = null
            )
        }
        Text(text = "Hotel", modifier = modifier.padding(top = 4.dp), color = Color.Gray)
    }
}

@Preview
@Composable
fun MainScreenPrev() {
    MainAppTheme {
        MainScreen()
    }
}