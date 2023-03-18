package net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.chestloot;

import org.bukkit.ChatColor;
import org.json.JSONObject;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.commons.utils.RandomGenerator;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.GameMap;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.triggers.DelayedGameTrigger;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.triggers.GameTrigger;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.triggers.TriggerCallback;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.triggers.TriggerFlag;
import net.zeeraa.novacore.spigot.module.ModuleManager;
import net.zeeraa.novacore.spigot.module.modules.chestloot.ChestLootManager;

public class ChestLoot extends MapModule {
	private String chestLootTable;
	private String enderChestLootTable;

	private int minRefillTime;
	private int maxRefillTime;

	private boolean announceRefills;

	private DelayedGameTrigger trigger;

	public ChestLoot(JSONObject json) {
		super(json);

		this.chestLootTable = json.optString("chest_loot");
		this.enderChestLootTable = json.optString("ender_chest_loot");
		this.minRefillTime =  json.optInt("min_refill_time", -1);
		this.maxRefillTime =  json.optInt("min_refill_time", -1);
		this.announceRefills =json.optBoolean("announce_refills", true);
		this.trigger = null;

		if (minRefillTime != -1 && maxRefillTime == -1) {
			maxRefillTime = minRefillTime;
		} else if (maxRefillTime != -1 && minRefillTime == -1) {
			minRefillTime = maxRefillTime;
		}

		this.trigger = new DelayedGameTrigger("novacore.chest.refill", minRefillTime * 20, new TriggerCallback() {
			@Override
			public void run(GameTrigger trigger, TriggerFlag reason) {
				ChestLootManager.getInstance().refillChests(announceRefills);
				startTask();
			}
		});
		trigger.addFlag(TriggerFlag.STOP_ON_GAME_END);
		trigger.setDescription("Refills chests");
	}

	public String getEnderChestLootTable() {
		return enderChestLootTable;
	}

	public boolean hasEnderChestLootTable() {
		return enderChestLootTable != null;
	}

	public String getChestLootTable() {
		return chestLootTable;
	}

	public boolean hasChestLootTable() {
		return chestLootTable != null;
	}

	public int getMinRefillTime() {
		return minRefillTime;
	}

	public int getMaxRefillTime() {
		return maxRefillTime;
	}

	public boolean isRefillsEnabled() {
		return minRefillTime > 0;
	}

	public boolean isAnnounceRefills() {
		return announceRefills;
	}

	@Override
	public void onMapLoad(GameMap map) {
		if (hasChestLootTable() || hasEnderChestLootTable()) {
			if (ModuleManager.isDisabled(ChestLootManager.class)) {
				Log.info("Loading ChestLootManager because the game map has a chest or ender chest loot table");
				ModuleManager.enable(ChestLootManager.class);
			}

			ChestLootManager.getInstance().setChestLootTable(getChestLootTable());
			ChestLootManager.getInstance().setEnderChestLootTable(getEnderChestLootTable());
		}
	}

	@Override
	public void onGameStart(Game game) {
		if (isRefillsEnabled()) {
			game.addTrigger(trigger);
		}
	}

	@Override
	public void onGameBegin(Game game) {
		if (isRefillsEnabled()) {
			startTask();
		}
	}

	public DelayedGameTrigger getTrigger() {
		return trigger;
	}

	private void startTask() {
		int delay = RandomGenerator.generate(minRefillTime, maxRefillTime);

		Log.debug("Next chest refill in " + delay + " seconds");

		trigger.stop();
		trigger.setDelay(delay * 20L);
		trigger.start();
	}

	@Override
	public String getSummaryString() {
		String summary = "";
		summary += ChatColor.GOLD + "Chest loot: " + ChatColor.AQUA + chestLootTable + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Ender chest loot: " + ChatColor.AQUA + enderChestLootTable + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Min refill time: " + ChatColor.AQUA + minRefillTime + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Max refill time: " + ChatColor.AQUA + maxRefillTime + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Announce refills: " + ChatColor.AQUA + announceRefills + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Task running: " + ChatColor.AQUA + trigger.isRunning() + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Ticks left: " + ChatColor.AQUA + trigger.getTicksLeft() + "(" + (int) (trigger.getTicksLeft() / 20) + "s)" + ChatColor.GOLD + ". ";
		return summary;
	}
}