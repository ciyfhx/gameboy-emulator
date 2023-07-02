package com.ciyfhx.emu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

class EmuContext {
    val registers = Registers()
    val memory = GameBoyMemory(registers)
    val cpu = CPU(memory, registers)

     fun start(){
         cpu.start()
     }
}

fun main() {
    val context = EmuContext()
    val app = application {

        Thread {
            context.start()
        }.start()

        Window(
            onCloseRequest = ::exitApplication,
            title = "GameBoy Emulator",
            state = rememberWindowState(width = 800.dp, height = 500.dp)
        ) {
            MaterialTheme {
                Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
                    Row(Modifier.weight(.7f)) {
                        RamTableScreen(context.memory)
                    }
                    Row(Modifier.weight(.3f)) {
                        RegistersTableScreen(context.registers)
                    }
                }
            }
        }
    }
}


@Composable
fun RowScope.TableHeaderCell(
    text: String,
    weight: Float,
) {
    Text(
        text = text,
        Modifier
            .border(.5.dp, Color.Black)
            .weight(weight)
            .padding(5.dp)
    )
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
) {
    Text(
        text = text,
        fontSize = 15.sp,
        modifier = Modifier
            .border(.5.dp, Color.Black)
            .weight(weight)
            .padding(5.dp)
    )
}

@Composable
fun RamTableScreen(memory: Memory) {
    val memoryStep = 15

    val column1Weight = .1f
    val column2Weight = .7f
    val column3Weight = .2f

    var invalidations by remember{
        mutableStateOf(false)
    }

    memory.addWriteListener{
        invalidations = !invalidations
    }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Row(Modifier.background(Color.Gray)) {
                TableHeaderCell(text = "Address", weight = column1Weight)
                TableHeaderCell(text = "", weight = column2Weight)
                TableHeaderCell(text = "Text", weight = column3Weight)
            }
        }
        invalidations.let {
            items(memory.memorySize / memoryStep) {index ->
                val baseAddress = index * memoryStep
                val memoryEntries = (0 until memoryStep).map { offset -> memory.read(baseAddress + offset) }
                Row(Modifier.fillMaxWidth()) {
                    TableCell(text = "0x%04x".format(baseAddress), weight = column1Weight)
                    for (i in 0 until memoryStep) {
                        TableCell(text = "%02x".format(memoryEntries[i].value.toInt()), weight = column2Weight / memoryStep)
                    }
                    TableCell(text = "", weight = column3Weight)
                }
            }
        }
    }
}


@Composable
fun RegistersTableScreen(registers: Registers) {
    var invalidations by remember{
        mutableStateOf(false)
    }

    registers.addRegisterChangedListener{
        invalidations = !invalidations
    }

    invalidations.let {
        Column(Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize().padding(16.dp)) {
            Row(Modifier.background(Color.Gray)) {
                TableHeaderCell(text = "Registers", weight = 1f)
            }
            Row(Modifier.background(Color.LightGray).fillMaxWidth()) {
                TableCell(text = "A", weight = .5f)
                TableCell(text = "F", weight = .5f)
            }
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = "%02x".format(registers.accumulator.toInt()), weight = .5f)
//                TableCell(text = "%02x".format(registers.flag.), weight = .5f)
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
