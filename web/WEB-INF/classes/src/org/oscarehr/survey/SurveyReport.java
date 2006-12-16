package org.oscarehr.survey;

import java.util.ArrayList;
import java.util.List;

public class SurveyReport {

	public static final int OPERATION_TOTAL = 1;
	public static final int OPERATION_AVERAGE = 2;
	
	private List entries = new ArrayList();

	public SurveyReport() {
		
	}
	
	public List getEntries() {
		return entries;
	}

	public void setEntries(List entries) {
		this.entries = entries;
	}
	
	
}
