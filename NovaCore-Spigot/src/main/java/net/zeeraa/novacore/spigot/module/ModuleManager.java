package net.zeeraa.novacore.spigot.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.Plugin;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.commons.utils.ClassFinder;
import net.zeeraa.novacore.spigot.module.annotations.EssentialModule;
import net.zeeraa.novacore.spigot.module.annotations.NovaAutoLoad;

/**
 * This class is used to register, enable and disable {@link NovaModule}s
 * 
 * @since 1.0
 * @author Zeeraa
 */
public class ModuleManager {
	private static Map<String, NovaModule> modules = new HashMap<>();

	/**
	 * Get a {@link Map} containing all modules with the module class name as key
	 * and the module as value
	 * 
	 * @since 1.0
	 * @return {@link Map} containing all modules
	 */
	public static Map<String, NovaModule> getModules() {
		return modules;
	}

	/**
	 * Get a loaded module
	 * 
	 * @since 1.0
	 * @param clazz The class of the module
	 * @param <T>   The class type to return
	 * @return {@link NovaModule} or null if not loaded
	 */
	@SuppressWarnings("unchecked")
	public static <T extends NovaModule> T getModule(Class<T> clazz) {
		new HashMap<Class<? extends NovaModule>, NovaModule>();
		return (T) modules.get(clazz.getName());
	}

	/**
	 * Get a loaded module
	 *
	 * @since 1.0
	 * @param className The class name of the module
	 * @return {@link NovaModule} or null if not loaded
	 */
	public static NovaModule getModule(String className) {
		return modules.get(className);
	}

	/**
	 * Check if a module is enabled
	 * 
	 * @since 1.0
	 * @param clazz The class of the module
	 * @return <code>true</code> if the module is disabled
	 */
	public static boolean isEnabled(Class<? extends NovaModule> clazz) {
		return isEnabled(clazz.getName());
	}

	/**
	 * Check if a module is enabled
	 * 
	 * @since 1.0
	 * @param className The class name of the module
	 * @return <code>true</code> if the module is enabled
	 */
	public static boolean isEnabled(String className) {
		NovaModule module = getModule(className);
		if (module != null) {
			return module.isEnabled();
		}
		return false;
	}

	/**
	 * Check if a module is disabled
	 * 
	 * @since 1.0
	 * @param clazz The class of the module
	 * @return <code>true</code> if the module is disabled
	 */
	public static boolean isDisabled(Class<? extends NovaModule> clazz) {
		return !isEnabled(clazz);
	}

	/**
	 * Check if a module is disabled
	 * 
	 * @since 1.0
	 * @param className The class name of the module
	 * @return <code>true</code> if the module is disabled
	 */
	public static boolean isDisabled(String className) {
		return !isEnabled(className);
	}

	/**
	 * Enable a module
	 * 
	 * @since 1.0
	 * @param clazz The class of the module
	 * @return <code>false</code> if {@link NovaModule#onEnable()} generated a
	 *         exception or if the module failed to enable
	 */
	public static boolean enable(Class<? extends NovaModule> clazz) {
		return enable(clazz.getName());
	}

	/**
	 * Enable a module
	 * 
	 * @since 1.0
	 * @param className The class name of the module
	 * @return <code>false</code> if {@link NovaModule#onEnable()} generated a
	 *         exception or if the module failed to enable
	 */
	public static boolean enable(String className) {
		NovaModule module = getModule(className);
		if (module != null) {
			return module.enable();
		}
		return false;
	}

	/**
	 * Reloads a module
	 * 
	 * @since 2.0.0
	 * @param clazz The class of the module
	 * @return <code>true</code> on success, false if module is not enabled or if
	 *         the enable function return an error
	 */
	public static boolean reload(Class<? extends NovaModule> clazz) {
		return reload(clazz.getName());
	}

	/**
	 * Reloads a module
	 * 
	 * @since 2.0.0
	 * @param className The class name of the module
	 * @return <code>true</code> on success, false if module is not enabled or if
	 *         the enable function return an error
	 */
	public static boolean reload(String className) {
		NovaModule module = getModule(className);
		if (module != null) {
			return module.reload();
		}
		return false;
	}

