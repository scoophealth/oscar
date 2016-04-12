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

<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.oscarehr.util.XmlUtils"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="org.oscarehr.myoscar_server.ws.Message2DataTransfer"%>
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

<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="org.oscarehr.myoscar.client.ws_manager.MessageManager"%>
<%@page import="org.oscarehr.myoscar.client.ws_manager.AccountManager"%>
<%@page import="org.oscarehr.myoscar_server.ws.MinimalPersonTransfer2"%>
<%@page import="org.oscarehr.myoscar_server.ws.MessageTransfer3"%>
<%@page import="oscar.util.DateUtils"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils,java.net.URLEncoder"%>
<%@page import="org.oscarehr.phr.web.MyOscarMessagesHelper"%>
<%@page import="oscar.oscarDemographic.data.*, java.util.Enumeration" %>
<%@page import="oscar.util.UtilDateUtilities,java.util.*" %>
<%@page import="org.oscarehr.phr.util.MyOscarUtils,org.oscarehr.common.model.Demographic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html-el" prefix="html-el" %>

<%
	Long messageId=new Long(request.getParameter("messageId"));
	String demographicNo = request.getParameter("demographicNo");
	MessageTransfer3 messageTransfer=MyOscarMessagesHelper.readMessage(session, messageId);
	String subject=MessageManager.getSubject(messageTransfer);
	String messageBody=MessageManager.getMessageBody(messageTransfer);
	
 	MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);
	Long senderPersonId=messageTransfer.getSenderPersonId();
	MinimalPersonTransfer2 minimalPersonSender=AccountManager.getMinimalPerson(myOscarLoggedInInfo, senderPersonId);
	
	Message2DataTransfer messageDataTransfer=MyOscarMessagesHelper.getFileAttachment(messageTransfer);
	String filename=null;
	String mimeType=null;
	int fileSize=0;
	if (messageDataTransfer!=null)
	{
		// filename / mimeType / bytes
		
		Document doc=XmlUtils.toDocument(messageDataTransfer.getContents());
		Node rootNode=doc.getFirstChild();
		filename=XmlUtils.getChildNodeTextContents(rootNode, "filename");
		mimeType=XmlUtils.getChildNodeTextContents(rootNode, "mimeType");
		String tempString=XmlUtils.getChildNodeTextContents(rootNode, "bytes");
		byte[] tempBytes=Base64.decodeBase64(tempString);
		fileSize=tempBytes.length;
	}

	String myOscarUserName=minimalPersonSender.getUserName();
	Demographic demographic=MyOscarUtils.getDemographicByMyOscarUserName(myOscarUserName);
%>

<html:html locale="true">
<head>
<link rel="stylesheet" type="text/css" href="../oscarMessenger/encounterStyles.css" media="screen">
<link rel="stylesheet" type="text/css" href="../oscarMessenger/printable.css" media="print">    

<title>
	View Message
</title>

<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script type="text/javascript">
function BackToOscar()
{
       window.close();
}

function gotoEchart3(demoNo) {
    var url = '<%=request.getContextPath()%>/oscarEncounter/IncomingEncounter.do?demographicNo='+ demoNo+'&reason=&appointmentDate=<%=UtilDateUtilities.DateToString(new Date())%>';
    openedWindow = popup(755,1048,url,'apptProvider');
}

function gotoMSG(demoNo){
	var url = '<%=request.getContextPath()%>/oscarMessenger/SendDemoMessage.do?demographic_no='+demoNo+'&subject=<%=URLEncoder.encode("PHR:"+subject,"UTF-8")%>&message=<%=URLEncoder.encode("\n\n\n---Message From PHR----\n"+messageBody,"UTF-8")%>';   
	openedWindow = popup(755,1048,url,'msg');
}


</script>

</head>

