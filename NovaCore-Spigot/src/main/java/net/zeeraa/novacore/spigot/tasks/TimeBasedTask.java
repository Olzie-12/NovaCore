package net.zeeraa.novacore.spigot.tasks;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.commons.tasks.Task;

/**
 * A {@link Task} that uses real time for the delay instead of counting ticks.
 * The way this works is that each tick the server checks the time since the
 * task executed last time and if the time is larger than or equal to the
 * provided target delay the task executes
 * <p>
 * NOTE: This timer will not bypass the 20 tps restriction!
 * 
 * @author Anton
 */
public class TimeBasedTask extends Task {
	protected Runnable runnable;
	protected Plugin plugin;
	protected long targetTimeBetweenExectionMS;
	protected boolean autoCancel;
	protected TaskExecutionMode taskExecutionMode;
	protected Instant lastExecution;
	protected long actualTimeSinceLastRun;
	protected List<TimeBasedTaskCallback> callbacks;

	protected BukkitTask task;

	/**
	 * A task that uses {@link Instant} to keep track instead of relying on the tick
	 * loop for high accuracy timed events. This will have slightly worse
	 * performance than {@link SimpleTask} so use this only if you need a task is
	 * not bound to the tick rate. NOTE: This will not allow you to run timers
	 * faster that the minecraft tick loop, it will still be restricted by the 20
	 * tps limit but will not cause timers to slow down from lower tick rate, for
	 * more info see the documentation of {@link TimeBasedTask}
	 * 
	 * @param runnable                    The {@link Runnable} to call when the task
	 *                                    executes
	 * @param plugin                      The {@link Plugin} that own the task
	 * @param targetTimeBetweenExectionMS The time that the task should try to use
	 */
	public TimeBasedTask(Runnable runnable, Plugin plugin, long targetTimeBetweenExectionMS) {
		this(runnable, plugin, targetTimeBetweenExectionMS, false, TaskExecutionMode.SYNCHRONOUS);
	}

	/**
	 * A task that uses {@link Instant} to keep track instead of relying on the tick
	 * loop for high accuracy timed events. This will have slightly worse
	 * performance than {@link SimpleTask} so use this only if you need a task is
	 * not bound to the tick rate. NOTE: This will not allow you to run timers
	 * faster that the minecraft tick loop, it will still be restricted by the 20
	 * tps limit but will not cause timers to slow down from lower tick rate, for
	 * more info see the documentation of {@link TimeBasedTask}
	 * 
	 * @param runnable                    The {@link Runnable} to call when the task
	 *                                    executes
	 * @param plugin                      The {@link Plugin} that own the task
	 * @param targetTimeBetweenExectionMS The time that the task should try to use
	 *                                    between each call to the provided runnable
	 * @param autoCancel                  <code>true</code> to run
	 *                                    {@link Task#tryStopTask(Task)} after each
	 *                                    time the task gets called. This can be
	 *                                    used to create timers that auto cancels
	 */
	public TimeBasedTask(Runnable runnable, Plugin plugin, long targetTimeBetweenExectionMS, boolean autoCancel) {
		this(runnable, plugin, targetTimeBetweenExectionMS, autoCancel, TaskExecutionMode.SYNCHRONOUS);
	}

	/**
	 * A task that uses {@link Instant} to keep track instead of relying on the tick
	 * loop for high accuracy timed events. This will have slightly worse
	 * performance than {@link SimpleTask} so use this only if you need a task is
	 * not bound to the tick rate. NOTE: This will not allow you to run timers
	 * faster that the minecraft tick loop, it will still be restricted by the 20
	 * tps limit but will not cause timers to slow down from lower tick rate, for
	 * more info see the documentation of {@link TimeBasedTask}
	 * 
	 * @param runnable                    The {@link Runnable} to call when the task
	 *                                    executes
	 * @param plugin                      The {@link Plugin} that own the task
	 * @param targetTimeBetweenExectionMS The time that the task should try to use
	 *                                    between each call to the provided runnable
	 * 
	 * @param taskExecutionMode           The {@link TaskExecutionMode} to run in
	 */
	public TimeBasedTask(Runnable runnable, Plugin plugin, long targetTimeBetweenExectionMS, TaskExecutionMode taskExecutionMode) {
		this(runnable, plugin, targetTimeBetweenExectionMS, false, taskExecutionMode);
	}

