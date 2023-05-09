package net.zeeraa.novacore.spigot.abstraction.packet.listener;

import net.zeeraa.novacore.spigot.abstraction.packet.event.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PacketEventBus {
    private List<PacketCallback> callbacks;

    private static List<PacketEventBus> allBuses = new ArrayList<>();

    public void run(PacketEvent e) {
        for (PacketCallback callback : callbacks) {
            if (!callback.isDisabled()) {
                if (!callback.getPlugin().isEnabled()) {
                    PacketEventBus.unregisterAll(callback.getPlugin());
                    continue;
                }
                if (!callback.ignoreCancelled()) {
                    if (e instanceof Cancellable) {
                        if (((Cancellable) e).isCancelled()) {
                            continue;
                        }
                    }
                }
                callback.run(e);
            }
        }
    }

    public PacketEventBus() {
        callbacks = new ArrayList<>();
        allBuses.add(this);
        Bukkit.getPluginManager();
    }


    public void register(PacketCallback callback) {
       callbacks.add(callback);
       List<PacketCallback> newList = callbacks.stream().sorted(Comparator.comparingInt(PacketCallback::getPriority)).sorted(Collections.reverseOrder()).collect(Collectors.toList());
       callbacks.clear();
       callbacks.addAll(newList);
    }

    public static void unregisterAll(PacketListener listener) {
        allBuses.forEach(bus -> bus.callbacks.stream().filter(callback -> callback.getOwner().getClass().equals(listener.getClass())).forEach(callback -> callback.setDisabled(true)));
    }

    public static void unregisterAll(Plugin plugin) {
        allBuses.forEach(bus -> bus.callbacks.stream().filter(callback -> callback.getPlugin().getName().equals(plugin.getName())).forEach(callback -> callback.setDisabled(true)));
    }

    public void unregister(PacketListener listener) {
        callbacks.stream().filter(callback -> callback.getOwner().getClass().equals(listener.getClass())).forEach(callback -> callback.setDisabled(true));
    }

    public void unregister(Plugin plugin) {
        callbacks.stream().filter(callback -> callback.getPlugin().getName().equals(plugin.getName())).forEach(callback -> callback.setDisabled(true));
    }

}
