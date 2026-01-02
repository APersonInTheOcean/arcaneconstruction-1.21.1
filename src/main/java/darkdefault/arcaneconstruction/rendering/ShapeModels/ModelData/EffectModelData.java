package darkdefault.arcaneconstruction.rendering.ShapeModels.ModelData;

import net.minecraft.client.model.*;

public final class EffectModelData {

    private EffectModelData() {}

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(25, 81).cuboid(-3.0F, 0.0F, -16.0F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(25, 82).cuboid(-3.0F, 0.0F, 15.0F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(1, 64).cuboid(15.0F, 0.0F, -3.0F, 1.0F, 0.0F, 6.0F, new Dilation(0.0F))
                .uv(25, 73).cuboid(14.0F, 0.0F, 3.0F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(25, 73).cuboid(14.0F, 0.0F, -6.0F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(22, 72).cuboid(13.0F, 0.0F, -8.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(22, 74).cuboid(13.0F, 0.0F, 6.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(28, 77).cuboid(12.0F, 0.0F, -9.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(28, 78).cuboid(12.0F, 0.0F, 8.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 76).cuboid(11.0F, 0.0F, -11.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(22, 78).cuboid(11.0F, 0.0F, 9.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(17, 64).cuboid(10.0F, 0.0F, -12.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(17, 66).cuboid(10.0F, 0.0F, 10.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(25, 94).cuboid(9.0F, 0.0F, -12.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(29, 94).cuboid(9.0F, 0.0F, 11.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(25, 91).cuboid(8.0F, 0.0F, -13.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(29, 91).cuboid(8.0F, 0.0F, 12.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(-1, 89).cuboid(6.0F, 0.0F, -14.0F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(5, 89).cuboid(6.0F, 0.0F, 13.0F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 66).cuboid(3.0F, 0.0F, -15.0F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 67).cuboid(3.0F, 0.0F, 14.0F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 68).cuboid(0.0F, 0.0F, -16.0F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 69).cuboid(0.0F, 0.0F, 15.0F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(-2, 64).cuboid(-16.0F, 0.0F, -3.0F, 1.0F, 0.0F, 6.0F, new Dilation(0.0F))
                .uv(25, 73).cuboid(-15.0F, 0.0F, 3.0F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(17, 68).cuboid(-14.0F, 0.0F, 6.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(25, 73).cuboid(-15.0F, 0.0F, -6.0F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(25, 92).cuboid(-13.0F, 0.0F, 8.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(17, 70).cuboid(-12.0F, 0.0F, 9.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(17, 72).cuboid(-11.0F, 0.0F, 10.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(29, 92).cuboid(-10.0F, 0.0F, 11.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(25, 93).cuboid(-9.0F, 0.0F, 12.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(11, 89).cuboid(-8.0F, 0.0F, 13.0F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 70).cuboid(-6.0F, 0.0F, 14.0F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(17, 74).cuboid(-14.0F, 0.0F, -8.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(29, 93).cuboid(-13.0F, 0.0F, -9.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(17, 76).cuboid(-12.0F, 0.0F, -11.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(17, 78).cuboid(-11.0F, 0.0F, -12.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(25, 95).cuboid(-10.0F, 0.0F, -12.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(29, 95).cuboid(-9.0F, 0.0F, -13.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(17, 89).cuboid(-8.0F, 0.0F, -14.0F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 71).cuboid(-6.0F, 0.0F, -15.0F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(21, 64).cuboid(10.0F, 0.0F, 15.0F, 5.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(24, 65).cuboid(15.0F, 0.0F, 10.0F, 1.0F, 0.0F, 6.0F, new Dilation(0.0F))
                .uv(21, 64).cuboid(10.0F, 0.0F, -16.0F, 5.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(-6, 72).cuboid(15.0F, 0.0F, -16.0F, 1.0F, 0.0F, 6.0F, new Dilation(0.0F))
                .uv(21, 64).cuboid(-15.0F, 0.0F, 15.0F, 5.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(-6, 72).cuboid(-16.0F, 0.0F, 10.0F, 1.0F, 0.0F, 6.0F, new Dilation(0.0F))
                .uv(24, 65).cuboid(-16.0F, 0.0F, -16.0F, 1.0F, 0.0F, 6.0F, new Dilation(0.0F))
                .uv(21, 64).cuboid(-15.0F, 0.0F, -16.0F, 5.0F, 0.0F, 1.0F, new Dilation(0.0F))
                .uv(-17, 64).cuboid(-11.0F, 0.0F, -9.0F, 1.0F, 0.0F, 19.0F, new Dilation(0.0F))
                .uv(-17, 64).cuboid(10.0F, 0.0F, -10.0F, 1.0F, 0.0F, 19.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r1 = bb_main.addChild("cube_r1", ModelPartBuilder.create().uv(-11, 64).cuboid(0.0F, 0.0F, -19.0F, 1.0F, 0.0F, 21.0F, new Dilation(0.0F)), ModelTransform.of(8.0F, 0.0F, -9.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData cube_r2 = bb_main.addChild("cube_r2", ModelPartBuilder.create().uv(-11, 64).cuboid(0.0F, 0.0F, -18.0F, 1.0F, 0.0F, 21.0F, new Dilation(0.0F)), ModelTransform.of(8.0F, 0.0F, 10.0F, 0.0F, 1.5708F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}

