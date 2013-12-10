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
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="java.net.URLEncoder"%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>E-Chart</title>

<script language="javascript">
<% 

GregorianCalendar cal = new GregorianCalendar();
int curYear = cal.get(Calendar.YEAR);
int curMonth = (cal.get(Calendar.MONTH)+1);
int curDay = cal.get(Calendar.DAY_OF_MONTH);

%>

location.href = '../oscarEncounter/IncomingEncounter.do?demographicNo=<%=request.getParameter("demographicNo")%>&reason=Lab+Results-Notes&curDate=<%=curYear%>-<%=curMonth%>-<%=curDay%>&encType=<%=URLEncoder.encode("Lab Results","UTF-8")%>&status=';
window.resizeTo(980,700);

</script>

</head>
<body>

<a
	href="javascript:popupPage(700, 980, '../oscarEncounter/IncomingEncounter.do?demographicNo=<%=request.getParameter("demographicNo")%>&reason=Lab+Results-Notes&curDate=<%=curYear%>-<%=curMonth%>-<%=curDay%>&encType=<%=URLEncoder.encode("Lab Results","UTF-8")%>&status=');window.close();">Please
click here to go to the patient's E-Chart.</a>

</body>
</html>
