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
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
-->
<%@ page import="java.util.*" %>
<%@ page import="oscar.oscarMDS.data.*,oscar.oscarLab.ca.on.*,oscar.util.StringUtils,oscar.util.UtilDateUtilities" %>
<%@ page import="org.apache.commons.collections.MultiHashMap" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@page import="org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils"%>
<%@page import="org.oscarehr.util.MiscUtils, org.apache.commons.lang.StringEscapeUtils"%>

<%

Integer pageNum=(Integer)request.getAttribute("pageNum");
Hashtable docType=(Hashtable)request.getAttribute("docType");
Hashtable patientDocs=(Hashtable)request.getAttribute("patientDocs");
String providerNo=(String)request.getAttribute("providerNo");
String searchProviderNo=(String)request.getAttribute("searchProviderNo");
Hashtable patientIdNames=(Hashtable)request.getAttribute("patientIdNames");
String patientIdNamesStr=(String)request.getAttribute("patientIdNamesStr");
Hashtable docStatus=(Hashtable)request.getAttribute("docStatus");
String patientIdStr =(String)request.getAttribute("patientIdStr");
Hashtable typeDocLab =(Hashtable)request.getAttribute("typeDocLab");
String demographicNo=(String)request.getAttribute("demographicNo");
String ackStatus = (String)request.getAttribute("ackStatus");
List labdocs=(List)request.getAttribute("labdocs");
Hashtable patientNumDoc=(Hashtable)request.getAttribute("patientNumDoc");
Integer totalDocs=(Integer) request.getAttribute("totalDocs");
Integer totalHL7=(Integer)request.getAttribute("totalHL7");
List<String> normals=(List<String>)request.getAttribute("normals");
List<String> abnormals=(List<String>)request.getAttribute("abnormals");
Integer totalNumDocs=(Integer)request.getAttribute("totalNumDocs");
String curUser_no = (String) session.getAttribute("user");
final String noAckStatus="N";
String ctx = request.getContextPath();
%>


<html>

<head>
    <!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript" src="../share/calendar/lang/<bean:message key='global.javascript.calendar'/>"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<!-- calendar style sheet -->
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/prototype.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/scriptaculous.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/effects.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>

        <script type="text/javascript" src="../share/javascript/controls.js"></script>

        <script type="text/javascript" src="../share/yui/js/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="../share/yui/js/connection-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/animation-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/datasource-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/autocomplete-min.js"></script>
        <script type="text/javascript" src="../js/demographicProviderAutocomplete.js"></script>
        <script type="text/javascript" src="../share/javascript/oscarMDSIndex.js"></script>
        
        <link rel="stylesheet" type="text/css" href="../share/yui/css/fonts-min.css"/>
        <link rel="stylesheet" type="text/css" href="../share/yui/css/autocomplete.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="../share/css/demographicProviderAutocomplete.css"  />
        <link rel="stylesheet" type="text/css" media="all" href="../share/css/oscarMDSIndex.css"  />


<title>
<bean:message key="oscarMDS.index.title"/> Page <%=pageNum%>
</title>
<html:base/>

<link rel="stylesheet" type="text/css" href="encounterStyles.css">
        <style type="text/css">
            .Cell{
                background-color:#9999CC;
border-color:#CCCCFF #6666CC #6666CC #CCCCFF;
border-left:thin solid #CCCCFF;
border-right:thin solid #6666CC;
border-style:solid;
border-width:thin;
height:0px;
            }
            .NormalRes{
                height:0px;
}
        </style>
