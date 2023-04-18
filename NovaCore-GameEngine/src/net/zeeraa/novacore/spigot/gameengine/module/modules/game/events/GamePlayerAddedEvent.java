package net.zeeraa.novacore.spigot.gameengine.module.modules.game.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;

/**
 * Called when a player is added to the game
 * 
 * @author Zeeraa
 */
public class GamePlayerAddedEvent extends GameEvent {
	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private Player player;

	public GamePlayerAddedEvent(Game game, Player player) {
		super(game);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}
}