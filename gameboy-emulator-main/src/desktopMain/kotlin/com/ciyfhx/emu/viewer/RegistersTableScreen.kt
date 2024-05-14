package com.ciyfhx.emu.viewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ciyfhx.emu.GameBoyEmulationViewModel


@Preview
@Composable
fun RegistersTableScreen() {
    val context = viewModel<GameBoyEmulationViewModel>()
    val registers by remember { context.registers }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.background(Color.Gray)) {
            TableHeaderCell(text = "Registers", weight = 1f)
        }
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()) {
            Row(Modifier.background(Color.LightGray).fillMaxWidth()) {
                TableCell(text = "A", weight = .5f)
                Row(modifier = Modifier.weight(.5f)) {
                    TableCell(text = "Zero", weight = .2f)
                    TableCell(text = "Subtract", weight = .2f)
                    TableCell(text = "H-Carry", weight = .2f)
                    TableCell(text = "Carry", weight = .2f)
                }
            }
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = "%02x".format(registers.accumulator.toInt()), weight = .5f)
                Row(modifier = Modifier.weight(.5f)) {
                    TableCell(text = if(registers.getZeroFlag()) "1" else "0", weight = .5f)
                    TableCell(text = if(registers.getSubtractFlag()) "1" else "0", weight = .5f)
                    TableCell(text = if(registers.getHalfCarryFlag()) "1" else "0", weight = .5f)
                    TableCell(text = if(registers.getCarryFlag()) "1" else "0", weight = .5f)
                }
            }
            Row(Modifier.background(Color.LightGray).fillMaxWidth()) {
                TableCell(text = "B", weight = .5f)
                TableCell(text = "C", weight = .5f)
            }
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = "%02x".format(registers.B.toInt()), weight = .5f)
                TableCell(text = "%02x".format(registers.C.toInt()), weight = .5f)
            }
            Row(Modifier.background(Color.LightGray).fillMaxWidth()) {
                TableCell(text = "D", weight = .5f)
                TableCell(text = "E", weight = .5f)
            }
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = "%02x".format(registers.D.toInt()), weight = .5f)
                TableCell(text = "%02x".format(registers.E.toInt()), weight = .5f)
            }
            Row(Modifier.background(Color.LightGray).fillMaxWidth()) {
                TableCell(text = "H", weight = .5f)
                TableCell(text = "L", weight = .5f)
            }
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = "%02x".format(registers.H.toInt()), weight = .5f)
                TableCell(text = "%02x".format(registers.L.toInt()), weight = .5f)
            }
            Row(Modifier.background(Color.LightGray).fillMaxWidth()) {
                TableCell(text = "SP", weight = .5f)
            }
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = "%04x".format(registers.stackPointer.toInt()), weight = .5f)
            }
            Row(Modifier.background(Color.LightGray).fillMaxWidth()) {
                TableCell(text = "PC", weight = .5f)
            }
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = "%04x".format(registers.programCounter.toInt()), weight = .5f)
            }
        }
    }


}
