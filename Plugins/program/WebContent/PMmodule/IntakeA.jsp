
<%@ page language="java"%>
<%@ page import="org.caisi.PMmodule.web.*" %>
<%@ page import="org.caisi.PMmodule.common.*" %>
<%@ page import="org.caisi.PMmodule.dao.*" %>
<%@ page import="org.caisi.PMmodule.model.*" %>
<%@ page import="org.caisi.PMmodule.utility.*" %>
<%@ page import="java.util.*" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>


<% response.setHeader("Cache-Control","no-cache");%>
<%
    PassIntakeFormVars passIntakeAVars = new PassIntakeFormVars(request, response);
    String viewIntakeA = passIntakeAVars.getViewIntakeA();

	Formintakea intakeA = new Formintakea();
	
	if(viewIntakeA != null  &&  viewIntakeA.equals("Y")  &&  request.getAttribute("intakeAClientInfo") != null)
	{
		intakeA = (Formintakea)request.getAttribute("intakeAClientInfo");   
		if(intakeA == null)
		{
			intakeA = new Formintakea();
		}

	}
	else if(viewIntakeA != null  &&  !viewIntakeA.equals("Y")  &&  request.getAttribute("newIntakeA") != null )
	{
		intakeA = (Formintakea)request.getAttribute("newIntakeA");
		
		if(intakeA == null)
		{
			intakeA = new Formintakea();
		}
	}
%>
<%
	String demographicNo = passIntakeAVars.getDemographicNo();
	String providerNo = Utility.convertToRelacementStrIfNull(passIntakeAVars.getProviderNo(), "0");
%>
<%
	String dateStr1 = Utility.escapeNull(intakeA.getAssessDate());
	String dateStr2 = Utility.escapeNull(intakeA.getEnterSeatonDate());
	if(viewIntakeA != null  &&  !viewIntakeA.equals("Y"))
	{
		dateStr1 = DateUtils.getCurrentDateOnlyStr("/");
		dateStr2 = DateUtils.getCurrentDateOnlyStr("/");
	}
%>
<%
	String timeDisplay = Utility.escapeNull(intakeA.getAssessStartTime());
	if(viewIntakeA != null  &&  !viewIntakeA.equals("Y"))
	{
		String dateTime = DateUtils.getDateTime();

		if(dateTime.length() >= 13)
		{
			timeDisplay = dateTime.substring(dateTime.length()-11, dateTime.length());
		}
	}

%>
<html:html>
<head>

<title>INTAKE A. RECEPTION ASSESSMENT</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" type="text/css" href="<html:rewrite page="/css/intakeA.css" />">

<script language="JavaScript" src="<html:rewrite page="/js/IntakeA.js" />"  ></script>

<html:base />

</head>

<body>

<html:form action="/PMmodule/IntakeA.do">

<input type="hidden" name="demographic_no" value="<%=demographicNo%>" />
<input type="hidden" name="provider_no"  value="<%=providerNo%>" />
<input type="hidden" name="formCreated"  value="<%=Utility.escapeNull(intakeA.getFormCreated())%>" />

<input type="hidden" name="submitForm" value="exit"/>
<input type="hidden" name="method" value=""/>

<table width="100%" border="0">
  <tr>
    <td  width="5%">&nbsp;
    	
    </td>

	<td>
<table width="95%" border="0">
  <tr>
    <td colspan="3"  class="style63">
    	INTAKE A. RECEPTION ASSESSMENT
    	&nbsp;&nbsp;&nbsp;
	  	<input type="button" value="Back" onclick="javascript:history.back()">
	</td>
    
  </tr>
</table>

<table width="95%" border="1">
  <tr>
    <td class="style76">
		Assessment Date (YYYY/MM/DD): <br>
		<input type="text" name="assessDate" value="<%=dateStr1%>">
	</td>
    <td class="style76">Assessment start time:
      <br>
		<input type="text" name="assessStartTime" value="<%=timeDisplay%>">
	    am / pm</br></td>
    <td class="style76">
		<input type="checkbox" name="cbox_sharing" value="Y" <%=Utility.escapeNull(intakeA.getCboxSharing()).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_sharing);" >
		Do you want to share your information with other agencies that provide you service?
		<span>
	</td>
  </tr>
  <tr>
    <td class="style76">
		Date client entered agency (YYYY/MM/DD)<br>
		<input type="text" name="enterSeatonDate" value="<%=dateStr2%>">
	</td>
    <td class="style76">
		<input type="checkbox" name="cbox_newClient" value="Y"  <%=Utility.escapeNull(intakeA.getCboxNewclient()).equalsIgnoreCase("Y")?"checked":"" %> >
	 	<span onClick="javascript:clickCheckBox(document.forms[0].cbox_newClient);" >New Client</span> 
	 </td>
    <td class="style76">
 		<input type="checkbox" name="cbox_dateOfReadmission" value="Y" <%=Utility.escapeNull(intakeA.getCboxDateofreadmission()).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_dateOfReadmission);" >Re-admission. Give date of last admission (YYYY/MM/DD).</span>
		<input type="text" name="dateOfReadmission" value="<%=Utility.escapeNull(intakeA.getDateOfReadmission())%>">
		  
	 </td>
  </tr>
</table>

<table width="95%" border="1">
  <tr>
    <td colspan="3" align="center" class="style51">Notes for completing this intake:
      </td>
  </tr>
</table>

<table width="95%" border="1">
  <tr>
    <td colspan="3" align="left" class="style61">
	Reception Staff:&nbsp;&nbsp;
	<input type="checkbox" name="cbox_isStatementRead"  
	  value="Y" <%=(Utility.escapeNull(intakeA.getCboxIsstatementread())).equalsIgnoreCase("Y")?"checked":""%> >
	<br>
	1. Please complete every section of this intake. If you are unable to complete a section - 
	   use 'Comments' section to explain why (e.g., client refused to answer, interview cut short).<br>
	2. Before beginning this intake, please read this paragraph to the client:"We would like to ask you some questions 
	to ensure that you are getting the health care that you need and want while at this agency. 
	Any answers you give us will be kept private in your file. We will collect numbers from the answers 
	that we get from everyone on a regular basis to try and make the system better at this agency and in the City 
	but in this case we would not use your name. You don't have to answer these questions. Your stay at this agency will not 
	change if you don't answer these questions"
	
    </td>
  </tr>
</table>

<table width="95%" border="1">
  <tr>
    <td colspan="3" align="center" class="style51">Identifying Data </td>
  </tr>
  <tr>
<%
	String readOnly = "";
	if(viewIntakeA != null  &&  viewIntakeA.equals("Y"))
	{
		readOnly = "readonly";
	}
