package com.example.bazar.ui.features.product

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bazar.R
import com.example.bazar.androidColors
import com.example.bazar.model.data.Ads
import com.example.bazar.model.data.Comment
import com.example.bazar.model.data.Product
import com.example.bazar.ui.features.signin.GenericTextField
import com.example.bazar.ui.theme.MainAppTheme
import com.example.bazar.ui.theme.blue
import com.example.bazar.ui.theme.cardViewBackGround
import com.example.bazar.ui.theme.priceItemBackGround
import com.example.bazar.ui.theme.shapes
import com.example.bazar.util.Constants
import com.example.bazar.util.MyScreens
import com.example.bazar.util.NetworkChecker
import com.example.bazar.util.stylePrice
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.sin

@Composable
fun ProductScreen(productId: String) {
    val context = LocalContext.current
    val viewModel = getViewModel<ProductViewModel>()
    val navigation = getNavController()
    viewModel.loadData(productId, NetworkChecker(context).isInternetConnected)
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 58.dp)
        ) {
            ProductToolBar(
                productName = "Details",
                badgeNumber = viewModel.badgeNumber,
                onCartClicked = {
                    if (NetworkChecker(context).isInternetConnected) navigation.navigate(MyScreens.CartScreen.route)
                    else Toast.makeText(context, "connect to intenet", Toast.LENGTH_SHORT).show()
                },
                onBackClicked = { navigation.popBackStack() })
            val comment= if (NetworkChecker(context).isInternetConnected) viewModel.comments else listOf()
            ProductItem(data = viewModel.product, onAddNewComment = {
                viewModel.addNewComment(productId, it) {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }, comments = comment) {
                navigation.navigate(MyScreens.CategoryScreen.route + "/" + it)
            }
        }
        AddToCart(viewModel.product.price, viewModel.isAddingProduct) {
            if (NetworkChecker(context).isInternetConnected) {
                viewModel.addToCart(productId) {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Connect to Internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    data: Product,
    comments: List<Comment>,
    onAddNewComment: (String) -> Unit,
    onCategoryClicked: (String) -> Unit
) {
    Column(modifier = modifier.padding(end = 16.dp, start = 16.dp)) {
        ProductDesign(data) {
            onCategoryClicked(it)
        }
        HorizontalDivider(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 14.dp),

            )
        ProductDetail(data, comments.size.toString())
        ProductComments(comments) {
            onAddNewComment(it)
        }
    }
}

@Composable
fun ProductComments(comments: List<Comment>, onAddCommentClicked: (String) -> Unit) {

    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    if (comments.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Comments",
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            )
            TextButton(onClick = {
                if (NetworkChecker(context).isInternetConnected) {
                    showDialog = true
                } else {
                    Toast.makeText(context, "connect to internet", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(
                    text = "add new Comment",
                    color = blue,
                    fontSize = 14.sp
                )
            }

        }
        comments.forEach {
            CommentsBody(it)
        }
    } else {
        TextButton(onClick = {
            if (NetworkChecker(context).isInternetConnected) {
                showDialog = true
            } else {
                Toast.makeText(context, "connect to internet", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(
                text = "add new Comment",
                color = blue,
                fontSize = 14.sp
            )
        }
    }
    if (showDialog) {
        AddNewCommentDialog(
            onDismiss = { showDialog = false },
            onPositiveClicked = {
                onAddCommentClicked(it)
            }
        )
    }
}

@Composable
fun AddNewCommentDialog(onDismiss: () -> Unit, onPositiveClicked: (String) -> Unit) {
    val context = LocalContext.current
    var userComment by remember { mutableStateOf("") }
    Dialog(onDismissRequest = onDismiss) {
        Card(Modifier.fillMaxHeight(.22f), shape = shapes.medium) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "write your comment",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                GenericTextField(
                    edtValue = userComment,
                    valueChanged = { userComment = it },
                    hint = "write something",
                    icon = R.drawable.baseline_add_24
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("cancel", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        if (userComment.isNotEmpty() or userComment.isNotBlank()) {
                            onPositiveClicked(userComment)
                            onDismiss()
                        } else {
                            Toast.makeText(context, "write something", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }) {
                        Text("ok")
                    }
                }


            }
        }
    }

}

@Composable
fun CommentsBody(comment: Comment) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = shapes.large
    ) {

        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = comment.userEmail,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = comment.text,
                fontSize = 14.sp,

                )
        }
    }
}

@Composable
fun ProductDetail(product: Product, commentNumber: String) {
    val context=LocalContext.current
    val commentCount=if (NetworkChecker(context).isInternetConnected) "$commentNumber Comments" else "No Internet"
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.ic_details_comment),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
                Text(
                    text = commentCount,
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 13.sp
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_details_material),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
                Text(
                    text = product.material,
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 13.sp
                )
            }
            Row(

                modifier = Modifier.padding(top = 8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_details_sold),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
                Text(
                    text = product.soldItem + " sold",
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 13.sp
                )
                Row(
                    horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(color = blue, shape = shapes.large) {
                        Text(
                            modifier = Modifier.padding(vertical = 3.dp, horizontal = 8.dp),
                            text = product.tags,
                            color = Color.White,
                            fontSize = 13.sp,


                            )
                    }

                }
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),

                )
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
        modifier = Modifier.padding(4.dp),
        textAlign = TextAlign.Justify
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
    TopAppBar(
        modifier = Modifier.fillMaxWidth(), navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(contentDescription = null, imageVector = Icons.AutoMirrored.Filled.ArrowBack)
            }
        }, title = {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                textAlign = TextAlign.Center,
                text = productName
            )
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        actions = {
            IconButton(onClick = onCartClicked) {
                if (badgeNumber == 0) {

                    Icon(contentDescription = null, imageVector = Icons.Default.ShoppingCart)
                } else {
                    BadgedBox(modifier=modifier.offset(x = (-8).dp),
                        badge = {
                            Badge(
                                modifier = Modifier
                                    .offset(x = (-6).dp).scale(.8f)

                            ) {
                                Text(text = badgeNumber.toString())
                            }
                        }
                    ) {
                        IconButton(onClick = { onCartClicked() }) {
                            Icon(Icons.Default.ShoppingCart, null)
                        }
                    }


                }
            }

        }

    )

}

