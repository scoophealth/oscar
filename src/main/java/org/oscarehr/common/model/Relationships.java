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
@Table(name="relationships")
public class Relationships extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="facility_id")
	private int facilityId;

	@Column(name="demographic_no")
	private int demographicNo;

	@Column(name="relation_demographic_no")
	private int relationDemographicNo;

	private String relation;

	@Column(name="creation_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Column(name="sub_decision_maker")
	private String subDecisionMaker;

	@Column(name="emergency_contact")
	private String emergencyContact;

	private String notes;

	private String deleted;

	private String creator;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getFacilityId() {
    	return facilityId;
    }

	public void setFacilityId(int facilityId) {
    	this.facilityId = facilityId;
    }

	public int getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public int getRelationDemographicNo() {
    	return relationDemographicNo;
    }

	public void setRelationDemographicNo(int relationDemographicNo) {
    	this.relationDemographicNo = relationDemographicNo;
    }

	public String getRelation() {
    	return relation;
    }

	public void setRelation(String relation) {
    	this.relation = relation;
    }

	public Date getCreationDate() {
    	return creationDate;
    }

	public void setCreationDate(Date creationDate) {
    	this.creationDate = creationDate;
    }

	public String getSubDecisionMaker() {
    	return subDecisionMaker;
    }

	public void setSubDecisionMaker(String subDecisionMaker) {
    	this.subDecisionMaker = subDecisionMaker;
    }

	public String getEmergencyContact() {
    	return emergencyContact;
    }

	public void setEmergencyContact(String emergencyContact) {
    	this.emergencyContact = emergencyContact;
    }

	public String getNotes() {
    	return notes;
    }

	public void setNotes(String notes) {
    	this.notes = notes;
    }

	public String getDeleted() {
    	return deleted;
    }

	public void setDeleted(String deleted) {
    	this.deleted = deleted;
    }

	public String getCreator() {
    	return creator;
    }

	public void setCreator(String creator) {
    	this.creator = creator;
    }



}
