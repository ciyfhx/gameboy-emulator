package com.ciyfhx.emu

import com.ciyfhx.emu.opcodes.*
import com.ciyfhx.emu.opcodes.Opcode
import java.util.*

class CPU(
    val memory: Memory, val registers: Registers
) {

    var halt = false
    //master interrupt enable flag
    var ime = false

    private var opcode: Int = 0
    private var decodedOpcode: Opcode = NOP

    var last = System.currentTimeMillis()
    var counter = 0
    private val systemClock: Clock = Clock{
        val now = System.currentTimeMillis()
        val diff = now - last

        //One second has passed
        if(diff >= 1000){
            println("Hz: $counter")
            last = now
            counter = 0
        }
        counter++


        //Run Cpu
        if(!halt){
            fetch()
            decode()
            execute()
        }
    }


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
            registeredOpcodes.add(LD_P_BC_A)
            registeredOpcodes.add(INC_BC)
            registeredOpcodes.add(INC_B)
            registeredOpcodes.add(DEC_B)
            registeredOpcodes.add(LD_B_D8)
            registeredOpcodes.add(RLCA)
            registeredOpcodes.add(LD_P_A16_SP)
            registeredOpcodes.add(ADD_HL_BC)
            registeredOpcodes.add(LD_A_P_BC)
            registeredOpcodes.add(DEC_BC)
            registeredOpcodes.add(INC_C)
            registeredOpcodes.add(DEC_C)
            registeredOpcodes.add(LD_C_D8)
            registeredOpcodes.add(RRCA)
            registeredOpcodes.add(STOP)
            registeredOpcodes.add(LD_P_DE_D16)
            registeredOpcodes.add(LD_DE_A)
            registeredOpcodes.add(INC_DE)
            registeredOpcodes.add(INC_D)
            registeredOpcodes.add(DEC_D)
            registeredOpcodes.add(LD_D_D8)
            registeredOpcodes.add(RLA)
            registeredOpcodes.add(JR_S8)
            registeredOpcodes.add(ADD_HL_DE)
            registeredOpcodes.add(LD_A_P_DE)
            registeredOpcodes.add(DEC_DE)
            registeredOpcodes.add(INC_E)
            registeredOpcodes.add(DEC_E)
            registeredOpcodes.add(LD_E_D8)
            registeredOpcodes.add(RRA)
            registeredOpcodes.add(JR_NZ_S8)
            registeredOpcodes.add(LD_HL_D16)
            registeredOpcodes.add(LD_P_HL_PLUS_A)
            registeredOpcodes.add(INC_HL)
            registeredOpcodes.add(INC_H)
            registeredOpcodes.add(DEC_H)
            registeredOpcodes.add(LD_H_D8)
            registeredOpcodes.add(DAA)
            registeredOpcodes.add(JR_Z_S8)
            registeredOpcodes.add(ADD_HL_HL)
            registeredOpcodes.add(LD_A_P_HL_PLUS)
            registeredOpcodes.add(DEC_HL)
            registeredOpcodes.add(INC_L)
            registeredOpcodes.add(DEC_L)
            registeredOpcodes.add(LD_L_D8)
            registeredOpcodes.add(CPL)
            registeredOpcodes.add(JR_NC_S8)
            registeredOpcodes.add(LD_SP_D16)
            registeredOpcodes.add(LD_P_HL_MINUS_A)
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
            registeredOpcodes.add(LD_P_HL_B)
            registeredOpcodes.add(LD_P_HL_C)
            registeredOpcodes.add(LD_P_HL_D)
            registeredOpcodes.add(LD_P_HL_E)
            registeredOpcodes.add(LD_P_HL_H)
            registeredOpcodes.add(LD_P_HL_L)
            registeredOpcodes.add(HALT)
            registeredOpcodes.add(LD_P_HL_A)
            registeredOpcodes.add(LD_A_B)
            registeredOpcodes.add(LD_A_C)
            registeredOpcodes.add(LD_A_D)
            registeredOpcodes.add(LD_A_E)
            registeredOpcodes.add(LD_A_H)
            registeredOpcodes.add(LD_A_L)
            registeredOpcodes.add(LD_A_P_HL)
            registeredOpcodes.add(LD_A_A)
            registeredOpcodes.add(ADD_A_B)
            registeredOpcodes.add(ADD_A_C)
            registeredOpcodes.add(ADD_A_D)
            registeredOpcodes.add(ADD_A_E)
            registeredOpcodes.add(ADD_A_H)
            registeredOpcodes.add(ADD_A_L)
            registeredOpcodes.add(ADD_A_P_HL)
            registeredOpcodes.add(ADD_A_A)
            registeredOpcodes.add(ADC_A_B)
            registeredOpcodes.add(ADC_A_C)
            registeredOpcodes.add(ADC_A_D)
            registeredOpcodes.add(ADC_A_E)
            registeredOpcodes.add(ADC_A_H)
            registeredOpcodes.add(ADC_A_L)
            registeredOpcodes.add(ADC_A_P_HL)
            registeredOpcodes.add(ADC_A_A)
            registeredOpcodes.add(SUB_B)
            registeredOpcodes.add(SUB_C)
            registeredOpcodes.add(SUB_D)
            registeredOpcodes.add(SUB_E)
            registeredOpcodes.add(SUB_H)
            registeredOpcodes.add(SUB_L)
            registeredOpcodes.add(SUB_P_HL)
            registeredOpcodes.add(SUB_A)
            registeredOpcodes.add(SBC_A_B)
            registeredOpcodes.add(SBC_A_C)
            registeredOpcodes.add(SBC_A_D)
            registeredOpcodes.add(SBC_A_E)
            registeredOpcodes.add(SBC_A_H)
            registeredOpcodes.add(SBC_A_L)
            registeredOpcodes.add(SBC_A_P_HL)
            registeredOpcodes.add(SBC_A_A)
            registeredOpcodes.add(AND_B)
            registeredOpcodes.add(AND_C)
            registeredOpcodes.add(AND_D)
            registeredOpcodes.add(AND_E)
            registeredOpcodes.add(AND_H)
            registeredOpcodes.add(AND_L)
            registeredOpcodes.add(AND_P_HL)
            registeredOpcodes.add(AND_A)
            registeredOpcodes.add(XOR_B)
            registeredOpcodes.add(XOR_C)
            registeredOpcodes.add(XOR_D)
            registeredOpcodes.add(XOR_E)
            registeredOpcodes.add(XOR_H)
            registeredOpcodes.add(XOR_L)
            registeredOpcodes.add(XOR_P_HL)
            registeredOpcodes.add(XOR_A)
            registeredOpcodes.add(OR_B)
            registeredOpcodes.add(OR_C)
            registeredOpcodes.add(OR_D)
            registeredOpcodes.add(OR_E)
            registeredOpcodes.add(OR_H)
            registeredOpcodes.add(OR_L)
            registeredOpcodes.add(OR_P_HL)
            registeredOpcodes.add(OR_A)
            registeredOpcodes.add(CP_B)
            registeredOpcodes.add(CP_C)
            registeredOpcodes.add(CP_D)
            registeredOpcodes.add(CP_E)
            registeredOpcodes.add(CP_H)
            registeredOpcodes.add(CP_L)
            registeredOpcodes.add(CP_P_HL)
            registeredOpcodes.add(CP_A)
            registeredOpcodes.add(RET_NZ)
            registeredOpcodes.add(POP_BC)
            registeredOpcodes.add(JP_NZ_A16)
            registeredOpcodes.add(JP_A16)
            registeredOpcodes.add(CALL_NZ_A16)
            registeredOpcodes.add(PUSH_BC)
            registeredOpcodes.add(ADD_A_D8)
            registeredOpcodes.add(RST_0)
            registeredOpcodes.add(RET_Z)
            registeredOpcodes.add(RET)
            registeredOpcodes.add(JP_Z_A16)
            registeredOpcodes.add(CB)
            registeredOpcodes.add(CALL_Z_A16)
            registeredOpcodes.add(CALL_A16)
            registeredOpcodes.add(ADC_A_D8)
            registeredOpcodes.add(RST_1)
            registeredOpcodes.add(RET_NC)
            registeredOpcodes.add(POP_DE)
            registeredOpcodes.add(JP_NC_A16)

            registeredOpcodes.add(NOP)

            registeredOpcodes.add(CALL_NC_A16)
            registeredOpcodes.add(PUSH_DE)
            registeredOpcodes.add(SUB_D8)
            registeredOpcodes.add(RST_2)
            registeredOpcodes.add(RET_C)
            registeredOpcodes.add(RETI)
            registeredOpcodes.add(JP_C_A16)

            registeredOpcodes.add(NOP)

            registeredOpcodes.add(CALL_C_A16)

            registeredOpcodes.add(NOP)

            registeredOpcodes.add(SBC_A_D8)
            registeredOpcodes.add(RST_3)
            registeredOpcodes.add(LD_P_A8_A)
            registeredOpcodes.add(POP_HL)
            registeredOpcodes.add(LD_P_C_A)

            registeredOpcodes.add(NOP)
            registeredOpcodes.add(NOP)

            registeredOpcodes.add(PUSH_HL)
            registeredOpcodes.add(AND_D8)
            registeredOpcodes.add(RST_4)
            registeredOpcodes.add(ADD_SP_S8)
            registeredOpcodes.add(JP_HL)
            registeredOpcodes.add(LD_P_A16_A)

            registeredOpcodes.add(NOP)
            registeredOpcodes.add(NOP)
            registeredOpcodes.add(NOP)

            registeredOpcodes.add(XOR_D8)
            registeredOpcodes.add(RST_5)
            registeredOpcodes.add(LD_A_P_A8)
            registeredOpcodes.add(POP_AF)
            registeredOpcodes.add(LD_A_P_C)
            registeredOpcodes.add(DI)

            registeredOpcodes.add(NOP)

            registeredOpcodes.add(PUSH_AF)
            registeredOpcodes.add(OR_D8)
            registeredOpcodes.add(RST_6)
            registeredOpcodes.add(LD_HL_SP_PLUS_S8)
            registeredOpcodes.add(LD_SP_HL)
            registeredOpcodes.add(LD_A_A16)
            registeredOpcodes.add(EI)

            registeredOpcodes.add(NOP)
            registeredOpcodes.add(NOP)

            registeredOpcodes.add(CP_D8)
            registeredOpcodes.add(RST_7)
        }

    }

    fun start(){
        halt = false
        systemClock.start()
    }

    fun stop(){
        halt = true
        systemClock.stop()
    }

    private fun fetch(){
        opcode = memory.readNextByte().toInt()
    }

    private fun decode(){
        try {
            Thread.sleep(1000)
            decodedOpcode = registeredOpcodes[opcode]
            println("Decoded: 0x${opcode.toHexCode()} $decodedOpcode")
        }catch(e: IndexOutOfBoundsException){
            println("Unknown opcode 0x${opcode.toHexCode()}")
        }
    }

    private fun execute(){
        decodedOpcode.execute(this, memory, registers)
        decodedOpcode = NOP
    }

}
