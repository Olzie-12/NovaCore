package net.zeeraa.novacore.spigot.module.modules.scoreboard.text;

import net.kyori.adventure.text.Component;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent.Builder;

public class StaticTextLine extends ScoreboardLine {
	private String text;
	private Component comp;

	public StaticTextLine(String text) {
		this.text = text;
		this.comp = Component.text(text);
	}

	public String getText() {
		return text;
	}

	@Override
	public Builder apply(Builder builder) {
		builder.addStaticLine(comp);
		return builder;
	}

	@Override
	public Component getComponent() {
		return comp;
	}
}