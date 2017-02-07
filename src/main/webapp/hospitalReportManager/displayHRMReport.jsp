<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.apache.commons.lang.StringUtils,oscar.log.*"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page language="java" contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="java.util.LinkedList, org.oscarehr.hospitalReportManager.*, org.oscarehr.hospitalReportManager.model.*, java.util.List, org.oscarehr.util.SpringUtils, org.oscarehr.PMmodule.dao.ProviderDao, java.util.Date" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
HRMDocument hrmDocument = (HRMDocument) request.getAttribute("hrmDocument");
HRMReport hrmReport = (HRMReport) request.getAttribute("hrmReport");
Integer hrmReportId = (Integer) request.getAttribute("hrmReportId");

HRMDocumentToDemographic demographicLink = (HRMDocumentToDemographic) request.getAttribute("demographicLink");
List<HRMDocumentToProvider> providerLinkList = (List<HRMDocumentToProvider>) request.getAttribute("providerLinkList");

LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");

if(demographicLink != null){
    LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_HRM, ""+hrmReportId, request.getRemoteAddr(),""+demographicLink.getDemographicNo());
}else{           
    LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_HRM, ""+hrmReportId, request.getRemoteAddr());
}

%>


<html>
<head>
<title>HRM Report</title>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../js/jquery-ui-1.8.18.custom.min.js"></script>
<script language="javascript" type="text/javascript" src="../share/javascript/Oscar.js" ></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>
<script type="text/javascript" src="../share/javascript/effects.js"></script>
<script type="text/javascript" src="../share/javascript/controls.js"></script>

<script type="text/javascript" src="../share/yui/js/yahoo-dom-event.js"></script>
<script type="text/javascript" src="../share/yui/js/connection-min.js"></script>
<script type="text/javascript" src="../share/yui/js/animation-min.js"></script>
<script type="text/javascript" src="../share/yui/js/datasource-min.js"></script>
<script type="text/javascript" src="../share/yui/js/autocomplete-min.js"></script>
<script type="text/javascript" src="../js/demographicProviderAutocomplete.js"></script>


<link rel="stylesheet" href="../js/jquery_css/smoothness/jquery-ui-1.7.3.custom.css" type="text/css" />  
<link rel="stylesheet" type="text/css" href="../share/yui/css/fonts-min.css"/>
<link rel="stylesheet" type="text/css" href="../share/yui/css/autocomplete.css"/>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/demographicProviderAutocomplete.css"  />


<style type="text/css">
#hrmReportContent {
	position: relative;
	float: left;
	padding: 25px;
	margin: 10px;
	border: 1px solid black;
	width: 550px;
}

#infoBox {
	position: relative;
	float: left;
	padding: 25px;
	margin: 10px;
	border: 1px solid black;
	width: 300px;
}

#infoBox th {
	text-align: right;
	vertical-align: top;
}

#hrmHeader {
    display: none;
}

#hrmNotice {
	border-bottom: 1px solid black;
	padding-bottom: 15px;
	margin-bottom: 15px;
	font-style: italic;
}

.documentLink_statusC {
	background-color: red;
}

#commentBox {
	clear: both;
	border: 1px solid black;
	margin: 20px;
}

.documentComment {
	border: 1px solid black;
	margin: 10px;
}


#metadataBox th {
	text-align: right;
}

@media print {
	#infoBox {
		display: none;
	}
	.boxButton {
	  display: none;
    }
	#hrmHeader {
	  display: block;
	}
}
.pdfviewer {
	width: 100%;
	height: 800px;
}
</style>

<script type="text/javascript">
function makeIndependent(reportId) {
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=makeIndependent&reportId=" + reportId,
		success: function(data) {
			
		}
	});
}

function addDemoToHrm(reportId) {
	var demographicNo = $("demofind" + reportId + "hrm").value;
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=assignDemographic&reportId=" + reportId + "&demographicNo=" + demographicNo,
		success: function(data) {
			if (data != null) {
				$("demostatus" + reportId).innerHTML = data;
				toggleButtonBar(true,reportId);
			}
		}
	});
}

function toggleButtonBar(show, reportId) {
	jQuery("#msgBtn_"+reportId).prop('disabled',!show);
	jQuery("#mainTickler_"+reportId).prop('disabled',!show);
	jQuery("#mainEchart_"+reportId).prop('disabled',!show);
	jQuery("#mainMaster_"+reportId).prop('disabled',!show);
	jQuery("#mainApptHistory_"+reportId).prop('disabled',!show);
	
}

