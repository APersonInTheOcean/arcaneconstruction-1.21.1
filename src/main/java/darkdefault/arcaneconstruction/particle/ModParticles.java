package darkdefault.arcaneconstruction.particle;

import com.mojang.serialization.MapCodec;
import darkdefault.arcaneconstruction.ArcaneConstruction;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModParticles {

    public static final ParticleType<SpellProjectileParticleEffect> SPELLPROJECTILE_PARTICLE = register("spellprojectile_particle", false, type -> SpellProjectileParticleEffect.CODEC, type -> SpellProjectileParticleEffect.PACKET_CODEC);


    private static <T extends ParticleEffect> ParticleType<T> register(
            String name,
            boolean alwaysShow,
            Function<ParticleType<T>, MapCodec<T>> codecGetter,
            Function<ParticleType<T>, PacketCodec<? super RegistryByteBuf, T>> packetCodecGetter
    )
    {
        return Registry.register(Registries.PARTICLE_TYPE, Identifier.of(ArcaneConstruction.MOD_ID, name), new ParticleType<T>(alwaysShow) {
            @Override
            public MapCodec<T> getCodec() {
                return (MapCodec<T>)codecGetter.apply(this);
            }

            @Override
            public PacketCodec<? super RegistryByteBuf, T> getPacketCodec() {
                return (PacketCodec<? super RegistryByteBuf, T>)packetCodecGetter.apply(this);
            }
        });
    }


    public static void registerParticles(){
        ArcaneConstruction.LOGGER.info("Registering Particles for "+ ArcaneConstruction.MOD_ID);

    }
}
