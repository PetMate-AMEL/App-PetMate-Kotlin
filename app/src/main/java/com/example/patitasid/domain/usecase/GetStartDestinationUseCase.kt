package com.example.patitasid.domain.usecase

import com.example.patitasid.domain.model.StartDestination
import com.example.patitasid.domain.repository.SessionRepository

// Decides the first screen: welcome without a session, pets or business panel with one (RF-01, RF-57).
class GetStartDestinationUseCase(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke(): StartDestination {
        val session = sessionRepository.current() ?: return StartDestination.Welcome
        return if (session.hasBusiness) StartDestination.BusinessPanel else StartDestination.Pets
    }
}
