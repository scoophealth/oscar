package oscar.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class FrmForm extends ActionForm
{
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        return errors;
    }
}