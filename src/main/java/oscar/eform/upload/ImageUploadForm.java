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


package oscar.eform.upload;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

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
            errors.add("image", new ActionMessage("eform.uploadimages.imageMissing"));
        }
        
        String serverImagePath = OscarProperties.getInstance().getProperty("eform_image") + "/" + image.getFileName();
        File testimage = new File(serverImagePath);
        
        if (testimage.exists()) {
            errors.add("image", new ActionMessage("eform.uploadimages.imageAlreadyExists", image.getFileName()));
        }
        
        if(errors.size()==0){
        	request.setAttribute("status", "success");
        }
        
        return errors;
    }
}
