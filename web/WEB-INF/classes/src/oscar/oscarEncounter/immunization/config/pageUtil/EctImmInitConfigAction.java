package oscar.oscarEncounter.immunization.config.pageUtil;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

public final class EctImmInitConfigAction extends Action
{

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);
        ActionErrors errors = new ActionErrors();
        return mapping.findForward("success");
    }
}
