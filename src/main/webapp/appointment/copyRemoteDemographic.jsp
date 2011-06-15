<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.dao.ClientDao"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%
	int remoteFacilityId = Integer.parseInt(request.getParameter("remoteFacilityId"));
	int remoteDemographicId = Integer.parseInt(request.getParameter("demographic_no"));

	Demographic demographic=CaisiIntegratorManager.makeUnpersistedDemographicObjectFromRemoteEntry(remoteFacilityId, remoteDemographicId);
	ClientDao clientDao=(ClientDao)SpringUtils.getBean("clientDao");
	clientDao.saveClient(demographic);
	
	// WebUtils.dumpParameters(request);
	// --- Dump Request Parameters Start ---
	// duration=15
	// messageID=null
	// location=
	// user_id=oscardoc, doctor
	// createdatetime=2011-6-13 13:36:24
	// type=
	// remarks=
	// creator=oscardoc, doctor
	// search_mode=search_name
	// orderby=last_name, first_name
	// originalpage=../appointment/addappointment.jsp
	// doctor_no=000000
	// bFirstDisp=false
	// notes=
	// end_time=09:14
	// start_time=09:00
	// ptstatus=active
	// demographic_no=1
	// status=t
	// originalPage=../appointment/addappointment.jsp
	// appointment_date=2011-06-13
	// limit2=5
	// limit1=0
	// resources=
	// provider_no=999998
	// reason=
	// chart_no=
	// remoteFacilityId=1
	// name=ASDF,ASDF
	// --- Dump Request Parameters End ---
	
	StringBuilder redirect=new StringBuilder();
	redirect.append(request.getParameter("originalPage"));
	redirect.append("?");

	redirect.append("demographic_no=");
	redirect.append(demographic.getDemographicNo());
	
	redirect.append("&doctor_no=");
	redirect.append(request.getParameter("provider_no"));
	
	@SuppressWarnings("unchecked")
	Enumeration<String> e = request.getParameterNames();
	while (e.hasMoreElements()) {
		String key = e.nextElement();
		
		if (key.equals("demographic_no") || key.equals("doctor_no") || key.equals("originalPage"))
		{
			// ignore these parameters
		}
		else
		{
			redirect.append("&");
			redirect.append(key);
			redirect.append("=");
			redirect.append(URLEncoder.encode(request.getParameter(key)));
		}
	}

	response.sendRedirect(redirect.toString());
%>