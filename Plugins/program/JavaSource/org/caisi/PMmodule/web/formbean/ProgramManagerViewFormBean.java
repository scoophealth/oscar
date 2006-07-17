package org.caisi.PMmodule.web.formbean;

public class ProgramManagerViewFormBean {
	private String tab;
	public static final String[] tabs = {"General","Staff","Function User","Teams","Clients","Queue","Access","Bed Log"};
	private String clientId;
	
	private String bedLogHour;
	private String bedLogMinute;
	private String bedLogAmPm;
	private String bedLogStatus;
	private String bedLogTime;
	
	
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
	/**
	 * @return Returns the clientId.
	 */
	public String getClientId() {
		return clientId;
	}
	/**
	 * @param clientId The clientId to set.
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getBedLogAmPm() {
		return bedLogAmPm;
	}
	public void setBedLogAmPm(String bedLogAmPm) {
		this.bedLogAmPm = bedLogAmPm;
	}
	public String getBedLogHour() {
		return bedLogHour;
	}
	public void setBedLogHour(String bedLogHour) {
		this.bedLogHour = bedLogHour;
	}
	public String getBedLogMinute() {
		return bedLogMinute;
	}
	public void setBedLogMinute(String bedLogMinute) {
		this.bedLogMinute = bedLogMinute;
	}
	public String getBedLogStatus() {
		return bedLogStatus;
	}
	public void setBedLogStatus(String bedLogStatus) {
		this.bedLogStatus = bedLogStatus;
	}
	public String getBedLogTime() {
		return bedLogTime;
	}
	public void setBedLogTime(String bedLogTime) {
		this.bedLogTime = bedLogTime;
	}
	
}
