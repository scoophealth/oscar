package oscar.oscarReport.pageUtil;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import java.util.Properties;
import oscar.oscarReport.data.*;
import oscar.util.*;
import oscar.oscarEncounter.oscarMeasurements.pageUtil.EctValidation;

public class ObecAction extends Action {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        Properties proppies = (Properties)  request.getSession().getAttribute("oscarVariables");

        ObecForm frm = (ObecForm)form;
        ObecData obecData1 = new ObecData();
        DateUtils dateUtils = new DateUtils();
        EctValidation validation = new EctValidation();
        ActionErrors errors = new ActionErrors();  
        
        String startDate = frm.getXml_vdate()==null?"":frm.getXml_vdate();
        if(!validation.isDate(startDate)){        
                System.out.println("Invalid date format!");
                errors.add(startDate,
                new ActionError("errors.invalid", "StartDate"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));            
        }        
        
        int numDays = frm.getNumDays();
        int startYear = 0;
        int startMonth = 0;
        int startDay = 0;
        
        int slashIndex1 = startDate.indexOf("-");
        if(slashIndex1>=0){
            startYear = Integer.parseInt(startDate.substring(0,slashIndex1));
            int slashIndex2 = startDate.indexOf("-", slashIndex1+1);
            if (slashIndex2>slashIndex1){
                startMonth = Integer.parseInt(startDate.substring(slashIndex1+1, slashIndex2));
                int length = startDate.length();
                startDay = Integer.parseInt(startDate.substring(slashIndex2+1, length)); 
            }            
        }
        
        
        
        String endDate = dateUtils.NextDay(startDay, startMonth, startYear, numDays);
        
        String obectxt = obecData1.generateOBEC(startDate, endDate, proppies)==null?"":obecData1.generateOBEC(startDate, endDate, proppies);
        request.setAttribute("obectxt", obectxt);

        return mapping.findForward("success");
    }
}

