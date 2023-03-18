package net.zeeraa.novacore.spigot.version.v1_8_R3;

import net.zeeraa.novacore.spigot.abstraction.CommandRegistrator;
import net.zeeraa.novacore.spigot.abstraction.Listeners;
import net.zeeraa.novacore.spigot.abstraction.particle.NovaParticleProvider;

public class VersionIndependentLoader extends net.zeeraa.novacore.spigot.abstraction.VersionIndependantLoader {
	@Override
	public CommandRegistrator getCommandRegistrator() {
		return new net.zeeraa.novacore.spigot.version.v1_8_R3.CommandRegistrator();
	}

	@Override
	public VersionIndependentUtils getVersionIndependentUtils() {
		return new net.zeeraa.novacore.spigot.version.v1_8_R3.VersionIndependentUtils();
	}

	@Override
	public Listeners getListeners() {
		return new net.zeeraa.novacore.spigot.version.v1_8_R3.Listeners();
	}
	
	@Override
	public NovaParticleProvider getVersionSpecificParticleProvider() {
		return new NMSParticleImplementation();
	}
}