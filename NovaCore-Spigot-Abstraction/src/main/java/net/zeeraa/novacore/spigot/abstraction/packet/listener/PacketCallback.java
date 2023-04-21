package net.zeeraa.novacore.spigot.abstraction.packet.listener;

import net.zeeraa.novacore.spigot.abstraction.packet.event.PacketEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;

public class PacketCallback {

    private boolean disabled;
    private Plugin plugin;
    private PacketListener owner;
    private int priority;
    private boolean ignoreCancelled;
    private Method method;


    public PacketCallback(Plugin plugin, PacketListener owner, int priority, boolean ignoreCancelled, Method method) {
        this.plugin = plugin;
        this.owner = owner;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
        this.disabled = false;
        this.method = method;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public PacketListener getOwner() {
        return owner;
    }

    public int getPriority() {
        return priority;
    }

    public boolean ignoreCancelled() {
        return ignoreCancelled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Method getMethod() {
        return method;
    }

    public void run(PacketEvent e) {
        try {
            method.invoke(owner, e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Callback[" + "plugin: " + plugin + ", owner: " + owner.getClass().getSimpleName() + ", priority: " + priority + ", ignoreCancelled: " + ignoreCancelled + ", method: " + method.getName() + "]";
    }
}
