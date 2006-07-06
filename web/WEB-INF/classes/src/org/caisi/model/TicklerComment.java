package org.caisi.model;

import java.util.Date;

public class TicklerComment extends BaseObject {
	private Long id;
	private long tickler_no;
	private String message;
	private String provider_no;
	private Date update_date;
	private Provider provider;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getProvider_no() {
		return provider_no;
	}
	public void setProvider_no(String provider_no) {
		this.provider_no = provider_no;
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
	public Provider getProvider() {
		return provider;
	}
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	
}
