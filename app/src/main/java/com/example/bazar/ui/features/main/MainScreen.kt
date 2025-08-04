package com.example.bazar.ui.features.main

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bazar.R
import com.example.bazar.model.data.Ads
import com.example.bazar.model.data.Product
import com.example.bazar.ui.theme.MainAppTheme
import com.example.bazar.ui.theme.blue
import com.example.bazar.ui.theme.cardViewBackGround
import com.example.bazar.ui.theme.shapes
import com.example.bazar.util.Constants
import com.example.bazar.util.MyScreens
import com.example.bazar.util.NetworkChecker
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

@Composable
fun MainScreen() {
    val scroller = rememberScrollState()
    val context = LocalContext.current
    val viewModel =
        getViewModel<MainViewModel>(parameters = { parametersOf(NetworkChecker(context).isInternetConnected) })
    val navigation = getNavController()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroller)
            .padding(bottom = 16.dp)
    ) {

        if (viewModel.showProgressBar) {
            LinearProgressIndicator(color = blue, modifier = Modifier.fillMaxWidth())
        }
        TopToolBar(
            onCartClicked = { navigation.navigate(MyScreens.CartScreen.route) },
            onProfileClicked = {
                navigation.navigate(
                    MyScreens.ProfileScreen.route
                )
            })
        CategoryBar(Constants.CATEGORY){
            navigation.navigate(MyScreens.CategoryScreen.route + "/" + it)
        }

        val productDataState = viewModel.products
        val adsDataState = viewModel.ads
        ProductSubjectList(Constants.TAGS, productDataState, adsDataState){
            navigation.navigate(MyScreens.ProductScreen.route+ "/" + it)
        }

    }
}

@Composable
fun ProductSubjectList(tags: List<String>, products: List<Product>, ads: List<Ads>,onProductClicked:(String)->Unit) {


    if (products.isNotEmpty()) {
        tags.forEachIndexed { index, _ ->
            val tagData = products.filter { product ->
                product.tags == tags[index]
            }
            Log.e("myTag", "$tagData", )
            ProductSubject(tags[index], tagData.shuffled(),onProductClicked)
            if (ads.size >= 2) {

                if (index == 1 || index == 2) BigPicture(ads[index.minus(1)],onProductClicked)
            }
        }
    }



}


@Composable
fun ProductSubject(subject: String, data: List<Product>,onProductClicked:(String)->Unit) {
    Column(modifier = Modifier.padding(top = 32.dp)) {
        Text(
            text = subject,
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.titleLarge
        )
        ProductBar(data,onProductClicked)
    }
}

@Composable
fun ProductBar(data: List<Product>,onProductClicked: (String) -> Unit) {
    LazyRow(
        modifier = Modifier.padding(top = 16.dp), contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(data.size) {

            ProductItem(data[it],onProductClicked)
        }
    }
}

@Composable
fun ProductItem(product: Product,onProductClicked:(String)->Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .clickable { onProductClicked(product.productId) }
            .padding(start = 16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        shape = shapes.medium) {
        Column() {
            AsyncImage(
                modifier = Modifier.size(200.dp),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                model = ImageRequest.Builder(context).data(product.imgUrl).crossfade(500).build()
            )
        }
        Column(
            modifier = Modifier.padding(top = 10.dp, bottom = 8.dp),
        ) {
            Text(
                text = product.name,
                style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp),
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = product.price + "Tomans",
                style = TextStyle(fontSize = 13.sp),
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = product.soldItem + "sold",
                style = TextStyle(fontSize = 12.sp),
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp)
            )


        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopToolBar(onCartClicked: () -> Unit, onProfileClicked: () -> Unit) {
    TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White), title = {
        Text(text = "Duni Bazzar")
    }, actions = {
        IconButton(onClick = { onCartClicked() }) {
            Icon(Icons.Default.ShoppingCart, null)
        }
        IconButton(onClick = { onProfileClicked() }) {
            Icon(Icons.Default.Person, null)
        }
    })
}

@Composable
fun CategoryBar(categoryList: List<Pair<String, Int>>, onCategoryClicked: (String) -> Unit) {
    LazyRow(Modifier.padding(top = 16.dp), contentPadding = PaddingValues(end = 16.dp)) {
        items(categoryList.size) {
            CategoryItems(category = categoryList[it], onCategoryClicked = onCategoryClicked)
        }
    }
}

@Composable
fun BigPicture(ads: Ads,onProductClicked:(String)->Unit) {
    val context = LocalContext.current
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
            .clip(shapes.medium)
            .clickable { onProductClicked(ads.productId) },
        model = ImageRequest.Builder(context).data(ads.imageURL).crossfade(500).build(),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun CategoryItems(
    modifier: Modifier = Modifier,
    category: Pair<String, Int>,
    onCategoryClicked: (String) -> Unit
) {
    Column(
        modifier = modifier
            .clickable { onCategoryClicked(category.first) }
            .padding(start = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Surface(shape = shapes.medium, color = cardViewBackGround) {
            Image(
                modifier = modifier.padding(16.dp),
                painter = painterResource(category.second),
                contentDescription = null
            )
        }
        Text(
            text = category.first,
            modifier = modifier.padding(top = 4.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
fun MainScreenPrev() {
    MainAppTheme {
        MainScreen()
    }
}