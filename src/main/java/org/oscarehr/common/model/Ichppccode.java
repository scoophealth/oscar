package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ichppccode")
public class Ichppccode extends AbstractModel<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ichppccode")
	private String id;

	@Column(name="diagnostic_code")
	private String diagnosticCode;

	private String description;

	public String getId() {
    	return id;
    }

	public void setId(String id) {
    	this.id = id;
    }

	public String getDiagnosticCode() {
    	return diagnosticCode;
    }

	public void setDiagnosticCode(String diagnosticCode) {
    	this.diagnosticCode = diagnosticCode;
    }

	public String getDescription() {
    	return description;
    }

	public void setDescription(String description) {
    	this.description = description;
    }


}
