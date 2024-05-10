package com.ciyfhx.emu.viewer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ciyfhx.emu.GameBoyEmulationViewModel
import com.ciyfhx.emu.gpu.Pixel


class DisplayScreenWindowState(
    val title: String,
    private val close: (DisplayScreenWindowState) -> Unit
) {
    fun close() = close(this)
}

@Composable
fun DisplayScreen(state: DisplayScreenWindowState) =
    Window(
        onCloseRequest = state::close,
        title = state.title,
        state = rememberWindowState(width = 1000.dp, height = 800.dp)
    ) {
        Display()
    }

@Composable
fun Display() {
    val context = viewModel<GameBoyEmulationViewModel>()
    val lcd by remember { context.lcd }
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawPixelArray(lcd.pixels)
    }
}

private fun DrawScope.drawPixelArray(pixelArray: Array<Array<Pixel>>) {
    val cellSize = size.width / pixelArray.size.toFloat()
    for (row in pixelArray.indices) {
        for (col in pixelArray[row].indices) {
            val color = when (pixelArray[row][col]) {
                Pixel.PIXEL_0 -> Color.White
                Pixel.PIXEL_1 -> Color.Gray
                Pixel.PIXEL_2 -> Color.DarkGray
                Pixel.PIXEL_3 -> Color.Black
            }
            drawRect(
                color = color,
                topLeft = Offset(col * cellSize, row * cellSize),
                size = Size(cellSize, cellSize)
            )
        }
    }
}