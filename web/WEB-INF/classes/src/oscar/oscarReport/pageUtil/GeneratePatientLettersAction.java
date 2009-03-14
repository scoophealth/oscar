/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * GeneratePatientLettersAction.java
 *
 * Created on October 13, 2006, 11:55 AM
 *
 */

package oscar.oscarReport.pageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.eform.APExecute;
import oscar.oscarPrevention.reports.FollowupManagement;
import oscar.oscarReport.data.ManageLetters;
import oscar.util.ConcatPDF;
import oscar.util.UtilDateUtilities;

/**

 * @author jay
 */
public class GeneratePatientLettersAction extends Action {
    
    private static Log log = LogFactory.getLog(GeneratePatientLettersAction.class);
    
    /**
     * Creates a new instance of GeneratePatientLettersAction
     */
    public GeneratePatientLettersAction() {
        
    }
    
     public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
    
        String classpath = (String) request.getSession().getServletContext().getAttribute("org.apache.catalina.jsp_classpath");
        System.setProperty("jasper.reports.compile.class.path", classpath); 
         
        String[] demos = request.getParameterValues("demos");
        String id = request.getParameter("reportLetter");
        String providerNo =(String) request.getSession().getAttribute("user");
         
        if (log.isTraceEnabled()) { 
            if (demos == null){
                log.trace("demos was null");
            }else{
                log.trace("# of demos "+demos.length);
            }
        }
        
        ServletOutputStream sos = null; 
        //OscarDocumentCreator osc = new OscarDocumentCreator();
        
        //Get Jasper Report 
        //InputStream ins = this.getClass().getClassLoader().getResourceAsStream("oscar/oscarBilling/ca/bc/reports/MyLetter.jrxml");
        
        if (log.isTraceEnabled()) { log.trace("Getting xml configuration stream ");}
        ManageLetters manageLetters = new ManageLetters();
        JasperReport  jasperReport =   manageLetters.getReport( id);//osc.getJasperReport(ins);
        
        Hashtable letterData = manageLetters.getReportData(id);
        
        String[] reportParams = manageLetters.getReportParams(jasperReport);
        APExecute apExe = new APExecute();
        if (log.isTraceEnabled()) { log.trace("Compiled Jasper Report ");}
        
        ArrayList fullPatientlist = new ArrayList(); 
        
        //for each demographic generate a letter for that patient 
        for (int i =0; i < demos.length; i++){
           //fill the map with patient info 
           if (log.isTraceEnabled()) { log.trace("Getting demographic info for "+demos[i]);}
           
           HashMap parameters = new HashMap();
              if ( reportParams != null ){
                   for (int p = 0; p < reportParams.length; p++){
                       System.out.println("demo = "+demos[i]);
                       parameters.put(reportParams[p],apExe.execute(reportParams[p],demos[i]));
                   }
              }
            
           try{ 
                
                if (log.isTraceEnabled()) { log.trace("Filling report for "+demos[i]);}
                JasperPrint print =  JasperFillManager.fillReport(jasperReport,(Map) parameters, new JREmptyDataSource()); 
                
                String description = letterData.get("ID")+"-"+letterData.get("report_name");
                String type = "others";
                String fileName = letterData.get("ID")+"-"+StringUtils.replace((String)letterData.get("report_name")," ","-")+"-"+demos[i]+".pdf";
                String html = "";
                char status = 'A';
                String observationDate = UtilDateUtilities.DateToString(UtilDateUtilities.now()); 
                String module = "demographic";
                String moduleId = demos[i];

                EDoc newDoc = new EDoc(description, type, fileName, "", providerNo, providerNo, "", status, observationDate, "", "", module, moduleId);
                newDoc.setDocPublic("0");
                newDoc.setContentType("application/pdf");
                
                fileName = newDoc.getFileName();
                String savePath = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + "/" + fileName;
                if (log.isTraceEnabled()) { log.trace("writing report to disk location "+savePath);}
                JasperExportManager.exportReportToPdfFile(print, savePath); 
                if (log.isTraceEnabled()) { log.trace("Saving reference to database for"+demos[i]);}
                EDocUtil.addDocumentSQL(newDoc);
                
                fullPatientlist.add(savePath);

            }catch(Exception  jpException){
               jpException.printStackTrace();
            }
           
        }
        
        
        //LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_JASPERREPORTLETER, demographic$, request.getRemoteAddr());
        manageLetters.logLetterCreated( providerNo, id,demos);
        System.out.println("Add Follow Up "+request.getParameter("addFollowUp"));
        if (request.getParameter("addFollowUp") != null && request.getParameter("addFollowUp").equals("ON")){
        //MARK IN MEASUREMENTS????
            System.out.println("IN MARK MEASUREMENTS");
            String followUpType =  request.getParameter("followupType");//"FLUF";
            String followUpValue = request.getParameter("followupValue"); //"L1";
            String comment = request.getParameter("message");
            System.out.println("Follow up type "+followUpType+" follow up value "+followUpValue);
            if ( followUpType != null && followUpValue != null){
                FollowupManagement fup = new FollowupManagement();
                fup.markFollowupProcedure(followUpType,followUpValue,demos,providerNo,UtilDateUtilities.now(),comment);
            }
        }
        
        response.setHeader("Content-disposition", "inline; filename=GeneratedLetters.pdf");    
        response.setHeader("Cache-Control", "max-age=0");
        response.setDateHeader("Expires", 0);
        response.setContentType("application/pdf");
        
        
        try {
            sos = response.getOutputStream();
        }catch (IOException ex) {
            ex.printStackTrace();
        }

 
        ConcatPDF.concat(fullPatientlist,sos);
        
        if (log.isTraceEnabled()) { log.trace("End of GeneratePatientLetters Action");}
        return null;   
     }
   
}