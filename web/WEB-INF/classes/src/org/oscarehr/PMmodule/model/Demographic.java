package org.oscarehr.PMmodule.model;

import java.util.Calendar;
import java.util.Date;

import org.oscarehr.PMmodule.model.base.BaseDemographic;
import org.oscarehr.PMmodule.utility.Utility;

/**
 * This is the object class that relates to the demographic table. Any customizations belong here.
 */
public class Demographic extends BaseDemographic {

	private static final String SEPERATOR = "-";

	/* [CONSTRUCTOR MARKER END] */
	public Demographic() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Demographic(java.lang.Integer _demographicNo) {
		super(_demographicNo);
	}

	/**
	 * Constructor for required fields
	 */
	public Demographic(java.lang.Integer _demographicNo, java.lang.String _firstName, java.lang.String _lastName) {
		super(_demographicNo, _firstName, _lastName);
	}

	/* [CONSTRUCTOR MARKER BEGIN] */

	private long agencyId;

	private String links = "";

	private DemographicExt[] extras;

	public String addZero(String text, int num) {
		text = text.trim();
		String zero = "0";
		for (int i = text.length(); i < num; i++) {
			text = zero + text;
		}
		return text;
	}

	public String getAge() {
		return (String.valueOf(Utility.calcAge(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(), "0001"), Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(), "01"), Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(), "01"))));
	}

	public String getAgeAsOf(Date asofDate) {
		return Utility.calcAgeAtDate(Utility.calcDate(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(), "0001"), Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(), "01"), Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(), "01")), asofDate);
	}

	public int getAgeInYears() {
		return Utility.getNumYears(Utility.calcDate(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(), "0001"), Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(), "01"), Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(), "01")), Calendar.getInstance().getTime());
	}

	public int getAgeInYearsAsOf(Date asofDate) {
		return Utility.getNumYears(Utility.calcDate(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(), "0001"), Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(), "01"), Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(), "01")), asofDate);
	}

	public long getAgencyId() {
		return agencyId;
	}

	public DemographicExt[] getExtras() {
		return extras;
	}

	public String getFormattedDob() {
		return this.getYearOfBirth() + SEPERATOR + this.getMonthOfBirth() + SEPERATOR + this.getDateOfBirth();
	}

	public String getFormattedLinks() {
		StringBuilder response = new StringBuilder();

		if (getNumLinks() > 0) {
			String[] links = getLinks().split(",");
			for (int x = 0; x < links.length; x++) {
				String components[] = links[x].split("/");
				String agencyId = components[0];
				if (response.length() > 0) {
					response.append(",");
				}
				response.append(Agency.getAgencyName(Long.valueOf(agencyId).longValue()));

			}
		}

		return response.toString();
	}

	public String getFormattedName() {
		return getLastName() + ", " + getFirstName();
	}

	public String getLinks() {
		return links;
	}

	public int getNumLinks() {
		if (getLinks() == null) {
			return 0;
		}

		if (getLinks().equals("")) {
			return 0;
		}

		return getLinks().split(",").length;
	}

	public void setAgencyId(long agencyId) {
		this.agencyId = agencyId;
	}

	public void setExtras(DemographicExt[] extras) {
		this.extras = extras;
	}

	public void setLinks(String links) {
		this.links = links;
	}

}