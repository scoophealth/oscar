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
