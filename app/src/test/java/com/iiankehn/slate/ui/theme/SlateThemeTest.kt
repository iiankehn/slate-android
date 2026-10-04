package com.iiankehn.slate.ui.theme

import androidx.compose.ui.graphics.luminance
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SlateThemeTest {
    @Test
    fun editorCanvasChangesWithSystemTheme() {
        val light = slateEditorColors(darkTheme = false)
        val dark = slateEditorColors(darkTheme = true)

        assertNotEquals(light.canvas, dark.canvas)
        assertNotEquals(light.paper, dark.paper)
        assertNotEquals(light.onPaper, dark.onPaper)
        assertTrue(light.paper.luminance() > dark.paper.luminance())
        assertTrue(light.onPaper.luminance() < dark.onPaper.luminance())
    }
}
