package darkdefault.arcaneconstruction.spells.SpellCrafting.Shapes;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import darkdefault.arcaneconstruction.entity.SpellProjectileEntity;
import darkdefault.arcaneconstruction.rendering.ShapeModels.ModelData.BreakModelData;
import darkdefault.arcaneconstruction.rendering.ShapeModels.SigilModel;
import darkdefault.arcaneconstruction.rendering.ShapeModels.SigilModels.BreakModel;
import darkdefault.arcaneconstruction.spells.SpellCrafting.*;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.AOE;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;

public class Break extends SimpleShape {
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
