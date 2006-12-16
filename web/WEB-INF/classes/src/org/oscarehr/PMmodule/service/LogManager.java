package org.oscarehr.PMmodule.service;

public interface LogManager {
	public void log(String providerNo, String accessType, String entity, String entityId,String ip);
}
