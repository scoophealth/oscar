package oscar.form.study;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import oscar.form.study.FrmStudyRecord;

public final class FrmStudyAction extends Action {
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException   {
        int newID = 0;
        FrmStudyRecord rec = null;
        String where = "";
        try {
            FrmStudyRecordFactory recorder = new FrmStudyRecordFactory();
            rec = recorder.factory(request.getParameter("study_name"));
            Properties props = new Properties();
            String name;
            for(Enumeration enum = request.getParameterNames(); enum.hasMoreElements(); props.setProperty(name, request.getParameter(name)))
                name = (String)enum.nextElement();

            newID = rec.saveFormRecord(props);
        
						String strAction = rec.findActionValue(request.getParameter("submit"));
		        ActionForward af = mapping.findForward(strAction);
				    where = af.getPath();
						where = rec.createActionURL(where, strAction, request.getParameter("demographic_no"), ""+newID, request.getParameter("study_no"), request.getParameter("study_link"));
            //System.out.println(" xxxxxxxxxxxxxxxxxx7");
        } catch(Exception ex) {
            throw new ServletException(ex);
        }

        return new ActionForward(where);
    }



/*        int newID = 0;
        try {
            EctType2DiabetesRecord rec = new EctType2DiabetesRecord();
            Properties props = new Properties();
            String name;
            for(Enumeration enum = request.getParameterNames(); enum.hasMoreElements(); props.setProperty(name, request.getParameter(name)))
                name = (String)enum.nextElement();

            newID = rec.saveType2DiabetesRecord(props);
        } catch(SQLException ex) {
            throw new ServletException(ex);
        }
        String where = "";
        if(request.getParameter("submit").equalsIgnoreCase("print")) {
            ActionForward af = mapping.findForward("print");
            where = af.getPath();
            where = where+"?demoNo="+request.getParameter("demographic_no")+"&formId="+newID;
        } else if(request.getParameter("submit").equalsIgnoreCase("save")) {
            ActionForward af = mapping.findForward("save");
            where = af.getPath();
            where = where+"?demographic_no="+request.getParameter("demographic_no")+"&formId="+newID;
        } else if(request.getParameter("submit").equalsIgnoreCase("exit")) {
            ActionForward af = mapping.findForward("exit");
            where = af.getPath();
        } else {
            ActionForward af = mapping.findForward("failure");
            where = af.getPath();
        }
        return new ActionForward(where);
    }
*/
}