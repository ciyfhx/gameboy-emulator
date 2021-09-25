package com.ciyfhx.emu

import com.ciyfhx.emu.opcodes.*
import com.ciyfhx.emu.opcodes.Opcode

class CPU {

    val registers = Registers()
    val memory = Memory(registers)

    private var opcode: Int = 0
    private var decodedOpcode: Opcode = NOP()

    init {
        //Register all the opcodes
        opcodes()
    }

    companion object {
        val registeredOpcodes = arrayListOf<Opcode>()

        @JvmStatic
        fun opcodes(){
            //Make sure the opcodes are in order
            registeredOpcodes.add(NOP())
            registeredOpcodes.add(LD_BC_D16())
            registeredOpcodes.add(LD_BC_A())
            registeredOpcodes.add(INC_BC())
            registeredOpcodes.add(INC_B())
            registeredOpcodes.add(DEC_B())
            registeredOpcodes.add(LD_B_D8())
            registeredOpcodes.add(RLCA())
            registeredOpcodes.add(LD_A16_SP())
            registeredOpcodes.add(ADD_HL_BC())
            registeredOpcodes.add(LD_A_BC())
            registeredOpcodes.add(DEC_BC())
            registeredOpcodes.add(INC_C())
            registeredOpcodes.add(DEC_C())
            registeredOpcodes.add(LD_C_D8())
            registeredOpcodes.add(RRCA())
            registeredOpcodes.add(STOP())
            registeredOpcodes.add(LD_DE_D16())
            registeredOpcodes.add(LD_DE_A())
            registeredOpcodes.add(INC_DE())
            registeredOpcodes.add(INC_D())
            registeredOpcodes.add(LD_D_D8())
            registeredOpcodes.add(RLA())
            registeredOpcodes.add(JR_S8())
            registeredOpcodes.add(ADD_HL_DE())
            registeredOpcodes.add(LD_A())
            registeredOpcodes.add(DEC_DE())
            registeredOpcodes.add(INC_E())
            registeredOpcodes.add(DEC_E())
            registeredOpcodes.add(LD_E_D8())
            registeredOpcodes.add(RRA())
        }

    }


    fun fetch(){
        opcode = memory.readNextByte().toInt()
    }

    fun decode(){
        try {
            decodedOpcode = registeredOpcodes[opcode]
        }catch(e: IndexOutOfBoundsException){
            println("Unknown opcode 0x${opcode.toString(16)}")
        }
    }

    fun execute(){
        decodedOpcode.execute(memory, registers)
    }

}