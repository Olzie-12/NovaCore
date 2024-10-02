package net.zeeraa.novacore.spigot.abstraction.bossbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.entity.Player;

public abstract class NovaBossBar {
	protected List<Player> players;
	protected float progress;
	protected NovaBarStyle style;
	protected NovaBarColor color;
	protected String text;

	public NovaBossBar(String text) {
		players = new ArrayList<>();
		progress = 1;
		color = NovaBarColor.PINK;
		style = NovaBarStyle.SOLID;
		this.text = text;
	}

	public void setColor(NovaBarColor color) {
		this.color = color;
		update();
	}

	public NovaBarColor getColor() {
		return color;
	}

	public void setStyle(NovaBarStyle style) {
		this.style = style;
		update();
	}

	public NovaBarStyle getStyle() {
		return style;
	}

	public void setProgress(float progress) {
		if (progress < 0 && progress > 1) {
			throw new IllegalArgumentException("progress has to be a value between 0 and 1");
		}
		this.progress = progress;
		update();
	}

	public float getProgress() {
		return progress;
	}

	public boolean addPlayer(Player player) {
		if (!players.contains(player)) {
			players.add(player);
			onPlayerAdded(player);
			return true;
		}
		return false;
	}

	public int addPlayers(Collection<Player> players) {
		AtomicInteger count = new AtomicInteger(0);
		players.forEach(player -> {
			if (addPlayer(player)) {
				count.incrementAndGet();
			}
		});
		return count.get();
	}

	public boolean removePlayer(Player player) {
		if (players.contains(player)) {
			players.remove(player);
			onPlayerRemoved(player);
		}
		return false;
	}

	public int removePlayers() {
		int count = 0;
		while (players.size() > 0) {
			Player player = players.remove(0);
			onPlayerRemoved(player);
			count++;
		}
		return count;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		update();
	}

	public abstract void update();

	protected abstract void onPlayerAdded(Player player);

	protected abstract void onPlayerRemoved(Player player);

	public enum NovaBarStyle {
		SOLID, SEGMENTED_6, SEGMENTED_10, SEGMENTED_12, SEGMENTED_20;
	}

	public enum NovaBarColor {
		PINK, BLUE, RED, GREEN, YELLOW, PURPLE, WHITE;
	}
}