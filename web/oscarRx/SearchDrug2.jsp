<%@page contentType="text/html" pageEncoding="ISO-8859-1"%> 
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
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo" %>
<%@ page import="oscar.oscarRx.data.*,oscar.oscarProvider.data.ProviderMyOscarIdData,oscar.oscarDemographic.data.DemographicData,oscar.OscarProperties,oscar.log.*"%>
<%@ page import="org.oscarehr.common.model.OscarAnnotation" %>
<%@page import="org.oscarehr.casemgmt.service.CaseManagementManager"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<%@page import="java.util.Enumeration"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.casemgmt.web.PrescriptDrug"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.ArrayList"%>
<bean:define id="patient" type="oscar.oscarRx.data.RxPatientData.Patient" name="Patient" />

<%
            if (session.getAttribute("userrole") == null) {
                response.sendRedirect("../logout.jsp");
            }
            String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r" 
                   reverse="<%=true%>">
    <%
            //LogAction.addLog((String) session.getAttribute("user"), LogConst.NORIGHT+LogConst.READ,  LogConst.CON_PRESCRIPTION, demographic$, request.getRemoteAddr(),demographic$);

            response.sendRedirect("../noRights.html");
    %>
</security:oscarSec>

<%
            response.setHeader("Cache-Control", "no-cache");
%>
<logic:notPresent name="RxSessionBean" scope="session">
    <logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
    <bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean"
                 name="RxSessionBean" scope="session" />
    <logic:equal name="bean" property="valid" value="false">
        <logic:redirect href="error.html" />
    </logic:equal>
</logic:present>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
            oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) pageContext.findAttribute("bean");
%>

<%

            System.out.println("***### in SearchDrug2.jsp");
            int n = pageContext.getAttributesScope("bean");
            System.out.println("scope of attr=" + Integer.toString(n));
            Enumeration emn = pageContext.getAttributeNamesInScope(n);
            while (emn.hasMoreElements()) {
                System.out.println("attr name in scope n=" + emn.nextElement().toString());
            }
            System.out.println("bean.getStashIndex() searchDrug2.jsp=" + bean.getStashIndex());

            RxDrugData drugData = new RxDrugData();


            String id = request.getParameter("id");
            String text = request.getParameter("text");
            String rand = request.getParameter("rand");
            System.out.println("rand=" + rand);

            Enumeration emnn = request.getAttributeNames();
            while (emnn.hasMoreElements()) {
                System.out.println("request attr in SearchDrug2.jsp=" + emnn.nextElement());
            }
            RxDrugData.DrugSearch drugSearch = (RxDrugData.DrugSearch) request.getAttribute("drugSearch");
            if (drugSearch != null) {
                System.out.println("durgSearch is not null");
            }

            RxPharmacyData pharmacyData = new RxPharmacyData();
            RxPharmacyData.Pharmacy pharmacy;
            pharmacy = pharmacyData.getPharmacyFromDemographic(Integer.toString(bean.getDemographicNo()));
            String prefPharmacy = "";
            if (pharmacy != null) {
                prefPharmacy = pharmacy.name;
            }



            //   System.out.println("pharmacy.address=" + pharmacy.address);
            String drugref_route = OscarProperties.getInstance().getProperty("drugref_route");
            if (drugref_route == null) {
                drugref_route = "";
            }

            //        System.out.println("d_route=" + drugref_route);
            String[] d_route = ("Oral," + drugref_route).split(",");
            //System.out.println("d_route="+d_route.toString());
            //     System.out.println("d_route.size" + "" + d_route.length);

            String annotation_display = org.oscarehr.casemgmt.model.CaseManagementNoteLink.DISP_PRESCRIP;

            oscar.oscarRx.data.RxPrescriptionData.Prescription[] prescribedDrugs;
            prescribedDrugs = patient.getPrescribedDrugScripts(); //this function only returns drugs which have an entry in prescription and drugs table
            String script_no = "";