<script type="text/javascript" >
Event.observe(window,'scroll',function(){//check for scrolling
    bufferAndShow();
});

                           var tabindex=0;
                           var current_first_doclab=0;
                           var currentBold='';
                           var number_of_row_per_page=20;
                           var current_category=new Array();
                           var current_rows=new Array();
                           var current_numberofpages=1;
                           var total_rows=new Array();
                           var msgSelectOneLab='<bean:message key="oscarMDS.index.msgSelectOneLab"/>';
                           var contextpath='<%=request.getContextPath()%>';
                           var msgConsReq='<bean:message key="oscarEncounter.oscarConsultationRequest.ViewConsultationRequests.msgConsReq"/>';
                           var providerNo='<%=providerNo%>';
                           var searchProviderNo='<%=searchProviderNo%>';
                           var typeDocLab=initTypeDocLab('<%=typeDocLab%>');   //{DOC=[357, 317, 316], HL7=[38, 33, 30, 28]}
                           var docType=initDocType('<%=docType%>');   //{357=DOC, 38=HL7, 317=DOC, 316=DOC, 33=HL7, 30=HL7, 28=HL7}
                           var patientDocs=initPatientDocs('<%=patientDocs%>');//{2=[316, 30, 28], 1=[33], -1=[357, 317, 38]}
                           var patientIdNames=initPatientIdNames('<%=StringEscapeUtils.escapeJavaScript(patientIdNamesStr)%>');//;2=TEST2, PATIENT2;1=Zrrr, Srrr;-1=Not, Assigned
                           //var patientIdStr='<%=patientIdStr%>';//2,1,-1,
                           var docStatus=initDocStatus('<%=docStatus%>');//{357=A, 38=N, 317=A, 316=A, 33=N, 30=N, 28=N}
                           var normals=initNormals('<%=normals%>');//[357, 317, 316, 38, 33, 30, 28]
                           var abnormals=initAbnormals('<%=abnormals%>');//[123,567]
                           var patientIds=initPatientIds('<%=patientIdStr%>');
                           var types=['DOC','HL7'];
                           /*console.log(typeDocLab);
                           console.log(docType);
                           console.log(patientDocs);
                           console.log(patientIdNames);
                           console.log(patientIds);
                           console.log(docStatus);
                           console.log(normals);*/

                            function popupPage(vheight,vwidth,varpage) { //open a new popup window
                              var page = "" + varpage;
                              windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
                              var popup=window.open(page, "groupno", windowprops);
                              if (popup != null) {
                                if (popup.opener == null) {
                                  popup.opener = self;
                                }
                                popup.focus();
                              }
                            }

</script>
</head>

