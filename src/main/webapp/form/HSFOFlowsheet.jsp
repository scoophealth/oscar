<%--

    Copyright (C) 2007  Heart & Stroke Foundation
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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="oscar.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>HSFO Registration Template</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script language="JavaScript" type="text/javascript">
 function popupXmlMsg(path){
 	window.open(path,"xmlMsg","location=1,status=1,resizable=1,scrollbars=1,width=800,height=300");
 }
 
 function flipview(){
    var regtable= document.getElementById('registartiontable');
    var formtable = document.getElementById('graphtable');
    //alert(formtable.style.display);
    //alert(regtable.style.display);
    
    if (regtable.style.display == "none"){
      //alert("regtable");
        regtable.style.display = "";
       formtable.style.display = "none";
       return false;
    }
    
    if (formtable.style.display == "none"){
        //alert("formtable");
       formtable.style.display = "";
       regtable.style.display = "none";
       return false;
    }
 }
 
</script>

</head>

<body onload="window.resizeTo(998,719)">
<jsp:useBean id="patientData" class="oscar.form.study.HSFO.PatientData"
	scope="request" />
<jsp:useBean id="visitData" class="oscar.form.study.HSFO.VisitData"
	scope="request" />
<jsp:useBean id="recordList" class="oscar.form.study.HSFO.RecordList"
	scope="request" />

<%
int[] Linesbpy1 = (int[])request.getAttribute("SBPArray");
if (Linesbpy1 == null ) { Linesbpy1 = new int[0];}
String[] Linesbpx1 = (String[])request.getAttribute("SBPDate");
if (Linesbpx1 == null ) { Linesbpx1 = new String[0]; }

int[] Linedbpy1 = (int[])request.getAttribute("DBPArray"); 
if (Linedbpy1 == null ) { Linedbpy1 = new int[0];}

String[] Linedbpx1 = (String[])request.getAttribute("DBPDate");
if (Linedbpx1 == null ) { Linedbpx1 = new String[0]; }

double[] LineBMIy1 = (double[])request.getAttribute("BMIArray");
 if (LineBMIy1 == null ) { LineBMIy1 = new double[0];}

String[] LineBMIx1 = (String[])request.getAttribute("BMIDate"); 
if (LineBMIx1 == null ) { LineBMIx1 = new String[0]; }

double[] LineWaisty1 = (double[])request.getAttribute("WaistArray");
 if (LineWaisty1 == null ) { LineWaisty1 = new double[0];}
 
String[] LineWaistx1 = (String[])request.getAttribute("WaistDate");
if (LineWaistx1 == null ) { LineWaistx1 = new String[0]; }

double[] LineTCHDLy1 = (double[])request.getAttribute("TCHDLArray"); 
 if (LineTCHDLy1 == null ) { LineTCHDLy1 = new double[0];}
 
String[] LineTCHDLx1 = (String[])request.getAttribute("TCHDLDateArray");
if (LineTCHDLx1 == null ) { LineTCHDLx1 = new String[0]; }

double[] LineLDLy1 = (double[])request.getAttribute("LDLArray");
 if (LineLDLy1 == null ) { LineLDLy1 = new double[0];}
 
String[] LineLDLx1 = (String[])request.getAttribute("LDLDateArray"); 
if (LineLDLx1 == null ) { LineLDLx1 = new String[0]; }

int[] LineImportancey1 = (int[])request.getAttribute("importanceArray");
if (LineImportancey1 == null ) { LineImportancey1 = new int[0];}

String[] LineImportancex1 = (String[])request.getAttribute("importanceDate");
if (LineImportancex1 == null ) { LineImportancex1 = new String[0]; }

int[] LineConfidencey1 = (int[])request.getAttribute("confidenceArray");
if (LineConfidencey1 == null ) { LineConfidencey1 = new int[0];}

String[] LineConfidencex1 = (String[])request.getAttribute("confidenceDate");
if (LineConfidencex1 == null ) { LineConfidencex1 = new String[0]; }

String Linesbpy = "";
String Linesbpx = "";
for (int i=0; i < Linesbpy1.length; i++)
{
	if(i==(Linesbpy1.length-1)){
		Linesbpy = Linesbpy + Linesbpy1[i];
		Linesbpx = Linesbpx + Linesbpx1[i];}
	else{
		Linesbpy = Linesbpy + Linesbpy1[i] + ",";
		Linesbpx = Linesbpx + Linesbpx1[i] + ",";}
}
String Linedbpy = "";
String Linedbpx = "";
for (int i=0; i < Linedbpy1.length; i++)
{
	if(i==(Linedbpy1.length-1)){
		Linedbpy = Linedbpy + Linedbpy1[i];
		Linedbpx = Linedbpx + Linedbpx1[i];}
	else{
		Linedbpy = Linedbpy + Linedbpy1[i] + ",";
		Linedbpx = Linedbpx + Linedbpx1[i] + ",";}
}
String LineBMIy = "";
String LineBMIx = "";
for (int i=0; i < LineBMIy1.length; i++)
{
	if(i==(LineBMIy1.length-1)){
		LineBMIy = LineBMIy + LineBMIy1[i];
		LineBMIx = LineBMIx + LineBMIx1[i];}
	else{
		LineBMIy = LineBMIy + LineBMIy1[i] + ",";
		LineBMIx = LineBMIx + LineBMIx1[i] + ",";}
}
String LineWaisty = "";
String LineWaistx = "";
for (int i=0; i < LineWaisty1.length; i++)
{
	if(i==(LineWaisty1.length-1)){
		LineWaisty = LineWaisty + LineWaisty1[i];
		LineWaistx = LineWaistx + LineWaistx1[i];}
	else{
		LineWaisty = LineWaisty + LineWaisty1[i] + ",";
		LineWaistx = LineWaistx + LineWaistx1[i] + ",";}
}
String LineTCHDLy = "";
String LineTCHDLx = "";
for (int i=0; i < LineTCHDLy1.length; i++)
{
	if(i==(LineTCHDLy1.length-1)){
		LineTCHDLy = LineTCHDLy + LineTCHDLy1[i];
		LineTCHDLx = LineTCHDLx + LineTCHDLx1[i];}
	else{
		LineTCHDLy = LineTCHDLy + LineTCHDLy1[i] + ",";
		LineTCHDLx = LineTCHDLx + LineTCHDLx1[i] + ",";}
}
String LineLDLy = "";
String LineLDLx = "";
for (int i=0; i < LineLDLy1.length; i++)
{
	if(i==(LineLDLy1.length-1)){
		LineLDLy = LineLDLy + LineLDLy1[i];
		LineLDLx = LineLDLx + LineLDLx1[i];}
	else{
		LineLDLy = LineLDLy + LineLDLy1[i] + ",";
		LineLDLx = LineLDLx + LineLDLx1[i] + ",";}
}
String LineImportancey = "";
String LineImportancex = "";
for (int i=0; i < LineImportancey1.length; i++)
{
	if(i==(LineImportancey1.length-1)){
		LineImportancey = LineImportancey + LineImportancey1[i];
		LineImportancex = LineImportancex + LineImportancex1[i];}
	else{
		LineImportancey = LineImportancey + LineImportancey1[i] + ",";
		LineImportancex = LineImportancex + LineImportancex1[i] + ",";}
}
String LineConfidencey = "";
String LineConfidencex = "";
for (int i=0; i < LineConfidencey1.length; i++)
{
	if(i==(LineConfidencey1.length-1)){
		LineConfidencey = LineConfidencey + LineConfidencey1[i];
		LineConfidencex = LineConfidencex + LineConfidencex1[i];}
	else{
		LineConfidencey = LineConfidencey + LineConfidencey1[i] + ",";
		LineConfidencex = LineConfidencex + LineConfidencex1[i] + ",";}
}
%>

<table width="82%" border="0" cellpadding="1" cellspacing="0"
	class="table">
	<tr bgcolor="#FFFFFF">
		<td width="40%" height="87" class="formtd">
		<p><font size="4" face="Arial, Helvetica, sans-serif"><%--img src="./images/hsfologo.jpg" width="200" height="85" /--%></font></p>
		</td>

		<td class="formtd"><font size="4"
			face="Arial, Helvetica, sans-serif"><strong>PATIENT
		REGISTRATION VISIT</strong></font><font size="3" face="Arial, Helvetica, sans-serif"><em><strong><br>
		HSFO High Blood Pressure Strategy</strong></em></font> <strong><font size="4"
			face="Arial, Helvetica, sans-serif"><br>
		</font></strong><br>

		<% String [] visitList = (String[])request.getAttribute("visitDates");
           String [] idList = (String[])request.getAttribute("visitIds");
if (visitList != null){
%> Jump to visit record: <html:form action="/form/HSFOform">
			<select name="recordNumber">
				<% for(int i=0; i< visitList.length; i++){%>
				<option value="<%= idList[i] %>"><%= visitList[i] %></option>
				<%} %>
			</select>
			<input name="demographic_no" type="hidden"
				value="<%= patientData.getPatient_Id() %>">
			<input type="hidden" name="refresh" value="true" />
			<input type="submit" name="submit" value="Submit" />
		</html:form> <%} %>
		</td>
	</tr>
</table>

