package com.example.patitasid.domain.usecase

import com.example.patitasid.domain.model.Session
import com.example.patitasid.domain.repository.SessionRepository

class FakeSessionRepository(var session: Session? = null) : SessionRepository {
    var clearCount = 0

    override suspend fun current(): Session? = session

    override suspend fun save(session: Session) {
        this.session = session
    }

    override suspend fun clear() {
        session = null
        clearCount++
    }
}
