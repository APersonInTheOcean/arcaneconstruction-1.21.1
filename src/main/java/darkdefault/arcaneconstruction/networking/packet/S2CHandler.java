package darkdefault.arcaneconstruction.networking.packet;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import darkdefault.arcaneconstruction.networking.ModMessages;
import darkdefault.arcaneconstruction.spells.SpellHandling;
import darkdefault.arcaneconstruction.util.IEntityDataSaver;
import darkdefault.arcaneconstruction.util.SigilData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

public class S2CHandler {

    public static void receive(ModMessages.S2CPayload payload, ClientPlayNetworking.Context context) {
        ClientPlayerEntity player = context.player();
//         Handle the packet, e.g., print the rune or trigger something in the game


        IEntityDataSaver target = (IEntityDataSaver) (player.getWorld().getPlayerByUuid(payload.uuid()));

        assert target != null;
        target.getPersistentData().put(ArcaneConstruction.sigilsNbtKey,payload.nbt().get(ArcaneConstruction.sigilsNbtKey));
        target.getPersistentData().put(ArcaneConstruction.castBoolNbtKey, payload.nbt().get(ArcaneConstruction.castBoolNbtKey));
        target.getPersistentData().put(ArcaneConstruction.castingProjectileBoolNbtKey, payload.nbt().get(ArcaneConstruction.castingProjectileBoolNbtKey));
        target.getPersistentData().put(ArcaneConstruction.sigilCooldownOverNbtKey, payload.nbt().get(ArcaneConstruction.sigilCooldownOverNbtKey));







    }
}
