package oscar.oscarReport.pageUtil;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

public class RptByExamplesAllFavoritesAction extends Action {

    Properties oscarVariables = null;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {   
        RptByExamplesAllFavoritesForm frm = (RptByExamplesAllFavoritesForm) form;     
        String providerNo = (String) request.getSession().getAttribute("user");
        RptByExampleQueryBeanHandler hd = new RptByExampleQueryBeanHandler(providerNo);  
        request.setAttribute("allFavorites", hd);      
        return mapping.findForward("success");        
    }        
        
}

