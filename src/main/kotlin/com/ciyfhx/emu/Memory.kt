package com.ciyfhx.emu

import com.ciyfhx.emu.opcodes.combineBytes
import com.ciyfhx.emu.opcodes.toHexCode
import java.util.*


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

fun interface WriteListener {
    fun change(newValue: Memory.MemoryEntry)
}

open class Memory(
    val registers: Registers
) {

    private val writeListeners = mutableListOf<WriteListener>()

    fun addWriteListener(listener: WriteListener){
        writeListeners += listener
    }
    fun removeWriteListener(listener: WriteListener) {
        writeListeners -= listener
    }
    private fun invokeChangeListener(writeValue: Memory.MemoryEntry){
        writeListeners.forEach { it.change(writeValue) }
    }


    private val memoryMappers = TreeMap<MemoryRegion, MemoryMapper>()

    fun registerMemoryMapper(mapper: MemoryMapper, region: MemoryRegion){
        memoryMappers[region] = mapper
    }

    private fun getMemoryMapperByRegion(region: MemoryRegion): MemoryMapper{
        return memoryMappers.floorEntry(region).value
    }

    open fun read(address: Int): UByte {
        return getMemoryMapperByRegion(MemoryRegion(address)).read(address)
    }

    open fun write(address: Int, value: UByte){
        val writeValue = MemoryEntry(address, value)
        invokeChangeListener(writeValue)
        getMemoryMapperByRegion(MemoryRegion(address)).write(writeValue)
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