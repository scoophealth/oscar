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
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {   
        RptByExampleForm frm = (RptByExampleForm) form;
        
        if(request.getSession().getValue("user") == null)
            response.sendRedirect("../logout.htm");        

        String bgcolor = "#ddddff";
        String sql = frm.getSql()==null?"":frm.getSql();
        String pros = "";

        oscar.oscarReport.data.RptByExampleData exampleData  = new oscar.oscarReport.data.RptByExampleData();
        Properties proppies = (Properties)  request.getSession().getAttribute("oscarVariables");


        String results = exampleData.exampleReportGenerate(sql, proppies)==null?null: exampleData.exampleReportGenerate(sql, proppies);
        String resultText = exampleData.exampleTextGenerate(sql, proppies)==null?null: exampleData.exampleTextGenerate(sql, proppies);

        request.setAttribute("results", results);
        request.setAttribute("resultText", resultText);
        
        return mapping.findForward("success");
    }
}

