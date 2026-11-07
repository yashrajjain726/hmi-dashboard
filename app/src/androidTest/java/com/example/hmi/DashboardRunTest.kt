package com.example.hmi

import android.graphics.Bitmap
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DashboardRunTest {
    @get:Rule val screen = createAndroidComposeRule<MainActivity>()

    @Test fun verifyLiveScenariosAndFailureRecovery() {
        val scenarios = listOf(
            Triple("Parked", "0", "OFF"),
            Triple("Ready", "0", "ON"),
            Triple("Riding", "25", "ON"),
            Triple("Accelerating", "50", "ON"),
            Triple("Motor disabled", "0", "OFF"),
            Triple("Coasting", "12", "OFF"),
        )
        scenarios.forEachIndexed { index, (scenario, speed, motor) ->
            awaitText(scenario)
            screen.onNodeWithText(scenario).assertIsDisplayed()
            screen.onNodeWithText(speed).assertIsDisplayed()
            screen.onNodeWithText(motor).assertIsDisplayed()
            screen.onNodeWithText(if (scenario == "Motor disabled") "ACTIVE" else "INACTIVE").assertIsDisplayed()
            capture("${index + 1}-${scenario.lowercase().replace(' ', '-')}")
        }

        screen.onNodeWithText("Invalid speed").performClick()
        awaitText("Invalid speed reading")
        screen.onNodeWithText("—").assertIsDisplayed()
        screen.onNodeWithText("ON").assertIsDisplayed()
        capture("7-invalid-speed")

        screen.onNodeWithText("Missing data").performClick()
        awaitText("Speed signal unavailable")
        check(screen.onAllNodesWithText("UNAVAILABLE").fetchSemanticsNodes().size == 2)
        screen.onAllNodesWithText("UNAVAILABLE")[0].assertIsDisplayed()
        screen.onAllNodesWithText("UNAVAILABLE")[1].assertIsDisplayed()
        capture("8-missing-data")

        screen.onNodeWithText("Normal").performClick()
        awaitText("Parked")
        screen.onNodeWithText("Pause data").performClick()
        awaitText("DATA STALE · LAST READING HIDDEN")
        screen.onNodeWithText("—").assertIsDisplayed()
        check(screen.onAllNodesWithText("UNAVAILABLE").fetchSemanticsNodes().size == 2)
        capture("9-stale-data")

        screen.onNodeWithText("Normal").performClick()
        awaitText("Ready")
        screen.onNodeWithText("LIVE SIMULATION").assertIsDisplayed()
        screen.onNodeWithText("ON").assertIsDisplayed()
        capture("10-recovered")
    }

    private fun awaitText(text: String) {
        screen.waitUntil(timeoutMillis = 20_000) {
            screen.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun capture(name: String) {
        screen.waitForIdle()
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val directory = File(instrumentation.targetContext.getExternalFilesDir(null), "verification")
        check(directory.mkdirs() || directory.isDirectory)
        val bitmap = checkNotNull(instrumentation.uiAutomation.takeScreenshot())
        File(directory, "$name.png").outputStream().use {
            check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, it))
        }
        bitmap.recycle()
    }
}
