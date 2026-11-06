package com.ciyfhx.emu.gpu

class LCD (
    val width:  Int = 160,
    val height: Int = 144,
    val pixels: Array<Array<Pixel>> = Array(height) { Array(width){Pixel.PIXEL_0} }
){

    private var _x = 0
    var _y = 0

    val x get() = _x
    val y get() = _y

    private val listeners = mutableListOf<() -> Unit>()

    // LCD.kt: replace push() with these
    fun startLine(y: Int) { _y = y; _x = 0 }

    fun push(pixel: Pixel) {
        if (_x < width) pixels[_y][_x++] = pixel
    }

    fun refresh() = listeners.forEach { it() }

    fun addRefreshListener(listener: () -> Unit){
        listeners += listener
    }

    fun removeRefreshListener(listener: () -> Unit){
        listeners -= listener
    }

}