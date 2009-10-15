<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        if (session.getAttribute("userrole") == null) response.sendRedirect("../logout.jsp");
        String roleName$ = (String)session.getAttribute("userrole") + "," + (String)session.getAttribute("user");
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
            RxPharmacyData pharmacyData = new RxPharmacyData();
            RxPharmacyData.Pharmacy pharmacy;
            pharmacy = pharmacyData.getPharmacyFromDemographic(Integer.toString(bean.getDemographicNo()));
            String prefPharmacy = "";
            if (pharmacy != null) {
                prefPharmacy = pharmacy.name;
            }

            String drugref_route = OscarProperties.getInstance().getProperty("drugref_route");
            if (drugref_route == null) {
                drugref_route = "";
            }
            String[] d_route = ("Oral," + drugref_route).split(",");

            String annotation_display = org.oscarehr.casemgmt.model.CaseManagementNoteLink.DISP_PRESCRIP;

             oscar.oscarRx.data.RxPrescriptionData.Prescription[] prescribedDrugs;
                        prescribedDrugs = patient.getPrescribedDrugScripts(); //this function only returns drugs which have an entry in prescription and drugs table
                        String script_no = "";

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->


<html:html locale="true">
    <head>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
        <title><bean:message key="SearchDrug.title" /></title>
        <link rel="stylesheet" type="text/css" href="styles.css">

        <html:base />


        <link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  ></link>
        <link rel="stylesheet" type="text/css" href="modaldbox.css"  />
        <script type="text/javascript" src="<c:out value="${ctx}/phr/phr.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/prototype.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/rx.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/scriptaculous.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/effects.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/controls.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/Oscar.js"/>"></script>
        <script type="text/javascript" src="<c:out value="modaldbox.js"/>"></script>



        <link rel="stylesheet" type="text/css" href="<c:out value="${ctx}/share/yui/css/fonts-min.css"/>" >
        <link rel="stylesheet" type="text/css" href="<c:out value="${ctx}/share/yui/css/autocomplete.css"/>" >
        <script type="text/javascript" src="<c:out value="${ctx}/share/yui/js/yahoo-dom-event.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/yui/js/connection-min.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/yui/js/animation-min.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/yui/js/datasource-min.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/yui/js/autocomplete-min.js"/>"></script>


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



           function popupRxSearchWindow(){
               var winX = (document.all)?window.screenLeft:window.screenX;
               var winY = (document.all)?window.screenTop:window.screenY;

               var top = winY+70;
               var left = winX+110;
               var url = "searchDrug.do?rx2=true&searchString="+$('searchString').value;
               popup2(600, 800, top, left, url, 'windowNameRxSearch<%=bean.getDemographicNo()%>');

           }

        </script>

        <style type="text/css">
#myAutoComplete {
    width:25em; /* set width here or else widget will expand to fit its container */
    padding-bottom:2em;
}

