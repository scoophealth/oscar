
package oscar.eform.upload;

import java.io.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import javax.servlet.*;
import javax.servlet.http.*;
import oscar.eform.EFormUtil;

public class HtmlUploadAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) {
        HtmlUploadForm fm = (HtmlUploadForm) form;
        FormFile formHtml = fm.getFormHtml();
        try {
            String formHtmlStr = readStream(formHtml);
            String formName = fm.getFormName();
            String subject = fm.getSubject();
            String fileName = formHtml.getFileName();
            EFormUtil.saveEForm(formName, subject, fileName, formHtmlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return(mapping.findForward("success"));
    }
    
    private String readStream(FormFile file) {
        try {
            InputStream is = file.getInputStream();
            int pointer;
            StringBuffer strb = new StringBuffer(file.getFileSize());
            while ((pointer = is.read()) != -1) {
                strb.append((char) pointer);
            }
            return(strb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return("");
    }
    
}
