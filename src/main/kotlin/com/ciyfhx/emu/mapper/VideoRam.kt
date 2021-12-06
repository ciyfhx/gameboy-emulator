package com.ciyfhx.emu.mapper

import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.MemoryMapper

class VideoRam: MemoryMapper {

    private var videoRam: Array<Memory.MemoryEntry>

    init {
        videoRam = Memory.MemoryEntry.createArray(0x2000)
    }

    override fun read(address: Int): Memory.MemoryEntry {
        return videoRam[address]
    }

    override fun write(memoryEntry: Memory.MemoryEntry) {
        TODO("Not yet implemented")
    }
}