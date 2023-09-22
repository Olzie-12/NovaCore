package net.zeeraa.novacore.spigot.librarymanagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.bukkit.plugin.Plugin;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.NovaCore;

public class NovaCoreLibraryManager {
	private static List<URLClassLoader> loaders = new ArrayList<>();

	public static void closeClassLoaders() {
		loaders.forEach(t -> {
			try {
				t.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		loaders.clear();
	}

	public static void extractLibrariesToDisk(Plugin owner, String pathInJar) throws IOException {
		Files.createDirectories(Paths.get(NovaCore.getInstance().getLibraryFolder().getAbsolutePath()));
		File file = new File(owner.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));

		JarFile jar = new JarFile(file);
		Enumeration<JarEntry> entries = jar.entries();

		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String entryName = entry.getName();
			if (entryName.startsWith(pathInJar + "/") && !entry.isDirectory()) {
				File output = new File(NovaCore.getInstance().getLibraryFolder().getAbsolutePath() + File.separator + entryName.substring(pathInJar.length() + 1));
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

	public static boolean loadIfClassIsMissing(String libraryName, String className) throws LibraryBlockedException, IOException {
		try {
			Class.forName(className);
			return false; // Already loaded
		} catch (ClassNotFoundException e) {
		}

		String libraryFileNameNoExt = removeExtension(libraryName);
		File libraryFile = new File(NovaCore.getInstance().getLibraryFolder().getAbsolutePath() + File.separator + libraryFileNameNoExt + ".jar");
		if (!libraryFile.exists()) {
			throw new FileNotFoundException("Cant find library " + libraryFile.getAbsolutePath());
		}

		if (NovaCore.getInstance().getBlockedLibraries().contains(libraryFileNameNoExt.toLowerCase())) {
			throw new LibraryBlockedException("The library " + libraryName + " is blocked in config.yml");
		}

		loadJarFile(libraryFile);

		return true;
	}

	private static URL getJarUrl(final File file) throws IOException {
		return new URL("jar:" + file.toURI().toURL().toExternalForm() + "!/");
	}

	private static void addClassPath(final URL url) throws IOException {
		final URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		final Class<URLClassLoader> sysclass = URLClassLoader.class;
		try {
			final Method method = sysclass.getDeclaredMethod("addURL",
					new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] { url });
		} catch (final Throwable t) {
			t.printStackTrace();
			throw new IOException("Error adding " + url + " to system classloader");
		}
	}

	public static void loadJarFile(File jarFile) throws IOException {
		URL jarUrl = jarFile.toURI().toURL();
		URLClassLoader classLoader = new URLClassLoader(new URL[] { jarUrl });
		try (JarFile jar = new JarFile(jarFile)) {
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();

				if (entry.getName().endsWith(".class")) {
					String className = entry.getName().replace('/', '.').replaceAll(".class$", "");
					try {
						classLoader.loadClass(className);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		loaders.add(classLoader);
	}

	private static String removeExtension(String filePath) {
		int lastDotIndex = filePath.lastIndexOf(".");
		if (lastDotIndex != -1) {
			return filePath.substring(0, lastDotIndex);
		}
		return filePath; // No extension found
	}
}