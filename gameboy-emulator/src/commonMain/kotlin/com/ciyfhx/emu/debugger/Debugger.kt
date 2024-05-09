package com.ciyfhx.emu.debugger

interface Debugger {

    fun stepOver(): DebugState
    fun setBreakpoint(memory: Int)
}