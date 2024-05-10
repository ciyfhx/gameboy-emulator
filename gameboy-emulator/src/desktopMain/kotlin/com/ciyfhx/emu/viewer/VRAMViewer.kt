package com.ciyfhx.emu.viewer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ciyfhx.emu.GameBoyEmulationViewModel
import com.ciyfhx.emu.gpu.Pixel
import com.ciyfhx.emu.gpu.Tile

class VRAMViewerWindowState(
    val title: String,
    private val close: (VRAMViewerWindowState) -> Unit
) {
    fun close() = close(this)
}

@Composable
fun VRAMViewer(){
//    val hexString = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF".chunked(2) { it.toString().toInt(16).toByte() }.toByteArray()
//    val tiles = List(16) { Tile(hexString) }
    val context = viewModel<GameBoyEmulationViewModel>()

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawVRAMTiles(context.vramTiles)
        }
    }
}

private fun DrawScope.drawVRAMTiles(tiles: List<Tile>) {
    val tileSize = size.width / 16
    val numRows = 24
    val numCols = 16
    val borderWidth = 2f

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
}