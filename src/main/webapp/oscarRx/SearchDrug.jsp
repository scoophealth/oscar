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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo" %>
<%@ page import="oscar.oscarRx.data.*,oscar.oscarProvider.data.ProviderMyOscarIdData,oscar.oscarDemographic.data.DemographicData,oscar.OscarProperties,oscar.log.*"%>
<%@ page import="org.oscarehr.common.model.*" %>
<%@page import="java.util.Enumeration"%>
<%@page import="org.oscarehr.common.model.ProviderPreference"%>
<%@page import="org.oscarehr.web.admin.ProviderPreferencesUIBean"%>


<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_rx" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
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
	oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");

	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	RxPharmacyData pharmacyData = new RxPharmacyData();
	List<PharmacyInfo>pharmacyList = pharmacyData.getPharmacyFromDemographic(Integer.toString(bean.getDemographicNo()));
	String prefPharmacy = "";
	PharmacyInfo pharmacy = null;
	if (pharmacyList != null && !pharmacyList.isEmpty()) {
	    prefPharmacy = pharmacyList.get(0).getName();
	    pharmacy = pharmacyList.get(0);
	}

	String drugref_route = OscarProperties.getInstance().getProperty("drugref_route");
	if (drugref_route == null) drugref_route = "";
	String[] d_route = ("Oral," + drugref_route).split(",");

	String annotation_display = org.oscarehr.casemgmt.model.CaseManagementNoteLink.DISP_PRESCRIP;
	
	//This checks if the provider has the ExternalPresriber feature enabled, if so then a link appear for the provider to access the ExternalPrescriber
	ProviderPreference providerPreference=ProviderPreferencesUIBean.getProviderPreference(loggedInInfo.getLoggedInProviderNo());
	
	boolean eRxEnabled= false;
	String eRx_SSO_URL = null;
	String eRxUsername = null;
	String eRxPassword = null;
	String eRxFacility = null;
	String eRxTrainingMode="0"; //not in training mode
	
	if(providerPreference!=null){
		eRxEnabled = providerPreference.isERxEnabled();
	    eRx_SSO_URL = providerPreference.getERx_SSO_URL();
	    eRxUsername = providerPreference.getERxUsername();
	    eRxPassword = providerPreference.getERxPassword();
	    eRxFacility = providerPreference.getERxFacility();
	
	    boolean eRxTrainingModeTemp = providerPreference.isERxTrainingMode();
	    if(eRxTrainingModeTemp) eRxTrainingMode="1";
	}
	
%>
<%@page import="org.oscarehr.casemgmt.service.CaseManagementManager"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.casemgmt.web.PrescriptDrug"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%><html:html locale="true">
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/share/javascript/prototype.js"></script>
<title><bean:message key="SearchDrug.title" /></title>
<link rel="stylesheet" type="text/css" href="styles.css">

<html:base />

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script type="text/javascript" src="<c:out value="${ctx}/phr/phr.js"/>"></script>

<script type="text/javascript">

function popupDrugOfChoice(vheight,vwidth,varpage) { //open a new popup window
    var page = varpage;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=40,screenY=10,top=10,left=60";
    var popup=window.open(varpage, "oscarDoc", windowprops);
    if (popup != null) {
	if (popup.opener == null) {
	    popup.opener = self;
	}
    }
}

function goDOC(){
    if (document.RxSearchDrugForm.searchString.value.length == 0){
	popupDrugOfChoice(720,700,'http://doc.oscartools.org/')
    }else{
	//var docURL = "http://resource.oscarmcmaster.org/oscarResource/DoC/OSCAR_search/OSCAR_search_results?title="+document.RxSearchDrugForm.searchString.value+"&SUBMIT=GO";
	var docURL = "http://doc.oscartools.org/search?SearchableText="+document.RxSearchDrugForm.searchString.value;
	popupDrugOfChoice(720,700,docURL);
    }
}


function goOMD(){
  	var docURL = "../common/OntarioMDRedirect.jsp?keyword=eCPS&params="+document.RxSearchDrugForm.searchString.value;
	popupDrugOfChoice(743,817,docURL);
}


