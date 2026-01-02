package darkdefault.arcaneconstruction.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class WandItem extends Item {

    public WandItem(Item.Settings settings) {
        super(settings);
    }

    public int getManaDiscount(){
        return 0;
    }
    public int getMaxManaAdded(){
        return 0;
    }
    public int getManaRegenAdded(){
        return 0;
    }
    public int getSigilCooldownChange(){
        return 0;
    }

}
