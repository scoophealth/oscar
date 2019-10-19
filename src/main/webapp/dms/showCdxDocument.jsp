<%--

    Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
    Department of Computer Science
    LeadLab
    University of Victoria
    Victoria, Canada

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib prefix = "x" uri = "http://java.sun.com/jsp/jstl/xml" %>
<%
    String roleName$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>


<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import="org.springframework.web.context.WebApplicationContext,org.oscarehr.common.dao.*,org.oscarehr.common.model.*,org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.*" %>
<%@ page import="oscar.oscarMDS.data.ReportStatus" %>
<%@ page import="oscar.oscarLab.ca.all.AcknowledgementData" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.oscarehr.integration.cdx.model.*" %>
<%@ page import="org.oscarehr.integration.cdx.dao.*" %>
<%@ page import="oscar.log.LogAction" %>
<%@ page import="oscar.log.LogConst" %>
<%@ page import="oscar.dms.EDocUtil" %>
<%

    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

    String demoName = request.getParameter("demoName");
    String documentNo = request.getParameter("segmentID");
    Integer documentNoInt = Integer.parseInt(documentNo);
    DocumentDao docDao = SpringUtils.getBean(DocumentDao.class);
    CtlDocumentDao ctlDocDao = SpringUtils.getBean(CtlDocumentDao.class);
    DemographicDao demoDao = SpringUtils.getBean(DemographicDao.class);
    CdxProvenanceDao provenanceDao = SpringUtils.getBean(CdxProvenanceDao.class);
    ProviderInboxRoutingDao providerInboxRoutingDao = (ProviderInboxRoutingDao) ctx.getBean("providerInboxRoutingDAO");
    UserPropertyDAO userPropertyDAO = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");

    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    CtlDocument ctlDoc = ctlDocDao.getCtrlDocument(documentNoInt);
    ArrayList doctypes = EDocUtil.getActiveDocTypes("demographic");

    if (ctlDoc == null) { %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Document no longer exists</title>
</head>
<body>
<h1>This document no longer exists</h1>
It must have been deleted. Please refresh your Inbox window.
</body>
</html>

<%
} else {

    Integer demoNoInt = ctlDoc.getId().getModuleId();
    String demoNo = demoNoInt.toString();
    Document curdoc = docDao.findActiveByDocumentNo(documentNoInt).get(0);
    CdxProvenance provenanceDoc = provenanceDao.findByDocumentNo(documentNoInt);


    String providerNo = request.getParameter("providerNo");
    UserProperty uProp = userPropertyDAO.getProp(providerNo, UserProperty.LAB_ACK_COMMENT);
    boolean skipComment = false;

    if( uProp != null && uProp.getValue().equalsIgnoreCase("yes")) {
        skipComment = true;
    }


    // mark document as read
    if (!demoNo.equals("-1")) {
        DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
        Demographic demographic = demographicDao.getDemographic(demoNo);
        demoName = demographic.getLastName() + "," + demographic.getFirstName();
        LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, documentNo, request.getRemoteAddr(), demoNo);
    }



%>
<html>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>CDX Document Inbox Viewer</title>

    <script src="<%= request.getContextPath() %>/share/javascript/prototype.js"></script>
    <script src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
    <script src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>
    <script src="<%= request.getContextPath() %>/share/javascript/Oscar.js" ></script>

    <link rel="stylesheet" href="<%= request.getContextPath() %>/share/css/bootstrap.min.css">

    <script src="<%= request.getContextPath() %>/share/javascript/bootstrap.min.js"></script>

    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/share/yui/css/fonts-min.css"/>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/share/yui/css/autocomplete.css"/>

    <script type="text/javascript" src="<%= request.getContextPath() %>/js/demographicProviderAutocomplete.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/oscarMDSIndex.js"></script>

    <link rel="stylesheet" type="text/css" media="all"
          href="<%= request.getContextPath() %>/share/css/demographicProviderAutocomplete.css"/>

    <script type="text/javascript">
        jQuery.noConflict();

        var contextpath = "<%=request.getContextPath()%>";

    </script>

    <script type="text/javascript" src="showDocument.js"></script>

</head>
<body>

