
package oscar.eform.actions;

import org.apache.struts.action.*;
import java.io.*;
import oscar.OscarProperties;
import javax.servlet.http.*;

public class DelImageAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
        String imgname = request.getParameter("filename");
        String imgpath = OscarProperties.getInstance().getProperty("eform_image");
        File image = new File(imgpath + "/" + imgname);
        image.delete();
        return mapping.findForward("success");
    }
    
}
