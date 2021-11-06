package com.ciyfhx.emu.opcodes

fun combineBytes(hob: UByte, lob: UByte): UInt{
    return (hob.toUInt() shr 8) or lob.toUInt()
}