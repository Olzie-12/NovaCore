package net.zeeraa.novacore.spigot.module.modules.cooldown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.spigot.module.NovaModule;
import net.zeeraa.novacore.spigot.tasks.SimpleTask;
import net.zeeraa.novacore.spigot.utils.PlayerUtils;

public class CooldownManager extends NovaModule {
	private static CooldownManager instance;

	private Task task;
	private Task cleanupTask;
	private Map<UUID, List<Cooldown>> cooldowns;

	public CooldownManager() {
		super("NovaCore.CooldownManager");
	}

	public static CooldownManager get() {
		return instance;
	}

	public boolean isActive(Player player, String name) {
		return this.isActive(player.getUniqueId(), name);
	}

	public boolean isActive(UUID uuid, String name) {
		if (!cooldowns.containsKey(uuid)) {
			return false;
		}

		return cooldowns.get(uuid).stream().filter(c -> c.getName().equals(name)).findFirst().isPresent();
	}

	public int getTimeLeft(Player player, String name) {
		return this.getTimeLeft(player.getUniqueId(), name);
	}

	public int getTimeLeft(UUID uuid, String name) {
		if (cooldowns.containsKey(uuid)) {
			Cooldown cooldown = cooldowns.get(uuid).stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
			if (cooldown != null) {
				return cooldown.getTicksLeft();
			}
		}
		return 0;
	}

	public void set(Player player, String name, int ticks) {
		this.set(player.getUniqueId(), name, ticks);
	}

	public void set(UUID uuid, String name, int ticks) {
		this.setupPlayer(uuid);
		Cooldown cooldown = cooldowns.get(uuid).stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
		if (cooldown != null) {
			cooldown.setTicksLeft(ticks);
		} else {
			cooldowns.get(uuid).add(new Cooldown(name, ticks));
		}
	}

	public void setupPlayer(Player player) {
		this.setupPlayer(player.getUniqueId());
	}

	public void setupPlayer(UUID uuid) {
		if (!cooldowns.containsKey(uuid)) {
			cooldowns.put(uuid, new ArrayList<>());
		}
	}

	@Override
	public void onLoad() {
		CooldownManager.instance = this;

		cooldowns = new HashMap<>();
		task = new SimpleTask(getPlugin(), () -> {
			cooldowns.values().forEach(c -> c.forEach(Cooldown::tick));
			cooldowns.values().forEach(c -> c.removeIf(Cooldown::isOver));
		}, 0L);

		cleanupTask = new SimpleTask(getPlugin(), () -> {
			List<UUID> empty = new ArrayList<>();
			cooldowns.forEach((key, val) -> {
				if (val.size() == 0) {
					if (!PlayerUtils.isOnline(key)) {
						empty.add(key);
					}
				}
			});
			empty.forEach(cooldowns::remove);
		}, 20L * 10);
	}

	@Override
	public void onEnable() throws Exception {
		Task.tryStartTask(task);
		Task.tryStartTask(cleanupTask);
	}

	@Override
	public void onDisable() throws Exception {
		Task.tryStopTask(task);
		Task.tryStopTask(cleanupTask);
		cooldowns.clear();
	}

	public static final String ticksToSecondsString(int ticks) {
		int seconds = (int) Math.ceil(((double) ticks) / 20D);
		return seconds + " second" + (seconds == 1 ? "" : "s");
	}
}