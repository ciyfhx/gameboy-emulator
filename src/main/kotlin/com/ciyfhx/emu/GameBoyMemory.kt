package com.ciyfhx.emu

import com.ciyfhx.emu.mapper.GameRom

class GameBoyMemory(registers: Registers) : Memory(registers) {

    init {
        //Define memory region
        registerMemoryMapper(GameRom("C:\\Users\\pehzi\\Downloads\\bgb\\Tetris 2 (USA, Europe) (SGB Enhanced).gb"), MemoryRegion(0))
//        registerMemoryMapper(, MemoryRegion(0x8000))
    }

}