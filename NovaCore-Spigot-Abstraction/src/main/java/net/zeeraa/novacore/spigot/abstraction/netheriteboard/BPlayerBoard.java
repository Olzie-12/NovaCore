package net.zeeraa.novacore.spigot.abstraction.netheriteboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.abstraction.INetheriteBoard;
import net.zeeraa.novacore.spigot.abstraction.enums.ObjectiveMode;

public abstract class BPlayerBoard implements PlayerBoard<String, Integer, String> {
	protected Player player;
	protected Scoreboard scoreboard;

	protected String name;

	protected Objective objective;
	protected Objective buffer;

	protected Map<Integer, String> lines = new HashMap<>();

	protected boolean deleted = false;

	private INetheriteBoard netheriteBoard;

	public BPlayerBoard(INetheriteBoard netheriteBoard, Player player, String name) throws Exception {
		this(netheriteBoard, player, null, name);
	}

	public BPlayerBoard(INetheriteBoard netheriteBoard, Player player, Scoreboard scoreboard, String name) throws Exception {
		this.netheriteBoard = netheriteBoard;
		this.player = player;
		this.scoreboard = scoreboard;

		if (this.scoreboard == null) {
			Scoreboard sb = player.getScoreboard();

			if (sb == null || sb == Bukkit.getScoreboardManager().getMainScoreboard())
				sb = Bukkit.getScoreboardManager().getNewScoreboard();

			this.scoreboard = sb;
		}

		this.name = name;
	}
	
	protected void init() throws Exception {
		String subName = player.getName().length() <= 14
				? player.getName()
				: player.getName().substring(0, 14);

		this.objective = this.scoreboard.getObjective("sb" + subName);
		this.buffer = this.scoreboard.getObjective("bf" + subName);

		if (this.objective == null)
			this.objective = this.scoreboard.registerNewObjective("sb" + subName, "dummy");
		if (this.buffer == null)
			this.buffer = this.scoreboard.registerNewObjective("bf" + subName, "dummy");

		this.objective.setDisplayName(name);
		sendObjective(this.objective, ObjectiveMode.CREATE);
		sendObjectiveDisplay(this.objective);

		this.buffer.setDisplayName(name);
		sendObjective(this.buffer, ObjectiveMode.CREATE);

		this.player.setScoreboard(this.scoreboard);
	}

	@Override
	public String get(Integer score) {
		if (this.deleted)
			throw new IllegalStateException("The PlayerBoard is deleted!");

		return this.lines.get(score);
	}

	@Override
	public void set(String name, Integer score) {
		if (this.deleted)
			throw new IllegalStateException("The PlayerBoard is deleted!");

		String oldName = this.lines.get(score);

		if (name.equals(oldName))
			return;

		this.lines.entrySet()
				.removeIf(entry -> entry.getValue().equals(name));

		try {
			if (oldName != null) {
				sendScore(this.buffer, oldName, score, true);
				sendScore(this.buffer, name, score, false);

				swapBuffers();

				sendScore(this.buffer, oldName, score, true);
				sendScore(this.buffer, name, score, false);
			} else {
				sendScore(this.objective, name, score, false);
				sendScore(this.buffer, name, score, false);
			}

			this.lines.put(score, name);
		} catch (Exception e) {
			Log.error("BPlayerBoard", "set() failed. " + e.getClass().getName());
			e.printStackTrace();
		}
	}

	@Override
	public void setAll(String... lines) {
		if (this.deleted)
			throw new IllegalStateException("The PlayerBoard is deleted!");

		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];

			set(line, lines.length - i);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		Set<Integer> scores = new HashSet(this.lines.keySet());
		for (int score : scores) {
			if (score <= 0 || score > lines.length) {
				remove(score);
			}
		}
	}

	@Override
	public void clear() {
		new HashSet<>(this.lines.keySet()).forEach(this::remove);
		this.lines.clear();
	}

	private void swapBuffers() throws Exception {
		sendObjectiveDisplay(this.buffer);

		Objective temp = this.buffer;

		this.buffer = this.objective;
		this.objective = temp;
	}

	protected abstract void sendObjective(Objective objective, ObjectiveMode mode) throws Exception;

	protected abstract void sendObjectiveDisplay(Objective objective) throws Exception;

	protected abstract void sendScore(Objective objective, String name, int score, boolean remove) throws Exception;

	@Override
	public void remove(Integer score) {
		if (this.deleted)
			throw new IllegalStateException("The PlayerBoard is deleted!");

		String name = this.lines.get(score);

		if (name == null)
			return;

		this.scoreboard.resetScores(name);
		this.lines.remove(score);
	}

	@Override
	public void delete() {
		if (this.deleted)
			return;

		netheriteBoard.removeBoard(player);

		try {
			sendObjective(this.objective, ObjectiveMode.REMOVE);
			sendObjective(this.buffer, ObjectiveMode.REMOVE);
		} catch (Exception e) {
			Log.warn("BPlayerBoard", "Failed to send packets in delete(). " + e.getClass().getName() + " " + e.getMessage());
			e.printStackTrace();
		}

		this.objective.unregister();
		this.objective = null;

		this.buffer.unregister();
		this.buffer = null;

		this.lines = null;

		this.deleted = true;

	}

	@Override
	public Map<Integer, String> getLines() {
		if (this.deleted)
			throw new IllegalStateException("The PlayerBoard is deleted!");

		return new HashMap<>(lines);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		if (this.deleted)
			throw new IllegalStateException("The PlayerBoard is deleted!");

		this.name = name;

		this.objective.setDisplayName(name);
		this.buffer.setDisplayName(name);

		try {
			sendObjective(this.objective, ObjectiveMode.UPDATE);
			sendObjective(this.buffer, ObjectiveMode.UPDATE);
		} catch (Exception e) {
			Log.error("BPlayerBoard", "setName() failed. " + e.getClass().getName());
			e.printStackTrace();
		}
	}

	public Player getPlayer() {
		return player;
	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}
}