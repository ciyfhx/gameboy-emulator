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

class LD_BC_D16 : Opcode(0x01) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.C = memory.readNextByte().toInt()
        registers.B = memory.readNextByte().toInt()
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
        val data = value + 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0F == 0x0F)
        registers.B = data
    }
}

class DEC_B : Opcode(0x05) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.B
        val data = value - 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0F == 0x00)
        registers.B = data
    }
}

class LD_B_D8 : Opcode(0x06) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.B = memory.readNextByte().toInt()
    }
}

class RLCA : Opcode(0x07) {
    override fun execute(memory: Memory, registers: Registers) {
        val carry = registers.accumulator and 0b1000_0000
        registers.accumulator = registers.accumulator shl 1
        registers.accumulator = registers.accumulator or (carry shr 7)
        registers.setZeroFlag(false)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(false)
        registers.setCarryFlag(carry == 0b1000_0000)
    }
}

class LD_A16_SP : Opcode(0x08) {
    override fun execute(memory: Memory, registers: Registers) {
        val lsb = memory.readNextByte()
        val hsb = memory.readNextByte()
        registers.stackPointer = combineBytes(hsb, lsb)
    }
}

class ADD_HL_BC : Opcode(0x09) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.getBC() + registers.getHL()
        val carry = value and 0b0000_0001_0000_0000 == 0b0000_0001_0000_0000
        registers.setBC(value)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(carry)
        registers.setCarryFlag(carry)
    }
}

class LD_A_BC : Opcode(0x0A) {
    override fun execute(memory: Memory, registers: Registers) {
        val address = registers.getBC()
        registers.accumulator = memory.read(address).toInt()
    }
}

class DEC_BC : Opcode(0x0B) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.setBC(registers.getBC() - 1)
    }
}

class INC_C : Opcode(0x0C) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.C
        val data = value + 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0F == 0x0F)
        registers.C = data
    }
}

class DEC_C : Opcode(0x0D) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.C
        val data = value - 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0F == 0x00)
        registers.C = data
    }
}

class LD_C_D8 : Opcode(0x0E) {
    override fun execute(memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.C = data.toInt()
    }
}

class RRCA : Opcode(0x0F) {
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

class STOP : Opcode(0x10) {
    override fun execute(memory: Memory, registers: Registers) {
        assert(memory.readNextByte().toInt() == 0x00)
        //TODO("Check for STOP conditions")
    }
}

class LD_DE_D16 : Opcode(0x11) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.E = memory.readNextByte().toInt()
        registers.D = memory.readNextByte().toInt()
    }
}