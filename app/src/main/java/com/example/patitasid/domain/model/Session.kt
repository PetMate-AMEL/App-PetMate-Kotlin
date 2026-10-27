package com.example.patitasid.domain.model

// Saved session (RF-57): the app keeps it between launches and only clears it on logout (HU-010).
data class Session(val accessToken: String, val refreshToken: String, val hasBusiness: Boolean)
