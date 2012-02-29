package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "quickListUser")
public class QuickListUser extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	private int providerNo;

	private String quickListName;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUsed;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(int providerNo) {
    	this.providerNo = providerNo;
    }

	public String getQuickListName() {
    	return quickListName;
    }

	public void setQuickListName(String quickListName) {
    	this.quickListName = quickListName;
    }

	public Date getLastUsed() {
    	return lastUsed;
    }

	public void setLastUsed(Date lastUsed) {
    	this.lastUsed = lastUsed;
    }


}
