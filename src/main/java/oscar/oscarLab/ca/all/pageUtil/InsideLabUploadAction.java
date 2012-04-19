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


/*
 * InsideLabUploadAction.java
 *
 * Created on June 28, 2007, 1:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.pageUtil;

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
import oscar.oscarLab.ca.all.upload.HandlerClassFactory;
import oscar.oscarLab.ca.all.upload.handlers.MessageHandler;
import oscar.oscarLab.ca.all.util.Utilities;

public class InsideLabUploadAction extends Action {
    Logger logger = Logger.getLogger(InsideLabUploadAction.class);
    
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
            if (type.equals("OTHER"))
                type = request.getParameter("otherType");
            
            String filePath = Utilities.saveFile(is, filename);
            is.close();
            File file = new File(filePath);
            
            is = new FileInputStream(filePath);
            int checkFileUploadedSuccessfully = FileUploadCheck.addFile(file.getName(),is,proNo);            
            is.close();
            
            if (checkFileUploadedSuccessfully != FileUploadCheck.UNSUCCESSFUL_SAVE){
                logger.info("filePath"+filePath);
                logger.info("Type :"+type);
                MessageHandler msgHandler = HandlerClassFactory.getHandler(type);
                if(msgHandler != null){
                   logger.info("MESSAGE HANDLER "+msgHandler.getClass().getName());
                }
                if((msgHandler.parse(getClass().getSimpleName(), filePath,checkFileUploadedSuccessfully)) != null)
                    outcome = "success";
                
            }else{
                outcome = "uploaded previously";
            }
            
        }catch(Exception e){
            logger.error("Error: ",e);
            outcome = "exception";
        }
        
        request.setAttribute("outcome", outcome);
        return mapping.findForward("success");
    }
}
