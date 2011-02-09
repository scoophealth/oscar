package org.oscarehr.survey.service;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OscarFormManager {

	public List getForms();
	public void generateCSV(Long formId, OutputStream out);
	public void generateInverseCSV(Long formId, OutputStream out);
	public void convertFormXMLToDb(Long formId);
	public Map<String[],String> getFormReport(Long formId, Date startDate, Date endDate);
}
