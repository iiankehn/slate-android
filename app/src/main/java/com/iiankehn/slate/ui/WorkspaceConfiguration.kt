package com.iiankehn.slate.ui

import com.iiankehn.slate.model.InputModality
import com.iiankehn.slate.model.R2FormFactor

data class WorkspaceConfiguration(
    val formFactor: R2FormFactor,
    val navigation: NavigationPresentation,
    val inspector: InspectorPresentation,
    val toolbar: ToolbarPresentation,
    val pageScaleMode: PageScaleMode,
)

enum class NavigationPresentation { DestinationScreen, PersistentRail, PersistentPanel }

enum class InspectorPresentation { ModalSheet, CollapsiblePanel, PersistentPanel }

enum class ToolbarPresentation { CompactDock, ScrollableRibbon, FullRibbon }

enum class PageScaleMode { FitWidth, FitPage, ActualSize }

/**
 * Produces deterministic workspace behavior from the complete window size and available input
 * hardware. Using the shortest dimension keeps landscape phones and short multi-window layouts
 * from being mistaken for tablets.
 */
fun workspaceConfiguration(
    widthDp: Int,
    heightDp: Int = Int.MAX_VALUE,
    smallestWidthDp: Int = minOf(widthDp, heightDp),
    isFoldable: Boolean = false,
    isGooglebookAndroid: Boolean = false,
    inputs: Set<InputModality> = setOf(InputModality.Touch),
): WorkspaceConfiguration {
    require(widthDp > 0) { "Workspace width must be positive." }
    require(heightDp > 0) { "Workspace height must be positive." }
    require(smallestWidthDp > 0) { "Smallest screen width must be positive." }

    val hasDesktopInput = InputModality.HardwareKeyboard in inputs ||
        InputModality.MouseTrackpad in inputs
    val compactWindow = smallestWidthDp < 600 || minOf(widthDp, heightDp) < 600

    val formFactor = when {
        isGooglebookAndroid -> R2FormFactor.GooglebookAndroid
        isFoldable -> R2FormFactor.Foldable
        !compactWindow -> R2FormFactor.Tablet
        else -> R2FormFactor.Phone
    }

    return when {
        compactWindow -> WorkspaceConfiguration(
            formFactor = formFactor,
            navigation = NavigationPresentation.DestinationScreen,
            inspector = InspectorPresentation.ModalSheet,
            toolbar = ToolbarPresentation.CompactDock,
            pageScaleMode = PageScaleMode.FitWidth,
        )

        widthDp >= 1200 -> WorkspaceConfiguration(
            formFactor = formFactor,
            navigation = NavigationPresentation.PersistentPanel,
            inspector = InspectorPresentation.PersistentPanel,
            toolbar = ToolbarPresentation.FullRibbon,
            pageScaleMode = if (hasDesktopInput) PageScaleMode.ActualSize else PageScaleMode.FitPage,
        )

        widthDp >= 840 -> WorkspaceConfiguration(
            formFactor = formFactor,
            navigation = NavigationPresentation.PersistentRail,
            inspector = InspectorPresentation.CollapsiblePanel,
            toolbar = ToolbarPresentation.ScrollableRibbon,
            pageScaleMode = PageScaleMode.FitPage,
        )

        widthDp >= 600 -> WorkspaceConfiguration(
            formFactor = formFactor,
            navigation = NavigationPresentation.PersistentRail,
            inspector = InspectorPresentation.ModalSheet,
            toolbar = ToolbarPresentation.ScrollableRibbon,
            pageScaleMode = PageScaleMode.FitWidth,
        )
        else -> error("Unreachable workspace width")
    }
}
