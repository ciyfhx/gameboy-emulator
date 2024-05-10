package com.ciyfhx.emu.opcodes

import com.ciyfhx.emu.CPU
import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.Registers
import com.ciyfhx.emu.opcodes.CALL_A16.logger
import io.github.oshai.kotlinlogging.KotlinLogging

abstract class Opcode(
    val opcode: Short
) {
    abstract fun execute(cpu: CPU, memory: Memory, registers: Registers)
}

object NOP : Opcode(0x00) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {}
}

object LD_BC_D16 : Opcode(0x01) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.C = memory.readNextByte().toUInt()
        registers.B = memory.readNextByte().toUInt()
    }
}

object LD_P_BC_A : Opcode(0x02) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val address = registers.getBC()
        memory.write(address, registers.accumulator.getLob())
    }
}

object INC_BC : Opcode(0x03) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.getBC()
        val data = value + 1u
        registers.setBC(data)
    }
}

object INC_B : Opcode(0x04) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.B
        val data = value + 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x0Fu)
        registers.B = data
    }
}

object DEC_B : Opcode(0x05) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.B
        val data = value - 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x00u)
        registers.B = data
    }
}

object LD_B_D8 : Opcode(0x06) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.B = memory.readNextByte().toUInt()
    }
}

object RLCA : Opcode(0x07) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val carry = registers.accumulator and 0b1000_0000u
        registers.accumulator = registers.accumulator shl 1
        registers.accumulator = registers.accumulator or (carry shr 7)
        registers.setZeroFlag(false)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(false)
        registers.setCarryFlag(carry == 0b1000_0000u)
    }
}

object LD_P_A16_SP : Opcode(0x08) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val lsb = memory.readNextByte()
        val hsb = memory.readNextByte()
        registers.stackPointer = combineBytes(hsb, lsb)
    }
}

object ADD_HL_BC : Opcode(0x09) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.getBC() + registers.getHL()
        val carry = value and 0b0000_0001_0000_0000u == 0b0000_0001_0000_0000u
        registers.setBC(value)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(carry)
        registers.setCarryFlag(carry)
    }
}

object LD_A_P_BC : Opcode(0x0A) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val address = registers.getBC()
        registers.accumulator = memory.read(address).value.toUInt()
    }
}

object DEC_BC : Opcode(0x0B) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.setBC(registers.getBC() - 1u)
    }
}

object INC_C : Opcode(0x0C) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.C
        val data = value + 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x0Fu)
        registers.C = data
    }
}

object DEC_C : Opcode(0x0D) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.C
        val data = value - 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x00u)
        registers.C = data
    }
}

object LD_C_D8 : Opcode(0x0E) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.C = data.toUInt()
    }
}

object RRCA : Opcode(0x0F) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val carry = registers.accumulator and 0b0000_0001u
        registers.accumulator = registers.accumulator shr 1
        registers.accumulator = registers.accumulator or (carry shl 7)
        registers.setZeroFlag(false)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(false)
        registers.setCarryFlag(carry == 0b0000_0001u)
    }
}

object STOP : Opcode(0x10) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        assert(memory.readNextByte().toInt() == 0x00)
        //TODO("Check for STOP conditions")
    }
}

object LD_P_DE_D16 : Opcode(0x11) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.E = memory.readNextByte().toUInt()
        registers.D = memory.readNextByte().toUInt()
    }
}

object LD_DE_A : Opcode(0x12) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val address = registers.getDE()
        memory.write(address, registers.accumulator.getLob())
    }
}

object INC_DE : Opcode(0x13) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.getDE()
        val data = value + 1u
        registers.setDE(data)
    }
}

object INC_D : Opcode(0x14) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.D
        val data = value + 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x0Fu)
        registers.D = data
    }
}

object DEC_D : Opcode(0x15) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.D
        val data = value - 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x00u)
        registers.D = data
    }
}

object LD_D_D8 : Opcode(0x16) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.D = memory.readNextByte().toUInt()
    }
}

object RLA : Opcode(0x17) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val bit = cpu.registers.accumulator.getBit(7)
        val carry = registers.getCarryFlag().toByte()

        registers.accumulator = registers.accumulator shl 1
        registers.accumulator = registers.accumulator or carry.toUInt()
        registers.setZeroFlag(registers.accumulator == 0u)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(false)
        cpu.registers.setCarryFlag(bit)
    }
}

object JR_S8 : Opcode(0x18) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val offset = memory.readNextByte().toByte().toInt()
        registers.programCounter = (registers.programCounter.toInt() + offset).toUInt()
    }
}

object ADD_HL_DE : Opcode(0x19) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.getHL() + registers.getDE()
        val carry = value and 0b0000_0001_0000_0000u == 0b0000_0001_0000_0000u
        registers.setHL(value)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(carry)
        registers.setCarryFlag(carry)
    }
}

