package darkdefault.arcaneconstruction.spells.SpellCrafting;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import darkdefault.arcaneconstruction.entity.SpellProjectileEntity;
import darkdefault.arcaneconstruction.rendering.ShapeModels.ModelData.ConjurationModelData;
import darkdefault.arcaneconstruction.rendering.ShapeModels.SigilModel;
import darkdefault.arcaneconstruction.rendering.ShapeModels.SigilModels.ConjurationModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.ColorHelper;

public abstract class DefaultModelShape implements Shape {
    @Environment(EnvType.CLIENT)
    @Override
    public SigilModel getSigilModel(ModelPart root) {
        return new ConjurationModel(root);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public EntityModelLayer getModelLayer() {
        return new EntityModelLayer(Identifier.of(ArcaneConstruction.MOD_ID, this.getName().toLowerCase() + "model"), "main");
    }

    @Environment(EnvType.CLIENT)
    @Override
    public EntityModelLayerRegistry.TexturedModelDataProvider getModelDataSupplier() {
        return ConjurationModelData::getTexturedModelData;
    }
    @Override
    public void onBlockHit(BlockHitResult blockHitResult, Spell spell, SpellProjectileEntity entity, SpellModule module) {



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
        return "";
    }

    @Override
    public String getName() {
        return "Default";
    }

    @Override
    public int getColor() {
        return ColorHelper.Argb.getArgb( 0, 0,  0);
    }

    @Override
    public int getManaCost() {
        return getSequence().length()*20;
    }

}