body {
	margin:0;
	padding:0;
}
</style>

    </head>

    <%
        boolean showall = false;

		if (request.getParameter("show") != null) if (request.getParameter("show").equals("all")) showall = true;
    %>

    

    <body  vlink="#0000FF" onload="initmb();load()" class="yui-skin-sam">
        <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1" height="100%">
            <%@ include file="TopLinks2.jsp" %><!-- Row One included here-->
            <tr>
                <%@ include file="SideLinksEditFavorites.jsp"%><%-- <td></td>Side Bar File --%>
                <td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%" valign="top"><!--Column Two Row Two-->
                    <table cellpadding="0" cellspacing="2" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%">

                        <tr>
                            <td>
                                <%-- div class="DivContentSectionHead"><bean:message key="SearchDrug.section3Title" /></div --%>
                                <html:form action="/oscarRx/searchDrug"  onsubmit="return processData();" styleId="drugForm">
                                    <div id="rxText" style="float:left;"></div><div id="interactionsRxMyD" style="float:right;"></div><br style="clear:left;">
                                
                                    <html:hidden property="demographicNo" value="<%=new Integer(patient.getDemographicNo()).toString()%>" />
                                    <table border="0">
                                        <tr valign="top">
                                            <td style="width:350px;"><bean:message key="SearchDrug.drugSearchTextBox"  />
                                                <html:text styleId="searchString" property="searchString"   size="16" maxlength="16" style="width:248px;\" autocomplete=\"off" />
                                                <div id="autocomplete_choices"></div>
                                                <span id="indicator1" style="display: none"><img src="/images/spinner.gif" alt="Working..." ></span>                                                


                                            </td>
                                            <td >
                                                <%--<html:submit property="submit" styleClass="ControlPushButton"><bean:message key="SearchDrug.msgSearch"/></html:submit> &nbsp;&nbsp;&nbsp; --%>
                                                <input type="button" name="search" class="ControlPushButton" value="<bean:message key="SearchDrug.msgSearch"/>" onclick="popupRxSearchWindow();">
                                                <a href="javascript:void(0);" onclick="callTreatments('searchString','treatmentsMyD')"><bean:message key="SearchDrug.msgDrugOfChoice" /></a>
                                                <%if (OscarProperties.getInstance().hasProperty("ONTARIO_MD_INCOMINGREQUESTOR")) {%>
                                                <a href="javascript:goOMD();"><bean:message key="SearchDrug.msgOMDLookup"/></a>
                                                <%}%>
                                                <div class="buttonrow"><input id="saveButton" type="button"  onclick="updateAllDrugs();return false;" value="Prescribe" />
                                                </div>
                                            </td>
                                            <td><oscar:oscarPropertiesCheck property="drugref_route_search" value="on">
                                                    <bean:message key="SearchDrug.drugSearchRouteLabel" />
                                                    <br>
                                                    <%for (int i = 0; i < d_route.length; i++) {%>
                                                    <input type="checkbox" name="route" <%=i%> value="<%=d_route[i].trim()%>"><%=d_route[i].trim()%> &nbsp;</input>
                                                    <%}%>
                                                    <html:hidden property="searchRoute" />
                                                </oscar:oscarPropertiesCheck></td>
                                        </tr>
                                        <tr>
                                            <td colspan="3">
                                                <%-- input type="button" class="ControlPushButton" onclick="customWarning();" value="<bean:message key="SearchDrug.msgCustomDrug"/>" / --%>
                                            </td>
                                        </tr>
                                    </table>
                                </html:form>
                                
                                