function removeDemoFromHrm(reportId) {
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=removeDemographic&reportId=" + reportId,
		success: function(data) {
			if (data != null) {
				$("demostatus" + reportId).innerHTML = data;
				toggleButtonBar(false,reportId);
			}
		}
	});
}

function addProvToHrm(reportId, providerNo) {
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=assignProvider&reportId=" + reportId + "&providerNo=" + providerNo,
		success: function(data) {
			if (data != null)
				$("provstatus" + reportId).innerHTML = data;
		}
	});
}

function removeProvFromHrm(mappingId, reportId) {
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=removeProvider&providerMappingId=" + mappingId,
		success: function(data) {
			if (data != null)
				$("provstatus" + reportId).innerHTML = data;
		}
	});
}

function makeActiveSubClass(reportId, subClassId) {
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=makeActiveSubClass&reportId=" + reportId + "&subClassId=" + subClassId,
		success: function(data) {
			if (data != null)
				$("subclassstatus" + reportId).innerHTML = data;
		}
	});
	
	window.location.reload();
}

function addComment(reportId) {
	var comment = jQuery("#commentField_" + reportId + "_hrm").val();
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=addComment&reportId=" + reportId + "&comment=" + comment,
		success: function(data) {
			if (data != null)
				$("commentstatus" + reportId).innerHTML = data;
		}
	});
}

function deleteComment(commentId, reportId) {
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=deleteComment&commentId=" + commentId,
		success: function(data) {
			if (data != null)
				$("commentstatus" + reportId).innerHTML = data;
		}
	});
}


function doSignOff(reportId, isSign) {
	var data;
	if (isSign)
		data = "method=signOff&signedOff=1&reportId=" + reportId;
	else
		data = "method=signOff&signedOff=0&reportId=" + reportId;
	
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: data,
		success: function(data) {
			window.location.reload();
		}
	});
}

function signOffHrm(reportId) {
	
	doSignOff(reportId, true);	
}

function revokeSignOffHrm(reportId) {
	doSignOff(reportId, false);
}

function setDescription(reportId) {
	var comment = jQuery("#descriptionField_" + reportId + "_hrm").val();
	jQuery.ajax({
		type: "POST",
		url: "<%=request.getContextPath() %>/hospitalReportManager/Modify.do",
		data: "method=setDescription&reportId=" + reportId + "&description=" + comment,
		success: function(data) {
			if (data != null)
				$("descriptionstatus" + reportId).innerHTML = data;
		}
	});
}

function popupEchart(height, width, url, windowName, docId, d, reason) {
	url += "&reason=" + encodeURIComponent(encodeURIComponent(reason)) + "&demographicNo=";
	return popupPatient(height, width, url, windowName, docId, d);
}

function popupPatient(height, width, url, windowName, docId, d) {
	  urlNew = url + d;	
	  return popup2(height, width, 0, 0, urlNew, windowName);
}

function popupPatientTickler(height, width, url, windowName,docId,d,n) {
	urlNew = url + "method=edit&tickler.demographic_webName=" + n + "&tickler.demographicNo=" +  d + "&docType=HRM&docId="+docId;
	return popup2(height, width, 0, 0, urlNew, windowName);
}

</script>
</head>
<body>

<% if (hrmReport==null) { %>
        <h1>HRM report not found! Please check the file location.</h1>
<%  return;
   } %>
   
<%
String btnDisabled = "disabled";
String demographicNo = "";
if(demographicLink != null) {
	btnDisabled="";
	demographicNo = demographicLink.getDemographicNo();
}
String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());	

