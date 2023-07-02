package com.ciyfhx.emu.gpu

import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.MemoryMapper
import com.ciyfhx.emu.opcodes.getBit

/**
 * LCD Control register
 */
class LCDC: MemoryMapper {

    private var status: UByte = 0u

    override fun read(memoryEntryRead: Memory.MemoryEntry): Memory.MemoryEntry {
        return memoryEntryRead
    }

    override fun write(memoryEntryWrite: Memory.MemoryEntry): Memory.MemoryEntry {
        assert(memoryEntryWrite.address == 0xFF40)
        val value = memoryEntryWrite.value
        status = value
        return memoryEntryWrite
    }

    fun getBgWindowDisplayPriority(): Boolean {
        return status.getBit(0)
    }

    fun getObjDisplayEnable(): Boolean {
        return status.getBit(1)
    }

    fun getObjSize(): Boolean {
        return status.getBit(2)
    }

    fun getBgTileMapDisplaySelect(): Boolean {
        return status.getBit(3)
    }

    fun getBgAndWindowTileDataSelect(): Boolean {
        return status.getBit(4)
    }

    fun getWindowDisplayEnable(): Boolean {
        return status.getBit(5)
    }

    fun getWindowTileMapDisplaySelect(): Boolean {
        return status.getBit(6)
    }

    fun getLCDDisplayEnable(): Boolean {
        return status.getBit(7)
    }



}