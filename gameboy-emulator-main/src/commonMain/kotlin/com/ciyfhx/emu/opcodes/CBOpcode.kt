package com.ciyfhx.emu.opcodes

import com.ciyfhx.emu.CPU
import com.ciyfhx.emu.IllegalOpcodeException
import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.Registers
import io.github.oshai.kotlinlogging.KotlinLogging

object CB: Opcode(0xCB) {

    val registeredOpcodes = mutableMapOf<Short, Opcode>()
    private val logger = KotlinLogging.logger {}


    override fun execute(cpu: CPU, memory: Memory, registers: Registers) {
        val op = memory.readNextByte().toInt()
        val target = op and 7
        val bit = (op shr 3) and 7
        val value = get(target, memory, registers)

        when (op shr 6) {
            0 -> set(target, shiftOrRotate(bit, value, registers), memory, registers)
            1 -> {
                registers.setZeroFlag(((value shr bit) and 1) == 0)
                registers.setSubtractFlag(false)
                registers.setHalfCarryFlag(true)
            }
            2 -> set(target, value and (1 shl bit).inv(), memory, registers)
            3 -> set(target, value or (1 shl bit), memory, registers)
        }
    }

    private fun shiftOrRotate(kind: Int, v: Int, registers: Registers): Int {
        val carryIn = if (registers.getCarryFlag()) 1 else 0
        val (result, carryOut) = when (kind) {
            0 -> Pair(((v shl 1) or (v shr 7)) and 0xFF, v shr 7)   // RLC
            1 -> Pair(((v shr 1) or (v shl 7)) and 0xFF, v and 1)   // RRC
            2 -> Pair(((v shl 1) or carryIn) and 0xFF, v shr 7)     // RL
            3 -> Pair((v shr 1) or (carryIn shl 7), v and 1)        // RR
            4 -> Pair((v shl 1) and 0xFF, v shr 7)                  // SLA
            5 -> Pair((v shr 1) or (v and 0x80), v and 1)           // SRA
            6 -> Pair(((v shl 4) or (v shr 4)) and 0xFF, 0)         // SWAP
            else -> Pair(v shr 1, v and 1)                          // SRL
        }
        registers.setZeroFlag(result == 0)
        registers.setSubtractFlag(false)
        registers.setHalfCarryFlag(false)
        registers.setCarryFlag(carryOut == 1)
        return result
    }

    private fun get(target: Int, memory: Memory, registers: Registers): Int = when (target) {
        0 -> registers.B.toInt()
        1 -> registers.C.toInt()
        2 -> registers.D.toInt()
        3 -> registers.E.toInt()
        4 -> registers.H.toInt()
        5 -> registers.L.toInt()
        6 -> memory.read(registers.getHL()).value.toInt()
        else -> registers.accumulator.toInt()
    }

    private fun set(target: Int, v: Int, memory: Memory, registers: Registers) {
        when (target) {
            0 -> registers.B = v.toUInt()
            1 -> registers.C = v.toUInt()
            2 -> registers.D = v.toUInt()
            3 -> registers.E = v.toUInt()
            4 -> registers.H = v.toUInt()
            5 -> registers.L = v.toUInt()
            6 -> memory.write(registers.getHL(), v.toUByte())
            else -> registers.accumulator = v.toUInt()
        }
    }

}