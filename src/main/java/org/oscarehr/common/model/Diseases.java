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
@Table(name="diseases")
public class Diseases extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="diseaseid")
	private Integer id;

	@Column(name="demographic_no")
	private int demographicNo;

	@Column(name="ICD9_E")
	private String icd9Entry;

	@Column(name="entry_date")
	@Temporal(TemporalType.DATE)
	private Date entryDate;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public String getIcd9Entry() {
    	return icd9Entry;
    }

	public void setIcd9Entry(String icd9Entry) {
    	this.icd9Entry = icd9Entry;
    }

	public Date getEntryDate() {
    	return entryDate;
    }

	public void setEntryDate(Date entryDate) {
    	this.entryDate = entryDate;
    }


}
