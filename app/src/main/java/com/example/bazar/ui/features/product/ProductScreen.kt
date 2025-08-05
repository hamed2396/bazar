package com.example.bazar.ui.features.product

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
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
import org.koin.core.parameter.parametersOf

@Composable
fun ProductScreen(productId: String) {
    val context = LocalContext.current
    val viewModel = getViewModel<ProductViewModel>()
    val navigation = getNavController()
    viewModel.loadData(productId)
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 58.dp)
        ) {
            ProductToolBar(
                productName = "Details",
                badgeNumber = 4,
                onCartClicked = {
                    if (NetworkChecker(context).isInternetConnected)
                        navigation.navigate(MyScreens.CartScreen.route)
                    else Toast.makeText(context, "connect to intenet", Toast.LENGTH_SHORT).show()
                },
                onBackClicked = { navigation.popBackStack() })
            ProductItem(data = viewModel.product) {
                navigation.navigate(MyScreens.CategoryScreen.route + "/" + it)
            }
        }
        }

}

@Composable
fun ProductItem(modifier: Modifier = Modifier, data: Product, onCategoryClicked: (String) -> Unit) {
    Column(modifier = modifier.padding( end = 16.dp, start = 16.dp)) {
        ProductDesign(data) {
            onCategoryClicked(it)
        }
    }
}

@Composable
fun ProductDesign(data: Product, onCategoryClicked: (String) -> Unit) {

    AsyncImage(
        model = data.imgUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(shapes.medium)
    )
    Text(
        text = data.name,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(14.dp)
    )
    Text(
        text = data.detailText,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(4.dp), textAlign = TextAlign.Justify
    )
    TextButton(onClick = { onCategoryClicked(data.category) }) {
        Text(
            text = "#" + data.category,
            fontSize = 13.sp,

            )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductToolBar(
    modifier: Modifier = Modifier,
    productName: String,
    badgeNumber: Int,
    onBackClicked: () -> Unit,
    onCartClicked: () -> Unit
) {
    TopAppBar( modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(contentDescription = null, imageVector = Icons.AutoMirrored.Filled.ArrowBack)
            }
        },
        title = {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                textAlign = TextAlign.Center,
                text = productName
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        actions = {
            IconButton(onClick = onCartClicked) {
                if (badgeNumber == 0) {

                    Icon(contentDescription = null, imageVector = Icons.Default.ShoppingCart)
                } else {
                    BadgedBox(badge = { Text(text = badgeNumber.toString()) }) {
                        Icon(contentDescription = null, imageVector = Icons.Default.ShoppingCart)
                    }
                }
            }

        }

    )

}

@Composable
fun AddToCart() {

}


@Preview
@Composable
fun MainScreenPrev() {
    MainAppTheme {
        ProductScreen("")
    }
}