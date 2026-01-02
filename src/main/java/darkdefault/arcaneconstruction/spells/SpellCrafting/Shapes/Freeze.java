package darkdefault.arcaneconstruction.spells.SpellCrafting.Shapes;

import darkdefault.arcaneconstruction.entity.SpellProjectileEntity;
import darkdefault.arcaneconstruction.spells.SpellCrafting.*;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.AOE;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.Duration;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Freeze extends SimpleDefaultModelShape {

    @Override
    public void onBlockHit(BlockHitResult blockHitResult, Spell spell, SpellProjectileEntity entity, SpellModule module) {
        World world = entity.getWorld();
        BlockPos blockPos = blockHitResult.getBlockPos();
        int radius = 2;
        radius = incrementFromAugments(radius, 3, AOE.class,module);

        if (world.isClient()){

            //Client
            float pitch = 1.5f - (0.5f*radius);
            world.playSound(
                    null,
                    blockPos.getX() + 0.5,
                    blockPos.getY() + 0.5,
                    blockPos.getZ() + 0.5,
                    SoundEvents.ENTITY_PLAYER_HURT_FREEZE,
                    SoundCategory.PLAYERS,
                    1.0f,
                    pitch
            );
        }

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {

                double distance = Math.sqrt(x * x + z * z);

                if (distance <= radius) {
                    BlockPos targetPos = blockPos.add(x, 0, z);

                    if (world.getBlockState(targetPos).isOf(Blocks.WATER)) {

                        world.setBlockState(targetPos, Blocks.FROSTED_ICE.getDefaultState());

                    }

                }
            }
        }

    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult,
                            Spell spell,
                            SpellProjectileEntity entity,
                            SpellModule module) {

        Entity target = entityHitResult.getEntity();
        World world = target.getWorld();
        BlockPos center = target.getBlockPos();


        if (target==entity.getOwner())return;

        int radius = 0;
        radius = incrementFromAugments(radius,2, AOE.class,module);

        int duration = 0;
        duration = incrementFromAugments(duration, 40, Duration.class, module);

        if (radius == 0) {
            target.setFrozenTicks(duration + 40);
            return;
        }



        float pitch = 1.5f - (0.5f*radius);
        world.playSound(
                null,
                center.getX() + 0.5,
                center.getY() + 0.5,
                center.getZ() + 0.5,
                SoundEvents.ENTITY_PLAYER_HURT_FREEZE,
                SoundCategory.PLAYERS,
                1.0f,
                pitch
        );

        if (world.isClient) return;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {

                    double distance = Math.sqrt(x * x + y * y + z * z);

                    if (distance <= radius) { // Only remove blocks inside the sphere
                        BlockPos targetPos = center.add(x, y, z);

                        if (world.getBlockState(targetPos).isOf(Blocks.AIR)) {

                            world.setBlockState(targetPos, Blocks.FROSTED_ICE.getDefaultState());

                        }

                    }
                }
            }
        }



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
                t.setFrozenTicks(duration+40);

            }
        }


    }

    @Override
    public void onSelfCast(Spell s, ServerPlayerEntity player, SpellModule module) {
        int duration = 20;
        duration = incrementFromAugments(duration, 20, Duration.class, module);
        int radius = 0;
        radius = incrementFromAugments(radius, 2,AOE.class, module);

        BlockPos blockPos = player.getBlockPos();
        World world = player.getWorld();

        if (world.isClient()){

            //Client
            float pitch = 1.5f - (0.5f*radius);
            world.playSound(
                    null,
                    blockPos.getX() + 0.5,
                    blockPos.getY() + 0.5,
                    blockPos.getZ() + 0.5,
                    SoundEvents.ENTITY_PLAYER_HURT_FREEZE,
                    SoundCategory.PLAYERS,
                    1.0f,
                    pitch
            );
        }
        else {

            //Server

            if (radius == 0){
                player.setFrozenTicks(duration);


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
                        t.setFrozenTicks(duration);

                    }
                }
            }


        }
    }

    @Override
    public void getTrailEffect(SpellProjectileEntity entity, SpellModule module) {

        World world = entity.getWorld();
        BlockPos blockPos = entity.getBlockPos();
        if (entity.getWorld().isClient()) {
            for (int i=0; i <= 1; i++){
                ThreadLocalRandom rand = ThreadLocalRandom.current();

                float random = 0.5f;

                double ox = rand.nextDouble(-random, random);
                double oy = rand.nextDouble(-random, random);
                double oz = rand.nextDouble(-random, random);



                entity.getWorld().addParticle(
                        ParticleTypes.SNOWFLAKE,
                        entity.getX() + ox,
                        entity.getY() + oy,
                        entity.getZ() + oz,
                        0, 0.0, 0.0
                );
            }
        }


        if(world.isClient) return;

        if (world.getBlockState(blockPos).isOf(Blocks.WATER)){
            BlockHitResult blockHitResult = new BlockHitResult(blockPos.toCenterPos(), Direction.UP,blockPos,true);



            if ( entity.getNoClipCount() > 0) {

                Spell s = entity.getSpell();

                for (SpellModule currentModule: s.getModules()) {
                    currentModule.getShape().onBlockHit(blockHitResult, s, entity, currentModule);


                }


            }

            //SERVER ONLY
            entity.addNoClipCount(-1);
            if (entity.getNoClipCount() == 0) {
                entity.discard();
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
        return "e";
    }

    @Override
    public String getName() {
        return "Freeze";
    }

    @Override
    public int getColor() {
        return ColorHelper.Argb.getArgb( 126, 208, 230);
    }

}
