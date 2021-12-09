package com.ciyfhx.emu

import com.ciyfhx.emu.mapper.GameRom
import com.ciyfhx.emu.mapper.VideoRam

class GameBoyMemory(registers: Registers) : Memory(0xFFFF, registers) {

    init {
        //Define memory region
        registerMemoryMapper(
            GameRom("C:\\Users\\pehzi\\Downloads\\bgb\\Tetris 2 (USA, Europe) (SGB Enhanced).gb"),
            MemoryRegion(0x0000..0x7FFF)
        )
        registerMemoryMapper(VideoRam(), MemoryRegion(0x8000..0x9FFF))
//        registerMemoryMapper(, MemoryRegion(0x8000))
    }

}