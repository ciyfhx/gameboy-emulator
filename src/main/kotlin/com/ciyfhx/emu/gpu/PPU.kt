package com.ciyfhx.emu.gpu

class PPU {
}

data class PixelTile(
    val data: Array<UByte>
){
    init {
        assert(data.size == 16) { "Pixel Tile must be in 16 bytes" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PixelTile

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}