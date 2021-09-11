package com.ciyfhx.emu.opcodes

import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.Registers

abstract class Opcode(
    val opcode: Short
) {
    abstract fun execute(memory: Memory, registers: Registers)
}

class NOP : Opcode(0x00) {
    override fun execute(memory: Memory, registers: Registers) {}
}

class LD1 : Opcode(0x01) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.B = memory.readNextByte().toInt()
        registers.C = memory.readNextByte().toInt()
    }
}

class LD2 : Opcode(0x02) {
    override fun execute(memory: Memory, registers: Registers) {
        val address = registers.getBC()
        memory.write(address, registers.accumulator.toByte())
    }
}
class INC : Opcode(0x03) {
    override fun execute(memory: Memory, registers: Registers) {
        var data = registers.getBC()
        data++
        registers.setBC(data)
    }
}

