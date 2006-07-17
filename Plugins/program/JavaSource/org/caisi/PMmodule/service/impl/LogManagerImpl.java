package org.caisi.PMmodule.service.impl;

import java.util.Date;

import org.caisi.PMmodule.dao.LogDAO;
import org.caisi.PMmodule.model.Log;
import org.caisi.PMmodule.model.LogPK;
import org.caisi.PMmodule.service.LogManager;


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
