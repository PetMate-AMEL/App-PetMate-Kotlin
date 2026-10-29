package com.example.patitasid.domain.repository

import com.example.patitasid.domain.model.Session

interface SessionRepository {
    // null when nobody has signed in yet or the session was cleared.
    suspend fun current(): Session?

    suspend fun save(session: Session)

    suspend fun clear()
}
