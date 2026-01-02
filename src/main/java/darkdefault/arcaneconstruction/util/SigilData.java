package darkdefault.arcaneconstruction.util;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import darkdefault.arcaneconstruction.networking.ModMessages;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.UUID;

public class SigilData {

    public static void addSigil(IEntityDataSaver player, String sigil) {
        // Get the player's persistent data
        NbtCompound nbt = player.getPersistentData();
        // Check if the "Sigils" NBT list already exists
        NbtList sigils;
        if (nbt.contains(ArcaneConstruction.sigilsNbtKey, 9)) { // 9 is the NBT tag type ID for lists
            sigils = nbt.getList(ArcaneConstruction.sigilsNbtKey, 8); // 8 is the NBT tag type ID for strings
        } else {
            sigils = new NbtList();
        }

        // Add the new sigil to the list
        sigils.add(NbtString.of(sigil));

        // Save the updated list back to the player's NBT data
        nbt.put(ArcaneConstruction.sigilsNbtKey, sigils);
        SigilData.setSigilCooldownOver(player,false);




    }

    public static void clearSigils(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        NbtList sigils = new NbtList();
        nbt.put(ArcaneConstruction.sigilsNbtKey, sigils);


    }





    public static void setSigilCooldown(IEntityDataSaver player,int amount){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
//            player.getPersistentData().put(ArcaneConstruction.manaNbtKey)
        }
        else {
            persistentData = player.getPersistentData();
        }
        persistentData.putInt(ArcaneConstruction.sigilCooldownNbtKey, amount);
    }
    public static void setSigilCooldownOver(IEntityDataSaver player, boolean bool){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        persistentData.putBoolean(ArcaneConstruction.sigilCooldownOverNbtKey,bool);
        if (!((PlayerEntity)player).getWorld().isClient()
            && bool
        ) {
           ModMessages.SigilCooldownOverS2CPayload payload = new ModMessages.SigilCooldownOverS2CPayload(((ServerPlayerEntity)player).getUuid());
           ServerPlayNetworking.send((ServerPlayerEntity)player,payload);
        }
    }
    public static boolean isSigilCooldownOver(IEntityDataSaver player){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
            persistentData.putBoolean(ArcaneConstruction.sigilCooldownOverNbtKey,true);
        }
        else {
            persistentData = player.getPersistentData();
        }
        return persistentData.getBoolean(ArcaneConstruction.sigilCooldownOverNbtKey);
    }
    public static int getSigilCooldown(IEntityDataSaver player){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        return persistentData.getInt(ArcaneConstruction.sigilCooldownNbtKey);
    }
    public static void addSigilCooldownTimer(IEntityDataSaver player,int amount){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        persistentData.putInt(ArcaneConstruction.sigilCooldownTimerNbtKey, getSigilCooldownTimer(player) + amount);

    }
    public static int getSigilCooldownTimer(IEntityDataSaver player){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        return persistentData.getInt(ArcaneConstruction.sigilCooldownTimerNbtKey);

    }

    public static void setMaxMana(IEntityDataSaver player,int amount){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
//            player.getPersistentData().put(ArcaneConstruction.manaNbtKey)
        }
        else {
            persistentData = player.getPersistentData();
        }
        persistentData.putInt(ArcaneConstruction.maxManaNbtKey, amount);
    }
    public static int getMaxMana(IEntityDataSaver player){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        return persistentData.getInt(ArcaneConstruction.maxManaNbtKey);
    }
    public static int getMana(IEntityDataSaver player){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        return persistentData.getInt(ArcaneConstruction.manaNbtKey);

    }
    public static void addMana(IEntityDataSaver player,int amount){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        persistentData.putInt(ArcaneConstruction.manaNbtKey, getMana(player) + amount);

        if (!((PlayerEntity) player).getWorld().isClient()) {
            ServerPlayerEntity p = (ServerPlayerEntity) player;
            UUID playerUuid = p.getUuid();
            ModMessages.ManaSyncS2CPayload manaSyncS2CPayload = new ModMessages.ManaSyncS2CPayload(persistentData.getInt(ArcaneConstruction.manaNbtKey),getMaxMana(player), playerUuid);

            MinecraftServer server = p.getServer();
            assert server != null;

            for (ServerPlayerEntity otherPlayer : server.getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(otherPlayer, manaSyncS2CPayload);
            }
        }
    }
    public static void setMana(IEntityDataSaver player,int amount){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        persistentData.putInt(ArcaneConstruction.manaNbtKey, amount);
        if (!((PlayerEntity) player).getWorld().isClient()) {
            ServerPlayerEntity p = (ServerPlayerEntity) player;
            UUID playerUuid = p.getUuid();
            ModMessages.ManaSyncS2CPayload manaSyncS2CPayload = new ModMessages.ManaSyncS2CPayload(getMana(player),getMaxMana(player), playerUuid);

            MinecraftServer server = p.getServer();
            assert server != null;

            for (ServerPlayerEntity otherPlayer : server.getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(otherPlayer, manaSyncS2CPayload);
            }
        }
    }
 }
