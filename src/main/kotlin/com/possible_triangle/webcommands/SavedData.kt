package com.possible_triangle.webcommands

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonException
import java.io.*

object SavedData {

    private val JSON = Json(JsonConfiguration(ignoreUnknownKeys = true, prettyPrint = true))

    lateinit var DATA: Data

    @Serializable
    data class Data(val key: String?)

    private val FILE = File("data.json")

    fun load() {
        if (!FILE.exists()) save()

        val reader = BufferedReader(FileReader(FILE))
        val json = reader.readText()
        reader.close()

        try {
            DATA = JSON.parse(Data.serializer(), json)
        } catch (e: JsonException) {
            System.err.println("Error parsing data file at '${FILE.path}'")
            save()
        }
    }

    fun save() {
        if (!FILE.exists()) FILE.createNewFile()
        val writer = BufferedWriter(FileWriter(FILE))
        val json = JSON.stringify(Data.serializer(), DATA)
        writer.write(json)
        writer.close()
    }


}