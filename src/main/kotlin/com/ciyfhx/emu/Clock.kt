package com.ciyfhx.emu

import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Clock/Timer of the [CPU], this will determine how fast the [CPU] will run
 */
class Clock(
    val hz: Long = 4_194_304,
    val run: () -> Unit
) {
    private val NANOSECOND = 10_0000_0000L
    private var running: Boolean = false

    private val executor = Executors.newSingleThreadScheduledExecutor()

    /**
     * Start the clock
     */
    fun start(){
        if(running) throw IllegalStateException("Clock is already running")
        running = true
        val period = NANOSECOND / hz
        val future = executor.scheduleAtFixedRate(run, 0, period, TimeUnit.NANOSECONDS)
        try {
            future.get()
        }catch (e: ExecutionException){
            e.printStackTrace()
        }
    }

    /**
     * Stop the clock
     */
    fun stop(){
        if(!running) throw IllegalStateException("Clock is not running")
        running = false

        executor.shutdownNow()
    }

}