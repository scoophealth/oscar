/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarReport.pageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

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

    private static Logger log = MiscUtils.getLogger();
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    

    /**
     * Creates a new instance of GeneratePatientLettersAction
     */
    public GeneratePatientLettersAction() {

    }

     public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {

    	 if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_report", "r", null)) {
     		  throw new SecurityException("missing required security object (_report)");
     	  	}
    	 
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

        String[] reportParams = ManageLetters.getReportParams(jasperReport);
        APExecute apExe = new APExecute();
        if (log.isTraceEnabled()) { log.trace("Compiled Jasper Report ");}

        ArrayList<Object> fullPatientlist = new ArrayList<Object>();

        //for each demographic generate a letter for that patient
        for (int i =0; i < demos.length; i++){
           //fill the map with patient info
           if (log.isTraceEnabled()) { log.trace("Getting demographic info for "+demos[i]);}

           HashMap parameters = new HashMap();
              if ( reportParams != null ){
                   for (int p = 0; p < reportParams.length; p++){
                       MiscUtils.getLogger().debug("demo = "+demos[i]);
                       parameters.put(reportParams[p],apExe.execute(reportParams[p],demos[i]));
                   }
              }

           try{

                if (log.isTraceEnabled()) { log.trace("Filling report for "+demos[i]);}
                JasperPrint print =  JasperFillManager.fillReport(jasperReport,parameters, new JREmptyDataSource());

                String description = letterData.get("ID")+"-"+letterData.get("report_name");
                String type = "others";
                String fileName = letterData.get("ID")+"-"+StringUtils.replace((String)letterData.get("report_name")," ","-")+"-"+demos[i]+".pdf";
                String html = "";
                char status = 'A';
                String observationDate = UtilDateUtilities.DateToString(new Date());
                String module = "demographic";
                String moduleId = demos[i];

                EDoc newDoc = new EDoc(description, type, fileName, "", providerNo, providerNo, "", status, observationDate, "", "", module, moduleId);
                newDoc.setDocPublic("0");
                newDoc.setContentType("application/pdf");
                
                // if the document was added in the context of a program
        		ProgramManager2 programManager = SpringUtils.getBean(ProgramManager2.class);
        		LoggedInInfo loggedInInfo  = LoggedInInfo.getLoggedInInfoFromSession(request);
        		ProgramProvider pp = programManager.getCurrentProgramInDomain(loggedInInfo, loggedInInfo.getLoggedInProviderNo());
        		if(pp != null && pp.getProgramId() != null) {
        			newDoc.setProgramId(pp.getProgramId().intValue());
        		}

                fileName = newDoc.getFileName();
                String savePath = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + "/" + fileName;
                if (log.isTraceEnabled()) { log.trace("writing report to disk location "+savePath);}
                JasperExportManager.exportReportToPdfFile(print, savePath);
                if (log.isTraceEnabled()) { log.trace("Saving reference to database for"+demos[i]);}
                EDocUtil.addDocumentSQL(newDoc);

                fullPatientlist.add(savePath);

            }catch(Exception  jpException){
            	MiscUtils.getLogger().error("Error", jpException);
            }

        }


        //LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_JASPERREPORTLETER, demographic$, request.getRemoteAddr());
        manageLetters.logLetterCreated( providerNo, id,demos);
        MiscUtils.getLogger().debug("Add Follow Up "+request.getParameter("addFollowUp"));
        if (request.getParameter("addFollowUp") != null && request.getParameter("addFollowUp").equals("ON")){
        //MARK IN MEASUREMENTS????
            MiscUtils.getLogger().debug("IN MARK MEASUREMENTS");
            String followUpType =  request.getParameter("followupType");//"FLUF";
            String followUpValue = request.getParameter("followupValue"); //"L1";
            String comment = request.getParameter("message");
            MiscUtils.getLogger().debug("Follow up type "+followUpType+" follow up value "+followUpValue);
            if ( followUpType != null && followUpValue != null){
                FollowupManagement fup = new FollowupManagement();
                fup.markFollowupProcedure(followUpType,followUpValue,demos,providerNo,new Date(),comment);
            }
        }

        response.setHeader("Content-disposition", "inline; filename=GeneratedLetters.pdf");
        response.setHeader("Cache-Control", "max-age=0");
        response.setDateHeader("Expires", 0);
        response.setContentType("application/pdf");


        try {
            sos = response.getOutputStream();
        }catch (IOException ex) {MiscUtils.getLogger().error("Error", ex);
        }


        ConcatPDF.concat(fullPatientlist,sos);

        if (log.isTraceEnabled()) { log.trace("End of GeneratePatientLetters Action");}
        return null;
     }

}
