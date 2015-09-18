<%--

    Copyright (c) 2008-2012 Indivica Inc.
    
    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".
    
--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_con" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_con");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%
String user_no = (String) session.getAttribute("user");
String userfirstname = (String) session.getAttribute("userfirstname");
String userlastname = (String) session.getAttribute("userlastname");
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties"
	scope="page" />
<%@ page
	import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, oscar.util.*, java.net.*,oscar.MyDateFormat, oscar.dms.*, oscar.dms.data.*, oscar.oscarEncounter.oscarConsultationRequest.pageUtil.ConsultationAttachDocs"%>
<%@ page import="oscar.oscarLab.ca.on.*"%>
<%@ page import="oscar.oscarLab.ca.all.Hl7textResultsData"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.util.SessionConstants"%>

<%

//preliminary JSP code

LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

// "Module" and "function" is the same thing (old dms module)
String module = "demographic";
String demoNo = request.getParameter("demo");
String requestId = request.getParameter("requestId");
String providerNo = request.getParameter("provNo");

if(demoNo == null && requestId == null ) response.sendRedirect("../error.jsp");

if( demoNo == null || demoNo.equals("null")  ) {

	ConsultationAttachDocs docsUtil = new ConsultationAttachDocs(requestId);
    demoNo = docsUtil.getDemoNo();
    
}

String patientName = EDocUtil.getDemographicName(loggedInInfo, demoNo);
String[] docType = {"D","L"};
String http_user_agent = request.getHeader("User-Agent");
boolean onIPad = http_user_agent.indexOf("iPad") >= 0;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery_oscar_defaults.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.colorbox-min.js"></script>
	
<title><bean:message
	key="oscarEncounter.oscarConsultationRequest.AttachDocPopup.title" /></title>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css" />

<script type="text/javascript">
//<!--   
<% 
CommonLabResultData labData = new CommonLabResultData();
ArrayList<LabResultData> labs = labData.populateLabResultsData(loggedInInfo, demoNo, requestId, CommonLabResultData.ATTACHED);
ArrayList<EDoc> privatedocs = new ArrayList<EDoc>();
privatedocs = EDocUtil.listDocs(loggedInInfo, demoNo, requestId, EDocUtil.ATTACHED);
String attachedDocs = "";
if (requestId == null || requestId.equals("") || requestId.equals("null")) {
	attachedDocs = "window.opener.document.EctConsultationFormRequestForm.documents.value";
}
else {
	for (int i = 0; i < privatedocs.size(); i++) {
	    attachedDocs += (attachedDocs.equals("") ? "" : "|") + "D" + (privatedocs.get(i)).getDocId(); 
	}
	
	for (int i = 0; i < labs.size(); i++) {
	    attachedDocs += (attachedDocs.equals("") ? "" : "|") + "L" + (labs.get(i)).getSegmentID(); 
	}
	attachedDocs = "\"" + attachedDocs + "\"";
}
%>  

//if consultation has not been saved, load existing docs into proper select boxes
function init() {	
	var docs = <%= attachedDocs %>; 
	docs = docs.split("|");
	checkDocuments(docs);                             
}

function checkDocuments(docs) {
	if (docs == null) { return; }
	for (var idx = 0; idx < docs.length; ++idx) {
        if (docs[idx].length < 2) { continue; }
        $("input[name='" + (docs[idx].charAt(0) == "L" ? "labNo" : "docNo") + "']"
              +"[value='" + docs[idx].substring(1) + "']").attr("checked", "checked");
    }
}


