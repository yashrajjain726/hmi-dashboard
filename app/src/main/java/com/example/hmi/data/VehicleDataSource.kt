package com.example.hmi.data

import kotlinx.coroutines.flow.Flow

interface VehicleDataSource {
    val readings: Flow<VehicleReading>
}
