package com.ciyfhx.emu

fun main() {
    var last = System.currentTimeMillis()
    var counter = 0
    val clock = Clock{
        val now = System.currentTimeMillis()
        val diff = now - last

        //One second has passed
        if(diff >= 1000){
            println("Hz: $counter")
            last = now
            counter = 0
        }
        counter++

    }
    clock.start()
}