<div id="previewForm" style="display:none;"></div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div>
                                    <table width="100%">
                                        <tr>
                                            <td>
                                                <div class="DivContentSectionHead">
                                                    <bean:message key="SearchDrug.section2Title" /> (<a href="javascript:popupWindow(720,700,'PrintDrugProfile.jsp','PrintDrugProfile')">
                                                    <bean:message key="SearchDrug.Print"/></a>) &nbsp;&nbsp;(<a href="#" onclick="$('reprint').toggle();return false;"><bean:message key="SearchDrug.Reprint"/></a>)
                                                    &nbsp;&nbsp;
                                                    (<a href="javascript:void(0);" name="cmdAllergies"   onclick="javascript:window.location.href='ShowAllergies.jsp';" style="width: 200px" ><bean:message key="SearchDrug.msgViewEditAllergies"/></a>)
                                                    &nbsp;&nbsp;
                                                    (<a href="javascript:void(0);"name="cmdRePrescribe"  onclick="javascript:RePrescribeLongTerm();" style="width: 200px" ><bean:message key="SearchDrug.msgReprescribeLongTermMed"/></a>)

                                                    &nbsp;&nbsp;
                                                    (<a href="javascript: void(0);" onclick="callReplacementWebService('GetmyDrugrefInfo.do?method=view','interactionsRxMyD');" >DS run</a>)
                                                                            
                                                    <%--
                                                                                <input type="button" name="cmdAllergies" value="<bean:message key="SearchDrug.msgViewEditAllergies"/>" class="ControlPushButton" onclick="javascript:window.location.href='ShowAllergies.jsp';" style="width: 200px" />
                                                                                <input type="button" name="cmdRePrescribe" value="<bean:message key="SearchDrug.msgReprescribeLongTermMed"/>" class="ControlPushButton" onclick="javascript:RePrescribeLongTerm();" style="width: 200px" />
                                                                                <input type="button" name="cmdDelete" value="<bean:message key="SearchDrug.msgDelete"/>" class="ControlPushButton" onclick="javascript:Delete();" style="width: 100px" />
                                                --%>
                                                </div>

                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div style="height: 100px; overflow: auto; background-color: #DCDCDC; border: thin solid green; display: none;" id="reprint">
                                                    <%
                       

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
                                                <table border="0">
                                                    <tr>
                                                        <td width="80%">
                                                            <div style="margin-top: 10px; margin-left: 20px; width: 100%">
                                                                <table width="100%" cellspacing="0" cellpadding="0">
                                                                    <tr>
                                                                        <td align="right">
                                                                            <span style="width: 350px; align: right">
                                                                          <%--      <input type="button" name="cmdAllergies" value="<bean:message key="SearchDrug.msgViewEditAllergies"/>" class="ControlPushButton" onclick="javascript:window.location.href='ShowAllergies.jsp';" style="width: 100px" />--%>
                                                                                <input type="button" name="cmdRePrescribe" value="<bean:message key="SearchDrug.msgReprescribeLongTermMed"/>" class="ControlPushButton" onclick="javascript:RePrescribeLongTerm();" style="width: 100px" />
                                                                              <%--  <input type="button" name="cmdDelete" value="<bean:message key="SearchDrug.msgDelete"/>" class="ControlPushButton" onclick="javascript:Delete();" style="width: 100px" />--%>
                                                                            </span>
                                                                        </td>
                                                                        <td align="left">
                                                                            <a href="javascript:void(0);" onclick="callReplacementWebService('ListDrugs.jsp','drugProfile');"><bean:message key="SearchDrug.msgShowCurrent"/></a>
                                                                            <a href="javascript:void(0);" onclick="callReplacementWebService('ListDrugs.jsp?show=all','drugProfile');"><bean:message key="SearchDrug.msgShowAll"/></a>
                                                                            <a href="javascript:void(0);" onclick="callReplacementWebService('ListDrugs.jsp?status=active','drugProfile');"><bean:message key="SearchDrug.msgActive"/></a>
                                                                            <a href="javascript:void(0);" onclick="callReplacementWebService('ListDrugs.jsp?status=inactive','drugProfile');"><bean:message key="SearchDrug.msgInactive"/></a>
                                                                            <a href="javascript:void(0);" onclick="callReplacementWebService('ListDrugs.jsp?status=all','drugProfile');"><bean:message key="SearchDrug.msgAll"/></a>
                                                                        </td>
                                                                        <td align="right">

                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </div>
                                                            <div id="drugProfile"></div>

                                                            <html:form action="/oscarRx/rePrescribe">
                                                                <html:hidden property="drugList" />
                                                                <input type="hidden" name="method">
                                                            </html:form> <br>
                                                            <html:form action="/oscarRx/deleteRx">
                                                                <html:hidden property="drugList" />
                                                            </html:form></td>

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
    <td width="100%" height="0%" colspan="2">&nbsp;
        <%-- pre>
            Things that need to work
            JAC-Need data saving in the session.%
            JAC-Represcribe
            JAC-*REPRESCRIBE ALL long term meds
            JAC-Need delete to work
            -*discontinue!
            
            JAC-format "more" section
            +Adjust search to have required info returned. (What is the required info?  ATC, DIN, Route Form??
            -*How to limit on Route and Form
            -+*TREATMENTS%  *has bug if something is not found

            +ajax ds.%

        </pre
--%>
    </td>
</tr>

<tr>
    <td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC" colspan="2">
       
    </td>
</tr>

</table>



<div id="treatmentsMyD" style="position: absolute; left: 1px; top: 1px; width: 800px; height: 600px; visibility: hidden; z-index: 1">
       <a href="javascript: function myFunction() {return false; }" onclick="hidepic('treatmentsMyD');" style="text-decoration: none;">X</a>
</div>


<%
                        if (pharmacy != null) {
%>
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
        </tr>

    </table>

</div>
<%
                        }
%>
<script type="text/javascript">


    function Delete(element){
        oscarLog(document.forms[2].action);

        if(confirm('Are you sure you wish to delete the selected prescriptions?')==true){
            oscarLog(element.id);
            document.forms[2].drugList.value = element.id;
            document.forms[2].submit();
        }
    }
//represcribe long term meds
    function RePrescribeLongTerm(){
        //var longTermDrugs=$(longTermDrugList).value;
       // var data="drugIdList="+longTermDrugs;
       var demoNo=<%=patient.getDemographicNo()%>;
       var showall=<%=showall%>;
       oscarLog("demoNo="+demoNo);
        var data="demoNo="+demoNo+"&showall="+showall;
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/rePrescribe2.do?method=repcbAllLongTerm";

        new Ajax.Updater('rxText',url, {method:'get',parameters:data,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom})
        return false;
    }

     function createNewRx(element){
       oscarLog("createNewRx called");
        var data="drugName="+element.value;
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?method=createNewRx";
        new Ajax.Request(url, {method: 'get',parameters:data})
        return false;
    }
    
    //not used
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
        try{oscarLog("popForm called");
            var url= "<c:out value="${ctx}"/>" + "/oscarRx/Preview2.jsp";

            var params = "demographicNo=<%=bean.getDemographicNo()%>";  //hack to get around ie caching the page
            new Ajax.Updater('previewForm',url, {method:'get',parameters:params,asynchronous:true,evalScripts:true,onComplete:function(transport){
                    oscarLog( "preview2 done");
                    sm('previewForm',400,650);
                }});
        }
        catch(er){alert(er);}
        oscarLog("bottom of popForm");
    }



     function callTreatments(textId,id){
         var ele = $(textId);
         var url = "TreatmentMyD.jsp"
         var ran_number=Math.round(Math.random()*1000000);
         var params = "demographicNo=<%=bean.getDemographicNo()%>&cond="+ele.value+"&rand="+ran_number;  //hack to get around ie caching the page
         new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:true});
         showpic('treatmentsMyD');

     }

     function callReplacementWebService(url,id){
               var ran_number=Math.round(Math.random()*1000000);
               var params = "demographicNo=<%=bean.getDemographicNo()%>&rand="+ran_number;  //hack to get around ie caching the page
               new Ajax.Updater(id,url, {method:'get',parameters:params,asynchronous:true});
         }
          //callReplacementWebService("InteractionDisplay.jsp",'interactionsRx');
          <oscar:oscarPropertiesCheck property="MYDRUGREF_DS" value="yes">
          callReplacementWebService("GetmyDrugrefInfo.do?method=view",'interactionsRxMyD');
          </oscar:oscarPropertiesCheck>
          callReplacementWebService("ListDrugs.jsp",'drugProfile');

    <%--
    new Ajax.Autocompleter("searchString", "autocomplete_choices", "search2.jsp", {paramName: "searchString",
       minChars: 2,
       //afterUpdateElement : getSelectionId,
       updateElement : upElement
});

