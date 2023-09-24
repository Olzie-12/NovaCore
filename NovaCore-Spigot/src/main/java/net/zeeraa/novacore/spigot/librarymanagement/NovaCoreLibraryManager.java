package net.zeeraa.novacore.spigot.librarymanagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.bukkit.plugin.Plugin;

import net.zeeraa.novacore.commons.log.Log;

public class NovaCoreLibraryManager {
	private List<URLClassLoader> loaders = new ArrayList<>();

	private Plugin owner;
	private File libFolder;
	private List<String> blockedLibraries;
	private boolean verboseMode;
	private ClassLoader parentClassLoader;

	private Map<String, Class<?>> pluginClassLoaderClassMap;

	public void close() {
		loaders.forEach(t -> {
			try {
				t.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		loaders.clear();
	}

	@SuppressWarnings("unchecked")
	public NovaCoreLibraryManager(Plugin owner, File libFolder, List<String> blockedLibraries, boolean verboseMode, ClassLoader parentClassLoader) {
		this.owner = owner;
		this.libFolder = libFolder;
		this.blockedLibraries = blockedLibraries;
		this.verboseMode = verboseMode;
		this.parentClassLoader = parentClassLoader;

		if (verboseMode) {
			Log.trace("NovaCoreLibraryManager", "Enumerating fields in " + parentClassLoader.getClass().getName());
			for (Field field : parentClassLoader.getClass().getDeclaredFields()) {
				Log.trace("NovaCoreLibraryManager", "Detected field named: " + field.getName());
			}
		}

		if (parentClassLoader.getClass().getName().equalsIgnoreCase("org.bukkit.plugin.java.PluginClassLoader")) {
			Field field = null;
			try {
				field = parentClassLoader.getClass().getDeclaredField("classes");
			} catch (NoSuchFieldException e) {
				if (verboseMode) {
					Log.debug("NovaCoreLibraryManager", "Could not find field classes in " + parentClassLoader.getClass().getName());
				}
			}

			if (field == null) {
				throw new RuntimeException("Failed to start NovaCoreLibraryManager. " + parentClassLoader.getClass().getName() + " does not contain a field for classes");
			}
			field.setAccessible(true);
			try {
				pluginClassLoaderClassMap = (Map<String, Class<?>>) field.get(parentClassLoader);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new NestableRuntimeException("Failed to start NovaCoreLibraryManager. Could not set field " + field.getName() + " to accessible in " + parentClassLoader.getClass().getName(), e);
			}
		} else {
			throw new RuntimeException("Failed to start NovaCoreLibraryManager. The provided class loader of type " + parentClassLoader.getClass().getName() + " is not supported by novacore");
		}
	}

	public void extractLibrariesToDisk(String pathInJar) throws IOException {
		Files.createDirectories(Paths.get(libFolder.getAbsolutePath()));
		File file = new File(owner.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));

		JarFile jar = new JarFile(file);
		Enumeration<JarEntry> entries = jar.entries();

		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String entryName = entry.getName();
			if (entryName.startsWith(pathInJar + "/") && !entry.isDirectory()) {
				File output = new File(libFolder.getAbsolutePath() + File.separator + entryName.substring(pathInJar.length() + 1));
				if (!output.exists()) {
					Log.trace("NovaCoreLibraryManager", "Extracting lib " + pathInJar + " to " + output.getAbsolutePath());
					InputStream inputStream = owner.getClass().getClassLoader().getResourceAsStream(entryName);
					OutputStream outputStream = new FileOutputStream(output);
					byte[] buffer = new byte[8192];
					int bytesRead;
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}
					outputStream.close();
					inputStream.close();
				}
			}
		}
		jar.close();
	}

	public boolean loadIfClassIsMissing(String libraryName, String className) throws LibraryBlockedException, IOException {
		Log.debug("NovaCoreLibraryManager", "Checking if " + libraryName + " needs to be loaded (" + className + ")");
		try {
			Class.forName(className);
			if (verboseMode) {
				Log.debug("NovaCoreLibraryManager", "No need to load " + libraryName + " because " + className + " was already found");
			}
			return false; // Already loaded
		} catch (ClassNotFoundException e) {
		}

		String libraryFileNameNoExt = removeExtension(libraryName);
		File libraryFile = new File(libFolder.getAbsolutePath() + File.separator + libraryFileNameNoExt + ".jar");
		if (!libraryFile.exists()) {
			throw new FileNotFoundException("Cant find library " + libraryFile.getAbsolutePath());
		}

		if (blockedLibraries.contains(libraryFileNameNoExt.toLowerCase())) {
			throw new LibraryBlockedException("The library " + libraryName + " is blocked in config.yml");
		}

		loadJarFile(libraryFile);

		return true;
	}

	public void loadJarFile(File jarFile) throws IOException {
		URL jarUrl = jarFile.toURI().toURL();
		URLClassLoader classLoader = new URLClassLoader(new URL[] { jarUrl }, parentClassLoader);
		try (JarFile jar = new JarFile(jarFile)) {
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();

				if (entry.getName().endsWith("module-info.class")) {
					continue;
				}

				if (entry.getName().endsWith(".class")) {
					if (verboseMode) {
						Log.debug("NovaCoreLibraryManager", "Found class " + entry.getName());
					}
					String className = entry.getName().replace('/', '.').replaceAll(".class$", "");
					
					try {
						Class.forName(className);
						if (verboseMode) {
							Log.trace("NovaCoreLibraryManager", "Skipping class " + className + " since it was already found");
						}
						continue;
					} catch(ClassNotFoundException e) {
					}

					try {
						Class<?> clazz = classLoader.loadClass(className);
						if (!pluginClassLoaderClassMap.containsKey(clazz.getName())) {
							if (verboseMode) {
								Log.debug("NovaCoreLibraryManager", "Adding class " + clazz.getName() + " to plugin class loader");
							}
							pluginClassLoaderClassMap.put(clazz.getName(), clazz);
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		loaders.add(classLoader);
	}

	private String removeExtension(String filePath) {
		int lastDotIndex = filePath.lastIndexOf(".");
		if (lastDotIndex != -1) {
			return filePath.substring(0, lastDotIndex);
		}
		return filePath; // No extension found
	}
}