%>
<html:html locale="true">
    <head>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
        <title><bean:message key="SearchDrug.title" /></title>
        <link rel="stylesheet" type="text/css" href="styles.css">

        <html:base />


        <link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
        <link rel="stylesheet" type="text/css" href="modaldbox.css"  />
        <script type="text/javascript" src="<c:out value="${ctx}/phr/phr.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/prototype.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/effects.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/controls.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/scriptaculous.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/rx.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/phr/phr.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/Oscar.js"/>"></script>
        <script type="text/javascript" src="<c:out value="modaldbox.js"/>"></script>

        <script type="text/javascript">
            

            function buildRoute() {

                pickRoute = "";
            <oscar:oscarPropertiesCheck property="drugref_route_search" value="on">
                <%for (int i = 0; i < d_route.length; i++) {%>
                        if (document.forms[2].route<%=i%>.checked) pickRoute += " "+document.forms[2].route<%=i%>.value;
                <%}%>
                        document.forms[2].searchRoute.value = pickRoute;
            </oscar:oscarPropertiesCheck>
                }


        </script>
        <style type="text/css">

            div.autocomplete {
                position:absolute;
                width:450px;
                background-color:white;
                border:1px solid #888;
                margin:0;
                padding:0;
            }
            div.autocomplete ul {
                list-style-type:none;
                margin:0;
                padding:0;
            }
            div.autocomplete ul li.selected { background-color: #ffb;}
            div.autocomplete ul li {
                list-style-type:none;
                display:block;
                margin:0;
                padding:2px;
                height:32px;
                cursor:pointer;
            }

        </style>
    </head>

    <%
            boolean showall = false;

            if (request.getParameter("show") != null) {
                if (request.getParameter("show").equals("all")) {
                    showall = true;
                }
            }
    %>

    <%
    String s=request.getParameter("represcribe2");if(s!=null){System.out.println("is represcribe2 here?"+s);}
    else{
    System.out.println("represcribe2 is not here");}
%>

    <body topmargin="0" leftmargin="0" vlink="#0000FF"  onload="initmb();document.forms[0].searchString.focus();"> <%--onload="load()">--%>

        <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1" height="100%">
            <%@ include file="TopLinks2.jsp" %><!-- Row One included here-->
            <tr>
                <%@ include file="SideLinksEditFavorites.jsp"%><!-- <td></td>Side Bar File --->
                <td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%" valign="top"><!--Column Two Row Two-->
                    <table cellpadding="0" cellspacing="2" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%">

                        <tr>
                            <td>
                                <%-- div class="DivContentSectionHead"><bean:message key="SearchDrug.section3Title" /></div --%>

                                <form id="drugForm" action=""> <!--onsubmit="getData();"-->
                                    <input type="hidden" name="demographicNo" value="<%=new Integer(patient.getDemographicNo()).toString()%>" />
                                    <table border="0">
                                        <tr valign="center">
                                            <td> 
                                                <div id="rxText"></div>
                                                <div id="autocomplete_choices" class="autocomplete"></div>
                                                <div><label for="drugName">DrugName:</label><input id="searchString" type="text" size="30" name="durgName" id="drugName" value=""  AUTOCOMPLETE=OFF /></div>
                                                <div class="buttonrow"><input id="saveButton" type="button" onclick="updateAllDrugs();return false;" value="Prescribe" />
                                                </div>
                                            </td>
                                            <td width="100">
                                            </td>
                                        </tr>

                                        <tr>
                                            <td colspan="3">
                                                <%-- input type="button" class="ControlPushButton" onclick="customWarning();" value="<bean:message key="SearchDrug.msgCustomDrug"/>" / --%>
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                            </td>
                        </tr>
                        <!--take 2 tabs for 20 days prn po od-->
                        <!--p> <span class="dialink" onclick="sm('parsedForm',1000,500)">SHOW FORM</span> </p-->
                        <!--div id="autocomplete_preview" class="autocomplete"></div-->
                        <div id="previewForm" style="display:none;">
                            <%--  <label style="float:left;width:180px;">Name:</label> <span id="drugName"> </span><br>
                                  <label style="float:left;width:180px;">Instructions: </label> <span id="instructions" > </span><br>
                                  <label style="float:left;width:180px;">Quantity:</label> <span id="quantity"></span><br>
                                  <label style="float:left;width:180px;">Repeats:</label> <span id="repeats" ></span><br>
                                  <label style="float:left;width:180px;">OutsideProviderName:</label> <span id="outsideProviderName" ></span><br>
                                  <label style="float:left;width:180px;">OutsideProviderOhip:</label> <span id="outsideProviderOhip" ></span><br>
                                  <label style="float:left;width:180px;">Start Date: </label> <span id="rxDate" ></span><br>
                                  <label style="float:left;width:180px;">Written Date: </label> <span id="writtenDate" ></span><br>
                                  <label style="float:left;width:180px;">Last Refill Date: </label> <span id="lastRefillDate" ></span><br>


                                                           <bean:message key="WriteScript.method"/>:<span id="rxMethod"  />
                                                            <bean:message key="WriteScript.route"/>:<span id="rxRoute"  />
                                                            <bean:message key="WriteScript.frequency"/>:<span id="rxFreq"  />

                                                            <bean:message key="WriteScript.duration"/>:<span id="rxDuration" />
                                                            <bean:message key="WriteScript.durationUnit"/>:<span id="rxDurationUnit"  />
                                                            <bean:message key="WriteScript.amount"/>:<span id="rxAmount"  />
                                                            <bean:message key="WriteScript.startDate"/>:<input type="text" id="rxDate" name="rxDate"--%>

                            <%--    <bean:message key="WriteScript.msgPrescribedByOutsideProvider"/>
                                <input type="checkbox" id="ocheck"  onclick="$('otext').toggle();" />
                                <div id="otext" style="display:none;padding:2px;" >
                                    <b><bean:message key="WriteScript.msgName"/>:</b> <input type="text" id="outsideProviderName" name="outsideProviderName" />
                                    <b><bean:message key="WriteScript.msgOHIPNO"/>:</b> <input type="text" id="outsideProviderOhip" name="outsideProviderOhip" />
                                </div>
                                <bean:message key="WriteScript.msgPastMedication"/><input type="checkbox" name="pastMed" id="pastMed" /><br />
                                <bean:message key="WriteScript.msgPatientCompliance"/>:
                                <bean:message key="WriteScript.msgYes"/><input type="checkbox"  name="patientComplianceY" id="patientComplianceY" />

                                                            <bean:message key="WriteScript.msgNo"/><input type="checkbox"  name="patientComplianceN" id="patientComplianceN" /><br/>

                                                            <bean:message key="WriteScript.msgLastRefillDate"/>:<input type="text" id="lastRefillDate"  name="lastRefillDate" onfocus="javascript:lastRefillDate.value='';" />
                                                            <bean:message key="WriteScript.msgRxWrittenDate"/>: <input type="text" id="writtenDate"  name="writtenDate"
                            --%>
                            <%--    <div id="popSave" style="text-align:center">Save data<br><button onclick="getData();">Save</button></div>
                                <div id="popEdit" style="text-align:center">Edit data<br><button onclick="hm('parsedForm')">Edit</button></div>--%>
                        </div>
                        <tr>
                            <td>
                                <div>
                                    <table width="100%">
                                        <tr>
                                            <td>
                                                <div class="DivContentSectionHead"><bean:message key="SearchDrug.section2Title" /> (<a href="javascript:popupWindow(720,700,'PrintDrugProfile.jsp','PrintDrugProfile')"><bean:message key="SearchDrug.Print"/></a>) &nbsp;&nbsp;(<a href="#"
                                                                                                                                                                                                                                                                                    onclick="$('reprint').toggle();return false;"><bean:message key="SearchDrug.Reprint"/></a>)</div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div style="height: 100px; overflow: auto; background-color: #DCDCDC; border: thin solid green; display: none;" id="reprint">
                                                    <%
            //   System.out.println("prescribedDrugs.length=" + prescribedDrugs.length);
            for (int i = 0; i < prescribedDrugs.length; i++) {
                oscar.oscarRx.data.RxPrescriptionData.Prescription drug = prescribedDrugs[i];
                if (drug.getScript_no() != null && script_no.equals(drug.getScript_no())) {
                                                    %>
                                                    <br>
                                                    <div style="float: left; width: 24%; padding-left: 40px;">&nbsp;</div>
                                                    <a style="float: left;" href="javascript:reprint('<%=drug.getScript_no()%>')"><%=drug.getRxDisplay()%></a>
                                                    <%
                } else {
                                                    %>
                                                    <%=i > 0 ? "<br style='clear:both;'><br style='clear:both;'>" : ""%><div style="float: left; width: 12%; padding-left: 20px;"><%=drug.getRxDate()%></div>
                                                    <div style="float: left; width: 12%; padding-left: 20px;"><%=drug.getNumPrints()%>&nbsp;Prints</div>
                                                    <a style="float: left;" href="javascript:reprint('<%=drug.getScript_no()%>')"><%=drug.getRxDisplay()%></a>
                                                    <%
                }
                script_no = drug.getScript_no() == null ? "" : drug.getScript_no();
            }
                                                    %>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <table><!--this table here-->
                                                    <tr>
                                                        <td width="100%">
                                                            <div style="margin-top: 10px; margin-left: 20px; width: 100%"><!--show buttons-->
                                                                <table width="100%" cellspacing="0" cellpadding="0">
                                                                    <tr>
                                                                        <td align="left">
                                                                            <%
            String show = "&show=all";
            if (showall) {
                                                                            %> <a href="SearchDrug.jsp"><bean:message key="SearchDrug.msgShowCurrent"/></a> <%                                                                                } else {
                show = "";
                                                                            %> <a href="SearchDrug.jsp?show=all"><bean:message key="SearchDrug.msgShowAll"/></a> <%
            }
                                                                            %> &nbsp;&nbsp;&nbsp; <a href="SearchDrug.jsp?status=active<%=show%>"><bean:message key="SearchDrug.msgActive"/></a> - <a
                                                                                href="SearchDrug.jsp?status=inactive<%=show%>"><bean:message key="SearchDrug.msgInactive"/></a> - <a href="SearchDrug.jsp?status=all<%=show%>"><bean:message key="SearchDrug.msgAll"/></a></td>
                                                                        <td align="right">

                                                                            <span style="width: 350px; align: right">
                                                                                <input type="button" name="cmdAllergies" value="<bean:message key="SearchDrug.msgViewEditAllergies"/>" class="ControlPushButton" onclick="javascript:window.location.href='ShowAllergies.jsp';" style="width: 100px" />
                                                                                <input type="button" name="cmdRePrescribe" value="<bean:message key="SearchDrug.msgReprescribeLongTermMed"/>" class="ControlPushButton" onclick="javascript:RePrescribeLongTerm();" style="width: 100px" />
                                                                                <input type="button" name="cmdDelete" value="<bean:message key="SearchDrug.msgDelete"/>" class="ControlPushButton" onclick="javascript:Delete();" style="width: 100px" />
                                                                            </span>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </div>
                                                            <div class="Step1Text" style="width: 100%">
                                                                <table width="100%" cellpadding="3">
                                                                    <tr>
                                                                        <th align="left"><b>Rx Date</b></th>
                                                                        <th align="left"><b><bean:message key="SearchDrug.msgPrescription"/></b></th>
                                                                        <th align="center" width="100px"><b><bean:message key="SearchDrug.msgReprescribe"/></b></th>
                                                                        <th align="center" width="100px"><b><bean:message key="SearchDrug.msgDelete"/></b></th>
                                                                        <th align="center" width="20px">&nbsp;</th>
                                                                        <%
            boolean integratorEnabled = LoggedInInfo.loggedInInfo.get().currentFacility.isIntegratorEnabled();

            if (integratorEnabled) {%>
                                                                        <td align="center"><bean:message key="SearchDrug.msgLocationPrescribed"/></td>
                                                                        <%}%>
                                                                    </tr>

                                                                    <%
            CaseManagementManager caseManagementManager = (CaseManagementManager) SpringUtils.getBean("caseManagementManager");
            System.out.println("patient.getDemographicNo()=" + patient.getDemographicNo());
            if (showall) {
                System.out.println("showall=true");
            } else {
                System.out.println("showall=false");
            }
            List<PrescriptDrug> prescriptDrugs = caseManagementManager.getPrescriptions(patient.getDemographicNo(), showall);

            long now = System.currentTimeMillis();
            long month = 1000L * 60L * 60L * 24L * 30L;
            //String [] listLongTermMed=new String[prescriptDrugs.size()];
            List listLongTermMed = new ArrayList();
            // int counter=0;
            for (PrescriptDrug prescriptDrug : prescriptDrugs) { //loop through all drugs prescribed
                String styleColor = "";
                System.out.println("prescriptDrug's local id=" + Integer.toString(prescriptDrug.getLocalDrugId()));
                if (request.getParameter("status") != null) { //TODO: Redo this in a better way
                    String stat = request.getParameter("status");
                    if (stat != null && stat.equals("active") && prescriptDrug.isExpired()) {
                        continue;
                    } else if (stat != null && stat.equals("inactive") && !prescriptDrug.isExpired()) {
                        continue;
                    }
                }
                //add all long term med drugIds to an array.
                if (prescriptDrug.isLongTerm()) {
                    System.out.println("long term med's prescriptDrug.getLocalDrugId()=" + prescriptDrug.getLocalDrugId());
                    // listLongTermMed[counter]=""+prescriptDrug.getLocalDrugId();
                    // counter++;
                    listLongTermMed.add(prescriptDrug.getLocalDrugId());
                }

                if (!prescriptDrug.isExpired() && prescriptDrug.isDrug_achived()) {
                    styleColor = "style=\"color:red;text-decoration: line-through;\"";
                } else if (!prescriptDrug.isExpired() && (prescriptDrug.getEnd_date().getTime() - now <= month)) {
                    styleColor = "style=\"color:orange;font-weight:bold;\"";
                } else if (!prescriptDrug.isExpired() && !prescriptDrug.isDrug_achived()) {
                    styleColor = "style=\"color:red;\"";
                } else if (prescriptDrug.isExpired() && prescriptDrug.isDrug_achived()) {
                    styleColor = "style=\"text-decoration: line-through;\"";
                }
                                                                    %>
                                                                    <tr>
                                                                        <td valign="top"><a <%=styleColor%> href="StaticScript.jsp?regionalIdentifier=<%=prescriptDrug.getRegionalIdentifier()%>&cn=<%=response.encodeURL(prescriptDrug.getCustomName())%>"> <%=prescriptDrug.getDate_prescribed()%> </a></td>
                                                                        <td><a <%=styleColor%> href="StaticScript.jsp?regionalIdentifier=<%=prescriptDrug.getRegionalIdentifier()%>&cn=<%=response.encodeURL(prescriptDrug.getCustomName())%>"> <%=RxPrescriptionData.getFullOutLine(prescriptDrug.getDrug_special()).replaceAll(";", " ")%>
                                                                            </a></td>
                                                                        <td width="100px" align="center">
                                                                            <%
                if (prescriptDrug.getRemoteFacilityName() == null) {
                                                                            %>
                                                                            <%--  <input type="checkbox" name="chkRePrescribe" align="center" onclick="showOldRxDrug(this)" value="<%=prescriptDrug.getLocalDrugId()%>" />--%>
                                                                            <a name="rePrescribe" id="<%=prescriptDrug.getLocalDrugId()%>" <%=styleColor%> href="javascript:void()" onclick="showOldRxDrug(this)"> <%="RePrescribe"%>
                                                                            </a>
                                                                            <%
                            } else {
                                                                            %>
                                                                            <form action="<%=request.getContextPath()%>/oscarRx/searchDrug.do" method="post">
                                                                                <input type="hidden" name="demographicNo" value="<%=patient.getDemographicNo()%>" />
                                                                                <%
                                String searchString = prescriptDrug.getBN();
                                if (searchString == null) {
                                    searchString = prescriptDrug.getCustomName();
                                }
                                if (searchString == null) {
                                    searchString = prescriptDrug.getRegionalIdentifier();
                                }
                                if (searchString == null) {
                                    searchString = prescriptDrug.getDrug_special();
                                }
                                                                                %>
                                                                                <input type="hidden" name="searchString" value="<%=searchString%>" />
                                                                                <input type="submit" class="ControlPushButton" value="Search to Re-prescribe" />
                                                                            </form>
                                                                            <%
                }
                                                                            %>
                                                                        </td>
                                                                        <td width="100px" align="center">
                                                                           <%
               if (prescriptDrug.getRemoteFacilityName() == null) {
                                                                           %>
                                                                           <a name="delete" id="<%=prescriptDrug.getLocalDrugId()%><%
                                System.out.println("drugId in SearchDrug2.jsp=" + prescriptDrug.getLocalDrugId());%>" <%=styleColor%> href="javascript:void()" onclick="Delete(this);"> <%="Delete"%>
                                                                            </a>
                                                                        <!--input type="checkbox" name="chkDelete" align="center" drugId="<%--=prescriptDrug.getLocalDrugId()--%>" /-->
                                                                            <%
                }
                                                                            %>
                                                                        </td>
                                                                        <td width="20px" align="center"><a href="#" title="Annotation"
                                                                                                           onclick="window.open('../annotation/annotation.jsp?display=<%=annotation_display%>&table_id=<%=prescriptDrug.getLocalDrugId()%>&demo=<%=bean.getDemographicNo()%>','anwin','width=400,height=250');"> <img src="../images/notes.gif" border="0"></a>
                                                                        </td>
                                                                        <%
                if (integratorEnabled) {
                                                                        %>
                                                                        <td align="center"><%=prescriptDrug.getRemoteFacilityName() == null ? "local" : prescriptDrug.getRemoteFacilityName()%></td>
                                                                        <%
                }
                                                                        %>
                                                                    </tr>
                                                                    <%
            }
                                                                    %>
                                                                </table>

                                                            </div>
                                                            <html:form action="/oscarRx/rePrescribe">
                                                                <html:hidden property="drugList" />
                                                                <input type="hidden" name="method">
                                                            </html:form> <br>
                                                            <html:form  action="/oscarRx/deleteRx">
                                                                <html:hidden property="drugList" />
                                                            </html:form></td>
                                                    <input type="hidden" id="longTermDrugList" value="<%=listLongTermMed%>"/>
                                        </tr>
                                    </table>
                            </td>
                        </tr>
                    </table>
                    </div>
                </td>
            </tr>


            <logic:notEqual name="bean" property="stashSize" value="0">
                <tr>
                    <td> <html:form action="/oscarRx/stash">
                            <html:hidden property="action" />
                            <html:hidden property="stashId" />
                        </html:form>

                        <div class="DivContentSectionHead"><bean:message key="WriteScript.section5Title" /></div>
                    </td>
                </tr>
                <tr>
                    <td>
                <element>
                    <table cellspacing="0" cellpadding="5">
                        <%int i = 0;%>
                        <logic:iterate id="rx" name="bean" property="stash" length="stashSize">
                            <tr>
                                <td><a href="javascript:submitPending(<%=i%>, 'edit');"><bean:message key="SearchDrug.msgEdit"/></a></td>
                                <td><a href="javascript:submitPending(<%=i%>, 'delete');"><bean:message key="SearchDrug.msgDelete"/></a></td>
                                <td><a href="javascript:submitPending(<%=i%>, 'edit');"> <bean:write name="rx" property="rxDisplay" /> </a></td>
                                <td><a href="javascript:ShowDrugInfo('<%=((oscar.oscarRx.data.RxPrescriptionData.Prescription) rx).getGenericName()%>');"><bean:message key="SearchDrug.msgInfo"/></a></td>
                            </tr>
                            <% i++;%>
                        </logic:iterate>
                    </table></element>
                <br>
                <input type="button" class="ControlPushButton" onclick="javascript:window.location.href='viewScript.do';" value="<bean:message key="SearchDrug.msgSaveAndPrint"/>" /></td>
        </tr>
    </logic:notEqual>
    <!----End new rows here-->
    <tr height="100%">
        <td></td>
    </tr>
</table>
</td>
</tr>
<tr>
    <td height="0%" style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
    <td height="0%" style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
</tr>
<tr>
    <td width="100%" height="0%" colspan="2">&nbsp;</td>
</tr>
<tr>
    <td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC" colspan="2"></td>
</tr>
</table>
<%
            Enumeration em = request.getAttributeNames();
            System.out.println("attributes in request in search");
            while (em.hasMoreElements()) {
                System.out.println("attribute=" + em.nextElement().toString());
            }
            if (pharmacy != null) {%>
<div id="Layer1" style="position: absolute; left: 1px; top: 1px; width: 350px; height: 311px; visibility: hidden; z-index: 1"><!--  This should be changed to automagically fill if this changes often -->

    <table border="0" cellspacing="1" cellpadding="1" align="center" class="hiddenLayer">
        <tr class="LightBG">
            <td class="wcblayerTitle">&nbsp;</td>
            <td class="wcblayerTitle">&nbsp;</td>
            <td class="wcblayerTitle" align="right"><a href="javascript: function myFunction() {return false; }" onclick="hidepic('Layer1');" style="text-decoration: none;">X</a></td>
        </tr>

        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgName"/></td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.name%></td>
        </tr>

        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgAddress"/></td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.address%></td>
        </tr>
        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgCity"/></td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.city%></td>
        </tr>

        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgProvince"/></td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.province%></td>
        </tr>
        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgPostalCode"/> :</td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.postalCode%></td>
        </tr>
        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgPhone1"/> :</td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.phone1%></td>
        </tr>
        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgPhone2"/> :</td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.phone2%></td>
        </tr>
        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgFax"/> :</td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.fax%></td>
        </tr>
        <tr class="LightBG">
            <td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgEmail"/> :</td>
            <td class="wcblayerItem">&nbsp;</td>
            <td><%=pharmacy.email%></td>
        </tr>
        <tr class="LightBG">
            <td colspan="3" class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgNotes"/> :</td>
        </tr>
        <tr class="LightBG">
            <td colspan="3"><%=pharmacy.notes%></td>
        </tr> </table></div>  <%}%>
<script type="text/javascript">
    var countPrescribe=-1;
    function setCounter(n){
        countPrescribe=n;
    }
    function increaseCountPrescribe(){
        countPrescribe++;
        return countPrescribe;
    }
    function getCounter(){
        return countPrescribe;
    }
    function initalizeCountPrescribe()
    {
    countPrescribe=-1;
    }
    function RePrescribeLongTerm(){
        var longTermDrugs=$(longTermDrugList).value;
       // alert(longTermDrugs);
        var ar=longTermDrugs.split("[");
        var s=ar[1];
        var ar2=s.split("]");
        var s2=ar2[0];
        var ar3=s2.split(",");

        //ar3 is an array of drugIds which are longterm med.
        for(var i=0;i<ar3.length;i++){
            var s=ar3[i];
            if(showOldRxDrug(s.replace(" ",""))){ //"trim" s
                oscarLog("--"+s+" done one round");
            }
        }
    }
    function Delete(element){
        oscarLog(document.forms[2].action);

        if(confirm('Are you sure you wish to delete the selected prescriptions?')==true){
            oscarLog(element.id);
            document.forms[2].drugList.value = element.id;
            document.forms[2].submit();
        }
    }
    //not used
    function createNewRx(element){
       oscarLog("createNewRx called");
       oscarLog("countPrescribe in createNewRx="+countPrescribe);
       //countPrescribe=increaseCountPrescribe();
       var ar=(element.id).split("_");
       var randomId=ar[1];
        var data="drugName="+element.value+"&countPrescribe="+countPrescribe+"&randomId="+randomId;
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?method=createNewRx";
        new Ajax.Request(url, {method: 'get',parameters:data})
        return false;
    }

    function updateAllDrugs(){

        var data=Form.serialize($('drugForm'))
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?method=updateAllDrugs";
        new Ajax.Request(url,
        {method: 'post',postBody:data,
            onSuccess:function(transport){
                oscarLog("successfully sent data "+url);
                popForm();
            }});

        return false;
    }

     function updateDrug2(randomId){//save drug to session.


        //make a call to stashaction to set the bean stash index.
        var data1="randomId="+randomId;
        var url1= "<c:out value="${ctx}"/>" + "/oscarRx/stash.do?method=setStashIndex";
        new Ajax.Request(url1,
        {method: 'post',postBody:data1,
            onSuccess:function(transport){
                oscarLog("successfully sent data to stashAction "+url1);

        var rand=randomId;
        var drugName="drugName_"+rand;
        var instructions="instructions_"+rand;
        var quantity="quantity_"+rand;
        var repeats="repeats_"+rand;
        var startDate="rxDate_"+rand;
        var writtenDate="writtenDate_"+rand;
        var lastRefillDate="lastRefillDate_"+rand;
        var longTerm="longTerm_"+rand;
        var ocheck="ocheck_"+rand;
        var outsideProviderName="outsideProviderName_"+rand;
        var outsideProviderOhip="outsideProviderOhip_"+rand;
        var pastMed="pastMed_"+rand;
        var patientComplianceY="patientComplianceY_"+rand;
        var patientComplianceN="patientComplianceN_"+rand;

        var data=drugName+"="+$(drugName).value+"&"+instructions+"="+$(instructions).value+"&"+quantity+"="+$(quantity).value+"&"+repeats+"="+$(repeats).value;

        if($(writtenDate).value!=null){
            data=data+"&"+writtenDate+"="+$(writtenDate).value;
        }
        if($(startDate).value!=null){
            data=data+"&"+startDate+"="+$(startDate).value;
        }
        if($(lastRefillDate).value!=null){
            data=data+"&"+lastRefillDate+"="+$(lastRefillDate).value;
        }
        if($(longTerm).checked){
            data=data+"&"+longTerm+"=on";
        }else{
            data=data+"&"+longTerm+"=off";
        }if($(pastMed).checked){
            data=data+"&"+pastMed+"=on";
        }else{
            data=data+"&"+pastMed+"=off";
        }if($(ocheck).checked){
            data=data+"&"+ocheck+"=on"+"&"+outsideProviderName+"="+$(outsideProviderName).value+"&"+outsideProviderOhip+"="+$(outsideProviderOhip).value;
        }else{
            data=data+"&"+ocheck+"=off"+"&"+outsideProviderName+"=''&"+outsideProviderOhip+"=''";
        }if($(patientComplianceY).checked){
            data=data+"&"+patientComplianceY+"=on";
        }else{
            data=data+"&"+patientComplianceY+"=off";
        }if($(patientComplianceN).checked){
            data=data+"&"+patientComplianceN+"=on";
        }else{
            data=data+"&"+patientComplianceN+"=off";
        }
        //oscarLog(data);
        //make call to server, send data to server.
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?method=updateDrug";
        new Ajax.Request(url,
        {method: 'post',postBody:data,
            onSuccess:function(transport){
                oscarLog("successfully sent data "+url);
            }});

}});
        return false;

    }

    function updateDrug(element,whichPrescribe){//save drug to session.
        //get the rand number
        oscarLog("whichPrescribe="+whichPrescribe);
        //make a call to stashaction to set the bean stash index.
        var data1="whichPrescribe="+whichPrescribe;
        var url1= "<c:out value="${ctx}"/>" + "/oscarRx/stash.do?method=setStashIndex";
        new Ajax.Request(url1,
        {method: 'post',postBody:data1,
            onSuccess:function(transport){
                oscarLog("successfully sent data to stashAction "+url1);
            

        var elemId=element.id;
        var ar=elemId.split("_");
        var rand=ar[1];
        var drugName="drugName_"+rand;
        var instructions="instructions_"+rand;
        var quantity="quantity_"+rand;
        var repeats="repeats_"+rand;
        var startDate="rxDate_"+rand;
        var writtenDate="writtenDate_"+rand;
        var lastRefillDate="lastRefillDate_"+rand;
        var longTerm="longTerm_"+rand;
        var ocheck="ocheck_"+rand;
        var outsideProviderName="outsideProviderName_"+rand;
        var outsideProviderOhip="outsideProviderOhip_"+rand;
        var pastMed="pastMed_"+rand;
        var patientComplianceY="patientComplianceY_"+rand;
        var patientComplianceN="patientComplianceN_"+rand;

        var data=drugName+"="+$(drugName).value+"&"+instructions+"="+$(instructions).value+"&"+quantity+"="+$(quantity).value+"&"+repeats+"="+$(repeats).value;

        if($(writtenDate).value!=null){
            data=data+"&"+writtenDate+"="+$(writtenDate).value;            
        }
        if($(startDate).value!=null){
            data=data+"&"+startDate+"="+$(startDate).value;            
        }
        if($(lastRefillDate).value!=null){
            data=data+"&"+lastRefillDate+"="+$(lastRefillDate).value;            
        }
        if($(longTerm).checked){
            data=data+"&"+longTerm+"=on";
        }else{
            data=data+"&"+longTerm+"=off";
        }if($(pastMed).checked){
            data=data+"&"+pastMed+"=on";
        }else{
            data=data+"&"+pastMed+"=off";
        }if($(ocheck).checked){
            data=data+"&"+ocheck+"=on"+"&"+outsideProviderName+"="+$(outsideProviderName).value+"&"+outsideProviderOhip+"="+$(outsideProviderOhip).value;
        }else{
            data=data+"&"+ocheck+"=off"+"&"+outsideProviderName+"=''&"+outsideProviderOhip+"=''";
        }if($(patientComplianceY).checked){
            data=data+"&"+patientComplianceY+"=on";
        }else{
            data=data+"&"+patientComplianceY+"=off";
        }if($(patientComplianceN).checked){
            data=data+"&"+patientComplianceN+"=on";
        }else{
            data=data+"&"+patientComplianceN+"=off";
        }
        //oscarLog(data);
        //make call to server, send data to server.
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?method=updateDrug";
        new Ajax.Request(url,
        {method: 'post',postBody:data,
            onSuccess:function(transport){
                oscarLog("successfully sent data "+url);
            }});

}});
        return false;

    }

    function popForm(){
        try{
            var url= "<c:out value="${ctx}"/>" + "/oscarRx/Preview2.jsp";
        
            var params = "demographicNo=<%=bean.getDemographicNo()%>";  //hack to get around ie caching the page
            new Ajax.Updater('previewForm',url, {method:'get',parameters:params,asynchronous:true,evalScripts:true,onComplete:function(transport){
                    sm('previewForm',400,650);
                }});
        }
        catch(er){alert(er);}
    }
      

   // console.log("before="+document.getElementById("autocomplete_choices"));
    new Ajax.Autocompleter("searchString", "autocomplete_choices", "search2.jsp", {minChars:2, paramName:"searchString", updateElement:upElement});
    
   // console.log("after");
   // var ran_number;
    function upElement(li){
        //console.log('In up Element '+li);
        //alert("here in upelement");
        oscarLog($(li).innerHTML);
        getSelectionId($(li).innerHTML,li);
        $('searchString').value = "";
    }
    
    function getSelectionId(text, li) {
       var ran_number=Math.round(Math.random()*1000000);
        oscarLog('In selection id');
        var url1=  "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?method=createNewRx";
        //var url = "prescribe.jsp";
        var data1="randomId="+ran_number+"&drugId="+li.id+"&text="+text;
       // countPrescribe=increaseCountPrescribe();
        //   alert(li.id+"||"+ text+"||"+ ran_number);
        new Ajax.Updater('rxText',url1, {method:'get',parameters:data1,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom})
    }


    //show original drug for represcription.
    function showOldRxDrug(element){
      var  ran_number=Math.round(Math.random()*1000000);
        oscarLog("randome number="+ran_number);
        var rxMethod="rxMethod_"+ran_number;
        var rxRoute="rxRoute_"+ran_number;
        var rxFreq="rxFreq_"+ran_number;
        var rxDrugForm="rxDrugForm_"+ran_number;
        var rxDuration="rxDuration_"+ran_number;
        var rxDurationUnit="rxDurationUnit_"+ran_number;
        var rxAmount="rxAmount_"+ran_number;
        var rxPRN="rxPRN_"+ran_number;
        var drugName="drugName_"+ran_number;
        var instructions="instructions_"+ran_number;
        var quantity="quantity_"+ran_number;
        var repeats="repeats_"+ran_number;
        var rxDate="rxDate_"+ran_number;
        var intrParseDiv="intr_parse_"+ran_number;
        var outsideProviderName="outsideProviderName_"+ran_number;
        var outsideProviderOhip="outsideProviderOhip_"+ran_number;
        var lastRefillDate ="lastRefillDate_"+ran_number;
        var  writtenDate="writtenDate_"+ran_number;
        var rxMoreDiv="rx_more_"+ran_number;
        var longTerm="longTerm_"+ran_number;

        var pastMed="pastMed_"+ran_number;
        var patientComplianceY="patientComplianceY_"+ran_number;
        var patientComplianceN="patientComplianceN_"+ran_number;

        var data;
        var drugId;
        var counter=increaseCountPrescribe();
        oscarLog(element.id);
        if(element.id==undefined){
            data="drugId="+element;
            drugId=element;
        }
        else{
            data="drugId="+element.id;
            drugId=element.id;
        }
        var rxString="rxString_"+ran_number;

        data=data+"&countPrescribe="+counter;
        oscarLog("data set in showOldRxDrug="+data);

        var url= "<c:out value="${ctx}"/>" + "/oscarRx/rePrescribe2.do?method=represcribe2";
        oscarLog("url in searchdrug2 "+url);
        new Ajax.Request(url,
        {method: 'post',postBody:data,
            onSuccess:function(transport){

                var json=transport.responseText.evalJSON();
                var drugName=json.drugName;
                var url = "prescribe.jsp";
                var warning=" WARNING: ";
                var params = "demographicNo=<%=bean.getDemographicNo()%>&id="+drugId+"&text="+drugName+"&rand="+ran_number+"&countPrescribe="+counter;  //hack to get around ie caching the page
                oscarLog('drugname '+drugName+' ran '+ran_number);
                new Ajax.Updater('rxText',url, {method:'get',parameters:params,asynchronous:false,evalScripts:true,insertion: Insertion.Bottom, onComplete:function(transport){

                        str="Method: "+json.method+"; Route:"+json.route+"; Frequency:"+json.frequency+"; Amount:"+json.amount+"; Duration:"+json.duration+"; DurationUnit:"+json.durationUnit;

                        var instr ;

                        //parse the quantity and repeats.
                        var qty=0;

                        var inAr=(json.instructions).split(" ");
                     //   alert(inAr);
                        for(var i=0;i<inAr.length;i++){
                            var elem=inAr[i];
                            if(elem.include("Qty")){
                                var eAr=elem.split(":");
                                qty=eAr[1];
                                break;
                            }

                        }
                    //    alert(inAr+"!!!");
                        oscarLog("qty="+qty);

                        //remove Qty:num Repeats:num
                        var re=new RegExp("Qty:[0-9]+");

                        instr=(json.instructions).replace(re,"");
                       // alert("first replace paseed"+instr);
                        re=new RegExp("Repeats:[0-9]+");
                        var instr2=instr.replace(re,"");
                       oscarLog("second replace paseed"+instr2);
                        $(instructions).value=instr2;
                      oscarLog(instructions);

                        oscarLog($(instructions).value);
                        if(qty==json.quantity){
                            $(quantity).value=json.quantity;
                        }
                        else{
                            $(quantity).value=qty;
                            $("quantityWarning_"+ran_number).innerHTML=warning+" Qty:"+qty+" setQuantity:"+json.quantity;
                        }

                        $(repeats).value=json.repeats;
                        //alert($(repeats).value);
                        $(rxString).innerHTML=str;

                        //     document.getElementById(intrParseDiv).style.display="table";
                        //    document.getElementById(rxMoreDiv).style.display="table";
                        //   $(rxMethod).value=json.method;
                        //  $(rxRoute).value=json.route;
                        //  $(rxFreq).value=json.frequency;
                        //   $(rxDrugForm).value=json.drugForm;
                        //   $(rxDuration).value=json.duration;
                        //   $(rxDurationUnit).value=json.durationUnit;
                        //   $(rxAmount).value=json.amount;

                        //  if(json.prn){
                        //        $(rxPRN).checked=true;
                        //    } else{
                        //        $(rxPRN).checked=false;
                        //    }
                        if(json.longTerm){
                            $(longTerm).checked=true;
                        } else{
                            $(longTerm).checked=false;
                        }

                        if(json.pastMed){
                            $(pastMed).checked=true;
                        } else{
                            $(pastMed).checked=false;
                        }
                        if(json.patientCompliance==1){
                            $(patientComplianceY).checked=true;
                        } else if(json.patientCompliance==-1){
                            $(patientComplianceN).checked=true;
                        } else{
                            $(patientComplianceY).checked=false;
                            $(patientComplianceN).checked=false;
                        }

                        $(rxDate).value=json.startDate;
                        $(writtenDate).value=json.writtenDate;
                        $(outsideProviderName).value=json.outsideProvName;
                        $(outsideProviderOhip).value=json.outsideProvOhip;
                        $(lastRefillDate).value=json.lastRefillDate;

                    }    });
            }});
        return true;
    }


    //show original drug for represcription.
    function showOldRxDrug(element){
      var  ran_number=Math.round(Math.random()*1000000);
        oscarLog("randome number="+ran_number);
        var rxMethod="rxMethod_"+ran_number;
        var rxRoute="rxRoute_"+ran_number;
        var rxFreq="rxFreq_"+ran_number;
        var rxDrugForm="rxDrugForm_"+ran_number;
        var rxDuration="rxDuration_"+ran_number;
        var rxDurationUnit="rxDurationUnit_"+ran_number;
        var rxAmount="rxAmount_"+ran_number;
        var rxPRN="rxPRN_"+ran_number;
        var drugName="drugName_"+ran_number;
        var instructions="instructions_"+ran_number;
        var quantity="quantity_"+ran_number;
        var repeats="repeats_"+ran_number;
        var rxDate="rxDate_"+ran_number;
        var intrParseDiv="intr_parse_"+ran_number;
        var outsideProviderName="outsideProviderName_"+ran_number;
        var outsideProviderOhip="outsideProviderOhip_"+ran_number;
        var lastRefillDate ="lastRefillDate_"+ran_number;
        var  writtenDate="writtenDate_"+ran_number;
        var rxMoreDiv="rx_more_"+ran_number;
        var longTerm="longTerm_"+ran_number;

        var pastMed="pastMed_"+ran_number;
        var patientComplianceY="patientComplianceY_"+ran_number;
        var patientComplianceN="patientComplianceN_"+ran_number;
        
        var data;
        var drugId;
        var counter=increaseCountPrescribe();
        oscarLog(element.id);
        if(element.id==undefined){
            data="drugId="+element;
            drugId=element;
        }
        else{
            data="drugId="+element.id;
            drugId=element.id;
        }
        var rxString="rxString_"+ran_number;

        data=data+"&countPrescribe="+counter;
        oscarLog("data set in showOldRxDrug="+data);

        var url= "<c:out value="${ctx}"/>" + "/oscarRx/rePrescribe2.do?method=represcribe2";
        oscarLog("url in searchdrug2 "+url);
        new Ajax.Request(url,
        {method: 'post',postBody:data,
            onSuccess:function(transport){

                var json=transport.responseText.evalJSON();
                var drugName=json.drugName;
                var url = "prescribe.jsp";
                var warning=" WARNING: ";
                var params = "demographicNo=<%=bean.getDemographicNo()%>&id="+drugId+"&text="+drugName+"&rand="+ran_number+"&countPrescribe="+counter;  //hack to get around ie caching the page
                oscarLog('drugname '+drugName+' ran '+ran_number);
                new Ajax.Updater('rxText',url, {method:'get',parameters:params,asynchronous:false,evalScripts:true,insertion: Insertion.Bottom, onComplete:function(transport){

                        str="Method: "+json.method+"; Route:"+json.route+"; Frequency:"+json.frequency+"; Amount:"+json.amount+"; Duration:"+json.duration+"; DurationUnit:"+json.durationUnit;

                        var instr ;

                        //parse the quantity and repeats.
                        var qty=0;

                        var inAr=(json.instructions).split(" ");
                     //   alert(inAr);
                        for(var i=0;i<inAr.length;i++){
                            var elem=inAr[i];
                            if(elem.include("Qty")){
                                var eAr=elem.split(":");
                                qty=eAr[1];
                                break;
                            }

                        }
                    //    alert(inAr+"!!!");
                        oscarLog("qty="+qty);

                        //remove Qty:num Repeats:num
                        var re=new RegExp("Qty:[0-9]+");
                
                        instr=(json.instructions).replace(re,"");
                       // alert("first replace paseed"+instr);
                        re=new RegExp("Repeats:[0-9]+");
                        var instr2=instr.replace(re,"");
                       oscarLog("second replace paseed"+instr2);
                        $(instructions).value=instr2;
                      oscarLog(instructions);

                        oscarLog($(instructions).value);
                        if(qty==json.quantity){
                            $(quantity).value=json.quantity;
                        }
                        else{
                            $(quantity).value=qty;
                            $("quantityWarning_"+ran_number).innerHTML=warning+" Qty:"+qty+" setQuantity:"+json.quantity;
                        }
                        
                        $(repeats).value=json.repeats;
                        //alert($(repeats).value);
                        $(rxString).innerHTML=str;

                        //     document.getElementById(intrParseDiv).style.display="table";
                        //    document.getElementById(rxMoreDiv).style.display="table";
                        //   $(rxMethod).value=json.method;
                        //  $(rxRoute).value=json.route;
                        //  $(rxFreq).value=json.frequency;
                        //   $(rxDrugForm).value=json.drugForm;
                        //   $(rxDuration).value=json.duration;
                        //   $(rxDurationUnit).value=json.durationUnit;
                        //   $(rxAmount).value=json.amount;

                        //  if(json.prn){
                        //        $(rxPRN).checked=true;
                        //    } else{
                        //        $(rxPRN).checked=false;
                        //    }
                        if(json.longTerm){
                            $(longTerm).checked=true;
                        } else{
                            $(longTerm).checked=false;
                        }

                        if(json.pastMed){
                            $(pastMed).checked=true;
                        } else{
                            $(pastMed).checked=false;
                        }
                        if(json.patientCompliance==1){
                            $(patientComplianceY).checked=true;
                        } else if(json.patientCompliance==-1){
                            $(patientComplianceN).checked=true;
                        } else{
                            $(patientComplianceY).checked=false;
                            $(patientComplianceN).checked=false;
                        }

                        $(rxDate).value=json.startDate;
                        $(writtenDate).value=json.writtenDate;
                        $(outsideProviderName).value=json.outsideProvName;
                        $(outsideProviderOhip).value=json.outsideProvOhip;
                        $(lastRefillDate).value=json.lastRefillDate;

                    }    });
            }});
        return true;
    }

    function checkQuantity(element){
        var elemId=element.id;
        var ar=elemId.split("_");
        var rand=ar[1];
        var calQ="calQuantity_"+rand;
        var warning=" WARNING: quantities don't match";
        //oscarLog($(calQ).value);
        //oscarLog(element.value);
        if(element.value==$(calQ).value){
            //if it contains warning, remove warning
            if(($("rxString_"+rand).innerHTML).include(warning)){
                var s=$("rxString_"+rand).innerHTML;
                $("rxString_"+rand).innerHTML=s.replace(/WARNING: quantities don't match/,"");
            }else{}
            //if it doesn't contain warning , do ntohing
            oscarLog("quantity entered equals to quantity calculated form instruction="+element.value);
        }else{
            //if it contains WARNING,do nothing
            if(($("rxString_"+rand).innerHTML).include(warning)){

            }else{
                //if it doesn't contain warning, add
                $("rxString_"+rand).innerHTML=$("rxString_"+rand).innerHTML + warning;
            }
            oscarLog("quantity entered does not match quantity calculated form instructions.");
        }
    }

    function parseIntr(element){
        var instruction="instruction="+element.value+"&action=parseInstructions";
        oscarLog(instruction);
        var elemId=element.id;
        var ar=elemId.split("_");
        var rand=ar[1];
        
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?method=updateDrug";
        var rxMethod="rxMethod_"+rand;
        var rxRoute="rxRoute_"+rand;
        var rxFreq="rxFreq_"+rand;
        var rxDrugForm="rxDrugForm_"+rand;
        var rxDuration="rxDuration_"+rand;
        var rxDurationUnit="rxDurationUnit_"+rand;
        var rxAmt="rxAmount_"+rand;
        var rxPRN="rxPRN_"+rand;
        var divId="intr_parse_"+rand;
        var str;
        var rxString="rxString_"+rand;
        var calQuantity="calQuantity_"+rand;
        //new Ajax.Request(url, method: 'post',postBody:instruction, onSuccess:function(transport)
        new Ajax.Request(url, {method: 'get',parameters:instruction, onSuccess:function(transport){
                var json=transport.responseText.evalJSON();
                /*$(rxMethod).value=json.method;
                $(rxRoute).value=json.route;
                $(rxAmt).value=json.amount;
                $(rxFreq).value=json.frequency;
                $(rxDuration).value=json.duration;
                $(rxDurationUnit).value=json.durationUnit;
                 */

                str="Method: "+json.method+"; Route:"+json.route+"; Frequency:"+json.frequency+"; Amount:"+json.amount+"; Duration:"+json.duration+"; DurationUnit:"+json.durationUnit;
                oscarLog("json.duration="+json.duration);
                $(calQuantity).value = json.calQuantity;
                oscarLog($(calQuantity).value);
                if(json.prn){
                    str=str+" prn";
                } else{
                    //$(rxPRN).checked=false;
                }
                oscarLog("str="+str);
                $(rxString).innerHTML=str;
                //document.getElementById(divId).style.display="table";
            }});                
        return true;
    }

    function getData(){
        var data=Form.serialize($('drugForm'))
        //   alert(data);
       
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?method=saveDrug";
        new Ajax.Request(url,
        {method: 'post',postBody:data,
            onSuccess:function(transport){
                oscarLog("successfully sent data "+url);
            }});
        
        return false;
    }

</script>

</body>
</html:html>
