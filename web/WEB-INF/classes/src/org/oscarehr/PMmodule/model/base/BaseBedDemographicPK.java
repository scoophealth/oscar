package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


public abstract class BaseBedDemographicPK implements Serializable {

	protected int hashCode = Integer.MIN_VALUE;

	private java.lang.Integer demographicNo;
	private java.lang.Integer bedId;


	public BaseBedDemographicPK () {}
	
	public BaseBedDemographicPK (
		java.lang.Integer demographicNo,
		java.lang.Integer bedId) {

		this.setDemographicNo(demographicNo);
		this.setBedId(bedId);
	}


	/**
	 * Return the value associated with the column: demographic_no
	 */
	public java.lang.Integer getDemographicNo () {
		return demographicNo;
	}

	/**
	 * Set the value related to the column: demographic_no
	 * @param demographicNo the demographic_no value
	 */
	public void setDemographicNo (java.lang.Integer demographicNo) {
		this.demographicNo = demographicNo;
	}



	/**
	 * Return the value associated with the column: bed_id
	 */
	public java.lang.Integer getBedId () {
		return bedId;
	}

	/**
	 * Set the value related to the column: bed_id
	 * @param bedId the bed_id value
	 */
	public void setBedId (java.lang.Integer bedId) {
		this.bedId = bedId;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.BedDemographicPK)) return false;
		else {
			org.oscarehr.PMmodule.model.BedDemographicPK mObj = (org.oscarehr.PMmodule.model.BedDemographicPK) obj;
			if (null != this.getDemographicNo() && null != mObj.getDemographicNo()) {
				if (!this.getDemographicNo().equals(mObj.getDemographicNo())) {
					return false;
				}
			}
			else {
				return false;
			}
			if (null != this.getBedId() && null != mObj.getBedId()) {
				if (!this.getBedId().equals(mObj.getBedId())) {
					return false;
				}
			}
			else {
				return false;
			}
			return true;
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			StringBuilder sb = new StringBuilder();
			if (null != this.getDemographicNo()) {
				sb.append(this.getDemographicNo().hashCode());
				sb.append(":");
			}
			else {
				return super.hashCode();
			}
			if (null != this.getBedId()) {
				sb.append(this.getBedId().hashCode());
				sb.append(":");
			}
			else {
				return super.hashCode();
			}
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}


}