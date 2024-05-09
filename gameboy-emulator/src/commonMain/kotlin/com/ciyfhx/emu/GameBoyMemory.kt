package com.ciyfhx.emu

import com.ciyfhx.emu.audio.Audio
import com.ciyfhx.emu.gpu.PPU
import com.ciyfhx.emu.mapper.*

class GameBoyMemory : Memory(0xFFFF) {

    init {
        val videoRam = VideoRam()
        //Define memory region
        registerMemoryMapper(
            GameRom("C:\\Users\\pehzi\\Downloads\\Tetris 2 (USA, Europe) (SGB Enhanced).gb"),
            MemoryRegion(0x0000..0x7FFF)
        )
        registerMemoryMapper(videoRam, MemoryRegion(0x8000..0x9FFF))
//        registerMemoryMapper(, MemoryRegion(0x8000))
        registerMemoryMapper(
            OAMRam(),
            MemoryRegion(0xFE00..0xFE9F)
        )
        registerMemoryMapper(Joypad(), MemoryRegion(0xFF00..0xFF00))
        registerMemoryMapper(Audio(), MemoryRegion(0xFF10..0xFF26))
        registerMemoryMapper(PPU(videoRam), MemoryRegion(0xFF40..0xFF4B))
        registerMemoryMapper(DMA(), MemoryRegion(0xFF46..0xFF46))
    }
}