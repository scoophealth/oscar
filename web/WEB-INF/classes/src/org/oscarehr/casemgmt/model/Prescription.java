package org.oscarehr.casemgmt.model;

import java.util.Date;

import org.caisi.model.BaseObject;

public class Prescription extends BaseObject {
	private Long id;
	private String provider_no;
	private String demographic_no;
	private Date date_prescribed;
	private Date date_printed;
	private String dates_reprinted;
	private String textView;
	private Drug drug;
	
	
	public Date getDate_prescribed() {
		return date_prescribed;
	}
	public void setDate_prescribed(Date date_prescribed) {
		this.date_prescribed = date_prescribed;
	}
	public Date getDate_printed() {
		return date_printed;
	}
	public void setDate_printed(Date date_printed) {
		this.date_printed = date_printed;
	}
	public String getDates_reprinted() {
		return dates_reprinted;
	}
	public void setDates_reprinted(String dates_reprinted) {
		this.dates_reprinted = dates_reprinted;
	}
	public String getDemographic_no() {
		return demographic_no;
	}
	public void setDemographic_no(String demographic_no) {
		this.demographic_no = demographic_no;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProvider_no() {
		return provider_no;
	}
	public void setProvider_no(String provider_no) {
		this.provider_no = provider_no;
	}
	public String getTextView() {
		return textView;
	}
	public void setTextView(String textView) {
		this.textView = textView;
	}
	public Drug getDrug() {
		return drug;
	}
	public void setDrug(Drug drug) {
		this.drug = drug;
	}
	
}
