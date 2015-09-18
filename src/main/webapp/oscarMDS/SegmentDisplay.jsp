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
<%@page errorPage="../provider/errorpage.jsp"%>
<%@ page
	import="java.util.*, oscar.oscarMDS.data.*,oscar.oscarLab.ca.on.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
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

<%


MDSSegmentData mDSSegmentData = new MDSSegmentData();
/*
String ackStatus = request.getParameter("status");
if ( request.getParameter("searchProviderNo") == null || request.getParameter("searchProviderNo").equals("") ) {
    ackStatus = "U";
} */
mDSSegmentData.populateMDSSegmentData(request.getParameter("segmentID"));

CommonLabResultData clrd = new CommonLabResultData();
String multiLabId = clrd.getMatchingLabs(request.getParameter("segmentID"), "MDS");
 String demoNo = clrd.getDemographicNo(request.getParameter("segmentID"),"MDS");

PatientData.Patient pd = new PatientData().getPatient(request.getParameter("segmentID"));
String AbnFlag = "";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><%=pd.getPatientName()%> - <bean:message
	key="oscarMDS.segmentDisplay.title" /></title>
<script language="javascript" type="text/javascript"
	src="../share/javascript/Oscar.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/jquery/jquery-1.4.2.js"></script>
<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

     
       <script  type="text/javascript" charset="utf-8">
       jQuery.noConflict();
        jQuery(function() {
        	jQuery("#createLabel").click(function() {
        		jQuery.ajax( {
      	      		type: "POST",      
      	      		url: '<%=request.getContextPath()%>'+"/lab/CA/ALL/createLabelTDIS.do",
      	      		dataType: "json",
      	      		data: { lab_no: jQuery("#labNum").val(),accessionNum: jQuery("#accNum").val(), label: jQuery("#label").val() },
      	      		success: function(data) {
      	      			jQuery("#labelspan").html(data.label);  
      	      	}
      	    });
      	  });
      });
        
    </script>
<script language="JavaScript">
function getComment() {
    var ret = true;
    var commentval = prompt("<bean:message key="oscarMDS.segmentDisplay.msgComment"/>", "");
    
    if( commentval == null )
        ret = false;
    else
        document.acknowledgeForm.comment.value = commentval;
        
    return ret;
}

function popupStart(vheight,vwidth,varpage,windowname) {
    var page = varpage;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
    var popup=window.open(varpage, windowname, windowprops);
}
function submitLabel(lblval){
	 document.forms['TDISLabelForm'].label.value = document.forms['acknowledgeForm'].label.value;
	
}
</script>

<body>
<!-- form forwarding of the lab -->
<form name="reassignForm" method="post" action="Forward.do"><input
	type="hidden" name="flaggedLabs"
	value="<%= request.getParameter("segmentID") %>" /> <input
	type="hidden" name="selectedProviders" value="" />
	<input type="hidden" name="favorites" value="" />
	 <input type="hidden" name="labType" value="MDS" /> <input type="hidden"
	name="labType<%= request.getParameter("segmentID") %>MDS"
	value="imNotNull" /> <input type="hidden" name="providerNo"
	value="<%= request.getParameter("providerNo") %>" /></form>
<form name="acknowledgeForm" method="post" action="UpdateStatus.do">

