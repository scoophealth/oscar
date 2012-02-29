package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="encounter")
public class Encounter extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="encounter_no")
	private Integer id;

	@Column(name="demographic_no")
	private int demographicNo;

	@Column(name="encounter_date")
	@Temporal(TemporalType.DATE)
	private Date encounterDate;

	@Column(name="encounter_time")
	@Temporal(TemporalType.TIME)
	private Date encounterTime;

	@Column(name="provider_no")
	private String providerNo;

	private String subject;

	private String content;

	@Column(name="encounter_attachment")
	private String encounterAttachment;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public Date getEncounterDate() {
    	return encounterDate;
    }

	public void setEncounterDate(Date encounterDate) {
    	this.encounterDate = encounterDate;
    }

	public Date getEncounterTime() {
    	return encounterTime;
    }

	public void setEncounterTime(Date encounterTime) {
    	this.encounterTime = encounterTime;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public String getSubject() {
    	return subject;
    }

	public void setSubject(String subject) {
    	this.subject = subject;
    }

	public String getContent() {
    	return content;
    }

	public void setContent(String content) {
    	this.content = content;
    }

	public String getEncounterAttachment() {
    	return encounterAttachment;
    }

	public void setEncounterAttachment(String encounterAttachment) {
    	this.encounterAttachment = encounterAttachment;
    }


}
