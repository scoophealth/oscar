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
  if(session.getValue("user") == null)    response.sendRedirect("../logout.htm");
  String curProvider_no = request.getParameter("provider_no");
  String curUser_no = (String) session.getAttribute("user");

  boolean bFirstDisp=true; //this is the first time to display the window
  if (request.getParameter("bFirstDisp")!=null) bFirstDisp= (request.getParameter("bFirstDisp")).equals("true");
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
<!-- 
function setfocus() {
	this.focus();
  document.EDITAPPT.keyword.focus();
  document.EDITAPPT.keyword.select();
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}
function onBlockFieldFocus(obj) {
  obj.blur();
  document.EDITAPPT.keyword.focus();
  document.EDITAPPT.keyword.select();
  window.alert("Please type in name in the Name field and then click 'Search' button.");
}
function demographicdetail(vheight,vwidth) {
  if(document.EDITAPPT.demographic_no.value=="") return;
  self.close();
  var page = "../demographic/demographiccontrol.jsp?demographic_no=" + document.EDITAPPT.demographic_no.value+"&displaymode=edit&dboperation=search_detail";
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
  var popup=window.open(page, "demographic", windowprops);
}
function labelprint(vheight,vwidth,varpage) {
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
  var popup=window.open(page, "encounterhist", windowprops);
}
// stop javascript -->
</script>

<body onload="setfocus()" background="../images/gray_bg.jpg"
	bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<FORM NAME="EDITAPPT" METHOD="post" ACTION="appointmentcontrol.jsp">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">EDIT
		AN APPOINTMENT</font></th>
	</tr>
</table>

<%
  String chartno="";
  String[] param = new String[1];
  param[0]=request.getParameter("appointment_no");
  String strApptDate = bFirstDisp?(request.getParameter("year")+"-"+request.getParameter("month")+"-"+request.getParameter("day") ):request.getParameter("appointment_date") ;