object LD_A_P_DE : Opcode(0x1A) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = memory.read(registers.getDE()).value.toUInt()
    }
}

object DEC_DE : Opcode(0x1B) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.setDE(registers.getDE() - 1u)
    }
}

object INC_E : Opcode(0x1C) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.E
        val data = value + 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x0Fu)
        registers.E = data
    }
}

object DEC_E : Opcode(0x1D) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.E
        val data = value - 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x00u)
        registers.E = data
    }
}

object LD_E_D8 : Opcode(0x1E) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.E = data.toUInt()
    }
}

object RRA : Opcode(0x1F) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val carry = if(registers.getCarryFlag()) 1u else 0u
        val a8 = registers.accumulator or 0b0000_0001u
        registers.accumulator = registers.accumulator shr 1
        registers.accumulator = registers.accumulator or (carry shl 7)
        registers.setZeroFlag(false)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(false)
        registers.setCarryFlag(a8 == 1u)
    }
}

object JR_NZ_S8 : Opcode(0x20) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val offset = memory.readNextByte().toByte()
        if(!registers.getZeroFlag()){
            registers.programCounter = (registers.programCounter.toInt() + offset).toUInt()
        }
    }
}

object LD_HL_D16 : Opcode(0x21) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.L = memory.readNextByte().toUInt()
        registers.H = memory.readNextByte().toUInt()
    }
}

object LD_P_HL_PLUS_A : Opcode(0x22) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.accumulator
        registers.setHL(registers.getHL() + 1u)
        memory.write(registers.getHL(), value.getLob())
    }
}

object INC_HL : Opcode(0x23) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.getHL()
        val data = value + 1u
        registers.setHL(data)
    }
}

object INC_H : Opcode(0x24) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.H
        val data = value + 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x0Fu)
        registers.H = data
    }
}

object DEC_H : Opcode(0x25) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.H
        val data = value - 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x00u)
        registers.H = data
    }
}

object LD_H_D8 : Opcode(0x26) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.H = memory.readNextByte().toUInt()
    }
}

object DAA : Opcode(0x27) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        var data = registers.accumulator
        var correction = 0u
        if(registers.getHalfCarryFlag() ||
            (!registers.getSubtractFlag() && (data and 0x0Fu) > 0x09u)){
            correction = correction or 0x06u
        }
        if(registers.getCarryFlag() ||
            (!registers.getSubtractFlag() && (data and 0xFFu) > 0x99u)){
            correction = correction or 0x60u
            registers.setCarryFlag(true)
        }
        if(registers.getSubtractFlag()){
            data -= correction
        }else{
            data += correction
        }
        val carry = data and 0b0000_0001_0000_0000u == 0b0000_0001_0000_0000u
        registers.accumulator = data
        registers.setZeroFlag(data == 0u)
        registers.setHalfCarryFlag(false)
        registers.setCarryFlag(carry)
    }
}

object JR_Z_S8 : Opcode(0x28) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val offset = memory.readNextByte().toInt()
        if(registers.getZeroFlag()){
            registers.programCounter = (registers.programCounter.toInt() + offset).toUInt()
        }
    }
}

object ADD_HL_HL : Opcode(0x29) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.getHL() + registers.getHL()
        val carry = value and 0b0000_0001_0000_0000u == 0b0000_0001_0000_0000u
        registers.setHL(value)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(carry)
        registers.setCarryFlag(carry)
    }
}

object LD_A_P_HL_PLUS : Opcode(0x2A) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.setHL(registers.getHL() + 1u)
        registers.accumulator = memory.read(registers.getHL()).value.toUInt()
    }
}

object DEC_HL : Opcode(0x2B) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.setHL(registers.getHL() - 1u)
    }
}

object INC_L : Opcode(0x2C) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.L
        val data = value + 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x0Fu)
        registers.L = data
    }
}


object DEC_L : Opcode(0x2D) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.L
        val data = value - 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x00u)
        registers.L = data
    }
}

object LD_L_D8 : Opcode(0x2E) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.L = data.toUInt()
    }
}

object CPL : Opcode(0x2F) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = registers.accumulator.inv()
    }
}

object JR_NC_S8 : Opcode(0x30) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val offset = memory.readNextByte().toInt()
        if(!registers.getCarryFlag()){
            registers.programCounter = (registers.programCounter.toInt() + offset).toUInt()
        }
    }
}

object LD_SP_D16 : Opcode(0x31) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.stackPointer = memory.readNextByte().toUInt()
        registers.stackPointer = registers.stackPointer or (memory.readNextByte().toUInt() shl 8)
    }
}

object LD_P_HL_MINUS_A : Opcode(0x32) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.accumulator
        memory.write(registers.getHL(), value.getLob())
        registers.setHL(registers.getHL() - 1u)
    }
}

