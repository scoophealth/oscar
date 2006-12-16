package org.oscarehr.PMmodule.service.impl;

import java.util.Date;

import org.oscarehr.PMmodule.dao.LogDAO;
import org.oscarehr.PMmodule.model.Log;
import org.oscarehr.PMmodule.service.LogManager;


public class LogManagerImpl implements LogManager {

	private LogDAO logDAO;
	
	public void setLogDAO(LogDAO dao) {
		this.logDAO = dao;
	}
	
	public void log(String providerNo, String accessType, String entity,
			String entityId, String ip) {
		
		Log log = new Log();
		//log.setId(new LogPK(providerNo,new Date()));
		log.setProviderNo(providerNo);
		log.setDateTime(new Date());
		log.setAction(accessType);
		log.setContent(entity);
		log.setContentId(entityId);
		log.setIp(ip);
	
		logDAO.saveLog(log);

	}

}
