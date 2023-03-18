package net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.startmessage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.JSONObject;

import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModule;

public class StartMessage extends MapModule {
	private String message;

	public StartMessage(JSONObject json) {
		super(json);

		this.message =  json.optString("message", ChatColor.DARK_RED + "error: missing json value: message");
	}

	@Override
	public void onGameStart(Game game) {
		Bukkit.getServer().broadcastMessage(message);
	}

	@Override
	public String getSummaryString() {
		return ChatColor.GOLD + "Start message: " + ChatColor.RESET + message;
	}
}