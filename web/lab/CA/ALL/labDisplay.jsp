<%@ page language="java" errorPage="../../../provider/errorpage.jsp"%>
<%@ page
	import="java.util.*,
java.sql.*,
oscar.oscarDB.*,
oscar.oscarLab.ca.all.*,
oscar.oscarLab.ca.all.util.*,
oscar.oscarLab.ca.all.parsers.*,
oscar.oscarMDS.data.ReportStatus,
org.apache.commons.codec.binary.Base64"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%


//String sql = "SELECT status FROM providerLabRouting WHERE lab_no='"+request.getParameter("segmentID")+"';";
String sql = "SELECT demographic_no FROM patientLabRouting WHERE lab_no='"+request.getParameter("segmentID")+"';";
DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
ResultSet rs = db.GetSQL(sql);
String demographicID = "";
while(rs.next()){
    demographicID = db.getString(rs,"demographic_no");
}
rs.close();

boolean ackFlag = false;
AcknowledgementData ackData = new AcknowledgementData();
ArrayList ackList = ackData.getAcknowledgements(request.getParameter("segmentID"));
if (ackList != null){
    for (int i=0; i < ackList.size(); i++){
        ReportStatus reportStatus = (ReportStatus) ackList.get(i);
        if ( reportStatus.getProviderNo().equals(request.getParameter("providerNo")) && reportStatus.getStatus().equals("A") ){
            ackFlag = true;
            break;
        }
    }
}
Factory f = new Factory();
MessageHandler handler = f.getHandler(request.getParameter("segmentID"));
Hl7textResultsData data = new Hl7textResultsData();
String multiLabId = data.getMatchingLabs(request.getParameter("segmentID"));


// check for errors printing
if (request.getAttribute("printError") != null && (Boolean) request.getAttribute("printError")){
%>
<script language="JavaScript">
    alert("The lab could not be printed due to an error. Please see the server logs for more detail.");
</script>
<%}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<html:base />
<title><%=handler.getPatientName()+" Lab Results"%></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="javascript" type="text/javascript"
	src="../../../share/javascript/Oscar.js"></script>
<link rel="stylesheet" type="text/css"
	href="../../../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<script language="JavaScript">
        function popupStart(vheight,vwidth,varpage,windowname) {
            var page = varpage;
            windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
            var popup=window.open(varpage, windowname, windowprops);
        }
        function getComment() {    
            var ret = true;
            var commentVal = prompt('<bean:message key="oscarMDS.segmentDisplay.msgComment"/>', '');
    
            if( commentVal == null )
                ret = false;
            else 
                document.acknowledgeForm.comment.value = commentVal;    
       
            return ret;
        }
        
        function printPDF(){
            document.acknowledgeForm.action="PrintPDF.do";
            document.acknowledgeForm.submit();
        }

        </script>

</head>

