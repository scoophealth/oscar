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
import java.util.Collection;
import oscar.oscarReport.data.*;
import oscar.oscarReport.bean.*;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.pageUtil.EctSessionBean;

public class RptByExampleAction extends Action {

    Properties oscarVariables = null;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {   
        RptByExampleForm frm = (RptByExampleForm) form;        
        
        if(request.getSession().getAttribute("user") == null)
            response.sendRedirect("../logout.htm");        
               
        String providerNo = (String) request.getSession().getAttribute("user");
        RptByExampleQueryBeanHandler hd = new RptByExampleQueryBeanHandler();  
        Collection favorites = hd.getFavoriteCollection(providerNo);       
        request.setAttribute("favorites", favorites);
        
                
        String bgcolor = "#ddddff";
        String sql = frm.getSql();
        String pros = "";
        
        if (sql!= null){
            write2Database(sql, providerNo);
        }
        else
            sql = "";
        
        oscar.oscarReport.data.RptByExampleData exampleData  = new oscar.oscarReport.data.RptByExampleData();
        Properties proppies = (Properties)  request.getSession().getAttribute("oscarVariables");

        String results = exampleData.exampleReportGenerate(sql, proppies)==null?null: exampleData.exampleReportGenerate(sql, proppies);
        String resultText = exampleData.exampleTextGenerate(sql, proppies)==null?null: exampleData.exampleTextGenerate(sql, proppies);

        request.setAttribute("results", results);
        request.setAttribute("resultText", resultText);
        
        return mapping.findForward("success");
    }
    
    public void write2Database(String query, String providerNo){
        if (query!=null && query.compareTo("")!=0){
            try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                oscar.oscarReport.data.RptByExampleData exampleData  = new oscar.oscarReport.data.RptByExampleData();

                query = exampleData.replaceSQLString (";","",query);
                query = exampleData.replaceSQLString("\"", "\'", query);            

                String sql = "INSERT INTO reportByExamples(providerNo, query, date) VALUES('" + providerNo + "','" + query + "', NOW())";
                db.RunSQL(sql);

                db.CloseConn();
            }
            catch(SQLException e) {
                System.out.println(e.getMessage());            
            }
        }
    }
}

