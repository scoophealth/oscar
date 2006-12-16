package org.oscarehr.PMmodule.web.formbean;

public class IntakeCFormBean {
	private String tab;
	public static final String[] tabs = {"Step 1","Step 2","Step 3","Step 4","Step 5","Step 6"};
	public static final String[] tabDescr = {"Identifying  Information","Presenting and  Referral Information","Legal and  ID Status","Medical Status","Social and Living  Arrangements","Employment, Education  and Income Status"};
	private int numPastAddresses;
	private int numContacts;
	private int numIdentification;
	private long admissionProgram;
	private int numHospitalization;
	private final String version = "1.1.0";
	
	public String getVersion() {
		return version;
	}
	public IntakeCFormBean() {
		numContacts = 1;
		numIdentification = 1;
		numHospitalization=1;
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
	public int getNumPastAddresses() {
		return numPastAddresses;
	}
	public void setNumPastAddresses(int numPastAddresses) {
		this.numPastAddresses = numPastAddresses;
	}
	public int getNumContacts() {
		return numContacts;
	}
	public void setNumContacts(int numContacts) {
		this.numContacts = numContacts;
	}
	public int getNumIdentification() {
		return numIdentification;
	}
	public void setNumIdentification(int numIdentification) {
		this.numIdentification = numIdentification;
	}
	public static String[] getTabDescr() {
		return tabDescr;
	}
	public long getAdmissionProgram() {
		return admissionProgram;
	}
	public void setAdmissionProgram(long admissionProgram) {
		this.admissionProgram = admissionProgram;
	}
	public int getNumHospitalization() {
		return numHospitalization;
	}
	public void setNumHospitalization(int numHospitalization) {
		this.numHospitalization = numHospitalization;
	}
}
