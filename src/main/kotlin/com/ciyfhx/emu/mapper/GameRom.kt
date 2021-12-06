package com.ciyfhx.emu.mapper

import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.RawFileReader
import com.ciyfhx.emu.ReadOnlyMemoryMapper
import com.ciyfhx.emu.opcodes.copyMemoryArray
import java.io.FileNotFoundException
import java.nio.file.Files
import kotlin.io.path.Path

class GameRom(
    romLocation: String
): ReadOnlyMemoryMapper {

    private val bootRom = BootRom()
    private var romData: Array<Memory.MemoryEntry>
    private var bootRomInit = false

    init{
        val data = readRom(romLocation)
        romData = Memory.MemoryEntry.createArray(0x8000)
        copyMemoryArray(0, data, romData)
    }

    override fun read(address: Int): Memory.MemoryEntry {
        if(address == 0x0100){
            bootRomInit = true
        }
        return if(address < 0x0100 && !bootRomInit){
            bootRom.read(address)
        }else{
            romData[address]
        }
    }
}

fun readRom(romLocation: String): ByteArray {
    val path = Path(romLocation)
    if(Files.exists(path))
        return RawFileReader.readRawFile(romLocation)!!
    else
        throw FileNotFoundException("Unable to find rom")
}