@Composable
fun AddToCart(price: String, isAddingToCart: Boolean, onCartClicked: () -> Unit) {

    Surface(
        color = androidColors.White, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.09f)
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onCartClicked,
                modifier = Modifier
                    .padding(16.dp)
                    .size(200.dp, 70.dp), shape = shapes.medium
            ) {
                if (isAddingToCart) {
                    DotsTyping()

                } else {
                    Text(
                        "add product to cart",
                        color = androidColors.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(2.dp)
                    )
                }

            }
            Surface(
                shape = shapes.medium,
                color = priceItemBackGround,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    "${stylePrice(price)} tomans",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                )
            }

        }

    }
}

@Composable
fun DotsTyping() {

    val dotSize = 10.dp
    val delayUnit = 350
    val maxOffset = 10f

    @Composable
    fun Dot(
        offset: Float
    ) = Spacer(
        Modifier
            .size(dotSize)
            .offset(y = -offset.dp)
            .background(
                color = Color.White,
                shape = CircleShape
            )
            .padding(start = 8.dp, end = 8.dp)
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateOffsetWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                0f at delay with LinearEasing
                maxOffset at delay + delayUnit with LinearEasing
                0f at delay + delayUnit * 2
            }
        )
    )

    val offset1 by animateOffsetWithDelay(0)
    val offset2 by animateOffsetWithDelay(delayUnit)
    val offset3 by animateOffsetWithDelay(delayUnit * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = maxOffset.dp)
    ) {
        val spaceSize = 2.dp

        Dot(offset1)
        Spacer(Modifier.width(spaceSize))
        Dot(offset2)
        Spacer(Modifier.width(spaceSize))
        Dot(offset3)
    }
}

@Preview
@Composable
fun MainScreenPrev() {
    MainAppTheme {
        ProductScreen("")
    }
}
