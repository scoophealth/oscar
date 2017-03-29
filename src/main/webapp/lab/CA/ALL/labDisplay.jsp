
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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="oscar.util.ConversionUtils"%>
<%@page import="org.oscarehr.common.dao.PatientLabRoutingDao"%>
<%@page import="org.oscarehr.common.model.PatientLabRouting"%>
<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="org.apache.commons.lang.builder.ReflectionToStringBuilder"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="org.oscarehr.caisi_integrator.ws.CachedDemographicLabResult"%>
<%@page import="oscar.oscarLab.ca.all.web.LabDisplayHelper"%>
<%@page errorPage="../../../provider/errorpage.jsp" %>
<%@ page import="java.util.*,
		 java.sql.*,
		 oscar.oscarDB.*, oscar.oscarLab.FileUploadCheck, oscar.util.UtilDateUtilities,
		 oscar.oscarLab.ca.all.*,
		 oscar.oscarLab.ca.all.util.*,
		 oscar.oscarLab.ca.all.parsers.*,
		 oscar.oscarLab.LabRequestReportLink,
		 oscar.oscarMDS.data.ReportStatus,oscar.log.*,
		 org.apache.commons.codec.binary.Base64,
         oscar.OscarProperties" %>
<%@ page import="org.oscarehr.casemgmt.model.CaseManagementNoteLink"%>
<%@ page import="org.oscarehr.casemgmt.model.CaseManagementNote"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="org.oscarehr.common.dao.UserPropertyDAO, org.oscarehr.common.model.UserProperty" %>
<%@ page import="org.oscarehr.common.model.MeasurementMap, org.oscarehr.common.dao.MeasurementMapDao" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.oscarehr.casemgmt.service.CaseManagementManager, org.oscarehr.common.dao.Hl7TextMessageDao, org.oscarehr.common.model.Hl7TextMessage,org.oscarehr.common.dao.Hl7TextInfoDao,org.oscarehr.common.model.Hl7TextInfo"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<%@	page import="javax.swing.text.rtf.RTFEditorKit"%>
<%@	page import="java.io.ByteArrayInputStream"%>
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
LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
oscar.OscarProperties props = oscar.OscarProperties.getInstance();
String segmentID = request.getParameter("segmentID");
String providerNo =request.getParameter("providerNo");
String searchProviderNo = StringUtils.trimToEmpty(request.getParameter("searchProviderNo"));
String patientMatched = request.getParameter("patientMatched");
String remoteFacilityIdString = request.getParameter("remoteFacilityId");
String remoteLabKey = request.getParameter("remoteLabKey");
String demographicID = request.getParameter("demographicId");
String showAllstr = request.getParameter("all");


if(providerNo == null) {
	providerNo = loggedInInfo.getLoggedInProviderNo();
}


UserPropertyDAO userPropertyDAO = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
UserProperty uProp = userPropertyDAO.getProp(providerNo, UserProperty.LAB_ACK_COMMENT);
boolean skipComment = false;
if( uProp != null && uProp.getValue().equalsIgnoreCase("yes")) {
	skipComment = true;
}


//Need date lab was received by OSCAR
Hl7TextMessageDao hl7TxtMsgDao = (Hl7TextMessageDao)SpringUtils.getBean("hl7TextMessageDao");
MeasurementMapDao measurementMapDao = (MeasurementMapDao) SpringUtils.getBean("measurementMapDao");
Hl7TextMessage hl7TextMessage = hl7TxtMsgDao.find(Integer.parseInt(segmentID));

String dateLabReceived = "n/a";
if(hl7TextMessage != null){
	java.util.Date date = hl7TextMessage.getCreated();
	String stringFormat = "yyyy-MM-dd HH:mm";
    dateLabReceived = UtilDateUtilities.DateToString(date, stringFormat);
}

boolean isLinkedToDemographic=false;
ArrayList<ReportStatus> ackList=null;
String multiLabId = null;
MessageHandler handler=null;
String hl7 = null;
String reqID = null, reqTableID = null;
String remoteFacilityIdQueryString="";

boolean bShortcutForm = OscarProperties.getInstance().getProperty("appt_formview", "").equalsIgnoreCase("on") ? true : false;
String formName = bShortcutForm ? OscarProperties.getInstance().getProperty("appt_formview_name") : "";
String formNameShort = formName.length() > 3 ? (formName.substring(0,2)+".") : formName;
String formName2 = bShortcutForm ? OscarProperties.getInstance().getProperty("appt_formview_name2", "") : "";
String formName2Short = formName2.length() > 3 ? (formName2.substring(0,2)+".") : formName2;
boolean bShortcutForm2 = bShortcutForm && !formName2.equals("");
List<MessageHandler>handlers = new ArrayList<MessageHandler>();
String []segmentIDs = null;
Boolean showAll = showAllstr != null && !"null".equalsIgnoreCase(showAllstr);

if (remoteFacilityIdString==null) // local lab
{
    
	Long reqIDL = LabRequestReportLink.getIdByReport("hl7TextMessage",Long.valueOf(segmentID));
	reqID = reqIDL==null ? "" : reqIDL.toString();
	reqIDL = LabRequestReportLink.getRequestTableIdByReport("hl7TextMessage",Long.valueOf(segmentID));
	reqTableID = reqIDL==null ? "" : reqIDL.toString();
	
	

	PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class); 
	for(PatientLabRouting r : dao.findByLabNoAndLabType(ConversionUtils.fromIntString(segmentID), "HL7")) {
		demographicID = "" + r.getDemographicNo();
	}

	if(demographicID != null && !demographicID.equals("")&& !demographicID.equals("0")){
	    isLinkedToDemographic=true;
	    LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_HL7_LAB, segmentID, request.getRemoteAddr(),demographicID);
	}else{
	    LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_HL7_LAB, segmentID, request.getRemoteAddr());
	}

	
	if( showAll ) {
		multiLabId = request.getParameter("multiID");		
		segmentIDs = multiLabId.split(",");
		for( int i = 0; i < segmentIDs.length; ++i) {
		    handlers.add(Factory.getHandler(segmentIDs[i]));
		}
		
		handler = handlers.get(0);
	}
	else {
		multiLabId = Hl7textResultsData.getMatchingLabs(segmentID);
		segmentIDs = multiLabId.split(",");
		
		List<String> segmentIdList = new ArrayList<String>();
		handler = Factory.getHandler(segmentID);
		handlers.add(handler);
		segmentIdList.add(segmentID);
		
		//this is where it gets weird. We want to show all messages with different filler order num but same accession in a single report
		segmentIDs = segmentIdList.toArray(new String[segmentIdList.size()]);
		
		hl7 = Factory.getHL7Body(segmentID);
		if (handler instanceof OLISHL7Handler) {
			%>
			<jsp:forward page="labDisplayOLIS.jsp" />
			<%
		}
	}
    
}
else // remote lab
{
	CachedDemographicLabResult remoteLabResult=LabDisplayHelper.getRemoteLab(loggedInInfo, Integer.parseInt(remoteFacilityIdString), remoteLabKey,Integer.parseInt(demographicID));
	MiscUtils.getLogger().debug("retrieved remoteLab:"+ReflectionToStringBuilder.toString(remoteLabResult));
	isLinkedToDemographic=true;

	LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_HL7_LAB, "segmentId="+segmentID+", remoteFacilityId="+remoteFacilityIdString+", remoteDemographicId="+demographicID);

	Document cachedDemographicLabResultXmlData=LabDisplayHelper.getXmlDocument(remoteLabResult);
	ackList=LabDisplayHelper.getReportStatus(cachedDemographicLabResultXmlData);
	multiLabId=LabDisplayHelper.getMultiLabId(cachedDemographicLabResultXmlData);
	handler=LabDisplayHelper.getMessageHandler(cachedDemographicLabResultXmlData);
	handlers.add(handler);
	segmentIDs = new String[] {"0"};  //fake segment ID for the for loop below to execute
	hl7=LabDisplayHelper.getHl7Body(cachedDemographicLabResultXmlData);
	
	try {
		remoteFacilityIdQueryString="&remoteFacilityId="+remoteFacilityIdString+"&remoteLabKey="+URLEncoder.encode(remoteLabKey, "UTF-8");
	} catch (Exception e) {
		MiscUtils.getLogger().error("Error", e);
	}
}

/********************** Converted to this sport *****************************/


