package com.ciyfhx.emu.viewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ciyfhx.emu.GameBoyEmulationViewModel

@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RamTableScreen() {
    val context = viewModel<GameBoyEmulationViewModel>()

    val memoryStep = 15

    val column1Weight = .1f
    val column2Weight = .7f
    val column3Weight = .2f

    val memory by remember { context.memory }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        stickyHeader {
            Row(Modifier.background(Color.Gray)) {
                TableHeaderCell(text = "Address", weight = column1Weight)
                TableHeaderCell(text = "", weight = column2Weight)
            }
        }
        items(memory.memorySize / memoryStep) {index ->
            val baseAddress = index * memoryStep
            val memoryEntries = (0 until memoryStep).map { offset -> memory.read(baseAddress + offset) }
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = "0x%04x".format(baseAddress), weight = column1Weight)
                for (i in 0 until memoryStep) {
                    TableCell(text = "%02x".format(memoryEntries[i].value.toInt()), weight = column2Weight / memoryStep)
                }
            }
        }
    }
}