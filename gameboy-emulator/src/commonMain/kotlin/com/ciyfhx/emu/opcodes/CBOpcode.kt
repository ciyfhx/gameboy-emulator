package com.ciyfhx.emu.opcodes

import com.ciyfhx.emu.CPU
import com.ciyfhx.emu.IllegalOpcodeException
import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.Registers
import io.github.oshai.kotlinlogging.KotlinLogging

object CB: Opcode(0xCB) {

    val registeredOpcodes = mutableMapOf<Short, Opcode>()
    private val logger = KotlinLogging.logger {}

//    val registeredOpcodes = arrayListOf<Opcode>()

    init{
//        registeredOpcodes.add(BIT_7_H)
        registeredOpcodes[BIT_7_H.opcode] = BIT_7_H
        registeredOpcodes[RL_C.opcode] = RL_C
    }

    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
//        val opcode = 0xCB00u or memory.readNextByte().toUInt()
        val opcode = memory.readNextByte().toShort()
        val decodedOpcode = registeredOpcodes[opcode]
        if(decodedOpcode == null){
//            println("Unknown opcode: 0x${opcode.toInt().toHexCode()}")
            logger.error(IllegalOpcodeException(opcode.toInt())) { "Unknown opcode: 0x${opcode.toInt().toHexCode()}" }

        }
        decodedOpcode!!.execute(cpu, memory, registers)

    }
}

object RLC_B: Opcode(0x00){
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val bit = cpu.registers.B.getBit(7)

        cpu.registers.B = (cpu.registers.B shl 1)

        cpu.registers.setCarryFlag(bit)
        cpu.registers.B.setBit(0, bit)
    }
}

object RL_C: Opcode(0x11){
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val bit = cpu.registers.C.getBit(7)

        cpu.registers.C = (cpu.registers.C shl 1)

        cpu.registers.setCarryFlag(bit)
    }
}

object BIT_7_H: Opcode(0x7C){
    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val bit = cpu.registers.H.getBit(7)
        cpu.registers.setZeroFlag(!bit)
    }
}

