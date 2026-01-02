package darkdefault.arcaneconstruction.rendering;

import darkdefault.arcaneconstruction.spells.SpellCrafting.Shape;
import darkdefault.arcaneconstruction.spells.SpellCrafting.SpellModule;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

public class ModModelLayers {

    public static void registerModelLayers(){

        for (Shape s: SpellModule.SpellCraftingRegistry.getRegisteredShapes()){
            System.out.println("Registering model layer: " + s.getModelLayer());
            EntityModelLayerRegistry.registerModelLayer(
                    s.getModelLayer(),
                    s.getModelDataSupplier()
            );
        }

    }
}
