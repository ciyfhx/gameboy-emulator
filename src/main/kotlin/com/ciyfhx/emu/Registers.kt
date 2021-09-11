package com.ciyfhx.emu

import com.ciyfhx.emu.opcodes.combineBytes
import java.util.*

enum class Flag {
    ZERO, SUBTRACT, HALF_CARRY, CARRY
}

/**
 * [CPU] Registers
 */
class Registers {
    var accumulator: Int = 0
    var flag = EnumSet.noneOf(Flag::class.java)

    var B: Int = 0
    var C: Int = 0

    var D: Int = 0
    var E: Int = 0

    var H: Int = 0
    var L: Int = 0

    var stackPointer: Int = 0
    var programCounter: Int = 0

    fun getBC(): Int {
        return combineBytes(B.toByte(), C.toByte())
    }

    fun setBC(value: Int){
        B = value and 0xF0
        C = value and 0x0F
    }

    fun setZeroFlag(set: Boolean){
        if(set){
            flag.add(Flag.ZERO)
        }else{
            flag.remove(Flag.ZERO)
        }
    }

    fun getZeroFlag(): Boolean{
        return flag.contains(Flag.ZERO)
    }

    fun setSubtractFlag(set: Boolean){
        if(set){
            flag.add(Flag.SUBTRACT)
        }else{
            flag.remove(Flag.SUBTRACT)
        }
    }

    fun getSubtractFlag(): Boolean{
        return flag.contains(Flag.SUBTRACT)
    }

    fun setHalfCarryFlag(set: Boolean){
        if(set){
            flag.add(Flag.HALF_CARRY)
        }else{
            flag.remove(Flag.HALF_CARRY)
        }
    }

    fun getHalfCarryFlag(): Boolean{
        return flag.contains(Flag.HALF_CARRY)
    }

    fun setCarryFlag(set: Boolean){
        if(set){
            flag.add(Flag.CARRY)
        }else{
            flag.remove(Flag.CARRY)
        }
    }

    fun getCarryFlag(): Boolean{
        return flag.contains(Flag.CARRY)
    }

}