function popupWindow(vheight,vwidth,varpage,varPageName) { //open a new popup window
    var page = varpage;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=40,screenY=10,top=10,left=60";
    var popup=window.open(varpage,varPageName, windowprops);
    if (popup != null) {
	if (popup.opener == null) {
	    popup.opener = self;
	}
    }
}

function customWarning(){
    if (confirm('This feature will allow you to manually enter a drug.'
	+ '\nWarning: Only use this feature if absolutely necessary, as you will lose the following functionality:'
	+ '\n  *  Known Dosage Forms / Routes'
	+ '\n  *  Drug Allergy Information'
	+ '\n  *  Drug-Drug Interaction Information'
	+ '\n  *  Drug Information'
	+ '\n\nAre you sure you wish to use this feature?')==true) {
	window.location.href = 'chooseDrug.do?demographicNo=<%=response.encodeURL(Integer.toString(bean.getDemographicNo()))%>';
    }
}

function showpic(picture){
    if (document.getElementById){ // Netscape 6 and IE 5+
	var targetElement = document.getElementById(picture);
	var bal = document.getElementById("Calcs");

	var offsetTrail = document.getElementById("Calcs");
	var offsetLeft = 0;
	var offsetTop = 0;
	while (offsetTrail) {
	    offsetLeft += offsetTrail.offsetLeft;
	    offsetTop += offsetTrail.offsetTop;
	    offsetTrail = offsetTrail.offsetParent;
	}
	if (navigator.userAgent.indexOf("Mac") != -1 &&
	    typeof document.body.leftMargin != "undefined") {
	    offsetLeft += document.body.leftMargin;
	    offsetTop += document.body.topMargin;
	}
	targetElement.style.left = offsetLeft +bal.offsetWidth;
	targetElement.style.top = offsetTop;
	targetElement.style.visibility = 'visible';
    }
}

function hidepic(picture){
    if (document.getElementById){ // Netscape 6 and IE 5+
	var targetElement = document.getElementById(picture);
	targetElement.style.visibility = 'hidden';
    }
}

function isEmpty(){
    if (document.RxSearchDrugForm.searchString.value.length == 0){

	document.RxSearchDrugForm.searchString.focus();
	return false;
    }
    return true;
}

function buildRoute() {

        pickRoute = "";
<oscar:oscarPropertiesCheck property="drugref_route_search" value="on">
<%for (int i = 0; i < d_route.length; i++)
					{%>
	if (document.forms[2].route<%=i%>.checked) pickRoute += " "+document.forms[2].route<%=i%>.value;
<%}%>
	document.forms[2].searchRoute.value = pickRoute;
</oscar:oscarPropertiesCheck>
}

function processData() {

    if (isEmpty())
        buildRoute();
    else
        return false;

    return true;
}

//make sure form is in viewport
function load() {
    window.scrollTo(0,0);
}


</script>
</head>

<%
	boolean showall = false;

		if (request.getParameter("show") != null) if (request.getParameter("show").equals("all")) showall = true;
%>

<bean:define id="patient" type="oscar.oscarRx.data.RxPatientData.Patient" name="Patient" />