<body oldclass="BodyStyle" vlink="#0000FF" onload="setTotalRows();checkBox();" >
    <form name="reassignForm" method="post" action="ReportReassign.do" id="lab_form">
        <table  oldclass="MainTable" id="scrollNumber1" border="0" name="encounterTable" cellspacing="0" cellpadding="3" height="100%" width="100%">
            <tr oldclass="MainTableTopRow">
                <td class="MainTableTopRowRightColumn" colspan="10" align="left">
                 <table width="100%">
                    <tr>
                        <td align="center" colspan="2" class="Nav">
                                <% if (demographicNo == null) { %>
                                    <span class="white">
                                     <% if (ackStatus.equals("N")) {%>
                                           <bean:message key="oscarMDS.index.msgNewLabReportsFor"/>
                                     <%} else if (ackStatus.equals("A")) {%>
                                           <bean:message key="oscarMDS.index.msgAcknowledgedLabReportsFor"/>
                                     <%} else {%>
                                           <bean:message key="oscarMDS.index.msgAllLabReportsFor"/>
                                     <%}%>&nbsp;
                                     
                                     <% if (searchProviderNo.equals("")) {%>
                                            <bean:message key="oscarMDS.index.msgAllPhysicians"/>
                                     <%} else if (searchProviderNo.equals("0")) {%>
                                            <bean:message key="oscarMDS.index.msgUnclaimed"/>
                                     <%} else {%>
                                            <%=ProviderData.getProviderName(searchProviderNo)%>
                                     <%}%>
                                        &nbsp;&nbsp;&nbsp;
                                        Page : <a id="currentPageNum"><%=pageNum%></a>
                                     </span>
                                <% } %>
                        </td>
                        </tr>
                        <tr>
                            <td align="left" valign="center" > 
                                <input type="hidden" name="providerNo" value="<%= providerNo %>">
                                <input type="hidden" name="searchProviderNo" value="<%= searchProviderNo %>">
                                <%= (request.getParameter("lname") == null ? "" : "<input type=\"hidden\" name=\"lname\" value=\""+request.getParameter("lname")+"\">") %>
                                <%= (request.getParameter("fname") == null ? "" : "<input type=\"hidden\" name=\"fname\" value=\""+request.getParameter("fname")+"\">") %>
                                <%= (request.getParameter("hnum") == null ? "" : "<input type=\"hidden\" name=\"hnum\" value=\""+request.getParameter("hnum")+"\">") %>
                                <input type="hidden" name="status" value="<%= ackStatus %>">
                                <input type="hidden" name="selectedProviders">
                                <% if (demographicNo == null) { %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnSearch"/>" onClick="window.location='<%=ctx%>/oscarMDS/Search.jsp?providerNo=<%= providerNo %>'">
                                <% } %>
                                <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnClose"/>" onClick="wrapUp()">
                                <input type="button" id="topFRBtn" class="smallButton" value="Forwarding Rules" onClick="javascript:reportWindow('<%=ctx%>/oscarMDS/ForwardingRules.jsp?providerNo=<%= providerNo %>')">
                                <% if (demographicNo == null && request.getParameter("fname") != null) { %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnDefaultView"/>" onClick="window.location='../dms/inboxManage.do?method=prepareForIndexPage&providerNo=<%= providerNo %>'">
                                <% } %>
                                <% if (demographicNo == null && labdocs.size() > 0) { %>                                    
                                    <input id="topFBtn" type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="checkSelected()">
                                    <% if (ackStatus.equals("N")) {%>
                                        <input id="topFileBtn" type="button" class="smallButton" value="File" onclick="submitFile()"/>
                                    <% }
                                }%>
                              	<input type="checkbox" name="documentCB" id="documentCB" checked onclick="syncCB(this);checkBox();" ><span name="cbText" class="categoryCB"><bean:message key="inboxmanager.documents"/></span>
                                <input type="checkbox" onclick="syncCB(this);checkBox();" checked id="hl7CB" name="hl7CB"><span name="cbText" class="categoryCB"><bean:message key="global.hl7"/></span>
                                <input type="checkbox" onclick="syncCB(this);checkBox();" id="normalCB"  name="normalCB"><span name="cbText" class="categoryCB"><bean:message key="global.normal"/></span>
                                <input type="checkbox" id="abnormalCB" onclick="syncCB(this);checkBox();" name="abnormalCB"><span name="cbText" class="categoryCB"><bean:message key="global.abnormal"/></span>
                                <input type="checkbox" name="notAssignedDocCB" id="notAssignedDocCB" onclick="syncCB(this);checkBox()"><span name="cbText" class="categoryCB"><bean:message key="inboxmanager.notAssignedDocs"/></span>

                                <input type="hidden" id="currentNumberOfPages" value="0"/>
                            </td>
                            
                            <td align="right" valign="center" width="30%">
                                <a href="javascript:popupStart(300,400,'../oscarEncounter/Help.jsp')" style="color: #FFFFFF;"><bean:message key="global.help"/></a>
                                | <a href="javascript:popupStart(300,400,'../oscarEncounter/About.jsp')" style="color: #FFFFFF;" ><bean:message key="global.about"/></a>
                                | <a href="javascript:popupStart(800,1000,'../lab/CA/ALL/testUploader.jsp')" style="color: #FFFFFF; "><bean:message key="admin.admin.hl7LabUpload"/></a>
                                | <a href="javascript:popupStart(600,500,'../dms/html5AddDocuments.jsp')" style="color: #FFFFFF; "><bean:message key="inboxmanager.document.uploadDoc"/></a>
                                | <a href="javascript:void(0);" onclick="changeView();" style="color: #FFFFFF;"><bean:message key="inboxmanager.document.changeView"/></a>
                                | <a href="javascript:popupStart(700,1100,'../dms/inboxManage.do?method=getDocumentsInQueues')" style="color: #FFFFFF;"><bean:message key="inboxmanager.document.pendingDocs"/></a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td style="margin:0px;padding:0px;">
                    <table id="summaryView" width="100%" height="100%" style="margin:0px;padding:0px;" cellpadding="0" cellspacing="0">
                        <tr>
                            <th align="left" valign="bottom" class="cell" nowrap>
                                <input type="checkbox" onclick="checkAll('lab_form');" name="checkA"/>
                                <bean:message key="oscarMDS.index.msgHealthNumber"/>
                            </th>                            
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgPatientName"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgSex"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgResultStatus"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgDateTest"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgOrderPriority"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgRequestingClient"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgDiscipline"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                <bean:message key="oscarMDS.index.msgReportStatus"/>
                            </th>
                            <th align="left" valign="bottom" class="cell">
                                Ack #
                            </th>
                        </tr>
                        
                                                <%
                                boolean assignedDoc=false;
                                boolean notAssignedDoc=false;
                            List doclabid_seq=new ArrayList();
                            int number_of_rows_per_page=20;
                            double totalNoPages=Math.ceil(labdocs.size()/number_of_rows_per_page);
                            if(labdocs.size()%number_of_rows_per_page!=0)
                                totalNoPages+=1;
                            int total_row_index=labdocs.size()-1;
                            for (int i = 0; i < labdocs.size(); i++) {

                                LabResultData   result =  (LabResultData) labdocs.get(i);
                                //LabResultData result = (LabResultData) labMap.get(labNoArray.get(i));

                                String segmentID        =  result.segmentID;
                                String status           =  result.acknowledgedStatus;
                                String bgcolor = i % 2 == 0 ? "#e0e0ff" : "#ccccff" ;
                                if (!result.isMatchedToPatient()){
                                   bgcolor = "#FFCC00";
                                }

                                if(result.isDocument()){
                                    if(result.isMatchedToPatient()){
                                        assignedDoc=true;
                                        notAssignedDoc=false;
                                    }
                                    else{
                                        notAssignedDoc=true;
                                        assignedDoc=false;
                                    }
                                }else{
                                    assignedDoc=false;
                                    notAssignedDoc=false;
                                }
                                
                                String discipline=result.getDiscipline();
                                if(discipline==null || discipline.equalsIgnoreCase("null"))
                                    discipline="";
                                MiscUtils.getLogger().debug("result.isAbnormal()="+result.isAbnormal());
                                doclabid_seq.add(segmentID);
                                String classname = "";
                                if( assignedDoc ) {
                                	classname = "assignedDoc";                                	
                                }
                                else if( notAssignedDoc ) {
                                	classname = "notAssignedDoc";
                                }
                                else {
                                	classname = result.isAbnormal() ? "AbnormalRes" : "NormalRes";
                                }
                                %>
                        
                                <tr id="row<%=i%>" style="display:none;" bgcolor="<%=bgcolor%>" class="<%=classname %>">
                                <td nowrap>
                                	<input type="checkbox" name="flaggedLabs" value="<%=segmentID%>">
                                	<%=result.getHealthNumber() %>
                                    <input type="hidden" id="totalNumberRow" value="<%=total_row_index+1%>"></td>
                                    
                                    <input type="hidden" name="labType<%=segmentID+result.labType%>" value="<%=result.labType%>"/>
                                    <input type="hidden" name="ackStatus" value="<%= result.isMatchedToPatient() %>" />
                                    <input type="hidden" name="patientName" value="<%= result.patientName %>"/>
                                    
                                </td>
                                <td nowrap>
                                    <% if ( result.isMDS() ){ %>
                                    <a href="javascript:reportWindow('SegmentDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')"><%= result.getPatientName()%></a>
                                    <% }else if (result.isCML()){ %>
                                    <a href="javascript:reportWindow('../lab/CA/ON/CMLDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')"><%=(String) result.getPatientName()%></a>
                                    <% }else if (result.isHL7TEXT())
                                   	{ 
                                    	String categoryType=result.getDiscipline();
                                   		
                                    	if ("REF_I12".equals(categoryType))
                                    	{
	                                    	%>
                                      			<a href="javascript:popupConsultation('<%=segmentID%>')"><%=result.getPatientName()%></a>
                                    		<%                                    		
                                    	}
                                    	else if (categoryType!=null && categoryType.startsWith("ORU_R01:"))
                                    	{
	                                    	%>
                                      			<a href="<%=request.getContextPath()%>/lab/CA/ALL/viewOruR01.jsp?segmentId=<%=segmentID%>"><%=result.getPatientName()%></a>
                                    		<%                                    		
                                    	}
                                    	else
                                    	{
	                                    	%>
	                                    		<a href="javascript:reportWindow('../lab/CA/ALL/labDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')"><%=(String) result.getPatientName()%></a>
	                                    	<%
                                    	}
                                    }
                                    else if (result.isDocument()){ %>
                                    <a href="javascript:reportWindow('../dms/MultiPageDocDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>&demoName=<%=(String)result.getPatientName()%> ',660,1020)"><%=(String) result.getPatientName()%></a>
                                    <% }else {%>
                                    <a href="javascript:reportWindow('../lab/CA/BC/labDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')"><%=(String) result.getPatientName()%></a>                                   
                                    <% }%>
                                </td>
                                <td nowrap>
                                    <center><%=result.getSex() %></center>
                                </td>
                                <td nowrap>
                                    <%= (result.isAbnormal() ? "Abnormal" : "" ) %>
                                </td>
                                <td nowrap>
                                    <%=result.getDateTime()%>
                                </td>
                                <td nowrap>
                                    <%=result.getPriority()%>
                                </td>
                                <td nowrap>
                                    <%=result.getRequestingClient()%>
                                </td>
                                <td nowrap>
                                    <%=result.getDisciplineDisplayString()%>
                                </td>
                                <td nowrap>
                                    <%= (result.isFinal() ? "Final" : "Partial")%>
                                </td>
                                <td nowrap>
                                    <% int multiLabCount = result.getMultipleAckCount(); %>
                                    <%= result.getAckCount() %>&#160<% if ( multiLabCount >= 0 ) { %>(<%= result.getMultipleAckCount() %>)<%}%>
                                </td> 
                            </tr>                         
                         <% } 

                            if ( total_row_index < 0 ) { %>
                            <tr>
                                <td colspan="9" align="center">
                                    <i><bean:message key="oscarMDS.index.msgNoReports"/></i>
                                </td>
                            </tr>
                         <% } %>
                         <tr id="blankrow" style="display:none" ><td height="100%" colspan="10">&nbsp;</td></tr>
                            <tr class="MainTableBottomRow">
                                <td class="MainTableBottomRowRightColumn" bgcolor="#003399" colspan="10" align="left">
                                    <table width="100%">
                                        <tr>
                                            <td align="left" valign="middle" width="30%">
                                                <% if (demographicNo == null) { %>
                                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnSearch"/>" onClick="window.location='<%=ctx%>/oscarMDS/Search.jsp?providerNo=<%= providerNo %>'">
                                                <% } %>
                                                <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnClose"/>" onClick="wrapUp()">
                                                <input type="button" class="smallButton" value="Forwarding Rules" onClick="javascript:reportWindow('<%=ctx%>/oscarMDS/ForwardingRules.jsp?providerNo=<%= providerNo %>')">
                                                <% if (demographicNo == null && request.getParameter("fname") != null) { %>
                                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnDefaultView"/>" onClick="window.location='../dms/inboxManage.do?method=prepareForIndexPage&providerNo=<%= providerNo %>'">
                                                <% } %>
                                                <% if (demographicNo == null && labdocs.size() > 0) { %>                                                    
                                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="checkSelected()">
                                                    <% if (ackStatus.equals("N")) {%>
                                                        <input type="button" class="smallButton" value="File" onclick="submitFile()"/>
                                                    <% }
                                                } %>                                                
                                                <input type="checkbox" name="documentCB2" id="documentCB2" checked onclick="syncCB(this);checkBox();"><span name="cbText" class="categoryCB"><bean:message key="inboxmanager.documents"/></span>
                                                <input type="checkbox" onclick="syncCB(this);checkBox();" checked id="hl7CB2" name="hl7CB2"><span name="cbText" class="categoryCB"><bean:message key="global.hl7"/></span>
                                                    <input type="checkbox" onclick="syncCB(this);checkBox();" id="normalCB2"  name="normalCB2"><span name="cbText" class="categoryCB"><bean:message key="global.normal"/></span>
                                                    <input type="checkbox" id="abnormalCB2" onclick="syncCB(this);checkBox();" name="abnormalCB2"><span name="cbText" class="categoryCB"><bean:message key="global.abnormal"/></span>
                                                    <input type="checkbox" name="notAssignedDocCB2" id="notAssignedDocCB2" onclick="syncCB(this);checkBox()"><span name="cbText" class="categoryCB"><bean:message key="inboxmanager.notAssignedDocs"/></span>

                                            </td>
                                        <script type="text/javascript">
                                                var doclabid_seq='<%=doclabid_seq%>';
                                                doclabid_seq=doclabid_seq.replace('[','');
                                                doclabid_seq=doclabid_seq.replace(']','');
                                                var arr=doclabid_seq.split(',');
                                                var arr2=new Array();
                                                for(var i=0;i<arr.length;i++){
                                                    var ele=arr[i];
                                                    ele=ele.replace(' ','');
                                                    arr2.push(ele);
                                                }
                                                doclabid_seq=arr2;

                                        </script>
                                        </tr>
                                        <tr>
                                            <td align="center" valign="middle" width="40%">
                                                <div class="Nav">
                                                <% if ( totalNoPages>1 ) {
                                                    %>
                                                    <a id="msgPrevious" <%if(pageNum<=1){%> style="display:none;" <%}%> href="javascript:void(0);" onclick="navigatePage('Previous');">< <bean:message key="oscarMDS.index.msgPrevious"/></a>
                                                    <span id="current_individual_pages"><%
                                                      for( int i =0; i <totalNoPages; i ++){
                                                      %>
                                                       <a  style="text-decoration:none;" href="javascript:void(0);" onclick="navigatePage('<%=i+1%>')">[<%=i+1%>]</a>
                                                      <%
                                                                                                           }%></span>
                                                                                                      
                    <a id="msgNext" <%if( pageNum==totalNoPages ) {%> style="display:none;" <%}%> href="javascript:void(0);" onclick="navigatePage('Next');"><bean:message key="oscarMDS.index.msgNext"/> ></a>

                                                <%  } %>
                                                 </div>
                                            </td>
                                         
                                        </tr>
                                    </table>
                                </td>                            </tr>
                    </table>
                </td>
            </tr>
        </table>
    </form>
        <table id="readerViewTable" style="display:none;table-layout: fixed;border-color: blue;border-width: thin;border-spacing: 0px;background-color: #E0E1FF" width="100%" height="100%" border="1">
                                                     <col width="120">
                                                     <col width="950">
                <tr oldclass="MainTableTopRow">
                <td style="border-right: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9; background-color: #003399;"  colspan="10" align="left">
                 <table width="100%">
                    <tr>
                        <td align="center" colspan="2" class="Nav">
                                <% if (demographicNo == null) { %>
                                    <span class="white">
                                     <% if (ackStatus.equals("N")) {%>
                                           <bean:message key="oscarMDS.index.msgNewLabReportsFor"/>
                                     <%} else if (ackStatus.equals("A")) {%>
                                           <bean:message key="oscarMDS.index.msgAcknowledgedLabReportsFor"/>
                                     <%} else {%>
                                           <bean:message key="oscarMDS.index.msgAllLabReportsFor"/>
                                     <%}%>&nbsp;

                                     <% if (searchProviderNo.equals("")) {%>
                                            <bean:message key="oscarMDS.index.msgAllPhysicians"/>
                                     <%} else if (searchProviderNo.equals("0")) {%>
                                            <bean:message key="oscarMDS.index.msgUnclaimed"/>
                                     <%} else {%>
                                            <%=ProviderData.getProviderName(searchProviderNo)%>
                                     <%}%>
                                        &nbsp;&nbsp;&nbsp;
                                        Page : <a id="currentPageNum"><%=pageNum%></a>
                                     </span>
                                <% } %>
                        </td>
                        </tr>
                        <tr>
                            <td align="left" valign="center" >
                                <input type="hidden" name="providerNo" value="<%= providerNo %>">
                                <input type="hidden" name="searchProviderNo" value="<%= searchProviderNo %>">
                                <%= (request.getParameter("lname") == null ? "" : "<input type=\"hidden\" name=\"lname\" value=\""+request.getParameter("lname")+"\">") %>
                                <%= (request.getParameter("fname") == null ? "" : "<input type=\"hidden\" name=\"fname\" value=\""+request.getParameter("fname")+"\">") %>
                                <%= (request.getParameter("hnum") == null ? "" : "<input type=\"hidden\" name=\"hnum\" value=\""+request.getParameter("hnum")+"\">") %>
                                <input type="hidden" name="status" value="<%= ackStatus %>">
                                <input type="hidden" name="selectedProviders">
                                <% if (demographicNo == null) { %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnSearch"/>" onClick="window.location='<%=ctx%>/oscarMDS/Search.jsp?providerNo=<%= providerNo %>'">
                                <% } %>
                                <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnClose"/>" onClick="wrapUp()">

                            </td>

                            <td align="right" valign="center" width="30%">
                                <a href="javascript:popupStart(300,400,'../oscarEncounter/Help.jsp')" style="color: #FFFFFF;"><bean:message key="global.help"/></a>
                                | <a href="javascript:popupStart(300,400,'../oscarEncounter/About.jsp')" style="color: #FFFFFF;" ><bean:message key="global.about"/></a>
                                | <a href="javascript:popupStart(800,1000,'../lab/CA/ALL/testUploader.jsp')" style="color: #FFFFFF; "><bean:message key="admin.admin.hl7LabUpload"/></a>
                                | <a href="javascript:popupStart(600,500,'../dms/html5AddDocuments.jsp')" style="color: #FFFFFF; "><bean:message key="inboxmanager.document.uploadDoc"/></a>
                                | <a href="javascript:void(0);" onclick="changeView();" style="color: #FFFFFF;"><bean:message key="inboxmanager.document.changeView"/></a>

                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
          <tr>
              <td valign="top" style="width:20%;height: 100%;overflow:hidden;border-color: blue;border-width: thin;background-color: #E0E1FF" >
                    <%Enumeration en=patientIdNames.keys();
                        if(en.hasMoreElements()){%>
                        <div><a id="totalAll" href="javascript:void(0);" onclick="resetCurrentFirstDocLab();showAllDocLabs();un_bold(this);">All (<span id="totalNumDocs"><%=totalNumDocs%></span>)</a></div><br>

    <%}
                    if(totalDocs>0){
%>
<div><a id="totalDocs" href="javascript:void(0);" onclick="resetCurrentFirstDocLab();showCategory('DOC');un_bold(this);" title="Documents">Documents(<span id="totalDocsNum"><%=totalDocs%></span>)</a></div>
                     <%} if(totalHL7>0){%>
                     <div><a id="totalHL7s" href="javascript:void(0);" onclick="resetCurrentFirstDocLab();showCategory('HL7');un_bold(this);" title="HL7">HL7(<span id="totalHL7Num"><%=totalHL7%></span>)</a></div>

<%}
if(totalDocs>0||totalHL7>0){%><br><%}
                    if(normals!=null && normals.size()>0){
        Integer normalNum=normals.size();
    %>
    <div><a id="totalNormals" href="javascript:void(0);" onclick="resetCurrentFirstDocLab();showAb_Normal('normal');un_bold(this);" title="Normal" >Normal(<span id="normalNum"><%=normalNum%></span>)</a></div>

<%} if(abnormals!=null && abnormals.size()>0){
        Integer abnormalNum=abnormals.size() ;
    %>
    <div><a id="totalAbnormals" href="javascript:void(0);" onclick="resetCurrentFirstDocLab();showAb_Normal('abnormal');un_bold(this);" title="Abnormal">Abnormal(<span id="abnormalNum"><%=abnormalNum%></span>)</a></div>

<%}%>
<dl id="patientsdoclabs">
    <%
         Enumeration em=patientIdNames.keys();
         while(em.hasMoreElements()){
                        String patientId=(String)em.nextElement();
                        String patientName=(String)patientIdNames.get(patientId);
                        int numDocs=(Integer)patientNumDoc.get(patientId);
                        //get patient's doc
                        List<String> docs=(List<String>)patientDocs.get(patientId);

                        //check each doc if for it's type
                    Integer scanDocs=0;
                    Integer HL7s=0;
                    Integer CMLs=0;
                    Integer MDSs=0;
                        for(String s:docs){
                            s=s.trim();
                            String t=(String)docType.get(s);
                        if(t.equals("DOC"))
                                scanDocs++;
                        else if(t.equals("HL7"))
                                HL7s++;
                        else if(t.equals("CML"))//not used
                                CMLs++;
                        else if(t.equals("MDSs"))//not used
                                MDSs++;
                        }


   %>

   <dt><img id="plus<%=patientId%>" alt="plus" src="../images/plus.png" onclick="showhideSubCat('plus','<%=patientId%>');"/>
       <img id="minus<%=patientId%>" alt="minus" style="display:none;" src="../images/minus.png" onclick="showhideSubCat('minus','<%=patientId%>');"/>
       <a id="patient<%=patientId%>all" href="javascript:void(0);"  onclick="resetCurrentFirstDocLab();showThisPatientDocs('<%=patientId%>');un_bold(this);" title="<%=patientName%>"><% if ( patientId.equals("-1")) { %>Unmatched<% } else { %><%=patientName%><% } %>(<span id="patientNumDocs<%=patientId%>"><%=numDocs%></span>)</a>&nbsp;<a href="javascript:void(0);" onclick="showPatientPreview('<%=patientId%>','<%=providerNo%>','<%=searchProviderNo%>','<%=noAckStatus%>'); return false;"  title="preview patient's document and lab." style="color:red;text-decoration: none;">*</a>
                         <dl id="labdoc<%=patientId%>showSublist" style="display:none" >
                        <%if(scanDocs>0){%>
                        <dt><a id="patient<%=patientId%>docs" href="javascript:void(0);" onclick="resetCurrentFirstDocLab();showSubType('<%=patientId%>','DOC');un_bold(this);" title="Documents">Documents(<span id="pDocNum_<%=patientId%>"><%=scanDocs%></span>)</a>
                        </dt>
                     <%} if(HL7s>0){%>
                     <dt><a id="patient<%=patientId%>hl7s" href="javascript:void(0);" onclick="resetCurrentFirstDocLab();showSubType('<%=patientId%>','HL7');un_bold(this);" title="HL7">HL7(<span id="pLabNum_<%=patientId%>"><%=HL7s%></span>)</a>
                        </dt>
                     <%}%>
                    </dl>
                        <%}%>

                    </dt></dl>
             </td>
             <td style="width:80%;height:100%;background-color: #E0E1FF">
                 <div id="docViews" style="width:100%;height:100%;"></div>
             </td>
          </tr>
     </table>
</body>
</html>
