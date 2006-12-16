package org.oscarehr.PMmodule.web.formbean;

public class AgencyManagerViewFormBean {

	public static final String[] tabs = { "General", "Bed", "Integrator", "Community" };

	private String tab;

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

}