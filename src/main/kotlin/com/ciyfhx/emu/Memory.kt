package com.ciyfhx.emu

class Memory(
    val registers: Registers
) {

    private val memory = ByteArray(1000)

    fun read(address: Int): Byte {
        return memory[address]
    }

    fun write(address: Int, value: Byte){
        memory[address] = value
    }

    fun readNextByte(): Byte{
        val byte = read(registers.programCounter)
        registers.programCounter++
        return byte
    }

    fun readNextShort(): Int{
        val byte1 = readNextByte()
        val byte2 = readNextByte()
        return byte1.toInt() shl 8 or byte2.toInt()
    }

}