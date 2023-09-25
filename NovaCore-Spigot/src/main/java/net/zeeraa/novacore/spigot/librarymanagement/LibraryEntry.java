package net.zeeraa.novacore.spigot.librarymanagement;

public class LibraryEntry {
private String className;
private String libraryName;

public LibraryEntry(String className, String libraryName) {
	this.className = className;
	this.libraryName = libraryName;
}

public String getClassName() {
	return className;
}

public String getLibraryName() {
	return libraryName;
}
}