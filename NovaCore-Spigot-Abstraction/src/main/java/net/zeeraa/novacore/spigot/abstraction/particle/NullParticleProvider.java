package net.zeeraa.novacore.spigot.abstraction.particle;

import java.util.Collection;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NullParticleProvider extends NovaParticleProvider {
	@Override
	public final void showColoredRedstoneParticle(Location location, NovaDustOptions options) {
	}

	@Override
	public final void showColoredRedstoneParticle(Location location, NovaDustOptions options, Player receiver) {
	}

	@Override
	public final void showColoredRedstoneParticle(Location location, NovaDustOptions options, Collection<Player> receivers) {
	}

	@Override
	public final void showColoredRedstoneParticle(Location location, NovaDustOptions options, Predicate<Player> predicate) {
	}

	@Override
	public final void showParticle(Location location, NovaParticleEffect effect) {
	}

	@Override
	public final void showParticle(Location location, NovaParticleEffect effect, Player receiver) {
	}

	@Override
	public final void showParticle(Location location, NovaParticleEffect effect, Collection<Player> receivers) {
	}

	@Override
	public final void showParticle(Location location, NovaParticleEffect effect, Predicate<Player> predicate) {
	}
}