function save() {
    var ret;    
    if(document.forms[0].requestId.value == "null") {                       
       var saved = "";       
       var list = window.opener.document.getElementById("attachedList");       
       var paragraph = window.opener.document.getElementById("attachDefault");
       
       paragraph.innerHTML = "";                                                            
       
       //delete what we have before adding new docs to list
       while(list.firstChild) {
            list.removeChild(list.firstChild);
       }
             
       $("input[name='docNo']:checked").each(function() {
           saved += (saved == "" ? "" : "|") + "D" + $(this).attr("value");
           listElem = window.opener.document.createElement("li");           
           listElem.innerHTML = $(this).next().get(0).innerHTML;           
           listElem.className = "doc";
           list.appendChild(listElem);
       });
       $("input[name='labNo']:checked").each(function() {
           saved += (saved == "" ? "" : "|") + "L" + $(this).attr("value");
           listElem = window.opener.document.createElement("li");
           listElem.innerHTML = $(this).next().get(0).innerHTML;
           listElem.className = "lab";
           list.appendChild(listElem);
       });
                   
       window.opener.document.EctConsultationFormRequestForm.documents.value = saved; 
      
       if( list.childNodes.length == 0 )
            paragraph.innerHTML = "<bean:message key="oscarEncounter.oscarConsultationRequest.AttachDoc.Empty"/>";
            
       ret = false;
    }    
    else {      
        window.opener.updateAttached();
        ret = true;
    }
    if (!ret) window.close();
    return ret;
}

function previewPDF(docId, url) {	
	$("#previewPane").attr("src", 
			"<%= request.getContextPath() %>/oscarEncounter/oscarConsultationRequest/displayImage.jsp?url=" 
					       + encodeURIComponent("<%= request.getContextPath() %>" + "/dms/ManageDocument.do?method=view&doc_no=" + docId) 
					       + "&link=" + encodeURIComponent(url));
}

function previewHTML(url) {
	$("#previewPane").attr("src", url);
}

function previewImage(url) {
	$("#previewPane").attr("src", "<%= request.getContextPath() %>/oscarEncounter/oscarConsultationRequest/displayImage.jsp?url=" + encodeURIComponent(url));
}

function toggleSelectAll() {
	$("input[type='checkbox']").attr("checked", $("#selectAll").attr("checked"));
}

//-->
</script>
<style type="text/css">
#documentList a {
    text-decoration:none;
}
</style>

</head>
<body style="font-family: Verdana, Tahoma, Arial, sans-serif; background-color: #ddddff" onload="init()" >

<h3 style="text-align: left"><bean:message
	key="oscarEncounter.oscarConsultationRequest.AttachDocPopup.header" />
