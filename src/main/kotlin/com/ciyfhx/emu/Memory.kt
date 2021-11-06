package com.ciyfhx.emu

import com.ciyfhx.emu.opcodes.combineBytes

class Memory(
    val registers: Registers
) {

    private val memory = ByteArray(65536)

    fun copyByteArray(offset: UInt, data: ByteArray){
        System.arraycopy(data, 0, memory, offset.toInt(), data.size)
    }

    fun read(address: UInt): UByte {
        return memory[address.toInt()].toUByte()
    }

    fun write(address: UInt, value: UByte){
        memory[address.toInt()] = value.toByte()
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

}