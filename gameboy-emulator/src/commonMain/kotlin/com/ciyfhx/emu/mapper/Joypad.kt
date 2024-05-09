package com.ciyfhx.emu.mapper

import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.ReadOnlyMemoryMapper

class Joypad : ReadOnlyMemoryMapper() {


    private var P15 = false // Select Buttons
    private var P14 = false // Select D-Pad

    private var P13 = false // Start or Down
    private var P12 = false // Select or Up
    private var P11 = false // B or Left
    private var P10 = false // A or Right


    override fun read(memoryEntryRead: Memory.MemoryEntry): Memory.MemoryEntry {
        memoryEntryRead.value = p1Register()
        return memoryEntryRead
    }

    private fun p1Register(): UByte{
        return (
                if(P15) 0x20 else 0 or
                if(P14) 0x10 else 0 or

                if(P13) 0x08 else 0 or
                if(P12) 0x04 else 0 or
                if(P11) 0x02 else 0 or
                if(P10) 0x01 else 0
                ).toUByte().inv()
    }

}