<table width="100%" height="100%" border="0" cellspacing="0"
	cellpadding="0">
	<tr>
		<td valign="top">
		<table width="100%" border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td align="left" class="MainTableTopRowRightColumn" width="100%">
				<input type="hidden" name="segmentID"
					value="<%= request.getParameter("segmentID") %>"> <input
					type="hidden" name="multiID" value="<%= multiLabId %>" /> <input
					type="hidden" name="providerNo"
					value="<%= request.getParameter("providerNo") %>"> <input
					type="hidden" name="status" value="A"> <input type="hidden"
					name="comment" value=""> <input type="hidden"
					name="labType" value="MDS" /> <% if ( request.getParameter("providerNo") != null && ! mDSSegmentData.getAcknowledgedStatus(request.getParameter("providerNo")) ) { %>
				<input type="submit"
					value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>"
					onclick="return getComment();"> <% } %> <input type="button"
					class="smallButton"
					value="<bean:message key="oscarMDS.index.btnForward"/>"
					onClick="popupStart(397, 700, 'SelectProvider.jsp', 'providerselect')">
				<input type="button" value=" <bean:message key="global.btnClose"/> "
					onClick="window.close()"> <input type="button"
					value=" <bean:message key="global.btnPrint"/> "
					onClick="window.print()"> <% if ( demoNo != null && !demoNo.equals("") && !demoNo.equalsIgnoreCase("null")){ %>
				<input type="button" value="Msg"
					onclick="popup(700,960,'../oscarMessenger/SendDemoMessage.do?demographic_no=<%=demoNo%>','msg')" />
				<input type="button" value="Tickler"
					onclick="popup(450,600,'../tickler/ForwardDemographicTickler.do?demographic_no=<%=demoNo%>','tickler')" />
				<% } %> <% if ( request.getParameter("searchProviderNo") == null ) { // we were called from e-chart %>
				<input type="button"
					value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> "
					onClick="window.close()"> <% } else { // we were called from lab module %>
				<input type="button"
					value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> "
					onClick="popupStart(360, 680, 'SearchPatient.do?labType=MDS&segmentID=<%= request.getParameter("segmentID")%>&name=<%=java.net.URLEncoder.encode(pd.getPatientName())%>', 'searchPatientWindow')">
				<% } %> &nbsp; <a
					href="javascript:popupStart(400,850,'../demographic/demographiccontrol.jsp?demographic_no=<%=demoNo%>&last_name=<%=demoNo%>&first_name=<%=demoNo%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=25','ApptHist')"
					style="font-size: 12px;" title="Click to see appointment history"><span
					class="Field2"><i>Next Appointment: <oscar:nextAppt
					demographicNo="<%=demoNo%>" /></i></span></a></td>
			</tr>
		</table>


		<table width="100%" border="1" cellspacing="0" cellpadding="3"
			bgcolor="#9999CC" bordercolordark="#bfcbe3">
			<%
                if (multiLabId != null){
                    String[] multiID = multiLabId.split(",");
                    if (multiID.length > 1){
                        %>
			<tr>
				<td class="Cell" colspan="2" align="middle">
				<div class="Field2">Version:&#160;&#160; <%
                                    for (int i=0; i < multiID.length; i++){
                                        if (multiID[i].equals(request.getParameter("segmentID"))){
                                            %>v<%= i+1 %>&#160;<%
                                        }else{
                                            if ( request.getParameter("searchProviderNo") != null ) { // null if we were called from e-chart
                                                %><a
					href="SegmentDisplay.jsp?segmentID=<%=multiID[i]%>&multiID=<%=multiLabId%>&providerNo=<%=request.getParameter("providerNo")%>&searchProviderNo=<%=request.getParameter("searchProviderNo")%>&status=<%=request.getParameter("status")%>">v<%= i+1 %></a>&#160;<%
                                            }else{
                                                %><a
					href="SegmentDisplay.jsp?segmentID=<%=multiID[i]%>&multiID=<%=multiLabId%>&providerNo=<%=request.getParameter("providerNo")%>&status=<%=request.getParameter("status")%>">v<%= i+1 %></a>&#160;<%
                                            }
                                        }
                                    }
                                    %>
				</div>
				</td>
			</tr>
			<%
                    }
                }
                %>
			<tr>
				<td width="66%" align="middle" class="Cell">
				<div class="Field2"><bean:message
					key="oscarMDS.segmentDisplay.formDetailResults" /></div>
				</td>
				<td width="33%" align="middle" class="Cell">
				<div class="Field2"><bean:message
					key="oscarMDS.segmentDisplay.formResultsInfo" /></div>
				</td>
			</tr>
			<tr>
				<td bgcolor="white" valign="top">
				<table valign="top" border="0" cellpadding="2" cellspacing="0"
					width="100%">
					<tr valign="top">
						<td valign="top" width="33%" align="left">
						<table width="100%" border="0" cellpadding="2" cellspacing="0"
							valign="top">
							<tr>
								<td valign="top" align="left">
								<table valign="top" border="0" cellpadding="3" cellspacing="0"
									width="100%">
									<tr>
										<td colspan="2" nowrap>
										<div class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formPatientName" />: </strong></div>
										</td>
										<td colspan="2" nowrap>
										<div class="FieldData" nowrap="nowrap">
										<% if ( request.getParameter("searchProviderNo") == null ) { // we were called from e-chart %>
										<a href="javascript:window.close()"> <% } else { // we were called from lab module %>

										<a
											href="javascript:popupStart(360, 680, 'SearchPatient.do?labType=MDS&segmentID=<%= request.getParameter("segmentID")%>&name=<%=java.net.URLEncoder.encode(pd.getPatientName() )%>', 'searchPatientWindow')">
										<% } %> <%=pd.getPatientName()%> </a></div>
										</td>
									</tr>
									<tr>
										<td colspan="2" nowrap>
										<div class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formDateBirth" />: </strong></div>
										</td>
										<td colspan="2" nowrap>
										<div class="FieldData" nowrap="nowrap"><%=pd.getDOB()%>
										</div>
										</td>
									</tr>
									<tr>
										<td colspan="2" nowrap>
										<div class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formAge" />: </strong><%=pd.getAge()%></div>
										</td>
										<td colspan="2" nowrap>
										<div class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formSex" />: </strong><%=pd.getSex()%></div>
										</td>
									</tr>
									<tr>
										<td colspan="2" nowrap>
										<div class="FieldData"><strong>
										<% if (pd.getHealthNumber().startsWith("X")) {%> <bean:message
											key="oscarMDS.segmentDisplay.formHealthNumber" /> <%} else {%>
										<bean:message key="oscarMDS.segmentDisplay.formMDSIDNumber" />
										<%}%> </strong></div>
										</td>
										<td colspan="2" nowrap>
										<div class="FieldData" nowrap="nowrap"><%=pd.getHealthNumber().substring(1)%>
										</div>
										</td>
									</tr>
								</table>
								</td>
								<td width="33%" valign="top">
								<table valign="top" border="0" cellpadding="3" cellspacing="0"
									width="100%">
									<tr>
										<td nowrap>
										<div align="left" class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formHomePhone" />: </strong></div>
										</td>
										<td nowrap>
										<div align="left" class="FieldData" nowrap="nowrap"><%=pd.getHomePhone()%>
										</div>
										</td>
									</tr>
									<tr>
										<td nowrap>
										<div align="left" class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formWorkPhone" />: </strong></div>
										</td>
										<td nowrap>
										<div align="left" class="FieldData" nowrap="nowrap"><%=pd.getWorkPhone()%>
										</div>
										</td>
									</tr>
									<tr>
										<td nowrap>
										<div align="left" class="FieldData" nowrap="nowrap"></div>
										</td>
										<td nowrap>
										<div align="left" class="FieldData" nowrap="nowrap"></div>
										</td>
									</tr>
									<tr>
										<td nowrap>
										<div align="left" class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formPatientLocation" />: </strong></div>
										</td>
										<td nowrap>
										<div align="left" class="FieldData" nowrap="nowrap"><%=pd.getPatientLocation()%>
										</div>
										</td>
									</tr>
								</table>
								</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
				<td bgcolor="white" valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="1">
					<tr>
						<td>
						<div class="FieldData"><strong><bean:message
							key="oscarMDS.segmentDisplay.formDateService" />:</strong></div>
						</td>
						<td>
						<div class="FieldData" nowrap="nowrap"><%= mDSSegmentData.reportDate %>
						</div>
						</td>
					</tr>
					<tr>
						<td>
						<div class="FieldData"><strong><bean:message
							key="oscarMDS.segmentDisplay.formReportStatus" />:</strong></div>
						</td>
						<td>
						<div class="FieldData" nowrap="nowrap"><%= mDSSegmentData.reportStatus %>
						</div>
						</td>
					</tr>
					<tr>
						<td></td>
					</tr>
					<tr>
						<td nowrap>
						<div class="FieldData"><strong><bean:message
							key="oscarMDS.segmentDisplay.formClientRefer" />:</strong></div>
						</td>
						<td nowrap>
						<div class="FieldData" nowrap="nowrap"><%= mDSSegmentData.clientNo %>
						</div>
						</td>
					</tr>
					<tr>
						<td>
						<div class="FieldData"><strong><bean:message
							key="oscarMDS.segmentDisplay.formAccession" />:</strong></div>
						</td>
						<td>
						<div class="FieldData" nowrap="nowrap"><%= mDSSegmentData.accessionNo %>
						</div>
						</td>
					</tr>
					<tr>
						<td>
						<div class="FieldData"><strong>Lab Vendor:</strong></div>
						</td>
						<td>
						<div class="FieldData" nowrap="nowrap">MDS</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td bgcolor="white" colspan="2">
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					bordercolor="#CCCCCC">
					<tr>
						<td bgcolor="white">
						<div class="FieldData"><strong><bean:message
							key="oscarMDS.segmentDisplay.formRequestingClient" />: </strong> <%= mDSSegmentData.providers.referringDoctor %>
						</div>
						</td>
						<td bgcolor="white">
						<div class="FieldData"><strong><bean:message
							key="oscarMDS.segmentDisplay.formReportToClient" />: </strong> <%= mDSSegmentData.providers.admittingDoctor %>
						</div>
						</td>
						<td bgcolor="white" align="right">
						<div class="FieldData"><strong><bean:message
							key="oscarMDS.segmentDisplay.formCCClient" />: </strong> <%= mDSSegmentData.providers.consultingDoctor %>
						</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td align="center" bgcolor="white" colspan="2">
				<%String[] multiID = multiLabId.split(",");
                                    boolean startFlag = false;                                    
                                    for (int j=multiID.length-1; j >=0; j--){
                                        MDSSegmentData currentData = new MDSSegmentData();
                                        currentData.populateMDSSegmentData(multiID[j]);
                                        
                                        if (multiID[j].equals(request.getParameter("segmentID")))
                                            startFlag = true;                                                              
                                        if (startFlag){
                                            if (currentData.statusArray.size() > 0){%>

				<table width="100%" height="20" cellpadding="2" cellspacing="2">
					<tr>
						<% if (multiID.length > 1){ %>
						<td align="center" bgcolor="white" width="20%" valign="top">
						<div class="FieldData"><b>Version:</b> v<%= j+1 %></div>
						</td>
						<td align="left" bgcolor="white" width="80%" valign="top">
						<% }else{ %>
						
						<td align="center" bgcolor="white">
						<% } %>
						<div class="FieldData"><!--center--> <% for (int i=0; i < currentData.statusArray.size(); i++) { 
                                                                    ReportStatus rs = (ReportStatus) currentData.statusArray.get(i); %>
						<%= rs.getProviderName() %> : <font color="red"><%= rs.getStatus() %></font>
						<% if ( rs.getStatus().equals("Acknowledged") ) { %> <%= rs.getTimestamp() %>,
						<%= ( rs.getComment().equals("") ? "no comment" : "comment : "+rs.getComment() ) %>
						<% } %> <br>
						<% } 
                                                                if (currentData.statusArray.size() == 0){
                                                                    %><font
							color="red">N/A</font>
						<%
                                                                }
                                                                %> <!--/center-->
						</div>
						</td>
					</tr>
				</table>

				<%}
                                        }
                                    }%>
				</td>
			</tr>
		</table>





		<% int i=0;
	       int j=0;
	       int k=0;
               int linenum=0;
               String highlight = "#E0E0FF";
	
     	       for(i=0;i<mDSSegmentData.headersArray.size();i++){
                   linenum=0;
                   if ( ((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).reportFlag.equals("MICROBIOLOGY CULTURE") ) { %>

		<table <%=i>0?"style=\"page-break-before:always;\"":""%> border="0"
			cellpadding="0" cellspacing="0" width="100%" bgcolor="#003399">
			<tr>
				<td colspan="4" height="7">&nbsp;</td>
			</tr>
			<tr>
				<td bgcolor="#FFCC00" width="200" height="22" valign="bottom">
				<div class="Title2"><%=((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).reportFlag%>
				</div>
				</td>
				<td align="right" bgcolor="#FFCC00" width="100">&nbsp;</td>
				<td width="9">&nbsp;</td>
				<td width="*">&nbsp;</td>
			</tr>
		</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="2"
			bgcolor="#CCCCFF" bordercolor="#9966FF" bordercolordark="#bfcbe3"
			name="tblDiscs" id="tblDiscs">
			<tr class="Field2">
				<td width="25%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formTestName" /></td>
				<td width="12%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formResult" /></td>
				<td width="12%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formAbn" /></td>
				<td width="12%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formReferenceRange" /></td>
				<td width="12%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formUnits" /></td>
				<td width="12%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formDateTimeCompleted" /></td>
				<td width="6%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formTestLocation" /></td>
				<td width="6%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formNew" /></td>
			</tr>
			<% if (((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).reportHeading != null) {
                                for (int x = 0; x < ((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).reportHeading.length; x++) { %>
			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="NormalRes">
				<td align="middle" colspan="8"><%=((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).reportHeading[x].equals("")?"&nbsp;":((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).reportHeading[x]%></td>
			</tr>
			<%  }
                                linenum++;
                               } %>

			<% j=0;
                                   int linenumbefore = linenum;
                                   while ( j<((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.size() ){
                                   if ( ((oscar.oscarMDS.data.Results)((oscar.oscarMDS.data.GroupedReports)((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.get(j)).resultsArray.get(0)).name.length() >= 3
                                        && ((oscar.oscarMDS.data.Results)((oscar.oscarMDS.data.GroupedReports)((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.get(j)).resultsArray.get(0)).name.substring(0,3).equals("ORG") ) {
                                       int firstorgindex = j;
                                       int lastorgindex = j;
                                       int m;

                                       while ( lastorgindex+1 < ((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.size()
                                               && ((oscar.oscarMDS.data.Results)((oscar.oscarMDS.data.GroupedReports)((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.get(lastorgindex+1)).resultsArray.get(0)).name.length() >= 3
                                               && ((oscar.oscarMDS.data.Results)((oscar.oscarMDS.data.GroupedReports)((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.get(lastorgindex+1)).resultsArray.get(0)).name.substring(0,3).equals("ORG") ) {
                                               lastorgindex++;
                                       } %>

			<% for (m=firstorgindex;m<=lastorgindex;m++) {  // print headers %>
			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="NormalRes">
				<td align="right"><bean:message
					key="oscarMDS.segmentDisplay.msgORG" /> <%=m-firstorgindex+1%></td>
				<td align="left" colspan="7"><%=((oscar.oscarMDS.data.Results)((oscar.oscarMDS.data.GroupedReports)((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.get(m)).resultsArray.get(0)).getLabNotes(0)%></td>


			</tr>
			<%     linenum++;
                                       } %>


			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="NormalRes">
				<td align="left"><bean:message
					key="oscarMDS.segmentDisplay.msgAntibiotic" /></td>
				<% for (m=firstorgindex;m<=lastorgindex;m++) {  // more headers %>
				<td align="middle"><bean:message
					key="oscarMDS.segmentDisplay.msgOrganism" /> <%=m-firstorgindex+1%></td>
				<% }
                                       linenum++; %>
				<td align="left" colspan="<%=6-lastorgindex+firstorgindex%>"></td>
			</tr>


			<% for (m=firstorgindex;m<=lastorgindex;m++) {  // m is organism index - inter over organisms to display results
                                           int n;  // n is antibiotic index
                                           String magicWord = "xyzzy";  // used to identify the results we've already processed
                                           oscar.oscarMDS.data.GroupedReports thisGroup = (oscar.oscarMDS.data.GroupedReports)((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.get(m);
                                           for (n = 1; n < thisGroup.resultsArray.size(); n++) {  // iter over antibiotics
                                               String aName = ((oscar.oscarMDS.data.Results)thisGroup.resultsArray.get(n)).name;
                                               if ( ((oscar.oscarMDS.data.Results)thisGroup.resultsArray.get(n)).observationValue != magicWord ) { // not yet seen this antibiotic %>
			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="NormalRes">
				<td valign="top" align="left"><%= ((oscar.oscarMDS.data.Results)thisGroup.resultsArray.get(n)).name %></td>
				<% for (int p=firstorgindex; p<=lastorgindex; p++) { // iter over organisms to print their results for this antibiotic
                                                            boolean foundResult = false;
                                                            for (int q=1; q < ((oscar.oscarMDS.data.GroupedReports)((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.get(p)).resultsArray.size(); q++) { // search the results for this organism for this particular antibiotic
                                                                if ( ((oscar.oscarMDS.data.Results)((oscar.oscarMDS.data.GroupedReports)((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.get(p)).resultsArray.get(q)).name.equals(aName) ) { %>
				<td align="middle"><%=((oscar.oscarMDS.data.Results)((oscar.oscarMDS.data.GroupedReports)((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.get(p)).resultsArray.get(q)).observationValue%></td>
				<% ((oscar.oscarMDS.data.Results)((oscar.oscarMDS.data.GroupedReports)((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.get(p)).resultsArray.get(q)).observationValue = magicWord; // signal that we've displayed this result
                                                                    foundResult = true;
                                                                    break; // finish the q=1...n for loop; we found what we want
                                                                }
                                                            }
                                                            if ( !foundResult ) { // display empty cell %>
				<td></td>
				<% }
                                                        } %>
				<td colspan="<%=6-lastorgindex+firstorgindex%>"></td>
			</tr>
			<%     linenum++;
                                               }  // end if result not processed
                                           }  // end for n=1... (iter over results for this organism)
                                       } // end for m=1... (iter over all organisms)
                                       j = lastorgindex + 1;                                       
                                   } else {  // not an organism sensitivity section
                                       for(k=0;k< ((oscar.oscarMDS.data.GroupedReports)((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.get(j)).resultsArray.size() ;k++){
                                       oscar.oscarMDS.data.Results thisResult = (oscar.oscarMDS.data.Results)((oscar.oscarMDS.data.GroupedReports)((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.get(j)).resultsArray.get(k);
                                       if (thisResult.resultStatus.startsWith("DNR") || (thisResult.resultStatus.startsWith("Deleted") && thisResult.notes == null)) continue;
                                       AbnFlag = thisResult.abnormalFlags; 
                                       boolean lineContinued = false; %>

			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="<%=thisResult.resultStatus.startsWith("Corrected")?"CorrectedRes":AbnFlag.compareTo("HI")==0?"AbnormalRes":AbnFlag.compareTo("LO")==0?"HiLoRes":"NormalRes"%>">
				<!--td valign="top" align="right"><%=thisResult.name %></td-->
				<td valign="top" align="left"><a
					href="../lab/CA/ON/labValues.jsp?testName=<%=thisResult.name%>&demo=<%=demoNo%>&labType=MDS"><%=thisResult.name %></a></td>
				<% if ( thisResult.observationValue.equals("") && thisResult.notes != null ) {
                                                lineContinued = true;
                                            } else { %>
				<td align="left"><%=thisResult.observationValue %></td>
				<td align="center"><%=thisResult.abnormalFlags %></td>
				<td align="left"><%=thisResult.referenceRange %></td>
				<td align="left"><%=thisResult.units %></td>
				<td align="center"></td>
				<td align="center"><%=thisResult.labID %></td>
				<td align="center"><%=thisResult.resultStatus %></td>
			</tr>
			<% } 

                                               if ( thisResult.notes != null) {
                                                   String notetext;
                                                   Iterator iter = thisResult.notes.listIterator();
                                                   while ( iter.hasNext() ) { 
                                                       notetext = (String)iter.next();
                                                           if ( !lineContinued) { %>
			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="<%=thisResult.resultStatus.startsWith("Corrected")?"CorrectedRes":AbnFlag.compareTo("HI")==0?"AbnormalRes":AbnFlag.compareTo("LO")==0?"HiLoRes":"NormalRes"%>">
				<td>&nbsp;</td>
				<% } // end if !lineContinued %>
				<td align="left" colspan="5"><%= notetext %></td>
				<% if (lineContinued) { %>
				<td align="center"><%=thisResult.labID %></td>
				<td align="center"><%=thisResult.resultStatus %></td>
				<% } else { %>
				<td></td>
				<td></td>
				<% } %>
			</tr>
			<%         lineContinued = false;
                                                   }  // end while iter.hasNext()
                                               }  // end if thisResult.notes != null
                                          linenum++;
                                        }  // for k=0... (results)
                                        j++;
                                     } // end if organism sensitivity reports
                                  }  // end while j=0... (result groups)
                                  if (linenumbefore == linenum) { %>
			<td colspan="9" align="center"><i>--- results deleted ---</i></td>
			<% } %>
		</table>

		<% } else {  // not a microbiology report group  %>

		<table style="page-break-inside: avoid;" bgcolor="#003399" border="0"
			cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td colspan="4" height="7">&nbsp;</td>
			</tr>
			<tr>
				<td bgcolor="#FFCC00" width="200" height="22" valign="bottom">
				<div class="Title2"><%=((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).reportFlag%>
				</div>
				</td>
				<td align="right" bgcolor="#FFCC00" width="100">&nbsp;</td>
				<td width="9">&nbsp;</td>
				<td width="*">&nbsp;</td>
			</tr>
		</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="2"
			bgcolor="#CCCCFF" bordercolor="#9966FF" bordercolordark="#bfcbe3"
			name="tblDiscs" id="tblDiscs">
			<tr class="Field2">
				<td width="25%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formTestName" /></td>
				<td width="15%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formResult" /></td>
				<td width="5%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formAbn" /></td>
				<td width="15%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formReferenceRange" /></td>
				<td width="10%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formUnits" /></td>
				<td width="15%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formDateTimeCompleted" /></td>
				<td width="6%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formTestLocation" /></td>
				<td width="6%" align="middle" valign="bottom" class="Cell"><bean:message
					key="oscarMDS.segmentDisplay.formNew" /></td>
			</tr>

			<% if (((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).reportHeading != null) {
                                for (int x = 0; x < ((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).reportHeading.length; x++) { %>
			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="NormalRes">
				<td align="middle" colspan="8"><%=((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).reportHeading[x].equals("")?"&nbsp;":((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).reportHeading[x]%></td>
			</tr>
			<%  }
                                linenum++;
                               } %>

			<% int linenumbefore = linenum;
                                   for(j=0;j<((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.size();j++){
                                   oscar.oscarMDS.data.GroupedReports thisGroup = (oscar.oscarMDS.data.GroupedReports)((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.get(j); %>

			<% for(k=0;k< ((oscar.oscarMDS.data.GroupedReports)((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.get(j)).resultsArray.size() ;k++){
                                       oscar.oscarMDS.data.Results thisResult = (oscar.oscarMDS.data.Results)((oscar.oscarMDS.data.GroupedReports)((oscar.oscarMDS.data.Headers)mDSSegmentData.headersArray.get(i)).groupedReportsArray.get(j)).resultsArray.get(k);
                                       if (thisResult.resultStatus.startsWith("DNR") || (thisResult.resultStatus.startsWith("Deleted") && thisResult.notes == null)) continue;
                                       AbnFlag = thisResult.abnormalFlags; 
                                       boolean lineContinued = false; %>

			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="<%=thisResult.resultStatus.startsWith("Corrected")?"CorrectedRes":AbnFlag.startsWith("HI", AbnFlag.indexOf("~") + 1)?"AbnormalRes":AbnFlag.startsWith("LO", AbnFlag.indexOf("~") + 1)?"HiLoRes":"NormalRes"%>">
				<!--td valign="top" align="left"><%=thisResult.name %></td-->
				<td valign="top" align="left"><a
					href="../lab/CA/ON/labValues.jsp?testName=<%=thisResult.name%>&demo=<%=demoNo%>&labType=MDS"><%=thisResult.name %></a></td>
				<% if ( thisResult.observationValue.equals("") && thisResult.notes != null ) {
                                                lineContinued = true;
                                            } else { %>
				<td align="right"><%=thisResult.observationValue %></td>
				<td align="center"><%=thisResult.abnormalFlags %></td>
				<td align="left"><%=thisResult.referenceRange %></td>
				<td align="left"><%=thisResult.units %></td>
				<td align="center"><%=thisGroup.timeStamp %></td>
				<td align="center"><%=thisResult.labID %></td>
				<td align="center"><%=thisResult.resultStatus %></td>
			</tr>
			<% }

                                               if ( thisResult.notes != null) {
                                                   String notetext;
                                                   Iterator iter = thisResult.notes.listIterator();
                                                   while ( iter.hasNext() ) { 
                                                       notetext = (String)iter.next(); 
                                                           if ( !lineContinued) { %>
			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="<%=thisResult.resultStatus.startsWith("Corrected")?"CorrectedRes":AbnFlag.compareTo("HI")==0?"AbnormalRes":AbnFlag.compareTo("LO")==0?"HiLoRes":"NormalRes"%>">
				<td>&nbsp;</td>
				<% } // end if !lineContinued %>
				<td align="left" colspan="5"><%= notetext %></td>
				<% if (lineContinued) { %>
				<td align="center"><%=thisResult.labID %></td>
				<td align="center"><%=thisResult.resultStatus %></td>
				<% } else { %>
				<td></td>
				<td></td>
				<% } %>
			</tr>

			<%         lineContinued = false;
                                                   }  // end while iter.hasNext()
                                               }  // end if thisResult.notes != null
                                        linenum++;
                                    }  // for k=0... (results)
                                    
                                  }  // for j=0... (result groups) 
                                  if (linenumbefore == linenum) { %>
			<td colspan="9" align="center"><i>--- results deleted ---</i></td>
			<% } %>
		</table>
		<% } // end if microbiology or not microbiology
              }  // for i=0... (headers) %> <!-- <table border="0" width="100%" cellpadding="5" cellspacing="0" bgcolor="white">
                <tr class="Field2">
                    <td width="20%" class="Cell2">
                        <div class="Field2" align="left">
                            <font color="white"></font>
                        </div>
                    </td>
                    <td width="60%" class="Cell2" valign="center" align="middle"><font color="white"><i>END
                        OF REPORT</i></font></td>
                    <td width="20%" class="Cell2" valign="center" align="right">&nbsp;</td>
                </tr>
            </table> -->
		<table width="100%" border="0" cellspacing="0" cellpadding="3"
			class="MainTableBottomRowRightColumn" bgcolor="#003399">
			<tr>
				<td align="left" width="40%">
				<% if ( request.getParameter("providerNo") != null && ! mDSSegmentData.getAcknowledgedStatus(request.getParameter("providerNo")) ) { %>
				<input type="submit"
					value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>"
					onclick="getComment()"> <% } %> <input type="button"
					class="smallButton"
					value="<bean:message key="oscarMDS.index.btnForward"/>"
					onClick="popupStart(397, 700, 'SelectProvider.jsp', 'providerselect')">
				<input type="button" value=" <bean:message key="global.btnClose"/> "
					onClick="window.close()"> <input type="button"
					value=" <bean:message key="global.btnPrint"/> "
					onClick="window.print()"> <% if ( demoNo != null && !demoNo.equals("") && !demoNo.equalsIgnoreCase("null")){ %>
				<input type="button" value="Msg"
					onclick="popup(700,960,'../oscarMessenger/SendDemoMessage.do?demographic_no=<%=demoNo%>','msg')" />
				<input type="button" value="Tickler"
					onclick="popup(450,600,'../tickler/ForwardDemographicTickler.do?demographic_no=<%=demoNo%>','tickler')" />
				<% } %> <% if ( request.getParameter("searchProviderNo") == null ) { // we were called from e-chart %>
				<input type="button"
					value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> "
					onClick="window.close()"> <% } else { // we were called from lab module %>
				<input type="button"
					value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> "
					onClick="popupStart(360, 680, 'SearchPatient.do?labType=MDS&segmentID=<%= request.getParameter("segmentID")%>&name=<%=java.net.URLEncoder.encode(pd.getPatientName() )%>', 'searchPatientWindow')">
				<% } %>
				</td>
				<td width="50%" valign="center" align="left"><span
					class="Field2"><i><bean:message
					key="oscarMDS.segmentDisplay.msgReportEnd" /></i></span></td>
			</tr>
		</table>
		</td>
	</tr>
</table>

</form>

</body>
</html>
