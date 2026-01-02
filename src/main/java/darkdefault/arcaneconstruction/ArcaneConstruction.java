package darkdefault.arcaneconstruction;

import darkdefault.arcaneconstruction.block.ModBlocks;
import darkdefault.arcaneconstruction.entity.ModEntities;
import darkdefault.arcaneconstruction.item.ModItems;
import darkdefault.arcaneconstruction.item.WandItem;
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

import java.util.Objects;

import static darkdefault.arcaneconstruction.util.SigilData.addMana;
import static darkdefault.arcaneconstruction.util.SigilData.setMana;

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
	private static final String lastMainHandItemNbtKey = "lastMainHandItemNbtKey";







	public static final String conjurationString = "Conjuration";
	public static final String evocationString = "Evocation";
	public static final String invocationString = "Invocation";
	public static final String summationString = "Summation";

	public static final TagKey<Item> WANDS =
			TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "wands"));

	public static final int baseManaPlayer = 100;
	public static final int baseSigilCooldownPlayer = 20;




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
		SpellModule.SpellCraftingRegistry.registerShape(new Heal());





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
//








		}



	}


	public static void onPlayerTick(PlayerEntity player) {

		IEntityDataSaver playerWithData = ((IEntityDataSaver)player);
		NbtCompound nbt = ((IEntityDataSaver)player).getPersistentData();




		//Reset Sigils when not holding #wands
		if (!player.getMainHandStack().isIn(ArcaneConstruction.WANDS)) {
			nbt.remove(ArcaneConstruction.sigilsNbtKey);
			nbt.remove(ArcaneConstruction.castBoolNbtKey);
			nbt.remove(ArcaneConstruction.castingProjectileBoolNbtKey);
		}




		if (player.getWorld().isClient()) return;


		//Only do this when mainhand changes, avoids lag.
		String playerMainhand = String.valueOf(player.getMainHandStack().getItem().getName());
		if(!Objects.equals(playerMainhand, nbt.getString(ArcaneConstruction.lastMainHandItemNbtKey))){

			if(player.getMainHandStack().getItem() instanceof WandItem wand){
				// Set MaxMana
				SigilData.setMaxMana(playerWithData, baseManaPlayer + wand.getMaxManaAdded());

				// Set SigilCooldown
				SigilData.setSigilCooldown(playerWithData, baseSigilCooldownPlayer + wand.getSigilCooldownChange());

			}
			else {
				// Set MaxMana
				SigilData.setMaxMana(playerWithData, baseManaPlayer);
				// Set SigilCooldown
				SigilData.setSigilCooldown(playerWithData, baseSigilCooldownPlayer );

			}

			// Reset PlayerLastMainhand
			nbt.putString(ArcaneConstruction.lastMainHandItemNbtKey,playerMainhand);
		}




		//Mana Regen
		if (SigilData.getMana(playerWithData)< SigilData.getMaxMana(playerWithData)
		) {
			if (((ServerPlayerEntity)player).getServer().getTicks() % 5 == 0) {
				addMana(playerWithData, getManaRegen(playerWithData));


			}
		} else if (SigilData.getMana(playerWithData)> SigilData.getMaxMana(playerWithData)) {
			if (((ServerPlayerEntity)player).getServer().getTicks() % 5 == 0) {
				setMana(playerWithData, SigilData.getMaxMana(playerWithData));


			}
		}

		//Sigil Cooldown Timer
		if (!SigilData.isSigilCooldownOver(playerWithData)
		){
			if (SigilData.getSigilCooldownTimer(playerWithData) < SigilData.getSigilCooldown(playerWithData)){
				SigilData.addSigilCooldownTimer(playerWithData, 1);
			}
			else {
				SigilData.addSigilCooldownTimer(playerWithData, -SigilData.getSigilCooldown(playerWithData));
				SigilData.setSigilCooldownOver(playerWithData,true);
			}

		}

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