%>
    <td  align="left" class="style76">
		Client's Surname
		<input type="text" name="clientSurname" value="<%=Utility.escapeNull(intakeA.getClientSurname())%>"  <%=readOnly%> >
	</td>
    <td  align="left" class="style76">
		Client's First Name
		<input type="text" name="clientFirstName" value="<%=Utility.escapeNull(intakeA.getClientFirstName())%>"  <%=readOnly%> >
	</td>

    <td  align="left" class="style76">Date of Birth<br>
	    (MM/DD/YYYY)<br>
		<select name="month"> 
			<option value=""  <%=Utility.escapeNull(intakeA.getMonth()).equals("")?"selected":""%>></option>
			<option value="01" <%=Utility.escapeNull(intakeA.getMonth()).equals("01")?"selected":""%>>1</option>
			<option value="02" <%=Utility.escapeNull(intakeA.getMonth()).equals("02")?"selected":""%>>2</option>
			<option value="03" <%=Utility.escapeNull(intakeA.getMonth()).equals("03")?"selected":""%>>3</option>
			<option value="04" <%=Utility.escapeNull(intakeA.getMonth()).equals("04")?"selected":""%>>4</option>			
			<option value="05" <%=Utility.escapeNull(intakeA.getMonth()).equals("05")?"selected":""%>>5</option>			
			<option value="06" <%=Utility.escapeNull(intakeA.getMonth()).equals("06")?"selected":""%>>6</option>			
			<option value="07" <%=Utility.escapeNull(intakeA.getMonth()).equals("07")?"selected":""%>>7</option>			
			<option value="08" <%=Utility.escapeNull(intakeA.getMonth()).equals("08")?"selected":""%>>8</option>			
			<option value="09" <%=Utility.escapeNull(intakeA.getMonth()).equals("09")?"selected":""%>>9</option>			
			<option value="10" <%=Utility.escapeNull(intakeA.getMonth()).equals("10")?"selected":""%>>10</option>			
			<option value="11" <%=Utility.escapeNull(intakeA.getMonth()).equals("11")?"selected":""%>>11</option>			
			<option value="12" <%=Utility.escapeNull(intakeA.getMonth()).equals("12")?"selected":""%>>12</option>			
		</select>
		<select name="day"> 
			<option value=""  <%=Utility.escapeNull(intakeA.getDay()).equals("")?"selected":""%>></option>
			<option value="01" <%=Utility.escapeNull(intakeA.getDay()).equals("01")?"selected":""%>>1</option>
			<option value="02" <%=Utility.escapeNull(intakeA.getDay()).equals("02")?"selected":""%>>2</option>
			<option value="03" <%=Utility.escapeNull(intakeA.getDay()).equals("03")?"selected":""%>>3</option>
			<option value="04" <%=Utility.escapeNull(intakeA.getDay()).equals("04")?"selected":""%>>4</option>			
			<option value="05" <%=Utility.escapeNull(intakeA.getDay()).equals("05")?"selected":""%>>5</option>			
			<option value="06" <%=Utility.escapeNull(intakeA.getDay()).equals("06")?"selected":""%>>6</option>			
			<option value="07" <%=Utility.escapeNull(intakeA.getDay()).equals("07")?"selected":""%>>7</option>			
			<option value="08" <%=Utility.escapeNull(intakeA.getDay()).equals("08")?"selected":""%>>8</option>			
			<option value="09" <%=Utility.escapeNull(intakeA.getDay()).equals("09")?"selected":""%>>9</option>			
			<option value="10" <%=Utility.escapeNull(intakeA.getDay()).equals("10")?"selected":""%>>10</option>			
			<option value="11" <%=Utility.escapeNull(intakeA.getDay()).equals("11")?"selected":""%>>11</option>			
			<option value="12" <%=Utility.escapeNull(intakeA.getDay()).equals("12")?"selected":""%>>12</option>			
			<option value="13" <%=Utility.escapeNull(intakeA.getDay()).equals("13")?"selected":""%>>13</option>
			<option value="14" <%=Utility.escapeNull(intakeA.getDay()).equals("14")?"selected":""%>>14</option>
			<option value="15" <%=Utility.escapeNull(intakeA.getDay()).equals("15")?"selected":""%>>15</option>
			<option value="16" <%=Utility.escapeNull(intakeA.getDay()).equals("16")?"selected":""%>>16</option>
			<option value="17" <%=Utility.escapeNull(intakeA.getDay()).equals("17")?"selected":""%>>17</option>
			<option value="18" <%=Utility.escapeNull(intakeA.getDay()).equals("18")?"selected":""%>>18</option>
			<option value="19" <%=Utility.escapeNull(intakeA.getDay()).equals("19")?"selected":""%>>19</option>
			<option value="20" <%=Utility.escapeNull(intakeA.getDay()).equals("20")?"selected":""%>>20</option>
			<option value="21" <%=Utility.escapeNull(intakeA.getDay()).equals("21")?"selected":""%>>21</option>
			<option value="22" <%=Utility.escapeNull(intakeA.getDay()).equals("22")?"selected":""%>>22</option>
			<option value="23" <%=Utility.escapeNull(intakeA.getDay()).equals("23")?"selected":""%>>23</option>
			<option value="24" <%=Utility.escapeNull(intakeA.getDay()).equals("24")?"selected":""%>>24</option>
			<option value="25" <%=Utility.escapeNull(intakeA.getDay()).equals("25")?"selected":""%>>25</option>
			<option value="26" <%=Utility.escapeNull(intakeA.getDay()).equals("26")?"selected":""%>>26</option>
			<option value="27" <%=Utility.escapeNull(intakeA.getDay()).equals("27")?"selected":""%>>27</option>
			<option value="28" <%=Utility.escapeNull(intakeA.getDay()).equals("28")?"selected":""%>>28</option>
			<option value="29" <%=Utility.escapeNull(intakeA.getDay()).equals("29")?"selected":""%>>29</option>
			<option value="30" <%=Utility.escapeNull(intakeA.getDay()).equals("30")?"selected":""%>>30</option>
			<option value="31" <%=Utility.escapeNull(intakeA.getDay()).equals("31")?"selected":""%>>31</option>
		</select>
            <input type="text" name="year" size="7"  maxlength="4" value="<%=Utility.escapeNull(intakeA.getYear())%>"> 
          </td>	
  </tr>
</table>


<table width="95%" border="1">
  <tr>
    <td  align="left" class="style76">
		Language(s) Spoken:
	</td>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_speakEnglish" value="Y" <%=(Utility.escapeNull(intakeA.getCboxSpeakenglish())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_speakEnglish);" >English </span>
	</td>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_speakFrench" value="Y" <%=(Utility.escapeNull(intakeA.getCboxSpeakfrench())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_speakFrench);" >French</span>
	</td>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_speakOther" value="Y" <%=(Utility.escapeNull(intakeA.getCboxSpeakother())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_speakOther);" >Other: </span>
		<input type="text" name="speakOther" value="<%=Utility.escapeNull(intakeA.getSpeakOther())%>">

	</td>
  </tr>
</table>

<table width="95%" border="1">
  <tr>
    <td  align="center" class="style51">1. Reason for Admission</td>
  </tr>		
  <tr>
    <td  align="left" class="style76">What is your reason for coming to this agency? (state briefly in client's own words if possible)</td>
  </tr>		
  <tr>
    <td  align="left" class="style76">
		<textArea name="reasonToSeaton"  cols="75"><%=Utility.escapeNull(intakeA.getReasonToSeaton())%></textArea>
	</td>
  </tr>		
  <tr>
    <td  align="left" class="style76">
		Have you ever stayed at this agency before? (give dates)
		<input type="text" name="datesAtSeaton" size="55" value="<%=Utility.escapeNull(intakeA.getDatesAtSeaton())%>">
	</td>
  </tr>		
</table>

<table width="95%" border="1">
  <tr>
    <td  align="center"  colspan="2" class="style51">2. Assistance Required</td>
  </tr>		
  <tr>
    <td  align="left" colspan="2"  class="style76">Do you require assistance with any of the following :</td>
  </tr>		
  <tr>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_assistInHealth" value="Y" <%=(Utility.escapeNull(intakeA.getCboxAssistinhealth())).equalsIgnoreCase("Y")?"checked":""%>>
	</td>		
	<td  align="left" class="style76">
	    <span onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInHealth);" >Physical or Mental Health, including medication</span>
	</td>
  </tr>		
  <tr>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_assistInIdentification" value="Y" <%=(Utility.escapeNull(intakeA.getCboxAssistinidentification())).equalsIgnoreCase("Y")?"checked":""%>>
	</td>		
	<td  align="left" class="style76">
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInIdentification);" >Obtaining Identification</span>
	</td>
  </tr>		
  <tr>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_assistInAddictions" value="Y" <%=(Utility.escapeNull(intakeA.getCboxAssistinaddictions())).equalsIgnoreCase("Y")?"checked":""%>>
	</td>		
	<td  align="left" class="style76">
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInAddictions);" >Addictions</span>	
	</td>
  </tr>		
  <tr>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_assistInHousing" value="Y" <%=(Utility.escapeNull(intakeA.getCboxAssistinhousing())).equalsIgnoreCase("Y")?"checked":""%>>
	</td>		
	<td  align="left" class="style76">
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInHousing);" >Housing issues</span>
	</td>
  </tr>		
  <tr>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_assistInEducation" value="Y" <%=(Utility.escapeNull(intakeA.getCboxAssistineducation())).equalsIgnoreCase("Y")?"checked":""%>>
	</td>		
	<td  align="left" class="style76">
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInEducation);" >Education Issues</span>	
	</td>
  </tr>	
  <tr>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_assistInEmployment" value="Y" <%=(Utility.escapeNull(intakeA.getCboxAssistinemployment())).equalsIgnoreCase("Y")?"checked":""%>>
	</td>		
	<td  align="left" class="style76">
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInEmployment);" >Employment issues</span>
	</td>
  </tr>		
  	
  <tr>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_assistInFinance" value="Y" <%=(Utility.escapeNull(intakeA.getCboxAssistinfinance())).equalsIgnoreCase("Y")?"checked":""%>>
	</td>		
	<td  align="left" class="style76">
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInFinance);" >Financial issues</span>
	</td>
  </tr>		
  <tr>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_assistInLegal" value="Y" <%=(Utility.escapeNull(intakeA.getCboxAssistinlegal())).equalsIgnoreCase("Y")?"checked":""%>>
	</td>		
	<td  align="left" class="style76">
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInLegal);" >Legal issues</span>
	</td>
  </tr>		
  <tr>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_assistInImmigration" value="Y" <%=(Utility.escapeNull(intakeA.getCboxAssistinimmigration())).equalsIgnoreCase("Y")?"checked":""%>>
	</td>		
	<td  align="left" class="style76">
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_assistInImmigration);" >Immigration issues</span>
	</td>
  </tr>		
  
</table>

