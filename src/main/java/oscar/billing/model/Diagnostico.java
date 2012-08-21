
/** Java class "Diagnostico.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.model;

import oscar.billing.cad.model.CadCid;


/**
 * <p>
 *
 * </p>
 */
@Deprecated
public class Diagnostico {
    ///////////////////////////////////////
    // attributes
    private boolean isSave = false;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private int ordem;

    ///////////////////////////////////////
    // associations

    /**
     * <p>
     *
     * </p>
     */
    public CadCid cadCid;

    /**
     * <p>
     *
     * </p>
     */
    public Appointment appointment;

    /**
     *
     */
    public Diagnostico() {
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
    public CadCid getCadCid() {
		if (cadCid != null) {
			return cadCid;
		} else {
			cadCid = new CadCid();
			return cadCid;
		}
    }

    /**
     * @return
     */
    public int getOrdem() {
        return ordem;
    }

    /**
     * @param appointment
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * @param cid
     */
    public void setCadCid(CadCid cid) {
        cadCid = cid;
    }

    /**
     * @param i
     */
    public void setOrdem(int i) {
        ordem = i;
    }

    /**
     * @return
     */
    public boolean isSave() {
        return isSave;
    }

    /**
     * @param b
     */
    public void setSave(boolean b) {
        isSave = b;
    }
}


// end Diagnostico
