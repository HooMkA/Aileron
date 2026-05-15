package com.lodestar.aileron.mixin;

import com.lodestar.aileron.AileronConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerTravelMixin {

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void disableFireworkBoost(Vec3 travelVector, CallbackInfo ci) {

        System.out.println("AILERON PLAYER TRAVEL MIXIN WORKS");

        if (AileronConfig.fireworkUseBehaviour() != AileronConfig.FireworkUseBehaviour.DISABLE) {
            return;
        }

        Player player = (Player)(Object)this;

        // сохраняем обычное движение, но без элитр/фейерверк логики
        if (player.isFallFlying()) {

            // полностью отключаем обработку элитр-физики (включая стартовый буст)
            ci.cancel();

            // fallback обычного движения (без буста)
            player.setDeltaMovement(travelVector);
        }
    }
}