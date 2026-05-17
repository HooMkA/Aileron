package com.lodestar.aileron.mixin;

import com.lodestar.aileron.AileronConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.Items;
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

        if (AileronConfig.fireworkUseBehaviour()
                != AileronConfig.FireworkUseBehaviour.COSMETIC_NO_BOOST) {
            return;
        }

        FireworkRocketEntity self = (FireworkRocketEntity)(Object)this;

        if (!(self.getOwner() instanceof Player player)) {
            return;
        }

        if (!player.isFallFlying()) {
            return;
        }

        this.life = Integer.MAX_VALUE - 1;

        player.getCooldowns().addCooldown(
                Items.FIREWORK_ROCKET,
                200
        );
    }
}