object INC_SP : Opcode(0x33) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.getHL()
        val data = value + 1u
        registers.stackPointer = data
    }
}

object INC_P_HL : Opcode(0x34) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val pointer = registers.getHL()
        val value = memory.read(pointer).value.toUInt()
        val data = value + 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x0Fu)
        memory.write(pointer, data.getLob())
    }
}

object DEC_P_HL : Opcode(0x35) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val pointer = registers.getHL()
        val value = memory.read(pointer).value.toUInt()
        val data = value - 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x00u)
        memory.write(pointer, data.getLob())
    }
}
object LD_P_HL_D8 : Opcode(0x36) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        val pointer = registers.getHL()
        memory.write(pointer, data)
    }
}

object SCF : Opcode(0x37) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(false)
        registers.setCarryFlag(true)
    }
}

object JR_C_S8 : Opcode(0x38) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val offset = memory.readNextByte().toInt()
        if(registers.getCarryFlag()){
            registers.programCounter = (registers.programCounter.toInt() + offset).toUInt()
        }
    }
}

object ADD_HL_SP : Opcode(0x39) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.getHL() + registers.stackPointer
        val carry = value and 0b0000_0001_0000_0000u == 0b0000_0001_0000_0000u
        registers.setHL(value)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(carry)
        registers.setCarryFlag(carry)
    }
}

object LD_A_HL_MINUS : Opcode(0x3A) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.setHL(registers.getHL() - 1u)
        registers.accumulator = memory.read(registers.getHL()).value.toUInt()
    }
}

object DEC_SP : Opcode(0x3B) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.stackPointer--
    }
}

object INC_A : Opcode(0x3C) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.accumulator
        val data = value + 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x0Fu)
        registers.accumulator = data
    }
}

object DEC_A : Opcode(0x3D) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val value = registers.accumulator
        val data = value - 1u and 0xFFu
        registers.setZeroFlag(data == 0u)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0Fu == 0x00u)
        registers.accumulator = data
    }
}

object LD_A_D8 : Opcode(0x3E) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.accumulator = data.toUInt()
    }
}

object CCF : Opcode(0x3F) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(false)
        registers.setCarryFlag(!registers.getCarryFlag())
    }
}

object LD_B_B : Opcode(0x40) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.B = registers.B
    }
}

object LD_B_C : Opcode(0x41) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.B = registers.C
    }
}

object LD_B_D : Opcode(0x42) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.B = registers.D
    }
}

object LD_B_E : Opcode(0x43) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.B = registers.E
    }
}

object LD_B_H : Opcode(0x44) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.B = registers.H
    }
}

object LD_B_L : Opcode(0x45) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.B = registers.L
    }
}

object LD_B_P_HL : Opcode(0x46) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.B = data.value.toUInt()
    }
}

object LD_B_A : Opcode(0x47) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.B = registers.accumulator
    }
}

object LD_C_B : Opcode(0x48) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.C = registers.B
    }
}

object LD_C_C : Opcode(0x49) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.C = registers.C
    }
}

object LD_C_D : Opcode(0x4A) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.C = registers.D
    }
}

object LD_C_E : Opcode(0x4B) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.C = registers.E
    }
}

object LD_C_H : Opcode(0x4C) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.C = registers.H
    }
}

object LD_C_L : Opcode(0x4D) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.C = registers.L
    }
}

object LD_C_P_HL : Opcode(0x4E) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.C = data.value.toUInt()
    }
}

object LD_C_A : Opcode(0x4F) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.C = registers.accumulator
    }
}

object LD_D_B : Opcode(0x50) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.D = registers.B
    }
}

object LD_D_C : Opcode(0x51) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.D = registers.C
    }
}

object LD_D_D : Opcode(0x52) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.D = registers.D
    }
}

object LD_D_E : Opcode(0x53) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.D = registers.E
    }
}

object LD_D_H : Opcode(0x54) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.D = registers.H
    }
}

object LD_D_L : Opcode(0x55) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.D = registers.L
    }
}

object LD_D_P_HL : Opcode(0x56) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.D = data.value.toUInt()
    }
}

object LD_D_A : Opcode(0x57) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.D = registers.accumulator
    }
}

object LD_E_B : Opcode(0x58) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.E = registers.B
    }
}

object LD_E_C : Opcode(0x59) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.E = registers.C
    }
}

object LD_E_D : Opcode(0x5A) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.E = registers.D
    }
}

object LD_E_E : Opcode(0x5B) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.E = registers.E
    }
}

object LD_E_H : Opcode(0x5C) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.E = registers.H
    }
}

object LD_E_L : Opcode(0x5D) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.E = registers.L
    }
}

