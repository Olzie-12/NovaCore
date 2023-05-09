package net.zeeraa.novacore.spigot.module.modules.specialevents.event;

import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.zeeraa.novacore.spigot.module.modules.specialevents.NovaSpecialEventsManager;

/**
 * This event is called when a new {@link TNTPrimed} is detected in the world.
 * If this event is canceled {@link TNTPrimed#remove()} will be called on the
 * tnt
 * <p>
 * For this event to be called the {@link NovaSpecialEventsManager} needs to be
 * enabled
 * 
 * @author Zeeraa
 */
public class NovaTNTPrimeEvent extends Event implements Cancellable {
	protected final TNTPrimed tnt;
	protected boolean cancelled;

	public NovaTNTPrimeEvent(TNTPrimed tnt) {
		this.tnt = tnt;
		this.cancelled = false;
	}

	/**
	 * Get the {@link TNTPrimed} that spawned
	 * 
	 * @return {@link TNTPrimed} that spawned
	 */
	public TNTPrimed getTnt() {
		return tnt;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	private static final HandlerList HANDLERS = new HandlerList();

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}