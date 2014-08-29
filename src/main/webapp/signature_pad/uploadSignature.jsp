<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@page import="org.oscarehr.util.DigitalSignatureUtils"%><%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.apache.commons.codec.binary.Base64" %>
<%@ page import="org.oscarehr.common.model.DigitalSignature" %>
<%
	String signatureId = "";
	try {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String filename = DigitalSignatureUtils
				.getTempFilePath(request
						.getParameter(DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY));
		
		String uploadSource = request.getParameter("source");

		if (uploadSource != null
				&& uploadSource.equalsIgnoreCase("IPAD")) {
                    
			FileOutputStream fos = new FileOutputStream(filename);
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
		} else if (uploadSource == null){

                        FileOutputStream fos = new FileOutputStream(filename);
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
