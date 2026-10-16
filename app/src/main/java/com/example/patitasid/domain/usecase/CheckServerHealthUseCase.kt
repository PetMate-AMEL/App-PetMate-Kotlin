package com.example.patitasid.domain.usecase

import com.example.patitasid.common.SealedResult
import com.example.patitasid.domain.repository.HealthRepository

class CheckServerHealthUseCase(private val healthRepository: HealthRepository) {
    suspend operator fun invoke(): SealedResult<Unit> = healthRepository.checkHealth()
}
