package net.zeeraa.novacore.spigot.abstraction.particle;

import java.util.Collection;
import java.util.function.Predicate;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class NovaParticleProvider {
	public void showColoredRedstoneParticle(Location location, java.awt.Color color) {
		this.showColoredRedstoneParticle(location, new NovaDustOptions(color));
	}

	public void showColoredRedstoneParticle(Location location, java.awt.Color color, Player receiver) {
		this.showColoredRedstoneParticle(location, new NovaDustOptions(color), receiver);
	}

	public void showColoredRedstoneParticle(Location location, java.awt.Color color, Collection<Player> receivers) {
		this.showColoredRedstoneParticle(location, new NovaDustOptions(color), receivers);
	}

	public void showColoredRedstoneParticle(Location location, java.awt.Color color, Predicate<Player> predicate) {
		this.showColoredRedstoneParticle(location, new NovaDustOptions(color), predicate);
	}

	public void showRedstoneParticle(Location location, Color color) {
		this.showColoredRedstoneParticle(location, new NovaDustOptions(color));
	}

	public void showColoredRedstoneParticle(Location location, Color color, Player receiver) {
		this.showColoredRedstoneParticle(location, new NovaDustOptions(color), receiver);
	}

	public void showColoredRedstoneParticle(Location location, Color color, Collection<Player> receivers) {
		this.showColoredRedstoneParticle(location, new NovaDustOptions(color), receivers);
	}

	public void showColoredRedstoneParticle(Location location, Color color, Predicate<Player> predicate) {
		this.showColoredRedstoneParticle(location, new NovaDustOptions(color), predicate);
	}

	public abstract void showColoredRedstoneParticle(Location location, NovaDustOptions options);

	public abstract void showColoredRedstoneParticle(Location location, NovaDustOptions options, Player receiver);

	public abstract void showColoredRedstoneParticle(Location location, NovaDustOptions options, Collection<Player> receivers);

	public abstract void showColoredRedstoneParticle(Location location, NovaDustOptions options, Predicate<Player> predicate);

	public abstract void showParticle(Location location, NovaParticleEffect effect);

	public abstract void showParticle(Location location, NovaParticleEffect effect, Player receiver);

	public abstract void showParticle(Location location, NovaParticleEffect effect, Collection<Player> receivers);

	public abstract void showParticle(Location location, NovaParticleEffect effect, Predicate<Player> predicate);
}