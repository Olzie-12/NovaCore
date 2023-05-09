package net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.infinitefood;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONObject;

import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModule;

public class InfiniteFoodMapModule extends MapModule implements Listener {
	public InfiniteFoodMapModule(JSONObject json) {
		super(json);
	}

	@Override
	public void onGameStart(Game game) {
		Bukkit.getServer().getPluginManager().registerEvents(this, game.getPlugin());
		Bukkit.getServer().getOnlinePlayers().forEach(p -> p.setFoodLevel(20));
	}

	@Override
	public void onGameEnd(Game game) {
		HandlerList.unregisterAll(this);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent e) {
		e.getPlayer().setFoodLevel(20);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayer(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
}