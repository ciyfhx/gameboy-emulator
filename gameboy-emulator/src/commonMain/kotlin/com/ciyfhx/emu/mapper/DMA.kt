package com.ciyfhx.emu.mapper

import com.ciyfhx.emu.Clock
import com.ciyfhx.emu.Memory
import com.ciyfhx.emu.MemoryMapper
import com.ciyfhx.emu.opcodes.combineBytes

class DMA : MemoryMapper {

    private lateinit var memory: Memory
    private var dmaIsTransfering = false
    private var dmaTransferSource = 0

    private val dmaTransferDestination = 0xFE00
    private val dmaTransferLength = 0xA0
    private var dmaTransferCount = 0
    //160 hertz
    private val clock = Clock(1000000){
        if (dmaIsTransfering) {

            //Transfer byte from source to destination
            memory.write(dmaTransferDestination + dmaTransferCount, memory.read(dmaTransferSource + dmaTransferCount).value)
            dmaTransferCount++

            if(dmaTransferCount==dmaTransferLength){
                dmaIsTransfering = false
                stopClock()
            }
        }
    }

    override fun initMemory(memory: Memory) {
        this.memory = memory
    }

    override fun read(memoryEntryRead: Memory.MemoryEntry): Memory.MemoryEntry {
        return memoryEntryRead
    }

    override fun write(memoryEntryWrite: Memory.MemoryEntry): Memory.MemoryEntry {
        if(memoryEntryWrite.address == 0xFF46) {
            //Start DMA transfer
            val source = combineBytes(memoryEntryWrite.value, 0x00u);
            startDMATransfer(source)
        }
        return memoryEntryWrite
    }

    private fun startDMATransfer(source: UInt) {
        dmaIsTransfering = true
        dmaTransferSource = source.toInt()
        dmaTransferCount = 0
        clock.start()
    }

    private fun stopClock(){
        clock.stop()
    }

}