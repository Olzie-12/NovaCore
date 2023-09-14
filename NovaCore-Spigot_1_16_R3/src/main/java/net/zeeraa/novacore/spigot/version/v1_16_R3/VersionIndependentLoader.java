package net.zeeraa.novacore.spigot.version.v1_16_R3;

import net.novauniverse.spigot.version.shared.v1_16plus.NativeParticleImplementation;
import net.zeeraa.novacore.spigot.abstraction.CommandRegistrator;
import net.zeeraa.novacore.spigot.abstraction.Listeners;
import net.zeeraa.novacore.spigot.abstraction.particle.NovaParticleProvider;

public class VersionIndependentLoader extends net.zeeraa.novacore.spigot.abstraction.VersionIndependantLoader {
	@Override
	public CommandRegistrator getCommandRegistrator() {
		return new NMSBasedCommandRegistrator();
	}

	@Override
	public VersionIndependentUtilsImplementation getVersionIndependentUtils() {
		return new VersionIndependentUtilsImplementation(this);
	}

	@Override
	public Listeners getListeners() {
		return new ListenersImplementation();
	}
	
	@Override
	public NovaParticleProvider getVersionSpecificParticleProvider() {
		return new NativeParticleImplementation();
	}
}