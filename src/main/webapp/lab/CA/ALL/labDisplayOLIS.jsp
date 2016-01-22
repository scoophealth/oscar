<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@ page language="java" errorPage="../../../provider/errorpage.jsp" %>
<%@ page import="java.util.*,java.sql.*,org.oscarehr.olis.*,org.oscarehr.common.dao.PatientLabRoutingDao, org.oscarehr.util.SpringUtils, org.oscarehr.common.model.PatientLabRouting,oscar.oscarLab.ca.all.*,oscar.oscarLab.ca.all.util.*,oscar.oscarLab.ca.all.parsers.*,oscar.oscarLab.LabRequestReportLink,oscar.oscarMDS.data.ReportStatus,oscar.log.*,org.apache.commons.codec.binary.Base64" %>
<%@page import="org.oscarehr.util.AppointmentUtil" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProperties"%>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo"%>
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

<%
	String segmentID = request.getParameter("segmentID");
String originalSegmentID = segmentID;
String providerNo = request.getParameter("providerNo");
String searchProviderNo = request.getParameter("searchProviderNo");

boolean preview = oscar.Misc.getStr(request.getParameter("preview"), "").equals("true");
Long reqIDL = preview ? null : LabRequestReportLink.getIdByReport("hl7TextMessage",Long.valueOf(segmentID));
String reqID = reqIDL==null ? "" : reqIDL.toString();
reqIDL = preview ? null : LabRequestReportLink.getRequestTableIdByReport("hl7TextMessage",Long.valueOf(segmentID));
String reqTableID = reqIDL==null ? "" : reqIDL.toString();

PatientLabRoutingDao plrDao = preview ? null : (PatientLabRoutingDao) SpringUtils.getBean("patientLabRoutingDao");
PatientLabRouting plr = preview ? null : plrDao.findDemographicByLabId(Integer.valueOf(segmentID));
String demographicID = preview || plr.getDemographicNo() == null ? "" : plr.getDemographicNo().toString();


if(demographicID != null && !demographicID.equals("")){
    LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_HL7_LAB, segmentID, request.getRemoteAddr(),demographicID);
}else{
    LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_HL7_LAB, segmentID, request.getRemoteAddr());
}


boolean ackFlag = false;
ArrayList ackList = preview ? null : AcknowledgementData.getAcknowledgements(segmentID);
Factory f;
MessageHandler handlerMain;
String hl7 = "";

if (!preview) {

	if (ackList != null){
	    for (int i=0; i < ackList.size(); i++){
	        ReportStatus reportStatus = (ReportStatus) ackList.get(i);
	        if ( reportStatus.getProviderNo().equals(providerNo) && reportStatus.getStatus().equals("A") ){
	            ackFlag = true;
	            break;
	        }
	    }
	}
	handlerMain = Factory.getHandler(segmentID);
	hl7 = Factory.getHL7Body(segmentID);

} else {
	String resultUuid = oscar.Misc.getStr(request.getParameter("uuid"), "");
	handlerMain = OLISResultsAction.searchResultsMap.get(resultUuid);
}




OLISHL7Handler handler = null;
if (handlerMain instanceof OLISHL7Handler) {
	handler = (OLISHL7Handler) handlerMain;
}
else {
%> <jsp:forward page="labDisplay.jsp" /> <%
}
if (!preview && "true".equals(request.getParameter("showLatest"))) {

	String multiLabId = Hl7textResultsData.getMatchingLabs(segmentID);
	segmentID = multiLabId.split(",")[multiLabId.split(",").length - 1];
}

String multiLabId = preview ? "" :  Hl7textResultsData.getMatchingLabs(segmentID);

for (String tempId : multiLabId.split(",")) {
	if (tempId.equals(segmentID) || tempId.equals("")) { continue; }
	else {
		try {
			handler.importSourceOrganizations((OLISHL7Handler)Factory.getHandler(tempId));
		} catch (Exception e) {
			org.oscarehr.util.MiscUtils.getLogger().error("error",e);
		}
	}
}

// check for errors printing
if (request.getAttribute("printError") != null && (Boolean) request.getAttribute("printError")){
%>
<script language="JavaScript">
    alert("The lab could not be printed due to an error. Please see the server logs for more detail.");
</script>
<%}
%>
<%!
public String strikeOutInvalidContent(String content, String status) {
     return status != null && status.startsWith("W") ? "<s>" + content + "</s>" : content;
}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
	<!--  This is an OLIS lab display -->
    <head>
        <html:base/>
        <title><%=handler.getPatientName()+" Lab Results"%></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script language="javascript" type="text/javascript" src="../../../share/javascript/Oscar.js" ></script>
        <link rel="stylesheet" type="text/css" href="../../../share/css/OscarStandardLayout.css">
        <style type="text/css">
            <!--
* { word-wrap: break-word; }
.RollRes     { font-weight: 700; font-size: 8pt; color: white; font-family:
               Verdana, Arial, Helvetica }
.RollRes a:link { color: white }
.RollRes a:hover { color: white }
.RollRes a:visited { color: white }
.RollRes a:active { color: white }
.AbnormalRollRes { font-weight: 700; font-size: 8pt; color: red; font-family:
               Verdana, Arial, Helvetica }
.AbnormalRollRes a:link { color: red }
.AbnormalRollRes a:hover { color: red }
.AbnormalRollRes a:visited { color: red }
.AbnormalRollRes a:active { color: red }
.CorrectedRollRes { font-weight: 700; font-size: 8pt; color: yellow; font-family:
               Verdana, Arial, Helvetica }
.CorrectedRollRes a:link { color: yellow }
.CorrectedRollRes a:hover { color: yellow }
.CorrectedRollRes a:visited { color: yellow }
.CorrectedRollRes a:active { color: yellow }
.AbnormalRes { font-weight: bold; font-size: 8pt; color: red; font-family:
               Verdana, Arial, Helvetica }
.AbnormalRes a:link { color: red }
.AbnormalRes a:hover { color: red }
.AbnormalRes a:visited { color: red }
.AbnormalRes a:active { color: red }
.NormalRes   { font-weight: bold; font-size: 8pt; color: black; font-family:
               Verdana, Arial, Helvetica }
.NormalRes a:link { color: black }
.NormalRes a:hover { color: black }
.NormalRes a:visited { color: black }
.NormalRes a:active { color: black }
.HiLoRes     { font-weight: bold; font-size: 8pt; color: blue; font-family:
               Verdana, Arial, Helvetica }
.HiLoRes a:link { color: blue }
.HiLoRes a:hover { color: blue }
.HiLoRes a:visited { color: blue }
.HiLoRes a:active { color: blue }
.CorrectedRes { font-weight: bold; font-size: 8pt; color: #E000D0; font-family:
               Verdana, Arial, Helvetica }