	/**
	 * A task that uses {@link Instant} to keep track instead of relying on the tick
	 * loop for high accuracy timed events. This will have slightly worse
	 * performance than {@link SimpleTask} so use this only if you need a task is
	 * not bound to the tick rate. NOTE: This will not allow you to run timers
	 * faster that the minecraft tick loop, it will still be restricted by the 20
	 * tps limit but will not cause timers to slow down from lower tick rate, for
	 * more info see the documentation of {@link TimeBasedTask}
	 * 
	 * @param runnable                    The {@link Runnable} to call when the task
	 *                                    executes
	 * @param plugin                      The {@link Plugin} that own the task
	 * @param targetTimeBetweenExectionMS The time that the task should try to use
	 *                                    between each call to the provided runnable
	 * @param autoCancel                  <code>true</code> to run
	 *                                    {@link Task#tryStopTask(Task)} after each
	 *                                    time the task gets called. This can be
	 *                                    used to create timers that auto cancels
	 * @param taskExecutionMode           The {@link TaskExecutionMode} to run in
	 */
	public TimeBasedTask(Runnable runnable, Plugin plugin, long targetTimeBetweenExectionMS, boolean autoCancel, TaskExecutionMode taskExecutionMode) {
		this.runnable = runnable;
		this.plugin = plugin;
		this.targetTimeBetweenExectionMS = targetTimeBetweenExectionMS;
		this.autoCancel = autoCancel;
		this.taskExecutionMode = taskExecutionMode;
		this.lastExecution = Instant.now();
		this.actualTimeSinceLastRun = 0L;

		if (taskExecutionMode == null) {
			throw new IllegalArgumentException("TaskExecutionMode cant be null");
		}

		this.task = null;
	}

	public List<TimeBasedTaskCallback> getCallbacks() {
		return callbacks;
	}

	public boolean addCallback(TimeBasedTaskCallback callback) {
		if (!this.callbacks.contains(callback)) {
			this.callbacks.add(callback);
			return true;
		}
		return false;
	}

	public long getMSElapsed() {
		return Duration.between(lastExecution, Instant.now()).toMillis();
	}

	public long getMSLeft() {
		long timeLeft = targetTimeBetweenExectionMS - Duration.between(lastExecution, Instant.now()).toMillis();
		if (timeLeft < 0) {
			timeLeft = 0;
		}
		return timeLeft;
	}

	private void tickCheck() {
		final long sinceLastExec = getMSElapsed(); // Duration.between(lastExecution, Instant.now()).toMillis();
		final long msLeft = targetTimeBetweenExectionMS - sinceLastExec;

		callbacks.forEach(c -> c.processTick(this, sinceLastExec, msLeft));

		if (sinceLastExec >= targetTimeBetweenExectionMS) {
			if (autoCancel) {
				stop();
			}
			actualTimeSinceLastRun = sinceLastExec;
			runnable.run();
			lastExecution = Instant.now();
		}
	}

	@Override
	public boolean start() {
		if (isRunning()) {
			return false;
		}

		lastExecution = Instant.now();

		callbacks.forEach(TimeBasedTaskCallback::reset);

		switch (taskExecutionMode) {
		case ASYNCHRONOUS:
			task = new BukkitRunnable() {
				@Override
				public void run() {
					tickCheck();
				}
			}.runTaskTimerAsynchronously(plugin, 0L, 0L);
			break;

		case SYNCHRONOUS:
			task = new BukkitRunnable() {
				@Override
				public void run() {
					tickCheck();
				}
			}.runTaskTimer(plugin, 0L, 0L);

			break;

		default:
			return false;
		}

		return true;
	}

	@Override
	public boolean stop() {
		if (isRunning()) {
			task.cancel();
			task = null;
			return true;
		}

		return false;
	}

	@Override
	public boolean isRunning() {
		return task != null;
	}

	/**
	 * Get the {@link TaskExecutionMode}
	 * 
	 * @return {@link TaskExecutionMode} the has been set, not that this might not
	 *         correctly reflect the active execution mode since if the mode was
	 *         changed after the task started the new mode wont be used until the
	 *         task has restarted
	 */
	public TaskExecutionMode getTaskExecutionMode() {
		return taskExecutionMode;
	}

