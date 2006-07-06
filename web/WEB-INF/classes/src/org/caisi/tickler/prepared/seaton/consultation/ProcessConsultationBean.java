package org.caisi.tickler.prepared.seaton.consultation;

public class ProcessConsultationBean {
	private String demographic_no;
	private String demographic_name;
	private String id;
	private String action;
	private String method;
	
	public ProcessConsultationBean() {
		setDemographic_no("");
		setDemographic_name("");
		setId("");
		setAction("");
		setMethod("prepared_tickler_edit");
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getDemographic_name() {
		return demographic_name;
	}
	public void setDemographic_name(String demographic_name) {
		this.demographic_name = demographic_name;
	}
	public String getDemographic_no() {
		return demographic_no;
	}
	public void setDemographic_no(String demographic_no) {
		this.demographic_no = demographic_no;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
}
