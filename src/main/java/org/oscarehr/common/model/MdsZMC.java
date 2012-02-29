package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mdsZMC")
public class MdsZMC extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	@Column(name="setID")
	private String setId;

	private String messageCodeIdentifier;

	private String messageCodeVersion;

	private String noMessageCodeDescLines;

	private String sigFlag;

	private String messageCodeDesc;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getSetId() {
    	return setId;
    }

	public void setSetId(String setId) {
    	this.setId = setId;
    }

	public String getMessageCodeIdentifier() {
    	return messageCodeIdentifier;
    }

	public void setMessageCodeIdentifier(String messageCodeIdentifier) {
    	this.messageCodeIdentifier = messageCodeIdentifier;
    }

	public String getMessageCodeVersion() {
    	return messageCodeVersion;
    }

	public void setMessageCodeVersion(String messageCodeVersion) {
    	this.messageCodeVersion = messageCodeVersion;
    }

	public String getNoMessageCodeDescLines() {
    	return noMessageCodeDescLines;
    }

	public void setNoMessageCodeDescLines(String noMessageCodeDescLines) {
    	this.noMessageCodeDescLines = noMessageCodeDescLines;
    }

	public String getSigFlag() {
    	return sigFlag;
    }

	public void setSigFlag(String sigFlag) {
    	this.sigFlag = sigFlag;
    }

	public String getMessageCodeDesc() {
    	return messageCodeDesc;
    }

	public void setMessageCodeDesc(String messageCodeDesc) {
    	this.messageCodeDesc = messageCodeDesc;
    }


}
