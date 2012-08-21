
package oscar.billing.controler;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import oscar.billing.cad.model.CadProcedimentos;
import oscar.billing.fat.model.FatFormularios;
import oscar.billing.model.Appointment;

@Deprecated
public class ProcedimentoRealizadoForm extends ActionForm {
    private String dispatch;
    private int strutsAction;
    private String strutsButton = "";
    private String[] procedimentosChecked;
    private Vector<CadProcedimentos> procedimentosForm;
    private Appointment appointment;
    private List<FatFormularios> formularios;
    private FatFormularios formulario;
    private String coCid;
	private String dsCid;
    private CadProcedimentos cadProcedimentos;
    private String tpAtendimento = "";	// attendance type - added in 13 nov 2003

    public ProcedimentoRealizadoForm() {
        this.appointment = new Appointment();
        this.procedimentosForm = new Vector<CadProcedimentos>();
        this.formularios = new ArrayList<FatFormularios>();
        this.formulario = new FatFormularios();
        this.cadProcedimentos = new CadProcedimentos();
    }

    public int getStrutsAction() {
        return strutsAction;
    }

    public void setStrutsAction(int strutsAction) {
        this.strutsAction = strutsAction;
    }

    public void setStrutsButton(String strutsButton) {
        this.strutsButton = strutsButton;
    }

    public String getStrutsButton() {
        return strutsButton;
    }

    public ActionErrors validate(ActionMapping mapping,
        javax.servlet.http.HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);

        return errors;
    }

    public void clear() {
		procedimentosChecked = new String[] {};
		procedimentosForm.clear();
		appointment.clear();
		formulario.clear();
		dispatch = "";
		coCid = "";
		tpAtendimento = "";
    }

    /**
     * Returns the dispatch.
     * @return String
     */
    public String getDispatch() {
        return dispatch;
    }

    /**
     * Sets the dispatch.
     * @param dispatch The dispatch to set
     */
    public void setDispatch(String dispatch) {
        this.dispatch = dispatch;
    }

    /**
     * @see org.apache.struts.action.ActionForm#reset(ActionMapping, HttpServletRequest)
     */
    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);
    }

    /**
     * @return
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * @return
     */
    public String[] getProcedimentosChecked() {
        return procedimentosChecked;
    }

    /**
     * @return
     */
    public Vector<CadProcedimentos> getProcedimentosForm() {
        return procedimentosForm;
    }

    /**
     * @param appointment
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * @param strings
     */
    public void setProcedimentosChecked(String[] strings) {
        procedimentosChecked = strings;
    }

    /**
     * @param list
     */
    public void setProcedimentosForm(Vector<CadProcedimentos> list) {
        procedimentosForm = list;
    }

    public CadProcedimentos getProcedimento(int i) {
        return this.procedimentosForm.elementAt(i);
    }

    /**
     * @return
     */
    public FatFormularios getFormulario() {
        return formulario;
    }

    /**
     * @return
     */
    public List<FatFormularios> getFormularios() {
        return formularios;
    }

    /**
     * @param formularios
     */
    public void setFormulario(FatFormularios formularios) {
        formulario = formularios;
    }

    /**
     * @param list
     */
    public void setFormularios(List<FatFormularios> list) {
        formularios = list;
    }

	/**
	 * @return
	 */
	public String getCoCid() {
		return coCid;
	}

	/**
	 * @param string
	 */
	public void setCoCid(String string) {
		coCid = string;
	}

	/**
	 * @return
	 */
	public CadProcedimentos getCadProcedimentos() {
		return cadProcedimentos;
	}

	/**
	 * @param procedimentos
	 */
	public void setCadProcedimentos(CadProcedimentos procedimentos) {
		cadProcedimentos = procedimentos;
	}

	/**
	 * @return
	 */
	public String getDsCid() {
		return dsCid;
	}

	/**
	 * @param string
	 */
	public void setDsCid(String string) {
		dsCid = string;
	}

	/**
	 * Returns the tpAtendimento.
	 * @return String
	 */
	public String getTpAtendimento() {
		return tpAtendimento;
	}

	/**
	 * Sets the tpAtendimento.
	 * @param tpAtendimento The tpAtendimento to set
	 */
	public void setTpAtendimento(String tpAtendimento) {
		this.tpAtendimento = tpAtendimento;
	}

}
