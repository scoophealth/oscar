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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_con" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_con");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%! boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable(); %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp" %>
<!-- add for special encounter -->
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/special_tag.tld" prefix="special" %>
<!-- end -->
<%@ page import="oscar.OscarProperties, oscar.oscarClinic.ClinicData, java.util.*, oscar.util.StringUtils" %>


<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.common.model.Site"%><html:html locale="true">

<%
	oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil reqFrm;
	reqFrm = new oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil ();
	reqFrm.estRequestFromId(LoggedInInfo.getLoggedInInfoFromSession(request), (String)request.getAttribute("reqId"));

	reqFrm.specPhone = request.getParameter("phone");

	if (reqFrm.specPhone == null || reqFrm.specPhone.equals("null")){
		reqFrm.specPhone = new String();
	}

	reqFrm.specFax = request.getParameter("fax");
	if (reqFrm.specFax == null || reqFrm.specFax.equals("null")){
		reqFrm.specFax = new String();
	}

	reqFrm.specAddr = request.getParameter("address");
	if (reqFrm.specAddr == null || reqFrm.specAddr.equals("null")){
		reqFrm.specAddr = new String();
	}

	OscarProperties props = OscarProperties.getInstance();
	ClinicData clinic = new ClinicData();
	String strPhones = clinic.getClinicDelimPhone();

	if (strPhones == null) { strPhones = ""; }
	String strFaxes  = clinic.getClinicDelimFax();
	if (strFaxes == null) { strFaxes = ""; }
	Vector vecPhones = new Vector();
	Vector vecFaxes  = new Vector();
	StringTokenizer st = new StringTokenizer(strPhones,"|");

	while (st.hasMoreTokens()) {
		 vecPhones.add(st.nextToken());
	}

	st = new StringTokenizer(strFaxes,"|");
	while (st.hasMoreTokens()) {
		 vecFaxes.add(st.nextToken());
	}

	// for satellite clinics
	Vector vecAddressName = null;
	Vector vecAddress = null;
	Vector vecAddressPhone = null;
	Vector vecAddressFax = null;
	Vector vecAddressBillingNo = null;
    String defaultAddrName = null;
	if (bMultisites) {
    	vecAddressName = new Vector();
        vecAddress = new Vector();
        vecAddressPhone = new Vector();
        vecAddressFax = new Vector();
        vecAddressBillingNo = new Vector();
        
    		SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
      		List<Site> sites = siteDao.getActiveSitesByProviderNo((String) session.getAttribute("user"));
      		Site defaultSite = sites.get(0);
      		for (Site s:sites) {
                vecAddressName.add(s.getName());
                vecAddress.add(s.getAddress() + ", " + s.getCity() + ", " + s.getProvince() + "  " + s.getPostal());
                vecAddressPhone.add(s.getPhone());
                vecAddressFax.add(s.getFax());      			
     		}
            // default address
            if (defaultSite!=null) {
	            clinic.setClinic_address(defaultSite.getAddress());
	            clinic.setClinic_city(defaultSite.getCity());
	            clinic.setClinic_province(defaultSite.getProvince());
	            clinic.setClinic_postal(defaultSite.getPostal());
	            clinic.setClinic_phone(defaultSite.getPhone());
	            clinic.setClinic_fax(defaultSite.getFax());
	            clinic.setClinic_name(defaultSite.getName());
	            defaultAddrName=defaultSite.getName();
            }
    } else
    if(props.getProperty("clinicSatelliteName") != null) {
		vecAddressName = new Vector();
		vecAddress = new Vector();
		vecAddressPhone = new Vector();
		vecAddressFax = new Vector();
		vecAddressBillingNo = new Vector();
		String[] temp0 = props.getProperty("clinicSatelliteName", "").split("\\|");
		String[] temp1 = props.getProperty("clinicSatelliteAddress", "").split("\\|");
		String[] temp2 = props.getProperty("clinicSatelliteCity", "").split("\\|");
		String[] temp3 = props.getProperty("clinicSatelliteProvince", "").split("\\|");
		String[] temp4 = props.getProperty("clinicSatellitePostal", "").split("\\|");
		String[] temp5 = props.getProperty("clinicSatellitePhone", "").split("\\|");
		String[] temp6 = props.getProperty("clinicSatelliteFax", "").split("\\|");
		String[] temp7 = props.getProperty("clinicDocBillingNoList", "").split("\\|");
		for(int i=0; i<temp0.length; i++) {
			vecAddressName.add(temp0[i]);
			vecAddress.add(temp1[i] + ", " + temp2[i] + ", " + temp3[i] + "  " + temp4[i]);
			vecAddressPhone.add(temp5[i]);
			vecAddressFax.add(temp6[i]);
		}
		for(int i=0; i<temp7.length; i++) {
			vecAddressBillingNo.add(temp7[i]);
		}
		// default address
		//clinic.setClinic_name();
		clinic.setClinic_address(temp1[0]);
		clinic.setClinic_city(temp2[0]);
		clinic.setClinic_province(temp3[0]);
		clinic.setClinic_postal(temp4[0]);
		clinic.setClinic_phone(temp5[0]);
		clinic.setClinic_fax(temp6[0]);
	}
