package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="incomingLabRules")
public class IncomingLabRules extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="provider_no")
	private String providerNo;

	private String status;

	@Column(name="frwdProvider_no")
	private String frwdProviderNo;

	private String archive;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public String getStatus() {
    	return status;
    }

	public void setStatus(String status) {
    	this.status = status;
    }

	public String getFrwdProviderNo() {
    	return frwdProviderNo;
    }

	public void setFrwdProviderNo(String frwdProviderNo) {
    	this.frwdProviderNo = frwdProviderNo;
    }

	public String getArchive() {
    	return archive;
    }

	public void setArchive(String archive) {
    	this.archive = archive;
    }


}
