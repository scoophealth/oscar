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
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page errorPage="../provider/errorpage.jsp"%>
<%@ page
	import="java.util.*, oscar.oscarMDS.data.*,oscar.oscarLab.ca.bc.PathNet.*,oscar.oscarLab.ca.on.CML.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%




PathnetLabTest lab = new PathnetLabTest();
lab.populateLab(request.getParameter("segmentID"));

String AbnFlag = "";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="org.oscarehr.util.MiscUtils"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title><%=lab.pName%> <bean:message
	key="oscarMDS.segmentDisplay.title" /></title>
<script language="javascript" type="text/javascript"
	src="../../../share/javascript/Oscar.js"></script>
<link rel="stylesheet" type="text/css"
	href="../../../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<script language="JavaScript">
function getComment() {    
    var ret = true;
    var commentVal = prompt('<bean:message key="oscarMDS.segmentDisplay.msgComment"/>', '');
    
    if( commentVal == null )
        ret = false;
    else 
       document.acknowledgeForm.comment.value = commentVal;    
       
    return ret;
}

function popupStart(vheight,vwidth,varpage,windowname) {
    var page = varpage;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
    var popup=window.open(varpage, windowname, windowprops);
}
</script>

<body>
<!-- form forwarding of the lab -->
<form name="reassignForm" method="post" action="Forward.do"><input
	type="hidden" name="flaggedLabs"
	value="<%= request.getParameter("segmentID") %>" /> <input
	type="hidden" name="selectedProviders" value="" /> 
	<input type="hidden" name="favorites" value="" />
	<input type="hidden" name="labType" value="BCP" /> <input type="hidden"
	name="labType<%= request.getParameter("segmentID") %>BCP"
	value="imNotNull" /> <input type="hidden" name="providerNo"
	value="<%= request.getParameter("providerNo") %>" /></form>
<form name="acknowledgeForm" method="post"
	action="../../../oscarMDS/UpdateStatus.do">

