package net.zeeraa.novacore.commons.utils.misc;

import java.util.List;
import java.util.function.Consumer;

public class LowEffortShittyBatchedExecutor<T> {
	private int division;
	private int runCounter;

	private List<T> source;
	private int index;

	public LowEffortShittyBatchedExecutor(int division, List<T> source) {
		this.division = division;
		this.source = source;
		this.runCounter = 0;

		index = 0;
	}

	public void consumeBatch(Consumer<T> consumer) {
		runCounter++;
		if (runCounter < 1) {
			return;
		}

		if (source.size() == 0) {
			return;
		}

		int toGet = (int) Math.ceil((double) source.size() / (double) division);

		for (int i = 0; i < toGet; i++) {
			consumer.accept(source.get(index));
			index++;
			if (index >= source.size()) {
				if (runCounter < division) {
					runCounter -= division;
				}
				index = 0;
				return;
			}
		}
	}
}