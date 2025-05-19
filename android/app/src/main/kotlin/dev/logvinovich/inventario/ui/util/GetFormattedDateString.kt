package dev.logvinovich.inventario.ui.util

import dev.logvinovich.inventario.util.toLocalDate
import kotlinx.datetime.format

fun Pair<Long?, Long?>.toFormattedString(): String {
    val start = first.toLocalDate()?.format(dateFormatter)
    val end = second.toLocalDate()?.format(dateFormatter)

    return when {
        start != null && end != null -> "$start - $end"
        start != null -> "From $start"
        end != null -> "Until $end"
        else -> ""
    }
}