package net.zeeraa.novacore.spigot.version.v1_12_R1;

import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

public class CustomManagerItemAdditions implements net.zeeraa.novacore.spigot.abstraction.CustomItemAdditionsManager {

    @Override
    public ItemStack getProjectileItemStack(ProjectileHitEvent event) {
        return null;
    }
}
