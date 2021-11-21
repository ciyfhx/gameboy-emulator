package com.ciyfhx.emu.mapper

import com.ciyfhx.emu.*
import com.ciyfhx.emu.opcodes.copyMemoryArray

class BootRom : ReadOnlyMemoryMapper {

    private val bootRomData: Array<Memory.MemoryEntry> = Memory.MemoryEntry.createArray(256)

    init {
        copyMemoryArray(0, readBootRom()!!, bootRomData)
    }

    override fun read(address: Int): UByte {
        return bootRomData[address].value
    }
}

fun readBootRom(): ByteArray? {
    return RawFileReader.readRawFile("bootrom.gb")
}
