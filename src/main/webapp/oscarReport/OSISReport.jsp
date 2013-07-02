<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<%@ page
	import="org.apache.commons.lang.StringUtils,org.apache.commons.lang.StringEscapeUtils,java.util.*,oscar.oscarReport.data.*,oscar.util.*,oscar.oscarDB.*,java.sql.*,oscar.oscarDemographic.data.*,oscar.eform.*,org.oscarehr.common.model.Provider,org.oscarehr.managers.ProviderManager2,org.oscarehr.util.SpringUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
ProviderManager2 providerManager = (ProviderManager2) SpringUtils.getBean("providerManager2");
%>

<jsp:useBean id="reportMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%  if(!reportMainBean.getBDoConfigure()) { %>
<%@ include file="/report/reportMainBeanConn.jspf"%>
<% }  %>


<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">
<%  //This could be done alot better.
if(session.getValue("user") == null)
    response.sendRedirect("../logout.htm");
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");

  String startStr = "";
  String endStr   = "";
  String[] providerNo = null;
  String formId = "";

  
  if (request.getParameter("startDate") != null && request.getParameter("endDate") != null && request.getParameter("formId") != null &&  request.getParameter("providerIds") != null) {

      String startDate =     request.getParameter("startDate");
      String endDate  =      request.getParameter("endDate");
      formId = request.getParameter("formId");
      providerNo = request.getParameterValues("providerIds");

      java.util.Date sDate = UtilDateUtilities.StringToDate(startDate);
      java.util.Date eDate = UtilDateUtilities.StringToDate(endDate);



      startStr = UtilDateUtilities.DateToString(sDate);
      endStr   = UtilDateUtilities.DateToString(eDate);
  }else{
     Calendar cal = Calendar.getInstance();
     int daysTillMonday = cal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
     java.util.Date endDate = cal.getTime();
     cal.add(Calendar.DATE,-daysTillMonday);
     java.util.Date startDate = cal.getTime();

     startStr = UtilDateUtilities.DateToString(startDate);
     endStr   = UtilDateUtilities.DateToString(endDate);
  }
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OSIS Report</title>

<style type="text/css" media="print">
.noprint {
	display: none;
}

table.ele {
	width: 450pt;
	margin-left: 0pt;
}
</style>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<script type="text/javascript">

</script>

</head>

<body class="BodyStyle" vlink="#0000FF">
<table class="MainTable noprint" id="scrollNumber1"
	name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Report</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>OSIS</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn">
		<form action="OSISReport.jsp">
		<fieldset><legend>OSIS Report</legend> <label>Start
		Date:</label> <input type="text" size="9" name="startDate"
			value="<%=startStr%>" /> <label>End Date:</label> <input type="text"
			size="9" name="endDate" value="<%=endStr%>" /> 
                <label>Form Id:</label><input type="text" size="3" name="formId" value="<%=formId%>"/>
		<br><br>
		<label>Providers to include:</label>
		<br>
		<select multiple="multiple" name="providerIds" size="15">
		<%
		// null for both active and inactive because the report might be for a provider
		// who's just left the current reporting period
		List<Provider> providers = providerManager.getProviders(null);

		for(Provider provider: providers){
			//skip (system,system)
			if(provider.getProviderNo().equals(Provider.SYSTEM_PROVIDER_NO)) continue;
		%>
			<option value="<%=provider.getProviderNo()%>"><%=StringEscapeUtils.escapeHtml(provider.getFormattedName())%></option>
		<%
		}
		%>
		</select>

		<input type="submit" value="Run Report" /></fieldset>
		</form>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>

