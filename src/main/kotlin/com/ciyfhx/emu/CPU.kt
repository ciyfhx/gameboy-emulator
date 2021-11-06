package com.ciyfhx.emu

import com.ciyfhx.emu.opcodes.*
import com.ciyfhx.emu.opcodes.Opcode
import java.util.*

class CPU {

    val registers = Registers()
    val memory = Memory(registers)

    private var opcode: Int = 0
    private var decodedOpcode: Opcode = NOP

    init {
        //Register all the opcodes
        opcodes()
    }

    companion object {
        val registeredOpcodes = arrayListOf<Opcode>()

        @JvmStatic
        fun opcodes(){
            //Make sure the opcodes are in order
            registeredOpcodes.add(NOP)
            registeredOpcodes.add(LD_BC_D16)
            registeredOpcodes.add(LD_BC_A)
            registeredOpcodes.add(INC_BC)
            registeredOpcodes.add(INC_B)
            registeredOpcodes.add(DEC_B)
            registeredOpcodes.add(LD_B_D8)
            registeredOpcodes.add(RLCA)
            registeredOpcodes.add(LD_A16_SP)
            registeredOpcodes.add(ADD_HL_BC)
            registeredOpcodes.add(LD_A_BC)
            registeredOpcodes.add(DEC_BC)
            registeredOpcodes.add(INC_C)
            registeredOpcodes.add(DEC_C)
            registeredOpcodes.add(LD_C_D8)
            registeredOpcodes.add(RRCA)
            registeredOpcodes.add(STOP)
            registeredOpcodes.add(LD_DE_D16)
            registeredOpcodes.add(LD_DE_A)
            registeredOpcodes.add(INC_DE)
            registeredOpcodes.add(INC_D)
            registeredOpcodes.add(DEC_D)
            registeredOpcodes.add(LD_D_D8)
            registeredOpcodes.add(RLA)
            registeredOpcodes.add(JR_S8)
            registeredOpcodes.add(ADD_HL_DE)
            registeredOpcodes.add(LD_A_DE)
            registeredOpcodes.add(DEC_DE)
            registeredOpcodes.add(INC_E)
            registeredOpcodes.add(DEC_E)
            registeredOpcodes.add(LD_E_D8)
            registeredOpcodes.add(RRA)
            registeredOpcodes.add(JR_NZ_S8)
            registeredOpcodes.add(LD_HL_D16)
            registeredOpcodes.add(LD_HL_P_A)
            registeredOpcodes.add(INC_HL)
            registeredOpcodes.add(INC_H)
            registeredOpcodes.add(DEC_H)
            registeredOpcodes.add(LD_H_D8)
            registeredOpcodes.add(DAA)
            registeredOpcodes.add(JR_Z_S8)
            registeredOpcodes.add(ADD_HL_HL)
            registeredOpcodes.add(LD_A_HL_PLUS)
            registeredOpcodes.add(DEC_HL)
            registeredOpcodes.add(INC_L)
            registeredOpcodes.add(DEC_L)
            registeredOpcodes.add(LD_L_D8)
            registeredOpcodes.add(CPL)
            registeredOpcodes.add(JR_NC_S8)
            registeredOpcodes.add(LD_SP_D16)
            registeredOpcodes.add(LD_HL_S_A)
            registeredOpcodes.add(INC_SP)
            registeredOpcodes.add(INC_P_HL)
            registeredOpcodes.add(DEC_P_HL)
            registeredOpcodes.add(LD_P_HL_D8)
            registeredOpcodes.add(SCF)
            registeredOpcodes.add(JR_C_S8)
            registeredOpcodes.add(ADD_HL_SP)
            registeredOpcodes.add(LD_A_HL_MINUS)
            registeredOpcodes.add(DEC_SP)
            registeredOpcodes.add(INC_A)
            registeredOpcodes.add(DEC_A)
            registeredOpcodes.add(LD_A_D8)
            registeredOpcodes.add(CCF)
            registeredOpcodes.add(LD_B_B)
            registeredOpcodes.add(LD_B_C)
            registeredOpcodes.add(LD_B_D)
            registeredOpcodes.add(LD_B_E)
            registeredOpcodes.add(LD_B_H)
            registeredOpcodes.add(LD_B_L)
            registeredOpcodes.add(LD_B_P_HL)
            registeredOpcodes.add(LD_B_A)
            registeredOpcodes.add(LD_C_B)
            registeredOpcodes.add(LD_C_C)
            registeredOpcodes.add(LD_C_D)
            registeredOpcodes.add(LD_C_E)
            registeredOpcodes.add(LD_C_H)
            registeredOpcodes.add(LD_C_L)
            registeredOpcodes.add(LD_C_P_HL)
            registeredOpcodes.add(LD_C_A)
            registeredOpcodes.add(LD_D_B)
            registeredOpcodes.add(LD_D_C)
            registeredOpcodes.add(LD_D_D)
            registeredOpcodes.add(LD_D_E)
            registeredOpcodes.add(LD_D_H)
            registeredOpcodes.add(LD_D_L)
            registeredOpcodes.add(LD_D_P_HL)
            registeredOpcodes.add(LD_D_A)
            registeredOpcodes.add(LD_E_B)
            registeredOpcodes.add(LD_E_C)
            registeredOpcodes.add(LD_E_D)
            registeredOpcodes.add(LD_E_E)
            registeredOpcodes.add(LD_E_H)
            registeredOpcodes.add(LD_E_L)
            registeredOpcodes.add(LD_E_P_HL)
            registeredOpcodes.add(LD_E_A)
            registeredOpcodes.add(LD_H_B)
            registeredOpcodes.add(LD_H_C)
            registeredOpcodes.add(LD_H_D)
            registeredOpcodes.add(LD_H_E)
            registeredOpcodes.add(LD_H_H)
            registeredOpcodes.add(LD_H_L)
            registeredOpcodes.add(LD_H_P_HL)
            registeredOpcodes.add(LD_H_A)
            registeredOpcodes.add(LD_L_B)
            registeredOpcodes.add(LD_L_C)
            registeredOpcodes.add(LD_L_D)
            registeredOpcodes.add(LD_L_E)
            registeredOpcodes.add(LD_L_H)
            registeredOpcodes.add(LD_L_L)
            registeredOpcodes.add(LD_L_P_HL)
            registeredOpcodes.add(LD_L_A)
        }

    }


    fun fetch(){
        opcode = memory.readNextByte().toInt()
    }

    fun decode(){
        try {
            decodedOpcode = registeredOpcodes[opcode]
            println("Decoded: 0x${opcode.toHexCode()} $decodedOpcode")
        }catch(e: IndexOutOfBoundsException){
            println("Unknown opcode 0x${opcode.toHexCode()}")
        }
    }

    fun execute(){
        decodedOpcode.execute(memory, registers)
        decodedOpcode = NOP
    }

}
fun Int.toHexCode(): String{
    return this.toUByte().toString(16).uppercase(Locale.getDefault())
}
