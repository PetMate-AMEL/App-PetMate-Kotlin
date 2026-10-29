package com.example.patitasid.data.local

import com.example.patitasid.domain.model.Session

// Kept as an interface so the repository can be unit tested without Android (RNF-22).
interface SessionStorage {
    fun read(): Session?

    fun write(session: Session)

    fun erase()
}