<body topmargin="0" leftmargin="0" vlink="#0000FF" onload="load()">
<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1" height="100%">
	<%@ include file="TopLinks.jsp"%><!-- Row One included here-->
	<tr>
		<td width="10%" height="100%" valign="top"><%@ include file="SideLinksEditFavorites.jsp"%></td><!-- <td></td>Side Bar File --->
		<td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%" valign="top"><!--Column Two Row Two-->
		<table cellpadding="0" cellspacing="2" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%">
			<tr>
				<td width="0%" valign="top">
				<div class="DivCCBreadCrumbs"><b><bean:message key="SearchDrug.title" /></b></div>
				</td>
			</tr>
			<!----Start new rows here-->
			<tr>
				<td>
					<div class="DivContentTitle"><bean:message key="SearchDrug.title" /></div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="DivContentSectionHead"><bean:message key="SearchDrug.section1Title" /></div>
				</td>
			</tr>
			<tr>
				<td>
				<table>
					<tr>
						<td><b><bean:message key="SearchDrug.nameText" /></b> <jsp:getProperty name="patient" property="firstName" /> <jsp:getProperty name="patient" property="surname" /></td>
						<td></td>
						<td><b><bean:message key="SearchDrug.ageText" /></b> <jsp:getProperty name="patient" property="age" /></td>
						<td></td>
						<td><b><bean:message key="SearchDrug.PreferedPharmacy"/> :</b> <a href="javascript: function myFunction() {return false; }" onClick="showpic('Layer1');" id="Calcs"><%=prefPharmacy%></a></td>
					</tr>
						<indivo:indivoRegistered demographic="<%=String.valueOf(bean.getDemographicNo())%>" provider="<%=bean.getProviderNo()%>">
							<tr>
								<td colspan="3">
									<a href="javascript: phrActionPopup('../oscarRx/SendToPhr.do?demoId=<%=Integer.toString(bean.getDemographicNo())%>', 'sendRxToPhr');">Send To PHR</a>
								</td>
							</tr>
						</indivo:indivoRegistered>
					
				</table>
				</td>
			</tr>

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
						oscar.oscarRx.data.RxPrescriptionData.Prescription[] prescribedDrugs;
							prescribedDrugs = patient.getPrescribedDrugScripts(); //this function only returns drugs which have an entry in prescription and drugs table
							String script_no = "";

							for (int i = 0; i < prescribedDrugs.length; i++)
							{
								oscar.oscarRx.data.RxPrescriptionData.Prescription drug = prescribedDrugs[i];
								if (drug.getScript_no() != null && script_no.equals(drug.getScript_no()))
								{
					%>
										<br>
										<div style="float: left; width: 24%; padding-left: 40px;">&nbsp;</div>
										<a style="float: left;" href="javascript:reprint('<%=drug.getScript_no()%>')"><%=drug.getRxDisplay()%></a>
									<%
 										}
 												else
 												{
 									%>
										<%=i > 0?"<br style='clear:both;'><br style='clear:both;'>":""%><div style="float: left; width: 12%; padding-left: 20px;"><%=drug.getRxDate()%></div>
										<div style="float: left; width: 12%; padding-left: 20px;"><%=drug.getNumPrints()%>&nbsp;Prints</div>
										<a style="float: left;" href="javascript:reprint('<%=drug.getScript_no()%>')"><%=drug.getRxDisplay()%></a>
									<%
 										}
 												script_no = drug.getScript_no() == null?"":drug.getScript_no();
 											}
 									%>
					</div>
				</td>
			</tr>
			<tr>
				<td>
				<table>
					<tr>
						<td width="100%">
						<div class="Step1Text" style="width: 100%">
						<table width="100%" cellpadding="3">
							<tr>
								<th align="left"><b>Rx Date</b></th>
								<th align="left"><b><bean:message key="SearchDrug.msgPrescription"/></b></th>
								<th align="center" width="100px"><b><bean:message key="SearchDrug.msgReprescribe"/></b></th>
								<th align="center" width="100px"><b><bean:message key="SearchDrug.msgDelete"/></b></th>
								<th align="center" width="20px">&nbsp;</th>
								<%
									boolean integratorEnabled=loggedInInfo.getCurrentFacility().isIntegratorEnabled();

									if (integratorEnabled)
									{
										%>
											<td align="center"><bean:message key="SearchDrug.msgLocationPrescribed"/></td>
										<%
									}
								%>
							</tr>

							<%
								CaseManagementManager caseManagementManager=(CaseManagementManager)SpringUtils.getBean("caseManagementManager");
								List<Drug> prescriptDrugs=caseManagementManager.getPrescriptions(loggedInInfo, patient.getDemographicNo(), showall);

								long now = System.currentTimeMillis();
								long month = 1000L * 60L * 60L * 24L * 30L;

								for (Drug prescriptDrug : prescriptDrugs)
								{
									String styleColor = "";

									if (request.getParameter("status") != null)
									{ //TODO: Redo this in a better way
										String stat = request.getParameter("status");
										if (stat != null && stat.equals("active") && prescriptDrug.isExpired())
										{
											continue;
										}
										else if (stat != null && stat.equals("inactive") && !prescriptDrug.isExpired())
										{
											continue;
										}
									}

									if (!prescriptDrug.isExpired() && prescriptDrug.isArchived())
									{
										styleColor = "style=\"color:red;text-decoration: line-through;\"";
									}
									else if (!prescriptDrug.isExpired() && (prescriptDrug.getEndDate().getTime() - now <= month))
									{
										styleColor = "style=\"color:orange;font-weight:bold;\"";
									}
									else if (!prescriptDrug.isExpired() && !prescriptDrug.isArchived())
									{
										styleColor = "style=\"color:red;\"";
									}
									else if (prescriptDrug.isExpired() && prescriptDrug.isArchived())
									{
										styleColor = "style=\"text-decoration: line-through;\"";
									}
							%>
							<tr>
								<td valign="top"><a <%=styleColor%> href="StaticScript.jsp?regionalIdentifier=<%=prescriptDrug.getRegionalIdentifier()%>&cn=<%=response.encodeURL(prescriptDrug.getCustomName())%>&bn=<%=response.encodeURL(prescriptDrug.getBrandName())%>"> <%=prescriptDrug.getRxDate()%> </a></td>
								<td><a <%=styleColor%> href="StaticScript.jsp?regionalIdentifier=<%=prescriptDrug.getRegionalIdentifier()%>&cn=<%=response.encodeURL(prescriptDrug.getCustomName())%>&bn=<%=response.encodeURL(prescriptDrug.getBrandName())%>"> <%=RxPrescriptionData.getFullOutLine(prescriptDrug.getSpecial()).replaceAll(";", " ")%>
								</a></td>
								<td width="100px" align="center">
								<%
									if (prescriptDrug.getRemoteFacilityName()==null)
									{
										%>
											<input type="checkbox" name="chkRePrescribe" align="center" drugId="<%=prescriptDrug.getId()%>" />
										<%
									}
									else
									{
										%>
											<form action="<%=request.getContextPath()%>/oscarRx/searchDrug.do" method="post">
												<input type="hidden" name="demographicNo" value="<%=patient.getDemographicNo()%>" />
												<%
													String searchString=prescriptDrug.getBrandName();
													if (searchString==null) searchString=prescriptDrug.getCustomName();
													if (searchString==null) searchString=prescriptDrug.getRegionalIdentifier();
													if (searchString==null) searchString=prescriptDrug.getSpecial();
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
									if (prescriptDrug.getRemoteFacilityName()==null)
									{
										%>
											<input type="checkbox" name="chkDelete" align="center" drugId="<%=prescriptDrug.getId()%>" />
										<%
									}
								%>
								</td>
								<td width="20px" align="center"><a href="#" title="Annotation"
									onclick="window.open('../annotation/annotation.jsp?display=<%=annotation_display%>&table_id=<%=prescriptDrug.getId()%>&demo=<%=bean.getDemographicNo()%>','anwin','width=400,height=500');"> <img src="../images/notes.gif" border="0"></a>
									</td>
								<%
									if (integratorEnabled)
									{
										%>
											<td align="center"><%=prescriptDrug.getRemoteFacilityName()==null?"local":prescriptDrug.getRemoteFacilityName()%></td>
										<%
									}
								%>
							</tr>
							<%
								}
							%>
						</table>

						</div>
						<div style="margin-top: 10px; margin-left: 20px; width: 100%">
						<table width="100%" cellspacing="0" cellpadding="0">
							<tr>
								<td align="left">
								<%
									String show = "&show=all";
										if (showall)
										{
								%> <a href="SearchDrug.jsp"><bean:message key="SearchDrug.msgShowCurrent"/></a> <%
 	}
 		else
 		{
 			show = "";
 %> <a href="SearchDrug.jsp?show=all"><bean:message key="SearchDrug.msgShowAll"/></a> <%
 	}
 %> &nbsp;&nbsp;&nbsp; <a href="SearchDrug.jsp?status=active<%=show%>"><bean:message key="SearchDrug.msgActive"/></a> - <a
									href="SearchDrug.jsp?status=inactive<%=show%>"><bean:message key="SearchDrug.msgInactive"/></a> - <a href="SearchDrug.jsp?status=all<%=show%>"><bean:message key="SearchDrug.msgAll"/></a></td>
								<td align="right">
                                                                    <span style="width: 350px; align: right">
                                                                       <input type="button" name="cmdAllergies" value="<bean:message key="SearchDrug.msgViewEditAllergies"/>" class="ControlPushButton" onclick="javascript:window.location.href='ShowAllergies.jsp?demographicNo=<%=request.getParameter("demographicNo")%>';" style="width: 100px" />
								       <input type="button" name="cmdRePrescribe" value="<bean:message key="SearchDrug.msgReprescribe"/>" class="ControlPushButton" onclick="javascript:RePrescribe();" style="width: 100px" />
                                                                       <input type="button" name="cmdDelete" value="<bean:message key="SearchDrug.msgDelete"/>" class="ControlPushButton" onclick="javascript:Delete();" style="width: 100px" />
                                                                    </span>
                                                                </td>
							</tr>
						</table>
						</div>

						<script language="javascript">
                                function reprint(drug) {
                                    document.forms[0].drugList.value = drug;
                                    document.forms[0].method.value = "reprint";
                                    document.forms[0].submit();

                                }

                                function RePrescribe(){

                                    if(document.getElementsByName('chkRePrescribe')!=null){
                                        var checks = document.getElementsByName('chkRePrescribe');
                                        var s='';
                                        var i;

                                        for(i=0; i<checks.length; i++){
                                            if(checks[i].checked==true){
                                                s += checks[i].getAttribute("drugId") + ',';

                                            }
                                        }

                                        if(s.length>1){
                                            s = s.substring(0, s.length - 1);

                                            document.forms[0].drugList.value = s;
                                            document.forms[0].method.value = "represcribe";
                                            document.forms[0].submit();
                                        }
                                    }
                                }

                                function Delete(){

                                     if(document.getElementsByName('chkDelete')!=null){
                                        var checks = document.getElementsByName('chkDelete');
                                        var s='';
                                        var i;

                                        for(i=0; i<checks.length; i++){
                                            if(checks[i].checked==true){
                                                s += checks[i].getAttribute("drugId") + ',';
                                            }
                                        }

                                        if(s.length>1){
                                            if(confirm('Are you sure you wish to delete the selected prescriptions?')==true){
                                                s = s.substring(0, s.length - 1);
                                                document.forms[1].drugList.value = s;
                                                document.forms[1].submit();
                                            }
                                        }
                                    }
                                }
                            </script> <html:form action="/oscarRx/rePrescribe">
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
			<tr>
				<td>
				<div class="DivContentSectionHead"><bean:message key="SearchDrug.section3Title" /></div>
				</td>
			</tr>

			<tr>
				<td><html:form action="/oscarRx/searchDrug" focus="searchString" onsubmit="return processData();">
					<html:hidden property="demographicNo" value="<%=new Integer(patient.getDemographicNo()).toString()%>" />
					<table>
						<tr valign="center">
							<td><bean:message key="SearchDrug.drugSearchTextBox" /><br>
							<html:text styleId="searchString" property="searchString" size="16" maxlength="16" /></td>
							<td width="100"><a href="javascript:goDOC();"><bean:message key="SearchDrug.msgDrugOfChoice" /></a> <%
 	if (OscarProperties.getInstance().hasProperty("ONTARIO_MD_INCOMINGREQUESTOR"))
 			{
 %> <a href="javascript:goOMD();"><bean:message key="SearchDrug.msgOMDLookup"/></a> <%
 	}
 %>
 <%if (eRxEnabled) {%>
	<a href="<%=eRx_SSO_URL%>User=<%=eRxUsername%>&Password=<%=eRxPassword%>&Clinic=<%=eRxFacility%>&PatientIdPMIS=<%=patient.getDemographicNo()%>&IsTraining=<%=eRxTrainingMode%>"><bean:message key="SearchDrug.eRx.msgExternalPrescriber"/></a>
 <%}%>
 
							</td>
							<td><oscar:oscarPropertiesCheck property="drugref_route_search" value="on">
								<bean:message key="SearchDrug.drugSearchRouteLabel" />
								<br>
								<%
									for (int i = 0; i < d_route.length; i++)
												{
								%>
								<input type="checkbox" name="route" <%=i%> value="<%=d_route[i].trim()%>"><%=d_route[i].trim()%> &nbsp;</input>
								<%
									}
								%>
								<html:hidden property="searchRoute" />
							</oscar:oscarPropertiesCheck></td>
						</tr>
						<tr>
							<td colspan="3">
                                                            <html:submit property="submit" styleClass="ControlPushButton"><bean:message key="SearchDrug.msgSearch"/></html:submit> &nbsp;&nbsp;&nbsp;
                                                            <input type="button" class="ControlPushButton" onclick="searchString.value='';searchRoute.value='';searchString.focus();" value="<bean:message key="SearchDrug.msgReset"/>" />
                                                            <input type="button" class="ControlPushButton" onclick="customWarning();" value="<bean:message key="SearchDrug.msgCustomDrug"/>" /></td>
						</tr>
					</table>
				</html:form></td>
			</tr>

			<logic:notEqual name="bean" property="stashSize" value="0">
				<tr>
					<td><script language="javascript">
                            function submitPending(stashId, action){
                                var frm = document.forms.RxStashForm;
                                frm.stashId.value = stashId;
                                frm.action.value = action;
                                frm.submit();
                            }

                        </script> <html:form action="/oscarRx/stash">
						<html:hidden property="action" />
						<html:hidden property="stashId" />
					</html:form>

					<div class="DivContentSectionHead"><bean:message key="WriteScript.section5Title" /></div>
					</td>
				</tr>
				<tr>
					<td><script language="javascript">
                            function ShowDrugInfo(GN){
                                window.open("drugInfo.do?GN=" + escape(GN), "_blank",
                                    "location=no, menubar=no, toolbar=no, scrollbars=yes, status=yes, resizable=yes");
                            }
                        </script>
						<element>
						<table cellspacing="0" cellpadding="5">
						<%
							int i = 0;
						%>
						<logic:iterate id="rx" name="bean" property="stash" length="stashSize">
							<tr>
								<td><a href="javascript:submitPending(<%=i%>, 'edit');"><bean:message key="SearchDrug.msgEdit"/></a></td>
								<td><a href="javascript:submitPending(<%=i%>, 'delete');"><bean:message key="SearchDrug.msgDelete"/></a></td>
								<td><a href="javascript:submitPending(<%=i%>, 'edit');"> <bean:write name="rx" property="rxDisplay" /> </a></td>
								<td><a href="javascript:ShowDrugInfo('<%=((oscar.oscarRx.data.RxPrescriptionData.Prescription)rx).getGenericName()%>');"><bean:message key="SearchDrug.msgInfo"/></a></td>
							</tr>
							<%
								i++;
							%>
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
	if (pharmacy != null)
		{
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
		<td><%=pharmacy.getName()%></td>
	</tr>

	<tr class="LightBG">
		<td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgAddress"/></td>
		<td class="wcblayerItem">&nbsp;</td>
		<td><%=pharmacy.getAddress()%></td>
	</tr>
	<tr class="LightBG">
		<td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgCity"/></td>
		<td class="wcblayerItem">&nbsp;</td>
		<td><%=pharmacy.getCity()%></td>
	</tr>

	<tr class="LightBG">
		<td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgProvince"/></td>
		<td class="wcblayerItem">&nbsp;</td>
		<td><%=pharmacy.getProvince()%></td>
	</tr>
	<tr class="LightBG">
		<td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgPostalCode"/> :</td>
		<td class="wcblayerItem">&nbsp;</td>
		<td><%=pharmacy.getPostalCode()%></td>
	</tr>
	<tr class="LightBG">
		<td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgPhone1"/> :</td>
		<td class="wcblayerItem">&nbsp;</td>
		<td><%=pharmacy.getPhone1()%></td>
	</tr>
	<tr class="LightBG">
		<td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgPhone2"/> :</td>
		<td class="wcblayerItem">&nbsp;</td>
		<td><%=pharmacy.getPhone2()%></td>
	</tr>
	<tr class="LightBG">
		<td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgFax"/> :</td>
		<td class="wcblayerItem">&nbsp;</td>
		<td><%=pharmacy.getFax()%></td>
	</tr>
	<tr class="LightBG">
		<td class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgEmail"/> :</td>
		<td class="wcblayerItem">&nbsp;</td>
		<td><%=pharmacy.getEmail()%></td>
	</tr>
	<tr class="LightBG">
		<td colspan="3" class="wcblayerTitle"><bean:message key="SearchDrug.pharmacy.msgNotes"/> :</td>
	</tr>
	<tr class="LightBG">
		<td colspan="3"><%=pharmacy.getNotes()%></td>
	</tr>

</table>

</div>
<%
	}
%>
</body>
</html:html>