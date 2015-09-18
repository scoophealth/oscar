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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page
	import="java.util.*,oscar.oscarReport.data.*,oscar.util.*,oscar.oscarDB.*,java.sql.*,oscar.oscarDemographic.data.*,oscar.oscarPrevention.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">
<%  //This could be done alot better.
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");

  String startStr = "";
  String endStr   = "";
  String injectionType ="";

  if (request.getParameter("startDate") != null && request.getParameter("startDate") != null && request.getParameter("startDate") != null  ){

      String startDate =     request.getParameter("startDate");
      String endDate  =      request.getParameter("endDate");
      injectionType = request.getParameter("injectionType");

      java.util.Date sDate = UtilDateUtilities.StringToDate(startDate);
      java.util.Date eDate = UtilDateUtilities.StringToDate(endDate);

      String keyVal = "lot";
      if (injectionType != null && injectionType.equals("RH")){
          keyVal = "product";
      }

      ArrayList<Map<String,Object>> list = PreventionData.getExtValues(injectionType,sDate,eDate,keyVal);
      pageContext.setAttribute("list",list);

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
<title>Injection Report</title>

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
<!--  -->
<table class="MainTable noprint" id="scrollNumber1"
	name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Report</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Injection</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn">
		<form action="InjectionReport2.jsp">
		<fieldset><legend>Injections</legend> <label>Start
		Date:</label> <input type="text" size="9" name="startDate"
			value="<%=startStr%>" /> <label>End Date:</label> <input type="text"
			size="9" name="endDate" value="<%=endStr%>" /> <label>Injection
		Type:</label> <select name="injectionType">
			<option value="RH" <%=selled ("RH",injectionType)%>>Rh</option>
			<option value="DTaP-IPV" <%=selled ("DTaP-IPV",injectionType)%>>DTaP-IPV</option>
			<option value="DTaP-IPV-Hib" <%=selled ("DTaP-IPV-Hib",injectionType)%>>DTaP-IPV-Hib</option>
			<option value="Hib" <%=selled ("Hib",injectionType)%>>Hib</option>
			<option value="HPV" <%=selled ("HPV",injectionType)%>>HPV</option>
			<option value="Pneu-C" <%=selled ("Pneu-C",injectionType)%>>Pneu-C</option>
			<option value="MMR" <%=selled ("MMR",injectionType)%>>MMR</option>
			<option value="MMRV" <%=selled ("MMRV",injectionType)%>>MMRV</option>
			<option value="MenC-C" <%=selled ("MenC-C",injectionType)%>>MenC-C</option>
			<option value="Men-P-ACWY" <%=selled ("Men-P-ACWY",injectionType)%>>Men-P-ACWY</option>
			<option value="Rot" <%=selled ("Rot",injectionType)%>>Rot</option>
			<option value="VZ" <%=selled ("VZ",injectionType)%>>VZ</option>
			<option value="HepB" <%=selled ("HepB",injectionType)%>>HepB</option>
			<option value="dTap" <%=selled ("dTap",injectionType)%>>dTap</option>
			<option value="Td" <%=selled ("Td",injectionType)%>>Td</option>
			<option value="Flu" <%=selled ("Flu",injectionType)%>>Flu</option>
			<option value="HepA" <%=selled ("HepA",injectionType)%>>HepA</option>
			<option value="HepAB" <%=selled ("HepAB",injectionType)%>>HepAB</option>
			<option value="Rabies" <%=selled ("Rabies",injectionType)%>>Rabies</option>
			
			<option value="Tuberculosis"
				<%=selled ("Tuberculosis",injectionType)%>>Tuberculosis</option>
			<option value="Pneumovax" <%=selled ("Pneumovax",injectionType)%>>Pneumovax</option>
			<option value="TdP" <%=selled ("TdP",injectionType)%>>TdP</option>
			<option value="IPV" <%=selled ("IPV",injectionType)%>>IPV</option>
                        <option value="H1N1" <%=selled ("H1N1",injectionType)%>>H1N1</option>
		</select> <input type="submit" value="Run Report" /></fieldset>
		</form>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>

<%
    ArrayList<Map<String,Object>> report = (ArrayList<Map<String,Object>>) pageContext.getAttribute("list");
    if (report != null){
        DemographicData demoData= new DemographicData();
    %>
<table class="ele">
	<tr>
		<th>#</th>
		<th>First Name</th>
		<th>Last Name</th>
		<th>DOB</th>
		<th>Chart #</th>
		<th>Product #</th>
		<th>Injection Date</th>
		<th>Comments</th>
	</tr>
	<% for (int i = 0; i < report.size(); i++){
				Map<String,Object> h = report.get(i);
                String demo = (String) h.get("demographic_no");
                org.oscarehr.common.model.Demographic demog = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demo);
                String comments = PreventionData.getPreventionComment((String)h.get("preventions_id"));
                if( comments == null ) {
                    comments = "";
                }
            %>
	<tr>
		<td><%=i+1%></td>
		<td><%=demog.getFirstName()%></td>
		<td><%=demog.getLastName()%></td>
		<td><%=DemographicData.getDob(demog,"-")%></td>
		<td><%=demog.getChartNo()%></td>
		<td><%=h.get("val")%>&nbsp;</td>
		<td><%=h.get("prevention_date")%></td>
		<td><%=comments%></td>
	</tr>
	<%}%>
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
