package net.zeeraa.novacore.spigot.librarymanagement;

import javax.annotation.Nullable;

public class LibraryEntry {
	private String className;
	private String libraryName;
	@Nullable
	private LibraryLoadCondition loadCondition;

	public LibraryEntry(String className, String libraryName) {
		this(className, libraryName, null);
	}

	public LibraryEntry(String className, String libraryName, @Nullable LibraryLoadCondition loadCondition) {
		this.className = className;
		this.libraryName = libraryName;
		this.loadCondition = loadCondition;
	}

	public String getClassName() {
		return className;
	}

	public String getLibraryName() {
		return libraryName;
	}

	@Nullable
	public LibraryLoadCondition getLoadCondition() {
		return loadCondition;
	}
	
	public boolean shouldLoad() {
		if(loadCondition != null) {
			return loadCondition.shouldLoad();
		}
		return true;
	}
}