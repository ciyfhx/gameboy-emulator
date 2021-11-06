package com.ciyfhx.emu.opcodes

import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.Registers

abstract class Opcode(
    val opcode: Short
) {
    abstract fun execute(memory: Memory, registers: Registers)
}

object NOP : Opcode(0x00) {
    override fun execute(memory: Memory, registers: Registers) {}
}

object LD_BC_D16 : Opcode(0x01) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.C = memory.readNextByte().toInt()
        registers.B = memory.readNextByte().toInt()
    }
}

object LD_BC_A : Opcode(0x02) {
    override fun execute(memory: Memory, registers: Registers) {
        val address = registers.getBC()
        memory.write(address, registers.accumulator.toByte())
    }
}
object INC_BC : Opcode(0x03) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.getBC()
        val data = value + 1
        registers.setBC(data)
    }
}

object INC_B : Opcode(0x04) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.B
        val data = value + 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0F == 0x0F)
        registers.B = data
    }
}

object DEC_B : Opcode(0x05) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.B
        val data = value - 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0F == 0x00)
        registers.B = data
    }
}

object LD_B_D8 : Opcode(0x06) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.B = memory.readNextByte().toInt()
    }
}

object RLCA : Opcode(0x07) {
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

object LD_A16_SP : Opcode(0x08) {
    override fun execute(memory: Memory, registers: Registers) {
        val lsb = memory.readNextByte()
        val hsb = memory.readNextByte()
        registers.stackPointer = combineBytes(hsb, lsb)
    }
}

object ADD_HL_BC : Opcode(0x09) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.getBC() + registers.getHL()
        val carry = value and 0b0000_0001_0000_0000 == 0b0000_0001_0000_0000
        registers.setBC(value)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(carry)
        registers.setCarryFlag(carry)
    }
}

object LD_A_BC : Opcode(0x0A) {
    override fun execute(memory: Memory, registers: Registers) {
        val address = registers.getBC()
        registers.accumulator = memory.read(address).toInt()
    }
}

object DEC_BC : Opcode(0x0B) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.setBC(registers.getBC() - 1)
    }
}

object INC_C : Opcode(0x0C) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.C
        val data = value + 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0F == 0x0F)
        registers.C = data
    }
}

object DEC_C : Opcode(0x0D) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.C
        val data = value - 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0F == 0x00)
        registers.C = data
    }
}

object LD_C_D8 : Opcode(0x0E) {
    override fun execute(memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.C = data.toInt()
    }
}

object RRCA : Opcode(0x0F) {
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

object STOP : Opcode(0x10) {
    override fun execute(memory: Memory, registers: Registers) {
        assert(memory.readNextByte().toInt() == 0x00)
        //TODO("Check for STOP conditions")
    }
}

object LD_DE_D16 : Opcode(0x11) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.E = memory.readNextByte().toInt()
        registers.D = memory.readNextByte().toInt()
    }
}

object LD_DE_A : Opcode(0x12) {
    override fun execute(memory: Memory, registers: Registers) {
        val address = registers.getDE()
        memory.write(address, registers.accumulator.toByte())
    }
}

object INC_DE : Opcode(0x13) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.getDE()
        val data = value + 1
        registers.setDE(data)
    }
}

object INC_D : Opcode(0x14) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.D
        val data = value + 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0F == 0x0F)
        registers.D = data
    }
}

object DEC_D : Opcode(0x15) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.D
        val data = value - 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0F == 0x00)
        registers.D = data
    }
}

object LD_D_D8 : Opcode(0x16) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.D = memory.readNextByte().toInt()
    }
}

object RLA : Opcode(0x17) {
    override fun execute(memory: Memory, registers: Registers) {
        val carry = registers.getCarryFlag()
        registers.accumulator = registers.accumulator shl 1
        registers.accumulator = if(carry){
            registers.accumulator or 1
        }else{
            registers.accumulator or 0
        }
        registers.setZeroFlag(false)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(false)
//        registers.setCarryFlag(carry == 0b1000_0000)
    }
}

object JR_S8 : Opcode(0x18) {
    override fun execute(memory: Memory, registers: Registers) {
        val offset = memory.readNextByte()
        registers.programCounter += offset
    }
}

object ADD_HL_DE : Opcode(0x19) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.getHL() + registers.getDE()
        val carry = value and 0b0000_0001_0000_0000 == 0b0000_0001_0000_0000
        registers.setHL(value)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(carry)
        registers.setCarryFlag(carry)
    }
}

object LD_A_DE : Opcode(0x1A) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.accumulator = memory.read(registers.getDE()).toInt()
    }
}

