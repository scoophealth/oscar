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

<%@page import="java.util.Date"%>
<%@page import="oscar.util.ConversionUtils"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.billing.CA.BC.dao.Hl7LinkDao" %>
<%@page import="org.oscarehr.billing.CA.BC.model.Hl7Link" %>
<%
	Hl7LinkDao linkDao = SpringUtils.getBean(Hl7LinkDao.class);

    String demo_no = request.getParameter("demo_no"),
    pid = request.getParameter("pid");
	if(null != request.getParameter("unlink")){
		Hl7Link h = linkDao.find(Integer.parseInt(pid));
		if(h != null) {
			h.setStatus("P");
			linkDao.merge(h);
		}
	}
	if(null == demo_no){
		out.println("<script language=\"JavaScript\">javascript:window.close();</SCRIPT>");
		return;
	}
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OSCAR PathNET - View Lab Reports</title>
<link rel="stylesheet" href="../../../share/css/oscar.css">
<script language="JavaScript">
var lab;
window.name= 'LabDemoIndex';
function PopupLab(pid)
{
	lab = window.open('report.jsp?viewed=true&pid=' + pid,'Lab','height=500,width=900,scrollbars=1,toolbar=0,status=1,menubar=0,location=0,directories=0,resizable=1');
	lab.focus();
	return false;
}
</script>
</head>
<body>
<table width="100%" class="DarkBG">
	<tr>
		<td height="40" width="25"></td>
		<td width="90%" align="left"><font color="#4D4D4D"><b><font
			size="4">oscar<font size="3">PathNET - View Lab Reports</font></font></b></font>
		</td>
	</tr>
</table>
<form action="viewreports.jsp" method="post">
<table width="100%">
	<%
		String dpid = "",
		diagnostic = "";
		java.text.SimpleDateFormat format = null;
		java.util.Date date = null;
		boolean other = false;
		for(Object[] o : linkDao.findLinksAndRequestDates(ConversionUtils.fromIntString(demo_no))) {
			Integer linkId = (Integer) o[0];
			Date obrRequestedDateTime = (Date) o[1];
			String obrDiagnosticServiceSectId = (String) o[2];
			
			format = new java.text.SimpleDateFormat("yyyy-MM-d HH:mm:ss");
			date = obrRequestedDateTime;
			format.applyPattern("MMM d, yyyy");
			if(dpid.equals(linkId.toString())){
				diagnostic += ", " + obrDiagnosticServiceSectId;
			} else {
				if(!dpid.equals("")){
					out.println("<tr bgcolor='" + (other? "F6F6F6" : "WHITE") + "'><td class=\"Text\"><a href=\"#\" onclick=\"return PopupLab('" + dpid + "');\">" + format.format(date) + " (" + diagnostic + ")</a></td><td class=\"Text\"><a onclick=\"return confirm('Are you sure you want to unlink this lab report?');\" href=\"viewreports.jsp?unlink=true&demo_no=" + demo_no + "&pid=" + dpid + "\">unlink</a></td></tr>");
				}
				dpid = "" + linkId;
				diagnostic = "" + obrDiagnosticServiceSectId;
				other = !other;
			}
		}
		out.println("<tr bgcolor='" + (other? "F6F6F6" : "WHITE") + "'><td class=\"Text\"><a href=\"#\" onclick=\"return PopupLab('" + dpid + "');\">" + format.format(date) + " (" + diagnostic + ")</a></td><td class=\"Text\"><a onclick=\"return confirm('Are you sure you want to unlink this lab report?');\" href=\"viewreports.jsp?unlink=true&demo_no=" + demo_no + "&pid=" + dpid + "\">unlink</a></td></tr>");	
%>
</table>
</form>
</body>
</html>
