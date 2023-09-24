package net.zeeraa.novacore.spigot.module.modules.scoreboard.title;

import net.kyori.adventure.text.Component;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;

public class StaticScoreboardTitle extends ScoreboardTitle {
	private SidebarComponent sidebarComponent;
	private Component component;
	
	public StaticScoreboardTitle(String title) {
		component = Component.text(title);
		sidebarComponent = SidebarComponent.staticLine(component);
	}

	@Override
	public SidebarComponent getComponent() {
		return sidebarComponent;
	}
	
	@Override
	public Component asNormalComponent() {
		return component;
	}
}