.CorrectedRes a:link { color: #6da997 }
.CorrectedRes a:hover { color: #6da997 }
.CorrectedRes a:visited { color: #6da997 }
.CorrectedRes a:active { color: #6da997 }
.Field       { font-weight: bold; font-size: 8.5pt; color: black; font-family:
               Verdana, Arial, Helvetica }
div.Field a:link { color: black }
div.Field a:hover { color: black }
div.Field a:visited { color: black }
div.Field a:active { color: black }
.Field2      { font-weight: bold; font-size: 8pt; color: #ffffff; font-family:
               Verdana, Arial, Helvetica }
div.Field2   { font-weight: bold; font-size: 8pt; color: #ffffff; font-family:
               Verdana, Arial, Helvetica }
div.FieldData { font-weight: normal; font-size: 8pt; color: black; font-family:
               Verdana, Arial, Helvetica }
div.Field3   { font-weight: normal; font-size: 8pt; color: black; font-style: italic;
               font-family: Verdana, Arial, Helvetica }
div.Title    { font-weight: 800; font-size: 10pt; color: white; font-family:
               Verdana, Arial, Helvetica; padding-top: 4pt; padding-bottom:
               2pt }
div.Title a:link { color: white }
div.Title a:hover { color: white }
div.Title a:visited { color: white }
div.Title a:active { color: white }
div.Title2   { font-weight: bolder; font-size: 9pt; color: black; text-indent: 5pt;
               font-family: Verdana, Arial, Helvetica; padding: 10pt 15pt 2pt 2pt}
div.Title2 a:link { color: black }
div.Title2 a:hover { color: black }
div.Title2 a:visited { color: black }
div.Title2 a:active { color: black }
.Cell        { background-color: #9999CC; border-left: thin solid #CCCCFF;
               border-right: thin solid #6666CC;
               border-top: thin solid #CCCCFF;
               border-bottom: thin solid #6666CC }
.Cell2       { background-color: #376c95; border-left-style: none; border-left-width: medium;
               border-right-style: none; border-right-width: medium;
               border-top: thin none #bfcbe3; border-bottom-style: none;
               border-bottom-width: medium }
.Cell3       { background-color: #add9c7; border-left: thin solid #dbfdeb;
               border-right: thin solid #5d9987;
               border-top: thin solid #dbfdeb;
               border-bottom: thin solid #5d9987 }
.CellHdr     { background-color: #cbe5d7; border-right-style: none; border-right-width:
               medium; border-bottom-style: none; border-bottom-width: medium }
.Nav         { font-weight: bold; font-size: 8pt; color: black; font-family:
               Verdana, Arial, Helvetica }
.PageLink a:link { font-size: 8pt; color: white }
.PageLink a:hover { color: red }
.PageLink a:visited { font-size: 9pt; color: yellow }
.PageLink a:active { font-size: 12pt; color: yellow }
.PageLink    { font-family: Verdana }
.text1       { font-size: 8pt; color: black; font-family: Verdana, Arial, Helvetica }
div.txt1     { font-size: 8pt; color: black; font-family: Verdana, Arial }
div.txt2     { font-weight: bolder; font-size: 6pt; color: black; font-family: Verdana, Arial }
div.Title3   { font-weight: bolder; font-size: 12pt; color: black; font-family:
               Verdana, Arial }
.red         { color: red }
.text2       { font-size: 7pt; color: black; font-family: Verdana, Arial }
.white       { color: white }
.title1      { font-size: 9pt; color: black; font-family: Verdana, Arial }
div.Title4   { font-weight: 600; font-size: 8pt; color: white; font-family:
               Verdana, Arial, Helvetica }
            -->
        </style>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
		<script type="text/javascript">
		    jQuery.noConflict();
		</script>

<script type="text/javascript" src="<%= request.getContextPath() %>/share/jquery/jquery.form.js"></script>

        <script type="text/javaScript">
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
            document.acknowledgeForm.action="PrintOLISLab.do";
            document.acknowledgeForm.submit();
        }

	function linkreq(rptId, reqId) {
	    var link = "../../LinkReq.jsp?table=hl7TextMessage&rptid="+rptId+"&reqid="+reqId;
	    window.open(link, "linkwin", "width=500, height=200");
	}

    function sendToPHR(labId, demographicNo) {
        popup(300, 600, "<%=request.getContextPath()%>/phr/SendToPhrPreview.jsp?labId=" + labId + "&demographic_no=" + demographicNo, "sendtophr");
    }

    window.ForwardSelectedRows = function() {
		var query = jQuery(document.reassignForm).formSerialize();
		jQuery.ajax({
			type: "POST",
			url:  "<%=request.getContextPath()%>/oscarMDS/ReportReassign.do",
			data: query,
			success: function (data) {
				self.close();
			}
		});
	}

        </script>

    </head>

    <body style="width:800px">
        <!-- form forwarding of the lab -->
        <form name="reassignForm" method="post" action="Forward.do">
            <input type="hidden" name="flaggedLabs" value="<%= segmentID %>" />
            <input type="hidden" name="selectedProviders" value="" />
            <input type="hidden" name="labType" value="HL7" />
            <input type="hidden" name="labType<%= segmentID %>HL7" value="imNotNull" />
            <input type="hidden" name="providerNo" value="<%= providerNo %>" />
        </form>
        <form name="acknowledgeForm" method="post" action="../../../oscarMDS/UpdateStatus.do">
            <input type="hidden" name="originalSegmentID" value="<%=originalSegmentID%>" />
            <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="3">
                            <tr>
                                <td align="left" class="MainTableTopRowRightColumn" width="100%">
                                	<input type="hidden" name="labName" value="<%=handler.getAccessionNum() %>"/>
                                    <input type="hidden" name="segmentID" value="<%= segmentID %>"/>
                                    <input type="hidden" name="multiID" value="<%= multiLabId %>" />
                                    <input type="hidden" name="providerNo" value="<%= providerNo %>"/>
                                    <input type="hidden" name="status" value="A"/>
                                    <input type="hidden" name="comment" value=""/>
                                    <input type="hidden" name="labType" value="HL7"/>
                                    <% if ( !ackFlag ) { %>
                                    <input type="submit" value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>" onclick="return getComment();">
                                    <% } %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="popupStart(300, 400, '../../../oscarMDS/SelectProvider.jsp', 'providerselect')">
                                    <input type="button" value=" <bean:message key="global.btnClose"/> " onClick="window.close()">
                                    <input type="button" value=" <bean:message key="global.btnPrint"/> " onClick="printPDF()">
                                    <% if ( demographicID != null && !demographicID.equals("") && !demographicID.equalsIgnoreCase("null")){ %>
                                    <input type="button" value="Msg" onclick="popup(700,960,'../../../oscarMessenger/SendDemoMessage.do?demographic_no=<%=demographicID%>','msg')"/>
                                    <input type="button" value="Tickler" onclick="popup(450,600,'../../../tickler/ForwardDemographicTickler.do?docType=HL7&docId=<%= segmentID %>&demographic_no=<%=demographicID%>','tickler')"/>
                                    <% } %>

                                    <input type="button" value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> " onClick="popupStart(360, 680, '../../../oscarMDS/SearchPatient.do?labType=HL7&segmentID=<%= segmentID %>&name=<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>', 'searchPatientWindow')">

				    <input type="button" value="Req# <%=reqTableID%>" title="Link to Requisition" onclick="linkreq('<%=segmentID%>','<%=reqID%>');" />
                                    <span class="Field2"><i>Next Appointment: <%=AppointmentUtil.getNextAppointment(demographicID) %></i></span>
                                </td>
                            </tr>
                        </table>
                        <table width="100%" border="1" cellspacing="0" cellpadding="3" bgcolor="#9999CC" bordercolordark="#bfcbe3">
                            <%
                            if (multiLabId != null){
                                String[] multiID = multiLabId.split(",");
                                if (multiID.length > 1){
                                    %>
                                    <tr>
                                        <td class="Cell" colspan="2" align="middle">
                                            <div class="Field2">
                                                Version:&#160;&#160;
                                                <%
                                                for (int i=0; i < multiID.length; i++){
                                                    if (multiID[i].equals(segmentID)){
                                                        %>v<%= i+1 %>&#160;<%
                                                    }else{
                                                        if ( searchProviderNo != null ) { // null if we were called from e-chart
                                                            %><a href="labDisplay.jsp?segmentID=<%=multiID[i]%>&multiID=<%=multiLabId%>&providerNo=<%= providerNo %>&searchProviderNo=<%= searchProviderNo %>">v<%= i+1 %></a>&#160;<%
                                                        }else{
                                                            %><a href="labDisplay.jsp?segmentID=<%=multiID[i]%>&multiID=<%=multiLabId%>&providerNo=<%= providerNo %>">v<%= i+1 %></a>&#160;<%
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
                                <td align="middle" class="Cell">
                                    <div class="Field2">
                                        <bean:message key="oscarMDS.segmentDisplay.formDetailResults"/>
                                    </div>
                                </td>
                                <td align="middle" class="Cell">
                                    <div class="Field2">
                                        <bean:message key="oscarMDS.segmentDisplay.formResultsInfo"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td bgcolor="white" valign="top">
                                    <table valign="top" border="0" cellpadding="2" cellspacing="0" width="100%">
                                        <tr valign="top">
                                            <td valign="top" width="33%" align="left">
                                                <table width="100%" border="0" cellpadding="2" cellspacing="0" valign="top">
                                                    <tr>
                                                        <td valign="top" align="left">
                                                            <table valign="top" border="0" cellpadding="3" cellspacing="0" width="100%">
                                                                <tr>
                                                                    <td valign="top">
                                                                        <div class="FieldData">
                                                                            <strong>
                                                                                Ontario Health Number
                                                                            </strong>
                                                                        </div>
                                                                    </td>
                                                                    <td>
                                                                        <div class="FieldData">
                                                                            <%=handler.getHealthNum()%>
                                                                        </div>
                                                                    </td>

                                                                </tr>


                                                                <%
                                                                Set<String> patientIdentifiers = handler.getPatientIdentifiers();
                                                                for (String ident : patientIdentifiers) {
                                                                	// The health number is displayed in a seperate location.
                                                                	if (ident.equals("JHN")) { continue; }
                                                                	String[] values = handler.getPatientIdentifier(ident);
                                                                	String value = values[0];
                                                                	String attrib = values[1];
                                                                	String attribName=  null;
                                                                	if (attrib != null) {
                                                                		attribName = handler.getSourceOrganization(attrib);
                                                                	}

                                                                %>
                                                                <tr>
                                                                    <td valign="top">
                                                                        <div class="FieldData">
                                                                            <strong><%=handler.getNameOfIdentifier(ident)%>: </strong>
                                                                        </div>
                                                                    </td>
                                                                    <td >
                                                                        <div class="FieldData">
                                                                            <%= value %>
                                                                             <% if (attribName != null) { %>
	                                                                             <span style="margin-left:15px; font-size:8px; color:#333333;">
	                                                                             <%= attribName %> (Lab <%=attrib %>)
	                                                                             </span>
                                                                             <% } %>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                                <% } %>
                                                                <tr>
                                                                    <td valign="top">
                                                                        <div class="FieldData">
                                                                            <strong><bean:message key="oscarMDS.segmentDisplay.formPatientName"/>: </strong>
                                                                        </div>
                                                                    </td>
                                                                    <td>
                                                                        <div class="FieldData" >
                                                                            <% if ( searchProviderNo == null ) { // we were called from e-chart%>
                                                                            <a href="javascript:window.close()">
                                                                            <% } else { // we were called from lab module%>
                                                                            <a href="javascript:popupStart(360, 680, '../../../oscarMDS/SearchPatient.do?labType=HL7&segmentID=<%= segmentID %>&name=<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>', 'searchPatientWindow')">
                                                                                <% } %>
                                                                                <%=handler.getPatientName()%>
                                                                            </a>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td valign="top">
                                                                        <div class="FieldData">
                                                                            <strong><bean:message key="oscarMDS.segmentDisplay.formDateBirth"/>: </strong>
                                                                        </div>
                                                                    </td>
                                                                    <td >
                                                                        <div class="FieldData">
                                                                            <%=handler.getDOB()%>
                                                                        </div>
                                                                    </td>

                                                                </tr>
                                                                <tr>
                                                                    <td valign="top">
                                                                        <div class="FieldData">
                                                                            <strong><bean:message key="oscarMDS.segmentDisplay.formAge"/>: </strong>
                                                                        </div>
                                                                    </td>
                                                                    <td>
                                                                        <div class="FieldData">
                                                                            <%=handler.getAge()%>
                                                                        </div>
                                                                    </td>
                                                                 </tr>
                                                                 <tr>
                                                                    <td valign="top">
                                                                        <div class="FieldData">
                                                                            <strong><bean:message key="oscarMDS.segmentDisplay.formSex"/>: </strong>
                                                                        </div>
                                                                    </td>
                                                                    <td align="left">
                                                                        <div class="FieldData">
                                                                            <%=handler.getSex()%>
                                                                        </div>
                                                                    </td>
                                                                </tr>

                                                                <%!

                                                                public boolean stringIsNullOrEmpty(String s) {
                                                                	return s == null || s.trim().length() == 0;
                                                                }
                                                                public String displayAddressFieldIfNotNullOrEmpty(HashMap<String,String> address, String key) {
                                                                	return displayAddressFieldIfNotNullOrEmpty(address, key, true);
                                                                }
                                                                public String displayAddressFieldIfNotNullOrEmpty(HashMap<String,String> address, String key, boolean newLine) {
                                                                	String value = address.get(key);
                                                                	if (stringIsNullOrEmpty(value)) { return ""; }
                                                                	String result = value + (newLine ? "<br />" : "");
                                                                	return result;
                                                                }
                                                                %>
                                                                <%
                                                                ArrayList<HashMap<String,String>> addresses = handler.getPatientAddresses();
                                                                for(HashMap<String, String> address : addresses) {
                                                                	String city = displayAddressFieldIfNotNullOrEmpty(address, "City", false);
                                                                	String province = displayAddressFieldIfNotNullOrEmpty(address, "Province", false);
                                                                %>
                                                                <tr>
                                                                    <td valign="top">
                                                                        <div align="left" class="FieldData">
                                                                            <strong> <%=address.get("Address Type")%></strong>
                                                                        </div>
                                                                    </td>
                                                                    <td>
                                                                        <div align="left" class="FieldData">
                                                                            <%= displayAddressFieldIfNotNullOrEmpty(address, "Street Address") %>
                                                                            <%= displayAddressFieldIfNotNullOrEmpty(address, "Other Designation") %>
                                                                            <%= displayAddressFieldIfNotNullOrEmpty(address, "Postal Code") %>
                                                                            <%= city + ("".equals(city) || "".equals(province) ? "" : ", ") + province + ("".equals(city) && "".equals(province) ? "" : "<br/>") %>
                                                                            <%= displayAddressFieldIfNotNullOrEmpty(address, "Country") %>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                                <% } %>
                                                                <%
                                                                ArrayList<HashMap<String,String>> homePhones = handler.getPatientHomeTelecom();
                                                                if (homePhones.size() > 0) {
                                                                %>
                                                                <tr><td colspan="2"><fieldset><legend>Home</legend><table>
                                                                <%
                                                                }
                                                                for(HashMap<String, String> homePhone : homePhones) {
                                                                %>
                                                                 <tr>
                                                                    <td valign="top">
                                                                        <div align="left" class="FieldData">
                                                                            <strong> <%=homePhone.get("equipType")%></strong>
                                                                        </div>
                                                                    </td>
                                                                    <td>
                                                                        <div align="left" class="FieldData">
                                                                        	<%
                                                                        	if (homePhone.get("email") != null) {
                                                                        	%>
                                                                        		<%=homePhone.get("email")%>
                                                                       		<%
                                                                       		} else {

                                                                       			String countryCode = homePhone.get("countryCode");
                                                                       			if (stringIsNullOrEmpty(countryCode)) {
                                                                       				countryCode = "";
                                                                       			}

                                                                       			String localNumber = homePhone.get("localNumber");
                                                                       			if (!stringIsNullOrEmpty(localNumber) && localNumber.length() > 4) {
                                                                       				localNumber = localNumber.substring(0,3) + "-" + localNumber.substring(3);
                                                                       			}
                                                                       			else { localNumber = ""; }
                                                                       			String areaCode = homePhone.get("areaCode");
                                                                       			if (!stringIsNullOrEmpty(areaCode)) {
                                                                       				areaCode = " ("+areaCode+") ";
                                                                       			}
                                                                       			else { areaCode = ""; }
                                                                       			String extension = homePhone.get("extension");
                                                                       			if (!stringIsNullOrEmpty(extension)) {
                                                                       				extension = " x" + extension;
                                                                       			}
                                                                       			else { extension = ""; }
                                                                    		%>
                                                                    			<%= countryCode + areaCode + localNumber + extension %>
                                                                    		<%
                                                                       		}
                                                                       		%>
                                                                            <span style="margin-left:15px; font-size:8px; color:#333333;">
						                                                    	<%=homePhone.get("useCode")%>
						                                                    </span>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                                <% }
                                                                if (homePhones.size() > 0) {
                                                                %>
                                                                </table></fieldset></td></tr>
                                                                <%
                                                                }
                                                                %>
                                                                <%
                                                                ArrayList<HashMap<String,String>> workPhones = handler.getPatientWorkTelecom();
                                                                if (workPhones.size() > 0) {
                                                                %>
                                                                <tr><td colspan="2"><fieldset><legend>Work</legend><table>
                                                                <%
                                                                }
                                                                for(HashMap<String, String> workPhone : workPhones) {
                                                                %>
                                                                 <tr>
                                                                    <td valign="top">
                                                                        <div align="left" class="FieldData">
                                                                            <strong> <%=workPhone.get("equipType")%></strong>
                                                                        </div>
                                                                    </td>
                                                                    <td>
                                                                        <div align="left" class="FieldData">
                                                                        	<%
                                                                        	if (workPhone.get("email") != null) {
                                                                        	%>
                                                                        		<%=workPhone.get("email")%>
                                                                       		<%
                                                                       		} else {

                                                                       			String countryCode = workPhone.get("countryCode");
                                                                       			if (stringIsNullOrEmpty(countryCode)) {
                                                                       				countryCode = "";
                                                                       			}

                                                                       			String localNumber = workPhone.get("localNumber");
                                                                       			if (!stringIsNullOrEmpty(localNumber) && localNumber.length() > 4) {
                                                                       				localNumber = localNumber.substring(0,3) + "-" + localNumber.substring(3);
                                                                       			}
                                                                       			else { localNumber = ""; }
                                                                       			String areaCode = workPhone.get("areaCode");
                                                                       			if (!stringIsNullOrEmpty(areaCode)) {
                                                                       				areaCode = " ("+areaCode+") ";
                                                                       			}
                                                                       			else { areaCode = ""; }
                                                                       			String extension = workPhone.get("extension");
                                                                       			if (!stringIsNullOrEmpty(extension)) {
                                                                       				extension = " x" + extension;
                                                                       			}
                                                                       			else { extension = ""; }
                                                                    		%>
                                                                    			<%= countryCode + areaCode + localNumber + extension %>
                                                                    		<%
                                                                       		}
                                                                       		%>
                                                                            <span style="margin-left:15px; font-size:8px; color:#333333;">
						                                                    	<%=workPhone.get("useCode")%>
						                                                    </span>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                                <% }
                                                                if (workPhones.size() > 0) {
                                                                %>
                                                                </table></fieldset></td></tr>
                                                                <%
                                                                }
                                                                %>
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
                                        <%--
                                        <tr>
                                            <td>
                                                <div class="FieldData">
                                                    <strong><bean:message key="oscarMDS.segmentDisplay.formDateService"/>:</strong>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="FieldData">
                                                    <%= handler.getServiceDate() %>
                                                </div>
                                            </td>
                                        </tr>
                                         --%>
                                        <tr>
                                            <td valign="top">
                                                <div class="FieldData">
                                                    <strong><bean:message key="oscarMDS.segmentDisplay.formReportStatus"/>:</strong>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="FieldData">
                                                    <%= ( (String) ( handler.getOrderStatus().equals("F") ? "Final" : handler.getOrderStatus().equals("C") ? "Corrected" : "Partial") )%>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td valign="top">
                                                <div class="FieldData">
                                                    <strong>Order Id:</strong>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="FieldData">
                                                    <%= handler.getAccessionNum()%>
                                                    <span style="margin-left:15px; font-size:8px; color:#333333;">
                                                    <%= handler.getAccessionNumSourceOrganization() %>
                                                    </span>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td valign="top">
                                                <div class="FieldData">
                                                    <strong>Order Date:</strong>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="FieldData">
                                                    <%= handler.getOrderDate()%>
                                                </div>
                                            </td>
                                        </tr>
                                        <% String lastUpdate = handler.getLastUpdateInOLIS();
                                           if (!stringIsNullOrEmpty(lastUpdate)) {
                                        %>
                                        <tr>
                                            <td valign="top">
                                                <div class="FieldData">
                                                    <strong>Last Updated in OLIS:</strong>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="FieldData">
                                                    <%= lastUpdate %>
                                                </div>
                                            </td>
                                        </tr>
                                        <% }
                                           String specimenReceived = handler.getSpecimenReceivedDateTime();
                                           if (!stringIsNullOrEmpty(specimenReceived)) {
                                        %>
                                         <tr>
                                            <td valign="top">
                                                <div class="FieldData">
                                                    <strong>Specimen Received:</strong>
                                                </div>
                                            </td>
                                            <td valign="top">
                                                <div class="FieldData">
                                                    <%= specimenReceived %>
                                                </div>
                                            </td>
                                        </tr>
                                        <%
                                           }
                                           if (!"".equals(handler.getOrderingFacilityName())) {
                                        %>
                                        <tr>

                                            <td colspan="2">
                                                <div class="FieldData">
                                                    <strong>Ordering Facility:</strong>
                                                </div>
                                            </td>
                                            </tr>
                                        <tr>
                                            <td colspan="2">
                                                <div class="FieldData">
                                                    <%= handler.getOrderingFacilityName() %>
                                                    <%
                                                    HashMap<String,String> address = handler.getOrderingFacilityAddress();
                                                    if (address != null && address.size() > 0) {
                                                    	String city = displayAddressFieldIfNotNullOrEmpty(address, "City", false);
                                                    	String province = displayAddressFieldIfNotNullOrEmpty(address, "Province", false);
                                                    %>
                                                    <br/>
                                                    <strong>Address:</strong><br/>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Street Address") %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Other Designation") %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Postal Code") %>
                                                    <%= city + ("".equals(city) || "".equals(province) ? "" : ", ") + province + ("".equals(city) && "".equals(province) ? "" : "<br/>") %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Country") %>
                                                    <% } %>
                                                </div>
                                            </td>
                                        </tr>
                                        <% } %>
                                        <tr>
                                            <td bgcolor="white" colspan="2">
                                                <div class="FieldData">
                                                    <strong>Ordering Provider: </strong>
                                                </div>
                                             </td>

                                        </tr>
                                        <tr>
                                            <td colspan="2">
                                                <div class="FieldData">
                                                    <%= handler.getDocName()%>
                                                    <%
                                                    HashMap<String,String> address = handler.getOrderingProviderAddress();
                                                    if (address != null && address.size() > 0) {
                                                    	String city = displayAddressFieldIfNotNullOrEmpty(address, "City", false);
                                                    	String province = displayAddressFieldIfNotNullOrEmpty(address, "Province", false);
                                                    %>
                                                    <br/>
                                                    <strong>Address:</strong><br/>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Street Address") %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Other Designation") %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Postal Code") %>
                                                    <%= city + ("".equals(city) || "".equals(province) ? "" : ", ") + province + ("".equals(city) && "".equals(province) ? "" : "<br/>") %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Country", false) %>
                                                    <% } %>
                                                    <%
                                                                ArrayList<HashMap<String,String>> phones = handler.getOrderingProviderPhones();
                                                                for(HashMap<String, String> phone : phones) {
                                                                %>

                                                                    	<br />
                                                                        <strong> <%=phone.get("useCode")%></strong>
                                                                        <br/>

                                                                        	<%
                                                                        	if (phone.get("email") != null) {
                                                                        	%>
                                                                        		<%=phone.get("email")%>
                                                                       		<%
                                                                       		} else {

                                                                       			String countryCode = phone.get("countryCode");
                                                                       			if (stringIsNullOrEmpty(countryCode)) {
                                                                       				countryCode = "";
                                                                       			}

                                                                       			String localNumber = phone.get("localNumber");
                                                                       			if (!stringIsNullOrEmpty(localNumber) && localNumber.length() > 4) {
                                                                       				localNumber = localNumber.substring(0,3) + "-" + localNumber.substring(3);
                                                                       			}
                                                                       			else { localNumber = ""; }
                                                                       			String areaCode = phone.get("areaCode");
                                                                       			if (!stringIsNullOrEmpty(areaCode)) {
                                                                       				areaCode = " ("+areaCode+") ";
                                                                       			}
                                                                       			else { areaCode = ""; }
                                                                       			String extension = phone.get("extension");
                                                                       			if (!stringIsNullOrEmpty(extension)) {
                                                                       				extension = " x" + extension;
                                                                       			}
                                                                       			else { extension = ""; }
                                                                    		%>
                                                                    			<%= countryCode + areaCode + localNumber + extension %>
                                                                    		<%
                                                                       		}
                                                                       		%>

                                                                <% }

                                                                %>
                                                </div>
                                            </td>
                                        </tr>
                                        <% if (!"".equals(handler.getAttendingProviderName())) { %>
                                        <tr>

                                            <td colspan="2">
                                                <div class="FieldData">
                                                    <strong>Attending Provider:</strong>
                                                </div>
                                            </td>
                                            </tr>
                                        <tr>
                                            <td colspan="2">
                                                <div class="FieldData">
                                                    <%= handler.getAttendingProviderName() %>
                                                </div>
                                            </td>
                                        </tr>
                                        <% }%>
                                        <% if (!"".equals(handler.getAdmittingProviderName())) { %>
                                        <tr>

                                            <td colspan="2">
                                                <div class="FieldData">
                                                    <strong>Admitting Provider:</strong>
                                                </div>
                                            </td>
                                            </tr>
                                        <tr>
                                            <td colspan="2">
                                                <div class="FieldData">
                                                    <%= handler.getAdmittingProviderName() %>
                                                </div>
                                            </td>
                                        </tr>
                                        <% }%>
                                         <%
                               String primaryFacility = handler.getPerformingFacilityName();
                               String reportingFacility = handler.getReportingFacilityName();
                               if (!stringIsNullOrEmpty(primaryFacility)) {
                            %>
                                        <tr>

                                            <td colspan="2">
                                                <div class="FieldData">
                                                    <strong>Performing <%=(primaryFacility.equals(reportingFacility) ? "and Reporting" : "")%> Facility:</strong>
                                                </div>
                                            </td>
                                            </tr>
                                        <tr>
                                            <td colspan="2">
                                                <div class="FieldData">
                                                    <%= handler.getPerformingFacilityName() %>
                                                    <%
                                                     address = handler.getPerformingFacilityAddress();
                                                    if (address != null && address.size() > 0) {
                                                    	String city = displayAddressFieldIfNotNullOrEmpty(address, "City", false);
                                                    	String province = displayAddressFieldIfNotNullOrEmpty(address, "Province", false);
                                                    %>
                                                    <br/>
                                                    <strong>Address:</strong><br/>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Street Address") %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Other Designation") %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Postal Code") %>
                                                    <%= city + ("".equals(city) || "".equals(province) ? "" : ", ") + province + ("".equals(city) && "".equals(province) ? "" : "<br/>") %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Country") %>
                                                    <% } %>
                                                </div>
                                            </td>
                                        </tr>
                                        <% } %>

                                          <%

                               if (!stringIsNullOrEmpty(reportingFacility) && !reportingFacility.equals(primaryFacility)) {
                            %>
                                        <tr>

                                            <td colspan="2">
                                                <div class="FieldData">
                                                    <strong>Reporting Facility:</strong>
                                                </div>
                                            </td>
                                            </tr>
                                        <tr>
                                            <td colspan="2">
                                                <div class="FieldData">
                                                    <%= reportingFacility %>
                                                    <%
                                                     address = handler.getReportingFacilityAddress();
                                                    if (address != null && address.size() > 0) {
                                                    	String city = displayAddressFieldIfNotNullOrEmpty(address, "City", false);
                                                    	String province = displayAddressFieldIfNotNullOrEmpty(address, "Province", false);
                                                    %>
                                                    <br/>
                                                    <strong>Address:</strong><br/>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Street Address") %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Other Designation") %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Postal Code") %>
                                                    <%= city + ("".equals(city) || "".equals(province) ? "" : ", ") + province + ("".equals(city) && "".equals(province) ? "" : "<br/>") %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Country") %>
                                                    <% } %>
                                                </div>
                                            </td>
                                        </tr>
                                        <% } %>
                                    </table>
                                </td>
                            </tr>

                            <tr>
                                <td bgcolor="white" colspan="2">
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0" bordercolor="#CCCCCC">
                                        <tr>
                                            <%-- <td bgcolor="white">
                                    <div class="FieldData">
                                        <strong><bean:message key="oscarMDS.segmentDisplay.formReportToClient"/>: </strong>
                                            <%= No admitting Doctor for CML messages%>
                                    </div>
                                </td> --%>
                                            <td bgcolor="white" align="right" colspan="2">
                                                <div class="FieldData">
                                                    <strong><bean:message key="oscarMDS.segmentDisplay.formCCClient"/>: </strong>
                                                    <%= handler.getCCDocs()%>

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
                                        ackList = AcknowledgementData.getAcknowledgements(multiID[j]);
                                        if (multiID[j].equals(segmentID))
                                            startFlag = true;
                                        if (startFlag)
                                            if (ackList.size() > 0){{%>
                                                <table width="100%" height="20" cellpadding="2" cellspacing="2">
                                                    <tr>
                                                        <% if (multiID.length > 1){ %>
                                                            <td align="center" bgcolor="white" width="20%" valign="top">
                                                                <div class="FieldData">
                                                                    <b>Version:</b> v<%= j+1 %>
                                                                </div>
                                                            </td>
                                                            <td align="left" bgcolor="white" width="80%" valign="top">
                                                        <% }else{ %>
                                                            <td align="center" bgcolor="white">
                                                        <% } %>
                                                            <div class="FieldData">
                                                                <!--center-->
                                                                    <% for (int i=0; i < ackList.size(); i++) {
                                                                        report = (ReportStatus) ackList.get(i); %>
                                                                        <%= report.getProviderName() %> :

                                                                        <% String ackStatus = report.getStatus();
                                                                            if(ackStatus.equals("A")){
                                                                                ackStatus = "Acknowledged";
                                                                            }else if(ackStatus.equals("F")){
                                                                                ackStatus = "Filed but not Acknowledged";
                                                                            }else{
                                                                                ackStatus = "Not Acknowledged";
                                                                            }
                                                                        %>
                                                                        <font color="red"><%= ackStatus %></font>
                                                                        <% if ( ackStatus.equals("Acknowledged") ) { %>
                                                                            <%= report.getTimestamp() %>,
                                                                            <%= ( report.getComment().equals("") ? "no comment" : "comment : "+report.getComment() ) %>
                                                                        <% } %>
                                                                        <br>
                                                                    <% }
                                                                    if (ackList.size() == 0){
                                                                        %><font color="red">N/A</font><%
                                                                    }
                                                                    %>
                                                                <!--/center-->
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>

                                            <%}
                                        }
                                    }%>
                                </td>
                            </tr>
                            <tr>
                                <td bgcolor="white" colspan="2">
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0" bordercolor="#CCCCCC">
                                        <tr>
                                            <td bgcolor="white">
                                                <div class="FieldData">
                                                <% if (handler.isReportBlocked()) { %>
                                                <%
                                                boolean hasBlockedTest=false;
                                                for(int i=0;i<handler.getHeaders().size();i++) {
                                                	int obr = handler.getMappedOBR(i);
                                                	if(handler.isOBRBlocked(obr)) {
                                                		hasBlockedTest=true;
                                                		break;
                                                	}
                                                }
                                                if(hasBlockedTest) {
                                                %>
                                                	<span style="color:red; font-weight:bold">Do Not Disclose Without Explicit Patient Consent</span>
                                                	<br/>
                                                <% } } %>

                                                    <strong>Report Comments: </strong>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td bgcolor="white" align="left">
                                                <div class="FieldData" style="width:700px;">

                                                    <% for (int i = 0, j = handler.getReportCommentCount(); i < j; i++) { %>
                                                    <span style="margin-left:15px; width: 700px; word-wrap: break-word;">
                                                    <%= (i > 0 ? "<br/>" : "") + handler.getReportComment(i) %>
                                                    </span>
                                                    <span style="margin-left:15px; font-size:8px; color:#333333;">
                                                    <%= handler.getReportSourceOrganization(i) %>
                                                    </span>
                                                    <% } %>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>


                        <% int i=0;

                        int obx = 0;
                        int l=0;
                        int linenum=0;
                        String highlight = "#E0E0FF";
                        ArrayList headers = handler.getHeaders();
                        int OBRCount = handler.getOBRCount();
                        String category = "";
                        String newCategory = "";

                        int obr;

                        for(i=0;i<headers.size();i++) {
                        	obr = handler.getMappedOBR(i);
                            linenum = obr + 1;
                            if (handler.isChildOBR(linenum)) {
                            	continue;
                            }
                        %>
                        <table style="page-break-inside:avoid;" bgcolor="#003399" border="0" cellpadding="0" cellspacing="0" width="100%">
                            <%
                            	newCategory = handler.getOBRCategory(obr);
                            	if (!category.equals(newCategory)) {
                            		if (i > 0) {
                            		%>
                            <tr>
                                <td colspan="4" height="7">&nbsp;</td>
                            </tr>
                            <%
                            		}
                         	%>

                        	<tr>
                        		 <td colspan="7" align="center" bgcolor="#FFCC00"><span style="font-size: large;"><%=newCategory%></span><td>
                        	</tr>
                            		<%
                            	}
                            	category = newCategory;
                            %>
                            <tr>
                                <td colspan="4" height="7">&nbsp;</td>
                            </tr>
                            <tr>
                                <td bgcolor="#FFCC00" width="300" valign="top">
                                    <div class="Title2">
                                        <%=headers.get(obr)%>
                                        <%
                                        String poc = handler.getPointOfCare(obr);
                                        if (!stringIsNullOrEmpty(poc)) {
                                        %>
                                        <br/>
                                        <span style="font-size:8px; color:#333333;">Test performed at patient location</span>
                                        <% } %>
                                        <%
                                        boolean blocked = handler.isOBRBlocked(obr);
                                        if (blocked) {
                                        %>
                                        <span style="font-size:8px; color:red;">(Do Not Disclose Without Explicit Patient Consent)</span>
                                        <% } %>
                                    </div>
                                </td>
                                <%--<td align="right" bgcolor="#FFCC00" width="100">&nbsp;</td>--%>
                                <td  bgcolor="#FFCC00" width="500">
                                	<div class="Title2">
                                	<table>
                                		<% if (!handler.getObrSpecimenSource(obr).equals("")) { %>
                                			<tr> <td> Specimen Source: </td><td><%=handler.getObrSpecimenSource(obr) %></td> </tr>
                                		<% } %>
                                		<tr> <td> Request Status: </td><td> <%=handler.getObrStatus(obr) %></td></tr>
                                	</table>

                                	</div>
                                </td>
                                <td width="9">&nbsp;</td>
                                <td width="*">&nbsp;</td>
                            </tr>
                            <tr>
                            <%--

                              Collection Date/Time
                              Specimen Collected By
                              Collection Volume
                              No. of Sample Containers


                             --%>
                             <%
                             HashMap<String,String> parameters = new HashMap<String,String>();

                             String collectionDateTime = handler.getCollectionDateTime(obr);
                             String specimenCollectedBy = handler.getSpecimenCollectedBy(obr);
                             String collectionVolume = handler.getCollectionVolume(obr);
							 String noOfSampleContainers = handler.getNoOfSampleContainers(obr);

							 if (!stringIsNullOrEmpty(collectionDateTime)) {
								 parameters.put("Collection Date/Time", collectionDateTime);
							 }

							 if (!stringIsNullOrEmpty(specimenCollectedBy)) {
								 parameters.put("Specimen Collected By", specimenCollectedBy);
							 }

							 if (!stringIsNullOrEmpty(collectionVolume)) {
								 parameters.put("Collection Volume", collectionVolume);
							 }

							 if (!stringIsNullOrEmpty(noOfSampleContainers)) {
								 parameters.put("No. of Sample Containers", noOfSampleContainers);
							 }

							 for (String key : parameters.keySet()) {

							 }
                             %>
								<td bgcolor="#FFCC00" colspan="2">
								<table width="100%">
									<tr><% if (!stringIsNullOrEmpty(collectionDateTime)) { %><th width="50%"> Collection Date/Time </th><% } %><% if (!stringIsNullOrEmpty(specimenCollectedBy)) { %><th width="50%">Specimen Collected By</th><% } %></tr>
									<tr><% if (!stringIsNullOrEmpty(collectionDateTime)) { %><td align="center"><%=collectionDateTime%></td><% } %><% if (!stringIsNullOrEmpty(specimenCollectedBy)) { %><td align="center"><%=specimenCollectedBy%></td><% } %></tr>
									<tr><% if (!stringIsNullOrEmpty(collectionVolume)) { %><th> Collection Volume </th><% } %><% if (!stringIsNullOrEmpty(noOfSampleContainers)) { %><th>No. of Sample Containers</th><% } %></tr>
									<tr><% if (!stringIsNullOrEmpty(collectionVolume)) { %><td align="center"><%=collectionVolume%></td><% } %><% if (!stringIsNullOrEmpty(noOfSampleContainers)) { %><td align="center"><%=noOfSampleContainers%></td><% } %></tr>
								</table>

								</td>
								<td width="9">&nbsp;</td>
                                <td width="*">&nbsp;</td>
							</tr>
							<%
							String performingFacility = handler.getOBRPerformingFacilityName(obr);
							if (!primaryFacility.equals(performingFacility) && !performingFacility.equals("")) {

                            %>
                                        <tr>
                                            <td bgcolor="#FFCC00">
                                                <div class="FieldData">
                                                    <strong>Performing Facility:</strong>
                                                </div>
                                            </td>
                                            <td  bgcolor="#FFCC00">
                                                <div class="FieldData">
                                                  <strong>Address:</strong><br/>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                        	<td bgcolor="#FFCC00" valign="top">
                                        		<div class="FieldData">
                                        			  <%= performingFacility %>
                                                </div>
                                            </td>
                                            <td bgcolor="#FFCC00" valign="top">
                                        		<div class="FieldData">
													<%
                                                    address = handler.getPerformingFacilityAddress(obr);
                                                    if (address != null && address.size() > 0) {
                                                    	String city = displayAddressFieldIfNotNullOrEmpty(address, "City", false);
                                                    	String province = displayAddressFieldIfNotNullOrEmpty(address, "Province", false);
                                                    %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Street Address") %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Other Designation") %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Postal Code") %>
                                                    <%= city + ("".equals(city) || "".equals(province) ? "" : ", ") + province + ("".equals(city) && "".equals(province) ? "" : "<br/>") %>
                                                    <%= displayAddressFieldIfNotNullOrEmpty(address, "Country") %>
                                                    <% } %>
                                                </div>
                                            </td>
                                        </tr>
                                        <% } %>
                                        <%
                                        String diagnosis = handler.getDiagnosis(obr);
                                        if (!stringIsNullOrEmpty(diagnosis)) {
                                        %>
                                        <tr>
                                            <td bgcolor="#FFCC00" colspan="2">
                                                <div class="FieldData">
                                                    <strong>Diagnosis:</strong><br/>
                                                    <%=diagnosis%>
                                                </div>
                                            </td>
                                        </tr>
                                        <% } %>
                        </table>

                        <table width="100%" border="0" cellspacing="0" cellpadding="2" bgcolor="#CCCCFF" bordercolor="#9966FF" bordercolordark="#bfcbe3" name="tblDiscs" id="tblDiscs">
                            <tr class="Field2">
                                <td width="25%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formTestName"/></td>
                                <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formResult"/></td>
                                <td width="5%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formAbn"/></td>
                                <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formReferenceRange"/></td>
                                <td width="10%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formUnits"/></td>
                                <td width="15%" align="middle" valign="bottom" class="Cell">
                                 <%--
                                 <bean:message key="oscarMDS.segmentDisplay.formDateTimeCompleted"/>
                                 --%>
                                </td>
                                <td width="6%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formNew"/></td>
                            </tr>

                            <%



                                boolean obrFlag = false;
                                int obxCount = handler.getOBXCount(obr);
                                String collectorsComment = handler.getCollectorsComment(obr); // TODO: get collector attribution
                                if (collectorsComment != null && !collectorsComment.equals("")) {
                                	 %>
                                     <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
                                         <td valign="top" align="left" colspan="7"><div style="margin-left:15px; width:700px">
                                         <strong>Comments:</strong> <%=handler.formatString(collectorsComment)%>
                                         <span style="margin-left:15px;font-size:8px; color:#333333;"><%=handler.getCollectorsCommentSourceOrganization(obr)%></span>
                                         </div></td>
                                     </tr>
                                     <%
                                }

                                if (handler.getObservationHeader(obr, 0).equals(headers.get(obr))) {
                                	int cc = handler.getOBRCommentCount(obr);
                                	for (int comment = 0; comment < cc; comment++){
                                    // the obrName should only be set if it has not been
                                    // set already which will only have occured if the
                                    // obx name is "" or if it is the same as the obr name
                                    String obxNN = handler.getOBXName(obr,0);
                                    if(!obrFlag && obxNN.equals("")){%>
                                        <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" >
                                            <td valign="top" align="left"><%=handler.getOBRName(comment)%></td>
                                            <td valign="top" align="left"><%=handler.getObrSpecimenSource(comment) %></td>
                                            <td colspan="5">&nbsp;</td>
                                        </tr>
                                        <%obrFlag = true;
                                    }

                                    String obrComment = handler.getOBRComment(obr, comment);
                                    String sourceOrg = handler.getOBRSourceOrganization(obr, comment);
                                    %>
                                <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
                                    <td valign="top" align="left" colspan="7">
                                    <div  style="margin-left:15px;width: 700px;">
                                    	<%=obrComment%>
                                    	<span style="margin-left:15px;font-size:8px; color:#333333;"><%=sourceOrg%></span>
                                   	</div>
                                    </td>
                                </tr>
                                <%

                                }//end for k=0
                            	}//end if handler.getObservation..

                                for (int k=0; k < obxCount; k++){
                                	obx = handler.getMappedOBX(obr, k);
                                    String obxName = handler.getOBXName(obr, obx);
                                    boolean b1=false, b2=false, b3=false;

                                    boolean fail = true;
                                    try {
                                    b1 = !handler.getOBXResultStatus(obr, obx).equals("DNS");
                                    b2 = !obxName.equals("");
                                    String currheader = (String) headers.get(obr);
                                    String obsHeader = handler.getObservationHeader(obr, obx);
                                    b3 = handler.getObservationHeader(obr, obx).equals(headers.get(obr));
                                    fail = false;





                                    } catch (Exception e){
                                    	//logger.info("ERROR :"+e);
                                    }


                                    if (!fail && b1 && b2 && b3){ // <<--  DNS only needed for MDS messages
                                        String obrName = handler.getOBRName(obr);
                                    	b1 = !obrFlag && !obrName.equals("");
                                    	b2 = !(obxName.contains(obrName));
                                    	b3 = obxCount < 2;
                                        if( b1 && b2 && b3){
                                        %>
                                        	<%--
                                            <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" >
                                                <td valign="top" align="left"><%=obrName%></td>
                                                <td colspan="6">&nbsp;</td>
                                            </tr>
                                             --%>
                                            <%
                                            obrFlag = true;

                                        }

                                        String status = handler.getOBXResultStatus(obr, obx).trim();
                                        String statusMsg = "";
                                        try {
                                        	 statusMsg = handler.getTestResultStatusMessage(handler.getOBXResultStatus(obr, obx).charAt(0));
                                        }
                                        catch (Exception e) {
                                        	statusMsg = "";
                                        }
                                        boolean strikeout = status != null && status.startsWith("W");
                                        String pre = "<u>";
                                        String post = "</u>";
                                        String obxDisplayName = "";
                                        if (strikeout) {
											pre = "<s>" + pre;
											post = post + "</s>";
                                        }
                                        String abnormalNature = handler.getNatureOfAbnormalTest(obr, obx);
                                        if (!stringIsNullOrEmpty(abnormalNature)) {
                                        	abnormalNature = " <span style=\"font-size:8px; color:#333333;\">"+abnormalNature+"</span>";
                                        }
                                        obxDisplayName = pre + obxName + post + abnormalNature;

                                        String lineClass = "NormalRes";
                                        String abnormal = handler.getOBXAbnormalFlag(obr, obx);
                                        if ( abnormal != null && abnormal.startsWith("L")){
                                            lineClass = "HiLoRes";
                                        } else if ( abnormal != null && ( abnormal.equals("A") || abnormal.startsWith("H") || handler.isOBXAbnormal(obr, obx) ) ){
                                            lineClass = "AbnormalRes";
                                        }
                                        String obxValueType = handler.getOBXValueType(obr,obx).trim();

                                        if (obxValueType.equals("ST") &&  handler.renderAsFT(obr,obx)) {
                                        	obxValueType = "FT";
                                        } else if (obxValueType.equals("TX") && handler.renderAsNM(obr,obx)) {
                                        	obxValueType = "NM";
                                        } else if (obxValueType.equals("FT") && handler.renderAsNM(obr,obx)) {
                                        	obxValueType = "NM";
                                        }

                                        if (obxValueType.equals("NM") 		// Numeric
                                        	|| obxValueType.equals("ST")) { // String Data
                                        	if (handler.isAncillary(obr,obx)) {
                                            %>
                                            <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
                                            	<td colspan="7">Patient Observation</td>
                                           	</tr>
                                            <% } %>
                                            <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
                                                <td valign="top" align="leftZOR"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier='+encodeURIComponent('<%= handler.getOBXIdentifier(obr, obx)%>'))"><%=obxDisplayName %></a></td>
                                                <td align="right"><%= strikeOutInvalidContent(handler.getOBXResult(obr, obx), status) %></td>
                                                <td align="center">
                                                        <%= strikeOutInvalidContent(handler.getOBXAbnormalFlag(obr, obx), status)%>
                                                </td>
                                                <td align="left"><%=strikeOutInvalidContent(handler.getOBXReferenceRange(obr, obx), status)%></td>
                                                <td align="left"><%=strikeOutInvalidContent(handler.formatString(handler.getOBXUnits(obr, obx)), status) %></td>
                                                <td align="center">
                                                <%--<%= strikeOutInvalidContent(handler.getTimeStamp(obr, obx), status) --%>
                                                </td>
                                                <td align="center"><%=statusMsg %></td>
                                            </tr>
                                            <%
                                        } else if (obxValueType.equals("SN")) { // or Structured Numeric
	                                              %>
	                                              <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
	                                                  <td valign="top" align="leftZOR"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier='+encodeURIComponent('<%= handler.getOBXIdentifier(obr, obx)%>'))"><%=obxDisplayName %></a></td>
	                                                  <td align="right"><%= strikeOutInvalidContent(handler.getOBXSNResult(obr, obx), status) %></td>
	                                                  <td align="center">
	                                                          <%= strikeOutInvalidContent(handler.getOBXAbnormalFlag(obr, obx), status)%>
	                                                  </td>
	                                                  <td align="left"><%=strikeOutInvalidContent(handler.getOBXReferenceRange(obr, obx), status)%></td>
	                                                  <td align="left"><%=strikeOutInvalidContent(handler.getOBXUnits(obr, obx), status) %></td>
	                                                  <td align="center"><%-- strikeOutInvalidContent(handler.getTimeStamp(obr, obx), status) --%></td>
	                                                  <td align="center"><%=statusMsg %></td>
	                                              </tr>
	                                              <%
                                        } else if (obxValueType.equals("TX") // Text Data (Display)
		                                        || obxValueType.equals("FT")) {  // Formatted Text (Display)
                                        	%>
                                        	<tr  bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
                                        		<td align="left" colspan="7"><b><%= obxDisplayName %></b></td>
                                        	</tr>
                                        	<tr  bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
                                        	<td align="left" colspan="6"><b><%= strikeOutInvalidContent(handler.formatString(handler.getOBXResult(obr, obx)), status) %></b></td>
                                        	<td align="center"> <%=statusMsg %></td>
                                        	</tr>
                                        	<%

										} else if (obxValueType.equals("TM")) { // Time
											%>
                                            <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
                                                <td valign="top" align="leftZOR"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier='+encodeURIComponent('<%= handler.getOBXIdentifier(obr, obx)%>'))"><%=obxDisplayName %></a></td>
                                                <td align="right"><%= strikeOutInvalidContent(handler.getOBXTMResult(obr, obx), status) %></td>
                                                <td align="center" colspan="4"></td>
                                                <td align="center"><%=statusMsg %></td>
                                            </tr>
                                            <%
										} else if (obxValueType.equals("DT")) { // Date
											%>
                                            <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
                                                <td valign="top" align="leftZOR"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier='+encodeURIComponent('<%= handler.getOBXIdentifier(obr, obx)%>'))"><%=obxDisplayName %></a></td>
                                                <td align="right"><%= strikeOutInvalidContent(handler.getOBXDTResult(obr, obx), status) %></td>
                                                <td align="center" colspan="4"></td>
                                                <td align="center"><%=statusMsg%></td>
                                            </tr>
                                            <%
										} else if (obxValueType.equals("TS")) { // Time Stamp (Date & Time)
											%>
                                            <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
                                                <td valign="top" align="leftZOR"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier='+encodeURIComponent('<%= handler.getOBXIdentifier(obr, obx)%>'))"><%=obxDisplayName %></a></td>
                                                <td align="right"><%= strikeOutInvalidContent(handler.getOBXTSResult(obr, obx), status) %></td>
                                                <td align="center" colspan="4"></td>
                                                <td align="center"><%=statusMsg %></td>
                                            </tr>
                                            <%
   										} else if (obxValueType.equals("ED")) { // Encapsulated Data
   											%>
   											<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
												<td colspan="7" valign="top" align="leftZOR"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier='+encodeURIComponent('<%= handler.getOBXIdentifier(obr, obx)%>'))"><%=obxDisplayName %></a></td>
   											</tr>
   											<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
   											<%if(!preview) { %>
   												<td colspan="4" valign="left"><a href="PrintOLIS.do?segmentID=<%=segmentID%>&obr=<%=obr%>&obx=<%=obx%>" style="margin-left: 30px;">Click to view attachment.</a>
   											<% } else { %>
   												<td colspan="4" valign="left"><a href="PrintOLIS.do?uuid=<%=oscar.Misc.getStr(request.getParameter("uuid"), "")%>&obr=<%=obr%>&obx=<%=obx%>" style="margin-left: 30px;">Click to view attachment.</a>   											
   											<% } %>
   												</td>
   												<td align="left" colspan="2"><%=strikeOutInvalidContent(handler.getOBXUnits(obr, obx), status) %></td>
   												<td align="center"><%=statusMsg %></td>
   											</tr>
   											<%
   										} else if (obxValueType.equals("CE")) { // Coded Entry

   											%>
   											<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
												<td colspan="7" valign="top" align="leftZOR"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier='+encodeURIComponent('<%= handler.getOBXIdentifier(obr, obx)%>'))"><%=obxDisplayName %></a></td>
   											</tr>
   											<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
   												<td colspan="6" valign="left"><span  style="margin-left:15px;"><%=handler.getOBXCEName(obr,obx) %></span></td>
   												<td align="center"><%=statusMsg %></td>
   											</tr>
   											<%
   											if (handler.isStatusFinal(handler.getOBXResultStatus(obr, obx).charAt(0))) {
  												String parentId = handler.getOBXCEParentId(obr, obx);
  												if (!stringIsNullOrEmpty(parentId)) {
   											%>
   											<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
   												<td colspan="7" align="center">
   													<table style="border: 1px solid black; margin-left 30px;">
   														<tr><th>Agent</th><th>Sensitivity</th> </tr>
   												    <%

   												    int childOBR = handler.getChildOBR(parentId) - 1;
   												    if (childOBR != -1) {
	   												    int childLength = handler.getOBXCount(childOBR);
	   												    for (int ceIndex = 0; ceIndex < childLength; ceIndex++) {
	   												    	String ceStatus = handler.getOBXResultStatus(childOBR, ceIndex).trim();
	   	   			                                        boolean ceStrikeout = ceStatus != null && ceStatus.startsWith("W");
	   	   			                                        String ceName = handler.getOBXName(childOBR,ceIndex);
	   	   			                                        ceName = ceStrikeout ? "<s>" + ceName + "</s>" : ceName;
	   	   			                                        String ceSense = handler.getOBXCESensitivity(childOBR,ceIndex);
	   	   			                                        ceSense = ceStrikeout ? "<s>" + ceSense + "</s>" : ceSense;
	   												    	%><tr><td><%=ceName%></td><td align="center"><%=ceSense%></td></tr><%
	   													}
   												    }
   													%>
   													</table>
   												</td>
  											</tr>
   											<% 		if (category.toUpperCase().trim().equals("MICROBIOLOGY")) {%>
   											<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
   												<td align="center" colspan="7">
   														S=Sensitive R=Resistant I=Intermediate MS=Moderately Sensitive VS=Very Sensitive

   												</td>
   											</tr>
											<%
													}
  												}
   											}
                                        } else {
                                        	%>
                                            <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
                                                <td valign="top" align="leftZOR"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier='+encodeURIComponent('<%= handler.getOBXIdentifier(obr, obx)%>'))"><%=obxDisplayName %></a></td>
                                                <td align="right"><%= strikeOutInvalidContent(handler.getOBXResult(obr, obx), status) %></td>
                                                <td align="center">
                                                        <%= strikeOutInvalidContent(handler.getOBXAbnormalFlag(obr, obx), status)%>
                                                </td>
                                                <td align="left"><%=strikeOutInvalidContent(handler.getOBXReferenceRange(obr, obx), status)%></td>
                                                <td align="left"><%=strikeOutInvalidContent(handler.getOBXUnits(obr, obx), status) %></td>
                                                <td align="center"><%-- strikeOutInvalidContent(handler.getTimeStamp(obr, obx), status) --%></td>
                                                <td align="center"><%=statusMsg %></td>
                                                <%--
                                                <td align="center"><%= handler.getOBXValueType(j, k) %></td>
                                                 --%>
                                            </tr>
                                            <%
                                        }
                                        String obsMethod = handler.getOBXObservationMethod(obr, obx);
                                        if (obsMethod != null && (obsMethod = obsMethod.trim()).length() > 0) {
                                        	%>
                                        	<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
                                                <td valign="top" align="left" colspan="7"><span style="margin-left:15px;">Observation Method: <%=obsMethod%></span></td>
                                            </tr>
                                        	<%
                                        }
                                        String obsDate = handler.getOBXObservationDate(obr, obx);
                                        if (obsDate != null && (obsDate = obsDate.trim()).length() > 0) {
                                        	%>
                                        	<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
                                                <td valign="top" align="left" colspan="7"><span style="margin-left:15px;">Observation Date: <%=obsDate%></span></td>
                                            </tr>
                                        	<%
                                        }
                                        for (l=0; l < handler.getOBXCommentCount(obr, obx); l++){%>
                                            <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
                                                <td valign="top" align="left" colspan="7" style="font-family:courier;">
                                                <div style="width:700px">
                                                	<%=handler.getOBXComment(obr, obx, l)%><span style="margin-left:15px;font-size:8px; color:#333333;word-break:normal;"><%=handler.getOBXSourceOrganization(obr, obx, l)%></span>
                                                </div>
                                                </td>
                                            </tr>
                                        <%}
                                    }
                                }
                            //}

                            String obsHeader = handler.getObservationHeader(obr, 0);
                            String headr = (String) headers.get(i);

                            //for ( j=0; j< OBRCount; j++){

                            //} //end for j=0; j<obrCount;
                            %>
                        </table>
                        <% // end for headers
                        }  // for i=0... (headers) line 625 %>

                        <table width="100%" border="0" cellspacing="0" cellpadding="3" class="MainTableBottomRowRightColumn" bgcolor="#003399">
                            <tr>
                                <td align="left" width="50%">
                                    <% if ( providerNo != null /*&& ! mDSSegmentData.getAcknowledgedStatus(providerNo) */) { %>
                                    <input type="submit" value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>" onclick="getComment()">
                                    <% } %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="popupStart(300, 400, '../../../oscarMDS/SelectProvider.jsp', 'providerselect')">
                                    <input type="button" value=" <bean:message key="global.btnClose"/> " onClick="window.close()">
                                    <input type="button" value=" <bean:message key="global.btnPrint"/> " onClick="printPDF()">
                                        <indivo:indivoRegistered demographic="<%=demographicID%>" provider="<%=providerNo%>">
                                        <input type="button" value="<bean:message key="global.btnSendToPHR"/>" onClick="sendToPHR('<%=segmentID%>', '<%=demographicID%>')">
                                        </indivo:indivoRegistered>
                                    <% if ( searchProviderNo != null ) { // we were called from e-chart %>
                                    <input type="button" value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> " onClick="popupStart(360, 680, '../../../oscarMDS/SearchPatient.do?labType=HL7&segmentID=<%=segmentID%>&name=<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>', 'searchPatientWindow')">
                                    <% } %>
                                </td>
                                <td width="50%" valign="center" align="left">
                                    <span class="Field2"><i><bean:message key="oscarMDS.segmentDisplay.msgReportEnd"/></i></span>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

        </form>
    </body>
</html>
