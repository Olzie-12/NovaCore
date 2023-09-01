package net.zeeraa.novacore.spigot.gameengine.module.modules.gamelobby.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.zeeraa.novacore.spigot.gameengine.module.modules.gamelobby.GameLobby;
import net.zeeraa.novacore.spigot.gameengine.module.modules.gamelobby.map.GameLobbyMap;

/**
 * This event is called when a map is activated by
 * {@link GameLobby#setActiveMap(GameLobbyMap)}
 * 
 * @author Zeeraa
 */
public class GameLobbyMapActivatedEvent extends Event {
	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private final GameLobbyMap map;

	public GameLobbyMapActivatedEvent(GameLobbyMap map) {
		this.map = map;
	}

	public GameLobbyMap getMap() {
		return map;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}
}