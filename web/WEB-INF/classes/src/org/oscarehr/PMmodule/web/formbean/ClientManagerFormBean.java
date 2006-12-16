package org.oscarehr.PMmodule.web.formbean;

public class ClientManagerFormBean {

	private String tab;

	public static final String[] tabs = { "Summary", "History", "Bed Reservation", "Forms", "Refer", "Discharge" };

	public ClientManagerFormBean() {
		setTab(tabs[0]);
	}

	/**
	 * @return Returns the tab.
	 */
	public String getTab() {
		return tab;
	}

	/**
	 * @param tab The tab to set.
	 */
	public void setTab(String tab) {
		this.tab = tab;
	}

}