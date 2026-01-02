package darkdefault.arcaneconstruction.particle;

import darkdefault.arcaneconstruction.spells.SpellCrafting.Spell;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class SpellProjectileParticle extends SpriteBillboardParticle {

    private Random random = Random.create();
    private int color;
    private float scaleFactor;

    protected SpellProjectileParticle(ClientWorld clientWorld, double x, double y, double z,
                                      SpriteProvider spriteProvider, int color, float scale, int lifetime,
                                      double xSpeed, double ySpeed, double zSpeed) {
        super(clientWorld, x, y, z, xSpeed, ySpeed, zSpeed);

        this.scaleFactor = scale;
        this.color = color;
        this.setColor(getRed(),getGreen(),getBlue());
        this.velocityMultiplier = 0.9f;
        this.setVelocity(
                xSpeed * ((double) random.nextBetween(-100, 100) /100),
                ySpeed * ((double) random.nextBetween(-100, 100) /100),
                zSpeed * ((double) random.nextBetween(-100, 100) /100));
        this.maxAge = lifetime;
        this.setSprite(spriteProvider);;

    }
    public void setMaxAge(int i){
        this.maxAge = i;
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

    @Override
    public void tick() {
        super.tick();



        this.alpha = 1f - (float) this.age / this.maxAge;
        this.scale = scaleFactor * Math.max(1.0f - ((float) this.age / this.maxAge), 0.001f);

    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class SpellProjectileFactory implements ParticleFactory<SpellProjectileParticleEffect> {

        private final SpriteProvider sprites;

        public SpellProjectileFactory(SpriteProvider sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SpellProjectileParticleEffect effect,
                                        ClientWorld world,
                                        double x, double y, double z,
                                        double vx, double vy, double vz) {

            SpellProjectileParticle particle = new SpellProjectileParticle(
                    world, x, y, z,
                    sprites, effect.color, effect.scale, effect.lifetime,
                    vx, vy,vz
            );

            return particle;
        }
    }
}
