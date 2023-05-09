package net.zeeraa.novacore.spigot.gameengine.module.modules.game.events;

import org.bukkit.event.HandlerList;

import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;

/**
 * Called before {@link Game#startGame()} is called
 * 
 * @author Zeeraa
 */
public class GameStartEvent extends GameEvent {
	private static final HandlerList HANDLERS_LIST = new HandlerList();

	public GameStartEvent(Game game) {
		super(game);
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}
}