package com.example.hmi.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hmi.dashboard.DashboardUiState
import com.example.hmi.dashboard.DataHealth
import com.example.hmi.data.SimulationMode

@Composable
fun DashboardScreen(state: DashboardUiState, selectedMode: SimulationMode, onModeChange: (SimulationMode) -> Unit) {
    val fresh = state.health == DataHealth.LIVE
    val healthColor = if (fresh) Mint else Warning
    val healthLabel = when (state.health) {
        DataHealth.WAITING -> "WAITING FOR DATA"
        DataHealth.LIVE -> "LIVE SIMULATION"
        DataHealth.STALE -> "DATA STALE · LAST READING HIDDEN"
        DataHealth.ERROR -> "DATA SOURCE UNAVAILABLE"
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(Modifier.safeDrawingPadding().padding(horizontal = 24.dp, vertical = 12.dp)) {
            val speedTextSize = (maxHeight.value * 0.34f).coerceIn(58f, 160f).sp
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("SIMPLE / RIDE", color = Mint, fontWeight = FontWeight.Bold, letterSpacing = 3.sp, fontSize = 14.sp)
                    Spacer(Modifier.weight(1f))
                    Text(healthLabel, color = healthColor, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
                HorizontalDivider(color = Color(0xFF262E33))
                Row(Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Column(
                        Modifier.weight(1f).fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text("HOME / RIDE", color = Muted, fontSize = 11.sp, letterSpacing = 3.sp)
                        Text(
                            text = if (fresh) state.speedKmh?.toString() ?: "—" else "—",
                            fontSize = speedTextSize,
                            lineHeight = speedTextSize,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.semantics {
                                contentDescription = if (fresh && state.speedKmh != null) "Speed ${state.speedKmh} kilometres per hour" else "Speed unavailable"
                            },
                        )
                        Text("km/h", color = Muted, fontSize = 16.sp, letterSpacing = 4.sp)
                        Spacer(Modifier.height(10.dp))
                        SpeedRibbon(if (fresh) state.speedKmh else null, Modifier.fillMaxWidth(0.9f).height(24.dp))
                        Text(
                            when {
                                !fresh -> "Waiting for a fresh reading"
                                state.speedInvalid -> "Invalid speed reading"
                                state.speedKmh == null -> "Speed signal unavailable"
                                else -> state.scenario
                            },
                            color = if (state.speedInvalid) Warning else Muted,
                            fontSize = 12.sp,
                        )
                    }
                    Column(
                        Modifier.weight(0.58f).fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                    ) {
                        StatusPanel("MOTOR", if (fresh) state.motorOn else null, "ON", "OFF", false)
                        StatusPanel("KILL SWITCH", if (fresh) state.killSwitchActive else null, "ACTIVE", "INACTIVE", true)
                        Text("SIMULATED VEHICLE DATA\nNo vehicle hardware connected", color = Muted, fontSize = 10.sp, lineHeight = 15.sp)
                    }
                }
                HorizontalDivider(color = Color(0xFF262E33))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("DEMO", color = Muted, fontSize = 10.sp, modifier = Modifier.padding(end = 4.dp))
                    listOf(
                        SimulationMode.NORMAL to "Normal",
                        SimulationMode.INVALID_SPEED to "Invalid speed",
                        SimulationMode.MISSING_SIGNALS to "Missing data",
                        SimulationMode.PAUSED to "Pause data",
                    ).forEach { (mode, label) ->
                        FilterChip(
                            selected = selectedMode == mode,
                            onClick = { onModeChange(mode) },
                            label = { Text(label, fontSize = 11.sp) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusPanel(label: String, value: Boolean?, active: String, inactive: String, activeIsWarning: Boolean) {
    val color = when {
        value == null -> Muted
        value && activeIsWarning -> Warning
        value -> Mint
        else -> Color(0xFFC2CBD0)
    }
    Column(
        Modifier.fillMaxWidth()
            .background(Panel, RoundedCornerShape(16.dp))
            .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(label, color = Muted, fontSize = 10.sp, letterSpacing = 2.sp)
        Text(when (value) { true -> active; false -> inactive; null -> "UNAVAILABLE" }, color = color, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun SpeedRibbon(speed: Int?, modifier: Modifier) {
    Canvas(modifier) {
        val ribbon = Path().apply {
            moveTo(0f, size.height * 0.75f)
            lineTo(size.width * 0.25f, 0f)
            lineTo(size.width * 0.75f, 0f)
            lineTo(size.width, size.height * 0.75f)
            lineTo(size.width, size.height)
            lineTo(size.width * 0.75f, size.height * 0.35f)
            lineTo(size.width * 0.25f, size.height * 0.35f)
            lineTo(0f, size.height)
            close()
        }
        drawPath(ribbon, Brush.horizontalGradient(listOf(Panel, if (speed != null && speed > 0) Mint else Muted, Panel)))
    }
}