object LD_E_P_HL : Opcode(0x5E) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.E = data.value.toUInt()
    }
}

object LD_E_A : Opcode(0x5F) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.E = registers.accumulator
    }
}

object LD_H_B : Opcode(0x60) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.H = registers.B
    }
}

object LD_H_C : Opcode(0x61) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.H = registers.C
    }
}

object LD_H_D : Opcode(0x62) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.H = registers.D
    }
}

object LD_H_E : Opcode(0x63) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.H = registers.E
    }
}

object LD_H_H : Opcode(0x64) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.H = registers.H
    }
}

object LD_H_L : Opcode(0x65) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.H = registers.L
    }
}

object LD_H_P_HL : Opcode(0x66) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.setHL(data.value.toUInt())
    }
}

object LD_H_A : Opcode(0x67) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.H = registers.accumulator
    }
}

object LD_L_B : Opcode(0x68) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.L = registers.B
    }
}

object LD_L_C : Opcode(0x69) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.L = registers.C
    }
}

object LD_L_D : Opcode(0x6A) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.L = registers.D
    }
}

object LD_L_E : Opcode(0x6B) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.L = registers.E
    }
}

object LD_L_H : Opcode(0x6C) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.L = registers.H
    }
}

object LD_L_L : Opcode(0x6D) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.L = registers.L
    }
}

object LD_L_P_HL : Opcode(0x6E) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.L = data.value.toUInt()
    }
}

object LD_L_A : Opcode(0x6F) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.L = registers.accumulator
    }
}

object LD_P_HL_B : Opcode(0x70) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        memory.write(registers.getHL(), registers.B.getLob())
    }
}

object LD_P_HL_C : Opcode(0x71) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        memory.write(registers.getHL(), registers.C.getLob())
    }
}

object LD_P_HL_D : Opcode(0x72) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        memory.write(registers.getHL(), registers.D.getLob())
    }
}

object LD_P_HL_E : Opcode(0x73) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        memory.write(registers.getHL(), registers.E.getLob())
    }
}
object LD_P_HL_H : Opcode(0x74) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        memory.write(registers.getHL(), registers.H.getLob())
    }
}

object LD_P_HL_L : Opcode(0x75) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        memory.write(registers.getHL(), registers.L.getLob())
    }
}

object HALT : Opcode(0x76) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        cpu.stop()
    }
}

object LD_P_HL_A : Opcode(0x77) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        memory.write(registers.getHL(), registers.accumulator.getLob())
    }
}

object LD_A_B : Opcode(0x78) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = registers.B
    }
}

object LD_A_C : Opcode(0x79) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = registers.C
    }
}

object LD_A_D : Opcode(0x7A) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = registers.D
    }
}

object LD_A_E : Opcode(0x7B) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = registers.E
    }
}

object LD_A_H : Opcode(0x7C) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = registers.H
    }
}

object LD_A_L : Opcode(0x7D) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = registers.L
    }
}

object LD_A_P_HL : Opcode(0x7E) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.accumulator = data.value.toUInt()
    }
}

object LD_A_A : Opcode(0x7F) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = registers.accumulator
    }
}

object ADD_A_B : Opcode(0x80) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.add(registers, registers.accumulator, registers.B)
    }
}

object ADD_A_C : Opcode(0x81) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.add(registers, registers.accumulator, registers.C)
    }
}

object ADD_A_D : Opcode(0x82) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.add(registers, registers.accumulator, registers.D)
    }
}

object ADD_A_E : Opcode(0x83) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.add(registers, registers.accumulator, registers.E)
    }
}

object ADD_A_H : Opcode(0x84) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.add(registers, registers.accumulator, registers.H)
    }
}

object ADD_A_L : Opcode(0x85) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.add(registers, registers.accumulator, registers.L)
    }
}

object ADD_A_P_HL : Opcode(0x86) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.accumulator = ALU.add(registers, registers.accumulator, data.value.toUInt())
    }
}

object ADD_A_A : Opcode(0x87) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.add(registers, registers.accumulator, registers.accumulator)
    }
}

object ADC_A_B : Opcode(0x88) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.adc(registers, registers.accumulator, registers.B)
    }
}

object ADC_A_C : Opcode(0x89) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.adc(registers, registers.accumulator, registers.C)
    }
}

object ADC_A_D : Opcode(0x8A) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.adc(registers, registers.accumulator, registers.D)
    }
}

object ADC_A_E : Opcode(0x8B) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.adc(registers, registers.accumulator, registers.E)
    }
}

object ADC_A_H : Opcode(0x8C) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.adc(registers, registers.accumulator, registers.H)
    }
}

object ADC_A_L : Opcode(0x8D) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.adc(registers, registers.accumulator, registers.D)
    }
}

