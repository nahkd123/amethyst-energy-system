package me.nahkd.amethystenergy.modules;

import net.minecraft.text.Text;

public enum ModuleSlot {
	// Universal slot
	HANDLE(Text.literal("Handle"), Text.literal("Handle")),
	BINDING(Text.literal("Binding"), Text.literal("Binding")),

	// Tool specific slot
	SWORD_BLADE(Text.literal("Sword Blade"), Text.literal("Blade")),
	HOE_BLADE(Text.literal("Hoe Blade"), Text.literal("Blade")),

	PICKAXE_HEAD(Text.literal("Pickaxe Head"), Text.literal("Head")),
	AXE_HEAD(Text.literal("Axe Head"), Text.literal("Head")),
	SHOVEL_HEAD(Text.literal("Shovel Head"), Text.literal("Head")),
	DIGGING_HEAD(Text.literal("Digging Tool Head"), Text.literal("Head")); // Applies to pickaxes, axes and shovels

	public final Text displayText;
	public final Text toolDisplayText;

	ModuleSlot(Text displayText, Text toolDisplayText) {
		this.displayText = displayText;
		this.toolDisplayText = toolDisplayText;
	}
}
