package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "preventions")
public class Prevention extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	@Column(name = "demographic_no")
	private Integer demographicId = null;

	@Column(name = "creation_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate = new Date();

	@Column(name = "prevention_date")
	@Temporal(TemporalType.DATE)
	private Date preventionDate = null;

	@Column(name = "provider_no")
	private String providerNo = null;

	@Column(name = "prevention_type")
	private String preventionType = null;

	private char deleted = '0';
	private char refused = '0';
	private char never = '0';

	@Column(name = "next_date")
	@Temporal(TemporalType.DATE)
	private Date nextDate = null;

	@Column(name = "creator")
	private String creatorProviderNo = null;
	
	private Date lastUpdateDate = null;

	public Integer getDemographicId() {
		return demographicId;
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public Date getPreventionDate() {
		return preventionDate;
	}

	public void setPreventionDate(Date preventionDate) {
		this.preventionDate = preventionDate;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getPreventionType() {
		return preventionType;
	}

	public void setPreventionType(String preventionType) {
		this.preventionType = preventionType;
	}

	public boolean isDeleted() {
		return deleted=='1';
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted ? '1' : '0';
	}

	public boolean isRefused() {
		return refused=='1';
	}
	
	public boolean isIneligible(){
		return refused == '2';
	}

	public void setRefused(boolean refused) {
		this.refused = refused ? '1' : '0';
	}
	
	public void setIneligible(boolean ineligible){
		this.refused = ineligible ? '2' : '0';
	}

	public Date getNextDate() {
		return nextDate;
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}

	public boolean isNever() {
		return never=='1';
	}

	public void setNever(boolean never) {
		this.never = never ? '1' : '0';
	}

	public String getCreatorProviderNo() {
		return creatorProviderNo;
	}

	public void setCreatorProviderNo(String creatorProviderNo) {
		this.creatorProviderNo = creatorProviderNo;
	}
	
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	@Override
    public Integer getId() {
		return id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	@PreUpdate
	@PrePersist
	protected void autoSetUpdateTime()
	{
		lastUpdateDate=new Date();
	}
}
