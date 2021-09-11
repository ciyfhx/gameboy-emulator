package com.ciyfhx.emu.opcodes

fun combineBytes(hob: Byte, lob: Byte): Int{
    return (hob.toInt() shr 8 or lob.toInt())
}