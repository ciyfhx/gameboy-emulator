package com.ciyfhx.emu.viewer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ciyfhx.emu.GameBoyEmulationViewModel

class DebugScreenWindowState(
    val title: String,
    private val close: (DebugScreenWindowState) -> Unit
) {
    fun close() = close(this)
}

@Composable
fun DebugScreen(state: DebugScreenWindowState) =
    Window(
        onCloseRequest = state::close,
        title = state.title,
        state = rememberWindowState(width = 1000.dp, height = 800.dp)
    ) {
        val context: GameBoyEmulationViewModel = viewModel()
        Row{
            Column(modifier = Modifier.weight(.35f)) {
                Row{
                    Button(onClick = {
                        context.start()
                    }){
                        Text("Start")
                    }
                    Button(onClick = {
                        context.stop()
                    }){
                        Text("Stop")
                    }
                }
                StackViewer()
            }

            Column(modifier = Modifier.weight(.65f)) {
                Row(Modifier.weight(.4f)) {
                    RegistersTableScreen()
                }
                Row(Modifier.weight(.6f)) {
                    RamTableScreen()
                }
            }
        }
    }

