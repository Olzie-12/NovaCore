package net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.infiniteoxygen;

import org.bukkit.Bukkit;
import org.json.JSONObject;

import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModule;
import net.zeeraa.novacore.spigot.tasks.SimpleTask;

public class InfiniteOxygenMapModule extends MapModule {
	private Task task;

	public InfiniteOxygenMapModule(JSONObject json) {
		super(json);
		task = new SimpleTask(() -> Bukkit.getServer().getOnlinePlayers().forEach(p -> p.setRemainingAir(p.getMaximumAir())), 1L);
	}

	@Override
	public void onGameStart(Game game) {
		Task.tryStartTask(task);
	}

	@Override
	public void onGameEnd(Game game) {
		Task.tryStopTask(task);
	}
}