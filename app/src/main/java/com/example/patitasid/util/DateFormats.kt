package com.example.patitasid.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val displayDate = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.forLanguageTag("es-MX"))

// RNF-14: every date shown to the user uses dd/mm/aaaa.
fun LocalDate.toDisplayDate(): String = format(displayDate)
