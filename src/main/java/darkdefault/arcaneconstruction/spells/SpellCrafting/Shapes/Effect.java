package darkdefault.arcaneconstruction.spells.SpellCrafting.Shapes;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import darkdefault.arcaneconstruction.entity.SpellProjectileEntity;
import darkdefault.arcaneconstruction.rendering.ShapeModels.ModelData.BreakModelData;
import darkdefault.arcaneconstruction.rendering.ShapeModels.ModelData.EffectModelData;
import darkdefault.arcaneconstruction.rendering.ShapeModels.SigilModel;
import darkdefault.arcaneconstruction.rendering.ShapeModels.SigilModels.BreakModel;
import darkdefault.arcaneconstruction.rendering.ShapeModels.SigilModels.EffectModel;
import darkdefault.arcaneconstruction.spells.SpellCrafting.*;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.Duration;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.Power;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;

public class Effect extends SimpleShape {

    private static final int baseDuration = 1200;
    private static final int baseAmplifier = 0;


    @Environment(EnvType.CLIENT)
    @Override
    public SigilModel getSigilModel(ModelPart root) {
        return new EffectModel(root);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public EntityModelLayer getModelLayer() {
        return new EntityModelLayer(Identifier.of(ArcaneConstruction.MOD_ID, getName().toLowerCase()+ "model"), "main");
    }

    @Environment(EnvType.CLIENT)
    @Override
    public EntityModelLayerRegistry.TexturedModelDataProvider getModelDataSupplier() {
        return EffectModelData::getTexturedModelData;
    }
    @Override
    public void onBlockHit(BlockHitResult blockHitResult, Spell spell, SpellProjectileEntity entity, SpellModule module) {

        int duration = baseDuration;
        duration = incrementFromAugments(duration, 1200, Duration.class,module);
        int power = baseAmplifier;
        power = incrementFromAugments(power, 1, Power.class,module);
        BlockPos blockPos = blockHitResult.getBlockPos();

        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(entity.getWorld(), blockPos.getX(),blockPos.offset(Direction.UP,1).getY(),blockPos.getZ());
        boolean addedEffect =false;

        for (SpellModule currentModule: spell.getModules()){
            if(currentModule.getShape() instanceof Fire){
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,duration,power));
                addedEffect =true;

            }
            else if(currentModule.getShape() instanceof Explosion){
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,duration,power));
                addedEffect =true;


            }
            else if(currentModule.getShape() instanceof Wind){
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.LEVITATION,duration,power));
                addedEffect =true;


            }
            else if(currentModule.getShape() instanceof Break){
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.HASTE,duration,power));
                addedEffect =true;


            }
            else if(currentModule.getShape() instanceof Freeze){
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,duration,power));
                addedEffect =true;


            }


        }
        if (!addedEffect){
            areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,duration,power));
            EntityEffectParticleEffect entityEffectParticleEffect = EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT,getColor());
            areaEffectCloudEntity.setParticleType(entityEffectParticleEffect);


        }
        entity.getWorld().spawnEntity(areaEffectCloudEntity);


    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult, Spell spell, SpellProjectileEntity entity, SpellModule module) {

        int duration = baseDuration;
        duration = incrementFromAugments(duration, 1200, Duration.class,module);
        int power = baseAmplifier;
        power = incrementFromAugments(power, 1, Power.class,module);
        Entity target = entityHitResult.getEntity();
        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(target.getWorld(), target.getX(),target.getY(),target.getZ());
        boolean addedEffect =false;

        for (SpellModule currentModule: spell.getModules()){
            if(currentModule.getShape() instanceof Fire){
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,duration,power));
                addedEffect =true;

            }
            else if(currentModule.getShape() instanceof Explosion){
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,duration,power));
                addedEffect =true;


            }
            else if(currentModule.getShape() instanceof Wind){
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.LEVITATION,duration,power));
                addedEffect =true;


            }
            else if(currentModule.getShape() instanceof Break){
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.HASTE,duration,power));
                addedEffect =true;


            }

        }
        if (!addedEffect){
            areaEffectCloudEntity.addEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,duration,power));


        }
        target.getWorld().spawnEntity(areaEffectCloudEntity);

    }

    @Override
    public void onSelfCast(Spell s, ServerPlayerEntity player, SpellModule module) {
        int duration = baseDuration;
        duration = incrementFromAugments(duration, 1200, Duration.class,module);
        int power = baseAmplifier;
        power = incrementFromAugments(power, 1, Power.class,module);
        boolean addedEffect =false;
        for (SpellModule currentModule: s.getModules()){
            if(currentModule.getShape() instanceof Fire){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,duration,power));
                addedEffect =true;

            }
            else if(currentModule.getShape() instanceof Explosion){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,duration,power));
                addedEffect =true;


            }
            else if(currentModule.getShape() instanceof Wind){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION,duration,power));
                addedEffect =true;


            }
            else if(currentModule.getShape() instanceof Break){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE,duration,power));
                addedEffect =true;


            }
            else if(currentModule.getShape() instanceof Heal){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,duration,power));
                addedEffect =true;


            }

        }
        if (!addedEffect){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,duration,power));


        }

    }
    public int incrementFromAugments(int valueToIncrement, int amountToIncrement, Class<? extends Augment> augmentToCheck, SpellModule currentModule) {
        int howMuchToIncrement = valueToIncrement;
        for (Augment a: currentModule.getAugments()) {
            if (augmentToCheck.isInstance(a)) {
                howMuchToIncrement+= amountToIncrement;
            }
        }
        return howMuchToIncrement;
    }

    @Override
    public void getTrailEffect(SpellProjectileEntity entity, SpellModule module) {

    }

    @Override
    public boolean shouldInterruptOnBlockHit() {
        return true;
    }

    @Override
    public boolean shouldInterruptOnEntityHit() {
        return true;
    }

    @Override
    public boolean shouldInterruptOnSelfCast() {
        return true;
    }

    @Override
    public String getSequence() {
        return "ic";
    }

    @Override
    public String getName() {
        return "Effect";
    }

    @Override
    public int getColor() {
        return ColorHelper.Argb.getArgb( 41, 19, 61);
    }

}
