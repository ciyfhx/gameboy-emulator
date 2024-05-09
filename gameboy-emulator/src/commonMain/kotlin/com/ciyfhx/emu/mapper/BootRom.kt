package com.ciyfhx.emu.mapper

import com.ciyfhx.emu.*

class BootRom : ReadOnlyMemoryMapper() {

    override fun initMemory(memory: Memory) {
        super.initMemory(memory)
        loadBootRom()
    }

    override fun read(memoryEntryRead: Memory.MemoryEntry): Memory.MemoryEntry {
        return memoryEntryRead
    }

    private fun loadBootRom() {
        memory.copyByteArray(readBootRom()!!, 0x0000)
    }
}

fun readBootRom(): ByteArray? {
    return RawFileReader.readRawFile("DMG_ROM.gb")
}
