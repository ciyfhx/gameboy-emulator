package com.ciyfhx.emu.debugger

import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.Registers

class DebugState (
    val location: Int,
    val registers: Registers,
    val memory: Memory
)