
package oscar.eform.upload;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import oscar.eform.upload.*;
import javax.servlet.http.*;
import oscar.OscarProperties;
import java.io.*;

public class ImageUploadAction extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
         ImageUploadForm fm = (ImageUploadForm) form;
         FormFile image = fm.getImage();
         try {
             byte[] imagebytes = image.getFileData();
             String filepath = OscarProperties.getInstance().getProperty("eform_image") + 
                               "/" + image.getFileName();
             FileOutputStream fos = new FileOutputStream(new File(filepath));
             fos.write(imagebytes);
         } catch (Exception e) { e.printStackTrace(); }
         return mapping.findForward("success");
    }
    
}
