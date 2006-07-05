package org.caisi.service;

import java.util.List;

import org.caisi.model.SystemMessage;

public interface SystemMessageManager {
	public SystemMessage getMessage(String messageId);
	public void saveSystemMessage(SystemMessage msg);
	public List getMessages();
}
