package darkdefault.arcaneconstruction.networking.packet;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import darkdefault.arcaneconstruction.networking.ModMessages;
import darkdefault.arcaneconstruction.particle.SpellProjectileParticleEffect;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Spell;
import darkdefault.arcaneconstruction.spells.SpellCrafting.SpellModule;
import darkdefault.arcaneconstruction.spells.SpellHandling;
import darkdefault.arcaneconstruction.util.IEntityDataSaver;
import darkdefault.arcaneconstruction.util.SigilData;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;
import java.util.UUID;

public class AddSigilC2SPacket {

    public static void receive(ModMessages.AddSigilC2SPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity p = context.player();
        ServerWorld world = context.player().getServerWorld();
        IEntityDataSaver player = (IEntityDataSaver) (context.player());
        NbtCompound playerNbt = player.getPersistentData();
        NbtList sigils;
        if(playerNbt.contains(ArcaneConstruction.sigilsNbtKey)){
            sigils = playerNbt.getList(ArcaneConstruction.sigilsNbtKey,8);

        }
        else {
            sigils = new NbtList();
        }

        if (!p.getMainHandStack().isIn(ArcaneConstruction.WANDS)) return;

        if (payload.cast()) {

            if(!SpellHandling.canCastNbtList(player,playerNbt.getList(ArcaneConstruction.sigilsNbtKey,8))) return;

            SpellHandling.setCasting( player,false);
            try {
                // Use the correct variable here
                SpellHandling.cast( player);

            } catch (Exception e) {
                e.printStackTrace();
            }
            spawnCastParticles(p,world, new Spell(SpellHandling.nbtListToSequence(playerNbt.getList(ArcaneConstruction.sigilsNbtKey,8))));

            playCastSound(p,world);

            SigilData.clearSigils( player);
        }


        if (SigilData.isSigilCooldownOver(player)) {
            if (payload.conjuration()){
                if(playerNbt.getList(ArcaneConstruction.sigilsNbtKey,8).isEmpty()){
                    SpellHandling.setCastingProjectile(player,true);

                }
                spawnAddSigilParticles(p,world);
                playsoundAddSigilParticles(p,world);

                SigilData.addSigil(player,ArcaneConstruction.conjurationString);
                SpellHandling.setCasting(player,true);
                if(playerNbt.getList(ArcaneConstruction.sigilsNbtKey,8).isEmpty()){
                    SpellHandling.setCastingProjectile(player,true);

                }
            }
            if (payload.invocation()){
                spawnAddSigilParticles(p,world);
                playsoundAddSigilParticles(p,world);
                SigilData.addSigil(player,ArcaneConstruction.invocationString);
                SpellHandling.setCasting(player,true);

            }
            if (payload.evocation()){
                spawnAddSigilParticles(p,world);
                playsoundAddSigilParticles(p,world);
                SigilData.addSigil(player,ArcaneConstruction.evocationString);
                SpellHandling.setCasting(player,true);

            }
            if (payload.summation()
                    && sigils.size() > 1
                    && sigils.getLast() != NbtString.of(ArcaneConstruction.summationString)
            ){
                spawnAddSigilParticles(p,world);
                playsoundAddSigilParticles(p,world);
                SigilData.addSigil(player,ArcaneConstruction.summationString);
                SpellHandling.setCasting(player,true);

            }
        }


        //Sync everything up with every other player

        UUID playerUuid = p.getUuid();
        NbtCompound nbt = player.getPersistentData();
        if (nbt == null) {
            nbt = new NbtCompound();
        }

        ModMessages.S2CPayload s2CPayload = new ModMessages.S2CPayload(nbt, playerUuid);

        MinecraftServer server = p.getServer();
        assert server != null;

        for (ServerPlayerEntity otherPlayer : server.getPlayerManager().getPlayerList()) {
            try {
                // Use the correct variable here
                ServerPlayNetworking.send(otherPlayer, s2CPayload);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        SigilData.setSigilCooldownOver(player,false);



    }

    public static void spawnAddSigilParticles(ServerPlayerEntity player, ServerWorld world){

        world.spawnParticles(
                ParticleTypes.ENCHANT,
                player.getX(),
                player.getY() + 1.5,
                player.getZ(),
                20,
                0.1, 0.1, 0.1,
                0.75
        );
    }
    public static void playsoundAddSigilParticles(ServerPlayerEntity player, ServerWorld world){

        world.playSound(
                null,
                player.getBlockPos(),
                SoundEvents.BLOCK_AMETHYST_BLOCK_FALL,
                SoundCategory.PLAYERS,
                1f,
                2f
        );
    }
    public static void spawnCastParticles(ServerPlayerEntity player, ServerWorld world, Spell spell){

        float red = 0;
        float green = 0;
        float blue = 0;

        for (SpellModule currentModule: spell.getModules()) {
            currentModule.getShape().getName();
            red += ColorHelper.Argb.getRed(currentModule.getShape().getColor());
            green += ColorHelper.Argb.getGreen(currentModule.getShape().getColor());
            blue  += ColorHelper.Argb.getBlue(currentModule.getShape().getColor());
        }


        if(spell.getForm()== Spell.Form.PROJECTILE){
            SpellProjectileParticleEffect particle = SpellProjectileParticleEffect.create(
                    ColorHelper.Argb.getArgb(
                            (int)(red / spell.getModules().size()),
                            (int)(green / spell.getModules().size()),
                            (int)(blue / spell.getModules().size())),
                    20,1f );

            Vec3d lookVec = player.getCameraEntity().getRotationVec(1.0F); // direction
            Vec3d pos = player.getEyePos();

            double distance = 2; // blocks in front of face
            Vec3d spawnPos = pos.add(lookVec.multiply(distance));

            world.spawnParticles(
                    particle,
                    spawnPos.x,
                    spawnPos.y,
                    spawnPos.z,
                    10,
                    0.1, 0.1, 0.1,
                    0.05
            );
            world.spawnParticles(
                    ParticleTypes.END_ROD,
                    player.getX(),
                    player.getY() + 0.5,
                    player.getZ(),
                    10,
                    0.25, 0, 0.25,
                    0.1
            );
        }
        if(spell.getForm()== Spell.Form.SELF){
            world.spawnParticles(
                    ParticleTypes.END_ROD,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    100,
                    0.5, 0.5, 0.5,
                    0.1
            );
            SpellProjectileParticleEffect particle = SpellProjectileParticleEffect.create(
                    ColorHelper.Argb.getArgb(
                            (int)(red / spell.getModules().size()),
                            (int)(green / spell.getModules().size()),
                            (int)(blue / spell.getModules().size())),
                    20,1.5f );
            world.spawnParticles(
                    particle,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    20,
                    1, 0, 1,
                    0.1
            );
        }


    }
    public static void playCastSound(ServerPlayerEntity player, ServerWorld world){
        world.playSound(
                null,
                player.getBlockPos(),
                SoundEvents.BLOCK_AMETHYST_BLOCK_HIT,
                SoundCategory.PLAYERS,
                1f,
                0f
        );
        world.playSound(
                null,
                player.getBlockPos(),
                SoundEvents.BLOCK_BEACON_POWER_SELECT,
                SoundCategory.PLAYERS,
                0.1f,
                2f
        );
    }
}
