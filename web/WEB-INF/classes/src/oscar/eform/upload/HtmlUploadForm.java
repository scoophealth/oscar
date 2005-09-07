
package oscar.eform.upload;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import javax.servlet.http.*;
import oscar.eform.EFormUtil;

public class HtmlUploadForm extends ActionForm {
    private FormFile formHtml = null;
    private String formName;
    private String subject;
    
    public HtmlUploadForm() {
    }

    public FormFile getFormHtml() {
        return formHtml;
    }    
    
    public void setFormHtml(FormFile formHtml) {
        this.formHtml = formHtml;
    }    
    
    public String getFormName() {
        return formName;
    }
    
    public void setFormName(String formName) {
        this.formName = formName;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if ((formName == null) || (formName.length() == 0)) {
            errors.add("form", new ActionError("eform.errors.file_name.missing"));
        }
        if (formHtml.getFileSize() == 0) {
            errors.add("form", new ActionError("eform.errors.form_html.missing"));
        }
        if (EFormUtil.formExistsInDB(formName)) {
            errors.add("form", new ActionError("eform.errors.form_name.exists", formName));
        }
        return(errors);
    }
    
}
