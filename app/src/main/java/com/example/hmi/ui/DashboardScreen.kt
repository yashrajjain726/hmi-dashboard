package com.example.hmi.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hmi.dashboard.DashboardUiState
import com.example.hmi.dashboard.DataHealth
import com.example.hmi.data.SimulationMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun DashboardScreen(state: DashboardUiState, selectedMode: SimulationMode, onModeChange: (SimulationMode) -> Unit) {
    var controlsOpen by remember { mutableStateOf(false) }
    val fresh = state.health == DataHealth.LIVE
    val motor = if (fresh) state.motorOn else null
    val kill = if (fresh) state.killSwitchActive else null
    val healthLabel = when (state.health) {
        DataHealth.WAITING -> "WAITING FOR DATA"
        DataHealth.LIVE -> "LIVE SIMULATION"
        DataHealth.STALE -> "DATA STALE · LAST READING HIDDEN"
        DataHealth.ERROR -> "DATA SOURCE UNAVAILABLE"
    }
    val clock = remember { LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE dd MMM  hh:mm a", Locale.ENGLISH)) }
    Surface(Modifier.fillMaxSize(), color = Color.Black) {
        ReferenceViewport {
            DashboardArtwork(Modifier.fillMaxSize())
            At(22, 8, 20, 24) { DashboardGlyph(Glyph.BLUETOOTH, Color(0xFF3260FF)) }
            At(45, 9, 170, 24) { Label("Demo phone… (18%)", 16, Color(0xFFBBBBBB)) }
            At(220, 10, 55, 22) { Label("●   ⊖", 18, Color(0xFFCCCCCC)) }
            At(320, 13, 375, 22) { Label(healthLabel, 10, if (fresh) Muted else Warning, align = TextAlign.Center) }
            At(750, 10, 215, 24) { Label("$clock  LTE ▂▅▇", 14, Color(0xFFCCCCCC), align = TextAlign.End) }

            At(481, 55, 38, 44) { DashboardGlyph(Glyph.MODE, Mint) }
            At(363, 140, 84, 34) {
                Box(Modifier.fillMaxSize().background(Color(0xFF242424), CircleShape), contentAlignment = Alignment.Center) {
                    Label("ECO", 20, Mint, FontWeight.Bold, TextAlign.Center)
                }
            }
            At(452, 146, 35, 28) { Label("⌁", 29, Muted) }
            At(542, 131, 55, 50) { Label("62", 36, Color(0xFFECECEC), FontWeight.Medium) }
            At(596, 139, 62, 40) { Label("km\nRange", 15, Color(0xFFC3C3C3), lineHeight = 16) }
            At(355, 225, 290, 145) {
                Text(
                    if (fresh) state.speedKmh?.toString() ?: "—" else "—",
                    modifier = Modifier.fillMaxWidth().semantics {
                        contentDescription = if (fresh && state.speedKmh != null) "Speed ${state.speedKmh} kilometres per hour" else "Speed unavailable"
                    },
                    color = Color.White, fontSize = 142.sp, lineHeight = 142.sp,
                    fontWeight = FontWeight.Medium, textAlign = TextAlign.Center,
                )
            }
            At(450, 369, 100, 24) { Label("km/h", 15, Color(0xFF8C8C8C), align = TextAlign.Center, spacing = 4) }
            At(0, 126, 40, 100) {
                Box(Modifier.fillMaxSize().background(Color(0xFF252525), RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)), contentAlignment = Alignment.Center) {
                    Label("P\nA\nR\nK\nI\nN\nG\n»", 9, Color(0xFFC5C5C5), align = TextAlign.Center, lineHeight = 11)
                }
            }
            At(82, 257, 164, 65) {
                Column(
                    Modifier.fillMaxSize().background(Color(0xFF101212), RoundedCornerShape(22.dp))
                        .border(1.dp, if (kill == true) Warning.copy(alpha = 0.5f) else Color(0xFF303536), RoundedCornerShape(22.dp))
                        .padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Label("KILL SWITCH", 10, Muted, spacing = 2)
                    Label(when (kill) { true -> "ACTIVE"; false -> "INACTIVE"; null -> "UNAVAILABLE" }, 19,
                        if (kill == true) Warning else if (kill == false) Color(0xFFCACFD0) else Muted, FontWeight.SemiBold)
                }
            }

            At(790, 120, 47, 42) { DashboardGlyph(Glyph.BRAKE, Color(0xFFFF303E)) }
            At(844, 119, 120, 52) { Label("Rapid\nBrake", 21, Color.White, FontWeight.Bold, lineHeight = 22) }
            At(791, 186, 143, 40) { SuperHold() }
            At(791, 251, 48, 48) {
                Box(Modifier.fillMaxSize().border(4.dp, Color(0xFFF04D68), CircleShape), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Label("80", 18, Color.White, FontWeight.Bold, TextAlign.Center, lineHeight = 18)
                        Label("km/h", 7, Color.White, align = TextAlign.Center, spacing = 1)
                    }
                }
            }
            At(844, 256, 120, 45) { Label("Speed\nExceeded", 17, Color.White, FontWeight.Bold, lineHeight = 18) }
            At(790, 323, 157, 60) {
                Row(Modifier.fillMaxSize().border(1.dp, Color(0xFF343434), CircleShape).padding(11.dp), verticalAlignment = Alignment.CenterVertically) {
                    DashboardGlyph(Glyph.CRUISE, Mint, Modifier.size(43.dp))
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Label("Active", 16, Mint, FontWeight.SemiBold)
                        Label("72 km/h", 20, Color.White, FontWeight.Bold)
                    }
                }
            }
            At(330, 480, 30, 35) { Label("‹", 32, Muted, align = TextAlign.Center) }
            At(420, 485, 180, 30) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("ODO", color = Color(0xFF747474), fontSize = 20.sp)
                    Spacer(Modifier.width(14.dp))
                    Label("1324 km", 20, Color(0xFFDEDEDE), FontWeight.Medium)
                }
            }
            At(645, 480, 30, 35) { Label("›", 32, Muted, align = TextAlign.Center) }
            At(280, 516, 440, 22) {
                Label(
                    when {
                        !fresh -> "Waiting for a fresh reading"
                        state.speedInvalid -> "Invalid speed reading"
                        state.speedKmh == null -> "Speed signal unavailable"
                        else -> state.scenario
                    },
                    12, if (state.speedInvalid) Warning else Muted, align = TextAlign.Center,
                )
            }
            At(18, 538, 60, 58) {
                IconButton(
                    onClick = { controlsOpen = true },
                    modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A), CircleShape)
                        .semantics { contentDescription = "Simulation controls" },
                ) { DashboardGlyph(Glyph.GRID, Color(0xFFDDDDDD), Modifier.size(25.dp)) }
            }
            At(116, 538, 59, 58) { RoundIcon(Glyph.NAVIGATION) }
            At(185, 538, 59, 58) { RoundIcon(Glyph.MUSIC) }
            At(542, 541, 53, 53) { RoundIcon(Glyph.TRACTION, Color(0xFFFFB400)) }
            At(664, 541, 53, 53) { RoundIcon(Glyph.TYRE) }
            At(724, 541, 159, 52) {
                Row(Modifier.fillMaxSize().background(Color(0xFF191919), CircleShape).padding(horizontal = 22.dp), verticalAlignment = Alignment.CenterVertically) {
                    DashboardGlyph(Glyph.BATTERY, Mint, Modifier.size(35.dp, 27.dp))
                    Spacer(Modifier.width(11.dp))
                    Label("100%", 21, Mint, FontWeight.Bold)
                }
            }
            At(895, 541, 88, 52) {
                Row(
                    Modifier.fillMaxSize().background(if (motor == true) Color(0xFF003B22) else Color(0xFF202525), CircleShape).padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
                ) {
                    if (motor != null) DashboardGlyph(Glyph.POWER, if (motor) Mint else Muted, Modifier.size(23.dp))
                    Spacer(Modifier.width(5.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Label("MOTOR", 7, Muted, align = TextAlign.Center)
                        Label(when (motor) { true -> "ON"; false -> "OFF"; null -> "UNAVAILABLE" }, if (motor == null) 9 else 17,
                            if (motor == true) Mint else Muted, FontWeight.Bold, TextAlign.Center)
                    }
                }
            }
        }
    }
    if (controlsOpen) {
        AlertDialog(
            onDismissRequest = { controlsOpen = false }, title = { Text("Vehicle simulation") },
            text = {
                Row {
                    listOf(
                        SimulationMode.NORMAL to "Normal", SimulationMode.INVALID_SPEED to "Invalid speed",
                        SimulationMode.MISSING_SIGNALS to "Missing data", SimulationMode.PAUSED to "Pause data",
                    ).forEach { (mode, label) ->
                        TextButton(
                            onClick = { onModeChange(mode); controlsOpen = false }, modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                        ) {
                            Text(label, fontSize = 11.sp, lineHeight = 14.sp, maxLines = 2,
                                textAlign = TextAlign.Center, color = if (selectedMode == mode) Mint else Color.White)
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { controlsOpen = false }) { Text("Close") } },
        )
    }
}

@Composable
private fun ReferenceViewport(content: @Composable BoxScope.() -> Unit) {
    val density = LocalDensity.current
    val width = with(density) { 1000.dp.roundToPx() }
    val height = with(density) { 600.dp.roundToPx() }
    Layout(content = { Box(Modifier.fillMaxSize(), content = content) }, modifier = Modifier.fillMaxSize().safeDrawingPadding()) { measurables, constraints ->
        val child = measurables.single().measure(Constraints.fixed(width, height))
        val scale = min(constraints.maxWidth / width.toFloat(), constraints.maxHeight / height.toFloat())
        layout(constraints.maxWidth, constraints.maxHeight) {
            child.placeWithLayer(((constraints.maxWidth - width * scale) / 2).roundToInt(), ((constraints.maxHeight - height * scale) / 2).roundToInt()) {
                scaleX = scale; scaleY = scale; transformOrigin = TransformOrigin(0f, 0f)
            }
        }
    }
}

@Composable
private fun At(x: Int, y: Int, width: Int, height: Int, content: @Composable BoxScope.() -> Unit) {
    Box(Modifier.offset(x.dp, y.dp).size(width.dp, height.dp), content = content)
}

@Composable
private fun Label(text: String, size: Int, color: Color, weight: FontWeight = FontWeight.Normal, align: TextAlign = TextAlign.Start, lineHeight: Int = size + 3, spacing: Int = 0) {
    Text(text, color = color, fontSize = size.sp, fontWeight = weight, textAlign = align,
        lineHeight = lineHeight.sp, letterSpacing = spacing.sp, modifier = Modifier.fillMaxWidth())
}

@Composable
private fun RoundIcon(glyph: Glyph, color: Color = Color(0xFFCECECE)) {
    Box(Modifier.fillMaxSize().background(Color(0xFF1C1C1C), CircleShape), contentAlignment = Alignment.Center) {
        DashboardGlyph(glyph, color, Modifier.size(28.dp))
    }
}

@Composable
private fun SuperHold() {
    Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Text("❨", color = Color(0xFF00A98D), fontSize = 36.sp, fontWeight = FontWeight.Bold)
        Box(Modifier.weight(1f).fillMaxHeight().background(Color(0xFF00DDB7), CircleShape), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Label("SUPER", 11, Color.Black, FontWeight.Bold, TextAlign.Center, 11, 2)
                Label("Hold", 22, Color.Black, FontWeight.Bold, TextAlign.Center, 23)
            }
        }
        Text("❩", color = Color(0xFF00A98D), fontSize = 36.sp, fontWeight = FontWeight.Bold)
    }
}
