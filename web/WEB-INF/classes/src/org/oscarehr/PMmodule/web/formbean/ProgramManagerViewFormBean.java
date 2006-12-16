package org.oscarehr.PMmodule.web.formbean;

import org.apache.struts.action.ActionForm;

public class ProgramManagerViewFormBean extends ActionForm {
	
    private static final long serialVersionUID = 1L;

	public static final String[] tabs = { "General", "Staff", "Function User", "Teams", "Clients", "Queue", "Access", "Bed Check" };

	private String tab;

	private String clientId;

	private String queueId;

	private String bedId;
	
	/**
	 * @return Returns the clientId.
	 */
	public String getClientId() {
		return clientId;
	}

	public String getQueueId() {
		return queueId;
	}

	/**
	 * @return Returns the tab.
	 */
	public String getTab() {
		return tab;
	}

	/**
	 * @param clientId
	 *            The clientId to set.
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setQueueId(String queueId) {
		this.queueId = queueId;
	}

	/**
	 * @param tab
	 *            The tab to set.
	 */
	public void setTab(String tab) {
		this.tab = tab;
	}

	public String getBedId() {
    	return bedId;
    }

	public void setBedId(String bedId) {
    	this.bedId = bedId;
    }

}