package net.zeeraa.novacore.spigot.command.commands.novacore.debug.providers;

import net.zeeraa.novacore.spigot.NovaCore;

public class NovaCoreMainInfoProvider implements NovaCoreDebugCommandInfoProvider {
	@Override
	public String getData() {
		String message = "";

		message += "No NMS Mode: " + NovaCore.getInstance().isNoNMSMode() + "\n";
		message += "GameEngine enabled: " + NovaCore.isNovaGameEngineEnabled() + "\n";
		if (NovaCore.getInstance().getVersionIndependentUtils() != null) {
			message += "VersionIndependent layer class: " + NovaCore.getInstance().getVersionIndependentUtils().getClass().getName() + "\n";
		} else {
			message += "VersionIndependent layer class: null\n";
		}
		message += "Particle provider: " + NovaCore.getInstance().getNovaParticleProvider().getClass().getName() + "\n";
		message += "Has team manager: " + NovaCore.getInstance().hasTeamManager() + "\n";
		message += "\n";

		return message;
	}
}