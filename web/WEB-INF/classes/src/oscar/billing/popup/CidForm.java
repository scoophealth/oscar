package oscar.billing.popup;

import org.apache.struts.action.*;

import oscar.billing.cad.model.CadProcedimentos;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


public class CidForm extends ActionForm {
    private String dispatch;
    private int strutsAction;
    private String strutsButton = "";
    private List cid;
    private String codigo;
    private String desc;

    public CidForm() {
        this.cid = new ArrayList();
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
        cid.clear();
        dispatch = "";
        codigo = "";
        desc = "";
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
        return (CadProcedimentos) this.cid.get(i);
    }

    /**
     * @return
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @return
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @return
     */
    public List getCid() {
        return cid;
    }

    /**
     * @param string
     */
    public void setCodigo(String string) {
        codigo = string;
    }

    /**
     * @param string
     */
    public void setDesc(String string) {
        desc = string;
    }

    /**
     * @param list
     */
    public void setCid(List list) {
        cid = list;
    }
}
