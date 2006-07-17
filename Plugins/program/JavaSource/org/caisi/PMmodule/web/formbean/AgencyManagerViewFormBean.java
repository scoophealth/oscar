package org.caisi.PMmodule.web.formbean;

public class AgencyManagerViewFormBean {
	private String tab;
	public static final String[] tabs = {"General","Integrator","Community"};
	
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
