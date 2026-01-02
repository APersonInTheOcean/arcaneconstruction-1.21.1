package darkdefault.arcaneconstruction.spells.SpellCrafting.Shapes;

import darkdefault.arcaneconstruction.entity.SpellProjectileEntity;
import darkdefault.arcaneconstruction.spells.SpellCrafting.*;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.Power;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.Optional;
import java.util.function.Function;

public class Wind extends SimpleDefaultModelShape {

    @Override
    public void onBlockHit(BlockHitResult blockHitResult, Spell spell, SpellProjectileEntity entity, SpellModule module) {

        World world = entity.getWorld();
        float power= 1.25f;
        power = incrementFromAugments(power,0.25f, Power.class, module);

        ExplosionBehavior EXPLOSION_BEHAVIOR = new AdvancedExplosionBehavior(true, false, Optional.of(power), Registries.BLOCK.getEntryList(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));
        world.createExplosion(entity, (DamageSource)null, EXPLOSION_BEHAVIOR, entity.getX(), entity.getY(), entity.getZ(), power, false, World.ExplosionSourceType.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST);



    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult,
                            Spell spell,
                            SpellProjectileEntity projectile,
                            SpellModule module) {


        World world = projectile.getWorld();
        Entity entity = entityHitResult.getEntity();
        float power= 1.25f;
        power = incrementFromAugments(power,0.25f, Power.class, module);
        if (entity==projectile.getOwner())return;


        ExplosionBehavior EXPLOSION_BEHAVIOR = new AdvancedExplosionBehavior(true, false, Optional.of(power), Registries.BLOCK.getEntryList(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));
        world.createExplosion(projectile, (DamageSource)null, EXPLOSION_BEHAVIOR, entity.getX(), entity.getY(), entity.getZ(), power, false, World.ExplosionSourceType.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST);


    }

    @Override
    public void onSelfCast(Spell s, ServerPlayerEntity player, SpellModule module) {

        World world = player.getWorld();
        float power= 1.25f;
        power = incrementFromAugments(power,0.25f, Power.class, module);


        ExplosionBehavior EXPLOSION_BEHAVIOR = new AdvancedExplosionBehavior(true, false, Optional.of(power), Registries.BLOCK.getEntryList(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));
        world.createExplosion(null, (DamageSource)null, EXPLOSION_BEHAVIOR, player.getX(), player.getY(), player.getZ(), power, false, World.ExplosionSourceType.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.GUST_EMITTER_LARGE, SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST);
        player.setIgnoreFallDamageFromCurrentExplosion(true);

    }

    public float incrementFromAugments(float valueToIncrement, float amountToIncrement, Class<? extends Augment> augmentToCheck, SpellModule currentModule) {
        float howMuchToIncrement = valueToIncrement;
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
    public String getSequence() {
        return "ii";
    }

    @Override
    public String getName() {
        return "Wind";
    }

    @Override
    public int getColor() {
        return ColorHelper.Argb.getArgb( 200, 200,  220);
    }


}
