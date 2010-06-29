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
<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="oscar.oscarMDS.data.*,oscar.oscarLab.ca.on.*,oscar.util.StringUtils,oscar.util.UtilDateUtilities" %>
<%@ page import="org.apache.commons.collections.MultiHashMap" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@page import="org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils"%>
<%@page import="org.oscarehr.util.MiscUtils"%>

<%

Integer pageNum=(Integer)request.getAttribute("pageNum");
Hashtable docType=(Hashtable)request.getAttribute("docType");
Hashtable patientDocs=(Hashtable)request.getAttribute("patientDocs");
String providerNo=(String)request.getAttribute("providerNo");
String searchProviderNo=(String)request.getAttribute("searchProviderNo");
Hashtable patientIdNames=(Hashtable)request.getAttribute("patientIdNames");
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

<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/prototype.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/scriptaculous.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/effects.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>

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
<script type="text/javascript" >
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
                           var typeDocLab='<%=typeDocLab%>';   
                           var docType='<%=docType%>';   
                           var patientDocs='<%=patientDocs%>';
                           var patientIdNames='<%=patientIdNames%>';
                           var patientIdStr='<%=patientIdStr%>';
                           var docStatus='<%=docStatus%>';
                           var normals='<%=normals%>';
                           var abnormals='<%=abnormals%>';
                           var tabindex=0;
                           var current_first_doclab=0;
                           /*oscarLog("typeDocLab ="+typeDocLab);
                           oscarLog("docType ="+docType);
                           oscarLog("patientDocs ="+patientDocs);
                           oscarLog("patientIdNames ="+patientIdNames);
                           oscarLog("patientIdStr ="+patientIdStr);
                           oscarLog("docStatus ="+docStatus);
                           oscarLog("normals ="+normals);
                           oscarLog("abnormals ="+abnormals);*/

                            
</script>
</head>

