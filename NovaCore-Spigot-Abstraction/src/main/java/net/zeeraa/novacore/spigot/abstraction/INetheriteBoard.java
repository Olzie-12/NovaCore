package net.zeeraa.novacore.spigot.abstraction;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import net.zeeraa.novacore.spigot.abstraction.netheriteboard.BPlayerBoard;

public interface INetheriteBoard {
	void deleteBoard(Player player);
	
	void removeBoard(Player player);
	
	boolean hasBoard(Player player);
	
	BPlayerBoard getBoard(Player player);
	
	Map<Player, BPlayerBoard> getBoards();

	BPlayerBoard createBoard(Player player, String name);
	
	BPlayerBoard createBoard(Player player, Scoreboard scoreboard, String name);
}