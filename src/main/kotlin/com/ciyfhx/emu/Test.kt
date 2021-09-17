package com.ciyfhx.emu

fun main() {
    val cpu = CPU()

    //Copy bootrom
    cpu.memory.copyByteArray(0, readBootRom())

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


        //Run Cpu
        cpu.fetch()
        cpu.decode()
        cpu.execute()
    }
    clock.start()
}