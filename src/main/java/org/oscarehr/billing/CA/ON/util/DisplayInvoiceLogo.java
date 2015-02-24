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

package org.oscarehr.billing.CA.ON.util;

import java.io.File;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DownloadAction;
import org.oscarehr.common.dao.DocumentDao;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public class DisplayInvoiceLogo extends DownloadAction{

	@Override
	protected StreamInfo getStreamInfo(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String fileName = getLogoImgAbsPath();
		if (fileName.isEmpty()) {
			throw new Exception("Can't open the file: " + fileName);
		}
        
        response.setHeader("Content-disposition","inline; filename=" + fileName);
        File file = new File(fileName);
        //gets content type from image extension
        String contentType = new MimetypesFileTypeMap().getContentType(file);
        /**
         * For encoding file types not included in the mimetypes file
         * You need to look at mimetypes file to check if the file type you are using is included
         *
         */
         try{
                if(extension(file.getName()).equalsIgnoreCase("png")){ // for PNG
                    contentType = "image/png";
                }else if(extension(file.getName()).equalsIgnoreCase("jpeg")||
                        extension(file.getName()).equalsIgnoreCase("jpe")||
                        extension(file.getName()).equalsIgnoreCase("jpg")){ //for JPEG,JPG,JPE
                    contentType = "image/jpeg";
                }else if(extension(file.getName()).equalsIgnoreCase("bmp")){ // for BMP
                    contentType = "image/bmp";
                }else if(extension(file.getName()).equalsIgnoreCase("cod")){ // for COD
                    contentType = "image/cis-cod";
                }else if(extension(file.getName()).equalsIgnoreCase("ief")){ // for IEF
                    contentType = "image/ief";
                }else if(extension(file.getName()).equalsIgnoreCase("jfif")){ // for JFIF
                    contentType = "image/pipeg";
                }else if(extension(file.getName()).equalsIgnoreCase("svg")){ // for SVG
                    contentType = "image/svg+xml";
                }else if(extension(file.getName()).equalsIgnoreCase("tiff")||
                         extension(file.getName()).equalsIgnoreCase("tif")){ // for TIFF or TIF
                    contentType = "image/tiff";
                }else if(extension(file.getName()).equalsIgnoreCase("pbm")){ // for PBM
                    contentType = "image/x-portable-bitmap";
                }else if(extension(file.getName()).equalsIgnoreCase("pnm")){ // for PNM
                    contentType = "image/x-portable-anymap";
                }else if(extension(file.getName()).equalsIgnoreCase("pgm")){ // for PGM
                    contentType = "image/x-portable-greymap";
                }else if(extension(file.getName()).equalsIgnoreCase("ppm")){ // for PPM
                    contentType = "image/x-portable-pixmap";
                }else if(extension(file.getName()).equalsIgnoreCase("xbm")){ // for XBM
                    contentType = "image/x-xbitmap";
                }else if(extension(file.getName()).equalsIgnoreCase("xpm")){ // for XPM
                    contentType = "image/x-xpixmap";
                }else if(extension(file.getName()).equalsIgnoreCase("xwd")){ // for XWD
                    contentType = "image/x-xwindowdump";
                }else if(extension(file.getName()).equalsIgnoreCase("rgb")){ // for RGB
                    contentType = "image/x-rgb";
                }else if(extension(file.getName()).equalsIgnoreCase("ico")){ // for ICO
                    contentType = "image/x-icon";
                }else if(extension(file.getName()).equalsIgnoreCase("cmx")){ // for CMX
                    contentType = "image/x-cmx";
                }else if(extension(file.getName()).equalsIgnoreCase("ras")){ // for RAS
                    contentType = "image/x-cmu-raster";
                }else if(extension(file.getName()).equalsIgnoreCase("gif")){ // for GIF
                    contentType = "image/gif";
                }else if(extension(file.getName()).equalsIgnoreCase("js")){ // for GIF
                    contentType = "text/javascript";
                }else if(extension(file.getName()).equalsIgnoreCase("css")){ // for GIF
                    contentType = "text/css";
                }else if(extension(file.getName()).equalsIgnoreCase("rtl") || extension(file.getName()).equalsIgnoreCase("html") || extension(file.getName()).equalsIgnoreCase("htm")){ // for HTML
                    contentType = "text/html";
                }else{
                    throw new Exception("please check the file type or update mimetypes.default file to include the "+"."+extension(file.getName()));
                }
            }catch(Exception e){MiscUtils.getLogger().error("Error", e);
                throw new Exception("Could not open file "+file.getName()+" wrong file extension, ",e);
            }
        

        return new FileStreamInfo(contentType, file);
	}

	public String extension(String f) {
		int dot = f.lastIndexOf(".");
		return f.substring(dot + 1);
	}
	
	public static String getLogoImgAbsPath() {
		String fileName = "";
		String logoDocType = OscarProperties.getInstance().getProperty("invoice_head_logo_doctype");
		if (logoDocType == null || logoDocType.trim().isEmpty()) {
			logoDocType = "invoice letterhead";
		}
		logoDocType = logoDocType.trim();
		DocumentDao docDao = (DocumentDao)SpringUtils.getBean("documentDao");
		if (docDao == null) {
			MiscUtils.getLogger().info("Can't get DocumentDAO bean");
			return fileName;
		}
		List<org.oscarehr.common.model.Document> docList = docDao.findByDoctype(logoDocType);
		if (docList == null || docList.size() < 1) {
			MiscUtils.getLogger().info("Can't get document according to doctype: " + logoDocType);
			return fileName;
		}
		
		org.oscarehr.common.model.Document doc = docList.get(docList.size() - 1);
		if (doc != null) {
			fileName = doc.getDocfilename();
		}
		
        String document_dir = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
        
        File file = null;
        try{
           File directory = new File(document_dir);
           if(!directory.exists()){
              MiscUtils.getLogger().info("Directory:  " + document_dir+ " does not exist");
              return fileName;
           }
           if (document_dir.lastIndexOf(File.separator) != (document_dir.length() - 1)) {
        	   fileName = document_dir + File.separator + fileName;
           } else {
        	   fileName = document_dir + fileName;
           }
           
           file = new File(fileName);
           if (!file.exists()) {
        	   MiscUtils.getLogger().info("File: " + fileName);
        	   return "";
           }
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        
        return fileName;
	}
}