<table width="100%" height="100%" border="0" cellspacing="0"
	cellpadding="0">
	<tr>
		<td valign="top">
		<table width="100%" border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td align="left" class="MainTableTopRowRightColumn" width="100%">
				<input type="hidden" name="segmentID"
					value="<%= request.getParameter("segmentID") %>" /> <input
					type="hidden" name="multiID" value="<%= lab.multiLabId %>" /> <input
					type="hidden" name="providerNo"
					value="<%= request.getParameter("providerNo") %>" /> <input
					type="hidden" name="status" value="A" /> <input type="hidden"
					name="comment" value="" /> <input type="hidden" name="labType"
					value="BCP" /> <% if ( request.getParameter("providerNo") != null /*&& ! mDSSegmentData.getAcknowledgedStatus(request.getParameter("providerNo")) */) { %>
				<input type="submit"
					value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>"
					onclick="return getComment();"> <% } %> <input type="button"
					class="smallButton"
					value="<bean:message key="oscarMDS.index.btnForward"/>"
					onClick="popupStart(397, 700, '../../../oscarMDS/SelectProvider.jsp', 'providerselect')">
				<input type="button" value=" <bean:message key="global.btnClose"/> "
					onClick="window.close()"> <input type="button"
					value=" <bean:message key="global.btnPrint"/> "
					onClick="window.print()"> <% if ( lab.getDemographicNo() != null && !lab.getDemographicNo().equals("") && !lab.getDemographicNo().equalsIgnoreCase("null")){ %>
				<input type="button" value="Msg"
					onclick="popup(700,960,'../../../oscarMessenger/SendDemoMessage.do?demographic_no=<%=lab.getDemographicNo()%>','msg')" />
				<input type="button" value="Tickler"
					onclick="popup(450,600,'../../../tickler/ForwardDemographicTickler.do?demographic_no=<%=lab.getDemographicNo()%>','tickler')" />
				<% } %> <% if ( request.getParameter("searchProviderNo") != null ) { // we were called from e-chart %>
				<input type="button"
					value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> "
					onClick="popupStart(360, 680, '../../../oscarMDS/SearchPatient.do?labType=BCP&segmentID=<%= request.getParameter("segmentID")%>&name=<%=java.net.URLEncoder.encode(lab.pName)%>', 'searchPatientWindow')">
				<% } %> <!--input type="button" value="Link To Lab Req" onClick="popupStart(360, 680, 'linkToLabReq.jsp?demo=<%=lab.getDemographicNo()%>&type=BCP&segmentID=<%=request.getParameter("segmentID")%>', 'searchPatientWindow')"-->
				<!--a href="linkToLabReq.jsp?demo=<%=lab.getDemographicNo()%>&type=BCP&segmentID=<%=request.getParameter("segmentID")%>"></a-->
				<span class="Field2"><i>Next Appointment: <oscar:nextAppt
					demographicNo="<%=lab.getDemographicNo()%>" /></i></span></td>
			</tr>
		</table>
		<table width="100%" border="1" cellspacing="0" cellpadding="3"
			bgcolor="#9999CC" bordercolordark="#bfcbe3">
			<%
                            if (lab.multiLabId != null){
                                String[] multiID = lab.multiLabId.split(",");
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
					href="labDisplay.jsp?segmentID=<%=multiID[i]%>&multiID=<%=lab.multiLabId%>&providerNo=<%=request.getParameter("providerNo")%>&searchProviderNo=<%=request.getParameter("searchProviderNo")%>">v<%= i+1 %></a>&#160;<%
                                                        }else{
                                                            %><a
					href="labDisplay.jsp?segmentID=<%=multiID[i]%>&multiID=<%=lab.multiLabId%>&providerNo=<%=request.getParameter("providerNo")%>">v<%= i+1 %></a>&#160;<%
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
											href="javascript:popupStart(360, 680, '../../../oscarMDS/SearchPatient.do?labType=BCP&segmentID=<%= request.getParameter("segmentID")%>&name=<%=java.net.URLEncoder.encode(lab.pName)%>', 'searchPatientWindow')">
										<% } %> <%=lab.pName%> </a></div>
										</td>
									</tr>
									<tr>
										<td colspan="2" nowrap>
										<div class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formDateBirth" />: </strong></div>
										</td>
										<td colspan="2" nowrap>
										<div class="FieldData" nowrap="nowrap"><%=lab.pDOB%></div>
										</td>
									</tr>
									<tr>
										<td colspan="2" nowrap>
										<div class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formAge" />: </strong><%=lab.getAge()%> <%
                                                                try{
                                                                    lab.getAge();                                                                    
                                                                    }catch(Exception e){ MiscUtils.getLogger().error("Error", e); }
                                                                
                                                                %>
										</div>
										</td>
										<td colspan="2" nowrap>
										<div class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formSex" />: </strong><%=lab.pSex%></div>
										</td>
									</tr>
									<tr>
										<td colspan="2" nowrap>
										<div class="FieldData"><strong>
										<% if (!lab.pHealthNum.startsWith("X")) {%> <bean:message
											key="oscarMDS.segmentDisplay.formHealthNumber" /> <%} else {%>
										<bean:message key="oscarMDS.segmentDisplay.formMDSIDNumber" />
										<%}%> </strong></div>
										</td>
										<td colspan="2" nowrap>
										<div class="FieldData" nowrap="nowrap"><%=lab.pHealthNum%>
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
										<div align="left" class="FieldData" nowrap="nowrap"><%=lab.pPhone%>
										</div>
										</td>
									</tr>
									<tr>
										<td nowrap>
										<div align="left" class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formWorkPhone" />: </strong></div>
										</td>
										<td nowrap>
										<div align="left" class="FieldData" nowrap="nowrap">
										&nbsp;</div>
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
										<div align="left" class="FieldData" nowrap="nowrap"><%=lab.patientLocation%>
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
						<div class="FieldData" nowrap="nowrap"><%= lab.serviceDate %>
						</div>
						</td>
					</tr>
					<tr>
						<td>
						<div class="FieldData"><strong><bean:message
							key="oscarMDS.segmentDisplay.formReportStatus" />:</strong></div>
						</td>
						<td>
						<div class="FieldData" nowrap="nowrap"><%= ( (String) ( lab.status.equals("F") ? "Final" : lab.status.equals("C") ? "Corrected" : "Partial") )%>
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
						<div class="FieldData" nowrap="nowrap"><%= lab.docNum%></div>
						</td>
					</tr>
					<tr>
						<td>
						<div class="FieldData"><strong><bean:message
							key="oscarMDS.segmentDisplay.formAccession" />:</strong></div>
						</td>
						<td>
						<div class="FieldData" nowrap="nowrap"><%= lab.accessionNum%>
						</div>
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
							key="oscarMDS.segmentDisplay.formRequestingClient" />: </strong> <%= lab.docName%>
						</div>
						</td>
						<td bgcolor="white">
						<div class="FieldData"><strong><bean:message
							key="oscarMDS.segmentDisplay.formReportToClient" />: </strong> <%= ""/*mDSSegmentData.providers.admittingDoctor not sure*/%>
						</div>
						</td>
						<td bgcolor="white" align="right">
						<div class="FieldData"><strong><bean:message
							key="oscarMDS.segmentDisplay.formCCClient" />: </strong> <%=lab.ccedDocs%>

						</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<% if (!lab.status.equals("U")){ %>
			<tr>
				<td align="center" bgcolor="white" colspan="2">
				<%String[] multiID = lab.multiLabId.split(",");
                                    boolean startFlag = false; 
                                    ArrayList statusArray;
                                    for (int j=multiID.length-1; j >=0; j--){
                                        statusArray = lab.getStatusArray(multiID[j]);
                                        
                                        if (multiID[j].equals(request.getParameter("segmentID")))
                                            startFlag = true;                                                              
                                        if (startFlag){
                                            if (statusArray.size() > 0){%>


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
						<div class="FieldData"><!--center--> <% for (int i=0; i < statusArray.size(); i++) { 
                                                                    ReportStatus rs = (ReportStatus) statusArray.get(i); %>
						<%= rs.getProviderName() %> : <font color="red"><%= rs.getStatus() %></font>
						<% if ( rs.getStatus().equals("Acknowledged") ) { %> <%= rs.getTimestamp() %>,
						<%= ( rs.getComment().equals("") ? "no comment" : "comment : "+rs.getComment() ) %>
						<% } %> <br>
						<% } %> <!--/center--></div>
						</td>
					</tr>
				</table>
				<%}    
                                        }
                                    }%>
				</td>
			</tr>
			<% } %>
		</table>





		<% int i=0;
	       int j=0;
	       int k=0;
               int linenum=0;
               String highlight = "#E0E0FF";
               
               ArrayList groupLabs = lab.getResults(lab.pid);
     	       for(i=0;i<groupLabs.size();i++){
                   linenum=0;
                   PathnetLabTest.GroupResults gResults = (PathnetLabTest.GroupResults) groupLabs.get(i);
                   %>
		<table style="page-break-inside: avoid;" bgcolor="#003399" border="0"
			cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td colspan="4" height="7">&nbsp;</td>
			</tr>
			<tr>
				<td bgcolor="#FFCC00" width="200" height="22" valign="bottom">
				<div class="Title2"><%=gResults.groupName%></div>
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

			<%
                        //int linenum = 1;
                        ArrayList labs = gResults.getLabResults();
                        String currentService = "";
                        for ( int l =0 ; l < labs.size() ; l++){
                            PathnetLabTest.LabResult thisResult = (PathnetLabTest.LabResult) labs.get(l);
                            //PATHL7LabTest.LabResult thisResult = (PATHL7LabTest.LabResult) labs.get(l);
                            String lineClass = "NormalRes";
                            if ( thisResult.abn != null && ( thisResult.abn.equals("A") || thisResult.abn.startsWith("H")) ){
                                lineClass = "AbnormalRes";
                            }else if ( thisResult.abn != null && thisResult.abn.equals("L")){
                                lineClass = "HiLoRes";
                            }
                            if (thisResult.isLabResult()){                               
                               if(!currentService.equals(thisResult.service_name)){%>

			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>">
				<td valign="top" align="left"><%=thisResult.service_name%></td>
				<td colspan="7">&nbsp;</td>
			</tr>
			<%currentService = thisResult.service_name;
                               if (currentService == null) { currentService = ""; }
                               }%>


			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="<%=lineClass%>">
				<td valign="top" align="left">&nbsp; &nbsp; <a
					href="../ON/labValues.jsp?testName=<%=thisResult.testName%>&demo=<%=lab.getDemographicNo()%>&labType=BCP"><%=thisResult.testName %></a></td>
				<td align="right"><%=thisResult.result %></td>
				<td align="center"><%=thisResult.abn %></td>
				<td align="left"><%=thisResult.getReferenceRange()%></td>
				<td align="left"><%=thisResult.units %></td>
				<td align="center"><%=thisResult.timeStamp %></td>
				<td align="center"><%=thisResult.locationId %></td>
				<td align="center"><%=thisResult.resultStatus %></td>
			</tr>
			<% }
                          if (thisResult.notes != null){%>
			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="<%=lineClass%>">
				<td valign="top" align="left" colspan="8"><pre
					style="margin-left: 100px;"><%=thisResult.notes %></pre></td>

			</tr>

			<% }%>

			<%}/*for lab.size*/%>
			<%ArrayList headerRes = gResults.getHeaderResults(); 
                          for( int h = 0; h < headerRes.size(); h++){  
                          String msg = (String) headerRes.get(h);  %>
			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="NormalRes">
				<td valign="top" align="left" colspan="8"><pre
					style="margin-left: 100px;"><%=msg%></pre></td>

			</tr>
			<%}%>

		</table>
		<% //} // end if microbiology or not microbiology
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
				<td align="left" width="50%">
				<% if ( request.getParameter("providerNo") != null /*&& ! mDSSegmentData.getAcknowledgedStatus(request.getParameter("providerNo")) */) { %>
				<input type="submit"
					value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>"
					onclick="getComment()"> <% } %> <input type="button"
					class="smallButton"
					value="<bean:message key="oscarMDS.index.btnForward"/>"
					onClick="popupStart(397, 700, '../../../oscarMDS/SelectProvider.jsp', 'providerselect')">
				<input type="button" value=" <bean:message key="global.btnClose"/> "
					onClick="window.close()"> <input type="button"
					value=" <bean:message key="global.btnPrint"/> "
					onClick="window.print()"> <% if ( lab.getDemographicNo() != null && !lab.getDemographicNo().equals("") && !lab.getDemographicNo().equalsIgnoreCase("null")){ %>
				<input type="button" value="Msg"
					onclick="popup(700,960,'../../../oscarMessenger/SendDemoMessage.do?demographic_no=<%=lab.getDemographicNo()%>','msg')" />
				<input type="button" value="Tickler"
					onclick="popup(450,600,'../../../tickler/ForwardDemographicTickler.do?demographic_no=<%=lab.getDemographicNo()%>','tickler')" />
				<% } %> <% if ( request.getParameter("searchProviderNo") != null ) { // we were called from e-chart %>
				<input type="button"
					value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> "
					onClick="popupStart(360, 680, '../../../oscarMDS/SearchPatient.do?labType=BCP&segmentID=<%= request.getParameter("segmentID")%>&name=<%=java.net.URLEncoder.encode(lab.pName)%>', 'searchPatientWindow')">
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
