package net.zeeraa.novacore.spigot.abstraction.commons;

public class EntityBoundingBox {
	private final float height;
	private final float width;

	public EntityBoundingBox(float height, float width) {
		this.height = height;
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}
}