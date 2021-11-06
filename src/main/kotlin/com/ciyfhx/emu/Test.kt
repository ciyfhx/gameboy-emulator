package com.ciyfhx.emu

fun main() {
    val cpu = CPU()

    //Copy bootrom
    cpu.memory.copyByteArray(0, readBootRom()!!)
    cpu.start()
}