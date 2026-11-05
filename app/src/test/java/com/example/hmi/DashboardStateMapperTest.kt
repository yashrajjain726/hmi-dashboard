package com.example.hmi

import com.example.hmi.dashboard.DashboardStateMapper
import com.example.hmi.data.VehicleReading
import org.junit.Assert.*
import org.junit.Test

class DashboardStateMapperTest {
    @Test fun invalidSpeedDoesNotDiscardOtherSignals() {
        listOf(-1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).forEach { speed ->
            val state = DashboardStateMapper.map(VehicleReading(speed, true, false, "Invalid"))
            assertNull(state.speedKmh)
            assertTrue(state.speedInvalid)
            assertEquals(true, state.motorOn)
            assertEquals(false, state.killSwitchActive)
        }
    }

    @Test fun motorOffDoesNotEraseCoastingSpeed() {
        val state = DashboardStateMapper.map(VehicleReading(12.0, false, false, "Coasting"))
        assertEquals(12, state.speedKmh)
        assertEquals(false, state.motorOn)
    }

    @Test fun missingSignalsStayUnknown() {
        val state = DashboardStateMapper.map(VehicleReading(null, null, null, "Missing"))
        assertNull(state.speedKmh)
        assertNull(state.motorOn)
        assertNull(state.killSwitchActive)
        assertFalse(state.speedInvalid)
    }
}