<body oldclass="BodyStyle" vlink="#0000FF" onload="setTotalRows();" >
    <form name="reassignForm" method="post" action="ReportReassign.do" id="lab_form">
        <table  oldclass="MainTable" id="scrollNumber1" border="0" name="encounterTable" cellspacing="0" cellpadding="3" width="100%">
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
                            <td align="left" valign="center" > <%-- width="30%" --%>
                                <input type="hidden" name="providerNo" value="<%= providerNo %>">
                                <input type="hidden" name="searchProviderNo" value="<%= searchProviderNo %>">
                                <%= (request.getParameter("lname") == null ? "" : "<input type=\"hidden\" name=\"lname\" value=\""+request.getParameter("lname")+"\">") %>
                                <%= (request.getParameter("fname") == null ? "" : "<input type=\"hidden\" name=\"fname\" value=\""+request.getParameter("fname")+"\">") %>
                                <%= (request.getParameter("hnum") == null ? "" : "<input type=\"hidden\" name=\"hnum\" value=\""+request.getParameter("hnum")+"\">") %>
                                <input type="hidden" name="status" value="<%= ackStatus %>">
                                <input type="hidden" name="selectedProviders">
                                <% if (demographicNo == null) { %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnSearch"/>" onClick="window.location='Search.jsp?providerNo=<%= providerNo %>'">
                                <% } %>
                                <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnClose"/>" onClick="wrapUp()">
                                <input type="button" id="topFRBtn" class="smallButton" value="Forwarding Rules" onClick="javascript:reportWindow('ForwardingRules.jsp?providerNo=<%= providerNo %>')">
                                <% if (demographicNo == null && request.getParameter("fname") != null) { %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnDefaultView"/>" onClick="window.location='Index.jsp?providerNo=<%= providerNo %>'">
                                <% } %>
                                <% if (demographicNo == null && labdocs.size() > 0) { %>                                    
                                    <input id="topFBtn" type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="checkSelected()">
                                    <% if (ackStatus.equals("N")) {%>
                                        <input id="topFileBtn" type="button" class="smallButton" value="File" onclick="submitFile()"/>
                                    <% }
                                }%>
                                
                                <input type="checkbox" name="documentCB" id="documentCB" onclick="syncCB(this);checkBox();" ><span name="cbText" class="categoryCB"><bean:message key="global.Document"/></span>
                                <input type="checkbox" onclick="syncCB(this);checkBox();" id="hl7CB" name="hl7CB"><span name="cbText" class="categoryCB"><bean:message key="global.hl7"/></span>
                                <input type="checkbox" onclick="syncCB(this);checkBox();" id="normalCB"  name="normalCB"><span name="cbText" class="categoryCB"><bean:message key="global.normal"/></span>
                                <input type="checkbox" id="abnormalCB" onclick="syncCB(this);checkBox();" name="abnormalCB"><span name="cbText" class="categoryCB"><bean:message key="global.abnormal"/></span>
                                <input type="hidden" id="currentNumberOfPages" value="0"/>
                            </td>
                            
                            <td align="right" valign="center" width="30%">
                                <a href="javascript:popupStart(300,400,'../oscarEncounter/Help.jsp')" style="color: #FFFFFF;"><bean:message key="global.help"/></a>
                                | <a href="javascript:popupStart(300,400,'../oscarEncounter/About.jsp')" style="color: #FFFFFF;" ><bean:message key="global.about"/></a>
                                | <a href="javascript:popupStart(800,1000,'../lab/CA/ALL/testUploader.jsp')" style="color: #FFFFFF; "><bean:message key="admin.admin.hl7LabUpload"/></a>
                                | <a href="javascript:popupStart(600,500,'../dms/addDocuments2.jsp')" style="color: #FFFFFF; "><bean:message key="inboxmanager.document.uploadDoc"/></a>
                                | <a href="javascript:void(0);" onclick="changeView();" style="color: #FFFFFF;"><bean:message key="inboxmanager.document.changeView"/></a>
                                
                                <!--input type="text" value="" onchange="updateDocLabData(this.value);"/-->
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td style="margin:0px;padding:0px;">
                    <table id="summaryView" width="100%" style="margin:0px;padding:0px;" cellpadding="0" cellspacing="0">
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
                            List doclabid_seq=new ArrayList();
                            int number_of_rows_per_page=20;
                            int totalNoPages=labdocs.size()/number_of_rows_per_page;
                            if(labdocs.size()%number_of_rows_per_page!=0)
                                totalNoPages+=1;
                            //System.out.println("totalNoPages="+totalNoPages);
                            int total_row_index=labdocs.size()-1;
                            //System.out.println("total_row_index="+total_row_index);
                            for (int i = 0; i < labdocs.size(); i++) {

                                LabResultData   result =  (LabResultData) labdocs.get(i);
                                //LabResultData result = (LabResultData) labMap.get(labNoArray.get(i));

                                String segmentID        =  result.segmentID;
                                String status           =  result.acknowledgedStatus;

                                String bgcolor = i % 2 == 0 ? "#e0e0ff" : "#ccccff" ;
                                if (!result.isMatchedToPatient()){
                                   bgcolor = "#FFCC00";
                                }

                                String discipline=result.getDiscipline();
                                if(discipline==null || discipline.equalsIgnoreCase("null"))
                                    discipline="";
                                MiscUtils.getLogger().debug("result.isAbnormal()="+result.isAbnormal());
                                doclabid_seq.add(segmentID);
                                %>
                        
                                <tr id="row<%=i%>" <%if((number_of_rows_per_page-1)<i){%>style="display:none;"<%}%> bgcolor="<%=bgcolor%>" <%if(result.isDocument()){%> name="scannedDoc" <%} else{%> name="HL7lab" <%}%> class="<%= (result.isAbnormal() ? "AbnormalRes" : "NormalRes" ) %>">
                                <td nowrap>
                                    <input type="hidden" id="totalNumberRow" value="<%=total_row_index+1%>">
                                    <input type="checkbox" name="flaggedLabs" value="<%=segmentID%>">
                                    <input type="hidden" name="labType<%=segmentID+result.labType%>" value="<%=result.labType%>"/>
                                    <input type="hidden" name="ackStatus" value="<%= result.isMatchedToPatient() %>" />
                                    <input type="hidden" name="patientName" value="<%= result.patientName %>"/>
                                    <%=result.getHealthNumber() %>
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
                                    <a href="javascript:reportWindow('../dms/DocumentDisplay3.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>&demoName=<%=(String)result.getPatientName()%> ',660,1020)"><%=(String) result.getPatientName()%></a>
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
                            <tr class="MainTableBottomRow">
                                <td class="MainTableBottomRowRightColumn" bgcolor="#003399" colspan="10" align="left">
                                    <table width="100%">
                                        <tr>
                                            <td align="left" valign="middle" width="30%">
                                                <% if (demographicNo == null) { %>
                                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnSearch"/>" onClick="window.location='Search.jsp?providerNo=<%= providerNo %>'">
                                                <% } %>
                                                <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnClose"/>" onClick="wrapUp()">
                                                <input type="button" class="smallButton" value="Forwarding Rules" onClick="javascript:reportWindow('ForwardingRules.jsp?providerNo=<%= providerNo %>')">
                                                <% if (request.getParameter("fname") != null) { %>
                                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnDefaultView"/>" onClick="window.location='Index.jsp?providerNo=<%= providerNo %>'">
                                                <% } %>
                                                <% if (demographicNo == null && labdocs.size() > 0) { %>                                                    
                                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="checkSelected()">
                                                    <% if (ackStatus.equals("N")) {%>
                                                        <input type="button" class="smallButton" value="File" onclick="submitFile()"/>
                                                    <% }
                                                } %>                                                
                                                    <input type="checkbox" name="documentCB2" id="documentCB2" onclick="syncCB(this);checkBox();"><span name="cbText" class="categoryCB"><bean:message key="global.Document"/></span>
                                                    <input type="checkbox" onclick="syncCB(this);checkBox();" id="hl7CB2" name="hl7CB2"><span name="cbText" class="categoryCB"><bean:message key="global.hl7"/></span>
                                                    <input type="checkbox" onclick="syncCB(this);checkBox();" id="normalCB2"  name="normalCB2"><span name="cbText" class="categoryCB"><bean:message key="global.normal"/></span>
                                                    <input type="checkbox" id="abnormalCB2" onclick="syncCB(this);checkBox();" name="abnormalCB2"><span name="cbText" class="categoryCB"><bean:message key="global.abnormal"/></span>
                                            </td>
                                        <script type="text/javascript">
                                                var doclabid_seq='<%=doclabid_seq%>';
                                                //oscarLog('doclabid_seq='+doclabid_seq);
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
                                                //oscarLog('doclabid_seq='+doclabid_seq);

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
        <table id="readerViewTable" style="display:none;table-layout: fixed;border-color: blue;border-width: thin;border-spacing: 0px;background-color: #E0E1FF" width="1080" border="1">
                                                     <col width="120">
                                                     <col width="950">
          <tr>
              <td valign="top" style="overflow:hidden;border-color: blue;border-width: thin;background-color: #E0E1FF" >
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
<dl>
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
       <a id="patient<%=patientId%>all" href="javascript:void(0);"  onclick="resetCurrentFirstDocLab();showThisPatientDocs('<%=patientId%>');un_bold(this);" title="<%=patientName%>"><%=patientName%> (<span id="patientNumDocs<%=patientId%>"><%=numDocs%></span>)</a>
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
             <td style="width:100%;height:600px;background-color: #E0E1FF">
                 <div id="docViews" style="width:100%;height:600px;overflow:auto;"></div>  
             </td>
          </tr>
     </table>
</body>
</html>
