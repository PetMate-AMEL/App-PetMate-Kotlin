package com.example.patitasid.data.repository

import com.example.patitasid.data.local.SessionStorage
import com.example.patitasid.domain.model.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class SessionLocalRepositoryTest {
    private class InMemoryStorage(var session: Session? = null) : SessionStorage {
        override fun read(): Session? = session

        override fun write(session: Session) {
            this.session = session
        }

        override fun erase() {
            session = null
        }
    }

    private val storage = InMemoryStorage()

    // Dispatchers.Unconfined keeps the test off the Android IO dispatcher.
    private val repository = SessionLocalRepository(storage, Dispatchers.Unconfined)

    @Test
    fun `without a saved session it returns null`() = runTest {
        assertNull(repository.current())
    }

    @Test
    fun `a saved session survives to be read back`() = runTest {
        repository.save(Session("acceso", "renovacion", hasBusiness = true))

        assertEquals(Session("acceso", "renovacion", hasBusiness = true), repository.current())
    }

    @Test
    fun `clearing removes the session`() = runTest {
        repository.save(Session("acceso", "renovacion", hasBusiness = false))

        repository.clear()

        assertNull(repository.current())
    }
}
