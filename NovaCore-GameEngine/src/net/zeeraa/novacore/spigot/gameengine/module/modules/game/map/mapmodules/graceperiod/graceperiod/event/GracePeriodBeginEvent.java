package net.zeeraa.novacore.spigot.gameengine.module.modules.game.map.mapmodules.graceperiod.graceperiod.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GracePeriodBeginEvent extends Event {
	private static final HandlerList HANDLERS_LIST = new HandlerList();

	private int time;

	public GracePeriodBeginEvent(int time) {
		this.time = time;
	}

	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}
}