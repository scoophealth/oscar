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
  if(session.getValue("user") == null)    response.sendRedirect("../logout.htm");
  String curProvider_no = request.getParameter("provider_no");
  String curUser_no = (String) session.getAttribute("user");
  String userfirstname = (String) session.getAttribute("userfirstname");
  String userlastname = (String) session.getAttribute("userlastname");

  boolean bFirstDisp=true; //this is the first time to display the window
  if (request.getParameter("bFirstDisp")!=null) bFirstDisp= (request.getParameter("bFirstDisp")).equals("true");
%>
<%@ page import="java.util.*" errorPage="errorpage.jsp"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ADD APPOINTMENTS</title>
</head>
<meta http-equiv="Cache-Control" content="no-cache">


<script language="javascript">
<!-- start javascript ---- check to see if it is really empty in database
function setfocus() {
	this.focus();
  document.ADDAPPT.keyword.focus();
  document.ADDAPPT.keyword.select();
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}
function onBlockFieldFocus(obj) {
  obj.blur();
  document.ADDAPPT.keyword.focus();
  document.ADDAPPT.keyword.select();
  window.alert("Please type in name in the Name field and then click 'Search' button.");
}

// stop javascript -->
</script>

<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<FORM NAME="ADDAPPT" METHOD="post" ACTION="appointmentcontrol.jsp">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">MAKE
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
					VALUE="<%=bFirstDisp?(request.getParameter("year")+"-"+request.getParameter("month")+"-"+request.getParameter("day") ):request.getParameter("appointment_date")%>"
					WIDTH="25" HEIGHT="20" border="0" hspace="2"></td>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial"> Status :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="status"
					VALUE="<%=bFirstDisp?"T":request.getParameter("status").equals("")?"":request.getParameter("status")%>"
					onBlur="upCaseCtrl(this)" WIDTH="25" HEIGHT="20" border="0"
					hspace="2"></td>
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
					VALUE="<%=bFirstDisp?"":request.getParameter("type").equals("")?"":request.getParameter("type")%>"
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
					TYPE="TEXT" NAME="keyword"
					VALUE="<%=bFirstDisp?"":request.getParameter("name").equals("")?session.getAttribute("appointmentname"):request.getParameter("name")%>"
					HEIGHT="20" border="0" hspace="2" width="25" tabindex="1">
				</td>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial"> <INPUT
					TYPE="hidden" NAME="orderby" VALUE="last_name"> <INPUT
					TYPE="hidden" NAME="search_mode" VALUE="search_name"> <INPUT
					TYPE="hidden" NAME="originalpage"
					VALUE="../appointment/addappointment.jsp"> <INPUT
					TYPE="hidden" NAME="limit1" VALUE="0"> <INPUT
					TYPE="hidden" NAME="limit2" VALUE="5"> <!--input type="hidden" name="displaymode" value="Search " -->
				<INPUT TYPE="submit" NAME="displaymode" VALUE="Search "></font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><input
					type="TEXT" name="demographic_no" ONFOCUS="onBlockFieldFocus(this)"
					readonly
					value="<%=bFirstDisp?"":request.getParameter("demographic_no").equals("")?"":request.getParameter("demographic_no")%>"
					width="25" height="20" border="0" hspace="2"></td>
			</tr>
			<tr valign="middle">
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Reason :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><font
					face="Times New Roman"> <textarea name="reason" tabindex="2"
					rows="2" wrap="virtual" cols="18"><%=bFirstDisp?"":request.getParameter("reason").equals("")?"":request.getParameter("reason")%></textarea>
				</font></TD>
				<td width="5%" BGCOLOR="#C4D9E7"><font face="Times New Roman">
				</font></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Notes :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><font
					face="Times New Roman"> <textarea name="notes" tabindex="3"
					rows="2" wrap="virtual" cols="18"><%=bFirstDisp?"":request.getParameter("notes").equals("")?"":request.getParameter("notes")%></textarea>
				</font></td>
			</tr>
			<tr valign="middle">
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Location :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><input
					type="TEXT" name="location" tabindex="4"
					value="<%=bFirstDisp?"":request.getParameter("location").equals("")?"":request.getParameter("location")%>"
					width="25" height="20" border="0" hspace="2"></TD>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Resources :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><input
					type="TEXT" name="resources" tabindex="5"
					value="<%=bFirstDisp?"":request.getParameter("resources").equals("")?"":request.getParameter("resources")%>"
					width="25" height="20" border="0" hspace="2"></td>
			</tr>
			<tr valign="middle">
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Creator :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="user_id" readonly
					VALUE='<%=bFirstDisp?(userlastname+", "+userfirstname):request.getParameter("user_id").equals("")?"Unknown":request.getParameter("user_id")%>'
					WIDTH="25" HEIGHT="20" border="0" hspace="2"></td>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Date Time :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<%
				GregorianCalendar now=new GregorianCalendar();
				String strDateTime=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+" "
					+	now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);
			%> <INPUT TYPE="TEXT" NAME="createdatetime" readonly
					VALUE="<%=strDateTime%>" WIDTH="25" HEIGHT="20" border="0"
					hspace="2"> <INPUT TYPE="hidden" NAME="provider_no"
					VALUE="<%=curProvider_no%>"> <INPUT TYPE="hidden"
					NAME="dboperation" VALUE="add_apptrecord"> <INPUT
					TYPE="hidden" NAME="creator" VALUE="<%=curUser_no%>"> <INPUT
					TYPE="hidden" NAME="remarks" VALUE=""></td>
			</tr>
		</table>

		</td>
	</tr>
</table>

<table width="100%" BGCOLOR="#486ebd">
	<tr>
		<TD align="RIGHT" width="50%"><INPUT TYPE="submit"
			NAME="displaymode" tabindex="6" VALUE="Add Appointment" SIZE="7">
		</TD>
		<TD></TD>
		<TD align="LEFT"><INPUT TYPE="RESET" VALUE=" Cancel "
			onClick="window.close();"></TD>
	</tr>
</TABLE>

</FORM>


</body>
</html>