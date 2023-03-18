package net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.instantvoidkill;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.json.JSONObject;

import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModule;
import net.zeeraa.novacore.spigot.tasks.SimpleTask;

public class InstantVoidKill extends MapModule {
	private Task taks;

	private int y;

	public InstantVoidKill(JSONObject json) {
		super(json);

		y = json.optInt("y", NovaCore.getInstance().isNoNMSMode() ? VersionIndependentUtils.get().getMinY() : 0);

		this.taks = new SimpleTask(() -> Bukkit.getServer().getOnlinePlayers().forEach(player -> {
			if (player.getLocation().getBlockY() < y) {
				if (!player.isDead()) {
					if (player.getHealth() > 0) {
						if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
							player.setHealth(0);
						}
					}
				}
			}
		}), 2L);
	}

	public int getY() {
		return y;
	}

	@Override
	public void onGameBegin(Game game) {
		Task.tryStartTask(taks);
	}

	@Override
	public void onGameEnd(Game game) {
		Task.tryStopTask(taks);
	}

	@Override
	public String getSummaryString() {
		return ChatColor.GOLD + "Y Level: " + ChatColor.AQUA + y;
	}
}