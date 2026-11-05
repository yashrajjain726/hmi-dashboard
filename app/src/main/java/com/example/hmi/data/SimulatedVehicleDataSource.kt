package com.example.hmi.data

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

enum class SimulationMode { NORMAL, INVALID_SPEED, MISSING_SIGNALS, PAUSED }

class SimulatedVehicleDataSource : VehicleDataSource {
    private val mode = MutableStateFlow(SimulationMode.NORMAL)

    fun setMode(value: SimulationMode) { mode.value = value }

    override val readings: Flow<VehicleReading> = flow {
        var index = 0
        while (currentCoroutineContext().isActive) {
            when (mode.value) {
                SimulationMode.NORMAL -> {
                    emit(scenarios[index % scenarios.size])
                    index++
                }
                SimulationMode.INVALID_SPEED -> emit(VehicleReading(-12.0, true, false, "Invalid speed"))
                SimulationMode.MISSING_SIGNALS -> emit(VehicleReading(null, null, null, "Missing signals"))
                SimulationMode.PAUSED -> Unit // No emissions, so the freshness timer can expire.
            }
            delay(1_000)
        }
    }

    companion object {
        val scenarios = listOf(
            VehicleReading(0.0, false, false, "Parked"),
            VehicleReading(0.0, true, false, "Ready"),
            VehicleReading(25.0, true, false, "Riding"),
            VehicleReading(50.0, true, false, "Accelerating"),
            VehicleReading(0.0, false, true, "Motor disabled"),
            VehicleReading(12.0, false, false, "Coasting"),
        )
    }
}