<div class="container-fluid">

    <div class="row">
        <div class="col-md-12">
            <div class="row">

                <div class="col-md-6">


                    <form id="forms_<%=documentNo%>" onsubmit="return updateCdxDocumentAndLinkDemo('forms_<%=documentNo%>');">
                        <input type="hidden" name="documentId" value="<%=documentNo%>" />
                        <input type="hidden" id="docDesc_<%=documentNo%>" name="documentDescription" value="<%=curdoc.getDocdesc()%>" />
                        <input type="hidden" id="observationDate<%=documentNo%>" name="observationDate" value="<%=curdoc.getObservationdate()%>"/>
                        <input type="hidden" id="docType<%=documentNo%>" name="docType" value="<%curdoc.getDoctype();%>"/>
                        <input type="hidden" name="method" value="documentUpdateAjax" />
                        <input id="saved<%=documentNo%>" type="hidden" name="saved" value="true"/>
                        <input type="hidden" value="<%=demoNo%>" name="demog" id="demofind<%=documentNo%>"/>
                        <input type="hidden" name="demofindName" value="<%=demoName%>"
                               id="demofindName<%=documentNo%>"/>

                        <input id="saved_<%=documentNo%>" type="hidden" name="saved" value="false"/>
                        <input type="hidden" name="provi" id="provfind<%=documentNo%>"/>

                        <%
                            boolean demoLinked = demoNo != null && !demoNo.equals("") && !demoNo.equalsIgnoreCase("null") && !demoNo.equals("-1");
                            if (!demoLinked) {
                        %>

                        <font color="red">
                            <strong>Warning!</strong> Name in document is <strong> not </strong> matched to a demographic.
                        </font>



                        <div>

                            <div class="input-group">
                                <span class="input-group-addon" id="activeGroup">Active
				 					                               <input type="checkbox" id="activeOnly<%=documentNo%>" name="activeOnly" checked="checked"
                                                                          value="true" onclick="setupDemoAutoCompletion()">
										            </span>
                                <input type="text" class="form-control" id="autocompletedemo<%=documentNo%>"
                                       onchange="checkSave('<%=documentNo%>');" name="demographicKeyword" placeholder="Demographic search..."/>

                                <span class="input-group-btn" >

                                <button type="submit" disabled name="save" class="btn btn-default" id="save<%=documentNo%>"> Link </button>




                                <input type="button" id="createNewDemo" value="New" class="btn btn-default"
                                       onclick="createNewDemoAndLinkIt()"/>
																																		</span>



                                <div id="autocomplete_choices<%=documentNo%>" class="autocomplete"></div>
                            </div>



                        </div>



                        <% } else {
                            Demographic pat = demoDao.getDemographic(demoNo);
                            boolean warningsExist = !(provenanceDoc.getWarnings() == null || provenanceDoc.getWarnings().equals(""));
                        %>

                        <div class="panel panel-default">
                            <div class="panel-heading"> Linked Demographic </div>
                            <div class="panel-body">


                                <h1>
                                    <%

                                        out.print(" " + pat.getFirstName()
                                                + " " + pat.getLastName()
                                                + " " + pat.getSex()
                                                + " " + pat.getBirthDayAsString()); %>
                                </h1>
                            </div>
                            <%
                                if (warningsExist) { %>
                            <div class="alert alert-danger alert-dismissible" role="alert">
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="alert-heading">Warning!</h4>
                                <%=provenanceDoc.getWarnings()%>
                            </div>

                            <%}%>
                        </div>

                        <div class = "row">
                            <div class = "col-md-6">

                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <bean:message key="dms.documentReport.msgDocType" />:
                                    </span>
                                    <select class="form-control" name="docType" id="docTypeSelector" >
                                        <option value=""><bean:message key="dms.addDocument.formSelect" /></option>
                                        <%for (int j = 0; j < doctypes.size(); j++) {
                                            String doctype = (String) doctypes.get(j);%>
                                        <option value="<%= doctype%>" <%=(curdoc.getDoctype().equals(doctype)) ? " selected" : ""%>><%= doctype%></option>
                                        <%}%>
                                    </select>
                                </div>
                            </div>

                            <div class="col-md-6">

                                <div class="input-group">
                                    <input type="text" placeholder="Flag provider..." class="form-control" id="autocompleteprov<%=documentNo%>" name="providerKeyword"/>
                                    <span class = "input-group-btn">
                                    <button type="button" name="save" id="flagsave<%=documentNo%>" <% if (demoNoInt==-1) out.print("disabled");%> class="btn btn-default" onclick="updateCdxDocument('forms_<%=documentNo%>')">
                                                                        Save
                                                                </button> </span>
                                    <div id="autocomplete_choicesprov<%=documentNo%>" class="autocomplete"></div>

                                </div>

                                        <%
                                            Properties p = (Properties) session.getAttribute("providerBean");
                                            List<ProviderInboxItem> routeList = providerInboxRoutingDao.getProvidersWithRoutingForDocument("DOC", documentNoInt);
                                        %>
                                        <ul>
                                            <%
                                                for (ProviderInboxItem pItem : routeList) {
                                                    String s = p.getProperty(pItem.getProviderNo(), pItem.getProviderNo());

                                                    if (!s.equals("0") && !s.equals("null") && !pItem.getStatus().equals("X")) {
                                            %>
                                            <li><%=s%><a href="#"
                                                         onclick="removeLink('DOC', '<%=documentNo %>', '<%=pItem.getProviderNo() %>', this);return false;"><bean:message
                                                    key="inboxmanager.document.RemoveLinkedProviderMsg"/></a></li>
                                            <%
                                                    }
                                                }
                                            %>
                                        </ul>
                                <div class="panel-body">
                                    <div id="providerList<%=documentNo%>"></div>
                                    <a id="saveSucessMsg_<%=documentNo%>" style="display:none;color:blue;">
                                        <bean:message key="inboxmanager.document.SuccessfullySavedMsg"/></a>
                                </div>

                            </div>

                        </div>
                        <% } %>



                    </form>
                </div>

                <div class="col-md-6">
                    <div class="row">

                        <div id="labdoc_<%=documentNo%>">
                            <%
                                ArrayList ackList = AcknowledgementData.getAcknowledgements("DOC",documentNo);
                                ReportStatus reportStatus;
                                String docCommentTxt = "";
                                String rptStatus;
                                boolean ackedOrFiled = false;
                                for (Object o : ackList) {
                                    reportStatus = (ReportStatus) o;

                                    if (reportStatus.getOscarProviderNo() != null && reportStatus.getOscarProviderNo().equals(providerNo)) {
                                        docCommentTxt = reportStatus.getComment();
                                        if (docCommentTxt == null) {
                                            docCommentTxt = "";
                                        }

                                        rptStatus = reportStatus.getStatus();

                                        if (rptStatus != null) {
                                            ackedOrFiled = rptStatus.equalsIgnoreCase("A") || (rptStatus.equalsIgnoreCase("F"));
                                        }
                                        break;
                                    }
                                }
                            %>

                            <form name="myForm">

                            </form>

                            <form id="acknowledgeForm_<%=documentNo%>" onsubmit="acknowledgeCdxDocument()" method="post" action="javascript:void(0);">

                                <input type="hidden" name="segmentID" value="<%= documentNo%>"/>
                                <input type="hidden" name="multiID" value="<%= documentNo%>" />
                                <input type="hidden" name="providerNo" value="<%= providerNo%>"/>
                                <input type="hidden" name="status" value="A" id="status_<%=documentNo%>">
                                <input type="hidden" name="labType" value="DOC"/>
                                <input type="hidden" name="ajaxcall" value="yes"/>
                                <input type="hidden" name="demofind" id="demofind_<%=documentNo%>" value="<%= demoNo%>"/>
                                <input type="hidden" name="comment" id="comment_<%=documentNo%>" value="<%=docCommentTxt%>">

                                <input type="button" class="btn btn-default" id="closeBtn_<%=documentNo%>" value=" <bean:message key="global.btnClose"/> " onClick="window.close()">
                                <input type="button" class="btn btn-danger" id="deleteBtn_<%=documentNo%>" value=" Delete" onClick="deleteCdxDocument(<%=documentNo%>)" <%=(demoLinked? "style='display:none'" : "")%>>
                                <button type="button" class="btn btn-small" aria-label="CDX help" onClick="window.open('https://simbioses.github.io/cdxuserman/006_for_users/009_receiving/cdx_10/');">
                                    <span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span>
                                </button>
                                <div class="btn-group" role="group" <%=(!demoLinked? "style='display:none'" : "")%>>


                                    <input type="submit" class="btn btn-success" id="ackBtn_<%=documentNo%>" value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>" <%=(ackedOrFiled? "style='display:none'" : "")%>>



                                    <input type="button" class="btn btn-default" id="msgBtn_<%=documentNo%>" value="Msg" onclick="popupPatient(700,960,'<%= request.getContextPath() %>/oscarMessenger/SendDemoMessage.do?demographic_no=','msg', '<%=documentNo%>')" />

                                    <%
                                        if(org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()) {
                                    %>
                                    <input type="button" class="btn btn-default" id="mainTickler_<%=documentNo%>" value="Tickler" onClick="popupPatientTicklerPlus(710, 1024,'<%= request.getContextPath() %>/Tickler.do?', 'Tickler','<%=documentNo%>')" >
                                    <% } else { %>
                                    <input type="button" class="btn btn-default" id="mainTickler_<%=documentNo%>" value="Tickler" onClick="popupPatientTickler(710, 1024,'<%= request.getContextPath() %>/tickler/ticklerAdd.jsp?', 'Tickler','<%=documentNo%>')" >
                                    <% } %>

                                    <input type="button" class="btn btn-default" id="mainEchart_<%=documentNo%>" value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> " onClick="popupPatient(710, 1024,'<%= request.getContextPath() %>/oscarEncounter/IncomingEncounter.do?reason=<bean:message key="oscarMDS.segmentDisplay.labResults"/>&curDate=<%=currentDate%>>&appointmentNo=&appointmentDate=&startTime=&status=&demographicNo=', 'encounter', '<%=documentNo%>')" >
                                    <input type="button" class="btn btn-default" id="mainMaster_<%=documentNo%>" value=" <bean:message key="oscarMDS.segmentDisplay.btnMaster"/>" onClick="popupPatient(710,1024,'<%= request.getContextPath() %>/demographic/demographiccontrol.jsp?displaymode=edit&dboperation=search_detail&demographic_no=','master','<%=documentNo%>')" >
                                    <input type="button" class="btn btn-default" id="mainApptHistory_<%=documentNo%>" value=" <bean:message key="oscarMDS.segmentDisplay.btnApptHist"/>" onClick="popupPatient(710,1024,'<%= request.getContextPath() %>/demographic/demographiccontrol.jsp?orderby=appttime&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=25&demographic_no=','ApptHist','<%=documentNo%>')" >

                                </div>



                            </form>


                        </div>

                    </div>



                    <div class="row">
                        <%
                            if (ackList.size() > 0){%>
                        <fieldset>
                            <table width="100%" height="20" cellpadding="2" cellspacing="2">
                                <tr>
                                    <td align="left" bgcolor="white">
                                        <div class="FieldData">
                                            <!--center-->
                                            <% for (Object o : ackList) {
                                                ReportStatus report = (ReportStatus) o; %>
                                            <%= report.getProviderName() %> :

                                            <% String ackStatus = report.getStatus();
                                                if (ackStatus.equals("A")) {
                                                    ackStatus = "Acknowledged";
                                                } else if (ackStatus.equals("F")) {
                                                    ackStatus = "Filed but not Acknowledged";
                                                } else {
                                                    ackStatus = "Not Acknowledged";
                                                }
                                            %>
                                            <font color="red"><%= ackStatus %>
                                            </font>
                                            <span id="timestamp_<%=documentNo + "_" + report.getOscarProviderNo()%>"><%= report.getTimestamp() == null ? "&nbsp;" : report.getTimestamp() + "&nbsp;"%></span>,
                                            comment: <span
                                                id="comment_<%=documentNo + "_" + report.getOscarProviderNo()%>"><%=report.getComment() == null || report.getComment().equals("") ? "no comment" : report.getComment()%></span>

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
                        </fieldset>
                        <%}%>

                    </div>

                </div>

            </div>

            <div class="row">
                <%@include  file = "renderCdxDocument.jsp" %>
            </div>

        </div>
    </div>
</div>


<script type="text/javascript">

    function setupDemoAutoCompletion() {
        if (jQuery("#autocompletedemo<%=documentNo%>")) {

            var url;
            if (jQuery("#activeOnly<%=documentNo%>").is(":checked")) {
                url = "<%= request.getContextPath() %>/demographic/SearchDemographic.do?jqueryJSON=true&activeOnly=" + jQuery("#activeOnly<%=documentNo%>").val();
            } else {
                url = "<%= request.getContextPath() %>/demographic/SearchDemographic.do?jqueryJSON=true";
            }

            jQuery("#autocompletedemo<%=documentNo%>").autocomplete({
                source: url,
                minLength: 2,

                focus: function (event, ui) {
                    jQuery("#autocompletedemo<%=documentNo%>").val(ui.item.label);
                    return false;
                },
                select: function (event, ui) {
                    jQuery("#autocompletedemo<%=documentNo%>").val(ui.item.label);
                    jQuery("#demofind<%=documentNo%>").val(ui.item.value);
                    jQuery("#demofindName<%=documentNo%>").val(ui.item.formattedName);
                    selectedDemos.push(ui.item.label);
                    console.log(ui.item.providerNo);

                    if (ui.item.providerNo !== undefined && ui.item.providerNo !== "" && ui.item.providerNo !== "null") {
                        addDocToList(ui.item.providerNo, ui.item.provider + " (MRP)", "<%=documentNo%>");
                    }
                    //enable Save button whenever a selection is made
                    jQuery('#save<%=documentNo%>').removeAttr('disabled');

                    jQuery('#msgBtn_<%=documentNo%>').removeAttr('disabled');
                    return false;
                }
            });
        }
    }


    jQuery(setupDemoAutoCompletion());

    function setupProviderAutoCompletion() {
        var url = "<%= request.getContextPath() %>/provider/SearchProvider.do?method=labSearch";

        jQuery("#autocompleteprov<%=documentNo%>").autocomplete({
            source: url,
            minLength: 2,

            focus: function (event, ui) {
                jQuery("#autocompleteprov<%=documentNo%>").val(ui.item.label);
                return false;
            },
            select: function (event, ui) {
                jQuery("#autocompleteprov<%=documentNo%>").val("");
                jQuery("#provfind<%=documentNo%>").val(ui.item.value);
                addDocToList(ui.item.value, ui.item.label, "<%=documentNo%>");

                return false;
            }
        });
    }

    jQuery(setupProviderAutoCompletion());


    jQuery(document).ready(function () { //pushing the autocomplete helper out of the visible page
        jQuery(".ui-helper-hidden-accessible").css({"position": "absolute", "left": "-999em"}) //{"display","none"} will remove the element from the page
    });




    function updateCdxDocument(eleId){
        //save doc info
        var selBox = $("docTypeSelector");
        if (selBox != null) {
            $("docType<%=documentNo%>").setAttribute("value", selBox.options[selBox.selectedIndex].value);
        }
        var url="../dms/ManageDocument.do",data=$(eleId).serialize(true);
        new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                var json=transport.responseText.evalJSON();
                var patientId;
                //oscarLog(json);
                if(json!=null ){
                    patientId=json.patientId;

                    var ar=eleId.split("_");
                    var num=ar[1];
                    num=num.replace(/\s/g,'');
                    document.location.reload();
                }
            }});

        return false;
    }

    function updateCdxDocumentAndLinkDemo(eleId) {
        if (confirm("Are you sure to link to this EXISTING demographic record? (Inconsistencies exist between the demographic master record and the patient named in this document.) Proceed only if you are sure you have the right patient."))
            return updateCdxDocument(eleId);
        else return false;
    }

    function createNewDemoAndLinkIt() {
        if (confirm("Are you sure to CREATE A NEW demographic for the patient named in this document? (Only do this if you are certain that this patient is new)")) {
            popup(350,480,'<%= request.getContextPath() %>/cdx/cdxCreateDemographic.jsp?msg_id=<%=provenanceDoc.getMsgId()%>&doc_no=<%=documentNo%>','demographic');

        }
        else return false;
    }

    function acknowledgeCdxDocument() {
        <%  if( skipComment ) { %>
        updateStatus('acknowledgeForm_<%=documentNo%>', false);
        <% } else { %>
        getDocComment(<%=documentNo%>,  <%=providerNo%> , false);
        <% } %>
        window.close();
    }

    function deleteCdxDocument(docNo) {
        if (confirm("ARE YOU SURE TO DELETE THIS INCOMING CDX DOCUMENT? \n(Documents should only be deleted if they were received in error.)")) {

            var url="../dms/DeleteDocument.do";
            var data='method=deleteDocument&documentId='+docNo;
            new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                    window.close();
                }});
        }

        return false;
    }


</script>
</body>
</html>
<%
    }
%>