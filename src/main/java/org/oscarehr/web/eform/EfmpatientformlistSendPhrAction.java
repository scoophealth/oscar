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


package org.oscarehr.web.eform;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.WKHtmlToPdfUtils;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.dms.actions.AddEditDocumentAction;

public final class EfmpatientformlistSendPhrAction {

	private static final Logger logger = MiscUtils.getLogger();

	private String localUri = null;
	private String clientId = null;
	private String providerNo = null;

	public EfmpatientformlistSendPhrAction(HttpServletRequest request) {
		localUri = getEformRequestUrl(request);

		// this really doesn't look thread safe, although I have no proof of it so I'll just note it as such.
		clientId = request.getParameter("clientId");
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		providerNo=loggedInInfo.getLoggedInProviderNo();

		logger.debug(ReflectionToStringBuilder.toString(this));
	}

	/**
	 * This method is a copy of Apache Tomcat's ApplicationHttpRequest getRequestURL method with the exception that the uri is removed and replaced with our eform viewing uri. Note that this requires that the remote url is valid for local access. i.e. the
	 * host name from outside needs to resolve inside as well. The result needs to look something like this : https://127.0.0.1:8443/oscar/eformViewForPdfGenerationServlet?fdid=2&parentAjaxId=eforms
	 */
	private String getEformRequestUrl(HttpServletRequest request) {
		StringBuilder url = new StringBuilder();
		String scheme = request.getScheme();
		int port = request.getServerPort();
		if (port < 0) port = 80; // Work around java.net.URL bug

		url.append(scheme);
		url.append("://");
		
		// IMPORTANT : do not change the serverName to 127.0.0.1
		// you can not do that because on virtual hosts or named hosts 127.0.0.1 may
		// not resolve to the same webapp. You must use the serverName that maps properly
		// as per the server.xml (in tomcat). Admittedly 95% of the time 127.0.0.1 would
		// work because most people don't do virtual hosting with tomcat on an oscar
		// system (but some caisi systems have in the past), but by keeping the hostName
		// this code would then work with everyone - although everyone needs to ensure
		// the serverName now resolves properly from localhost, i.e. usually this means
		// make a /etc/hosts entry if you're using NAT.
		url.append(request.getServerName());
		if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
			url.append(':');
			url.append(port);
		}
		url.append(request.getContextPath());
		url.append("/eformViewForPdfGenerationServlet?parentAjaxId=eforms&fdid=");

		return (url.toString());
	}

	/**
	 * This method will take eforms and send them to a PHR.
	 * 
	 * @return a list of documentIds it just added
	 */
	public ArrayList<String> sendEFormsToPhr(String[] eFormIds) {
		// This is a 2 phase algorithm,
		// 1) convert an eform to a pdf oscar document
		// 2) send the pdf oscar document to the phr

		ArrayList<String> docIds=new ArrayList<String>();
		
		for (String eFormId : eFormIds) {
			try {
				String newDocId=sendEformToPhr(Integer.parseInt(eFormId));
				docIds.add(newDocId);
			} catch (Exception e) {
				logger.error("Error converting eform to oscar document. eformId="+eFormId, e);
			}
		}
		
		return(docIds);
	}

	/**
	 * @return the new document id
	 */
	private String sendEformToPhr(int eFormId) throws Exception {
		File tempFile = null;

		try {
			logger.debug("Send eform to PHR. id=" + eFormId);

			tempFile = File.createTempFile("eform.", ".pdf");
			tempFile.deleteOnExit();

			// convert to PDF
			String viewUri = localUri + eFormId;
			WKHtmlToPdfUtils.convertToPdf(viewUri, tempFile);
			logger.debug("Writing pdf to : " + tempFile.getCanonicalPath());

			// upload pdf to oscar docs
			return(uploadToOscarDocuments(tempFile, "eform", "eform"));
		} finally {
			// we'll be nice and if debugging is enabled we'll leave the file lying around so you can see it.
			if (tempFile != null && !logger.isDebugEnabled()) tempFile.delete();
		}
	}

	/**
	 * @return the new documentId
	 */
	private String uploadToOscarDocuments(File file, String description, String type) throws Exception {

		String originalFileName = file.getName();
		EDoc newDoc = new EDoc(description, type, originalFileName, "", providerNo, "", "", 'A', oscar.util.UtilDateUtilities.getToday("yyyy-MM-dd"), "", "", "demographic", clientId);
		newDoc.setContentType("application/pdf");
		String newFileName = newDoc.getFileName();

		FileInputStream fis = new FileInputStream(file);
		try {
			AddEditDocumentAction.writeLocalFile(fis, newFileName);
		} finally {
			fis.close();
		}

		return(EDocUtil.addDocumentSQL(newDoc));
	}
}
