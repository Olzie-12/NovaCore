package net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.buildlimit;

import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.gameengine.NovaCoreGameEngine;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.json.JSONObject;

public class BuildLimitMapModule extends MapModule implements Listener {
	private int maxLimit;
	private int minLimit;
	private boolean sendMessage;
	private String message;

	@Override
	public void onGameStart(Game game) {
		Bukkit.getServer().getPluginManager().registerEvents(this, NovaCoreGameEngine.getInstance());
	}

	@Override
	public void onGameEnd(Game game) {
		HandlerList.unregisterAll(this);
	}

	public BuildLimitMapModule(JSONObject json) {
		super(json);
		maxLimit = json.optInt("max", 256);
		minLimit = json.optInt("min", NovaCore.getInstance().isNoNMSMode() ? VersionIndependentUtils.get().getMinY() : 0);
		sendMessage = json.optBoolean("send_message", true);
		message = json.optString("message", ChatColor.RED + "Reached build limit.");
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (e.getBlock().getLocation().getBlockY() > maxLimit || e.getBlock().getLocation().getBlockY() < minLimit) {
			e.setCancelled(true);
			if (sendMessage) {
				e.getPlayer().sendMessage(message);
			}

		}
	}
}