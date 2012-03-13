package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Column;

public class ScheduleTemplatePrimaryKey implements Serializable {

	/**
	 * Don't blame me, I wasn't the one to start doing this, I'm just making the constant for something already in use. Someday we should refactor it to null.
	 */
	public static final String DODGY_FAKE_PROVIDER_NO_USED_TO_HOLD_PUBLIC_TEMPLATES="Public";
	
	@Column(name="provider_no")
    private String providerNo;
	private String name;

   public ScheduleTemplatePrimaryKey() {
   	//required by JPA
   }

   public ScheduleTemplatePrimaryKey(String providerNo, String name) {
	  this.providerNo = providerNo;
      this.name = name;      
   }
  

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return ("name=" + name + ", providerNo=" + providerNo);
	}

	@Override
	public int hashCode() {
		return (toString().hashCode());
	}

	@Override
	public boolean equals(Object o) {
		try {
			ScheduleTemplatePrimaryKey o1 = (ScheduleTemplatePrimaryKey) o;
			return ((name == o1.name) && (providerNo == o1.providerNo));
		} catch (RuntimeException e) {
			return (false);
		}
	}
}
