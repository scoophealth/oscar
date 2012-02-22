package org.oscarehr.ws.transfer_objects;

public final class LoginResultTransfer {
	private Integer securityId;
	private String securityTokenKey;

	public Integer getSecurityId() {
		return (securityId);
	}

	public void setSecurityId(Integer securityId) {
		this.securityId = securityId;
	}

	public String getSecurityTokenKey() {
		return (securityTokenKey);
	}

	public void setSecurityTokenKey(String securityTokenKey) {
		this.securityTokenKey = securityTokenKey;
	}
}
