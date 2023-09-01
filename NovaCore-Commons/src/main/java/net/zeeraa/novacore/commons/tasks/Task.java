package net.zeeraa.novacore.commons.tasks;

import javax.annotation.Nullable;

/**
 * Represents a task that can be started or canceled
 * 
 * @author Zeeraa
 */
public abstract class Task {
	/**
	 * Start the task
	 * 
	 * @return <code>true</code> if the task was started
	 */
	public abstract boolean start();

	/**
	 * Stop the task
	 * 
	 * @return <code>true</code> if the task was stopped
	 */
	public abstract boolean stop();

	/**
	 * Check if the task is running
	 * 
	 * @return <code>true</code> if the task is running
	 */
	public abstract boolean isRunning();

	/**
	 * Restart the task. This is the same as calling {@link Task#stop()} and then
	 * {@link Task#start()}
	 */
	public void restart() {
		this.stop();
		this.start();
	}

	/**
	 * Stop the task if its running.
	 * <p>
	 * This will call {@link Task#stop()} if {@link Task#isRunning()} is
	 * <code>true</code>
	 * 
	 * @return if the task is running it will return the result of
	 *         {@link Task#stop()}, if not it will return <code>false</code>
	 */
	public boolean stopIfRunning() {
		if (this.isRunning()) {
			return this.stop();
		}
		return false;
	}

	/**
	 * Call {@link Task#tryStopTask(Task)} on this task.
	 * <p>
	 * See {@link Task#tryStopTask(Task)} for more details
	 * 
	 * @return The result from {@link Task#tryStopTask(Task)}
	 */
	public boolean tryStop() {
		return Task.tryStopTask(this);
	}

	/**
	 * Try to stop the task provided.
	 * <p>
	 * This will only call {@link Task#stop()} on the task if the task is not
	 * <code>null</code> and {@link Task#isRunning()} is <code>true</code>
	 * 
	 * @param task The {@link Task} to stop. <code>null</code> is accepted but will
	 *             make this return <code>false</code>
	 * @return The result of {@link Task#stop()} if the task was running. Will also
	 *         return <code>false</code> if the task is <code>null</code>
	 */
	public static boolean tryStopTask(@Nullable Task task) {
		if (task != null) {
			if (task.isRunning()) {
				return task.stop();
			}
		}
		return false;
	}

	/**
	 * Try to start a task
	 * <p>
	 * This will try to call {@link Task#start()} if the task is not already running
	 * and the task is not <code>null</code>
	 * 
	 * @param task The task to try to start
	 * @return <code>true</code> if the task was started and the task is not
	 *         <code>null</code>
	 */
	public static boolean tryStartTask(@Nullable Task task) {
		if (task != null) {
			if (!task.isRunning()) {
				return task.start();
			}
		}
		return false;
	}
}