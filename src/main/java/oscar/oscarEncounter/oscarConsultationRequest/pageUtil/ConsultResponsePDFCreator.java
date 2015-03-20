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

package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.WKHtmlToPdfUtils;

import oscar.OscarProperties;

public class ConsultResponsePDFCreator {
	private static Logger logger = MiscUtils.getLogger();
	
	public static String create(String consultResponseHtmlPage) {
		String tmpDir = OscarProperties.getInstance().getProperty("TMP_DIR");
		String filename = tmpDir + "/ConsultResponse" + System.currentTimeMillis();
		
		//save consultResponse as .html
		File fileHtml = new File(filename+".html");
        try {
        	FileOutputStream fos = new FileOutputStream(fileHtml);
			byte[] pageInBytes = consultResponseHtmlPage.getBytes();
			fos.write(pageInBytes);
			fos.flush();
			fos.close();
        } catch (Exception ex) {
        	logger.error("Error saving html", ex);
        }
		
		//convert consultResponse.html to pdf
        File filePDF = new File(filename+".pdf");
        try {
	        WKHtmlToPdfUtils.convertToPdf(fileHtml.getPath(), filePDF);
        } catch (IOException ex) {
        	logger.error("Error saving pdf", ex);
        }
		
		//mark temporary files for clean-up afterwards
        fileHtml.deleteOnExit();
        filePDF.deleteOnExit();
		
		return filePDF.getPath();
	}
}
