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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%
  if(session.getValue("user") == null)
    response.sendRedirect("../login.htm");
  String curProvider_no;
  curProvider_no = (String) session.getAttribute("user");
  //display the main provider page
  //includeing the provider name and a month calendar
%>
<%@ page import="java.util.*, java.sql.*, oscar.*"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>EDIT APPOINTMENTS</title>
</head>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">


<script language="javascript">
<!-- start javascript ---- check to see if it is really empty in database
    function start(){
      this.focus();
    }
// stop javascript -->
</script>

<body onload="start()" background="../images/gray_bg.jpg"
	bgproperties="fixed">
<FORM NAME="EDITAPPT" METHOD="post" ACTION="appointmentcontrol.jsp">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">EDIT
		AN APPOINTMENT</font></th>
	</tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="100%">

		<table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%"
			BGCOLOR="#C0C0C0">
			<tr valign="middle">
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial"> Date :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="appointment_date"
					VALUE="<%=request.getParameter("appointment_date").equals("")?(request.getParameter("year")+"-"+request.getParameter("month")+"-"+request.getParameter("day")):request.getParameter("appointment_date")%>"
					WIDTH="25" HEIGHT="20" border="0" hspace="2"></td>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial"> Status :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="status"
					VALUE="<%=request.getParameter("status").equals("")?"":request.getParameter("status")%>"
					WIDTH="25" HEIGHT="20" border="0" hspace="2"></td>
			</tr>
			<tr valign="middle">
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial"><font
					face="arial"> Start Time :</font></font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="start_time"
					VALUE="<%=request.getParameter("start_time")%>" WIDTH="25"
					HEIGHT="20" border="0" hspace="2"></td>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial"> Type :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="type"
					VALUE="<%=request.getParameter("type").equals("")?"":request.getParameter("type")%>"
					WIDTH="25" HEIGHT="20" border="0" hspace="2"></td>
			</tr>
			<tr valign="middle">
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial"> End Time :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="end_time"
					VALUE="<%=request.getParameter("end_time")%>" WIDTH="25"
					HEIGHT="20" border="0" hspace="2"></td>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">&nbsp;</td>
			</tr>
			<tr valign="middle">
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial"> Name :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="name"
					VALUE="<%=request.getParameter("name").equals("")?session.getAttribute("appointmentname"):request.getParameter("name")%>"
					HEIGHT="20" border="0" hspace="2" width="25"></td>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><input type="submit" name="displaymode"
					value="CPP ID"></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><input
					type="TEXT" name="cpp_id"
					value="<%=request.getParameter("cpp_id").equals("")?"":request.getParameter("cpp_id")%>"
					width="25" height="20" border="0" hspace="2"></td>
			</tr>
			<tr valign="middle">
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Notes:</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><font
					face="Times New Roman"> <textarea name="notes" rows="2"
					wrap="virtual" cols="18"><%=request.getParameter("notes").equals("")?"":request.getParameter("notes")%></textarea>
				</font></TD>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Reason :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><font
					face="Times New Roman"> <textarea name="reason" rows="2"
					wrap="virtual" cols="18"><%=request.getParameter("reason").equals("")?"":request.getParameter("reason")%></textarea>
				</font></td>
			</tr>
			<tr valign="middle">
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Location :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="location"
					VALUE="<%=request.getParameter("location").equals("")?"":request.getParameter("location")%>"
					WIDTH="25" HEIGHT="20" border="0" hspace="2"></td>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Resources :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><input
					type="TEXT" name="resources"
					value="<%=request.getParameter("resources").equals("")?"":request.getParameter("resources")%>"
					width="25" height="20" border="0" hspace="2"></td>
			</tr>
			<tr valign="middle">
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">User Id :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="user_id" VALUE="Muffin WebServer" WIDTH="25"
					HEIGHT="20" border="0" hspace="2"></td>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Date Time :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<%
    // the cursor of ResultSet only goes through once from top
				GregorianCalendar now=new GregorianCalendar();
				String strDateTime=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+" "
					+	now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);
%> <INPUT TYPE="TEXT" NAME="createdatetime" VALUE="<%=strDateTime%>"
					WIDTH="25" HEIGHT="20" border="0" hspace="2"> <INPUT
					TYPE="hidden" NAME="provider_no" VALUE="<%=curProvider_no%>">
				<INPUT TYPE="hidden" NAME="dboperation" VALUE="update_record">
				</td>
			</tr>
		</table>

		</td>
	</tr>
</table>

<table width="100%" BGCOLOR="#486ebd">
	<tr>
		<TD align="center"><INPUT TYPE="submit" NAME="displaymode"
			VALUE="Update Record" SIZE="7"> <INPUT TYPE="submit"
			name="displaymode" VALUE="Delete Record"> <input
			type="button" name="Button" value="Cancel" onClick="self.close()">
		</TD>
	</tr>
</TABLE>

</FORM>

<div align="center"><font size="1" face="Verdana" color="#0000FF"><B>StoneChurch
Family Health Centre</B></font></div>

</body>
</html>