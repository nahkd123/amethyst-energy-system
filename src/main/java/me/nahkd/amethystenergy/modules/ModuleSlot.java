package me.nahkd.amethystenergy.modules;

import net.minecraft.text.Text;

public enum ModuleSlot {
	// Universal slot
	HANDLE("Handle", Text.literal("Handle"), Text.literal("Handle")),
	BINDING("Binding", Text.literal("Binding"), Text.literal("Binding")),

	// Tool specific slot
	SWORD_BLADE("SwordBlade", Text.literal("Sword Blade"), Text.literal("Blade")),
	HOE_BLADE("HoeBlade", Text.literal("Hoe Blade"), Text.literal("Blade")),

	PICKAXE_HEAD("PickaxeHead", Text.literal("Pickaxe Head"), Text.literal("Head")),
	AXE_HEAD("AxeHead", Text.literal("Axe Head"), Text.literal("Head")),
	SHOVEL_HEAD("ShovelHead", Text.literal("Shovel Head"), Text.literal("Head")),
	DIGGING_HEAD("DiggingToolHead", Text.literal("Digging Tool Head"), Text.literal("Head")); // Applies to pickaxes, axes and shovels

	public final String slotName;
	public final Text displayText;
	public final Text toolDisplayText;

	ModuleSlot(String slotName, Text displayText, Text toolDisplayText) {
		this.slotName = slotName;
		this.displayText = displayText;
		this.toolDisplayText = toolDisplayText;
	}
}
