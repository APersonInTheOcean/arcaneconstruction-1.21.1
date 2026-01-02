package darkdefault.arcaneconstruction.networking;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import darkdefault.arcaneconstruction.networking.packet.AddSigilC2SPacket;
import darkdefault.arcaneconstruction.networking.packet.ManaUpdatePacketS2C;
import darkdefault.arcaneconstruction.networking.packet.S2CHandler;
import darkdefault.arcaneconstruction.networking.packet.SigilCooldownOverS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import java.util.UUID;

public class ModMessages {
    public static final Identifier ADD_SIGIL_ID = Identifier.of(ArcaneConstruction.MOD_ID, "add_sigil_id");
    public static final Identifier CAST_ID = Identifier.of(ArcaneConstruction.MOD_ID, "cast_id");

    public static final Identifier SIGILSYNC_ID = Identifier.of(ArcaneConstruction.MOD_ID, "sigilsync_id");

    public static final Identifier MANASYNC_ID = Identifier.of(ArcaneConstruction.MOD_ID, "manasync_id");
    public static final Identifier SIGIL_COOLDOWN_OVER_ID = Identifier.of(ArcaneConstruction.MOD_ID, "sigil_cooldown_over_id");




    public record AddSigilC2SPayload(boolean conjuration,
                                     boolean invocation,
                                     boolean evocation,
                                     boolean summation,
                                     boolean cast
    ) implements CustomPayload {

        public static final Id<AddSigilC2SPayload> ID =
                new Id<>(ADD_SIGIL_ID);

        public static final PacketCodec<RegistryByteBuf, AddSigilC2SPayload> CODEC =
                PacketCodec.tuple(
                        PacketCodecs.BOOL, AddSigilC2SPayload::conjuration,
                        PacketCodecs.BOOL, AddSigilC2SPayload::invocation,
                        PacketCodecs.BOOL, AddSigilC2SPayload::evocation,
                        PacketCodecs.BOOL, AddSigilC2SPayload::summation,
                        PacketCodecs.BOOL, AddSigilC2SPayload::cast,
                        AddSigilC2SPayload::new
                );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }




    public record S2CPayload(NbtCompound nbt, UUID uuid) implements CustomPayload {

        public static final Id<S2CPayload> ID =
                new Id<>(SIGILSYNC_ID);

        public static final PacketCodec<RegistryByteBuf, S2CPayload> CODEC =
                PacketCodec.tuple(
                        PacketCodecs.NBT_COMPOUND, S2CPayload::nbt,
                        PacketCodecs.STRING, p -> p.uuid().toString(),
                        (nbt, uuidStr) -> new S2CPayload(nbt, UUID.fromString(uuidStr))
                );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    public record ManaSyncS2CPayload(int amount, int maxAmount, UUID uuid) implements CustomPayload {

        public static final Id<ManaSyncS2CPayload> ID =
                new Id<>(MANASYNC_ID);

        public static final PacketCodec<RegistryByteBuf, ManaSyncS2CPayload> CODEC =
                PacketCodec.tuple(
                        PacketCodecs.INTEGER, ManaSyncS2CPayload::amount,
                        PacketCodecs.INTEGER, ManaSyncS2CPayload::maxAmount,
                        PacketCodecs.STRING, p -> p.uuid().toString(),
                        (amount,maxAmount, uuidStr) -> new ManaSyncS2CPayload(amount,maxAmount, UUID.fromString(uuidStr))
                );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    public record SigilCooldownOverS2CPayload( UUID uuid) implements CustomPayload {

        public static final Id<SigilCooldownOverS2CPayload> ID =
                new Id<>(SIGIL_COOLDOWN_OVER_ID);

        public static final PacketCodec<RegistryByteBuf, SigilCooldownOverS2CPayload> CODEC =
                PacketCodec.tuple(
                        PacketCodecs.STRING, p -> p.uuid().toString(),
                        ( uuidStr) -> new SigilCooldownOverS2CPayload( UUID.fromString(uuidStr))
                );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }



    public static void registerPayloadTypes() {
        PayloadTypeRegistry.playC2S().register(AddSigilC2SPayload.ID, AddSigilC2SPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(S2CPayload.ID, S2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ManaSyncS2CPayload.ID, ManaSyncS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SigilCooldownOverS2CPayload.ID, SigilCooldownOverS2CPayload.CODEC);


    }

    // Server-side handler registration
    public static void registerC2SHandlers() {
        ServerPlayNetworking.registerGlobalReceiver(AddSigilC2SPayload.ID, AddSigilC2SPacket::receive);
    }

    // Client-side handler registration
    public static void registerS2CHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(S2CPayload.ID, S2CHandler::receive);
        ClientPlayNetworking.registerGlobalReceiver(ManaSyncS2CPayload.ID, ManaUpdatePacketS2C::receive);
        ClientPlayNetworking.registerGlobalReceiver(SigilCooldownOverS2CPayload.ID, SigilCooldownOverS2CPacket::receive);


    }


}
