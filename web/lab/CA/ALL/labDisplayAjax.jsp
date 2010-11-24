<%@page errorPage="../provider/errorpage.jsp" %>
<%@ page import="java.util.*,
		 java.sql.*,
		 oscar.oscarDB.*,
		 oscar.oscarLab.ca.all.*,
		 oscar.oscarLab.ca.all.util.*,
		 oscar.oscarLab.ca.all.parsers.*,
		 oscar.oscarLab.LabRequestReportLink,
		 oscar.oscarMDS.data.ReportStatus,oscar.log.*,
                 oscar.oscarDB.DBHandler,
		 org.apache.commons.codec.binary.Base64" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProperties"%>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo"%>
<%

String segmentID = request.getParameter("segmentID");
String providerNo = request.getParameter("providerNo");
String searchProviderNo = request.getParameter("searchProviderNo");
String patientMatched = request.getParameter("patientMatched");
Long reqIDL = LabRequestReportLink.getIdByReport("hl7TextMessage",Long.valueOf(segmentID.trim()));
String reqID = reqIDL==null ? "" : String.valueOf(reqIDL);
String sql = "SELECT demographic_no FROM patientLabRouting WHERE lab_type='HL7' and lab_no='"+segmentID+"';";

ResultSet rs = DBHandler.GetSQL(sql);
String demographicID = "";

while(rs.next()){
    demographicID = oscar.Misc.getString(rs,"demographic_no");
}
rs.close();

boolean isLinkedToDemographic=false;
if(demographicID != null && !demographicID.equals("")&& !demographicID.equals("0")){
    isLinkedToDemographic=true;
    LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_HL7_LAB, segmentID, request.getRemoteAddr(),demographicID);
}else{
    LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_HL7_LAB, segmentID, request.getRemoteAddr());
}

boolean ackFlag = false;
AcknowledgementData ackData = new AcknowledgementData();
ArrayList ackList = ackData.getAcknowledgements(segmentID);
if (ackList != null){
    for (int i=0; i < ackList.size(); i++){
        ReportStatus reportStatus = (ReportStatus) ackList.get(i);
        if ( reportStatus.getProviderNo().equals(providerNo) && reportStatus.getStatus().equals("A") ){
            ackFlag = true;
            break;
        }
    }
}
MessageHandler handler = Factory.getHandler(segmentID);
Hl7textResultsData data = new Hl7textResultsData();
String multiLabId = data.getMatchingLabs(segmentID);

