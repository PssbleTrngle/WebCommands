package com.possible_triangle.webcommands.mixin

import com.possible_triangle.webcommands.SocketServer
import net.minecraft.server.MinecraftServer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.*
import net.minecraft.text.Text

@Suppress("CAST_NEVER_SUCCEEDS")
@Mixin(MinecraftServer::class)
class MinecraftServerMixin {

    private var socket: SocketServer? = null

    @Inject(at = [At("RETURN")], method = ["Lnet/minecraft/server/MinecraftServer;stop(Z)V"])
    fun stop(bl: Boolean, callback: CallbackInfo) {
        socket?.stop()
    }

    @Inject(at = [At("RETURN")], method = ["<init>"])
    fun start(callback: CallbackInfo) {
        socket = SocketServer(this as MinecraftServer)
        socket?.start()
    }

    @Inject(at = [At("RETURN")], method = ["Lnet/minecraft/server/MinecraftServer;sendSystemMessage(Lnet/minecraft/text/Text;Ljava/util/UUID;)V"])
    fun message(message: Text, sender: UUID, callback: CallbackInfo) {
        val server = this as MinecraftServer
        val player = server.playerManager.getPlayer(sender)
        socket?.sendMessage(message, player)
    }

}