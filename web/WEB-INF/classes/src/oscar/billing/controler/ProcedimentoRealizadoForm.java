package oscar.billing.controler;

import org.apache.struts.action.*;

import oscar.billing.cad.model.CadProcedimentos;

import oscar.billing.fat.model.FatFormularios;

import oscar.billing.model.Appointment;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;


public class ProcedimentoRealizadoForm extends ActionForm {
    private String dispatch;
    private int strutsAction;
    private String strutsButton = "";
    private String[] procedimentosChecked;
    private Vector procedimentosForm;
    private Appointment appointment;
    private List formularios;
    private FatFormularios formulario;
    private String coCid;
    private CadProcedimentos cadProcedimentos;

    public ProcedimentoRealizadoForm() {
        this.appointment = new Appointment();
        this.procedimentosForm = new Vector();
        this.formularios = new ArrayList();
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
    public Vector getProcedimentosForm() {
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
    public void setProcedimentosForm(Vector list) {
        procedimentosForm = list;
    }

    public CadProcedimentos getProcedimento(int i) {
        return (CadProcedimentos) this.procedimentosForm.elementAt(i);
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
    public List getFormularios() {
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
    public void setFormularios(List list) {
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

}
