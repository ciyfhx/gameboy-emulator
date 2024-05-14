package com.ciyfhx.emu

import java.io.File


object RawFileReader {

    fun readRawFileFromResources(resourceName: String): ByteArray? {
        return this.javaClass.classLoader.getResourceAsStream(resourceName).use {
            it?.readAllBytes()
        }
    }

    fun readRawFileFromPath(path: String): ByteArray {
        return File(path).readBytes()
    }
}