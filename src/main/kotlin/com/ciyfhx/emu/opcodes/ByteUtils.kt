package com.ciyfhx.emu.opcodes

import java.util.*

fun combineBytes(hob: UByte, lob: UByte): UInt{
    return (hob.toUInt() shr 8) or lob.toUInt()
}

fun UInt.toHexCode(): String{
    return this.toUByte().toString(16).uppercase(Locale.getDefault())
}

fun UInt.getLob() = this.toUByte()

fun UInt.getHob(): UByte {
    return (this and 0xFF00u shr 8).toUByte()
}