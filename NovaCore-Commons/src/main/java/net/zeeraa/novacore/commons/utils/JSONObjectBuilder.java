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

	public JSONObjectBuilder put(String key, boolean value) {
		obj.put(key, value);
		return this;
	}

	public JSONObjectBuilder put(String key, Collection<?> value) {
		obj.put(key, value);
		return this;
	}

	public JSONObjectBuilder put(String key, double value) {
		obj.put(key, value);
		return this;
	}

	public JSONObjectBuilder put(String key, float value) {
		obj.put(key, value);
		return this;
	}

	public JSONObjectBuilder put(String key, int value) {
		obj.put(key, value);
		return this;
	}

	public JSONObjectBuilder put(String key, long value) {
		obj.put(key, value);
		return this;
	}

	public JSONObjectBuilder put(String key, Map<?, ?> value) {
		obj.put(key, value);
		return this;
	}

	public JSONObjectBuilder put(String key, Object value) {
		obj.put(key, value);
		return this;
	}

	public JSONObject build() {
		return this.obj;
	}
}