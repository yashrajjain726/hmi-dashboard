package com.example.hmi

import com.example.hmi.data.SimulatedVehicleDataSource
import com.example.hmi.data.SimulationMode
import com.example.hmi.data.VehicleReading
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SimulatorTest {
    @Test fun emitsTheRequiredSequenceOncePerSecond() = runTest {
        val source = SimulatedVehicleDataSource()
        val received = mutableListOf<VehicleReading>()
        val job = launch { source.readings.take(6).toList(received) }
        runCurrent()
        assertEquals(1, received.size)
        advanceTimeBy(999)
        assertEquals(1, received.size)
        advanceTimeBy(4_001)
        runCurrent()
        assertEquals(SimulatedVehicleDataSource.scenarios, received)
        job.join()
    }

    @Test fun pausedSourceStopsEmittingAndNormalModeResumes() = runTest {
        val source = SimulatedVehicleDataSource()
        val received = mutableListOf<VehicleReading>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            source.readings.toList(received)
        }
        assertEquals(1, received.size)
        source.setMode(SimulationMode.PAUSED)
        advanceTimeBy(4_000)
        runCurrent()
        assertEquals(1, received.size)
        source.setMode(SimulationMode.NORMAL)
        advanceTimeBy(1_000)
        runCurrent()
        assertEquals(2, received.size)
        assertEquals("Ready", received.last().scenario)
        job.cancel()
    }
}
