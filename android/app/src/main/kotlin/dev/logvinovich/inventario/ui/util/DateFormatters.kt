package dev.logvinovich.inventario.ui.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.char

val dateFormatter =
    LocalDate.Format { dayOfMonth(); char('/'); monthNumber(); char('/'); yearTwoDigits(1960) }

val dateFormatterWithDots =
    LocalDate.Format { dayOfMonth(); char('.'); monthNumber(); char('.'); yearTwoDigits(1960) }


val timeFormatter = LocalTime.Format { hour(); char(':'); minute(); char(':'); second() }