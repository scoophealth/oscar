package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="measurementCSSLocation")
public class MeasurementTypeDeleted extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String type;

	private String typeDisplayName;

	private String typeDescription;

	private String measuringInstruction;

	private String validation;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateDeleted;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getType() {
    	return type;
    }

	public void setType(String type) {
    	this.type = type;
    }

	public String getTypeDisplayName() {
    	return typeDisplayName;
    }

	public void setTypeDisplayName(String typeDisplayName) {
    	this.typeDisplayName = typeDisplayName;
    }

	public String getTypeDescription() {
    	return typeDescription;
    }

	public void setTypeDescription(String typeDescription) {
    	this.typeDescription = typeDescription;
    }

	public String getMeasuringInstruction() {
    	return measuringInstruction;
    }

	public void setMeasuringInstruction(String measuringInstruction) {
    	this.measuringInstruction = measuringInstruction;
    }

	public String getValidation() {
    	return validation;
    }

	public void setValidation(String validation) {
    	this.validation = validation;
    }

	public Date getDateDeleted() {
    	return dateDeleted;
    }

	public void setDateDeleted(Date dateDeleted) {
    	this.dateDeleted = dateDeleted;
    }


}
