package darkdefault.arcaneconstruction.mixin;

import com.mojang.authlib.GameProfile;
import darkdefault.arcaneconstruction.ArcaneConstruction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {


    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo ci) {
        ArcaneConstruction.onPlayerTick((PlayerEntity) (Object)this);
    }
}
