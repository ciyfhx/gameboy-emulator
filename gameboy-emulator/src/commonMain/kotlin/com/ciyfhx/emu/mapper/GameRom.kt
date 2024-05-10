package com.ciyfhx.emu.mapper

import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.RawFileReader
import com.ciyfhx.emu.ReadOnlyMemoryMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.FileNotFoundException
import java.nio.file.Files
import kotlin.io.path.Path

class GameRom(
    val romLocation: String
) : ReadOnlyMemoryMapper() {

    private val bootRom = BootRom()
    private var bootRomEnabled = true
    private var overrideBootRomWithGameRom = false
    private lateinit var data: ByteArray

    private val logger = KotlinLogging.logger {}

    override fun initMemory(memory: Memory) {
        super.initMemory(memory)
        bootRom.initMemory(memory)
        data = readRom(romLocation)
        memory.copyByteArray(data, 0x0100, data.size - 0x0101, 0x0100)//0x7FFF
    }

    override fun read(memoryEntryRead: Memory.MemoryEntry): Memory.MemoryEntry {

        val address = memoryEntryRead.address
        if(address < 0x0100){
            val bootRomEnabledUpdated = checkBootRomEnabled()
            if(bootRomEnabledUpdated == bootRomEnabled) return memoryEntryRead // NO Change

            //Update the ROM location
            if(bootRomEnabledUpdated){
                memory.copyByteArray(data, 0x0100, data.size)//0x7FFF
            }else{
                if(!overrideBootRomWithGameRom) {
                    overrideBootRomWithGameRom = true
                    logger.debug { "Boot ROM disabled" }
                    //Copy the rest of the game rom that overlaps with the boot rom
                    memory.copyByteArray(data, 0x0000, 0x0100)
                }
            }
        }
        return memoryEntryRead
    }

    private fun checkBootRomEnabled(): Boolean{
        val bootRomMemoryEntry = memory.read(0xFF50).value
        return bootRomMemoryEntry == 0.toUByte()
    }
}

fun readRom(romLocation: String): ByteArray {
    val path = Path(romLocation)
    if (Files.exists(path))
        return RawFileReader.readRawFileFromPath(romLocation)
    else
        throw FileNotFoundException("Unable to find rom")
}