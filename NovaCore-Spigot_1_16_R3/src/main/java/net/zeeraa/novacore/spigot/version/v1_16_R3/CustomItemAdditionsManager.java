package net.zeeraa.novacore.spigot.version.v1_16_R3;

import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

public class CustomItemAdditionsManager implements net.zeeraa.novacore.spigot.abstraction.CustomItemAdditionsManager {

    public ItemStack getProjectileItemStack(ProjectileHitEvent event) {
        if (event.getEntity() instanceof ThrowableProjectile) {
            ThrowableProjectile throwableProjectile = (ThrowableProjectile) event.getEntity();
            return throwableProjectile.getItem();
        }
        return null;
    }
}
