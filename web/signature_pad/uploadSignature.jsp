
<%@page import="org.oscarehr.util.DigitalSignatureUtils"%><%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%
	try
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
		String filename=DigitalSignatureUtils.getTempFilePath(request.getParameter(DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY));
		FileOutputStream fos=new FileOutputStream(filename);
	
		int i=0;
		int counter=0;
		InputStream is=request.getInputStream();
		while ((i=is.read())!=-1)
		{
			fos.write(i);
			counter++;
		}
		fos.flush();
		fos.close();

		MiscUtils.getLogger().debug("Signature uploaded : "+filename+", size="+counter);
		
		response.setStatus(HttpServletResponse.SC_OK);
	}
	catch (Exception e)
	{
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		MiscUtils.getLogger().error("Error receiving signature : "+request.getQueryString(), e);
	}
%>