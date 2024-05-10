package com.ciyfhx.emu.viewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState

@Preview
@Composable
fun BackgroundMapViewerScreen(state: BackgroundMapViewerWindowState) = Window(
    onCloseRequest = state::close,
    title = state.title,
    state = rememberWindowState(width = 1000.dp, height = 800.dp)
) {
    BackgroundMapViewer()
}
