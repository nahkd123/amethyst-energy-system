package me.nahkd.amethystenergy;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.nahkd.amethystenergy.blocks.AESBlockEntities;
import me.nahkd.amethystenergy.blocks.AESBlockScreenHandlers;
import me.nahkd.amethystenergy.blocks.AESBlocks;
import me.nahkd.amethystenergy.entities.AmethystEntities;
import me.nahkd.amethystenergy.modules.Modules;
import me.nahkd.amethystenergy.tools.AmethystTools;
import me.nahkd.amethystenergy.utilities.AESUtilities;

public class AmethystEnergy implements ModInitializer {
	public static final String MODID = "amethystenergy";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
    	AESItemGroups.registerAll();

    	AESBlocks.registerAll();
    	AESBlockEntities.registerAll();
    	AESBlockScreenHandlers.registerAll();

    	AmethystTools.registerAll();
        Modules.registerAll();
        AmethystEntities.registerAll();
        AESUtilities.registerAll();
    }
   
    public static Identifier id(String id) {
    	return new Identifier(MODID, id);
    }
}