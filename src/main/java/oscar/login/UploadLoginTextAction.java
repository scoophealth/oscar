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


package oscar.login;

import java.io.FileOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.oscarehr.util.MiscUtils;

import oscar.oscarLab.ca.bc.PathNet.pageUtil.LabUploadForm;


/**
 *
 * @author rjonasz
 */
public class UploadLoginTextAction extends Action {
    Logger _logger = Logger.getLogger(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
       InputStream fis = null;
       FileOutputStream fos = null;
       LabUploadForm frm = (LabUploadForm) form;
       FormFile importFile = frm.getImportFile();
       boolean error = true;

       try {
            if( importFile.getFileName().length() > 0 ) {
                fis = importFile.getInputStream();
                String savePath = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + "/OSCARloginText.txt";
                fos = new FileOutputStream(savePath);
                byte[] buf = new byte[128*1024];
                int i = 0;
                while ((i = fis.read(buf)) != -1) {
                    fos.write(buf, 0, i);
                }
                error = false;
            }
       } catch( Exception e) {
           MiscUtils.getLogger().error("Error", e);           
       }

       request.setAttribute("error", error);
       return mapping.findForward("success");
    }
}
