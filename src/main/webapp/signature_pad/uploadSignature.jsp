
<%@page import="org.oscarehr.util.DigitalSignatureUtils"%><%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.apache.commons.codec.binary.Base64" %>
<%@ page import="org.oscarehr.common.model.DigitalSignature" %>
<%
	String signatureId = "";
	try {
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
		String filename = DigitalSignatureUtils
				.getTempFilePath(request
						.getParameter(DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY));
		FileOutputStream fos = new FileOutputStream(filename);

		String uploadSource = request.getParameter("source");

		if (uploadSource != null
				&& uploadSource.equalsIgnoreCase("IPAD")) {
			
			String imageString = request.getParameter("signatureImage");
			imageString = imageString.substring(imageString.indexOf(",")+1);
			
			Base64 b64 = new Base64();
			byte[] imageByteData = imageString.getBytes();
			
			byte[] imageData = b64.decode(imageByteData);
			
			if (imageData != null) {
				fos.write(imageData);
			}
			
			fos.flush();
			fos.close();
			
			MiscUtils.getLogger().debug("Signature uploaded: " + filename + ", size=" + imageData.length);
		} else {

			int i = 0;
			int counter = 0;
			InputStream is = request.getInputStream();
			while ((i = is.read()) != -1) {
				fos.write(i);
				counter++;
			}
			fos.flush();
			fos.close();

			MiscUtils.getLogger().debug(
					"Signature uploaded : " + filename + ", size="
							+ counter);

		}
		
		boolean saveToDB = "true".equals(request.getParameter("saveToDB")); 
		if (saveToDB) {
			Integer demo; 
			
			try {
				demo = Integer.parseInt(request.getParameter("demographicNo"));
			}
			catch (NumberFormatException nfe) {
				demo = -1;
			}
			DigitalSignature signature = DigitalSignatureUtils.storeDigitalSignatureFromTempFileToDB(
						loggedInInfo, 
						request.getParameter(DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY), 
						demo);
			if (signature != null) {
				signatureId = "" + signature.getId();
			}
		}
		response.setStatus(HttpServletResponse.SC_OK);
	} catch (Exception e) {
		response.sendError(
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
				e.getMessage());
		MiscUtils.getLogger().error(
				"Error receiving signature : "
						+ request.getQueryString(), e);
	}
%>
<input type="hidden" name="signatureId" value="<%=signatureId%>" />
