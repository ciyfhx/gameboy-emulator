package com.ciyfhx.emu.mapper

import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.MemoryMapper
import com.ciyfhx.emu.opcodes.toHexCode
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.math.abs

class VideoRam : MemoryMapper {

    private val logger = KotlinLogging.logger {}

    var tileData: ByteArray = ByteArray(abs(0x8000-0x9800)) // VRAM Tile Data for 0x8000-0x97FF
    var tileMap1: ByteArray = ByteArray(abs(0x9800-0x9C00)) // VRAM 32x32 Tile Map 1 for 0x9800-0x9BFF
    var tileMap2: ByteArray = ByteArray(abs(0x9C00-0xA000)) // VRAM 32x32 Tile Map 2 for 0x9C00-0x9FFF


    override fun read(memoryEntryRead: Memory.MemoryEntry): Memory.MemoryEntry {
        when(memoryEntryRead.address){
            in 0x8000..0x97FF -> memoryEntryRead.value = tileData[memoryEntryRead.address - 0x8000].toUByte()
            in 0x9800..0x9BFF -> memoryEntryRead.value = tileMap1[memoryEntryRead.address - 0x9800].toUByte()
            in 0x9C00..0x9FFF -> memoryEntryRead.value = tileMap2[memoryEntryRead.address - 0x9C00].toUByte()
            else ->
                logger.error(IllegalArgumentException("unknown address 0x${memoryEntryRead.address.toHexCode(4)}")){
                    "Unable to read address 0x${memoryEntryRead.address.toHexCode(4)}"
                }
        }
        return memoryEntryRead
    }

    override fun write(memoryEntryWrite: Memory.MemoryEntry): Memory.MemoryEntry {
        when(memoryEntryWrite.address){
            in 0x8000..0x97FF -> tileData[memoryEntryWrite.address - 0x8000] = memoryEntryWrite.value.toByte()
            in 0x9800..0x9BFF -> tileMap1[memoryEntryWrite.address - 0x9800] = memoryEntryWrite.value.toByte()
            in 0x9C00..0x9FFF -> tileMap2[memoryEntryWrite.address - 0x9C00] = memoryEntryWrite.value.toByte()
            else ->
                logger.error(IllegalArgumentException("unknown address 0x${memoryEntryWrite.address.toHexCode(4)}")){
                    "Unable to read address 0x${memoryEntryWrite.address.toHexCode(4)}"
                }
        }
        return memoryEntryWrite
    }

}