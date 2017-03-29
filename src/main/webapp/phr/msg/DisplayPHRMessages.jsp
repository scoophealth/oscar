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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_phr" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_phr");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.myoscar.commons.MedicalDataType"%>
<%@page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.myoscar.client.ws_manager.MessageManager"%>
<%@page import="org.oscarehr.myoscar_server.ws.Message2RecipientPersonAttributesTransfer"%>
<%@page import="org.oscarehr.myoscar.client.ws_manager.AccountManager"%>
<%@page import="org.oscarehr.myoscar_server.ws.MinimalPersonTransfer2"%>
<%@page import="org.oscarehr.myoscar_server.ws.MessageTransfer3"%>
<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="oscar.util.DateUtils"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.phr.web.MyOscarMessagesHelper"%>
<%@page import="org.oscarehr.myoscar_server.ws.MessageTransfer"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.net.URLEncoder"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html-el" prefix="html-el" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested" prefix="nested" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/phr-tag.tld" prefix="phr" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>

<%@ page import="oscar.oscarDemographic.data.DemographicData"%>
<%@ page import="org.oscarehr.phr.model.PHRAction"%>
<%@ page import="oscar.oscarProvider.data.ProviderData"%>
<%@ page import="org.oscarehr.phr.model.PHRMessage"%>
<%@ page import="org.oscarehr.phr.dao.PHRActionDAO, org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="oscar.util.StringUtils"%>
<%@ page import="org.oscarehr.phr.indivo.service.accesspolicies.IndivoAPService" %>
<%@ page import="oscar.util.UtilDateUtilities" %>

<%
LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
String providerNo=loggedInInfo.getLoggedInProviderNo();
String providerName = loggedInInfo.getLoggedInProvider().getFormattedName();

MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);

request.setAttribute("forwardto", request.getRequestURI());

//some phrAction static constants
pageContext.setAttribute("STATUS_OTHER_ERROR", PHRAction.STATUS_OTHER_ERROR);
pageContext.setAttribute("STATUS_NOT_AUTHORIZED", PHRAction.STATUS_NOT_AUTHORIZED);
pageContext.setAttribute("STATUS_SENT", PHRAction.STATUS_SENT);
pageContext.setAttribute("STATUS_SEND_PENDING", PHRAction.STATUS_SEND_PENDING);
pageContext.setAttribute("STATUS_ON_HOLD", PHRAction.STATUS_ON_HOLD);

pageContext.setAttribute("ACTION_ADD", PHRAction.ACTION_ADD);
pageContext.setAttribute("ACTION_UPDATE", PHRAction.ACTION_UPDATE);

pageContext.setAttribute("TYPE_BINARYDATA", MedicalDataType.BINARY_DOCUMENT.name());
pageContext.setAttribute("TYPE_MEDICATION", MedicalDataType.MEDICATION.name());
pageContext.setAttribute("TYPE_ACCESSPOLICIES", "RELATIONSHIP");

String pageMethod = request.getParameter("method");
if (pageMethod==null) pageMethod = "viewMessages";

if (pageMethod.equals("delete") || pageMethod.equals("resend")) 
    pageMethod = "viewSentMessages";
if (pageMethod.equals("archive"))
    pageMethod = "viewMessages";
if (pageMethod.equals("unarchive"))
    pageMethod = "viewArchivedMessages";
    
