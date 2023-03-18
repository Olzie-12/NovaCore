package net.zeeraa.novacore.spigot.abstraction.particle;

import org.bukkit.Color;

/**
 * NovaCore version of
 * https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.DustOptions.html
 * 
 * @author Zeeraa
 */
public class NovaDustOptions {
	public static final NovaDustOptions AQUA = new NovaDustOptions(Color.AQUA);
	public static final NovaDustOptions BLACK = new NovaDustOptions(Color.BLACK);
	public static final NovaDustOptions BLUE = new NovaDustOptions(Color.BLUE);
	public static final NovaDustOptions FUCHSIA = new NovaDustOptions(Color.FUCHSIA);
	public static final NovaDustOptions GRAY = new NovaDustOptions(Color.GRAY);
	public static final NovaDustOptions GREEN = new NovaDustOptions(Color.GREEN);
	public static final NovaDustOptions LIME = new NovaDustOptions(Color.LIME);
	public static final NovaDustOptions MAROON = new NovaDustOptions(Color.MAROON);
	public static final NovaDustOptions NAVY = new NovaDustOptions(Color.NAVY);
	public static final NovaDustOptions OLIVE = new NovaDustOptions(Color.OLIVE);
	public static final NovaDustOptions ORANGE = new NovaDustOptions(Color.ORANGE);
	public static final NovaDustOptions PURPLE = new NovaDustOptions(Color.PURPLE);
	public static final NovaDustOptions RED = new NovaDustOptions(Color.RED);
	public static final NovaDustOptions SILVER = new NovaDustOptions(Color.SILVER);
	public static final NovaDustOptions TEAL = new NovaDustOptions(Color.TEAL);
	public static final NovaDustOptions WHITE = new NovaDustOptions(Color.WHITE);
	public static final NovaDustOptions YELLOW = new NovaDustOptions(Color.YELLOW);

	private final Color color;
	private final java.awt.Color awtColor;

	public NovaDustOptions(java.awt.Color color) {
		this.color = Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
		this.awtColor = color;
	}

	public NovaDustOptions(Color color) {
		this.color = color;
		this.awtColor = new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue());
	}

	public java.awt.Color getAwtColor() {
		return awtColor;
	}

	public Color getColor() {
		return color;
	}
}