%>
<div >
<input type="button" id="msgBtn_<%=hrmReportId%>" value="Msg" onclick="popupPatient(700,960,'<%= request.getContextPath() %>/oscarMessenger/SendDemoMessage.do?demographic_no=','msg', '<%=hrmReportId%>','<%=demographicNo %>')" <%=btnDisabled %>/>
<!--input type="button" id="ticklerBtn_<%=hrmReportId%>" value="Tickler" onclick="handleDocSave('<%=hrmReportId%>','addTickler')"/-->
<input type="button" id="mainTickler_<%=hrmReportId%>" value="Tickler" onClick="popupPatientTickler(710, 1024,'<%= request.getContextPath() %>/Tickler.do?', 'Tickler','<%=hrmReportId%>','<%=demographicNo %>')" <%=btnDisabled %>>
<input type="button" id="mainEchart_<%=hrmReportId%>" value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> " onClick="popupEchart(710, 1024,'<%= request.getContextPath() %>/oscarEncounter/IncomingEncounter.do?curDate=<%=currentDate%>&appointmentNo=&appointmentDate=&startTime=&status=', 'encounter', '<%=hrmReportId%>','<%=demographicNo %>', '<bean:message key="oscarMDS.segmentDisplay.labResults"/>')" <%=btnDisabled %>>
<input type="button" id="mainMaster_<%=hrmReportId%>" value=" <bean:message key="oscarMDS.segmentDisplay.btnMaster"/>" onClick="popupPatient(710,1024,'<%= request.getContextPath() %>/demographic/demographiccontrol.jsp?displaymode=edit&dboperation=search_detail&demographic_no=','master','<%=hrmReportId%>','<%=demographicNo %>')" <%=btnDisabled %>>
<input type="button" id="mainApptHistory_<%=hrmReportId%>" value=" <bean:message key="oscarMDS.segmentDisplay.btnApptHist"/>" onClick="popupPatient(710,1024,'<%= request.getContextPath() %>/demographic/demographiccontrol.jsp?orderby=appttime&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=25&demographic_no=','ApptHist','<%=hrmReportId%>','<%=demographicNo %>')" <%=btnDisabled %>>
</div>
                            
<div id="hrmReportContent">
	<div id="hrmHeader"><b>Demographic Info:</b><br />
			<%=hrmReport.getLegalName() %> <br />
			<%=hrmReport.getHCN() %> &nbsp; <%=hrmReport.getHCNVersion() %> &nbsp; <%=hrmReport.getGender() %><br />
	       <b>DOB:</b><%=hrmReport.getDateOfBirthAsString() %>
	</div>
	<br />
	<div id="hrmNotice">
	This report was received from the Hospital Report Manager (HRM) at <%=(String) request.getAttribute("hrmReportTime") %>.
	<% if (request.getAttribute("hrmDuplicateNum") != null && ((Integer) request.getAttribute("hrmDuplicateNum")) > 0) { %><br /><i>OSCAR has received <%=request.getAttribute("hrmDuplicateNum") %> duplicates of this report.</i><% } %>
	<%
	List<HRMDocument> allDocumentsWithRelationship = (List<HRMDocument>) request.getAttribute("allDocumentsWithRelationship");
	if (allDocumentsWithRelationship != null && allDocumentsWithRelationship.size() >= 1) {
	%>
		OSCAR has detected that this is similar to the following reports: 
		<% 
		List<Integer> seenBefore = new LinkedList<Integer>();
		for (HRMDocument relationshipDocument : allDocumentsWithRelationship) { 
			if (!seenBefore.contains(relationshipDocument.getId().intValue())) { %>
			<span class="documentLink_status<%=relationshipDocument.getReportStatus() %>" title="<%=relationshipDocument.getReportDate().toString() %>">
			<% if (relationshipDocument.getId().intValue() != hrmReportId.intValue()) { %><a href="<%=request.getContextPath() %>/hospitalReportManager/Display.do?id=<%=relationshipDocument.getId() %>"><% } %>[<%=relationshipDocument.getId() %>]<% if (relationshipDocument.getId().intValue() != hrmReportId.intValue()) { %></a><% } %>
			</span>&nbsp;&nbsp;
		<% 	seenBefore.add(relationshipDocument.getId().intValue());
			}
		} %>
		 <div class="boxButton">
		   <input type="button" onClick="makeIndependent('<%=hrmReportId %>')" value="Make Independent" />
		 </div>  
	<% } %>
	</div>

