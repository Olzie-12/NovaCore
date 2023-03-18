package net.zeeraa.novacore.spigot.abstraction.particle;

import java.util.Collection;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NullParticleProvider extends NovaParticleProvider {
	@Override
	public void showColoredRedstoneParticle(Location location, NovaDustOptions options) {
	}

	@Override
	public void showColoredRedstoneParticle(Location location, NovaDustOptions options, Player receiver) {
	}

	@Override
	public void showColoredRedstoneParticle(Location location, NovaDustOptions options, Collection<Player> receivers) {
	}

	@Override
	public void showColoredRedstoneParticle(Location location, NovaDustOptions options, Predicate<Player> predicate) {
	}

	@Override
	public void showParticle(Location location, NovaParticleEffect effect) {
	}

	@Override
	public void showParticle(Location location, NovaParticleEffect effect, Player receiver) {
	}

	@Override
	public void showParticle(Location location, NovaParticleEffect effect, Collection<Player> receivers) {
	}

	@Override
	public void showParticle(Location location, NovaParticleEffect effect, Predicate<Player> predicate) {
	}
}