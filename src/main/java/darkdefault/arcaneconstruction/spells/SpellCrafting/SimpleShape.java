package darkdefault.arcaneconstruction.spells.SpellCrafting;

import darkdefault.arcaneconstruction.entity.SpellProjectileEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;

public abstract class SimpleShape implements Shape{

    //Simple just means that it doesn't interrupt any on... events.

    @Override
    public void onBlockHit(BlockHitResult blockHitResult, Spell spell, SpellProjectileEntity entity, SpellModule module) {

    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult, Spell spell, SpellProjectileEntity entity, SpellModule module) {

    }

    @Override
    public void onSelfCast(Spell s, ServerPlayerEntity player, SpellModule module) {

    }

    @Override
    public void getTrailEffect(SpellProjectileEntity entity, SpellModule module) {

    }

    @Override
    public boolean shouldInterruptOnBlockHit() {
        return false;
    }

    @Override
    public boolean shouldInterruptOnEntityHit() {
        return false;
    }

    @Override
    public boolean shouldInterruptOnSelfCast() {
        return false;
    }

    @Override
    public String getSequence() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return getSequence().length()*20;
    }
}