<%
	if(hrmReport.isBinary()) {
		String reportFileData = hrmReport.getFileData();
		String noMessageIdFileData = reportFileData.replaceAll("<MessageUniqueID>.*?</MessageUniqueID>", "<MessageUniqueID></MessageUniqueID>");
		String noMessageIdHash = org.apache.commons.codec.digest.DigestUtils.md5Hex(noMessageIdFileData);
		
		if(hrmReport.getFileExtension() != null && (".gif".equals(hrmReport.getFileExtension()) || ".jpg".equals(hrmReport.getFileExtension()) || ".png".equals(hrmReport.getFileExtension()))) {
			%><img src="<%=request.getContextPath() %>/hospitalReportManager/HRMDownloadFile.do?hash=<%=noMessageIdHash%>"/><br/><%	
		}
		 String downFileName = (hrmReport.getLegalLastName() + "-" + hrmReport.getLegalFirstName() + "-" +  hrmReport.getFirstReportClass() + hrmReport.getFileExtension()).replaceAll("\\s", "_");
		%>
		<a href="<%=request.getContextPath() %>/hospitalReportManager/HRMDownloadFile.do?hash=<%=noMessageIdHash%>"><%= downFileName%></a>&nbsp;&nbsp;
		
		<br/>
		<%
		if(hrmReport.getFileExtension() != null && (".gif".equals(hrmReport.getFileExtension()) || ".jpg".equals(hrmReport.getFileExtension()) || ".png".equals(hrmReport.getFileExtension()))) {
			%>
		<span>(Please use the link above to download the attachement.)</span>
		<%
		}
		
		else {
		
		if (hrmReport.getFileExtension() != null && ".pdf".equals(hrmReport.getFileExtension())) {
				// save pdf attachment and get path
				HRMUtil.storeAttachment(noMessageIdHash);
				String pdfFilePath = noMessageIdHash + "-" + downFileName;
 		%>
		<iframe src="<%=request.getContextPath()%>/share/pdfjs/viewer.jsp?file=<%=noMessageIdHash %>" class="pdfviewer"></iframe>
		<%} else {%>
 		<span style="color:red">(This report contains an attachment which cannot be viewed in your browser. Please use the link above to view/download the content contained within.)</span>
		<%}
		}
		
		
	} else {

%>
	<%=hrmReport.getFirstReportTextContent().replaceAll("\n", "<br />") %>
	
	<% } %>
	
	<%
	String confidentialityStatement = (String) request.getAttribute("confidentialityStatement");
	if (confidentialityStatement != null && confidentialityStatement.trim().length() > 0) {
	%>
	<hr />
	<em><strong>Provider Confidentiality Statement</strong><br /><%=confidentialityStatement %></em>
	<% } %>
</div>

