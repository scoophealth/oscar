package com.quatro.model;

import java.util.Date;

import org.caisi.model.BaseObject;

public class ScratchPad extends BaseObject {
	private Long id;
	private String providerNo;
	private Date date_time;
	private String scratch_text;
	
	public ScratchPad() {

	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getScratch_text() {
		return scratch_text;
	}

	public void setScratch_text(String scratch_text) {
		this.scratch_text = scratch_text;
	}
	
}
