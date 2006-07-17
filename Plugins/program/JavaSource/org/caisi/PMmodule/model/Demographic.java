package org.caisi.PMmodule.model;

import java.util.Calendar;
import java.util.Date;

import org.caisi.PMmodule.model.base.BaseDemographic;
import org.caisi.PMmodule.utility.Utility;

/**
 * This is the object class that relates to the demographic table.
 * Any customizations belong here.
 */
public class Demographic extends BaseDemographic {

/*[CONSTRUCTOR MARKER BEGIN]*/
	public Demographic () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Demographic (java.lang.Integer _demographicNo) {
		super(_demographicNo);
	}

	/**
	 * Constructor for required fields
	 */
	public Demographic (
		java.lang.Integer _demographicNo,
		java.lang.String _sex,
		java.lang.String _firstName,
		java.lang.String _lastName) {

		super (
			_demographicNo,
			_sex,
			_firstName,
			_lastName);
	}
/*[CONSTRUCTOR MARKER END]*/	
//###################################################################
    public String getAge() 
    {
        return (String.valueOf(Utility.calcAge(
        		Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(),"0001"),
        		Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(),"01"),
        		Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(),"01")
        		)));            
    }
    
    public String getAgeAsOf(Date asofDate) 
    {
   		return  Utility.calcAgeAtDate(
   				Utility.calcDate(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(),"0001"),
   						Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(),"01"),
   						Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(),"01")), 
   						asofDate);            
    }
    
    public int getAgeInYears()
    {            
   		return  Utility.getNumYears(
   				Utility.calcDate(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(),"0001"),
   						Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(),"01"),
   						Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(),"01")),
   						Calendar.getInstance().getTime());
    }
    
    public int getAgeInYearsAsOf(Date asofDate)
    {            
   		return  Utility.getNumYears(
   				Utility.calcDate(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(),"0001"),
   						         Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(),"01"),
   						         Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(),"01")),
   						         asofDate);
    }
                     
    public String getDob() 
    {
    	return  addZero(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(),"0001"), 4) + 
    			addZero(Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(),"01"), 2) + 
    			addZero(Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(),"01"), 2);
    }
    
    public String getDob(String seperator)
    {
    	return this.getYearOfBirth() + seperator + this.getMonthOfBirth() + seperator + this.getDateOfBirth();
    }
    
    public String getFormattedDob()
    {
    	String seperator = "-";
    	return this.getYearOfBirth() + seperator + this.getMonthOfBirth() + seperator + this.getDateOfBirth();
    }
    public String getYearOfBirth()
    {
    	return addZero(Utility.convertToReplaceStrIfEmptyStr(super.getYearOfBirth(),"0001"), 4);
    }
	
    public String getMonthOfBirth()
    {
    	return addZero(Utility.convertToReplaceStrIfEmptyStr(super.getMonthOfBirth(),"01"), 2);
    }

    public String getDateOfBirth()
    {
    	return addZero(Utility.convertToReplaceStrIfEmptyStr(super.getDateOfBirth(),"01"), 2);
    }

    public String getHealthCardNum() {
        return super.getHin();
    }

    public String addZero(String text, int num)
    {
        text = text.trim();
        String zero = "0";
        for (int i = text.length();i<num; i++)
        {
            text = zero + text;
        }
        return text;
    }

	public String getFormattedName() {
		return getLastName() + "," + getFirstName();
	}
	
//###################################################################	

}