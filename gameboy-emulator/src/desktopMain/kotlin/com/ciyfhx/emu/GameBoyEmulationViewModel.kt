package com.ciyfhx.emu

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.lifecycle.ViewModel
import com.ciyfhx.emu.gpu.LCD
import com.ciyfhx.emu.gpu.Tile
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

class GameBoyEmulationViewModel : ViewModel() {
    //TODO: Create wrapper class for creating components
    private val _memory = mutableStateOf(GameBoyMemory(), neverEqualPolicy())
    val memory: State<GameBoyMemory> = _memory
    val cpu = CPU(_memory.value)

    private val _registers = mutableStateOf(cpu.registers, neverEqualPolicy())
    val registers: State<Registers> = _registers

    private val _lcd = mutableStateOf(memory.value.lcd, neverEqualPolicy())
    val lcd: State<LCD> = _lcd

    val vramTiles: List<Tile> get() = memory.value.videoRam.tileData.toList().chunked(16).map {
        Tile(it.toByteArray())
    }

    private val logger: KLogger
        get() = KotlinLogging.logger {}

    fun start(){
        logger.info { "Starting Emulation!" }
        memory.value.apply {
            initCPU(this@GameBoyEmulationViewModel.cpu)
            addWriteListener{
                _memory.value = _memory.value //Trigger update
            }
        }
        cpu.registers.apply {
            addRegisterChangedListener{
                _registers.value = _registers.value
            }
        }
        lcd.value.addRefreshListener {
            _lcd.value = _lcd.value
        }
        _memory.value.start()
    }

    fun stop(){
        logger.info { "Stopping Emulation!" }
        _memory.value.stop()
    }


}
