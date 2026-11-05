package com.example.hmi

import androidx.lifecycle.ViewModelStore
import com.example.hmi.dashboard.DashboardViewModel
import com.example.hmi.dashboard.DataHealth
import com.example.hmi.data.SimulatedVehicleDataSource
import com.example.hmi.data.VehicleDataSource
import com.example.hmi.data.VehicleReading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val store = ViewModelStore()

    @Before fun setUp() { Dispatchers.setMain(dispatcher) }
    @After fun tearDown() { store.clear(); Dispatchers.resetMain() }

    private fun model(source: VehicleDataSource): DashboardViewModel =
        DashboardViewModel(source, { dispatcher.scheduler.currentTime }).also { store.put("dashboard", it) }

    @Test fun everyScenarioReachesTheUiState() = runTest(dispatcher) {
        val source = FakeVehicleDataSource()
        val vm = model(source)
        runCurrent()
        assertEquals(DataHealth.WAITING, vm.state.value.health)
        SimulatedVehicleDataSource.scenarios.forEach { reading ->
            source.send(reading)
            runCurrent()
            assertEquals(reading.speedKmh!!.toInt(), vm.state.value.speedKmh)
            assertEquals(reading.motorOn, vm.state.value.motorOn)
            assertEquals(reading.killSwitchActive, vm.state.value.killSwitchActive)
            assertEquals(DataHealth.LIVE, vm.state.value.health)
        }
    }

    @Test fun silenceMarksDataStaleAndNewDataRestoresIt() = runTest(dispatcher) {
        val source = FakeVehicleDataSource()
        val vm = model(source)
        source.send(VehicleReading(25.0, true, false, "Riding"))
        runCurrent()
        advanceTimeBy(2_999)
        assertEquals(DataHealth.LIVE, vm.state.value.health)
        advanceTimeBy(1)
        runCurrent()
        assertEquals(DataHealth.STALE, vm.state.value.health)
        source.send(VehicleReading(0.0, false, true, "Motor disabled"))
        runCurrent()
        assertEquals(DataHealth.LIVE, vm.state.value.health)
        assertEquals(0, vm.state.value.speedKmh)
        assertEquals(true, vm.state.value.killSwitchActive)
    }

    @Test fun sourceFailureBecomesAnErrorState() = runTest(dispatcher) {
        val source = object : VehicleDataSource {
            override val readings = flow<VehicleReading> { error("Disconnected") }
        }
        val vm = model(source)
        runCurrent()
        assertEquals(DataHealth.ERROR, vm.state.value.health)
    }

    @Test fun noFirstReadingRemainsWaiting() = runTest(dispatcher) {
        val vm = model(FakeVehicleDataSource())
        advanceTimeBy(5_000)
        runCurrent()
        assertEquals(DataHealth.WAITING, vm.state.value.health)
        assertNull(vm.state.value.speedKmh)
    }
}

private class FakeVehicleDataSource : VehicleDataSource {
    private val channel = Channel<VehicleReading>(Channel.UNLIMITED)
    override val readings = channel.receiveAsFlow()
    suspend fun send(reading: VehicleReading) { channel.send(reading) }
}