%>
<html:base/>

<HEAD>
	<style type="text/css" media="print">
		.header {
		display:none;
		}

		.header INPUT {
		display:none;
		}

		.header A {
		display:none;
		}
	</style>

	<STYLE type="text/css">

		table.printTable{
			width: 480pt;
			font-size: 10pt;
			font-family: arial, verdana, tahoma, helvetica, sans serif;
		}

		table.doctorInfo{
			border-bottom: 2pt solid #444444;
		}

		table.patientLeft{
			border-left: 1pt solid #AAAAAA;
		}

		table.consultDetail{
			font-size: 10pt;
			font-family: arial, verdana, tahoma, helvetica, sans serif;
		}


		td.doctorName{
			font-size:12pt;
			font-weight:900;
			font-family: arial, verdana, tahoma, helvetica, sans serif;
		}

		td.doctorAddress{
			font-size:10pt;
			font-weight:400;
			font-family: arial, verdana, tahoma, helvetica, sans serif;
		}


		td.patientBottom{
			border-bottom: 2pt solid #444444;
			font-size:10pt;
			font-family: arial, verdana, tahoma, helvetica, sans serif;
		}

		td.subTitles{
			font-size:10pt;
			font-weight:bold;
			vertical-align: top;
			font-family: arial, verdana, tahoma, helvetica, sans serif;
		}

		td.fillLine{
			font-size:10pt;
			font-weight:400;
			vertical-align: top;
			font-family: arial, verdana, tahoma, helvetica, sans serif;
		}

		td.consult{
			font-size:10pt;
			font-family: arial, verdana, tahoma, helvetica, sans serif;
		}

    </STYLE>

	<script type="text/javascript">

		var flag = 1;
		function PrintWindow(){
			window.print();
		}

		function CloseWindow(){
			window.close();
		}

		function flipFaxFooter(){
			if (flag == 1 ){
				document.getElementById("faxFooter").innerHTML="<hr><bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgFaxFooterMessage"/>";
				flag = 0;
			}else{
				document.getElementById("faxFooter").innerHTML="";
				flag = 1;
			}
		}

		function phoneNumSelect() {
			document.getElementById("clinicPhone").innerHTML="Tel: "+document.getElementById("sendersPhone").value;
		}

		function faxNumSelect() {
			document.getElementById("clinicFax").innerHTML="Fax: "+document.getElementById("sendersFax").value;
		}

		function addressSelect() {
			<% if(vecAddressName != null) {
				for(int i=0; i<vecAddressName.size(); i++) {%>
					if(document.getElementById("addressSel").value=="<%=i%>") {
						document.getElementById("clinicName").innerHTML="<%=vecAddressName.get(i)%>";
						document.getElementById("clinicAddress").innerHTML="<%=vecAddress.get(i)%>";
						document.getElementById("clinicPhone").innerHTML="Tel: "+"<%=vecAddressPhone.get(i)%>";
						document.getElementById("clinicFax").innerHTML="Fax: "+"<%=vecAddressFax.get(i)%>";
					}
				<% }
			}%>
		}

	</script>
	<title>
		<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.title"/>
	</title>


</HEAD>


