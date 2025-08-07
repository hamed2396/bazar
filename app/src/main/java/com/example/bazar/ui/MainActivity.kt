package com.example.bazar.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bazar.di.myModules
import com.example.bazar.model.repository.TokenInMemory
import com.example.bazar.model.repository.user.UserRepository
import com.example.bazar.ui.features.IntroScreen
import com.example.bazar.ui.features.cart.CartScreen
import com.example.bazar.ui.features.cateory.CategoryScreen
import com.example.bazar.ui.features.main.MainScreen
import com.example.bazar.ui.features.product.ProductScreen
import com.example.bazar.ui.features.profile.ProfileScreen
import com.example.bazar.ui.features.signin.SignInScreen
import com.example.bazar.ui.features.signup.SignUpScreen
import com.example.bazar.ui.theme.MainAppTheme
import com.example.bazar.util.Constants.CATEGORY_NAME
import com.example.bazar.util.Constants.PRODUCT_ID
import com.example.bazar.util.MyScreens
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.navigation.KoinNavHost
import dev.burnoo.cokoin.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Koin(appDeclaration = {
                modules(myModules)
                androidContext(this@MainActivity)
            }) {
                MainAppTheme {
                    val repository: UserRepository = get()
                    repository.loadToken()
                    DuniBazaarUi()
                }

            }


        }
    }
}

@Composable
fun DuniBazaarUi() {
    val navController = rememberNavController()
    KoinNavHost(
        navController = navController,
        startDestination = MyScreens.MainScreen.route

    ) {
        composable(MyScreens.MainScreen.route) {
            if (TokenInMemory.token != null && TokenInMemory.token != "") {

                MainScreen()
            } else {
                IntroScreen()
            }
        }

        composable(
            route = MyScreens.ProductScreen.route + "/" + "{$PRODUCT_ID}",
            arguments = listOf(navArgument(PRODUCT_ID) { type = NavType.StringType })
        ) {
            ProductScreen(it.arguments!!.getString(PRODUCT_ID, "null"))
        }

        composable(
            MyScreens.CategoryScreen.route + "/" + "{$CATEGORY_NAME}",
            arguments = listOf(navArgument(CATEGORY_NAME) { type = NavType.StringType })
        ) {
            CategoryScreen(it.arguments!!.getString(CATEGORY_NAME, "null"))
        }

        composable(MyScreens.ProfileScreen.route) {
            ProfileScreen()
        }

        composable(MyScreens.CartScreen.route) {
            CartScreen()
        }

        composable(MyScreens.SignUpScreen.route) {
            SignUpScreen()
        }

        composable(MyScreens.SignInScreen.route) {
            SignInScreen()
        }




    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainAppTheme {

    }
}