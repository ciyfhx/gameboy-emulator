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

    fun push(pixel: Pixel){
        pixels[_y][_x] = pixel
        _x++
        if(_x == width){
            _y++
            _x = 0
            if(_y == height){
                _y = 0
                listeners.forEach{listener -> listener()}
            }
        }
    }

    fun addRefreshListener(listener: () -> Unit){
        listeners += listener
    }

    fun removeRefreshListener(listener: () -> Unit){
        listeners -= listener
    }

}