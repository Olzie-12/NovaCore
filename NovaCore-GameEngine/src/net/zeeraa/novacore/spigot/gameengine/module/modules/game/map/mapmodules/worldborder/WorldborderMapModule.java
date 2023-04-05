package net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.worldborder;

import org.bukkit.Bukkit;
import org.json.JSONObject;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.abstraction.enums.VersionIndependentSound;
import net.zeeraa.novacore.spigot.gameengine.NovaCoreGameEngine;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.worldborder.event.NovaWorldborderEstimatedFinishedEvent;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.triggers.DelayedGameTrigger;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.triggers.TriggerFlag;
import net.zeeraa.novacore.spigot.language.LanguageManager;
import net.zeeraa.novacore.spigot.tasks.SimpleTask;

public class WorldborderMapModule extends MapModule {
	private double centerX;
	private double centerZ;

	private double startSize;
	private double endSize;

	private double damage;
	private double damageBuffer;

	private int shrinkDuration;
	private int startDelay;

	private DelayedGameTrigger startTrigger;

	private Game game;

	private int stepTime;
	private double stepShrinkValue;
	private int totalSteps;
	private int activeStep;
	private double lastSize;

	private int estimatedShrinkTimeLeft;

	private Task shrinkTask;
	private Task shrinkEstimatedTimeTask;

	private boolean isShrinking;

	private boolean shouldSendEvent;

	public WorldborderMapModule(JSONObject json) {
		super(json);

		this.centerX = 0.5;
		this.centerZ = 0.5;

		this.startSize = 501;
		this.endSize = 51;

		this.damage = json.optDouble("damage", 5);
		this.damageBuffer = json.optDouble("damage_buffer", 2);

		this.shrinkDuration = 900;
		this.startDelay = 600;

		this.shouldSendEvent = false;

		this.stepTime = json.optInt("step_time", 30);

		if (json.has("center_x")) {
			this.centerX = json.getDouble("center_x");
		} else {
			Log.warn("Missing center_x for wordborder map module");
		}

		if (json.has("center_z")) {
			this.centerZ = json.getDouble("center_z");
		} else {
			Log.warn("Missing center_z for wordborder map module");
		}

		if (json.has("start_size")) {
			this.startSize = json.getDouble("start_size");
		} else {
			Log.warn("Missing start_size for wordborder map module");
		}

		if (json.has("end_size")) {
			this.endSize = json.getDouble("end_size");
		} else {
			Log.warn("Missing end_size for wordborder map module");
		}

		if (json.has("shrink_duration")) {
			this.shrinkDuration = json.getInt("shrink_duration");
		} else {
			Log.warn("Missing shrink_duration for wordborder map module");
		}

		if (json.has("start_delay")) {
			this.startDelay = json.getInt("start_delay");
		} else {
			Log.warn("Missing start_delay for wordborder map module");
		}

		this.totalSteps = shrinkDuration / stepTime;

		this.stepShrinkValue = (startSize - endSize) / (double) totalSteps;

		this.lastSize = startSize;

		this.activeStep = 0;

		this.startTrigger = new DelayedGameTrigger("novacore.worldborder.start", startDelay * 20L, (trigger, reason) -> {
			startTrigger.stop();
			// Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD +
			// "The world border is starting to shrink");
			LanguageManager.broadcast("novacore.game.wordborder.start");

			Bukkit.getServer().getOnlinePlayers().forEach(player -> VersionIndependentUtils.get().playSound(player, player.getLocation(), VersionIndependentSound.NOTE_PLING, 1F, 1F));

			start();
		});
		this.startTrigger.setDescription("Starts the worldborder shrinking");
		this.startTrigger.addFlag(TriggerFlag.RUN_ONLY_ONCE);
		this.startTrigger.addFlag(TriggerFlag.STOP_ON_GAME_END);

		this.shrinkTask = new SimpleTask(NovaCoreGameEngine.getInstance(), () -> {
			if (activeStep >= totalSteps) {
				cancel();
				isShrinking = true; // Because last step is not finished yet (probably)
				return;
			}

			if (game == null) {
				return;
			}

			if (game.getWorld() == null) {
				return;
			}

			estimatedShrinkTimeLeft = (totalSteps - activeStep) * stepTime;

			game.getWorld().getWorldBorder().setSize(lastSize - stepShrinkValue, stepTime);

			lastSize -= stepShrinkValue;

			activeStep++;
		}, 0, stepTime * 20L);

		shrinkEstimatedTimeTask = new SimpleTask(NovaCoreGameEngine.getInstance(), () -> {
			if (estimatedShrinkTimeLeft > 0) {
				shouldSendEvent = true;
				estimatedShrinkTimeLeft--;
			} else {
				if (shouldSendEvent) {
					isShrinking = false;
					shouldSendEvent = false;
					Bukkit.getServer().getPluginManager().callEvent(new NovaWorldborderEstimatedFinishedEvent());
				}
			}
		}, 20L);
	}

	public int getEstimatedShrinkTimeLeft() {
		return estimatedShrinkTimeLeft;
	}

	public double getCenterX() {
		return centerX;
	}

	public double getCenterZ() {
		return centerZ;
	}

	public double getStartSize() {
		return startSize;
	}

	public double getEndSize() {
		return endSize;
	}

	public int getShrinkDuration() {
		return shrinkDuration;
	}

	public double getDamage() {
		return damage;
	}

	public double getDamageBuffer() {
		return damageBuffer;
	}

	public DelayedGameTrigger getStartTrigger() {
		return startTrigger;
	}

	public boolean isShrinking() {
		return isShrinking;
	}

	@Override
	public void onGameStart(Game game) {
		this.game = game;

		Task.tryStartTask(shrinkEstimatedTimeTask);

		game.addTrigger(startTrigger);

		if (game.hasWorld()) {
			game.getWorld().getWorldBorder().setCenter(centerX, centerZ);
			game.getWorld().getWorldBorder().setSize(startSize);
			game.getWorld().getWorldBorder().setDamageAmount(damage);
			game.getWorld().getWorldBorder().setDamageBuffer(damageBuffer);
			Log.info("The worldborder in world " + game.getWorld().getName() + " has been reset");
		} else {
			Log.fatal("Worldborder cant set initial size because the game does not have a world set");
		}
	}

	@Override
	public void onGameBegin(Game game) {
		startTrigger.start();
	}

	@Override
	public void onGameEnd(Game game) {
		Task.tryStopTask(shrinkEstimatedTimeTask);
		this.game = null;
	}

	public boolean cancel() {
		if (shrinkTask.isRunning()) {
			isShrinking = false;
			shrinkTask.stop();
			return true;
		}
		return false;
	}

	public boolean start() {
		if (shrinkTask.isRunning()) {
			return false;
		}
		shrinkTask.start();
		isShrinking = true;
		return true;
	}

	public boolean isRunning() {
		return shrinkTask.isRunning();
	}
}