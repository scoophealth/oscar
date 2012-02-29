package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="demographicstudy")
public class DemographicStudy extends AbstractModel<DemographicStudyPK>{

	@EmbeddedId
	private DemographicStudyPK id;

	private String providerNo;

	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	public DemographicStudyPK getId() {
    	return id;
    }

	public void setId(DemographicStudyPK id) {
    	this.id = id;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public Date getTimestamp() {
    	return timestamp;
    }

	public void setTimestamp(Date timestamp) {
    	this.timestamp = timestamp;
    }



}
