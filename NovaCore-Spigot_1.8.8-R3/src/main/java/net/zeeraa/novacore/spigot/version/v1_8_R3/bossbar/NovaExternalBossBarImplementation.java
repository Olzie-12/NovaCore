package net.zeeraa.novacore.spigot.version.v1_8_R3.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.inventivetalent.bossbar.BossBarAPI;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.abstraction.bossbar.NovaBossBar;

public class NovaExternalBossBarImplementation extends NovaBossBar {
	public NovaExternalBossBarImplementation(String text) {
		super(text);
		if (Bukkit.getPluginManager().getPlugin("BossBarAPI") == null) {
			throw new IllegalStateException("To use nova boss bars on 1.8 you need to install BossBarAPI");
		}
	}

	@Override
	public void update() {
		players.forEach(this::update);
	}

	@SuppressWarnings("deprecation")
	public void update(Player player) {
		Log.debug("Send boss bar to " + player.getName() + " progress: " + (progress * 100F) + " text: " + text);
		BossBarAPI.setMessage(player, text, progress * 100F);
	}

	@Override
	protected void onPlayerAdded(Player player) {
		update(player);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPlayerRemoved(Player player) {
		BossBarAPI.removeBar(player);
	}
}