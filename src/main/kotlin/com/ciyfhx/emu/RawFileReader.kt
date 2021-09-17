package com.ciyfhx.emu

fun readBootRom(): ByteArray {
    return RawFileReader.readRawFile("bootrom.gb")
}

object RawFileReader {

    fun readRawFile(resourceName: String): ByteArray {
        return this.javaClass.classLoader.getResourceAsStream("bootrom.gb").readAllBytes()
    }
}