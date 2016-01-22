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

<%@page import="org.oscarehr.common.model.PatientLabRouting"%>
<%@page import="oscar.util.ConversionUtils"%>
<%@page import="org.oscarehr.common.dao.PatientLabRoutingDao"%>
<%@page errorPage="../provider/errorpage.jsp" %>
<%@ page import="java.util.*,
		 java.sql.*,
		 oscar.oscarDB.*,
		 oscar.oscarLab.ca.all.*,
		 oscar.oscarLab.ca.all.util.*,org.oscarehr.util.SpringUtils,
		 oscar.oscarLab.ca.all.parsers.*,
		 oscar.oscarLab.LabRequestReportLink,
		 oscar.oscarMDS.data.ReportStatus,oscar.log.*,
         oscar.OscarProperties,
		 org.apache.commons.codec.binary.Base64,org.oscarehr.common.dao.Hl7TextInfoDao,org.oscarehr.common.model.Hl7TextInfo,
		 org.oscarehr.common.dao.UserPropertyDAO, org.oscarehr.common.model.UserProperty,
		javax.swing.text.rtf.RTFEditorKit,
		java.io.ByteArrayInputStream"%>
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
oscar.OscarProperties props = oscar.OscarProperties.getInstance();
String segmentID = request.getParameter("segmentID");
String providerNo = request.getParameter("providerNo");
String searchProviderNo = request.getParameter("searchProviderNo");
String patientMatched = request.getParameter("patientMatched");

UserPropertyDAO userPropertyDAO = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
UserProperty uProp = userPropertyDAO.getProp(providerNo, UserProperty.LAB_ACK_COMMENT);
boolean skipComment = false;
if( uProp != null && uProp.getValue().equalsIgnoreCase("yes")) {
	skipComment = true;
}

String ackLabFunc;
if( skipComment ) {
	ackLabFunc = "handleLab('acknowledgeForm_" + segmentID + "','" + segmentID + "','ackLab');";
}
else {
	ackLabFunc = "getComment('" + segmentID + "','ackLab');";
}

Long reqIDL = LabRequestReportLink.getIdByReport("hl7TextMessage",Long.valueOf(segmentID.trim()));
String reqID = reqIDL==null ? "" : reqIDL.toString();
reqIDL = LabRequestReportLink.getRequestTableIdByReport("hl7TextMessage",Long.valueOf(segmentID.trim()));
String reqTableID = reqIDL==null ? "" : reqIDL.toString();

PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class);
String demographicID = "";
for(PatientLabRouting r : dao.findByLabNoAndLabType(ConversionUtils.fromIntString(segmentID), "HL7")) {
    demographicID = "" + r.getDemographicNo();
}

boolean isLinkedToDemographic=false;
if(demographicID != null && !demographicID.equals("")&& !demographicID.equals("0")){
    isLinkedToDemographic=true;
    LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_HL7_LAB, segmentID, request.getRemoteAddr(),demographicID);
}else{
    LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_HL7_LAB, segmentID, request.getRemoteAddr());
}

boolean ackFlag = false;
ArrayList ackList = AcknowledgementData.getAcknowledgements(segmentID);
String labStatus = "";
if (ackList != null){
    for (int i=0; i < ackList.size(); i++){
        ReportStatus reportStatus = (ReportStatus) ackList.get(i);
        if ( reportStatus.getProviderNo().equals(providerNo) ) {
            labStatus = reportStatus.getStatus();
            if(labStatus.equals("A") ){
            	ackFlag = true;
            	break;
            }
        }
    }
}

String multiLabId = Hl7textResultsData.getMatchingLabs(segmentID);

