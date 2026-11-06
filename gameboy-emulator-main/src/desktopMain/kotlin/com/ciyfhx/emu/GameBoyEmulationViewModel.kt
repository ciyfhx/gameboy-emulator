package com.ciyfhx.emu

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.lifecycle.ViewModel
import com.ciyfhx.emu.gpu.LCD
import com.ciyfhx.emu.gpu.Tile
import com.ciyfhx.emu.mapper.Joypad.JoypadEvent
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

data class ViewPort(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
)

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

    val backgroundTiles: List<Tile> get() = memory.value.videoRam.tileMap1.toList().map {
        vramTiles[it.toInt()]
    }

    val viewPort: ViewPort get() = memory.value.ppu.let {
        val scyValue =  it.scxValue.toInt()
        val scxValue =  it.scxValue.toInt()
        ViewPort(scxValue, scyValue,
            scxValue + 160, scyValue + 144)
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

    fun onKeyEvent(keyEvent: KeyEvent): Boolean {
        logger.debug { "Key event: ${keyEvent.key} ${keyEvent.type}" }
        val button = when (keyEvent.key) {
            Key.DirectionUp -> JoypadEvent.UP
            Key.DirectionDown -> JoypadEvent.DOWN
            Key.DirectionLeft -> JoypadEvent.LEFT
            Key.DirectionRight -> JoypadEvent.RIGHT
            Key.Z -> JoypadEvent.A
            Key.X -> JoypadEvent.B
            Key.Enter -> JoypadEvent.START
            Key.Backspace -> JoypadEvent.SELECT
            else -> return false
        }
        when (keyEvent.type) {
            KeyEventType.KeyDown -> memory.value.joypad.setButton(button, true)
            KeyEventType.KeyUp -> memory.value.joypad.setButton(button, false)
            else -> return false
        }
        return true
    }


}
