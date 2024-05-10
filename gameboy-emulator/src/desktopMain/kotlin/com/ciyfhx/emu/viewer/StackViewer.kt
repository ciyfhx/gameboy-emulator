package com.ciyfhx.emu.viewer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ciyfhx.emu.GameBoyEmulationViewModel
import com.ciyfhx.emu.opcodes.toHexCode

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StackViewer(){
    val context = viewModel<GameBoyEmulationViewModel>()
    Column {
        Text(text = "Stack Values:", modifier = Modifier.padding(8.dp))

        val memory by remember { context.memory }

        // Display stack values with memory addresses using LazyColumn
        val sp = context.cpu.registers.stackPointer.toInt()
        val stackEntries = (0xFF80..0xFFFE).reversed().map {
            memory.read(it)
        }


        LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
            stickyHeader  {
                Row(Modifier.background(Color.Gray)) {
                    TableHeaderCell(text = "Address", weight = 0.2f)
                    TableHeaderCell(text = "Text", weight = 0.3f)
                }
            }
            itemsIndexed(stackEntries) { _, entry ->
                Row(Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(0.2f)
                        .border(.5.dp, Color.Black)
                        .padding(5.dp)) {
                        Row {
                            if (sp == entry.address) {
                                Icon(
                                    painter = painterResource("arrow.png"),
                                    contentDescription = "SP",
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                            Text(
                                text = "0x${entry.address.toHexCode(4)}",
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                    TableCell(text = "0x${entry.value.toHexCode()}", weight = .3f)
                }
            }
        }
    }
}