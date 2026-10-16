package com.example.patitasid.data.repository

import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.repository.HealthRepository

// Demo implementation: lets screens run without the server (RNF-22).
class HealthLocalRepository : HealthRepository {
    override suspend fun checkHealth(): SealedResult<Unit> = SealedResult.Success(Unit)
}
