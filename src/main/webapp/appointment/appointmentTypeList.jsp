<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*, oscar.appt.*, org.oscarehr.common.dao.AppointmentTypeDao, org.oscarehr.common.model.AppointmentType, org.oscarehr.util.SpringUtils" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ include file="../admin/dbconnection.jsp" %>
<%--RJ 07/07/2006 --%>
<%  
  String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
  
  String sError = "";
  if (request.getParameter("err")!=null &&  !request.getParameter("err").equals(""))
  	sError = "Error: " + request.getParameter("err");
%>

<%@ page errorPage="../errorpage.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="oscar.util.*" %>
<%@ page import="oscar.login.*" %>
<%@ page import="oscar.log.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
  <html>
    <head>
      <title>
        APPOINTMENT TYPES
      </title>
<script language="JavaScript">
	function popupPage(vheight,vwidth,title,varpage) {
		var page = "" + varpage;
		var leftVal = (screen.width-850) / 2;
		var topVal = (screen.height-300) / 2;  		
		windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top="+topVal+",left="+leftVal;
		var popup=window.open(page, title, windowprops);
  		if (popup != null) {
    		if (popup.opener == null) {
      			popup.opener = self;
    		}
    		popup.focus();
		}
	}
	function popupResponce(href)
	{
		window.location.href = href;
	}
	function setfocus() {
		this.focus();
	  document.forms[0].name.focus();
	  document.forms[0].name.select();
	}

	function upCaseCtrl(ctrl) {
		ctrl.value = ctrl.value.toUpperCase();
	}

	function onBlockFieldFocus(obj) {
	  obj.blur();
	  document.forms[0].name.focus();
	  document.forms[0].name.select();
	  window.alert("Please enter appointment type name");
	}

	function checkTypeNum(typeIn) {
		var typeInOK = true;
		var i = 0;
		var length = typeIn.length;
		var ch;
		// walk through a string and find a number
		if (length>=1) {
		  while (i <  length) {
			  ch = typeIn.substring(i, i+1);
			  if (ch == ":") { i++; continue; }
			  if ((ch < "0") || (ch > "9") ) {
				  typeInOK = false;
				  break;
			  }
		    i++;
	      }
		} else typeInOK = false;
		return typeInOK;
	}
	function checkTimeTypeIn(obj) {
	  if(!checkTypeNum(obj.value) ) {
//		  alert ("Please enter numeric value in Duration field");
		} else {
		  if(obj.value == '') {
		    alert("Please enter value in Names field");
			onBlockFieldFocus(obj);
		  } 
		}
	}
