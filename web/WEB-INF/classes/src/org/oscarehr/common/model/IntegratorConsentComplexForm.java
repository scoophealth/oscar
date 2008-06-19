package org.oscarehr.common.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class IntegratorConsentComplexForm
{

	@Id
	private int integratorConsentId = 0;
	private String form = null;
	private String printedFormLocation = null;
	private boolean refusedToSign = false;

	public int getIntegratorConsentId()
	{
		return integratorConsentId;
	}

	public void setIntegratorConsentId(int integratorConsentId)
	{
		this.integratorConsentId = integratorConsentId;
	}

	public String getForm()
	{
		return form;
	}

	public void setForm(String form)
	{
		this.form = form;
	}

	public String getPrintedFormLocation()
	{
		return printedFormLocation;
	}

	public void setPrintedFormLocation(String printedFormLocation)
	{
		this.printedFormLocation = printedFormLocation;
	}

	public boolean isRefusedToSign()
	{
		return refusedToSign;
	}

	public void setRefusedToSign(boolean refusedToSign)
	{
		this.refusedToSign = refusedToSign;
	}

}
