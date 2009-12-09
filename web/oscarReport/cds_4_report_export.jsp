
<%@page import="java.util.HashMap"%>
<%@page import="org.oscarehr.web.CdsManualLineEntry"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.oscarehr.web.Cds4ReportUIBean"%><%-- 
/*
* Copyright (c) 2007-2009. CAISI, Toronto. All Rights Reserved.
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
* This software was written for 
* CAISI, 
* Toronto, Ontario, Canada 
*/
--%>
<%@page contentType="application/octet-stream"%>
<%
	int startYear = Integer.parseInt(request.getParameter("startYear"));
	int startMonth = Integer.parseInt(request.getParameter("startMonth"));
	int endYear = Integer.parseInt(request.getParameter("endYear"));
	int endMonth = Integer.parseInt(request.getParameter("endMonth"));
	
	String[] caisiProgramIdsString=request.getParameterValues("caisiProgramIds");
	int[] caisiProgramIds=new int[caisiProgramIdsString.length];
	for (int i=0; i<caisiProgramIdsString.length; i++)
	{
		caisiProgramIds[i]=Integer.parseInt(caisiProgramIdsString[i]);
	}
	
	String ministryOrganisationNumber=request.getParameter("ministryOrganisationNumber");
	String ministryProgramNumber=request.getParameter("ministryProgramNumber");
	String ministryFunctionCode=request.getParameter("ministryFunctionCode");
	String[] serviceLanguages=request.getParameterValues("serviceLanguages");
	String[] serviceDeliveryLhins=request.getParameterValues("serviceDeliveryLhin");
	boolean measureServiceRecipientSatisfaction=WebUtils.isChecked(request, "measureServiceRecipientSatisfaction");
	boolean measureServiceRecipientFamiltySatisfaction=WebUtils.isChecked(request, "measureServiceRecipientFamiltySatisfaction");
	boolean qualityImprovementStrategies=WebUtils.isChecked(request, "qualityImprovementStrategies");
	boolean participateInAccreditation=WebUtils.isChecked(request, "participateInAccreditation");
	
	HashMap<String,CdsManualLineEntry> manualSections=new HashMap<String,CdsManualLineEntry>();
	CdsManualLineEntry section7_02=CdsManualLineEntry.getCdsManualLineEntry(request, "007-02");
	manualSections.put("007-02", section7_02);
	
	ArrayList<String> results=Cds4ReportUIBean.getAsciiExportData(caisiProgramIds, startYear, startMonth, endYear, endMonth, ministryOrganisationNumber, ministryProgramNumber, ministryFunctionCode, serviceLanguages, serviceDeliveryLhins, manualSections, measureServiceRecipientSatisfaction, measureServiceRecipientFamiltySatisfaction, qualityImprovementStrategies, participateInAccreditation);
	
	response.setHeader("Content-Disposition", "attachment; filename="+Cds4ReportUIBean.getFilename(ministryOrganisationNumber, ministryProgramNumber, ministryFunctionCode));
	PrintWriter responseWriter=response.getWriter();
	
	for (String s : results)
	{
		responseWriter.println(s);
	}
	
	responseWriter.flush();
%>