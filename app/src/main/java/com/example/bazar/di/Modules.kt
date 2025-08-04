package com.example.bazar.di


import android.content.Context
import androidx.room.Room
import com.example.bazar.model.db.AppDataBase
import com.example.bazar.model.net.ApiService
import com.example.bazar.model.net.createApiService
import com.example.bazar.model.repository.product.ProductRepository
import com.example.bazar.model.repository.product.ProductRepositoryImpl
import com.example.bazar.model.repository.user.UserRepository
import com.example.bazar.model.repository.user.UserRepositoryImpl
import com.example.bazar.ui.features.cateory.CategoryViewModel
import com.example.bazar.ui.features.main.MainViewModel
import com.example.bazar.ui.features.product.ProductViewModel
import com.example.bazar.ui.features.signin.SignInViewModel
import com.example.bazar.ui.features.signup.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module


val myModules = module {
    factory<ApiService> {
        createApiService()
    }
    single {
        androidContext().getSharedPreferences("data", Context.MODE_PRIVATE)
    }
    single {
        UserRepositoryImpl(get(), get())
    } bind UserRepository::class

    viewModel { SignUpViewModel(get()) }

    viewModel { SignInViewModel(get()) }

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "appDb")
            .fallbackToDestructiveMigration().build()
    }
    single {
        ProductRepositoryImpl(get(), get<AppDataBase>().productDao())
    } bind ProductRepository::class

    viewModel { (connected: Boolean) -> MainViewModel(get(), get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { ProductViewModel() }

}