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
<%
    String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
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


<%@ page import="org.oscarehr.integration.cdx.dao.CdxDocumentDao" %>

<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import="org.springframework.web.context.WebApplicationContext,org.oscarehr.common.dao.*,org.oscarehr.common.model.*,org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxDocument" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxPerson" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxPersonDao" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxAttachment" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxAttachmentDao" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Properties" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxPersonId" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxPersonIdDao" %>
<%

    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

    String demoName = request.getParameter("demoName");
    String documentNo = request.getParameter("segmentID");
    Integer documentNoInt = Integer.parseInt(documentNo);
    DocumentDao docDao = SpringUtils.getBean(DocumentDao.class);
    CdxDocumentDao cdxDocDao = SpringUtils.getBean(CdxDocumentDao.class);
    CdxPersonDao cdxPersonDao = SpringUtils.getBean(CdxPersonDao.class);
    CdxPersonIdDao cdxPersonIdDao = SpringUtils.getBean(CdxPersonIdDao.class);
    CdxAttachmentDao cdxAttachmentDao = SpringUtils.getBean(CdxAttachmentDao.class);
    CtlDocumentDao ctlDocDao = SpringUtils.getBean(CtlDocumentDao.class);
    DemographicDao demoDao = SpringUtils.getBean(DemographicDao.class);
    ProviderInboxRoutingDao providerInboxRoutingDao = (ProviderInboxRoutingDao) ctx.getBean("providerInboxRoutingDAO");

    CdxDocument cdxDoc = cdxDocDao.getCdxDocument(documentNo);
    CtlDocument ctlDoc = ctlDocDao.getCtrlDocument(documentNoInt);

    Integer demoNoInt = ctlDoc.getId().getModuleId();
    String demoNo = demoNoInt.toString();
    CdxPerson patient = cdxPersonDao.findRoleInDocument(cdxDoc.getId(), CdxPerson.rolePatient).get(0);
    Document curdoc = docDao.findActiveByDocumentNo(documentNoInt).get(0);
%>
<html>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title><% out.print(cdxDoc.getTemplateName()
            + "/"
            + cdxDoc.getLoincName()); %></title>

    <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/prototype.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js" ></script>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">


    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>


    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/share/yui/css/fonts-min.css"/>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/share/yui/css/autocomplete.css"/>

    <script type="text/javascript" src="<%= request.getContextPath() %>/js/demographicProviderAutocomplete.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/oscarMDSIndex.js"></script>

    <link rel="stylesheet" type="text/css" media="all"
          href="<%= request.getContextPath() %>/share/css/demographicProviderAutocomplete.css"/>

    <script type="text/javascript">
        jQuery.noConflict();

    </script>

</head>
<body>

