package oscar.billing.popup;

import org.apache.struts.action.*;

import oscar.billing.cad.model.CadProcedimentos;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


public class ProcedimentoForm extends ActionForm {
    private String dispatch;
    private int strutsAction;
    private String strutsButton = "";
    private List procedimentos;
    private String codigoProc;
    private String descProc;

    public ProcedimentoForm() {
        this.procedimentos = new ArrayList();
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
        procedimentos.clear();
        dispatch = "";
        codigoProc = "";
        descProc = "";
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

    public CadProcedimentos getProcedimento(int i) {
        return (CadProcedimentos) this.procedimentos.get(i);
    }

    /**
     * @return
     */
    public String getCodigoProc() {
        return codigoProc;
    }

    /**
     * @return
     */
    public String getDescProc() {
        return descProc;
    }

    /**
     * @return
     */
    public List getProcedimentos() {
        return procedimentos;
    }

    /**
     * @param string
     */
    public void setCodigoProc(String string) {
        codigoProc = string;
    }

    /**
     * @param string
     */
    public void setDescProc(String string) {
        descProc = string;
    }

    /**
     * @param list
     */
    public void setProcedimentos(List list) {
        procedimentos = list;
    }
}
