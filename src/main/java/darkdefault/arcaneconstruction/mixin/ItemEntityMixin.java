package darkdefault.arcaneconstruction.mixin;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import darkdefault.arcaneconstruction.item.ArcaniteItem;
import darkdefault.arcaneconstruction.item.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {


    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    public void tick(CallbackInfo ci) {
        ItemEntity item = (ItemEntity)(Object)this;
        if (item.getStack().isOf(Items.AMETHYST_SHARD)
            && (item.getWorld().getBlockState(item.getBlockPos().offset(Direction.DOWN,1)) == Blocks.ENCHANTING_TABLE.getDefaultState()
                || item.getWorld().getBlockState(item.getBlockPos()) == Blocks.ENCHANTING_TABLE.getDefaultState())
        ) {
            createArcanite(item);
        }
    }
    public void createArcanite(ItemEntity item){
        if(item.getWorld().isClient()) return;
        ServerWorld world = (ServerWorld) item.getWorld();
        BlockPos blockPos = item.getBlockPos();

        world.spawnParticles(
                ParticleTypes.END_ROD,
                item.getX(),
                item.getY(),
                item.getZ(),
                100,
                0.5, 0.5, 0.5,
                0.1
        );
        world.playSound(
                null,
                blockPos,
                SoundEvents.BLOCK_BEACON_POWER_SELECT,
                SoundCategory.BLOCKS,
                0.1f,
                2f
        );

        ItemStack arcaniteItem = new ItemStack(ModItems.ARCANITE);
        arcaniteItem.setCount(item.getStack().getCount());
        item.setStack(arcaniteItem);

    }
}
