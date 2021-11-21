package com.ciyfhx.emu


object RawFileReader {

    fun readRawFile(resourceName: String): ByteArray? {
        return this.javaClass.classLoader.getResourceAsStream("bootrom.gb").use {
            it?.readAllBytes()
        }
    }
}