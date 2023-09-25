package net.zeeraa.novacore.spigot.abstraction.enums;

public enum NovaCoreGameVersion {
	V_1_8(100),
	V_1_12(1000),
	V_1_16(2000),
	/**
	 * Deprecated: This version of NovaCore is no longer supported
	 */
	@Deprecated
	V_1_17(2100),
	V_1_18(2200),
	V_1_18_R2(2300),
	V_1_19_R1(2400),
	V_1_19_R2(2500),
	V_1_19_R3(2600),
	V_1_20_R1(3000);

	private int versionId;

	private NovaCoreGameVersion(int versionId) {
		this.versionId = versionId;
	}

	public int getVersionId() {
		return versionId;
	}

	public boolean isBefore(NovaCoreGameVersion version) {
		return this.getVersionId() < version.getVersionId();
	}

	public boolean isBeforeOrEqual(NovaCoreGameVersion version) {
		return this.getVersionId() <= version.getVersionId();
	}

	public boolean isAfter(NovaCoreGameVersion version) {
		return this.getVersionId() > version.getVersionId();
	}

	public boolean isAfterOrEqual(NovaCoreGameVersion version) {
		return this.getVersionId() >= version.getVersionId();
	}

	public boolean matchesAny(NovaCoreGameVersion... versions) {
		for (NovaCoreGameVersion version : versions) {
			if (version.getVersionId() == this.versionId) {
				return true;
			}
		}
		return false;
	}

	public boolean hasOffhand() {
		return this != V_1_8;
	}
}