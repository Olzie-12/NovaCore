package net.zeeraa.novacore.spigot.version.v1_8_R3;

import java.util.Collection;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.zeeraa.novacore.spigot.abstraction.particle.NMSBasedParticleProvider;
import net.zeeraa.novacore.spigot.abstraction.particle.NovaDustOptions;
import net.zeeraa.novacore.spigot.abstraction.particle.NovaParticleEffect;
import net.zeeraa.novacore.spigot.abstraction.particle.NovaParticleProvider;

public class NMSParticleImplementation extends NovaParticleProvider implements NMSBasedParticleProvider {
	public PacketPlayOutWorldParticles constructRedstoneDustParticle(Location location, NovaDustOptions options) {
		return new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), options.getColor().getRed(), options.getColor().getGreen(), options.getColor().getBlue(), 0, 0, 0);
	}

	public PacketPlayOutWorldParticles constructNormalParticle(Location location, NovaParticleEffect effect) {
		if (effect == NovaParticleEffect.REDSTONE) {
			return constructRedstoneDustParticle(location, NovaDustOptions.RED);
		}

		EnumParticle particle = novaToNMSParticle(effect);

		return new PacketPlayOutWorldParticles(particle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, 0, 0, 0);
	}

	public EnumParticle novaToNMSParticle(NovaParticleEffect effect) {
		switch (effect) {
		case CRIT_MAGIC:
			return EnumParticle.CRIT_MAGIC;

		case FIREWORKS_SPARK:
			return EnumParticle.FIREWORKS_SPARK;

		case HEART:
			return EnumParticle.HEART;

		case REDSTONE:
			return EnumParticle.REDSTONE;

		case SMOKE_LARGE:
			return EnumParticle.SMOKE_LARGE;

		case SMOKE_NORMAL:
			return EnumParticle.SMOKE_NORMAL;

		case EXPLOSION_NORMAL:
			return EnumParticle.EXPLOSION_NORMAL;
			
		case EXPLOSION_LARGE:
			return EnumParticle.EXPLOSION_LARGE;
			
		case EXPLOSION_HUGE:
			return EnumParticle.EXPLOSION_HUGE;
			
		case FOOTSTEP:
			return EnumParticle.FOOTSTEP;
		}

		return null;
	}

	@Override
	public boolean runNovaParticleEffectConversionTest(NovaParticleEffect effect) {
		return novaToNMSParticle(effect) != null;
	}

	@Override
	public void showColoredRedstoneParticle(Location location, NovaDustOptions options) {
		PacketPlayOutWorldParticles particle = constructRedstoneDustParticle(location, options);
		location.getWorld().getPlayers().forEach(p -> {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(particle);
		});
	}

	@Override
	public void showColoredRedstoneParticle(Location location, NovaDustOptions options, Player receiver) {
		PacketPlayOutWorldParticles particle = constructRedstoneDustParticle(location, options);
		((CraftPlayer) receiver).getHandle().playerConnection.sendPacket(particle);
	}

	@Override
	public void showColoredRedstoneParticle(Location location, NovaDustOptions options, Collection<Player> receivers) {
		PacketPlayOutWorldParticles particle = constructRedstoneDustParticle(location, options);
		receivers.forEach(p -> {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(particle);
		});
	}

	@Override
	public void showColoredRedstoneParticle(Location location, NovaDustOptions options, Predicate<Player> predicate) {
		PacketPlayOutWorldParticles particle = constructRedstoneDustParticle(location, options);
		location.getWorld().getPlayers().stream().filter(predicate).forEach(p -> {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(particle);
		});
	}

	@Override
	public void showParticle(Location location, NovaParticleEffect effect) {
		PacketPlayOutWorldParticles particle = constructNormalParticle(location, effect);
		location.getWorld().getPlayers().forEach(p -> {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(particle);
		});
	}

	@Override
	public void showParticle(Location location, NovaParticleEffect effect, Player receiver) {
		PacketPlayOutWorldParticles particle = constructNormalParticle(location, effect);
		((CraftPlayer) receiver).getHandle().playerConnection.sendPacket(particle);
	}

	@Override
	public void showParticle(Location location, NovaParticleEffect effect, Collection<Player> receivers) {
		PacketPlayOutWorldParticles particle = constructNormalParticle(location, effect);
		receivers.forEach(p -> {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(particle);
		});
	}

	@Override
	public void showParticle(Location location, NovaParticleEffect effect, Predicate<Player> predicate) {
		PacketPlayOutWorldParticles particle = constructNormalParticle(location, effect);
		location.getWorld().getPlayers().stream().filter(predicate).forEach(p -> {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(particle);
		});
	}
}