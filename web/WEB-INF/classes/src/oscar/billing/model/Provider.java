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
