package com.ciyfhx.emu.opcodes

import java.util.*

fun combineBytes(hob: UByte, lob: UByte): UInt{
    return (hob.toUInt() shr 8) or lob.toUInt()
}

fun Int.toHexCode(take: Int = 2): String {
    return this.toString(16).takeLast(take).uppercase(Locale.getDefault())
}

fun UByte.toHexCode(): String {
    return this.toString(16).uppercase(Locale.getDefault())
}

fun UInt.getLob() = this.toUByte()

fun UInt.getHob(): UByte {
    return (this and 0xFF00u shr 8).toUByte()
}

fun UByte.getBit(position: Int): Boolean {
    return (this.toInt() shr position) and 1 == 1
}

fun UByte.setBit(position: Int, value: Boolean): UByte {
    return if(value) {
        (this.toInt() or (1 shl position))
    } else {
        (this.toInt() and (1 shl position).inv())
    }.toUByte()
}