package net.zeeraa.novacore.spigot.module.modules.scoreboard.text;

import java.util.function.Supplier;

import net.kyori.adventure.text.Component;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent.Builder;

public class DynamicTextLine extends ScoreboardLine {
	private Supplier<String> supplier;

	public DynamicTextLine(Supplier<String> supplier) {
		this.supplier = supplier;
	}

	@Override
	public Builder apply(Builder builder) {
		builder.addDynamicLine(() -> {
			return Component.text(supplier.get());
		});
		return builder;
	}

	@Override
	public Component getComponent() {
		return Component.text(supplier.get());
	}
}