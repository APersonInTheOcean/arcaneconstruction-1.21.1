package darkdefault.arcaneconstruction.networking.packet;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import darkdefault.arcaneconstruction.networking.ModMessages;
import darkdefault.arcaneconstruction.util.IEntityDataSaver;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;

public class ManaUpdatePacketS2C {
    public static void receive(ModMessages.ManaSyncS2CPayload payload, ClientPlayNetworking.Context context) {
        ClientPlayerEntity player = context.player();
//         Handle the packet, e.g., print the rune or trigger something in the game


//        if (player.getUuid().equals(payload.uuid())) return;


        IEntityDataSaver target = (IEntityDataSaver) (player.getWorld().getPlayerByUuid(payload.uuid()));

        assert target != null;
        target.getPersistentData().putInt(ArcaneConstruction.manaNbtKey,payload.amount());
        target.getPersistentData().putInt(ArcaneConstruction.maxManaNbtKey,payload.maxAmount());


    }
}
