package com.ciyfhx.emu.mapper

import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.MemoryMapper
import java.util.concurrent.CopyOnWriteArraySet

class Joypad : MemoryMapper {

    enum class JoypadEvent(val line: Int, val isDirection: Boolean) {
        A(0, false),
        B(1, false),
        SELECT(2, false),
        START(3, false),
        RIGHT(0, true),
        LEFT(1, true),
        UP(2, true),
        DOWN(3, true),
    }

    //unused
    @Volatile private var P15 = false
    @Volatile private var P14 = false

    private val pressed = CopyOnWriteArraySet<JoypadEvent>()

    fun setButton(button: JoypadEvent, isPressed: Boolean) {
        if (isPressed) pressed.add(button) else pressed.remove(button)
    }

    override fun read(memoryEntryRead: Memory.MemoryEntry): Memory.MemoryEntry {
        memoryEntryRead.value = p1Register()
        return memoryEntryRead
    }

    override fun write(memoryEntryWrite: Memory.MemoryEntry): Memory.MemoryEntry {
        val value = memoryEntryWrite.value.toInt()
        P15 = (value and 0x20) == 0
        P14 = (value and 0x10) == 0
        return memoryEntryWrite
    }

    private fun p1Register(): UByte {
        var register = 0xFF
        if (P15) register = register and 0x20.inv()
        if (P14) register = register and 0x10.inv()
        for (button in pressed) {
            val selected = if (button.isDirection) P14 else P15
            if (selected) register = register and (1 shl button.line).inv()
        }
        return register.toUByte()
    }

}
