package com.turastory.shanghycon.util

import java.util.concurrent.Executors

/**
 * Created by tura on 2018-09-15.
 */
object Exec {
    private val executor = Executors.newFixedThreadPool(3)

    fun work(task: () -> Unit) = executor.submit(task)
}