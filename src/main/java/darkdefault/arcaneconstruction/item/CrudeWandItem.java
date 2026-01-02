package darkdefault.arcaneconstruction.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class CrudeWandItem extends WandItem {

    public CrudeWandItem(Item.Settings settings) {
        super(settings.rarity(Rarity.UNCOMMON));
    }

    @Override
    public int getManaDiscount() {
        return 5;
    }
    @Override
    public int getMaxManaAdded() {
        return 100;
    }
    public int getSigilCooldownChange(){
        return -10;
    }
}
