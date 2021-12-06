package com.ciyfhx.emu.viewer

import com.ciyfhx.emu.CPU
import com.ciyfhx.emu.GameBoyMemory
import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.Registers
import tornadofx.*


class EmuApp: App(RamViewer::class)
class RamViewer: View() {
    val controller: RamViewerController by inject()

    val ramValues = List(0x8000){
        controller.memory.read(it)
    }.asObservable()

    init {
        controller.memory.addWriteListener{
            ramValues[it.address] = it
        }
    }

    override val root = vbox {
//        controller
        listview(ramValues)
//        ramListView
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
        return (index until (index+size)).map(memory::read).map{it.toString()}
    }

}


class EmuContext: ViewModel(){
    val registers = Registers()
    val memory = GameBoyMemory(registers)
    val cpu = CPU(memory, registers)

     fun start(){
         cpu.start()
     }
}

fun main(args: Array<String>) {
    launch<EmuApp>(args)
}