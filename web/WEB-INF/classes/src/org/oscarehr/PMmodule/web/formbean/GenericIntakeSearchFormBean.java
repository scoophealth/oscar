package org.oscarehr.PMmodule.web.formbean;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;
import org.oscarehr.PMmodule.model.Demographic;

public class GenericIntakeSearchFormBean extends ActionForm {
	
    private static final long serialVersionUID = 1L;
    
	private LabelValueBean[] months;
	private LabelValueBean[] days;
    
	private String method;
    
    private String firstName;
	private String lastName;
	private String monthOfBirth;
	private String dayOfBirth;
	private String yearOfBirth;
	private String healthCardNumber;
	private String healthCardVersion;
	
	private boolean localMatch;
	private boolean remoteMatch;
	private Demographic[] matches;
	
	private Integer clientId;
	private Long agencyId;
	
	public GenericIntakeSearchFormBean() {
		setMonths(GenericIntakeConstants.MONTHS);
		setDays(GenericIntakeConstants.DAYS);
	}
	
	public LabelValueBean[] getMonths() {
		return months;
	}
	
	public void setMonths(LabelValueBean[] months) {
	    this.months = months;
    }
	
	public LabelValueBean[] getDays() {
    	return days;
    }
	
	public void setDays(LabelValueBean[] days) {
	    this.days = days;
    }

	public String getMethod() {
	    return method;
    }
	
	public void setMethod(String method) {
	    this.method = method;
    }
	
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
    	return lastName;
    }

	public String getMonthOfBirth() {
    	return monthOfBirth;
    }

	public String getDayOfBirth() {
		return dayOfBirth;
	}

	public String getYearOfBirth() {
    	return yearOfBirth;
    }

	public String getHealthCardNumber() {
		return healthCardNumber;
	}

	public String getHealthCardVersion() {
		return healthCardVersion;
	}

	public void setFirstName(String firstName) {
    	this.firstName = firstName;
    }

	public void setLastName(String lastName) {
    	this.lastName = lastName;
    }

	public void setMonthOfBirth(String monthOfBirth) {
    	this.monthOfBirth = monthOfBirth;
    }

	public void setDayOfBirth(String dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}

	public void setYearOfBirth(String yearOfBirth) {
    	this.yearOfBirth = yearOfBirth;
    }

	public void setHealthCardNumber(String healthCardNumber) {
		this.healthCardNumber = healthCardNumber;
	}

	public void setHealthCardVersion(String healthCardVersion) {
		this.healthCardVersion = healthCardVersion;
	}
	
	public boolean isLocalMatch() {
    	return localMatch;
    }

	public boolean isRemoteMatch() {
    	return remoteMatch;
    }
	
	public Demographic[] getMatches() {
    	return matches;
    }
	
	public String getMatchType() {
		return isRemoteMatch() ? "Integrator" : "Agency";
	}
	
	public void setLocalMatches(List matches) {
		if (matches != null && matches.size() > 0) {
			this.localMatch = true;

			this.matches = new Demographic[matches.size()];
			for (int i = 0; i < matches.size(); i++) {
				this.matches[i] = ((Demographic) matches.get(i));
			}
		}
	}

	public void setRemoteMatches(Demographic[] matches) {
		if (matches != null && matches.length > 0) {
			this.remoteMatch = true;
			this.matches = matches;
		}
	}

	public Integer getClientId() {
    	return clientId;
    }

	public void setClientId(Integer clientId) {
    	this.clientId = clientId;
    }

	public Long getAgencyId() {
    	return agencyId;
    }

	public void setAgencyId(Long agencyId) {
    	this.agencyId = agencyId;
    }
	
}