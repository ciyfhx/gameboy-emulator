package com.ciyfhx.emu.mapper

import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.MemoryMapper

class OAMRam: MemoryMapper {

    private lateinit var memory: Memory

    override fun initMemory(memory: Memory) {
        this.memory = memory
    }

    override fun read(memoryEntryRead: Memory.MemoryEntry): Memory.MemoryEntry {
        return memoryEntryRead
    }

    override fun write(memoryEntryWrite: Memory.MemoryEntry): Memory.MemoryEntry {
        return memoryEntryWrite
    }

}

data class OAMEntry(
    var x: UByte,
    var y: UByte,
    var tileIndex: UByte,
    var flags: UByte
)