object ADC_A_P_HL : Opcode(0x8E) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.accumulator = ALU.adc(registers, registers.accumulator, data.value.toUInt())
    }
}

object ADC_A_A : Opcode(0x8F) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.adc(registers, registers.accumulator, registers.accumulator)
    }
}

object SUB_B : Opcode(0x90) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.sub(registers, registers.accumulator, registers.B)
    }
}

object SUB_C : Opcode(0x91) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.sub(registers, registers.accumulator, registers.C)
    }
}

object SUB_D : Opcode(0x92) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.sub(registers, registers.accumulator, registers.D)
    }
}

object SUB_E : Opcode(0x93) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.sub(registers, registers.accumulator, registers.E)
    }
}

object SUB_H : Opcode(0x94) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.sub(registers, registers.accumulator, registers.H)
    }
}

object SUB_L : Opcode(0x95) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.sub(registers, registers.accumulator, registers.L)
    }
}

object SUB_P_HL : Opcode(0x96) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.accumulator = ALU.sub(registers, registers.accumulator, data.value.toUInt())
    }
}

object SUB_A : Opcode(0x97) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.sub(registers, registers.accumulator, registers.accumulator)
    }
}

object SBC_A_B : Opcode(0x98) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.sbc(registers, registers.accumulator, registers.B)
    }
}

object SBC_A_C : Opcode(0x99) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.sbc(registers, registers.accumulator, registers.C)
    }
}

object SBC_A_D : Opcode(0x9A) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.sbc(registers, registers.accumulator, registers.D)
    }
}

object SBC_A_E : Opcode(0x9B) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.sbc(registers, registers.accumulator, registers.E)
    }
}

object SBC_A_H : Opcode(0x9C) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.sbc(registers, registers.accumulator, registers.H)
    }
}

object SBC_A_L : Opcode(0x9D) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.sbc(registers, registers.accumulator, registers.L)
    }
}

object SBC_A_P_HL : Opcode(0x9E) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.accumulator = ALU.sbc(registers, registers.accumulator, data.value.toUInt())
    }
}

object SBC_A_A : Opcode(0x9F) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.sbc(registers, registers.accumulator, registers.accumulator)
    }
}

object AND_B : Opcode(0xA0) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.and(registers, registers.B, registers.accumulator)
    }
}

object AND_C : Opcode(0xA1) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.and(registers, registers.C, registers.accumulator)
    }
}

object AND_D : Opcode(0xA2) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.and(registers, registers.D, registers.accumulator)
    }
}

object AND_E : Opcode(0xA3) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.and(registers, registers.E, registers.accumulator)
    }
}

object AND_H : Opcode(0xA4) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.and(registers, registers.H, registers.accumulator)
    }
}

object AND_L : Opcode(0xA5) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.and(registers, registers.L, registers.accumulator)
    }
}

object AND_P_HL : Opcode(0xA6) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.accumulator = ALU.and(registers, data.value.toUInt(), registers.accumulator)
    }
}

object AND_A : Opcode(0xA7) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.and(registers, registers.accumulator, registers.accumulator)
    }
}

object XOR_B : Opcode(0xA8) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.xor(registers, registers.B, registers.accumulator)
    }
}

object XOR_C : Opcode(0xA9) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.xor(registers, registers.C, registers.accumulator)
    }
}

object XOR_D : Opcode(0xAA) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.xor(registers, registers.D, registers.accumulator)
    }
}

object XOR_E : Opcode(0xAB) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.xor(registers, registers.E, registers.accumulator)
    }
}

object XOR_H : Opcode(0xAC) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.xor(registers, registers.H, registers.accumulator)
    }
}

object XOR_L : Opcode(0xAD) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.xor(registers, registers.L, registers.accumulator)
    }
}

object XOR_P_HL : Opcode(0xAE) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.accumulator = ALU.xor(registers, data.value.toUInt(), registers.accumulator)
    }
}

object XOR_A : Opcode(0xAF) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.xor(registers, registers.accumulator, registers.accumulator)
    }
}

object OR_B : Opcode(0xB0) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.or(registers, registers.B, registers.accumulator)
    }
}

object OR_C : Opcode(0xB1) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.or(registers, registers.C, registers.accumulator)
    }
}

object OR_D: Opcode(0xB2) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.or(registers, registers.D, registers.accumulator)
    }
}

object OR_E: Opcode(0xB3) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.or(registers, registers.E, registers.accumulator)
    }
}

object OR_H: Opcode(0xB4) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.or(registers, registers.H, registers.accumulator)
    }
}

object OR_L: Opcode(0xB5) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.or(registers, registers.L, registers.accumulator)
    }
}

object OR_P_HL: Opcode(0xB6) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.accumulator = ALU.or(registers, data.value.toUInt(), registers.accumulator)
    }
}

