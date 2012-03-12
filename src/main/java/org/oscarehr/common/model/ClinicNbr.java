package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "clinic_nbr")
public class ClinicNbr extends AbstractModel<Integer> implements Serializable {
	
	@Id
   	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nbr_id")
	private Integer id;
	
	@Column(name = "nbr_value")
	private String nbrValue;
	
	@Column(name = "nbr_string")
	private String nbrString;
	
	@Column(name = "nbr_status")
	private String nbrStatus = "A";
	
	public Integer getId() {
		return this.id;
	}
	
	public String getNbrValue() {
		return this.nbrValue;
	}
	
	public void setNbrValue(String nbrValue) {
		this.nbrValue = StringUtils.trimToNull(nbrValue);
	}
	
	public String getNbrString() {
		return this.nbrString;
	}
	
	public void setNbrString(String nbrString) {
		this.nbrString = StringUtils.trimToNull(nbrString);
	}

	public String getNbrStatus() {
	    return nbrStatus;
    }

	public void setNbrStatus(String nbrStatus) {
	    this.nbrStatus = nbrStatus;
    }
	
}