package net.zeeraa.novacore.commons.utils;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;

/**
 * A object wrapper that keeps track of time until the object should expire
 * 
 * @author Zeeraa
 *
 * @param <T> The object type to use
 */
public class ExpiringObjectWrapper<T> {
	protected T object;
	@Nonnull
	protected LocalDateTime expiresAt;
	@Nonnull
	protected ComparisonMode comparisonMode;

	/**
	 * Create a {@link ExpiringObjectWrapper}
	 * 
	 * @param object         The object to keep in the wrapper
	 * @param expiresAt      The {@link LocalDateTime} when it expires
	 * @param comparisonMode The {@link ComparisonMode} to use. See documentation
	 *                       for {@link ComparisonMode} to see the differences
	 */
	public ExpiringObjectWrapper(T object, @Nonnull LocalDateTime expiresAt, @Nonnull ComparisonMode comparisonMode) {
		if (comparisonMode == null) {
			throw new IllegalArgumentException("comparisonMode cant be null");
		}

		if (expiresAt == null) {
			throw new IllegalArgumentException("expiresAt cant be null");
		}

		this.object = object;
		this.expiresAt = expiresAt;
		this.comparisonMode = comparisonMode;
	}

	/**
	 * Create a {@link ExpiringObjectWrapper} with the default
	 * {@link ComparisonMode} of {@link ComparisonMode#CONTENT}
	 * 
	 * @param object    The object to keep in the wrapper
	 * @param expiresAt The {@link LocalDateTime} when it expires
	 */
	public ExpiringObjectWrapper(T object, LocalDateTime expiresAt) {
		this(object, expiresAt, ComparisonMode.CONTENT);
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		if (expiresAt == null) {
			throw new IllegalArgumentException("expiresAt cant be null");
		}
		this.expiresAt = expiresAt;
	}

	public boolean hasExpired() {
		return LocalDateTime.now().isAfter(expiresAt);
	}

	public ComparisonMode getComparisonMode() {
		return comparisonMode;
	}

	public void setComparisonMode(ComparisonMode comparisonMode) {
		if (comparisonMode == null) {
			throw new IllegalArgumentException("comparisonMode cant be null");
		}
		this.comparisonMode = comparisonMode;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		switch (comparisonMode) {
		case CONTENT:
			if (obj instanceof ExpiringObjectWrapper) {
				return object.equals(((ExpiringObjectWrapper) obj).getObject());
			}
			return object.equals(obj);

		case INSTANCE:
			return obj.equals(this);

		default:
			return obj.equals(this);
		}
	}

	public static ExpiryTimeBuilder timeBuilder() {
		return new ExpiryTimeBuilder();
	}
}

class ExpiryTimeBuilder {
	protected LocalDateTime time;

	public ExpiryTimeBuilder() {
		time = LocalDateTime.now();
	}

	public ExpiryTimeBuilder plusNanos(long nanoseconds) {
		time.plusNanos(nanoseconds);
		return this;
	}

	public ExpiryTimeBuilder plusSeconds(long seconds) {
		time.plusSeconds(seconds);
		return this;
	}

	public ExpiryTimeBuilder plusMinutes(long minutes) {
		time.plusMinutes(minutes);
		return this;
	}

	public ExpiryTimeBuilder plusHours(long hours) {
		time.plusHours(hours);
		return this;
	}

	public ExpiryTimeBuilder plusDays(long days) {
		time.plusDays(days);
		return this;
	}

	public ExpiryTimeBuilder plusWeeks(long weeks) {
		time.plusWeeks(weeks);
		return this;
	}

	public ExpiryTimeBuilder plusYears(long years) {
		time.plusYears(years);
		return this;
	}

	public LocalDateTime build() {
		return time;
	}
}

/**
 * The way the {@link ExpiringObjectWrapper} should handle the
 * {@link ExpiringObjectWrapper#equals(Object)} function
 * 
 * @author Zeeraa
 */
enum ComparisonMode {
	/**
	 * Checks if its the same wrapper object
	 */
	INSTANCE,
	/**
	 * Check if its the same object contained in the wrapper
	 */
	CONTENT;
}