package org.caisi.PMmodule.service.impl;

public class BaseIntakeManager {

	private boolean enabled;
	private boolean newClientForm;
	
	
	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isNewClientForm() {
		return newClientForm;
	}

	public void setNewClientForm(boolean newClientForm) {
		this.newClientForm = newClientForm;
	}
	
}
