package net.zeeraa.novacore.spigot.librarymanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class LibraryLoadCondition {
	private List<Supplier<Boolean>> dontLoadIf;

	public LibraryLoadCondition() {
		dontLoadIf = new ArrayList<>();
	}

	public void addPreventLoadCondition(Supplier<Boolean> condition) {
		this.dontLoadIf.add(condition);
	}

	public boolean shouldLoad() {
		if (dontLoadIf.stream().anyMatch(s -> s.get())) {
			return false;
		}

		return true;
	}
}