MessageHandler handler = Factory.getHandler(segmentID);
String hl7 = Factory.getHL7Body(segmentID);
Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao) SpringUtils.getBean("hl7TextInfoDao");
int lab_no = Integer.parseInt(segmentID);
String label = ""; Hl7TextInfo hl7Lab = hl7TextInfoDao.findLabId(lab_no);
if (hl7Lab.getLabel()!=null) label = hl7Lab.getLabel();
// check for errors printing
if (request.getAttribute("printError") != null && (Boolean) request.getAttribute("printError")){
%>
<script language="JavaScript">
    alert("The lab could not be printed due to an error. Please see the server logs for more detail.");
</script>
<%}
%>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/jquery/jquery-1.4.2.js"></script>

                <script language="JavaScript">
         popupStart=function(vheight,vwidth,varpage,windowname) {
            var page = varpage;
            windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
            var popup=window.open(varpage, windowname, windowprops);
        }
         
         
     	<%
     		int version = 0;
     		if( multiLabId != null ) {
     		String[] multiID = multiLabId.split(",");
     		    if( multiID.length > 1 ) {
     		    	for( int k = 0; k < multiID.length; ++k ) {
     					if( multiID[k].equals(segmentID)) {     	
     						version = k;     	
     					}
     				}
     			}
     		}
     	%>
     	
         getComment=function(labid, action) {
            var ret = true;            
            var text = "V" + <%=version%> + "commentText" + labid + $("providerNo").value;
            
            var commentVal = "";
            
            if( $(text) != null ) {
            	commentVal = $(text).innerHTML;
            	if( commentVal == null ) {
            		commentVal = "";
            	}
            }
            var commentID = "comment_" + labid;
            
            var comment = prompt('<bean:message key="oscarMDS.segmentDisplay.msgComment"/>', commentVal);
			
            if( comment == null )
                ret = false;
            else if ( comment != null && comment.length > 0 ){
                $(commentID).value = comment;
            }
            else {
            	$(commentID).value = commentVal;
            }
            if(ret)
                handleLab('acknowledgeForm_'+labid,labid,action);

            return false;
        }

         printPDF=function(doclabid){
            document.forms['acknowledgeForm_'+doclabid].action="../lab/CA/ALL/PrintPDF.do";
            document.forms['acknowledgeForm_'+doclabid].submit();
        }

	 linkreq=function(rptId, reqId) {
	    var link = "../lab/LinkReq.jsp?table=hl7TextMessage&rptid="+rptId+"&reqid="+reqId;
	    window.open(link, "linkwin", "width=500, height=200");
	}

         sendToPHR=function(labId, demographicNo) {
            popup(300, 600, "<%=request.getContextPath()%>/phr/SendToPhrPreview.jsp?labId=" + labId + "&demographic_no=" + demographicNo, "sendtophr");
        }
        popupStart=function(vheight,vwidth,varpage,windowname) {
            var windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
            var popup=window.open(varpage, windowname, windowprops);
        }
        handleLab=function(formid,labid,action){
            var url='../dms/inboxManage.do';
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
                                                                                	$("status_"+labid).value = "A";
                                                                                    updateStatus(formid);
                                                                                }
                                                                            }else if(action=='msgLab'){
                                                                                demoid=json.demoId;
                                                                                if(demoid!=null && demoid.length>0)
                                                                                    window.popup(700,960,'../oscarMessenger/SendDemoMessage.do?demographic_no='+demoid,'msg');
                                                                            }else if(action=='ticklerLab'){
                                                                                demoid=json.demoId;
                                                                                if(demoid!=null && demoid.length>0)
                                                                                    window.popup(450,600,'../tickler/ForwardDemographicTickler.do?docType=HL7&docId='+labid+'&demographic_no='+demoid,'tickler')
                                                                            }
                                                                            else if(action == 'addComment') {
                                                                            	addComment(formid,labid);
                                                                            }

                                                                        }else{
                                                                            if(action=='ackLab'){
                                                                                if(confirmAckUnmatched()) {
                                                                                	$("status_"+labid).value = "A";
                                                                                    updateStatus(formid);
                                                                                }
                                                                                else{
                                                                                    var pn=$("demoName"+labid).value;
                                                                                    if(pn) popupStart(360, 680, '../oscarMDS/SearchPatient.do?labType=HL7&segmentID='+labid+'&name='+pn, 'searchPatientWindow');
                                                                                }
                                                                            }else{
                                                                                alert("Please relate lab to a demographic.");
                                                                                //pop up relate demo window
                                                                                var pn=$("demoName"+labid).value;
                                                                                if(pn) popupStart(360, 680, '../oscarMDS/SearchPatient.do?labType=HL7&segmentID='+labid+'&name='+pn, 'searchPatientWindow');
                                                                            }
                                                                        }
                                                                    }
                                                            }});
        }
        
        function addComment(formid,labid) {
        	var url='<%=request.getContextPath()%>'+"/oscarMDS/UpdateStatus.do?method=addComment";
        	var status = "status_" + labid;
        	
			if( $F(status) == "" ) {
				$(status).value = "N";
			}
        	var data=$(formid).serialize(true);
        	
        	var label = "V" + <%=version%> + "commentLabel" + labid + $F("providerNo");
        	var text = "V" + <%=version%> + "commentText" + labid + $("providerNo").value;
			var commentID = "comment_" + labid;
			var newComment;
			
            new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){            	
            	if( $(label) != null && $(text) != null ) {            		
            		newComment = $(commentID).value;
            		$(label).update("comment: ");            		            		
            		$(text).update(newComment);            		
            	}
            	else {
            		alert("Comment '" + $(commentID).value + "' added!\nThis lab has been forwarded to you.");
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
        confirmAckUnmatched=function(){
            return confirm('<bean:message key="oscarMDS.index.msgConfirmAcknowledgeUnmatched"/>');
        }
        updateStatus=function(formid){
            var url='<%=request.getContextPath()%>'+"/oscarMDS/UpdateStatus.do";
            var data=$(formid).serialize(true);

            new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                    var num=formid.split("_");
                 if(num[1]){
                     Effect.BlindUp('labdoc_'+num[1]);
                     //updateDocLabData(num[1]);
                     refreshCategoryList();

                }
        }});

        }

        createTdisLabel=function(tdisformid,ackformid,labelspanid,labelid){
        	document.forms[tdisformid].label.value = document.forms[ackformid].label.value;
        	var url = '<%=request.getContextPath()%>'+"/lab/CA/ALL/createLabelTDIS.do";
        	var data=$(tdisformid).serialize(true);
        	new Ajax.Request(url,{method:'post', parameters:data

        	});
        	document.getElementById(labelspanid).innerHTML= "<i> Label: "+document.getElementById(labelid).value+"</i>";
        	document.getElementById(labelid).value="";

        };
        </script>


    <div id="labdoc_<%=segmentID%>">
        <!-- form forwarding of the lab -->
        <form name="reassignForm_<%=segmentID%>" >
            <input type="hidden" name="flaggedLabs" value="<%= segmentID %>" />
            <input type="hidden" name="selectedProviders" value="" />
            <input type="hidden" name="labType" value="HL7" />
            <input type="hidden" name="labType<%= segmentID %>HL7" value="imNotNull" />
            <input type="hidden" name="providerNo" id="providerNo" value="<%= providerNo %>" />
            <input type="hidden" name="ajax" value="yes" />
        </form>
        <form name="TDISLabelForm" id="TDISLabelForm<%=segmentID%>" method='POST' onsubmit="createTdisLabel('TDISLabelForm<%=segmentID%>');" action="javascript:void(0);">
			<input type="hidden" id="labNum" name="lab_no" value="<%=lab_no%>">
			<input type="hidden" id="label" name="label" value="<%=label%>">
		</form>
        <form name="acknowledgeForm" id="acknowledgeForm_<%=segmentID%>" onsubmit="javascript:void(0);" method="post" action="javascript:void(0);">

            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="3">
                            <tr>
                                <td align="left" class="MainTableTopRowRightColumn" width="100%">
                                    <input type="hidden" name="segmentID" value="<%= segmentID %>"/>
                                    <input type="hidden" name="multiID" value="<%= multiLabId %>" />
                                    <input type="hidden" name="providerNo" value="<%= providerNo %>"/>
                                    <input type="hidden" name="status" value="<%=labStatus%>"/ id="status_<%=segmentID%>">
                                    <input type="hidden" name="comment" value=""/ id="comment_<%=segmentID%>">
                                    <input type="hidden" name="labType" value="HL7"/>
                                    <input type="hidden" name="ajaxcall" value="yes"/>
                                    <input type="hidden" id="demoName<%=segmentID%>" value="<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>"/>
                                    <% if ( !ackFlag ) { %>
                                    <input type="button" value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>" onclick="<%=ackLabFunc%>">
                                    <input type="button" value="<bean:message key="oscarMDS.segmentDisplay.btnComment"/>" onclick="return getComment('<%=segmentID%>','addComment');">
                                    <% } %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="popupStart(300, 400, '../oscarMDS/SelectProviderAltView.jsp?doc_no=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>', 'providerselect')">
                                    <input type="button" value=" <bean:message key="global.btnPrint"/> " onClick="printPDF('<%=segmentID%>')">

                                    <input type="button" value="Msg" onclick="handleLab('','<%=segmentID%>','msgLab');"/>
                                    <input type="button" value="Tickler" onclick="handleLab('','<%=segmentID%>','ticklerLab');"/>

                                    <% if ( searchProviderNo != null ) { // null if we were called from e-chart%>
                                    <input type="button" value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> " onClick="popupStart(360, 680, '../oscarMDS/SearchPatient.do?labType=HL7&segmentID=<%= segmentID %>&name=<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>', 'searchPatientWindow')">
                                    <% } %>
				    <input type="button" value="Req# <%=reqTableID%>" title="Link to Requisition" onclick="linkreq('<%=segmentID%>','<%=reqID%>');" />
                                    <% if (!label.equals(null) && !label.equals("")) { %>
				<button type="button" id="createLabel" value="Label" onClick="createTdisLabel('TDISLabelForm<%=segmentID%>','acknowledgeForm_<%=segmentID%>','labelspan_<%=segmentID%>','label_<%=segmentID%>')">Label</button>
				<%} else { %>
				<button type="button" id="createLabel" style="background-color:#6699FF" value="Label" onClick="createTdisLabel('TDISLabelForm<%=segmentID%>','acknowledgeForm_<%=segmentID%>','labelspan_<%=segmentID%>','label_<%=segmentID%>')">Label</button>
				<%} %>
                 <input type="text" id="label_<%=segmentID%>" name="label" value=""/>
                 <% String labelval="";
                 if (label!="" && label!=null) {
                 	labelval = label;
                 }else {
                	 labelval = "(not set)";

                 } %>
                 <span id="labelspan_<%=segmentID%>" class="Field2"><i>Label: <%=labelval%> </i></span>
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
                                                        	%><a href="javascript:void(0);"   onclick="popup(850, 950, '../lab/CA/ALL/labDisplay.jsp?segmentID=<%=multiID[i]%>&multiID=<%=multiLabId%>&providerNo=<%= providerNo %>&searchProviderNo=<%= searchProviderNo %>', 'labVersion');">v<%= i+1 %></a>&#160;<%
                                                        }else{
                                                            %><a href="javascript:void(0);"  onclick="popup(850, 950, '../lab/CA/ALL/labDisplay.jsp?segmentID=<%=multiID[i]%>&multiID=<%=multiLabId%>&providerNo=<%= providerNo %>', 'labVersion');"  >v<%= i+1 %></a>&#160;<%
                                                        }
                                                    }
                                                }
                                                if( multiID.length > 1 ) {
                                                    if ( searchProviderNo != null ) { // null if we were called from e-chart
                                                        %><a href="javascript:void(0);" onclick="popup(850, 950, '../lab/CA/ALL/labDisplay.jsp?segmentID=<%=segmentID%>&multiID=<%=multiLabId%>&providerNo=<%= providerNo %>&searchProviderNo=<%= searchProviderNo %>&all=true', 'labVersion');">All</a>&#160;<%
                                                    }else{
                                                        %><a href="javascript:void(0);" onclick="popup(850, 950, '../lab/CA/ALL/labDisplay.jsp?segmentID=<%=segmentID%>&multiID=<%=multiLabId%>&providerNo=<%= providerNo %>&all=true', 'labVersion');">All</a>&#160;<%
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
                                                <table width="100%" border="0" cellpadding="2" cellspacing="0" valign="top">
                                                    <tr>
                                                        <td valign="top" align="left">
                                                            <table width="100%" border="0" cellpadding="2" cellspacing="0" valign="top"  <% if ( demographicID.equals("") || demographicID.equals("0")){ %> bgcolor="orange" <% } %> id="DemoTable<%=segmentID%>" >
                                                                <tr>
                                                                    <td nowrap>
                                                                        <div class="FieldData">
                                                                            <strong><bean:message key="oscarMDS.segmentDisplay.formPatientName"/>: </strong>
                                                                        </div>
                                                                    </td>
                                                                    <td nowrap>
                                                                        <div class="FieldData" nowrap="nowrap">
                                                                            <% if ( searchProviderNo == null ) { // we were called from e-chart
                                                                                %>
                                                                            <a href="javascript:window.close()"> <% } else { // we were called from lab module
    %></a>
                                                                            <a href="javascript:popupStart(360, 680, '../oscarMDS/SearchPatient.do?labType=HL7&segmentID=<%= segmentID %>&name=<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>', 'searchPatientWindow')">
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
                                                                        <div align="left" class="FieldData" nowrap="nowrap">
                                                                        </div>
                                                                    </td>
                                                                    <td nowrap>
                                                                        <div align="left" class="FieldData" nowrap="nowrap">
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
                                                    <strong><bean:message key="oscarMDS.segmentDisplay.formDateService"/>:</strong>
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
                                        <% } %>
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
                                                                        
                                                                            <%= report.getTimestamp() %>,
                                                                            <% String commentTitle = null;
                                                                               if(report.getComment() == null || report.getComment().equals("")) {
                                                                        	   	commentTitle = "no comment";
                                                                               }
                                                                               else {
                                                                        	   	commentTitle = "comment: ";
                                                                               }
                                                                            %>
                                                                            <span id="<%="V" + j + "commentLabel" + segmentID + report.getProviderNo()%>"><%=commentTitle%></span><span id="<%="V" + j + "commentText" + segmentID + report.getProviderNo()%>"> <%=report.getComment() == null ? "" : report.getComment()%></span>
                                                                        
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
                        </table>


                        <% int i=0;
                        int j=0;
                        int k=0;
                        int l=0;
                        int linenum=0;
                        String highlight = "#E0E0FF";

						ArrayList <String> headers = handler.getHeaders();
						int OBRCount = handler.getOBRCount();

                        if (handler.getMsgType().equals("MEDVUE")) { %>
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
                           </tr>
	                        <tr class="TDISRes">
		                      	<td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><b>Radiologist: </b><b><%=handler.getRadiologistInfo()%></b></pre></td>
		                      	</td>
	                     	 </tr>
	                        <tr class="TDISRes">
		                       	<td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><b><%=handler.getOBXComment(1, 1, 1)%></b></pre></td>
		                       	</td>
	                      	 </tr>
                     	 </table>
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
    						}
		                       %><table style="page-break-inside:avoid;" bgcolor="#003399" border="0" cellpadding="0" cellspacing="0" width="100%">
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
                           	<%if(isUnstructuredDoc){%>
	                       <table width="100%" border="0" cellspacing="0" cellpadding="2" bgcolor="#CCCCFF" bordercolor="#9966FF" bordercolordark="#bfcbe3" name="tblDiscs" id="tblDiscs">
	                           <tr class="Field2">
	                               <td width="20%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formTestName"/></td>
	                               <td width="60%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formResult"/></td>
	                               <td width="20%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formDateTimeCompleted"/></td>
	                           </tr><%
						} else {%>
                        <table width="100%" border="0" cellspacing="0" cellpadding="2" bgcolor="#CCCCFF" bordercolor="#9966FF" bordercolordark="#bfcbe3" name="tblDiscs" id="tblDiscs">
                            <tr class="Field2">
                                <td width="25%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formTestName"/></td>
                                <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formResult"/></td>
                                <td width="5%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formAbn"/></td>
                                <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formReferenceRange"/></td>
                                <td width="10%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formUnits"/></td>
                                <td width="15%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formDateTimeCompleted"/></td>
                                <td width="6%" align="middle" valign="bottom" class="Cell"><bean:message key="oscarMDS.segmentDisplay.formNew"/></td>
                            </tr>

                            <%}

                            for ( j=0; j < OBRCount; j++){

                                boolean obrFlag = false;
                                int obxCount = handler.getOBXCount(j);

                                for (k=0; k < obxCount; k++){
                                    String obxName = handler.getOBXName(j, k);
									boolean isAllowedDuplicate = false;
									if(handler.getMsgType().equals("PATHL7")){
										//if the obxidentifier and result name are any of the following, they must be displayed (they are the Excepetion to Excelleris TX/FT duplicate result name display rules)
										if((handler.getOBXName(j, k).equals("Culture") && handler.getOBXIdentifier(j, k).equals("6463-4")) || 
										(handler.getOBXName(j, k).equals("Organism") && (handler.getOBXIdentifier(j, k).equals("X433") || handler.getOBXIdentifier(j, k).equals("X30011")))){
										isAllowedDuplicate = true;
											}
									}
                                     boolean b2 = !obxName.equals(""), b3=handler.getObservationHeader(j, k).equals(headers.get(i));
                                    if (handler.getMsgType().equals("EPSILON")) {
                                    	b2=true; b3=true;
                                    } else if(handler.getMsgType().equals("PFHT") || handler.getMsgType().equals("HHSEMR")) {
                                    	b2=true;
                                    }
                                    
                                    
                                    if ( !handler.getOBXResultStatus(j, k).equals("DNS") && b2 && b3){ // <<--  DNS only needed for MDS messages
                                        String obrName = handler.getOBRName(j);
                                        if(!obrFlag && !obrName.equals("") && !(obxName.contains(obrName) && obxCount < 2)){%>
                                           <%--  <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" >
                                                <td valign="top" align="left"><%=obrName%></td>
                                                <td colspan="6">&nbsp;</td>
                                            </tr> --%>
                                            <%obrFlag = true;
                                        }

                                        String lineClass = "NormalRes";
                                        String abnormal = handler.getOBXAbnormalFlag(j, k);
                                        if ( abnormal != null && abnormal.startsWith("L")){
                                            lineClass = "HiLoRes";
                                        } else if ( abnormal != null && ( abnormal.equals("A") || abnormal.startsWith("H") || handler.isOBXAbnormal( j, k) ) ){
                                            lineClass = "AbnormalRes";
                                        }%>
                                        <%if (handler.getMsgType().equals("EPSILON")) {
	                                    	   if (handler.getOBXIdentifier(j,k).equals(headers.get(i)) && !obxName.equals("")) { %>

	                                        	<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
		                                            <td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../lab/CA/ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier='+encodeURIComponent('<%= handler.getOBXIdentifier(j, k)%>'))"><%=obxName %></a></td>
		                                            <td align="right"><%= handler.getOBXResult( j, k) %></td>

		                                            <td align="center">
		                                                    <%= handler.getOBXAbnormalFlag(j, k)%>
		                                            </td>
		                                            <td align="left"><%=handler.getOBXReferenceRange( j, k)%></td>
		                                            <td align="left"><%=handler.getOBXUnits( j, k) %></td>
		                                            <td align="center"><%= handler.getTimeStamp(j, k) %></td>
		                                            <td align="center"><%= handler.getOBXResultStatus( j, k) %></td>
	                                       		</tr>
	                                       <% } else if (handler.getOBXIdentifier(j,k).equals(headers.get(i)) && obxName.equals("")) { %>
	                                       			<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
	                                                    <td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><%=handler.getOBXResult( j, k)%></pre></td>
	                                                </tr>
	                                       	<% }
                                        } else if (handler.getMsgType().equals("PFHT") || handler.getMsgType().equals("HHSEMR")) {
	                                    	   if (!obxName.equals("")) { %>
		                                    		<tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
			                                            <td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../lab/CA/ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier='+encodeURIComponent('<%= handler.getOBXIdentifier(j, k)%>'))"><%=obxName %></a></td>
			                                            <td align="right"><%= handler.getOBXResult( j, k) %></td>

			                                            <td align="center">
			                                                    <%= handler.getOBXAbnormalFlag(j, k)%>
			                                            </td>
			                                            <td align="left"><%=handler.getOBXReferenceRange( j, k)%></td>
			                                            <td align="left"><%=handler.getOBXUnits( j, k) %></td>
			                                            <td align="center"><%= handler.getTimeStamp(j, k) %></td>
			                                            <td align="center"><%= handler.getOBXResultStatus( j, k) %></td>
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


                                      } else  if (!handler.getOBXResultStatus(j, k).equals("TDIS") && !handler.getMsgType().equals("EPSILON")) {
                                          	%><tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>"><%
                                       		if(isUnstructuredDoc){
	                                   			if(handler.getOBXIdentifier(j, k).equalsIgnoreCase(handler.getOBXIdentifier(j, k-1)) && (obxCount>1)){%>
	                                   				<td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier='+encodeURIComponent('<%= handler.getOBXIdentifier(j, k)%>'))"></a><%
	                                   				}
	                                   			else{%> <td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier=<%= handler.getOBXIdentifier(j, k) %>')"><%=obxName %></a><%}%>
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
	                                        		else{%> <td align="center"><%= handler.getTimeStamp(j, k) %></td><%}
                                   			}//end of isUnstructuredDoc

                                   			else{//if it isn't a PATHL7 doc
                                   				//if there are duplicate FT/TX obxNames, only display the first (only if handler is PATHL7)
	                                   			if(handler.getMsgType().equals("PATHL7")&& !isAllowedDuplicate && (obxCount>1) && handler.getOBXIdentifier(j, k).equalsIgnoreCase(handler.getOBXIdentifier(j, k-1)) && (handler.getOBXValueType(j, k).equals("TX") || handler.getOBXValueType(j, k).equals("FT"))){%>
	                                   				<td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier=<%= handler.getOBXIdentifier(j, k) %>')"></a><%
	                                   				}
	                               				else{%>
	                                            <td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../lab/CA/ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier='+encodeURIComponent('<%= handler.getOBXIdentifier(j, k)%>'))"><%=obxName %></a></td><%}%>
	                                            <%
	                                          	//for pathl7, if it is an SG/CDC result greater than 100 characters, left justify it 
	                                            if((handler.getOBXResult(j, k).length() > 100) && isSGorCDC){%>
	                                            	<td align="left"><%= handler.getOBXResult( j, k) %></td><%
	                                            }else{%>
	                                            <td align="right"><%= handler.getOBXResult( j, k) %></td><%}%>
	                                            <td align="center">
	                                                    <%= handler.getOBXAbnormalFlag(j, k)%>
	                                            </td>
	                                            <td align="left"><%=handler.getOBXReferenceRange( j, k)%></td>
	                                            <td align="left"><%=handler.getOBXUnits( j, k) %></td>
	                                            <td align="center"><%= handler.getTimeStamp(j, k) %></td>
	                                            <td align="center"><%= handler.getOBXResultStatus( j, k) %></td><%
	                                   			}//end of PATHL7 else %>
                                        </tr>

                                        <%for (l=0; l < handler.getOBXCommentCount(j, k); l++){%>
                                            <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
                                                <td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><%=handler.getOBXComment(j, k, l)%></pre></td>
                                            </tr>
                                        <%}
	                                    } else { %>
		                                	<%for (l=0; l < handler.getOBXCommentCount(j, k); l++){%>
		                                     <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="TDISRes">
		                                        <td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><%=handler.getOBXComment(j, k, l)%></pre></td>
		                                     </tr>
		                                	<%}
	                                   }
                                   }
                                }
                            //}

                            //for ( j=0; j< OBRCount; j++){
                             if (!handler.getMsgType().equals("PFHT")) {
                                if (headers.get(i).equals(handler.getObservationHeader(j, 0))) {%>
                                <%for (k=0; k < handler.getOBRCommentCount(j); k++){
                                    // the obrName should only be set if it has not been
                                    // set already which will only have occured if the
                                    // obx name is "" or if it is the same as the obr name
                                    if(!obrFlag && handler.getOBXName(j, 0).equals("")){%>
                                        <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" >
                                            <td valign="top" align="left"><%=handler.getOBRName(j)%></td>
                                            <td colspan="6">&nbsp;</td>
                                        </tr>
                                        <%obrFlag = true;
                                    }%>
                                <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
                                    <td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><%=handler.getOBRComment(j, k)%></pre></td>
                                </tr>
                                <% if  (!handler.getMsgType().equals("HHSEMR")) {
                                	if(handler.getOBXName(j,k).equals("")){
                                       String result = handler.getOBXResult(j, k);%>
                                        <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" >
                                                <td colspan="7" valign="top"  align="left"><%=result%></td>
                                        </tr>
                                            <%
                                    }
                                }


                                }
                            }
                             } // end for if (PFHT)
                            }
                          } // end for handler.getMsgType().equals("MEDVUE")
                            %>
                        </table>
                        <% // end for headers
                        }  // for i=0... (headers)
  					 %>

                        <table width="100%" border="0" cellspacing="0" cellpadding="3" class="MainTableBottomRowRightColumn" bgcolor="#003399">
                            <tr>
                                <td align="left" width="50%">
                                    <% if ( !ackFlag ) { %>
                                    <input type="button" value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>" onclick="<%=ackLabFunc%>">
                                    <input type="button" value="<bean:message key="oscarMDS.segmentDisplay.btnComment"/>" onclick="getComment('<%=segmentID%>','addComment')">
                                    <% } %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="popupStart(300, 400, '../oscarMDS/SelectProviderAltView.jsp?doc_no=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>', 'providerselect')">

                                    <input type="button" value=" <bean:message key="global.btnPrint"/> " onClick="printPDF('<%=segmentID%>')">
                                        <indivo:indivoRegistered demographic="<%=demographicID%>" provider="<%=providerNo%>">
                                        <input type="button" value="<bean:message key="global.btnSendToPHR"/>" onClick="sendToPHR('<%=segmentID%>', '<%=demographicID%>')">
                                        </indivo:indivoRegistered>
                                    <% if ( searchProviderNo != null ) { // we were called from e-chart %>
                                    <input type="button" value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> " onClick="popupStart(360, 680, '../oscarMDS/SearchPatient.do?labType=HL7&segmentID=<%= segmentID %>&name=<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>', 'searchPatientWindow')">

                                    <% } %>
                                </td>
                                <td width="50%" valign="center" align="left">
                                    <span class="Field2"><i><bean:message key="oscarMDS.segmentDisplay.msgReportEnd"/></i></span>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr><td colspan="1"><a style="color:white;" href="javascript:void(0);" onclick="showHideItem('rawhl7_<%=segmentID%>');" >show/hide</a>
                    <pre id="rawhl7_<%=segmentID%>" style="display:none;"><%=hl7%></pre></td></tr>
                <tr><td colspan="1" ><hr width="100%" color="red"></td></tr>
            </table>
        </form>

    </div>
