package net.zeeraa.novacore.spigot.module.modules.scoreboard.text;

import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;

public abstract class ScoreboardLine {
	public abstract SidebarComponent.Builder apply(SidebarComponent.Builder builder);
	
	public void tick() {
	}
}
