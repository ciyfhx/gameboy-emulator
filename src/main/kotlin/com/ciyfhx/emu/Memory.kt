package com.ciyfhx.emu

import com.ciyfhx.emu.opcodes.combineBytes
import com.ciyfhx.emu.opcodes.toHexCode

open class Memory(
    val registers: Registers
) {

//    private val memory = ByteArray(65536)
    protected val memory: Array<MemoryEntry> = Array(65536){ MemoryEntry(it.toUInt(), 0u) }

    fun copyByteArray(offset: Int, data: ByteArray){
//        System.arraycopy(data, 0, memory, offset, data.size)
        for(i in data.indices){
            memory[i + offset].value = data[i].toUByte()
        }
    }

    open fun read(address: UInt): UByte {
        return memory[address.toInt()].value
    }

    open fun write(address: UInt, value: UByte){
        memory[address.toInt()].value = value
    }

    fun readNextByte(): UByte{
        val byte = read(registers.programCounter)
        registers.programCounter++
        return byte
    }

    fun readNextShort(): UShort{
        val lob = readNextByte()
        val hob = readNextByte()
        return combineBytes(hob, lob).toUShort()
    }

    class MemoryEntry(
        val address: UInt,
        var value: UByte
    ){
        override fun toString() = "0x${address.toHexCode(4)}: ${value.toHexCode()}"
    }

}