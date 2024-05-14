package com.ciyfhx.emu.audio

import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.MemoryMapper
import com.ciyfhx.emu.opcodes.toHexCode
import io.github.oshai.kotlinlogging.KotlinLogging

class Audio: MemoryMapper {
    private val logger = KotlinLogging.logger {}
    override fun read(memoryEntryRead: Memory.MemoryEntry): Memory.MemoryEntry {
        TODO("Not yet implemented")
    }

    override fun write(memoryEntryWrite: Memory.MemoryEntry): Memory.MemoryEntry {
//        TODO("Not yet implemented")
        logger.error { "Trying to write to audio memory 0x${memoryEntryWrite.address.toHexCode(4)} which is not implemented" }
        return memoryEntryWrite
    }
}