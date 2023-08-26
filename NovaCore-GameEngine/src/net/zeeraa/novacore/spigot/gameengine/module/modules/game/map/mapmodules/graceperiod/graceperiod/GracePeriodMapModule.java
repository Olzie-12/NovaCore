package net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.graceperiod.graceperiod;

import java.util.ArrayList;
import java.util.List;

import net.zeeraa.novacore.spigot.language.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.json.JSONArray;
import org.json.JSONObject;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.abstraction.enums.VersionIndependentSound;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.events.GameBeginEvent;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodule.MapModule;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.graceperiod.graceperiod.event.GracePeriodBeginEvent;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.graceperiod.graceperiod.event.GracePeriodFinishEvent;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.graceperiod.graceperiod.event.GracePeriodTimerEvent;
import net.zeeraa.novacore.spigot.teams.TeamManager;
import net.zeeraa.novacore.spigot.timers.BasicTimer;

public class GracePeriodMapModule extends MapModule implements Listener {
	private BasicTimer endTimer;
	private boolean isActive;
	private int seconds;
	private int initialTime;

	private List<Long> warnings;

	public static final List<DamageCause> BLOCKED_CAUSES = new ArrayList<>();

	static {
		BLOCKED_CAUSES.add(DamageCause.BLOCK_EXPLOSION);
		BLOCKED_CAUSES.add(DamageCause.CONTACT);
		BLOCKED_CAUSES.add(DamageCause.DROWNING);
		BLOCKED_CAUSES.add(DamageCause.ENTITY_ATTACK);
		BLOCKED_CAUSES.add(DamageCause.ENTITY_EXPLOSION);
		BLOCKED_CAUSES.add(DamageCause.FALLING_BLOCK);
		BLOCKED_CAUSES.add(DamageCause.FIRE);
		BLOCKED_CAUSES.add(DamageCause.LAVA);
		BLOCKED_CAUSES.add(DamageCause.LIGHTNING);
		BLOCKED_CAUSES.add(DamageCause.MAGIC);
		BLOCKED_CAUSES.add(DamageCause.POISON);
		BLOCKED_CAUSES.add(DamageCause.PROJECTILE);
		BLOCKED_CAUSES.add(DamageCause.SUFFOCATION);
		BLOCKED_CAUSES.add(DamageCause.THORNS);
		BLOCKED_CAUSES.add(DamageCause.WITHER);
	}

	public GracePeriodMapModule(JSONObject json) {
		super(json);

		seconds = 15;
		initialTime = 15;
		warnings = new ArrayList<>();

		if (json.has("time")) {
			seconds = json.getInt("time");
			initialTime = json.getInt("time");
		} else {
			// TODO: language file
			Log.warn("GracePeriodMapModule", "No time defined. Using the default of 15 seconds");
		}

		boolean legacyMode = json.optBoolean("legacy_mode", false);

		if (!legacyMode) {
			for (DamageCause cause : DamageCause.values()) {
				if (!BLOCKED_CAUSES.contains(cause)) {
					BLOCKED_CAUSES.add(cause);
				}
			}
		}

		if (json.has("warnings")) {
			JSONArray warningsJSON = json.getJSONArray("warnings");
			for (int i = 0; i < warningsJSON.length(); i++) {
				warnings.add(warningsJSON.getLong(i));
			}
		}

		isActive = false;

		endTimer = new BasicTimer(seconds, 20L);

		endTimer.addTickCallback(timeLeft -> {
			Bukkit.getServer().getPluginManager().callEvent(new GracePeriodTimerEvent(timeLeft));
			if (warnings.contains(timeLeft)) {
				Bukkit.getServer().getOnlinePlayers().forEach(x -> {
					VersionIndependentSound.NOTE_PLING.play(x);
					x.sendMessage(LanguageManager.getString(x, "novacore.game.modules.graceperiod.ending_in", timeLeft));
				});
			}
		});

		endTimer.addFinishCallback(() -> {
			isActive = false;
			Bukkit.getServer().getPluginManager().callEvent(new GracePeriodFinishEvent());
			LanguageManager.broadcast("novacore.game.modules.graceperiod.over");
			Bukkit.getServer().getOnlinePlayers().forEach(player -> VersionIndependentUtils.get().sendTitle(player, "", LanguageManager.getString(player, "novacore.game.modules.graceperiod.over_title"), 0, 60, 20));
		});
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			if (isActive) {
				if (e.getDamager() instanceof Player) {
					Player attacker = (Player) e.getDamager();
					if (TeamManager.hasTeamManager()) {
						if (!TeamManager.getTeamManager().isInSameTeam(e.getEntity().getUniqueId(), attacker.getUniqueId())) {
							attacker.sendMessage(LanguageManager.getString((Player) e.getEntity(), "novacore.game.modules.graceperiod.cant_attack"));
						}
					}
				}
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.ENTITY_ATTACK) {
			return;
		}

		if (e.getEntity() instanceof Player) {
			if (isActive) {
				if (BLOCKED_CAUSES.contains(e.getCause())) {
					e.setCancelled(true);
				}
			}
		}
	}

	public BasicTimer getEndTimer() {
		return endTimer;
	}

	public boolean isActive() {
		return isActive;
	}

	public int getInitialTime() {
		return initialTime;
	}

	public Long getTimeLeft() {
		return endTimer.getTimeLeft();
	}

	@EventHandler
	public void onGameBegin(GameBeginEvent e) {
		Log.info("GracePeriodMapModule", "Received GameBeginEvent");
		isActive = true;
		LanguageManager.broadcast("novacore.game.modules.graceperiod.ending_in", seconds);
		endTimer.start();
	}

	@Override
	public void onGameStart(Game game) {
		GracePeriodBeginEvent event = new GracePeriodBeginEvent(initialTime);
		seconds = event.getTime();
		initialTime = event.getTime();

		Bukkit.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(LanguageManager.getString(player, "novacore.game.modules.graceperiod.started", seconds)));
		Bukkit.getServer().getPluginManager().registerEvents(this, game.getPlugin());
	}

	@Override
	public void onGameEnd(Game game) {
		isActive = false;
		HandlerList.unregisterAll(this);
	}

	@Override
	public String getSummaryString() {
		String summary = "";
		summary += ChatColor.GOLD + "Initial time: " + ChatColor.AQUA + initialTime + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Is active: " + ChatColor.AQUA + isActive + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Timer active: " + ChatColor.AQUA + endTimer.isRunning() + ChatColor.GOLD + ". ";
		summary += ChatColor.GOLD + "Timer time left: " + ChatColor.AQUA + endTimer.getTimeLeft() + ChatColor.GOLD + ". ";
		return summary;
	}
}