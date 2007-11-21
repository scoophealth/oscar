package org.oscarehr.survey.service.impl;

import java.io.OutputStream;
import java.util.List;

import org.oscarehr.survey.dao.oscar.OscarFormDAO;
import org.oscarehr.survey.service.OscarFormManager;

public class OscarFormManagerImpl implements OscarFormManager {

	private OscarFormDAO dao;
	
	public void setOscarFormDAO(OscarFormDAO dao) {
		this.dao = dao;
	}
	
	public List getForms() {
		return dao.getOscarForms();
	}
	
	public void generateCSV(Long formId, OutputStream out) {
		dao.generateCSV(formId, out);
	}

}
