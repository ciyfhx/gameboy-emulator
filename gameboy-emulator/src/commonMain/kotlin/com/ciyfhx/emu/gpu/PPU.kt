package com.ciyfhx.emu.gpu

import com.ciyfhx.emu.Clock
import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.MemoryMapper
import com.ciyfhx.emu.mapper.VideoRam
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

class PPU(
    private val videoRam: VideoRam
) : MemoryMapper {


    private val logger: KLogger
        get() = KotlinLogging.logger {}

    private val lcdc: LCDC = LCDC() //LCD Control (R/W)
    private val stat: STAT = STAT() //LCDC Status (R/W)
    private var scy: UByte = 30u  //Scroll Y (R/W)
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

    private val fetcher = Fetcher(this)
    private val pixelFifo = PixelFIFO()

    val lcd = LCD()

    private var xClock = 0

    private var counter = 0
    val clock = Clock(1_048_576){

        when(stat.modeFlag()){
            STAT.ModeFlag.SEARCHING_OAM -> {
                oamSearch()
                if(xClock == 19) {
                    stat.transferMode()
                }
            }
            STAT.ModeFlag.TRANSFERRING_DATA -> {
                pixelTransfer()
                if(lcd.x == 159){
                    fetcher.clearPixelBuffer()
                    pixelFifo.reset()
                    stat.hBlankMode()
                }
            }
            STAT.ModeFlag.HBLANK -> {
                h_blank()
                if(xClock == 113){
                    if(ly.toInt()<=143)stat.oamMode()
                    else stat.vBlankMode()
                }
            }
            STAT.ModeFlag.VBLANK -> {
                v_blank()
                if(ly.toInt()==153) {
                    ly = 0u
                    stat.oamMode()
                }
            }
        }

        if(!fetcher.containsPixelBuffer())fetcher.readPixel()

        xClock++
        if(xClock == 114) {
            xClock = 0
            ly++
            if(ly.toInt() == 154)ly = 0u
        }
    }

    fun start(){
        logger.info { "Starting PPU!" }
        stat.oamMode()
        clock.start()
    }

    fun stop(){
        logger.info { "Stopping PPU!" }
        clock.stop()
    }

    private fun oamSearch(){

        //DO OAM HERE


    }

    private fun pixelTransfer(){
        if(pixelFifo.capacity() >= 8 && fetcher.containsPixelBuffer()) {
            val pixelData = fetcher.retrievePixelBuffer()
            for(pixel in pixelData){
                pixelFifo.push(pixel)
            }
        }

        if(!pixelFifo.isEmpty())lcd.push(pixelFifo.pop())



        //TESTING
//        try {
//            val pixelData = ByteArray(16)
//            memory.readByteArray(getTileDataAddress(0x02u), pixelData, 16)
//            val tile = Tile(pixelData)
//            println(tile)
//        }catch (e: Exception){
//            e.printStackTrace()
//        }
    }
    private fun h_blank(){
    }

    private fun v_blank(){
        fetcher.reset()
    }

    override fun initMemory(memory: Memory) {
        super.initMemory(memory)
        this.memory = memory
    }

    internal fun getTileDataAddress(indexes: UByte) =
        if(lcdc.getBgAndWindowTileDataSelect()){
            //0x8000 method
            val baseAddress = 0x8000
             baseAddress + (indexes.toInt() * 0x10)
        }else{
            //0x8800 method
            val baseAddress = 0x8800
            baseAddress + (indexes.toInt() * 0x10)
        }


    override fun read(memoryEntryRead: Memory.MemoryEntry): Memory.MemoryEntry {
        when(memoryEntryRead.address){
            0xFF44 -> memoryEntryRead.value = ly
            0xFF45 -> memoryEntryRead.value = lyc
            0xFF46 -> memoryEntryRead.value = stat.read(memoryEntryRead).value
            0xFF47 -> memoryEntryRead.value = bgp
            0xFF48 -> memoryEntryRead.value = obj1
            0xFF49 -> memoryEntryRead.value = obj2
        }
        return memoryEntryRead
    }

    override fun write(memoryEntryWrite: Memory.MemoryEntry): Memory.MemoryEntry {
        when(memoryEntryWrite.address){
            0xFF40 -> lcdc.write(memoryEntryWrite)
            0xFF41 -> stat.write(memoryEntryWrite)
//            0xFF42 -> scy = memoryEntryWrite.value
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

    private class PixelFIFO {
        private val MAX_SIZE = 16
        private var start = 0
        private var end = 0
        private var size = 0
        private val pixelData = Array(MAX_SIZE){ Pixel.PIXEL_0 }

        private val logger: KLogger
            get() = KotlinLogging.logger {}

        fun push(pixel: Pixel){
            if(isFull()) {
                val error = IllegalStateException("Unable to push more pixels because PPU FIFO is full!")
                logger.error(error) { "Unable to push more pixels because PPU FIFO is full!" }
                throw error
            }
            this.pixelData[end] = pixel
            end = (end + 1) % MAX_SIZE
            size++
        }

        fun pop(): Pixel{
            if(isEmpty()) {
                val error = IllegalStateException("Unable to pop more pixels because PPU FIFO is empty!")
                logger.error(error) { "Unable to pop more pixels because PPU FIFO is empty!" }
                throw error
            }
            val pixel = pixelData[start]
            start = (start + 1) % MAX_SIZE
            size--
            return pixel
        }

        fun reset(){
            start = 0
            end = 0
            size = 0
        }

        fun isEmpty() = size == 0
        fun isFull() = size == MAX_SIZE
        fun size() = size
        fun capacity() = MAX_SIZE - size

    }

    private class Fetcher(val ppu: PPU){

        private var xCounter = 0
        private val pixelBuffer = Array(8){ Pixel.PIXEL_0 }
        private var updatePixelBuffer = true

        private val logger: KLogger
            get() = KotlinLogging.logger {}

        fun readPixel() {
            if(!updatePixelBuffer) {
                val error = IllegalArgumentException("PPU Fetcher already contains a unread pixel buffer!")
                logger.error(error) { "PPU Fetcher already contains a unread pixel buffer!" }
                throw error
            }

            val xCoord = (ppu.scx.toInt() / 8 + xCounter) and 0x1F
            val yCoord = (ppu.ly + ppu.scy).toInt() and 0xFF

            val offsetAddress = 0x1F * (yCoord / 0x8) + xCoord

            val baseAddress = 0x9800
            val mapAddress =  baseAddress + offsetAddress
            val tileNo = ppu.memory.read(mapAddress)
            val tileAddress = ppu.getTileDataAddress(tileNo.value)
            val dataAddress = tileAddress + 2 * ((ppu.ly + ppu.scy).toInt() % 8)
            val d1 = ppu.memory.read(dataAddress).value
            val d2 = ppu.memory.read(dataAddress + 1).value

            for(index in pixelBuffer.indices){
                val i = (index % 8)
                val offset = 7 - i
                val mask =  0x1 shl offset
                val pixel = ((d1.toInt() and mask) shr offset) or (((d2.toInt() and mask) shr offset) shl 1)
                pixelBuffer[index] = Pixel.fromInt(pixel)
            }
            xCounter+=1
            updatePixelBuffer = false
        }

        fun retrievePixelBuffer(): Array<Pixel> {
            return pixelBuffer.copyOf().also {
                updatePixelBuffer = true
            }
        }

        fun containsPixelBuffer() = !updatePixelBuffer

        fun reset(){
//            xCounter = 0
            updatePixelBuffer = true
        }

        fun clearPixelBuffer() {
            updatePixelBuffer = true
        }

    }

}
enum class Pixel(val value: Int) {
    PIXEL_0(0), //00
    PIXEL_1(1), //01
    PIXEL_2(2), //10
    PIXEL_3(3); //11

    override fun toString(): String {
        return value.toString()
    }

    companion object {
        fun fromInt(value: Int) = Pixel.values().first { it.value == value }
    }
}
data class Tile(
    val data: ByteArray
){

    private val logger: KLogger = KotlinLogging.logger {}

    init {
        if(data.size != 16){
            val error = IllegalArgumentException("Tile must be in 16 bytes")
            logger.error(error) { "Tile must be in 16 bytes" }
            throw error
        }
    }

    fun pixelAt(x: Int, y: Int): Pixel {
        val index = y * 8 + x
        return pixelAt(index)
    }

    fun pixelAt(index: Int): Pixel{
        if(index > 72 || index < 0){
            val error = IllegalArgumentException("Out of Range")
            logger.error(error) { "Out of Range" }
            throw error
        }

        val d1 = data[(index / 8) * 2]
        val d2 = data[(index / 8) * 2 + 1]
        val i = (index % 8)
        val offset = 7 - i
        val mask =  0x1 shl offset
        val pixel = ((d1.toInt() and mask) shr offset) or (((d2.toInt() and mask) shr offset) shl 1)
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

    override fun toString() = buildString{
        for (y in 0 until 8){
            for (x in 0 until 8){
                append(pixelAt(x,y))
            }
            append('\n')
        }
    }

}



fun main(){
    val hexString = "3C7E4242424242427E5E7E0A7C56387C"
//    val hexString = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"
    val byteArray = hexString.chunked(2) { it.toString().toInt(16).toByte() }.toByteArray()
    val test = Tile(byteArray)
    println(test)
//    println(test.pixelAt(7))
//    println(test.pixelAt(6))
}