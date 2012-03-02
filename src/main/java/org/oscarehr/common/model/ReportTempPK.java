package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class ReportTempPK implements Serializable {

	@Column(name="demographic_no")
	private int demographicNo;

	@Temporal(TemporalType.DATE)
	private Date edb;

	public int getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public Date getEdb() {
    	return edb;
    }

	public void setEdb(Date edb) {
    	this.edb = edb;
    }


	@Override
	public String toString() {
		return ("demographicNo=" + demographicNo + ", edb=" + edb);
	}

	@Override
	public int hashCode() {
		return (demographicNo);
	}

	@Override
	public boolean equals(Object o) {
		try {
			ReportTempPK o1 = (ReportTempPK) o;
			return ((demographicNo == o1.demographicNo) && (edb.equals(o1.edb)));
		} catch (RuntimeException e) {
			return (false);
		}
	}

}
