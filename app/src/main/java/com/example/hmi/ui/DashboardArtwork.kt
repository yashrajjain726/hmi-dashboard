package com.example.hmi.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale

@Composable
fun DashboardArtwork(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        scale(size.width / 1000f, size.height / 600f, pivot = Offset.Zero) {
            val road = Path().apply {
                moveTo(365f, 421f); lineTo(635f, 421f); lineTo(975f, 600f); lineTo(25f, 600f); close()
            }
            drawPath(road, Brush.verticalGradient(listOf(Color(0xFF030303), Color(0xFF0C0C0C)), 420f, 600f))
            listOf(140f to 335f, 300f to 425f, 575f to 700f, 665f to 860f).forEach { (left, right) ->
                val stripe = Path().apply {
                    moveTo(500f + (left - 500f) * 0.2f, 435f); lineTo(500f + (right - 500f) * 0.2f, 435f)
                    lineTo(right, 600f); lineTo(left, 600f); close()
                }
                drawPath(stripe, Color.Black)
            }
            val tray = Path().apply {
                moveTo(347f, 121f); lineTo(653f, 121f); lineTo(660f, 130f); lineTo(650f, 180f)
                cubicTo(628f, 203f, 554f, 231f, 500f, 242f)
                cubicTo(446f, 231f, 372f, 203f, 350f, 180f); lineTo(340f, 130f); close()
            }
            drawPath(tray, Brush.verticalGradient(listOf(Color(0xFF464646), Color(0xFF383838), Color.Black), 120f, 242f))
            drawLine(Color(0xFF727272), Offset(347f, 122f), Offset(653f, 122f), 2f)
            drawLine(Color(0xFF151515), Offset(346f, 128f), Offset(654f, 128f), 5f)
            drawLine(Mint, Offset(378f, 125f), Offset(622f, 125f), 5f)
            drawLine(Color(0xFF555555), Offset(357f, 184f), Offset(643f, 184f), 1.5f)
            val underwing = Path().apply {
                moveTo(30f, 477f); lineTo(235f, 411f); cubicTo(370f, 389f, 630f, 389f, 765f, 411f)
                lineTo(970f, 477f); cubicTo(900f, 493f, 827f, 473f, 740f, 458f)
                cubicTo(625f, 433f, 375f, 433f, 260f, 458f); cubicTo(173f, 473f, 100f, 493f, 30f, 477f); close()
            }
            drawPath(underwing, Brush.verticalGradient(listOf(Color(0xFF292929), Color(0xFF0D0D0D)), 410f, 485f))
            val light = Path().apply {
                moveTo(85f, 438f); cubicTo(184f, 446f, 246f, 434f, 338f, 416f)
                cubicTo(414f, 407f, 586f, 407f, 662f, 416f); cubicTo(754f, 434f, 816f, 446f, 915f, 438f)
                lineTo(915f, 462f); cubicTo(812f, 470f, 750f, 452f, 660f, 436f)
                cubicTo(580f, 428f, 420f, 428f, 340f, 436f); cubicTo(250f, 452f, 188f, 470f, 85f, 462f); close()
            }
            drawPath(light, Brush.horizontalGradient(listOf(Color(0xFF0C0C0C), Color(0xFF4D4632), Color(0xFFFFE590), Color(0xFFFF6C00), Color(0xFFFF5A00), Color(0xFFFFDE7A), Color(0xFF3A382D), Color(0xFF0B0B0B)), endX = 1000f))
            clipPath(light) {
                for (x in 90..920 step 7) {
                    drawLine(Color.Black.copy(alpha = 0.18f), Offset(x.toFloat(), 410f), Offset(x.toFloat(), 475f), 2f)
                    drawLine(Color.White.copy(alpha = 0.1f), Offset(x + 2f, 410f), Offset(x + 2f, 475f), 1f)
                }
            }
            val canopy = Path().apply {
                moveTo(78f, 432f); lineTo(248f, 381f); lineTo(248f, 377f)
                cubicTo(291f, 359f, 324f, 345f, 378f, 351f); cubicTo(428f, 340f, 460f, 339f, 500f, 343f)
                cubicTo(540f, 339f, 572f, 340f, 622f, 351f); cubicTo(676f, 345f, 709f, 359f, 752f, 377f)
                lineTo(752f, 381f); lineTo(922f, 432f); cubicTo(818f, 445f, 748f, 417f, 660f, 413f)
                cubicTo(575f, 410f, 425f, 410f, 340f, 413f); cubicTo(252f, 417f, 182f, 445f, 78f, 432f); close()
            }
            drawPath(canopy, Brush.verticalGradient(listOf(Color(0xFF565656), Color(0xFF292929), Color(0xFF101010)), 340f, 441f))
            val ridge = Path().apply {
                moveTo(248f, 377f); cubicTo(291f, 359f, 324f, 345f, 378f, 351f)
                cubicTo(428f, 340f, 460f, 339f, 500f, 343f); cubicTo(540f, 339f, 572f, 340f, 622f, 351f)
                cubicTo(676f, 345f, 709f, 359f, 752f, 377f)
            }
            drawPath(ridge, Color(0xFF686868), style = Stroke(1f))
            clipPath(canopy) {
                for (row in 0..3) for (column in 0..15) {
                    val x = 100f + column * 13f + row * 11f
                    val y = 428f - column * 2.7f - row * 5f
                    rotate(-12f, Offset(x, y)) { drawOval(Color(0xFF161616), Offset(x, y), Size(10f, 3f)) }
                    rotate(12f, Offset(1000f - x, y)) { drawOval(Color(0xFF161616), Offset(990f - x, y), Size(10f, 3f)) }
                }
            }
            drawLine(Color(0xFFFF3038), Offset(280f, 369f), Offset(315f, 359f), 4f)
            drawLine(Color(0xFFFF3038), Offset(685f, 359f), Offset(720f, 369f), 4f)
            for (row in 0..20) {
                val alpha = (1f - row / 21f) * 0.7f
                for (column in 0..2) drawLine(Color(0xFF555555).copy(alpha = alpha), Offset(11f + column * 8, 232f + row * 7), Offset(15f + column * 8, 232f + row * 7), 2f, StrokeCap.Round)
            }
        }
    }
}

