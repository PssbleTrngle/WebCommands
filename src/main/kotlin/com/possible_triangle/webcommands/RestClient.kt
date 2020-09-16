package com.possible_triangle.webcommands

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import net.minecraft.server.MinecraftServer
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object RestClient {

    private val HOST = "localhost:8080"
    private val BASE_URL = "http://$HOST/api/"
    private val JSON = Json(JsonConfiguration(ignoreUnknownKeys = true))

    @Serializable
    data class ServerData(val motd: String, val container: String?)

    fun onStart(server: MinecraftServer) {
        val data = ServerData(server.serverMotd, Docker.findContainer())
        GlobalScope.launch {
            if(SavedData.DATA.key == null) login().join()
            post("server/start", JSON.stringify(ServerData.serializer(), data))
        }
    }

    fun onStop(server: MinecraftServer) {
        if(SavedData.DATA.key != null) {
            post("server/stop")
        }
    }

    fun login(): Job {
        return GlobalScope.launch {
            val createdKey = post("server/create")
            SavedData.DATA = SavedData.Data(createdKey)
            SavedData.save()
        }
    }

    private fun get(endpoint: String): String {
        return method("GET", endpoint)
    }

    private fun post(endpoint: String, body: String? = null): String {
        return method("POST", endpoint, body)
    }

    private fun put(endpoint: String, body: String? = null): String {
        return method("PUT", endpoint, body)
    }

    private fun method(method: String, endpoint: String, body: String? = null): String {
        val url = URL(BASE_URL + endpoint)
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = method

            val key = SavedData.DATA.key
            if (key != null) setRequestProperty("Authorization", "Token $key")

            if (body != null) {
                doOutput = true
                val wr = OutputStreamWriter(outputStream);
                wr.write(body);
                wr.flush();
            }

            val response = StringBuffer()
            BufferedReader(InputStreamReader(inputStream)).use {

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }

            }

            return response.toString()
        }
    }

}