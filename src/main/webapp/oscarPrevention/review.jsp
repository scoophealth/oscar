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

<%@page import="org.hl7.fhir.dstu3.model.codesystems.PractitionerSpecialty"%>
<%@page import="org.hl7.fhir.dstu3.model.ContactPoint"%>
<%@page import="org.hl7.fhir.dstu3.model.Identifier"%>
<%@page import="org.hl7.fhir.dstu3.model.HumanName"%>
<%@page import="org.hl7.fhir.dstu3.model.Immunization"%>
<%@page import="org.hl7.fhir.dstu3.model.Patient"%>
<%@page import="org.hl7.fhir.dstu3.model.Practitioner"%>
<%@page import="org.hl7.fhir.dstu3.model.Organization"%>
<%@page import="org.hl7.fhir.dstu3.model.MessageHeader"%>
<%@page import="org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent"%>
<%@page import="org.oscarehr.common.model.Prevention"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.common.model.CVCMapping"%>
<%@page import="org.oscarehr.common.dao.CVCImmunizationDao"%>
<%@page import="org.oscarehr.common.dao.CVCMappingDao"%>
<%@page import="org.oscarehr.common.model.CVCMedicationLotNumber"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.oscarehr.common.model.CVCImmunization"%>
<%@page import="org.oscarehr.managers.CanadianVaccineCatalogueManager"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.oscarProvider.data.ProviderData"%>
<%@ page import="oscar.oscarDemographic.data.DemographicData,java.text.SimpleDateFormat, java.util.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*"%>
<%@ page import="org.oscarehr.casemgmt.model.CaseManagementNoteLink" %>
<%@ page import="org.oscarehr.casemgmt.service.CaseManagementManager" %>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.DemographicExtDao" %>
<%@page import="org.oscarehr.common.dao.PreventionsLotNrsDao" %>
<%@page import="org.oscarehr.common.model.PreventionsLotNrs" %>
<%@page import="org.hl7.fhir.dstu3.model.Bundle" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_prevention" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_prevention");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
  DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
  CanadianVaccineCatalogueManager cvcManager = SpringUtils.getBean(CanadianVaccineCatalogueManager.class);
  CVCMappingDao cvcMappingDao = SpringUtils.getBean(CVCMappingDao.class);
  CVCImmunizationDao cvcImmunizationDao = SpringUtils.getBean(CVCImmunizationDao.class);
  ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
  
  LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
  
  //String[] demoInfo = demoData.getNameAgeSexArray(LoggedInInfo.getLoggedInInfoFromSession(request), Integer.valueOf(demographic_no));
 // String nameage = demoInfo[0] + ", " + demoInfo[1] + " " + demoInfo[2] + " " + age;

 Bundle bundle = (Bundle)request.getAttribute("bundle");
 String errorMsg = (String)request.getAttribute("error");
 
 String submittingProviderNo = null;
 String sender = null;
 String sourceName = null;
 String sourceSoftware=null;
 String sourceVersion=null;
 String sourceEndpoint = null;
 String destinationName = null;
 String destinationEndpoint = null;
 String phu = null;
 String phuName = null;
 
 Practitioner submittingPractitioner = null;
 Map<String,Practitioner> performingPractitioners = new HashMap<String,Practitioner>();
 Patient patient = null;
 
 Map<String,Immunization> immunizations = new HashMap<String,Immunization>();
 
 
 for(BundleEntryComponent bec : bundle.getEntry()) {
		if(bec.getResource().fhirType().equals("MessageHeader")) {
			MessageHeader mh  = (MessageHeader)bec.getResource();
			submittingProviderNo = mh.getAuthor().getReference().split("/")[1];
			sender = mh.getSender().getDisplay();
			sourceName = mh.getSource().getName();
			sourceSoftware = mh.getSource().getSoftware();
			sourceVersion = mh.getSource().getVersion();
			sourceEndpoint = mh.getSource().getEndpoint();
			destinationName = mh.getDestination().get(0).getName();
			destinationEndpoint = mh.getDestination().get(0).getEndpoint();
		}
		if(bec.getResource().fhirType().equals("Organization")) {
			Organization organization = (Organization)bec.getResource();
			phu = organization.getIdentifier().get(0).getValue();
			phuName = organization.getName();
		}
		if(bec.getResource().fhirType().equals("Practitioner")) {
			Practitioner p = (Practitioner)bec.getResource();
			if(p.getId().equals(submittingProviderNo)) {
				submittingPractitioner = p;
			} else {
				performingPractitioners.put(p.getId(),p);
			}
		}
		if(bec.getResource().fhirType().equals("Patient")) {
			patient  = (Patient)bec.getResource();
			
		}
		
		if(bec.getResource().fhirType().equals("Immunization")) {
			Immunization i = (Immunization)bec.getResource();
			immunizations.put(i.getId(),i);			
		}
  }
 
 
