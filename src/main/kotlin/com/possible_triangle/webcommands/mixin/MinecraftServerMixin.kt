package com.possible_triangle.webcommands.mixin

import net.minecraft.server.MinecraftServer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Suppress("CAST_NEVER_SUCCEEDS")
@Mixin(MinecraftServer::class)
class MinecraftServerMixin {

    @Inject(at = [At("RETURN")], method = ["Lnet/minecraft/server/MinecraftServer;stop(Z)V"])
    fun stop(bl: Boolean, callback: CallbackInfo) {
        val server = this as MinecraftServer

    }

    @Inject(at = [At("RETURN")], method = ["<init>"])
    fun start(bl: Boolean, callback: CallbackInfo) {
        val server = this as MinecraftServer

    }

}