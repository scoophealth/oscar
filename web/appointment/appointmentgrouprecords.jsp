<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<% 
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String curProvider_no = request.getParameter("provider_no");
  String mygroupno = (String) session.getAttribute("groupno");  
%>

<%@ page import="java.util.*, java.sql.*,java.net.*, oscar.*" errorPage="errorpage.jsp" %>
<jsp:useBean id="groupApptBean" class="oscar.AppointmentMainBean" scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
    {"search_groupprovider", "select p.last_name, p.first_name, p.provider_no from mygroup m, provider p where m.mygroup_no=? and m.provider_no=p.provider_no order by p.last_name"}, 
    {"add_appt", "insert into appointment (provider_no,appointment_date,start_time,end_time,name, notes,reason,location,resources,type, style,billing,status,createdatetime,creator, remarks, demographic_no) values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?)"}, 
    {"delete", "delete from appointment where appointment_date=? and start_time=? and end_time=? and name=? and creator=?"}, 
  };
  groupApptBean.doConfigure(dbParams,dbQueries);
%>

<%
  if(request.getParameter("groupappt")!=null && request.getParameter("groupappt").equals("Add Group Appointment") ) {
    String[] param =new String[16];
    int rowsAffected=0, datano=0;
    StringBuffer strbuf=new StringBuffer();

  	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	    strbuf=new StringBuffer(e.nextElement().toString());
		  if( strbuf.toString().indexOf("data")==-1 ) continue;
		  datano=Integer.parseInt(request.getParameter(strbuf.toString()) );
    
	    param[0]=request.getParameter("provider_no"+datano);
	    param[1]=request.getParameter("appointment_date");
	    param[2]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
	    param[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
	    param[4]=request.getParameter("keyword");
	    param[5]=request.getParameter("notes");
	    param[6]=request.getParameter("reason");
	    param[7]=request.getParameter("location");
	    param[8]=request.getParameter("resources");
	    param[9]=request.getParameter("type");
	    param[10]=request.getParameter("style");
	    param[11]=request.getParameter("billing");
	    param[12]=request.getParameter("status");
	    param[13]=request.getParameter("createdatetime");
	    param[14]=request.getParameter("creator");
	    param[15]=request.getParameter("remarks");
	    int[] intparam=new int [1];
	    if(!(request.getParameter("demographic_no").equals(""))) intparam[0]= Integer.parseInt(request.getParameter("demographic_no"));
	    else intparam[0]=0;
      rowsAffected = groupApptBean.queryExecuteUpdate(param,intparam,"add_appt");
    }
    
    if (rowsAffected ==1) {
%>
  <p><h1>Successful Addition of a Group Record.</h1></p>
<script LANGUAGE="JavaScript">
      self.close();
     	self.opener.refresh();
</script>
<%
    }  else {
%>
  <p><h1>Sorry, addition has failed.</h1></p>
<%  
    }
    groupApptBean.closePstmtConn();
    return;
  }
%>
<html> 
<head>
<title> Group Appt</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
}

//-->
</SCRIPT>
</head>
<body  background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()"  topmargin="0" leftmargin="0" rightmargin="0">
<form name="groupappt" method="POST" action="appointmentgrouprecords.jsp">
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
	  <tr bgcolor="#486ebd"><th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">GROUP APPOINTMENT</font></th></tr>
</table>

	<%
  String temp=null;
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("dboperation") ||temp.equals("displaymode") ||temp.equals("search_mode") ||temp.equals("chart_no")) continue;
  	out.println("<input type='hidden' name='"+temp+"' value='"+request.getParameter(temp)+"'>");
  }
	%> 

    Select Providers:
          <table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%" BGCOLOR="#C0C0C0">
            <tr BGCOLOR="#CCFFFF" > 
              <td ALIGN="center" colspan=2> <font face="arial"> Group : <%=mygroupno%></font></td>
          </tr>
<%
   int i=0;
  ResultSet rsdemo = groupApptBean.queryResults(mygroupno, "search_groupprovider");
  while (rsdemo.next()) { 
     i++;

%>      
          <tr BGCOLOR="#C4D9E7">
            <td align='right' width='50%'>  &nbsp;<%=rsdemo.getString("p.last_name")%>, <%=rsdemo.getString("p.first_name")%></font></td>
              <td >   &nbsp;
                <input type="checkbox" name="data<%=i%>" value="<%=i%>">
                <input type="hidden" name="provider_no<%=i%>" value="<%=rsdemo.getString("p.provider_no")%>">
                <INPUT TYPE="hidden" NAME="last_name<%=i%>" VALUE='<%=rsdemo.getString("p.last_name")%>'>
                <INPUT TYPE="hidden" NAME="first_name<%=i%>" VALUE='<%=rsdemo.getString("p.first_name")%>'>
              </td>
          </tr>
<%
   }
   groupApptBean.closePstmtConn();
%>
    </tr>
    </table>
<table width="100%" BGCOLOR="#486ebd">
  <tr>
    <TD nowrap>
        <INPUT TYPE="submit" NAME="groupappt" VALUE="Add Group Appointment" >
      </TD>
    <TD></TD>
    <TD align="right"><INPUT TYPE = "RESET" VALUE = " Cancel " onClick="window.close();"></TD>
  </tr>
</TABLE>
	</form>
</body>
</html>