/*  param[0]=curProvider_no;
  param[1]=bFirstDisp?(request.getParameter("year")+"-"+request.getParameter("month")+"-"+request.getParameter("day") ):request.getParameter("appointment_date") ;
  param[2]=request.getParameter("start_time");
  param[3]=request.getParameter("demographic_no");
*/  
  ResultSet rs = null, rsdemo = null;
  if(bFirstDisp) rs = apptMainBean.queryResults(param, request.getParameter("dboperation"));
  if(bFirstDisp&&rs==null) {
    out.println("failed!!! No such appointment to edit! Select Back button.");
  } else {
    while (bFirstDisp?rs.next():true) { 
	  //get chart_no from demographic table if it exists.
	  if(bFirstDisp && rs.getString("demographic_no")!=null) {
	    String demono=rs.getString("demographic_no");
		  if(!demono.equals("0") && !demono.equals("") ) {
    	  rsdemo = apptMainBean.queryResults(demono, "search_detail");
		    if(rsdemo.next()) chartno = rsdemo.getString("chart_no");
		  }
	  }  
%>
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
					TYPE="TEXT" NAME="appointment_date" VALUE="<%=strApptDate%>"
					WIDTH="25" HEIGHT="20" border="0" hspace="2"></td>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial"> Status :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="status"
					VALUE="<%=bFirstDisp?rs.getString("status"):request.getParameter("status")%>"
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
					VALUE="<%=bFirstDisp?MyDateFormat.getTimeXX_XXampm(rs.getString("start_time")):request.getParameter("start_time")%>"
					WIDTH="25" HEIGHT="20" border="0" hspace="2"></td>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial"> Type :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="type"
					VALUE="<%=bFirstDisp?rs.getString("type"):request.getParameter("type")%>"
					WIDTH="25" HEIGHT="20" border="0" hspace="2"></td>
			</tr>
			<tr valign="middle">
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial"> End Time :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="end_time"
					VALUE="<%=bFirstDisp?MyDateFormat.getTimeXX_XXampm(rs.getString("end_time")):request.getParameter("end_time")%>"
					WIDTH="25" HEIGHT="20" border="0" hspace="2"></td>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="Arial, Helvetica, sans-serif">Chart
				No. :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><input
					type="TEXT" name="chart_no" readonly
					value="<%=bFirstDisp?chartno:request.getParameter("chart_no")%>"
					width="25" height="20" border="0" hspace="2"></td>
			</tr>
			<tr valign="middle">
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial"> <a href=#
					onClick="demographicdetail(550,700)">Name</a> :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="keyword" tabindex="1"
					VALUE="<%=bFirstDisp?rs.getString("name"):request.getParameter("name")%>"
					HEIGHT="20" border="0" hspace="2" width="25"></td>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="hidden" NAME="orderby" VALUE="last_name"> <INPUT
					TYPE="hidden" NAME="search_mode" VALUE="search_name"> <INPUT
					TYPE="hidden" NAME="originalpage"
					VALUE="../appointment/editappointment.jsp"> <INPUT
					TYPE="hidden" NAME="limit1" VALUE="0"> <INPUT
					TYPE="hidden" NAME="limit2" VALUE="5"> <!--input type="hidden" name="displaymode" value="Search " -->
				<div align="right"><input type="submit" name="displaymode"
					value="Search "></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><input
					type="TEXT" name="demographic_no" onFocus="onBlockFieldFocus(this)"
					readonly
					value="<%=bFirstDisp?( rs.getInt("demographic_no")==0?"":(""+rs.getInt("demographic_no")) ):request.getParameter("demographic_no")%>"
					width="25" height="20" border="0" hspace="2"></td>
			</tr>
			<tr valign="middle">
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial"> Reason :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><font
					face="Times New Roman"> <textarea name="reason" tabindex="2"
					rows="2" wrap="virtual" cols="18"><%=bFirstDisp?rs.getString("reason"):request.getParameter("reason")%></textarea>
				</font></TD>
				<td width="5%" BGCOLOR="#C4D9E7"><font face="Times New Roman">
				</font></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Notes :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><font
					face="Times New Roman"> <textarea name="notes" tabindex="3"
					rows="2" wrap="virtual" cols="18"><%=bFirstDisp?rs.getString("notes"):request.getParameter("notes")%></textarea>
				</font></td>
			</tr>
			<tr valign="middle">
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Location :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><INPUT
					TYPE="TEXT" NAME="location" tabindex="4"
					VALUE="<%=bFirstDisp?rs.getString("location"):request.getParameter("location")%>"
					WIDTH="25" HEIGHT="20" border="0" hspace="2"></td>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Resources :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT"><input
					type="TEXT" name="resources" tabindex="5"
					value="<%=bFirstDisp?rs.getString("resources"):request.getParameter("resources")%>"
					width="25" height="20" border="0" hspace="2"></td>
			</tr>
			<tr valign="middle">
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Last Creator :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<%
  String lastCreatorNo = bFirstDisp?rs.getString("creator"):"";
  String lastCreatorName = bFirstDisp?"":request.getParameter("user_id");
  if(bFirstDisp) {
    if(lastCreatorNo==null || lastCreatorNo.equals("")) {
      lastCreatorName="Unknown";
    } else {
      ResultSet rspn = null;
      rspn = apptMainBean.queryResults(lastCreatorNo, "searchprovidername");
      while (rspn.next()) {
        lastCreatorName=rspn.getString("last_name") +", "+ rspn.getString("first_name");
      }
    }
  }
%> <INPUT TYPE="TEXT" NAME="user_id" VALUE="<%=lastCreatorName%>"
					readonly WIDTH="25" HEIGHT="20" border="0" hspace="2"></td>
				<td width="5%" BGCOLOR="#C4D9E7"></td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<div align="right"><font face="arial">Last Time :</font></div>
				</td>
				<td width="20%" BGCOLOR="#C4D9E7" ALIGN="LEFT">
				<%
      break;
    } //end while
  } //end if
  if(bFirstDisp) apptMainBean.closePstmtConn();
 
    // the cursor of ResultSet only goes through once from top
				GregorianCalendar now=new GregorianCalendar();
				String strDateTime=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+" "
					+	now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);
%> <INPUT TYPE="TEXT" NAME="lastcreatedatetime" readonly
					VALUE="<%=bFirstDisp?rs.getString("createdatetime"):request.getParameter("lastcreatedatetime")%>"
					WIDTH="25" HEIGHT="20" border="0" hspace="2"> <INPUT
					TYPE="hidden" NAME="createdatetime" VALUE="<%=strDateTime%>">
				<INPUT TYPE="hidden" NAME="provider_no" VALUE="<%=curProvider_no%>">
				<INPUT TYPE="hidden" NAME="dboperation" VALUE="update_apptrecord">
				<INPUT TYPE="hidden" NAME="creator" VALUE="<%=curUser_no%>">
				<INPUT TYPE="hidden" NAME="remarks" VALUE=""> <INPUT
					TYPE="hidden" NAME="appointment_no"
					VALUE="<%=request.getParameter("appointment_no")%>"></td>
			</tr>
		</table>
		</td>
	</tr>
</table>

<table width="100%" BGCOLOR="#486ebd">
	<tr>
		<TD align="center"><input type="submit" name="displaymode"
			value="Update Appointment" size="7"> <INPUT TYPE="submit"
			tabindex="6" name="displaymode" VALUE="Delete Appointment"> <INPUT
			TYPE="button" name="labelprint" VALUE="Label"
			onClick="window.open('../demographic/demographiclabelprintsetting.jsp?demographic_no='+document.EDITAPPT.demographic_no.value, 'labelprint','height=550,width=700,location=no,scrollbars=yes,menubars=no,toolbars=no' )">
		<input type="button" name="Button" value="Exit" onClick="self.close()">
		</TD>
	</tr>
</TABLE>

</FORM>
</body>
</html>