</script>
    </head>
    <body topmargin="0" leftmargin="0" rightmargin="0">
	<table width="100%">
	  <tr><td colspan="3" height="30"></td></tr>
	  <tr>	
	  <td width="100">&nbsp;</td>
	  <td align="center">
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr bgcolor="#486ebd" height="30">
          <th align="LEFT" width="90%">
            <font face="Helvetica" color="#FFFFFF">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:errors/>
            </font>
          </th>
          <td nowrap>
            <font size="-1" color="#FFFFFF">&nbsp;
            </font>
          </td>
        </tr>
      </table>
      <table width="100%" border="0" bgcolor="ivory" cellspacing="1" cellpadding="1">
          <tr bgcolor="mediumaquamarine">
            <th align="right"></th>
			<th colspan="6" align="left">
              &nbsp;&nbsp;&nbsp;&nbsp; Appointment Types
            </th>
          </tr>
          <tr>
            <td colspan=7>
              <center>
				<html:form action="appointment/appointmentTypeAction">  
  					<input TYPE="hidden" NAME="oper" VALUE="save" />
  					<input TYPE="hidden" NAME="id" VALUE="<bean:write name="AppointmentTypeForm" property="id"/>" /> 
  					<table border=0 cellspacing=0 cellpadding=0 width="100%" > 
    					<tr  bgcolor="#CCCCFF"> 
      						<th><font face="Helvetica">EDIT APPOINTMENT TYPE</font></th> 
    					</tr> 
  					</table> 
  					<table border="0" cellpadding="0" cellspacing="0" width="100%"> 
    					<tr> 
      						<td width="100%"> 
      							<table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%" BGCOLOR="#C0C0C0"> 
          							<tr valign="middle" BGCOLOR="#EEEEFF"> 
            							<td width="30%"> <div align="right"><font face="arial">Name:</font></div></td> 
			            				<td width="25%"> <INPUT TYPE="TEXT" NAME="name" VALUE="<bean:write name="AppointmentTypeForm" property="name"/>" WIDTH="10" HEIGHT="20" border="0" hspace="2" maxlength="50" onChange="checkTimeTypeIn(this)"> 
            							<td width="20%"> <div align="right"><font face="arial">Duration:</font></div></td> 
			            				<td width="25%"> <INPUT TYPE="TEXT" NAME="duration" VALUE="<bean:write name="AppointmentTypeForm" property="duration"/>" WIDTH="5" HEIGHT="20" border="0"  onChange="checkTimeTypeIn(this)"> </td> 
          							</tr> 
          							<tr valign="middle" BGCOLOR="#EEEEFF"> 
            							<td > <div align="right"><font face="arial"><font face="arial">Reason:</font></font></div></td> 
            							<td > <TEXTAREA NAME="reason" COLS="40" ROWS="2" border="0" hspace="2"><bean:write name="AppointmentTypeForm" property="reason"/></TEXTAREA> </td> 
            							<td > <div align="right"><font face="arial">Notes:</font></div></td> 
            							<td > <TEXTAREA NAME="notes"  COLS="40" ROWS="2" border="0" hspace="2"><bean:write name="AppointmentTypeForm" property="notes"/></TEXTAREA> </td> 
          							</tr> 
          							<tr valign="middle" BGCOLOR="#EEEEFF" > 
            							<td align="right"> <font face="arial">Location:</font> </td>
            							<td>
            							  <logic:notEmpty name="locationsList"> 
											<html:select property="location" >
												<html:option value="0">Select Location</html:option>
												<logic:iterate id="location" name="locationsList">
												    <bean:define id="locValue" ><bean:write name='location' property='label'/></bean:define>
													<html:option value="<%= locValue %>">
														<bean:write name="location" property="label"/>
													</html:option>	
												</logic:iterate>
											</html:select>
										  </logic:notEmpty>
										  <logic:empty name="locationsList">
            								<INPUT TYPE="TEXT" NAME="location" VALUE="<bean:write name="AppointmentTypeForm" property="location"/>" WIDTH="30" HEIGHT="20" border="0" hspace="2" maxlength="30" > 
										  </logic:empty>
										</td>	
            							<td > <div align="right"><font face="arial">Resources:</font></div></td> 
            							<td ><INPUT TYPE="TEXT" NAME="resources" VALUE="<bean:write name="AppointmentTypeForm" property="resources"/>" WIDTH="10" HEIGHT="20" maxlength="10" border="0" hspace="2" > </td> 
          							</tr> 
        						</table>
        					</td> 
    					</tr> 
  					</table>
  					<table border="0" cellpadding="0" cellspacing="0" width="100%"> 
    					<tr bgcolor="#CCCCFF"> 
      						<TD nowrap align="center"> <html:submit value="    Save  "  /> 
      						</TD> 
    					</tr> 
  					</table> 
				</html:form>
	          </center>
            </td>
          </tr>
          <tr bgcolor="silver">
            <th width="15%" nowrap>
              Name
            </th>
            <th width="5%" nowrap>
              Duration
            </th>
            <th width="20%" nowrap>
              Reason
            </th>
            <th width="20%" nowrap>
              Notes
            </th>
            <th width="15%" nowrap>
              Location
            </th>
            <th width="15%" nowrap>
              Resources
            </th>
            <th width="10%" nowrap>
            </th>
          </tr>
<% 
boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable();
List<AppointmentType> types = new ArrayList<AppointmentType>();
AppointmentTypeDao dao = (AppointmentTypeDao) SpringUtils.getBean("appointmentTypeDao");
types = dao.listAll();

  int rowNum = 0;
  String color = "#ccCCFF";
  String bgColor = "#EEEEFF";
  if(types != null && types.size()>0) {
  	for(AppointmentType type : types) {
		bgColor = bgColor.equals("#EEEEFF")?color:"#EEEEFF";
%> 
          <tr bgcolor="<%=bgColor%>">
            <td>
              <%= type.getName() %>
            </td>
            <th >
              <%= Integer.toString(type.getDuration()) %> min
            </th>
            <th >
              <%= type.getReason() %>
            </th>
            <th >
              <%= type.getNotes() %>
            </th>
            <th nowrap>
              <%= type.getLocation() %>
            </th>
            <th nowrap>
              <%= type.getResources() %>
            </th>
            <th nowrap>
              <a href="appointmentTypeAction.do?oper=edit&no=<%= type.getId() %>">edit</a>
              &nbsp;&nbsp;<a href="javascript:delType('appointmentTypeAction.do?oper=del&no=<%= type.getId() %>')">delete</a>
            </th>
          </tr>
<%
  	}
  }
%>
        </table>
	  <td width="100">&nbsp;</td>
	  </tr>
	</table>
	</body>
<script type="text/javascript">
function delType(url) {
var answer = confirm("Type will be deleted! Are you sure?")
	if (answer){
		window.location = url;
	} 
}
</script>
  </html>
