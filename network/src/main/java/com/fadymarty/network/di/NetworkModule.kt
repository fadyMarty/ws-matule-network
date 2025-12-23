package com.fadymarty.network.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.fadymarty.network.data.manager.AuthManagerImpl
import com.fadymarty.network.data.remote.MatuleApi
import com.fadymarty.network.data.remote.MatuleInterceptor
import com.fadymarty.network.data.repository.MatuleRepositoryImpl
import com.fadymarty.network.domain.manager.AuthManager
import com.fadymarty.network.domain.repository.MatuleRepository
import com.fadymarty.network.domain.use_case.cart.AddProductToCartUseCase
import com.fadymarty.network.domain.use_case.cart.DeleteCartUseCase
import com.fadymarty.network.domain.use_case.cart.GetCartsUseCase
import com.fadymarty.network.domain.use_case.cart.ObserveCartsUseCase
import com.fadymarty.network.domain.use_case.cart.UpdateCartUseCase
import com.fadymarty.network.domain.use_case.order.CreateOrderUseCase
import com.fadymarty.network.domain.use_case.project.GetProjectsUseCase
import com.fadymarty.network.domain.use_case.project.ObserveProjectsUseCase
import com.fadymarty.network.domain.use_case.shop.GetNewsUseCase
import com.fadymarty.network.domain.use_case.shop.GetProductByIdUseCase
import com.fadymarty.network.domain.use_case.shop.GetProductsUseCase
import com.fadymarty.network.domain.use_case.shop.SearchProductsUseCase
import com.fadymarty.network.domain.use_case.user.ClearSessionUseCase
import com.fadymarty.network.domain.use_case.user.GetTokenUseCase
import com.fadymarty.network.domain.use_case.user.GetUserByIdUseCase
import com.fadymarty.network.domain.use_case.user.GetUserIdUseCase
import com.fadymarty.network.domain.use_case.user.LoginUseCase
import com.fadymarty.network.domain.use_case.user.RegisterUseCase
import com.fadymarty.network.domain.use_case.user.UpdateUserUseCase
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val Context.dataStore by preferencesDataStore("settings")

/**
 * DI Модуль сетевого слоя
 */
val networkModule = module {

    singleOf(::AuthManagerImpl) { bind<AuthManager>() }
    singleOf(::MatuleRepositoryImpl) { bind<MatuleRepository>() }

    singleOf(::MatuleInterceptor)

    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(get<MatuleInterceptor>())
            .build()
    }

    single {
        val json = Json { ignoreUnknownKeys = true }
        Retrofit.Builder()
            .baseUrl(MatuleApi.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(get())
            .build()
            .create(MatuleApi::class.java)
    }

    factoryOf(::ObserveCartsUseCase)
    factoryOf(::ObserveProjectsUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::UpdateUserUseCase)
    factoryOf(::GetNewsUseCase)
    factoryOf(::GetProductsUseCase)
    factoryOf(::SearchProductsUseCase)
    factoryOf(::GetProductByIdUseCase)
    factoryOf(::AddProductToCartUseCase)
    factoryOf(::UpdateCartUseCase)
    factoryOf(::CreateOrderUseCase)
    factoryOf(::GetProjectsUseCase)
    factoryOf(::GetUserByIdUseCase)
    factoryOf(::GetCartsUseCase)
    factoryOf(::GetCartsUseCase)
    factoryOf(::DeleteCartUseCase)
    factoryOf(::GetTokenUseCase)
    factoryOf(::GetUserIdUseCase)
    factoryOf(::ClearSessionUseCase)
}