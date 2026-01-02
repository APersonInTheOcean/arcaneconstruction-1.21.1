package darkdefault.arcaneconstruction.spells;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import darkdefault.arcaneconstruction.entity.SpellProjectileEntity;
import darkdefault.arcaneconstruction.particle.ModParticles;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augment;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Shapes.Effect;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Spell;
import darkdefault.arcaneconstruction.spells.SpellCrafting.SpellModule;

import darkdefault.arcaneconstruction.util.IEntityDataSaver;
import darkdefault.arcaneconstruction.util.SigilData;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.nbt.*;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;


public class SpellHandling {

    public static void cast(IEntityDataSaver p) {
        NbtCompound nbt = p.getPersistentData();

        ServerPlayerEntity player = (ServerPlayerEntity) p;


        NbtList sigils = nbt.getList(ArcaneConstruction.sigilsNbtKey, 8);

//        player.sendMessage(Text.literal(sigils.toString()).styled(style -> {
//            return style.withBold(true);
//        }));

        Spell newSpell = new Spell(nbtListToSequence(sigils));
//        SigilData.addMana(p,-newSpell.getManaCost());
        SigilData.addMana(p,-newSpell.getManaCost());

        castSpell(newSpell,player);

        castMessage(newSpell,player);
    }


    public static void castSpell(Spell s, ServerPlayerEntity player){
        World world = player.getWorld();
        switch(s.getForm()) {
            case Spell.Form.PROJECTILE:
                SpellProjectileEntity spellProj = new SpellProjectileEntity(world,player,s);
                world.spawnEntity(spellProj);
            break;

            case Spell.Form.SELF:
                castSelfSpell(s,player);
            break;


        }






    }

    public static void castSelfSpell(Spell s, ServerPlayerEntity player){

        boolean continueMethod = true;
        for (SpellModule module: s.getModules()) {
            if (module.getShape() instanceof Effect) {
                module.getShape().onSelfCast(s,player,module);
                continueMethod = false;
                break;
            }
        }
        if(continueMethod){
            for (SpellModule module: s.getModules()){
                module.getShape().onSelfCast(s,player,module);

            }
        }


    }



    public static void castMessage(Spell s,ServerPlayerEntity player){
        StringBuilder output = new StringBuilder();
        output.append("You casted ");
        output.append(s.getFormEffectText());
        output.append("with the art of ");

        MutableText text = Text.literal(output.toString());

        int amountOfModulesInSpell = s.getModules().size();



        for (int i = 0; i < amountOfModulesInSpell;i++) {
            SpellModule currentModule = s.getModules().get(i);
            text.append(Text.literal(currentModule.getShape().getName()).styled(style -> style.withColor(TextColor.fromRgb(currentModule.getShape().getColor())).withBold(true)));

            ArrayList<Augment> currentModuleAugments = currentModule.getAugments();
            int amountOfAugmentsInCurrentModule = currentModuleAugments.size();
            if (!currentModuleAugments.isEmpty()){

                text.append(Text.literal(" enhanced with "));
                for (int e = 0; e < amountOfAugmentsInCurrentModule;e++) {
                    Augment currentAugment = currentModuleAugments.get(e);
                    text.append(Text.literal(currentAugment.getName()).styled(style -> style.withColor(TextColor.fromRgb(currentModule.getShape().getColor())).withBold(true)));



                    if (e != amountOfAugmentsInCurrentModule - 1) { // not the last
                        text.append(Text.literal(" and "));

                    }
                }
            }


            if (i != amountOfModulesInSpell - 1) { // not the last
                text.append(Text.literal(", and "));
            }
        }

        player.sendMessage(text,true);

    }

    public static void setCasting(IEntityDataSaver player,boolean b) {

        NbtCompound nbt = player.getPersistentData();
        NbtByte bool = NbtByte.of(b);

        if (!b) {
            setCastingProjectile(player,false);
        }

        nbt.put(ArcaneConstruction.castBoolNbtKey,bool);

    }
    public static void setCastingProjectile(IEntityDataSaver player,boolean b) {

        NbtCompound nbt = player.getPersistentData();
        NbtByte bool = NbtByte.of(b);

        nbt.put(ArcaneConstruction.castingProjectileBoolNbtKey,bool);

    }

    public static boolean isCasting(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        return nbt.getBoolean(ArcaneConstruction.castBoolNbtKey);

    }
    public static boolean isCastingProjectile(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        return nbt.getBoolean(ArcaneConstruction.castingProjectileBoolNbtKey);

    }

    public static boolean canCastNbtList(IEntityDataSaver player, NbtList sigils){
        if (sigils.size() < 2) return false;
        if (sigils.getLast().equals(NbtString.of(ArcaneConstruction.summationString))) return false;
        if(new Spell(SpellHandling.nbtListToSequence(sigils)).getManaCost() > SigilData.getMana(player) ){
            ((PlayerEntity)player).sendMessage(Text.literal("Not enough Mana!").styled(style -> {
                        return style.withColor(ColorHelper.Argb.getArgb(247,124,0));
                    }
            ));
            return false;

        }
        return true;

    }
    public static String nbtListToSequence(NbtList nbtList){
        StringBuilder stringBuilder = new StringBuilder();
        for(NbtElement nbtElement: nbtList){
            stringBuilder.append(nbtElement.toString().toLowerCase().charAt(1));
        }
        return stringBuilder.toString();
    }


}
