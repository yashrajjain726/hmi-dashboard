package com.example.hmi.dashboard

enum class DataHealth { WAITING, LIVE, STALE, ERROR }

data class DashboardUiState(
    val speedKmh: Int? = null,
    val motorOn: Boolean? = null,
    val killSwitchActive: Boolean? = null,
    val scenario: String = "Waiting for vehicle data",
    val health: DataHealth = DataHealth.WAITING,
    val speedInvalid: Boolean = false,
)
