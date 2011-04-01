package org.oscarehr.common.model;

import javax.persistence.Entity;

@Entity
public class ProfessionalContact extends Contact {
	
	private String specialty;	
	private String cpso;
	
	//to be used on a site specific basis. Any additional ids should be done with other_id
	private String systemId;
	
	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	
	public String getCpso() {
		return cpso;
	}

	public void setCpso(String cpso) {
		this.cpso = cpso;
	}
	
	
	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String toString() {
		return "ProfessionalRelationship - id:"+ getId();
	}

}
