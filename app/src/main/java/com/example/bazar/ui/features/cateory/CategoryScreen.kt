package com.example.bazar.ui.features.cateory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bazar.model.data.Product
import com.example.bazar.ui.theme.blue
import com.example.bazar.ui.theme.shapes
import com.example.bazar.util.MyScreens
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

@Composable
fun CategoryScreen(categoryName: String) {
    val viewModel = getViewModel<CategoryViewModel>()
    val navigation = getNavController()
    viewModel.loadCategoryData(categoryName)
    Column(modifier = Modifier.fillMaxSize()) {

        CategoryToolBar(categoryName)
        val data = viewModel.products
        CategoryList(data) {
            navigation.navigate(MyScreens.ProductScreen.route + "/" + it)
        }
    }
}

@Composable
fun CategoryList(product: List<Product>, onProductClicked: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(product.size) { items ->
            CategoryItem(data = product[items]) {
                onProductClicked(it)
            }
        }
    }
}

@Composable
fun CategoryItem(modifier: Modifier = Modifier, data: Product, onProductClicked: (String) -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .clickable { onProductClicked(data.productId) },
        shape = shapes.large,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        AsyncImage(
            model = data.imgUrl,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            Column(modifier = modifier.padding(10.dp)) {
                Text(
                    text = data.name,
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp)
                )
                Text(
                    text = data.price + " tomans",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                    modifier = modifier.padding(4.dp)
                )
            }
            Surface(
                modifier = modifier
                    .padding(end = 8.dp, bottom = 8.dp)
                    .align(Alignment.Bottom)
                    .clip(shape = shapes.large),
                color = blue
            ) {
                Text(
                    text = data.soldItem + " sold",
                    modifier = modifier.padding(4.dp),
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryToolBar(categoryName: String) {
    TopAppBar(title = {
        Text(
            text = categoryName,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White))
}