<table width="95%" border="1">
  <tr>
    <td  colspan="4" align="center" class="style51">3. Identification</td>
  </tr>		
  <tr>
    <td  colspan="2"  align="left" class="style76">What identification do you have?</td>
    <td  colspan="2"  align="left" class="style76">
		<input type="checkbox" name="cbox_noID" value="Y" <%=(Utility.escapeNull(intakeA.getCboxNoid())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_noID);" >No ID</span>
	</td>
	
  </tr>		
  <tr>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_sinCard" value="Y" <%=(Utility.escapeNull(intakeA.getCboxSincard())).equalsIgnoreCase("Y")?"checked":""%>>
	    <span onClick="javascript:clickCheckBox(document.forms[0].cbox_sinCard);" >SIN Card:</span> <br>
		<input type="text" name="sinNum" maxlength="18"  value="<%=Utility.escapeNull(intakeA.getSinNum())%>">
	</td>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_healthCard" value="Y" <%=(Utility.escapeNull(intakeA.getCboxHealthcard())).equalsIgnoreCase("Y")?"checked":""%>>
	    <span onClick="javascript:clickCheckBox(document.forms[0].cbox_healthCard);" >Ontario Health Card# (ver)</span>  <br>
		<input type="text" name="healthCardNum"  maxlength="18" value="<%=Utility.escapeNull(intakeA.getHealthCardNum())%>">
		<input type="text" name="healthCardVer"  maxlength="2"  size="2"  value="<%=Utility.escapeNull(intakeA.getHealthCardVer())%>">
		
	</td>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_birthCertificate" value="Y" <%=(Utility.escapeNull(intakeA.getCboxBirthcertificate())).equalsIgnoreCase("Y")?"checked":""%>>
	    <span onClick="javascript:clickCheckBox(document.forms[0].cbox_birthCertificate);" >Birth Certificate</span><br>
		<input type="text" name="birthCertificateNum"  maxlength="18" value="<%=Utility.escapeNull(intakeA.getBirthCertificateNum())%>">
	</td>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_citzenshipCard" value="Y" <%=(Utility.escapeNull(intakeA.getCboxCitzenshipcard())).equalsIgnoreCase("Y")?"checked":""%>>
	    <span onClick="javascript:clickCheckBox(document.forms[0].cbox_citzenshipCard);" >Citizenship Card</span><br>
		<input type="text" name="citzenshipCardNum"  maxlength="18" value="<%=Utility.escapeNull(intakeA.getCitzenshipCardNum())%>">
	</td>
	
  </tr>		
  <tr>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_immigrant" value="Y" <%=(Utility.escapeNull(intakeA.getCboxImmigrant())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_immigrant);" >Landed Immigrant</span><br>
		<input type="text" name="immigrantNum"  maxlength="18" value="<%=Utility.escapeNull(intakeA.getImmigrantNum())%>">
	</td>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_refugee" value="Y" <%=(Utility.escapeNull(intakeA.getCboxRefugee())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_refugee);" >Convention Refugee</span><br>
		<input type="text" name="refugeeNum"  maxlength="18"  value="<%=Utility.escapeNull(intakeA.getRefugeeNum())%>">
	</td>
    <td  colspan="2" align="left" class="style76">
		<input type="checkbox" name="cbox_otherID" value="Y" <%=(Utility.escapeNull(intakeA.getCboxOtherid())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_otherID);" >Other (specify)</span>
		<input type="text" name="otherIdentification"  maxlength="18" size="35" value="<%=Utility.escapeNull(intakeA.getOtherIdentification())%>">
	</td>
	
  </tr>		
  <tr>
    <td  colspan="2" align="left" class="style76">STAFF: Please photocopy and certify ID, and put copy in client file	</td>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_idFiled" value="Y" <%=(Utility.escapeNull(intakeA.getCboxIdfiled())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_idFiled);" >ID copied and filed</span>
	</td>
    <td  align="left" class="style76">
		<input type="checkbox" name="cbox_idNone" value="Y" <%=(Utility.escapeNull(intakeA.getCboxIdnone())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_idNone);" >ID not available</span>
	</td>
	
  </tr>		
  <tr>
    <td  colspan="4" align="left" valign="top" class="style76">
		Comments:<br>
		<textArea name="commentsOnID"  cols="75"><%=Utility.escapeNull(intakeA.getCommentsOnID())%></textArea>
		
	</td>
  </tr>		

</table>

<table width="95%" border="1">
  <tr>
    <td  colspan="9" align="center" class="style51">4. ON-LINE CHECK</td>
  </tr>		
  <tr>
    <td  colspan="9"  align="left" class="style76">What has been your main source of income in the past 12 months (check one)</td>
  </tr>		
  <tr>
    <td  colspan="1" width="10%"  align="left" class="style76">
		<input type="checkbox" name="cbox_OW" value="Y" <%=(Utility.escapeNull(intakeA.getCboxOw())).equalsIgnoreCase("Y")?"checked":""%>>
	    <span onClick="javascript:clickCheckBox(document.forms[0].cbox_OW);" >OW</span>
	</td>
    <td  colspan="1"  width="12%"  align="left" class="style76">
		<input type="checkbox" name="cbox_ODSP" value="Y" <%=(Utility.escapeNull(intakeA.getCboxOdsp())).equalsIgnoreCase("Y")?"checked":""%>>
	    <span onClick="javascript:clickCheckBox(document.forms[0].cbox_ODSP);" >ODSP</span>
	</td>
    <td  colspan="1" width="12%"  align="left" class="style76">
		<input type="checkbox" name="cbox_WSIB" value="Y" <%=(Utility.escapeNull(intakeA.getCboxWsib())).equalsIgnoreCase("Y")?"checked":""%>>
	    <span onClick="javascript:clickCheckBox(document.forms[0].cbox_WSIB);" >WSIB</span>
	</td>
    <td  colspan="2" width="15%"  align="left" class="style76">
		<input type="checkbox" name="cbox_Employment" value="Y" <%=(Utility.escapeNull(intakeA.getCboxEmployment())).equalsIgnoreCase("Y")?"checked":""%>>
	    <span onClick="javascript:clickCheckBox(document.forms[0].cbox_Employment);" >Employment</span>
	</td>
    <td  colspan="1" width="11%"  align="left" class="style76">
		<input type="checkbox" name="cbox_EI" value="Y" <%=(Utility.escapeNull(intakeA.getCboxEi())).equalsIgnoreCase("Y")?"checked":""%>>
	    <span onClick="javascript:clickCheckBox(document.forms[0].cbox_EI);" >EI</span>
	</td>
    <td  colspan="1" width="11%"  align="left" class="style76">
		<input type="checkbox" name="cbox_OAS" value="Y" <%=(Utility.escapeNull(intakeA.getCboxOas())).equalsIgnoreCase("Y")?"checked":""%>>
	    <span onClick="javascript:clickCheckBox(document.forms[0].cbox_OAS);" >OAS</span>
	</td>
    <td  colspan="1" width="10%"  align="left" class="style76">
		<input type="checkbox" name="cbox_CPP" value="Y" <%=(Utility.escapeNull(intakeA.getCboxCpp())).equalsIgnoreCase("Y")?"checked":""%>>
	    <span onClick="javascript:clickCheckBox(document.forms[0].cbox_CPP);" >CPP</span>
	</td>
    <td width="19%"  colspan="2" align="left" class="style76">
		<input type="checkbox" name="cbox_OtherIncome" value="Y" <%=(Utility.escapeNull(intakeA.getCboxOtherincome())).equalsIgnoreCase("Y")?"checked":""%>>
	    <span onClick="javascript:clickCheckBox(document.forms[0].cbox_OtherIncome);" >Other</span>
	</td>
  </tr>		
  <tr>
    <td  colspan="5" align="left" class="style76">
		OW/ODSP On-Line check completed by worker?
	</td>
    <td colspan="2"  align="left" class="style76">
		<input type="radio" name="radio_onlineCheck" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioOnlinecheck())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_onlineCheck[0]);" >Yes</span>
	</td>
    <td  colspan="2" align="left" class="style76">
		<input type="radio" name="radio_onlineCheck" value="no"  <%=(Utility.escapeNull(intakeA.getRadioOnlinecheck())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_onlineCheck[1]);" >No</span>
	</td>
  </tr>		
  <tr>
    <td  colspan="2" align="left" class="style76">
		<input type="radio" name="radio_active" value="active"  <%=(Utility.escapeNull(intakeA.getRadioActive())).equalsIgnoreCase("active")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_active[0]);" >Active</span>
	</td>
    <td  colspan="2"  align="left" class="style76">
		<input type="radio" name="radio_active" value="inactive"  <%=(Utility.escapeNull(intakeA.getRadioActive())).equalsIgnoreCase("inactive")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_active[1]);" >Inactive</span>
	</td>
    <td  colspan="5" align="left" class="style76">
		<input type="checkbox" name="cbox_noRecord" value="Y" <%=(Utility.escapeNull(intakeA.getCboxNorecord())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_noRecord);" >No Record</span>

	</td>
  </tr>	
 
  <tr>
    <td  colspan="2" align="left" class="style76">
		Last issuance date (YYYY/MM/DD):
		<input type="text" name="lastIssueDate" value="<%=Utility.escapeNull(intakeA.getLastIssueDate())%>">
	</td>
    <td  colspan="3"  align="left" class="style76">
		Office:
		<input type="text" name="office" value="<%=Utility.escapeNull(intakeA.getOffice())%>">
	</td>
    <td  colspan="2" align="left" class="style76">
		Worker#:
		<input type="text" name="workerNum" value="<%=Utility.escapeNull(intakeA.getWorkerNum())%>">
	</td>
    <td  colspan="2" align="left" class="style76">
		Amount Received: <br>$
		<input type="text" name="amtReceived"  maxlength="9" value="<%=Utility.escapeNull(intakeA.getAmtReceived())%>" size="10">
	</td>
	
  </tr>		
</table>

