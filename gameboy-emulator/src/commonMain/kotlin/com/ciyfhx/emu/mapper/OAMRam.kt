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

    private fun searchOAMSprite(){
        val oams = mutableListOf<OAMEntry>()
        //Find all sprites in OAM
        for(i in 0 until 40) {
            val spriteAddress = 0xFE00 + i * 4
            val entry = OAMEntry(
                memory.read(spriteAddress).value,
                memory.read(spriteAddress + 1).value,
                memory.read(spriteAddress + 2).value,
                memory.read(spriteAddress + 3).value
            )
            oams += entry
        }

//        //Filter OAMs by visible sprite
//        oams.filter {
//            it.x != 0 && it.y
//        }

    }

}

data class OAMEntry(
    var x: UByte,
    var y: UByte,
    var tileIndex: UByte,
    var flags: UByte
)