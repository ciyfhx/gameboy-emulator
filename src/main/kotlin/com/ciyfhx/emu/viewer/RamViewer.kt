package com.ciyfhx.emu.viewer

import com.ciyfhx.emu.CPU
import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.Registers
import com.ciyfhx.emu.readBootRom
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.*


class EmuApp: App(RamViewer::class)

class RamViewer: View() {
    val controller: RamViewerController by inject()

    override val root = vbox {
        listview(controller.ramValues)
    }
}

class RamViewerController: Controller(){
    private val context: EmuContext by inject()
    var memory: Memory? = null

    init {
        initMemoryList()
    }

    val ramValues: ObservableList<Memory.MemoryEntry>? get() {
        val memory = this.memory
        return if(memory is ObservableMemory){
            memory.observableList
        } else null
    }

    private fun initMemoryList() {
        context.start()
        memory = context.cpu.memory
    }

    class ObservableMemory(registers: Registers): Memory(registers) {
        val observableList = FXCollections.observableList(this.memory.toList())

        override fun write(address: UInt, value: UByte) {
            observableList.invalidate()
            super.write(address, value)
        }
    }

}

class EmuContext: ViewModel(){
    val registers = Registers()
    val memory = RamViewerController.ObservableMemory(registers)
    val cpu = CPU(memory, registers)

     fun start(){
         //Copy bootrom
         cpu.memory.copyByteArray(0, readBootRom()!!)
         cpu.start()
     }
}

fun main(args: Array<String>) {
    launch<EmuApp>(args)
}