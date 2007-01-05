package org.oscarehr.PMmodule.web.formbean;

import org.apache.struts.action.ActionForm;
import org.oscarehr.PMmodule.model.Bed;

public class ProgramManagerViewFormBean extends ActionForm {

	private static final long serialVersionUID = 1L;

	public static final String[] tabs = { "General", "Staff", "Function User", "Teams", "Clients", "Queue", "Access", "Bed Check" };

	private String tab;
	private String clientId;
	private String queueId;
	private Bed[] reservedBeds;

	/**
	 * @return Returns the tab.
	 */
	public String getTab() {
		return tab;
	}
	
	/**
	 * @param tab
	 *            The tab to set.
	 */
	public void setTab(String tab) {
		this.tab = tab;
	}

	/**
	 * @return Returns the clientId.
	 */
	public String getClientId() {
		return clientId;
	}
	/**
	 * @param clientId
	 *            The clientId to set.
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getQueueId() {
		return queueId;
	}

	public void setQueueId(String queueId) {
		this.queueId = queueId;
	}

	public Bed[] getReservedBeds() {
    	return reservedBeds;
    }

	public void setReservedBeds(Bed[] reservedBeds) {
    	this.reservedBeds = reservedBeds;
    }

}