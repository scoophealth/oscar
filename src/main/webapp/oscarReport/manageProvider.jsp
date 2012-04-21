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
	import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbReport.jspf"%>
<%    
 
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  String nowDate = String.valueOf(curYear)+"-"+String.valueOf(curMonth) + "-" + String.valueOf(curDay);
//String nowDate = "2002-08-21";
int dob_yy=0, dob_dd=0, dob_mm=0, age=0;
//int  recordAffected = apptMainBean.queryExecuteUpdate(nowDate,"delete_reportagesex_bydate");
String demo_no="", demo_sex="", provider_no="", roster="", patient_status="", status="";
String demographic_dob="1800";
String action = request.getParameter("action");
String last_name="", first_name="", mygroup="";
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscarReport.manageProvider.title" /></title>
<link rel="stylesheet" href="oscarReport.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script language="JavaScript">
<!--

function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}
function openBrWindow(theURL,winName,features) { 
  window.open(theURL,winName,features);
}

function refresh() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    history.go(0);
  }
}
//-->
</script>


</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0"
	topmargin="10">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%"><input type='button' name='print'
			value='<bean:message key="global.btnPrint"/>'
			onClick='window.print()'></td>
		<td width="90%" align="left">
		<p><font face="Verdana, Arial, Helvetica, sans-serif"
			color="#FFFFFF"><b><font
			face="Arial, Helvetica, sans-serif" size="4">oscar<font
			size="3">Report</font></font></b></font></p>
		</td>
	</tr>
</table>
<form name="form1" action="dbManageProvider.jsp" method="post">
<table width="50%" border="0" cellspacing="0" cellpadding="2">
	<tr bgcolor="#CCCCFF">
		<td width="100%" colspan="3" align="center"><b><bean:message
			key="oscarReport.manageProvider.msgManageProvider" /> <%=action.toUpperCase()%></b>
		</td>
	</tr>
	<tr bgcolor="#CCCCFF">
		<td width="40%"><bean:message
			key="oscarReport.manageProvider.msgTeam" /></td>
		<td width="50%" align="left"><bean:message
			key="oscarReport.manageProvider.msgProviderName" /></td>
		<td width="10%" align="left"><bean:message
			key="oscarReport.manageProvider.msgCheck" /></td>
	</tr>

	<% 

    ResultSet rsdemo = null;
  boolean bodd=true;	
	    ResultSet rsdemo2 = null;
	        ResultSet rsdemo3 = null;
	    int count1 = 0;
		 String param4 = "%";
 				String[] param =new String[3];
		 	          	    	
		 	          	    	   
		 	          	 
		 	          
		 	          	    rsdemo = apptMainBean.queryResults(param4, "search_mygroup");
		 	             while (rsdemo.next()) { 
		 	             
		 	                   bodd=bodd?false:true; //for the color of rows
		 	             
		 	             
				     		 	          	    rsdemo2 = apptMainBean.queryResults(rsdemo.getString("mygroup_no"), "search_mygroup_provider");
		 	             while (rsdemo2.next()) { 
		 	             param[0] = rsdemo2.getString("provider_no");
		 	             param[1] = rsdemo2.getString("mygroup_no");
		 	             param[2] = action;
		 	             status = "";
		 	             rsdemo3 = apptMainBean.queryResults(param, "search_reportprovider_check");
		 	                    while (rsdemo3.next()) {
		 	                    status = rsdemo3.getString("status");
		 	                    }
		 	   
		 	             
		 	             
%>

	<tr bgcolor="<%=bodd?"#EEEEFF":"white"%>">
		<td width="40%"><%=rsdemo2.getString("mygroup_no")%></td>
		<td width="50%" align="left"><%=rsdemo2.getString("last_name")%>,
		<%=rsdemo2.getString("first_name")%>
	</tr>
	<td width="10%" align="left"><input type="checkbox"
		name="provider<%=count1%>"
		value="<%=rsdemo2.getString("provider_no")%>|<%=rsdemo2.getString("mygroup_no")%>"
		<%=status.equals("A")?"checked":""%>></td>
	</tr>

	<%
count1 = count1 + 1;

		 	          }
             
             

}
 	    
%>
	<tr>
		<td colspan=3><input type="hidden" name="submit" value="Submit">
		<input type=submit
			value=<bean:message key="oscarReport.manageProvider.btnSubmit"/>>
		<input type=hidden name=action value=<%=action%>> <input
			type=hidden name=count value=<%=count1%>></td>
	</tr>
</table>
</form>
</body>
</html:html>
