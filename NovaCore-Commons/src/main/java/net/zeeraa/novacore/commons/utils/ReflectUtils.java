package net.zeeraa.novacore.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Class with utils for java.lang.reflect
 */
public class ReflectUtils {
	private static final Map<Class<?>, Method> HandleCache = new HashMap<>();

	public static Object getHandle(Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Class<?> clazz = object.getClass();
		if (!HandleCache.containsKey(clazz)) {
			Method method = clazz.getDeclaredMethod("getHandle");
			method.setAccessible(true);
			HandleCache.put(clazz, method);
		}
		return HandleCache.get(clazz).invoke(object);
	}

	public static Object getPrivateField(String fieldName, Class<?> clazz, Object object) {
		Field field;
		Object o = null;
		try {
			field = clazz.getDeclaredField(fieldName);

			field.setAccessible(true);

			o = field.get(object);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return o;
	}

	public static boolean hasMethod(Class<?> clazz, String methodName, Class<?>... parameters) {
		Method methodToFind;
		try {
			methodToFind = clazz.getMethod(methodName, parameters);
		} catch (NoSuchMethodException | SecurityException e) {
			methodToFind = null;
			// gotta be sure you know
		}
		return methodToFind != null;
	}
}