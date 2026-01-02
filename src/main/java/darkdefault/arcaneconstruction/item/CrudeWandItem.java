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

public class CrudeWandItem extends Item {

    public CrudeWandItem(Item.Settings settings) {
        super(settings.rarity(Rarity.UNCOMMON));
    }

    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 0;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
//        this.playStopUsingSound(user);
        return stack;
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
    }
}
