package com.ciyfhx.emu.viewer

import com.ciyfhx.emu.CPU
import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.MemoryRegion
import com.ciyfhx.emu.Registers
import com.ciyfhx.emu.mapper.BootRom
import com.ciyfhx.emu.opcodes.toHexCode
import tornadofx.*


class EmuApp: App(RamViewer::class)
class RamViewer: View() {
    val controller: RamViewerController by inject()

    private val ramListView =
        LazyListView(RamViewerMemoryLazyLoader(controller.memory), 0xFFFF)

    override val root = vbox {
        controller
        ramListView
    }


}

class RamViewerController: Controller(){
    private val context: EmuContext by inject()
    var memory = context.cpu.memory

}

class RamViewerMemoryLazyLoader(
    val memory: Memory
) : LazyLoader {

    init {
        memory.addWriteListener{

        }
    }

    override fun loadItems(index: Int, size: Int): Collection<String> {
        return (index until (index+size)).map(memory::read).map{it.toHexCode()}
    }

}


class EmuContext: ViewModel(){
    val registers = Registers()
    val memory = Memory(registers)
    val cpu = CPU(memory, registers)

     fun start(){
         //Copy bootrom
         memory.registerMemoryMapper(BootRom(), MemoryRegion(0))
         cpu.start()
     }
}

fun main(args: Array<String>) {
    launch<EmuApp>(args)
}