<table width="95%" border="1">
  <tr>
    <td  colspan="9" align="center" class="style51">5. HEALTH</td>
  </tr>		
  <tr>
    <td  colspan="9"  align="left" class="style76">Physical Health </td>
  </tr>		
  <tr>
    <td  colspan="6"  align="left" class="style76">
		1. Do you have a regular medical doctor or specialist?
	</td>
    <td width="11%"  colspan="1"   align="left" class="style76">
		<input type="radio" name="radio_hasDoctor" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioHasdoctor())).equalsIgnoreCase("yes")?"checked":""%>>
	    <span onClick="javascript:clickRadio(document.forms[0].radio_hasDoctor[0]);" >Yes</span>
	</td>
    <td  colspan="1"   align="left" class="style76">
		<input type="radio" name="radio_hasDoctor" value="no"  <%=(Utility.escapeNull(intakeA.getRadioHasdoctor())).equalsIgnoreCase("no")?"checked":""%>>
	    <span onClick="javascript:clickRadio(document.forms[0].radio_hasDoctor[1]);" >No</span>
	</td>
    <td  colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_hasDoctor" value="dontKnow"  <%=(Utility.escapeNull(intakeA.getRadioHasdoctor())).equalsIgnoreCase("dontKnow")?"checked":""%>>
	    <span onClick="javascript:clickRadio(document.forms[0].radio_hasDoctor[2]);" >Don't know</span>
	</td>

  </tr>	
  <tr>	
  	<td colspan="9" class="style76">
		If yes, what is the name, address and phone # of your doctor or specialist?	
	</td>
  </tr>
  <tr>
    <td  colspan="4" align="left" class="style76">
		Name:
		<input type="text" name="doctorName" value="<%=Utility.escapeNull(intakeA.getDoctorName())%>">
		
	</td>
    <td colspan="5"  align="left" class="style76">
		Phone #:
		<input type="text" name="doctorPhone" value="<%=Utility.escapeNull(intakeA.getDoctorPhone())%>">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Ext.
		<input type="text" name="doctorPhoneExt" value="<%=Utility.escapeNull(intakeA.getDoctorPhoneExt())%>">
	</td>
  </tr>		
  <tr>
    <td  colspan="9" align="left" class="style76">
		Address:	
		<input type="text" name="doctorAddress" size="70"  maxlength="150" value="<%=Utility.escapeNull(intakeA.getDoctorAddress())%>">
	</td>
  </tr>	
  
  <tr>
    <td  colspan="7" align="left" class="style76">
		If yes, Would you be able to see this doctor again if you needed to see a doctor?
	</td>
    <td width="8%"  colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_seeDoctor" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioSeedoctor())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_seeDoctor[0]);" >Yes</span>		
	</td>
    <td width="15%"  colspan="1" align="left" class="style76">
		<input type="radio" name="radio_seeDoctor" value="no"  <%=(Utility.escapeNull(intakeA.getRadioSeedoctor())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_seeDoctor[1]);" >No</span>
	</td>
  </tr>		
  
  <tr>
    <td  colspan="7" align="left" class="style76">
		2. Do you have any health issue that we should know about in the event of an emergency?	</td>
    <td  colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_healthIssue" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioHealthissue())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_healthIssue[0]);" >Yes</span>		
	</td>
    <td  colspan="1" align="left" class="style76">
		<input type="radio" name="radio_healthIssue" value="no"  <%=(Utility.escapeNull(intakeA.getRadioHealthissue())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_healthIssue[1]);" >No</span>
	</td>
  </tr>		
  <tr>
	<td colspan="9" class="style76">
		If yes, give details <br><br>
		<textArea name="healthIssueDetails"  cols="75"><%=Utility.escapeNull(intakeA.getHealthIssueDetails())%></textArea>
	</td>
  </tr>
  <tr>
	<td colspan="9" class="style76">
		3. Do you have any of the following (read list and check all that apply): 
	</td>
  </tr>
  <tr>
    <td  colspan="4" align="left" class="style76">
		<input type="checkbox" name="cbox_hasDiabetes" value="Y" <%=(Utility.escapeNull(intakeA.getCboxHasdiabetes())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_hasDiabetes);" >Diabetes. If yes --></span>	
	</td>
    <td  colspan="2"  align="left" class="style76">
		<input type="checkbox" name="cbox_insulin" value="Y" <%=(Utility.escapeNull(intakeA.getCboxInsulin())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_insulin);" >Insulin dependent?</span>		
	</td>
    <td  colspan="1"  align="left" class="style76">
		<input type="checkbox" name="cbox_epilepsy" value="Y" <%=(Utility.escapeNull(intakeA.getCboxEpilepsy())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_epilepsy);" >Epilepsy</span>		
	</td>
    <td  colspan="2"  align="left" class="style76">
		<input type="checkbox" name="cbox_bleeding" value="Y" <%=(Utility.escapeNull(intakeA.getCboxBleeding())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_bleeding);" >Bleeding Disorder</span>		
	</td>
  </tr>

  <tr>
    <td  colspan="2" align="left" class="style76">
		<input type="checkbox" name="cbox_hearingImpair"  value="Y" <%=(Utility.escapeNull(intakeA.getCboxHearingimpair())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_hearingImpair);" >Hearing Impairment</span>	
	</td>
    <td width="19%"  colspan="1"  align="left" class="style76">
		<input type="checkbox" name="cbox_visualImpair"  value="Y" <%=(Utility.escapeNull(intakeA.getCboxVisualimpair())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_visualImpair);" >Visual Impairment</span>		
	</td>
    <td  colspan="6"  align="left" class="style76">
		<input type="checkbox" name="cbox_mobilityImpair"  value="Y" <%=(Utility.escapeNull(intakeA.getCboxMobilityimpair())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_mobilityImpair);" >Mobility impairment. Give details: </span>		
		<input type="text" name="mobilityImpair" size="35" value="<%=Utility.escapeNull(intakeA.getMobilityImpair())%>">
				
	</td>
  </tr>
  <tr>
    <td  colspan="5" align="left" class="style76">
		4. Do you have any other health concern that you wish to share with us? 
	</td>
    <td colspan="2"  align="left" class="style76">
		<input type="radio" name="radio_otherHealthConcern" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioOtherhealthconcern())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_otherHealthConcern[0]);" >Yes</span>
	</td>
    <td  colspan="2" align="left" class="style76">
		<input type="radio" name="radio_otherHealthConcern" value="no"  <%=(Utility.escapeNull(intakeA.getRadioOtherhealthconcern())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_otherHealthConcern[1]);" >No</span>
		
	</td>
  </tr>		
  <tr>
	      <td colspan="9"  align="left" class="style76"> If yes, give details including duration of problem, 
            any medications taken (include over the counter meds), outcome, etc <br><br>
		<textArea name="otherHealthConerns"  cols="75"><%=Utility.escapeNull(intakeA.getOtherHealthConerns())%></textArea>
	</td>
  </tr>
  <tr>
    <td  colspan="9"  align="left" class="style76">Medications  </td>
  </tr>		
  <tr>
    <td  colspan="5" align="left" class="style76">
		1. Are you presently taking any medications?
	</td>
    <td colspan="2"  align="left" class="style76">
		<input type="radio" name="radio_takeMedication" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioTakemedication())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_takeMedication[0]);" >Yes</span>
	</td>
    <td  colspan="2" align="left" class="style76">
		<input type="radio" name="radio_takeMedication" value="no"  <%=(Utility.escapeNull(intakeA.getRadioTakemedication())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_takeMedication[1]);" >No</span>
	</td>
  </tr>		

  <tr>
    <td  colspan="9" align="left" class="style76">
		Name(s) of Medication(s)
		<input type="text" name="namesOfMedication"  size="70"  value="<%=Utility.escapeNull(intakeA.getNamesOfMedication())%>">
		
	</td>
  </tr>		
  <tr>
    <td  colspan="5" align="left" class="style76">
		2. Do you need help obtaining medication?
	</td>
    <td colspan="2"  align="left" class="style76">
		<input type="radio" name="radio_helpObtainMedication" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioHelpobtainmedication())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_helpObtainMedication[0]);" >Yes</span>
	</td>
    <td  colspan="2" align="left" class="style76">
		<input type="radio" name="radio_helpObtainMedication" value="no"  <%=(Utility.escapeNull(intakeA.getRadioHelpobtainmedication())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_helpObtainMedication[1]);" >No</span>
	</td>
  </tr>		

  <tr>
    <td  colspan="9" align="left" class="style76">
		If yes, give details <br><br>
		<textArea name="helpObtainMedication"  cols="75"><%=Utility.escapeNull(intakeA.getHelpObtainMedication())%></textArea>
		
	</td>
  </tr>		

  <tr>
    <td  colspan="5" align="left" class="style76">
		3. Are you allergic to any medications? 
	</td>
    <td colspan="2"  align="left" class="style76">
		<input type="radio" name="radio_allergicToMedication" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioAllergictomedication())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_allergicToMedication[0]);" >Yes</span>
	</td>
    <td  colspan="2" align="left" class="style76">
		<input type="radio" name="radio_allergicToMedication" value="no"  <%=(Utility.escapeNull(intakeA.getRadioAllergictomedication())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_allergicToMedication[1]);" >No</span>
	</td>
  </tr>		

  <tr>
    <td  colspan="9" align="left" class="style76">
		If yes, give medication names:<br><br>
		<input type="text" name="allergicToMedicationName"  size="75"  value="<%=Utility.escapeNull(intakeA.getAllergicToMedicationName())%>"> <br><br>
		If yes, give reaction (hives, rash, anaphylaxis=breathing problems, shortness of breath, itching swelling of mouth and throat)
		<textArea name="reactionToMedication"  cols="75"><%=Utility.escapeNull(intakeA.getReactionToMedication())%></textArea>
		
	</td>
  </tr>		

  <tr>
    <td  colspan="9"  align="left" class="style76">Mental Health </td>
  </tr>		
  <tr>
    <td  colspan="5" align="left" class="style76">
		1. Do you have any mental health concerns that you wish to share with us?
	</td>
    <td colspan="2"  align="left" class="style76">
		<input type="radio" name="radio_mentalHealthConcerns" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioMentalhealthconcerns())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_mentalHealthConcerns[0]);" >Yes</span>
	</td>
    <td  colspan="2" align="left" class="style76">
		<input type="radio" name="radio_mentalHealthConcerns" value="no"  <%=(Utility.escapeNull(intakeA.getRadioMentalhealthconcerns())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_mentalHealthConcerns[1]);" >No</span>
	</td>
  </tr>		

  <tr>
    <td  colspan="9" align="left" class="style76">
		If yes, give diagnosis (schizophrenia, depression etc, and date diagnosed)<br><br>
		<textArea name="mentalHealthConcerns"  cols="75"><%=Utility.escapeNull(intakeA.getMentalHealthConcerns())%></textArea>
		
	</td>
  </tr>		
