package net.zeeraa.novacore.spigot.utils;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.json.JSONObject;

public class RGBColorData {
	private int r;
	private int g;
	private int b;

	public RGBColorData(int r, int g, int b) {
		this.r = 0;
		this.g = 0;
		this.b = 0;

		setR(r);
		setG(g);
		setB(b);
	}

	public RGBColorData(@Nonnull org.bukkit.Color color) {
		this.r = color.getRed();
		this.g = color.getGreen();
		this.b = color.getBlue();
	}

	public RGBColorData(@Nonnull java.awt.Color color) {
		this.r = color.getRed();
		this.g = color.getGreen();
		this.b = color.getBlue();
	}

	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}

	public void setR(int r) {
		if (r < 0) {
			throw new IllegalArgumentException("r cant be less than 0");
		}
		if (r > 255) {
			throw new IllegalArgumentException("r cant be larget than 255");
		}
		this.r = r;
	}

	public void setG(int g) {
		if (g < 0) {
			throw new IllegalArgumentException("g cant be less than 0");
		}
		if (g > 255) {
			throw new IllegalArgumentException("g cant be larget than 255");
		}
		this.g = g;
	}

	public void setB(int b) {
		if (b < 0) {
			throw new IllegalArgumentException("b cant be less than 0");
		}
		if (b > 255) {
			throw new IllegalArgumentException("b cant be larget than 255");
		}
		this.b = b;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("r", r);
		json.put("g", g);
		json.put("b", b);
		return json;
	}

	public static RGBColorData fromJSON(@Nonnull JSONObject json) {
		return new RGBColorData(json.getInt("r"), json.getInt("g"), json.getInt("b"));
	}

	public org.bukkit.Color toBukkitColor() {
		return org.bukkit.Color.fromRGB(r, g, b);
	}

	public java.awt.Color toAWTColor() {
		return new java.awt.Color(r, g, b);
	}

	public Color getColorByChatColor(@Nonnull net.md_5.bungee.api.ChatColor color) {
		switch (color) {
		case BLACK:
			return Color.BLACK;
		case DARK_BLUE:
			return Color.NAVY;
		case DARK_GREEN:
			return Color.GREEN;
		case DARK_AQUA:
			return Color.TEAL;
		case DARK_RED:
			return Color.MAROON;
		case DARK_PURPLE:
			return Color.PURPLE;
		case GOLD:
			return Color.ORANGE;
		case GRAY:
			return Color.SILVER;
		case DARK_GRAY:
			return Color.GRAY;
		case BLUE:
			return Color.BLUE;
		case GREEN:
			return Color.LIME;
		case AQUA:
			return Color.AQUA;
		case RED:
			return Color.RED;
		case LIGHT_PURPLE:
			return Color.FUCHSIA;
		case YELLOW:
			return Color.YELLOW;
		case WHITE:
			return Color.WHITE;
		default:
			return Color.WHITE;
		}
	}

	public static RGBColorData fromChatColor(@Nonnull ChatColor color) {
		return ChatColorRGBMapper.chatColorToRGBColorData(color);
	}

	public static RGBColorData fromChatColor(@Nonnull net.md_5.bungee.api.ChatColor color) {
		return ChatColorRGBMapper.chatColorToRGBColorData(color);
	}

	public static RGBColorData fromBukkitColor(@Nonnull org.bukkit.Color color) {
		return new RGBColorData(color);
	}

	public static RGBColorData fromAWTColor(@Nonnull java.awt.Color color) {
		return new RGBColorData(color);
	}
}