package net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.fireresistance;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.json.JSONObject;

import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModule;
import net.zeeraa.novacore.spigot.tasks.SimpleTask;

public class FireReistance extends MapModule implements Listener {
	private boolean extinguish;
	private Task task;

	public FireReistance(JSONObject json) {
		super(json);

		extinguish = json.optBoolean("extinguish", false);
		task = null;
	}

	@Override
	public void onGameStart(Game game) {
		Bukkit.getServer().getPluginManager().registerEvents(this, game.getPlugin());
		if (extinguish) {
			task = new SimpleTask(game.getPlugin(), () -> Bukkit.getServer().getOnlinePlayers().forEach(player -> player.setFireTicks(0)), 1L);
			Task.tryStartTask(task);
		}
	}

	@Override
	public void onGameEnd(Game game) {
		HandlerList.unregisterAll(this);
		Task.tryStopTask(task);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getCause() == DamageCause.LAVA || e.getCause() == DamageCause.FIRE || e.getCause() == DamageCause.FIRE_TICK) {
				e.setCancelled(true);
			}
		}
	}

	@Override
	public String getSummaryString() {
		return ChatColor.GOLD + "Extinguish: " + ChatColor.AQUA + extinguish;
	}
}