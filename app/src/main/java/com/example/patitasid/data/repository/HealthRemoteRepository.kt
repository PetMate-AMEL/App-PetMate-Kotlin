package com.example.patitasid.data.repository

import com.example.patitasid.common.SealedResult
import com.example.patitasid.data.remote.HealthApiService
import com.example.patitasid.data.remote.safeApiCall
import com.example.patitasid.domain.repository.HealthRepository

class HealthRemoteRepository(private val api: HealthApiService) : HealthRepository {
    override suspend fun checkHealth(): SealedResult<Unit> = safeApiCall { api.getHealth() }
}
