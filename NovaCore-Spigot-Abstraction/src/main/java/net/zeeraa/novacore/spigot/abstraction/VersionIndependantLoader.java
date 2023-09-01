package net.zeeraa.novacore.spigot.abstraction;

import net.zeeraa.novacore.spigot.abstraction.particle.NovaParticleProvider;

public abstract class VersionIndependantLoader {
	public abstract CommandRegistrator getCommandRegistrator();

	public abstract VersionIndependentUtils getVersionIndependentUtils();

	public abstract Listeners getListeners();

	public abstract NovaParticleProvider getVersionSpecificParticleProvider();
}