package net.zeeraa.novacore.commons.utils;

import java.util.ArrayList;
import java.util.List;

public class Pair<T> {
	private T object1;
	private T object2;

	public Pair(T object1, T object2) {
		this.object1 = object1;
		this.object2 = object2;
	}

	public T getObject1() {
		return object1;
	}

	public T getObject2() {
		return object2;
	}

	public void setObject1(T object1) {
		this.object1 = object1;
	}

	public void setObject2(T object2) {
		this.object2 = object2;
	}

	public List<T> toList() {
		List<T> result = new ArrayList<T>();
		result.add(object1);
		result.add(object2);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pair) {
			Pair pair2 = (Pair) obj;

			if (this.object1.equals(pair2.getObject1()) && this.object2.equals(pair2.getObject2())) {
				return true;
			}
		}

		return super.equals(obj);
	}
}