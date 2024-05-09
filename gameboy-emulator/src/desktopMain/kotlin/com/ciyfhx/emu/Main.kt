package com.ciyfhx.emu

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ciyfhx.emu.opcodes.toHexCode
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging


class ViewModelStoreOwner(override val viewModelStore: ViewModelStore = ViewModelStore()) : ViewModelStoreOwner
@Composable
fun ViewModelStoreOwnerProvider(content: @Composable () -> Unit) {
    val viewModelStoreOwner = remember { ViewModelStoreOwner() }
    CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
        content()
    }
}

class EmuContext : ViewModel() {
    //TODO: Create wrapper class for creating components
    private val _memory = mutableStateOf(GameBoyMemory(), neverEqualPolicy())
    val memory: State<GameBoyMemory> = _memory
    val cpu = CPU(_memory.value)

    private val _registers = mutableStateOf(cpu.registers, neverEqualPolicy())
    val registers: State<Registers> = _registers

    private val logger: KLogger
        get() = KotlinLogging.logger {}

     fun start(){
         logger.info { "Starting Emulation!" }
         memory.value.apply {
             initCPU(this@EmuContext.cpu)
             addWriteListener{
                 _memory.value = _memory.value //Trigger update
             }
         }
         cpu.registers.apply {
             addRegisterChangedListener{
                 _registers.value = _registers.value
             }
         }
         cpu.start()
     }
}

fun main() {
    val app = application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "GameBoy Emulator",
            state = rememberWindowState(width = 1000.dp, height = 800.dp)
        ) {
            MaterialTheme {
                ViewModelStoreOwnerProvider{
                    val context: EmuContext = viewModel()
                    Row{
                        Column(modifier = Modifier.weight(.35f)) {
                            Button(onClick = {
                                Thread {
                                    context.start()
                                }.start()
                            }){
                                Text("Start")
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

@Preview
@Composable
fun RamTableScreen() {
    val context = viewModel<EmuContext>()

    val memoryStep = 15

    val column1Weight = .1f
    val column2Weight = .7f
    val column3Weight = .2f

    val memory by remember { context.memory }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        item {
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


@Composable
fun RegistersTableScreen() {
    val context = viewModel<EmuContext>()
    val registers by remember { context.registers }
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

@Composable
fun StackViewer(){
    val context = viewModel<EmuContext>()
    Column {
        Text(text = "Stack Values:", modifier = Modifier.padding(8.dp))

        val memory by remember { context.memory }

        // Display stack values with memory addresses using LazyColumn
        val sp = context.cpu.registers.stackPointer.toInt()
        val stackEntries = (0xFF80..0xFFFE).reversed().map {
            memory.read(it)
        }

        LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
            item {
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