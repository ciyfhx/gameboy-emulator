package com.ciyfhx.emu


import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.ciyfhx.emu.viewer.*

class ApplicationState {

    var debugScreen: DebugScreenWindowState? = null
    var display: DisplayScreenWindowState? = null
    var vramViewer: VRAMViewerWindowState? = null

    init {
        debugScreen = DebugScreenWindowState("GameBoy Emulator (Debug)")
        display = DisplayScreenWindowState("GameBoy Emulator")
        vramViewer = VRAMViewerWindowState("GameBoy Emulator (VRAM)")
    }

    private fun DisplayScreenWindowState(
        title: String
    ) = DisplayScreenWindowState(
        title,
    ) {
        display = null
    }

    private fun DebugScreenWindowState(
        title: String
    ) = DebugScreenWindowState(
        title,
    ) {
        debugScreen = null
    }

    private fun VRAMViewerWindowState(
        title: String
    ) = VRAMViewerWindowState(
        title,
    ) {
        vramViewer = null
    }

}

class ViewModelStoreOwner(override val viewModelStore: ViewModelStore = ViewModelStore()) : ViewModelStoreOwner
@Composable
fun ViewModelStoreOwnerProvider(content: @Composable () -> Unit) {
    val viewModelStoreOwner = remember { ViewModelStoreOwner() }
    CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
        content()
    }
}


fun main() =
    application {
        val applicationState = remember { ApplicationState() }
        MaterialTheme {
            ViewModelStoreOwnerProvider {
                applicationState.debugScreen?.let { DebugScreen(it) }
                applicationState.display?.let { DisplayScreen(it) }
                applicationState.vramViewer?.let { VRAMViewerScreen(it) }
            }
        }
    }