<div id="infoBox">
	<table>
		<tr>
			<th>Report Date</th>
			<td><%=(hrmReport.getFirstReportEventTime() != null ? hrmReport.getFirstReportEventTime().getTime().toString() : 
					((hrmReport.getFirstAccompanyingSubClassDateTime() != null ? hrmReport.getFirstAccompanyingSubClassDateTime().getTime().toString() : ""))) %></td>
		</tr>
		<tr>
			<th>Demographic Info</th>
			<td>
				<%=hrmReport.getLegalName() %><br />
				<% try { %>
					<%=hrmReport.getAddressLine1() %><br />
					<%=hrmReport.getAddressLine2() != null ? hrmReport.getAddressLine2() : "" %><br />
					<%=hrmReport.getAddressCity() %>
				<% } catch(Exception e) { %>
					NO ADDRESS IN RECORD<br>
				<% } %>
			</td>
		</tr>
		<tr>
			<th>Linked with Demographic</th>
			<td>
				<div id="demostatus<%=hrmReportId %>"></div>
				<% if (demographicLink != null) { %>
					<oscar:nameage demographicNo="<%=demographicLink.getDemographicNo() %>" /> <a href="#" onclick="removeDemoFromHrm('<%=hrmReportId %>')">(remove)</a>
				<% } else { %>
					<i>Not currently linked</i><br />
					<input type="hidden" id="demofind<%=hrmReportId %>hrm" value="" />
					<input type="hidden" id="routetodemo<%=hrmReportId %>hrm" value="" />
					<input type="text" id="autocompletedemo<%=hrmReportId %>hrm" onchange="checkSave('<%=hrmReportId%>hrm')" name="demographicKeyword" />
					<div id="autocomplete_choices<%=hrmReportId%>hrm" class="autocomplete"></div>
                                            
				<% } %>
			</td>
		</tr>
		<tr>
			<th>Assigned Providers</th>
			<td>
				<div id="provstatus<%=hrmReportId %>"></div>
				<% if (providerLinkList != null || providerLinkList.size() >= 1) {
					for (HRMDocumentToProvider p : providerLinkList) { 
						if (!p.getProviderNo().equalsIgnoreCase("-1")) { %>
						<%=providerDao.getProviderName(p.getProviderNo())%> <%=p.getSignedOff() !=null && p.getSignedOff()  == 1 ? "<abbr title='" + p.getSignedOffTimestamp() + "'>(Signed-Off)</abbr>" : "" %> <a href="#" onclick="removeProvFromHrm('<%=p.getId() %>', '<%=hrmReportId %>')">(remove)</a><br />
				<%		}  
					}
				} else { %>
					<i>No providers currently assigned</i><br />
				<% } %>
				<% if (hrmDocument.getUnmatchedProviders() != null && hrmDocument.getUnmatchedProviders().trim().length() >= 1) {
					String[] unmatchedProviders = hrmDocument.getUnmatchedProviders().substring(1).split("\\|");
					for (String unmatchedProvider : unmatchedProviders) { %>
						<i><abbr title="From the HRM document"><%=unmatchedProvider %></abbr></i><br />
					<% }
				} %>
				<div id="providerList<%=hrmReportId %>hrm"></div>
				<input type="hidden" name="provi" id="provfind<%=hrmReportId%>hrm" />
                <input type="text" id="autocompleteprov<%=hrmReportId%>hrm" name="demographicKeyword"/>
                <div id="autocomplete_choicesprov<%=hrmReportId%>hrm" class="autocomplete"></div>
			</td>
		</tr>
		<tr>
			<td colspan=2><hr /></td>
		</tr>
		<tr>
			<th>Report Class</th>
			<td><%=hrmReport.getFirstReportClass() %></td>
		</tr>
		<% if (hrmReport.getFirstReportClass().equalsIgnoreCase("Diagnostic Imaging Report") || hrmReport.getFirstReportClass().equalsIgnoreCase("Cardio Respiratory Report")) { %>
		<tr>
			<th>Accompanying Subclass</th>
			<td>
				<%
				List<List<Object>> subClassListFromReport = hrmReport.getAccompanyingSubclassList();
				List<HRMDocumentSubClass> subClassListFromDb = (List<HRMDocumentSubClass>) request.getAttribute("subClassList");
				
				if (subClassListFromReport.size() > 0) {
				%>
				<i>From the Report</i><br />
					<% for (List<Object> subClass : subClassListFromReport) { %>
						<abbr title="Type: <%=(String) subClass.get(0) %>; Date of Observation: <%=((Date) subClass.get(3)).toString() %>">(<%=(String) subClass.get(1) %>) <%=(String) subClass.get(2) %></abbr><br />
					<% }
				} %><br />
				<%
				if (subClassListFromDb != null && subClassListFromDb.size() > 0) { %>
				<i>Stored in Database</i><br />
					<div id="subclassstatus<%=hrmReportId %>"></div>
					<% for (HRMDocumentSubClass subClass : subClassListFromDb) { %>
						<abbr title="Type: <%=subClass.getSubClass() %>; Date of Observation: <%=subClass.getSubClassDateTime().toString() %>">(<%=subClass.getSubClassMnemonic() %>) <%=subClass.getSubClassDescription() %></abbr>
						<% if (!subClass.isActive()) { %> (<a href="#" onclick="makeActiveSubClass('<%=hrmReportId %>', '<%=subClass.getId() %>')">make active</a>)<% } %><br />
					<% }
				} %>
			</td>
		</tr>
		<% } else { %>
		<tr>
			<th>Subclass</th>
			<td>
				<%
				String[] subClassFromReport = hrmReport.getFirstReportSubClass().split("\\^");
				if (subClassFromReport.length == 2) {
				%>
				<abbr title="Subclass: <%=subClassFromReport[0] %>"><%=subClassFromReport[1] %></abbr>
				<% } %>
			</td>
		</tr>
		<% } %>
		<tr>
			<th>Categorization</th>
			<td>
				<%
					HRMCategory category = (HRMCategory) request.getAttribute("category");
					if (category != null){
				%>
				<%=StringEscapeUtils.escapeHtml(category.getCategoryName())%>
				<%  }%> 
			</td>
		</tr>
		<tr>
			<td colspan=2>
				<input type="button" value="Print" onClick="window.print()" />
				<input type="button" style="display: none;" value="Save" id="save<%=hrmReportId %>hrm" />
				<% 
				HRMDocumentToProvider hrmDocumentToProvider = HRMDisplayReportAction.getHRMDocumentFromProvider(loggedInInfo.getLoggedInProviderNo(), hrmReportId);
				if (hrmDocumentToProvider != null && hrmDocumentToProvider.getSignedOff() != null && hrmDocumentToProvider.getSignedOff() == 1) {
				%>
				<input type="button" id="signoff<%=hrmReportId %>" value="Revoke Sign-Off" onClick="revokeSignOffHrm('<%=hrmReportId %>')" />
				<%
				} else { 
				%>
				<input type="button" id="signoff<%=hrmReportId %>" value="Sign-Off" onClick="signOffHrm('<%=hrmReportId %>')" />
				<%
				} 
				%>
			</td>
		</tr>
			
	</table>
