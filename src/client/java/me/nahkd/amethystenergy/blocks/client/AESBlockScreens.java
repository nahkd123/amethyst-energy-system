package me.nahkd.amethystenergy.blocks.client;

import me.nahkd.amethystenergy.blocks.AESBlockScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class AESBlockScreens {
	public static void registerAll() {
		HandledScreens.register(AESBlockScreenHandlers.AMETHYST_WORKBENCH, AmethystWorkbenchScreen::new);
	}
}
