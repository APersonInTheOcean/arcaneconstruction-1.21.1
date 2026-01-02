package darkdefault.arcaneconstruction.spells.SpellCrafting.Shapes;

import darkdefault.arcaneconstruction.entity.SpellProjectileEntity;
import darkdefault.arcaneconstruction.spells.SpellCrafting.*;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.Range;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.ColorHelper;

public class Piercing extends SimpleDefaultModelShape {

    @Override
    public void onBlockHit(BlockHitResult blockHitResult, Spell spell, SpellProjectileEntity spellProjectileEntity, SpellModule module) {

    }

    public void addPiercing(SpellProjectileEntity entity, SpellModule module){
        int addPiercing =1;
        for(Augment a: module.getAugments()){
            if(a instanceof Range) {
                addPiercing += 2;
            }
        }
        entity.addPiercingCount(addPiercing);
    }
    @Override
    public void onEntityHit(EntityHitResult entityHitResult, Spell spell, SpellProjectileEntity entity, SpellModule module) {



    }

    @Override
    public void onSelfCast(Spell s, ServerPlayerEntity player, SpellModule module) {

    }

    @Override
    public void getTrailEffect(SpellProjectileEntity spellProjectileEntity, SpellModule module) {

    }

    @Override
    public String getSequence() {
        return "cei";
    }

    @Override
    public String getName() {
        return "Piercing";
    }

    @Override
    public int getColor() {
        return ColorHelper.Argb.getArgb( 100, 200,  200);
    }

}
