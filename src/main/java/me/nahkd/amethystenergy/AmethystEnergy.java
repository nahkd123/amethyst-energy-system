package me.nahkd.amethystenergy;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.nahkd.amethystenergy.modules.Modules;
import me.nahkd.amethystenergy.tools.AmethystTools;

public class AmethystEnergy implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("amethystenergy");

    @Override
    public void onInitialize() {
    	AmethystTools.registerAll();
        Modules.registerAll();
    }
}