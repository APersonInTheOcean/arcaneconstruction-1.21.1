package darkdefault.arcaneconstruction.spells.SpellCrafting.Shapes;

import darkdefault.arcaneconstruction.entity.SpellProjectileEntity;
import darkdefault.arcaneconstruction.spells.SpellCrafting.*;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.Range;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.ColorHelper;

public class NoClip extends SimpleDefaultModelShape {

    public void addNoClip(SpellProjectileEntity entity, SpellModule module){
        int addNoClipCount =2;
        for(Augment a: module.getAugments()){
            if(a instanceof Range) {
                addNoClipCount += 2;
            }
        }
        entity.addNoClipCount(addNoClipCount);
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
    public String getSequence() {
        return "cee";
    }

    @Override
    public String getName() {
        return "NoClip";
    }

    @Override
    public int getColor() {
        return ColorHelper.Argb.getArgb( 200, 100,  200);
    }

}
