/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
/** Java class "Demographic.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.model;


/**
 * <p>
 *
 * </p>
 */
public class Demographic {
    ///////////////////////////////////////
    // attributes

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private long demographicNo;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String lastName;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String firstName;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String yearOfbirth;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String monthOfbirth;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String dateOfbirth;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String sex;
    
    private String name;

    ///////////////////////////////////////
    // associations

    /**
     * <p>
     *
     * </p>
     */
    public Appointment appointment;

    /**
     *
     */
    public Demographic() {
    }

    ///////////////////////////////////////
    // operations

    /**
     * <p>
     * Does ...
     * </p><p>
     *
     * </p>
     */
    public void clear() {
        // your code here
    }

    // end clear        

    /**
     * @return
     */
    public Appointment getAppointment() {
        if (appointment != null) {
            return appointment;
        } else {
        	appointment = new Appointment();
            return appointment;
        }
    }

    /**
     * @return
     */
    public String getDateOfbirth() {
        return dateOfbirth;
    }

    /**
     * @return
     */
    public long getDemographicNo() {
        return demographicNo;
    }

    /**
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return
     */
    public String getMonthOfbirth() {
        return monthOfbirth;
    }

    /**
     * @return
     */
    public String getSex() {
        return sex;
    }

    /**
     * @return
     */
    public String getYearOfbirth() {
        return yearOfbirth;
    }

    /**
     * @param appointment
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * @param string
     */
    public void setDateOfbirth(String string) {
        dateOfbirth = string;
    }

    /**
     * @param l
     */
    public void setDemographicNo(long l) {
        demographicNo = l;
    }

    /**
     * @param string
     */
    public void setFirstName(String string) {
        firstName = string;
    }

    /**
     * @param string
     */
    public void setLastName(String string) {
        lastName = string;
    }

    /**
     * @param string
     */
    public void setMonthOfbirth(String string) {
        monthOfbirth = string;
    }

    /**
     * @param string
     */
    public void setSex(String string) {
        sex = string;
    }

    /**
     * @param string
     */
    public void setYearOfbirth(String string) {
        yearOfbirth = string;
    }
	/**
	 * @return
	 */
	public String getName() {
		name = firstName + " " + lastName;
		return name;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

}


// end Demographic
