package net.zeeraa.novacore.spigot.delayedrunner;

import org.bukkit.scheduler.BukkitRunnable;

import net.zeeraa.novacore.commons.utils.DelayedRunnerImplementation;
import net.zeeraa.novacore.spigot.NovaCore;

public class DelayedRunnerImplementationSpigot implements DelayedRunnerImplementation {
	@Override
	public void runDelayed(Runnable runnable, long ticks) {
		new BukkitRunnable() {
			@Override
			public void run() {
				runnable.run();
			}
		}.runTaskLater(NovaCore.getInstance(), ticks);
	}
}