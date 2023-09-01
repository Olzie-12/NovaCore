package net.zeeraa.novacore.commons.jarresourcereader;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class JARResourceReader {
	private static Map<String, String> cache = new HashMap<>();

	public static void clearCache() {
		cache.clear();
	}

	public static String readFileFromJARAsString(@SuppressWarnings("rawtypes") Class owner, String resourcePath) {
		String key = owner.getName() + ":" + resourcePath;
		if (cache.containsKey(key)) {
			return cache.get(key);
		}

		try {
			InputStream stream = owner.getResourceAsStream(resourcePath);
			String content = IOUtils.toString(stream, StandardCharsets.UTF_8);
			stream.close();
			
			cache.put(key, content);

			return content;
		} catch (Exception e) {
			throw new JARResourceNotFoundException("Could not find jar resource at path " + resourcePath);
		}
	}
}