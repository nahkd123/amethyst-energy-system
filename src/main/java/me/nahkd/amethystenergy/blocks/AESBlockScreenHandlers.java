package me.nahkd.amethystenergy.blocks;

import me.nahkd.amethystenergy.AmethystEnergy;
import me.nahkd.amethystenergy.blocks.workbench.AmethystWorkbenchScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;

public class AESBlockScreenHandlers {
	public static final ScreenHandlerType<AmethystWorkbenchScreenHandler> AMETHYST_WORKBENCH = new ScreenHandlerType<>(AmethystWorkbenchScreenHandler::new, FeatureFlags.VANILLA_FEATURES);

	public static void registerAll() {
		Registry.register(Registries.SCREEN_HANDLER, AmethystEnergy.id("amethyst_workbench"), AMETHYST_WORKBENCH);
	}
}
