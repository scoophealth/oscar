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
            
            Utilities u = new Utilities();
            String filePath = u.saveFile(is, filename);
            is.close();
            File file = new File(filePath);
            
            is = new FileInputStream(filePath);
            FileUploadCheck fileC = new FileUploadCheck();
            int checkFileUploadedSuccessfully = fileC.addFile(file.getName(),is,proNo);            
            is.close();
            
            if (checkFileUploadedSuccessfully != FileUploadCheck.UNSUCCESSFUL_SAVE){
                logger.info("filePath"+filePath);
                HandlerClassFactory f = new HandlerClassFactory();
                MessageHandler msgHandler = f.getHandler(type);
                if((msgHandler.parse(filePath,checkFileUploadedSuccessfully)) != null)
                    outcome = "success";
                
            }else{
                outcome = "uploaded previously";
            }
            
        }catch(Exception e){
            logger.error("Error: "+e);
            outcome = "exception";
        }
        
        request.setAttribute("outcome", outcome);
        return mapping.findForward("success");
    }
    
    
    public InsideLabUploadAction() {
        
    }
}
