package net.zeeraa.novacore.spigot.abstraction;

import org.bukkit.Location;

import net.zeeraa.novacore.spigot.abstraction.particle.NovaDustOptions;
import net.zeeraa.novacore.spigot.abstraction.particle.NovaParticleEffect;

public interface NMSBasedParticleProvider {
	public Object novaToNMSParticle(NovaParticleEffect effect);
	
	public Object constructRedstoneDustParticle(Location location, NovaDustOptions options);
	
	public Object constructNormalParticle(Location location, NovaParticleEffect effect);
}