// form_class - a part of class name
// c_lastVisited, formId - if the form has multiple pages

package oscar.form;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import oscar.form.FrmRecord;

public final class FrmAction extends Action {
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException   {
        int newID = 0;
        FrmRecord rec = null;
        String where = "";

        try {
            FrmRecordFactory recorder = new FrmRecordFactory();
            rec = recorder.factory(request.getParameter("form_class"));
            Properties props = new Properties();

			boolean bMulPage = request.getParameter("c_lastVisited") != null ? true : false;

			if (bMulPage) {
				//copy an old record
				props = rec.getFormRecord(Integer.parseInt(request.getParameter("demographic_no")), Integer.parseInt(request.getParameter("formId")));
			}
				
			//update the current record
			String name;
			for(Enumeration enum = request.getParameterNames(); enum.hasMoreElements(); props.setProperty(name, request.getParameter(name)))
				name = (String)enum.nextElement();

			props.setProperty("provider_no", (String) request.getSession().getAttribute("user"));
			newID = rec.saveFormRecord(props);

        	String strAction = rec.findActionValue(request.getParameter("submit"));
			ActionForward af = mapping.findForward(strAction);
			where = af.getPath();
			where = rec.createActionURL(where, strAction, request.getParameter("demographic_no"), ""+newID);

        } catch(Exception ex) {
            throw new ServletException(ex);
        }

        return new ActionForward(where);
    }

}