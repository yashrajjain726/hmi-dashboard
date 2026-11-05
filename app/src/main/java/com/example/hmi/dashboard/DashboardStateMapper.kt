package com.example.hmi.dashboard

import com.example.hmi.data.VehicleReading
import kotlin.math.roundToInt

object DashboardStateMapper {
    fun map(reading: VehicleReading): DashboardUiState {
        val speed = reading.speedKmh
        val validSpeed = speed != null && speed.isFinite() && speed >= 0 && speed <= Int.MAX_VALUE.toDouble()
        return DashboardUiState(
            speedKmh = if (validSpeed) speed!!.roundToInt() else null,
            motorOn = reading.motorOn,
            killSwitchActive = reading.killSwitchActive,
            scenario = reading.scenario,
            health = DataHealth.LIVE,
            speedInvalid = speed != null && !validSpeed,
        )
    }
}