%>


<html:html locale="true">

<head>
<title>OSCAR Prevention Review Screen</title><!--I18n-->
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../share/calendar/calendar.js" ></script>
<script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>" ></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js" ></script>

<style type="text/css">
  div.ImmSet { background-color: #ffffff; }
  div.ImmSet h2 {  }
  div.ImmSet ul {  }
  div.ImmSet li {  }
  div.ImmSet li a { text-decoration:none; color:blue;}
  div.ImmSet li a:hover { text-decoration:none; color:red; }
  div.ImmSet li a:visited { text-decoration:none; color:blue;}


  ////////
  div.prevention {  background-color: #999999; }
  div.prevention fieldset {width:35em; font-weight:bold; }
  div.prevention legend {font-weight:bold; }

  ////////
</style>



<style type="text/css">
	table.outline{
	   margin-top:50px;
	   border-bottom: 1pt solid #888888;
	   border-left: 1pt solid #888888;
	   border-top: 1pt solid #888888;
	   border-right: 1pt solid #888888;
	}
	table.grid{
	   border-bottom: 1pt solid #888888;
	   border-left: 1pt solid #888888;
	   border-top: 1pt solid #888888;
	   border-right: 1pt solid #888888;
	}
	td.gridTitles{
		border-bottom: 2pt solid #888888;
		font-weight: bold;
		text-align: center;
	}
        td.gridTitlesWOBottom{
                font-weight: bold;
                text-align: center;
        }
	td.middleGrid{
	   border-left: 1pt solid #888888;
	   border-right: 1pt solid #888888;
           text-align: center;
	}


label{
float: left;
width: 120px;
font-weight: bold;
}

label.checkbox{
float: left;
width: 116px;
font-weight: bold;
}

label.fields{
float: left;
width: 80px;
font-weight: bold;
}

span.labelLook{
font-weight:bold;

}

input, textarea,select{

//margin-bottom: 5px;
}

textarea{
width: 450px;
height: 100px;
}


.boxes{
width: 1em;
}

#submitbutton{
margin-left: 120px;
margin-top: 5px;
width: 90px;
}

br{
clear: left;
}
</style>


</head>

<body class="BodyStyle" vlink="#0000FF" onload="disableifchecked(document.getElementById('neverWarn'),'nextDate');">
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn" width="100" >
              DHIR Submission Review
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td >
                            
                        </td>
                        <td  >&nbsp;

                        </td>
                        <td style="text-align:right">
                                <oscar:help keywords="prevention" key="app.top1"/> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn" valign="top">
               &nbsp;
            </td>
            <td valign="top" class="MainTableRightColumn">
<%
			if(session.getAttribute("oneIdEmail") == null) {
		%>
		<div style="width:100%;background-color:pink;text-align:center;font-weight:bold;font-size:13pt">
			Warning: You are not logged into OneId and will not be able to submit data to DHIR
		</div>
		<% } %>
		
		<br/>
		
			<table style="width:90%;background-color: lightblue">
				<thead>
					<tr>
						<th colspan="2" style="text-align:left;background-color:#6699CC">Submitter</th>
					</tr>
					<tr>
						<th colspan="2" vheight="10px"></th>
					</tr>
				</thead>
				<tr>
					<td width="15%"><b>Name:</b></td>
					<td><%=getPractitionerName(submittingPractitioner) %></td>
				</tr>
				<tr>
					<td width="15%"><b>OneId username:</b></td>
					<td><%=getOneId(submittingPractitioner) %></td>
				</tr>
				<tr>
					<td width="15%"><b>Phone:</b></td>
					<td><%=getPractitionerPhone(submittingPractitioner) %></td>
				</tr>
			</table>
		
			<br/>
			
			<table style="width:90%;background-color: lightblue">
				<thead>
					<tr>
						<th colspan="2" style="text-align:left;background-color:#6699CC">Source</th>
					</tr>
					<tr>
						<th colspan="2" vheight="10px"></th>
					</tr>
				</thead>
				<tr>
					<td width="15%"><b>Name:</b></td>
					<td><%=sourceName %></td>
				</tr>
				<tr>
					<td width="15%"><b>Software:</b></td>
					<td><%=sourceSoftware %></td>
				</tr>
				<tr>
					<td width="15%"><b>Version:</b></td>
					<td><%=sourceVersion %></td>
				</tr>
				<tr>
					<td width="15%"><b>Endpoint:</b></td>
					<td><%=sourceEndpoint %></td>
				</tr>
			</table>
			
			<br/>
			
			<table style="width:90%;background-color: lightblue">
				<thead>
					<tr>
						<th colspan="2" style="text-align:left;background-color:#6699CC">Organization</th>
					</tr>
					<tr>
						<th colspan="2" vheight="10px"></th>
					</tr>
				</thead>
				<tr>
					<td width="15%"><b>Phu Id:</b></td>
					<td><%=phu %></td>
				</tr>
				<tr>
					<td width="15%"><b>PHU Name:</b></td>
					<td><%=phuName %></td>
				</tr>
				
			</table>
			
			<br/>
			
			<table style="width:90%;background-color: lightblue">
				<thead>
					<tr>
						<th colspan="2" style="text-align:left;background-color:#6699CC">Patient</th>
					</tr>
					<tr>
						<th colspan="2" vheight="10px"></th>
					</tr>
				</thead>
				<tr>
					<td width="15%"><b>Demographic No:</b></td>
					<td><%=patient.getId() %></td>
				</tr>
				<tr>
					<td width="15%"><b>Name:</b></td>
					<td><%=getPatientName(patient) %></td>
				</tr>
				<tr>
					<td width="15%"><b>HIN:</b></td>
					<td><%=getHIN(patient) %></td>
				</tr>
				<tr>
					<td width="15%"><b>Phone:</b></td>
					<td><%=getPatientPhone(patient) %></td>
				</tr>
				<tr>
					<td width="15%"><b>Gender:</b></td>
					<td><%=patient.getGender() %></td>
				</tr>
				<tr>
					<td width="15%"><b>DOB:</b></td>
					<td><%=patient.getBirthDate() %></td>
				</tr>
				<tr>
					<td width="15%" valign="top"><b>Address:</b></td>
					<td><%=patient.getAddress().get(0).getLine().get(0).asStringValue() %><br/><%=patient.getAddress().get(0).getCity() %><br/><%=patient.getAddress().get(0).getState() %><br/><%=patient.getAddress().get(0).getPostalCode() %></td>
				</tr>
				
			</table>
		
		<br/>
			
			<%
				Iterator iter = immunizations.keySet().iterator();
			
				while(iter.hasNext()) {
					Immunization immunization = immunizations.get((String)iter.next());
					String apProviderNo = immunization.getPractitioner().get(0).getActor().getReference().split("/")[1];
					Practitioner performer = performingPractitioners.get(apProviderNo);
					
			%>
			
			<table style="width:90%;background-color: lightblue">
				<thead>
					<tr>
						<th colspan="2" style="text-align:left;background-color:#6699CC">Immunization</th>
					</tr>
					<tr>
						<th colspan="2" vheight="10px"></th>
					</tr>
				</thead>
				<tr>
					<td width="15%"><b>Status:</b></td>
					<td><%=immunization.getStatus() %></td>
				</tr>
				<tr>
					<td width="15%"><b>Not Given:</b></td>
					<td><%=immunization.getNotGiven() %></td>
				</tr>
				<tr>
					<td width="15%"><b>Snomed Code:</b></td>
					<td><%=getVaccineCode(immunization)%></td>
				</tr>
				<tr>
					<td width="15%"><b>Snomed Display:</b></td>
					<td><%=getVaccineCodeDisplay(immunization)%></td>
				</tr>
				<tr>
					<td width="15%"><b>Date Given:</b></td>
					<td><%=immunization.getDate() %></td>
				</tr>
				<tr>
					<td width="15%"><b>Primary Source:</b></td>
					<td><%=immunization.getPrimarySource() %></td>
				</tr>
				<!-- 
				<tr>
					<td width="15%"><b>Report Origin:</b></td>
					<td></td>
				</tr>
				-->
				<tr>
					<td width="15%"><b>Lot #:</b></td>
					<td><%=immunization.getLotNumber() %></td>
				</tr>
				<tr>
					<td width="15%"><b>Expiration Date:</b></td>
					<td><%=immunization.getExpirationDate() %></td>
				</tr>
				<tr>
					<td width="15%"><b>Site:</b></td>
					<td><%=immunization.getSite().getText() %></td>
				</tr>
				<tr>
					<td width="15%"><b>Route Display:</b></td>
					<td><%=immunization.getRoute().getCoding().get(0).getDisplay() %></td>
				</tr>
				<tr>
					<td width="15%"><b>Dose Qty:</b></td>
					<td><%=immunization.getDoseQuantity().getValue() %></td>
				</tr>
				<tr>
					<td width="15%"><b>Dose Unit:</b></td>
					<td><%=immunization.getDoseQuantity().getUnit()%></td>
				</tr>
				<tr>
					<td width="15%"><b>Reason:</b></td>
					<td><%=immunization.getExplanation().getReason().get(0).getCoding().get(0).getDisplay() %></td>
				</tr>
			
			
				<tr>
					<th colspan="2" style="text-align:left;background-color:#6699CC">Performer Information</th>
				</tr>
		
				<tr>
					<td width="15%"><b>Provider No:</b></td>
					<td><%=performer.getId() %></td>
				</tr>
				<tr>
					<td width="15%"><b>Name:</b></td>
					<td><%=getPractitionerName(performer) %></td>
				</tr>
				<tr>
					<td width="15%"><b>Id Type:</b></td>
					<td><%=getPractitionerCollegeIdType(performer) %></td>
				</tr>
				<tr>
					<td width="15%"><b>Id:</b></td>
					<td><%=getPractitionerCollegeId(performer) %></td>
				</tr>
					
			</table>
			
			<br/>
			
			<% } %>
			
			<form action="<%=request.getContextPath()%>/dhir/submit.do">
				<input type="hidden" name="uuid" value="<%=bundle.getId()%>"/>
				<input type="submit" value="Submit" />
				&nbsp;&nbsp;
				<input type="button" value="Cancel" onClick="window.close()"/>
			</form>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">
            &nbsp;
            </td>
            <td class="MainTableBottomRowRightColumn" valign="top">
            &nbsp;
            </td>
        </tr>
    </table>

</body>
</html:html>
<%!
	String getPractitionerName(Practitioner p) {
		HumanName name = p.getName().get(0);
		String lastName = name.getFamily();
		String given = name.getGiven().get(0).asStringValue();
		return lastName + ", " + given;
	}

	String getOneId(Practitioner p) {
		for(Identifier i : p.getIdentifier()) {
			if("https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-provider-oneid".equals(i.getSystem())) {
				return i.getValue();				
			}
		}
		return null;
	}
	
	String getPractitionerPhone(Practitioner p) {
		for(ContactPoint cp : p.getTelecom()) {
			if(cp.getSystem().toString().equals("PHONE") && cp.getUse().toString().equals("WORK")) {
				return cp.getValue();
			}
		}
		return null;
	}
	
	String getPatientName(Patient p) {
		HumanName name = p.getName().get(0);
		String lastName = name.getFamily();
		String given = name.getGiven().get(0).asStringValue();
		return lastName + ", " + given;
	}
	
	String getHIN(Patient p) {
		for(Identifier i : p.getIdentifier()) {
			if("http://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-patient-hcn".equals(i.getSystem())) {
				return i.getValue();				
			}
		}
		return null;
	}
	
	String getPatientPhone(Patient p) {
		for(ContactPoint cp : p.getTelecom()) {
			if(cp.getSystem().toString().equals("PHONE") && cp.getUse().toString().equals("HOME")) {
				return cp.getValue();
			}
		}
		return null;
	}
	
	String getPractitionerCollegeId(Practitioner p) {
		Identifier id = p.getIdentifier().get(0);
		return id.getValue();
	}
	
	String getPractitionerCollegeIdType(Practitioner p) {
		Identifier id = p.getIdentifier().get(0);
		if("https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-license-nurse".equals(id.getSystem())) {
			return "CNO";
		}
		if("https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-license-physician".equals(id.getSystem())) {
			return "CPSO";
		}
		return null;
	}

	String getVaccineCode(Immunization i) {
		return i.getVaccineCode().getCoding().get(0).getCode();
	}
	
	String getVaccineCodeDisplay(Immunization i) {
		return i.getVaccineCode().getCoding().get(0).getDisplay();
	}
%>