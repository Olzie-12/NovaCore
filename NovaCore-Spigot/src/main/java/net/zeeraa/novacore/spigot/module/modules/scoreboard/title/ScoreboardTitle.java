package net.zeeraa.novacore.spigot.module.modules.scoreboard.title;

import net.kyori.adventure.text.Component;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;

public abstract class ScoreboardTitle {
	public abstract SidebarComponent getComponent();

	public abstract Component asNormalComponent();
	
	public void tick() {
	}

	public static ScoreboardTitle staticText(String text) {
		return new StaticScoreboardTitle(text);
	}
}
