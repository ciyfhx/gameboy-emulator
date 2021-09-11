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

class LD_BC : Opcode(0x01) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.B = memory.readNextByte().toInt()
        registers.C = memory.readNextByte().toInt()
    }
}

class LD_BC_A : Opcode(0x02) {
    override fun execute(memory: Memory, registers: Registers) {
        val address = registers.getBC()
        memory.write(address, registers.accumulator.toByte())
    }
}
class INC_BC : Opcode(0x03) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.getBC()
        val data = value + 1
        registers.setBC(data)
    }
}

class INC_B : Opcode(0x04) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.B
        val data = value + 1
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0F == 0x0F)
        registers.B = data
    }
}

class DEC_B : Opcode(0x05) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.B
        val data = value - 1
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0F == 0x0F)
        registers.B = data
    }
}

class LD_B : Opcode(0x06) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.B = memory.readNextByte().toInt()
    }
}

class RLCA : Opcode(0x07) {
    override fun execute(memory: Memory, registers: Registers) {
        val carry = registers.accumulator and 0b0000_0001
        registers.accumulator = registers.accumulator shr 1
        registers.accumulator = registers.accumulator or (carry shl 7)
        registers.setZeroFlag(false)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(false)
        registers.setCarryFlag(carry == 0b0000_0001)
    }
}