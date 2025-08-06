package com.example.bazar.ui.features.profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.bazar.R
import com.example.bazar.model.data.Product
import com.example.bazar.ui.features.signin.GenericTextField
import com.example.bazar.ui.theme.blue
import com.example.bazar.ui.theme.shapes
import com.example.bazar.util.MyScreens
import com.example.bazar.util.styleTime
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel
import java.nio.file.WatchEvent

@Composable
fun ProfileScreen() {
    val viewModel = getViewModel<ProfileViewModel>()
    val navigation = getNavController()
    val context = LocalContext.current
    viewModel.loadUserData()
    Box() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ProfileToolBar {
                navigation.popBackStack()
            }
            MainAnimation()
            Spacer(modifier = Modifier.padding(top = 6.dp))
            UserDataSection("email address", text = viewModel.email) {}
            UserDataSection("address", text = viewModel.address) {
                viewModel.showLocationDialog = true
            }
            UserDataSection(
                "postal code", text = viewModel.postalCode
            ) { viewModel.showLocationDialog = true }
            UserDataSection("login time", text = styleTime(viewModel.loginTime.toLong())) {}
if (viewModel.showLocationDialog){
    AddUserLocationDataDialog(
        true,
        onDismiss = { viewModel.showLocationDialog = false },
        onSubmitClicked = { address, postalCode, _ ->
            viewModel.setUserLocation(address,postalCode)

        }
    )
}

        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = {
                    Toast.makeText(context, "HOPE TO SEE YOU AGAIN", Toast.LENGTH_SHORT).show()
                    viewModel.signOut()
                    navigation.navigate(MyScreens.MainScreen.route) {
                        popUpTo(MyScreens.MainScreen.route) {
                            inclusive = true
                        }
                        navigation.popBackStack()
                        navigation.popBackStack()
                    }
                }, modifier = Modifier
                    .fillMaxWidth(.8f)
                    .padding(top = 36.dp)
            ) {
                Text("Sign out")
            }
        }

    }


}

@Composable
fun AddUserLocationDataDialog(
    showSaveLocation: Boolean,
    onDismiss: () -> Unit,
    onSubmitClicked: (String, String, Boolean) -> Unit
) {

    val context = LocalContext.current
    val checkedState = remember { mutableStateOf(true) }
    val userAddress = remember { mutableStateOf("") }
    val userPostalCode = remember { mutableStateOf("") }
    val fraction = if (showSaveLocation) 0.695f else 0.625f

    Dialog(onDismissRequest = onDismiss) {

        Card(
            modifier = Modifier.fillMaxHeight(fraction), shape = shapes.medium
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {

                Text(
                    text = "Add Location Data",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))

                GenericTextField(
                    userAddress.value, {
                        userAddress.value = it
                    }, icon = R.drawable.outline_add_location_24, hint = "You're address"
                )

                GenericTextField(userPostalCode.value, {
                    userPostalCode.value = it
                }, icon = R.drawable.outline_add_location_24, hint = "You're postal code ")

                if (showSaveLocation) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, start = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it },
                        )

                        Text(text = "Save To Profile")

                    }

                }


                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onDismiss) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = {

                        if ((userAddress.value.isNotEmpty() || userAddress.value.isNotBlank()) && (userPostalCode.value.isNotEmpty() || userPostalCode.value.isNotBlank())) {
                            onSubmitClicked(
                                userAddress.value, userPostalCode.value, checkedState.value
                            )
                            onDismiss.invoke()
                        } else {
                            Toast.makeText(context, "please write first...", Toast.LENGTH_SHORT)
                                .show()
                        }


                    }) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}

@Composable
fun UserDataSection(
    subject: String, text: String, onLocationClicked: (() -> Unit)?
) {

    Column(
        modifier = Modifier
            .clickable { onLocationClicked?.invoke() }
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.Start) {
        Text(
            text = subject, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = blue
        )
        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,

            )
        Divider(thickness = .5.dp, color = blue, modifier = Modifier.padding(top = 16.dp))
    }


}

@Composable
fun MainAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.profile_anim))
    LottieAnimation(
        modifier = Modifier
            .size(240.dp)
            .padding(top = 36.dp, bottom = 16.dp),
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileToolBar(onBackClicked: () -> Unit) {

    TopAppBar(title = {
        Text(
            "Profile",
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 35.dp),
            textAlign = TextAlign.Center
        )

    }, navigationIcon = {
        IconButton(onBackClicked) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }
    })
}

