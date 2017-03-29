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


package oscar.oscarBilling.ca.bc.pageUtil;

import java.io.File;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DownloadAction;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;


/**
 * Processes downloading files
 * @author jay
 */
public class DownloadFileAction extends DownloadAction{
    
    /** Creates a new instance of DownloadFileAction */
    public DownloadFileAction() {
    }
    
    protected StreamInfo getStreamInfo(ActionMapping mapping, 
                                       ActionForm form,
                                       HttpServletRequest request, 
                                       HttpServletResponse response)
            throws Exception {
        //TODO:NEED TO CHECK TO SEE IF THEY HAVE admin or admin.billing privies
        
        String contentType = "application/octet-stream";
        String home_dir = OscarProperties.getInstance().getProperty("HOME_DIR");
        String fileName = request.getParameter("filename");
        
        response.setHeader("Content-disposition", 
                           "attachment; filename=" + URLEncoder.encode(fileName,"UTF-8"));
        File file = null;
        try{
           File directory = new File(home_dir);
           if(!directory.exists()){
              throw new Exception("Directory:  "+home_dir+ " does not exist");
           }
           file = new File(directory,fileName);
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
            throw new Exception("Could not open file "+home_dir+fileName +" does "+home_dir+ " exist ?",e);
        }
        return new FileStreamInfo(contentType, file);   
    }   
}
