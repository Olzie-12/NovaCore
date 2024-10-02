package net.zeeraa.novacore.spigot.gameengine.module.modules.gamelobby.events;

import java.util.List;
import java.util.UUID;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is called before the game is started by game lobby
 * 
 * @author Zeeraa
 */
public class GameLobbyStartingEvent extends Event implements Cancellable {
	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private List<UUID> waitingPlayers;
	private boolean cancelled;

	public GameLobbyStartingEvent(List<UUID> waitingPlayers) {
		this.waitingPlayers = waitingPlayers;
		this.cancelled = false;
	}

	public List<UUID> getWaitingPlayers() {
		return waitingPlayers;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = true;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}
}