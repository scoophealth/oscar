package org.oscarehr.survey.service;

import java.io.OutputStream;
import java.util.List;

public interface OscarFormManager {

	public List getForms();
	public void generateCSV(Long formId, OutputStream out);
	public void convertFormXMLToDb(Long formId);
}
