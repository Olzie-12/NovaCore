package net.novauniverse.spigot.version.shared.v1_16plus;

import java.util.Collection;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import net.zeeraa.novacore.spigot.abstraction.particle.NovaDustOptions;
import net.zeeraa.novacore.spigot.abstraction.particle.NovaParticleEffect;
import net.zeeraa.novacore.spigot.abstraction.particle.NovaParticleProvider;

public class NativeParticleImplementation extends NovaParticleProvider {
	@Override
	public void showColoredRedstoneParticle(Location location, NovaDustOptions options) {
		location.getWorld().spawnParticle(Particle.REDSTONE, location.getX(), location.getY(), location.getZ(), 1, new Particle.DustOptions(options.getColor(), options.getSize()));
	}

	@Override
	public void showColoredRedstoneParticle(Location location, NovaDustOptions options, Player receiver) {
		receiver.spawnParticle(Particle.REDSTONE, location.getX(), location.getY(), location.getZ(), 1, new Particle.DustOptions(options.getColor(), options.getSize()));
	}

	@Override
	public void showColoredRedstoneParticle(Location location, NovaDustOptions options, Collection<Player> receivers) {
		Particle.DustOptions data = new Particle.DustOptions(options.getColor(), options.getSize());
		receivers.forEach(receiver -> receiver.spawnParticle(Particle.REDSTONE, location.getX(), location.getY(), location.getZ(), 1, data));
	}

	@Override
	public void showColoredRedstoneParticle(Location location, NovaDustOptions options, Predicate<Player> predicate) {
		Particle.DustOptions data = new Particle.DustOptions(options.getColor(), options.getSize());
		location.getWorld().getPlayers().stream().filter(predicate).forEach(receiver -> receiver.spawnParticle(Particle.REDSTONE, location.getX(), location.getY(), location.getZ(), 1, data));
	}

	private Particle mapParticle(NovaParticleEffect effect) {
		switch (effect) {
		case CRIT_MAGIC:
			return Particle.CRIT_MAGIC;
		case REDSTONE:
			return Particle.REDSTONE;
		case FIREWORKS_SPARK:
			return Particle.FIREWORKS_SPARK;
		case SMOKE_LARGE:
			return Particle.SMOKE_LARGE;
		case HEART:
			return Particle.HEART;
		case SMOKE_NORMAL:
			return Particle.SMOKE_NORMAL;
		}
		return null;
	}

	@Override
	public void showParticle(Location location, NovaParticleEffect effect) {
		location.getWorld().spawnParticle(mapParticle(effect), location, 1);
	}

	@Override
	public void showParticle(Location location, NovaParticleEffect effect, Player receiver) {
		receiver.spawnParticle(mapParticle(effect), location, 1);
	}

	@Override
	public void showParticle(Location location, NovaParticleEffect effect, Collection<Player> receivers) {
		receivers.forEach(receiver -> receiver.spawnParticle(mapParticle(effect), location, 1));
	}

	@Override
	public void showParticle(Location location, NovaParticleEffect effect, Predicate<Player> predicate) {
		location.getWorld().getPlayers().stream().filter(predicate).forEach(receiver -> receiver.spawnParticle(mapParticle(effect), location, 1));
	}
}