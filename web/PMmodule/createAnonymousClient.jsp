<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<!--
 ../oscarSurveillance/CheckSurveillance.do?demographicNo=2&proceed=..%2FoscarEncounter%2FIncomingEncounter.do%3FproviderNo%3D999998%26appointmentNo%3D0%26demographicNo%3D2%26curProviderNo%3D999998%26reason%3D%26encType%3Dface%2Bto%2Bface%2Bencounter%2Bwith%2Bclient%26userName%3Ddoctor%2Boscardoc%26curDate%3D2010-3-3%26appointmentDate%3D2010-3-3%26startTime%3D14%3A49%26status%3DT%26source%3Dcm
 
-->
<%
	Demographic demographic = null;

	String strProgramId = request.getParameter("programId");
	int programId = 0;
	try {
		programId = Integer.parseInt(strProgramId);
	} catch(NumberFormatException e) {}
	
	int clientId = 0;
	if(programId == 0) {
		//error has occured
	} else {
		demographic = org.oscarehr.PMmodule.web.CreateAnonymousClientAction.generateAnonymousClient(programId);
	}
	
	Provider provider = LoggedInInfo.loggedInInfo.get().loggedInProvider;
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
	String curDate = sdf.format(new java.util.Date());
	java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("kk:mm");
	String curTime = sdf2.format(new java.util.Date());
	
	response.sendRedirect("../oscarSurveillance/CheckSurveillance.do?demographicNo="+demographic.getDemographicNo()+"&proceed=..%2FoscarEncounter%2FIncomingEncounter.do%3FproviderNo%3D"+provider.getProviderNo()+"%26appointmentNo%3D0%26demographicNo%3D"+demographic.getDemographicNo()+"%26curProviderNo%3D"+provider.getProviderNo()+"%26reason%3D%26encType%3Dface%2Bto%2Bface%2Bencounter%2Bwith%2Bclient%26userName%3D"+provider.getFormattedName()+"%26curDate%3D"+curDate+"%26appointmentDate%3D"+curDate+"%26startTime%3D"+curTime+"%26status%3DT%26source%3Dcm");
			 
%>