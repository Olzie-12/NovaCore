package net.zeeraa.novacore.spigot.utils.exceptions;

public class WorldNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -3597643265505898352L;

	public WorldNotFoundException() {
		super();
	}

	public WorldNotFoundException(String message) {
		super(message);
	}

	public WorldNotFoundException(Throwable cause) {
		super(cause);
	}

	public WorldNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}