<BODY>


	<form  method="get "action="attachmentReport.jsp">
		<input type="hidden" name="reqId" value="<%=request.getAttribute("reqId")%>"/>
		<input type="hidden" name="demographicNo" value="<%=request.getParameter("demographicNo")%>"/>
		<input type="hidden" name="providerNo" value="<%=reqFrm.providerNo%>"/>
		<table class="header" >
			<tr>
				<td align="center">
					<oscarProp:oscarPropertiesCheck property="EMAIL_REFERRAL" value="yes">
						<a href="mailto:<%=reqFrm.getSpecailistsEmail(reqFrm.specialist)%>?subject=Consultation%20Request&body=<%=formatEmail(reqFrm)%>">mail</a>
					</oscarProp:oscarPropertiesCheck>
					<input type=button value="<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgFaxFooter"/>" onclick="javascript :flipFaxFooter();"/>
				</td>
				<td align="center">
					<input type=button value="<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPrint"/>" onclick="javascript: PrintWindow();"/>
				</td>
				<td align="center">
					<input type="submit" value="<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPrintAttached"/>" />
				</td>
				<td align="center">
					<input type=button value="<bean:message key="global.btnClose"/>" onclick="javascript: CloseWindow();"/>
				</td>
				<% if(vecPhones.size() > 0) { %>
				<td align="center">
				P
				<select name="sendersPhone" id="sendersPhone" onChange="phoneNumSelect()">
			<%  for (int i =0; i < vecPhones.size();i++){
				String te = (String) vecPhones.elementAt(i);
			%>
					   <option value="<%=te%>"><%=te%></option>
			<%	}%>
					</select>
				</td>
		<% } %>
		<% if(vecFaxes.size() > 0) { %>
				<td align="center">
				F
					<select name="sendersFax" id="sendersFax" onChange="faxNumSelect()">
			<%  for (int i =0; i < vecFaxes.size();i++){
				 String te = (String) vecFaxes.elementAt(i);
			%>
						<option value="<%=te%>"><%=te%></option>
			<%  }%>
					</select>
				</td>
		<% } %>
		<% if(vecAddress != null) { %>
				<td align="center">
				Address
					<select name="addressSel" id="addressSel" onChange="addressSelect()">
			<%  for (int i =0; i < vecAddressName.size();i++){
				 String te = (String) vecAddressName.get(i);
			%>
						<option value="<%=i%>" <%= te.equals(defaultAddrName)?"selected":"" %>><%=te%></option>
			<%  }%>
					</select>
				</td>
		<% } %>
			</tr>
		</table>
	</form>

<!--- END INPUT -->


	<TABLE class="printTable" name="headerTable" width=450>
		<TR>
			<TD>
<!-- Doctor's info -->
				<TABLE name="innerTable" class="doctorInfo" border="0" width='100%'>
					<TR>
						<TD class="doctorName" id="clinicName" valign="top">
<!-- Clinic Name -->
							<%=clinic.getClinicName()%>
						</TD>
						<TD>
						</TD>
						<TD class="doctorAddress" align = right valign=top>
							<span id="clinicAddress"><%=clinic.getClinicAddress()%>, <%=clinic.getClinicCity()%>, <%=clinic.getClinicProvince()%>,  <%=clinic.getClinicPostal()%></span><BR>
							Tel: <span id="clinicPhone"><%=vecPhones.size()>=1?vecPhones.elementAt(0):clinic.getClinicPhone()%></span> &nbsp;&nbsp;&nbsp;
							Fax: <span id="clinicFax"><%=vecFaxes.size()>=1?vecFaxes.elementAt(0):clinic.getClinicFax()%></span>

						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD class="patientBottom">
<!-- Patient's details -->
				<TABLE class="patientDetail" width="100%">
					<TR>
						<TD>
					<table border=0 align="center" width="100%" cellspacing="0" class="patientInfo">
						<tr>
							<td valign="top" align="left" width="50%">
								<table border=0 width="100%" >
									<tr>
										<td class="subTitles">
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgDate"/>:
										</td>
										<td class="fillLine">
											<% if(reqFrm.pwb.equals("1")){ 	%>
												<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.pwb"/>
											<%}else{ %>
												<%=reqFrm.referalDate%>
											<%}%>
										</td>
									</tr>
									<tr>
										<td class="subTitles">
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgStatus"/>:
										</td>
										<td class="fillLine">
				<% if (reqFrm.urgency.equals("1")) { %>
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgUrgent"/>
				<%  }else if(reqFrm.urgency.equals("2")){ %>
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgNUrgent"/>
				<%	}else if (reqFrm.urgency.equals("3")){ %>
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgReturn"/>
				<% } %>
										</td>
									</tr>
									<tr>
										<td class="subTitles">
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgService"/>:
										</td>
										<td class="fillLine">
								<%=reqFrm.getServiceName(reqFrm.service) %>
										</td>
									</tr>
									<tr>
										<td class="subTitles">
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgConsultant"/>:
										</td>
										<td class="fillLine">
								<%=reqFrm.getSpecailistsName(reqFrm.specialist) %>
										</td>
									</tr>
									<tr>
										<td class="subTitles">
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPhone"/>:
										</td>
										<td class="fillLine">
								<%=reqFrm.specPhone%>
										</td>
									</tr>
									<tr>
										<td class="subTitles">
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgFax"/>:
										</td>
										<td class="fillLine">
								<%=reqFrm.specFax%>
										</td>
									</tr>
									<tr>
										<td class="subTitles" >
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgAddr"/>:
										</td>
										<td class="fillLine" >
								<%=reqFrm.specAddr%>
										</td>
									</tr>
