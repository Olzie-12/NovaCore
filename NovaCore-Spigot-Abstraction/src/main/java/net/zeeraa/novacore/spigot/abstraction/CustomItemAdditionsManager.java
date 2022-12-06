package net.zeeraa.novacore.spigot.abstraction;

import org.bukkit.entity.Item;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

public interface CustomItemAdditionsManager {

    public ItemStack getProjectileItemStack(ProjectileHitEvent event);


}
