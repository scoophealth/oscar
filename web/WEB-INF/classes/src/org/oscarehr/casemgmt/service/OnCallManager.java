package org.oscarehr.casemgmt.service;

import org.oscarehr.casemgmt.dao.OnCallDAO;
import org.oscarehr.casemgmt.model.OnCallQuestionnaire;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class OnCallManager {

	private OnCallDAO dao;
	
	public void setOnCallDao(OnCallDAO dao) {
		this.dao = dao;
	}
	
	public void save(OnCallQuestionnaire bean) {
		dao.save(bean);
	}
}
