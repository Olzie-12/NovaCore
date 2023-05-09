package net.zeeraa.novacore.spigot.utils;

import javax.annotation.Nonnull;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.zeeraa.novacore.spigot.NovaCore;

public class DelayedTaskCreator {
	public static final BukkitTask runLater(@Nonnull Runnable runnable, long delay) {
		return DelayedTaskCreator.runLater(NovaCore.getInstance(), runnable, delay);
	}

	public static final BukkitTask runLater(@Nonnull Plugin plugin, @Nonnull Runnable runnable, long delay) {
		return new BukkitRunnable() {
			@Override
			public void run() {
				runnable.run();
			}
		}.runTaskLater(plugin, delay);
	}

	public static final BukkitTask runLaterAsync(@Nonnull Runnable runnable, long delay) {
		return DelayedTaskCreator.runLaterAsync(NovaCore.getInstance(), runnable, delay);
	}

	public static final BukkitTask runLaterAsync(@Nonnull Plugin plugin, @Nonnull Runnable runnable, long delay) {
		return new BukkitRunnable() {
			@Override
			public void run() {
				runnable.run();
			}
		}.runTaskLaterAsynchronously(plugin, delay);
	}
}