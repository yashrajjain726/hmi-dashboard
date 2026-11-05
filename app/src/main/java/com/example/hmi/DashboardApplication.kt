package com.example.hmi

import android.app.Application
import com.example.hmi.data.SimulatedVehicleDataSource

class DashboardApplication : Application() {
    val vehicleData by lazy { SimulatedVehicleDataSource() }
}
