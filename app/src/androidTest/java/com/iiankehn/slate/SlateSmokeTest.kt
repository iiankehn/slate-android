package com.iiankehn.slate

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SlateSmokeTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun launchCreateAndBackToLibrary() {
        composeRule.waitForText("New document")
        composeRule.onNodeWithText("New document")
            .assertIsDisplayed()
            .performClick()

        composeRule.waitForText("Untitled document")
        composeRule.onNodeWithText("Untitled document")
            .assertIsDisplayed()

        composeRule.runOnUiThread {
            composeRule.activity.onBackPressedDispatcher.onBackPressed()
        }

        composeRule.waitForText("Create a document")
        composeRule.onNodeWithText("Create a document")
            .assertIsDisplayed()
    }

    private fun ComposeTestRule.waitForText(text: String) {
        waitUntil(timeoutMillis = 10_000) {
            onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
