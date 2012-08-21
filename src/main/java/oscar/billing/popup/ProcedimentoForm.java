
package oscar.billing.popup;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import oscar.billing.cad.model.CadProcedimentos;

@Deprecated
public class ProcedimentoForm extends ActionForm {
    private String dispatch;
    private int strutsAction;
    private String strutsButton = "";
    private List<CadProcedimentos> procedimentos;
    private String codigoProc;
    private String descProc;

    public ProcedimentoForm() {
        this.procedimentos = new ArrayList<CadProcedimentos>();
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
        return this.procedimentos.get(i);
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
    public List<CadProcedimentos> getProcedimentos() {
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
    public void setProcedimentos(List<CadProcedimentos> list) {
        procedimentos = list;
    }
}
