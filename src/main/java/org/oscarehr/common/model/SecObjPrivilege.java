package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="secObjPrivilege")
public class SecObjPrivilege extends AbstractModel<SecObjPrivilegePrimaryKey> {
	
	@EmbeddedId
	private SecObjPrivilegePrimaryKey id;
	
	private String privilege = "|0|";
	private int priority = 0;
	@Column(name="provider_no")
	private String providerNo = null;
	
	public SecObjPrivilegePrimaryKey getId() {
		return id;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	
	

}
