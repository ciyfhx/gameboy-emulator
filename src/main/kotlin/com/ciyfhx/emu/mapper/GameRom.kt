package com.ciyfhx.emu.mapper

import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.RawFileReader
import com.ciyfhx.emu.ReadOnlyMemoryMapper
import java.io.FileNotFoundException
import java.nio.file.Files
import kotlin.io.path.Path

class GameRom(
    val romLocation: String
) : ReadOnlyMemoryMapper {

    private val bootRom = BootRom()
    private var bootRomInit = false
    private lateinit var memory: Memory
    private lateinit var data: ByteArray

    override fun initMemory(memory: Memory) {
        this.memory = memory
        bootRom.initMemory(memory)
        data = readRom(romLocation)
        memory.copyByteArray(data, 0x0100, 0x7FFF)
    }

    override fun read(memoryEntryRead: Memory.MemoryEntry): Memory.MemoryEntry {
        val address = memoryEntryRead.address
        if (address == 0x0100) {
            bootRomInit = true
            //Copy the rest of the game rom that overlaps with the boot rom
            memory.copyByteArray(data, 0x0000, 0x0100)
        }
        return memoryEntryRead
    }
}

fun readRom(romLocation: String): ByteArray {
    val path = Path(romLocation)
    if (Files.exists(path))
        return RawFileReader.readRawFile(romLocation)!!
    else
        throw FileNotFoundException("Unable to find rom")
}