function getSelectionId(text, li) {
    oscarLog('In selection id');
    var url = "prescribe.jsp";
    var ran_number=Math.round(Math.random()*1000000);
    var params = "demographicNo=<%=bean.getDemographicNo()%>&id="+li.id+"&text="+text+"&rand="+ran_number;  //hack to get around ie caching the page
    new Ajax.Updater('rxText',url, {method:'get',parameters:params,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom
});
    
}

function upElement(li){
    //oscarLog('In up Element '+li);
    //alert($(li).innerHTML);
    getSelectionId($(li).innerHTML,li);
    $('searchString').value = "";
}

--%>


 //used in autocomplete
    function getSelectionId(text, li) {
       var ran_number=Math.round(Math.random()*1000000);
        oscarLog('In selection id');
        var url1=  "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?method=createNewRx";
        //var url = "prescribe.jsp";
        var data1="randomId="+ran_number+"&drugId="+li.id+"&text="+text;
       // countPrescribe=increaseCountPrescribe();
        //   alert(li.id+"||"+ text+"||"+ ran_number);
        new Ajax.Updater('rxText',url1, {method:'get',parameters:data1,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom})

                //var params = "demographicNo=<%--=bean.getDemographicNo()--%>&id="+li.id+"&text="+text+"&rand="+ran_number+"&notRePrescribe=true"+"&countPrescribe="+countPrescribe; //hack to get around ie caching the page
                  //  new Ajax.Updater('rxText',url, {method:'get',parameters:params,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom})}})
        //  chooseDrug();
    }
    
