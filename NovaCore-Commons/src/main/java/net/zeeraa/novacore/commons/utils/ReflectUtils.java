package net.zeeraa.novacore.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Class with utils for java.lang.reflect
 */
public class ReflectUtils {
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