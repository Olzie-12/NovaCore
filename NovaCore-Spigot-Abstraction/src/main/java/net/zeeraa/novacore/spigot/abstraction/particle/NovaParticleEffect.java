package net.zeeraa.novacore.spigot.abstraction.particle;

import java.util.Collection;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public enum NovaParticleEffect {
	CRIT_MAGIC, REDSTONE, FIREWORKS_SPARK, SMOKE_LARGE, HEART, SMOKE_NORMAL, EXPLOSION_NORMAL, EXPLOSION_LARGE, EXPLOSION_HUGE, FOOTSTEP;

	public void display(Location location) {
		StaticParticleProviderInstance.getInstance().showParticle(location, this);
	}

	public void display(Location location, Player player) {
		StaticParticleProviderInstance.getInstance().showParticle(location, this, player);
	}

	public void display(Location location, Collection<Player> players) {
		StaticParticleProviderInstance.getInstance().showParticle(location, this, players);
	}

	public void display(Location location, Predicate<Player> predicate) {
		StaticParticleProviderInstance.getInstance().showParticle(location, this, predicate);
	}
}