object DEC_DE : Opcode(0x1B) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.setDE(registers.getDE() - 1)
    }
}

object INC_E : Opcode(0x1C) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.E
        val data = value + 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0F == 0x0F)
        registers.E = data
    }
}

object DEC_E : Opcode(0x1D) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.E
        val data = value - 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0F == 0x00)
        registers.E = data
    }
}

object LD_E_D8 : Opcode(0x1E) {
    override fun execute(memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.E = data.toInt()
    }
}

object RRA : Opcode(0x1F) {
    override fun execute(memory: Memory, registers: Registers) {
        val carry = if(registers.getCarryFlag()) 1 else 0
        val a8 = registers.accumulator or 0b0000_0001
        registers.accumulator = registers.accumulator shr 1
        registers.accumulator = registers.accumulator or (carry shl 7)
        registers.setZeroFlag(false)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(false)
        registers.setCarryFlag(a8 == 1)
    }
}

object JR_NZ_S8 : Opcode(0x20) {
    override fun execute(memory: Memory, registers: Registers) {
        val s8 = memory.readNextByte()
        if(!registers.getZeroFlag()){
            registers.programCounter += s8
        }
    }
}

object LD_HL_D16 : Opcode(0x21) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.L = memory.readNextByte().toInt()
        registers.H = memory.readNextByte().toInt()
    }
}

object LD_HL_P_A : Opcode(0x22) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.accumulator
        memory.write(registers.getHL(), value.toByte())
        registers.setHL(registers.getHL() + 1)
    }
}

object INC_HL : Opcode(0x23) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.getHL()
        val data = value + 1
        registers.setHL(data)
    }
}

object INC_H : Opcode(0x24) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.H
        val data = value + 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0F == 0x0F)
        registers.H = data
    }
}

object DEC_H : Opcode(0x25) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.H
        val data = value - 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0F == 0x00)
        registers.H = data
    }
}

object LD_H_D8 : Opcode(0x26) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.H = memory.readNextByte().toInt()
    }
}

object DAA : Opcode(0x27) {
    override fun execute(memory: Memory, registers: Registers) {
        var data = registers.accumulator
        var correction = 0
        if(registers.getHalfCarryFlag() ||
            (!registers.getSubtractFlag() && (data and 0x0F) > 0x09)){
            correction = correction or 0x06
        }
        if(registers.getCarryFlag() ||
            (!registers.getSubtractFlag() && (data and 0xFF) > 0x99)){
            correction = correction or 0x60
            registers.setCarryFlag(true)
        }
        data += if(registers.getSubtractFlag()) -correction else correction
        val carry = data and 0b0000_0001_0000_0000 == 0b0000_0001_0000_0000
        registers.accumulator = data
        registers.setZeroFlag(data == 0)
        registers.setHalfCarryFlag(false)
        registers.setCarryFlag(carry)
    }
}

object JR_Z_S8 : Opcode(0x28) {
    override fun execute(memory: Memory, registers: Registers) {
        val offset = memory.readNextByte()
        if(registers.getZeroFlag()){
            registers.programCounter += offset
        }
    }
}

object ADD_HL_HL : Opcode(0x29) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.getHL() + registers.getHL()
        val carry = value and 0b0000_0001_0000_0000 == 0b0000_0001_0000_0000
        registers.setHL(value)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(carry)
        registers.setCarryFlag(carry)
    }
}

object LD_A_HL_PLUS : Opcode(0x2A) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.accumulator = memory.read(registers.getHL()).toInt()
        registers.setHL(registers.getHL() + 1)
    }
}

object DEC_HL : Opcode(0x2B) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.setHL(registers.getHL() - 1)
    }
}

object INC_L : Opcode(0x2C) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.L
        val data = value + 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0F == 0x0F)
        registers.L = data
    }
}


object DEC_L : Opcode(0x2D) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.L
        val data = value - 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0F == 0x00)
        registers.L = data
    }
}

object LD_L_D8 : Opcode(0x2E) {
    override fun execute(memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.L = data.toInt()
    }
}

object CPL : Opcode(0x2F) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.accumulator = registers.accumulator.inv()
    }
}

object JR_NC_S8 : Opcode(0x30) {
    override fun execute(memory: Memory, registers: Registers) {
        val s8 = memory.readNextByte()
        if(!registers.getCarryFlag()){
            registers.programCounter += s8
        }
    }
}

object LD_SP_D16 : Opcode(0x31) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.stackPointer = memory.readNextByte().toInt()
        registers.stackPointer = registers.stackPointer or memory.readNextByte().toInt() shl 8
    }
}

