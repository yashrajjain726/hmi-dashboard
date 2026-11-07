package com.example.hmi.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Mint = Color(0xFF28E6A0)
val Muted = Color(0xFF9BA5AA)
val Panel = Color(0xFF171C20)
val Warning = Color(0xFFFFB75C)

@Composable
fun DashboardTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Mint,
            secondaryContainer = Color(0xFF123D30),
            onSecondaryContainer = Mint,
            background = Color(0xFF080B0D),
            surface = Panel,
            onBackground = Color.White,
            onSurface = Color.White,
        ),
        content = content,
    )
}
