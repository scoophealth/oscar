package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


public abstract class BaseBedDemographicHistoricalPK implements Serializable {

	protected int hashCode = Integer.MIN_VALUE;

	private java.lang.Integer bedId;
	private java.lang.Integer demographicNo;
	private java.util.Date usageStart;


	public BaseBedDemographicHistoricalPK () {}
	
	public BaseBedDemographicHistoricalPK (
		java.lang.Integer bedId,
		java.lang.Integer demographicNo,
		java.util.Date usageStart) {

		this.setBedId(bedId);
		this.setDemographicNo(demographicNo);
		this.setUsageStart(usageStart);
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
	 * Return the value associated with the column: usage_start
	 */
	public java.util.Date getUsageStart () {
		return usageStart;
	}

	/**
	 * Set the value related to the column: usage_start
	 * @param usageStart the usage_start value
	 */
	public void setUsageStart (java.util.Date usageStart) {
		this.usageStart = usageStart;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.BedDemographicHistoricalPK)) return false;
		else {
			org.oscarehr.PMmodule.model.BedDemographicHistoricalPK mObj = (org.oscarehr.PMmodule.model.BedDemographicHistoricalPK) obj;
			if (null != this.getBedId() && null != mObj.getBedId()) {
				if (!this.getBedId().equals(mObj.getBedId())) {
					return false;
				}
			}
			else {
				return false;
			}
			if (null != this.getDemographicNo() && null != mObj.getDemographicNo()) {
				if (!this.getDemographicNo().equals(mObj.getDemographicNo())) {
					return false;
				}
			}
			else {
				return false;
			}
			if (null != this.getUsageStart() && null != mObj.getUsageStart()) {
				if (!this.getUsageStart().equals(mObj.getUsageStart())) {
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
			if (null != this.getBedId()) {
				sb.append(this.getBedId().hashCode());
				sb.append(":");
			}
			else {
				return super.hashCode();
			}
			if (null != this.getDemographicNo()) {
				sb.append(this.getDemographicNo().hashCode());
				sb.append(":");
			}
			else {
				return super.hashCode();
			}
			if (null != this.getUsageStart()) {
				sb.append(this.getUsageStart().hashCode());
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