object OR_A: Opcode(0xB7) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.accumulator = ALU.or(registers, registers.accumulator, registers.accumulator)
    }
}

object CP_B: Opcode(0xB8) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        ALU.sub(registers, registers.accumulator, registers.B)
    }
}

object CP_C: Opcode(0xB9) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        ALU.sub(registers, registers.accumulator, registers.C)
    }
}

object CP_D: Opcode(0xBA) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        ALU.sub(registers, registers.accumulator, registers.D)
    }
}

object CP_E: Opcode(0xBB) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        ALU.sub(registers, registers.accumulator, registers.E)
    }
}

object CP_H: Opcode(0xBC) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        ALU.sub(registers, registers.accumulator, registers.H)
    }
}

object CP_L: Opcode(0xBD) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        ALU.sub(registers, registers.accumulator, registers.L)
    }
}

object CP_P_HL: Opcode(0xBE) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        ALU.sub(registers, registers.accumulator, data.value.toUInt())
    }
}

object CP_A: Opcode(0xBF) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        ALU.sub(registers, registers.accumulator, registers.accumulator)
    }
}

object RET_NZ: Opcode(0xC0) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        if(!registers.getZeroFlag()){
            val lob = memory.read(registers.stackPointer++)
            val hob = memory.read(registers.stackPointer++)
            registers.programCounter = combineBytes(hob.value, lob.value)
        }
    }
}

object POP_BC: Opcode(0xC1) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.C = memory.read(registers.stackPointer++).value.toUInt()
        registers.B = memory.read(registers.stackPointer++).value.toUInt()
    }
}

object JP_NZ_A16: Opcode(0xC2) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        if(!registers.getZeroFlag()){
            registers.programCounter = memory.readNextShort().toUInt()
        }
    }
}

object JP_A16: Opcode(0xC3) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.programCounter = memory.readNextShort().toUInt()
    }
}

object CALL_NZ_A16: Opcode(0xC4) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val address = memory.readNextShort()
        if(!registers.getZeroFlag()){
            registers.stackPointer--
            memory.write(registers.stackPointer--, registers.programCounter.getHob())
            memory.write(registers.stackPointer, registers.programCounter.getLob())
            registers.programCounter = address.toUInt()
        }
    }
}

object PUSH_BC: Opcode(0xC5) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.stackPointer--
        memory.write(registers.stackPointer--, registers.B.getLob())
        memory.write(registers.stackPointer, registers.C.getLob())
    }
}

object ADD_A_D8: Opcode(0xC6) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.accumulator = ALU.add(registers, data.toUInt(), registers.accumulator)
    }
}

object RST_0: Opcode(0xC7) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.stackPointer--
        memory.write(registers.stackPointer--, registers.programCounter.getHob())
        memory.write(registers.stackPointer, registers.programCounter.getLob())
        registers.programCounter = 0u
    }
}

object RET_Z: Opcode(0xC8) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        if(registers.getZeroFlag()){
            val lob = memory.read(registers.stackPointer++)
            val hob = memory.read(registers.stackPointer++)
            registers.programCounter = combineBytes(hob.value, lob.value)
        }
    }
}

object RET: Opcode(0xC9) {
    private val logger
        get() = KotlinLogging.logger {}
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val lob = memory.read(registers.stackPointer++)
        val hob = memory.read(registers.stackPointer++)
        registers.programCounter = combineBytes(hob.value, lob.value)
        logger.debug { "Returning from function to 0x${registers.programCounter.toInt().toHexCode(4)}" }
    }
}

object JP_Z_A16: Opcode(0xCA) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        if(registers.getZeroFlag()){
            registers.programCounter = memory.readNextShort().toUInt()
        }
    }
}

object CALL_Z_A16: Opcode(0xCC) {
    private val logger
        get() = KotlinLogging.logger {}
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val address = memory.readNextShort()
        if(registers.getZeroFlag()){
            registers.stackPointer--
            memory.write(registers.stackPointer--, registers.programCounter.getHob())
            memory.write(registers.stackPointer, registers.programCounter.getLob())
            registers.programCounter = address.toUInt()
            logger.debug { "Calling function at 0x${address.toInt().toHexCode(4)}" }
        }
    }
}

object CALL_A16: Opcode(0xCD) {
    private val logger
        get() = KotlinLogging.logger {}
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val address = memory.readNextShort()
        registers.stackPointer--
        memory.write(registers.stackPointer--, registers.programCounter.getHob())
        memory.write(registers.stackPointer, registers.programCounter.getLob())
        registers.programCounter = address.toUInt()
        logger.debug { "Calling function at 0x${address.toInt().toHexCode(4)}" }
    }
}

object ADC_A_D8: Opcode(0xCE) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.accumulator = ALU.adc(registers, registers.accumulator, data.toUInt())
    }
}

