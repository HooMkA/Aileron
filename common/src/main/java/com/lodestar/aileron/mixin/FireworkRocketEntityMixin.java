package com.lodestar.aileron.mixin;

import com.lodestar.aileron.AileronConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin {

    @Shadow
    private int life;

    @Inject(method = "tick", at = @At("HEAD"))
    private void aileron$disableBoost(CallbackInfo ci) {

        System.out.println("AILERON FIREWORK ROCKET ENTITY MIXIN WORKS");

        if (AileronConfig.fireworkUseBehaviour()
                != AileronConfig.FireworkUseBehaviour.DISABLE) return;

        FireworkRocketEntity self = (FireworkRocketEntity)(Object)this;

        if (!(self.getOwner() instanceof Player player)) return;

        // важно: только если реально используется для элитр
        if (!player.isFallFlying()) return;

        /*
         * КЛЮЧЕВАЯ ИДЕЯ:
         * мы НЕ трогаем velocity напрямую
         * мы ломаем сам "boost eligibility"
         */

        // 1. убираем эффект ускорения через мгновенное завершение жизни ракеты
        this.life = Integer.MAX_VALUE - 1;

        // 2. обнуляем внутренний tick-эффект перед применением импульса
        self.setDeltaMovement(0, 0, 0);
    }
}