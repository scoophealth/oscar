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
<%@page import="org.oscarehr.myoscar.client.ws_manager.MessageManager"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="java.util.Calendar"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.oscarehr.util.XmlUtils"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="org.oscarehr.myoscar_server.ws.MessageTransfer3"%>
<%@page import="org.oscarehr.myoscar_server.ws.Message2DataTransfer"%>
<%@page import="org.oscarehr.phr.web.MyOscarMessagesHelper"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.phr.web.PHRMessageAction"%>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(session);
   		 
	Integer demographicNo=new Integer(request.getParameter("demographicNo"));
	Long messageId=new Long(request.getParameter("messageId"));

	MessageTransfer3 messageTransfer=MyOscarMessagesHelper.readMessage(session, messageId);

	Message2DataTransfer messageDataTransfer=MyOscarMessagesHelper.getFileAttachment(messageTransfer);
	String filename=null;
	String mimeType=null;
	byte[] dataBytes=null;
	if (messageDataTransfer!=null)
	{
		// filename / mimeType / bytes
		
		Document doc=XmlUtils.toDocument(messageDataTransfer.getContents());
		Node rootNode=doc.getFirstChild();
		filename=XmlUtils.getChildNodeTextContents(rootNode, "filename");
		mimeType=XmlUtils.getChildNodeTextContents(rootNode, "mimeType");
		String tempString=XmlUtils.getChildNodeTextContents(rootNode, "bytes");
		dataBytes=Base64.decodeBase64(tempString);
	}

	//--------------------------
	
	String subject=MessageManager.getSubject(messageTransfer);	
	Calendar date=messageTransfer.getSentDate();

	PHRMessageAction.saveAttachmentToEchartDocuments(loggedInInfo, demographicNo, subject, date, filename, mimeType, dataBytes);
%>

<html>
	<head></head>
	<body style="text-align:center">
		<h2>Attachment saved to echart documents</h2>
		<br />
		<input type="button" value="ok" onclick="location.href='<%=request.getContextPath()%>/phr/PhrMessage.do?&method=read&comingfrom=viewMessages&messageId=<%=messageId%>&demographicNo=<%=demographicNo%>'" />
	</body>
</html>