</table>

<table width="95%" border="1">
  <tr>
     <td  colspan="6" align="center" class="style51">6. SURVEY MODULE - ACCESS TO HEALTH CARE</td>
  </tr>		
  <tr>
    <td  colspan="6"  align="left" class="style61">
		Please read the following statement to the client:"To better provide care at this agency, 
		I'd like to ask you a few more questions about your access to health care"  &nbsp;&nbsp;	
		<input type="checkbox" name="cbox_isStatement6Read"  
	  	value="Y" <%=(Utility.escapeNull(intakeA.getCboxIsstatement6read())).equalsIgnoreCase("Y")?"checked":""%> >

	</td>
  </tr>		
  <tr>
    <td  colspan="6"  align="left" class="style76">
		1. Not counting when you were an overnight patient, in the past 12 months, 
		how many times have you seen a general practitioner or family physician about your physical, emotional or mental health? 
		<input type="text" name="frequencyOfSeeingDoctor" value="<%=Utility.escapeNull(intakeA.getFrequencyOfSeeingDoctor())%>">
	</td>
  </tr>	
  <tr>	
  	<td colspan="6"  align="left" class="style76">
		2. Where did the most recent contact take place? (Read list, Mark one only.)	
	</td>
  </tr>
  <tr>
    <td  colspan="2" align="left" class="style76">
		<input type="checkbox" name="cbox_visitWalkInClinic" value="Y" <%=(Utility.escapeNull(intakeA.getCboxVisitwalkinclinic())).equalsIgnoreCase("Y")?"checked":""%>>
 		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_visitWalkInClinic);" >Walk-in clinic</span> <br>
		<input type="checkbox" name="cbox_visitHealthCenter" value="Y" <%=(Utility.escapeNull(intakeA.getCboxVisithealthcenter())).equalsIgnoreCase("Y")?"checked":""%>>
 		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_visitHealthCenter);" >Community health centre</span>
	</td>
    <td colspan="2"  align="left" class="style76">
		<input type="checkbox" name="cbox_visitEmergencyRoom" value="Y" <%=(Utility.escapeNull(intakeA.getCboxVisitemergencyroom())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_visitEmergencyRoom);" >Hospital emergency room</span><br>
		<input type="checkbox" name="cbox_visitOthers" value="Y" <%=(Utility.escapeNull(intakeA.getCboxVisitothers())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_visitOthers);" >Other (specify)</span>
		
	</td>
    <td   colspan="2"  align="left" valign="top" class="style76">
		<input type="checkbox" name="cbox_visitHealthOffice" value="Y" <%=(Utility.escapeNull(intakeA.getCboxVisithealthoffice())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_visitHealthOffice);" >Health Professionals office</span>
		
	</td>
	
  </tr>		
  <tr>
    <td  colspan="4" align="left" class="style76">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;If more than 1 contact and not in emergency room: <br>	
		3. Would you be able to see this doctor again if you needed to see a doctor?	
	</td>
    <td width="17%" colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_seeSameDoctor" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioSeesamedoctor())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_seeSameDoctor[0]);" >Yes</span>
	</td>
    <td width="23%"  colspan="1" align="left" class="style76">
		<input type="radio" name="radio_seeSameDoctor" value="no"  <%=(Utility.escapeNull(intakeA.getRadioSeesamedoctor())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_seeSameDoctor[1]);" >No</span>
	</td>
  </tr>	
  <tr>
    <td  colspan="6"  align="left" class="style76">
		4. In the past 12 months, how many times have you seen a physician in a hospital Emergency room?  
		<input type="text" name="frequencyOfSeeingEmergencyRoomDoctor"  size="15" value="<%=Utility.escapeNull(intakeA.getFrequencyOfSeeingEmergencyRoomDoctor())%>">
	</td>
  </tr>	

  <tr>
    <td  colspan="4" align="left" class="style76">
		5. During the past 12 months, was there ever a time when you needed health care or advice but did not receive it?	
	</td>
    <td colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_didNotReceiveHealthCare" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioDidnotreceivehealthcare())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_didNotReceiveHealthCare[0]);" >Yes </span><br>
		
		<input type="radio" name="radio_didNotReceiveHealthCare" value="dontKnow"  <%=(Utility.escapeNull(intakeA.getRadioDidnotreceivehealthcare())).equalsIgnoreCase("dontKnow")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_didNotReceiveHealthCare[1]);" >Don't know</span>		
	</td>
    <td width="23%"  colspan="1" align="left" class="style76">
		<input type="radio" name="radio_didNotReceiveHealthCare" value="no"  <%=(Utility.escapeNull(intakeA.getRadioDidnotreceivehealthcare())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_didNotReceiveHealthCare[2]);" >No </span><br>
		<input type="radio" name="radio_didNotReceiveHealthCare" value="refuse"  <%=(Utility.escapeNull(intakeA.getRadioDidnotreceivehealthcare())).equalsIgnoreCase("refuse")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_didNotReceiveHealthCare[3]);" >Refuse</span><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; to answer		
	</td>
  </tr>		

  <tr>
	<td colspan="6" align="left" class="style76">
		If yes, Thinking of the most recent time, what was the type of care that was needed? (Do not read list. Mark all that apply)
	</td>
  </tr>
  
  <tr>
    <td  colspan="2" align="left" class="style76">
		<input type="checkbox" name="cbox_treatPhysicalHealth" value="Y" <%=(Utility.escapeNull(intakeA.getCboxTreatphysicalhealth())).equalsIgnoreCase("Y")?"checked":""%>>
        <span onClick="javascript:clickCheckBox(document.forms[0].cbox_treatPhysicalHealth);" >Treatment of a physical </span><br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;health problem <br>
		<input type="checkbox" name="cbox_treatMentalHealth" value="Y" <%=(Utility.escapeNull(intakeA.getCboxTreatmentalhealth())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_treatMentalHealth);" >Treatment of an emotional or </span><br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;mental health problem		
	</td>
    <td colspan="2"  align="left" class="style76">
		<input type="checkbox" name="cbox_regularCheckup" value="Y" <%=(Utility.escapeNull(intakeA.getCboxRegularcheckup())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_regularCheckup);" >A regular check-up</span><br>
		<input type="checkbox" name="cbox_treatOtherReasons" value="Y" <%=(Utility.escapeNull(intakeA.getCboxTreatotherreasons())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_treatOtherReasons);" >Any other reason (specify)</span><br>
		<input type="text" name="treatOtherReasons" size="35" value="<%=Utility.escapeNull(intakeA.getTreatOtherReasons())%>">
		
	</td>
    <td   colspan="2"  align="left" valign="top" class="style76">
		<input type="checkbox" name="cbox_treatInjury" value="Y" <%=(Utility.escapeNull(intakeA.getCboxTreatinjury())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_treatInjury);" >Care of injury</span>
	</td>
  </tr>		
  <tr>
	<td colspan="6" align="left"  class="style76">
		6. If you had a physical, emotional or mental health problem that you needed help with, where would you go for help? (Read list. Mark one only)
	</td>
  </tr>
 
  <tr>
    <td  colspan="2" align="left" class="style76">
		<input type="checkbox" name="cbox_goToWalkInClinic" value="Y" <%=(Utility.escapeNull(intakeA.getCboxGotowalkinclinic())).equalsIgnoreCase("Y")?"checked":""%>>
           <span onClick="javascript:clickCheckBox(document.forms[0].cbox_goToWalkInClinic);" >Walk-in clinic</span><br>
		<input type="checkbox" name="cbox_goToHealthCenter" value="Y" <%=(Utility.escapeNull(intakeA.getCboxGotohealthcenter())).equalsIgnoreCase("Y")?"checked":""%>>
		   <span onClick="javascript:clickCheckBox(document.forms[0].cbox_goToHealthCenter);" >Community health centre</span>
	</td>
    <td colspan="2"  align="left" class="style76">
		<input type="checkbox" name="cbox_goToEmergencyRoom" value="Y" <%=(Utility.escapeNull(intakeA.getCboxGotoemergencyroom())).equalsIgnoreCase("Y")?"checked":""%>>
            <span onClick="javascript:clickCheckBox(document.forms[0].cbox_goToEmergencyRoom);" >Hospital emergency room</span><br>
		<input type="checkbox" name="cbox_goToOthers" value="Y" <%=(Utility.escapeNull(intakeA.getCboxGotoothers())).equalsIgnoreCase("Y")?"checked":""%>>
			<span onClick="javascript:clickCheckBox(document.forms[0].cbox_goToOthers);" >Other (specify)</span><br>
		<input type="text" name="goToOthers" size="35" value="<%=Utility.escapeNull(intakeA.getGoToOthers())%>">
		
	</td>
    <td   colspan="2"  align="left" valign="top" class="style76">
		<input type="checkbox" name="cbox_HealthOffice" value="Y" <%=(Utility.escapeNull(intakeA.getCboxHealthoffice())).equalsIgnoreCase("Y")?"checked":""%>>
			<span onClick="javascript:clickCheckBox(document.forms[0].cbox_HealthOffice);" >Health Professionals office</span>
	</td>
  </tr>		
  <tr>
    <td  colspan="4" align="left" class="style76">
		7. Do you have an appointment to see a general practitioner or family doctor in the next 3 months?
	</td>
    <td width="17%" colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_appmtSeeDoctorIn3Mths" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioAppmtseedoctorin3mths())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_appmtSeeDoctorIn3Mths[0]);" >Yes</span>
	</td>
    <td width="23%"  colspan="1" align="left" class="style76">
		<input type="radio" name="radio_appmtSeeDoctorIn3Mths" value="no"  <%=(Utility.escapeNull(intakeA.getRadioAppmtseedoctorin3mths())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_appmtSeeDoctorIn3Mths[1]);" >(A)No</span>
	</td>
  </tr>	
  <tr>
    <td  colspan="4" align="left" class="style76">
		8. Do you feel you would benefit from having a regular doctor or do you need a regular doctor?
	</td>
    <td width="17%" colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_needRegularDoctor" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioNeedregulardoctor())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_needRegularDoctor[0]);" >(B)Yes</span>
	</td>
    <td width="23%"  colspan="1" align="left" class="style76">
		<input type="radio" name="radio_needRegularDoctor" value="no"  <%=(Utility.escapeNull(intakeA.getRadioNeedregulardoctor())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_needRegularDoctor[1]);" >No</span>
	</td>
  </tr>	
  <tr>
    <td  colspan="4" align="left" class="style76">
		9. Would you object to having an appointment with a regular doctor in the next 4 weeks?
	</td>
    <td width="17%" colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_objectToRegularDoctorIn4Wks" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioObjecttoregulardoctorin4wks())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_objectToRegularDoctorIn4Wks[0]);" >Yes</span>
	</td>
    <td width="23%"  colspan="1" align="left" class="style76">
		<input type="radio" name="radio_objectToRegularDoctorIn4Wks" value="no"  <%=(Utility.escapeNull(intakeA.getRadioObjecttoregulardoctorin4wks())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_objectToRegularDoctorIn4Wks[1]);" >(C)No</span>
	</td>
  </tr>	
  <tr>
    <td  colspan="2" align="left" class="style76">
		10. How would you rate your overall health?
	</td>
    <td width="19%"  colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_rateOverallHealth" value="poor"  <%=(Utility.escapeNull(intakeA.getRadioRateoverallhealth())).equalsIgnoreCase("poor")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_rateOverallHealth[0]);" >Poor</span>
	</td>
    <td width="19%"   colspan="1" align="left" class="style76">
		<input type="radio" name="radio_rateOverallHealth" value="fair"  <%=(Utility.escapeNull(intakeA.getRadioRateoverallhealth())).equalsIgnoreCase("fair")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_rateOverallHealth[1]);" >Fair</span>
	</td>
    <td  colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_rateOverallHealth" value="good"  <%=(Utility.escapeNull(intakeA.getRadioRateoverallhealth())).equalsIgnoreCase("good")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_rateOverallHealth[2]);" >Good</span>
	</td>
    <td  colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_rateOverallHealth" value="excellent"  <%=(Utility.escapeNull(intakeA.getRadioRateoverallhealth())).equalsIgnoreCase("excellent")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_rateOverallHealth[3]);" >Excellent</span>
	</td>
  </tr>	

  <tr>
    <td  colspan="6" align="left" class="style76">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		Intake Staff: Has a "YES" been selected for an A, B, AND C question?<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		If yes, then ask:  "Would you be willing to speak to a researcher about a study on accessing primary health care?"
	</td>
  </tr>	
  <tr> 
    <td   colspan="2"  align="left" class="style76">
		<input type="radio" name="radio_speakToResearcher" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioSpeaktoresearcher())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_speakToResearcher[0]);" >Yes</span>
	</td>
    <td    colspan="4" align="left" class="style76">
		<input type="radio" name="radio_speakToResearcher" value="no"  <%=(Utility.escapeNull(intakeA.getRadioSpeaktoresearcher())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_speakToResearcher[1]);" >No</span>
	</td>
  </tr>	