object RST_1: Opcode(0xCF) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.stackPointer--
        memory.write(registers.stackPointer--, registers.programCounter.getHob())
        memory.write(registers.stackPointer, registers.programCounter.getLob())
        registers.programCounter = 0x08u
    }
}

object RET_NC: Opcode(0xD0) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        if(!registers.getCarryFlag()){
            val lob = memory.read(registers.stackPointer++)
            val hob = memory.read(registers.stackPointer++)
            registers.programCounter = combineBytes(hob.value, lob.value)
        }
    }
}

object POP_DE: Opcode(0xD1) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.E = memory.read(registers.stackPointer++).value.toUInt()
        registers.D = memory.read(registers.stackPointer++).value.toUInt()
    }
}

object JP_NC_A16: Opcode(0xD2) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        if(!registers.getCarryFlag()){
            registers.programCounter = memory.readNextShort().toUInt()
        }
    }
}

object CALL_NC_A16: Opcode(0xD4) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val address = memory.readNextShort()
        if(!registers.getCarryFlag()){
            registers.stackPointer--
            memory.write(registers.stackPointer--, registers.programCounter.getHob())
            memory.write(registers.stackPointer, registers.programCounter.getLob())
            registers.programCounter = address.toUInt()
        }
    }
}

object PUSH_DE: Opcode(0xD5) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.stackPointer--
        memory.write(registers.stackPointer--, registers.D.getLob())
        memory.write(registers.stackPointer, registers.E.getLob())
    }
}

object SUB_D8: Opcode(0xD6) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.accumulator = ALU.sub(registers, data.toUInt(), registers.accumulator)
    }
}

object RST_2: Opcode(0xD7) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.stackPointer--
        memory.write(registers.stackPointer--, registers.programCounter.getHob())
        memory.write(registers.stackPointer, registers.programCounter.getLob())
        registers.programCounter = 0x10u
    }
}

object RET_C: Opcode(0xD8) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        if(registers.getCarryFlag()){
            val lob = memory.read(registers.stackPointer++)
            val hob = memory.read(registers.stackPointer++)
            registers.programCounter = combineBytes(hob.value, lob.value)
        }
    }
}

object RETI: Opcode(0xD9) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val lob = memory.read(registers.stackPointer++)
        val hob = memory.read(registers.stackPointer++)
        registers.programCounter = combineBytes(hob.value, lob.value)

        cpu.ime = true
    }
}

object JP_C_A16: Opcode(0xDA) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        if(!registers.getCarryFlag()){
            registers.programCounter = memory.readNextShort().toUInt()
        }
    }
}

object CALL_C_A16: Opcode(0xDC) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val address = memory.readNextShort()
        if(!registers.getCarryFlag()){
            registers.stackPointer--
            memory.write(registers.stackPointer--, registers.programCounter.getHob())
            memory.write(registers.stackPointer, registers.programCounter.getLob())
            registers.programCounter = address.toUInt()
        }
    }
}

object SBC_A_D8: Opcode(0xDE) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.accumulator = ALU.sbc(registers, registers.accumulator, data.toUInt())
    }
}

object RST_3: Opcode(0xDF) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.stackPointer--
        memory.write(registers.stackPointer--, registers.programCounter.getHob())
        memory.write(registers.stackPointer, registers.programCounter.getLob())
        registers.programCounter = 0x18u
    }
}

object LD_P_A8_A : Opcode(0xE0) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val address = memory.readNextByte().toUInt()
        memory.write(0xFF00u or address, registers.accumulator.getLob())
    }
}

object POP_HL: Opcode(0xE1) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.L = memory.read(registers.stackPointer++).value.toUInt()
        registers.H = memory.read(registers.stackPointer++).value.toUInt()
    }
}

object LD_P_C_A : Opcode(0xE2) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val address = registers.C
        memory.write(0xFF00u or address, registers.accumulator.getLob())
    }
}

object PUSH_HL: Opcode(0xE5) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.stackPointer--
        memory.write(registers.stackPointer--, registers.H.getLob())
        memory.write(registers.stackPointer, registers.L.getLob())
    }
}

object AND_D8: Opcode(0xE6) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.accumulator = ALU.and(registers, data.toUInt(), registers.accumulator)
    }
}

object RST_4: Opcode(0xE7) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.stackPointer--
        memory.write(registers.stackPointer--, registers.programCounter.getHob())
        memory.write(registers.stackPointer, registers.programCounter.getLob())
        registers.programCounter = 0x20u
    }
}

object ADD_SP_S8: Opcode(0xE8) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.readNextByte().toInt()
        val result = (data + registers.stackPointer.toInt()).toUInt()
        val carry = result > 0xFFu

        registers.setZeroFlag(false)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag((result.getLob() + registers.stackPointer.getLob()) > 0x0Fu)
        registers.setCarryFlag(carry)
    }
}

