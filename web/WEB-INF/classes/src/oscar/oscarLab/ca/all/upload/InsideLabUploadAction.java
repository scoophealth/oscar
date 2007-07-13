/*
 * InsideLabUploadAction.java
 *
 * Created on June 28, 2007, 1:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.upload;

import javax.servlet.http.*;
import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import oscar.oscarLab.ca.all.upload.handlers.MessageHandler;
import oscar.oscarLab.ca.all.util.Utilities;

public class InsideLabUploadAction extends Action {
    Logger logger = Logger.getLogger(InsideLabUploadAction.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
        LabUploadForm frm = (LabUploadForm) form;
        FormFile importFile = frm.getImportFile();
        request.setAttribute("outcome", "failure");
        
        try{
            
            String type = request.getParameter("type");
            if (type.equals("OTHER"))
                type = request.getParameter("otherType");
            
            Utilities u = new Utilities();
            String filePath = u.saveFile(importFile.getInputStream(), importFile.getFileName());
            
            MessageHandler msgHandler = HandlerClassFactory.getInstance().getHandler(type);
            if((msgHandler.parse(filePath)) != null)
                request.setAttribute("outcome", "success");
            
        }catch(Exception e){
            logger.error("Error: "+e);
        }
        
        return mapping.findForward("success");
    }
    
    
    public InsideLabUploadAction() {
        
    }
}
