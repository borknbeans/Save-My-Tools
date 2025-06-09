package borknbeans.savemytools;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class SaveMyToolsClient implements ClientModInitializer {

	public static KeyBinding ignoreWarningKeyBind;

	@Override
	public void onInitializeClient() {
		ignoreWarningKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.savemytools.ignorewarning",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_LEFT_ALT,
			"category.savemytools.title"
		));
	}
}