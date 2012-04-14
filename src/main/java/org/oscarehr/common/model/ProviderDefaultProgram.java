package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="provider_default_program")
public class ProviderDefaultProgram extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="provider_no")
	private String providerNo;

	@Column(name="program_id")
	private int programId;

	@Column(name="signnote")
	private boolean sign;

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

	public int getProgramId() {
    	return programId;
    }

	public void setProgramId(int programId) {
    	this.programId = programId;
    }

	public boolean isSign() {
    	return sign;
    }

	public void setSign(boolean sign) {
    	this.sign = sign;
    }


}
