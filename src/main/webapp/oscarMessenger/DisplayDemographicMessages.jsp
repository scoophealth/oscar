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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ page import="oscar.oscarDemographic.data.DemographicData"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_msg" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_msg");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%

//pageContext.setAttribute("pageType",""+pageType);


if (request.getParameter("orderby") != null){
    String orderby = request.getParameter("orderby");
    String sessionOrderby = (String) session.getAttribute("orderby");     
    if (sessionOrderby != null && sessionOrderby.equals(orderby)){
        orderby = "!"+orderby;
    }
    session.setAttribute("orderby",orderby);
}
String orderby = (String) session.getAttribute("orderby");

String moreMessages="false";
if (request.getParameter("moreMessages") != null){
   moreMessages=request.getParameter("moreMessages");
   }
final int INITIAL_DISPLAY=20;
%>

<logic:notPresent name="msgSessionBean" scope="session">
	<logic:redirect href="index.jsp" />
</logic:notPresent>
<logic:present name="msgSessionBean" scope="session">
	<bean:define id="bean"
		type="oscar.oscarMessenger.pageUtil.MsgSessionBean"
		name="msgSessionBean" scope="session" />
	<logic:equal name="bean" property="valid" value="false">
		<logic:redirect href="index.jsp" />
	</logic:equal>
</logic:present>
<%
oscar.oscarMessenger.pageUtil.MsgSessionBean bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean)pageContext.findAttribute("bean");
String demographic_no = "";
if(request.getParameter("demographic_no")!=null){
	demographic_no = request.getParameter("demographic_no");
}else{
	demographic_no = bean.getDemographic_no();
}

String demographic_name = "";
if ( demographic_no != null ) {   
    DemographicData demographic_data = new DemographicData();
    org.oscarehr.common.model.Demographic demographic = demographic_data.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographic_no);
    demographic_name = demographic.getLastName() + ", " + demographic.getFirstName();
}

%>
<jsp:useBean id="DisplayMessagesBeanId" scope="session"
	class="oscar.oscarMessenger.pageUtil.MsgDisplayMessagesBean" />
<% DisplayMessagesBeanId.setProviderNo(bean.getProviderNo());
bean.nullAttachment();%>
<jsp:setProperty name="DisplayMessagesBeanId" property="*" />
<jsp:useBean id="ViewMessageForm" scope="session"
	class="oscar.oscarMessenger.pageUtil.MsgViewMessageForm" />





<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<title><bean:message key="oscarMessenger.DisplayMessages.title" />
</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<style>
.TopStatusBar{
width:100% !important;
height:100% !important;
}
</style>

<script type="text/javascript">
function BackToOscar()
{
    if (opener.callRefreshTabAlerts) {
	opener.callRefreshTabAlerts("oscar_new_msg");
        setTimeout("window.close()", 100);
    } else {
        window.close();
    }
}

function unlink(){    
    document.forms[0].submit();    
}
</script>
</head>

