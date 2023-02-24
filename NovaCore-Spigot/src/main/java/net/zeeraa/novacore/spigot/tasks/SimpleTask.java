package net.zeeraa.novacore.spigot.tasks;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.spigot.NovaCore;

/**
 * This creates a task using the {@link BukkitRunnable} class
 * 
 * @author Anton
 */
public class SimpleTask extends Task {
	protected Runnable runnable;
	protected Plugin plugin;
	protected long delay;
	protected long period;

	protected BukkitTask bukkitTask;

	protected TaskExecutionMode taskExecutionMode;

	public SimpleTask(Runnable runnable, long period, TaskExecutionMode taskExecutionMode) {
		this(NovaCore.getInstance(), runnable, period, period, taskExecutionMode);
	}

	public SimpleTask(Runnable runnable, long period) {
		this(NovaCore.getInstance(), runnable, period, period, TaskExecutionMode.SYNCHRONOUS);
	}

	public SimpleTask(Plugin plugin, Runnable runnable, long period) {
		this(plugin, runnable, period, period, TaskExecutionMode.SYNCHRONOUS);
	}

	public SimpleTask(Runnable runnable, long delay, long period) {
		this(NovaCore.getInstance(), runnable, delay, period, TaskExecutionMode.SYNCHRONOUS);
	}

	public SimpleTask(Plugin plugin, Runnable runnable, long delay, long period) {
		this(plugin, runnable, delay, period, TaskExecutionMode.SYNCHRONOUS);
	}

	public SimpleTask(Plugin plugin, Runnable runnable, long delay, long period, TaskExecutionMode taskExecutionMode) {
		this.bukkitTask = null;

		this.plugin = plugin;
		this.runnable = runnable;
		this.delay = delay;
		this.period = period;

		if (taskExecutionMode == null) {
			throw new IllegalArgumentException("TaskExecutionMode cant be null");
		}

		this.taskExecutionMode = taskExecutionMode;
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
	 * Update the interval the task run at. This also calls
	 * {@link SimpleTask#restart()} if the task is running
	 * 
	 * @param delay The delay
	 */
	public void updateInterval(long delay) {
		this.updateInterval(delay, delay);
	}

	/**
	 * Update the interval the task run at. This also calls
	 * {@link SimpleTask#restart()} if the task is running
	 * 
	 * @param delay  The delay
	 * @param period The period
	 */
	public void updateInterval(long delay, long period) {
		this.delay = delay;
		this.period = period;
		if (isRunning()) {
			restart();
		}
	}

	@Override
	public boolean start() {
		if (bukkitTask != null) {
			return false;
		}

		switch (taskExecutionMode) {
		case ASYNCHRONOUS:
			bukkitTask = new BukkitRunnable() {
				@Override
				public void run() {
					runnable.run();
				}
			}.runTaskTimerAsynchronously(plugin, delay, period);
			break;

		case SYNCHRONOUS:
			bukkitTask = new BukkitRunnable() {
				@Override
				public void run() {
					runnable.run();
				}
			}.runTaskTimer(plugin, delay, period);

			break;

		default:
			return false;
		}

		return true;
	}

	@Override
	public boolean stop() {
		if (bukkitTask == null) {
			return false;
		}
		bukkitTask.cancel();
		bukkitTask = null;
		return true;
	}

	@Override
	public boolean isRunning() {
		return bukkitTask != null;
	}
}