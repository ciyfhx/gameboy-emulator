package com.ciyfhx.emu.viewer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ciyfhx.emu.GameBoyEmulationViewModel
import com.ciyfhx.emu.ViewPort
import com.ciyfhx.emu.gpu.Pixel
import com.ciyfhx.emu.gpu.Tile

class BackgroundMapViewerWindowState(
    val title: String,
    private val close: (BackgroundMapViewerWindowState) -> Unit
) {
    fun close() = close(this)
}

@Composable
fun BackgroundMapViewer(){
    val context = viewModel<GameBoyEmulationViewModel>()

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawBackgroundTiles(context.backgroundTiles, context.viewPort)
        }
    }
}

private fun DrawScope.drawBackgroundTiles(tiles: List<Tile>, viewPort: ViewPort) {
    val tileSize = size.width / 32
    val numRows = 32
    val numCols = 32
    val borderWidth = 2f
    val viewPortWidth = 3f

    for (i in 0 until numRows) {
        for (j in 0 until numCols) {

            // Draw border
            drawRect(
                color = Color.Blue,
                topLeft = Offset(j * tileSize, i * tileSize),
                size = Size(tileSize, tileSize),
                style = Stroke(width = borderWidth)
            )

            val tile = tiles[i * numCols + j]
            for (row in 0 until 8) {
                for (col in 0 until 8) {
                    val color = when (tile.pixelAt(col, row)) {
                        Pixel.PIXEL_0 -> Color.White
                        Pixel.PIXEL_1 -> Color.Gray
                        Pixel.PIXEL_2 -> Color.DarkGray
                        Pixel.PIXEL_3 -> Color.Black
                    }
                    drawRect(
                        color = color,
                        topLeft = Offset(j * tileSize + col * (tileSize / 8), i * tileSize + row * (tileSize / 8)),
                        size = Size(tileSize / 8, tileSize / 8)
                    )
                }
            }
        }
    }

    val pixelSize = tileSize / 8
    //Draw viewport
    drawRect(
        color = Color.Red,
        topLeft = Offset(pixelSize*viewPort.x, pixelSize*viewPort.y),
        style = Stroke(width = viewPortWidth),
        size = Size(viewPort.width*pixelSize, viewPort.height*pixelSize)
    )

}