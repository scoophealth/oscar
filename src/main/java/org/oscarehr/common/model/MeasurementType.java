package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name="measurementType")
public class MeasurementType extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String type;
	private String typeDisplayName;
	private String typeDescription;
	private String measuringInstruction;
	private String validation;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate=new Date();
	
	@PreRemove
	@PreUpdate
	protected void jpaPreventChange() {
		throw (new UnsupportedOperationException("Remove/update is not allowed for this type of item."));
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getType() {
    	return (type);
    }

	public void setType(String type) {
    	this.type = StringUtils.trimToNull(type);
    }

	public String getTypeDisplayName() {
    	return (typeDisplayName);
    }

	public void setTypeDisplayName(String typeDisplayName) {
    	this.typeDisplayName = StringUtils.trimToNull(typeDisplayName);
    }

	public String getTypeDescription() {
    	return (typeDescription);
    }

	public void setTypeDescription(String typeDescription) {
    	this.typeDescription = StringUtils.trimToNull(typeDescription);
    }

	public String getMeasuringInstruction() {
    	return (measuringInstruction);
    }

	public void setMeasuringInstruction(String measuringInstruction) {
    	this.measuringInstruction = StringUtils.trimToNull(measuringInstruction);
    }

	public String getValidation() {
    	return (validation);
    }

	public void setValidation(String validation) {
    	this.validation = StringUtils.trimToNull(validation);
    }

	public Date getCreateDate() {
    	return (createDate);
    }
}
