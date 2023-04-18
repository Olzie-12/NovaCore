package net.zeeraa.novacore.spigot.utils;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;

public class BukkitUtils {
	public static boolean hasPlugin(@Nonnull String name) {
		return Bukkit.getServer().getPluginManager().getPlugin(name) != null;
	}
}