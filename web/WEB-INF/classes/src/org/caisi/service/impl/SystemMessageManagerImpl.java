package org.caisi.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.dao.SystemMessageDAO;
import org.caisi.model.SystemMessage;
import org.caisi.service.SystemMessageManager;

public class SystemMessageManagerImpl implements SystemMessageManager {

	private static Log log = LogFactory.getLog(SystemMessageManagerImpl.class);
	
	
	private SystemMessageDAO dao = null;
	
	public SystemMessage getMessage(String messageId) {
		return dao.getMessage(Long.valueOf(messageId));
	}
	public void setSystemMessageDAO(SystemMessageDAO dao) {
		this.dao = dao;
	}
	public void saveSystemMessage(SystemMessage msg) {
		dao.saveMessage(msg);
	}
	
	public List getMessages() {
		return dao.getMessages();
	}

}