request.setAttribute("pageMethod",pageMethod);
   
    GregorianCalendar now=new GregorianCalendar();
    int curYear = now.get(Calendar.YEAR);
    int curMonth = (now.get(Calendar.MONTH)+1);
    int curDay = now.get(Calendar.DAY_OF_MONTH);
    String dateString = curYear+"-"+curMonth+"-"+curDay;    
    
    //get Actions Pending Approval
    List<PHRAction> actionsPendingApproval = (List<PHRAction>) request.getSession().getAttribute("actionsPendingApproval");
    

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<html>
    <head>
        <html:base />
        <link rel="stylesheet" type="text/css" href="../../oscarMessenger/encounterStyles.css">
        <title>
        	myOSCAR
        </title>
        <script type="text/javascript" src="../../share/javascript/prototype.js"></script>
        <script type="text/javascript" src="../../share/javascript/Oscar.js"></script>
        <script type="text/javascript" src="../../phr/phr.js"></script>
        <style type="text/css">
        td.messengerButtonsA{
            /*background-color: #6666ff;*/
            /*background-color: #6699cc;*/
            background-color: #003399;
        }
        td.messengerButtonsACurrent{
            /*background-color: #6666ff;*/
            /*background-color: #6699cc;*/
            background-color: #00ae99;
        }
        td.messengerButtonsAWarning{
            background-color: red;
        }
        
        td.messengerButtonsD{
            /*background-color: #84c0f4;*/
            background-color: #555599;
        }
        a.messengerButtons{
            color: #ffffff;
            font-size: 9pt;
            text-decoration: none;
        }


        table.messButtonsA{
        border-top: 2px solid #cfcfcf;
        border-left: 2px solid #cfcfcf;
        border-bottom: 2px solid #333333;
        border-right: 2px solid #333333;
        }

        table.messButtonsD{
        border-top: 2px solid #333333;
        border-left: 2px solid #333333;
        border-bottom: 2px solid #cfcfcf;
        border-right: 2px solid #cfcfcf;
        }
        
        .myoscarLoginElementNoAuth {
            border: 0;
            padding-left: 3px;
            padding-right: 3px;
            background-color: #f3e9e9;
        }
        
        .myoscarLoginElementAuth {
            border: 0;
            padding-left: 3px;
            padding-right: 3px;
            background-color: #d9ecd8;
        }
        .moreInfoBoxoverBody{
            border: 1px solid #9fbbe8;
            padding: 1px;
            padding-left: 3px;
            padding-right: 3px;
            border-top: 0px;
            font-size: 10px;
            background-color: white;
        }
        .moreInfoBoxoverHeader{
            border: 1px solid #9fbbe8;
            background-color: #e8ecf3;
            padding: 2px;
            padding-left: 3px;
            padding-right: 3px;
            border-bottom: 0px;
            font-size: 10px;
            color: red;
        }
        table.messageTable {
        }
        
        tr.normal td {
            background-color: #EEEEFF;
        }
        tr.new td {
            
        }
        
        .notAuthorized {
            background-color: #ffcdb9;
        }
        .sendPending {
            background-color: #e1eddb;
        }
        .onHold {
            background-color: #edebdb;
        }
        .normal {
            background-color: #EEEEFF;
        }
        .new {
            background-color: #EEEEFF;
            font-weight: bold;
        }
        
        .statusDiv {
            background-color: #fb8781;
        }
        
        div.sharingAlert {
            background-color: #ffdf6f; /*#ffd649;*/
            width: 99%;
            font-size: 11px;
            margin-top: 1px;
            overflow: hidden;
            white-space: nowrap;
            
        }
        </style>

        <script type="text/javascript">
        function BackToOscar()
        {
               window.close();
        } 
        function setFocus() {
            if (document.getElementById('phrPassword'))
                document.getElementById('phrPassword').focus();
        }
        function reloadWindow() {
            window.location = "../../phr/PhrMessage.do?method=<%=request.getParameter("method")%>";
        }
        
        function setStatus(message) {
            if (message.length == 0) {
                $('statusDiv').hide();
            } else {
                $('statusDiv').show();
                $("statusDiv").innerHTML = message;
            }
        }
        
        function gotoEchart2(demoNo,myoscarmsg) {
            var url = '<%=request.getContextPath()%>/oscarEncounter/IncomingEncounter.do?demographicNo='+ demoNo+'&myoscarmsg='+myoscarmsg+'&appointmentDate=<%=UtilDateUtilities.DateToString(new Date())%>';
            popup(755,1048,url,'apptProvider');
        }
        
        function gotoEchart3(demoNo) {
            var url = '<%=request.getContextPath()%>/oscarEncounter/IncomingEncounter.do?demographicNo='+ demoNo+'&appointmentDate=<%=UtilDateUtilities.DateToString(new Date())%>';
            popup(755,1048,url,'apptProvider');
        }
        
        </script>
    </head>

    <body class="BodyStyle" vlink="#0000FF" onload="window.focus(); setFocus();">

    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <bean:message key="oscarMessenger.DisplayMessages.msgMessenger"/>
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                            <div class="DivContentTitle"><bean:message key="oscarMessenger.DisplayMessages.msgInbox"/></div>
                        </td>
                        <td  >
                        </td>
                        <td style="text-align:right">
                            <oscar:help keywords="myoscar message" key="app.top1"/> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about"/></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license"/></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">
                &nbsp;
            </td>
            <td class="MainTableRightColumn">
                <table width="100%">
                    <tr>
                        <td>
                            <table  cellspacing=3 style="display:inline">
                                <tr>
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA<%if (pageMethod.equals("viewMessages")) {%>Current<%}%>">
                                            <html:link page="/phr/PhrMessage.do?method=viewMessages" styleClass="messengerButtons">
                                                <bean:message key="oscarMessenger.DisplayMessages.msgInbox"/>
                                            </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA<%if (pageMethod.equals("viewSentMessages")) {%>Current<%}%>">
                                            <html:link page="/phr/PhrMessage.do?method=viewSentMessages" styleClass="messengerButtons">
                                                <bean:message key="oscarMessenger.DisplayMessages.msgSentTitle"/>
                                            </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA<%if (pageMethod.equals("viewArchivedMessages")) {%>Current<%}%>">
                                            <html:link page="/phr/PhrMessage.do?method=viewArchivedMessages" styleClass="messengerButtons">
                                                <bean:message key="oscarMessenger.DisplayMessages.msgArchived"/>
                                            </html:link>
                                        </td></tr></table>
                                    </td>
                                    <td >
                                        <table class=messButtonsA cellspacing=0 cellpadding=3><tr><td class="messengerButtonsA">
                                          	<a href="javascript:window.close()" class="messengerButtons">Exit</a>
                                        </td></tr></table>
                                    </td>
                                    <%
                                    	if (MyOscarUtils.isMyOscarEnabled((String) session.getAttribute("user")) && myOscarLoggedInInfo!=null && myOscarLoggedInInfo.isLoggedIn())
                                    	{
		                                    %>
		                                        <td class="myoscarLoginElementAuth">
		                                            <div>
		                                                Status: <b>Logged in as <%=myOscarLoggedInInfo.getLoggedInPerson().getFirstName()+' '+myOscarLoggedInInfo.getLoggedInPerson().getLastName()%></b> (<%=myOscarLoggedInInfo.getLoggedInPerson().getUserName()%>)
		                                                <form action="../../phr/Logout.do" name="phrLogout" method="POST"  style="margin: 0px; padding: 0px;">
		                                                    <input type="hidden" name="forwardto" value="<%=request.getServletPath()%>?method=<%=request.getParameter("method")%>">
		                                                    <center><a href="javascript: document.forms['phrLogout'].submit()">Logout</a><div class="statusDiv" id="statusDiv"></div></center>
		                                                </form>
		                                            </div>
		                                        </td>
											<%
                                    	}
                                    	else
                                    	{
											%>
		                                        <td class="myoscarLoginElementNoAuth">
		                                            <div>
		                                                <form action="../../phr/Login.do" name="phrLogin" method="POST"  style="margin-bottom: 0px;">
		                                                    <logic:present name="phrUserLoginErrorMsg">
		                                                        <div class="phrLoginErrorMsg"><font color="red"><bean:write name="phrUserLoginErrorMsg"/>.</font>  
		                                                        <logic:present name="phrTechLoginErrorMsg">
		                                                            <a href="javascript:;" title="fade=[on] requireclick=[off] cssheader=[moreInfoBoxoverHeader] cssbody=[moreInfoBoxoverBody] singleclickstop=[on] header=[MyOSCAR Server Response:] body=[<bean:write name="phrTechLoginErrorMsg"/> </br>]">More Info</a></div>
		                                                        </logic:present>
		                                                    </logic:present>
		                                                    Status: <b>Not logged in</b><br/>
		                                                    <%=providerName%> password: <input type="password" id="phrPassword" name="phrPassword" style="font-size: 8px; width: 40px;"> <a href="javascript: document.forms['phrLogin'].submit()">Login</a>
		                                                    <br />
		                                                    Keep me logged in <input type="checkbox" checked="checked" name="saveMyOscarPassword" />
		                                                    <input type="hidden" name="forwardto" value="<%=request.getServletPath()%>?method=<%=request.getParameter("method")%>">
		                                                </form>
		                                            </div>
		                                        </td>
			                                <%
                                    	}
                                    %>
                                </tr>
                            </table><!--cell spacing=3-->
                            <%
                            	if (MyOscarUtils.isMyOscarEnabled((String) session.getAttribute("user")) && myOscarLoggedInInfo!=null && myOscarLoggedInInfo.isLoggedIn())
                            	{
                            		%>
                           				<button style="float:right" onclick="window.open('<%=request.getContextPath()%>/myoscar/myoscar_page_link_action.jsp?redirectPage=/v2/external_message_settings.jsp');">PHR Message Settings</button>
                           			<%
                           		}
                           	%>
                        </td>
                    </tr>
                </table><!--table width="100%">-->
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">
                &nbsp;
            </td>
        	<td>
        		<%
					int startIndex=0;
					try
					{
						startIndex=Integer.parseInt(request.getParameter("startIndex"));
					}
					catch (Exception e)
					{
						// okay to ignore, either no parameter or some one messing with url string.
					}
        		%>
				<a href="?method=<%=pageMethod%>&startIndex=<%=MyOscarMessagesHelper.getPreviousPageStartIndex(startIndex)%>" /><bean:message key="oscarMessenger.DisplayMessages.msgNewerMessage"/></a>
				<a href="?method=<%=pageMethod%>&startIndex=<%=MyOscarMessagesHelper.getNextPageStartIndex(startIndex)%>" /><bean:message key="oscarMessenger.DisplayMessages.msgOlderMessage"/></a>
        	</td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">
                &nbsp;
            </td>
            <td class="MainTableRightColumn">
                
                <%-- Sharing approval alerts -------------- --%>
                <%if (actionsPendingApproval != null) {
                    for (PHRAction actionPendingApproval: actionsPendingApproval) {
                        ProviderData senderProvider = new ProviderData(actionPendingApproval.getSenderOscar()); 
                        List idAndPermission = IndivoAPService.getProposalIdAndPermission(actionPendingApproval);
                        String demographicIndivoId = (String) idAndPermission.get(0);
                        String permission = (String) idAndPermission.get(1);
                        String permissionReadable = permission.substring(permission.lastIndexOf(':')+1, permission.length());
                                %>
                    <div class="sharingAlert">
                    <span style="float: left;">
                        Add sharing for <span style="font-weight: bold; color: #339a8a;"><%=demographicIndivoId%></span>.  
                        Share type: <span style="font-weight: bold; color: #d0722e;"><%=permissionReadable%></span> 
                        (Proposed by <span style="font-weight: bold; color: #339a8a;"><%= senderProvider.getFirst_name()%> <%=senderProvider.getLast_name()%></span>)
                    </span>
                        <span style="float: right;"><a href="../../phr/UserManagement.do?method=approveAction&actionId=<%=actionPendingApproval.getId()%>"><b>Approve</b></a> <a href="../../phr/UserManagement.do?method=denyAction&actionId=<%=actionPendingApproval.getId()%>"><b>Deny</b></a></span>
                    </div>
                    
                  <%} 
                }%>
                
                
                <table class="messageTable" border="0" width="99%" cellspacing="1">
                    <tr>
                        <th bgcolor="#DDDDFF" width="30">
                            &nbsp;
                        </th>
                        <th align="left" bgcolor="#DDDDFF">
                            <html-el:link action="/phr/PhrMessage.do?orderby=0&method=${pageMethod}" >
                                <bean:message key="oscarMessenger.DisplayMessages.msgStatus"/>
                            </html-el:link>
                        </th>
                        <th align="left" bgcolor="#DDDDFF">
                        	<bean:message key="oscarMessenger.DisplayMessages.msgFrom"/>
                        </th>
                        <th align="left" bgcolor="#DDDDFF">
                        	<bean:message key="oscarMessenger.DisplayMessages.msgTo"/>
                        </th>
                        <th align="left" bgcolor="#DDDDFF">
                            <bean:message key="oscarMessenger.DisplayMessages.msgSubject"/>
                        </th>
                        <th align="left" bgcolor="#DDDDFF">
                            <bean:message key="oscarMessenger.DisplayMessages.msgDate"/>
                        </th>
                        <th align="center" style="width: 30px;" bgcolor="#DDDDFF">
                                &nbsp;
                        </th>
                        <%if (pageMethod.equals("viewSentMessages")) {%>
                            <th align="center" style="width: 30px;" bgcolor="#DDDDFF">
                                &nbsp;
                            </th>
                        <%}%>
                    </tr>
                                
 <%-- Inbox -------------------------------------------------------------- --%>
	
				<%					
					if (myOscarLoggedInInfo!=null && myOscarLoggedInInfo.isLoggedIn())
					{
						List<MessageTransfer3> messages=null;
						
						if ("viewSentMessages".equals(pageMethod)) messages=MyOscarMessagesHelper.getSentMessages(session, startIndex);
						else if ("viewArchivedMessages".equals(pageMethod)) messages=MyOscarMessagesHelper.getReceivedMessages(session, false, startIndex);
						else messages=MyOscarMessagesHelper.getReceivedMessages(session, true, startIndex);
 
						for (MessageTransfer3 message : messages)
						{
							Long senderPersonId=message.getSenderPersonId();
							MinimalPersonTransfer2 minimalPersonSender=AccountManager.getMinimalPerson(myOscarLoggedInInfo, senderPersonId);

		               		StringBuilder displayName=new StringBuilder();
		               		if (minimalPersonSender.getLastName()!=null) displayName.append(minimalPersonSender.getLastName()).append(", ");
		               		if (minimalPersonSender.getFirstName()!=null) displayName.append(minimalPersonSender.getFirstName());
		               		displayName.append(" (");
		               		displayName.append(minimalPersonSender.getUserName());
		               		displayName.append(")");
							
							String senderString=displayName.toString();
							
							StringBuilder sb=new StringBuilder();
		               		for (Long recipientId : message.getRecipientPeopleIds())
		               		{
		               			MinimalPersonTransfer2 recipient=AccountManager.getMinimalPerson(myOscarLoggedInInfo, recipientId);
		               			sb.append(recipient.getLastName()+", "+recipient.getFirstName()+" ("+recipient.getUserName()+"); ");
		               		}
							String recipientString=sb.toString();
							if (recipientString.length()>32) recipientString=recipientString.substring(0, 32)+"...";
		               		
							String subject=MessageManager.getSubject(message);
							
							Message2RecipientPersonAttributesTransfer recipientAttributes=message.getOneSpecificRecipientAttributes();

							%>
		                        <tr class="<%=recipientAttributes!=null && recipientAttributes.getFirstViewDate()!=null?"normal":"new"%>">
		                            <td bgcolor="#EEEEFF" width="30">
		                            	<%
		                            		if (recipientAttributes!=null && recipientAttributes.getFirstViewDate()!=null)
		                            		{
		                            			%>
		                            				-->
		                            			<%
		                            		}
		                            	%>
		                            </td>
		                            <td bgcolor="#EEEEFF" width="75">
		                            	<%=recipientAttributes!=null && recipientAttributes.getFirstViewDate()!=null?"read":"new"%>
		                            </td>
		                            <td bgcolor="#EEEEFF">      
		                            	<%
		                                String demographicLink = "";
		                                String myOscarUserName=minimalPersonSender.getUserName();
		                                Demographic demographic=MyOscarUtils.getDemographicByMyOscarUserName(myOscarUserName);
		                                if (demographic!=null){
		                                		demographicLink = "&demographicNo="+demographic.getDemographicNo();
	                                	}
		                                %>
		                            	<a href="<%=request.getContextPath()%>/phr/PhrMessage.do?&method=read&comingfrom=viewMessages&messageId=<%=message.getId()%><%=demographicLink%>">    
		                            		 <%=StringEscapeUtils.escapeHtml(senderString)%>
		                            	</a>                      
		                               <%
		                                	if (demographic!=null)
		                                	{
				                                %>
		                                			</a> <a href="?<%=request.getQueryString()%>" onClick="gotoEchart3(<%=demographic.getDemographicNo()%>);" >E</a>
	                                			<%
		                                	}	
	                                	%>
		                            </td>
		                            <td bgcolor="#EEEEFF">                                
		                                <%
		                                	if (recipientAttributes!=null)
		                                	{
			                                	Long recipientPersonId=recipientAttributes.getRecipientPersonId();
			                                	MinimalPersonTransfer2 minimalPerson=AccountManager.getMinimalPerson(myOscarLoggedInInfo, recipientPersonId);
			                                	demographic=MyOscarUtils.getDemographicByMyOscarUserName(minimalPerson.getUserName());
			                                	if (demographic!=null)
			                                	{
					                                %>
					                               		<a href="?<%=request.getQueryString()%>" onClick="gotoEchart2(<%=demographic.getDemographicNo()%>,<%=message.getId()%>);" >
				                               		<%
		                                		}
		                                	}
			                            %>
		                                <%=StringEscapeUtils.escapeHtml(recipientString)%>
										<%
		                                	if (demographic!=null)
		                                	{
				                                %>
		                                			</a>
	                                			<%
		                                	}
	                                	%>
		                            </td>
		                            <td bgcolor="#EEEEFF">
		                                <a href="<%=request.getContextPath()%>/phr/PhrMessage.do?&method=read&comingfrom=viewMessages&messageId=<%=message.getId()%><%=demographicLink%>">
		                                   <%=StringEscapeUtils.escapeHtml(subject)%>
		                                </a>
		                            </td>
		                            <td bgcolor="#EEEEFF"> 
	                                   <%=DateUtils.formatDate(message.getSentDate(), request.getLocale())%>
										&nbsp;
	                                   <%=DateUtils.formatTime(message.getSentDate(), request.getLocale())%>
		                            </td>
		                            <td>
		                            	<% 
		                            		if (!"viewSentMessages".equals(pageMethod))
		                            		{
				                            	Message2RecipientPersonAttributesTransfer recipientAttributesTransfer= message.getOneSpecificRecipientAttributes();
				                            	if (recipientAttributesTransfer!=null)
				                            	{
					                            	%>
					                                <a href="<%=request.getContextPath()%>/phr/PhrMessage.do?prevDisplay=<%=pageMethod%>&method=flipActive&messageId=<%=message.getId()%>"  >
							                            <%
							                            	if (recipientAttributesTransfer.isActive())
							                            	{
							                            		%>
									                                   <bean:message key="oscarMessenger.DisplayMessages.formArchive"/>
							                            		<%
							                            	}
							                            	else
							                            	{
							                            		%>
									                                   <bean:message key="oscarMessenger.DisplayMessages.formUnarchive"/>
							                            		<%			                            		
							                            	}
						                            	%>
					                                </a>
					                              <%
				                            	}
		                            		}
				                        %>
		                            </td>
		                        </tr>						
							<%
						}
					}
				%>

                </table>
            </td>
        </tr>
        <tr>
            <script type="text/javascript" src="../../share/javascript/boxover.js"></script>
            <td class="MainTableBottomRowLeftColumn">
            </td>
            <td class="MainTableBottomRowRightColumn">
            </td>
        </tr>
        
    </table>
    </body>
</html>
