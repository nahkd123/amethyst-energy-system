package me.nahkd.amethystenergy.blocks.client;

import java.util.Map;

import me.nahkd.amethystenergy.blocks.workbench.AmethystWorkbenchScreenHandler;
import me.nahkd.amethystenergy.modules.ModuleSlot;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AmethystWorkbenchScreen extends HandledScreen<AmethystWorkbenchScreenHandler> {
	private static final Identifier TEXTURE = new Identifier("amethystenergy", "textures/gui/container/amethyst_workbench.png");
	private static final Map<ModuleSlot, int[]> SLOT_PLACEHOLDERS = Map.of(
			ModuleSlot.HANDLE, new int[] { 176, 0 },
			ModuleSlot.BINDING, new int[] { 176, 18 },
			ModuleSlot.SWORD_BLADE, new int[] { 176, 36 },
			ModuleSlot.HOE_BLADE, new int[] { 176, 54 }
			);

	private AmethystWorkbenchScreenHandler handler;

	public AmethystWorkbenchScreen(AmethystWorkbenchScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.handler = handler;
	}

	@Override
	protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
		var cornerX = (width - backgroundWidth) / 2;
		var cornerY = (height - backgroundHeight) / 2;
		ctx.drawTexture(TEXTURE, cornerX, cornerY, 0, 0, backgroundWidth, backgroundHeight);

		for (int i = 0; i < handler.moduleSlotTypes.size(); i++) {
			var slotType = handler.moduleSlotTypes.get(i);
			var slotIndex = handler.moduleSlotIndexes.get(i);
			var rect = SLOT_PLACEHOLDERS.get(slotType);
			if (rect == null) rect = new int[] { 9, 51 };
			var x = (i % 7) * 18 + 43;
			var y = (i / 7) * 18 + 16;

			ctx.drawTexture(TEXTURE, cornerX + x, cornerY + y, rect[0], rect[1], 18, 18);
			ctx.drawText(textRenderer, "" + (slotIndex + 1), cornerX + x + 2, cornerY + y + 2, 0x303030, false);
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderBackground(context);
		super.render(context, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}
}
