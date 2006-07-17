package org.caisi.PMmodule.web.formbean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClientSearchFormBean {
	private static Log log = LogFactory.getLog(ClientSearchFormBean.class);

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	private Calendar calendar = Calendar.getInstance();
	
	private String firstName;
	private String lastName;
	private String dob;
	private String healthCardNumber;
	private String healthCardVersion;
	private boolean searchOutsideDomain;
	private boolean searchUsingSoundex;
	
	private List programDomain;
	
	
	/**
	 * @return Returns the dob.
	 */
	public String getDob() {
		return dob;
	}
	/**
	 * @param dob The dob to set.
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return Returns the healthCardNumber.
	 */
	public String getHealthCardNumber() {
		return healthCardNumber;
	}
	/**
	 * @param healthCardNumber The healthCardNumber to set.
	 */
	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}
	/**
	 * @return Returns the healthCardVersion.
	 */
	public String getHealthCardVersion() {
		return healthCardVersion;
	}
	/**
	 * @param healthCardVersion The healthCardVersion to set.
	 */
	public void setHealthCardVersion(String healthCardVersion) {
		this.healthCardVersion = healthCardVersion;
	}
	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return Returns the searchOutsideDomain.
	 */
	public boolean isSearchOutsideDomain() {
		return searchOutsideDomain;
	}
	/**
	 * @param searchOutsideDomain The searchOutsideDomain to set.
	 */
	public void setSearchOutsideDomain(boolean searchOutsideDomain) {
		this.searchOutsideDomain = searchOutsideDomain;
	}
	
	/**
	 * @return Returns the searchUsingSoundex.
	 */
	public boolean isSearchUsingSoundex() {
		return searchUsingSoundex;
	}
	/**
	 * @param searchUsingSondex The searchOutsideDomain to set.
	 */
	public void setSearchUsingSoundex(boolean searchUsingSoundex) {
		this.searchUsingSoundex = searchUsingSoundex;
	}
	
	public String getYearOfBirth() {
		try {
			Date d = formatter.parse(getDob());
			calendar.setTime(d);
			return String.valueOf(calendar.get(Calendar.YEAR));
		}catch(Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public String getMonthOfBirth() {
		try {
			Date d = formatter.parse(getDob());
			calendar.setTime(d);
			return String.valueOf(calendar.get(Calendar.MONTH)+1);
		}catch(Exception e) {
			log.error(e);
		}
		return null;
	}
	
	public String getDayOfBirth() {
		try {
			Date d = formatter.parse(getDob());
			calendar.setTime(d);
			return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		}catch(Exception e) {
			log.error(e);
		}
		return null;
	}
	/**
	 * @return Returns the programDomain.
	 */
	public List getProgramDomain() {
		return programDomain;
	}
	/**
	 * @param programDomain The programDomain to set.
	 */
	public void setProgramDomain(List programDomain) {
		this.programDomain = programDomain;
	}
	
}
