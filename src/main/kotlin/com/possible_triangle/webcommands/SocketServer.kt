@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.possible_triangle.webcommands

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.CommandOutput
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import java.net.URI
import org.java_websocket.server.WebSocketServer
import java.lang.Exception
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.CompletableFuture

class SocketServer(private val server: MinecraftServer) : WebSocketServer(InetSocketAddress(9000)) {

    @Serializable
    data class ServerMessage(val text: String, val sender: String?)

    @Serializable
    data class Message(val type: String, val command: String? = null, val data: String? = null)

    @Serializable
    data class Suggestion(val start: Int, val end: Int, val text: String, val tooltip: String?)

    @Serializable
    data class Error(val position: Int, val message: String, val context: String)

    @Serializable
    data class Check(val suggestions: List<Suggestion> = listOf(), val errors: List<Error> = listOf())

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
        println("Content: ${handshake.content}")
        conn.send(Json.stringify(Message.serializer(), Message("opened")))
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String?, remote: Boolean) {
        println("Connection closed")
    }

    override fun onMessage(conn: WebSocket, json: String) {
        val message = Json.parse(Message.serializer(), json)
        println("Received message: ${message.type}")
        when (message.type) {
            "check" -> checkCommand(message.command!!, conn)
        }
    }

    override fun onStart() {
        println("Opened WebCommand Socket")
    }

    override fun onError(conn: WebSocket, ex: Exception) {
        System.err.println("Encountered error: ${ex.message}")
    }

    private fun checkCommand(command: String, conn: WebSocket) {
        val source = server.commandSource
        val result = server.commandManager.dispatcher.parse(command, source)

        val check = if (result.exceptions.isNotEmpty()) {
            CompletableFuture.completedFuture(Check(errors = result.exceptions.map {
                Error(it.value.cursor, it.value.message!!, it.value.context)
            }))
        } else {
            server.commandManager.dispatcher.getCompletionSuggestions(result).handle { s, _ ->
                Check(suggestions = s.list.map {
                    Suggestion(it.range.start, it.range.end, it.text, it.tooltip?.string)
                })
            }
        }

        check.handle { c, e ->
            if (e != null) e.printStackTrace()
            else if (c != null) {
                val data = Json.stringify(Check.serializer(), c)
                val json = Json.stringify(Message.serializer(), Message("check", data = data))
                conn.send(json)
            }
        }
    }

    fun sendMessage(text: Text, sender: PlayerEntity?) {
        val message = Json.stringify(
            ServerMessage.serializer(), ServerMessage(
                text.asString(),
                sender?.displayName?.asString()
            )
        )
        broadcast(Json.stringify(Message.serializer(), Message("message", data = message)))
    }

}