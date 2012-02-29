package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mdsOBR")
public class MdsOBR extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	@Column(name="ObrID")
	private int ObrId;

	private String placerOrderNo;

	@Column(name="universalServiceID")
	private String universalServiceId;

	private String observationDateTime;

	private String specimenRecDateTime;

	private String fillerFieldOne;

	private String quantityTiming;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getObrId() {
    	return ObrId;
    }

	public void setObrId(int obrId) {
    	ObrId = obrId;
    }

	public String getPlacerOrderNo() {
    	return placerOrderNo;
    }

	public void setPlacerOrderNo(String placerOrderNo) {
    	this.placerOrderNo = placerOrderNo;
    }

	public String getUniversalServiceId() {
    	return universalServiceId;
    }

	public void setUniversalServiceId(String universalServiceId) {
    	this.universalServiceId = universalServiceId;
    }

	public String getObservationDateTime() {
    	return observationDateTime;
    }

	public void setObservationDateTime(String observationDateTime) {
    	this.observationDateTime = observationDateTime;
    }

	public String getSpecimenRecDateTime() {
    	return specimenRecDateTime;
    }

	public void setSpecimenRecDateTime(String specimenRecDateTime) {
    	this.specimenRecDateTime = specimenRecDateTime;
    }

	public String getFillerFieldOne() {
    	return fillerFieldOne;
    }

	public void setFillerFieldOne(String fillerFieldOne) {
    	this.fillerFieldOne = fillerFieldOne;
    }

	public String getQuantityTiming() {
    	return quantityTiming;
    }

	public void setQuantityTiming(String quantityTiming) {
    	this.quantityTiming = quantityTiming;
    }


}
