package darkdefault.arcaneconstruction.entity.client;

import darkdefault.arcaneconstruction.entity.SpellProjectileEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;


public class SpellProjectileRenderer extends EntityRenderer<SpellProjectileEntity> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/enderdragon/dragon_fireball.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE);

    public SpellProjectileRenderer(EntityRendererFactory.Context context) {
        super(context);
    }


    protected int getBlockLight(SpellProjectileEntity spellProjectileEntity, BlockPos blockPos) {
        return 15;
    }

    public void render(SpellProjectileEntity spellProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(2.0F, 2.0F, 2.0F);
        matrixStack.multiply(this.dispatcher.getRotation());
        MatrixStack.Entry entry = matrixStack.peek();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
        matrixStack.pop();
        super.render(spellProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void produceVertex(VertexConsumer vertexConsumer, MatrixStack.Entry matrix, int light, float x, int z, int textureU, int textureV) {
        vertexConsumer.vertex(matrix, x - 0.5F, z - 0.25F, 0.0F)
                .color(Colors.WHITE)
                .texture(textureU, textureV)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(matrix, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public Identifier getTexture(SpellProjectileEntity entity) {
        return TEXTURE;
    }
}
