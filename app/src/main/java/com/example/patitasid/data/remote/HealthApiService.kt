package com.example.patitasid.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface HealthApiService {
    @GET("api/salud")
    suspend fun getHealth(): Response<Unit>
}
