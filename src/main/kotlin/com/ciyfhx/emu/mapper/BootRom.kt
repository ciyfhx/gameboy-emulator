package com.ciyfhx.emu.mapper

import com.ciyfhx.emu.*

class BootRom : ReadOnlyMemoryMapper {

    private lateinit var memory: Memory

    override fun initMemory(memory: Memory) {
        this.memory = memory
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
    return RawFileReader.readRawFile("bootrom.gb")
}
