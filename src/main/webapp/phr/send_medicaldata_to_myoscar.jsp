<%@page import="org.oscarehr.common.service.myoscar.MyOscarMedicalDataManagerUtils"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.oscarehr.util.LocaleUtils"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%
	String medicalDataType = request.getParameter("medicalDataType");
	String parentPage = request.getParameter("parentPage");
	String demographicNoString=request.getParameter("demographicId");
	
	String sendDataPath = "send_medicaldata_to_myoscar_action.jsp?"
							+ "medicalDataType=" + medicalDataType + "&"
							+ "parentPage=" + parentPage + "&"
							+ "demographicId=" + demographicNoString;

	try
	{
		String verificationLevel = MyOscarMedicalDataManagerUtils.getVerificationLevel(Integer.parseInt(demographicNoString));
		
		if ("+3".equals(verificationLevel)) {
			response.sendRedirect(sendDataPath);
		}
	}
	catch (Exception e)
	{
		MiscUtils.getLogger().error("error", e);
		WebUtils.addErrorMessage(session, LocaleUtils.getMessage(request,"UnexpectedError"));
		response.sendRedirect(parentPage);
	}
%>

<html>
<head>
	<title>
		<%=LocaleUtils.getMessage(request, "ConfirmSending")%>&nbsp;<%=LocaleUtils.getMessage(request, medicalDataType)%>&nbsp;<%=LocaleUtils.getMessage(request, "ToMyOscar")%>
	</title>
</head>
<body>
	<p>
	<h3><%=LocaleUtils.getMessage(request, "WarningNotLevel3")%></h3>
	<h3><%=LocaleUtils.getMessage(request, "AreYouSureYouWantToSend")%>&nbsp;<%=LocaleUtils.getMessage(request, medicalDataType)%>&nbsp;<%=LocaleUtils.getMessage(request, "ToMyOscar")%>?</h3>
	<p>
	<input type="button" value="Yes" onclick="window.location.href='<%=sendDataPath%>'" />
	<p>
	<input type="button" value="No" onclick="window.location.href='<%=parentPage%>'" />
</body>
</html>