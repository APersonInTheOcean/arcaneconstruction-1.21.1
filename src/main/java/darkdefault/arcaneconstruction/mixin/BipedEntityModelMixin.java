package darkdefault.arcaneconstruction.mixin;


import darkdefault.arcaneconstruction.spells.SpellHandling;
import darkdefault.arcaneconstruction.util.IEntityDataSaver;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)

public class BipedEntityModelMixin<T extends LivingEntity> {
    @Shadow
    public ModelPart rightArm;
    @Shadow
    public ModelPart leftArm;
    @Shadow
    public ModelPart head;

    @Shadow
    public BipedEntityModel.ArmPose rightArmPose;
    @Shadow
    public BipedEntityModel.ArmPose leftArmPose;



    @Inject(
            method = "positionRightArm",
            at = @At("HEAD"),
            cancellable = true
    )
    private void arcane$castingRightArm(T entity, CallbackInfo ci) {
        if (!(entity instanceof PlayerEntity player)) return;
        if (!SpellHandling.isCastingProjectile((IEntityDataSaver) player)) return;

        if (player.getMainArm() == Arm.RIGHT) {
            // CASTING ARM
            this.rightArm.pitch = -1.6F;
            this.rightArm.yaw = 0.3F;
            this.rightArm.roll = 0.0F;
        } else {
            // NON-CASTING ARM → reset / idle
            this.rightArm.pitch = 0.0F;
            this.rightArm.yaw = 0.0F;
            this.rightArm.roll = 0.0F;
        }

        ci.cancel(); // ALWAYS cancel when casting
    }
    @Inject(
            method = "positionLeftArm",
            at = @At("HEAD"),
            cancellable = true
    )
    private void arcane$castingLeftArm(T entity, CallbackInfo ci) {
        if (!(entity instanceof PlayerEntity player)) return;
        if (!SpellHandling.isCastingProjectile((IEntityDataSaver) player)) return;

        if (player.getMainArm() == Arm.LEFT) {
            // CASTING ARM
            this.leftArm.pitch = -1.6F;
            this.leftArm.yaw = 0.3F;
            this.leftArm.roll = 0.0F;
        } else {
            // NON-CASTING ARM → reset / idle
            this.leftArm.pitch = 0.0F;
            this.leftArm.yaw = 0.0F;
            this.leftArm.roll = 0.0F;
        }

        ci.cancel(); // ALWAYS cancel when casting
    }


}
