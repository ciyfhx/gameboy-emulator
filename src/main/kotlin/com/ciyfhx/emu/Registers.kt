package com.ciyfhx.emu

import com.ciyfhx.emu.opcodes.combineBytes

/**
 * [CPU] Registers
 */
class Registers {
    var accumulator: Int = 0
    var flag: Byte = 0

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



}