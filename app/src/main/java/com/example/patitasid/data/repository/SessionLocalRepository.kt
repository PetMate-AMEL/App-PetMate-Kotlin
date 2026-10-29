package com.example.patitasid.data.repository

import com.example.patitasid.data.local.SessionStorage
import com.example.patitasid.domain.model.Session
import com.example.patitasid.domain.repository.SessionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// The session lives only on the device; reads and writes move off the main thread (RNF-13).
class SessionLocalRepository(
    private val storage: SessionStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SessionRepository {
    override suspend fun current(): Session? = withContext(dispatcher) { storage.read() }

    override suspend fun save(session: Session) = withContext(dispatcher) { storage.write(session) }

    override suspend fun clear() = withContext(dispatcher) { storage.erase() }
}
