package net.zeeraa.novacore.spigot.gameengine.module.modules.game.events;

import org.bukkit.event.HandlerList;

import net.zeeraa.novacore.spigot.gameengine.module.modules.game.Game;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.GameManager;
import net.zeeraa.novacore.spigot.gameengine.module.modules.game.MapGame;

/**
 * Called when {@link GameManager#start()} is called before the
 * {@link Game#startGame()} is called. If the game is a {@link MapGame} the map
 * will be selected and loaded before this event is called
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