String hl7 = Factory.getHL7Body(segmentID);
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

        <html:base/>
        <title><%=handler.getPatientName()+" Lab Results"%></title>
                <script language="JavaScript">
         popupStart=function(vheight,vwidth,varpage,windowname) {
            var page = varpage;
            windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
            var popup=window.open(varpage, windowname, windowprops);
        }
         getComment=function(labid) {
            var ret = true;
            var commentVal = prompt('<bean:message key="oscarMDS.segmentDisplay.msgComment"/>', '');

            if( commentVal == null ||commentVal.length==0)
                ret = false;
            else{
                document.forms['acknowledgeForm_'+labid].comment.value = commentVal;
            }
            if(ret)
                handleLab('acknowledgeForm_'+labid,labid,'ackLab');

            return ret;
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
                                                                            
                                                                        }else{
                                                                            if(action=='ackLab'){
                                                                                if(confirmAckUnmatched())
                                                                                    updateStatus(formid);
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
        confirmAck=function() {
            return confirm('<bean:message key="oscarMDS.index.msgConfirmAcknowledge"/>');
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
                     updateDocLabData(num[1]);

                }
        }});

        }
        </script>


    <div id="labdoc_<%=segmentID%>">
        <!-- form forwarding of the lab -->
        <form name="reassignForm_<%=segmentID%>" >
            <input type="hidden" name="flaggedLabs" value="<%= segmentID %>" />
            <input type="hidden" name="selectedProviders" value="" />
            <input type="hidden" name="labType" value="HL7" />
            <input type="hidden" name="labType<%= segmentID %>HL7" value="imNotNull" />
            <input type="hidden" name="providerNo" value="<%= providerNo %>" />
            <input type="hidden" name="ajax" value="yes" />
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
                                    <input type="hidden" name="status" value="A"/>
                                    <input type="hidden" name="comment" value=""/>
                                    <input type="hidden" name="labType" value="HL7"/>
                                    <input type="hidden" name="ajaxcall" value="yes"/>
                                    <input type="hidden" id="demoName<%=segmentID%>" value="<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>"/>
                                    <% if ( !ackFlag ) { %>
                                    <input type="button" value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>" onclick="handleLab('acknowledgeForm_<%=segmentID%>','<%=segmentID%>','ackLab');">
                                    <input type="button" value="<bean:message key="oscarMDS.segmentDisplay.btnComment"/>" onclick="return getComment('<%=segmentID%>');">
                                    <% } %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="popupStart(300, 400, '../oscarMDS/SelectProviderAltView.jsp?doc_no=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>', 'providerselect')">
                                    <input type="button" value=" <bean:message key="global.btnPrint"/> " onClick="printPDF('<%=segmentID%>')">

                                    <input type="button" value="Msg" onclick="handleLab('','<%=segmentID%>','msgLab');"/>
                                    <input type="button" value="Tickler" onclick="handleLab('','<%=segmentID%>','ticklerLab');"/>

                                    <% if ( searchProviderNo != null ) { // null if we were called from e-chart%>
                                    <input type="button" value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> " onClick="popupStart(360, 680, '../oscarMDS/SearchPatient.do?labType=HL7&segmentID=<%= segmentID %>&name=<%=java.net.URLEncoder.encode(handler.getLastName()+", "+handler.getFirstName())%>', 'searchPatientWindow')">
                                    <% } %>
				    <input type="button" value="Req# <%=reqID%>" title="Link to Requisition" onclick="linkreq('<%=segmentID%>','<%=reqID%>');" />
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
                                                    <%= ( (String) ( handler.getOrderStatus().equals("F") ? "Final" : handler.getOrderStatus().equals("C") ? "Corrected" : "Partial") )%>
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
                                        ackList = ackData.getAcknowledgements(multiID[j]);
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

                            <%

                            for ( j=0; j < OBRCount; j++){

                                boolean obrFlag = false;
                                int obxCount = handler.getOBXCount(j);
                                for (k=0; k < obxCount; k++){
                                    String obxName = handler.getOBXName(j, k);
                                    if ( !handler.getOBXResultStatus(j, k).equals("DNS") && !obxName.equals("") && handler.getObservationHeader(j, k).equals(headers.get(i))){ // <<--  DNS only needed for MDS messages
                                        String obrName = handler.getOBRName(j);
                                        if(!obrFlag && !obrName.equals("") && !(obxName.contains(obrName) && obxCount < 2)){%>
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
                                        }%>
                                        <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="<%=lineClass%>">
                                            <td valign="top" align="left"><%= obrFlag ? "&nbsp; &nbsp; &nbsp;" : "&nbsp;" %><a href="javascript:popupStart('660','900','../lab/CA/ON/labValues.jsp?testName=<%=obxName%>&demo=<%=demographicID%>&labType=HL7&identifier=<%= handler.getOBXIdentifier(j, k) %>')"><%=obxName %></a></td>
                                            <td align="right"><%= handler.getOBXResult( j, k) %></td>
                                            <td align="center">
                                                    <%= handler.getOBXAbnormalFlag(j, k)%>
                                            </td>
                                            <td align="left"><%=handler.getOBXReferenceRange( j, k)%></td>
                                            <td align="left"><%=handler.getOBXUnits( j, k) %></td>
                                            <td align="center"><%= handler.getTimeStamp(j, k) %></td>
                                            <td align="center"><%= handler.getOBXResultStatus( j, k) %></td>
                                        </tr>

                                        <%for (l=0; l < handler.getOBXCommentCount(j, k); l++){%>
                                            <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
                                                <td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><%=handler.getOBXComment(j, k, l)%></pre></td>
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
                                        <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" >
                                            <td valign="top" align="left"><%=handler.getOBRName(j)%></td>
                                            <td colspan="6">&nbsp;</td>
                                        </tr>
                                        <%obrFlag = true;
                                    }%>
                                <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" class="NormalRes">
                                    <td valign="top" align="left" colspan="8"><pre  style="margin:0px 0px 0px 100px;"><%=handler.getOBRComment(j, k)%></pre></td>
                                </tr>
                                <% if(handler.getOBXName(j,k).equals("")){
                                       String result = handler.getOBXResult(j, k);%>
                                        <tr bgcolor="<%=(linenum % 2 == 1 ? highlight : "")%>" >
                                                <td colspan="7" valign="top"  align="left"><%=result%></td>
                                        </tr>
                                            <%
                                    }


                                }
                            }
                            }%>
                        </table>
                        <% // end for headers
                        }  // for i=0... (headers) %>

                        <table width="100%" border="0" cellspacing="0" cellpadding="3" class="MainTableBottomRowRightColumn" bgcolor="#003399">
                            <tr>
                                <td align="left" width="50%">
                                    <% if ( !ackFlag ) { %>
                                    <input type="button" value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>" onclick="handleLab('acknowledgeForm_<%=segmentID%>','<%=segmentID%>','ackLab');">
                                    <input type="button" value="<bean:message key="oscarMDS.segmentDisplay.btnComment"/>" onclick="getComment('<%=segmentID%>')">
                                    <% } %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="popupStart(300, 400, '../oscarMDS/SelectProviderAltView.jsp?doc_no=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>', 'providerselect')">

                                    <input type="button" value=" <bean:message key="global.btnPrint"/> " onClick="printPDF('<%=segmentID%>')">
                                    <oscarProperties:oscarPropertiesCheck property="MY_OSCAR" value="yes">
                                        <indivo:indivoRegistered demographic="<%=demographicID%>" provider="<%=providerNo%>">
                                        <input type="button" value="<bean:message key="global.btnSendToPHR"/>" onClick="sendToPHR('<%=segmentID%>', '<%=demographicID%>')">
                                        </indivo:indivoRegistered>
                                    </oscarProperties:oscarPropertiesCheck>
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
