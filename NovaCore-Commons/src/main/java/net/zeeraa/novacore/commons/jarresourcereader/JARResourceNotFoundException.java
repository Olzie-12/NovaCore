package net.zeeraa.novacore.commons.jarresourcereader;

public class JARResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -5796842609455596965L;

	public JARResourceNotFoundException(String message) {
		super(message);
	}
}