package darkdefault.arcaneconstruction.spells.SpellCrafting.Shapes;

import darkdefault.arcaneconstruction.entity.SpellProjectileEntity;
import darkdefault.arcaneconstruction.spells.SpellCrafting.*;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.AOE;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.Duration;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.Power;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Heal extends SimpleDefaultModelShape {
    private int baseHeal = 6;
    private int powerAugmentIncrease = 2;
    @Override
    public void onBlockHit(BlockHitResult blockHitResult, Spell spell, SpellProjectileEntity entity, SpellModule module) {
        World world = entity.getWorld();
        BlockPos blockPos = blockHitResult.getBlockPos();
        int radius = 2;
        radius = incrementFromAugments(radius, 3, AOE.class,module);

        float pitch = 1.5f - (0.5f*radius);
        world.playSound(
                null,
                blockPos.getX() + 0.5,
                blockPos.getY() + 0.5,
                blockPos.getZ() + 0.5,
                SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                SoundCategory.PLAYERS,
                1.0f,
                pitch
        );
        if(world.isClient) return;
        

        int healAmount = baseHeal;
        healAmount = incrementFromAugments(healAmount, powerAugmentIncrease, Power.class, module);


        radius = (int )(radius * 1.5);

        Box box = Box.of(blockPos.toCenterPos(), radius * 2, radius * 2, radius * 2);

        List<LivingEntity> entities = world.getEntitiesByClass(
                LivingEntity.class,
                box,
                e -> e.isAlive()
//                        && e != projectile.getOwner()
        );

        for (LivingEntity t : entities) {
            double distanceSq = t.squaredDistanceTo(
                    blockPos.getX() + 0.5,
                    blockPos.getY() + 0.5,
                    blockPos.getZ() + 0.5
            );

            if (distanceSq <= radius * radius) {
                t.heal(healAmount);
                t.getWorld().addParticle(
                        ParticleTypes.HEART,
                        t.getX(),
                        t.getY(),
                        t.getZ(),
                        1, 0.0, 1.0
                );


            }
        }

    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult,
                            Spell spell,
                            SpellProjectileEntity entity,
                            SpellModule module) {

        Entity targetOg = entityHitResult.getEntity();
        World world = targetOg.getWorld();
        BlockPos center = targetOg.getBlockPos();


        if (targetOg==entity.getOwner())return;
        if(!(targetOg instanceof LivingEntity target)) return;

        int radius = 0;
        radius = incrementFromAugments(radius,2, AOE.class,module);

        int healAmount = baseHeal;
        healAmount = incrementFromAugments(healAmount, powerAugmentIncrease, Power.class, module);

        if (radius == 0) {
            target.heal(healAmount);
            target.getWorld().addParticle(
                    ParticleTypes.HEART,
                    target.getX(),
                    target.getY(),
                    target.getZ(),
                    1, 0.0, 1.0
            );

            return;
        }



        float pitch = 1.5f - (0.5f*radius);
        world.playSound(
                null,
                center.getX() + 0.5,
                center.getY() + 0.5,
                center.getZ() + 0.5,
                SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                SoundCategory.PLAYERS,
                1.0f,
                pitch
        );

        if (world.isClient) return;




        radius = (int )(radius * 1.5);

        Box box = Box.of(center.toCenterPos(), radius * 2, radius * 2, radius * 2);

        List<LivingEntity> entities = world.getEntitiesByClass(
                LivingEntity.class,
                box,
                e -> e.isAlive()
//                        && e != projectile.getOwner()
        );

        for (LivingEntity t : entities) {
            double distanceSq = t.squaredDistanceTo(
                    center.getX() + 0.5,
                    center.getY() + 0.5,
                    center.getZ() + 0.5
            );

            if (distanceSq <= radius * radius) {
                t.heal(healAmount);
                t.getWorld().addParticle(
                        ParticleTypes.HEART,
                        t.getX(),
                        t.getY(),
                        t.getZ(),
                        1, 0.0, 1.0
                );
            }
        }


    }

    @Override
    public void onSelfCast(Spell s, ServerPlayerEntity player, SpellModule module) {
        int healAmount = baseHeal;
        healAmount = incrementFromAugments(healAmount, powerAugmentIncrease, Power.class, module);
        int radius = 0;
        radius = incrementFromAugments(radius, 2,AOE.class, module);

        BlockPos blockPos = player.getBlockPos();
        World world = player.getWorld();

        if (world.isClient()) return;
        else {
            float pitch = 1.5f - (0.5f*radius);
            world.playSound(
                    null,
                    blockPos.getX() + 0.5,
                    blockPos.getY() + 0.5,
                    blockPos.getZ() + 0.5,
                    SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                    SoundCategory.PLAYERS,
                    1.0f,
                    pitch
            );
            //Server

            if (radius == 0){
                player.heal(healAmount);
                player.getWorld().addParticle(
                        ParticleTypes.HEART,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        1, 0.0, 1.0
                );



            }
            else {
                Box box = Box.of(blockPos.toCenterPos(), radius * 2, radius * 2, radius * 2);

                List<LivingEntity> entities = world.getEntitiesByClass(
                        LivingEntity.class,
                        box,
                        e -> e.isAlive()
                );

                for (LivingEntity t : entities) {
                    double distanceSq = t.squaredDistanceTo(
                            blockPos.getX() + 0.5,
                            blockPos.getY() + 0.5,
                            blockPos.getZ() + 0.5
                    );

                    if (distanceSq <= radius * radius) {
                        t.heal(healAmount);
                        t.getWorld().addParticle(
                                ParticleTypes.HEART,
                                t.getX(),
                                t.getY(),
                                t.getZ(),
                                1, 0.0, 1.0
                        );

                    }
                }
            }


        }
    }

    @Override
    public void getTrailEffect(SpellProjectileEntity entity, SpellModule module) {

        if (entity.getWorld().isClient()) {
            for (int i=0; i <= 1; i++){
                ThreadLocalRandom rand = ThreadLocalRandom.current();

                float random = 0.5f;

                double ox = rand.nextDouble(-random, random);
                double oy = rand.nextDouble(-random, random);
                double oz = rand.nextDouble(-random, random);



                entity.getWorld().addParticle(
                        ParticleTypes.HEART,
                        entity.getX() + ox,
                        entity.getY() + oy,
                        entity.getZ() + oz,
                        0, 0.0, 0.0
                );
            }
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
    public String getSequence() {
        return "ei";
    }

    @Override
    public String getName() {
        return "Heal";
    }

    @Override
    public int getColor() {
        return ColorHelper.Argb.getArgb( 168, 50, 52);
    }

}