</table>


<table width="95%" border="1">
  <tr>
    <td  colspan="4" align="center" class="style51">7. EMERGENCY CONTACT</td>
  </tr>		
  <tr>
    <td  colspan="4"  align="left" class="style76">
		Please give the name/ address/ phone # of someone who may be contacted in the event of an emergency?
	</td>
  </tr>		
  <tr>
    <td  colspan="2"  align="left" class="style76">
		Name:
		<input type="text" name="contactName" value="<%=Utility.escapeNull(intakeA.getContactName())%>">
	</td>
    <td  colspan="2"  width="50%"  align="left" class="style76">
	    Phone #:
		<input type="text" name="contactPhone" value="<%=Utility.escapeNull(intakeA.getContactPhone())%>">
	</td>

  </tr>		
  <tr>
    <td  colspan="4" align="left" class="style76">
		Address:
		<input type="text" name="contactAddress" value="<%=Utility.escapeNull(intakeA.getContactAddress())%>">
	</td>
  </tr>		
  <tr>
    <td  colspan="4" align="left" class="style76">
		What relationship is this person to you? (friend, sister, next-of-kin, etc.)	
		<input type="text" name="contactRelationship" value="<%=Utility.escapeNull(intakeA.getContactRelationship())%>">
	</td>
  </tr>	
</table>

<table width="95%" border="0">
  <tr>
    <td  class="style76">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;For Staff Use Only
      </td>
  </tr>
</table>

