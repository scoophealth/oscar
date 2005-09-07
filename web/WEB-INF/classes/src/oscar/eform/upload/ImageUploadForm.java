
package oscar.eform.upload;

import org.apache.struts.upload.*;
import org.apache.struts.action.*;
import javax.servlet.http.*;
import java.io.*;
import oscar.OscarProperties;

public class ImageUploadForm extends ActionForm {
    private FormFile image = null;
    
    public ImageUploadForm() {
    }
    
    public void setImage(FormFile image) {
        this.image = image;
    }
    
    public FormFile getImage() {
        return image;
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (image.getFileSize() == 0) {
            errors.add("image", new ActionError("eform.uploadimages.imageMissing"));
        }
        String serverImagePath = OscarProperties.getInstance().getProperty("eform_image") + 
                                "/" + image.getFileName();
        File testimage = new File(serverImagePath);
        if (testimage.exists()) {
            errors.add("image", new ActionError("eform.uploadimages.imageAlreadyExists", image.getFileName()));
        }
        return errors;
    }
}
