package me.nahkd.amethystenergy.modules;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModuleInstance {
	private NbtCompound moduleData;

	public ModuleInstance(NbtCompound moduleData) {
		this.moduleData = moduleData != null? moduleData : new NbtCompound();
	}

	public ModuleInstance(Module type, int quality) {
		this(null);
		setModuleType(type);
		setModuleQuality(quality);
	}

	public boolean isEmpty() {
		return moduleData.isEmpty();
	}

	public NbtCompound getModuleData() {
		return moduleData;
	}

	public Module getModuleType() {
		if (!moduleData.contains(Module.TAG_ID, NbtElement.STRING_TYPE)) return null;
		var id = moduleData.getString(Module.TAG_ID);
		var item = Registries.ITEM.get(new Identifier(id));
		return (item != null && item instanceof Module module)? module : null;
	}

	public void setModuleType(Module module) {
		if (module == null) {
			moduleData.remove(Module.TAG_ID);
			return;
		}

		moduleData.putString(Module.TAG_ID, Registries.ITEM.getId(module).toString());
	}

	public int getModuleQuality() {
		return moduleData.getInt(Module.TAG_QUALITY);
	}

	public void setModuleQuality(int quality) {
		moduleData.putInt(Module.TAG_QUALITY, quality);
	}

	public void clearModule() {
		moduleData.remove(Module.TAG_ID);
		moduleData.remove(Module.TAG_QUALITY);
	}
}
