package me.nahkd.amethystenergy;

import java.text.DecimalFormat;

public class AEUtils {
	private static final DecimalFormat PERCENTAGE = new DecimalFormat("#0%");
	private static final DecimalFormat ENERGY = new DecimalFormat("#0.00");
	private static final DecimalFormat STAT = new DecimalFormat("#0.##");

	public static String formatPercentage(double v) {
		return PERCENTAGE.format(v);
	}

	public static String formatEnergy(float e) {
		return ENERGY.format(e);
	}

	public static String formatStat(float v) {
		return STAT.format(v);
	}
}
