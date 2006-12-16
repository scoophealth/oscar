package org.oscarehr.PMmodule.service.impl;

import java.util.List;

import org.oscarehr.PMmodule.dao.FormsDAO;
import org.oscarehr.PMmodule.service.FormsManager;

public class FormsManagerImpl implements FormsManager {

	private FormsDAO formsDAO;
	
	public void setFormsDAO(FormsDAO dao) {
		this.formsDAO = dao;
	}
	public void saveForm(Object o) {
		formsDAO.saveForm(o);
	}

	public Object getCurrentForm(String clientId, Class clazz) {
		return formsDAO.getCurrentForm(clientId,clazz);
	}

	public List getFormInfo(String clientId,Class clazz) {
		return formsDAO.getFormInfo(clientId,clazz);
	}
}
