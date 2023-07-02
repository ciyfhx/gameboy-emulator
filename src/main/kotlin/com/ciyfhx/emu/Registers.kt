package com.ciyfhx.emu

import com.ciyfhx.emu.opcodes.combineBytes
import java.util.*

enum class Flag {
    ZERO, SUBTRACT, HALF_CARRY, CARRY
}

fun interface OnRegisterChangedListener {
    fun onRegisterChanged()
}

/**
 * [CPU] Registers
 */
class Registers {

    private val onRegisterChangedListeners = mutableListOf<OnRegisterChangedListener>()

    fun addRegisterChangedListener(listener: OnRegisterChangedListener){
        onRegisterChangedListeners += listener
    }
    fun removeRegisterListener(listener: OnRegisterChangedListener) {
        onRegisterChangedListeners -= listener
    }
    private fun invokeChangeListener(){
        onRegisterChangedListeners.forEach { it.onRegisterChanged() }
    }

    var accumulator: UInt = 0u
        set(value) {
            invokeChangeListener()
            field = value
        }
    var flag = EnumSet.noneOf(Flag::class.java)

    var B: UInt = 0u
        set(value) {
            invokeChangeListener()
            field = value
        }
    var C: UInt = 0u
        set(value) {
            invokeChangeListener()
            field = value
        }

    var D: UInt = 0u
        set(value) {
            invokeChangeListener()
            field = value
        }
    var E: UInt = 0u
        set(value) {
            invokeChangeListener()
            field = value
        }

    var H: UInt = 0u
        set(value) {
            invokeChangeListener()
            field = value
        }
    var L: UInt = 0u
        set(value) {
            invokeChangeListener()
            field = value
        }

    var stackPointer: UInt = 0u
        set(value) {
            invokeChangeListener()
            field = value
        }
    var programCounter: UInt = 0u
        set(value) {
            invokeChangeListener()
            field = value
        }

    fun getBC(): UInt {
        return combineBytes(B.toUByte(), C.toUByte())
    }

    fun setBC(value: UInt){
        B = value and 0xF0u
        C = value and 0x0Fu
    }

    fun getDE(): UInt {
        return combineBytes(D.toUByte(), E.toUByte())
    }

    fun setDE(value: UInt){
        D = value and 0xF0u
        E = value and 0x0Fu
    }

    fun getHL(): UInt {
        return combineBytes(H.toUByte(), L.toUByte())
    }

    fun setHL(value: UInt){
        H = value and 0xF0u
        L = value and 0x0Fu
    }

    fun setZeroFlag(set: Boolean){
        if(set){
            flag.add(Flag.ZERO)
        }else{
            flag.remove(Flag.ZERO)
        }
    }

    fun getZeroFlag(): Boolean{
        return flag.contains(Flag.ZERO)
    }

    fun setSubtractFlag(set: Boolean){
        if(set){
            flag.add(Flag.SUBTRACT)
        }else{
            flag.remove(Flag.SUBTRACT)
        }
    }

    fun getSubtractFlag(): Boolean{
        return flag.contains(Flag.SUBTRACT)
    }

    fun setHalfCarryFlag(set: Boolean){
        if(set){
            flag.add(Flag.HALF_CARRY)
        }else{
            flag.remove(Flag.HALF_CARRY)
        }
    }

    fun getHalfCarryFlag(): Boolean{
        return flag.contains(Flag.HALF_CARRY)
    }

    fun setCarryFlag(set: Boolean){
        if(set){
            flag.add(Flag.CARRY)
        }else{
            flag.remove(Flag.CARRY)
        }
    }

    fun getCarryFlag(): Boolean{
        return flag.contains(Flag.CARRY)
    }

}