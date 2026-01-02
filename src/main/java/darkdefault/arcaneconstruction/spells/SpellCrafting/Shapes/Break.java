package darkdefault.arcaneconstruction.spells.SpellCrafting.Shapes;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import darkdefault.arcaneconstruction.entity.SpellProjectileEntity;
import darkdefault.arcaneconstruction.networking.ModMessages;
import darkdefault.arcaneconstruction.rendering.ShapeModels.ModelData.BreakModelData;
import darkdefault.arcaneconstruction.rendering.ShapeModels.SigilModel;
import darkdefault.arcaneconstruction.rendering.ShapeModels.SigilModels.BreakModel;
import darkdefault.arcaneconstruction.spells.SpellCrafting.*;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.AOE;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.Duration;
import darkdefault.arcaneconstruction.util.IEntityDataSaver;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.UUID;

public class Break extends SimpleShape {

    public static final String multiBreakActiveNbtKey = "multiBreakActiveBool";
    public static final String multiBreakAmplifierNbtKey = "mulitBreakAmplifier";
    public static final String multiBreakTriggersNbtKey = "multiBreakTriggers";
    public static final String multiBreakMiningSideNbtKey = "multiBreakMiningSide";



    @Environment(EnvType.CLIENT)
    @Override
    public SigilModel getSigilModel(ModelPart root) {
        return new BreakModel(root);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public EntityModelLayer getModelLayer() {
        return new EntityModelLayer(Identifier.of(ArcaneConstruction.MOD_ID, "breakmodel"), "main");
    }

    @Environment(EnvType.CLIENT)
    @Override
    public EntityModelLayerRegistry.TexturedModelDataProvider getModelDataSupplier() {
        return BreakModelData::getTexturedModelData;
    }
    @Override
    public void onBlockHit(BlockHitResult blockHitResult, Spell spell, SpellProjectileEntity entity, SpellModule module) {

        BlockPos blockPos = blockHitResult.getBlockPos();
        World world =  entity.getWorld();
        int radius = 0;

        for(Augment a: module.getAugments()){
            if(a instanceof AOE){
                if(radius <= 1) {
                    radius++;
                }
                else if (radius <= 8){
                    radius += 2;
                }
                else {
                    radius += 1;
                }
            }
        }


        if(world.isClient) return;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {

                    double distance = Math.sqrt(x * x + y * y + z * z);

                    if (distance <= radius) { // Only remove blocks inside the sphere
                        BlockPos targetPos = blockPos.add(x, y, z);

                        if (world.getBlockState(targetPos).isIn(BlockTags.AXE_MINEABLE)
                                || world.getBlockState(targetPos).isIn(BlockTags.HOE_MINEABLE)
                                || world.getBlockState(targetPos).isIn(BlockTags.PICKAXE_MINEABLE)
                                || world.getBlockState(targetPos).isIn(BlockTags.SHOVEL_MINEABLE)) {

                            world.breakBlock(targetPos, true, entity.getOwner());
                        }

                    }
                }
            }
        }


    }


    @Override
    public void onEntityHit(EntityHitResult entityHitResult, Spell spell, SpellProjectileEntity entity, SpellModule module) {

    }

    @Override
    public void onSelfCast(Spell s, ServerPlayerEntity player, SpellModule module) {
        IEntityDataSaver p = (IEntityDataSaver) player;
        int amplifier = 0;
        amplifier = incrementFromAugments(amplifier,1, AOE.class,module);
        int triggers = 5;
        triggers = incrementFromAugments(triggers,10, Duration.class ,module);

        if(!isMultiBreakActive(p)){


            setMultiBreakActive(p, true);
            setMultiBreakTriggers(p,triggers);
            setMultiBreakAmplifier(p,amplifier);
        }
        else if(getMultiBreakAmplifier(p) < amplifier) {
            setMultiBreakTriggers(p,triggers);
            setMultiBreakAmplifier(p,amplifier);
        }
        else if(getMultiBreakAmplifier(p) == amplifier) {
            addMultiBreakTriggers(p,triggers);
        }



    }

    public static void MultiBreakTriggered(World world, PlayerEntity p, BlockPos blockPos, BlockState state, BlockEntity blockEntity){

        IEntityDataSaver player = (IEntityDataSaver) p;

        int radius = getMultiBreakAmplifier(player);

        if (getMiningSide(player)==Direction.DOWN
            ||getMiningSide(player)==Direction.UP
        ){


            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {

                    BlockPos targetPos = blockPos.add(x, y, 0);

                    ServerPlayerEntity serverPlayer = (ServerPlayerEntity) p;
                    serverPlayer.interactionManager.tryBreakBlock(targetPos);
                }
            }
        }
        if (getMiningSide(player)==Direction.EAST
                ||getMiningSide(player)==Direction.WEST
        ){
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {

                    BlockPos targetPos = blockPos.add(x, 0, z);

                    ServerPlayerEntity serverPlayer = (ServerPlayerEntity) p;
                    serverPlayer.interactionManager.tryBreakBlock(targetPos);
                }
            }

        }
        if (getMiningSide(player)==Direction.NORTH
                ||getMiningSide(player)==Direction.SOUTH
        ){

            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {

                    BlockPos targetPos = blockPos.add(0, y, z);

                    ServerPlayerEntity serverPlayer = (ServerPlayerEntity) p;
                    serverPlayer.interactionManager.tryBreakBlock(targetPos);
                }
            }
        }



    }

    public static boolean isMultiBreakActive(IEntityDataSaver player){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
            persistentData.putBoolean(multiBreakActiveNbtKey,false);
        }
        else {
            persistentData = player.getPersistentData();
        }
        return persistentData.getBoolean(multiBreakActiveNbtKey);

    }
    public static void setMultiBreakActive (IEntityDataSaver player, boolean bool){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        persistentData.putBoolean(multiBreakActiveNbtKey, bool);
        if (!bool){
            setMultiBreakAmplifier(player,0);
            setMultiBreakTriggers(player,0);
        }
    }

    public static void setMultiBreakTriggers (IEntityDataSaver player, int amount){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        persistentData.putInt(multiBreakTriggersNbtKey, amount);
    }
    public static int getMultiBreakTriggers(IEntityDataSaver player){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        return persistentData.getInt(multiBreakTriggersNbtKey);
    }

    public static void addMultiBreakTriggers(IEntityDataSaver player,int amount){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        persistentData.putInt(multiBreakTriggersNbtKey, getMultiBreakTriggers(player) + amount);


    }
    public static int getMultiBreakAmplifier(IEntityDataSaver player){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        return persistentData.getInt(multiBreakAmplifierNbtKey);

    }
    public static void setMultiBreakAmplifier(IEntityDataSaver player,int amount){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        persistentData.putInt(multiBreakAmplifierNbtKey, amount);
    }
    public static void setMiningSide(IEntityDataSaver player, Direction direction){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        persistentData.putString(multiBreakMiningSideNbtKey, direction.getName());
    }
    public static Direction getMiningSide(IEntityDataSaver player){
        NbtCompound persistentData;
        if (player.getPersistentData() == null){
            persistentData = new NbtCompound();
        }
        else {
            persistentData = player.getPersistentData();
        }
        String directionString = persistentData.getString(multiBreakMiningSideNbtKey);
        return Direction.byName(directionString);

    }


    public int incrementFromAugments(int valueToIncrement, int amountToIncrement, Class<? extends Augment> augmentToCheck, SpellModule currentModule) {
        int howMuchToIncrement = valueToIncrement;
        for (Augment a: currentModule.getAugments()) {
            if (augmentToCheck.isInstance(a)) {
                howMuchToIncrement+= amountToIncrement;
            }
        }
        return howMuchToIncrement;
    }
    @Override
    public void getTrailEffect(SpellProjectileEntity spellProjectileEntity, SpellModule module) {

    }

    @Override
    public String getSequence() {
        return "i";
    }

    @Override
    public String getName() {
        return "Break";
    }

    @Override
    public int getColor() {
        return ColorHelper.Argb.getArgb( 0, 255,  0);
    }

}