	/**
	 * Disable a module
	 * 
	 * @since 1.0
	 * @param clazz The class of the module
	 * @return <code>false</code> if {@link NovaModule#onDisable()} generated a
	 *         exception or if the module failed to disable
	 */
	public static boolean disable(Class<? extends NovaModule> clazz) {
		return disable(clazz.getName());
	}

	/**
	 * Disable a module
	 * 
	 * @since 1.0
	 * @param className The class name of the module
	 * @return <code>false</code> if {@link NovaModule#onDisable()} generated a
	 *         exception or if the module failed to disable
	 */
	public static boolean disable(String className) {
		NovaModule module = getModule(className);
		if (module != null) {
			return module.disable();
		}
		return false;
	}

	/**
	 * Get the reason why a module failed to load
	 * 
	 * @since 1.0
	 * @param clazz The class of the module
	 * @return The {@link ModuleEnableFailureReason} or <code>null</code> if the
	 *         module did not fail or was disabled
	 */
	public static ModuleEnableFailureReason getEnableFailureReason(Class<? extends NovaModule> clazz) {
		return getEnableFailureReason(clazz.getName());
	}

	/**
	 * Get the reason why a module failed to load
	 * 
	 * @since 1.0
	 * @param className The class name of the module
	 * @return The {@link ModuleEnableFailureReason} or <code>null</code> if the
	 *         module did not fail or was disabled
	 */
	public static ModuleEnableFailureReason getEnableFailureReason(String className) {
		NovaModule module = getModule(className);
		if (module != null) {
			return module.getEnableFailureReason();
		}
		return null;
	}

	/**
	 * Check if a module has been loaded
	 * 
	 * @since 1.0
	 * @param clazz The class of the module
	 * @return <code>true</code> if the module has been loaded
	 */
	public static boolean moduleExists(Class<? extends NovaModule> clazz) {
		return moduleExists(clazz.getName());
	}

	/**
	 * Check if a module has been loaded
	 * 
	 * @since 1.0
	 * @param className The class name of the module
	 * @return <code>true</code> if the module has been loaded
	 */
	public static boolean moduleExists(String className) {
		return modules.containsKey(className);
	}

	/**
	 * Load a module
	 * 
	 * @since 1.0
	 * @param owner The {@link Plugin} the module belongs to
	 * @param clazz The class of the module
	 * @return <code>true</code> on success
	 * 
	 * @throws InvalidModuleNameException if the module name contains spaces
	 */
	public static boolean loadModule(Plugin owner, Class<? extends NovaModule> clazz) {
		return loadModule(owner, clazz, false);
	}