	/**
	 * Change the {@link TaskExecutionMode}. The task needs to restart to apply the
	 * mode, see {@link Task#restart()}
	 * 
	 * @param taskExecutionMode The new {@link TaskExecutionMode} to use
	 */
	public void setTaskExecutionMode(TaskExecutionMode taskExecutionMode) {
		if (taskExecutionMode == null) {
			throw new IllegalArgumentException("TaskExecutionMode cant be null");
		}

		this.taskExecutionMode = taskExecutionMode;
	}

	/**
	 * @return The time that the task will try to use between each execution
	 */
	public long getTargetTimeBetweenExectionMS() {
		return targetTimeBetweenExectionMS;
	}

	/**
	 * Sets the time to try to use between each execution. You dont need to restart
	 * the timer to apply this
	 * 
	 * @param targetTimeBetweenExectionMS New time to use
	 */
	public void setTargetTimeBetweenExectionMS(long targetTimeBetweenExectionMS) {
		this.targetTimeBetweenExectionMS = targetTimeBetweenExectionMS;
	}

	/**
	 * @return <code>true</code> if the timer will auto cancel
	 */
	public boolean isAutoCancel() {
		return autoCancel;
	}

	public void setAutoCancel(boolean autoCancel) {
		this.autoCancel = autoCancel;
	}

	/**
	 * @return The {@link Instant} when the task was executed last time. If the
	 *         timer has not yet executed this value will be the time the task was
	 *         created at
	 */
	public Instant getLastExecution() {
		return lastExecution;
	}

	/**
	 * @return Get the time in milliseconds since the last run. If this is the first
	 *         time the tast was called it will return the time since the task was
	 *         started
	 */
	public long getActualTimeSinceLastRun() {
		return actualTimeSinceLastRun;
	}

	public static class TimeBasedTaskCallback {
		protected Consumer<TimeBasedTask> callback;
		protected TimeBasedTaskCallbackMode mode;
		protected long timeMS;
		protected boolean didRun;
		protected boolean logExceptionsWithNovaLogger;

		public TimeBasedTaskCallback(Consumer<TimeBasedTask> callback, TimeBasedTaskCallbackMode mode, long timeMS) {
			this(callback, mode, timeMS, true);
		}

		public TimeBasedTaskCallback(Consumer<TimeBasedTask> callback, TimeBasedTaskCallbackMode mode, long timeMS, boolean logExceptionsWithNovaLogger) {
			this.callback = callback;
			this.mode = mode;
			this.timeMS = timeMS;
			this.didRun = false;

		}

		public Consumer<TimeBasedTask> getCallback() {
			return callback;
		}

		public TimeBasedTaskCallbackMode getMode() {
			return mode;
		}

		public boolean didRun() {
			return this.didRun;
		}

		public long getTimeMS() {
			return timeMS;
		}

		public boolean isLogExceptionsWithNovaLogger() {
			return logExceptionsWithNovaLogger;
		}

		public void setLogExceptionsWithNovaLogger(boolean logExceptionsWithNovaLogger) {
			this.logExceptionsWithNovaLogger = logExceptionsWithNovaLogger;
		}

		protected void reset() {
			didRun = false;
		}

		protected void run(TimeBasedTask timeBasedTask) {
			didRun = true;
			try {
				callback.accept(timeBasedTask);
			} catch (Exception e) {
				if (logExceptionsWithNovaLogger) {
					Log.error("TimeBasedTaskCallback", "An error occured in " + this.toString() + " belonging to task " + timeBasedTask + ". " + e.getClass().getName() + " " + e.getMessage() + (e.getCause() == null ? "" : ". Caused by " + e.getCause().getClass().getName() + ". " + e.getCause().getMessage()));
				}
				e.printStackTrace();
			}
		}

		protected void processTick(TimeBasedTask timeBasedTask, long sinceLastExec, long msLeft) {
			if (didRun) {
				return;
			}

			if (mode == TimeBasedTaskCallbackMode.RUN_AFTER_TIME) {
				if (sinceLastExec > timeMS) {
					this.run(timeBasedTask);
				}
			} else if (mode == TimeBasedTaskCallbackMode.RUN_WHEN_TIME_LEFT) {
				if (msLeft < timeMS) {
					this.run(timeBasedTask);
				}
			}
		}
	}

	public static enum TimeBasedTaskCallbackMode {
		RUN_AFTER_TIME, RUN_WHEN_TIME_LEFT;
	}
}