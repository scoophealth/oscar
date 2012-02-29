package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DemographicStudyPK implements Serializable {

	@Column(name="demographic_no")
	private Integer demographicNo;
	@Column(name="study_no")
	private Integer studyNo;



	public Integer getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(Integer demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public Integer getStudyNo() {
    	return studyNo;
    }

	public void setStudyNo(Integer studyNo) {
    	this.studyNo = studyNo;
    }

	@Override
	public String toString() {
		return ("demographicNo=" + demographicNo + ", studyNo=" + studyNo);
	}

	@Override
	public int hashCode() {
		return (demographicNo);
	}

	@Override
	public boolean equals(Object o) {
		try {
			DemographicStudyPK o1 = (DemographicStudyPK) o;
			return ((demographicNo == o1.demographicNo) && (studyNo == o1.studyNo));
		} catch (RuntimeException e) {
			return (false);
		}
	}

}
