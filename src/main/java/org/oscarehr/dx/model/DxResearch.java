package org.oscarehr.dx.model;

import java.util.Date;

import org.caisi.model.BaseObject;

public class DxResearch extends BaseObject {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private int demographicNo;
	private Date startDate;
	private Date updateDate;
	private String status;
	private String code;
	private String codingSystem;
	private boolean association;
	
	
	public boolean isAssociation() {
		return association;
	}
	public void setAssociation(boolean association) {
		this.association = association;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodingSystem() {
		return codingSystem;
	}
	public void setCodingSystem(String codingSystem) {
		this.codingSystem = codingSystem;
	}
	
	
}
