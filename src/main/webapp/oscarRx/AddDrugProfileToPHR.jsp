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
<%@page import="oscar.oscarRx.data.RxPatientData"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp"%>
<%@ page
	import="java.util.*,oscar.ping.xml.*,oscar.ping.xml.impl.*,javax.xml.bind.*, oscar.oscarEncounter.data.EctProviderData"%>
<%@ page import="org.chip.ping.client.*"%>
<%@ page import="org.chip.ping.xml.*"%>
<%@ page import="org.chip.ping.xml.talk.*"%>
<%@ page import="org.chip.ping.xml.cddm.*"%>
<%@ page import="org.chip.ping.xml.record.*"%>
<%@ page import="org.chip.ping.xml.record.impl.*"%>
<%@ page
	import="org.chip.ping.xml.cddm.impl.*,org.w3c.dom.*,javax.xml.parsers.*"%>
<%@ page import="oscar.util.Send2Indivo"%>
<%@ page import="oscar.oscarDemographic.data.*"%>
<%@ page import="oscar.oscarProvider.data.*, oscar.OscarProperties"%>

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


<%

oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");
EctProviderData.Provider prov = new EctProviderData().getProvider(bean.getProviderNo());
String actorTicket = "";
String actor = prov.getIndivoId();
String actorPassword = prov.getIndivoPasswd();
String fullName = prov.getFirstName() + " " + prov.getSurname();
String role = "provider";

DemographicData demoData = new DemographicData();
String patientPingId = demoData.getDemographic(""+bean.getDemographicNo()).getMyOscarUserName();

Send2Indivo indivoServer = new Send2Indivo(actor, actorPassword, fullName, role);
String server = OscarProperties.getInstance().getProperty("INDIVO_SERVER");
indivoServer.setServer(server);

boolean connected = true;
String connectErrorMsg = "";
if( !indivoServer.authenticate() ) {
    connectErrorMsg = indivoServer.getErrorMsg();    
    connected = false;
}
else
    actorTicket = indivoServer.getSessionId();
        
/*
try{   
actorTicket = ping.connect(actor,actorPassword);
}catch(Exception eCon){
    connectErrorMsg = eCon.getMessage();
    connected = false;
}


oscar.oscarRx.data.RxProviderData.Provider provider = new oscar.oscarRx.data.RxProviderData().getProvider(bean.getProviderNo());
ProSignatureData sig = new ProSignatureData();
boolean hasSig = sig.hasSignature(bean.getProviderNo());
String doctorName = "";
if (hasSig){
   doctorName = sig.getSignature(bean.getProviderNo());
}else{
   doctorName = (provider.getFirstName() + ' ' + provider.getSurname());
}


String owner = patientPingId;      
String originAgent = actor;
String author = actor;
String level1 = CddmLevels.CUMULATIVE;
String level2 = CddmLevels.MEDICATIONS;
 */
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Send Prescription to PHR</title>
<html:base />



<link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body topmargin="0" leftmargin="0" vlink="#0000FF">

<table border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse" bordercolor="#111111" width="100%"
	id="AutoNumber1" height="100%">
	<%@ include file="TopLinks.jsp"%><!-- Row One included here-->
	<tr>
		<%@ include file="SideLinksNoEditFavorites.jsp"%><!-- <td></td>Side Bar File --->
		<td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%"
			valign="top">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">
			<tr>
				<td width="0%" valign="top">
				<div class="DivCCBreadCrumbs">
				<% if (connected){%> &nbsp;&nbsp;Session Id: <%=actorTicket%><br />
				<%}else{%> <%=connectErrorMsg%> <%}%>
				</div>
				</td>
			</tr>
			<!----Start new rows here-->
			<tr>
				<td>
				<div class="DivContentTitle">Send Prescriptions to <%=patientPingId%>
				Personal Health Record</div>
				</td>
			</tr>
			<tr>
				<td>
				<div class="DivContentSectionHead">Adding</div>
				</td>
			</tr>
			<tr>
				<td>
				<%if(connected){
                            oscar.oscarRx.data.RxPatientData.Patient patient ;

                            String demoNo = request.getParameter("demoId");            
                            patient = RxPatientData.getPatient(Integer.parseInt(demoNo));

                            oscar.oscarRx.data.RxPrescriptionData.Prescription[] prescribedDrugs;                                                               
                            prescribedDrugs = patient.getPrescribedDrugsUnique();
                            
                            String authorFname;
                            String authorLname;
                            boolean indivoUpdated;
                            int sentcount = 0;
                            for( int idx = 0; idx < prescribedDrugs.length; ++idx ) {
                                oscar.oscarRx.data.RxPrescriptionData.Prescription drug = prescribedDrugs[idx];
                                if(drug.isCurrent() == true && !drug.isArchived() ){
                                    sentcount++;
                                   indivoUpdated = false;
                                   prov = new EctProviderData().getProvider(drug.getProviderNo());
                                    
                                    if( drug.isRegisteredIndivo() ) {
                                        if( indivoServer.updateMedication(drug, drug.getIndivoIdx(), prov.getFirstName(), prov.getSurname(), patientPingId) ) {
                                                indivoUpdated = true;                                                
                                        }
                                        else {
                                        %> An Error Occurred While
				Updating Medication <%=indivoServer.getErrorMsg()%> <%
                                                break;
                                        }
                                    }
                                    else {
                                        if( indivoServer.sendMedication(drug, prov.getFirstName(), prov.getSurname(), patientPingId) ) {
                                            drug.setIndivoIdx(indivoServer.getIndivoDocIdx());
                                            indivoUpdated = true;                                                                                   
                                        }
                                        else {
                                        %> An Error Occurred While
				Adding Medication <%=indivoServer.getErrorMsg()%> <%
                                                break;
                                        }
                                    }  
                                    
                                    if( indivoUpdated ) {
                                        if( drug.registerIndivo() ) {
                                     %> <%= drug.getRxDisplay() %><br>
				<%    
                                        }
                                        else {
                                     %> Error updating local database,
				please contact system administrator <%
                                        }
                                    }
                                  
                                }
                            } //endfor

                      
                       %> <!--<textarea cols="100" rows="40">
                                
                                </textarea>--> <%
                                
                        }else{
                            out.write("none"); 
                        }        %> <%=connectErrorMsg%></td>
			</tr>

			<tr>
				<td><input type=button class="ControlPushButton"
					onclick="javascript:window.close();" value="Close Window" /></td>
			</tr>
			<!----End new rows here-->
			<tr height="100%">
				<td></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
	</tr>
	<tr>
		<td width="100%" height="0%" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC"
			colspan="2"></td>
	</tr>
</table>

</body>

</html:html>
