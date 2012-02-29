package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ctl_doctype")
public class CtlDocType extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String module;

	@Column(name="doctype")
	private String docType;

	private String status;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getModule() {
    	return module;
    }

	public void setModule(String module) {
    	this.module = module;
    }

	public String getDocType() {
    	return docType;
    }

	public void setDocType(String docType) {
    	this.docType = docType;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }


}
