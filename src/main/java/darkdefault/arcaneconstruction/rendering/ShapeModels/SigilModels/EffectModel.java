package darkdefault.arcaneconstruction.rendering.ShapeModels.SigilModels;

import darkdefault.arcaneconstruction.rendering.ShapeModels.SigilModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class EffectModel extends SigilModel {
    public EffectModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    public void scale(float scale){
        bb_main.scale(new Vector3f((scale *0.5f)- 0.5f ));
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {

        bb_main.render(matrices, vertexConsumer, light, overlay, color);


    }
}
