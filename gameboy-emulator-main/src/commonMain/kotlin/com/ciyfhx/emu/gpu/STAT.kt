package com.ciyfhx.emu.gpu

import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.MemoryMapper
import com.ciyfhx.emu.opcodes.getBit
import com.ciyfhx.emu.opcodes.setBit

/**
 * LCD Status register
 */
class STAT: MemoryMapper {

    private var status: UByte = 0u

    override fun read(memoryEntryRead: Memory.MemoryEntry): Memory.MemoryEntry {
        memoryEntryRead.value = status
        return memoryEntryRead
    }

    override fun write(memoryEntryWrite: Memory.MemoryEntry): Memory.MemoryEntry {
        assert(memoryEntryWrite.address == 0xFF41)
        val value = memoryEntryWrite.value
        status = value
        return memoryEntryWrite
    }

    fun setLycLyCoincidenceInterruptEnable(enable: Boolean) {
        status = status.setBit(6, enable)
    }

    fun isLycLyCoincidenceInterruptEnable(): Boolean {
        return status.getBit(6)
    }

    fun setMode2OamInterruptEnable(enable: Boolean) {
        status = status.setBit(5, enable)
    }

    fun isMode2OAMInterruptEnable(): Boolean {
        return status.getBit(5)
    }

    fun setMode1VBlankInterruptEnable(enable: Boolean) {
        status = status.setBit(4, enable)
    }

    fun isMode1VBlankInterruptEnable(): Boolean {
        return status.getBit(4)
    }

    fun setMode0HBlankInterruptEnable(enable: Boolean) {
        status = status.setBit(3, enable)
    }

    fun isMode0HBlankInterruptEnable(): Boolean {
        return status.getBit(3)
    }

    fun isCoincidenceFlagSet(): Boolean {
        return status.getBit(2)
    }

    fun oamMode(){
        status = (status and 0b00u) or 0b10u
    }
    fun vBlankMode(){
        status = (status and 0b00u) or 0b01u
    }

    fun hBlankMode(){
        status = (status and 0b00u)
    }

    fun transferMode(){
        status = (status or 0b11u)
    }


    fun modeFlag(): ModeFlag {
        (status and 0b11u).toInt().let {
            return when (it) {
                0 -> ModeFlag.HBLANK
                1 -> ModeFlag.VBLANK
                2 -> ModeFlag.SEARCHING_OAM
                3 -> ModeFlag.TRANSFERRING_DATA
                else -> throw IllegalStateException("Invalid mode: $it")
            }
        }
    }


    enum class ModeFlag {
        HBLANK,
        VBLANK,
        SEARCHING_OAM,
        TRANSFERRING_DATA
    }

}