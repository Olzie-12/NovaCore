package net.zeeraa.novacore.spigot.spectators;

import net.zeeraa.novacore.spigot.NovaCore;
import net.zeeraa.novacore.spigot.abstraction.manager.CustomSpectatorManager;
import net.zeeraa.novacore.spigot.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.RegisteredListener;

public class SpectatorListener implements Listener {
	public SpectatorListener() {
		RegisteredListener onEvent = new RegisteredListener(this, (listener, event) -> onEvent(event), EventPriority.MONITOR, NovaCore.getInstance(), true);

		for (HandlerList handler : HandlerList.getHandlerLists()) {
			handler.register(onEvent);
		}
	}

	public void onEvent(Event e) {
		if (e instanceof BlockCanBuildEvent) {
			BlockCanBuildEvent bcbe = (BlockCanBuildEvent) e;
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (CustomSpectatorManager.isSpectator(player)) {
					if (LocationUtils.entityIsInsideBlockLocation(bcbe.getBlock(), player)) {
						bcbe.setBuildable(true);
					}
				}
			}

		}
		if (e instanceof PlayerEvent) {
			if (CustomSpectatorManager.isSpectator(((PlayerEvent) e).getPlayer())) {
				boolean cancel = true;
				for (Class<? extends Event> clazz : CustomSpectatorManager.getPermittedEvents()) {
					if (e.getClass().isAssignableFrom(clazz)) {
						cancel = false;
						break;
					}
				}
				if (e instanceof Cancellable) {
					((Cancellable) e).setCancelled(cancel);
				}
			}
		}
	}
}