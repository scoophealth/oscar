package org.oscarehr.web.reports.ocan.beans;

import java.util.Map;

import org.oscarehr.common.model.OcanStaffForm;
import org.oscarehr.common.model.OcanStaffFormData;

public class CachedOcanFormAndData {
	private OcanStaffForm ocanForm;
	private Map<String,OcanStaffFormData> ocanFormDataMap;

	public CachedOcanFormAndData() {

	}

	public CachedOcanFormAndData(OcanStaffForm ocanForm, Map<String,OcanStaffFormData> ocanFormDataMap) {
		this.ocanForm = ocanForm;
		this.ocanFormDataMap = ocanFormDataMap;
	}

	public OcanStaffForm getOcanForm() {
		return ocanForm;
	}

	public void setOcanForm(OcanStaffForm ocanForm) {
		this.ocanForm = ocanForm;
	}

	public Map<String, OcanStaffFormData> getOcanFormDataMap() {
		return ocanFormDataMap;
	}

	public void setOcanFormDataMap(Map<String, OcanStaffFormData> ocanFormDataMap) {
		this.ocanFormDataMap = ocanFormDataMap;
	}


}
