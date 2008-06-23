package org.oscarehr.common.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class IntegratorConsentComplexForm {
	@EmbeddedId
	private FacilityDemographicPrimaryKey id;
	private String form = null;
	private String printedFormLocation = null;
	private boolean refusedToSign = false;

	public FacilityDemographicPrimaryKey getId() {
		return id;
	}

	public void setId(FacilityDemographicPrimaryKey id) {
		this.id = id;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getPrintedFormLocation() {
		return printedFormLocation;
	}

	public void setPrintedFormLocation(String printedFormLocation) {
		this.printedFormLocation = printedFormLocation;
	}

	public boolean isRefusedToSign() {
		return refusedToSign;
	}

	public void setRefusedToSign(boolean refusedToSign) {
		this.refusedToSign = refusedToSign;
	}

}
