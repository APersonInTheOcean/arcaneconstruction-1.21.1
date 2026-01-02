package darkdefault.arcaneconstruction.entity;

import darkdefault.arcaneconstruction.entity.SpellProjectileEntity;
import darkdefault.arcaneconstruction.particle.ModParticles;
import darkdefault.arcaneconstruction.particle.SpellProjectileParticleEffect;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augment;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Shapes.Effect;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Shapes.NoClip;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Shapes.Piercing;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Spell;
import darkdefault.arcaneconstruction.spells.SpellCrafting.SpellModule;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.Objects;

import static darkdefault.arcaneconstruction.entity.ModEntities.SPELLPROJECTILE;

public class SpellProjectileEntity extends ProjectileEntity {
    private static final TrackedData<String> SPELL_SEQUENCE= DataTracker.registerData(SpellProjectileEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Integer> NOCLIPCOUNT= DataTracker.registerData(SpellProjectileEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> PIERCINGCOUNT= DataTracker.registerData(SpellProjectileEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private int age;
    private int maxAge= 20;


    public SpellProjectileEntity(EntityType<? extends SpellProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;


    }



    public SpellProjectileEntity(World world, LivingEntity owner, Spell s) {
        super(SPELLPROJECTILE, world);
        this.noClip = true;

        // Range and Speed Augments

        this.dataTracker.set(SPELL_SEQUENCE,"c" + s.sequence);
        calcNoClip();
        calcPiercing();

        int maxAge = 20;
        float speed = 1f;

        for (SpellModule currentModule: s.getModules()){
            for (Augment a: currentModule.getAugments()){
                if (Objects.equals(a.getName(), "Range")){
                    maxAge += 10;
                }
                if (Objects.equals(a.getName(), "Speed")){
                    speed += 0.5f;
                }
            }
        }
        this.setVelocity(owner.getRotationVec(0.0F).normalize()
                .multiply(speed));
        this.maxAge = maxAge;


        this.setPosition(
                owner.getEyePos().x + owner.getRotationVec(0f).x * 1,
                owner.getEyePos().y + owner.getRotationVec(0f).y * 1,
                owner.getEyePos().z + owner.getRotationVec(0f).z * 1
        );
        this.setOwner(owner);
    }


    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(SPELL_SEQUENCE, "ccc");
        builder.add(NOCLIPCOUNT, 1);
        builder.add(PIERCINGCOUNT, 1);

    }

    @Override
    public void tick() {
        super.tick();


        age();

        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onCollision(hitResult);
        }


        move();
        spawnParticles();

        spawnTrailEffect();


    }



    private void age(){
        if(age != maxAge){
            age++;
        }
        else {
            if(!this.getWorld().isClient()){
                this.discard();

            }
        }
    }



    private int getParticleColor(){


        float red = 0;
        float green = 0;
        float blue = 0;

        Spell s = getSpell();

        for (SpellModule currentModule: s.getModules()) {
            currentModule.getShape().getName();
            red += ColorHelper.Argb.getRed(currentModule.getShape().getColor());
            green += ColorHelper.Argb.getGreen(currentModule.getShape().getColor());
            blue  += ColorHelper.Argb.getBlue(currentModule.getShape().getColor());
        }


        return ColorHelper.Argb.getArgb(
                (int)(red / s.getModules().size()),
                (int)(green / s.getModules().size()),
                (int)(blue / s.getModules().size()));

    }

    private void spawnParticles(){

        if (!this.getWorld().isClient) return;

// Delta movement
        double dx = this.prevX - this.getX();
        double dy = this.prevY - this.getY();
        double dz = this.prevZ - this.getZ();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

// Spawn density (blocks between particles)
        double step = 0.01;
        int count = Math.max(1, (int)(distance / step));

// Projectile velocity
        Vec3d velocity = this.getVelocity();
        double speed = velocity.length();

// Normalize direction safely
        Vec3d direction = speed > 0 ? velocity.normalize() : Vec3d.ZERO;

// Particle velocity tuning
        double directionScale = 0.05 * speed;    // forward motion
        double randomScale = 0.05 * speed; // random drift




        for (int i = 1; i <= count; i++) {
            double t = i / (double) count;

            double particleX = this.prevX + dx * t;
            double particleY = this.prevY + dy * t;
            double particleZ = this.prevZ + dz * t;

            double vx = direction.x * directionScale + (random.nextDouble() - 0.5) * randomScale;
            double vy = direction.y * directionScale + (random.nextDouble() - 0.5) * randomScale;
            double vz = direction.z * directionScale + (random.nextDouble() - 0.5) * randomScale;


            SpellProjectileParticleEffect particle = SpellProjectileParticleEffect.create(
                    getParticleColor(),
                    0.08f);


            this.getWorld().addParticle(
                    particle,
                    true,
                    particleX,
                    particleY,
                    particleZ,
                    vx,
                    vy,
                    vz
            );
        }


    }

    private void spawnTrailEffect() {


        Spell s = getSpell();

        for (SpellModule module: s.getModules()) {
            module.getShape().getTrailEffect(this,module);
        }

    }

    private void move(){
        Vec3d velocity = this.getVelocity();
        this.move(MovementType.SELF, velocity);
    // Update rotation AFTER moving
        this.updateRotation();

    }

    private void calcNoClip(){
         Spell s = getSpell();
        for (SpellModule module: s.getModules()) {
            if(module.getShape() instanceof NoClip noclip){
                noclip.addNoClip(this,module);
            }

        }
    }
    private void calcPiercing(){
        Spell s = getSpell();
        for (SpellModule module: s.getModules()) {
            if(module.getShape() instanceof Piercing piercing){
                piercing.addPiercing(this,module);
            }

        }
    }
    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        if ( getNoClipCount() > 0) {

            Spell s = getSpell();

            boolean continueMethod = true;
            for (SpellModule module: s.getModules()) {
                if (module.getShape() instanceof Effect) {
                    module.getShape().onBlockHit(blockHitResult, s, this, module);
                    continueMethod = false;
                    break;
                }
            }
            if(continueMethod){
                for (SpellModule module: s.getModules()) {
                    module.getShape().onBlockHit(blockHitResult, s, this, module);
                }
            }



        }

        //SERVER ONLY
        if(this.getWorld().isClient) return;
        addNoClipCount(-1);
        if (getNoClipCount() == 0) {
            this.discard();
        }

    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        if ( getPiercingCount() > 0) {

            Spell s = getSpell();

            boolean continueMethod = true;
            for (SpellModule module: s.getModules()) {
                if (module.getShape() instanceof Effect) {
                    module.getShape().onEntityHit(entityHitResult, s, this, module);
                    continueMethod = false;
                    break;
                }

            }
            if(continueMethod){
                for (SpellModule module: s.getModules()) {
                    module.getShape().onEntityHit(entityHitResult, s, this, module);
                }
            }



        }

        //SERVER ONLY
        if(this.getWorld().isClient) return;
        addPiercingCount(-1);
        if (getPiercingCount() == 0) {
            this.discard();
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
//        nbt.putInt("Color", this.getParticleColor());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
//        if (nbt.contains("Color", NbtElement.INT_TYPE)) {
//            this.Color= nbt.getByte("ExplosionPower");
//        }
    }

    public int getPiercingCount() {
        return dataTracker.get(PIERCINGCOUNT);
    }

    public void addPiercingCount(int piercingCount) {
        dataTracker.set(PIERCINGCOUNT, getPiercingCount() + piercingCount);
    }

    public int getNoClipCount() {
        return dataTracker.get(NOCLIPCOUNT);
    }

    public void addNoClipCount(int noClipCount) {
        dataTracker.set(NOCLIPCOUNT, getNoClipCount() + noClipCount);
    }

    public Spell getSpell(){
        return new Spell(dataTracker.get(SPELL_SEQUENCE));
    }
}