YAHOO.example.BasicRemote = function() {
    var oDS = new YAHOO.util.XHRDataSource("http://localhost:8080/drugref2/test4.jsp");
    //var oDS = new YAHOO.util.XHRDataSource("http://localhost:8084/drugref2/test4.jsp");
    oDS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;// Set the responseType
    // Define the schema of the delimited results
    oDS.responseSchema = {
        resultsList : "results",
        fields : ["name", "id"]
    };
    // Enable caching
    oDS.maxCacheEntries = 500;
    // Instantiate the AutoComplete
    var oAC = new YAHOO.widget.AutoComplete("searchString", "autocomplete_choices", oDS);
    oAC.queryMatchSubset = true;
    oAC.minQueryLength = 3;
    oAC.maxResultsDisplayed = 25;
    //oAC.typeAhead = true;
    //oAC.queryMatchContains = true;
    oAC.itemSelectEvent.subscribe(function(type, args) {
 		oscarLog(type+" :: "+args);
                oscarLog(args[2]);
                arr = args[2];
                oscarLog('In selection id----'+arr[1]);
                var url = "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?method=createNewRx"; //"prescribe.jsp";
                var ran_number=Math.round(Math.random()*1000000);
                var params = "demographicNo=<%=bean.getDemographicNo()%>&drugId="+arr[1]+"&text="+arr[0]+"&randomId="+ran_number;  //hack to get around ie caching the page
                new Ajax.Updater('rxText',url, {method:'get',parameters:params,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom});
                $('searchString').value = "";
    });
<%--
    /* when this was enabled nothing happened when you selected the event
     oAC.itemSelectEvent = function(acinst,ele,item){
        oscarLog(acinst+" :: "+ele+" -- "+item);
        return this;
    }
        */
    /*
     oAC.applyLocalFilter = true; // pass results thru filter
    oAC.filterResults = function(sQuery, oFullResponse,oParsedResponse, oCallback) {
        oscarLog("inhere"+sQuery);
        var matches = [], matchee;
        for(var i=0; i<oParsedResponse.results.length; i++) {
            if(oParsedResponse.results[i].someValue > 0) {
                matches[matches.length] =oParsedResponse.results[i]
            }
        }
        //oParsedResponse.results = matches;
        return oParsedResponse;
    }
*/
--%>

    return {
        oDS: oDS,
        oAC: oAC
    };
}();

function setSearchedDrug(drugId,name){

    var url = "prescribe.jsp";
    var ran_number=Math.round(Math.random()*1000000);
    var params = "demographicNo=<%=bean.getDemographicNo()%>&id="+drugId+"&text="+name+"&rand="+ran_number;  //hack to get around ie caching the page
    new Ajax.Updater('rxText',url, {method:'get',parameters:params,asynchronous:true,evalScripts:true,insertion: Insertion.Bottom});

    $('searchString').value = "";
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

    //represcribe a drug
    function represcribe(element){
        var drugId=element.id;
        var data="drugId="+drugId;
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/rePrescribe2.do?method=represcribe2";
        new Ajax.Updater('rxText',url, {method:'get',parameters:data,asynchronous:false,evalScripts:true,insertion: Insertion.Bottom});
        
    }

    //not used
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
                var params = "demographicNo=<%=bean.getDemographicNo()%>&id="+drugId+"&text="+drugName+"&randomId="+ran_number;  //hack to get around ie caching the page
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


//Called onclick for prescribe button. serilaizes the form and passes to WriteScruipt/updateAllDrugs Action
    function updateAllDrugs(){

        var data=Form.serialize($('drugForm'))
        var url= "<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?method=updateAllDrugs";
        new Ajax.Request(url,
        {method: 'post',postBody:data,
            onSuccess:function(transport){
                oscarLog("successfully sent data "+url);
                popForm();
            }});

      //pass a list of randomIds to the server
      //server has a action to find all rx relates to each randomId and update one by one
      //only call server action class once.

        return false;
    }


$("searchString").focus();
//load();
</script>


</body>
</html:html>








