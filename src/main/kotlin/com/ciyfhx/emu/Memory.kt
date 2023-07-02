package com.ciyfhx.emu

import com.ciyfhx.emu.opcodes.combineBytes
import com.ciyfhx.emu.opcodes.toHexCode
import java.util.*


interface MemoryMapper {
    fun initMemory(memory: Memory){}
    fun read(memoryEntryRead: Memory.MemoryEntry): Memory.MemoryEntry
    fun write(memoryEntryWrite: Memory.MemoryEntry): Memory.MemoryEntry
}

interface ReadOnlyMemoryMapper: MemoryMapper {
    override fun write(memoryEntryWrite: Memory.MemoryEntry): Memory.MemoryEntry {
//        throw ReadOnlyMemory()
        println("Tried to access read-only memory")
        return memoryEntryWrite
    }
}

data class MemoryRegion(
    val range: IntRange
): Comparable<MemoryRegion>{

    val start: Int = range.first
    val end: Int = range.last - 1

    override fun compareTo(other: MemoryRegion): Int {
        return start.compareTo(other.start)
    }
}

fun interface WriteListener {
    fun change(newValue: Memory.MemoryEntry)
}

open class Memory(
    val memorySize: Int,
    val registers: Registers
) {

    private val memory = MemoryEntry.createArray(memorySize)
    private val writeListeners = mutableListOf<WriteListener>()

    fun addWriteListener(listener: WriteListener){
        writeListeners += listener
    }
    fun removeWriteListener(listener: WriteListener) {
        writeListeners -= listener
    }
    private fun invokeChangeListener(writeValue: MemoryEntry){
        writeListeners.forEach { it.change(writeValue) }
    }


    private val memoryMappers = TreeMap<MemoryRegion, MemoryMapper>()

    fun registerMemoryMapper(mapper: MemoryMapper, region: MemoryRegion){
        memoryMappers[region] = mapper
        mapper.initMemory(this)
    }

    private fun getMemoryMapperByRegion(region: MemoryRegion): MemoryMapper{
        return memoryMappers.floorEntry(region).value
    }

    open fun read(address: Int): MemoryEntry {
        val readValue = memory[address]
        return getMemoryMapperByRegion(MemoryRegion(address..address)).read(readValue)
    }

    open fun write(address: Int, value: UByte){
        var writeValue = MemoryEntry(address, value)
        invokeChangeListener(writeValue)
        writeValue = getMemoryMapperByRegion(MemoryRegion(address..address)).write(writeValue)
        memory[writeValue.address] = writeValue
    }

    fun read(address: UInt): MemoryEntry {
        return read(address.toInt())
    }

    fun write(address: UInt, value: UByte){
        write(address.toInt(), value)
    }

    fun readNextByte(): UByte{
        val entry = read(registers.programCounter.toInt())
        registers.programCounter++
        return entry.value
    }

    fun readNextShort(): UShort{
        val lob = readNextByte()
        val hob = readNextByte()
        return combineBytes(hob, lob).toUShort()
    }

    fun copyByteArray(data: ByteArray, offset: Int = 0, length: Int = data.size): Int {
        var i = 0
        while (i < length) {
            val memoryEntry = memory[i + offset]
            memoryEntry.value = data[i].toUByte()
//            memory[i + offset] = MemoryEntry(i + offset, data[i].toUByte())
            i++
        }
        return i
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