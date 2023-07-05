package me.nahkd.amethystenergy;

import me.nahkd.amethystenergy.blocks.client.AESBlockScreens;
import me.nahkd.amethystenergy.entities.client.AmethystEntityRenderers;
import net.fabricmc.api.ClientModInitializer;

public class AmethystEnergyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
    	AESBlockScreens.registerAll();
    	AmethystEntityRenderers.registerAll();
    }
}