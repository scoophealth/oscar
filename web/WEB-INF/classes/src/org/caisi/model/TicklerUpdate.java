package org.caisi.model;

import java.util.Date;

import org.oscarehr.PMmodule.model.Provider;

public class TicklerUpdate extends BaseObject {
	private Long id;
	private long tickler_no;
	private char status;
	private String provider_no;
	private Date update_date;
	private Provider provider;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProvider_no() {
		return provider_no;
	}
	public void setProvider_no(String provider) {
		this.provider_no = provider;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public long getTickler_no() {
		return tickler_no;
	}
	public void setTickler_no(long tickler_no) {
		this.tickler_no = tickler_no;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	public Provider getProvider() {
		return provider;
	}
}
