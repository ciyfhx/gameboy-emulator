package com.ciyfhx.emu.opcodes

import java.util.*

fun combineBytes(hob: UByte, lob: UByte): UInt{
    return (hob.toUInt() shr 8) or lob.toUInt()
}

fun UInt.toHexCode(take: Int = 2): String {
    return this.toString(16).takeLast(take).uppercase(Locale.getDefault())
}

fun UByte.toHexCode(): String {
    return this.toString(16).uppercase(Locale.getDefault())
}

fun UInt.getLob() = this.toUByte()

fun UInt.getHob(): UByte {
    return (this and 0xFF00u shr 8).toUByte()
}