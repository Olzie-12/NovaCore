package net.zeeraa.novacore.spigot.abstraction.particle;

import org.bukkit.Location;

public interface NMSBasedParticleProvider {
	public Object novaToNMSParticle(NovaParticleEffect effect);
	
	public Object constructRedstoneDustParticle(Location location, NovaDustOptions options);
	
	public Object constructNormalParticle(Location location, NovaParticleEffect effect);
}