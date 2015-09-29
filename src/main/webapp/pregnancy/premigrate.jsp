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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="oscar.util.*" %>

<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="java.util.*"%>
<%@page import="org.oscarehr.common.model.Episode" %>
<%@page import="org.oscarehr.common.dao.EpisodeDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>

<%
	Properties props = (Properties)request.getAttribute("props");
%>
<html:html locale="true">
<head>
<script src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js" type="text/javascript"></script>

<title>Migration Tool</title>


<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/css/OscarStandardLayout.css">
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/share/javascript/Oscar.js"></script>
<script src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">
<style>
div#demo
{
	margin-left: auto;
	margin-right: auto;
	width: 90%;
	text-align: left;
}
</style>

<script>
$(document).ready(function() {		
	$("select[name='c_province']").val('<%= UtilMisc.htmlEscape(props.getProperty("c_province", "")) %>');
	$("select[name='pg1_maritalStatus']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_maritalStatus", "")) %>');
	$("select[name='pg1_language']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_language", "")) %>');		
	$("select[name='pg1_partnerEduLevel']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_partnerEduLevel", "")) %>');
	
	$("select[name='pg1_eduLevel']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_eduLevel", "")) %>');
	
	$("select[name='pg1_ethnicBgMother']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_ethnicBgMother", "")) %>');
	$("select[name='pg1_ethnicBgFather']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_ethnicBgFather", "")) %>');
	$("select[name='c_hinType']").val('<%= UtilMisc.htmlEscape(props.getProperty("c_hinType", "")) %>');
	
	$("select[name='pg1_box3']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_box3", "")) %>');		
	$("select[name='pg1_labHIV']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labHIV", "")) %>');
	$("select[name='pg1_labABO']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labABO", "")) %>');
	$("select[name='pg1_labRh']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labRh", "")) %>');
	$("select[name='pg1_labGC']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labGC", "")) %>');
	$("select[name='pg1_labChlamydia']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labChlamydia", "")) %>');
	$("select[name='pg1_labHBsAg']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labHBsAg", "")) %>');
	$("select[name='pg1_labVDRL']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labVDRL", "")) %>');
	$("select[name='pg1_labSickle']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labSickle", "")) %>');
	$("select[name='pg1_labRubella']").val('<%= UtilMisc.htmlEscape(props.getProperty("pg1_labRubella", "")) %>');

	$("select[name='ar2_strep']").val('<%= UtilMisc.htmlEscape(props.getProperty("ar2_strep", "")) %>');
	$("select[name='ar2_bloodGroup']").val('<%= UtilMisc.htmlEscape(props.getProperty("ar2_bloodGroup", "")) %>');
	$("select[name='ar2_rh']").val('<%=  UtilMisc.htmlEscape(props.getProperty("ar2_rh", "")) %>');
	
	$("input[name='ar2_lab2GTT1']").bind('keyup',function(){
		updateGtt();
	});
	$("input[name='ar2_lab2GTT2']").bind('keyup',function(){
		updateGtt();
	});
	$("input[name='ar2_lab2GTT3']").bind('keyup',function(){
		updateGtt();
	});
	
	var gttVal = $("input[name='ar2_lab2GTT']").val();
	if(gttVal.length > 0) {
		var parts = gttVal.split("/");
		$("input[name='ar2_lab2GTT1']").val(parts[0]);
		$("input[name='ar2_lab2GTT2']").val(parts[1]);
		$("input[name='ar2_lab2GTT3']").val(parts[2]);
	}

	
});


function updateGtt() {
	$("input[name='ar2_lab2GTT']").val($("input[name='ar2_lab2GTT1']").val() + "/" + $("input[name='ar2_lab2GTT2']").val() + "/" + $("input[name='ar2_lab2GTT3']").val());
}
</script>
</head>

<body>

	<br/>
	<h2 style="text-align:center">Pregnancy Migration Tool</h2>
	<br/>
<%
	if(request.getAttribute("error") != null) {
%>
	<h2 style="color:red"><%=request.getAttribute("error") %></h2>
<% 
	return;
	} 