object LD_HL_S_A : Opcode(0x32) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.accumulator
        memory.write(registers.getHL(), value.toByte())
        registers.setHL(registers.getHL() - 1)
    }
}

object INC_SP : Opcode(0x33) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.getHL()
        val data = value + 1
        registers.stackPointer = data
    }
}

object INC_P_HL : Opcode(0x34) {
    override fun execute(memory: Memory, registers: Registers) {
        val pointer = registers.getHL()
        val value = memory.read(pointer).toInt()
        val data = value + 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0F == 0x0F)
        memory.write(pointer, data.toByte())
    }
}

object DEC_P_HL : Opcode(0x35) {
    override fun execute(memory: Memory, registers: Registers) {
        val pointer = registers.getHL()
        val value = memory.read(pointer).toInt()
        val data = value + 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0F == 0x0F)
        memory.write(pointer, data.toByte())
    }
}
object LD_P_HL_D8 : Opcode(0x36) {
    override fun execute(memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        val pointer = registers.getHL()
        memory.write(pointer, data)
    }
}

object SCF : Opcode(0x37) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(false)
        registers.setCarryFlag(true)
    }
}

object JR_C_S8 : Opcode(0x38) {
    override fun execute(memory: Memory, registers: Registers) {
        val offset = memory.readNextByte()
        if(registers.getCarryFlag()){
            registers.programCounter += offset
        }
    }
}

object ADD_HL_SP : Opcode(0x39) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.getHL() + registers.stackPointer
        val carry = value and 0b0000_0001_0000_0000 == 0b0000_0001_0000_0000
        registers.setHL(value)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(carry)
        registers.setCarryFlag(carry)
    }
}

object LD_A_HL_MINUS : Opcode(0x3A) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.accumulator = memory.read(registers.getHL()).toInt()
        registers.setHL(registers.getHL() - 1)
    }
}

object DEC_SP : Opcode(0x3B) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.stackPointer--
    }
}

object INC_A : Opcode(0x3C) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.accumulator
        val data = value + 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(value and 0x0F == 0x0F)
        registers.accumulator = data
    }
}

object DEC_A : Opcode(0x3D) {
    override fun execute(memory: Memory, registers: Registers) {
        val value = registers.accumulator
        val data = value - 1 and 0xFF
        registers.setZeroFlag(data == 0)
        registers.setSubtractFlag(true)
        registers.setHalfCarryFlag(value and 0x0F == 0x00)
        registers.accumulator = data
    }
}

object LD_A_D8 : Opcode(0x3E) {
    override fun execute(memory: Memory, registers: Registers) {
        val data = memory.readNextByte()
        registers.accumulator = data.toInt()
    }
}

object CCF : Opcode(0x3F) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(false)
        registers.setCarryFlag(!registers.getCarryFlag())
    }
}

object LD_B_B : Opcode(0x40) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.B = registers.B
    }
}

object LD_B_C : Opcode(0x41) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.B = registers.C
    }
}

object LD_B_D : Opcode(0x42) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.B = registers.D
    }
}

object LD_B_E : Opcode(0x43) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.B = registers.E
    }
}

object LD_B_H : Opcode(0x44) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.B = registers.H
    }
}

object LD_B_L : Opcode(0x45) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.B = registers.L
    }
}

object LD_B_P_HL : Opcode(0x46) {
    override fun execute(memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.B = data.toInt()
    }
}

object LD_B_A : Opcode(0x47) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.B = registers.accumulator
    }
}

object LD_C_B : Opcode(0x48) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.C = registers.B
    }
}

object LD_C_C : Opcode(0x49) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.C = registers.C
    }
}

object LD_C_D : Opcode(0x4A) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.C = registers.D
    }
}

object LD_C_E : Opcode(0x4B) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.C = registers.E
    }
}

object LD_C_H : Opcode(0x4C) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.C = registers.H
    }
}

object LD_C_L : Opcode(0x4D) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.C = registers.L
    }
}

object LD_C_P_HL : Opcode(0x4E) {
    override fun execute(memory: Memory, registers: Registers) {
        val data = memory.read(registers.getHL())
        registers.C = data.toInt()
    }
}

object LD_C_A : Opcode(0x4F) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.C = registers.accumulator
    }
}

object LD_D_B : Opcode(0x50) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.D = registers.B
    }
}

object LD_D_C : Opcode(0x51) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.D = registers.C
    }
}

object LD_D_D : Opcode(0x52) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.D = registers.D
    }
}

object LD_D_E : Opcode(0x53) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.D = registers.E
    }
}

object LD_D_H : Opcode(0x54) {
    override fun execute(memory: Memory, registers: Registers) {
        registers.D = registers.H
    }
}