<table width="95%" border="1">
  <tr>	
  	<td colspan="4" align="center" class="style51">
		8. STAFF RATINGS AND IDENTIFIED ISSUES	
	</td>
  </tr>
  <tr>	
  	<td colspan="4" align="left" class="style76">
		Based on my own observations and from information from other members on my team:	
	</td>
  </tr>
  <tr>	
  	<td colspan="4" align="left" class="style76">
		I think this person currently has uncontrolled severe mental illness (like schizophrenia, bipolar disorder)	
	</td>
  </tr>
  
  <tr>
    <td  colspan="1" width="22%" align="left" class="style76">&nbsp;
			
	</td>
    <td width="26%" colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_hasMentalIllness" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioHasmentalillness())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_hasMentalIllness[0]);" >Yes</span>
	</td>
    <td width="28%"   colspan="1"  align="left" valign="top" class="style76">
		<input type="radio" name="radio_hasMentalIllness" value="uncertain"  <%=(Utility.escapeNull(intakeA.getRadioHasmentalillness())).equalsIgnoreCase("uncertain")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_hasMentalIllness[1]);" >Uncertain</span>
	</td>
    <td width="24%"   colspan="1"  align="left" valign="top" class="style76">
		<input type="radio" name="radio_hasMentalIllness" value="no"  <%=(Utility.escapeNull(intakeA.getRadioHasmentalillness())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_hasMentalIllness[2]);" >No</span>
	</td>
  </tr>		

  <tr>	
  	<td colspan="4" align="left" class="style76">
		I am concerned that this person has severe problems from uncontrolled drinking	
	</td>
  </tr>
  
  <tr>
    <td  colspan="1" width="22%" align="left" class="style76">&nbsp;
			
	</td>
    <td width="26%" colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_hasDrinkingProblem" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioHasdrinkingproblem())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_hasDrinkingProblem[0]);" >Yes</span>
		
	</td>
    <td width="28%"   colspan="1"  align="left" valign="top" class="style76">
		<input type="radio" name="radio_hasDrinkingProblem" value="uncertain"  <%=(Utility.escapeNull(intakeA.getRadioHasdrinkingproblem())).equalsIgnoreCase("uncertain")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_hasDrinkingProblem[1]);" >Uncertain</span>
		
	</td>
    <td width="24%"   colspan="1"  align="left" valign="top" class="style76">
		<input type="radio" name="radio_hasDrinkingProblem" value="no"  <%=(Utility.escapeNull(intakeA.getRadioHasdrinkingproblem())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_hasDrinkingProblem[2]);" >No</span>
		
	</td>
  </tr>	
  <tr>	
  	<td colspan="4" align="left" class="style76">
		I am concerned that this person has severe problems from uncontrolled drug use	
	</td>
  </tr>
  
  <tr>
    <td  colspan="1" width="22%" align="left" class="style76">&nbsp;
			
	</td>
    <td width="26%" colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_hasDrugProblem" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioHasdrugproblem())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_hasDrugProblem[0]);" >Yes</span>
		
	</td>
    <td width="28%"   colspan="1"  align="left" valign="top" class="style76">
		<input type="radio" name="radio_hasDrugProblem" value="uncertain"  <%=(Utility.escapeNull(intakeA.getRadioHasdrugproblem())).equalsIgnoreCase("uncertain")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_hasDrugProblem[1]);" >Uncertain</span>
		
	</td>
    <td width="24%"   colspan="1"  align="left" valign="top" class="style76">
		<input type="radio" name="radio_hasDrugProblem" value="no"  <%=(Utility.escapeNull(intakeA.getRadioHasdrugproblem())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_hasDrugProblem[2]);" >No</span>
	</td>
  </tr>	
  
  
  <tr>	
  	<td colspan="4" align="left" class="style76">
		I am concerned that this person has severe problems from an uncontrolled physical health problem	
	</td>
  </tr>
  
  <tr>
    <td  colspan="1" width="22%" align="left" class="style76">&nbsp;
			
	</td>
    <td width="26%" colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_hasHealthProblem" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioHashealthproblem())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_hasHealthProblem[0]);" >Yes</span>
		
	</td>
    <td width="28%"   colspan="1"  align="left" valign="top" class="style76">
		<input type="radio" name="radio_hasHealthProblem" value="uncertain"  <%=(Utility.escapeNull(intakeA.getRadioHashealthproblem())).equalsIgnoreCase("uncertain")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_hasHealthProblem[1]);" >Uncertain</span>
		
	</td>
    <td width="24%"   colspan="1"  align="left" valign="top" class="style76">
		<input type="radio" name="radio_hasHealthProblem" value="no"  <%=(Utility.escapeNull(intakeA.getRadioHashealthproblem())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_hasHealthProblem[2]);" >No</span>
	</td>
  </tr>	
  <tr>	
  	<td colspan="4" align="left" class="style76">
		I am concerned that this person is handicapped or disabled by severe behaviour problems 	
	</td>
  </tr>
  
  <tr>
    <td  colspan="1" width="22%" align="left" class="style76">&nbsp;
			
	</td>
    <td width="26%" colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_hasBehaviorProblem" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioHasbehaviorproblem())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_hasBehaviorProblem[0]);" >Yes</span>
		
	</td>
    <td width="28%"   colspan="1"  align="left" valign="top" class="style76">
		<input type="radio" name="radio_hasBehaviorProblem" value="uncertain"  <%=(Utility.escapeNull(intakeA.getRadioHasbehaviorproblem())).equalsIgnoreCase("uncertain")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_hasBehaviorProblem[1]);" >Uncertain</span>
		
	</td>
    <td width="24%"   colspan="1"  align="left" valign="top" class="style76">
		<input type="radio" name="radio_hasBehaviorProblem" value="no"  <%=(Utility.escapeNull(intakeA.getRadioHasbehaviorproblem())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_hasBehaviorProblem[2]);" >No</span>
	</td>
  </tr>	
  <tr>	
  	<td colspan="4" align="left" class="style76">
		I think this person will need our (Agency's) services for more than 60 days	
	</td>
  </tr>
  
  <tr>
    <td  colspan="1" width="22%" align="left" class="style76">&nbsp;
			
	</td>
    <td width="26%" colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_needSeatonService" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioNeedseatonservice())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_needSeatonService[0]);" >Yes</span>
		
	</td>
    <td width="28%"   colspan="1"  align="left" valign="top" class="style76">
		<input type="radio" name="radio_needSeatonService" value="uncertain"  <%=(Utility.escapeNull(intakeA.getRadioNeedseatonservice())).equalsIgnoreCase("uncertain")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_needSeatonService[1]);" >Uncertain</span>
		
	</td>
    <td width="24%"   colspan="1"  align="left" valign="top" class="style76">
		<input type="radio" name="radio_needSeatonService" value="no"  <%=(Utility.escapeNull(intakeA.getRadioNeedseatonservice())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_needSeatonService[2]);" >No</span>
	</td>
  </tr>	
  
  <tr>	
  	<td colspan="4" align="left" class="style76">
		I am concerned that this person is handicapped or disabled by severe behaviour problems 	
	</td>
  </tr>
  <tr>	
  	<td colspan="4" align="left" class="style76">
		PRINT AND SIGN NAME:	
	</td>
  </tr>
</table>  
<table width="95%" border="1">
  <tr>	
  	<td colspan="6" align="center" class="style51">
		9. ORIENTATION TO AGENCY	
	</td>
  </tr>
  
  <tr>
    <td  colspan="1" width="25%" align="left" class="style76">
		Tour given?
	</td>
    <td width="10%" colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_seatonTour" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioSeatontour())).equalsIgnoreCase("yes")?"checked":""%> >
		<span onClick="javascript:clickRadio(document.forms[0].radio_seatonTour[0]);" >Yes</span>
		
	</td>
    <td    colspan="4"  align="left" valign="top" class="style76">
		<input type="radio" name="radio_seatonTour" value="no"   <%=(Utility.escapeNull(intakeA.getRadioSeatontour())).equalsIgnoreCase("no")?"checked":""%> >
		<span onClick="javascript:clickRadio(document.forms[0].radio_seatonTour[1]);" >No (If no, give reason)</span>
		<input type="text" name="seatonNotToured" size="55" value="<%=Utility.escapeNull(intakeA.getSeatonNotToured())%>">
	</td>
  </tr>	
  
  <tr>
    <td  colspan="1" width="25%" align="left" class="style76">
		Information pamphlet issued?
	</td>
    <td width="10%" colspan="1"  align="left" class="style76">
		<input type="radio" name="radio_pamphletIssued" value="yes"  <%=(Utility.escapeNull(intakeA.getRadioPamphletissued())).equalsIgnoreCase("yes")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_pamphletIssued[0]);" >Yes</span>
		
	</td>
    <td    colspan="4"  align="left" valign="top" class="style76">
		<input type="radio" name="radio_pamphletIssued" value="no"  <%=(Utility.escapeNull(intakeA.getRadioPamphletissued())).equalsIgnoreCase("no")?"checked":""%>>
		<span onClick="javascript:clickRadio(document.forms[0].radio_pamphletIssued[1]);" >No (If no, give reason)</span>
		<input type="text" name="pamphletNotIssued" size="55" value="<%=Utility.escapeNull(intakeA.getPamphletNotIssued())%>">
	</td>
  </tr>	
  	
</table>

<table width="95%" border="1">
  <tr>	
  	<td colspan="6" align="center" class="style51">
		10. COMMENTS/SUMMARY	
	</td>
  </tr>
  <tr>	
  	<td colspan="6" align="center" class="style76">
		<textArea name="summary"  cols="95" rows="7"><%=Utility.escapeNull(intakeA.getSummary())%></textArea>
	</td>
  </tr>

</table>
<table width="95%" border="1">
  <tr>	
  	<td colspan="6" align="center" class="style51">
		COMPLETED BY	
	</td>
  </tr>
  <tr>
    <td  colspan="4"  align="left" class="style76">
		Completed by: (sign and print name)<br><br>
		<input type="text" name="completedBy" size="45" value="<%=Utility.escapeNull(intakeA.getCompletedBy())%>">
	</td>
    <td width="28%"   colspan="1"  align="center" class="style76">
		Time Assessment Completed:		
	</td>
    <td width="22%"  colspan="1"  align="left" valign="middle"  STYLE="text-align:right"  class="style76">
		<input type="text" name="assessCompleteTime" size="15" value="<%=Utility.escapeNull(intakeA.getAssessCompleteTime())%>">
		am/pm
	</td>
  </tr>	

</table>

