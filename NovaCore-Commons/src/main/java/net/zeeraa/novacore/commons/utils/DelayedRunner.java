package net.zeeraa.novacore.commons.utils;

import net.zeeraa.novacore.commons.log.Log;

public abstract class DelayedRunner {
	private static DelayedRunnerImplementation implementation = null;
	public static final void setImplementation(DelayedRunnerImplementation implementation) {
		DelayedRunner.implementation = implementation;
	}

	public static final boolean hasImplementation() {
		return DelayedRunner.implementation != null;
	}
	
	public static final void runDelayed(Runnable runnable, long ticks) {
		if(implementation == null) {
			Log.error("DelayedRunner", "No platform specific implementation has been enabled. Make sure DelayedRunner#setImplementation() has been called");
		}
		DelayedRunner.implementation.runDelayed(runnable, ticks);
	}
}