<body>
<!-- form forwarding of the lab -->
<form name="reassignForm" method="post" action="Forward.do"><input
	type="hidden" name="flaggedLabs"
	value="<%= request.getParameter("segmentID") %>" /> <input
	type="hidden" name="selectedProviders" value="" /> <input
	type="hidden" name="labType" value="HL7" /> <input type="hidden"
	name="labType<%= request.getParameter("segmentID") %>HL7"
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
					type="hidden" name="multiID" value="<%= multiLabId %>" /> <input
					type="hidden" name="providerNo"
					value="<%= request.getParameter("providerNo") %>" /> <input
					type="hidden" name="status" value="A" /> <input type="hidden"
					name="comment" value="" /> <input type="hidden" name="labType"
					value="HL7" /> <% if ( !ackFlag ) { %> <input type="submit"
					value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>"
					onclick="return getComment();"> <% } %> <input type="button"
					class="smallButton"
					value="<bean:message key="oscarMDS.index.btnForward"/>"
					onClick="popupStart(300, 400, '../../../oscarMDS/SelectProvider.jsp', 'providerselect')">
				<input type="button" value=" <bean:message key="global.btnClose"/> "
					onClick="window.close()"> <input type="button"
					value=" <bean:message key="global.btnPrint"/> "
					onClick="printPDF()"> <% if ( demographicID != null && !demographicID.equals("") && !demographicID.equalsIgnoreCase("null")){ %>
				<input type="button" value="Msg"
					onclick="popup(700,960,'../../../oscarMessenger/SendDemoMessage.do?demographic_no=<%=demographicID%>','msg')" />
				<input type="button" value="Tickler"
					onclick="popup(450,600,'../../../tickler/ForwardDemographicTickler.do?demographic_no=<%=demographicID%>','tickler')" />
				<% } %> <% if ( request.getParameter("searchProviderNo") != null ) { // null if we were called from e-chart%>
				<input type="button"
					value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> "
					onClick="popupStart(360, 680, '../../../oscarMDS/SearchPatient.do?labType=HL7&segmentID=<%= request.getParameter("segmentID")%>&name=<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>', 'searchPatientWindow')">
				<% } %> <span class="Field2"><i>Next Appointment: <oscar:nextAppt
					demographicNo="<%=demographicID%>" /></i></span></td>
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
					href="labDisplay.jsp?segmentID=<%=multiID[i]%>&multiID=<%=multiLabId%>&providerNo=<%=request.getParameter("providerNo")%>&searchProviderNo=<%=request.getParameter("searchProviderNo")%>">v<%= i+1 %></a>&#160;<%
                                                        }else{
                                                            %><a
					href="labDisplay.jsp?segmentID=<%=multiID[i]%>&multiID=<%=multiLabId%>&providerNo=<%=request.getParameter("providerNo")%>">v<%= i+1 %></a>&#160;<%
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
										<td nowrap>
										<div class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formPatientName" />: </strong></div>
										</td>
										<td nowrap>
										<div class="FieldData" nowrap="nowrap">
										<% if ( request.getParameter("searchProviderNo") == null ) { // we were called from e-chart%>
										<a href="javascript:window.close()"> <% } else { // we were called from lab module%>
										<a
											href="javascript:popupStart(360, 680, '../../../oscarMDS/SearchPatient.do?labType=HL7&segmentID=<%= request.getParameter("segmentID")%>&name=<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>', 'searchPatientWindow')">
										<% } %> <%=handler.getPatientName()%> </a></div>
										</td>
										<td colspan="2"></td>
									</tr>
									<tr>
										<td nowrap>
										<div class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formDateBirth" />: </strong></div>
										</td>
										<td nowrap>
										<div class="FieldData" nowrap="nowrap"><%=handler.getDOB()%>
										</div>
										</td>
										<td colspan="2"></td>
									</tr>
									<tr>
										<td nowrap>
										<div class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formAge" />: </strong></div>
										</td>
										<td nowrap>
										<div class="FieldData"><%=handler.getAge()%></div>
										</td>
										<td nowrap>
										<div class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formSex" />: </strong></div>
										</td>
										<td align="left" nowrap>
										<div class="FieldData"><%=handler.getSex()%></div>
										</td>
									</tr>
									<tr>
										<td nowrap>
										<div class="FieldData"><strong> <bean:message
											key="oscarMDS.segmentDisplay.formHealthNumber" /> </strong></div>
										</td>
										<td nowrap>
										<div class="FieldData" nowrap="nowrap"><%=handler.getHealthNum()%>
										</div>
										</td>
										<td colspan="2"></td>
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
										<div align="left" class="FieldData" nowrap="nowrap"><%=handler.getHomePhone()%>
										</div>
										</td>
									</tr>
									<tr>
										<td nowrap>
										<div align="left" class="FieldData"><strong><bean:message
											key="oscarMDS.segmentDisplay.formWorkPhone" />: </strong></div>
										</td>
										<td nowrap>
										<div align="left" class="FieldData" nowrap="nowrap"><%=handler.getWorkPhone()%>
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
										<div align="left" class="FieldData" nowrap="nowrap"><%=handler.getPatientLocation()%>
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
						<div class="FieldData" nowrap="nowrap"><%= handler.getServiceDate() %>
						</div>
						</td>
					</tr>
					<tr>
						<td>
						<div class="FieldData"><strong><bean:message
							key="oscarMDS.segmentDisplay.formReportStatus" />:</strong></div>
						</td>
						<td>
						<div class="FieldData" nowrap="nowrap"><%= ( (String) ( handler.getOrderStatus().equals("F") ? "Final" : handler.getOrderStatus().equals("C") ? "Corrected" : "Partial") )%>
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
						<div class="FieldData" nowrap="nowrap"><%= handler.getClientRef()%>
						</div>
						</td>
					</tr>
					<tr>
						<td>
						<div class="FieldData"><strong><bean:message
							key="oscarMDS.segmentDisplay.formAccession" />:</strong></div>
						</td>
						<td>
						<div class="FieldData" nowrap="nowrap"><%= handler.getAccessionNum()%>
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
							key="oscarMDS.segmentDisplay.formRequestingClient" />: </strong> <%= handler.getDocName()%>
						</div>
						</td>
						<%-- <td bgcolor="white">
                                    <div class="FieldData">
                                        <strong><bean:message key="oscarMDS.segmentDisplay.formReportToClient"/>: </strong>
                                            <%= No admitting Doctor for CML messages%>
                                    </div>
                                </td> --%>
						<td bgcolor="white" align="right">
						<div class="FieldData"><strong><bean:message
							key="oscarMDS.segmentDisplay.formCCClient" />: </strong> <%= handler.getCCDocs()%>

						</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td align="center" bgcolor="white" colspan="2">
				<%String[] multiID = multiLabId.split(",");
                                    ReportStatus report;          
                                    boolean startFlag = false;
                                    for (int j=multiID.length-1; j >=0; j--){
                                        ackList = ackData.getAcknowledgements(multiID[j]);                                        
                                        if (multiID[j].equals(request.getParameter("segmentID")))
                                            startFlag = true;                                                              
                                        if (startFlag)
                                            if (ackList.size() > 0){{%>
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
						<div class="FieldData"><!--center--> <% for (int i=0; i < ackList.size(); i++) { 
                                                                        report = (ReportStatus) ackList.get(i); %>
						<%= report.getProviderName() %> : <% String ackStatus = report.getStatus(); 
                                                                            if(ackStatus.equals("A")){
                                                                                ackStatus = "Acknowledged"; 
                                                                            }else if(ackStatus.equals("F")){
                                                                                ackStatus = "Filed but not Acknowledged";
                                                                            }else{
                                                                                ackStatus = "Not Acknowledged";
                                                                            }                                                                             
                                                                        %>
						<font color="red"><%= ackStatus %></font> <% if ( ackStatus.equals("Acknowledged") ) { %>
						<%= report.getTimestamp() %>, <%= ( report.getComment().equals("") ? "no comment" : "comment : "+report.getComment() ) %>
						<% } %> <br>
						<% } 
                                                                    if (ackList.size() == 0){
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
                        int l=0;
                        int linenum=0;
                        String highlight = "#E0E0FF";
                        
                        ArrayList headers = handler.getHeaders();
                        int OBRCount = handler.getOBRCount();
                        for(i=0;i<headers.size();i++){
                            linenum=0;
                        %>
		<table style="page-break-inside: avoid;" bgcolor="#003399" border="0"
			cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td colspan="4" height="7">&nbsp;</td>
			</tr>
			<tr>
				<td bgcolor="#FFCC00" width="300" valign="bottom">
				<div class="Title2"><%=headers.get(i)%></div>
				</td>
				<%--<td align="right" bgcolor="#FFCC00" width="100">&nbsp;</td>--%>
				<td width="9">&nbsp;</td>
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
					key="oscarMDS.segmentDisplay.formNew" /></td>
			</tr>

			<%

                            for ( j=0; j < OBRCount; j++){
                                
                                boolean obrFlag = false;
                                int obxCount = handler.getOBXCount(j);
                                for (k=0; k < obxCount; k++){ 
                                    String obxName = handler.getOBXName(j, k);
                                    if ( !handler.getOBXResultStatus(j, k).equals("DNS") && !obxName.equals("") && handler.getObservationHeader(j, k).equals(headers.get(i))){ // <<--  DNS only needed for MDS messages
                                        String obrName = handler.getOBRName(j);
                                        if(!obrFlag && !obrName.equals("") && !(obxName.contains(obrName) && obxCount < 2)){%>
			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>">
				<td valign="top" align="left"><%=obrName%></td>
				<td colspan="6">&nbsp;</td>
			</tr>
			<%obrFlag = true;
                                        }
                                        
                                        String lineClass = "NormalRes";
                                        String abnormal = handler.getOBXAbnormalFlag(j, k);
                                        if ( abnormal != null && ( abnormal.equals("A") || abnormal.startsWith("H")) ){
                                            lineClass = "AbnormalRes";
                                        }else if ( abnormal != null && abnormal.startsWith("L")){
                                            lineClass = "HiLoRes";
                                        }%>
			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="<%=lineClass%>">
				<td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a
					href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier=<%= handler.getOBXIdentifier(j, k) %>')"><%=obxName %></a></td>
				<td align="right"><%= handler.getOBXResult( j, k) %></td>
				<td align="center">
				<%if (handler.isOBXAbnormal( j, k)) {%> <%= handler.getOBXAbnormalFlag(j, k)%>
				<%}else{%> <%= "N"%> <%}%>
				</td>
				<td align="left"><%=handler.getOBXReferenceRange( j, k)%></td>
				<td align="left"><%=handler.getOBXUnits( j, k) %></td>
				<td align="center"><%= handler.getTimeStamp(j, k) %></td>
				<td align="center"><%= handler.getOBXResultStatus( j, k) %></td>
			</tr>

			<%for (l=0; l < handler.getOBXCommentCount(j, k); l++){%>
			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="NormalRes">
				<td valign="top" align="left" colspan="8"><pre
					style="margin: 0px 0px 0px 100px;"><%=handler.getOBXComment(j, k, l)%></pre></td>
			</tr>
			<%}
                                    }
                                }                                                             
                            //}                             
                                                        
                            //for ( j=0; j< OBRCount; j++){    
                                if (handler.getObservationHeader(j, 0).equals(headers.get(i))) {%>
			<%for (k=0; k < handler.getOBRCommentCount(j); k++){
                                    // the obrName should only be set if it has not been
                                    // set already which will only have occured if the
                                    // obx name is "" or if it is the same as the obr name
                                    if(!obrFlag && handler.getOBXName(j, 0).equals("")){%>
			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>">
				<td valign="top" align="left"><%=handler.getOBRName(j)%></td>
				<td colspan="6">&nbsp;</td>
			</tr>
			<%obrFlag = true;
                                    }%>
			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>"
				class="NormalRes">
				<td valign="top" align="left" colspan="8"><pre
					style="margin: 0px 0px 0px 100px;"><%=handler.getOBRComment(j, k)%></pre></td>
			</tr>
			<%}
                            }
                            }%>
		</table>
		<% // end for headers
                        }  // for i=0... (headers) %>

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
					onClick="popupStart(300, 400, '../../../oscarMDS/SelectProvider.jsp', 'providerselect')">
				<input type="button" value=" <bean:message key="global.btnClose"/> "
					onClick="window.close()"> <input type="button"
					value=" <bean:message key="global.btnPrint"/> "
					onClick="printPDF()"> <% if ( request.getParameter("searchProviderNo") != null ) { // we were called from e-chart %>
				<input type="button"
					value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> "
					onClick="popupStart(360, 680, '../../../oscarMDS/SearchPatient.do?labType=HL7&segmentID=<%= request.getParameter("segmentID")%>&name=<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>', 'searchPatientWindow')">
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
