package darkdefault.arcaneconstruction.rendering;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import darkdefault.arcaneconstruction.rendering.ShapeModels.SigilModel;
import darkdefault.arcaneconstruction.rendering.ShapeModels.SigilModels.BreakModel;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Shape;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Spell;
import darkdefault.arcaneconstruction.spells.SpellCrafting.SpellModule;
import darkdefault.arcaneconstruction.util.IEntityDataSaver;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.RotationAxis;

public class SigilFeatureRenderer<T extends LivingEntity, M extends PlayerEntityModel<T>> extends FeatureRenderer<T, M> {

    private final EntityRendererFactory.Context context;
    public SigilFeatureRenderer(EntityRendererFactory.Context context, LivingEntityRenderer<T, M> entityRenderer) {
        super(entityRenderer);
        this.context = context;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        // Optional condition (only render when casting)
        if (!(entity instanceof PlayerEntity player)) return;


        VertexConsumer vertexConsumer =
                vertexConsumers.getBuffer(RenderLayer.getEntitySolid(
                        Identifier.of(ArcaneConstruction.MOD_ID, "models/feature/features.png")
                ));

        NbtCompound nbt = ((IEntityDataSaver) player).getPersistentData();
        NbtList sigils = nbt.getList(ArcaneConstruction.sigilsNbtKey, 8);
        if (sigils.isEmpty()) return;
        if (sigils.size() <2) return;

        StringBuilder keybinds = new StringBuilder();
        for (NbtElement s:  sigils ) {
            if (s.toString().charAt(1) != ArcaneConstruction.summationString.charAt(0)
                || s != sigils.getLast()
            ) {
                keybinds.append(s.toString().charAt(1));

            }
        }
        String spellSequence = keybinds.toString().toLowerCase();
        Spell spell = new Spell(spellSequence);


        matrices.push();

        ModelPart body;
        switch (spell.getForm()) {
            case Spell.Form.PROJECTILE:
                body = this.getContextModel().head;
                body.rotate(matrices);
                adjustPosition((PlayerEntity) entity,matrices);

                matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) (Math.PI / 2)));
                matrices.translate(0, -player.getEyeHeight(player.getPose()), 1.5);

                int counter1 = 0;
                for (SpellModule currentModule:spell.getModules()) {
                    counter1++;
                    if(currentModule.getShape() == null) break;
                    Shape shape = currentModule.getShape();
                    SigilModel model = shape.getSigilModel(context.getPart(shape.getModelLayer()));
                    // reset model state
                    model.reset();
                    matrices.translate(0, -counter1*0.05, 0);
                    model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, shape.getColor());


                }
                break;
            case Spell.Form.SELF:
                body = this.getContextModel().body;
                matrices.translate(
                        body.pivotX / 16.0F,
                        body.pivotY / 16.0F,
                        body.pivotZ / 16.0F
                );
                adjustPosition((PlayerEntity) entity,matrices);


                int counter2 = 0;
                for (SpellModule currentModule:spell.getModules()) {
                    counter2++;
                    if(currentModule.getShape() == null) break;
                    Shape shape = currentModule.getShape();
                    SigilModel model = shape.getSigilModel(context.getPart(shape.getModelLayer()));
                    // reset model state
                    model.reset();
                    model.scale(counter2);
                    model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, shape.getColor());


                }
                break;


            case Spell.Form.AOE:
                body = this.getContextModel().body;
                matrices.translate(
                        body.pivotX / 16.0F,
                        body.pivotY / 16.0F,
                        body.pivotZ / 16.0F
                );
                adjustPosition((PlayerEntity) entity,matrices);

                int counter3 = 0;
                for (SpellModule currentModule:spell.getModules()) {
                    counter3++;
                    if(currentModule.getShape() == null) break;
                    Shape shape = currentModule.getShape();
                    SigilModel model = shape.getSigilModel(context.getPart(shape.getModelLayer()));
                    // reset model state
                    model.reset();
                    model.scale(counter3+2);
                    model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, shape.getColor());


                }
                break;


        }


        matrices.pop();

    }

    private void adjustPosition(PlayerEntity entity, MatrixStack matrices){
        if (entity.isInSneakingPose()) {

            //Cancel sneak translation
            matrices.translate(
                    0.0F,
                    entity.getScale() * -5.1F / 16.0F,
                    0.0F
            );
        }
        //Position it correctly with player height
        matrices.translate(
                0.0F,
                1.25,
                0.0F
        );
    }
}
