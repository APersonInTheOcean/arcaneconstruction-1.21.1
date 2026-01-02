package darkdefault.arcaneconstruction.hud;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import darkdefault.arcaneconstruction.util.IEntityDataSaver;
import darkdefault.arcaneconstruction.util.SigilData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;
@Environment(EnvType.CLIENT)
public class SigilHud implements HudRenderCallback{

    private static final Identifier CONJURATION = Identifier.of(ArcaneConstruction.MOD_ID,
            "textures/sigils/conjuration.png"
            );
    private static final Identifier INVOCATION = Identifier.of(ArcaneConstruction.MOD_ID,
            "textures/sigils/invocation.png"
    );
    private static final Identifier EVOCATION = Identifier.of(ArcaneConstruction.MOD_ID,
            "textures/sigils/evocation.png"
    );
    private static final Identifier SUMMATION = Identifier.of(ArcaneConstruction.MOD_ID,
            "textures/sigils/summation.png"
    );
    private static final Identifier MANABAR = Identifier.of(ArcaneConstruction.MOD_ID,
            "textures/sigils/manabar.png"
    );
    private static final Identifier MANABAR_FILL = Identifier.of(ArcaneConstruction.MOD_ID,
            "textures/sigils/manabar_fill.png"
    );

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {

        MinecraftClient client = MinecraftClient.getInstance();

        if (client == null) return;
        IEntityDataSaver p = (IEntityDataSaver) client.player;
        if (p.getPersistentData() == null) return;


        NbtList sigils = p.getPersistentData().getList(ArcaneConstruction.sigilsNbtKey, 8);

        if (!sigils.isEmpty()) {
            renderSigils(drawContext,renderTickCounter,client,sigils);
        }
        // DEBUG CAST BOOL
//            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
//            int width = client.getWindow().getScaledWidth()/2;
//            int height = client.getWindow().getScaledHeight() / 5;
//            drawContext.drawCenteredTextWithShadow(textRenderer, Text.of("Casting: "+ String.valueOf(p.getPersistentData().getBoolean(ArcaneConstruction.castBoolNbtKey))), width , height, 0xFFFFFF);



        //  MANA BAR

        if (client.player.getMainHandStack().isIn(ArcaneConstruction.WANDS)){
            renderManaBar(drawContext,renderTickCounter,client,p);

        }


        //     DEBUG MANA
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int width = client.getWindow().getScaledWidth()/2;
        int height = client.getWindow().getScaledHeight() / 5;
        drawContext.drawCenteredTextWithShadow(textRenderer, Text.of("Mana: "+ String.valueOf(p.getPersistentData().getInt(ArcaneConstruction.manaNbtKey)) + "/" + String.valueOf(p.getPersistentData().getInt(ArcaneConstruction.maxManaNbtKey))), width , height, 0xFFFFFF);


        // DEBUG SIGIL COOLDOWN
//            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
//            int width = client.getWindow().getScaledWidth()/2;
//            int height = client.getWindow().getScaledHeight() / 4;
//            drawContext.drawCenteredTextWithShadow(textRenderer, Text.of("Sigil Cooldown: "+ String.valueOf(p.getPersistentData().getBoolean(ArcaneConstruction.sigilCooldownOverNbtKey))), width , height, 0xFFFFFF);
    }


    public void renderManaBar(DrawContext drawContext, RenderTickCounter renderTickCounter, MinecraftClient client,IEntityDataSaver p){

        int scale = 4 ;
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight =  client.getWindow().getScaledHeight();
        int textureWidth = 32 * scale;
        int textureHeight = 16 * scale;
        int textureX = screenWidth - textureWidth;
        int textureY = screenHeight-textureHeight ;
        int amountOfMana = SigilData.getMana(p);
        int maxMana = SigilData.getMaxMana(p);

        drawContext.drawTexture(MANABAR_FILL, textureX, textureY, 0, 0, (textureWidth*amountOfMana)/Math.max(maxMana,1), textureHeight, textureWidth, textureHeight);

        drawContext.drawTexture(MANABAR, textureX, textureY, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }
    public void renderSigils(DrawContext drawContext, RenderTickCounter renderTickCounter,MinecraftClient client, NbtList sigils){

        int textureSize = 16; // Size of each texture
        int spacing = 4;      // Space between textures
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight() / 2;

        // Calculate total width of sigils row
        int totalWidth = (sigils.size() * textureSize) + ((sigils.size() - 1) * spacing);

        // Calculate starting X position for centering
        int textureX = (screenWidth - totalWidth) / 2;

        for (int i = 0; i < sigils.size(); i++) {
            int textureY = screenHeight + textureSize/2; // Center vertically

            // Draw textures based on the sigil type
            if (Objects.equals(sigils.getString(i), "Conjuration")) {
                drawContext.drawTexture( CONJURATION, textureX, textureY, 0, 0, textureSize, textureSize, textureSize, textureSize);
            }
            if (Objects.equals(sigils.getString(i), "Invocation")) {
                drawContext.drawTexture( INVOCATION, textureX, textureY, 0, 0, textureSize, textureSize, textureSize, textureSize);
            }
            if (Objects.equals(sigils.getString(i), "Evocation")) {
                drawContext.drawTexture( EVOCATION, textureX, textureY, 0, 0, textureSize, textureSize, textureSize, textureSize);
            }
            if (Objects.equals(sigils.getString(i), "Summation")) {
                drawContext.drawTexture( SUMMATION, textureX, textureY, 0, 0, textureSize, textureSize, textureSize, textureSize);
            }

            // Increment textureX for the next texture
            textureX += textureSize + spacing;
        }
    }
}
