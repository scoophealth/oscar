package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "measurementsDeleted")
public class MeasurementsDeleted extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	@Column(nullable = false)
	private Integer demographicNo = null;
	
	@Column(nullable = false)
	private String type = null;
	
	@Column(nullable = false)
	private String providerNo = null;
	
	@Column(nullable = false)
	private String dataField = null;
	
	@Column(nullable = false)
	private String measuringInstruction = null;
	
	@Column(nullable = false)
	private String comments = null;
	
	@Column(nullable = false)
	private Date dateObserved = null;
	
	@Column(nullable = false)
	private Date dateEntered = null;
	
	@Column(nullable = false)
	private Date dateDeleted = new Date();

	@Column(nullable = false)
	private Integer originalId = null;

	@Override
    public Integer getId() {
		return id;
	}

	public Integer getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	
	public String getDataField() {
		return dataField;
	}
	public void setDataField(String dataField) {
		this.dataField = dataField;
	}
	
	public String getMeasuringInstruction() {
		return measuringInstruction;
	}
	public void setMeasuringInstruction(String instruction) {
		this.measuringInstruction = instruction;
	}
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public Date getDateObserved() {
		return dateObserved;
	}
	public void setDateObserved(Date dateObserved) {
		this.dateObserved = dateObserved;
	}
	
	public Date getDateEntered() {
		return dateEntered;
	}
	public void setDateEntered(Date dateEntered) {
		this.dateEntered = dateEntered;
	}
	
	public Date getDateDeleted() {
		return dateDeleted;
	}
	public void setDateDeleted(Date dateDeleted) {
		this.dateDeleted = dateDeleted;
	}
	
	public Integer getOriginalId() {
		return originalId;
	}
	public void setOriginalId(Integer originalId) {
		this.originalId = originalId;
	}
}
