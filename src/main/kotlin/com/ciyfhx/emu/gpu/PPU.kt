package com.ciyfhx.emu.gpu

import com.ciyfhx.emu.Clock
import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.MemoryMapper
import com.ciyfhx.emu.mapper.VideoRam
import com.ciyfhx.emu.opcodes.getBit

class PPU(
    private val videoRam: VideoRam
) : MemoryMapper {

//    private val videoRam: VideoRam
    private val lcdc: LCDC = LCDC() //LCD Control (R/W)
    private val stat: STAT = STAT() //LCDC Status (R/W)
    private var scy: UByte = 0u  //Scroll Y (R/W)
    private var scx: UByte = 0u  //Scroll X (R/W)
    private var ly: UByte = 0u  //LCDC Y-Coordinate (R)
    private var lyc: UByte = 0u //LY Compare (R/W)
    private var dma: UByte = 0u //DMA Transfer and Start
    private var bgp: UByte = 0u //BG Palette (R/W)
    private var obj1: UByte = 0u //Object Palette 0 (R/W)
    private var obj2: UByte = 0u //Object Palette 1 (R/W)
    private var wy: UByte = 0u //Window Y Position (R/W)
    private var wx: UByte = 0u //Window X Position (R/W)

    private lateinit var memory: Memory

    val clock = Clock(60){

    }

    override fun initMemory(memory: Memory) {
        super.initMemory(memory)
        this.memory = memory
    }

    private fun readTile(indexes: Byte): Tile{
        if(lcdc.getBgAndWindowTileDataSelect()){
            //0x8000 method
            val baseAddress = 0x8000
            val address = baseAddress + (indexes.toUByte().toInt() * 8)
            val pixelData = ByteArray(16)
            memory.readByteArray(address, pixelData, 16)
            return Tile(pixelData)
        }else{
            //0x8800 method
            val baseAddress = 0x8800
            val address = baseAddress + (indexes.toInt() * 8)
            val pixelData = ByteArray(16)
            memory.readByteArray(address, pixelData, 16)
            return Tile(pixelData)
        }
    }

    override fun read(memoryEntryRead: Memory.MemoryEntry): Memory.MemoryEntry {
        TODO("Not yet implemented")
    }

    override fun write(memoryEntryWrite: Memory.MemoryEntry): Memory.MemoryEntry {
        when(memoryEntryWrite.address){
            0xFF40 -> lcdc.write(memoryEntryWrite)
            0xFF41 -> stat.write(memoryEntryWrite)
            0xFF42 -> scy = memoryEntryWrite.value
            0xFF43 -> scx = memoryEntryWrite.value
            0xFF44 -> ly =  memoryEntryWrite.value
            0xFF45 -> lyc = memoryEntryWrite.value
            0xFF46 -> dma = memoryEntryWrite.value
            0xFF47 -> bgp = memoryEntryWrite.value
            0xFF48 -> obj1 = memoryEntryWrite.value
            0xFF49 -> obj2 = memoryEntryWrite.value
            0xFF4A -> wy = memoryEntryWrite.value
            0xFF4B -> wx = memoryEntryWrite.value
            else -> throw IllegalArgumentException("Unknown memory entry ${memoryEntryWrite.address}")
        }
        return memoryEntryWrite
    }
}
enum class Pixel(val value: Int) {
    PIXEL_0(0), //00
    PIXEL_1(1), //01
    PIXEL_2(2), //10
    PIXEL_3(3); //11

    companion object {
        fun fromInt(value: Int) = Pixel.values().first { it.value == value }
    }
}
data class Tile(
    val data: ByteArray
){
    init {
        assert(data.size == 16) { "Tile must be in 16 bytes" }
    }

    fun pixelAt(x: Int, y: Int): Pixel {
        val index = y * 8 + x
        return pixelAt(index)
    }

    fun pixelAt(index: Int): Pixel{
        assert(index > 72 || index < 0) { "Out of Range" }

        val d1 = data[(index / 8) * 2]
        val d2 = data[(index / 8) * 2 + 1]
        val i = (index % 8)
        val offset = 7 - i
        val mask =  0x1 shl offset
        val pixel = ((d1.toInt() and mask) shr offset) or ((d2.toInt() and mask) shr offset-1)
        return Pixel.fromInt(pixel)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tile

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}
fun main(){
    val hexString = "3C7E4242424242427E5E7E0A7C56387C"
    val byteArray = hexString.chunked(2) { it.toString().toInt(16).toByte() }.toByteArray()
    val test = Tile(byteArray)
    println(test.pixelAt(2,4))
    for (y in 0 until 8){
        for (x in 0 until 8){
            print(test.pixelAt(x,y))
        }
        println()
    }
}