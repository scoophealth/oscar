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
@Table(name="config_Immunization")
public class ConfigImmunization extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="setId")
	private Integer id;

	@Column(name="setName")
	private String name;

	@Column(name="setXmlDoc")
	private String xmlDoc;

	@Temporal(TemporalType.DATE)
	private Date createDate;

	private String providerNo;

	private int archived;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public String getXmlDoc() {
    	return xmlDoc;
    }

	public void setXmlDoc(String xmlDoc) {
    	this.xmlDoc = xmlDoc;
    }

	public Date getCreateDate() {
    	return createDate;
    }

	public void setCreateDate(Date createDate) {
    	this.createDate = createDate;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public int getArchived() {
    	return archived;
    }

	public void setArchived(int archived) {
    	this.archived = archived;
    }


}