// check for errors printing
if (request.getAttribute("printError") != null && (Boolean) request.getAttribute("printError")){
%>
<script language="JavaScript">
    alert("The lab could not be printed due to an error. Please see the server logs for more detail.");
</script>
<%}


	String annotation_display = org.oscarehr.casemgmt.model.CaseManagementNoteLink.DISP_LABTEST;
	CaseManagementManager caseManagementManager = (CaseManagementManager) SpringUtils.getBean("caseManagementManager");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <html:base/>
        <title><%=handler.getPatientName()+" Lab Results"%></title>
        <script language="javascript" type="text/javascript" src="../../../share/javascript/Oscar.js" ></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/prototype.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/scriptaculous.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/effects.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/jquery/jquery-1.4.2.js"></script>
      	<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/jquery/jquery.form.js"></script>
      	

       <script  type="text/javascript" charset="utf-8">

     	  jQuery.noConflict();
		</script>
		
	
	<oscar:customInterface section="labView"/>

		
        <script language="javascript" type="text/javascript">
            // alternately refer to this function in oscarMDSindex.js as labDisplayAjax.jsp does
		function updateLabDemoStatus(labno){
                                    if(document.getElementById("DemoTable"+labno)){
                                       document.getElementById("DemoTable"+labno).style.backgroundColor="#FFF";
                                    }
                                }
	</script>
        <link rel="stylesheet" type="text/css" href="../../../share/css/OscarStandardLayout.css">
        <style type="text/css">
            <!--
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
.TDISRes	{font-weight: bold; font-size: 10pt; color: black; font-family:
               Verdana, Arial, Helvetica}
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
.CorrectedRes         a:link { color: #6da997 }
.CorrectedRes a:hover { color: #6da997 }
.CorrectedRes a:visited { color: #6da997 }
.CorrectedRes a:active { color: #6da997 }
.Field       { font-weight: bold; font-size: 8.5pt; color: black; font-family:
               Verdana, Arial, Helvetica }
.NarrativeRes { font-weight: 700; font-size: 10pt; color: black; font-family:
               Courier New, Courier, mono }
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
pre {
	display: block;
    font-family:  Verdana, Arial, Helvetica;
    white-space: -moz-pre-space;
    margin:0px;
    font-size: x-small;
    font-weight:600;
} 
            -->
        </style>

        <script language="JavaScript">
        var providerNo = '<%=providerNo%>';
        function popupStart(vheight,vwidth,varpage,windowname) {
            var page = varpage;
            windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
            var popup=window.open(varpage, windowname, windowprops);
        }
        function getComment(action, segmentId) {
       
            var ret = true;
            var comment = "";
            var text = providerNo + "_" + segmentId + "commentText";
            if( $(text) != null ) {
	            comment = $(text).innerHTML;
	            if( comment == null ) {
	            	comment = "";
	            }
            }
            var commentVal = prompt('<bean:message key="oscarMDS.segmentDisplay.msgComment"/>', comment);

            if( commentVal == null ) {
            	ret = false;
            }
            else if( commentVal != null && commentVal.length > 0 )
                document.forms['acknowledgeForm_'+ segmentId].comment.value = commentVal;
            else
            	document.forms['acknowledgeForm_'+ segmentId].comment.value = comment;

           if(ret) handleLab('acknowledgeForm_'+segmentId,segmentId, action);

            return false;
        }

        function printPDF(labid){
        	var frm = "acknowledgeForm_" + labid;
        	document.forms[frm].action="PrintPDF.do";
        	document.forms[frm].submit();            
        }

	function linkreq(rptId, reqId) {
	    var link = "../../LinkReq.jsp?table=hl7TextMessage&rptid="+rptId+"&reqid="+reqId;
	    window.open(link, "linkwin", "width=500, height=200");
	}

        function sendToPHR(labId, demographicNo) {
        	<%
        		MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);

        		if (myOscarLoggedInInfo==null || !myOscarLoggedInInfo.isLoggedIn())
        		{
        			%>
    					alert('Please Login to MyOscar before performing this action.');
        			<%
        		}
        		else
        		{
        			%>
	                    popup(450, 600, "<%=request.getContextPath()%>/phr/SendToPhrPreview.jsp?labId=" + labId + "&demographic_no=" + demographicNo, "sendtophr");
        			<%
        		}
        	%>
        }

        function matchMe() {
            <% if ( !isLinkedToDemographic) { %>
               	popupStart(360, 680, '../../../oscarMDS/SearchPatient.do?labType=HL7&segmentID=<%= segmentID %>&name=<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>', 'searchPatientWindow');
            <% } %>
	}


        function handleLab(formid,labid,action){
            var url='../../../dms/inboxManage.do';
                                           var data='method=isLabLinkedToDemographic&labid='+labid;
                                           new Ajax.Request(url, {method: 'post',parameters:data,onSuccess:function(transport){
                                                                    var json=transport.responseText.evalJSON();
                                                                    if(json!=null){
                                                                        var success=json.isLinkedToDemographic;
                                                                        var demoid='';
                                                                        //check if lab is linked to a provider
                                                                        if(success){
                                                                            if(action=='ackLab'){
                                                                                if(confirmAck()){
                                                                                	$("labStatus_"+labid).value = "A";
                                                                                    updateStatus(formid,labid);
                                                                                }
                                                                            }else if(action=='msgLab'){
                                                                                demoid=json.demoId;
                                                                                if(demoid!=null && demoid.length>0)
                                                                                    window.popup(700,960,'../../../oscarMessenger/SendDemoMessage.do?demographic_no='+demoid,'msg');
                                                                            }else if(action=='ticklerLab'){
                                                                                demoid=json.demoId;
                                                                                if(demoid!=null && demoid.length>0)
                                                                                    window.popup(450,600,'../../../tickler/ForwardDemographicTickler.do?docType=HL7&docId='+labid+'&demographic_no='+demoid,'tickler')
                                                                            }
                                                                            else if( action == 'addComment' ) {
                                                                            	addComment(formid,labid);
                                                                            } else if (action == 'unlinkDemo') {
                                                                                unlinkDemographic(labid);
                                                                            }

                                                                        }else{
                                                                            if(action=='ackLab'){
                                                                                if(confirmAckUnmatched()) {
                                                                                	$("labStatus_"+labid).value = "A";
                                                                                    updateStatus(formid,labid);
                                                                                }
                                                                                else {
                                                                                    matchMe();
                                                                                }

                                                                            }else{
                                                                                alert("Please relate lab to a patient");
                                                                                matchMe();
                                                                            }
                                                                        }
                                                                    }
                                                            }});
        }
        function confirmAck(){
		<% if (props.getProperty("confirmAck", "").equals("yes")) { %>
            		return confirm('<bean:message key="oscarMDS.index.msgConfirmAcknowledge"/>');
            	<% } else { %>
            		return true;
            	<% } %>
	}

        function confirmCommentUnmatched(){
            return confirm('<bean:message key="oscarMDS.index.msgConfirmAcknowledgeUnmatched"/>');
        }

        function confirmAckUnmatched(){
            return confirm('<bean:message key="oscarMDS.index.msgConfirmAcknowledgeUnmatched"/>');
        }
        function updateStatus(formid,labid){
        	
            var url='<%=request.getContextPath()%>'+"/oscarMDS/UpdateStatus.do";
            var data=$(formid).serialize(true);

            new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
            	
            	if( <%=showAll%> ) {                	
                	window.location.reload();
                }
            	else if( window.opener.document.getElementById('labdoc_'+labid) != null ) {
                	window.opener.Effect.BlindUp('labdoc_'+labid);
                    window.opener.refreshCategoryList();  
                    window.close();
            	}
                else {
                	window.close();
                }
                
        }});

        }
        
        function unlinkDemographic(labNo){           
            var reason = "Incorrect demographic";
            reason = prompt('<bean:message key="oscarMDS.segmentDisplay.msgUnlink"/>', reason);

            //must include reason
            if( reason == null || reason.length == 0) {
            	return false;
            }
            
            var urlStr='<%=request.getContextPath()%>'+"/lab/CA/ALL/UnlinkDemographic.do";
            var dataStr="reason="+reason+"&labNo="+labNo;
            jQuery.ajax({
    			type: "POST",
    			url:  urlStr,
    			data: dataStr,
    			success: function (data) {
                            top.opener.location.reload();
                            window.close();
    			}
            });                            
        }

        function addComment(formid,labid) {
        	var url='<%=request.getContextPath()%>'+"/oscarMDS/UpdateStatus.do?method=addComment";
			if( $F("labStatus_"+labid) == "" ) {
				$("labStatus_"+labid).value = "N";
			}
			
        	var data=$(formid).serialize(true);
        	
            new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){				
            		window.location.reload();
            	
        }});
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

        function submitLabel(lblval, segmentID){
       		document.forms['TDISLabelForm_'+segmentID].label.value = document.forms['acknowledgeForm_'+segmentID].label.value;
       	}
        </script>

    </head>

    <body onLoad="javascript:matchMe();">
        <!-- form forwarding of the lab -->
        <%        
        	for( int idx = 0; idx < segmentIDs.length; ++idx ) {
        		       		
        		if (remoteFacilityIdString==null) {
        			ackList = AcknowledgementData.getAcknowledgements(segmentID);
        			segmentID = segmentIDs[idx];          		
                	handler = handlers.get(idx);
        		}
        		
        		boolean notBeenAcked = ackList.size() == 0;
        		boolean ackFlag = false;
        		String labStatus = "";
        		if (ackList != null){
        		    for (int i=0; i < ackList.size(); i++){
        		        ReportStatus reportStatus = ackList.get(i);
        		        if (reportStatus.getProviderNo() != null && reportStatus.getProviderNo().equals(providerNo) ) {
        		        	labStatus = reportStatus.getStatus();
        		        	if( labStatus.equals("A") ){
        		            	ackFlag = true;//lab has been ack by this provider.
        		            	break;
        		        	}
        		        }
        		    }
        		}
        		
        		Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao) SpringUtils.getBean("hl7TextInfoDao");
        		int lab_no = Integer.parseInt(segmentID);
        		Hl7TextInfo hl7Lab = hl7TextInfoDao.findLabId(lab_no);
        		String label = "";
        		if (hl7Lab != null && hl7Lab.getLabel()!=null) label = hl7Lab.getLabel();
        		
        		String ackLabFunc;
        		if( skipComment ) {
        			ackLabFunc = "handleLab('acknowledgeForm_" + segmentID + "','" + segmentID + "','ackLab');";
        		}
        		else {
        			ackLabFunc = "getComment('ackLab', " + segmentID + ");";
        		}

        %>
        <script type="text/javascript">

        jQuery(function() {
      	  jQuery("#createLabel_<%=segmentID%>").click(function() {
      	    jQuery.ajax( {
      	      type: "POST",
      	      url: '<%=request.getContextPath()%>'+"/lab/CA/ALL/createLabelTDIS.do",
      	      dataType: "json",
      	      data: { lab_no: jQuery("#labNum_<%=segmentID%>").val(),accessionNum: jQuery("#accNum").val(), label: jQuery("#label_<%=segmentID%>").val(), ajaxcall: true },
      	      success: function(result) {alert("label applied");
      	    	jQuery("#labelspan_<%=segmentID%>").children().get(0).innerHTML = "Label: " +  jQuery("#label_<%=segmentID%>").val();
        	  	document.forms['acknowledgeForm_<%=segmentID%>'].label.value = "";    
      	      }
      	    }
      	   );
      	});
      });

		</script>
		<div id="lab_<%=segmentID%>">
        <form name="reassignForm_<%=segmentID%>" method="post" action="Forward.do">
            <input type="hidden" name="flaggedLabs" value="<%=segmentID%>" />
            <input type="hidden" name="selectedProviders" value="" />
            <input type="hidden" name="favorites" value="" />
            <input type="hidden" name="labType" value="HL7" />
            <input type="hidden" name="labType<%= segmentID %>HL7" value="imNotNull" />
            <input type="hidden" id="providerNo_<%=segmentID %>" name="providerNo" value="<%= providerNo %>" />
        </form>

        <form name="TDISLabelForm_<%=segmentID%>"  method='POST' action="../../../lab/CA/ALL/createLabelTDIS.do">
					<input type="hidden" id="labNum_<%=segmentID %>" name="lab_no" value="<%=lab_no%>">
					<input type="hidden" id="label_<%=segmentID %>" name="label" value="<%=label%>">
		</form>

        <form name="acknowledgeForm_<%=segmentID%>" id="acknowledgeForm_<%=segmentID%>" method="post" onsubmit="javascript:void(0);" method="post" action="javascript:void(0);" >

            <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="3">
                            <tr>
                                <td align="left" class="MainTableTopRowRightColumn" width="100%">
                                    <input type="hidden" name="segmentID" value="<%= segmentID %>"/>
                                    <input type="hidden" name="multiID" value="<%= multiLabId %>" />
                                    <input type="hidden" name="providerNo" id="providerNo" value="<%= providerNo %>"/>
                                    <input type="hidden" name="status" value="<%=labStatus%>" id="labStatus_<%=segmentID%>"/>
                                    <input type="hidden" name="comment" value=""/>
                                    <input type="hidden" name="labType" value="HL7"/>
                                    <%
                                    if ( !ackFlag ) {
                                    %>

                                    <input type="button" value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>" onclick="<%=ackLabFunc%>" >
                                    <input type="button" value="<bean:message key="oscarMDS.segmentDisplay.btnComment"/>" onclick="return getComment('addComment',<%=segmentID%>);">
                                    <% } %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="popupStart(355, 675, '../../../oscarMDS/SelectProvider.jsp?docId=<%=segmentID%>&labDisplay=true', 'providerselect')">
                                    <input type="button" value=" <bean:message key="global.btnClose"/> " onClick="window.close()">
                                    <input type="button" value=" <bean:message key="global.btnPrint"/> " onClick="printPDF('<%=segmentID%>')">

                                    <input type="button" value="Msg" onclick="handleLab('','<%=segmentID%>','msgLab');"/>
                                    <input type="button" value="Tickler" onclick="handleLab('','<%=segmentID%>','ticklerLab');"/>
                                    <input type="button" value="<bean:message key="oscarMDS.segmentDisplay.btnUnlinkDemo"/>" onclick="handleLab('','<%=segmentID%>','unlinkDemo');"/>

                                    <% if ( searchProviderNo != null ) { // null if we were called from e-chart%>
                                    <input type="button" value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/>" onClick="popupStart(360, 680, '../../../oscarMDS/SearchPatient.do?labType=HL7&segmentID=<%= segmentID %>&name=<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>', 'encounter')">
                                    <% } %>
				    <input type="button" value="Req# <%=reqTableID%>" title="Link to Requisition" onclick="linkreq('<%=segmentID%>','<%=reqID%>');" />


                                   	<% if (bShortcutForm) { %>
									<input type="button" value="<%=formNameShort%>" onClick="popupStart(700, 1024, '../../../form/forwardshortcutname.jsp?formname=<%=formName%>&demographic_no=<%=demographicID%>', '<%=formNameShort%>')" />
									<% } %>
									<% if (bShortcutForm2) { %>
									<input type="button" value="<%=formName2Short%>" onClick="popupStart(700, 1024, '../../../form/forwardshortcutname.jsp?formname=<%=formName2%>&demographic_no=<%=demographicID%>', '<%=formName2Short%>')" />
									<% } %>


                                    <% if (!label.equals(null) && !label.equals("")) { %>
										<button type="button" id="createLabel_<%=segmentID%>" value="Label" onclick="submitLabel(this, '<%=segmentID%>');">Label</button>
										<%} else { %>
										<button type="button" id="createLabel_<%=segmentID%>" style="background-color:#6699FF" value="Label" onclick="submitLabel(this, '<%=segmentID%>');">Label</button>
										<%} %>
										<input type="hidden" id="labNum_<%=segmentID %>" name="lab_no" value="<%=lab_no%>">
						                <input type="text" id="acklabel_<%=segmentID %>" name="label" value=""/>

						                 <% String labelval="";
						                 if (label!="" && label!=null) {
						                 	labelval = label;
						                 }else {
						                	 labelval = "(not set)";

						                 } %>
					                 <span id="labelspan_<%=segmentID%>" class="Field2"><i>Label: <%=labelval %> </i></span>

                                    <span class="Field2"><i>Next Appointment: <oscar:nextAppt demographicNo="<%=demographicID%>"/></i></span>
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
                                                if( multiID.length > 1 ) {
                                                    if ( searchProviderNo != null ) { // null if we were called from e-chart
                                                        %><a href="labDisplay.jsp?segmentID=<%=segmentID%>&multiID=<%=multiLabId%>&providerNo=<%= providerNo %>&searchProviderNo=<%= searchProviderNo %>&all=true">All</a>&#160;<%
                                                    }else{
                                                        %><a href="labDisplay.jsp?segmentID=<%=segmentID%>&multiID=<%=multiLabId%>&providerNo=<%= providerNo %>&all=true">All</a>&#160;<%
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
                                    <div class="Field2">
                                        <bean:message key="oscarMDS.segmentDisplay.formDetailResults"/>
                                    </div>
                                </td>
                                <td width="33%" align="middle" class="Cell">
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
                                                <table width="100%" border="0" cellpadding="2" cellspacing="0" valign="top"  <% if ( !isLinkedToDemographic){ %> bgcolor="orange" <% } %> id="DemoTable<%=segmentID%>" >                                                    <tr>
                                                        <td valign="top" align="left">
                                                            <table valign="top" border="0" cellpadding="3" cellspacing="0" width="100%">
                                                                <tr>
                                                                    <td nowrap>
                                                                        <div class="FieldData">
                                                                            <strong><bean:message key="oscarMDS.segmentDisplay.formPatientName"/>: </strong>
                                                                        </div>
                                                                    </td>
                                                                    <td nowrap>
                                                                        <div class="FieldData" nowrap="nowrap">
                                                                            <% if ( searchProviderNo == null ) { // we were called from e-chart%>
                                                                            <a href="javascript:window.close()">
                                                                            <% } else { // we were called from lab module%>
                                                                            <a href="javascript:popupStart(360, 680, '../../../oscarMDS/SearchPatient.do?labType=HL7&segmentID=<%= segmentID %>&name=<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>', 'searchPatientWindow')">
                                                                                <% } %>
                                                                                <%=handler.getPatientName()%>
                                                                            </a>
                                                                        </div>
                                                                    </td>
                                                                    <td colspan="2"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>
                                                                        <div class="FieldData">
                                                                            <strong><bean:message key="oscarMDS.segmentDisplay.formDateBirth"/>: </strong>
                                                                        </div>
                                                                    </td>
                                                                    <td nowrap>
                                                                        <div class="FieldData" nowrap="nowrap">
                                                                            <%=handler.getDOB()%>
                                                                        </div>
                                                                    </td>
                                                                    <td colspan="2"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>
                                                                        <div class="FieldData">
                                                                            <strong><bean:message key="oscarMDS.segmentDisplay.formAge"/>: </strong>
                                                                        </div>
                                                                    </td>
                                                                    <td nowrap>
                                                                        <div class="FieldData">
                                                                            <%=handler.getAge()%>
                                                                        </div>
                                                                    </td>
                                                                    
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>
                                                                        <div class="FieldData">
                                                                            <strong>
                                                                                <bean:message key="oscarMDS.segmentDisplay.formHealthNumber"/>
                                                                            </strong>
                                                                        </div>
                                                                    </td>
                                                                    <td nowrap>
                                                                        <div class="FieldData" nowrap="nowrap">
                                                                            <%=handler.getHealthNum()%>
                                                                        </div>
                                                                    </td>
                                                                    <td colspan="2"></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td width="33%" valign="top">
                                                            <table valign="top" border="0" cellpadding="3" cellspacing="0" width="100%">
                                                                <tr>
                                                                    <td nowrap>
                                                                        <div align="left" class="FieldData">
                                                                            <strong><bean:message key="oscarMDS.segmentDisplay.formHomePhone"/>: </strong>
                                                                        </div>
                                                                    </td>
                                                                    <td nowrap>
                                                                        <div align="left" class="FieldData" nowrap="nowrap">
                                                                            <%=handler.getHomePhone()%>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>
                                                                        <div align="left" class="FieldData">
                                                                            <strong><bean:message key="oscarMDS.segmentDisplay.formWorkPhone"/>: </strong>
                                                                        </div>
                                                                    </td>
                                                                    <td nowrap>
                                                                        <div align="left" class="FieldData" nowrap="nowrap">
                                                                            <%=handler.getWorkPhone()%>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                   <td nowrap>
                                                                        <div class="FieldData">
                                                                            <strong><bean:message key="oscarMDS.segmentDisplay.formSex"/>: </strong>
                                                                        </div>
                                                                    </td>
                                                                    <td align="left" nowrap>
                                                                        <div class="FieldData">
                                                                            <%=handler.getSex()%>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td nowrap>
                                                                        <div align="left" class="FieldData">
                                                                            <strong><bean:message key="oscarMDS.segmentDisplay.formPatientLocation"/>: </strong>
                                                                        </div>
                                                                    </td>
                                                                    <td nowrap>
                                                                        <div align="left" class="FieldData" nowrap="nowrap">
                                                                            <%=handler.getPatientLocation()%>
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
                                                <div class="FieldData">
                                                <% if ("CLS".equals(handler.getMsgType())) { %>
                                                    <strong><bean:message key="oscarMDS.segmentDisplay.formDateServiceCLS"/>:</strong>
												<% } else { %>
                                                    <strong><bean:message key="oscarMDS.segmentDisplay.formDateService"/>:</strong>
												<% } %>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="FieldData" nowrap="nowrap">
                                                    <%= handler.getServiceDate() %>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                        	<td>
                                        		<div class="FieldData">
                                                <% if ("CLS".equals(handler.getMsgType())) { %>
                                                    <strong><bean:message key="oscarMDS.segmentDisplay.formDateReceivedCLS"/>:</strong>
												<% } else { %>
                                                    <strong><bean:message key="oscarMDS.segmentDisplay.formDateReceived"/>:</strong>
												<% } %>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="FieldData" nowrap="nowrap">
                                                    <%= dateLabReceived %>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="FieldData">
                                                    <strong><bean:message key="oscarMDS.segmentDisplay.formReportStatus"/>:</strong>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="FieldData" nowrap="nowrap">
                                                    <%= ( handler.getOrderStatus().equals("F") ? "Final" : handler.getOrderStatus().equals("C") ? "Corrected" : (handler.getMsgType().equals("PATHL7") && handler.getOrderStatus().equals("P")) ? "Preliminary": handler.getOrderStatus().equals("X") ? "DELETED": handler.getOrderStatus()) %>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td nowrap>
                                                <div class="FieldData">
                                                    <strong><bean:message key="oscarMDS.segmentDisplay.formClientRefer"/>:</strong>
                                                </div>
                                            </td>
                                            <td nowrap>
                                                <div class="FieldData" nowrap="nowrap">
                                                    <%= handler.getClientRef()%>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div class="FieldData">
                                                    <strong><bean:message key="oscarMDS.segmentDisplay.formAccession"/>:</strong>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="FieldData" nowrap="nowrap">
                                                    <%= handler.getAccessionNum()%>
                                                </div>
                                            </td>
                                        </tr>
                                        <% if (handler.getMsgType().equals("MEDVUE")) {  %>
                                        <tr>
                                        	<td>
                                                <div class="FieldData">
                                                    <strong>MEDVUE Encounter Id:</strong>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="FieldData" nowrap="nowrap">
                                                   <%= handler.getEncounterId() %>
                                                </div>
                                            </td>
                                        </tr>
                                        <% } 
                                        String comment = handler.getNteForPID();
                                        if (comment != null && !comment.equals("")) {%>
                                        <tr>
                                        	<td>
                                                <div class="FieldData">
                                                    <strong>Remarks:</strong>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="FieldData" nowrap="nowrap">
                                                   <%= comment %>
                                                </div>
                                            </td>
                                        </tr>
                                        <%} %>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td bgcolor="white" colspan="2">
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0" bordercolor="#CCCCCC">
                                        <tr>
                                            <td bgcolor="white">
                                                <div class="FieldData">
                                                    <strong><bean:message key="oscarMDS.segmentDisplay.formRequestingClient"/>: </strong>
                                                    <%= handler.getDocName()%>
                                                </div>
                                            </td>
                                            <%-- <td bgcolor="white">
                                    <div class="FieldData">
                                        <strong><bean:message key="oscarMDS.segmentDisplay.formReportToClient"/>: </strong>
                                            <%= No admitting Doctor for CML messages%>
                                    </div>
                                </td> --%>
                                            <td bgcolor="white" align="right">
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
                                        if (startFlag) {
                                            //if (ackList.size() > 0){{%>
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
                                                                        report = ackList.get(i); %>
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
                                                                        <% } %>
                                                                        <span id="<%=report.getOscarProviderNo() + "_" + segmentID%>commentLabel"><%=report.getComment() == null || report.getComment().equals("") ? "no comment" : "comment : "%></span><span id="<%=report.getOscarProviderNo() + "_" + segmentID%>commentText"><%=report.getComment()==null ? "" : report.getComment()%></span>
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

                                            <%//}
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

                        ArrayList<String> headers = handler.getHeaders();
                        int OBRCount = handler.getOBRCount();
                        
                        if (handler.getMsgType().equals("MEDVUE")) { %>
<%-- MEDVUE Redirect. --%>
                        <table style="page-break-inside:avoid;" bgcolor="#003399" border="0" cellpadding="0" cellspacing="0" width="100%">
                           <tr>
                               <td colspan="4" height="7">&nbsp;</td>
                           </tr>
                           <tr>
                               <td bgcolor="#FFCC00" width="300" valign="bottom">
                                   <div class="Title2">
                                      <%=headers.get(0)%>
                                   </div>
                               </td>
                               <%--<td align="right" bgcolor="#FFCC00" width="100">&nbsp;</td>--%>
                               <td width="9">&nbsp;</td>
                               <td width="9">&nbsp;</td>
                               <td width="*">&nbsp;</td>
                           </tr>
                       </table>
                       <table width="100%" border="0" cellspacing="0" cellpadding="2" bgcolor="#CCCCFF" bordercolor="#9966FF" bordercolordark="#bfcbe3" name="tblDiscs" id="tblDiscs">
                           <tr class="Field2">
                               <td width="25%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formTestName"/></td>
                               <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formResult"/></td>
                               <td width="5%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formAbn"/></td>
                               <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formReferenceRange"/></td>
                               <td width="10%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formUnits"/></td>
                               <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formDateTimeCompleted"/></td>
                               <td width="6%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formNew"/></td>
                               <td width="6%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formAnnotate"/></td>
                           </tr>
	                        <tr class="TDISRes">
		                      	<td valign="top" align="left" colspan="8" ><pre  style="margin:0px 0px 0px 100px;"><b>Radiologist: </b><b><%=handler.getRadiologistInfo()%></b></pre></td>
		                      	</td>
	                     	 </tr>
	                        <tr class="TDISRes">
		                       	<td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><b><%=handler.getOBXComment(1, 1, 1)%></b></pre></td>
		                       	</td>
		                       	<td align="center" valign="top">
                                    <a href="javascript:void(0);" title="Annotation" onclick="window.open('<%=request.getContextPath()%>/annotation/annotation.jsp?display=<%=annotation_display%>&amp;table_id=<%=segmentID%>&amp;demo=<%=demographicID%>&amp;other_id=<%=String.valueOf(1) + "-" + String.valueOf(1) %>','anwin','width=400,height=500');">
                                    	<img src="../../../images/notes.gif" alt="rxAnnotation" height="16" width="13" border="0"/>
                                    </a>
                                </td>
	                      	 </tr>
                     	 </table>
 <%-- ALL OTHERS Redirect. --%>                    	 
                     <% } else {

                      for(i=0;i<headers.size();i++){
                           linenum=0;
						boolean isUnstructuredDoc = false;
						boolean	isVIHARtf = false;
						boolean isSGorCDC = false;

						//Checks to see if the PATHL7 lab is an unstructured document, a VIHA RTF pathology report, or if the patient location is SG/CDC
						//labs that fall into any of these categories have certain requirements per Excelleris
						if(handler.getMsgType().equals("PATHL7")){
							isUnstructuredDoc = ((PATHL7Handler) handler).unstructuredDocCheck(headers.get(i));
							isVIHARtf = ((PATHL7Handler) handler).vihaRtfCheck(headers.get(i));
							if(handler.getPatientLocation().equals("SG") || handler.getPatientLocation().equals("CDC")){
								isSGorCDC = true;
							}

						} else if(handler.getMsgType().equals("CLS")){
                            isUnstructuredDoc = ((CLSHandler) handler).isUnstructured();
                        }
						
						if( handler.getMsgType().equals("MEDITECH") ) {
							isUnstructuredDoc = ((MEDITECHHandler) handler).isUnstructured();
						} %>
						
		                       <table style="page-break-inside:avoid;" bgcolor="#003399" border="0" cellpadding="0" cellspacing="0" width="100%">
	                           <tr>
	                               <td colspan="4" height="7">&nbsp;</td>
	                           </tr>
	                           <tr>
	                               <td bgcolor="#FFCC00" width="300" valign="bottom">
	                                   <div class="Title2">
	                                       <%=headers.get(i)%>
	                                   </div>
	                               </td>
	                               <%--<td align="right" bgcolor="#FFCC00" width="100">&nbsp;</td>--%>
	                               <td width="9">&nbsp;</td>
	                               <td width="9">&nbsp;</td>
	                               <td width="*">&nbsp;</td>
	                           </tr>
	                       </table>
	                   <% if ( ( handler.getMsgType().equals("MEDITECH") && isUnstructuredDoc) || 
	                		   ( handler.getMsgType().equals("MEDITECH") && ((MEDITECHHandler) handler).isReportData() ) ) { %>
	                       	<table style="width:100%;border-collapse:collapse;" bgcolor="#CCCCFF" bordercolor="#9966FF" bordercolordark="#bfcbe3" name="tblDiscs" id="tblDiscs" >	                  
	                       	<tr><td colspan="4" style="padding-left:10px;">

                       	<%} else if( isUnstructuredDoc){%>
	                       <table width="100%" border="0" cellspacing="0" cellpadding="2" bgcolor="#CCCCFF" bordercolor="#9966FF" bordercolordark="#bfcbe3" name="tblDiscs" id="tblDiscs">

	                           <tr class="Field2">

	                               <td width="20%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formTestName"/></td>
	                               <td width="60%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formResult"/></td>
								   <% if ("CLS".equals(handler.getMsgType())) { %>
									   <td width="20%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formDateTimeCompletedCLS"/></td>
								   <% } else { %>
									   <td width="20%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formDateTimeCompleted"/></td>
								   <% } %>

	                           </tr><%
						} else {%>
                       <table width="100%" border="0" cellspacing="0" cellpadding="2" bgcolor="#CCCCFF" bordercolor="#9966FF" bordercolordark="#bfcbe3" name="tblDiscs" id="tblDiscs">
                           
                           <% if( "MIC".equals( ((MEDITECHHandler) handler).getSendingApplication() ) ) { %>
	                          		<tr>
			                   			<td colspan="8" ></td>
		                   			</tr>
		                   			<tr>
			                   			<td valign="top" align="left" style="padding-left:20px;font-weight:bold;" >SPECIMEN SOURCE: </td>
			                   			<td valign="top" align="left" style="font-weight:bold;" colspan="7"><%= ((MEDITECHHandler) handler).getSpecimenSource(i) %></td>
		                   			</tr>
		                   			<tr>
			                   			<td valign="top" align="left" style="padding-left:20px;font-weight:bold;">SPECIMEN DESCRIPTION: </td>
			                   			<td valign="top" align="left" style="font-weight:bold;" colspan="7"><%= ((MEDITECHHandler) handler).getSpecimenDescription(i) %></td>
		                   			</tr>
		                   			<tr>
			                   			<td colspan="8" ></td>
		                   			</tr>
		                   		<% }%>
                           
                           <tr class="Field2">
                               <td width="25%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formTestName"/></td>
                               <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formResult"/></td>
                               <td width="5%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formAbn"/></td>
                               <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formReferenceRange"/></td>
                               <td width="10%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formUnits"/></td>
                               <% if ("CLS".equals(handler.getMsgType())) { %>
                                   <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formDateTimeCompletedCLS"/></td>
							   <% } else { %>
                                   <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formDateTimeCompleted"/></td>
							   <% } %>
                               <td width="6%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formNew"/></td>
                          	   <td width="6%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formAnnotate"/></td>
                           </tr>
                           
 							<%
						} // end else / if isUnstructured
                           
                        for ( j=0; j < OBRCount; j++) {

                        	   String lastObxSetId = "0";
                               boolean obrFlag = false;
                               int obxCount = handler.getOBXCount(j);
                               
                               for (k=0; k < obxCount; k++) {

                               	String obxName = handler.getOBXName(j, k);

								boolean isAllowedDuplicate = false;
								if(handler.getMsgType().equals("PATHL7")){
									//if the obxidentifier and result name are any of the following, they must be displayed (they are the Excepetion to Excelleris TX/FT duplicate result name display rules)
									if((handler.getOBXName(j, k).equals("Culture") && handler.getOBXIdentifier(j, k).equals("6463-4")) || 
									(handler.getOBXName(j, k).equals("Organism") && (handler.getOBXIdentifier(j, k).equals("X433") || handler.getOBXIdentifier(j, k).equals("X30011")))){
					   					isAllowedDuplicate = true;
					   				}
								}
                                   boolean b1=false, b2=false, b3=false;

                                   boolean fail = true;
                                   try {
                                   b1 = !handler.getOBXResultStatus(j, k).equals("DNS");
                                 	b2 = !obxName.equals("");
                                   String currheader = headers.get(i);
                                   String obsHeader = handler.getObservationHeader(j,k);
                                   b3 = handler.getObservationHeader(j, k).equals(headers.get(i));
                                   fail = false;
                                 } catch (Exception e){
                                   	//logger.info("ERROR :"+e);
                                   }

                                   if (handler.getMsgType().equals("EPSILON")) {
                                   	b2=true;
                                   	b3=true; //Because Observation header can never be the same as the header. Observation header = OBX-4.2 and header= OBX-4.1
                                   } else if(handler.getMsgType().equals("PFHT") || handler.getMsgType().equals("CML") || handler.getMsgType().equals("HHSEMR")) {
                                   	b2=true;
                                   }

                                    if (!fail && b1 && b2 && b3){ // <<--  DNS only needed for MDS messages

                                   	String obrName = handler.getOBRName(j);
                                   	b1 = !obrFlag && !obrName.equals("");
                                   	b2 = !(obxName.contains(obrName));
                                   	b3 = obxCount < 2;
                                       if( b1 && b2 && b3){
                                       %>
                                           <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" >
                                               <td valign="top" align="left"><%=obrName%></td>
                                               <td colspan="6">&nbsp;</td>
                                           </tr>
                                           <%obrFlag = true;
                                       }

                                       String lineClass = "NormalRes";
                                       String abnormal = handler.getOBXAbnormalFlag(j, k);
                                       if ( abnormal != null && abnormal.startsWith("L")){
                                           lineClass = "HiLoRes";
                                       } else if ( abnormal != null && ( abnormal.equals("A") || abnormal.startsWith("H") || handler.isOBXAbnormal( j, k) ) ){
                                           lineClass = "AbnormalRes";
                                       }

                                       boolean isPrevAnnotation = false;
                                       CaseManagementNoteLink cml = caseManagementManager.getLatestLinkByTableId(CaseManagementNoteLink.LABTEST,Long.valueOf(segmentID),j+"-"+k);
                                       CaseManagementNote p_cmn = null;
                                       if (cml!=null) {p_cmn = caseManagementManager.getNote(cml.getNoteId().toString());}
                                       if (p_cmn!=null){isPrevAnnotation=true;}

                                       String loincCode = null;
                                       try{
                                       	List<MeasurementMap> mmapList =  measurementMapDao.getMapsByIdent(handler.getOBXIdentifier(j, k));
                                       	if (mmapList.size()>0) {
	                                    	MeasurementMap mmap =mmapList.get(0);
	                                       	loincCode = mmap.getLoincCode();
                                       	}
                                       }catch(Exception e){
                                        	MiscUtils.getLogger().error("loincProb",e);
                                       }

                                       if (handler.getMsgType().equals("EPSILON")) {
	                                    	   if (handler.getOBXIdentifier(j,k).equals(headers.get(i)) && !obxName.equals("")) { %>

	                                        	<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
		                                            <td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier=<%= URLEncoder.encode(handler.getOBXIdentifier(j, k).replaceAll("&","%26"),"UTF-8") %>')"><%=obxName %></a>
		                                            &nbsp;<%if(loincCode != null){ %>
                                                	<a href="javascript:popupStart('660','1000','http://apps.nlm.nih.gov/medlineplus/services/mpconnect.cfm?mainSearchCriteria.v.cs=2.16.840.1.113883.6.1&mainSearchCriteria.v.c=<%=loincCode%>&informationRecipient.languageCode.c=en')"> info</a>
                                                	<%} %>
                                                	</td>
		                                            <td align="right"><%= handler.getOBXResult( j, k) %></td>

		                                            <td align="center">
		                                                    <%= handler.getOBXAbnormalFlag(j, k)%>
		                                            </td>
		                                            <td align="left"><%=handler.getOBXReferenceRange( j, k)%></td>
		                                            <td align="left"><%=handler.getOBXUnits( j, k) %></td>
		                                            <td align="center"><%= handler.getTimeStamp(j, k) %></td>
		                                            <td align="center"><%= handler.getOBXResultStatus( j, k) %></td>
		                                            <td align="center" valign="top">
	                                                <a href="javascript:void(0);" title="Annotation" onclick="window.open('<%=request.getContextPath()%>/annotation/annotation.jsp?display=<%=annotation_display%>&amp;table_id=<%=segmentID%>&amp;demo=<%=demographicID%>&amp;other_id=<%=String.valueOf(j) + "-" + String.valueOf(k) %>','anwin','width=400,height=500');">
	                                                	<%if(!isPrevAnnotation){ %><img src="../../../images/notes.gif" alt="rxAnnotation" height="16" width="13" border="0"/><%}else{ %><img src="../../../images/filledNotes.gif" alt="rxAnnotation" height="16" width="13" border="0"/> <%} %>
	                                                </a>
                                                </td>
	                                       		</tr>
	                                       <% } else if (handler.getOBXIdentifier(j,k).equals(headers.get(i)) && obxName.equals("")) { %>
	                                       			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
	                                                    <td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><%=handler.getOBXResult( j, k)%></pre></td>

	                                                </tr>
	                                       	<% }
                                       } else if (handler.getMsgType().equals("IHA") ) {
                                           if(handler.getOBXValueType(j,k) != null &&  handler.getOBXValueType(j,k).equalsIgnoreCase("NAR")) {
                                               %>                                         
                                               <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%="NarrativeRes"%>" >
                                                   <td align="left" colspan="7"style="padding-left:10px;"><%= handler.getOBXResult( j, k) %></td>
                                               </tr>
                                           <%}else{%>
                                               <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
                                                   <td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier=<%= URLEncoder.encode(handler.getOBXIdentifier(j, k).replaceAll("&","%26"),"UTF-8") %><%=remoteFacilityIdQueryString%>')"><%=obxName %></a>
                                                   &nbsp;
                                                   	<%if(loincCode != null){ %>
                                                   	<a href="javascript:popupStart('660','1000','http://apps.nlm.nih.gov/medlineplus/services/mpconnect.cfm?mainSearchCriteria.v.cs=2.16.840.1.113883.6.1&mainSearchCriteria.v.c=<%=loincCode%>&informationRecipient.languageCode.c=en')"> info</a>
                                                   	<%} %>
                                                   </td>
                                                   <td align="right"><%= handler.getOBXResult( j, k) %></td>
                                                   <td align="center" valign="top"><%= handler.getOBXAbnormalFlag(j, k)%></td>
                                                   <td align="left" valign="top"><%=handler.getOBXReferenceRange( j, k)%></td>
                                                   <td align="left" valign="top"><%=handler.getOBXUnits( j, k) %></td>
                                                   <td align="center" valign="top"><%= handler.getTimeStamp(j, k) %></td>
                                                   <td align="center" valign="top"><%= handler.getOBXResultStatus( j, k) %></td>
                                                   <td align="center" valign="top">
   	                                                <a href="javascript:void(0);" title="Annotation" onclick="window.open('<%=request.getContextPath()%>/annotation/annotation.jsp?display=<%=annotation_display%>&amp;table_id=<%=segmentID%>&amp;demo=<%=demographicID%>&amp;other_id=<%=String.valueOf(j) + "-" + String.valueOf(k) %>','anwin','width=400,height=500');">
   	                                                	<%if(!isPrevAnnotation){ %><img src="../../../images/notes.gif" alt="rxAnnotation" height="16" width="13" border="0"/><%}else{ %><img src="../../../images/filledNotes.gif" alt="rxAnnotation" height="16" width="13" border="0"/> <%} %>
   	                                                </a>
                                                   </td>
                                               </tr>
                                           <%}%>
                                           <%for (l=0; l < handler.getOBXCommentCount(j, k); l++){%>
                                               <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes" >
                                                   <td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><%=handler.getOBXComment(j, k, l)%></pre></td>
                                               </tr>
                                           <%}
                                   
                                      } else if (handler.getMsgType().equals("PFHT") || handler.getMsgType().equals("HHSEMR") || handler.getMsgType().equals("CML")) {
                                   	   if (!obxName.equals("")) { %>
	                                    		<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
		                                            <td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier=<%= URLEncoder.encode(handler.getOBXIdentifier(j, k).replaceAll("&","%26"),"UTF-8") %>')"><%=obxName %></a>
		                                            &nbsp;
		                                            <%if(loincCode != null){ %>
                                                	<a href="javascript:popupStart('660','1000','http://apps.nlm.nih.gov/medlineplus/services/mpconnect.cfm?mainSearchCriteria.v.cs=2.16.840.1.113883.6.1&mainSearchCriteria.v.c=<%=loincCode%>&informationRecipient.languageCode.c=en')"> info</a>
                                                	<%} %> </td>
		                                            <td align="right"><%= handler.getOBXResult( j, k) %></td>

		                                            <td align="center">
		                                                    <%= handler.getOBXAbnormalFlag(j, k)%>
		                                            </td>
		                                            <td align="left"><%=handler.getOBXReferenceRange( j, k)%></td>
		                                            <td align="left"><%=handler.getOBXUnits( j, k) %></td>
		                                            <td align="center"><%= handler.getTimeStamp(j, k) %></td>
		                                            <td align="center"><%= handler.getOBXResultStatus( j, k) %></td>
		                                            <td align="center" valign="top">
	                                                <a href="javascript:void(0);" title="Annotation" onclick="window.open('<%=request.getContextPath()%>/annotation/annotation.jsp?display=<%=annotation_display%>&amp;table_id=<%=segmentID%>&amp;demo=<%=demographicID%>&amp;other_id=<%=String.valueOf(j) + "-" + String.valueOf(k) %>','anwin','width=400,height=500');">
	                                                	<%if(!isPrevAnnotation){ %><img src="../../../images/notes.gif" alt="rxAnnotation" height="16" width="13" border="0"/><%}else{ %><img src="../../../images/filledNotes.gif" alt="rxAnnotation" height="16" width="13" border="0"/> <%} %>
	                                                </a>
                                                </td>
	                                       		 </tr>

                                   	 	<%} else { %>
                                   		   <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
	      	                                     <td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><%=handler.getOBXResult( j, k)%></pre></td>

	      	                                   </tr>
                                   	 	<%}
	                                    	if (!handler.getNteForOBX(j,k).equals("") && handler.getNteForOBX(j,k)!=null) { %>
		                                       <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
		                                       		<td valign="top" align="left"colspan="8"><pre  style="margin:0px 0px 0px 100px;"><%=handler.getNteForOBX(j,k)%></pre></td>
		                                       </tr>
		                                    <% }
			                                for (l=0; l < handler.getOBXCommentCount(j, k); l++){%>
			                                     <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
			                                        <td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><%=handler.getOBXComment(j, k, l)%></pre></td>
			                                     </tr>
			                                <%}

                                   } else if ((!handler.getOBXResultStatus(j, k).equals("TDIS") && handler.getMsgType().equals("Spire")) )  { %>
											<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
                                           <td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier=<%= URLEncoder.encode(handler.getOBXIdentifier(j, k).replaceAll("&","%26"),"UTF-8") %>')"><%=obxName %></a>
                                           &nbsp;<%if(loincCode != null){ %>
                                                	<a href="javascript:popupStart('660','1000','http://apps.nlm.nih.gov/medlineplus/services/mpconnect.cfm?mainSearchCriteria.v.cs=2.16.840.1.113883.6.1&mainSearchCriteria.v.c=<%=loincCode%>&informationRecipient.languageCode.c=en')"> info</a>
                                                	<%} %> </td>
                                           <% 	if (handler.getOBXResult( j, k).length() > 20) {
													%>

													<td align="left" colspan="4"><%= handler.getOBXResult( j, k) %></td>

													<% 	String abnormalFlag = handler.getOBXAbnormalFlag(j, k);
														if (abnormalFlag != null && abnormalFlag.length() > 0) {
													 %>
		                                           <td align="center">
		                                                   <%= abnormalFlag%>
		                                           </td>
		                                           <% } %>

		                                           <% 	String refRange = handler.getOBXReferenceRange(j, k);
														if (refRange != null && refRange.length() > 0) {
													 %>
		                                           <td align="left"><%=refRange%></td>
		                                           <% } %>

		                                           <% 	String units = handler.getOBXUnits(j, k);
														if (units != null && units.length() > 0) {
													 %>
		                                           <td align="left"><%=units %></td>
		                                           <% } %>
												<%
												} else {
												%>
												   <td align="right" colspan="1"><%= handler.getOBXResult( j, k) %></td>
		                                           <td align="center"> <%= handler.getOBXAbnormalFlag(j, k)%> </td>
		                                           <td align="left"> <%=handler.getOBXReferenceRange(j, k)%> </td>
		                                           <td align="left"> <%=handler.getOBXUnits(j, k) %> </td>
												<%
												}
												%>

                                           <td align="center"><%= handler.getTimeStamp(j, k) %></td>
                                           <td align="center"><%= handler.getOBXResultStatus(j, k) %></td>

                                      		<td align="center" valign="top">
	                                                <a href="javascript:void(0);" title="Annotation" onclick="window.open('<%=request.getContextPath()%>/annotation/annotation.jsp?display=<%=annotation_display%>&amp;table_id=<%=segmentID%>&amp;demo=<%=demographicID%>&amp;other_id=<%=String.valueOf(j) + "-" + String.valueOf(k) %>','anwin','width=400,height=500');">
	                                                	<%if(!isPrevAnnotation){ %><img src="../../../images/notes.gif" alt="rxAnnotation" height="16" width="13" border="0"/><%}else{ %><img src="../../../images/filledNotes.gif" alt="rxAnnotation" height="16" width="13" border="0"/> <%} %>
	                                                </a>
                                                </td>
                                       </tr>

                                       <%for (l=0; l < handler.getOBXCommentCount(j, k); l++){%>
                                            <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
                                               <td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><%=handler.getOBXComment(j, k, l)%></pre></td>
                                            </tr>
                                       <%}


                                    } else if ((!handler.getOBXResultStatus(j, k).equals("TDIS") && !handler.getMsgType().equals("EPSILON")) )  {

                                    	if(isUnstructuredDoc){%>
                                   			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%="NarrativeRes"%>"><% 
                                   			if(handler.getOBXIdentifier(j, k).equalsIgnoreCase(handler.getOBXIdentifier(j, k-1)) && (obxCount>1)){%>
                                   					<td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier='<%= URLEncoder.encode(handler.getOBXIdentifier(j, k).replaceAll("&","%26"),"UTF-8")%>')"></a><%
                                   			}else{%> 
                                   					<td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier=<%= URLEncoder.encode(handler.getOBXIdentifier(j, k).replaceAll("&","%26"),"UTF-8") %>')"><%=obxName %></a>
                                   			<%}%>
											<%if(isVIHARtf){
											    //create bytes from the rtf string
										    	byte[] rtfBytes = handler.getOBXResult(j, k).getBytes();
										    	ByteArrayInputStream rtfStream = new ByteArrayInputStream(rtfBytes);
										    	
										    	//Use RTFEditor Kit to get plaintext from RTF
										    	RTFEditorKit rtfParser = new RTFEditorKit();
										    	javax.swing.text.Document doc = rtfParser.createDefaultDocument();
										    	rtfParser.read(rtfStream, doc, 0);
										    	String rtfText = doc.getText(0, doc.getLength()).replaceAll("\n", "<br>");
										    	String disclaimer = "<br>IMPORTANT DISCLAIMER: You are viewing a PREVIEW of the original report. The rich text formatting contained in the original report may convey critical information that must be considered for clinical decision making. Please refer to the ORIGINAL report, by clicking 'Print', prior to making any decision on diagnosis or treatment.";%>
										    	<td align="left"><%= rtfText + disclaimer %></td><%} %><%
											else{%>
                                           		<td align="left"><%= handler.getOBXResult( j, k) %></td><%} %>
                                           	<%if(handler.getTimeStamp(j, k).equals(handler.getTimeStamp(j, k-1)) && (obxCount>1)){
                                        			%><td align="center"></td><%}
	                                       		else{%> <td align="center"><%= handler.getTimeStamp(j, k) %></td><%}%>
	                                          	
                                           	<td align="center" valign="top">
	                                          		<a href="javascript:void(0);" title="Annotation" onclick="window.open('<%=request.getContextPath()%>/annotation/annotation.jsp?display=<%=annotation_display%>&amp;table_id=<%=segmentID%>&amp;demo=<%=demographicID%>&amp;other_id=<%=String.valueOf(j) + "-" + String.valueOf(k) %>','anwin','width=400,height=500');">
			                                        	<%if(!isPrevAnnotation){ %><img src="../../../images/notes.gif" alt="rxAnnotation" height="16" width="13" border="0"/><%}else{ %><img src="../../../images/filledNotes.gif" alt="rxAnnotation" height="16" width="13" border="0"/> <%} %>
			                                        </a>
			                                    </td>
			                                           	
	                                          	
	                                  			<% }//end of isUnstructuredDoc
                                   			
                                   			else{//if it isn't a PATHL7 doc%>

                               		<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
                               		
                               		
                               		
                               		<%
                               			if(handler.getMsgType().equals("PATHL7") && !isAllowedDuplicate && (obxCount>1) && handler.getOBXIdentifier(j, k).equalsIgnoreCase(handler.getOBXIdentifier(j, k-1)) && (handler.getOBXValueType(j, k).equals("TX") || handler.getOBXValueType(j, k).equals("FT"))){%>

                                   				<td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier=<%= URLEncoder.encode(handler.getOBXIdentifier(j, k).replaceAll("&","%26"),"UTF-8") %>')"></a><%
                                   				}
                               				else{
                               				
                               				 
                               					if(handler instanceof AlphaHandler && lastObxSetId.equals(((AlphaHandler)handler).getObxSetId(j, k))) {
                               						%><td></td><%
                               					} else {
                               				
                               				%>
                                           <td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier=<%= URLEncoder.encode(handler.getOBXIdentifier(j, k).replaceAll("&","%26"),"UTF-8") %>')"><%=obxName %></a>
                                           &nbsp;<%if(loincCode != null){ %>
                                                	<a href="javascript:popupStart('660','1000','http://apps.nlm.nih.gov/medlineplus/services/mpconnect.cfm?mainSearchCriteria.v.cs=2.16.840.1.113883.6.1&mainSearchCriteria.v.c=<%=loincCode%>&informationRecipient.languageCode.c=en')"> info</a>
                                                	<%} %> </td>
                                           <%} }%>
                                           
                                           
                                           <% if(handler instanceof AlphaHandler && "FT".equals(handler.getOBXValueType(j, k))) { %>
                                           		<td colspan="4"><%= handler.getOBXResult( j, k) %></td>
                                           <%
                                       			lastObxSetId = ((AlphaHandler)handler).getObxSetId(j,k);
												
                                           } else if(handler.getMsgType().equals("MEDITECH")  && isUnstructuredDoc ) { %>
                                           	
                                           	
				                                        <pre>
					                             		<%= handler.getOBXResult(j,k) %>
					                             		</pre>  
					                             		                                                 
											<% } else if(handler.getMsgType().equals("MEDITECH")  && ((MEDITECHHandler) handler).isReportData() ) { %>
				                                    	<tr>
				                                    		<td>
					                             				<%= handler.getOBXResult(j,k) %>
					                             			</td>
					                             		</tr>
				                                 
                                           	<%		
                                           		} else {
                                           	%>
                                           	
                                           	<%
                                           	String align = "right";
                                          	//for pathl7, if it is an SG/CDC result greater than 100 characters, left justify it
                                           	if((handler.getOBXResult(j, k).length() > 100) && (isSGorCDC)){
                                           		align="left";
                                           	}%>

                                           <td align="<%=align%>"><%= handler.getOBXResult( j, k) %></td>
                                          
                                           <td align="center">
                                                   <%= handler.getOBXAbnormalFlag(j, k)%>
                                           </td>
                                           <td align="left"><%=handler.getOBXReferenceRange( j, k)%></td>
                                           <td align="left"><%=handler.getOBXUnits( j, k) %></td>
                                           
                                           <%}%>
                                           
                                           <td align="center"><%= handler.getTimeStamp(j, k) %></td>
                                           <td align="center"><%= handler.getOBXResultStatus( j, k) %></td>
                                      		<td align="center" valign="top">                                           <a href="javascript:void(0);" title="Annotation" onclick="window.open('<%=request.getContextPath()%>/annotation/annotation.jsp?display=<%=annotation_display%>&amp;table_id=<%=segmentID%>&amp;demo=<%=demographicID%>&amp;other_id=<%=String.valueOf(j) + "-" + String.valueOf(k) %>','anwin','width=400,height=500');">
	                                                	<%if(!isPrevAnnotation){ %><img src="../../../images/notes.gif" alt="rxAnnotation" height="16" width="13" border="0"/><%}else{ %><img src="../../../images/filledNotes.gif" alt="rxAnnotation" height="16" width="13" border="0"/> <%} %>
	                                                </a>
                                                </td>
                                       </tr>

										<%}

                                        for (l=0; l < handler.getOBXCommentCount(j, k); l++){%>
                                        <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
                                           <td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><%=handler.getOBXComment(j, k, l)%></pre></td>
                                        </tr>
                                   		<%}


                                    } else { %>
                                       	<%for (l=0; l < handler.getOBXCommentCount(j, k); l++){
                                       			if (!handler.getOBXComment(j, k, l).equals("")) {
                                       		%>
                                            <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="TDISRes">
                                               <td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><%=handler.getOBXComment(j, k, l)%></pre></td>
                                            	<td align="center" valign="top">
	                                                <a href="javascript:void(0);" title="Annotation" onclick="window.open('<%=request.getContextPath()%>/annotation/annotation.jsp?display=<%=annotation_display%>&amp;table_id=<%=segmentID%>&amp;demo=<%=demographicID%>&amp;other_id=<%=String.valueOf(1) + "-" + String.valueOf(1) %>','anwin','width=400,height=500');">
	                                                	<%if(!isPrevAnnotation){ %><img src="../../../images/notes.gif" alt="rxAnnotation" height="16" width="13" border="0"/><%}else{ %><img src="../../../images/filledNotes.gif" alt="rxAnnotation" height="16" width="13" border="0"/> <%} %>
	                                                </a>
                                             </td>
                                            </tr>
                                       			<%}
                                       	} 
                                       	
                                       
                                       	%>
								 <%  }

                                  }
                                    
                           } // end for (k=0; k < obxCount; k++){

                           if (!handler.getMsgType().equals("PFHT")) {
                               if (headers.get(i).equals(handler.getObservationHeader(j, 0))) {
                               	 %>
                               <%for (k=0; k < handler.getOBRCommentCount(j); k++){
                                   // the obrName should only be set if it has not been
                                   // set already which will only have occured if the
                                   // obx name is "" or if it is the same as the obr name
                                   if(!obrFlag && handler.getOBXName(j, 0).equals("")){  %>
                                       <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" >
                                           <td valign="top" align="left"><%=handler.getOBRName(j)%> </td>
                                           <td colspan="6">&nbsp;</td>
                                       </tr>
                                       <%obrFlag = true;
                                   }%>
                               <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
                                   <td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><%=handler.getOBRComment(j, k)%></pre></td>
                               </tr>
                               <% if  (!handler.getMsgType().equals("HHSEMR") || !handler.getMsgType().equals("TRUENORTH")) {
                               		if(handler.getOBXName(j,k).equals("")){
	                                        String result = handler.getOBXResult(j, k);%>
	                                         <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" >
	                                                 <td colspan="7" valign="top"  align="left"><%=result%></td>
	                                         </tr>
	                              		<%}
                               	}
                                } // end for (k=0; k < handler.getOBRCommentCount(j); k++)
                             }//end if (headers.get(i).equals(handler.getObservationHeader(j, 0))) {
                          } // end (!handler.getMsgType().equals("PFHT"))
                        	
                               
                       } //end for ( j=0; j < OBRCount; j++)
                            		  
                    } // end for(i=0;i<headers.size();i++)
                         
							if (handler.getMsgType().equals("Spire")) {

								int numZDS = ((SpireHandler)handler).getNumZDSSegments();
								String lineClass = "NormalRes";
								int lineNumber = 0;
								MiscUtils.getLogger().info("HERE: " + numZDS);

								if (numZDS > 0) { %>
									<tr class="Field2">
		                               <td width="25%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formTestName"/></td>
		                               <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formResult"/></td>
		                               <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formProvider"/></td>
		                               <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formDateTimeCompleted"/></td>
		                               <td width="6%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formNew"/></td>
		                            </tr>
								<%
								}

								for (int m=0; m < numZDS; m++) {
									%>
									<tr bgcolor="<%=(lineNumber % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
										<td valign="top" align="left"> <%=((SpireHandler)handler).getZDSName(m)%> </td>
										<td align="right"><%= ((SpireHandler)handler).getZDSResult(m) %></td>
										<td align="center"><%= ((SpireHandler)handler).getZDSProvider(m) %></td>
										<td align="center"><%= ((SpireHandler)handler).getZDSTimeStamp(m) %></td>
										<td align="center"><%= ((SpireHandler)handler).getZDSResultStatus(m) %></td>
									</tr>
									<%
									lineNumber++;
								}
							} // end (handler.getMsgType().equals("Spire")

                           %> 
                           </table> 
                           <%

                } // end for handler.getMsgType().equals("MEDVUE")

                       %>
<%-- FOOTER --%>
                        <table width="100%" border="0" cellspacing="0" cellpadding="3" class="MainTableBottomRowRightColumn" bgcolor="#003399">
                            <tr>
                                <td align="left" width="50%">
                                    <% if ( !ackFlag ) { %>
                                    <input type="button" value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge" />" onclick="<%=ackLabFunc%>" >
                                    <input type="button" value="<bean:message key="oscarMDS.segmentDisplay.btnComment"/>" onclick="return getComment('addComment',<%=segmentID%>);">
                                    <% } %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="popupStart(397, 700, '../../../oscarMDS/SelectProvider.jsp?docId=<%=segmentID%>&labDisplay=true', 'providerselect')">
                                    <input type="button" value=" <bean:message key="global.btnClose"/> " onClick="window.close()">
                                    <input type="button" value=" <bean:message key="global.btnPrint"/> " onClick="printPDF('<%=segmentID%>')">
                                        <indivo:indivoRegistered demographic="<%=demographicID%>" provider="<%=providerNo%>">
                                        <input type="button" value="<bean:message key="global.btnSendToPHR"/>" onClick="sendToPHR('<%=segmentID%>', '<%=demographicID%>')">
                                        </indivo:indivoRegistered>
                                    <% if ( searchProviderNo != null ) { // we were called from e-chart %>
                                    <input type="button" value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> " onClick="popupStart(360, 680, '../../../oscarMDS/SearchPatient.do?labType=HL7&segmentID=<%= segmentID %>&name=<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>', 'encounter')">

                                    <% } %>
                                </td>
                                <td width="50%" valign="center" align="left">
                                <% if ("CLS".equals(handler.getMsgType())) { %>
									<span class="Field2"><i><bean:message key="oscarMDS.segmentDisplay.msgReportEndCLS"/></i></span>
								<% } else { %>
									<span class="Field2"><i><bean:message key="oscarMDS.segmentDisplay.msgReportEnd"/></i></span>
								<% } %>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

        </form>      
        
        <%String s = ""+System.currentTimeMillis();%>
        <a style="color:white;" href="javascript: void(0);" onclick="showHideItem('rawhl7<%=s%>');" >show</a>
        <pre id="rawhl7<%=s%>" style="display:none;"><%=hl7%></pre>
        </div>
        <%} %>
        
    </body>
</html>
<%!
    public String[] divideStringAtFirstNewline(String s){
        int i = s.indexOf("<br />");
        String[] ret  = new String[2];
        if(i == -1){
               ret[0] = new String(s);
               ret[1] = null;
            }else{
               ret[0] = s.substring(0,i);
               ret[1] = s.substring(i+6);
            }
        return ret;
    }
%>
 <%--
    AD Address
    CE Coded Entry
    CF Coded Element With Formatted Values
    CK Composite ID With Check Digit
    CN Composite ID And Name
    CP Composite Price
    CX Extended Composite ID With Check Digit
    DT Date
    ED Encapsulated Data
    FT Formatted Text (Display)
    MO Money
    NAR Narrative (not official HL7. Added for IHA POI interface)
    NM Numeric
    PN Person Name
    RP Reference Pointer
    SN Structured Numeric
    ST String Data.
    TM Time
    TN Telephone Number
    TS Time Stamp (Date & Time)
    TX Text Data (Display)
    XAD Extended Address
    XCN Extended Composite Name And Number For Persons
    XON Extended Composite Name And Number For Organizations
    XPN Extended Person Number
    XTN Extended Telecommunications Number
 --%>
