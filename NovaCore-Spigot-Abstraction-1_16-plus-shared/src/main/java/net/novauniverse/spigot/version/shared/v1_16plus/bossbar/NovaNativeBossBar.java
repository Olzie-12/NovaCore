package net.novauniverse.spigot.version.shared.v1_16plus.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import net.zeeraa.novacore.spigot.abstraction.bossbar.NovaBossBar;

public class NovaNativeBossBar extends NovaBossBar {
	protected BossBar nativeBossBar;

	public NovaNativeBossBar(String text) {
		super(text);
		nativeBossBar = Bukkit.createBossBar(text, BarColor.PINK, BarStyle.SOLID);
	}

	@Override
	public void update() {
		nativeBossBar.setColor(BarColor.valueOf(color.name()));
		nativeBossBar.setStyle(BarStyle.valueOf(style.name()));
		nativeBossBar.setProgress(progress);
		nativeBossBar.setTitle(text);
	}

	@Override
	protected void onPlayerAdded(Player player) {
		nativeBossBar.addPlayer(player);
	}

	@Override
	protected void onPlayerRemoved(Player player) {
		nativeBossBar.removePlayer(player);
	}

	public BossBar getNativeBossBar() {
		return nativeBossBar;
	}
}