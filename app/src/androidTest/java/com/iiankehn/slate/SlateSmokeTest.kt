package com.iiankehn.slate

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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
        composeRule.onNodeWithText("Notes and word processing, in one workspace")
            .assertIsDisplayed()

        composeRule.onNodeWithText("New document")
            .assertIsDisplayed()
            .performClick()

        composeRule.onNodeWithText("Untitled document")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Start writing…")
            .assertIsDisplayed()

        composeRule.runOnUiThread {
            composeRule.activity.onBackPressedDispatcher.onBackPressed()
        }

        composeRule.onNodeWithText("Create a document")
            .assertIsDisplayed()
    }
}
