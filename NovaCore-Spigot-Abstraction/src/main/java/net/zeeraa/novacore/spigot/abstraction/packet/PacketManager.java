package net.zeeraa.novacore.spigot.abstraction.packet;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.commons.utils.ReflectUtils;
import net.zeeraa.novacore.spigot.abstraction.packet.listener.PacketCallback;
import net.zeeraa.novacore.spigot.abstraction.packet.event.PacketEvent;
import net.zeeraa.novacore.spigot.abstraction.packet.listener.PacketEventBus;
import net.zeeraa.novacore.spigot.abstraction.packet.listener.PacketHandler;
import net.zeeraa.novacore.spigot.abstraction.packet.listener.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Packet Manager for registering players
 *
 * @author Bruno
 */
public abstract class PacketManager implements Listener {
	private final List<Player> playersDigging;

	public static void fireEvent(PacketEvent event) {
		event.getPacketEventBus().run(event);
	}
	public static void registerEvents(Plugin plugin, PacketListener listener) {
		for (Method method : listener.getClass().getMethods()) {
			if (!method.isAnnotationPresent(PacketHandler.class)) {
				continue;
			}
			if (method.getParameters().length != 1) {
				continue;
			}
			if (!PacketEvent.class.isAssignableFrom(method.getParameters()[0].getType())) {
				continue;
			}
			// found usable method
			int priority = method.getAnnotation(PacketHandler.class).priority();
			boolean ignoreCancelled = method.getAnnotation(PacketHandler.class).ignoreCancelled();
			Class<? extends PacketEvent> event = (Class<? extends PacketEvent>) method.getParameters()[0].getType();
			if (!ReflectUtils.hasMethod(event, "getEventBus")) {
				Log.error("PacketManager", "static method getEventBus doesn't exist on " + event.getSimpleName());
				continue;
			}
			try {
				Method m = event.getMethod("getEventBus");
				if (!Modifier.isStatic(m.getModifiers()) || !Modifier.isPublic(m.getModifiers())) {
					Log.error("PacketManager", "method getEventBus isnt public and static on " + event.getSimpleName());
					continue;
				}
				if (!PacketEventBus.class.isAssignableFrom(m.getReturnType())) {
					Log.error("PacketManager", "method getEventBus does not return " + PacketEventBus.class.getSimpleName() + " on " + event.getSimpleName());
					continue;
				}
				PacketEventBus eventBus = (PacketEventBus) m.invoke(null);
				eventBus.register(new PacketCallback(plugin, listener, priority, ignoreCancelled, method));
				System.out.println("registered " + method.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public PacketManager() {
		playersDigging = new ArrayList<>();
	}

	public List<Player> getPlayersDigging() {
		return playersDigging;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		this.registerPlayer(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLeave(PlayerQuitEvent e) {
		this.removePlayer(e.getPlayer());
	}

	/**
	 * Registers/injects all online {@link Player}s.
	 */
	public void registerOnlinePlayers() {
		Bukkit.getOnlinePlayers().forEach(this::registerPlayer);
	}

	/**
	 * Registers/injects specific {@link Player}.
	 * 
	 * @param player {@link Player}.
	 */
	public abstract void registerPlayer(Player player);

	/**
	 * Removes all online {@link Player}s
	 */
	public void removeOnlinePlayers() {
		Bukkit.getOnlinePlayers().forEach(this::removePlayer);
	}

	/**
	 * Removes specific {@link Player}
	 * 
	 * @param player {@link Player}.
	 */
	public abstract void removePlayer(Player player);
}