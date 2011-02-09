package org.oscarehr.PMmodule.web.reports.custom;

import org.apache.struts.util.LabelValueBean;

public interface CustomReportDataSource {

	public LabelValueBean[] getFormNames();
	
	public LabelValueBean[] getItems(String formId);
	
	public Item getItem(String formId, String id);
}
