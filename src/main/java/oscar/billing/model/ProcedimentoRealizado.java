
/** Java class "ProcedimentoRealizado.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.model;

import java.util.Date;

import oscar.billing.cad.model.CadProcedimentos;
import oscar.billing.cad.model.CadTiposAtendimento;


/**
 * <p>
 * Bean for a done procedure.
 * </p>
 */
@Deprecated
public class ProcedimentoRealizado {
    ///////////////////////////////////////
    // attributes
    
    private boolean isSave = false;
    private Date dtRealizacao;
    public CadProcedimentos cadProcedimentos;
    public Appointment appointment;
    private CadTiposAtendimento tpAtendimento = new CadTiposAtendimento();

	/**
	 * 
	 */
	public ProcedimentoRealizado() {
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
    public CadProcedimentos getCadProcedimentos() {
		if (cadProcedimentos != null) {
			return cadProcedimentos;
		} else {
			cadProcedimentos = new CadProcedimentos();
			return cadProcedimentos;
		}
    }

    /**
     * @return
     */
    public Date getDtRealizacao() {
        return dtRealizacao;
    }

    /**
     * @param appointment
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * @param procedimentos
     */
    public void setCadProcedimentos(CadProcedimentos procedimentos) {
        cadProcedimentos = procedimentos;
    }

    /**
     * @param date
     */
    public void setDtRealizacao(Date date) {
        dtRealizacao = date;
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

	/**
	 * Returns the tpAtendimento.
	 * @return int
	 */
	public CadTiposAtendimento getTpAtendimento() {
		return tpAtendimento;
	}

	/**
	 * Sets the tpAtendimento.
	 * @param tpAtendimento The tpAtendimento to set
	 */
	public void setTpAtendimento(CadTiposAtendimento tpAtendimento) {
		this.tpAtendimento = tpAtendimento;
	}

}
 // end ProcedimentoRealizado
