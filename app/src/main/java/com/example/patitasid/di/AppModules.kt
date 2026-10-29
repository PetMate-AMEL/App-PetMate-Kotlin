package com.example.patitasid.di

import com.example.patitasid.BuildConfig
import com.example.patitasid.data.local.EncryptedSessionStorage
import com.example.patitasid.data.local.SessionStorage
import com.example.patitasid.data.remote.AccountApiService
import com.example.patitasid.data.remote.HealthApiService
import com.example.patitasid.data.remote.PasswordApiService
import com.example.patitasid.data.repository.AccountLocalRepository
import com.example.patitasid.data.repository.AccountRemoteRepository
import com.example.patitasid.data.repository.HealthLocalRepository
import com.example.patitasid.data.repository.HealthRemoteRepository
import com.example.patitasid.data.repository.PasswordLocalRepository
import com.example.patitasid.data.repository.PasswordRemoteRepository
import com.example.patitasid.data.repository.SessionLocalRepository
import com.example.patitasid.domain.repository.AccountRepository
import com.example.patitasid.domain.repository.HealthRepository
import com.example.patitasid.domain.repository.PasswordRepository
import com.example.patitasid.domain.repository.SessionRepository
import com.example.patitasid.domain.usecase.CheckServerHealthUseCase
import com.example.patitasid.domain.usecase.GetStartDestinationUseCase
import com.example.patitasid.domain.usecase.RequestPasswordResetUseCase
import com.example.patitasid.domain.usecase.ResetPasswordUseCase
import com.example.patitasid.domain.usecase.SignUpUseCase
import com.example.patitasid.domain.usecase.ValidateResetLinkUseCase
import com.example.patitasid.ui.feature.home.HomeViewModel
import com.example.patitasid.ui.feature.password.ForgotPasswordViewModel
import com.example.patitasid.ui.feature.password.ResetPasswordViewModel
import com.example.patitasid.ui.feature.signup.SignUpViewModel
import com.example.patitasid.ui.feature.startup.StartupViewModel
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
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
    single<PasswordApiService> { get<Retrofit>().create(PasswordApiService::class.java) }
    single<PasswordRepository> { if (BuildConfig.USE_LOCAL_DATA) PasswordLocalRepository() else PasswordRemoteRepository(api = get()) }
    single<AccountApiService> { get<Retrofit>().create(AccountApiService::class.java) }
    single<AccountRepository> { if (BuildConfig.USE_LOCAL_DATA) AccountLocalRepository() else AccountRemoteRepository(api = get()) }
    // The session always lives on the device, so there is no local/remote switch here (RNF-06).
    single<SessionStorage> { EncryptedSessionStorage(context = androidContext()) }
    single<SessionRepository> { SessionLocalRepository(storage = get()) }
}

val featureModule = module {
    factory<CheckServerHealthUseCase> { CheckServerHealthUseCase(healthRepository = get()) }
    viewModel<HomeViewModel> { HomeViewModel(checkServerHealth = get()) }
    factory<RequestPasswordResetUseCase> { RequestPasswordResetUseCase(passwordRepository = get()) }
    factory<ValidateResetLinkUseCase> { ValidateResetLinkUseCase(passwordRepository = get()) }
    factory<ResetPasswordUseCase> { ResetPasswordUseCase(passwordRepository = get()) }
    viewModel<ForgotPasswordViewModel> { ForgotPasswordViewModel(requestPasswordReset = get()) }
    viewModel<ResetPasswordViewModel> { params ->
        ResetPasswordViewModel(token = params.get(), validateResetLink = get(), resetPassword = get())
    }
    factory<GetStartDestinationUseCase> { GetStartDestinationUseCase(sessionRepository = get()) }
    viewModel<StartupViewModel> { StartupViewModel(getStartDestination = get()) }
    factory<SignUpUseCase> { SignUpUseCase(accountRepository = get(), sessionRepository = get()) }
    viewModel<SignUpViewModel> { SignUpViewModel(signUp = get()) }
}

val appModules = listOf(coreModule, dataModule, featureModule)
