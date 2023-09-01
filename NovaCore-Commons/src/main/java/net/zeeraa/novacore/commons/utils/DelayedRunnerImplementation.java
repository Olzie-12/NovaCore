package net.zeeraa.novacore.commons.utils;

public interface DelayedRunnerImplementation {
	void runDelayed(Runnable runnable, long ticks);
}