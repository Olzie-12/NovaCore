package net.zeeraa.novacore.commons.utils;

import java.util.Collection;
import java.util.Map;

import org.json.JSONObject;

public class JSONObjectBuilder {
	protected JSONObject obj;

	public JSONObjectBuilder() {
		this(new JSONObject());
	}

	public JSONObjectBuilder(JSONObject obj) {
		this(obj, false);
	}

	public JSONObjectBuilder(JSONObject obj, boolean clone) {
		this.obj = clone ? new JSONObject(obj.toString()) : obj;
	}

	public void put(String key, boolean value) {
		obj.put(key, value);
	}

	public void put(String key, Collection<?> value) {
		obj.put(key, value);
	}

	public void put(String key, double value) {
		obj.put(key, value);
	}

	public void put(String key, float value) {
		obj.put(key, value);
	}

	public void put(String key, int value) {
		obj.put(key, value);
	}

	public void put(String key, long value) {
		obj.put(key, value);
	}

	public void put(String key, Map<?, ?> value) {
		obj.put(key, value);
	}

	public void put(String key, Object value) {
		obj.put(key, value);
	}

	public JSONObject build() {
		return this.obj;
	}
}