<%

	if (providerNo != null && !formId.equals("")){
        
        
        
    %>

<table border="1">
	<tr>
		<td>&nbsp;</td>
		<th colspan="4">Number of Individuals<br>HOMELESS</th>
		<th colspan="4">Number of Individuals<br>HOUSED</th>
	</tr>
	<tr>
		<th>Age / Gender of Individuals</th>
		<th>Male</th>
		<th>Female</th>
		<th>Transgender<br>(Self-Identified</th>
		<th>Gender<br>Unknown</th>
		<th>Male</th>
		<th>Female</th>
		<th>Transgender<br>(Self-Identified</th>
		<th>Gender<br>Unknown</th>
	</tr>

	<tr>
		<td>15-21 years</td>
		<td><%=OSISReportUtil.getOSISReport("","15-21","008-01","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","15-21","008-02","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","15-21","008-03","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","15-21","008-04","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","15-21","008-01","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","15-21","008-02","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","15-21","008-03","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","15-21","008-04","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
	</tr>

	<tr style="background-color:gainsboro;">
		<td>22-30 years</td>
		<td><%=OSISReportUtil.getOSISReport("","22-30","008-01","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","22-30","008-02","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","22-30","008-03","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","22-30","008-04","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","22-30","008-01","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","22-30","008-02","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","22-30","008-03","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","22-30","008-04","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
	</tr>

		
	<tr>
		<td>31-45 years</td>
		<td><%=OSISReportUtil.getOSISReport("","31-45","008-01","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","31-45","008-02","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","31-45","008-03","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","31-45","008-04","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","31-45","008-01","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","31-45","008-02","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","31-45","008-03","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","31-45","008-04","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
	</tr>


	
	<tr style="background-color:gainsboro;">
		<td>46-64 years</td>
		<td><%=OSISReportUtil.getOSISReport("","46-64","008-01","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","46-64","008-02","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","46-64","008-03","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","46-64","008-04","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","46-64","008-01","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","46-64","008-02","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","46-64","008-03","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","46-64","008-04","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
	</tr>
		

	<tr>
		<td>65+ years</td>
		<td><%=OSISReportUtil.getOSISReport("","65+","008-01","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","65+","008-02","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","65+","008-03","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","65+","008-04","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","65+","008-01","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","65+","008-02","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","65+","008-03","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","65+","008-04","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
	</tr>



	
	<tr style="background-color:gainsboro;">
		<td>unknown years</td>
		<td><%=OSISReportUtil.getOSISReport("","unknown","008-01","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","unknown","008-02","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","unknown","008-03","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","unknown","008-04","CurrentlyHomeless","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","unknown","008-01","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","unknown","008-02","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","unknown","008-03","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","unknown","008-04","CurrentlyHoused","",providerNo,formId,startStr,endStr)%></td>
	</tr>
	

	</table>
	<br><br>
	<table border="1">
	<tr>
		<th>Other Demographic<br>Information of Individuals</th>
		<th>Male</th>
		<th>Female</th>
		<th>Transgender<br>(Self-Identified)</th>
		<th>Gender<br>Unknown</th>
		
		<th>Male</th>
		<th>Female</th>
		<th>Transgender<br>(Self-Identified)</th>
		<th>Gender<br>Unknown</th>
	</tr>
	<tr>
		<td>Aboriginal/Metis/First Nation/Inuit</td>		
		<td><%=OSISReportUtil.getOSISReport("","","008-01","CurrentlyHomeless","011-01",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","","008-02","CurrentlyHomeless","011-01",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","","008-03","CurrentlyHomeless","011-01",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","","008-04","CurrentlyHomeless","011-01",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","","008-01","CurrentlyHoused","011-01",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","","008-02","CurrentlyHoused","011-01",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","","008-03","CurrentlyHoused","011-01",providerNo,formId,startStr,endStr)%></td>
		<td><%=OSISReportUtil.getOSISReport("","","008-04","CurrentlyHoused","011-01",providerNo,formId,startStr,endStr)%></td>
	</tr>

	</table>

	<br><br>
	<table border="1">
	<tr>
		<th>Transitions from Homelessness to Housing:</th>
		<th># Individuals (Households)</th>
	</tr>
	
	<tr style="background-color:gainsboro;">
		<td># Singles moved from street to emergency shelter</td>
		<td><%=OSISReportUtil.getOSISReport("Street to Emergency Shelter","","","","",providerNo,formId,startStr,endStr)%></td>
	</tr>
	<tr>
		<td># Singles moved from street to temporary/transitional housing</td>
		<td><%=OSISReportUtil.getOSISReport("Street to Temporary/Transitional Housing","","","","",providerNo,formId,startStr,endStr)%></td>
	</tr>
	
	<tr style="background-color:gainsboro;">
		<td># Singles moved from street to supportive/supported housing</td>
		<td><%=OSISReportUtil.getOSISReport("Street to Supportive/Supported Housing","","","","",providerNo,formId,startStr,endStr)%></td>
	</tr>
	<tr>
		<td># Singles moved from street to regular/permanent housing</td>
		<td><%=OSISReportUtil.getOSISReport("Street to Regular/Permanent Housing","","","","",providerNo,formId,startStr,endStr)%></td>
	</tr>
	
	<tr style="background-color:gainsboro;">
		<td># Singles moved from emergency shelter to temporary/transitional</td>
		<td><%=OSISReportUtil.getOSISReport("Emergency Shelter to Temporary/Transitional Housing","","","","",providerNo,formId,startStr,endStr)%></td>
	</tr>
	<tr>
		<td># Singles moved from emergency shelter to supportive/supported</td>
		<td><%=OSISReportUtil.getOSISReport("Emergency Shelter to Supportive/Supported Housing","","","","",providerNo,formId,startStr,endStr)%></td>
	</tr>
	
	<tr style="background-color:gainsboro;">
		<td># Singles moved from emergency shelter to regular/permanent</td>
		<td><%=OSISReportUtil.getOSISReport("Emergency Shelter to Regular/Permanent Housing","","","","",providerNo,formId,startStr,endStr)%></td>
	</tr>
	<tr>
		<td># Singles moved from hidden homelessness to temporary/trantional</td>
		<td><%=OSISReportUtil.getOSISReport("Hidden Homelessness to Temporary/Transitional Housing","","","","",providerNo,formId,startStr,endStr)%></td>
	</tr>
	
	<tr style="background-color:gainsboro;">
		<td># Singles moved from hidden homelessness to supportive/supported</td>
		<td><%=OSISReportUtil.getOSISReport("Hidden Homelessness to Supportive/Supported Housing","","","","",providerNo,formId,startStr,endStr)%></td>
	</tr>
	<tr>
		<td># Singles moved from hidden homelessness to regular/permanent</td>
		<td><%=OSISReportUtil.getOSISReport("Hidden Homelessness to Regular/Permanent Housing","","","","",providerNo,formId,startStr,endStr)%></td>
	</tr>
	
	<tr style="background-color:gainsboro;">
		<td>Other type of move that resulsted in Single person moving from homelessness to housing (temporary or permanent)</td>
		<td><%=OSISReportUtil.getOSISReport("Other","","","","",providerNo,formId,startStr,endStr)%></td>	
	</tr>
	<tr>
		<td>Other type of service-street outreach provided</td>
		<td><%=OSISReportUtil.getOSISReport("Other2","","","","",providerNo,formId,startStr,endStr)%></td>
	</tr>
	</table>

	</table>


	<%/*

		 for (int i = 0; i < report.size(); i++){
		HashMap<String,String> h = report.get(i);
                /*
				String demo = (String) h.get("demographic_no");
                org.oscarehr.common.model.Demographic demog = demoData.getDemographic(demo);
                String comments = PreventionData.getPreventionComment((String)h.get("preventions_id"));
                if( comments == null ) {
                    comments = "";
                }*/
		
		//Set set = h.entrySet();
		//Iterator x = set.iterator();
		//while(x.hasNext()){
		//	Map.Entry me = (Map.Entry)x.next();	
		
            %>
				
	

		<%//}%>
	<%//}%>
</table>
<input type="button" onclick="window.print();" value="Print"
	class="noprint" />

<%}%>
</body>
</html>
<%!
String selled (String i,String mons){
	String retval = "";
        if ( i.equals(mons) ){
           retval = "selected";
        }
	
	return retval;
}
%>
