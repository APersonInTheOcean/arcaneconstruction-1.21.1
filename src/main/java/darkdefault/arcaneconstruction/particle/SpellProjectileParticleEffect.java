package darkdefault.arcaneconstruction.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.*;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class SpellProjectileParticleEffect implements ParticleEffect {
    public final int color;
    public final float scale;
    public final int lifetime;


    public static final MapCodec<SpellProjectileParticleEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.INT.fieldOf("color").forGetter(effect -> effect.color),
                            Codec.INT.fieldOf("lifetime").forGetter(effect -> effect.lifetime),

                            Codec.FLOAT.fieldOf("scale").forGetter(effect -> effect.scale)
                    )
                    .apply(instance, SpellProjectileParticleEffect::new)
    );
    public static final PacketCodec<RegistryByteBuf, SpellProjectileParticleEffect> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, effect -> effect.color,
            PacketCodecs.INTEGER, effect -> effect.lifetime,
            PacketCodecs.FLOAT, effect -> effect.scale,
            SpellProjectileParticleEffect::new
    );;

    private SpellProjectileParticleEffect(int color, int lifetime, float scale) {
        this.color = color;
        this.scale = scale;
        this.lifetime = lifetime;
    }

    @Override
    public ParticleType<SpellProjectileParticleEffect> getType() {
        return ModParticles.SPELLPROJECTILE_PARTICLE;
    }

    public int getColor() {
        return this.color;
    }
    public int getLifetime() {
        return this.lifetime;
    }

    public float getRed() {
        return ColorHelper.Argb.getRed(this.color) / 255.0F;
    }

    public float getGreen() {
        return ColorHelper.Argb.getGreen(this.color) / 255.0F;
    }

    public float getBlue() {
        return ColorHelper.Argb.getBlue(this.color) / 255.0F;
    }

    public float getAlpha() {
        return ColorHelper.Argb.getAlpha(this.color) / 255.0F;
    }

    public static SpellProjectileParticleEffect create(int color, float scale) {
        return new SpellProjectileParticleEffect(color, 10, scale);
    }

    public static SpellProjectileParticleEffect create(int r, int g, int b, float scale) {
        return create(ColorHelper.Argb.getArgb( r, g, b),scale);
    }
    public static SpellProjectileParticleEffect create(int color, int lifetime, float scale) {
        return new SpellProjectileParticleEffect(color, lifetime, scale);
    }

    public static SpellProjectileParticleEffect create(int r, int g, int b, int lifetime, float scale) {
        return create(ColorHelper.Argb.getArgb( r, g, b), lifetime, scale);
    }
}
