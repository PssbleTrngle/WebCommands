package com.possible_triangle.webcommands.mixin;

import com.possible_triangle.webcommands.RestClient;
import com.possible_triangle.webcommands.SocketServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.UUID;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Nullable
    private SocketServer socket = null;

    @Inject(at = @At("RETURN"), method = "stop(Z)V")
    public void stop(boolean bl, CallbackInfo callback) {
        RestClient.INSTANCE.onStop((MinecraftServer) (Object) this);
        if(socket != null) {
            try {
                socket.stop();
            } catch (IOException | InterruptedException e) {
                System.err.println("An error occurred on closing the websocket");
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    public void start(CallbackInfo callback) {
        RestClient.INSTANCE.onStart((MinecraftServer) (Object) this);
        socket = new SocketServer((MinecraftServer) (Object) this);
        socket.start();
    }

    @Inject(at = @At("RETURN"), method = "sendSystemMessage(Lnet/minecraft/text/Text;Ljava/util/UUID;)V")
    public void message(Text message, UUID sender, CallbackInfo callback) {
        if(socket != null) {
            ServerPlayerEntity player = ((MinecraftServer) (Object) this).getPlayerManager().getPlayer(sender);
            socket.sendMessage(message, player);
        }
    }

}
