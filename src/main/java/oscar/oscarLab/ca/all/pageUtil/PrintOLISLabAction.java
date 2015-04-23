/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package oscar.oscarLab.ca.all.pageUtil;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WKHtmlToPdfUtils;

import oscar.OscarProperties;
import oscar.util.UtilDateUtilities;

import com.sun.xml.messaging.saaj.util.ByteOutputStream;

public class PrintOLISLabAction extends Action {

	private static final Logger logger = MiscUtils.getLogger();

	private static String localUri = null;
	
	private HttpServletResponse response;
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_lab", "r", null)) {
			throw new SecurityException("missing required security object (_lab)");
		}
		
		this.response = response;
		// segmentID=2&providerNo=999998&searchProviderNo=0&status=U&showLatest=true
		String segmentID = request.getParameter("segmentID"); 
		String searchProviderNo  = request.getParameter("searchProviderNo");
		String providerNo = request.getParameter("providerNo");
		String status = request.getParameter("status");
		String showLatest = request.getParameter("showLatest");
		String labName = request.getParameter("labName");
		localUri = getLabRequestUrl(request, segmentID, searchProviderNo, providerNo, status, showLatest);
		
		printLab(labName, segmentID, searchProviderNo, providerNo, status, showLatest);
		
		return mapping.findForward("success");
	}
	
	/**
	 * This method is a copy of Apache Tomcat's ApplicationHttpRequest getRequestURL method with the exception that the uri is removed and replaced with our eform viewing uri. Note that this requires that the remote url is valid for local access. i.e. the
	 * host name from outside needs to resolve inside as well. The result needs to look something like this : https://127.0.0.1:8443/oscar/eformViewForPdfGenerationServlet?fdid=2&parentAjaxId=eforms
	 */
	private String getLabRequestUrl(HttpServletRequest request, String segmentID, String searchProviderNo, String providerNo, String status, String showLatest) {
		StringBuilder url = new StringBuilder();
		String scheme = request.getScheme();
		Integer port = new Integer(OscarProperties.getInstance().getProperty("oscar_port"));//request.getServerPort();
		if (port < 0) port = 80; // Work around java.net.URL bug

		url.append(scheme);
		url.append("://");
		//url.append(request.getServerName());
		url.append("127.0.0.1");
		
		if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
			url.append(':');
			url.append(port);
		}
		url.append(request.getContextPath());
		url.append("/LabViewForPdfGenerationServlet?segmentID=");
		url.append(segmentID);
		url.append("&searchProviderNo=");
		url.append(searchProviderNo);
		url.append("&providerNo=");
		url.append(providerNo);
		url.append("&status=");
		url.append(status);
		url.append("&showLatest=");
		url.append(showLatest);

		return (url.toString());
	}

	/**
	 * This method will take eforms and send them to a PHR.
	 */
	public void printLab(String labName, String segmentID, String searchProviderNo, String providerNo, String status, String showLatest) {
		
		File tempFile = null;

		try {
			logger.info("Generating PDF for lab with segment id = " + segmentID);

			tempFile = File.createTempFile(labName, ".pdf");
			//tempFile.deleteOnExit();

			// convert to PDF
			String viewUri = localUri;
			WKHtmlToPdfUtils.convertToPdf(viewUri, tempFile);
			logger.info("Writing pdf to : "+tempFile.getCanonicalPath());
			
			
			InputStream is = new BufferedInputStream(new FileInputStream(tempFile));
			ByteOutputStream bos = new ByteOutputStream();
			byte buffer[] = new byte[1024];
			int read;
			while (is.available() != 0) {
				read = is.read(buffer,0,1024);
				bos.write(buffer,0, read);
			}
			
			//while (fos.read() != -1)
			response.setContentType("application/pdf");  //octet-stream
            response.setHeader("Content-Disposition", "attachment; filename=\"" + labName + "_"
							+ UtilDateUtilities.getToday("yyyy-mm-dd.hh.mm.ss")
							+ ".pdf\"");
			response.getOutputStream().write(bos.getBytes(), 0, bos.getCount());
			
			// Removing the consulation pdf.
			tempFile.delete();			
						
		} catch (IOException e) {
			//logger.error("Error converting and sending eform. id=" + eFormId, e);
			MiscUtils.getLogger().error("error",e);
		} finally {			
			// we'll be nice and if debugging is enabled we'll leave the file lying around so you can see it.
			//if (tempFile != null && !logger.isDebugEnabled()) tempFile.delete();
		}
	}

	
}