<body class="BodyStyle" vlink="#0000FF" onload="window.focus()">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="oscarMessenger.DisplayMessages.msgMessenger" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>
				<div class="DivContentTitle">Messages related to <%=demographic_name%>
				</div>
				</td>
				<td></td>
				<td style="text-align: right">
				  <oscar:help keywords="message" key="app.top1"/> | 
				  <a href="javascript:void(0)" onclick="javascript:popupPage(600,700,'../oscarEncounter/About.jsp')"><bean:message key="global.about" /></a>
			    </td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn">
		<table width="100%">
			<tr>
				<td>
				<table cellspacing=3>
					<tr>
						<td>
						<table class=messButtonsA cellspacing=0 cellpadding=3>
							<tr>
								<td class="messengerButtonsA"><a
									href="javascript:BackToOscar()" class="messengerButtons"><bean:message
									key="oscarMessenger.DisplayMessages.btnExit" /></a></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>



			<tr>
				<td>
				<%String strutsAction = "/oscarMessenger/DisplayDemographicMessages.do?demographic_no="+demographic_no; %>

				<html:form action="<%=strutsAction%>">

					<table border="0" width="80%" cellspacing="1">
						<tr>
							<th bgcolor="#DDDDFF" width="75">&nbsp;</th>
							<th align="left" bgcolor="#DDDDFF">
							<% if (moreMessages.equals("true")){%> <html:link
								page="/oscarMessenger/DisplayDemographicMessages.jsp?orderby=from&moreMessages=true">
								<bean:message key="oscarMessenger.DisplayMessages.msgFrom" />
							</html:link> <%}else{%> <html:link
								page="/oscarMessenger/DisplayDemographicMessages.jsp?orderby=from&moreMessages=false">
								<bean:message key="oscarMessenger.DisplayMessages.msgFrom" />
							</html:link> <%}%>
							</th>
							<th align="left" bgcolor="#DDDDFF">
							<% if (moreMessages.equals("true")){%> <html:link
								page="/oscarMessenger/DisplayDemographicMessages.jsp?orderby=subject&moreMessages=true">
								<bean:message key="oscarMessenger.DisplayMessages.msgSubject" />
							</html:link> <%}else{%> <html:link
								page="/oscarMessenger/DisplayDemographicMessages.jsp?orderby=subject&moreMessages=false">
								<bean:message key="oscarMessenger.DisplayMessages.msgSubject" />
							</html:link> <%}%>
							</th>
							<th align="left" bgcolor="#DDDDFF">
							<% if (moreMessages.equals("true")){%> <html:link
								page="/oscarMessenger/DisplayDemographicMessages.jsp?orderby=date&moreMessages=true">
								<bean:message key="oscarMessenger.DisplayMessages.msgDate" />
							</html:link> <%}else{%> <html:link
								page="/oscarMessenger/DisplayDemographicMessages.jsp?orderby=date&moreMessages=false">
								<bean:message key="oscarMessenger.DisplayMessages.msgDate" />
							</html:link> <%}%>
							</th>
                                                        <th align="left" bgcolor="#DDDDFF">
                                                        <% if (moreMessages.equals("true")){%>
                                                            <html:link page="/oscarMessenger/DisplayDemographicMessages.jsp?orderby=linked&moreMessages=true">
                                                            <bean:message key="oscarMessenger.DisplayMessages.msgLinked"/>
                                                            </html:link>
                                                        <%}else{%>
                                                            <html:link page="/oscarMessenger/DisplayDemographicMessages.jsp?orderby=linked&moreMessages=false">
                                                            <bean:message key="oscarMessenger.DisplayMessages.msgLinked"/>
                                                            </html:link>
                                                        <%}%>
                                                        </th>
						</tr>
						<% //java.util.Vector theMessages = new java.util.Vector() ;
                                   java.util.Vector theMessages2 = new java.util.Vector() ;
                                   theMessages2 = DisplayMessagesBeanId.estDemographicInbox(orderby,demographic_no);
                                   String msgCount = Integer.toString(theMessages2.size());
                                %>
						<!--   for loop Control Initiliation variabe changed to nextMessage   -->
						<%for (int i = 0; i < theMessages2.size() ; i++) {  
                                        oscar.oscarMessenger.data.MsgDisplayMessage dm;
                                        dm = (oscar.oscarMessenger.data.MsgDisplayMessage) theMessages2.get(i);
                                        String isLastMsg = "false";
                            %>
						<tr>
							<td bgcolor="#EEEEFF" width="75"><html:checkbox
								property="messageNo" value="<%=dm.messageId %>" /> <% String atta = dm.attach;
                                            if (atta.equals("1")){
                                            %><img src="img/clip4.jpg">
							<%
                                            }
                                         %> &nbsp;</td>

							<td bgcolor="#EEEEFF"><%= dm.sentby  %></td>
							<td bgcolor="#EEEEFF"><a
								href="<%=request.getContextPath()%>/oscarMessenger/ViewMessage.do?from=encounter&demographic_no=<%=demographic_no%>&msgCount=<%=msgCount%>&orderBy=<%=orderby%>&messageID=<%=dm.messageId%>&messagePosition=<%=dm.messagePosition%>">
							<%=dm.thesubject%> </a></td>
							<td bgcolor="#EEEEFF"><%= dm.thedate  %></td>
                                                        <td bgcolor="#EEEEFF">
                                                            <oscar:nameage demographicNo="<%=dm.demographic_no%>"></oscar:nameage>
                                                        </td>
						</tr>
						<%}%>
					</table>
					<table width="80%">
						<tr>
							<td><input type="button" value="Unlink Messages"
								onclick="javascript:unlink();"></td>
							<%
                                       if (moreMessages.equals("false") && theMessages2.size()>=INITIAL_DISPLAY){
                                    %>
							<td width="60%"></td>
							<td align="left"><html:link
								page="/oscarMessenger/DisplayMessages.jsp?moreMessages=true">
								<bean:message key="oscarMessenger.DisplayMessages.msgAllMessage" />
							</html:link></td>
							<%}%>
						</tr>
					</table>

				</html:form></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
