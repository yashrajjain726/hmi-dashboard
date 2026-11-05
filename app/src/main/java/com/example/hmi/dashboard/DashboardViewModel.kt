package com.example.hmi.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hmi.data.VehicleDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DashboardViewModel(
    source: VehicleDataSource,
    private val nowMillis: () -> Long,
    private val staleAfterMillis: Long = 3_000,
) : ViewModel() {
    private val mutableState = MutableStateFlow(DashboardUiState())
    val state = mutableState.asStateFlow()
    private var lastReceivedAt: Long? = null

    init {
        require(staleAfterMillis > 0)
        viewModelScope.launch {
            source.readings.catch {
                mutableState.update { it.copy(health = DataHealth.ERROR) }
            }.collect { reading ->
                lastReceivedAt = nowMillis()
                mutableState.value = DashboardStateMapper.map(reading)
            }
        }
        viewModelScope.launch {
            while (isActive) {
                delay(250)
                val receivedAt = lastReceivedAt ?: continue
                if (nowMillis() - receivedAt >= staleAfterMillis) {
                    mutableState.update {
                        if (it.health == DataHealth.LIVE) it.copy(health = DataHealth.STALE) else it
                    }
                }
            }
        }
    }
}