object JP_HL: Opcode(0xE9) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.programCounter = registers.getHL()
    }
}

object LD_P_A16_A : Opcode(0xEA) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val address = memory.readNextShort()
        memory.write(address.toUInt(), registers.accumulator.getLob())
    }
}

object XOR_D8 : Opcode(0xEE) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.accumulator = ALU.xor(registers, data.toUInt(), registers.accumulator)
    }
}

object RST_5: Opcode(0xEF) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.stackPointer--
        memory.write(registers.stackPointer--, registers.programCounter.getHob())
        memory.write(registers.stackPointer, registers.programCounter.getLob())
        registers.programCounter = 0x28u
    }
}

object LD_A_P_A8 : Opcode(0xF0) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read((0xFF00 or memory.readNextByte().toInt()))
        registers.accumulator = data.value.toUInt()
    }
}

object POP_AF: Opcode(0xC1) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val lob = memory.read(registers.stackPointer++)
        val hob = memory.read(registers.stackPointer++)
        registers.accumulator = combineBytes(hob.value, lob.value)
    }
}

object LD_A_P_C : Opcode(0xF2) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.read(0xFF00u or registers.C)
        registers.accumulator = data.value.toUInt()
    }
}

object DI : Opcode(0xF3) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        cpu.ime = false
    }
}

object PUSH_AF: Opcode(0xF5) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.stackPointer--
        memory.write(registers.stackPointer--, registers.accumulator.getHob())
        memory.write(registers.stackPointer, registers.accumulator.getLob())
    }
}

object OR_D8: Opcode(0xF6) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.accumulator = ALU.or(registers, data.toUInt(), registers.accumulator)
    }
}

object RST_6: Opcode(0xF7) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.stackPointer--
        memory.write(registers.stackPointer--, registers.programCounter.getHob())
        memory.write(registers.stackPointer, registers.programCounter.getLob())
        registers.programCounter = 0x30u
    }
}

object LD_HL_SP_PLUS_S8: Opcode(0xF8) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.readNextByte().toInt()
        registers.setHL((registers.stackPointer.toInt() + data).toUInt())
    }
}

object LD_SP_HL: Opcode(0xF9) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.stackPointer = registers.getHL()
    }
}

object LD_A_A16: Opcode(0xFA) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val address = memory.readNextShort()
        registers.accumulator = memory.read(address.toUInt()).value.toUInt()
    }
}

object EI: Opcode(0xFB) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        cpu.ime = true
    }
}

object CP_D8: Opcode(0xFE) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        ALU.sub(registers, registers.accumulator, data.toUInt())
    }
}

object RST_7: Opcode(0xB8) {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        registers.stackPointer--
        memory.write(registers.stackPointer--, registers.programCounter.getHob())
        memory.write(registers.stackPointer, registers.programCounter.getLob())
        registers.programCounter = 0x38u
    }
}

object ALU {
    fun add(registers: Registers, operand1: UInt, operand2: UInt): UInt{
        val result = operand1 + operand2
        val carry = result > 0xFFu

        registers.setZeroFlag(result == 0u)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag((operand1.getLob() + operand2.getLob()) > 0x0Fu)
        registers.setCarryFlag(carry)

        return result
    }

    fun adc(registers: Registers, operand1: UInt, operand2: UInt): UInt {
        return if(registers.getCarryFlag()){
            add(registers, operand1, operand2 + 1u)
        }else add(registers, operand1, operand2)
    }

    fun sub(registers: Registers, operand1: UInt, operand2: UInt): UInt{
        val result = operand1 - operand2
        val carry = operand2 > operand1

        registers.setZeroFlag(result == 0u)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(operand1.getLob() < operand2.getLob())
        registers.setCarryFlag(carry)

        return result
    }

    fun sbc(registers: Registers, operand1: UInt, operand2: UInt): UInt {
        return if(registers.getCarryFlag()){
            sub(registers, operand1, operand2 - 1u)
        }else sub(registers, operand1, operand2)
    }

    fun and(registers: Registers, operand1: UInt, operand2: UInt): UInt{
        val result = operand1 and operand2

        registers.setZeroFlag(result == 0u)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(true)
        registers.setCarryFlag(false)

        return result
    }

    fun xor(registers: Registers, operand1: UInt, operand2: UInt): UInt{
        val result = operand1 xor operand2

        registers.setZeroFlag(result == 0u)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(false)
        registers.setCarryFlag(false)

        return result
    }

    fun or(registers: Registers, operand1: UInt, operand2: UInt): UInt{
        val result = operand1 or operand2

        registers.setZeroFlag(result == 0u)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(false)
        registers.setCarryFlag(false)

        return result
    }

}