<html:form action="/form/HSFOsaveform.do" onsubmit="return checkform()"
	styleId="form1">
	<input type="hidden" name="dispatch" value="flowsheet" />
	<table width="82%" border="0" cellpadding="1" cellspacing="0"
		class="table">
		<tr>
			<td valign="top">
			<div align="left"><font size="2"
				face="Arial, Helvetica, sans-serif">Patient ID/Name <input
				name="Patient_Id" type="text" size="8"
				value="<%= patientData.getPatient_Id() %>"> <%if (visitList != null){%>
			<a href="javascript: function myFunction() {return false; }"
				onclick="flipview();" style="text-decoration: none;">Switch View</a>
			<%}%> <input name="SiteCode" type="hidden" value="20"> <input
				name="ID" type="hidden" value="<%= visitData.getID() %>"> <%String goback = (String)request.getAttribute("goback");
 if (goback!= null && goback.equals("yes"))
		 {
 %> <input name="goback" type="hidden" value="<%= goback%>"> <%} %>
			
			</td>
			<td>
			<div align="right"><strong><font size="3"
				face="Arial, Helvetica, sans-serif">You are currently editing
			the visit record for: <font color="#990000"><%=UtilDateUtilities.DateToString( visitData.getVisitDate_Id() , "yyyy-MMM-dd")%>
			</font></font></strong></div>
			</td>
		</tr>
		<tr>
			<td width="40%" height="796" rowspan="3" valign="top">
			<table width="100%" height="100%" border="0" cellpadding="1"
				cellspacing="3" class="formtable" <%=getDisplayIfMore1(visitList)%>
				id="graphtable">
				<tr>
					<td valign="top" class="formtd">
					<div align="center"><font size="2"
						face="Arial, Helvetica, sans-serif"><strong>Blood
					Pressure </strong></font> <img src="HSFOgraph.jsp?id=HSFOBPCHART" /></div>
					</td>
				</tr>

				<tr>
					<td height="18" valign="top" class="formtd">
					<div align="center"><font size="2"
						face="Arial, Helvetica, sans-serif"><strong>BMI </strong></font> <img
						src="HSFOgraph.jsp?id=HSFOBBMICHART" /></div>
					</td>
				</tr>
				<tr>
					<td height="18" valign="top" class="formtd">
					<div align="center"><font size="2"
						face="Arial, Helvetica, sans-serif"><strong> Waist
					Circumference</strong></font> <img src="HSFOgraph.jsp?id=HSFOWAISTCHART" /></div>
					</td>
				</tr>
				<tr>
					<td height="18" class="formtd">
					<div align="center"><font size="2"
						face="Arial, Helvetica, sans-serif"><strong>TC/HCL
					&amp; LDL</strong></font> <img src="HSFOgraph.jsp?id=HSFODLCHART" /></div>
					</td>
				</tr>
				<tr>
					<td height="18" class="formtd">
					<div align="center"><strong><font size="2"
						face="Arial, Helvetica, sans-serif">Lifestyle: Goal,
					Importance, Confidence</font></strong> <img
						src="HSFOgraph.jsp?id=HSFOimportanceconfidenceCHART" /></div>
					</td>
				</tr>
			</table>


			<table width="90%" border="0" cellpadding="1" cellspacing="3"
				class="formtable" id="registartiontable"
				<%=getDisplayIfLess1(visitList)%>>
				<%String isfirstrecord = (String)request.getAttribute("isFirstRecord"); %>
				<tr>
					<td width="75%" height="179">
					<p><font size="2" face="Arial, Helvetica, sans-serif">Use
					this Registration form ONLY for a patient's FIRST visit that begins
					the program.</font></p>
					<p>&nbsp;</p>
					<table width="100%" border="0">
						<tr>
							<td width="80"><font size="2"
								face="Arial, Helvetica, sans-serif"><strong>First
							Name:</strong> </font></td>
							<td width="174"><font size="2"
								face="Arial, Helvetica, sans-serif"> <input name="FName"
								type="text" size="30"
								value="<%=(patientData.getFName()!=null) ? patientData.getFName() :"" %>">
							<span class="style1">* </span></font></td>
						</tr>
						<tr>
							<td>
							<div align="left"><font size="2"
								face="Arial, Helvetica, sans-serif"><strong>Last
							Name: </strong></font></div>
							</td>
							<td><font size="2" face="Arial, Helvetica, sans-serif">
							<input type="text" name="LName"
								value="<%=(patientData.getLName()!=null) ? patientData.getLName() :"" %>">
							<span class="style1">* </span> </font></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td height="88"><font size="2"
						face="Arial, Helvetica, sans-serif"><strong>Date
					of signed consent</strong><br>
					<br>
					<input name="consentDate_year" type="text" size="8"
						value="<%=(patientData.getConsentDate() != null) ? ""+(patientData.getConsentDate().getYear() + 1900) : ""%>">
					- <SELECT name="consentDate_month">
						<option
							value="<%=(patientData.getConsentDate() != null) ? ""+(patientData.getConsentDate().getMonth() + 1) : ""%>"
							selected><%=(patientData.getConsentDate() != null) ? ""+(patientData.getConsentDate().getMonth() + 1) : ""%>
						<option value="1">Jan
						<option value="2">Feb
						<option value="3">Mar
						<option value="4">Apr
						<option value="5">May
						<option value="6">Jun
						<option value="7">Jul
						<option value="8">Aug
						<option value="9">Sep
						<option value="10">Oct
						<option value="11">Nov
						<option value="12">Dec
					</SELECT> - <SELECT name="consentDate_day">
						<option
							value="<%=(patientData.getConsentDate() != null) ? ""+(patientData.getConsentDate().getDate()) : ""%>"
							selected><%=(patientData.getConsentDate() != null) ? ""+(patientData.getConsentDate().getDate()) : ""%>
						<option value="1">1
						<option value="2">2
						<option value="3">3
						<option value="4">4
						<option value="5">5
						<option value="6">6
						<option value="7">7
						<option value="8">8
						<option value="9">9
						<option value="10">10
						<option value="11">11
						<option value="12">12
						<option value="13">13
						<option value="14">14
						<option value="15">15
						<option value="16">16
						<option value="17">17
						<option value="18">18
						<option value="19">19
						<option value="20">20
						<option value="21">21
						<option value="22">22
						<option value="23">23
						<option value="24">24
						<option value="25">25
						<option value="26">26
						<option value="27">27
						<option value="28">28
						<option value="30">30
						<option value="31">31
					</SELECT> <span class="style1">* </span><br>
					</font>
					<table width="202" border="0">
						<tr>
							<td width="83"><font size="2"
								face="Arial, Helvetica, sans-serif">Year</font></td>
							<td width="59"><font size="2"
								face="Arial, Helvetica, sans-serif">month</font></td>
							<td width="46"><font size="2"
								face="Arial, Helvetica, sans-serif">day</font></td>
						</tr>

					</table>
					<hr size="1" color="#cccccc">
					</td>
				</tr>
				<tr>
					<td height="142"><font size="2"
						face="Arial, Helvetica, sans-serif"><strong>Patient
					is rostered to:</strong><br>
					<input name="EmrHCPId1" type="text" size="5"
						value="<%=(String)request.getAttribute("EmrHCPId1") %>"> -
					<input type="text" name="EmrHCPId2"
						value="<%=(String)request.getAttribute("EmrHCPId2") %>"> <span
						class="style1">* </span><br>
					Study ID of MD or NP - and Last Name<br>

					<br>
					<strong>Date of birth</strong><br>
					<input name="BirthDate_year" type="text" size="8"
						value="<%=(patientData.getBirthDate() != null) ? ""+(patientData.getBirthDate().getYear() + 1900) : ""%>">
					- <SELECT name="BirthDate_month">
						<option
							value="<%=(patientData.getBirthDate() != null) ? ""+(patientData.getBirthDate().getMonth() + 1) : ""%>"
							selected><%=(patientData.getBirthDate() != null) ? ""+(patientData.getBirthDate().getMonth() + 1) : ""%>
						<option value="1">Jan
						<option value="2">Feb
						<option value="3">Mar
						<option value="4">Apr
						<option value="5">May
						<option value="6">Jun
						<option value="7">Jul
						<option value="8">Aug
						<option value="9">Sep
						<option value="10">Oct
						<option value="11">Nov
						<option value="12">Dec
					</SELECT> - <SELECT name="BirthDate_day">
						<option
							value="<%=(patientData.getBirthDate() != null) ?""+(patientData.getBirthDate().getDate()) : ""%>"
							selected><%=(patientData.getBirthDate() != null) ?""+(patientData.getBirthDate().getDate()) : ""%>
						<option value="1">1
						<option value="2">2
						<option value="3">3
						<option value="4">4
						<option value="5">5
						<option value="6">6
						<option value="7">7
						<option value="8">8
						<option value="9">9
						<option value="10">10
						<option value="11">11
						<option value="12">12
						<option value="13">13
						<option value="14">14
						<option value="15">15
						<option value="16">16
						<option value="17">17
						<option value="18">18
						<option value="19">19
						<option value="20">20
						<option value="21">21
						<option value="22">22
						<option value="23">23
						<option value="24">24
						<option value="25">25
						<option value="26">26
						<option value="27">27
						<option value="28">28
						<option value="30">30
						<option value="31">31
					</SELECT> <span class="style1">* </span><br>
					</font>
					<table width="202" border="0">
						<tr>
							<td width="83"><font size="2"
								face="Arial, Helvetica, sans-serif">Year</font></td>
							<td width="59"><font size="2"
								face="Arial, Helvetica, sans-serif">month</font></td>
							<td width="46"><font size="2"
								face="Arial, Helvetica, sans-serif">day</font></td>
						</tr>

					</table>
					</td>
				</tr>
				<tr>
					<td>
					<table width="100%" border="0">
						<tr>
							<td width="145"><font size="2"
								face="Arial, Helvetica, sans-serif"><strong>Sex</strong>
							<input type="radio" name="Sex" value="m"
								<%=(patientData.getSex()!=null && patientData.getSex().equalsIgnoreCase("m")) ? "checked" :"" %>>
							M <input type="radio" name="Sex" value="f"
								<%=(patientData.getSex()!=null)  && patientData.getSex().equalsIgnoreCase("f")? "checked" :"" %>>
							F <span class="style1">* </span></font></td>
						</tr>
						<tr>
							<td><font size="2" face="Arial, Helvetica, sans-serif"><strong><br>
							Postal code FSA</strong><br>
							<input name="PostalCode" type="text" size="8"
								value="<%=(patientData.getPostalCode()!=null) ? patientData.getPostalCode() :"" %>">
							First 3 characters ONLY <span class="style1">* </span></font></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td height="73"><font size="2"
						face="Arial, Helvetica, sans-serif"><strong>Pharmacy
					used most often</strong><br>

					<input type="text" name="PharmacyName"
						value="<%=(patientData.getPharmacyName()!=null) ? patientData.getPharmacyName() :"" %>">
					(name)<span class="style1">* </span><br>
					<input type="text" name="PharmacyLocation"
						value="<%=(patientData.getPharmacyLocation()!=null) ? patientData.getPharmacyLocation() :"" %>">
					(location) <span class="style1">* </span></font></td>
				</tr>
				<tr>
					<td><font size="2" face="Arial, Helvetica, sans-serif"><strong>Height:</strong>
					<input name="Height" type="text" size="8"
						value="<%=patientData.getHeight() %>"> (NNN.N) <input
						type="radio" name="Height_unit" value="cm"
						<%=(patientData.getHeight_unit()!=null)  && patientData.getHeight_unit().equals("cm")? "checked" :"" %>>
					cm <input type="radio" name="Height_unit" value="inch"
						<%=(patientData.getHeight_unit()!=null)  && patientData.getHeight_unit().equals("inch")? "checked" :"" %>>
					in<span class="style1">* </span></font></td>
				</tr>
				<tr>
					<td height="170" colspan="2">
					<table width="326" border="0">
						<tr>
							<td colspan="2"><font size="2"
								face="Arial, Helvetica, sans-serif"><strong>Ethnicity
							(self reported)</strong><span class="style1">* </span></font><font size="2"
								face="Arial, Helvetica, sans-serif">(fill ALL that apply)</font></td>
						</tr>
						<tr>
							<td width="134"><font size="2"
								face="Arial, Helvetica, sans-serif"> <input
								type="checkbox" name="Ethnic_White" value="Ethnic_White"
								<%=patientData.isEthnic_White()? "checked" :"" %>> White<br>
							<input type="checkbox" name="Ethnic_Black" value="Ethnic_Black"
								<%=patientData.isEthnic_Black()? "checked" :"" %>> Black<br>
							<input type="checkbox" name="Ethnic_EIndian"
								value="Ethnic_EIndian"
								<%=patientData.isEthnic_EIndian()? "checked" :"" %>> E.
							Indian<br>
							<input type="checkbox" name="Ethnic_Pakistani"
								value="Ethnic_Pakistani"
								<%=patientData.isEthnic_Pakistani()? "checked" :"" %>>
							Pakistani<br>
							<input type="checkbox" name="Ethnic_SriLankan"
								value="Ethnic_SriLankan"
								<%=patientData.isEthnic_SriLankan()? "checked" :"" %>>
							Sri Lankan<br>
							<input type="checkbox" name="Ethnic_Bangladeshi"
								value="Ethnic_Bangladeshi"
								<%=patientData.isEthnic_Bangladeshi()? "checked" :"" %>>
							Bangladeshi<br>
							<input type="checkbox" name="Ethnic_Chinese"
								value="Ethnic_Chinese"
								<%=patientData.isEthnic_Chinese()? "checked" :"" %>>
							Chinese </font></td>
							<td><font size="2" face="Arial, Helvetica, sans-serif">
							<input type="checkbox" name="Ethnic_Japanese"
								value="Ethnic_Japanese"
								<%=patientData.isEthnic_Japanese()? "checked" :"" %>>
							Japanese<br>
							<input type="checkbox" name="Ethnic_Korean" value="Ethnic_Korean"
								<%=patientData.isEthnic_Korean()? "checked" :"" %>>
							Korean<br>
							<input type="checkbox" name="Ethnic_Hispanic"
								value="Ethnic_Hispanic"
								<%=patientData.isEthnic_Hispanic()? "checked" :"" %>>
							Hispanic<br>
							<input type="checkbox" name="Ethnic_FirstNation"
								value="Ethnic_FirstNation"
								<%=patientData.isEthnic_FirstNation()? "checked" :"" %>>
							N. American First Nations<br>
							<input type="checkbox" name="Ethnic_Other" value="Ethnic_Other"
								<%=patientData.isEthnic_Other()? "checked" :"" %>> Other<br>
							<input type="checkbox" name="Ethnic_Refused"
								value="Ethnic_Refused"
								<%=patientData.isEthnic_Refused()? "checked" :"" %>>
							Refused<br>
							<input type="checkbox" name="Ethnic_Unknown"
								value="Ethnic_Unknown"
								<%=patientData.isEthnic_Unknown()? "checked" :"" %>>
							Unknown </font></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td height="40"><font size="2"
						face="Arial, Helvetica, sans-serif"><strong>1&deg;
					hypertension was diagnosed </strong>(fill ONE)<span class="style1">*
					</span><br>

					<input type="radio" name="sel_TimeAgoDx" value="Under1YrAgo"
						<%=(patientData.getSel_TimeAgoDx()!=null)  && patientData.getSel_TimeAgoDx().equals("Under1YrAgo")? "checked" :"" %>>
					&gt; 1 yr ago <input type="radio" name="sel_TimeAgoDx"
						value="AtLeast1YrAgo"
						<%=(patientData.getSel_TimeAgoDx()!=null)  && patientData.getSel_TimeAgoDx().equals("AtLeast1YrAgo")? "checked" :"" %>>
					&lt; 1 yr ago <input type="radio" name="sel_TimeAgoDx" value="NA"
						<%=(patientData.getSel_TimeAgoDx()!=null)  && patientData.getSel_TimeAgoDx().equals("NA")? "checked" :"" %>>
					Not (yet) diagnosed</font></td>
				</tr>
				<tr>
					<td height="117">&nbsp;</td>
				</tr>
			</table>





			</td>
			<td height="493" valign="top">
			<table width="96%" height="493" cellpadding="1" cellspacing="0">
				<tr>
					<td width="290" height="491" valign="top">
					<table width="90%" height="100%" border="0" cellpadding="1"
						cellspacing="3" class="formtable">
						<tr class="dash">
							<td class="coldash"><font size="2"
								face="Arial, Helvetica, sans-serif"><strong>Medical
							Dx &amp; Hx</strong></font></td>
							<td width="47">
							<div align="center"><font size="2"
								face="Arial, Helvetica, sans-serif"><strong>Family
							Hx</strong></font></div>
							</td>
						</tr>
						<tr>
							<td width="172" class="coldash"><font size="2"
								face="Arial, Helvetica, sans-serif"> <input type="radio"
								name="HtnDxType" value="ElevatedBpReadings"
								<%=(visitData.getHtnDxType() != null && visitData.getHtnDxType().equals("ElevatedBpReadings")) ? "checked" :"" %>>
							Elevated BP readings <br>
							or<br>
							<input type="radio" name="HtnDxType" value="PrimaryHtn"
								<%=(visitData.getHtnDxType() != null && visitData.getHtnDxType().equals("PrimaryHtn")) ? "checked" :"" %>>
							1&deg; Hypertension<br>
							<input type="checkbox" name="Dyslipid" value="Dyslipid"
								<%=(visitData.isDyslipid()) ? "checked" :"" %>>
							Dyslipidemia<br>
							<input type="checkbox" name="Diabetes" value="Diabetes"
								<%=(visitData.isDiabetes()) ? "checked" :"" %>> Diabetes<br>
							<input type="checkbox" name="KidneyDis" value="KidneyDis"
								<%=(visitData.isKidneyDis()) ? "checked" :"" %>> Kidney
							disease<br>
							<input type="checkbox" name="Obesity" value="Obesity"
								<%=(visitData.isObesity()) ? "checked" :"" %>> Obesity<br>
							<input type="checkbox" name="CHD" value="CHD"
								<%=(visitData.isCHD()) ? "checked" :"" %>> Coronary
							heart disease<br>
							<input type="checkbox" name="Stroke_TIA" value="Stroke_TIA"
								<%=(visitData.isStroke_TIA()) ? "checked" :"" %>> Stroke
							or TIA<br>
							</font></td>
							<td><font size="2" face="Arial, Helvetica, sans-serif"><br>
							<br>
							<input type="checkbox" name="FamHx_Htn" value="FamHx_Htn"
								<%=(visitData.isFamHx_Htn() ) ? "checked" :"" %>> <br>
							<input type="checkbox" name="FamHx_Dyslipid"
								value="FamHx_Dyslipid"
								<%=(visitData.isFamHx_Dyslipid()) ? "checked" :"" %>> <br>
							<input type="checkbox" name="FamHx_Diabetes"
								value="FamHx_Diabetes"
								<%=(visitData.isFamHx_Diabetes()) ? "checked" :"" %>> <br>
							<input type="checkbox" name="FamHx_KidneyDis"
								value="FamHx_KidneyDis"
								<%=(visitData.isFamHx_KidneyDis()) ? "checked" :"" %>> <br>
							<input type="checkbox" name="FamHx_Obesity" value="FamHx_Obesity"
								<%=(visitData.isFamHx_Obesity()) ? "checked" :"" %>> <br>
							<input type="checkbox" name="FamHx_CHD" value="FamHx_CHD"
								<%=(visitData.isFamHx_CHD()) ? "checked" :"" %>> <br>
							<input type="checkbox" name="FamHx_Stroke_TIA"
								value="FamHx_Stroke_TIA"
								<%=(visitData.isFamHx_Stroke_TIA()) ? "checked" :"" %>>
							</font></td>
						</tr>
						<tr>
							<td height="219" colspan="2">
							<hr size="1" color="#cccccc">
							<table width="224" border="0">
								<tr>
									<td width="138" height="20" class="coldash"><font size="2"
										face="Arial, Helvetica, sans-serif"><strong>CV
									Risk Factors</strong></font></td>
									<td width="76" rowspan="2"
										style="border-bottom: 1px dashed #999999;">
									<div align="center"><font size="2"
										face="Arial, Helvetica, sans-serif"><strong>1
									Patient Selected Lifestyle Goal</strong></font></div>
									<div align="center"></div>
									</td>
								</tr>
								<tr class="dash">
									<td height="20" class="coldash"><font size="2"
										face="Arial, Helvetica, sans-serif"> <input
										type="checkbox" name="Risk_weight" value="Risk_weight"
										<%=(visitData.isRisk_weight()) ? "checked" :"" %>>
									Weight</font></td>
								</tr>
								<tr class="dash">
									<td height="20" class="coldash"><font size="2"
										face="Arial, Helvetica, sans-serif"> <input
										type="checkbox" name="Risk_activity" value="Risk_activity"
										<%=(visitData.isRisk_activity()) ? "checked" :"" %>>
									Physical activity</font></td>
									<td>
									<div align="left"><font size="2"
										face="Arial, Helvetica, sans-serif"> <input
										type="radio" name="LifeGoal" value="Goal_activity"
										<%=visitData.getLifeGoal() != null && visitData.getLifeGoal().equals("Goal_activity")? "checked" :"" %>>
									</font></div>
									</td>
								</tr>
								<tr class="dash">
									<td height="20" class="coldash"><font size="2"
										face="Arial, Helvetica, sans-serif"> <input
										type="checkbox" name="Risk_diet" value="Risk_diet"
										<%=(visitData.isRisk_diet()) ? "checked" :"" %>> Diet<br>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									- Dash<br>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									- Salt</font></td>
									<td>
									<div align="left"><font size="2"
										face="Arial, Helvetica, sans-serif"><br>
									<input type="radio" name="LifeGoal" value="Goal_dietDash"
										<%=visitData.getLifeGoal() != null && visitData.getLifeGoal().equals("Goal_dietDash") ? "checked" :"" %>>
									<br>
									<input type="radio" name="LifeGoal" value="Goal_dietSalt"
										<%=visitData.getLifeGoal() != null && visitData.getLifeGoal().equals("Goal_dietSalt") ? "checked" :"" %>>
									</font></div>
									</td>
								</tr>
								<tr class="dash">
									<td height="20" class="coldash"><font size="2"
										face="Arial, Helvetica, sans-serif"> <input
										type="checkbox" name="Risk_smoking" value="Risk_smoking"
										<%=(visitData.isRisk_smoking()) ? "checked" :"" %>>
									Smoking</font></td>
									<td>
									<div align="left"><font size="2"
										face="Arial, Helvetica, sans-serif"> <input
										type="radio" name="LifeGoal" value="Goal_smoking"
										<%=visitData.getLifeGoal() != null && visitData.getLifeGoal().equals("Goal_smoking") ? "checked" :"" %>>
									</font></div>
									</td>
								</tr>
								<tr class="dash">
									<td height="21" class="coldash"><font size="2"
										face="Arial, Helvetica, sans-serif"> <input
										type="checkbox" name="Risk_alcohol" value="Risk_alcohol"
										<%=(visitData.isRisk_alcohol()) ? "checked" :"" %>>
									Alcohol intake</font></td>
									<td>
									<div align="left"><font size="2"
										face="Arial, Helvetica, sans-serif"> <input
										type="radio" name="LifeGoal" value="Goal_alcohol"
										<%=visitData.getLifeGoal() != null && visitData.getLifeGoal().equals("Goal_alcohol") ? "checked" :"" %>>
									</font></div>
									</td>
								</tr>
								<tr>
									<td height="20" class="coldash"><font size="2"
										face="Arial, Helvetica, sans-serif"> <input
										type="checkbox" name="Risk_stress" value="Risk_stress"
										<%=(visitData.isRisk_stress()) ? "checked" :"" %>>
									Stress</font></td>
									<td>
									<div align="left"><font size="2"
										face="Arial, Helvetica, sans-serif"> <input
										type="radio" name="LifeGoal" value="Goal_stress"
										<%=visitData.getLifeGoal() != null && visitData.getLifeGoal().equals("Goal_stress") ? "checked" :"" %>>
									</font></div>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td colspan="2">
							<hr size="1" color="#cccccc">
							<font size="2" face="Arial, Helvetica, sans-serif"> <strong>Patient
							view of selected lifestyle goal</strong><br>
							</font>
							<table width="100%" border="0">
								<tr>
									<td width="50%"><font size="2"
										face="Arial, Helvetica, sans-serif"> <input
										type="radio" name="PtView" value="Uninterested"
										<%=visitData.getPtView() != null && visitData.getPtView().equals("Uninterested") ? "checked" :"" %>>
									Uninterested<br>
									<input type="radio" name="PtView" value="Thinking"
										<%=visitData.getPtView() != null && visitData.getPtView().equals("Thinking") ? "checked" :"" %>>
									Thinking<br>
									<input type="radio" name="PtView" value="Deciding"
										<%=visitData.getPtView() != null && visitData.getPtView().equals("Deciding") ? "checked" :"" %>>
									Deciding</font></td>
									<td width="50%"><font size="2"
										face="Arial, Helvetica, sans-serif"> <input
										type="radio" name="PtView" value="TakingAction"
										<%=visitData.getPtView() != null && visitData.getPtView().equals("TakingAction") ? "checked" :"" %>>
									Taking action<br>
									<input type="radio" name="PtView" value="Maintaining"
										<%=visitData.getPtView() != null && visitData.getPtView().equals("Maintaining") ? "checked" :"" %>>
									Maintaining<br>
									<input type="radio" name="PtView" value="Relapsing"
										<%=visitData.getPtView() != null && visitData.getPtView().equals("Relapsing") ? "checked" :"" %>>
									Relapsed</font></td>
								</tr>
							</table>

							</td>
						</tr>
					</table>
					</td>
					<td width="512" valign="top">
					<table width="367" height="100%" border="0" cellpadding="1"
						cellspacing="3" class="formtable">
						<tr>
							<td height="96" colspan="3" valign="top">
							<table width="100%" border="0">
								<tr>
									<td><font size="2" face="Arial, Helvetica, sans-serif"><strong>Date
									of visit</strong> </font></td>
									<td colspan="3"><font size="2"
										face="Arial, Helvetica, sans-serif"> <select
										name="VisitDate_Id_year">
										<option value="2007"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getYear()+1900 == 2007) ? "selected" : ""%>>2007</option>
										<option value="2008"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getYear()+1900 == 2008) ? "selected" : ""%>>2008</option>
										<option value="2009"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getYear()+1900 == 2009) ? "selected" : ""%>>2009</option>
									</select> - <select name="VisitDate_Id_month">
										<option value="1"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getMonth()+1 == 1) ? "selected" : ""%>>Jan
										</option>
										<option value="2"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getMonth()+1 == 2) ? "selected" : ""%>>Feb
										</option>
										<option value="3"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getMonth()+1 == 3) ? "selected" : ""%>>Mar
										</option>
										<option value="4"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getMonth()+1 == 4) ? "selected" : ""%>>Apr
										</option>
										<option value="5"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getMonth()+1 == 5) ? "selected" : ""%>>May
										</option>
										<option value="6"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getMonth()+1 == 6) ? "selected" : ""%>>Jun
										</option>
										<option value="7"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getMonth()+1 == 7) ? "selected" : ""%>>Jul
										</option>
										<option value="8"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getMonth()+1 == 8) ? "selected" : ""%>>Aug
										</option>
										<option value="9"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getMonth()+1 == 9) ? "selected" : ""%>>Sep
										</option>
										<option value="10"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getMonth()+1 == 10) ? "selected" : ""%>>Oct
										</option>
										<option value="11"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getMonth()+1 == 11) ? "selected" : ""%>>Nov
										</option>
										<option value="12"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getMonth()+1 == 12) ? "selected" : ""%>>Dec
										</option>
									</select> - <select name="VisitDate_Id_day">
										<option value="1"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 1) ? "selected" : ""%>>1
										</option>
										<option value="2"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 2) ? "selected" : ""%>>2
										</option>
										<option value="3"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 3) ? "selected" : ""%>>3
										</option>
										<option value="4"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 4) ? "selected" : ""%>>4
										</option>
										<option value="5"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 5) ? "selected" : ""%>>5
										</option>
										<option value="6"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 6) ? "selected" : ""%>>6
										</option>
										<option value="7"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 7) ? "selected" : ""%>>7
										</option>
										<option value="8"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 8) ? "selected" : ""%>>8
										</option>
										<option value="9"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 9) ? "selected" : ""%>>9
										</option>
										<option value="10"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 10) ? "selected" : ""%>>10
										</option>
										<option value="11"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 11) ? "selected" : ""%>>11
										</option>
										<option value="12"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 12) ? "selected" : ""%>>12
										</option>
										<option value="13"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 13) ? "selected" : ""%>>13
										</option>
										<option value="14"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 14) ? "selected" : ""%>>14
										</option>
										<option value="15"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 15) ? "selected" : ""%>>15
										</option>
										<option value="16"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 16) ? "selected" : ""%>>16
										</option>
										<option value="17"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 17) ? "selected" : ""%>>17
										</option>
										<option value="18"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 18) ? "selected" : ""%>>18
										</option>
										<option value="19"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 19) ? "selected" : ""%>>19
										</option>
										<option value="20"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 20) ? "selected" : ""%>>20
										</option>
										<option value="21"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 21) ? "selected" : ""%>>21
										</option>
										<option value="22"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 22) ? "selected" : ""%>>22
										</option>
										<option value="23"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 23) ? "selected" : ""%>>23
										</option>
										<option value="24"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 24) ? "selected" : ""%>>24
										</option>
										<option value="25"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 25) ? "selected" : ""%>>25
										</option>
										<option value="26"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 26) ? "selected" : ""%>>26
										</option>
										<option value="27"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 27) ? "selected" : ""%>>27
										</option>
										<option value="28"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 28) ? "selected" : ""%>>28
										</option>
										<option value="29"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 29) ? "selected" : ""%>>29</option>
										<option value="30"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 30) ? "selected" : ""%>>30
										</option>
										<option value="31"
											<%= (visitData.getVisitDate_Id()!=null && visitData.getVisitDate_Id().getDate() == 31) ? "selected" : ""%>>31
										</option>
									</select> <span class="style1">* </span></font></td>
								</tr>
								<tr>
									<td width="101" height="28">&nbsp;</td>
									<td width="71"><font size="2"
										face="Arial, Helvetica, sans-serif">Year</font></td>
									<td width="78"><font size="2"
										face="Arial, Helvetica, sans-serif">month</font></td>
									<td width="89"><font size="2"
										face="Arial, Helvetica, sans-serif">day</font></td>
								</tr>
							</table>
							<font size="2" face="Arial, Helvetica, sans-serif"><strong>Adequate
							drug coverage</strong> <input type="radio" name="Drugcoverage" value="yes"
								<%=(visitData.getDrugcoverage() != null && visitData.getDrugcoverage().equals("yes")) ? "checked" :"" %>>
							Y <input type="radio" name="Drugcoverage" value="no"
								<%=(visitData.getDrugcoverage() != null && visitData.getDrugcoverage().equals("no")) ? "checked" :"" %>>
							N </font>
							<hr size="1" color="#cccccc">
							</td>
						</tr>
						<tr valign="top">
							<td height="18" colspan="3"><font size="2"
								face="Arial, Helvetica, sans-serif"><strong>Physical
							exam </strong></font></td>
						</tr>
						<tr valign="top">
							<td width="58" height="23"><font size="2"
								face="Arial, Helvetica, sans-serif">SBP </font></td>
							<td width="157"><font size="2"
								face="Arial, Helvetica, sans-serif"> <input name="SBP"
								type="text" size="5"
								value="<%=visitData.getSBP() != 0? ""+visitData.getSBP(): ""%>">
							(NNN) mmHg<br>
							</font></td>
							<td width="132"><font size="2"
								face="Arial, Helvetica, sans-serif"> goal is: <input
								name="SBP_goal" type="text" size="3"
								value="<%=visitData.getSBP_goal() != 0? ""+visitData.getSBP_goal(): ""%>">
							(NNN)</font></td>
						</tr>
						<tr valign="top">
							<td height="23"><font size="2"
								face="Arial, Helvetica, sans-serif">DBP </font></td>
							<td width="157"><font size="2"
								face="Arial, Helvetica, sans-serif"> <input name="DBP"
								type="text" size="5"
								value="<%=visitData.getDBP() != 0? ""+visitData.getDBP(): ""%>">
							(NNN) mmHg</font></td>
							<td width="132"><font size="2"
								face="Arial, Helvetica, sans-serif">goal is: <input
								name="DBP_goal" type="text" size="3"
								value="<%=visitData.getDBP_goal() != 0? ""+visitData.getDBP_goal(): ""%>">
							(NNN)</font></td>
						</tr>
						<tr valign="top">
							<td height="26" colspan="3"><font size="2"
								face="Arial, Helvetica, sans-serif">BpTRU used: </font><font
								size="2" face="Arial, Helvetica, sans-serif"> <input
								type="radio" name="Bptru_used" value="yes"
								<%=(visitData.getBptru_used() != null && visitData.getBptru_used().equals("yes")) ? "checked" :"" %>>
							Y <input type="radio" name="Bptru_used" value="no"
								<%=(visitData.getBptru_used() != null && visitData.getBptru_used().equals("no")) ? "checked" :"" %>>
							N </font></td>
						</tr>
						<tr valign="top">
							<td height="21"><font size="2"
								face="Arial, Helvetica, sans-serif">Weight<br>
							</font></td>
							<td height="21" colspan="2"><font size="2"
								face="Arial, Helvetica, sans-serif"> <input name="Weight"
								type="text" size="5"
								value="<%=visitData.getWeight() != 0.0? ""+visitData.getWeight(): ""%>">
							(NNN.N)</font><font size="2" face="Arial, Helvetica, sans-serif">
							<input type="radio" name="Weight_unit" value="kg"
								<%=(visitData.getWeight_unit() != null && visitData.getWeight_unit().equals("kg")) ? "checked" :"" %>>
							kg <input type="radio" name="Weight_unit" value="lb"
								<%=(visitData.getWeight_unit() != null && visitData.getWeight_unit().equals("lb")) ? "checked" :"" %>>
							lb </font></td>
						</tr>
						<tr valign="top">
							<td height="22"><font size="2"
								face="Arial, Helvetica, sans-serif">Waist</font></td>
							<td height="22" colspan="2"><font size="2"
								face="Arial, Helvetica, sans-serif"> <input name="Waist"
								type="text" size="5"
								value="<%=visitData.getWaist() != 0.0? ""+visitData.getWaist(): ""%>">
							(NNN.N)</font><font size="2" face="Arial, Helvetica, sans-serif">
							<input type="radio" name="Waist_unit" value="cm"
								<%=(visitData.getWaist_unit() != null && visitData.getWaist_unit().equals("cm")) ? "checked" :"" %>>
							cm <input type="radio" name="Waist_unit" value="inch"
								<%=(visitData.getWaist_unit() != null && visitData.getWaist_unit().equals("inch")) ? "checked" :"" %>>
							in </font></td>
						</tr>
						<tr>
							<td height="216" colspan="3">
							<hr size="1" color="#cccccc">
							<table width="100%" border="0">
								<tr class="dash">
									<td height="22" colspan="2"><font size="2"
										face="Arial, Helvetica, sans-serif"><strong>Lab
									work </strong>(date and results of most recent)</font><font size="2"
										face="Arial, Helvetica, sans-serif">&nbsp;</font></td>
								</tr>
								<tr>
									<td width="68"><font size="2"
										face="Arial, Helvetica, sans-serif">TC/HDL</font></td>
									<td width="247"><font size="2"
										face="Arial, Helvetica, sans-serif"> <input
										name="TC_HDL" type="text" size="5"
										value="<%=visitData.getTC_HDL() != 0.0? ""+visitData.getTC_HDL(): ""%>">
									ratio (goal &lt; 4.0) (NN.N)</font></td>
								</tr>
								<tr class="dash">
									<td>
									<div align="right"><font size="2"
										face="Arial, Helvetica, sans-serif"><em>Date</em></font></div>
									</td>
									<td><font size="2" face="Arial, Helvetica, sans-serif"><em>
									</em></font><font size="2" face="Arial, Helvetica, sans-serif"> <input
										name="TC_HDL_LabresultsDate_year" type="text" size="8"
										value="<%=(visitData.getTC_HDL_LabresultsDate() != null) ? ""+(visitData.getTC_HDL_LabresultsDate().getYear()+1900) : ""%>">
									- <select name="TC_HDL_LabresultsDate_month">
										<option value="1"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getMonth()+1 == 1) ? "selected" : ""%>>Jan
										
										<option value="2"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getMonth()+1 == 2) ? "selected" : ""%>>Feb
										
										<option value="3"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getMonth()+1 == 3) ? "selected" : ""%>>Mar
										
										<option value="4"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getMonth()+1 == 4) ? "selected" : ""%>>Apr
										
										<option value="5"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getMonth()+1 == 5) ? "selected" : ""%>>May
										
										<option value="6"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getMonth()+1 == 6) ? "selected" : ""%>>Jun
										
										<option value="7"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getMonth()+1 == 7) ? "selected" : ""%>>Jul
										
										<option value="8"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getMonth()+1 == 8) ? "selected" : ""%>>Aug
										
										<option value="9"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getMonth()+1 == 9) ? "selected" : ""%>>Sep
										
										<option value="10"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getMonth()+1 == 10) ? "selected" : ""%>>Oct
										
										<option value="11"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getMonth()+1 == 11) ? "selected" : ""%>>Nov
										
										<option value="12"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getMonth()+1 == 12) ? "selected" : ""%>>Dec
										
									</select> - <select name="TC_HDL_LabresultsDate_day">
										<option value="1"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 1) ? "selected" : ""%>>1
										
										<option value="2"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 2) ? "selected" : ""%>>2
										
										<option value="3"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 3) ? "selected" : ""%>>3
										
										<option value="4"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 4) ? "selected" : ""%>>4
										
										<option value="5"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 5) ? "selected" : ""%>>5
										
										<option value="6"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 6) ? "selected" : ""%>>6
										
										<option value="7"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 7) ? "selected" : ""%>>7
										
										<option value="8"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 8) ? "selected" : ""%>>8
										
										<option value="9"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 9) ? "selected" : ""%>>9
										
										<option value="10"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 10) ? "selected" : ""%>>10
										
										<option value="11"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 11) ? "selected" : ""%>>11
										
										<option value="12"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 12) ? "selected" : ""%>>12
										
										<option value="13"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 13) ? "selected" : ""%>>13
										
										<option value="14"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 14) ? "selected" : ""%>>14

										
										<option value="15"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 15) ? "selected" : ""%>>15
										
										<option value="16"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 16) ? "selected" : ""%>>16
										
										<option value="17"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 17) ? "selected" : ""%>>17
										
										<option value="18"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 18) ? "selected" : ""%>>18
										
										<option value="19"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 19) ? "selected" : ""%>>19
										
										<option value="20"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 20) ? "selected" : ""%>>20
										
										<option value="21"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 21) ? "selected" : ""%>>21
										
										<option value="22"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 22) ? "selected" : ""%>>22
										
										<option value="23"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 23) ? "selected" : ""%>>23
										
										<option value="24"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 24) ? "selected" : ""%>>24
										
										<option value="25"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 25) ? "selected" : ""%>>25
										
										<option value="26"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 26) ? "selected" : ""%>>26
										
										<option value="27"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 27) ? "selected" : ""%>>27
										
										<option value="28"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 28) ? "selected" : ""%>>28
										
										<option value="29"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 29) ? "selected" : ""%>>29
										
										<option value="30"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 30) ? "selected" : ""%>>30
										
										<option value="31"
											<%= (visitData.getTC_HDL_LabresultsDate() != null && visitData.getTC_HDL_LabresultsDate().getDate() == 31) ? "selected" : ""%>>31
										
									</select> </font></td>
								</tr>
								<tr>
									<td><font size="2" face="Arial, Helvetica, sans-serif">LDL</font></td>
									<td><font size="2" face="Arial, Helvetica, sans-serif">
									<input name="LDL" type="text" size="5"
										value="<%=visitData.getLDL() != 0.0? ""+visitData.getLDL(): ""%>">
									mmol/L (goal &lt; 2.5) (NN.N) </font></td>
								</tr>
								<tr class="dash">
									<td>
									<div align="right"><font size="2"
										face="Arial, Helvetica, sans-serif"><em>Date</em></font></div>
									</td>
									<td><font size="2" face="Arial, Helvetica, sans-serif"><em>
									</em></font><font size="2" face="Arial, Helvetica, sans-serif"> <input
										name="LDL_LabresultsDate_year" type="text" size="8"
										value="<%=(visitData.getLDL_LabresultsDate() != null) ? ""+(visitData.getLDL_LabresultsDate().getYear()+1900) : ""%>">
									- <select name="LDL_LabresultsDate_month">
										<option value="1"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getMonth()+1 == 1) ? "selected" : ""%>>Jan
										
										<option value="2"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getMonth()+1 == 2) ? "selected" : ""%>>Feb
										
										<option value="3"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getMonth()+1 == 3) ? "selected" : ""%>>Mar
										
										<option value="4"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getMonth()+1 == 4) ? "selected" : ""%>>Apr
										
										<option value="5"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getMonth()+1 == 5) ? "selected" : ""%>>May
										
										<option value="6"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getMonth()+1 == 6) ? "selected" : ""%>>Jun
										
										<option value="7"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getMonth()+1 == 7) ? "selected" : ""%>>Jul
										
										<option value="8"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getMonth()+1 == 8) ? "selected" : ""%>>Aug
										
										<option value="9"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getMonth()+1 == 9) ? "selected" : ""%>>Sep
										
										<option value="10"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getMonth()+1 == 10) ? "selected" : ""%>>Oct
										
										<option value="11"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getMonth()+1 == 11) ? "selected" : ""%>>Nov
										
										<option value="12"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getMonth()+1 == 12) ? "selected" : ""%>>Dec
										
									</select> - <select name="LDL_LabresultsDate_day">
										<option value="1"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 1) ? "selected" : ""%>>1
										
										<option value="2"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 2) ? "selected" : ""%>>2
										
										<option value="3"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 3) ? "selected" : ""%>>3
										
										<option value="4"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 4) ? "selected" : ""%>>4
										
										<option value="5"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 5) ? "selected" : ""%>>5
										
										<option value="6"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 6) ? "selected" : ""%>>6
										
										<option value="7"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 7) ? "selected" : ""%>>7
										
										<option value="8"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 8) ? "selected" : ""%>>8
										
										<option value="9"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 9) ? "selected" : ""%>>9
										
										<option value="10"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 10) ? "selected" : ""%>>10
										
										<option value="11"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 11) ? "selected" : ""%>>11
										
										<option value="12"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 12) ? "selected" : ""%>>12
										
										<option value="13"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 13) ? "selected" : ""%>>13
										
										<option value="14"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 14) ? "selected" : ""%>>14
										
										<option value="15"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 15) ? "selected" : ""%>>15
										
										<option value="16"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 16) ? "selected" : ""%>>16
										
										<option value="17"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 17) ? "selected" : ""%>>17
										
										<option value="18"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 18) ? "selected" : ""%>>18
										
										<option value="19"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 19) ? "selected" : ""%>>19
										
										<option value="20"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 20) ? "selected" : ""%>>20
										
										<option value="21"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 21) ? "selected" : ""%>>21
										
										<option value="22"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 22) ? "selected" : ""%>>22
										
										<option value="23"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 23) ? "selected" : ""%>>23
										
										<option value="24"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 24) ? "selected" : ""%>>24
										
										<option value="25"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 25) ? "selected" : ""%>>25
										
										<option value="26"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 26) ? "selected" : ""%>>26
										
										<option value="27"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 27) ? "selected" : ""%>>27
										
										<option value="28"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 28) ? "selected" : ""%>>28
										
										<option value="29"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 29) ? "selected" : ""%>>29
										
										<option value="30"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 30) ? "selected" : ""%>>30
										
										<option value="31"
											<%= (visitData.getLDL_LabresultsDate() != null && visitData.getLDL_LabresultsDate().getDate() == 31) ? "selected" : ""%>>31
										
									</select> </font></td>
								</tr>
								<tr>
									<td><font size="2" face="Arial, Helvetica, sans-serif">HDL</font></td>
									<td><font size="2" face="Arial, Helvetica, sans-serif">
									<input name="HDL" type="text" size="5"
										value="<%=visitData.getHDL() != 0.0? ""+ visitData.getHDL(): ""%>">
									mmol/L (goal &gt; 1.0) (N.N) </font></td>
								</tr>
								<tr class="dash">
									<td>
									<div align="right"><em><font size="2"
										face="Arial, Helvetica, sans-serif">Date</font></em></div>
									</td>
									<td><em><font size="2"
										face="Arial, Helvetica, sans-serif"> </font></em><font size="2"
										face="Arial, Helvetica, sans-serif"> <input
										name="HDL_LabresultsDate_year" type="text" size="8"
										value="<%=(visitData.getHDL_LabresultsDate() != null) ? ""+(visitData.getHDL_LabresultsDate().getYear()+1900) : ""%>">
									- <select name="HDL_LabresultsDate_month">
										<option value="1"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getMonth()+1 == 1) ? "selected" : ""%>>Jan
										
										<option value="2"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getMonth()+1 == 2) ? "selected" : ""%>>Feb
										
										<option value="3"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getMonth()+1 == 3) ? "selected" : ""%>>Mar
										
										<option value="4"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getMonth()+1 == 4) ? "selected" : ""%>>Apr
										
										<option value="5"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getMonth()+1 == 5) ? "selected" : ""%>>May
										
										<option value="6"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getMonth()+1 == 6) ? "selected" : ""%>>Jun
										
										<option value="7"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getMonth()+1 == 7) ? "selected" : ""%>>Jul
										
										<option value="8"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getMonth()+1 == 8) ? "selected" : ""%>>Aug
										
										<option value="9"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getMonth()+1 == 9) ? "selected" : ""%>>Sep
										
										<option value="10"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getMonth()+1 == 10) ? "selected" : ""%>>Oct
										
										<option value="11"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getMonth()+1 == 11) ? "selected" : ""%>>Nov
										
										<option value="12"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getMonth()+1 == 12) ? "selected" : ""%>>Dec
										
									</select> - <select name="HDL_LabresultsDate_day">
										<option value="1"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 1) ? "selected" : ""%>>1
										
										<option value="2"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 2) ? "selected" : ""%>>2
										
										<option value="3"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 3) ? "selected" : ""%>>3
										
										<option value="4"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 4) ? "selected" : ""%>>4
										
										<option value="5"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 5) ? "selected" : ""%>>5
										
										<option value="6"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 6) ? "selected" : ""%>>6
										
										<option value="7"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 7) ? "selected" : ""%>>7
										
										<option value="8"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 8) ? "selected" : ""%>>8
										
										<option value="9"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 9) ? "selected" : ""%>>9
										
										<option value="10"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 10) ? "selected" : ""%>>10
										
										<option value="11"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 11) ? "selected" : ""%>>11
										
										<option value="12"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 12) ? "selected" : ""%>>12
										
										<option value="13"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 13) ? "selected" : ""%>>13
										
										<option value="14"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 14) ? "selected" : ""%>>14
										
										<option value="15"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 15) ? "selected" : ""%>>15
										
										<option value="16"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 16) ? "selected" : ""%>>16
										
										<option value="17"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 17) ? "selected" : ""%>>17
										
										<option value="18"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 18) ? "selected" : ""%>>18
										
										<option value="19"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 19) ? "selected" : ""%>>19
										
										<option value="20"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 20) ? "selected" : ""%>>20
										
										<option value="21"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 21) ? "selected" : ""%>>21
										
										<option value="22"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 22) ? "selected" : ""%>>22
										
										<option value="23"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 23) ? "selected" : ""%>>23
										
										<option value="24"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 24) ? "selected" : ""%>>24
										
										<option value="25"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 25) ? "selected" : ""%>>25
										
										<option value="26"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 26) ? "selected" : ""%>>26
										
										<option value="27"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 27) ? "selected" : ""%>>27
										
										<option value="28"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 28) ? "selected" : ""%>>28
										
										<option value="29"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 29) ? "selected" : ""%>>29
										
										<option value="30"
											<%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 30) ? "selected" : ""%>>30
										
										<option
											value="31 <%= (visitData.getHDL_LabresultsDate() != null && visitData.getHDL_LabresultsDate().getDate() == 31) ? "selected" : ""%>">31
										
									</select> </font></td>
								</tr>
								<tr>
									<td><font size="2" face="Arial, Helvetica, sans-serif">A1C</font></td>
									<td><font size="2" face="Arial, Helvetica, sans-serif">
									<input name="A1C" type="text" size="5"
										value="<%=(visitData.getA1C() != 0.0) ? ""+(visitData.getA1C()) : ""%>">
									% (goal &lt; 7.0%) (N.NN) </font></td>
								</tr>
								<tr>
									<td>
									<div align="right"><font size="2"
										face="Arial, Helvetica, sans-serif"><em>Date</em></font></div>
									</td>
									<td><font size="2" face="Arial, Helvetica, sans-serif"><em>
									</em></font><font size="2" face="Arial, Helvetica, sans-serif"> <input
										name="A1C_LabresultsDate_year" type="text" size="8"
										value="<%=(visitData.getA1C_LabresultsDate() != null) ? ""+(visitData.getA1C_LabresultsDate().getYear()+1900) : ""%>">
									- <select name="A1C_LabresultsDate_month">
										<option value="1"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getMonth()+1 == 1) ? "selected" : ""%>>Jan
										
										<option value="2"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getMonth()+1 == 2) ? "selected" : ""%>>Feb
										
										<option value="3"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getMonth()+1 == 3) ? "selected" : ""%>>Mar
										
										<option value="4"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getMonth()+1 == 4) ? "selected" : ""%>>Apr
										
										<option value="5"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getMonth()+1 == 5) ? "selected" : ""%>>May
										
										<option value="6"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getMonth()+1 == 6) ? "selected" : ""%>>Jun
										
										<option value="7"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getMonth()+1 == 7) ? "selected" : ""%>>Jul
										
										<option value="8"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getMonth()+1 == 8) ? "selected" : ""%>>Aug
										
										<option value="9"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getMonth()+1 == 9) ? "selected" : ""%>>Sep
										
										<option value="10"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getMonth()+1 == 10) ? "selected" : ""%>>Oct
										
										<option value="11"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getMonth()+1 == 11) ? "selected" : ""%>>Nov
										
										<option value="12"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getMonth()+1 == 12) ? "selected" : ""%>>Dec
										
									</select> - <select name="A1C_LabresultsDate_day">
										<option value="1"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 1) ? "selected" : ""%>>1
										
										<option value="2"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 2) ? "selected" : ""%>>2
										
										<option value="3"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 3) ? "selected" : ""%>>3
										
										<option value="4"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 4) ? "selected" : ""%>>4
										
										<option value="5"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 5) ? "selected" : ""%>>5
										
										<option value="6"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 6) ? "selected" : ""%>>6
										
										<option value="7"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 7) ? "selected" : ""%>>7
										
										<option value="8"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 8) ? "selected" : ""%>>8
										
										<option value="9"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 9) ? "selected" : ""%>>9
										
										<option value="10"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 10) ? "selected" : ""%>>10
										
										<option value="11"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 11) ? "selected" : ""%>>11
										
										<option value="12"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 12) ? "selected" : ""%>>12
										
										<option value="13"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 13) ? "selected" : ""%>>13
										
										<option value="14"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 14) ? "selected" : ""%>>14
										
										<option value="15"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 15) ? "selected" : ""%>>15
										
										<option value="16"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 16) ? "selected" : ""%>>16
										
										<option value="17"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 17) ? "selected" : ""%>>17
										
										<option value="18"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 18) ? "selected" : ""%>>18
										
										<option value="19"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 19) ? "selected" : ""%>>19
										
										<option value="20"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 20) ? "selected" : ""%>>20
										
										<option value="21"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 21) ? "selected" : ""%>>21
										
										<option value="22"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 22) ? "selected" : ""%>>22
										
										<option value="23"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 23) ? "selected" : ""%>>23
										
										<option value="24"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 24) ? "selected" : ""%>>24
										
										<option value="25"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 25) ? "selected" : ""%>>25
										
										<option value="26"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 26) ? "selected" : ""%>>26
										
										<option value="27"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 27) ? "selected" : ""%>>27
										
										<option value="28"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 28) ? "selected" : ""%>>28
										
										<option value="30"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 29) ? "selected" : ""%>>29
										
										<option value="30"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 30) ? "selected" : ""%>>30
										
										<option value="31"
											<%= (visitData.getA1C_LabresultsDate() != null && visitData.getA1C_LabresultsDate().getDate() == 31) ? "selected" : ""%>>31
										
									</select> </font></td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td height="374" valign="top"><font size="2"
				face="Arial, Helvetica, sans-serif">&nbsp; </font>
			<table width="100%" height="138" cellpadding="1" cellspacing="0">
				<tr bgcolor="#FFFFFF">
					<td height="24" colspan="2" valign="top"><font size="2"
						face="Arial, Helvetica, sans-serif"><strong>Current
					assessment of selected lifestyle goal </strong></font></td>

				</tr>
				<tr>
					<td width="411" valign="top">
					<table width="100%" height="100%" border="0" cellpadding="0"
						cellspacing="3" class="formtable">
						<tr>
							<td valign="top"><font size="2"
								face="Arial, Helvetica, sans-serif">How important is this
							lifestyle change to pt?<br>
							(1-10; 10 = most)</font></td>
							<td valign="top"><font size="2"
								face="Arial, Helvetica, sans-serif"> <input
								name="Change_importance" type="text" size="8"
								value="<%=visitData.getChange_importance() != 0? ""+ visitData.getChange_importance(): ""%>">
							</font></td>
						</tr>
						<tr>
							<td width="307" height="32" valign="top"><font size="2"
								face="Arial, Helvetica, sans-serif">How confident is pt
							in carrying out the lifestyle change? (10 = most)</font></td>
							<td width="55" valign="top"><font size="2"
								face="Arial, Helvetica, sans-serif"> <input
								name="Change_confidence" type="text" size="8"
								value="<%=visitData.getChange_confidence() != 0? ""+ visitData.getChange_confidence(): ""%>">
							</font></td>
						</tr>
					</table>
					</td>
					<td width="457" height="110" valign="top">
					<table width=100% height="100%" border="0" cellpadding="0"
						cellspacing="3" class="formtable">

						<tr>
							<td width="69"><font size="2"
								face="Arial, Helvetica, sans-serif">Exercise</font></td>
							<td width="197"><font size="2"
								face="Arial, Helvetica, sans-serif"> <input
								name="exercise_minPerWk" type="text" size="2"
								value="<%=visitData.getExercise_minPerWk() != 0? ""+ visitData.getExercise_minPerWk(): ""%>">
							min/wk (0 or more) </font></td>
						</tr>
						<tr>
							<td><font size="2" face="Arial, Helvetica, sans-serif">Smoking</font></td>
							<td height="45"><font size="2"
								face="Arial, Helvetica, sans-serif"> <input
								name="smoking_cigsPerDay" type="text" size="2"
								value="<%=visitData.getSmoking_cigsPerDay() != 0? ""+ visitData.getSmoking_cigsPerDay(): ""%>">

							cigs/day (0 or more) </font></td>
						</tr>
						<tr>
							<td><font size="2" face="Arial, Helvetica, sans-serif">Alcohol</font></td>
							<td height="22"><font size="2"
								face="Arial, Helvetica, sans-serif"> <input
								name="alcohol_drinksPerWk" type="text" size="2"
								value="<%=visitData.getAlcohol_drinksPerWk() != 0? ""+ visitData.getAlcohol_drinksPerWk(): ""%>">
							drinks/wk (0 or more) </font></td>
						</tr>

					</table>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellpadding="1" cellspacing="3"
				class="formtable">
				<tr>
					<td><font size="2" face="Arial, Helvetica, sans-serif">DASH
					diet </font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="sel_DashDiet" value="Always"
						<%=(visitData.getSel_DashDiet()!= null && visitData.getSel_DashDiet().equals("Always"))? "checked" :"" %>>
					Always&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" name="sel_DashDiet" value="Often"
						<%=(visitData.getSel_DashDiet()!= null && visitData.getSel_DashDiet().equals("Often"))? "checked" :"" %>>

					Often&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input
						type="radio" name="sel_DashDiet" value="Sometimes"
						<%=(visitData.getSel_DashDiet()!= null && visitData.getSel_DashDiet().equals("Sometimes"))? "checked" :"" %>>
					Sometimes&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" name="sel_DashDiet" value="Never"
						<%=(visitData.getSel_DashDiet()!= null && visitData.getSel_DashDiet().equals("Never"))? "checked" :"" %>>
					Never</font></td>
				</tr>
				<tr>
					<td><font size="2" face="Arial, Helvetica, sans-serif">High
					salt foods </font></td>

					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="sel_HighSaltFood" value="Always"
						<%=(visitData.getSel_HighSaltFood()!=null && visitData.getSel_HighSaltFood().equals("Always"))? "checked" :"" %>>
					Always&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" name="sel_HighSaltFood" value="Often"
						<%=(visitData.getSel_HighSaltFood()!=null && visitData.getSel_HighSaltFood().equals("Often"))? "checked" :"" %>>
					Often &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input
						type="radio" name="sel_HighSaltFood" value="Sometimes"
						<%=(visitData.getSel_HighSaltFood()!=null && visitData.getSel_HighSaltFood().equals("Sometimes"))? "checked" :"" %>>
					Sometimes &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input
						type="radio" name="sel_HighSaltFood" value="Never"
						<%=(visitData.getSel_HighSaltFood()!=null && visitData.getSel_HighSaltFood().equals("Never"))? "checked" :"" %>>
					Never</font></td>

				</tr>
				<tr>
					<td width="169" height="17"><font size="2"
						face="Arial, Helvetica, sans-serif">Overwhelmed or stressed
					</font></td>
					<td width="435"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="radio"
						name="sel_Stressed" value="Always"
						<%=(visitData.getSel_Stressed()!=null && visitData.getSel_Stressed().equals("Always"))? "checked" :"" %>>
					Always&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" name="sel_Stressed" value="Often"
						<%=(visitData.getSel_Stressed()!=null && visitData.getSel_Stressed().equals("Often"))? "checked" :"" %>>
					Often&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input
						type="radio" name="sel_Stressed" value="Sometimes"
						<%=(visitData.getSel_Stressed()!=null && visitData.getSel_Stressed().equals("Sometimes"))? "checked" :"" %>>
					Sometimes&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" name="sel_Stressed" value="Never"
						<%=(visitData.getSel_Stressed()!=null && visitData.getSel_Stressed().equals("Never"))? "checked" :"" %>>
					Never</font></td>
				</tr>
			</table>

			<font size="2" face="Arial, Helvetica, sans-serif">&nbsp; </font>
			<table width="100%" cellpadding="1" cellspacing="1" class="formtable">
				<tr>
					<td colspan="3" class="coldash"><font size="2"
						face="Arial, Helvetica, sans-serif"><strong>Current
					meds </strong></font></td>
					<td colspan="5" class="colsolid">
					<div align="center"><font size="2"
						face="Arial, Helvetica, sans-serif">Rx decision today </font></div>
					</td>
					<td width="83" valign="bottom" class="colsolid">
					<div align="center"></div>
					</td>
				</tr>
				<tr class="dash">
					<td width="146">&nbsp;</td>
					<td width="56"><font size="2"
						face="Arial, Helvetica, sans-serif">Currently Rx'd </font></td>
					<td width="60" class="coldash"><font size="2"
						face="Arial, Helvetica, sans-serif">Side effects </font></td>
					<td width="45"><font size="2"
						face="Arial, Helvetica, sans-serif">Same</font></td>
					<td width="63"><font size="2"
						face="Arial, Helvetica, sans-serif">Increase</font></td>
					<td width="65"><font size="2"
						face="Arial, Helvetica, sans-serif">Decrease</font></td>
					<td width="54"><font size="2"
						face="Arial, Helvetica, sans-serif">Stop</font></td>
					<td width="57"><font size="2"
						face="Arial, Helvetica, sans-serif">Start</font></td>
					<td width="83" valign="bottom"><font size="2"
						face="Arial, Helvetica, sans-serif">In-class switch </font></td>
				</tr>
				<tr class="dash">
					<td><font size="2" face="Arial, Helvetica, sans-serif">Diuretic<br>
					ACE inhibitor</font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="checkbox" name="Diuret_rx" value="Diuret_rx"
						<%=visitData.isDiuret_rx()? "checked" :"" %>> <br>
					<input type="checkbox" name="Ace_rx" value="Ace_rx"
						<%=visitData.isAce_rx()? "checked" :"" %>> </font></td>
					<td class="coldash"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="checkbox"
						name="Diuret_SideEffects" value="Diuret_SideEffects"
						<%=visitData.isDiuret_SideEffects()? "checked" :"" %>> <br>
					<input type="checkbox" name="Ace_SideEffects"
						value="Ace_SideEffects"
						<%=visitData.isAce_SideEffects()? "checked" :"" %>> </font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Diuret_RxDecToday" value="Same"
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked"
						<%=(visitData.getDiuret_RxDecToday()!= null && visitData.getDiuret_RxDecToday().equals("Same")) ? "checked" :"" %>>
					<br>
					<input type="radio" name="Ace_RxDecToday" value="Same"
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked"
						<%=(visitData.getAce_RxDecToday()!=null && visitData.getAce_RxDecToday().equals("Same")) ? "checked" :"" %>>
					<br>
					</font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Diuret_RxDecToday" value="Increase"
						<%=(visitData.getDiuret_RxDecToday()!=null && visitData.getDiuret_RxDecToday().equals("Increase")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Ace_RxDecToday" value="Increase"
						<%=(visitData.getAce_RxDecToday()!=null && visitData.getAce_RxDecToday().equals("Increase")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Diuret_RxDecToday" value="Decrease"
						<%=(visitData.getDiuret_RxDecToday()!=null && visitData.getDiuret_RxDecToday().equals("Decrease")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Ace_RxDecToday" value="Decrease"
						<%=(visitData.getDiuret_RxDecToday()!=null && visitData.getDiuret_RxDecToday().equals("Decrease")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Diuret_RxDecToday" value="Stop"
						<%=(visitData.getDiuret_RxDecToday()!= null && visitData.getDiuret_RxDecToday().equals("Stop")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Ace_RxDecToday" value="Stop"
						<%=(visitData.getAce_RxDecToday()!=null && visitData.getAce_RxDecToday().equals("Stop")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td class="coldash"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="radio"
						name="Diuret_RxDecToday" value="Start"
						<%=(visitData.getDiuret_RxDecToday()!=null && visitData.getDiuret_RxDecToday().equals("Start")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Ace_RxDecToday" value="Start"
						<%=(visitData.getAce_RxDecToday()!=null && visitData.getAce_RxDecToday().equals("Start")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td>
					<div align="left"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="radio"
						name="Diuret_RxDecToday" value="InClassSwitch"
						<%=(visitData.getDiuret_RxDecToday()!=null && visitData.getDiuret_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Ace_RxDecToday" value="InClassSwitch"
						<%=(visitData.getAce_RxDecToday()!=null && visitData.getAce_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></div>
					</td>
				</tr>
				<tr class="dash">
					<td><font size="2" face="Arial, Helvetica, sans-serif">A-II
					receptor antagonist<br>
					Beta blocker<br>
					Calcium channel blocker </font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="checkbox" name="Arecept_rx" value="Arecept_rx"
						<%=visitData.isArecept_rx()? "checked" :"" %>> <br>
					<input type="checkbox" name="Beta_rx" value="Beta_rx"
						<%=visitData.isBeta_rx()? "checked" :"" %>> <br>
					<input type="checkbox" name="Calc_rx" value="Calc_rx"
						<%=visitData.isCalc_rx()? "checked" :"" %>> </font></td>
					<td class="coldash"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="checkbox"
						name="Arecept_SideEffects" value="Arecept_SideEffects"
						<%=visitData.isArecept_SideEffects()? "checked" :"" %>> <br>
					<input type="checkbox" name="Beta_SideEffects"
						value="Beta_SideEffects"
						<%=visitData.isBeta_SideEffects()? "checked" :"" %>> <br>
					<input type="checkbox" name="Calc_SideEffects"
						value="Calc_SideEffects"
						<%=visitData.isCalc_SideEffects()? "checked" :"" %>> </font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Arecept_RxDecToday" value="Same"
						<%=(visitData.getArecept_RxDecToday()!= null && visitData.getArecept_RxDecToday().equals("Same")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Beta_RxDecToday" value="Same"
						<%=(visitData.getBeta_RxDecToday()!=null && visitData.getBeta_RxDecToday().equals("Same")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Calc_RxDecToday" value="Same"
						<%=(visitData.getCalc_RxDecToday()!=null && visitData.getCalc_RxDecToday().equals("Same")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Arecept_RxDecToday" value="Increase"
						<%=(visitData.getArecept_RxDecToday()!=null && visitData.getArecept_RxDecToday().equals("Increase")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Beta_RxDecToday" value="Increase"
						<%=(visitData.getBeta_RxDecToday()!=null && visitData.getBeta_RxDecToday().equals("Increase")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Calc_RxDecToday" value="Increase"
						<%=(visitData.getCalc_RxDecToday()!=null && visitData.getCalc_RxDecToday().equals("Increase")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Arecept_RxDecToday" value="Decrease"
						<%=(visitData.getArecept_RxDecToday()!=null && visitData.getArecept_RxDecToday().equals("Decrease")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Beta_RxDecToday" value="Decrease"
						<%=(visitData.getBeta_RxDecToday()!=null && visitData.getBeta_RxDecToday().equals("Decrease")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Calc_RxDecToday" value="Decrease"
						<%=(visitData.getCalc_RxDecToday()!=null && visitData.getCalc_RxDecToday().equals("Decrease")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Arecept_RxDecToday" value="Stop"
						<%=(visitData.getArecept_RxDecToday()!=null && visitData.getArecept_RxDecToday().equals("Stop")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Beta_RxDecToday" value="Stop"
						<%=(visitData.getBeta_RxDecToday()!=null && visitData.getBeta_RxDecToday().equals("Stop")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Calc_RxDecToday" value="Stop"
						<%=(visitData.getCalc_RxDecToday()!=null && visitData.getCalc_RxDecToday().equals("Stop")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td class="coldash"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="radio"
						name="Arecept_RxDecToday" value="Start"
						<%=(visitData.getArecept_RxDecToday()!=null && visitData.getArecept_RxDecToday().equals("Start")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Beta_RxDecToday" value="Start"
						<%=(visitData.getBeta_RxDecToday()!=null && visitData.getBeta_RxDecToday().equals("Start")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Calc_RxDecToday" value="Start"
						<%=(visitData.getCalc_RxDecToday()!=null && visitData.getCalc_RxDecToday().equals("Start")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td>
					<div align="left"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="radio"
						name="Arecept_RxDecToday" value="InClassSwitch"
						<%=(visitData.getArecept_RxDecToday()!=null && visitData.getArecept_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Beta_RxDecToday" value="InClassSwitch"
						<%=(visitData.getBeta_RxDecToday()!=null && visitData.getBeta_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Calc_RxDecToday" value="InClassSwitch"
						<%=(visitData.getCalc_RxDecToday()!=null && visitData.getCalc_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></div>
					</td>
				</tr>
				<tr class="dash">
					<td><font size="2" face="Arial, Helvetica, sans-serif">Other
					antihypertensive <br>
					Statin<br>
					Other lipid-lowering</font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="checkbox" name="Anti_rx" value="Anti_rx"
						<%=visitData.isAnti_rx()? "checked" :"" %>> <br>
					<input type="checkbox" name="Statin_rx" value="Statin_rx"
						<%=visitData.isStatin_rx()? "checked" :"" %>> <br>
					<input type="checkbox" name="Lipid_rx" value="Lipid_rx"
						<%=visitData.isLipid_rx()? "checked" :"" %>> </font></td>
					<td class="coldash"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="checkbox"
						name="Anti_SideEffects" value="Anti_SideEffects"
						<%=visitData.isAnti_SideEffects()? "checked" :"" %>> <br>
					<input type="checkbox" name="Statin_SideEffects"
						value="Statin_SideEffects"
						<%=visitData.isStatin_SideEffects()? "checked" :"" %>> <br>
					<input type="checkbox" name="Lipid_SideEffects"
						value="Lipid_SideEffects"
						<%=visitData.isLipid_SideEffects()? "checked" :"" %>> </font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Anti_RxDecToday" value="Same"
						<%=(visitData.getAnti_RxDecToday()!=null && visitData.getAnti_RxDecToday().equals("Same")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Statin_RxDecToday" value="Same"
						<%=(visitData.getStatin_RxDecToday()!=null && visitData.getStatin_RxDecToday().equals("Same")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Lipid_RxDecToday" value="Same"
						<%=(visitData.getLipid_RxDecToday()!=null && visitData.getLipid_RxDecToday().equals("Same")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Anti_RxDecToday" value="Increase"
						<%=(visitData.getAnti_RxDecToday()!=null && visitData.getAnti_RxDecToday().equals("Increase")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Statin_RxDecToday" value="Increase"
						<%=(visitData.getStatin_RxDecToday()!=null && visitData.getStatin_RxDecToday().equals("Increase")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Lipid_RxDecToday" value="Increase"
						<%=(visitData.getLipid_RxDecToday()!=null && visitData.getLipid_RxDecToday().equals("Increase")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Anti_RxDecToday" value="Decrease"
						<%=(visitData.getAnti_RxDecToday()!=null && visitData.getAnti_RxDecToday().equals("Decrease")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Statin_RxDecToday" value="Decrease"
						<%=(visitData.getStatin_RxDecToday()!=null && visitData.getStatin_RxDecToday().equals("Decrease")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Lipid_RxDecToday" value="Decrease"
						<%=(visitData.getLipid_RxDecToday()!=null && visitData.getLipid_RxDecToday().equals("Decrease")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Anti_RxDecToday" value="Stop"
						<%=(visitData.getAnti_RxDecToday()!=null && visitData.getAnti_RxDecToday().equals("Stop")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Statin_RxDecToday" value="Stop"
						<%=(visitData.getStatin_RxDecToday()!=null && visitData.getStatin_RxDecToday().equals("Stop")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Lipid_RxDecToday" value="Stop"
						<%=(visitData.getLipid_RxDecToday()!=null && visitData.getLipid_RxDecToday().equals("Stop")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td class="coldash"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="radio"
						name="Anti_RxDecToday" value="Start"
						<%=(visitData.getAnti_RxDecToday()!=null && visitData.getAnti_RxDecToday().equals("Start")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Statin_RxDecToday" value="Start"
						<%=(visitData.getStatin_RxDecToday()!=null && visitData.getStatin_RxDecToday().equals("Start")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Lipid_RxDecToday" value="Start"
						<%=(visitData.getLipid_RxDecToday()!=null && visitData.getLipid_RxDecToday().equals("Start")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td>
					<div align="left"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="radio"
						name="Anti_RxDecToday" value="InClassSwitch"
						<%=(visitData.getAnti_RxDecToday()!=null && visitData.getAnti_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Statin_RxDecToday" value="InClassSwitch"
						<%=(visitData.getStatin_RxDecToday()!=null && visitData.getStatin_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Lipid_RxDecToday" value="InClassSwitch"
						<%=(visitData.getLipid_RxDecToday()!=null && visitData.getLipid_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></div>
					</td>
				</tr>
				<tr>
					<td><font size="2" face="Arial, Helvetica, sans-serif">Oral
					hypoglycemic<br>
					Insulin </font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="checkbox" name="Hypo_rx" value="Hypo_rx"
						<%=visitData.isHypo_rx()? "checked" :"" %>> <br>
					<input type="checkbox" name="Insul_rx" value="Insul_rx"
						<%=visitData.isInsul_rx()? "checked" :"" %>> </font></td>
					<td class="coldash"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="checkbox"
						name="Hypo_SideEffects" value="Hypo_SideEffects"
						<%=visitData.isHypo_SideEffects()? "checked" :"" %>> <br>
					<input type="checkbox" name="Insul_SideEffects"
						value="Insul_SideEffects"
						<%=visitData.isInsul_SideEffects()? "checked" :"" %>> </font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Hypo_RxDecToday" value="Same"
						<%=(visitData.getHypo_RxDecToday()!=null && visitData.getHypo_RxDecToday().equals("Same")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Insul_RxDecToday" value="Same"
						<%=(visitData.getInsul_RxDecToday()!=null && visitData.getInsul_RxDecToday().equals("Same")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Hypo_RxDecToday" value="Increase"
						<%=(visitData.getHypo_RxDecToday()!=null && visitData.getHypo_RxDecToday().equals("Increase")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Insul_RxDecToday" value="Increase"
						<%=(visitData.getInsul_RxDecToday()!=null && visitData.getInsul_RxDecToday().equals("Increase")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Hypo_RxDecToday" value="Decrease"
						<%=(visitData.getHypo_RxDecToday()!=null && visitData.getHypo_RxDecToday().equals("Decrease")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Insul_RxDecToday" value="Decrease"
						<%=(visitData.getInsul_RxDecToday()!=null && visitData.getInsul_RxDecToday().equals("Decrease")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif">
					<input type="radio" name="Hypo_RxDecToday" value="Stop"
						<%=(visitData.getHypo_RxDecToday()!=null && visitData.getHypo_RxDecToday().equals("Stop")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Insul_RxDecToday" value="Stop"
						<%=(visitData.getInsul_RxDecToday()!=null && visitData.getInsul_RxDecToday().equals("Stop")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td class="coldash"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="radio"
						name="Hypo_RxDecToday" value="Start"
						<%=(visitData.getHypo_RxDecToday()!=null && visitData.getHypo_RxDecToday().equals("Start")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Insul_RxDecToday" value="Start"
						<%=(visitData.getInsul_RxDecToday()!=null && visitData.getInsul_RxDecToday().equals("Start")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></td>
					<td>
					<div align="left"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="radio"
						name="Hypo_RxDecToday" value="InClassSwitch"
						<%=(visitData.getHypo_RxDecToday()!=null && visitData.getHypo_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					<br>
					<input type="radio" name="Insul_RxDecToday" value="InClassSwitch"
						<%=(visitData.getInsul_RxDecToday()!=null && visitData.getInsul_RxDecToday().equals("InClassSwitch")) ? "checked" :"" %>
						onclick="this.__chk = this.__chk ? this.checked = !this.__chk : this.checked">
					</font></div>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellpadding="1" cellspacing="1"
				class="formtable">
				<tr>
					<td width="189" height="35"><font size="2"
						face="Arial, Helvetica, sans-serif">How often does pt miss
					taking his/her meds? </font></td>
					<td width="194"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input
						name="Often_miss" type="text" size="2"
						value="<%=visitData.getOften_miss() != 0? ""+ visitData.getOften_miss(): ""%>">
					/wk (0 or more) </font></td>
					<td width="156"><font size="2"
						face="Arial, Helvetica, sans-serif">Does pt take any herbal
					remedies? </font></td>
					<td width="198"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="radio"
						name="Herbal" value="yes"
						<%=(visitData.getHerbal()!=null && visitData.getHerbal().equals("yes"))? "checked" :"" %>>
					Y <input type="radio" name="Herbal" value="no"
						<%=(visitData.getHerbal()!=null && visitData.getHerbal().equals("no"))? "checked" :"" %>>
					N </font></td>
				</tr>

			</table>
			<br>
			<table width="100%" cellpadding="1" cellspacing="3" class="formtable">
				<tr class="dash">
					<td height="32" colspan="3"><font size="2"
						face="Arial, Helvetica, sans-serif"><strong>Plan</strong>&nbsp;&nbsp;
					<u>Next visit</u> in <input type="radio" name="Nextvisit"
						value="Under1Mo"
						<%=(visitData.getNextvisit()!=null && visitData.getNextvisit().equals("Under1Mo")) ? "checked" :"" %>>
					&lt; 1 mo <input type="radio" name="Nextvisit" value="1to2Mo"
						<%=(visitData.getNextvisit()!=null && visitData.getNextvisit().equals("1to2Mo")) ? "checked" :"" %>>

					1 - 2 mo <input type="radio" name="Nextvisit" value="3to6Mo"
						<%=(visitData.getNextvisit()!=null && visitData.getNextvisit().equals("3to6Mo")) ? "checked" :"" %>>
					3 - 6 mo <input type="radio" name="Nextvisit" value="Over6Mo"
						<%=(visitData.getNextvisit()!=null && visitData.getNextvisit().equals("Over6Mo")) ? "checked" :"" %>>
					&gt; 6 mo</font></td>
				</tr>
				<tr>
					<td height="21" class="coldash"><font size="2"
						face="Arial, Helvetica, sans-serif"><u>Tools
					provided/discussed</u></font></td>

					<td class="coldash"><font size="2"
						face="Arial, Helvetica, sans-serif"><u>BP monitoring </u> </font></td>
					<td><font size="2" face="Arial, Helvetica, sans-serif"><u>Resources
					and referrals </u></font></td>
				</tr>
				<tr valign="top">
					<td width="228" class="coldash"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="checkbox"
						name="Bpactionplan" value="Bpactionplan"
						<%=visitData.isBpactionplan()? "checked" :"" %>> BP Action
					Plan e-tool <br>
					<input type="checkbox" name="PressureOff" value="PressureOff"
						<%=visitData.isPressureOff()? "checked" :"" %>> Take the
					Pressure Off book <br>
					<input type="checkbox" name="PatientProvider"
						value="PatientProvider"
						<%=visitData.isPatientProvider()? "checked" :"" %>>
					Patient-Provider agreement <br>
					</font></td>
					<td width="185" class="coldash"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="checkbox"
						name="ABPM" value="ABPM" <%=visitData.isABPM()? "checked" :"" %>>
					ABPM<br>
					<input type="checkbox" name="Home" value="Home"
						<%=visitData.isHome()? "checked" :"" %>> Home</font></td>
					<td width="316"><font size="2"
						face="Arial, Helvetica, sans-serif"> <input type="checkbox"
						name="CommunityRes" value="CommunityRes"
						<%=visitData.isCommunityRes()? "checked" :"" %>> Offer
					community resources <br>
					<input type="checkbox" name="ProRefer" value="ProRefer"
						<%=visitData.isProRefer()? "checked" :"" %>> Refer to
					health professional </font></td>

				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td height="115" valign="bottom">
			<p align="right"><input type="submit" name="Save" value="Save">
			<input type="submit" name="Save" value="Save and Exit"> <input
				type="button" name="Reset" value="Reset" onClick="confirmReset()">
			<input type="button" name="Close" value="Exit"
				onClick="confirmExit(document.FrmForm)"> <%String patient=patientData.getPatient_Id();
              String user=(String)session.getAttribute("user");%> <input
				type="button"
				onclick="popupXmlMsg('<html:rewrite action="/form/HSFOXmlTransfer.do" />?xmlHsfoProviderNo=<%=user%>&xmlHsfoDemographicNo=<%=patient%>')"
				value="Submit current patient's data"></p>
			</td>
		</tr>

	</table>
	<p align="center"><font size="2"
		face="Arial, Helvetica, sans-serif">Copyright &copy; 2006 Heart
	&amp; Stroke Foundation.</font></p>
</html:form>

<script language="JavaScript" type="text/javascript">
function confirmReset()
{
        form1 = document.getElementById('form1');
        
	if(window.confirm("Are you SURE you want to clear all form data? Click OK to continue, Cancel to return to the form and save."))
	{ 
        
	form1.reset();
	}
	else
	return false;
}

function confirmExit(frm){
	if(window.confirm("Are you SURE you want to exit without saving? Click OK to continue, Cancel to return to the form and save."))
	{
	//closing script
	}
	else
	return false;
}
function checkform (){
form1 = document.getElementById('form1');

        if (form1.LName.value == "") {
    alert( "Please enter patient's last name." );
    form1.LName.focus();
    return false ;
  }
  if (form1.FName.value == "") {
    alert( "Please enter patient's first name." );
    form1.FName.focus();
    return false ;
  }
  if (form1.Patient_Id.value == "") {
    alert( "Please enter patient's registration ID." );
    form1.Patient_Id.focus();
    return false ;
  }

  if (form1.Height.value == "") {
    alert( "Please enter patient's height." );
    form1.Height.focus();
    return false ;
  } else if (isNaN(parseFloat(form1.Height.value))) {
    alert( "Please enter patient's height in NNN.N format." );
    form1.Height.focus();
    return false ;
  } else if (form1.Height.value <40 || form1.Height.value > 250) {
    alert( "Please enter patient's height between 40 and 250." );
    form1.Height.focus();
    return false ;
	}
	
	if (!/^\d\d\d\d$/.test(form1.consentDate_year.value) || form1.consentDate_year.value=="") {
    alert( "Please enter patient's year of consent." );
    form1.consentDate_year.focus();
    return false ;
	}
	if (!/^\d\d\d\d$/.test(form1.BirthDate_year.value) || form1.BirthDate_year.value=="") {
    alert( "Please enter patient's year of birth." );
    form1.BirthDate_year.focus();
    return false ;
	}
	if (!/^\d\d\d\d$/.test(form1.VisitDate_Id_year.value) || form1.VisitDate_Id_year.value=="") {
    alert( "Please enter patient's year of last visit." );
    form1.VisitDate_Id_year.focus();
    return false ;
	}
	if (form1.EmrHCPId1.value=="") {
    alert( "Please enter patient's Study ID of MD." );
    form1.EmrHCPId1.focus();
    return false ;
	}
	if (form1.EmrHCPId2.value=="") {
    alert( "Please enter patient's MD's last name." );
    form1.EmrHCPId2.focus();
    return false ;
	}
	if (form1.PostalCode.value=="" || !/^\D\d\D$/.test(form1.PostalCode.value)) {
    alert( "Please enter patient's postal code." );
    form1.PostalCode.focus();
    return false ;
	}
	if (!form1.Sex[0].checked &&
	!form1.Sex[1].checked) {
	alert( "Please select patient's sex." );
	return false;
	}
	if (!form1.Height_unit[0].checked &&
	!form1.Height_unit[1].checked) {
	alert( "Please select patient's height measurement." );
        form1.Height_unit[0].focus();
	return false;
	}
	if (form1.PharmacyName.value=="") {
    alert( "Please enter patient's pharmacy name." );
    form1.PharmacyName.focus();
    return false ;
	}
	if (form1.PharmacyLocation.value=="") {
    alert( "Please enter patient's pharmacy address." );
    form1.PharmacyLocation.focus();
    return false ;
	}
	if (!form1.Ethnic_White.checked && !form1.Ethnic_Black.checked && !form1.Ethnic_EIndian.checked && !form1.Ethnic_Pakistani.checked && !form1.Ethnic_SriLankan.checked && !form1.Ethnic_Bangladeshi.checked && !form1.Ethnic_Chinese.checked && !form1.Ethnic_Japanese.checked && !form1.Ethnic_Korean.checked && !form1.Ethnic_FirstNation.checked && !form1.Ethnic_Other.checked && !form1.Ethnic_Hispanic.checked && !form1.Ethnic_Refused.checked && !form1.Ethnic_Unknown.checked){
	alert( "Please select patient's ethnicity." );
    form1.PharmacyLocation.focus();
    return false ;
	}
	if (!form1.sel_TimeAgoDx[0].checked &&
	!form1.sel_TimeAgoDx[1].checked && !form1.sel_TimeAgoDx[2].checked) {
	alert( "Please select patient's diagnosis period." );
	return false;
	}
	if (!form1.Change_importance.value=="" && (form1.Change_importance.value<1 || form1.Change_importance.value>10 || /\D/.test(form1.Change_importance.value))) {
    alert( "Please check lifestyle change is between 1 and 10" );
    form1.Change_importance.focus();
    return false ;
	}
	if (!form1.Change_confidence.value=="" && (form1.Change_confidence.value<1 || form1.Change_confidence.value>10 || /\D/.test(form1.Change_confidence.value))) {
    alert( "Please check confidence is between 1 and 10" );
    form1.Change_confidence.focus();
    return false ;
	}
	
        if (form1.consentDate_year.value<2007) {
    alert( "Please check that the year of consent is greater than 2006" );
    form1.consentDate_year.focus();
    return false ;
	}
	if (form1.BirthDate_year.value<1901) {
    alert( "Please check that the year of birth is greater than 1900" );
    form1.BirthDate_year.focus();
    return false ;
	}
   
	if (!/^\d\d\d\d$/.test(form1.VisitDate_Id_year.value) || form1.VisitDate_Id_year.value=="") {
    alert( "Please enter patient's year of last visit." );
    form1.VisitDate_Id_year.focus();
    return false ;
	}
	if (!form1.SBP.value =="" && (form1.SBP.value<40 || form1.SBP.value>300 || isNaN(parseFloat(form1.SBP.value)))) {
    alert( "Please check that SBP is between 40 and 300" );
    form1.SBP.focus();
    return false ;
	}
	if (!form1.DBP.value =="" && (form1.DBP.value<0 || form1.DBP.value>140 || isNaN(parseFloat(form1.DBP.value)))) {
    alert( "Please check that DBP is between 0 and 140" );
    form1.DBP.focus();
    return false ;
	}
	if (!form1.SBP_goal.value =="" && (form1.SBP_goal.value<40 || form1.SBP_goal.value>300 || isNaN(parseFloat(form1.SBP_goal.value)))) {
    alert( "Please check that SBP goal is between 40 and 300" );
    form1.SBP_goal.focus();
    return false ;
	}
	if (!form1.DBP_goal.value =="" && (form1.DBP_goal.value<0 || form1.DBP_goal.value>140 || isNaN(parseFloat(form1.DBP_goal.value)))) {
    alert( "Please check that DBP goal is between 0 and 140" );
    form1.DBP_goal.focus();
    return false ;
	}
	if (!form1.Weight.value =="" && (form1.Weight.value<35 || form1.Weight.value>500 || isNaN(parseFloat(form1.Weight.value)))) {
    alert( "Please check that weight is between 35 and 500" );
    form1.Weight.focus();
    return false ;
	}
	//add by victor for waist unit null bug
	if (form1.Weight.value !="" && !form1.Weight_unit[0].checked &&!form1.Weight_unit[1].checked)
	{
	alert( "Please choose the weight unit" );
    form1.Weight.focus();
    }
	if (form1.Waist.value !="" && !form1.Waist_unit[0].checked &&!form1.Waist_unit[1].checked)
	{
	alert( "Please choose the waist unit" );
    form1.Waist.focus();
    }
	//end of add
	if (!form1.Waist.value =="" && (form1.Waist.value<15 || form1.Waist.value>200 || isNaN(parseFloat(form1.Waist.value)))) {
    alert( "Please check that waist is between 15 and 200" );
    form1.Waist.focus();
    return false ;
	}
	if (!form1.TC_HDL.value =="" && (form1.TC_HDL.value<1 || form1.TC_HDL.value>30 || isNaN(parseFloat(form1.TC_HDL.value)))) {
    alert( "Please check that TC_HDL is between 1 and 30" );
    form1.TC_HDL.focus();
    return false ;
	}
	if (!form1.LDL.value=="" && (form1.LDL.value < 0.5 || form1.LDL.value>10 || isNaN(parseFloat(form1.LDL.value)))) {
    alert( "Please check that LDL is between 0.5 and 10" );
    form1.LDL.focus();
    return false ;
	}
	if (!form1.HDL.value =="" && (form1.HDL.value<0.4 || form1.HDL.value>8 || isNaN(parseFloat(form1.HDL.value)))) {
    alert( "Please check that HDL is between 0.4 and 8" );
    form1.HDL.focus();
    return false ;
	}
	if (!form1.A1C.value =="" && (form1.A1C.value<4 || form1.A1C.value>25 || isNaN(parseFloat(form1.A1C.value)))) {
    alert( "Please check that A1C is between 4 and 25" );
    form1.A1C.focus();
    return false ;
	}
	if (!form1.exercise_minPerWk.value =="" && (form1.exercise_minPerWk.value<0 || form1.exercise_minPerWk.value>999 || isNaN(parseFloat(form1.exercise_minPerWk.value)))) {
    alert( "Please check that exercise per week is between 0 and 999" );
    form1.exercise_minPerWk.focus();
    return false ;
	}
	if (!form1.smoking_cigsPerDay.value =="" && (form1.smoking_cigsPerDay.value<0 || form1.smoking_cigsPerDay.value>99 || isNaN(parseFloat(form1.smoking_cigsPerDay.value)))) {
    alert( "Please check that smoking per day is between 0 and 99" );
    form1.smoking_cigsPerDay.focus();
    return false ;
	}
	if (!form1.alcohol_drinksPerWk.value =="" && (form1.alcohol_drinksPerWk.value<0 || form1.alcohol_drinksPerWk.value>99 || isNaN(parseFloat(form1.alcohol_drinksPerWk.value)))) {
    alert( "Please check that alcohol per week is between 0 and 99" );
    form1.alcohol_drinksPerWk.focus();
    return false ;
	}
	if (!form1.Often_miss.value =="" && (form1.Often_miss.value<0 || form1.Often_miss.value>42 || isNaN(parseFloat(form1.Often_miss.value)))) {
    alert( "Please check that meds missed per week is between 0 and 42" );
    form1.Often_miss.focus();
    return false ;
	}
  return true ;
}
function checkdate(date)
{
	var datevalue;
	switch (date){
		case "Jan-07": 
		datevalue = 0;
		break;
		case "Feb-07": 
		datevalue = 0.33;
		break;
		case "Mar-07": 
		datevalue = 0.66;
		break;
		case "Apr-07": 
		datevalue = 1;
		break;
		case "May-07": 
		datevalue = 1.33;
		break;
		case "Jun-07": 
		datevalue = 1.66;
		break;
		case "Jul-07": 
		datevalue = 2;
		break;
		case "Aug-07": 
		datevalue = 2.33;
		break;
		case "Sep-07": 
		datevalue = 2.66;
		break;
		case "Oct-07": 
		datevalue = 3;
		break;
		case "Nov-07": 
		datevalue = 3.33;
		break;
		case "Dec-07": 
		datevalue = 3.66;
		break;
		case "Jan-08": 
		datevalue = 4;
		break;
		case "Feb-08": 
		datevalue = 4.33;
		break;
		case "Mar-08": 
		datevalue = 4.66;
		break;
		case "Apr-08": 
		datevalue = 5;
		break;
		case "May-08": 
		datevalue = 5.33;
		break;
		case "Jun-08": 
		datevalue = 5.66;
		break;
		case "Jul-08": 
		datevalue = 6;
		break;
		case "Aug-08": 
		datevalue = 6.33;
		break;
		case "Sep-08": 
		datevalue = 6.66;
		break;
		case "Oct-08": 
		datevalue = 7;
		break;
		case "Nov-08": 
		datevalue = 7.33;
		break;
		case "Dec-08": 
		datevalue = 7.66;
		break;
		case "Jan-08": 
		datevalue = 8;
		break;
		case "Feb-08": 
		datevalue = 8.33;
		break;
		case "Mar-08": 
		datevalue = 8.66;
		break;
		case "Apr-08": 
		datevalue = 9;
		break;
		case "May-08": 
		datevalue = 9.33;
		break;
		case "Jun-08": 
		datevalue = 9.66;
		break;
		case "Jul-08": 
		datevalue = 10;
		break;
		}
		return datevalue;
}
<%--	var GraphBMI = new XYGraph(); // define new XYGraph object
	var LineBMI = new XYLine();   // define new XYLine object
	var LineWaist = new XYLine();
	
	var GraphBp = new XYGraph();
	var Linesbp = new XYLine();
	var Linedbp = new XYLine();
	
	var GraphTCHDL = new XYGraph();
	var LineTCHDL = new XYLine();
	var LineLDL = new XYLine();
	
	var GraphImportance = new XYGraph();
	var LineImportance = new XYLine();
	var LineConfidence = new XYLine();
	
	GraphImportance.ymax = 10;
	GraphImportance.xmax = 10;
	GraphImportance.yaxis = "Importance";
	GraphImportance.yaxis2 = "Confidence";
	GraphImportance.xscale=1;
	GraphImportance.num=0;
	GraphImportance.userxticlabels=["'07 Jan","","'07 Jul","","'08 Jan","","'08 Jul","","'09 Jan","","'09 Jul"];
	var LineImportancex = "<%=LineImportancex%>";
	var LineImportanceDate = LineImportancex.split(",");
	var LineImportancey = "<%=LineImportancey%>";
	LineImportance.y = LineImportancey.split(",");
	for (i=0; i< LineImportance.y.length; i++){
		LineImportance.x[i] = checkdate(LineImportanceDate[i]);
	}
	var LineConfidencex = "<%=LineConfidencex%>";
	var LineConfidenceDate = LineConfidencex.split(",");
	var LineConfidencey = "<%=LineConfidencey%>";
	LineConfidence.y = LineConfidencey.split(",");
	for (i=0; i< LineConfidence.y.length; i++){
		LineConfidence.x[i] = checkdate(LineConfidenceDate[i]);
	}
	LineConfidence.VMLpointshapetype="circle";
	LineConfidence.VMLstroke = "weight='1pt'; color='red'; dashstyle='solid';";
	LineConfidence.pointfillcolor="red";	// point fill color
	LineConfidence.pointstrokecolor="red";	// point line color
	
	GraphTCHDL.ymax = 10;
	GraphTCHDL.xmax = 10;
	GraphTCHDL.yaxis = "TC/HDL";
	GraphTCHDL.yaxis2 = "LDL";
	GraphTCHDL.xscale=1;
	GraphTCHDL.num=0;
	GraphTCHDL.userxticlabels=["'07 Jan","","'07 Jul","","'08 Jan","","'08 Jul","","'09 Jan","","'09 Jul"];
	var LineTCHDLx = "<%=LineTCHDLx%>";
	var LineTCHDLDate = LineTCHDLx.split(",");
	var LineTCHDLy = "<%=LineTCHDLy%>";
	LineTCHDL.y = LineTCHDLy.split(",");
	for (i=0; i< LineTCHDL.y.length; i++){
		LineTCHDL.x[i] = checkdate(LineTCHDLDate[i]);
	}
	var LineLDLx = "<%=LineLDLx%>";
	var LineLDLDate = LineLDLx.split(",");
	var LineLDLy = "<%=LineLDLy%>";
	LineLDL.y = LineLDLy.split(",");
	for (i=0; i< LineLDL.y.length; i++){
		LineLDL.x[i] = checkdate(LineLDLDate[i]);
	}
	LineLDL.VMLpointshapetype="circle";
	LineLDL.VMLstroke = "weight='1pt'; color='red'; dashstyle='solid';";
	LineLDL.pointfillcolor="red";	// point fill color
	LineLDL.pointstrokecolor="red";	// point line color
	
	GraphBp.ymax = 250;
	GraphBp.xmax = 10;
	GraphBp.yaxis = "SBP(mm Hg)";
	GraphBp.yaxis2 = "DBP(mm Hg)";
	GraphBp.xscale=1;
	GraphBp.num=0;
	GraphBp.userxticlabels=["'07 Jan","","'07 Jul","","'08 Jan","","'08 Jul","","'09 Jan","","'09 Jul"];
	var Linesbpx = "<%=Linesbpx%>";
	var LinesbpDate = Linesbpx.split(",");
	var Linesbpy = "<%=Linesbpy%>";
	Linesbp.y = Linesbpy.split(",");
	
	var Linedbpx = "<%=Linedbpx%>";
	var LinedbpDate = Linedbpx.split(",");
	var Linedbpy = "<%=Linedbpy%>";
	Linedbp.y = Linedbpy.split(",");
	for (i=0; i< Linesbp.y.length; i++){
		Linesbp.x[i] = checkdate(LinesbpDate[i]);
	}
	for (i=0; i< Linedbp.y.length; i++){
		Linedbp.x[i] = checkdate(LinedbpDate[i]);
	}
	
	Linedbp.VMLpointshapetype="circle";
	Linedbp.VMLstroke = "weight='1pt'; color='red'; dashstyle='solid';";
	Linedbp.pointfillcolor="red";	// point fill color
	Linedbp.pointstrokecolor="red";	// point line color
	
	LineWaist.axis2 = true;
	var LineWaistx = "<%=LineWaistx%>";
	var LineWaistDate = LineWaistx.split(",");
	var LineWaisty = "<%=LineWaisty%>";
	LineWaist.y = LineWaisty.split(",");
	for (i=0; i< LineWaist.y.length; i++){
		LineWaist.x[i] = checkdate(LineWaistDate[i]);
	}
	LineWaist.VMLpointshapetype="circle";
	LineWaist.VMLstroke = "weight='1pt'; color='red'; dashstyle='solid';";
	LineWaist.pointfillcolor="red";	// point fill color
	LineWaist.pointstrokecolor="red";	// point line color

	var LineBMIx = "<%=LineBMIx%>";
	var LineBMIDate = LineBMIx.split(",");
	var LineBMIy = "<%=LineBMIy%>";
	LineBMI.y = LineBMIy.split(",");
	for (i=0; i< LineBMI.y.length; i++){
		LineBMI.x[i] = checkdate(LineBMIDate[i]);
	}
	GraphBMI.xmax = 10;
	GraphBMI.yaxis = "BMI";
	GraphBMI.yaxis2 = "Waist (cm)";
	GraphBMI.ymax = 50;
	GraphBMI.xscale=1;
	GraphBMI.userxticlabels=["'07 Jan","","'07 Jul","","'08 Jan","","'08 Jul","","'09 Jan","","'09 Jul"];
	GraphBMI.num=50;
	function Calculate() {
	graphdiv1.innerHTML=GraphBMI.Plot(LineBMI, LineWaist);
	graphdiv2.innerHTML=GraphBp.Plot(Linesbp, Linedbp);
	graphdiv3.innerHTML=GraphTCHDL.Plot(LineTCHDL, LineLDL);
	graphdiv4.innerHTML=GraphImportance.Plot(LineImportance, LineConfidence);}
        --%>
</script>

</body>
</html:html>
<%!

String getDisplayIfMore1(Object[] s){
    if(s != null && s.length > 1){
        return "";
    }else{
        return "style=\"display:none;\" ";
    }           
}

String getDisplayIfLess1(Object[] s){
    if(s == null || s.length < 1){
        return "";
    }else{
        return "style=\"display:none;\" ";
    }
} 
%>
