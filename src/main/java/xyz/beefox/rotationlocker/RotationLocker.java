package xyz.beefox.rotationlocker;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;

public class RotationLocker implements ModInitializer {
	
	public static final Logger LOGGER = LoggerFactory.getLogger("rotationlocker");

	private static KeyBinding rotationLockKeyBinding;
	@Nullable
	public static Direction lockDirection;
	private static Boolean enableKey;

	@Override
	public void onInitialize() {

		rotationLockKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.rotationlocker.lock", // The translation key of the keybinding's name
			InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
			GLFW.GLFW_KEY_K, // The keycode of the key
			"category.rotationlocker" // The translation key of the keybinding's category.
		));
		enableKey = true;
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if(client.player != null){
				if (rotationLockKeyBinding.isPressed() && enableKey == true) {
					enableKey = false;
					if(lockDirection == null){
						lockDirection = Direction.getEntityFacingOrder(client.player)[0];
						client.player.sendMessage(Text.translatable("text.rotationlocker.lock", lockDirection.asString()).formatted(Formatting.GRAY), false);
					} else {
						if(client.options.sneakKey.isPressed()){
							lockDirection = lockDirection.getOpposite();
							client.player.sendMessage(Text.translatable("text.rotationlocker.flip", lockDirection.asString()).formatted(Formatting.GRAY));
						} else {
							lockDirection = null;
							client.player.sendMessage(Text.translatable("text.rotationlocker.unlock").formatted(Formatting.GRAY), false);	
						}
						}
				}
				if (!rotationLockKeyBinding.isPressed() && enableKey == false){
					enableKey = true;
				}
			}
			
		});
	}
}
