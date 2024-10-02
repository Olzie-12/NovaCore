package net.zeeraa.novacore.spigot.utils;

import java.util.Base64;

import javax.annotation.Nonnull;

import org.json.JSONObject;

public class Base64Utils {
	public static final String getSkinUrlFromBase64(@Nonnull String base64) {
		String data = Base64Utils.base64ToString(base64);
		JSONObject json = new JSONObject(data);
		return json.getJSONObject("textures").getJSONObject("SKIN").getString("url");
	}

	public static final String stringToBase64(@Nonnull String input) {
		return Base64.getEncoder().encodeToString(input.getBytes());
	}

	public static final String base64ToString(@Nonnull String base64) {
		byte[] decodedBytes = Base64.getDecoder().decode(base64);
		return new String(decodedBytes);
	}
}