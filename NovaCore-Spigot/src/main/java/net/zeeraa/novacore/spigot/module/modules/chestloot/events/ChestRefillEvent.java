package net.zeeraa.novacore.spigot.module.modules.chestloot.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChestRefillEvent extends Event implements Cancellable {
	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private boolean showMessage;
	private boolean cancel;

	public ChestRefillEvent(boolean showMessage) {
		this.showMessage = showMessage;
		this.cancel = false;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	public boolean isShowMessage() {
		return showMessage;
	}
	
	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}
}