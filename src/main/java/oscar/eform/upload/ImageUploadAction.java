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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class ImageUploadAction extends Action {
    
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "w", null)) {
			throw new SecurityException("missing required security object (_eform)");
		}
    	
         ImageUploadForm fm = (ImageUploadForm) form;
         FormFile image = fm.getImage();
         try {
             byte[] imagebytes = image.getFileData();
             OutputStream fos = ImageUploadAction.getEFormImageOutputStream(image.getFileName());
             fos.write(imagebytes);
         } catch (Exception e) { MiscUtils.getLogger().error("Error", e); }
         return mapping.findForward("success");
    }

    public static OutputStream getEFormImageOutputStream(String imageFileName) throws FileNotFoundException {
        String filepath = OscarProperties.getInstance().getProperty("eform_image") + "/" + imageFileName;
        FileOutputStream fos = new FileOutputStream(new File(filepath));
        return fos;
    }

    public static File getImageFolder() throws IOException {
        File imageFolder = new File(OscarProperties.getInstance().getProperty("eform_image") + "/");
        if (!imageFolder.exists() && !imageFolder.mkdirs()) throw new IOException("Could not create directory " + imageFolder.getAbsolutePath() + " check permissions and ensure the correct eform_image property is set in the properties file");
        return imageFolder;
    }
    
}
