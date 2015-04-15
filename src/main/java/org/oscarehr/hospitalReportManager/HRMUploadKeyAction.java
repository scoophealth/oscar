/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.hospitalReportManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import oscar.oscarLab.FileUploadCheck;
import oscar.oscarLab.ca.all.pageUtil.LabUploadForm;
import oscar.oscarLab.ca.all.upload.HandlerClassFactory;
import oscar.oscarLab.ca.all.upload.handlers.DefaultHandler;
import oscar.oscarLab.ca.all.util.Utilities;

public class HRMUploadKeyAction extends Action {
    Logger logger = Logger.getLogger(HRMUploadKeyAction.class);
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
        LabUploadForm frm = (LabUploadForm) form;
        FormFile importFile = frm.getImportFile();
        String filename = importFile.getFileName();
        String proNo = (String) request.getSession().getAttribute("user");
        String outcome = "failure";
        
        try{
            InputStream is = importFile.getInputStream();
            
            String type = request.getParameter("type");
           
            
            String filePath = Utilities.saveFile(is, filename);
            is.close();
            File file = new File(filePath);
            
            is = new FileInputStream(filePath);
            int checkFileUploadedSuccessfully = FileUploadCheck.addFile(file.getName(),is,proNo);            
            is.close();
          
            if (checkFileUploadedSuccessfully != FileUploadCheck.UNSUCCESSFUL_SAVE){
                logger.debug("filePath"+filePath);
                logger.debug("Type :"+type);
                
                DefaultHandler defaultHandler =  HandlerClassFactory.getDefaultHandler();
                if(defaultHandler != null) {
                	logger.debug("MESSAGE HANDLER "+defaultHandler.getClass().getName());
                }
                if((defaultHandler.readTextFile(filePath))!= null) {
                	outcome = "success";
                }
                else{
	                outcome = "uploaded previously";
	            } 
            } 
            request.setAttribute("filePath", filePath);
            request.setAttribute("type", type);
        }catch(Exception e){
            logger.error("Error: "+e);
            outcome = "exception";
        }
        
        request.setAttribute("outcome", outcome);
       
        return mapping.findForward("success");
    }
    
    
}