<plugin:hideWhenCompExists componentName="specialencounterComp" reverse="true">
<special:SpecialEncounterTag moduleName="eyeform">
<% 
String requestId=(String)request.getAttribute("reqId");
String aburl="/EyeForm.do?method=showCC";
if (requestId!=null) aburl+="&requestId="+requestId; %>
<plugin:include componentName="specialencounterComp" absoluteUrl="<%=aburl %>"></plugin:include>
</special:SpecialEncounterTag>
</plugin:hideWhenCompExists>

								</table>
							</td>
							<td valign="top">
								<table border=0 class="leftPatient">
									<tr>
										<td class="subTitles">
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPat"/>:
										</td>
										<td class="fillLine">
							<%=reqFrm.patientName %>
										</td>
									</tr>
									<tr>
										<td class="subTitles">
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgAddr"/>:
										</td>
										<td class="fillLine">
								<%=reqFrm.patientAddress %>
										</td>
									</tr>
									<tr>
										<td class="subTitles">
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgPhone"/>
										</td>
										<td class="fillLine">
								<%=reqFrm.patientPhone %>
										</td>
									</tr>
									<tr>
										<td class="subTitles">
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgBirth"/>:
										</td>
										<td class="fillLine">
								<%=reqFrm.patientDOB %>  (y/m/d)
										</td>
									</tr>
									<tr>
										<td class="subTitles">
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgCard"/>
										</td>
										<td class="fillLine">
											 <%=reqFrm.patientHealthCardType%>&nbsp;<%=reqFrm.patientHealthNum %>&nbsp;<%=reqFrm.patientHealthCardVersionCode%>&nbsp;
										</td>
									</tr>
									<tr>
										<td class="subTitles">
											<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgappDate"/>:
										</td>
 										<td class="fillLine">
											<%if (Integer.parseInt(reqFrm.status) > 2 ){%>
												<%=reqFrm.appointmentHour %>:<%=reqFrm.appointmentMinute %> <%=reqFrm.appointmentPm %>
				 							<%}else{%>
							   					&nbsp;
						   					<%}%>
											<%if (Integer.parseInt(reqFrm.status) > 2 ){%>
												<BR>
												<%=reqFrm.appointmentDate %>  (y/m/d)
							  		  	   <%}%>
										</td>
									</tr>
									<% if(getlen(reqFrm.patientChartNo) > 1) {%>
	                                    <tr>
    	                                    <td class="subTitles">
        	                                    <bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgChart"/>
            	                            </td>
                	                        <td class="fillLine">
			        	                        <%=reqFrm.patientChartNo%>
                        	                </td>
                            	        </tr>
                            	    <%}%>
								</table>
							</td>
						</tr>
					</table>
						</TD>
					</TR>
				</TABLE>

			</TD>
		</TR>


<!-- Consultation details -->


		<TR>
			<TD>
				<TABLE class="consultDetail">
					<tr>
						<td class="consult">
							<B>
								<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgReason"/> : &nbsp;
							</B>
							<%=divyup(reqFrm.reasonForConsultation) %>
						</td>
					</tr>
					<% if(getlen(reqFrm.clinicalInformation) > 1) {%>
						<tr>
							<td class="consult">
								<BR>
								<B>
									<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgClinicalInfom"/> : &nbsp;
								</B>
								<%=divyup(reqFrm.clinicalInformation) %>
							</td>
						</tr>
					<%}%>
					<% if(getlen(reqFrm.concurrentProblems) > 1) {%>
						<tr>
							<td class="consult">
								<BR>
								<B>
									<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgSigProb"/> : &nbsp;
								</B>
								<%=divyup(reqFrm.concurrentProblems) %>
							</td>
						</tr>
					<%}%>
