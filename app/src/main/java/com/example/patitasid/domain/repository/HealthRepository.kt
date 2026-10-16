package com.example.patitasid.domain.repository

import com.example.patitasid.common.SealedResult

interface HealthRepository {
    suspend fun checkHealth(): SealedResult<Unit>
}
