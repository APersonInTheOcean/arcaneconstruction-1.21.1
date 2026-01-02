package darkdefault.arcaneconstruction.entity;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<SpellProjectileEntity> SPELLPROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(ArcaneConstruction.MOD_ID, "spellprojectile"),
            EntityType.Builder.<SpellProjectileEntity>create(SpellProjectileEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25f, 0.25f).build());


    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, id, type.build(id));
    }
    public static void registerModEntities() {
        ArcaneConstruction.LOGGER.info("Registering Mod Entities for " + ArcaneConstruction.MOD_ID);
    }

}
