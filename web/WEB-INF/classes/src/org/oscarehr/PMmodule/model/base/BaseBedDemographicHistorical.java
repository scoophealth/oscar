package org.oscarehr.PMmodule.model.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bed_demographic_historical table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bed_demographic_historical"
 */

public abstract class BaseBedDemographicHistorical  implements Serializable {

	public static String REF = "BedDemographicHistorical";
	public static String PROP_USAGE_END = "usageEnd";
	public static String PROP_ID = "id";


	// constructors
	public BaseBedDemographicHistorical () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBedDemographicHistorical (org.oscarehr.PMmodule.model.BedDemographicHistoricalPK id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBedDemographicHistorical (
		org.oscarehr.PMmodule.model.BedDemographicHistoricalPK id,
		java.util.Date usageEnd) {

		this.setId(id);
		this.setUsageEnd(usageEnd);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private org.oscarehr.PMmodule.model.BedDemographicHistoricalPK id;

	// fields
	private java.util.Date usageEnd;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     */
	public org.oscarehr.PMmodule.model.BedDemographicHistoricalPK getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (org.oscarehr.PMmodule.model.BedDemographicHistoricalPK id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: usage_end
	 */
	public java.util.Date getUsageEnd () {
		return usageEnd;
	}

	/**
	 * Set the value related to the column: usage_end
	 * @param usageEnd the usage_end value
	 */
	public void setUsageEnd (java.util.Date usageEnd) {
		this.usageEnd = usageEnd;
	}




	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof org.oscarehr.PMmodule.model.BedDemographicHistorical)) return false;
		else {
			org.oscarehr.PMmodule.model.BedDemographicHistorical bedDemographicHistorical = (org.oscarehr.PMmodule.model.BedDemographicHistorical) obj;
			if (null == this.getId() || null == bedDemographicHistorical.getId()) return false;
			else return (this.getId().equals(bedDemographicHistorical.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


	public String toString () {
		return super.toString();
	}


}