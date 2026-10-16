package com.example.patitasid.di

import com.example.patitasid.BuildConfig
import com.example.patitasid.data.remote.HealthApiService
import com.example.patitasid.data.repository.HealthLocalRepository
import com.example.patitasid.data.repository.HealthRemoteRepository
import com.example.patitasid.domain.repository.HealthRepository
import com.example.patitasid.domain.usecase.CheckServerHealthUseCase
import com.example.patitasid.ui.feature.home.HomeViewModel
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val NETWORK_TIMEOUT_SECONDS = 30L

val coreModule = module {
    single<Retrofit> {
        val client = OkHttpClient.Builder()
            .connectTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}

// The only place that decides local vs remote; screens never know which one they get (RNF-22).
val dataModule = module {
    single<HealthApiService> { get<Retrofit>().create(HealthApiService::class.java) }
    single<HealthRepository> { if (BuildConfig.USE_LOCAL_DATA) HealthLocalRepository() else HealthRemoteRepository(api = get()) }
}

val featureModule = module {
    factory<CheckServerHealthUseCase> { CheckServerHealthUseCase(healthRepository = get()) }
    viewModel<HomeViewModel> { HomeViewModel(checkServerHealth = get()) }
}

val appModules = listOf(coreModule, dataModule, featureModule)
