package me.nahkd.amethystenergy;

import me.nahkd.amethystenergy.blocks.client.AESBlockScreens;
import me.nahkd.amethystenergy.entities.client.AESRenderers;
import net.fabricmc.api.ClientModInitializer;

public class AmethystEnergyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
    	AESBlockScreens.registerAll();
    	AESRenderers.registerAll();
    }
}