<%=patientName%></h3>
<html:form action="/oscarConsultationRequest/attachDoc">
	<html:hidden property="requestId" value="<%=requestId%>" />
	<html:hidden property="demoNo" value="<%=demoNo%>" />
	<html:hidden property="providerNo" value="<%=providerNo%>" />
	<table style="width:1080px; border: solid 1px blue; font-size: x-small; background-color:white;" >
		<tr>
			<th style="text-align: center"><bean:message
				key="oscarEncounter.oscarConsultationRequest.AttachDocPopup.available" /></th>			
			<th style="text-align: center"><bean:message
				key="oscarEncounter.oscarConsultationRequest.AttachDocPopup.preview" /></th>
		</tr>
		<tr valign="top" style="border-top:thin dotted blue;">
			<td style="width: 225px; text-align: left; background-color: white; border-right:thin dotted blue; position:absolute; height:600px;" >
			<input type="submit" class="btn" style="position: absolute; left: 35px; top: 5px;"
                name="submit"
                value="<bean:message key="oscarEncounter.oscarConsultationRequest.AttachDocPopup.submit"/>"
                onclick="return save();" />
			<ul id="documentList" style="list-style:none; padding:5px; margin-top:35px; height:515px; overflow:auto;"> 
			
            <%
            final String PRINTABLE_IMAGE = request.getContextPath() + "/images/printable.png";
            final String PRINTABLE_TITLE = "This file can be automatically printed to PDF with the consultation request.";
            final String PRINTABLE_ALT = "Printable";
            final String UNPRINTABLE_IMAGE = request.getContextPath() + "/images/notprintable.png";
            final String UNPRINTABLE_TITLE = "This file must be manually printed.";
            final String UNPRINTABLE_ALT = "Unprintable";
            
            privatedocs = EDocUtil.listDocs(loggedInInfo, "demographic", demoNo, null, EDocUtil.PRIVATE, EDocUtil.EDocSort.OBSERVATIONDATE);
            labData = new CommonLabResultData();
            labs = labData.populateLabResultsData(loggedInInfo, "",demoNo, "", "","","U");
            Collections.sort(labs);       
            
            if (labs.size() == 0 && privatedocs.size() == 0) {
            %>
                <li> There are no documents to attach. </li>
            <% }
            else {
            %> 
            <li>
                 <input class="tightCheckbox1" id="selectAll"
                        type="checkbox" onclick="toggleSelectAll()"
                        value="" title="Select/un-select all documents."
                        style="margin: 0px; padding: 0px;"/> Select all  
            </li>
            <% if(privatedocs.size() > 0){%>
            	<h2>Documents</h2>
            <%}%>
            <%
	            EDoc curDoc;
	            String url;      
	            String printTitle;
	            String printImage;
	            String printAlt;
	            String date;
	            String truncatedDisplayName;
	            for(int idx = 0; idx < privatedocs.size(); ++idx)
	            {                    
	                curDoc = privatedocs.get(idx); 
	                int slash = 0;
	                String contentType = "";
	                if ((slash = curDoc.getContentType().indexOf('/')) != -1) {
	                    contentType = curDoc.getContentType().substring(slash+1);
	                }
	                String dStatus = "";
	                if ((curDoc.getStatus() + "").compareTo("A") == 0) dStatus="active";
	                else if ((curDoc.getStatus() + "").compareTo("H") == 0) dStatus="html";
	                url = request.getContextPath() + "/oscarEncounter/oscarConsultationRequest/" 
	                    + "documentGetFile.jsp?document=" + StringEscapeUtils.escapeJavaScript(curDoc.getFileName()) 
	                    + "&type=" + dStatus + "&doc_no=" + curDoc.getDocId();
	                String onClick = "";
	                
	                if (curDoc.isPDF()) {                        
	                    onClick = "javascript:previewPDF('" + curDoc.getDocId() + "','" + StringEscapeUtils.escapeJavaScript(url) + "');";
	                }
	                else if (curDoc.isImage()) {
	                    onClick = "javascript:previewImage('" + url + "');";
	                }
	                else {
	                    onClick = "javascript:previewHTML('" + url + "');";
	                }                
	                
	                if (curDoc.isPrintable()) {
	                    printImage = PRINTABLE_IMAGE;
	                    printTitle = PRINTABLE_TITLE;
	                    printAlt   = PRINTABLE_ALT; 
	                    
	                }
	                else {
	                    printImage = UNPRINTABLE_IMAGE;
	                    printTitle = UNPRINTABLE_TITLE;
	                    printAlt   = UNPRINTABLE_ALT;
	                }
	                date = DateUtils.getDate(MyDateFormat.getCalendar(curDoc.getObservationDate()).getTime(), "dd-MMM-yyyy", request.getLocale());
	                truncatedDisplayName = StringUtils.maxLenString(curDoc.getDescription(),14,11,"");
	                if (StringUtils.isNullOrEmpty(truncatedDisplayName)) { truncatedDisplayName = "(none)"; }
	                %>
		                <li class="doc" title="<%=curDoc.getDescription()%>" id="<%=docType[0]+curDoc.getDocId()%>">
		                    <div>
		                    <div style="float:left; height:20px; line-height:20px;">
		                    	<input class="tightCheckbox1"
				                        type="checkbox" name="docNo" id="docNo<%=curDoc.getDocId()%>"
				                        value="<%=curDoc.getDocId()%>"
				                        style="margin: 0px; padding: 0px;" />
				                <span class="url" style="display:none">
		                        	<a  title="<%=curDoc.getDescription()%>" href="<%=url%>" style="color: blue; text-decoration: none;" target="_blank">
										<img style="width:15px;height:15px" title="<%= printTitle %>" src="<%= printImage %>" alt="<%= printAlt %>" />
										<%=truncatedDisplayName%>
									</a>										
			                    </span>  
			                    <img title="<%= printTitle %>" src="<%= printImage %>" alt="<%= printAlt %>">		                    
			                    <a class="docPreview" href="#" onclick="<%=onClick%>" >
			                        <span class="text"><%=truncatedDisplayName%></span>                
			                    </a>
			                    
			               </div>
			               <div style="float:right; height:25px; line-height:25px; margin-top:3px;">		                    
			                    <a class="docPreview" href="#" onclick="<%=onClick%>" >		                        
			                        <span>... <%=date%></span>
			                    </a>
		                   </div>
		                   <div style="clear:both;"></div>                    
		                   </div>
		                </li>
	                <%                                           
	                }
	            	if(labs.size() > 0){
	            	%>
	            		<h2>Labs</h2>	
	            	<%
	            	}
		                 
	                LabResultData result;
	                String labDisplayName;
	                printImage = PRINTABLE_IMAGE;
	                printTitle = PRINTABLE_TITLE;
	                printAlt = PRINTABLE_ALT;
	                for(int idx = 0; idx < labs.size(); ++idx) 
	                {
	                     result = labs.get(idx);
	                     if ( result.isMDS() ){ 
	                         url ="../../oscarMDS/SegmentDisplay.jsp?providerNo="+providerNo+"&segmentID="+result.segmentID+"&status="+result.getReportStatus();
	                         labDisplayName = result.getDiscipline();
	                     }else if (result.isCML()){ 
	                         url ="../../lab/CA/ON/CMLDisplay.jsp?providerNo="+providerNo+"&segmentID="+result.segmentID; 
	                         labDisplayName = result.getDiscipline();
	                     }else if (result.isHL7TEXT()){
	                         // Modified display name to append the lab's date and time.  
	                         labDisplayName = result.getDiscipline();
	                         url ="../../lab/CA/ALL/labDisplay.jsp?providerNo="+providerNo+"&segmentID="+result.segmentID;
	                     }else{    
	                         url ="../../lab/CA/BC/labDisplay.jsp?segmentID="+result.segmentID+"&providerNo="+providerNo;
	                         labDisplayName = result.getDiscipline();
	                     }	                     
	                     
	                     if (onIPad) {
	                         truncatedDisplayName = labDisplayName;
	                     }
	                     else {
	                         truncatedDisplayName = StringUtils.maxLenString(labDisplayName,14,11,"");
	                     }	                     
	                     date = DateUtils.getDate(result.getDateObj(), "dd-MMM-yyyy", request.getLocale());          				 
          				 if (StringUtils.isNullOrEmpty(truncatedDisplayName)) { truncatedDisplayName = "(none)"; }
	                     %>
						    <li class="lab" title="<%=labDisplayName%>" id="<%=docType[1]+result.labPatientId%>">
						        <div>
							        <div style="float:left; height:20px; line-height:20px; white-space:nowrap;">
								        <input class="tightCheckbox1" type="checkbox" 
			                               name="labNo" id="labNo<%=result.segmentID%>"
			                               value="<%=result.segmentID%>"
			                               style="margin: 0px; padding: 0px;" />       
			                            <span class="url" style="display:none">
								               <a href="<%=url%>" title="<%=labDisplayName%>" style="color: #CC0099; text-decoration: none;" target="_blank">
											   <img style="width:15px;height:15px" title="<%= printTitle %>" src="<%= printImage %>" alt="<%= printAlt %>" />
											<%=truncatedDisplayName%></a>
								        </span>             
								        <img title="<%= printTitle %>" src="<%= printImage %>" alt="<%= printAlt %>">
								        <a class="labPreview" href="#" onclick="javascript:previewHTML('<%=url%>');">
								           <span class="text"><%=truncatedDisplayName%></span>								           
								        </a>
								       
							        </div>
							        <div style="float:right; height:25px; line-height:25px; white-space:nowrap;">
							        	<a class="labPreview" href="#" onclick="javascript:previewHTML('<%=url%>');">
							            <span style="float:right;">... <%=date%></span>                         
							        </a>
							        </div>
						        </div>
						    </li>
	    
	                <% 
	                     } 
                    } %>               
                         
            </ul>
            <input type="submit" class="btn" style="position: absolute; left: 35px; bottom: 5px;"
                name="submit"
                value="<bean:message key="oscarEncounter.oscarConsultationRequest.AttachDocPopup.submit"/>"
                onclick="return save();" />
            </td>
            <td style="background-color:white; position:relative; width: 850px;"><iframe id="previewPane" style="width:100%; height: 600px; overflow: auto; border:0;" ></iframe></td>
		</tr>
	</table>	
</html:form>
</body>
</html:html>
