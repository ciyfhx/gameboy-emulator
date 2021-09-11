package com.ciyfhx.emu

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

    fun getBC(): Short {
        return (B shl 8 or C).toShort()
    }

}