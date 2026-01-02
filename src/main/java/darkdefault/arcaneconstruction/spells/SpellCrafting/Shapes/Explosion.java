package darkdefault.arcaneconstruction.spells.SpellCrafting.Shapes;

import darkdefault.arcaneconstruction.entity.SpellProjectileEntity;
import darkdefault.arcaneconstruction.spells.SpellCrafting.*;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.AOE;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.Power;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Explosion extends SimpleDefaultModelShape {



    @Override
    public String getSequence() {
        return "iceec";
    }

    @Override
    public String getName() {
        return "Explosion";
    }

    @Override
    public int getColor() {
        return ColorHelper.Argb.getArgb( 200, 0, 0);
    }

    @Override
    public void onBlockHit(BlockHitResult blockHitResult, Spell spell, SpellProjectileEntity entity, SpellModule module) {
        int radius = 2;
        BlockPos blockPos = blockHitResult.getBlockPos();
        World world =  entity.getWorld();

        //Radius

        radius = incrementFromAugments(radius,1, Power.class,module);
        radius = incrementFromAugments(radius,2, AOE.class,module);


        spawnParticles(entity,25, 1f, ParticleTypes.EXPLOSION);

        if(world.isClient) return;


        float pitch = 1.5f - (0.1f*radius);
        world.playSound(
                null,
                blockPos.getX() + 0.5,
                blockPos.getY() + 0.5,
                blockPos.getZ() + 0.5,
                SoundEvents.ENTITY_GENERIC_EXPLODE,
                SoundCategory.NEUTRAL,
                1.0f,
                pitch
        );


        float randomEdge = 0.5f;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {

                    double distance = Math.sqrt(x * x + y * y + z * z);

                    ThreadLocalRandom rand = ThreadLocalRandom.current();

                    double haze = rand.nextDouble(-randomEdge, randomEdge);




                    if (distance-haze <= radius) { // Only remove blocks inside the sphere
                        BlockPos targetPos = blockPos.add(x, y, z);

                        // Stops from removing bedrock
                        if (world.getBlockState(targetPos).isIn(BlockTags.AXE_MINEABLE)
                                || world.getBlockState(targetPos).isIn(BlockTags.HOE_MINEABLE)
                                || world.getBlockState(targetPos).isIn(BlockTags.PICKAXE_MINEABLE)
                                || world.getBlockState(targetPos).isIn(BlockTags.SHOVEL_MINEABLE)) {

                            boolean shouldDropBlocks = world.random.nextFloat() < 0.1;
                            world.breakBlock(targetPos, shouldDropBlocks, entity.getOwner());

                        }
                    }
                }
            }
        }
        hurtEntitiesInExplosion(entity,world,blockPos,radius);

    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult,
                            Spell spell,
                            SpellProjectileEntity entity,
                            SpellModule module) {
        int amountOfFire = 0;
        int radius = 2;
        Entity target = entityHitResult.getEntity();

        BlockPos blockPos = target.getBlockPos();
        World world =  target.getWorld();




        //Radius

        radius = incrementFromAugments(radius,1, Power.class,module);
        radius = incrementFromAugments(radius,2, AOE.class,module);


        spawnParticles(entity,25, 1f, ParticleTypes.EXPLOSION);

        if(world.isClient) return;


        float pitch = 1.5f - (0.1f*radius);
        world.playSound(
                null,
                blockPos.getX() + 0.5,
                blockPos.getY() + 0.5,
                blockPos.getZ() + 0.5,
                SoundEvents.ENTITY_GENERIC_EXPLODE,
                SoundCategory.NEUTRAL,
                1.0f,
                pitch
        );


        float randomEdge = 0.5f;


        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {

                    double distance = Math.sqrt(x * x + y * y + z * z);

                    ThreadLocalRandom rand = ThreadLocalRandom.current();



                    double haze = rand.nextDouble(-randomEdge, randomEdge);
                    if (distance-haze <= radius) { // Only remove blocks inside the sphere
                        BlockPos targetPos = blockPos.add(x, y, z);

                        // Stops from removing bedrock
                        if (world.getBlockState(targetPos).isIn(BlockTags.AXE_MINEABLE)
                                || world.getBlockState(targetPos).isIn(BlockTags.HOE_MINEABLE)
                                || world.getBlockState(targetPos).isIn(BlockTags.PICKAXE_MINEABLE)
                                || world.getBlockState(targetPos).isIn(BlockTags.SHOVEL_MINEABLE)) {

                            boolean shouldDropBlocks = world.random.nextFloat() < 0.1;
                            world.breakBlock(targetPos, shouldDropBlocks, entity.getOwner());

                        }


                    }
                }
            }
        }

        hurtEntitiesInExplosion(entity,world,blockPos,radius);


    }

    @Override
    public void onSelfCast(Spell s, ServerPlayerEntity player, SpellModule module) {

    }

    public int incrementFromAugments(int valueToIncrement,int amountToIncrement, Class<? extends Augment> augmentToCheck, SpellModule currentModule) {
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
        spawnParticles(entity, 25, 0.3f, ParticleTypes.SMOKE);
    }

    public void hurtEntitiesInExplosion(
            SpellProjectileEntity projectile,
            World world,
            BlockPos center,
            int radius)
    {
        radius = (int )(radius * 1.5);
        if (world.isClient) return;

        Box box = Box.of(center.toCenterPos(), radius * 2, radius * 2, radius * 2);

        List<LivingEntity> entities = world.getEntitiesByClass(
                LivingEntity.class,
                box,
                e -> e.isAlive()
//                        && e != projectile.getOwner()
        );

        for (LivingEntity target : entities) {
            double distanceSq = target.squaredDistanceTo(
                    center.getX() + 0.5,
                    center.getY() + 0.5,
                    center.getZ() + 0.5
            );

            if (distanceSq <= radius * radius) {
                target.damage(
                        world.getDamageSources().explosion(projectile, projectile.getOwner()),
                        radius * 5
                );
            }

            //KNOCKBACK
            Vec3d explosionCenter = center.toCenterPos();

            double dx = target.getX() - explosionCenter.x;
            double dy = target.getBodyY(0.5) - explosionCenter.y;
            double dz = target.getZ() - explosionCenter.z;

            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (distance == 0) continue;

// Normalize direction
            dx /= distance;
            dy /= distance;
            dz /= distance;

// Distance falloff (closer = stronger)
            double strength = 2.0 - (distance / radius);
            strength = Math.max(strength, 0);

// Knockback power (tweak this)
            double power = 0.8;

// Apply velocity
            target.addVelocity(
                    dx * strength * power,
                    dy * strength * power,
                    dz * strength * power
            );

// Required for server → client sync
            target.velocityModified = true;
        }


    }

    public void spawnParticles(Entity entity, int amount, float spread, ParticleEffect particle){
        if (entity.getWorld().isClient){


            for (int i=0; i <= amount; i++){
                ThreadLocalRandom rand = ThreadLocalRandom.current();

                double ox = rand.nextDouble(-spread, spread);
                double oy = rand.nextDouble(-spread, spread);
                double oz = rand.nextDouble(-spread, spread);



                entity.getWorld().addParticle(
                        particle,
                        entity.getX() + ox,
                        entity.getY() + oy,
                        entity.getZ() + oz,
                        0, 0, 0
                );
            }
        }
    }


}
