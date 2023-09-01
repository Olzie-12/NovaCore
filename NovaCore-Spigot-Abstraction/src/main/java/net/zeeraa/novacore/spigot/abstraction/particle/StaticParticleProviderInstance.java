package net.zeeraa.novacore.spigot.abstraction.particle;

public class StaticParticleProviderInstance {
	private static NovaParticleProvider instance;
	
	public static NovaParticleProvider getInstance() {
		return instance;
	}
	
	public static void setInstance(NovaParticleProvider instance) {
		StaticParticleProviderInstance.instance = instance;
	}
}
