package com.possible_triangle.webcommands

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import java.net.URI
import org.java_websocket.server.WebSocketServer
import java.lang.Exception
import java.net.InetSocketAddress


object Websocket : WebSocketServer(InetSocketAddress(9000)) {

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
        TODO("Not yet implemented")
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String?, remote: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onMessage(conn: WebSocket, message: String) {
        TODO("Not yet implemented")
    }

    override fun onStart() {
        TODO("Not yet implemented")
    }

    override fun onError(conn: WebSocket, ex: Exception) {
        TODO("Not yet implemented")
    }

}