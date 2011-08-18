/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.service;

import org.apache.log4j.Logger;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

/**
 * @author apavel
 */
public class DSServiceThread extends Thread {
	private static final Logger logger = MiscUtils.getLogger();

	private DSService service;
	private String providerNo;

	public DSServiceThread(DSService service, String providerNo) {
		this.service = service;
		this.providerNo = providerNo;
	}

	@Override
	public void run() {
		try {
			service.fetchGuidelinesFromService(providerNo);
		} catch (Exception e) {
			logger.error("Error", e);
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}
}
