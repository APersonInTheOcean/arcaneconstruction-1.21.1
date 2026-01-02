package darkdefault.arcaneconstruction;

import darkdefault.arcaneconstruction.entity.ModEntities;
import darkdefault.arcaneconstruction.entity.client.SpellProjectileRenderer;
import darkdefault.arcaneconstruction.event.KeyInputHandler;
import darkdefault.arcaneconstruction.hud.SigilHud;
import darkdefault.arcaneconstruction.networking.ModMessages;
import darkdefault.arcaneconstruction.particle.ModParticles;
import darkdefault.arcaneconstruction.particle.SpellProjectileParticle;
import darkdefault.arcaneconstruction.rendering.ModModelLayers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ArcaneConstructionClient implements ClientModInitializer {


    public static final TagKey<Item> WANDS =
            TagKey.of(RegistryKeys.ITEM, Identifier.of(ArcaneConstruction.MOD_ID, "wands"));

    @Override
    public void onInitializeClient() {


        KeyInputHandler.register();
        ModMessages.registerS2CHandlers();
        HudRenderCallback.EVENT.register(new SigilHud());

        ModModelLayers.registerModelLayers();
        EntityRendererRegistry.register(ModEntities.SPELLPROJECTILE, SpellProjectileRenderer::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.SPELLPROJECTILE_PARTICLE, SpellProjectileParticle.SpellProjectileFactory::new);

    }
}
