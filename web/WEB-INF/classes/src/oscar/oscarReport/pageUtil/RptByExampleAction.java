package oscar.oscarReport.pageUtil;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import java.util.Properties;
import oscar.oscarReport.data.*;
public class RptByExampleAction extends Action {
Properties oscarVariables = null;
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
//   HttpSession session = request.getSession();
//           oscarVariables = (Properties)  session.getAttribute("oscarVariables" );

//           RptByExampleData rbed = new RptByExampleData();
//           rbed.setVariables(oscarVariables);

           RptByExampleForm frm = (RptByExampleForm)form;


        return mapping.findForward("success");
    }
}

