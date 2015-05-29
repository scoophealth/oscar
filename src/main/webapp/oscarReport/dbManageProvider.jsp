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

<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.ReportProvider" %>
<%@ page import="org.oscarehr.common.dao.ReportProviderDao" %>
<%
	ReportProviderDao reportProviderDao = SpringUtils.getBean(ReportProviderDao.class);
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    function closeit() {
    	//self.opener.refresh();
      //self.close();
    }
    //-->
</script>
</head>
<body onload="start()">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ADD A Provider List</font></th>
	</tr>
</table>
<%

GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);

  String nowDate = String.valueOf(curYear)+"-"+String.valueOf(curMonth) + "-" + String.valueOf(curDay);
//String nowDate = "2002-08-21";
int dob_yy=0, dob_dd=0, dob_mm=0, age=0;
String demo_no="", demo_sex="", provider_no="", roster="", patient_status="";
String demographic_dob="1800";
String action = request.getParameter("action");
String count = request.getParameter("count");
String last_name="", first_name="", mygroup="", temp="";

List<ReportProvider> res = reportProviderDao.findByAction(action);
for(ReportProvider rp:res) {
	reportProviderDao.remove(rp.getId());
}

	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if( temp.indexOf("provider")==-1 ) continue;


		provider_no = request.getParameter(temp).substring(0, request.getParameter(temp).indexOf("|"));
		mygroup = request.getParameter(temp).substring(request.getParameter(temp).indexOf("|")+1);

	  ReportProvider rp = new ReportProvider();
	  rp.setAction(action);
	  rp.setProviderNo(provider_no);
	  rp.setStatus("A");
	  rp.setTeam(mygroup);
	  reportProviderDao.persist(rp);
%> <%
}
%> 
<script type="text/javascript">
	<%if(action.equals("visitreport")){%>
	window.opener.location.href="<%=request.getContextPath() %>/administration/?show=<%=action%>";
	<%}%>  

    window.close();
</script> <%
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>