<div class="container-fluid">

    <div class="row">
        <div class="col-md-12">
            <h3><% out.print(cdxDoc.getTemplateName()
                    + "/"
                    + cdxDoc.getLoincName()); %></h3>
            <div class="row col-md-12">

                <form id="forms_<%=documentNo%>" onsubmit="return updateDocument('forms_<%=documentNo%>');">
                    <input type="hidden" name="documentId" value="<%=documentNo%>" />
                    <input type="hidden" id="docDesc_<%=documentNo%>"  type="text" name="documentDescription" value="<%=curdoc.getDocdesc()%>" />
                    <input type="hidden" id="observationDate<%=documentNo%>" name="observationDate" type="text" value="<%=curdoc.getObservationdate()%>"/>
                    <input type="hidden" id="docType<%=documentNo%>" name="docType" type="text" value="DOC"/>
                    <input type="hidden" name="method" value="documentUpdateAjax" />
                    <input id="saved<%=documentNo%>" type="hidden" name="saved" value="true"/>
                    <input type="hidden" value="<%=demoNo%>" name="demog" id="demofind<%=documentNo%>"/>
                    <input type="hidden" name="demofindName" value="<%=demoName%>"
                           id="demofindName<%=documentNo%>"/>

                        <%
                        if (demoNoInt == -1) {
                    %>
                    <a id="warningMsg_<%=documentNo%>" style="color:red;">
                        <strong>Warning!</strong> Name in document is <strong> not </strong> matched to a demographic. </a>

                    <input type="checkbox" id="activeOnly<%=documentNo%>" name="activeOnly" checked="checked"
                           value="true" onclick="setupDemoAutoCompletion()">Active Only<br>
                    <input type="text" style="width:400px;" id="autocompletedemo<%=documentNo%>"
                           onchange="checkSave('<%=documentNo%>');" name="demographicKeyword"/>
                    <div id="autocomplete_choices<%=documentNo%>" class="autocomplete"></div>

                    <input type="button" id="createNewDemo" value="Create New Demographic"
                           onclick="popup(700,960,'<%= request.getContextPath() %>/demographic/demographicaddarecordhtm.jsp','demographic')"/>


                    <input id="saved_<%=documentNo%>" type="hidden" name="saved" value="false"/>

                    <input type="submit" disabled name="save" id="save<%=documentNo%>" value="Save"/>

                    <a id="saveSucessMsg_<%=documentNo%>" style="display:none;color:blue;">
                        <bean:message key="inboxmanager.document.SuccessfullySavedMsg"/></a>

                        <% } else {
                            Demographic pat = demoDao.getDemographic(demoNo);
                        %>

                    <div class="alert alert-success" role="alert">
                        <h4>

                            <a href="javascript:popupStart(360, 680, '..//oscarMDS/SearchPatient.do?labType=DOC&segmentID=<%= documentNo %>&name=<%=java.net.URLEncoder.encode(pat.getLastName()+", "+pat.getFirstName())%>', 'searchPatientWindow')">
                                <% out.print(" " + pat.getFirstName()
                                        + " " + pat.getLastName()
                                        + " " + pat.getSex()
                                        + " " + pat.getBirthDayAsString()); %>
                            </a>
 </h4> </div>
                        <% } %>

                    <div>
                        <bean:message key="inboxmanager.document.LinkedProvidersMsg"/>
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

                        <bean:message key="inboxmanager.document.FlagProviderMsg"/>
                        <input type="hidden" name="provi" id="provfind<%=documentNo%>"/>
                        <input type="text" id="autocompleteprov<%=documentNo%>" name="demographicKeyword"/>
                        <input type="submit" name="save" id="save<%=documentNo%>" value="Save"/>

                        <a id="saveSucessMsg_<%=documentNo%>" style="display:none;color:blue;">
                            <bean:message key="inboxmanager.document.SuccessfullySavedMsg"/></a>
                        <div id="autocomplete_choicesprov<%=documentNo%>" class="autocomplete"></div>

                        <div id="providerList<%=documentNo%>"></div>

                    </div>

            </div>

        </div>

        <hr/>

        <div class="row">
            <div class="col-md-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Header information</h3>
                    </div>
                    <div class="panel-body">

                        <table class="table table-condensed">
                            <tbody>
                            <tr>
                                <td class="active col-md-2">Patient (named in document):</td>
                                <td ><%
                                    out.print(patient.getFirstName()
                                            + " " + patient.getLastName()
                                            + " " + patient.getGender()
                                            + " " + patient.getBirthdate()); %>
                                    <ul>
                                        <%
                                            for (CdxPersonId pid : cdxPersonIdDao.findIdsForPerson(patient.getId())) {
                                        %>
                                        <li>
                                            <%
                                                out.print(pid.getIdType() + ": " + pid.getIdCode());
                                            %>
                                        </li>
                                        <%
                                            }
                                        %>
                                    </ul>

                                </td>
                            </tr>
                            <tr>
                                <td class="active">Author, Date:</td>
                                <td><%
                                    out.print(cdxPersonDao.findRoleInDocumentNamesAsString(cdxDoc.getId(), CdxPerson.roleAuthor).get(0));
                                %> <br> <%
                                    out.print(cdxDoc.getAuthoringTimeAsString());
                                %></td>
                            </tr>
                            <tr>
                                <td class="active">Status:</td>
                                <td><%
                                    out.print(cdxDoc.getStatusCode());
                                %></td>
                            </tr>
                            <tr>
                                <td class="active">Custodian:</td>
                                <td><%
                                    out.print(cdxDoc.getCustodian());
                                %></td>
                            </tr>
                            <tr>
                                <td class="active">Device, Time:</td>
                                <td><%
                                    out.print(cdxDoc.getDevice());
                                %> <br> <%
                                    out.print(cdxDoc.getEffectiveTimeAsString());
                                %></td>
                            </tr>
                            <tr>
                                <td class="active">Parent document:</td>
                                <td><%
                                    out.print(cdxDoc.getParentDocId());
                                %></td>
                            </tr>
                            <tr>
                                <td class="active">Procedure:</td>
                                <td><%
                                    out.print(cdxDoc.getProcedureName());
                                %> <br> <%
                                    if (cdxDoc.getObservationDate() != null) {
                                        out.print(cdxDoc.getObservationDateAsString());
                                    }
                                %></td>
                            </tr>
                            <tr>
                                <td class="active">Procedure Performer:</td>
                                <td><%
                                    out.print(cdxPersonDao.findRoleInDocumentNamesAsString(cdxDoc.getId(), CdxPerson.roleProcedurePerformer).get(0));
                                %></td>
                            </tr>
                            <tr>
                                <td class="active">Recipients:</td>
                                <td class="col-md-3">

                                    <%
                                        out.print(cdxPersonDao.findRoleInDocumentNamesAsString(cdxDoc.getId(), CdxPerson.rolePrimaryRecipient).get(0) + " (Primary)");
                                    %>
                                    <ul>
                                        <%
                                            for (CdxPerson q : cdxPersonDao.findRoleInDocument(cdxDoc.getId(), CdxPerson.roleSecondaryRecipient)) {
                                        %> <li> <%
                                        out.print(q.getFullProviderName());
                                    %> </li> <%
                                        }
                                    %>

                                    </ul>

                                </td>
                            </tr>
                            <%
                                List<CdxPerson> ops = cdxPersonDao.findRoleInDocument(cdxDoc.getId(), CdxPerson.roleOrderingProvider);
                                if (!ops.isEmpty()) {
                            %>
                            <tr>
                                <td class="active">Ordering Provider:</td>

                                <td><%
                                    out.print(ops.get(0).getFullProviderName());
                                %></td>
                            </tr>
                            <%
                                }
                            %>

                            <%
                                List<CdxPerson> fps = cdxPersonDao.findRoleInDocument(cdxDoc.getId(), CdxPerson.roleFamilyProvider);
                                if (!fps.isEmpty()) {
                            %>
                            <tr>
                                <td class="active">Family Provider:</td>
                                <td><%
                                    out.print(fps.get(0).getFullProviderName());
                                %></td>
                            </tr>
                            <%
                                }
                            %>

                            <%
                                List<CdxPerson> pps = cdxPersonDao.findRoleInDocument(cdxDoc.getId(), CdxPerson.roleParticipatingProvider);
                                if (!pps.isEmpty()) {
                            %>
                            <tr>
                                <td class="active">Participating Providers:</td>
                                <td><%
                                    List<String> names = cdxPersonDao.findRoleInDocumentNamesAsString(cdxDoc.getId(), CdxPerson.roleParticipatingProvider);
                                    for (int i = 0; i < names.size(); i++) {
                                        out.print(names.get(i));
                                        if (i < names.size())
                                            out.print("<br>");
                                    }
                                %></td>
                            </tr>

                            <%
                                }
                            %>

                            <%
                                if (cdxDoc.getAdmissionDate()!=null || cdxDoc.getDischargeDate()!=null) {
                            %>
                            <tr>
                                <td class="active">Admission, Discharge:</td>
                                <td><%
                                    out.print(cdxDoc.getAdmissionDateAsString() + ", ");
                                    out.print(cdxDoc.getDischargeDateAsString());
                                %></td>
                            </tr>
                            <%
                                }
                            %>

                            <%
                                if (!cdxDoc.getDisposition().equals("")) {
                            %>
                            <tr>
                                <td class="active">Disposition:</td>
                                <td><%
                                    out.print(cdxDoc.getDisposition());
                                %></td>
                            </tr>
                            <%
                                }
                            %>
                            <%
                                List<CdxAttachment> attachments = cdxAttachmentDao.findByDocNo(cdxDoc.getId());

                                if (!attachments.isEmpty()) { %>
                            <tr>
                                <td class="active">Attachments:</td>
                                <td><%
                                    for (CdxAttachment a : attachments) {
                                        out.print(a.getReference());
                                        out.println(" (" + a.getAttachmentType() + ")");
                                    } %>
                                </td>
                            </tr>
                            <% } %>



                            </tbody>
                        </table>
                    </div> </div>
            </div>
            <div class="col-md-6">

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Content</h3>
                    </div>
                    <div class="panel-body">
                        <%
                            out.print(cdxDoc.getContents());
                        %>
                    </div>
                </div>

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

                    if (ui.item.providerNo != undefined && ui.item.providerNo != null && ui.item.providerNo != "" && ui.item.providerNo != "null") {
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

</script>

</body>
</html>