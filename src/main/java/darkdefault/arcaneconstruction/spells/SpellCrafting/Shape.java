package darkdefault.arcaneconstruction.spells.SpellCrafting;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import darkdefault.arcaneconstruction.entity.SpellProjectileEntity;
import darkdefault.arcaneconstruction.rendering.ShapeModels.SigilModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;

import java.util.function.Supplier;

public interface Shape {


    @Environment(EnvType.CLIENT)
    default SigilModel getSigilModel(ModelPart root) {
        return null;
    }

    @Environment(EnvType.CLIENT)
    default EntityModelLayer getModelLayer() {
        return null;
    }

    @Environment(EnvType.CLIENT)
    default EntityModelLayerRegistry.TexturedModelDataProvider getModelDataSupplier() {
        return null;
    }

    void onBlockHit(BlockHitResult blockHitResult,
                    Spell spell,
                    SpellProjectileEntity entity,
                    SpellModule module);

    void onEntityHit(EntityHitResult entityHitResult,
                     Spell spell,
                     SpellProjectileEntity entity,
                     SpellModule module);

    void onSelfCast(Spell s, ServerPlayerEntity player, SpellModule module);

    void getTrailEffect(SpellProjectileEntity entity, SpellModule module);

    boolean shouldInterruptOnBlockHit();
    boolean shouldInterruptOnEntityHit();
    boolean shouldInterruptOnSelfCast();


    String getSequence();
    String getName();
    int getColor();;

    int getManaCost();

}
