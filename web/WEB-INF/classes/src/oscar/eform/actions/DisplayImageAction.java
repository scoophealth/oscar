/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * DisplayImageAction.java
 *
 * Created on February 17, 2007, 3:32 PM
 *
 */

package oscar.eform.actions;

import java.io.File;
import java.io.IOException;

import java.io.InputStream;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DownloadAction;

import oscar.OscarProperties;

/**
 * eform_image
 * @author jay
 *and Paul
 */
public class DisplayImageAction extends DownloadAction{

    /** Creates a new instance of DisplayImageAction */
    public DisplayImageAction() {
    }
    protected StreamInfo getStreamInfo(ActionMapping mapping,
                                       ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response)
            throws Exception {
        String fileName = request.getParameter("imagefile");
        //if (fileName.indexOf('/') != -1) return null;  //prevents navigating away from the page.
        String home_dir = OscarProperties.getInstance().getProperty("eform_image");

        response.setHeader("Content-disposition","inline; filename=" + fileName);

        File file = null;
        try{
           File directory = new File(home_dir);
           if(!directory.exists()){
              throw new Exception("Directory:  "+home_dir+ " does not exist");
           }
           file = new File(directory,fileName);
           //String canonicalPath = file.getParentFile().getCanonicalPath(); //absolute path of the retrieved file
           //System.out.println("absolutepath: " + canonicalPath);
           if (!directory.equals(file.getParentFile())) {
               System.out.println("SECURITY WARNING: Illegal file path detected, client attempted to navigate away from the file directory");
               throw new Exception("Could not open file " + fileName + ".  Check the file path");
           }
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("Could not open file "+home_dir+fileName +" does "+home_dir+ " exist ?",e);
        }
        //gets content type from image extension
        String contentType = new MimetypesFileTypeMap().getContentType(file);
        /**
         * for png files (this if-statement will force every file extension with "application/octet-stream" to png)
         * This was a temporary fix.You need to look at mimetypes file to
         *
         */
        //check if the file type you are using is included
        if(contentType.equalsIgnoreCase("application/octet-stream")){
            contentType = "image/png";
            System.out.println("----hard-coded-file type:<error-check the mimetype file to see if the filetype of file: "+file.getName()+" is in there> ...");
        }
        return new FileStreamInfo(contentType, file);
    }

    public static File getImageFile(String imageFileName) throws Exception {
        String home_dir = OscarProperties.getInstance().getProperty("eform_image");

        File file = null;
        try{
           File directory = new File(home_dir);
           if(!directory.exists()){
              throw new Exception("Directory:  "+home_dir+ " does not exist");
           }
           file = new File(directory,imageFileName);
           //String canonicalPath = file.getParentFile().getCanonicalPath(); //absolute path of the retrieved file
           //System.out.println("absolutepath: " + canonicalPath);
           if (!directory.equals(file.getParentFile())) {
               System.out.println("SECURITY WARNING: Illegal file path detected, client attempted to navigate away from the file directory");
               throw new Exception("Could not open file " + imageFileName + ".  Check the file path");
           }
           return file;
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception("Could not open file "+home_dir+imageFileName +" does "+home_dir+ " exist ?",e);
        }
    }
    /**
     *
     * Process only files under dir
     * This method used to list images for eform generator
     *
     */
 public String[] visitAllFiles(File dir) {
    String[] children=null;
    if (dir.isDirectory()) {
         children = dir.list();
        for (int i=0; i<children.length; i++) {
            visitAllFiles(new File(dir, children[i]));
        }
    }
     return children;
}
}