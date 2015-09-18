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
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="java.util.List"%>
<%@page import="oscar.util.ConversionUtils"%>
<%@page import="java.util.Date"%>
<%@page import="org.oscarehr.billing.CA.BC.dao.Hl7LinkDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%
	Hl7LinkDao dao = SpringUtils.getBean(Hl7LinkDao.class);

    Date start       = ConversionUtils.fromDateString(oscar.Misc.check(request.getParameter("start"), ""));
    Date end         = ConversionUtils.fromDateString(oscar.Misc.check(request.getParameter("end"), ""));
    
	String provider_no = oscar.Misc.check(request.getParameter("provider_no"), "");
    String orderby     = oscar.Misc.check(request.getParameter("orderby"), "pid_id");
	
    String command     = oscar.Misc.check(request.getParameter("cmd_search"), "");
		
	String url = "searchreports.jsp?cmd_search=search&provider_no=" + provider_no;
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OSCAR PathNET - Search Lab Reports</title>
<link rel="stylesheet" href="../../../share/css/oscar.css">
<script language="JavaScript">
var labReport;
function PopupLab(pid)
{
	labReport = window.opener.open('report.jsp?pid=' + pid,'LabSearchReport','height=500,width=900,scrollbars=1,toolbar=0,status=1,menubar=0,location=0,directories=0,resizable=1');
	labReport.focus();
}
</script>
</head>
<body>
<form action="searchreports.jsp" method="post">
<table width="100%" class="DarkBG">
	<tr>
		<td height="40" width="25"></td>
		<td width="50%" align="left"><font color="#4D4D4D"><b><font
			size="4">oscar<font size="3">PathNET - Search Lab
		Reports</font></font></b></font></td>
		<td class="Text" align="right"><a href="index.jsp">Patient
		Linking</a>&nbsp;</td>
	</tr>
</table>
<table width="100%">
	<tr bgcolor="E6E6E6">
		<td class="Text">Provider No</td>
		<td class="Text">Start Date</td>
		<td class="Text">End Date</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td class="Text"><select name="provider_no" id="provider_no"
			size=1>
			<option value="-APL"
				<%=(provider_no.equals("-APL")? "selected" : "")%>>Linked
			Labs</option>
			<option value="-ULL"
				<%=(provider_no.equals("-ULL")? "selected" : "")%>>Unlinked
			Labs</option>
			<option value="-UAP"
				<%=(provider_no.equals("-UAP")? "selected" : "")%>>Unassigned
			Patients</option>

			<%
	
	for(Object[] o : dao.findReports(start, end, provider_no, orderby, command)) {
		String providerNo = String.valueOf(o[0]);
		String lastName = String.valueOf(o[1]);
		String firstName = String.valueOf(o[2]);
		
		out.println("<option value='" + providerNo + "'" + (providerNo.equals(provider_no)? "selected" : "") + ">" + lastName + ", " + firstName + "</option>");
	}
%>
		</select></td>
		<td class="Text"><input name="start" value="<%=start%>" /></td>
		<td class="Text"><input name="end" value="<%=end%>" /></td>
		<td class="Text"><input type="submit" name="cmd_search"
			value="Search" /></td>
	</tr>
</table>
</form>
<table width="100%">
	<tr>
		<td class="Header" nowrap><a href="<%=url%>">Lab Id</a></td>
		<td class="Header" nowrap><a
			href="<%=url + "&orderby=patient_name"%>">Patient Name</a></td>
		<td class="Header" nowrap><a
			href="<%=url + "&orderby=ordering_provider"%>">Ordering Provider</a></td>
		<td class="Header" nowrap><a
			href="<%=url + "&orderby=result_copies_to"%>">Result Copies To</a></td>
		<td class="Header" nowrap><a href="<%=url + "&orderby=status"%>">Status</a></td>
		<td class="Header" nowrap><a
			href="<%=url + "&orderby=signed_on"%>">Signed</a></td>
		<td class="Header" nowrap><a
			href="<%=url + "&orderby=last_name"%>">Signing Provider</a></td>
		<td class="Header" nowrap><a
			href="<%=url + "&orderby=date_time"%>">Date Received</a></td>
	</tr>
	<%
	if(command != null){
		List<Object[]> reports = dao.findReports(start, end, provider_no, orderby, command);
		
		boolean other = true;
		for(Object[] o : reports) {
			String pid_id = String.valueOf(o[0]);
			String patient_name = String.valueOf(o[1]);
			String ordering_provider = String.valueOf(o[2]);
			String result_copies_to = String.valueOf(o[3]);
			String status = String.valueOf(o[4]);
			String signed_on = String.valueOf(o[5]);
			String last_name = String.valueOf(o[6]);
			String first_name = String.valueOf(o[7]);
			String date_time = String.valueOf(o[8]);			
%>
	<tr class="<%=(other? "LightBG" : "WhiteBG")%>">
		<td class="Text"><a href="searchreports.jsp"
			onclick="PopupLab('<%=pid_id%>'); return false;"><%=pid_id%></a></td>
		<td class="Text" nowrap><%=oscar.Misc.check(patient_name, "")%></td>
		<td class="Text" nowrap><%=oscar.Misc.check(ordering_provider, "").replaceAll("~", ",<br/>")%></td>
		<td class="Text"><%=oscar.Misc.check(result_copies_to, "").replaceAll("~", ",<br/>")%></td>
		<td class="Text" nowrap>
		<%
			if(status != null){
				switch(status.toCharArray()[0]){
					case 'S':
						out.print("Signed");
						break;
					case 'P':
						out.print("Pending");
						break;
					case 'A':
						out.print("Assigned");
						break;
					case 'N':
						out.print("New");
						break;
				}
			}
%>
		</td>
		<td class="Text" nowrap>
		<%String signed = oscar.Misc.check(signed_on, "");
			out.print( (signed.indexOf(" ")>-1)? signed.substring(0, signed.indexOf(" ")) : signed);%>
		</td>
		<td class="Text" nowrap><%=((last_name != null && !last_name.equals(""))? oscar.Misc.check(last_name, "") + ", " + oscar.Misc.check(first_name, "") : "&nbsp;")%></td>
		<td class="Text" nowrap><%=date_time.substring(0, date_time.indexOf(" "))%></td>
	</tr>
	<%
		other = !other;
		}
	}
%>
</table>
</body>
</html>
