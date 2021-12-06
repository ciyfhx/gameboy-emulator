package com.ciyfhx.emu.gpu

import com.ciyfhx.emu.opcodes.getBit

class LCDC {

    private var status: UByte = 0u

    fun setValue(status: UByte) {
        this.status = status
    }

    fun getBgWindowDisplayPriority(): Boolean {
        return status.getBit(0)
    }

    fun getObjDisplayEnable(): Boolean {
        return status.getBit(1)
    }

    fun getObjSize(): Boolean {
        return status.getBit(2)
    }

    fun getBgTileMapDisplaySelect(): Boolean {
        return status.getBit(3)
    }

    fun getBgAndWindowTileDataSelect(): Boolean {
        return status.getBit(4)
    }

    fun getWindowDisplayEnable(): Boolean {
        return status.getBit(5)
    }

    fun getWindowTileMapDisplaySelect(): Boolean {
        return status.getBit(6)
    }

    fun getLCDDisplayEnable(): Boolean {
        return status.getBit(7)
    }

}