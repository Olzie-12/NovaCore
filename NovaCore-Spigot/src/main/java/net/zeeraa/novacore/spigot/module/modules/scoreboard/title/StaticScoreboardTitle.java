package net.zeeraa.novacore.spigot.module.modules.scoreboard.title;

import net.kyori.adventure.text.Component;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;

public class StaticScoreboardTitle extends ScoreboardTitle {
	private SidebarComponent component;

	public StaticScoreboardTitle(String title) {
		component = SidebarComponent.staticLine(Component.text(title));
	}

	@Override
	public SidebarComponent getComponent() {
		return component;
	}
}