package com.example.bazar.ui.features.signin

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bazar.R
import com.example.bazar.ui.theme.blue
import com.example.bazar.ui.theme.shapes
import com.example.bazar.util.Constants.VALUE_SUCCESS
import com.example.bazar.util.MyScreens
import com.example.bazar.util.NetworkChecker
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.viewmodel.getViewModel

@Composable
fun SignInScreen() {
    val navigation = getNavController()
    val viewModel = getViewModel<SignInViewModel>()
    val context=LocalContext.current
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.4f)
                .background(blue)
        ) {

        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconApp()

            MainCard(viewModel = viewModel, navigation = navigation) {
                viewModel.signInUser {
                    if (it == VALUE_SUCCESS) {
                        navigation.navigate(MyScreens.MainScreen.route) {
                            popUpTo(MyScreens.IntroScreen.route) {
                                inclusive = true
                            }
                        }

                    } else {
                        showToast(context = context, it)
                    }
                }
            }

        }
    }
}

@Composable
fun IconApp(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.size(64.dp), shape = CircleShape) {
        Image(
            modifier = modifier.padding(14.dp),
            painter = painterResource(R.drawable.ic_icon_app),
            contentDescription = null
        )
    }
}


@Composable
fun GenericTextField(
    edtValue: String,
    valueChanged: (String) -> Unit,
    @DrawableRes icon: Int,
    hint: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {

    var passwordVisible by remember { mutableStateOf(false) }

    val visualTransformation = when {
        !isPassword -> VisualTransformation.None
        passwordVisible -> VisualTransformation.None
        else -> PasswordVisualTransformation()
    }

    val keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text


    val trailingIcon: @Composable (() -> Unit)? = if (!isPassword) {
        null
    } else {
        {
            val iconRes = if (passwordVisible) R.drawable.ic_visible
            else R.drawable.ic_invisible

            Icon(
                painter = painterResource(iconRes),
                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                modifier = Modifier.clickable { passwordVisible = !passwordVisible })
        }
    }

    OutlinedTextField(
        value = edtValue,
        onValueChange = valueChanged,
        modifier = modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 8.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(icon), contentDescription = "$hint icon"
            )
        },
        placeholder = { Text(hint) },
        label = { Text(hint) },
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon = trailingIcon,
        shape = shapes.medium
    )
}


@Composable
fun MainCard(
    modifier: Modifier = Modifier,
    navigation: NavController,
    viewModel: SignInViewModel,
    signInEvent: () -> Unit,
) {
    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = shapes.medium

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()
        ) {
            Text(
                modifier = modifier.padding(bottom = 18.dp, top = 18.dp),
                text = "Sign Up",
                style = TextStyle(color = blue, fontWeight = Bold, fontSize = 28.sp)
            )

            GenericTextField(
                edtValue = email, icon = R.drawable.ic_email, hint = "email", valueChanged = {
                    viewModel.email.value = it
                })
            GenericTextField(
                edtValue = password,
                icon = R.drawable.ic_password,
                hint = "password",
                valueChanged = {
                    viewModel.password.value = it
                },
                isPassword = true
            )

            Button(onClick = {


                when {
                    !areFieldsFilled(email, password) -> showToast(
                        context,
                        "Please fill all fields"
                    )

                    !isEmailValid(email) -> showToast(context, "Wrong email format")

                    else -> {
                        if (NetworkChecker(context).isInternetConnected)
                            signInEvent() else showToast(context, "Please Connect to internet")
                    }
                }
            }, modifier = modifier.padding(top = 28.dp, bottom = 8.dp)) {
                Text("Log In", modifier = modifier.padding(8.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't Have an Account?")
                TextButton(onClick = {
                    navigation.navigate(MyScreens.SignUpScreen.route) {
                        popUpTo(MyScreens.SignInScreen.route) { inclusive = true }
                    }
                }) {
                    Text("Register here", color = blue)
                }


            }
        }
    }
}

private fun areFieldsFilled(vararg fields: String): Boolean {
    return fields.all { it.isNotEmpty() }
}

private fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}



private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Preview(showSystemUi = true)
@Composable
private fun SignUpScreenPrev() {
    SignInScreen()


}