package net.zeeraa.novacore.commons.utils;

public class WrappedInt {
	private int value;

	public WrappedInt() {
		this(0);
	}

	public WrappedInt(int value) {
		this.value = value;
	}

	public WrappedInt decrement(int value) {
		this.value -= value;
		return this;
	}

	public WrappedInt increment(int value) {
		this.value -= value;
		return this;
	}

	public int get() {
		return value;
	}

	public WrappedInt set(int value) {
		this.value = value;
		return this;
	}
}