<plugin:hideWhenCompExists componentName="specialencounterComp" reverse="true">
<special:SpecialEncounterTag moduleName="eyeform">
<script type="text/javascript" src="js/prototype.js">
</script>
<%
String requestId=(String)request.getAttribute("reqId");
String aburl="/EyeForm.do?method=showSpecial";
if (requestId!=null) aburl+="&requestId="+requestId; %>
<plugin:include componentName="specialencounterComp" absoluteUrl="<%=aburl %>"></plugin:include>
</special:SpecialEncounterTag>
</plugin:hideWhenCompExists>

					<% if(getlen(reqFrm.currentMedications) > 1) {%>
						<tr>
							<td class="consult">
								<BR>
								<B>
									<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgCurrMed"/> : &nbsp;
								</B>
								<%=divy(reqFrm.currentMedications) %>
							</td>
						</tr>
					<%}%>
					<% if(getlen(reqFrm.allergies) > 1) {%>
						<tr>
							<td class="consult">
								<BR>
								<B>
									<bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgAllergies"/> : &nbsp;
								</B>
								<%=divy(reqFrm.allergies) %>
							</td>
						</tr>
					<%}%>
				</TABLE>
			</TD>
		</TR>

                <% if (props.getProperty("FORMS_PROMOTEXT") != null){%>
                    <tr align="center"><td></br></br><%= props.getProperty("FORMS_PROMOTEXT") %></td></tr>
                <%}%>

	</TABLE>
</BODY>
</html:html>
<%!
public String divy (String str){
	StringBuffer stringBuffer = new StringBuffer();
	stringBuffer.append(str);
	int j =0;
	int i = 0 ;
	while (i < stringBuffer.length() ){
		if (stringBuffer.charAt(i) == '\n'){
			stringBuffer.insert(i,"<BR>");
			i = i + 4;
		}
		i++;
	}
return stringBuffer.toString();
}

public String divyup (String str){
	StringBuffer stringBuffer = new StringBuffer();
	stringBuffer.append(str);
	int i = 2 ;
	int k = 0;
	while ((i < (stringBuffer.length()-2)) && (i > 0)){
		k = 0;
		while ((stringBuffer.charAt(i) == '\n') || (stringBuffer.charAt(i) == '\r')) {
			stringBuffer.deleteCharAt(i);
			k++;
		}
		if (k > 2) {
			stringBuffer.insert(i,"<BR>");
			i = i+3;
		}
		i++;
	}
return stringBuffer.toString();
}


public int getlen (String str){
	StringBuffer stringBuffer = new StringBuffer();
	stringBuffer.append(str);
	int j = stringBuffer.length();
	return j;
}

public String formatEmail(oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil reqFrm){
   String Urgency = "";
   if (reqFrm.urgency.equals("1")) {
	  Urgency = "Urgent";
   }else if(reqFrm.urgency.equals("2")){
	  Urgency = "Non-Urgent";
   }else if (reqFrm.urgency.equals("3")){
	  Urgency = "Return";
   }

   String s = "";
   s +="<b>Date:</b>"+reqFrm.referalDate+"<br/>";
   s +="<b>Status:</b>"+Urgency+"<br/>";
   s +="<b>Service:</b>"+reqFrm.getServiceName(reqFrm.service)+"<br/>";

   s +="<b>Specailist:</b>"+reqFrm.getSpecailistsName(reqFrm.specialist) +" <b>Phone:</b>"+reqFrm.specPhone+" <b>Fax:</b> "+reqFrm.specFax+"<br/>";
   s +="<b>Address:</b> "+reqFrm.specAddr+"<br/><br/><br/>";

   s +="<b>Patient Info</b><br/>";
   s +="<b>Name:</b>"+reqFrm.patientName+"<br/>";
   s +="<b>DOB:</b>"+reqFrm.patientDOB+"<br/>";
   s +="<b>HIN:</b>"+reqFrm.patientHealthNum+" "+reqFrm.patientHealthCardVersionCode+" <b>HIN Type:</b>"+reqFrm.patientHealthCardType+"<br/>";
   s +="<b>Phone:</b>"+reqFrm.patientPhone+"<br/>";
   s +="<b>Address:</b>"+reqFrm.patientAddress+"<br/><br/><br/>";


   s += "<b>Reason For Consultation</b> <br/> ";
   s += divy(reqFrm.reasonForConsultation);
   s += "<br/><br/>";
   s += "<b>Pertinent Clinical Information</b><br/> ";
   s += divy(reqFrm.clinicalInformation);
   s += "<br/><br/>";
   s += "<b>Significant Concurrent Problems</b><br/> ";
   s += divy(reqFrm.concurrentProblems);
   s += "<br/><br/>";
   s += "<b>Current Medications</b><br/> ";
   s += divy(reqFrm.currentMedications);
   s += "<br/><br/>";
   s += "<b>Allergies</b><br/> ";
   s += divy(reqFrm.allergies);
   s += "<br/><br/>";
   s = s.replaceAll("\"","&quot;");
   return s;
}
%>
