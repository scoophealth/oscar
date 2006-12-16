package org.oscarehr.PMmodule.model;

import java.util.Map;

import org.oscarehr.PMmodule.model.base.BaseAgency;

/**
 * This is the object class that relates to the agency table. Any customizations belong here.
 */
public class Agency extends BaseAgency {

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Agency() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Agency(java.lang.Long _id) {
		super(_id);
	}

	/**
	 * Constructor for required fields
	 */
	public Agency(java.lang.Long _id, java.lang.String _name, boolean _local, boolean _integratorEnabled) {
		super(_id, _name, _local, _integratorEnabled);
	}

	/* [CONSTRUCTOR MARKER END] */

	public boolean getLocal() {
		return isLocal();
	}

	protected static Map agencyMap;

	public static void setAgencyMap(Map map) {
		agencyMap = map;
	}

	public static Map getAgencyMap() {
		return agencyMap;
	}

	public static String getAgencyName(long agencyId) {
		if (agencyMap == null) {
			return "Unknown (" + agencyId + ")";
		}
		Agency agency = (Agency) agencyMap.get(new Long(agencyId));
		if (agency == null) {
			return "Unknown (" + agencyId + ")";
		} else {
			return agency.getName();
		}
	}

	protected static Agency localAgency;

	public static Agency getLocalAgency() {
		return localAgency;
	}

	public static void setLocalAgency(Agency agency) {
		localAgency = agency;
	}
}