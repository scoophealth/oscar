package org.oscarehr.PMmodule.web.formbean;

import org.apache.struts.action.ActionForm;

public class ProgramManagerViewFormBean extends ActionForm {

	private static final long serialVersionUID = 1L;

	public static final String[] tabs = { "General", "Staff", "Function User", "Teams", "Clients", "Queue", "Access", "Bed Check" };

	private String tab;
	private String bedId;
	private String clientId;
	private String queueId;

	/**
	 * @return Returns the tab.
	 */
	public String getTab() {
		return tab;
	}

	public String getBedId() {
		return bedId;
	}

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
	 * @param tab
	 *            The tab to set.
	 */
	public void setTab(String tab) {
		this.tab = tab;
	}

	public void setBedId(String bedId) {
		this.bedId = bedId;
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

}