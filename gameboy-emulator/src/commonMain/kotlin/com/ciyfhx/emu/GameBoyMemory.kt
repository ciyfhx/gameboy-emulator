package com.ciyfhx.emu

import com.ciyfhx.emu.audio.Audio
import com.ciyfhx.emu.gpu.LCD
import com.ciyfhx.emu.gpu.PPU
import com.ciyfhx.emu.mapper.*

class GameBoyMemory : Memory(0xFFFF) {

    private val videoRam: VideoRam
    private val ppu: PPU

    val lcd: LCD
        get() = ppu.lcd

    init {
        videoRam = VideoRam()
        ppu = PPU(videoRam)
        //Define memory region
        registerMemoryMapper(
            GameRom("C:\\Users\\pehzi\\Downloads\\bgb\\bgbtest.gb"),
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

        registerMemoryMapper(ppu, MemoryRegion(0xFF40..0xFF4B))
        registerMemoryMapper(DefaultMemoryMapper(), MemoryRegion(0xFF80..0xFFFE))//HRAM
//        registerMemoryMapper(DMA(), MemoryRegion(0xFF46..0xFF46))
    }


    fun start(){
        cpu.start()
        ppu.start()
    }

    fun stop(){
        cpu.stop()
        ppu.stop()
    }

}