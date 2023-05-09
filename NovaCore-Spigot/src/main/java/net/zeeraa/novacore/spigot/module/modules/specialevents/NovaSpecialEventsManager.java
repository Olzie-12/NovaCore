package net.zeeraa.novacore.spigot.module.modules.specialevents;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Listener;

import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.spigot.module.NovaModule;
import net.zeeraa.novacore.spigot.module.modules.specialevents.event.NovaTNTPrimeEvent;
import net.zeeraa.novacore.spigot.tasks.SimpleTask;

/**
 * This module enables some special events in novacore
 * 
 * @author Zeeraa
 */
public class NovaSpecialEventsManager extends NovaModule implements Listener {
	private Task tickTask;
	private List<TNTPrimed> foundTNT;

	public NovaSpecialEventsManager() {
		super("NovaCore.SpecialEventsManager");
		foundTNT = new ArrayList<>();
	}

	@Override
	public void onLoad() {
		tickTask = new SimpleTask(new Runnable() {
			@Override
			public void run() {
				Bukkit.getServer().getWorlds().forEach(w -> {
					w.getEntitiesByClass(TNTPrimed.class).stream().filter(tnt -> !foundTNT.contains(tnt)).forEach(tnt -> {
						foundTNT.add(tnt);
						NovaTNTPrimeEvent event = new NovaTNTPrimeEvent(tnt);
						Bukkit.getServer().getPluginManager().callEvent(event);
						if(event.isCancelled()) {
							tnt.remove();
						}
					});
				});
				foundTNT.removeIf(Entity::isDead);
			}
		}, 0L);
	}

	@Override
	public void onEnable() throws Exception {
		foundTNT.clear();
		Task.tryStartTask(tickTask);
	}

	@Override
	public void onDisable() throws Exception {
		foundTNT.clear();
		Task.tryStopTask(tickTask);
	}
}