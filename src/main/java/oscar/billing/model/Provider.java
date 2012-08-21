/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


/** Java class "Provider.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.model;

import oscar.billing.cad.model.CadAtividadesSaude;

/**
 * <p>
 *
 * </p>
 */
@Deprecated
public class Provider {
    /**
     * <p>
     * Represents ...
     * </p>
     */
    public static final int DOCTOR = 1;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    public static final int RECEPTIONIST = 2;

    ///////////////////////////////////////
    // attributes

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String providerNo;

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
    private int providerType;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String specialty;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String status;
    
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
     * <p>
     *
     * </p>
     */
    public CadAtividadesSaude cadAtividadesSaude;

	/**
	 * 
	 */
	public Provider() {
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
    public static int getDOCTOR() {
        return DOCTOR;
    }

    /**
     * @return
     */
    public static int getRECEPTIONIST() {
        return RECEPTIONIST;
    }

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
    public CadAtividadesSaude getCadAtividadesSaude() {
		if (cadAtividadesSaude != null) {
			return cadAtividadesSaude;
		} else {
			cadAtividadesSaude = new CadAtividadesSaude();
			return cadAtividadesSaude;
		}        
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
    public String getProviderNo() {
        return providerNo;
    }

    /**
     * @return
   * @Deprecated no longer is use 2010-04-23, marked for future removal 
     */
    public int getProviderType() {
        return providerType;
    }

    /**
     * @return
     */
    public String getSpecialty() {
        return specialty;
    }

    /**
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param appointment
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * @param saude
     */
    public void setCadAtividadesSaude(CadAtividadesSaude saude) {
        cadAtividadesSaude = saude;
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
    public void setProviderNo(String string) {
        providerNo = string;
    }

    /**
     * @param i
     */
    public void setProviderType(int i) {
        providerType = i;
    }

    /**
     * @param string
     */
    public void setSpecialty(String string) {
        specialty = string;
    }

    /**
     * @param string
     */
    public void setStatus(String string) {
        status = string;
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
 // end Provider
