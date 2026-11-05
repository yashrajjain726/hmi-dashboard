package com.example.hmi.data

data class VehicleReading(
    val speedKmh: Double?,
    val motorOn: Boolean?,
    val killSwitchActive: Boolean?,
    val scenario: String,
)
