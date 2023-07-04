package me.nahkd.amethystenergy.aes;

/**
 * <p>An interface for accessing stored AE in an item/block.</p>
 * @author nahkd
 *
 */
public interface AmethystEnergyInterface {
	public float getMaxAmethystEnergy();

	public float getCurrentAmethystEnergy();
	public void setCurrentAmethystEnergy(float amount);

	default boolean useAmethystEnergy(float amount) {
		var current = getCurrentAmethystEnergy();
		if ((current - amount) < 0) return false;
		setCurrentAmethystEnergy(current - amount);
		return true;
	}
}
