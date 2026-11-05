package com.example.hmi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.os.SystemClock
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.hmi.dashboard.DashboardViewModel
import com.example.hmi.data.SimulatedVehicleDataSource
import com.example.hmi.ui.DashboardScreen
import com.example.hmi.ui.DashboardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val source = remember { SimulatedVehicleDataSource() }
            val dashboard: DashboardViewModel = viewModel(factory = viewModelFactory {
                initializer { DashboardViewModel(source, SystemClock::elapsedRealtime) }
            })
            val state by dashboard.state.collectAsStateWithLifecycle()
            DashboardTheme { DashboardScreen(state, source::setMode) }
        }
    }
}
