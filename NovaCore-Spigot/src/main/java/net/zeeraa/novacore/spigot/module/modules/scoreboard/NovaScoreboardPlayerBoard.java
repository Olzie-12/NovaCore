package net.zeeraa.novacore.spigot.module.modules.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatColor;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import net.megavex.scoreboardlibrary.api.team.ScoreboardTeam;
import net.megavex.scoreboardlibrary.api.team.TeamDisplay;
import net.megavex.scoreboardlibrary.api.team.TeamManager;
import net.zeeraa.novacore.spigot.module.modules.scoreboard.text.ScoreboardEntry;
import net.zeeraa.novacore.spigot.module.modules.scoreboard.title.ScoreboardTitle;

public class NovaScoreboardPlayerBoard {
	private NovaScoreboardManager novaScoreboardManager;

	private Player owner;

	private Sidebar sidebar;
	private TeamManager teamManager;
	private ComponentSidebarLayout layout;
	private ScoreboardTitle title;

	private int lineCount;
	private int oldLineCount;

	private Map<Player, ChatColor> playerNameColors;

	private Map<ChatColor, ScoreboardTeam> colorTeams;

	private Map<Integer, ScoreboardEntry> playerLines;

	public NovaScoreboardPlayerBoard(NovaScoreboardManager novaScoreboardManager, Player player) {
		this.novaScoreboardManager = novaScoreboardManager;
		this.owner = player;

		playerNameColors = new HashMap<>();
		colorTeams = new HashMap<>();
		playerLines = new HashMap<>();

		lineCount = novaScoreboardManager.getDefaultLineCount();

		sidebar = novaScoreboardManager.getScoreboardLibrary().createSidebar();
		teamManager = novaScoreboardManager.getScoreboardLibrary().createTeamManager();

		sidebar.addPlayer(player);
		teamManager.addPlayer(player);

		title = null;

		for (ChatColor color : NovaScoreboardManager.VALID_COLORS) {
			String name = "NC_" + color.name();
			ScoreboardTeam team = teamManager.createIfAbsent(name);
			NamedTextColor namedColor = NovaScoreboardManager.Utils.ChatColorToNamedTextColor(color);
			team.defaultDisplay().displayName(Component.text(name));
			team.defaultDisplay().playerColor(namedColor);
			colorTeams.put(color, team);
		}

		updateLayout();
		updateTeams();
	}

	public void tick() {
		if (title != null) {
			title.tick();
		}
		layout.apply(sidebar);
	}

	public Player getOwner() {
		return owner;
	}

	public int getLineCount() {
		return lineCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
		if (oldLineCount != lineCount) {
			oldLineCount = lineCount;
			updateLayout();
		}
	}

	public void setPlayerLine(int line, ScoreboardEntry content) {
		playerLines.put(line, content);
		forceContentUpdate();
	}

	public void setPlayerLines(Map<Integer, ScoreboardEntry> lines) {
		lines.forEach((key, val) -> playerLines.put(key, val));
		forceContentUpdate();
	}

	public void clearPlayerLine(int line) {
		playerLines.remove(line);
		forceContentUpdate();
	}

	public void clearAllPlayerlLines() {
		playerLines.clear();
		forceContentUpdate();
	}

	private void updateLayout() {
		SidebarComponent.Builder builder = SidebarComponent.builder();

		Map<Integer, ScoreboardEntry> content = new HashMap<>();
		novaScoreboardManager.getGlobalLines().forEach((k, v) -> content.put(k, v));

		playerLines.forEach((k, v) -> {
			if (v == null) {
				content.remove(k);
			} else {
				content.put(k, v);
			}
		});

		for (int i = 0; i < lineCount; i++) {
			if (content.containsKey(i)) {
				content.get(i).apply(builder);
			} else {
				builder.addBlankLine();
			}
		}

		layout = new ComponentSidebarLayout((title == null ? novaScoreboardManager.getDefaultTitle() : title).getComponent(), builder.build());
	}

	private void updateTeams() {
		Map<Player, ChatColor> colors = new HashMap<>();

		for (Entry<Player, ChatColor> entry : novaScoreboardManager.getGlobalPlayerNameColors().entrySet()) {
			colors.put(entry.getKey(), entry.getValue());
		}

		for (Entry<Player, ChatColor> entry : playerNameColors.entrySet()) {
			if (entry.getValue() == null) {
				colors.remove(entry.getKey());
			} else {
				colors.put(entry.getKey(), entry.getValue());
			}
		}

		colorTeams.forEach((color, team) -> {
			TeamDisplay display = team.defaultDisplay();
			List<String> oldEntries = new ArrayList<>(display.entries());
			oldEntries.forEach(display::removeEntry);
			colors.forEach((player, playerColor) -> {
				if (playerColor == color) {
					display.addEntry(player.getName());
				}
			});
			team.display(owner, display);
		});
	}

	public void forceTeamColorUpdate() {
		updateTeams();
	}

	public void forceContentUpdate() {
		updateLayout();
	}

	public Map<Player, ChatColor> getPlayerNameColors() {
		return playerNameColors;
	}

	public void setPlayerNameColor(Player player, @Nullable ChatColor color) {
		playerNameColors.put(player, color);
		updateTeams();
	}

	public void removePlayerNameColor(Player player) {
		if (playerNameColors.containsKey(player)) {
			playerNameColors.remove(player);
			updateTeams();
		}
	}

	public ScoreboardTitle getTitle() {
		return title;
	}

	public void setTitle(ScoreboardTitle title) {
		this.title = title;
		updateLayout();
	}

	public void dispose() {
		sidebar.close();
		teamManager.close();
		playerNameColors.clear();
		colorTeams.clear();
		playerLines.clear();
	}
}
