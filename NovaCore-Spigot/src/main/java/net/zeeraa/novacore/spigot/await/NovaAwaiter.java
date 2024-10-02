package net.zeeraa.novacore.spigot.await;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.plugin.Plugin;

import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.spigot.tasks.SimpleTask;
import net.zeeraa.novacore.spigot.tasks.TaskExecutionMode;

/**
 * A utility to wait for tasks to complete and then call a runnable when all
 * tasks are done
 * 
 * @author Zeeraa
 */
public class NovaAwaiter implements NovaAwaitable, NovaAwaitableContainer {
	private Plugin plugin;
	private List<NovaAwaitable> awaitables;
	private Runnable callback;
	private SimpleTask task;
	private boolean finished;

	public NovaAwaiter(Plugin plugin, Runnable callback, NovaAwaitable... awaitables) {
		this.plugin = plugin;
		this.callback = callback;
		this.awaitables = new ArrayList<>();
		this.finished = false;
		this.awaitables.addAll(Arrays.asList(awaitables));
		setupTask();
	}

	public NovaAwaiter(Plugin plugin, Runnable callback, List<NovaAwaitable> awaitables) {
		this.plugin = plugin;
		this.callback = callback;
		this.awaitables = new ArrayList<>(awaitables);
		setupTask();
	}

	private void setupTask() {
		task = new SimpleTask(plugin, () -> {
			if (awaitables.stream().allMatch(NovaAwaitable::isFininshed)) {
				Task.tryStopTask(task);
				finished = true;
				callback.run();
			}
		}, 1L);
		task.setTaskExecutionMode(TaskExecutionMode.SYNCHRONOUS);
	}

	public List<NovaAwaitable> getAwaitables() {
		return awaitables;
	}

	public void addAwaitable(NovaAwaitable awaitable) {
		awaitables.add(awaitable);
	}

	public boolean begin() {
		if (finished) {
			return false;
		}
		Task.tryStartTask(task);
		return true;
	}

	/**
	 * Cancel the awaiter. This will also flag this awaiter as finished
	 * @return <code>true</code> if canceled, <code>false</code> if already finished
	 */
	public boolean cancel() {
		if (finished) {
			return false;
		}
		Task.tryStopTask(task);
		finished = true;
		return true;
	}

	@Override
	public boolean isFininshed() {
		return finished;
	}
}