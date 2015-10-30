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
<%@page import="java.io.File"%>
<%@page import="org.oscarehr.myoscar.client.ws_manager.MessageManager"%>
<%@page import="oscar.log.LogConst"%>
<%@page import="oscar.log.LogAction"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.ConformanceTestHelper"%>
<%@page import="oscar.dms.EDocUtil"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="oscar.dms.actions.AddEditDocumentAction"%>
<%@page import="org.oscarehr.util.DateUtils"%>
<%@page import="org.joda.time.format.ISODateTimeFormat"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.dms.EDoc"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="org.oscarehr.util.XmlUtils"%>
<%@page import="org.oscarehr.myoscar_server.ws.Message2DataTransfer"%>
<%@page import="org.oscarehr.phr.web.MyOscarMessagesHelper"%>
<%@page import="org.oscarehr.myoscar_server.ws.MessageTransfer3"%>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(session);
   		 
	Long demographicNo=new Long(request.getParameter("demographicNo"));
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
	String description = "Attachment : "+subject;
	
	String date=DateUtils.getIsoDate(messageTransfer.getSentDate());
	date=date.replaceAll("-", "/");

	EDoc newDoc = new EDoc(description, "others", filename, "", loggedInInfo.getLoggedInProviderNo(), "", "", 'A', date, "", "", "demographic", demographicNo.toString());

	// new file name with date attached
	String fileName2 = newDoc.getFileName();
	
	// save local file
	ByteArrayInputStream bais=new ByteArrayInputStream(dataBytes);
	File file=AddEditDocumentAction.writeLocalFile(bais, fileName2);
	

	newDoc.setContentType(mimeType);
    if ("application/pdf".equals(mimeType)) {
        int numberOfPages = AddEditDocumentAction.countNumOfPages(fileName2);
        newDoc.setNumberOfPages(numberOfPages);                        
    }
				 	
			
	String doc_no = EDocUtil.addDocumentSQL(newDoc);
	if(ConformanceTestHelper.enableConformanceOnlyTestFeatures){
		AddEditDocumentAction.storeDocumentInDatabase(file, Integer.parseInt(doc_no));
	}
	LogAction.addLog(loggedInInfo.getLoggedInProviderNo(), LogConst.ADD, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());
%>

<html>
	<head></head>
	<body style="text-align:center">
		<h2>Attachment saved to echart documents</h2>
		<br />
		<input type="button" value="ok" onclick="location.href='<%=request.getContextPath()%>/phr/PhrMessage.do?&method=read&comingfrom=viewMessages&messageId=<%=messageId%>&demographicNo=<%=demographicNo%>'" />
	</body>
</html>
