package net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.lootdrop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.json.JSONArray;
import org.json.JSONObject;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.commons.utils.RandomGenerator;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.GameMap;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.triggers.DelayedGameTrigger;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.triggers.TriggerFlag;
import net.zeeraa.novacore.spigot.module.modules.lootdrop.LootDropManager;
import net.zeeraa.novacore.spigot.utils.LocationData;

public class LootDropMapModule extends MapModule {
	private String lootTable;

	private int minDropTime;
	private int maxDropTime;

	private DelayedGameTrigger trigger;

	private List<LocationData> locations;

	private List<Location> locationsReal;

	private int timeToSpawn;

	public LootDropMapModule(JSONObject json) {
		super(json);

		this.lootTable = null;

		this.minDropTime = 200;
		this.maxDropTime = 900;

		this.locations = new ArrayList<>();

		this.locationsReal = null;

		JSONArray locationJson = json.getJSONArray("locations");

		for (int i = 0; i < locationJson.length(); i++) {
			locations.add(new LocationData(locationJson.getJSONObject(i)));
		}

		this.lootTable = json.getString("loot_table");

		this.minDropTime = json.getInt("min_drop_time");

		if (json.has("max_drop_time")) {
			this.maxDropTime = json.getInt("max_drop_time");
		} else {
			Log.warn("Missing max_drop_time for loot drop");
		}

		if (minDropTime != -1 && maxDropTime == -1) {
			maxDropTime = minDropTime;
		}

		this.timeToSpawn = 0;
		if (json.has("time_to_descend")) {
			this.timeToSpawn = json.getInt("time_to_descend");
		}

		trigger = new DelayedGameTrigger("novacore.lootdrop", minDropTime, (trigger2, reason) -> onTrigger(reason));
		trigger.addFlag(TriggerFlag.STOP_ON_GAME_END);
		trigger.setDescription("Spawns a loot drop");
	}

	private void onTrigger(TriggerFlag reason) {
		spawnDrop();
		startTriggerTimer();
	}

	private void startTriggerTimer() {
		int delay = RandomGenerator.generate(minDropTime, maxDropTime);

		Log.debug("Next loot drop in " + delay + " seconds");

		if (trigger.isRunning()) {
			trigger.stop();
		}

		trigger.setDelay(delay * 20L);

		trigger.start();
	}

	@Override
	public void onMapLoad(GameMap map) {
		if (lootTable != null && minDropTime > 0) {
			if (!LootDropManager.getInstance().isEnabled()) {
				LootDropManager.getInstance().enable();
			}

			if (timeToSpawn > 0) {
				LootDropManager.getInstance().setDefaultSpawnTimeTicks(timeToSpawn);
			}

			locationsReal = LocationData.toLocations(locations, map.getWorld());
		} else {
			Log.error("Cant enable loot drop because loot table or min drop time is not set");
		}
	}

	@Override
	public void onGameStart(Game game) {
		game.addTrigger(trigger);
		if (lootTable != null && minDropTime > 0) {
			startTriggerTimer();
		}
	}

	@Override
	public void onGameEnd(Game game) {
		if (trigger != null) {
			trigger.stop();
		}
		game.removeTrigger(trigger);
	}

	public DelayedGameTrigger getTrigger() {
		return trigger;
	}

	public boolean spawnDrop() {
		if (lootTable != null && minDropTime > 0) {
			if (locationsReal.size() > 0) {
				for (int i = 0; i < 40; i++) {
					Location location = locationsReal.get(new Random().nextInt(locationsReal.size()));

					if (LootDropManager.getInstance().canSpawnAt(location)) {
						Log.debug("Loot drop spawned after " + i + " tries");
						LootDropManager.getInstance().spawnDrop(location, lootTable);
						return true;
					}
				}
			} else {
				Log.warn("No loot drop locations specified");
			}
		}

		return false;
	}

	@Override
	public String getSummaryString() {
		String summary = "";

		summary += ChatColor.GOLD + "Loot table: " + ChatColor.AQUA + lootTable + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Min drop time: " + ChatColor.AQUA + minDropTime + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Max drop time: " + ChatColor.AQUA + maxDropTime + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Trigger running: " + ChatColor.AQUA + trigger.isRunning() + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Trigger ticks left: " + ChatColor.AQUA + trigger.getTicksLeft() + ChatColor.GOLD + "(" + ChatColor.AQUA + ((int) (trigger.getTicksLeft() / 20)) + "s" + ChatColor.GOLD + ") " + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Trigger count: " + ChatColor.AQUA + trigger.getTriggerCount() + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Location count: " + ChatColor.AQUA + locations.size() + ChatColor.GOLD + ". ";
		String locationString = "";
		for (LocationData l : locations) {
			locationString += ChatColor.GOLD + "X: " + ChatColor.AQUA + l.getX() + ChatColor.GOLD + " Y: " + ChatColor.AQUA + l.getX() + ChatColor.GOLD + " Z: " + ChatColor.AQUA + l.getX() + ChatColor.GOLD + ". ";
		}
		summary += ChatColor.GOLD + "Locations: " + ChatColor.AQUA + locationString + ChatColor.GOLD + ". ";

		return summary;
	}
}