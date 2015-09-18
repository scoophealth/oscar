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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.messenger" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_admin&type=_admin.messenger");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<!DOCTYPE html>
<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarMessenger.config.MessengerAdmin.title" /></title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../../share/css/extractedFromPages.css"  />

<script>
function BackToOscar()
{
    if (opener.callRefreshTabAlerts) {
	opener.callRefreshTabAlerts("oscar_new_msg");
        setTimeout("window.close()", 100);
    } else {
        window.close();
    }
}
</script>

<link rel="stylesheet" type="text/css" href="../styles.css">
</head>


<body topmargin="0" leftmargin="0" vlink="#0000FF">

<%
////////////////////////////////////////////////////////////////////////////////
   oscar.oscarMessenger.config.data.MsgMessengerGroupData adminUtil = new oscar.oscarMessenger.config.data.MsgMessengerGroupData();
   String grpNo = new String();
   if( request.getParameter("groupNo") != null || request.getAttribute("groupNo") != null){
      if (request.getAttribute("groupNo") != null){
         grpNo = (String) request.getAttribute("groupNo");
      }else{
         grpNo = request.getParameter("groupNo");
      }

   }else{
      grpNo = "0";
   }
   if (grpNo.equals("")){
     grpNo = "0";
   }

   String currGroupName = adminUtil.getMyName(grpNo);
    currGroupName = adminUtil.printAllBelowGroups(grpNo);
////////////////////////////////////////////////////////////////////////////////
%>

<html:errors />
<table border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse" bordercolor="#111111" width="100%"
	id="AutoNumber1" height="100%">

	<tr>
		<td width="10%" height="37" bgcolor="#000000">&nbsp;</td>
		<td width="100%" bgcolor="#000000"
			style="border-left: 2px solid #A9A9A9; padding-left: 5" height="0%">
		<p class="ScreenTitle"><bean:message
			key="oscarMessenger.config.MessengerAdmin.2ndTitle" /></p>
		</td>
	</tr>
	<tr>
		<td></td>
		<td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%"
			valign="top">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">

			<!-- Start new rows here -->
			<tr>
				<td><!--<div class="DivContentTitle"><bean:message key="displayMessages.title"/> of 
								<%
                                //oscar.oscarMessenger.pageUtil.SessionBean bean = (oscar.oscarMessenger.pageUtil.SessionBean)request.getSession().getAttribute("SessionBean");

                                /*out.print("dsffdsfdsuserName");*/%>
                      </div>-->
                </td>
			</tr>
			<tr>
				<td>
				<div class="DivContentSectionHead"><%=currGroupName%></div>
				</td>
			</tr>
			<tr>
				<td>
				<table>
					<tr>
						<td class=cellButtonLook><a
							href="MessengerCreateGroup.jsp?Level=<%=grpNo%>"><bean:message
							key="oscarMessenger.config.MessengerAdmin.newGroup" /></a></td>
						<td><bean:message
							key="oscarMessenger.config.MessengerAdmin.msgCreateNewGroup" /></td>
					</tr>
					<%
                           String par = adminUtil.parentDirectory(grpNo);

                           if ( !(par).equals("") ){;
                           %>
					<tr>
						<td class=cellButtonLook><a
							href="MessengerAdmin.jsp?groupNo=<%=par%>"><bean:message
							key="oscarMessenger.config.MessengerAdmin.goBack" /></a></td>
						<td><bean:message
							key="oscarMessenger.config.MessengerAdmin.msgGoToParent" /></td>
					</tr>
					<tr>
						<td class=cellButtonLook><a
							href="MessengerCreateGroup.jsp?Group=<%=grpNo%>"><bean:message
							key="oscarMessenger.config.MessengerAdmin.rename" /></a></td>
						<td><bean:message
							key="oscarMessenger.config.MessengerAdmin.msgChangeGroupsName" />
						</td>
					</tr>
					<%}%>
				</table>

				<%
                           adminUtil.parentDirectory(grpNo);
                        %> <br>


				<% //This Part Prints all the groups for this level

                        String groupList = adminUtil.printGroups(grpNo);
                        int numGroups = adminUtil.numGroups;
                        if ( numGroups > 0 ){%>
				<table>
					<tr>
						<td><bean:message
							key="oscarMessenger.config.MessengerAdmin.msgExploreLowerLevel" />
						</td>
					</tr>
				</table>
				<%    out.print (groupList);
                        }
                        %> <%
                        if (request.getAttribute("fail") != null){
                           String error = ((String) request.getAttribute("fail"));
                           out.print("<br><font color=red>"+error+"</font><br>");
                        }

                        %> 
                        <html:form	action="/oscarMessenger/UpdateMembers">
					<input type=hidden name="grpNo" value=<%=grpNo%>>
					<input type=submit name="update" class="ControlPushButton"
						value="<bean:message key="oscarMessenger.config.MessengerAdmin.btnUpdateGroupMembers"/>">
					<input type=submit name="delete" class="ControlPushButton"
						value="<bean:message key="oscarMessenger.config.MessengerAdmin.btnDeleteThisGroup"/>">
					<br>
					<bean:message
						key="oscarMessenger.config.MessengerAdmin.msgAllMembersChecked" />
					<div class="ChooseRecipientsBox1">
					<table>
						<tr>
							<th>&nbsp;</th>
							<th bgcolor=#eeeeff><bean:message
								key="oscarMessenger.config.MessengerAdmin.lastName" /></th>
							<th bgcolor=#eeeeff><bean:message
								key="oscarMessenger.config.MessengerAdmin.firstName" /></th>
							<th bgcolor=#eeeeff><bean:message
								key="oscarMessenger.config.MessengerAdmin.providerType" /></th>
						</tr>
						<%
                            adminUtil.printAllProvidersWithMembers(request.getLocale(),grpNo,out);
                           %>
					</table>
					</div>

				</html:form></td>
			</tr>
			<!-- End new rows here -->

			<tr height="100%">
				<td></td>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
	</tr>
	<tr>
		<td width="100%" height="0%" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC"
			colspan="2"></td>
	</tr>
	
</table>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>

<script>
$( document ).ready(function() {	
    parent.parent.resizeIframe($('html').height());
});
</script>
</body>
</html:html>
