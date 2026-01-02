package darkdefault.arcaneconstruction.event;

import darkdefault.arcaneconstruction.ArcaneConstruction;
import darkdefault.arcaneconstruction.networking.ModMessages;
import darkdefault.arcaneconstruction.spells.SpellCrafting.Spell;
import darkdefault.arcaneconstruction.spells.SpellHandling;
import darkdefault.arcaneconstruction.util.IEntityDataSaver;
import darkdefault.arcaneconstruction.util.SigilData;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_ARCANECONSTRUCTION = "key.category.arcaneconstruction";
    public static final String KEY_CONJURATION = "key.arcaneconstruction.conjuration";
    public static final String KEY_INVOCATION = "key.arcaneconstruction.invocation";
    public static final String KEY_EVOCATION = "key.arcaneconstruction.evocation";
    public static final String KEY_SUMMATION = "key.arcaneconstruction.summation";
    public static final String KEY_CAST = "key.arcaneconstruction.cast";

    public static KeyBinding conjurationKey;
    public static KeyBinding invocationKey;
    public static KeyBinding evocationKey;
    public static KeyBinding summationKey;
    public static KeyBinding castKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {



            if (client.world == null) return;
            if (client.player == null) return;


            boolean conjuration = wasPressed(conjurationKey);
            boolean invocation = wasPressed(invocationKey);
            boolean evocation = wasPressed(evocationKey);
            boolean summation = wasPressed(summationKey);
            boolean cast = wasPressed(castKey);

            if(conjuration || invocation || evocation || summation || cast) {
                ModMessages.AddSigilC2SPayload payload = new ModMessages.AddSigilC2SPayload(
                        conjuration, invocation, evocation, summation, cast
                );
                ClientPlayNetworking.send(payload);
            }



        });
    }


    public static void register() {
        conjurationKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_CONJURATION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                KEY_CATEGORY_ARCANECONSTRUCTION
        ));
        invocationKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_INVOCATION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                KEY_CATEGORY_ARCANECONSTRUCTION
        ));
        evocationKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_EVOCATION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                KEY_CATEGORY_ARCANECONSTRUCTION
        ));
        summationKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SUMMATION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                KEY_CATEGORY_ARCANECONSTRUCTION
        ));
        castKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_CAST,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                KEY_CATEGORY_ARCANECONSTRUCTION
        ));

        registerKeyInputs();
    }

    public static boolean wasPressed(KeyBinding key){
        boolean waspressed = false;
        while(key.wasPressed()){
            waspressed=true;
            key.setPressed(false);
        }
        return waspressed;
    }


}
