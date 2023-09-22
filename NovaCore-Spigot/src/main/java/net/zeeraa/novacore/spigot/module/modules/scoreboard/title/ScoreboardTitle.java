package net.zeeraa.novacore.spigot.module.modules.scoreboard.title;

import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;

public abstract class ScoreboardTitle {
	public abstract SidebarComponent getComponent();

	public void tick() {
	}

	public static ScoreboardTitle staticText(String text) {
		return new StaticScoreboardTitle(text);
	}
}
