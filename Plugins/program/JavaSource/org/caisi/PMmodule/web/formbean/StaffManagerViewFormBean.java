package org.caisi.PMmodule.web.formbean;

public class StaffManagerViewFormBean {
	private String tab;
	public static final String[] tabs = {"Summary","Programs"};
	
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
