package darkdefault.arcaneconstruction.rendering.ShapeModels;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public abstract class SigilModel extends EntityModel<Entity> {
    public final ModelPart bb_main;

    protected SigilModel(ModelPart bbMain) {
        bb_main = bbMain;
    }
    @Override
    public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
    public void scale(float scale){
        bb_main.scale(new Vector3f(((scale* 1.2f)*0.5f)-0.5f));
        bb_main.translate(new Vector3f().add(0,0.1f,0));
    }
    public void reset(){
        bb_main.pitch = 0f;
        bb_main.yaw = 0f;
        bb_main.roll = 0f;

    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        bb_main.render(matrices, vertexConsumer, light, overlay, color);
    }
}
