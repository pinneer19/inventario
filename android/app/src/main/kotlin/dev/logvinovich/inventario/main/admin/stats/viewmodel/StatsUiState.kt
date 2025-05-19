package dev.logvinovich.inventario.main.admin.stats.viewmodel

import dev.logvinovich.inventario.domain.model.Stats

data class StatsUiState(
    val stats: Stats = Stats(emptyList(), emptyList(), emptyList(), -1f, -1),
    val loading: Boolean = false
)