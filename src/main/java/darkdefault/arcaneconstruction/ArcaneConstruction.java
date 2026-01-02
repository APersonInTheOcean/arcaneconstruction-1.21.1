package darkdefault.arcaneconstruction;

import darkdefault.arcaneconstruction.block.ModBlocks;
import darkdefault.arcaneconstruction.entity.ModEntities;
import darkdefault.arcaneconstruction.item.ModItems;
import darkdefault.arcaneconstruction.networking.ModMessages;
import darkdefault.arcaneconstruction.particle.ModParticles;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Augments.*;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Shapes.*;
import darkdefault.arcaneconstruction.spells.SpellCrafting.SpellModule;
import darkdefault.arcaneconstruction.util.IEntityDataSaver;
import darkdefault.arcaneconstruction.util.SigilData;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static darkdefault.arcaneconstruction.util.SigilData.addMana;

public class ArcaneConstruction implements ModInitializer {

	public static final String MOD_ID = "arcaneconstruction";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final String sigilsNbtKey = "sigils";
	public static final String castBoolNbtKey = "castBool";
	public static final String castingProjectileBoolNbtKey = "castingProjectileBool";
	public static final String manaNbtKey = "mana";
	public static final String maxManaNbtKey = "maxMana";
	public static final String sigilCooldownNbtKey = "sigilCooldown";
	public static final String sigilCooldownOverNbtKey = "sigilCooldownOverBool";
	public static final String sigilCooldownTimerNbtKey = "sigilCooldownTimer";







	public static final String conjurationString = "Conjuration";
	public static final String evocationString = "Evocation";
	public static final String invocationString = "Invocation";
	public static final String summationString = "Summation";

	public static final TagKey<Item> WANDS =
			TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "wands"));


	@Override
	public void onInitialize() {

		ServerTickEvents.END_SERVER_TICK.register(ArcaneConstruction::onServerTick);


        ModItems.registerModItems();
		ModBlocks.registerModBlocks();


		ModMessages.registerPayloadTypes();
		ModMessages.registerC2SHandlers();

		ModEntities.registerModEntities();

		ModParticles.registerParticles();

		SpellModule.SpellCraftingRegistry.registerShape(new Explosion());
		SpellModule.SpellCraftingRegistry.registerShape(new NoClip());
		SpellModule.SpellCraftingRegistry.registerShape(new Fire());
		SpellModule.SpellCraftingRegistry.registerShape(new Piercing());
		SpellModule.SpellCraftingRegistry.registerShape(new Break());
		SpellModule.SpellCraftingRegistry.registerShape(new Freeze());
		SpellModule.SpellCraftingRegistry.registerShape(new Wind());
		SpellModule.SpellCraftingRegistry.registerShape(new Effect());





		SpellModule.SpellCraftingRegistry.registerAugment(new Speed());
		SpellModule.SpellCraftingRegistry.registerAugment(new Range());
		SpellModule.SpellCraftingRegistry.registerAugment(new Power());
		SpellModule.SpellCraftingRegistry.registerAugment(new AOE());
		SpellModule.SpellCraftingRegistry.registerAugment(new Duration());




	}

	public static void onServerTick(MinecraftServer server){


		for (ServerPlayerEntity playerServer: server.getPlayerManager().getPlayerList()){
			// Set MaxMana
			if(playerServer.getServer() == null) return;
			IEntityDataSaver player = (IEntityDataSaver) playerServer;
			SigilData.setMaxMana(player, 100);
			// Set SigilCooldown
			SigilData.setSigilCooldown(player, 20);



			//Mana Regen
			if (SigilData.getMana(player)< SigilData.getMaxMana(player)
			) {
				if (playerServer.getServer().getTicks() % 5 == 0) {
					addMana(player, getManaRegen(player));


				}
			}
			//Sigil Cooldown Timer
			if (!SigilData.isSigilCooldownOver(player)
			){
				if (SigilData.getSigilCooldownTimer(player) < SigilData.getSigilCooldown(player)){
					SigilData.addSigilCooldownTimer(player, 1);
				}
				else {
					SigilData.addSigilCooldownTimer(player, -SigilData.getSigilCooldown(player));
					SigilData.setSigilCooldownOver(player,true);
				}

			}



		}



	}


	public static void onPlayerTick(PlayerEntity player) {

		NbtCompound nbt = ((IEntityDataSaver)player).getPersistentData();




		//Reset Sigils when not holding #wands
		if (!player.getMainHandStack().isIn(ArcaneConstruction.WANDS)) {
			nbt.remove(ArcaneConstruction.sigilsNbtKey);
			nbt.remove(ArcaneConstruction.castBoolNbtKey);
			nbt.remove(ArcaneConstruction.castingProjectileBoolNbtKey);
		}




		if (player.getWorld().isClient()) return;



		//While casting effects
        if (nbt.getBoolean(ArcaneConstruction.castBoolNbtKey)){
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    5,
                    4,
                    true,
                    false,
                    false
            ));
        }
	}

	public static int getManaRegen(IEntityDataSaver player){
		int baseManaRegen = SigilData.getMaxMana(player)/100;

		return baseManaRegen;
	}
}