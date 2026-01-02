package darkdefault.arcaneconstruction.item;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems{
    public static final Item ARCANITE = registerItem("arcanite", new Item(new Item.Settings()));
    public static final Item CRUDE_WAND = registerItem("crude_wand", new CrudeWandItem(new Item.Settings().maxCount(1)));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(ArcaneConstruction.MOD_ID, name), item);
    }

    public static void registerModItems() {
        ArcaneConstruction.LOGGER.info("Registering Mod Items for " + ArcaneConstruction.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(ARCANITE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(CRUDE_WAND);
        });
    }
}
