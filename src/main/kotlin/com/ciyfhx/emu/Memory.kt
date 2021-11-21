package com.ciyfhx.emu

import com.ciyfhx.emu.opcodes.combineBytes
import com.ciyfhx.emu.opcodes.toHexCode
import java.util.*
import kotlin.Comparator


interface MemoryMapper {

    fun read(address: Int): UByte
    fun write(memoryEntry: Memory.MemoryEntry)
}

interface ReadOnlyMemoryMapper: MemoryMapper {
    override fun write(memoryEntry: Memory.MemoryEntry) {
        throw ReadOnlyMemory()
    }
}

data class MemoryRegion(
    val location: Int,
    val size: Int = 0
): Comparable<MemoryRegion>{
    override fun compareTo(other: MemoryRegion): Int {
        return location.compareTo(other.location)
    }
}

open class Memory(
    val registers: Registers
) {

    private val memoryMappers = TreeMap<MemoryRegion, MemoryMapper>()

    fun registerMemoryMapper(mapper: MemoryMapper, region: MemoryRegion){
        memoryMappers[region] = mapper
    }

//    private val memory = ByteArray(65536)
//    protected val memory: Array<MemoryEntry> = Array(65536){ MemoryEntry(it.toUInt(), 0u) }

//    fun copyByteArray(offset: Int, data: ByteArray){
////        System.arraycopy(data, 0, memory, offset, data.size)
////        for(i in data.indices){
////            memory[i + offset].value = data[i].toUByte()
////        }
//    }

    private fun getMemoryMapperByRegion(region: MemoryRegion): MemoryMapper{
        return memoryMappers.floorEntry(region).value
    }

    open fun read(address: Int): UByte {
//        return memory[address.toInt()].value
        return getMemoryMapperByRegion(MemoryRegion(address)).read(address)
    }

    open fun write(address: Int, value: UByte){
        getMemoryMapperByRegion(MemoryRegion(address)).write(MemoryEntry(address, value))
//        memory[address.toInt()].value = value
    }

    fun read(address: UInt): UByte {
        return read(address.toInt())
    }

    fun write(address: UInt, value: UByte){
        write(address.toInt(), value)
    }

    fun readNextByte(): UByte{
        val byte = read(registers.programCounter.toInt())
        registers.programCounter++
        return byte
    }

    fun readNextShort(): UShort{
        val lob = readNextByte()
        val hob = readNextByte()
        return combineBytes(hob, lob).toUShort()
    }

    class MemoryEntry(
        val address: Int,
        var value: UByte
    ){
        override fun toString() = "0x${address.toHexCode(4)}: ${value.toHexCode()}"
        companion object {
            fun createArray(size: Int, value: UByte = 0u): Array<MemoryEntry>{
                return Array(size){ MemoryEntry(it, value) }
            }
        }
    }

}

class ReadOnlyMemory : Exception()