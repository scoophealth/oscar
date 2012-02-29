package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mdsZCT")
public class MdsZCT extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	private String barCodeIdentifier;

	private String placerGroupNo;

	private String observationDateTime;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getBarCodeIdentifier() {
    	return barCodeIdentifier;
    }

	public void setBarCodeIdentifier(String barCodeIdentifier) {
    	this.barCodeIdentifier = barCodeIdentifier;
    }

	public String getPlacerGroupNo() {
    	return placerGroupNo;
    }

	public void setPlacerGroupNo(String placerGroupNo) {
    	this.placerGroupNo = placerGroupNo;
    }

	public String getObservationDateTime() {
    	return observationDateTime;
    }

	public void setObservationDateTime(String observationDateTime) {
    	this.observationDateTime = observationDateTime;
    }


}