	/**
	 * Load a module
	 * 
	 * @since 1.0
	 * @param owner  The {@link Plugin} the module belongs to
	 * @param clazz  The class of the module
	 * @param enable set to <code>true</code> to enable the module on load
	 * @return <code>true</code> on success
	 * 
	 * @throws InvalidModuleNameException if the module name contains spaces
	 */
	public static boolean loadModule(Plugin owner, Class<? extends NovaModule> clazz, boolean enable) {
		if (moduleExists(clazz)) {
			Log.warn("ModuleManager", "Module " + clazz.getName() + " was already loaded");
			return false;
		}
		Log.info("ModuleManager", "Loading module " + clazz.getName());
		try {
			Object module = clazz.getConstructor().newInstance(new Object[] {});

			if (module instanceof NovaModule) {
				if (((NovaModule) module).getName().trim().length() == 0) {
					Log.error("ModuleManager", "The module name can't be empty. Module class: " + module.getClass().getName());
					throw new InvalidModuleNameException("The module name can't be empty. Module class: " + module.getClass().getName());
				}

				if (((NovaModule) module).getName().contains(" ")) {
					Log.error("ModuleManager", "The module name can't contain spaces. Module class: " + module.getClass().getName());
					throw new InvalidModuleNameException("The module name can't contain spaces. Module class: " + module.getClass().getName());
				}

				NovaModule mod = ((NovaModule) module);

				mod.setPlugin(owner);
				mod.onLoad();
				modules.put(mod.getClassName(), mod);

				if (enable) {
					enable(clazz);
				}

				return true;
			} else {
				Log.error("ModuleManager", "Module " + clazz.getName() + " does not extend EZModule");
				return false;
			}
		} catch (Exception e) {
			Log.error("ModuleManager", "An exception occured while loading " + clazz.getName());
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Removes and disables all modules owned by the specified {@link Plugin}
	 * 
	 * @param plugin The {@link Plugin} to remove modules from
	 */
	public static void removePluginModules(Plugin plugin) {
		List<NovaModule> toRemove = modules.values().stream().filter(m -> m.getPlugin().equals(plugin)).collect(Collectors.toList());
		toRemove.stream().filter(NovaModule::isEnabled).forEach(NovaModule::disable);
		toRemove.forEach(module -> {
			String className = module.getClass().getName();
			Log.trace("ModuleManager", "Removing " + className + " from module list");
			modules.remove(className);
		});
	}

	/**
	 * Try to disable all modules that belongs to a specific plugin
	 * 
	 * @param plugin The {@link Plugin} to disabled modules from
	 * 
	 * @since 2.0.0
	 */
	public static void disableAll(Plugin plugin) {
		Log.info("ModuleManager", "Disabling all modules from plugin " + plugin.getName());
		modules.values().stream().filter(m -> m.getPlugin().equals(plugin)).filter(NovaModule::isEnabled).forEach(NovaModule::disable);
	}

	/**
	 * Try to disable all modules
	 * 
	 * @since 1.0
	 */
	public static void disableAll() {
		Log.info("ModuleManager", "Disabling all modules");
		modules.values().stream().filter(NovaModule::isEnabled).forEach(NovaModule::disable);
	}

	/**
	 * Enable module if not already loaded
	 * 
	 * @since 1.0
	 * @param clazz The class of the module to enable
	 * @return <code>true</code> if successful or the module is already enabled.
	 *         <code>false</code> on error
	 */
	public static boolean require(Class<? extends NovaModule> clazz) {
		if (!isEnabled(clazz)) {
			return enable(clazz);
		}
		return true;
	}

	/**
	 * Scan for modules in a plugin and load them if they have the
	 * {@link NovaAutoLoad} annotation
	 * 
	 * @param plugin        The plugin that owns the package
	 * @param packageToScan The package to scan in
	 * @since 1.1
	 */
	@SuppressWarnings("unchecked")
	public static void scanForModules(Plugin plugin, String packageToScan) {
		Log.info("ModuleManager", "Scanning for nova modules in package " + packageToScan + " of plugin " + plugin.getName());

		Set<Class<?>> classes = ClassFinder.getClasses(FileUtils.toFile(plugin.getClass().getProtectionDomain().getCodeSource().getLocation()), packageToScan);
		for (Class<?> clazz : classes) {

			// Ignore anonymous classes
			if (clazz.getName().contains("$")) {
				continue;
			}

			if (NovaModule.class.isAssignableFrom(clazz)) {
				NovaAutoLoad autoLoadAnnotation = clazz.getAnnotation(NovaAutoLoad.class);

				if (autoLoadAnnotation != null) {
					Log.info("ModuleManager", "Scanner found class " + clazz.getName());
					boolean enable = autoLoadAnnotation.shouldEnable();

					ModuleManager.loadModule(plugin, (Class<? extends NovaModule>) clazz, enable);
				}
			}
		}
	}

	/**
	 * Check if the module has the {@link EssentialModule} annotation
	 * 
	 * @param module The {@link NovaModule} to check
	 * @return <code>true</code> if the module has the {@link EssentialModule}
	 *         annotation
	 */
	public static boolean isEssential(NovaModule module) {
		return ModuleManager.isEssential(module.getClass());
	}

	/**
	 * Check if the module has the {@link EssentialModule} annotation
	 * 
	 * @param clazz The module class to check
	 * @return <code>true</code> if the module has the {@link EssentialModule}
	 *         annotation
	 */
	public static boolean isEssential(Class<? extends NovaModule> clazz) {
		EssentialModule essentialModuleAnnotation = clazz.getAnnotation(EssentialModule.class);
		return essentialModuleAnnotation != null;
	}
}