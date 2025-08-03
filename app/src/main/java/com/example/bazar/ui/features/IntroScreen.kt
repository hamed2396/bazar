package com.example.bazar.ui.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.bazar.R
import com.example.bazar.ui.theme.MainAppTheme
import com.example.bazar.ui.theme.blue
import com.example.bazar.ui.theme.colorBackGroundMain
import com.example.bazar.util.MyScreens
import dev.burnoo.cokoin.navigation.getNavController

@Composable
fun IntroScreen() {
   val navigation = getNavController()

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(R.drawable.img_intro),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.78f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Button(modifier = Modifier.fillMaxWidth(.7f),
            onClick = {
                navigation.navigate(MyScreens.SignUpScreen.route)
            }) {
            Text(text = "Sign Up", color = Color.White)
        }
        Button(
            modifier = Modifier.fillMaxWidth(.7f),
            onClick = {
                navigation.navigate(MyScreens.SignInScreen.route)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),

            ) {
            Text(text = "Sign In", color = blue)
        }

    }

}

@Preview(showSystemUi = true)
@Composable
private fun IntroPrev() {
    MainAppTheme {
        Surface(color = colorBackGroundMain) {
            IntroScreen()

        }
    }

}