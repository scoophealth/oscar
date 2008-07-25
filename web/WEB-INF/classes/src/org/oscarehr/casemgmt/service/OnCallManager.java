package org.oscarehr.casemgmt.service;

import org.oscarehr.casemgmt.dao.OnCallDAO;
import org.oscarehr.casemgmt.model.OnCallQuestionnaire;

public class OnCallManager {

	private OnCallDAO dao;
	
	public void setOnCallDao(OnCallDAO dao) {
		this.dao = dao;
	}
	
	public void save(OnCallQuestionnaire bean) {
		dao.save(bean);
	}
}