enum class Glyph { BLUETOOTH, MODE, GRID, NAVIGATION, MUSIC, TRACTION, TYRE, BATTERY, POWER, BRAKE, CRUISE }

@Composable
fun DashboardGlyph(glyph: Glyph, color: Color, modifier: Modifier = Modifier.fillMaxSize()) {
    Canvas(modifier) {
        scale(size.width / 40f, size.height / 40f, pivot = Offset.Zero) {
            fun line(x1: Float, y1: Float, x2: Float, y2: Float, width: Float = 2.5f) = drawLine(color, Offset(x1, y1), Offset(x2, y2), width, StrokeCap.Round)
            when (glyph) {
                Glyph.BLUETOOTH -> { line(20f, 3f, 20f, 37f); line(20f, 3f, 32f, 13f); line(32f, 13f, 9f, 30f); line(9f, 10f, 32f, 27f); line(32f, 27f, 20f, 37f) }
                Glyph.MODE -> {
                    val hex = Path().apply { moveTo(20f, 0f); lineTo(38f, 10f); lineTo(38f, 30f); lineTo(20f, 40f); lineTo(2f, 30f); lineTo(2f, 10f); close() }
                    drawPath(hex, color)
                    for (angle in listOf(0f, 90f, 180f, 270f)) rotate(angle, Offset(20f, 20f)) { drawOval(Color.Black, Offset(13f, 7f), Size(9f, 17f)) }
                    drawCircle(color, 4f, Offset(20f, 20f))
                }
                Glyph.GRID -> {
                    drawRoundRect(color, Offset(3f, 4f), Size(14f, 14f), CornerRadius(2f))
                    rotate(45f, Offset(29f, 11f)) { drawRect(color, Offset(23f, 5f), Size(12f, 12f)) }
                    drawRoundRect(color, Offset(3f, 23f), Size(14f, 14f), CornerRadius(2f)); drawRoundRect(color, Offset(23f, 23f), Size(14f, 14f), CornerRadius(2f))
                }
                Glyph.NAVIGATION -> {
                    val arrow = Path().apply { moveTo(20f, 2f); lineTo(36f, 36f); lineTo(21f, 29f); lineTo(5f, 36f); close() }; drawPath(arrow, color)
                }
                Glyph.MUSIC -> { line(15f, 8f, 33f, 3f, 5f); line(15f, 8f, 15f, 30f, 4f); line(33f, 3f, 33f, 26f, 4f); drawOval(color, Offset(4f, 27f), Size(13f, 10f)); drawOval(color, Offset(22f, 23f), Size(13f, 10f)) }
                Glyph.BATTERY -> { drawRoundRect(color, Offset(2f, 6f), Size(31f, 28f), CornerRadius(6f), style = Stroke(2f)); drawRoundRect(color, Offset(6f, 10f), Size(23f, 20f), CornerRadius(3f)); line(38f, 16f, 38f, 24f, 3f) }
                Glyph.BRAKE -> {
                    val bolt = Path().apply { moveTo(12f, 2f); lineTo(39f, 2f); lineTo(24f, 18f); lineTo(37f, 18f); lineTo(13f, 38f); lineTo(0f, 38f); lineTo(17f, 20f); lineTo(3f, 20f); close() }; drawPath(bolt, color)
                }
                Glyph.POWER -> {
                    drawArc(color, -55f, 290f, false, Offset(4f, 4f), Size(32f, 32f), style = Stroke(2.5f)); line(20f, 1f, 20f, 16f)
                    val bolt = Path().apply { moveTo(23f, 12f); lineTo(15f, 24f); lineTo(21f, 24f); lineTo(18f, 32f); lineTo(28f, 19f); lineTo(22f, 19f); close() }; drawPath(bolt, color)
                }
                Glyph.CRUISE -> {
                    drawArc(color, 135f, 270f, false, Offset(4f, 6f), Size(32f, 32f), style = Stroke(3f)); line(20f, 24f, 29f, 13f)
                    for (angle in 0..4) rotate(angle * 45f - 90f, Offset(20f, 22f)) { line(20f, 7f, 20f, 11f, 2f) }; drawCircle(color, 3f, Offset(20f, 24f))
                }
                Glyph.TYRE -> {
                    drawCircle(color, 15f, Offset(20f, 18f), style = Stroke(2f)); drawCircle(color, 10f, Offset(20f, 18f), style = Stroke(2f))
                    for (angle in 0..3) rotate(angle * 90f, Offset(20f, 18f)) { line(20f, 5f, 20f, 11f, 2f) }; line(5f, 37f, 35f, 37f, 2f)
                }
                Glyph.TRACTION -> { drawArc(color, 120f, 290f, false, Offset(4f, 4f), Size(32f, 32f), style = Stroke(2.5f)); line(10f, 35f, 31f, 2f, 3f); line(14f, 13f, 14f, 28f); line(9f, 13f, 19f, 13f); drawArc(color, 80f, 270f, false, Offset(22f, 14f), Size(10f, 14f), style = Stroke(2f)) }
            }
        }
    }
}
