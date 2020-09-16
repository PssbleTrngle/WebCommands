package com.possible_triangle.webcommands

import java.io.File

object Docker {

    /**
     * If run inside a container, this should contain the docker container ID
     * @return The container ID if found
     */
    fun findContainer(): String? {
        val file = File("/etc/hostname")
        return if (file.exists()) {
             file.bufferedReader().readText()
        } else null
    }

}