</div>

<div id="commentBox">
Set description to this report:<br />
<input type="text" id="descriptionField_<%=hrmReportId %>_hrm" size="100" value="<%=StringEscapeUtils.escapeHtml(hrmDocument.getDescription())%>"/><br />

 <div class="boxButton">
   <input type="button" onClick="setDescription('<%=hrmReportId %>')" value="Set Description" /><span id="descriptionstatus<%=hrmReportId %>"></span><br /><br />
 </div>

</div>

<div id="commentBox">
Add a comment to this report:<br />
<textarea rows="10" cols="50" id="commentField_<%=hrmReportId %>_hrm"></textarea><br />

 <div class="boxButton">
   <input type="button" onClick="addComment('<%=hrmReportId %>')" value="Add Comment" /><span id="commentstatus<%=hrmReportId %>"></span><br /><br />
 </div>
<%
List<HRMDocumentComment> documentComments = (List<HRMDocumentComment>) request.getAttribute("hrmDocumentComments");

if (documentComments != null) {
	%>Displaying <%=documentComments.size() %> comment<%=documentComments.size() != 1 ? "s" : "" %><br />
	<% for (HRMDocumentComment comment : documentComments) { %>
		<div class="documentComment"><strong><%=providerDao.getProviderName(comment.getProviderNo()) %> on <%=comment.getCommentTime().toString() %> wrote...</strong><br />
		<%=comment.getComment() %><br />
		<a href="#" onClick="deleteComment('<%=comment.getId() %>', '<%=hrmReportId %>'); return false;">(Delete this comment)</a></div>
	<% }
}
%>
</div>

<div id="metadataBox">
	<table style="border: 1px solid black;margin: 20px;">
		<tr>
			<th>Message Unique ID</th>
			<td><%=hrmReport.getMessageUniqueId() %></td>
		</tr>
		<tr>
			<th>Sending Facility ID</th>
			<td><%=hrmReport.getSendingFacilityId() %></td>
		</tr>
		<tr>
			<th>Sending Facility Report No.</th>
			<td><%=hrmReport.getSendingFacilityReportNo() %></td>
		</tr>
		<tr>
			<th>Date and Time of Report</th>
			<td><%=HRMReportParser.getAppropriateDateFromReport(hrmReport) %></td>
		</tr>
		<tr>
			<th>Result Status</th>
			<td><%=(hrmReport.getResultStatus() != null && hrmReport.getResultStatus().equalsIgnoreCase("C")) ? "Cancelled" : "Signed by the responsible author and Released by health records"  %></td>
		</tr>
	</table>
</div>


