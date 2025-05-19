package dev.logvinovich.inventario.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private val timeZone = TimeZone.currentSystemDefault()

fun Long?.toLocalDate(): LocalDate? {
    return this?.let {
        Instant.fromEpochMilliseconds(it).toLocalDateTime(timeZone).date
    }
}