<body class="BodyStyle" vlink="#0000FF" >

    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                View Message
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                            <bean:message key="oscarMessenger.ViewMessage.msgViewMessage"/>
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
            <td class="MainTableRightColumn Printable">
                <table>
                    <tr>
                        <td>
                            <table  cellspacing=3>
                                <tr>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3 ><tr><td class="messengerButtonsA">
                                            <a href="javascript:window.print()" class="messengerButtons"><bean:message key="oscarMessenger.ViewMessage.btnPrint"/></a>
                                        </td></tr></table>
                                    </td>   
                                  
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3 ><tr><td class="messengerButtonsA">
                                           <a href="#" class="messengerButtons" onclick="history.go(-1)">
                                             Back
                                           </a>
                                        </td></tr></table>
                                    </td>                                    
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3 ><tr><td class="messengerButtonsA">
                                            <a href="javascript:BackToOscar()" class="messengerButtons">Exit</a>
                                        </td></tr></table>
                                    </td>                                    
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td class="Printable">
                            <table border="0" cellspacing="1" valign="top">
                                <tr>
                                    <td class="Printable" bgcolor="#DDDDFF" align="right">
                                    <bean:message key="oscarMessenger.ViewMessage.msgFrom"/>:
                                    </td>
                                    <td class="Printable" bgcolor="#CCCCFF">
                                    	<%
                                    		StringBuilder displayName=new StringBuilder();
                                    		if (minimalPersonSender.getLastName()!=null) displayName.append(minimalPersonSender.getLastName()).append(", ");
                                    		if (minimalPersonSender.getFirstName()!=null) displayName.append(minimalPersonSender.getFirstName());
                                    		displayName.append(" (");
                                    		displayName.append(minimalPersonSender.getUserName());
                                    		displayName.append(")");
                                    	%>
                                    	<%=StringEscapeUtils.escapeHtml(displayName.toString())%>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="Printable" bgcolor="#DDDDFF" align="right">
                                    <bean:message key="oscarMessenger.ViewMessage.msgTo"/>:
                                    </td>
                                    <td class="Printable" bgcolor="#BFBFFF">
                                    	<%
                                    		for (Long recipientId : messageTransfer.getRecipientPeopleIds())
                                    		{
                                    			MinimalPersonTransfer2 recipient=AccountManager.getMinimalPerson(myOscarLoggedInInfo, recipientId);
                                    			%>
			                                    	<%=StringEscapeUtils.escapeHtml(recipient.getLastName()+", "+recipient.getFirstName()+" ("+recipient.getUserName()+"); ")%>
                                    			<%
                                    		}
                                    	%>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="Printable" bgcolor="#DDDDFF" align="right">
                                        <bean:message key="oscarMessenger.ViewMessage.msgSubject"/>:
                                    </td>
                                    <td class="Printable" bgcolor="#BBBBFF">
                                    	<%=StringEscapeUtils.escapeHtml(subject)%>
                                    </td>
                                </tr>
                                <tr>
                                  <td class="Printable" bgcolor="#DDDDFF" align="right">
                                      <bean:message key="oscarMessenger.ViewMessage.msgDate"/>:
                                  </td>
                                  <td  class="Printable" bgcolor="#B8B8FF">
                                   	<%=StringEscapeUtils.escapeHtml(DateUtils.formatDateTime(messageTransfer.getSentDate(), request.getLocale()))%>
                                  </td>
                                </tr>
                                <tr>
                                    
                                    <td bgcolor="#EEEEFF" ></td>
                                    <td bgcolor="#EEEEFF" >
                                        <textarea name="msgBody" wrap="hard" readonly="true" rows="18" cols="60" ><%=StringEscapeUtils.escapeHtml(messageBody)%></textarea><br>
                                        
                                        <%
                                        	if (filename!=null)
                                        	{
                                        		%>
			                                        <div style="padding-top:0.5em;padding-bottom:0.5em">
				                                        <%=StringEscapeUtils.escapeHtml(filename)%>
				                                    	&nbsp;
				                                    	(<%=StringEscapeUtils.escapeHtml(mimeType)%> <%=fileSize%> bytes)
				                                    	&nbsp;
				                                    	<a href="msg/attachment_retriever.jsp?messageId=<%=messageId%>&amp;download=false">open</a>
				                                    	&nbsp;
				                                    	<a href="msg/attachment_retriever.jsp?messageId=<%=messageId%>&amp;download=true" download="<%=StringEscapeUtils.escapeHtml(filename)%>">download</a>
				                                    	<%
				                                    		if (demographic !=null)
				                                    		{
				                                    			%>
							                                    	&nbsp;
							                                    	<a href="msg/save_to_echart_documents_action.jsp?messageId=<%=messageId%>&demographicNo=<%=demographicNo%>">save to echart documents</a>
				                                    			<%
				                                    		}
				                                    	%> 
			                                        </div>
                                        		<%
                                        	}
                                        %>
                                        
                                        <input class="ControlPushButton" type="button" value="<bean:message key="oscarMessenger.ViewMessage.btnReply"/>" onclick="window.location.href='<%=request.getContextPath()%>/phr/msg/CreatePHRMessage.jsp?replyToMessageId=<%=messageId%>&amp;demographicNo=<%=demographicNo%>'"/>
                                        <input class="ControlPushButton" type="button" value="<bean:message key="oscarMessenger.ViewMessage.btnReplyAll"/>" onclick="window.location.href='<%=request.getContextPath()%>/phr/msg/CreatePHRMessage.jsp?replyAll=true&amp;replyToMessageId=<%=messageId%>&amp;demographicNo=<%=demographicNo%>'"/>
 
                                    	<input 
                                    	<%if (demographic == null){%>
		                                   disabled="disabled"
		                                   title="<bean:message key="global.no.phr.account.registered"/>"
		                                <%}%> 
                                    	class="ControlPushButton" type="button" onclick="gotoEchart3('<%=demographicNo%>');" value="<bean:message key="oscarMessenger.CreateMessage.btnOpenEchart"/>" >
                                    	
                                    	<input 
                                    	<%if (demographic == null){%>
		                                   disabled="disabled"
		                                   title="<bean:message key="global.no.phr.account.registered"/>"
		                                <%}%> 
                                    	class="ControlPushButton" type="button" onclick="gotoMSG('<%=demographicNo%>');" value="<bean:message key="oscarMessenger.CreateMessage.btnOpenInOscarMsg"/>" >
                                    	
                                    	
                                    </td>
                                </tr>
                                <tr>
                                    <td bgcolor="#EEEEFF" ></td>
                                    <td bgcolor="#EEEEFF" >&nbsp;</td>
                                </tr>
                            </table>
                        </td>                        
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">

            </td>
            <td class="MainTableBottomRowRightColumn">

            </td>
        </tr>
    </table>
</body>

</html:html>