<table width="95%" border="1">
  <tr>	
  	<td colspan="3" align="center" class="style51">
		11. TRIAGE- REFERRAL TO A PROGRAM	
	</td>
  </tr>
  <tr>	
  	<td colspan="3" align="left" class="style76">
		Based on my own observations (and from information from other members on my team) 
		I believe this client may be appropriate for one of the following programs:
	</td>
  </tr>
  
  <tr>
    <td width="36%"  colspan="1"  align="center" class="style76">
		Admission Criteria
	</td>
    <td width="33%" colspan="1"  align="center" class="style76">
		Exclusion Criteria:	
	</td>
    <td width="31%" colspan="1"  align="center" class="style76">
		Triage Contact #s:	
	</td>
  </tr>	

  <tr>
    <td  colspan="1"  align="left"  valign="top" class="style76">
		<input type="checkbox" name="cbox_pamphletIssued" value="Y" <%=(Utility.escapeNull(intakeA.getCboxPamphletissued())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_pamphletIssued);" >Emergency Hostel Program</span>
		<ul>
			<li>Homeless man</li>			
			<li>No other current resources</li>
			<li>No source of income</li>
			<li>Willing to follow case plan</li>
		</ul>
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
		<ul>
			<li>Violence or illicit drug use may result in client being barred from the Hostel</li>			
		</ul>
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake: <br>
		(416) 392-5529 <br>
		(416) 338-3196 <br>
		(416) 338-3197<br><br>
		
		Steering Committee Member:<br>
		Maurice Jefferson (416) 392-5531	
		
	</td>
  </tr>	

  <tr>
    <td  colspan="1"  align="left"  valign="top" class="style76">
		<input type="checkbox" name="cbox_hostel" value="Y" <%=(Utility.escapeNull(intakeA.getCboxHostel())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_hostel);" >Hostel - Fusion of Care Team</span>
		<ul>
			<li>	Emergency Hostel Program client</li>			
			<li>	High mental or physical health needs</li>
		</ul>
	</td>
    <td colspan="1"  align="left" class="style76">&nbsp;
			
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake: <br>
		(416) 338-3311<br><br>
		
		Steering Committee Member:<br>
		Shawn Yoder (416) 392-6706		
	</td>
  </tr>	
  <tr>
    <td  colspan="1"  align="left"  valign="top" class="style76">
		<input type="checkbox" name="cbox_rotaryClub" value="Y" <%=(Utility.escapeNull(intakeA.getCboxRotaryclub())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_rotaryClub);" >Rotary Club of Toronto Infirmary</span>
		<ul>
			<li>	Client is to be discharged from hospital and requires: <br>
			(i) Frequent physician or nursing follow-up and/or  <br>
			(ii) Rehabilitation to return to previous level of functioning.
			</li>			
		</ul>
		Client may be admitted to the Infirmary if he exhibits one or more of the following health conditions:
		<ul>
			<li>Requires follow-up care (post-operative care and wound care)</li>
			<li>Uncontrolled/poorly controlled chronic illness such as diabetes, cirrhosis, seizures, CHF and HIV</li>
			<li>Terminal illness that requires palliative care for comfort</li>
		</ul>
				
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
		<ul>
			<li>Client unable to transfer safely (move from bed to chair unassisted)</li>
			<li>Client is incontinent and using a wheelchair </li>
		</ul>
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake: <br>
		(416) 392-5598<br><br>
		
		Steering Committee Member:<br>
		Karen Smith (416) 392-5598
	</td>
  </tr>	

  <tr>
    <td  colspan="1"  align="left" valign="top" class="style76">
		<input type="checkbox" name="cbox_annexHarm" value="Y" <%=(Utility.escapeNull(intakeA.getCboxAnnexharm())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_annexHarm);" >Annex Harm Reduction Program</span>
		<ul>
			<li>Chronic alcohol, substance use (including non-palatable substances)</li>
			<li>Severe uncontrolled physical illness </li>
			<li>Severe uncontrolled mental illness</li>
			<li>Severe uncontrolled behaviour problems</li>
			<li>Chronically homeless</li>
		</ul>
				
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
		<ul>
			<li>Client wishing to maintain abstinence may not do well in this environment</li>
		</ul>
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake: <br>
		(416) 392-5519<br><br>
		
		Steering Committee Member: <br>
		Ken Mendonca (416) 392-5522
	</td>
  </tr>	

  <tr>
    <td  colspan="1"  align="left" valign="top" class="style76">
		<input type="checkbox" name="cbox_longTermProgram" value="Y" <%=(Utility.escapeNull(intakeA.getCboxLongtermprogram())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_longTermProgram);" >Long Term Program</span>
		<ul>
			<li>Age 65+ - refer direct from Reception </li>
		</ul>
			OR
		<ul>
			<li>Age 50+</li>
			<li>Willing and able to pay rent </li>
		</ul>
			OR Any age and one of the following criteria:
		<ul>
			<li>On pension</li>
			<li>Physically or socially vulnerable</li>
			<li>Mental health issues</li>
			<li>Physical disability</li>
			<li>Short-term disability (non-ambulatory)</li>
		</ul>
				
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
		<ul>
			<li>Client poses significant risk to residents of long term program</li>
			<li>History of aggressive or predatory behaviour</li>
		</ul>
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake:<br>
		(416) 392-6049 <br>
		(416) 338-3175<br><br>
		
		Steering Committee Member:<br>
		Mark Headley (416) 392-5543
	</td>
  </tr>	

  <tr>
    <td  colspan="1"  align="left" valign="top" class="style76">
		<input type="checkbox" name="cbox_birchmountResidence" value="Y" <%=(Utility.escapeNull(intakeA.getCboxBirchmountresidence())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_birchmountResidence);" >Birchmount Residence</span>
		<ul>
			<li>Age 55+ (men aged 50-55 considered)</li>
			<li>Chronic health issues</li>
			<li>Able to carry out the activities of daily living without staff assistance</li>
			<li>Able to climb stairs</li>
			<li>Able to behave in a reliable, predictable and respectful manner in the community</li>
			<li>Stable mental health</li>
			<li>Able to act appropriately and responsibly if consuming alcohol and willing to cooperate with a harm reduction approach</li>
			<li>Willing to take medication as prescribed by the attending physician and cooperative with medical and Nursing plan and attend medical appointments and procedures as required.</li>
			<li>Willing to be financially responsible and pay rent</li>
			<li>Has legal status in Canada</li>
			<li>Willing to cooperate with plan for future housing or appropriate care facility</li>
		</ul>
				
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
		<ul>
			<li>Recent history of incarceration, criminal activity, violence</li>
			<li>Requires bedside nursing or personal care</li>
			<li>Requires oxygen, IV or intrusive medical procedures of equipment</li>
			<li>Incontinence issues</li>
			<li>Danger to self or others</li>
			<li>Abuse of medications or street drugs</li>
		</ul>
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake: <br>
		(416) 392-6167<br><br>
		
		Steering Committee Member:<br>
		Carla O'Brien (416) 392-5543<br><br>
		Referrals to Birchmount are made by Long Term program.<br><br>
		
		N.B. Client must be resident of Seaton House Long Term program for more than 30 days prior to admission (some exceptions considered).	</td>
  </tr>	

  <tr>
    <td  colspan="1"  align="left" valign="top" class="style76">
		<input type="checkbox" name="cbox_oNeillHouse" value="Y" <%=(Utility.escapeNull(intakeA.getCboxOneillhouse())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_oNeillHouse);" >O'Neill House</span>
		<ul>
			<li>Severe problems related to street drug use (Crack cocaine)</li>
			<li>Client does not want treatment</li>
			<li>Barred from Hostel due to drug use</li>
			<li>2 or more incident reports for drug use</li>
		</ul>
				
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake <br>
		(416) 392-5407<br><br>
		
		Steering Committee Member:<br>
		Tom Fulgosi (416) 392-5436  
		
	</td>
  </tr>	

  <tr>
    <td  colspan="1"  align="left" valign="top" class="style76">
		<input type="checkbox" name="cbox_fortYork" value="Y" <%=(Utility.escapeNull(intakeA.getCboxFortyork())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_fortYork);" >Fort York Residence</span>
		<ul>
	        <li>Willing and able to work</li>
    	    <li>If employed: willing to save 60% of income</li>
        	<li>Willing to do volunteer work</li>
	        <li>Has ID (hard copy)</li>
    	    <li>Has up-to-date resume</li>
        	<li>Willing and able to follow case plan</li>
		</ul>
				
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
		Clients with substance use issues will be required to complete a Rehab program prior to admission to Fort York Residence.	
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake:<br>
		Mark Horne: # TBA*<br><br>
		
		Steering Committee Member#: TBA*		
	</td>
  </tr>	
  <tr>
    <td  colspan="1"  align="left" valign="top" class="style76">
		<input type="checkbox" name="cbox_downsviewDells" value="Y" <%=(Utility.escapeNull(intakeA.getCboxDownsviewdells())).equalsIgnoreCase("Y")?"checked":""%>>
		<span onClick="javascript:clickCheckBox(document.forms[0].cbox_downsviewDells);" >Downsview Dells</span>
		<ul>
            <li>Chemically dependent now wishing abstinence</li>
            <li>Willing and able to complete 30-day treatment at Humber River Regional Hospital</li>
		</ul>
				
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
		Disruptive behaviour	
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake: <br>
		(416) 392-5452<br><br>
		
		Steering Committee:<br>
		MemberDon Inglis (416) 392-5452		
	</td>
  </tr>	
</table>
<%
	if(viewIntakeA == null  ||  !viewIntakeA.equals("Y"))
	{
%>
<table width="95%" border="1">
  <tr>	
  	<td colspan="3" align="center" class="style51">
		12. Admission	
	</td>
  </tr>
  <tr>	
  	<td colspan="3" align="left" class="style76">
		You must admit this client into a program. If you cannot admit into a program in your domain,
		the client will be admitted to the "holding tank" program by default.
	</td>
  </tr>
  <tr>	
  	<td colspan="3" align="left" class="style76">
		Program: 
		<html:select property="admissionProgram">
			<html:option value="0">&nbsp;</html:option>
			<html:options collection="programDomain" property="id" labelProperty="name"/>
		</html:select>
	</td>
  </tr>
</table>
<%
	}
%>
<table width="95%" border="0">
  <tr>
          <td class="style76">&nbsp; </td>
  </tr>
</table>

<%
	if(viewIntakeA == null  ||  !viewIntakeA.equals("Y"))
	{
%>
<table width="95%" border="0">
<tr>
<td align="center" class="style76">
            <input type="submit" value="Save" onclick="javascript:return onSave();" />
            <input type="button" value="Print" onclick="javascript:return onPrint();"/>
</td>
</tr>
</table>
<%
	}//end of if(!viewIntakeA.equals("Y"))
	else
	{
%>
<table width="95%" border="0">
<tr>
<td align="center" class="style76">
     <input type="button" value="Update" onclick="javascript:onUpdate('<html:rewrite action="/PMmodule/IntakeA" />');" />
<!--     <input type="submit" value="Exit" onclick="javascript:return onExit();"/>  -->
     <input type="button" value="Print" onclick="javascript:return onPrint();"/>
</td>
</tr>
</table>

<%
	}//end of if(viewIntakeA.equals("Y"))
%>

<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->

</td>
</tr>
</table>

</html:form>
</body>
</html:html>