<script type="text/javascript">
YAHOO.example.BasicRemote = function() {
    if($("autocompletedemo<%=hrmReportId%>hrm") && $("autocomplete_choices<%=hrmReportId%>hrm")){
           oscarLog('in basic remote');
          var url = "../demographic/SearchDemographic.do";
          var oDS = new YAHOO.util.XHRDataSource(url,{connMethodPost:true,connXhrMode:'ignoreStaleResponses'});
          oDS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;// Set the responseType
          // Define the schema of the delimited resultsTEST, PATIENT(1985-06-15)
          oDS.responseSchema = {
              resultsList : "results",
              fields : ["formattedName","fomattedDob","demographicNo"]
          };
          // Enable caching
          oDS.maxCacheEntries = 0;
          // Instantiate the AutoComplete
          var oAC = new YAHOO.widget.AutoComplete("autocompletedemo<%=hrmReportId%>hrm","autocomplete_choices<%=hrmReportId%>hrm",oDS);
          oAC.queryMatchSubset = true;
          oAC.minQueryLength = 3;
          oAC.maxResultsDisplayed = 25;
          oAC.formatResult = resultFormatter2;
          oAC.queryMatchContains = true;
          oAC.itemSelectEvent.subscribe(function(type, args) {
             var str = args[0].getInputEl().id.replace("autocompletedemo","demofind");
             $(str).value = args[2][2];//li.id;
             args[0].getInputEl().value = args[2][0] + "("+args[2][1]+")";
             $("routetodemo<%=hrmReportId %>hrm").value = args[0].getInputEl().value;
          	 
             addDemoToHrm('<%=hrmReportId %>');
          });


          return {
              oDS: oDS,
              oAC: oAC
          };
      }
      }();
      
      YAHOO.example.BasicRemote = function() {
          var url = "<%= request.getContextPath() %>/provider/SearchProvider.do";
          var oDS = new YAHOO.util.XHRDataSource(url,{connMethodPost:true,connXhrMode:'ignoreStaleResponses'});
          oDS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;// Set the responseType
          // Define the schema of the delimited resultsTEST, PATIENT(1985-06-15)
          oDS.responseSchema = {
              resultsList : "results",
              fields : ["providerNo","firstName","lastName"]
          };
          // Enable caching
          oDS.maxCacheEntries = 0;
          // Instantiate the AutoComplete
          var oAC = new YAHOO.widget.AutoComplete("autocompleteprov<%=hrmReportId%>hrm", "autocomplete_choicesprov<%=hrmReportId%>hrm", oDS);
          oAC.queryMatchSubset = true;
          oAC.minQueryLength = 3;
          oAC.maxResultsDisplayed = 25;
          oAC.formatResult = resultFormatter3;
          oAC.queryMatchContains = true;
          oAC.itemSelectEvent.subscribe(function(type, args) {
             var myAC = args[0];
             var str = myAC.getInputEl().id.replace("autocompleteprov","provfind");
             var oData=args[2];
             $(str).value = args[2][0];//li.id;
             myAC.getInputEl().value = args[2][2] + ","+args[2][1];
             var adoc = document.createElement('div');
             adoc.appendChild(document.createTextNode(oData[2] + " " +oData[1]));
             var idoc = document.createElement('input');
             idoc.setAttribute("type", "hidden");
             idoc.setAttribute("name","flagproviders");
             idoc.setAttribute("value",oData[0]);
             adoc.appendChild(idoc);

             var providerList = $('providerList<%=hrmReportId%>hrm');
             providerList.appendChild(adoc);

             myAC.getInputEl().value = '';//;oData.fname + " " + oData.lname ;

             addProvToHrm('<%=hrmReportId %>', args[2][0]);
          });


          return {
              oDS: oDS,
              oAC: oAC
          };
      }();
</script>

<%
String duplicateLabIdsString=StringUtils.trimToNull(request.getParameter("duplicateLabIds"));
if (duplicateLabIdsString!=null)
{
	Map<Integer,Date> dupReportDates = (Map<Integer,Date>)request.getAttribute("dupReportDates");
	Map<Integer,Date> dupTimeReceived = (Map<Integer,Date>)request.getAttribute("dupTimeReceived");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	%>
		<hr />
		Report History:<br />
		
		<table border="1">
			<tr>
				<th>ID</th>
				<th>Report Date</th>
				<th>Date Received</th>
				<th></th>
			</tr>
	<%
	//need datetime of report.
	String[] duplicateLabIdsStringSplit=duplicateLabIdsString.split(",");
	for (String tempId : duplicateLabIdsStringSplit)
	{
		%>
			<tr>
				<td><%=tempId %></td>
				<td><%=formatter.format(dupReportDates.get(Integer.parseInt(tempId))) %></td>
				<td><%=formatter.format(dupTimeReceived.get(Integer.parseInt(tempId))) %></td>
			    <td><input type="button" value="Open Report" onclick="window.open('?id=<%=tempId%>&segmentId=<%=tempId%>&providerNo=<%=request.getParameter("providerNo")%>&searchProviderNo=<%=request.getParameter("searchProviderNo")%>&status=<%=request.getParameter("status")%>&demoName=<%=StringEscapeUtils.escapeHtml(request.getParameter("demoName"))%>', null)" /> </td> 
			</tr>
			
		<%
	}
	
	%></table><%
}
%>

<br/>

Duplicates of this report have been received <%=request.getAttribute("hrmDuplicateNum")!=null?request.getAttribute("hrmDuplicateNum"):"0"%> time(s).

</body>
</html>
