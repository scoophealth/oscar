package org.oscarehr.web.eform;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.WKHtmlToPdfUtils;

public final class EfmpatientformlistSendPhrAction {

	private static final Logger logger = MiscUtils.getLogger();

	private static String localUri = null;

	public EfmpatientformlistSendPhrAction(HttpServletRequest request) {
		localUri = getEformRequestUrl(request);
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
	 */
	public void sendEFormsToPhr(String[] eFormIds) {
		// This is a 2 phase algorithm,
		// 1) convert an eform to a pdf oscar document
		// 2) send the pdf oscar document to the phr

		for (String eFormId : eFormIds) {
			sendEformToPhr(Integer.parseInt(eFormId));
		}
	}

	private void sendEformToPhr(int eFormId) {
		File tempFile = null;

		try {
			logger.debug("Send eform to PHR. id=" + eFormId);

			tempFile = File.createTempFile("eform.", ".pdf");
			tempFile.deleteOnExit();

			// convert to PDF
			String viewUri = localUri + eFormId;
			WKHtmlToPdfUtils.convertToPdf(viewUri, tempFile);
			logger.debug("Writing pdf to : "+tempFile.getCanonicalPath());
			
			// upload pdf to oscar docs
logger.error("NOT COMPLETED, NEED TO UPLOAD TO OSCAR DOCS AND SEND TO PHR HERE");			
		} catch (IOException e) {
			logger.error("Error converting and sending eform. id=" + eFormId, e);
		} finally {
			// we'll be nice and if debugging is enabled we'll leave the file lying around so you can see it.
			if (tempFile != null && !logger.isDebugEnabled()) tempFile.delete();
		}
	}
}