%>

	<h3>Values on the left side are from the existing AR2005 form. Please set drop down boxes on right.</h3>

	<form action="Pregnancy.do">
		<input type="hidden" name="method" value="doMigrate"/>
		<input type="hidden" name="demographicNo" value="<%=request.getParameter("demographicNo")%>"/>
		<fieldset>
			<table>
				<thead>
					<tr>
						<th>Field</th>
						<th>Value in current form</th>
						<th>Value to set in <b>Enhanced</b> form</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>Province:</td>
						<td><input type="text" name="c_province_old" value="<%=props.getProperty("c_province","")%>"/></td>
						<td>
			
			
				<select name="c_province" style="width: 100%">
					<option value="AB" >AB-Alberta</option>
					<option value="BC" >BC-British Columbia</option>
					<option value="MB" >MB-Manitoba</option>
					<option value="NB" >NB-New Brunswick</option>
					<option value="NL" >NL-Newfoundland Labrador</option>
					<option value="NT" >NT-Northwest Territory</option>
					<option value="NS" >NS-Nova Scotia</option>
					<option value="NU" >NU-Nunavut</option>
					<option value="ON" >ON-Ontario</option>
					<option value="PE" >PE-Prince Edward Island</option>
					<option value="QC" >QC-Quebec</option>
					<option value="SK" >SK-Saskatchewan</option>
					<option value="YT" >YT-Yukon</option>
					<option value="US" >US resident</option>
					<option value="US-AK" >US-AK-Alaska</option>
					<option value="US-AL" >US-AL-Alabama</option>
					<option value="US-AR" >US-AR-Arkansas</option>
					<option value="US-AZ" >US-AZ-Arizona</option>
					<option value="US-CA" >US-CA-California</option>
					<option value="US-CO" >US-CO-Colorado</option>
					<option value="US-CT" >US-CT-Connecticut</option>
					<option value="US-CZ" >US-CZ-Canal Zone</option>
					<option value="US-DC" >US-DC-District Of Columbia</option>
					<option value="US-DE" >US-DE-Delaware</option>
					<option value="US-FL" >US-FL-Florida</option>
					<option value="US-GA" >US-GA-Georgia</option>
					<option value="US-GU" >US-GU-Guam</option>
					<option value="US-HI" >US-HI-Hawaii</option>
					<option value="US-IA" >US-IA-Iowa</option>
					<option value="US-ID" >US-ID-Idaho</option>
					<option value="US-IL" >US-IL-Illinois</option>
					<option value="US-IN" >US-IN-Indiana</option>
					<option value="US-KS" >US-KS-Kansas</option>
					<option value="US-KY" >US-KY-Kentucky</option>
					<option value="US-LA" >US-LA-Louisiana</option>
					<option value="US-MA" >US-MA-Massachusetts</option>
					<option value="US-MD" >US-MD-Maryland</option>
					<option value="US-ME" >US-ME-Maine</option>
					<option value="US-MI" >US-MI-Michigan</option>
					<option value="US-MN" >US-MN-Minnesota</option>
					<option value="US-MO" >US-MO-Missouri</option>
					<option value="US-MS" >US-MS-Mississippi</option>
					<option value="US-MT" >US-MT-Montana</option>
					<option value="US-NC" >US-NC-North Carolina</option>
					<option value="US-ND" >US-ND-North Dakota</option>
					<option value="US-NE" >US-NE-Nebraska</option>
					<option value="US-NH" >US-NH-New Hampshire</option>
					<option value="US-NJ" >US-NJ-New Jersey</option>
					<option value="US-NM" >US-NM-New Mexico</option>
					<option value="US-NU" >US-NU-Nunavut</option>
					<option value="US-NV" >US-NV-Nevada</option>
					<option value="US-NY" >US-NY-New York</option>
					<option value="US-OH" >US-OH-Ohio</option>
					<option value="US-OK" >US-OK-Oklahoma</option>
					<option value="US-OR" >US-OR-Oregon</option>
					<option value="US-PA" >US-PA-Pennsylvania</option>
					<option value="US-PR" >US-PR-Puerto Rico</option>
					<option value="US-RI" >US-RI-Rhode Island</option>
					<option value="US-SC" >US-SC-South Carolina</option>
					<option value="US-SD" >US-SD-South Dakota</option>
					<option value="US-TN" >US-TN-Tennessee</option>
					<option value="US-TX" >US-TX-Texas</option>
					<option value="US-UT" >US-UT-Utah</option>
					<option value="US-VA" >US-VA-Virginia</option>
					<option value="US-VI" >US-VI-Virgin Islands</option>
					<option value="US-VT" >US-VT-Vermont</option>
					<option value="US-WA" >US-WA-Washington</option>
					<option value="US-WI" >US-WI-Wisconsin</option>
					<option value="US-WV" >US-WV-West Virginia</option>
					<option value="US-WY" >US-WY-Wyoming</option>
					<option value="OT" >Other</option>
				</select>						
						</td>
					</tr>
					
					<tr>
						<td>Language:</td>
						<td><input type="text" name="pg1_language_old" value="<%=props.getProperty("pg1_language","")%>"/></td>
						<td>
					<select name="pg1_language"  style="width: 100%">
						<option value="ENG">English</option>
						<option value="FRA">French</option>
						<option value="AAR">Afar</option>
						<option value="AFR">Afrikaans</option>
						<option value="AKA">Akan</option>
						<option value="SQI">Albanian</option>
						<option value="ASE">American Sign Language (ASL)</option>
						<option value="AMH">Amharic</option>
						<option value="ARA">Arabic</option>
						<option value="ARG">Aragonese</option>
						<option value="HYE">Armenian</option>
						<option value="ASM">Assamese</option>
						<option value="AVA">Avaric</option>
						<option value="AYM">Aymara</option>
						<option value="AZE">Azerbaijani</option>
						<option value="BAM">Bambara</option>
						<option value="BAK">Bashkir</option>
						<option value="EUS">Basque</option>
						<option value="BEL">Belarusian</option>
						<option value="BEN">Bengali</option>
						<option value="BIS">Bislama</option>
						<option value="BOS">Bosnian</option>
						<option value="BRE">Breton</option>
						<option value="BUL">Bulgarian</option>
						<option value="MYA">Burmese</option>
						<option value="CAT">Catalan</option>
						<option value="KHM">Central Khmer</option>
						<option value="CHA">Chamorro</option>
						<option value="CHE">Chechen</option>
						<option value="YUE">Chinese Cantonese</option>
						<option value="CMN">Chinese Mandarin</option>
						<option value="CHV">Chuvash</option>
						<option value="COR">Cornish</option>
						<option value="COS">Corsican</option>
						<option value="CRE">Cree</option>
						<option value="HRV">Croatian</option>
						<option value="CES">Czech</option>
						<option value="DAN">Danish</option>
						<option value="DIV">Dhivehi</option>
						<option value="NLD">Dutch</option>
						<option value="DZO">Dzongkha</option>						
						<option value="EST">Estonian</option>
						<option value="EWE">Ewe</option>
						<option value="FAO">Faroese</option>
						<option value="FIJ">Fijian</option>
						<option value="FIL">Filipino</option>
						<option value="FIN">Finnish</option>						
						<option value="FUL">Fulah</option>
						<option value="GLG">Galician</option>
						<option value="LUG">Ganda</option>
						<option value="KAT">Georgian</option>
						<option value="DEU">German</option>
						<option value="GRN">Guarani</option>
						<option value="GUJ">Gujarati</option>
						<option value="HAT">Haitian</option>
						<option value="HAU">Hausa</option>
						<option value="HEB">Hebrew</option>
						<option value="HER">Herero</option>
						<option value="HIN">Hindi</option>
						<option value="HMO">Hiri Motu</option>
						<option value="HUN">Hungarian</option>
						<option value="ISL">Icelandic</option>
						<option value="IBO">Igbo</option>
						<option value="IND">Indonesian</option>
						<option value="IKU">Inuktitut</option>
						<option value="IPK">Inupiaq</option>
						<option value="GLE">Irish</option>
						<option value="ITA">Italian</option>
						<option value="JPN">Japanese</option>
						<option value="JAV">Javanese</option>
						<option value="KAL">Kalaallisut</option>
						<option value="KAN">Kannada</option>
						<option value="KAU">Kanuri</option>
						<option value="KAS">Kashmiri</option>
						<option value="KAZ">Kazakh</option>
						<option value="KIK">Kikuyu</option>
						<option value="KIN">Kinyarwanda</option>
						<option value="KIR">Kirghiz</option>
						<option value="KOM">Komi</option>
						<option value="KON">Kongo</option>
						<option value="KOR">Korean</option>
						<option value="KUA">Kuanyama</option>
						<option value="KUR">Kurdish</option>
						<option value="LAO">Lao</option>
						<option value="LAV">Latvian</option>
						<option value="LIM">Limburgan</option>
						<option value="LIN">Lingala</option>
						<option value="LIT">Lithuanian</option>
						<option value="LUB">Luba-Katanga</option>
						<option value="LTZ">Luxembourgish</option>
						<option value="MKD">Macedonian</option>
						<option value="MLG">Malagasy</option>
						<option value="MSA">Malay</option>
						<option value="MAL">Malayalam</option>
						<option value="MLT">Maltese</option>
						<option value="GLV">Manx</option>
						<option value="MRI">Maori</option>
						<option value="MAR">Marathi</option>
						<option value="MAH">Marshallese</option>
						<option value="ELL">Greek</option>
						<option value="MON">Mongolian</option>
						<option value="NAU">Nauru</option>
						<option value="NAV">Navajo</option>
						<option value="NDO">Ndonga</option>
						<option value="NEP">Nepali</option>
						<option value="NDE">North Ndebele</option>
						<option value="SME">Northern Sami</option>
						<option value="NOR">Norwegian</option>
						<option value="NOB">Norwegian Bokmål</option>
						<option value="NNO">Norwegian Nynorsk</option>
						<option value="NYA">Nyanja</option>
						<option value="OCI">Occitan (post 1500)</option>
						<option value="OJI">Ojibwa</option>
						<option value="OJC">Oji-cree</option>
						<option value="ORI">Oriya</option>
						<option value="ORM">Oromo</option>
						<option value="OSS">Ossetian</option>
						<option value="PAN">Panjabi</option>
						<option value="FAS">Persian</option>
						<option value="POL">Polish</option>
						<option value="POR">Portuguese</option>
						<option value="PUS">Pushto</option>
						<option value="QUE">Quechua</option>
						<option value="RON">Romanian</option>
						<option value="ROH">Romansh</option>
						<option value="RUN">Rundi</option>
						<option value="RUS">Russian</option>
						<option value="SMO">Samoan</option>
						<option value="SAG">Sango</option>
						<option value="SRD">Sardinian</option>
						<option value="GLA">Scottish Gaelic</option>
						<option value="SRP">Serbian</option>
						<option value="SNA">Shona</option>
						<option value="III">Sichuan Yi</option>
						<option value="SND">Sindhi</option>
						<option value="SIN">Sinhala</option>
						<option value="SGN">Other Sign Language</option>
						<option value="SLK">Slovak</option>
						<option value="SLV">Slovenian</option>
						<option value="SOM">Somali</option>
						<option value="NBL">South Ndebele</option>
						<option value="SOT">Southern Sotho</option>
						<option value="SPA">Spanish</option>
						<option value="SUN">Sundanese</option>
						<option value="SWA">Swahili (macrolanguage)</option>
						<option value="SSW">Swati</option>
						<option value="SWE">Swedish</option>
						<option value="TGL">Tagalog</option>
						<option value="TAH">Tahitian</option>
						<option value="TGK">Tajik</option>
						<option value="TAM">Tamil</option>
						<option value="TAT">Tatar</option>
						<option value="TEL">Telugu</option>
						<option value="THA">Thai</option>
						<option value="BOD">Tibetan</option>
						<option value="TIR">Tigrinya</option>
						<option value="TON">Tonga (Tonga Islands)</option>
						<option value="TSO">Tsonga</option>
						<option value="TSN">Tswana</option>
						<option value="TUR">Turkish</option>
						<option value="TUK">Turkmen</option>
						<option value="TWI">Twi</option>
						<option value="UIG">Uighur</option>
						<option value="UKR">Ukrainian</option>
						<option value="URD">Urdu</option>
						<option value="UZB">Uzbek</option>
						<option value="VEN">Venda</option>
						<option value="VIE">Vietnamese</option>
						<option value="WLN">Walloon</option>
						<option value="CYM">Welsh</option>
						<option value="FRY">Western Frisian</option>
						<option value="WOL">Wolof</option>
						<option value="XHO">Xhosa</option>
						<option value="YID">Yiddish</option>
						<option value="YOR">Yoruba</option>
						<option value="ZHA">Zhuang</option>
						<option value="ZUL">Zulu</option>
						<option value="OTH">Other</option>
						<option value="UN">Unknown</option>					
					</select>								
						</td>
					</tr>
					
					<tr>
						<td>Partner Education Level:</td>
						<td><input type="text" name="pg1_partnerEduLevel_old" value="<%=props.getProperty("pg1_partnerEduLevel","")%>"/></td>
						
						<td>
											<select name="pg1_partnerEduLevel" style="width: 100%" >
						<option value="UNK">Select</option>
						<option value="ED001">College/University Completed</option>
						<option value="ED002">College/University not Completed</option>						
						<option value="ED004">High School Completed</option>
						<option value="ED005">High School not Completed</option>		
						<option value="ED003">Grade School Completed</option>
						<option value="ED006">Grade School not Completed</option>
					</select>			
					
						</td>
					</tr>
					
					<tr>
						<td>Education Level:</td>
						<td><input type="text" name="pg1_eduLevel_old" value="<%=props.getProperty("pg1_eduLevel","")%>"/></td>
						
						<td>
											<select name="pg1_eduLevel" style="width: 100%" >
						<option value="UNK">Select</option>
						<option value="ED001">College/University Completed</option>
						<option value="ED002">College/University not Completed</option>						
						<option value="ED004">High School Completed</option>
						<option value="ED005">High School not Completed</option>		
						<option value="ED003">Grade School Completed</option>
						<option value="ED006">Grade School not Completed</option>
					</select>			
					
						</td>
					</tr>
					
					<tr>
						<td>Ethnic Background:</td>
						<td><input type="text" name="pg1_ethnicBg_old" value="<%=props.getProperty("pg1_ethnicBg","")%>"/></td>						
						<td>
				Mother:
				<select name="pg1_ethnicBgMother" >
					<option value="UN">Select</option>					
					<option value="ANC001">Aboriginal</option>
					<option value="ANC002">Asian</option>
					<option value="ANC005">Black</option>
					<option value="ANC007">Caucasian</option>
					<option value="OTHER">Other</option>									
				</select>
				<br/>
				Father
				<select name="pg1_ethnicBgFather"  >
					<option value="UN">Select</option>					
					<option value="ANC001">Aboriginal</option>
					<option value="ANC002">Asian</option>
					<option value="ANC005">Black</option>
					<option value="ANC007">Caucasian</option>
					<option value="OTHER">Other</option>									
				</select>						
						</td>
					</tr>
					
					<tr>
						<td>Billing Type:</td>
						<td></td>
						<td>
				<select name="c_hinType">
					<option value="OHIP">OHIP</option>
					<option value="RAMQ">RAMQ</option>
					<option value="OTHER">OTHER</option>
				</select>						
						</td>
					</tr>
					
					<tr>
						<td>Smoking - Cigs/Day:</td>
						<td><input type="text" name="pg1_box3_old" value="<%=props.getProperty("pg1_box3","")%>"/></td>						
						
						<td>
								<select name="pg1_box3">
									<option value="">Select</option>
									<option value="LESS10">&lt;10</option>
									<option value="UP20">10-20</option>
									<option value="OVER20">&gt;20</option>
								</select>								
						</td>
					</tr>
					<tr>
						<td>HIV:</td>
						<td><input type="text" name="pg1_labHIV_old" value="<%=props.getProperty("pg1_labHIV","")%>"/></td>						
						
						<td>
						<select name="pg1_labHIV">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="NEG">Negative</option>
							<option value="IND">Indeterminate</option>
							<option value="UNK">Unknown</option>
						</select>							
						</td>
					</tr>
					
					<tr>
						<td>ABO:</td>
						<td><input type="text" name="pg1_labABO_old" value="<%=props.getProperty("pg1_labABO","")%>"/></td>						
						
						<td>
						<select name="pg1_labABO">
							<option value="NDONE">Not Done</option>
							<option value="A">A</option>
							<option value="B">B</option>
							<option value="AB">AB</option>
							<option value="O">O</option>
							<option value="UNK">Unknown</option>
						</select>							
						</td>
					</tr>
					
					<tr>
						<td>RH:</td>
						<td><input type="text" name="pg1_labRh_old" value="<%=props.getProperty("pg1_labRh","")%>"/></td>						
						
						<td>
						<select name="pg1_labRh">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="WPOS">Weak Positive</option>
							<option value="NEG">Negative</option>
							<option value="UNK">Unknown</option>
						</select>							
						</td>
					</tr>
					
					<tr>
						<td>GC/Chlamydia:</td>
						<td><input type="text" name="pg1_labGC_old" value="<%=props.getProperty("pg1_labGC","")%>"/></td>						
						
						<td>
						<select name="pg1_labGC">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="NEG">Negative</option>
							<option value="UNK">Unknown</option>
						</select>		
						<br/>
						<select name="pg1_labChlamydia">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="NEG">Negative</option>
							<option value="UNK">Unknown</option>
						</select>											
						</td>
					</tr>
					
					<tr>
						<td>Rubella:</td>
						<td><input type="text" name="pg1_labRubella_old" value="<%=props.getProperty("pg1_labRubella","")%>"/></td>						
						
						<td>
						<select name="pg1_labRubella">
							<option value="NDONE">Not Done</option>
							<option value="Non-Immune">Non-Immune</option>
							<option value="Immune">Immune </option>
							<option value="Indeterminate">Indeterminate</option>
						</select>							
						</td>
					</tr>			
					
					<tr>
						<td>HbsAg:</td>
						<td><input type="text" name="pg1_labHBsAg_old" value="<%=props.getProperty("pg1_labHBsAg","")%>"/></td>						
						
						<td>
						<select name="pg1_labHBsAg">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="NEG">Negative</option>
							<option value="UNK">Unknown</option>
						</select>							
						</td>
					</tr>
					
					<tr>
						<td>VDRL:</td>
						<td><input type="text" name="pg1_labVDRL_old" value="<%=props.getProperty("pg1_labVDRL","")%>"/></td>						
						
						<td>
						<select name="pg1_labVDRL">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="NEG">Negative</option>
							<option value="UNK">Unknown</option>
						</select>							
						</td>
					</tr>
					
					<tr>
						<td>Sickle Cell:</td>
						<td><input type="text" name="pg1_labSickle_old" value="<%=props.getProperty("pg1_labSickle","")%>"/></td>						
						
						<td>
						<select name="pg1_labSickle">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="NEG">Negative</option>
							<option value="UNK">Unknown</option>
						</select>							
						</td>
					</tr>
					
					<tr>
						<td>AR2 ABO:</td>
						<td><input type="text" name="ar2_bloodGroup_old" value="<%=props.getProperty("ar2_bloodGroup","")%>"/></td>						
						
						<td>
						<select name="ar2_bloodGroup">
							<option value="NDONE">Not Done</option>
							<option value="A">A</option>
							<option value="B">B</option>
							<option value="AB">AB</option>
							<option value="O">O</option>
							<option value="UNK">Unknown</option>
						</select>						
						</td>
					</tr>
					
					
					<tr>
						<td>AR2 RH:</td>
						<td><input type="text" name="ar2_rh_old" value="<%=props.getProperty("ar2_rh","")%>"/></td>						
						
						<td>
						<select name="ar2_rh">
							<option value="NDONE">Not Done</option>
							<option value="POS">Positive</option>
							<option value="WPOS">Weak Positive</option>
							<option value="NEG">Negative</option>
							<option value="UNK">Unknown</option>
						</select>							
						</td>
					</tr>
					
					<tr>
						<td>GBS:</td>
						<td><input type="text" name="ar2_strep_old" value="<%=props.getProperty("ar2_strep","")%>"/></td>						
						
						<td>
						<select name="ar2_strep">
							<option value="NDONE">Not Done</option>
							<option value="POSSWAB">Positive swab result</option>
							<option value="POSURINE">Urine Positive for GBS</option>
							<option value="NEGSWAB">Negative swab result</option>
							<option value="DONEUNK">Done-result unknown</option>
							<option value="UNK">Unknown if screened</option>						
						</select>			
						</td>
					</tr>
					
					<tr>
						<td>2 Hr GTT:</td>
						<td><input type="text" name="ar2_lab2GTT_old" value="<%=props.getProperty("ar2_lab2GTT","")%>"/></td>						
						
						<td>
						<input type="hidden" name="ar2_lab2GTT" value="<%= UtilMisc.htmlEscape(props.getProperty("ar2_lab2GTT", "")) %>"/> 
						<input style="width:50px" type="text" name="ar2_lab2GTT1" size="4" maxlength="4">/
						<input style="width:50px" type="text" name="ar2_lab2GTT2" size="4" maxlength="4">/
						<input style="width:50px" type="text" name="ar2_lab2GTT3" size="4" maxlength="4">

						</td>
					</tr>																									
					
				</tbody>
			</table>
			<button>Perform Migration</button>		
		</fieldset>
	</form>

</html:html>
