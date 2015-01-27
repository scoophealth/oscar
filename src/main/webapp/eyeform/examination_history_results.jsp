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

<%@ include file="/taglibs.jsp"%>
<%@page import="org.oscarehr.eyeform.web.EyeformAction"%>
<%@page import="org.oscarehr.common.model.Appointment"%>
<%@page import="oscar.oscarEncounter.oscarMeasurements.data.Measurements"%>
<%@page import="java.util.List"%>
<%@page import="oscar.util.StringUtils" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page import="oscar.dms.*,java.util.*" %>
<%@ page import="oscar.OscarProperties"%>
<%
	String sdate = StringUtils.transformNullInEmptyString((String)request.getAttribute("sdate"));
	String edate = StringUtils.transformNullInEmptyString((String)request.getAttribute("edate"));
	oscar.OscarProperties props1 = oscar.OscarProperties.getInstance();
	String eyeform = props1.getProperty("cme_js");
	String[] fields = request.getParameterValues("fromlist2");
	List<String> fieldList = new ArrayList<String>();
	if(fields != null) {
		for(String field:fields) {
			fieldList.add(field);
		}
	}
	
	String[] field1 = request.getParameterValues("fromlist1");
	List<String> fieldList1 = new ArrayList<String>();
	if(field1 != null) {
		for(String field:field1) {
			fieldList1.add(field);
		}
	}
%>

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />

<html>
	<head>
    	<title>Examination History Results</title>
    	<link rel="stylesheet" type="text/css" href='<html:rewrite page="/jsCalendar/skins/aqua/theme.css" />' />
		<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/calendar/calendar.css" title="win2k-cold-1" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar-setup.js"></script>
    	<link rel="stylesheet" href="<%=request.getContextPath()%>/eyeform/display2.css" type="text/css">
		
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
		
    	<style type="text/css">
			* { font-family: Trebuchet MS, Lucida Sans Unicode, Arial, Helvetica, sans-serif; color: #000; margin: 0px; padding: 0px; }
			body { padding: 10px; }

			td.inner{
			border:1px solid #666;
			}

			table.common{
			border:0;
			font-size: 10pt;
			}
			h5{
				margin-top: 1px;
				border-bottom: 1px solid #000;
				font-weight: bold;
				list-style-type: none;
				font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif;
				font-size: 10pt;
				overflow: hidden;
				background-color: #ccccff;
				padding: 0px;
				color: black;
				width: 300px;
			}
			th {white-space:nowrap}

			.centered {text-align:center}
			
			.title { font-family: Arial, Helvetica, sans-serif; font-size: 16px; font-weight: bold; color: #333333; }
			.title1 { font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; color: #333333; }
		</style>
		<script>
			function allsubmit(){
				document.getElementById("sdate").value = "";
				document.getElementById("edate").value = "";
				inputForm.submit();
			}
		</script>
		<script>
			function insert_title(){			
				var glasshxElm = $("h5").filter(function(){
					if(this.innerHTML.indexOf("Glasses History") != -1){
						return true;
					}
					return false;
				});
				if(glasshxElm != null && glasshxElm.length > 0){
					glasshxElm = glasshxElm[0];
					var targetTbl = $(glasshxElm).parent().children("table");
					if(targetTbl != null){					
						var title = "<tr><th class=\"centered\"></th><th colspan=\"5\" style=\"text-align:center\">OD</th><th colspan=\"5\" style=\"text-align:center\">OS</th><th></th><th></th></tr>"
						$($(targetTbl).find("thead")).prepend(title);
					}
				}
				
				var assessmentElm = $("h5").filter(function(){
					if (this.innerHTML.indexOf("VISION ASSESSMENT") != -1) {
						return true;
					}
					return false;
					});

				if (assessmentElm != null && assessmentElm.length > 0) {
					assessmentElm = assessmentElm[0];
					var targetTbl = $(assessmentElm).parent().children("table");
					if (targetTbl != null) {
						var title = "<tr><th class=\"centered\"></th>";
						<%if(fieldList.contains("Distance vision (sc)")){%>
							title = title + "<th colspan=\"3\" style=\"text-align:center\">Distance SC</th>";
						<%}%>
						<%if(fieldList.contains("Distance vision (cc)")){%>
							title = title + "<th colspan=\"3\" style=\"text-align:center\">Distance CC</th>";
						<%}%>
						<%if(fieldList.contains("Distance vision (ph)")){%>
							title = title + "<th colspan=\"2\" style=\"text-align:center\">Pin Hole</th>";
						<%}%>
						<%if(fieldList.contains("Intermediate vision (sc)")){%>
							title = title + "<th colspan=\"3\" style=\"text-align:center\">Intermediate SC</th>";
						<%}%>
						<%if(fieldList.contains("Intermediate vision (cc)")){%>
							title = title + "<th colspan=\"3\" style=\"text-align:center\">Intermediate CC</th>";
						<%}%>
						<%if(fieldList.contains("Near vision (sc)")){%>
							title = title + "<th colspan=\"3\" style=\"text-align:center\">Near SC</th>";
						<%}%>
						<%if(fieldList.contains("Near vision (cc)")){%>
							title = title + "<th colspan=\"3\" style=\"text-align:center\">Near CC</th>";
						<%}%>
						title = title + "</tr>";
						//var title = "<tr><th class=\"centered\"></th><th colspan=\"3\" style=\"text-align:center\">Distance SC</th><th colspan=\"3\" style=\"text-align:center\">Distance CC</th><th colspan=\"2\" style=\"text-align:center\">Pin Hole</th><th colspan=\"3\" style=\"text-align:center\">Intermediate SC</th><th colspan=\"3\" style=\"text-align:center\">Intermediate CC</th><th colspan=\"3\" style=\"text-align:center\">Near SC</th><th colspan=\"3\" style=\"text-align:center\">Near CC</th></tr>";
						$($(targetTbl).find("thead")).prepend(title);
					}
				}
				
				var visionElm = $("h5").filter(function(){
					if (this.innerHTML.indexOf("VISION MEASUREMENT") != -1) {
						return true;
					}
					return false;
					});

				if (visionElm != null && visionElm.length > 0) {
					visionElm = visionElm[0];
					var targetTbl = $(visionElm).parent().children("table");
					if (targetTbl != null) {
						var title = "<tr><th class=\"centered\"></th>";
						<%if(fieldList.contains("Keratometry")){%>
						title = title + "<th colspan=\"6\" style=\"text-align:center\">Keratometry</th>";
						<%}%>
						<%if(fieldList.contains("Auto-refraction")){%>
						title = title + "<th colspan=\"8\" style=\"text-align:center\">Autorefraction</th>";
						<%}%>
						<%if(fieldList.contains("Manifest distance")){%>
						title = title + "<th colspan=\"9\" style=\"text-align:center\">Manifest Distance</th>";
						<%}%>
						<%if(fieldList.contains("Manifest near")){%>
						title = title + "<th colspan=\"9\" style=\"text-align:center\">Manifest Near</th>";
						<%}%>
						<%if(fieldList.contains("Cycloplegic refraction")){%>
						title = title + "<th colspan=\"8\" style=\"text-align:center\">Cycloplegic</th>";
						<%}%>
						title = title + "</tr>";
						//var title = "<tr><th class=\"centered\"></th><th colspan=\"6\" style=\"text-align:center\">Keratometry</th><th colspan=\"8\" style=\"text-align:center\">Autorefraction</th><th colspan=\"9\" style=\"text-align:center\">Manifest Distance</th><th colspan=\"9\" style=\"text-align:center\">Manifest Near</th><th colspan=\"8\" style=\"text-align:center\">Cycloplegic</th></tr>";
						$($(targetTbl).find("thead")).prepend(title);
					}
				}
				
				var intraocularElm = $("h5").filter(function(){
					if(this.innerHTML.indexOf("INTRAOCULAR PRESSURE") != -1){
						return true;
					}
					return false;
				});
				if(intraocularElm != null && intraocularElm.length > 0){
					intraocularElm = intraocularElm[0];
					var targetTbl = $(intraocularElm).parent().children("table");
					if(targetTbl != null){
						var title = "<tr><th class=\"centered\"></th>";
						<%if(fieldList.contains("NCT")){%>
						title = title + "<th colspan=\"3\" style=\"text-align:center\">Non-contact</th>";
						<%}%>
						<%if(fieldList.contains("Applanation")){%>
						title = title + "<th colspan=\"3\" style=\"text-align:center\">Applanation</th>";
						<%}%>
						<%if(fieldList.contains("Central corneal thickness")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Pachymetry</th>";
						<%}%>
						title = title + "</tr>";
						//var title = "<tr><th class=\"centered\"></th><th colspan=\"3\" style=\"text-align:center\">Non-contact</th><th colspan=\"3\" style=\"text-align:center\">Applanation</th><th colspan=\"2\" style=\"text-align:center\">Pachymetry</th></tr>"
						$($(targetTbl).find("thead")).prepend(title);
					}
				}
				
				var refractiveElm = $("h5").filter(function(){
					if(this.innerHTML.indexOf("REFRACTIVE") != -1){
						return true;
					}
					return false;
				});
				if(refractiveElm != null && refractiveElm.length > 0){
					refractiveElm = refractiveElm[0];
					var targetTbl = $(refractiveElm).parent().children("table");
					if(targetTbl != null){
						var title = "<tr><th class=\"centered\"></th>";
						<%if(fieldList.contains("Dominance")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Dominance</th>";
						<%}%>
						<%if(fieldList.contains("Mesopic pupil size")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Scotopic Pupil</th>";
						<%}%>
						<%if(fieldList.contains("Angle Kappa")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Kappa</th>";
						<%}%>
						title = title + "</tr>";
						//var title = "<tr><th class=\"centered\"></th><th colspan=\"2\" style=\"text-align:center\">Dominance</th><th colspan=\"2\" style=\"text-align:center\">Scotopic Pupil</th><th colspan=\"2\" style=\"text-align:center\">Kappa</th></tr>"
						$($(targetTbl).find("thead")).prepend(title);
					}
				}
				
				var otherElm = $("h5").filter(function(){
					if(this.innerHTML.indexOf("OTHER EXAM") != -1){
						return true;
					}
					return false;
				});
				if(otherElm != null && otherElm.length > 0){
					otherElm = otherElm[0];
					var targetTbl = $(otherElm).parent().children("table");
					if(targetTbl != null){
						var title = "<tr><th class=\"centered\"></th>";
						<%if(fieldList.contains("Colour vision")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Colour vision</th>";
						<%}%>
						<%if(fieldList.contains("Pupil")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Pupils</th>";
						<%}%>
						<%if(fieldList.contains("Amsler grid")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Amsler</th>";
						<%}%>
						<%if(fieldList.contains("Potential acuity meter")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">PAM</th>";
						<%}%>
						<%if(fieldList.contains("Confrontation fields")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Confrontation </th>";
						<%}%>
						<%if(fieldList.contains("Maddox rod")){%>
						title = title + "<th></th>";
						<%}%>
						<%if(fieldList.contains("Bagolini test")){%>
						title = title + "<th></th>";
						<%}%>
						<%if(fieldList.contains("Worth 4 Dot (distance)") || fieldList.contains("Worth 4 Dot (near)")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Worth 4 dot</th>";
						<%}%>
						title = title + "</tr>";
						//var title = "<tr><th class=\"centered\"></th><th colspan=\"2\" style=\"text-align:center\">Colour vision</th><th colspan=\"2\" style=\"text-align:center\">Pupils</th><th colspan=\"2\" style=\"text-align:center\">Amsler</th><th colspan=\"2\" style=\"text-align:center\">PAM</th><th colspan=\"2\" style=\"text-align:center\">Confrontation </th><th></th><th></th><th colspan=\"2\" style=\"text-align:center\">Worth 4 dot</th></tr>"
						$($(targetTbl).find("thead")).prepend(title);
					}
				}
				
				var externalElm = $("h5").filter(function(){
					if(this.innerHTML.indexOf("EXTERNAL/ORBIT") != -1){
						return true;
					}
					return false;
				});
				if(externalElm != null && externalElm.length > 0){
					externalElm = externalElm[0];
					var targetTbl = $(externalElm).parent().children("table");
					if(targetTbl != null){
						var title = "<tr><th class=\"centered\"></th>";
						<%if(fieldList.contains("Face")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Face</th>";
						<%}%>
						<%if(fieldList.contains("Retropulsion")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Retropulsion</th>";
						<%}%>
						<%if(fieldList.contains("Hertel")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Hertel</th>";
						<%}%>
						title = title + "</tr>";
						//var title = "<tr><th class=\"centered\"></th><th colspan=\"2\" style=\"text-align:center\">Face</th><th colspan=\"2\" style=\"text-align:center\">Retropulsion</th><th colspan=\"2\" style=\"text-align:center\">Hertel</th></tr>"
						$($(targetTbl).find("thead")).prepend(title);
					}
				}
				
				var ductElm = $("h5").filter(function(){
					if(this.innerHTML.indexOf("EYELID/NASOLACRIMAL DUCT") != -1){
						return true;
					}
					return false;
				});
				if(ductElm != null && ductElm.length > 0){
					ductElm = ductElm[0];
					var targetTbl = $(ductElm).parent().children("table");
					if(targetTbl != null){
						var title = "<tr><th class=\"centered\"></th>";
						<%if(fieldList.contains("Upper lid")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Upper Lid</th>";
						<%}%>
						<%if(fieldList.contains("Lower lid")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Lower Lid</th>";
						<%}%>
						<%if(fieldList.contains("Lacrimal lake")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Lacrimal Lake</th>";
						<%}%>
						<%if(fieldList.contains("Lacrimal irrigation")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Irrigation</th>";
						<%}%>
						<%if(fieldList.contains("Punctum")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Punctum</th>";
						<%}%>
						<%if(fieldList.contains("Nasolacrimal duct")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">NLD</th>";
						<%}%>
						<%if(fieldList.contains("Dye disappearance")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Dye Disappearance</th>";
						<%}%>
						title = title + "</tr>";
						//var title = "<tr><th class=\"centered\"></th><th colspan=\"2\" style=\"text-align:center\">Upper Lid</th><th colspan=\"2\" style=\"text-align:center\">Lower Lid</th><th colspan=\"2\" style=\"text-align:center\">Lacrimal Lake</th><th colspan=\"2\" style=\"text-align:center\">Irrigation</th><th colspan=\"2\" style=\"text-align:center\">Punctum</th><th colspan=\"2\" style=\"text-align:center\">NLD</th><th colspan=\"2\" style=\"text-align:center\">Dye Disappearance</th></tr>"
						$($(targetTbl).find("thead")).prepend(title);
					}
				}
				
				var eyelidElm = $("h5").filter(function(){
					if(this.innerHTML.indexOf("EYELID MEASUREMENT") != -1){
						return true;
					}
					return false;
				});
				if(eyelidElm != null && eyelidElm.length > 0){
					eyelidElm = eyelidElm[0];
					var targetTbl = $(eyelidElm).parent().children("table");
					if(targetTbl != null){
						var title = "<tr><th class=\"centered\"></th>";
						<%if(fieldList.contains("Margin reflex distance")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">MRD</th>";
						<%}%>
						<%if(fieldList.contains("Inferior scleral show")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">ISS</th>";
						<%}%>
						<%if(fieldList.contains("Levator function")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Levator Function</th>";
						<%}%>
						<%if(fieldList.contains("Lagophthalmos")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Lag Ophthalmos</th>";
						<%}%>
						<%if(fieldList.contains("Blink reflex")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Blink reflex</th>";
						<%}%>
						<%if(fieldList.contains("Cranial Nerve VII function")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">CNVII</th>";
						<%}%>
						<%if(fieldList.contains("Bells phenomenon")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Bell</th>";
						<%}%>
						<%if(fieldList.contains("Schirmer test")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Schirmer</th>";
						<%}%>
						title = title + "</tr>";
						//var title = "<tr><th class=\"centered\"></th><th colspan=\"2\" style=\"text-align:center\">MRD</th><th colspan=\"2\" style=\"text-align:center\">ISS</th><th colspan=\"2\" style=\"text-align:center\">Levator Function</th><th colspan=\"2\" style=\"text-align:center\">Lag Ophthalmos</th><th colspan=\"2\" style=\"text-align:center\">Blink reflex</th><th colspan=\"2\" style=\"text-align:center\">CNVII</th><th colspan=\"2\" style=\"text-align:center\">Bell</th><th colspan=\"2\" style=\"text-align:center\">Schirmer</th></tr>"
						$($(targetTbl).find("thead")).prepend(title);
					}
				}
				
				var eyelidElm = $("h5").filter(function(){
					if(this.innerHTML.indexOf("ANTERIOR SEGMENT") != -1){
						return true;
					}
					return false;
				});
				if(eyelidElm != null && eyelidElm.length > 0){
					eyelidElm = eyelidElm[0];
					var targetTbl = $(eyelidElm).parent().children("table");
					if(targetTbl != null){
						var title = "<tr><th class=\"centered\"></th>";
						<%if(fieldList.contains("Cornea")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Cornea</th>";
						<%}%>
						<%if(fieldList.contains("Conjunctiva/Sclera")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Conj/Sclera</th>";
						<%}%>
						<%if(fieldList.contains("Anterior chamber")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Anterior Chamber</th>";
						<%}%>
						<%if(fieldList.contains("Angle")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Angle</th>";
						<%}%>
						<%if(fieldList.contains("Iris")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Iris</th>";
						<%}%>
						<%if(fieldList.contains("Lens")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Lens</th>";
						<%}%>
						title = title + "</tr>";
						//var title = "<tr><th class=\"centered\"></th><th colspan=\"2\" style=\"text-align:center\">Cornea</th><th colspan=\"2\" style=\"text-align:center\">Conj/Sclera</th><th colspan=\"2\" style=\"text-align:center\">Anterior Chamber</th><th colspan=\"2\" style=\"text-align:center\">Angle</th><th colspan=\"2\" style=\"text-align:center\">Iris</th><th colspan=\"2\" style=\"text-align:center\">Lens</th></tr>"
						$($(targetTbl).find("thead")).prepend(title);
					}
				}
				
				var posteriorElm = $("h5").filter(function(){
					if(this.innerHTML.indexOf("POSTERIOR SEGMENT") != -1){
						return true;
					}
					return false;
				});
				if(posteriorElm != null && posteriorElm.length > 0){
					posteriorElm = posteriorElm[0];
					var targetTbl = $(posteriorElm).parent().children("table");
					if(targetTbl != null){
						var title = "<tr><th class=\"centered\"></th>";
						<%if(fieldList.contains("Optic disc")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Disc</th>";
						<%}%>
						<%if(fieldList.contains("C/D ratio")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">C/D Ratio</th>";
						<%}%>
						<%if(fieldList.contains("Macula")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Macula</th>";
						<%}%>
						<%if(fieldList.contains("Retina")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Retina</th>";
						<%}%>
						<%if(fieldList.contains("Vitreous")){%>
						title = title + "<th colspan=\"2\" style=\"text-align:center\">Vitreous</th>";
						<%}%>
						title = title + "</tr>";
						//var title = "<tr><th class=\"centered\"></th><th colspan=\"2\" style=\"text-align:center\">Disc</th></th><th colspan=\"2\" style=\"text-align:center\">C/D Ratio</th></th><th colspan=\"2\" style=\"text-align:center\">Macula</th></th><th colspan=\"2\" style=\"text-align:center\">Retina</th></th><th colspan=\"2\" style=\"text-align:center\">Vitreous</th></tr>"
						$($(targetTbl).find("thead")).prepend(title);
					}
				}
			}
			
			function adjust_title(){
				var title = ["Glasses History","VISION ASSESSMENT","STEREO VISION","VISION MEASUREMENT","INTRAOCULAR PRESSURE","REFRACTIVE","OTHER EXAM","DUCTION/DIPLOPIA TESTING","DEVIATION MEASUREMENT","EXTERNAL/ORBIT","EYELID/NASOLACRIMAL DUCT","EYELID MEASUREMENT","ANTERIOR SEGMENT","POSTERIOR SEGMENT"];
				var i = 0;
				for(i = 0; i < title.length; i ++){
					var Elm = $("h5").filter(function(){
						if(this.innerHTML.indexOf(title[i]) != -1){
							return true;
						}
						return false;
					});
					if(Elm != null && Elm.length > 0){
						Elm = Elm[0];
						Elm.innerHTML = title[i] + ":";
						var targetSpan = $(Elm).parent().children("span");
						if(targetSpan != null && targetSpan.length > 0){
							if(targetSpan.length == 4){
								var addspan = targetSpan[0];							
								targetSpan[0].style.display = "none";
								targetSpan[2].style.display = "none";
								targetSpan[3].style.display = "none";
								addspan.style.display = "inline";
								$(Elm).append(addspan);
								
								var targetTbl = $(Elm).parent().children("table"); 
								var tabWidth = $(targetTbl).innerWidth();
								targetSpan[1].style.width = tabWidth;
								Elm.style.width = tabWidth;
							}
							if(targetSpan.length == 2){
								var addspan = targetSpan[0];							
								targetSpan[0].style.display = "none";
								targetSpan[1].style.display = "none";							
								addspan.style.display = "inline";
								$(Elm).append(addspan);
								
								var targetTbl = $(Elm).parent().children("table"); 
								var tabWidth = $(targetTbl).innerWidth();
								Elm.style.width = tabWidth;
							}
						}	
					}
				}
			}
		</script>
	</head>

	<body onload="insert_title();adjust_title();">
		<form action="<%=request.getContextPath()%>/eyeform/ExaminationHistory.do" method="POST" id="inputForm" name="inputForm">
		<input type="hidden" name="method" value="query"/>
		<input type="hidden" name="demographicNo" value="<c:out value="${demographic.demographicNo}"/>"/>
		<input type="hidden" name="refPage" value="<c:out value="${refPage}"/>"/>
		<c:forEach var="field" items="${fields}">
			<input type="hidden" name="fromlist2" value="<c:out value="${field}"/>"/>
		</c:forEach>
		<table class="common">
	  		<tr>
	  			<td>
	  				<h4 style="background-color: #69c">Demographic name:<c:out value="${demographic.formattedName}"/></h4>
	  			</td>
	  		</tr>
			<tr>
				<table style="background-color: #efefff">
					<tr>
						<td>Start Date:</td>
						<td>
							<%if(("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform)) || ("eyeform3.2".equals(eyeform))){%>
							<input type="text" class="plain" name="sdate" id="sdate" size="12" onfocus="this.blur()" readonly="readonly" value="<%=sdate%>"/>
							<%}else{%>
			 				<input type="text" class="plain" name="sdate" id="sdate" size="12" onfocus="this.blur()" readonly="readonly" value="<%=sdate%>"/>
							<%}%>
			 				<img src="<%=request.getContextPath()%>/images/cal.gif" id="sdate_cal">
			 				<script type="text/javascript">
								Calendar.setup({ inputField : "sdate", ifFormat : "%Y-%m-%d", showsTime :false, button : "sdate_cal", singleClick : true, step : 1 });
							</script>
		    			</td>
						<td>End Date:</td>
						<td>
							<input type="text" class="plain" name="edate" id="edate" size="12" onfocus="this.blur()" readonly="readonly" value="<%=edate%>"/>
							<img src="<%=request.getContextPath()%>/images/cal.gif" id="edate_cal">
			 				<script type="text/javascript">
								Calendar.setup({ inputField : "edate", ifFormat : "%Y-%m-%d", showsTime :false, button : "edate_cal", singleClick : true, step : 1 });
							</script>
						</td>
						<td></td>
						<td>
							<input type="submit" onclick="this.form.refPage.value=null" value="Search"/>
							<%if(("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform)) || ("eyeform3.2".equals(eyeform))){%>
							<!--<input type="button" onclick="allsubmit();" value="Alls" />-->
							<%}%>
						</td>
					</tr>
				</table>
			</tr>
	 	</table>
<% if((!"eyeform3.1".equals(eyeform)) && (!"eyeform3.2".equals(eyeform)) && (!"eyeform3".equals(eyeform))){%>
		<h5>Simple field history:</h5>
		<table class="display" style="width:20%">
		<tr style="background-color: rgb(204, 204, 255);">
	    <td>Total <c:out value="${numPages}"/> pages.</td>
		<td align="right">
		<%
			int numPages = (Integer)request.getAttribute("numPages");
			int pageNumber = (Integer)request.getAttribute("refPage");
			if(pageNumber>1) {
				%><a href="#" onclick="document.inputForm.refPage.value=<%=(pageNumber-1)%>;return document.inputForm.submit();">prev</a><%
			} else {
				%>prev<%
			}
		%>
		&nbsp;
		<%
			if(numPages > 1 && pageNumber<numPages) {
				%><a href="#" onclick="document.inputForm.refPage.value=<%=(pageNumber+1)%>;return document.inputForm.submit();">next</a><%
			} else {
				%>next<%
			}
		%>
		</td>
	  </tr>
		<table class="display" style="width:20%">
			<tr style="background-color: rgb(204, 204, 255);">
		  		<td nowrap="nowrap"></td>
		  		<%
		  			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		  			for(Appointment appointment:(List<Appointment>)request.getAttribute("appointments")) {
		  				out.println("<td nowrap=\"nowrap\">"+formatter.format(appointment.getAppointmentDate())+"</td>");
		  			}
		  		%>
			</tr>
			<%
				@SuppressWarnings("unchecked")
				List<String> simpleFieldNames = (List<String>)request.getAttribute("simpleFieldNames");
				Measurements simpleFields[][] = (Measurements[][])request.getAttribute("simpleFields");
				for(int x=0;x<simpleFields.length;x++) {
					out.println("<tr class=\""+(((x%2)==0)?"even":"odd")+"\">");
					out.println("<td nowrap=\"nowrap\">"+simpleFieldNames.get(x)+"</td>");
					for(int y=0;y<simpleFields[x].length;y++) {
						out.println("<td nowrap=\"nowrap\">"+((simpleFields[x][y]!=null)?simpleFields[x][y].getDataField():"")+"</td>");
					}
					out.println("</tr>");
				}
			%>
</table>
<%}%>
<%if("eyeform3.1".equals(eyeform)){%>
<table width="100%" cellpadding="1" cellspacing="4" border="0">
<%
//if(fieldList.contains("Glasses Rx")){
if(fieldList1.contains("GLASSES HISTORY")){
%>
<tr>
	<td class="title">GLASSES HISTORY :</td>
</tr>

<%
	ArrayList<Map<String,String>>  glasses_str = new ArrayList<Map<String,String>>();
	glasses_str = (ArrayList<Map<String,String>>)request.getAttribute("glasses");
	if(glasses_str != null){
	for(int i=0;i < glasses_str.size(); i ++ ){
%>
<tr>
<%
		Map<String,String> glasses = glasses_str.get(i);
		String date_str = glasses.get("date");
		String gl_rs = glasses.get("gl_rs");
		String gl_rc = glasses.get("gl_rc");
		String gl_rx = glasses.get("gl_rx");
		String gl_ra = glasses.get("gl_ra");
		String gl_rp = glasses.get("gl_rp");
		String gl_ls = glasses.get("gl_ls");
		String gl_lc = glasses.get("gl_lc");
		String gl_lx = glasses.get("gl_lx");
		String gl_la = glasses.get("gl_la");
		String gl_lp = glasses.get("gl_lp");
		String gl_date = glasses.get("gl_date");
		String gl_note = glasses.get("gl_note");
		String gl_type = glasses.get("gl_type");
	
%>
		<td style="word-break:break-all"><span class="title1">(<%=date_str%>)&nbsp; &nbsp;</span>
<%
	if(gl_type.length() > 0){
%>
	Type:<%=gl_type%>
<%
	}
%>
<%
		if((gl_rs.length() > 0) || (gl_rc.length() > 0) || (gl_rx.length() > 0) || (gl_ra.length() > 0) || (gl_rp.length() > 0)){
%>		
		OD
<%		
		if(gl_rs.length() > 0){		
%>
		<%=gl_rs%>
<%
		}
		if(gl_rc.length() > 0){
%>
		<%=gl_rc%>
<%
		}if(gl_rx.length() > 0){
%>
		x <%=gl_rx%>
<%
		}
%>	
<%
		if(gl_ra.length() > 0){
%>
		add <%=gl_ra%>
<%
		}
%>	
<%
		if(gl_rp.length() > 0){
%>
		prism <%=gl_rp%>
<%
		}
%>
<%
	}
%>	

<%
	if((gl_ls.length() > 0) || (gl_lc.length() > 0) || (gl_lx.length() > 0) || (gl_la.length() > 0) || (gl_lp.length() > 0)){
%>
		OS
<%
		if(gl_ls.length() > 0){
%>
		<%=gl_ls%> 
<%
		}if(gl_lc.length() > 0){
%>	
		<%=gl_lc%>
<%
		}if(gl_lx.length() > 0){
%>
		x <%=gl_lx%>
<%
		}
%>
<%
		if(gl_la.length() > 0){
%>
		add <%=gl_la%>
<%
		}
%>	
<%
		if(gl_lp.length() > 0){
%>
		prism <%=gl_lp%>
<%
		}
%>
<%
	}
%>	

<%
		if(gl_date.length() > 0){
%>
		date: <%=gl_date%>
<%
		}
%>	
<%
		if(gl_note.length() > 0){
%>
		note: <%=gl_note%>.
<%
		}
%>	
</td>
	</tr>	
<%		
		}
	}
%>
<tr><td>&nbsp;</td></tr>
<%
}
//if(fieldList.contains("Distance vision (sc)")){
if(fieldList1.contains("VISION ASSESSMENT")){
%>
<tr>
	<td class="title">VISION ASSESSMENT :</td>
</tr>
<%
	ArrayList<Map<String,String>>  distance_vision = new ArrayList<Map<String,String>>(); 
	distance_vision = (ArrayList<Map<String,String>>)request.getAttribute("distance_vision");
	if(distance_vision != null){
	for(int i=0;i < distance_vision.size(); i ++ ){
		Map<String,String> distance = distance_vision.get(i);
		String date_str = distance.get("date");
%>
<tr><td style="word-break:break-all"><span class="title1">(<%= date_str%>)&nbsp; &nbsp;</span>
<%	
	if(fieldList.contains("Distance vision (sc)")){		
		String v_rdsc = distance.get("v_rdsc");
		String v_ldsc = distance.get("v_ldsc");
		String v_dsc = distance.get("v_dsc");
		if((v_rdsc.length() > 0) || (v_ldsc.length() > 0) || (v_dsc.length() > 0)){
%>
		<span class="title1">Distance vision (sc)</span>
<%		
		if(v_rdsc.length() > 0){
%>
			OD <%=v_rdsc%>;
<%
		}
%>
<%
		if(v_ldsc.length() > 0){
%>
			OS <%=v_ldsc%>;
<%
		}
%>
<%
		if(v_dsc.length() > 0){
%>
			OU <%=v_dsc%>.
<%
		}
%>
<%
		}
	}
%>
<%	
	if(fieldList.contains("Distance vision (cc)")){		
		String v_rdcc = distance.get("v_rdcc");
		String v_ldcc = distance.get("v_ldcc");
		String v_dcc = distance.get("v_dcc");
		if((v_rdcc.length() > 0) || (v_ldcc.length() > 0) || (v_dcc.length() > 0)){
%>
		<span class="title1">Distance vision (cc) </span>
<%
		if(v_rdcc.length() > 0){
%>
		OD <%=v_rdcc%>;
<%
		}
%>
<%
		if(v_ldcc.length() > 0){
%>
			OS <%=v_ldcc%>;
<%
		}
%>
<%
		if(v_dcc.length() > 0){
%>
			OU <%=v_dcc%>.
<%
		}
%>	
<%
		}
	}
%>
<%	
	if(fieldList.contains("Distance vision (ph)")){	
		String v_rph = distance.get("v_rph");
		String v_lph = distance.get("v_lph");
		if((v_rph.length() > 0) || (v_lph.length() > 0)){
%>
		<span class="title1">Distance vision (ph) </span>
<%
		if(v_rph.length() > 0){
%>
		OD <%=v_rph%>;
<%
		}
%>
<%
		if(v_lph.length() > 0){
%>
			OS <%=v_lph%>;
<%
		}
%>	
<%
		}
	}
%>
<%	
	if(fieldList.contains("Intermediate vision (sc)")){	
		String v_risc = distance.get("v_risc");
		String v_lisc = distance.get("v_lisc");
		String v_isc = distance.get("v_isc");
		if((v_risc.length() > 0) || (v_lisc.length() > 0) || (v_isc.length() > 0) ){
%>
		<span class="title1">Intermediate vision (sc) </span>
<%
		if(v_risc.length() > 0){
%>
		OD <%=v_risc%>;
<%
		}
%>
<%
		if(v_lisc.length() > 0){
%>
			OS <%=v_lisc%>;
<%
		}
%>
<%
		if(v_isc.length() > 0){
%>
			OU <%=v_isc%>.
<%
		}
%>	

<%
		}
	}
%>
<%	
	if(fieldList.contains("Intermediate vision (cc)")){		
		String v_ricc = distance.get("v_ricc");
		String v_licc = distance.get("v_licc");
		String v_icc = distance.get("v_icc");
		if((v_ricc.length() > 0) || (v_licc.length() > 0) || (v_icc.length() > 0) ){
%>
		<span class="title1">Intermediate vision (cc) </span>
<%
		if(v_ricc.length() > 0){
%>
		OD <%=v_ricc%>;
<%
		}
%>
<%
		if(v_licc.length() > 0){
%>
			OS <%=v_licc%>;
<%
		}
%>
<%
		if(v_icc.length() > 0){
%>
			OU <%=v_icc%>.
<%
		}
%>	

<%
		}
	}
%>
<%	
	 if(fieldList.contains("Near vision (sc)")){	
		String v_rnsc = distance.get("v_rnsc");
		String v_lnsc = distance.get("v_lnsc");
		String v_nsc = distance.get("v_nsc");
		if( (v_rnsc.length() > 0) || (v_lnsc.length() > 0) || (v_nsc.length() > 0) ){
%>
		<span class="title1">Near vision (sc) </span>
<%
		if(v_rnsc.length() > 0){
%>
		OD <%=v_rnsc%>;
<%
		}
%>
<%
		if(v_lnsc.length() > 0){
%>
			OS <%=v_lnsc%>;
<%
		}
%>
<%
		if(v_nsc.length() > 0){
%>
			OU <%=v_nsc%>.
<%
		}
%>	
<%
		}
	}
%>
<%	
	if(fieldList.contains("Near vision (cc)")){	
		String v_rncc = distance.get("v_rncc");
		String v_lncc = distance.get("v_lncc");
		String v_ncc = distance.get("v_ncc");
		if((v_rncc.length() > 0) || (v_lncc.length() > 0) || (v_ncc.length() > 0) ){
%>
		<span class="title1">Near vision (cc) </span>
<%	
		if(v_rncc.length() > 0){
%>
		OD <%=v_rncc%>;
<%
		}
%>
<%
		if(v_lncc.length() > 0){
%>
			OS <%=v_lncc%>;
<%
		}
%>
<%
		if(v_ncc.length() > 0){
%>
			OU <%=v_ncc%>.
<%
		}
%>	

<%
		}
	}
%>
</td>
</tr>
<%		
	}
}
%>
<tr><td>&nbsp;</td></tr>
<%
}
//if(fieldList.contains("Fly test")){
if(fieldList1.contains("STEREO VISION")){
%>
<tr>
	<td class="title">STEREO VISION :</td>
</tr>
<%
	ArrayList<Map<String,String>>  fly_test = new ArrayList<Map<String,String>>(); 
	fly_test = (ArrayList<Map<String,String>>)request.getAttribute("fly_test");
	if(fly_test != null){
	for(int i=0;i < fly_test.size(); i ++ ){
		Map<String,String> fly = fly_test.get(i);
		String date_str = fly.get("date");
%>
<tr><td style="word-break:break-all"><span class="title1">(<%= date_str%>)&nbsp; &nbsp;</span>
<%	
		if(fieldList.contains("Fly test")){		
		String v_fly = fly.get("v_fly");
		if(v_fly.length() > 0){
%>
		<span class="title1">Fly test </span>
		<%=v_fly%>.
<%
		}
%>
<%
	}
%>
<%	
		if(fieldList.contains("Stereo-acuity")){	
		String v_stereo = fly.get("v_stereo");
		if(v_stereo.length() > 0){
%>
		<span class="title1">Stereo-acuity </span>
		<%=v_stereo%>.
<%
		}
%>

<%
	}
%>
</td>
</tr>
<%		
	}}
%>
<tr><td>&nbsp;</td></tr>
<%
}
//if(fieldList.contains("Keratometry")){
if(fieldList1.contains("VISION MEASUREMENT")){
%>
<tr>
	<td class="title">VISION MEASUREMENT :</td>
</tr>
<%
	ArrayList<Map<String,String>>  keratometry = new ArrayList<Map<String,String>>(); 
	keratometry = (ArrayList<Map<String,String>>)request.getAttribute("keratometry");
	if(keratometry != null){
	for(int i=0;i < keratometry.size(); i ++ ){
		Map<String,String> keratometry_str = keratometry.get(i);
		String date_str = keratometry_str.get("date");
%>
<tr><td style="word-break:break-all"><span class="title1">(<%= date_str%>)&nbsp; &nbsp;</span>
<%	
	if(fieldList.contains("Keratometry")){		
		String v_rk1 = keratometry_str.get("v_rk1");
		String v_rk2 = keratometry_str.get("v_rk2");
		String v_rkx = keratometry_str.get("v_rkx");
		String v_lk1 = keratometry_str.get("v_lk1");
		String v_lk2 = keratometry_str.get("v_lk2");
		String v_lkx = keratometry_str.get("v_lkx");
		if((v_rk1.length() > 0) || (v_rk2.length() > 0) || (v_rkx.length() > 0) || (v_lk1.length() > 0) || (v_lk2.length() > 0) || (v_lkx.length() > 0) ){
%>
		<span class="title1">Keratometry </span>
<%
		if(v_rk1.length() > 0){
%>
		OD <%=v_rk1%>
<%
		}
%>
<%
		if(v_rk2.length() > 0){
%>
		x <%=v_rk2%>
<%
		}
%>
<%
		if(v_rkx.length() > 0){
%>
		@ <%=v_rkx%>;
<%
		}
%>
<%
		if(v_lk1.length() > 0){
%>
		OS <%=v_lk1%>
<%
		}
%>
<%
		if(v_lk2.length() > 0){
%>
		x <%=v_lk2%>
<%
		}
%>
<%
		if(v_lkx.length() > 0){
%>
		@ <%=v_lkx%>.
<%
		}
%>
<%
		}
	}
%>	
<%	
	if(fieldList.contains("Auto-refraction")){	
		String v_rs = keratometry_str.get("v_rs");
		String v_rc = keratometry_str.get("v_rc");
		String v_rx = keratometry_str.get("v_rx");
		String v_rar = keratometry_str.get("v_rar");
		String v_ls = keratometry_str.get("v_ls");
		String v_lc = keratometry_str.get("v_lc");
		String v_lx = keratometry_str.get("v_lx");
		String v_lar = keratometry_str.get("v_lar");
		if((v_rs.length() > 0) || (v_rc.length() > 0) || (v_rx.length() > 0) || (v_rar.length() > 0) || (v_ls.length() > 0) || (v_lc.length() > 0) || (v_lx.length() > 0) || (v_lar.length() > 0) ){
%>
		<span class="title1">Auto-refraction </span>
<%
		if((v_rs.length() > 0) || (v_rc.length() > 0)){
%>
		OD <%=v_rs%> <%=v_rc%>
<%
		}
%>
<%
		if(v_rx.length() > 0){
%>
		x <%=v_rx%>
<%
		}
%>
<%
		if(v_rar.length() > 0){
%>
		(<%=v_rar%>);
<%
		}
%>
<%
		if((v_ls.length() > 0) || (v_lc.length() > 0)){
%>
		OS <%=v_ls%> <%=v_lc%>
<%
		}
%>
<%
		if(v_lx.length() > 0){
%>
		x <%=v_lx%>
<%
		}
%>
<%
		if(v_lar.length() > 0){
%>
		(<%=v_lar%>).
<%
		}
%>
<%
		}
	}
%>	
<%	
		if(fieldList.contains("Manifest distance")){	
		String v_rds = keratometry_str.get("v_rds");
		String v_rdc = keratometry_str.get("v_rdc");
		String v_rdx = keratometry_str.get("v_rdx");
		String v_rdv = keratometry_str.get("v_rdv");
		String v_lds = keratometry_str.get("v_lds");
		String v_ldc = keratometry_str.get("v_ldc");
		String v_ldx = keratometry_str.get("v_ldx");
		String v_ldv = keratometry_str.get("v_ldv");
		String v_dv = keratometry_str.get("v_dv");
		if((v_rds.length() > 0) || (v_rdc.length() > 0) || (v_rdx.length() > 0) || (v_rdv.length() > 0) || (v_lds.length() > 0) || (v_ldc.length() > 0) || (v_ldx.length() > 0) || (v_ldv.length() > 0) || (v_dv.length() > 0)){
%>
		<span class="title1">Manifest distance </span>
<%	
		if((v_rds.length() > 0) || (v_rdc.length() > 0)){
%>
		OD <%=v_rds%> <%=v_rdc%>
<%
		}
%>
<%
		if(v_rdx.length() > 0){
%>
		x <%=v_rdx%>
<%
		}
%>
<%
		if(v_rdv.length() > 0){
%>
		(<%=v_rdv%>);
<%
		}
%>
<%
		if((v_lds.length() > 0) || (v_ldc.length() > 0)){
%>
		OS <%=v_lds%> <%=v_ldc%>
<%
		}
%>
<%
		if(v_ldx.length() > 0){
%>
		x <%=v_ldx%>
<%
		}
%>
<%
		if(v_ldv.length() > 0){
%>
		(<%=v_ldv%>);
<%
		}
%>
<%
		if(v_dv.length() > 0){
%>
		OU <%=v_dv%>.
<%
		}
%>
<%
		}
	}
%>	
<%	
	if(fieldList.contains("Manifest near")){		
		String v_rns = keratometry_str.get("v_rns");
		String v_rnc = keratometry_str.get("v_rnc");
		String v_rnx = keratometry_str.get("v_rnx");
		String v_rnv = keratometry_str.get("v_rnv");
		String v_lns = keratometry_str.get("v_lns");
		String v_lnc = keratometry_str.get("v_lnc");
		String v_lnx = keratometry_str.get("v_lnx");
		String v_lnv = keratometry_str.get("v_lnv");
		String v_nv = keratometry_str.get("v_nv");
		if((v_rns.length() > 0) || (v_rnc.length() > 0) || (v_rnx.length() > 0) || (v_rnv.length() > 0) || (v_lns.length() > 0) || (v_lnc.length() > 0) || (v_lnx.length() > 0) || (v_lnv.length() > 0) || (v_nv.length() > 0)){
%>
		<span class="title1">Manifest near </span>
<%		
		if((v_rns.length() > 0) || (v_rnc.length() > 0)){
%>
		OD <%=v_rns%> <%=v_rnc%>
<%
		}
%>
<%
		if(v_rnx.length() > 0){
%>
		x <%=v_rnx%>
<%
		}
%>
<%
		if(v_rnv.length() > 0){
%>
		(<%=v_rnv%>);
<%
		}
%>
<%
		if((v_lns.length() > 0) || (v_lnc.length() > 0)){
%>
		OS <%=v_lns%> <%=v_lnc%>
<%
		}
%>
<%
		if(v_lnx.length() > 0){
%>
		x <%=v_lnx%>
<%
		}
%>
<%
		if(v_lnv.length() > 0){
%>
		(<%=v_lnv%>);
<%
		}
%>
<%
		if(v_nv.length() > 0){
%>
		OU <%=v_nv%>.
<%
		}
%>
<%
		}
	}
%>
<%	
	if(fieldList.contains("Cycloplegic refraction")){		
		String v_rcs = keratometry_str.get("v_rcs");
		String v_rcc = keratometry_str.get("v_rcc");
		String v_rcx = keratometry_str.get("v_rcx");
		String v_rcv = keratometry_str.get("v_rcv");
		String v_lcs = keratometry_str.get("v_lcs");
		String v_lcc = keratometry_str.get("v_lcc");
		String v_lcx = keratometry_str.get("v_lcx");
		String v_lcv = keratometry_str.get("v_lcv");
		if((v_rcs.length() > 0) || (v_rcc.length() > 0) || (v_rcx.length() > 0) || (v_rcv.length() > 0) || (v_lcs.length() > 0) || (v_lcc.length() > 0) || (v_lcx.length() > 0) || (v_lcv.length() > 0) ){
%>
		<span class="title1">Cycloplegic refraction </span>
<%
		if((v_rcs.length() > 0) || (v_rcc.length() > 0)){
%>
		OD <%=v_rcs%> <%=v_rcc%>
<%
		}
%>
<%
		if(v_rcx.length() > 0){
%>
		x <%=v_rcx%>
<%
		}
%>
<%
		if(v_rcv.length() > 0){
%>
		(<%=v_rcv%>);
<%
		}
%>
<%
		if((v_lcs.length() > 0) || (v_lcc.length() > 0)){
%>
		OS <%=v_lcs%> <%=v_lcc%>
<%
		}
%>
<%
		if(v_lcx.length() > 0){
%>
		x <%=v_lcx%>
<%
		}
%>
<%
		if(v_lcv.length() > 0){
%>
		(<%=v_lcv%>).
<%
		}
%>
<%
		}
	}
%>
</td>	
</tr>
<%}}%>
<tr><td>&nbsp;</td></tr>
<%
}
//if(fieldList.contains("NCT")){
if(fieldList1.contains("INTRAOCULAR PRESSURE")){
%>
<tr>
	<td class="title">INTRAOCULAR PRESSURE :</td>
</tr>
<%
	ArrayList<Map<String,String>>  nct = new ArrayList<Map<String,String>>(); 
	nct = (ArrayList<Map<String,String>>)request.getAttribute("nct");
	if(nct != null){
	for(int i=0;i < nct.size(); i ++ ){
		Map<String,String> nct_str = nct.get(i);
		String date_str = nct_str.get("date");
%>
<tr><td style="word-break:break-all"><span class="title1">(<%= date_str%>)&nbsp; &nbsp;</span>
<%	
	if(fieldList.contains("NCT")){		
		String iop_rn = nct_str.get("iop_rn");
		String iop_ln = nct_str.get("iop_ln");
		String iop_ntime = nct_str.get("iop_ntime");
		if(iop_ntime == null){
			iop_ntime = "";
		}
		if((iop_rn.length() > 0) || (iop_ln.length() > 0)){
%>
		<span class="title1">NCT </span>
<%
		if(iop_rn.length() > 0){
%>
		OD <%=iop_rn%>;
<%
		}
%>
<%		
		if(iop_ln.length() > 0){
%>
		OS <%=iop_ln%>;
<%
		}
%>	
<%		
		if(iop_ntime.length() > 0){
%>
		(<%=iop_ntime%>).
<%
		}
%>		
<%
		}
	}
%>	
<%	
	if(fieldList.contains("Applanation")){		
		String iop_ra = nct_str.get("iop_ra");
		String iop_la = nct_str.get("iop_la");
		String iop_atime = nct_str.get("iop_atime");
		if(iop_atime == null){
			iop_atime = "";
		}
		if((iop_ra.length() > 0) || (iop_la.length() > 0)){
%>
		<span class="title1">Applanation  </span>
<%		
		if(iop_ra.length() > 0){
%>
		OD <%=iop_ra%>;
<%
		}
%>
<%		
		if(iop_la.length() > 0){
%>
		OS <%=iop_la%>;
<%
		}
%>	
<%		
		if(iop_atime.length() > 0){
%>
		(<%=iop_atime%>).
<%
		}
%>		
<%
		}
	}
%>
<%	
	if(fieldList.contains("Central corneal thickness")){		
		String cct_r = nct_str.get("cct_r");
		String cct_l = nct_str.get("cct_l");
		if((cct_r.length() > 0) || (cct_l.length() > 0)){
%>
		<span class="title1">Central corneal thickness </span>
<%
		if(cct_r.length() > 0){
%>
		OD <%=cct_r%>microns;
<%
		}
%>
<%		
		if(cct_l.length() > 0){
%>
		OS <%=cct_l%>microns.
<%
		}
%>		
<%
		}
	}
%>	
</td>	
</tr>
<%}}%>
<tr><td>&nbsp;</td></tr>
<%
}
//if(fieldList.contains("Dominance")){
if(fieldList1.contains("REFRACTIVE")){
%>
<tr>
	<td class="title">REFRACTIVE :</td>
</tr>
<%
	ArrayList<Map<String,String>>  dominance = new ArrayList<Map<String,String>>(); 
	dominance = (ArrayList<Map<String,String>>)request.getAttribute("dominance");
	if(dominance != null){
	for(int i=0;i < dominance.size(); i ++ ){
		Map<String,String> dominance_str = dominance.get(i);
		String date_str = dominance_str.get("date");
%>
<tr><td style="word-break:break-all"><span class="title1">(<%= date_str%>)&nbsp; &nbsp;</span>
<%	
	if(fieldList.contains("Dominance")){
		
		String ref_rdom = dominance_str.get("ref_rdom");
		String ref_ldom = dominance_str.get("ref_ldom");
		if((ref_rdom.length() > 0) || (ref_ldom.length() > 0)){
%>
		<span class="title1">Dominance </span>
<%
		if(ref_rdom.length() > 0){
%>
		OD <%=ref_rdom%>;
<%
		}
%>
<%		
		if(ref_ldom.length() > 0){
%>
		OS <%=ref_ldom%>.
<%
		}
%>		
<%
		}
	}
%>	
<%	
	if(fieldList.contains("Mesopic pupil size")){		
		String ref_rpdim = dominance_str.get("ref_rpdim");
		String ref_lpdim = dominance_str.get("ref_lpdim");
		if((ref_rpdim.length() > 0) || (ref_lpdim.length() > 0)){
%>
		<span class="title1">Mesopic pupil size </span>
<%
		if(ref_rpdim.length() > 0){
%>
		OD <%=ref_rpdim%>;
<%
		}
%>
<%		
		if(ref_lpdim.length() > 0){
%>
		OS <%=ref_lpdim%>.
<%
		}
%>
<%
		}
	}
%>
<%	
		if(fieldList.contains("Angle Kappa")){		
		String ref_rkappa = dominance_str.get("ref_rkappa");
		String ref_lkappa = dominance_str.get("ref_lkappa");
		if((ref_rkappa.length() > 0) || (ref_lkappa.length() > 0)){
%>
		<span class="title1">Angle Kappa </span>
<%
		if(ref_rkappa.length() > 0){
%>
		OD <%=ref_rkappa%>;
<%
		}
%>
<%		
		if(ref_lkappa.length() > 0){
%>
		OS <%=ref_lkappa%>.
<%
		}
%>		

<%
		}
	}
%>
</td>
</tr>
<%}}%>
<tr><td>&nbsp;</td></tr>
<%
}
//if(fieldList.contains("Colour vision")){
if(fieldList1.contains("OTHER EXAM")){
%>
<tr>
	<td class="title">OTHER EXAM :</td>
</tr>
<%
	ArrayList<Map<String,String>>  colour_vision = new ArrayList<Map<String,String>>(); 
	colour_vision = (ArrayList<Map<String,String>>)request.getAttribute("colour_vision");
	if(colour_vision != null){
	for(int i=0;i < colour_vision.size(); i ++ ){
		Map<String,String> colour = colour_vision.get(i);
		String date_str = colour.get("date");
%>
<tr><td style="word-break:break-all"><span class="title1">(<%= date_str%>)&nbsp; &nbsp;</span>
<%	
		if(fieldList.contains("Colour vision")){		
		String o_rcolour = colour.get("o_rcolour");
		String o_lcolour = colour.get("o_lcolour");
		if((o_rcolour.length() > 0) || (o_lcolour.length() > 0)){
%>
		<span class="title1">Colour vision </span>
<%
		if(o_rcolour.length() > 0){
%>
		OD <%=o_rcolour%>;
<%
		}
%>
<%		
		if(o_lcolour.length() > 0){
%>
		OS <%=o_lcolour%>.
<%
		}
%>		
<%
		}
	}
%>
<%	
	if(fieldList.contains("Pupil")){		
		String o_rpupil = colour.get("o_rpupil");
		String o_lpupil = colour.get("o_lpupil");
		if((o_rpupil.length() > 0) || (o_lpupil.length() > 0)){
%>
		<span class="title1">Pupil </span>
<%
		if(o_rpupil.length() > 0){
%>
		OD <%=o_rpupil%>;
<%
		}
%>
<%		
		if(o_lpupil.length() > 0){
%>
		OS <%=o_lpupil%>.
<%
		}
%>		
<%
		}
	}
%>
<%	
	if(fieldList.contains("Amsler grid")){		
		String o_ramsler = colour.get("o_ramsler");
		String o_lamsler = colour.get("o_lamsler");
		if((o_ramsler.length() > 0) || (o_lamsler.length() > 0)){
%>
		<span class="title1">Amsler grid </span>
<%
		if(o_ramsler.length() > 0){
%>
		OD <%=o_ramsler%>;
<%
		}
%>
<%		
		if(o_lamsler.length() > 0){
%>
		OS <%=o_lamsler%>.
<%
		}
%>		
<%
		}
	}
%>
<%	
		if(fieldList.contains("Potential acuity meter")){		
		String o_rpam = colour.get("o_rpam");
		String o_lpam = colour.get("o_lpam");
		if((o_rpam.length() > 0) || (o_lpam.length() > 0)){
%>
		<span class="title1">Potential acuity meter </span>
<%
		if(o_rpam.length() > 0){
%>
		OD <%=o_rpam%>;
<%
		}
%>
<%		
		if(o_lpam.length() > 0){
%>
		OS <%=o_lpam%>.
<%
		}
%>		
<%
		}
	}
%>
<%	
	if(fieldList.contains("Confrontation fields")){		
		String o_rconf = colour.get("o_rconf");
		String o_lconf = colour.get("o_lconf");
		if((o_rconf.length() > 0) || (o_lconf.length() > 0)){
%>
		<span class="title1">Confrontation fields </span>
<%
		if(o_rconf.length() > 0){
%>
		OD <%=o_rconf%>;
<%
		}
%>
<%		
		if(o_lconf.length() > 0){
%>
		OS <%=o_lconf%>.
<%
		}
%>		
<%
		}
	}
%>
<%	
	if(fieldList.contains("Maddox rod")){		
		String o_mad = colour.get("o_mad");
		if(o_mad.length() > 0){
%>
		<span class="title1">Maddox rod </span>
		<%=o_mad%>.
<%
		}
%>
<%
	}
%>	
<%	
	if(fieldList.contains("Bagolini test")){		
		String o_bag = colour.get("o_bag");
		if(o_bag.length() > 0){
%>
		<span class="title1">Bagolini test </span>
		<%=o_bag%>.
<%
		}
%>
<%
	}
%>
<%	
	if(fieldList.contains("Worth 4 Dot (distance)")){		
		String o_w4dd = colour.get("o_w4dd");
		if(o_w4dd.length() > 0){
%>
		<span class="title1">Worth 4 Dot (distance) </span>
		<%=o_w4dd%>.
<%
		}
%>
<%
	}
%>
<%	
	if(fieldList.contains("Worth 4 Dot (near)")){		
		String o_w4dn = colour.get("o_w4dn");
		if(o_w4dn.length() > 0){
%>
		<span class="title1">Worth 4 Dot (near) </span>
		<%=o_w4dn%>.
<%
		}
%>

<%
	}
%>
</td>
</tr>
<%}}%>
<tr><td>&nbsp;</td></tr>
<%
}
//if(fieldList.contains("DUCTION/DIPLOPIA TESTING")){
if(fieldList1.contains("DUCTION/DIPLOPIA TESTING")){
%>
<tr>
	<td class="title">DUCTION/DIPLOPIA TESTING :</td>
</tr>
<%
	ArrayList<Map<String,String>>  ductlion = new ArrayList<Map<String,String>>(); 
	ductlion = (ArrayList<Map<String,String>>)request.getAttribute("ductlion");
	if(ductlion != null){
	for(int i=0;i < ductlion.size(); i ++ ){
		Map<String,String> ductlion_str = ductlion.get(i);
		String duc_rur = ductlion_str.get("duc_rur");
		String duc_rul = ductlion_str.get("duc_rul");
		String duc_lur = ductlion_str.get("duc_lur");
		String duc_lul = ductlion_str.get("duc_lul");
		String dip_ur = ductlion_str.get("dip_ur");
		String dip_u = ductlion_str.get("dip_u");
		String dip_ul = ductlion_str.get("dip_ul");
		String duc_rr = ductlion_str.get("duc_rr");
		String duc_rl = ductlion_str.get("duc_rl");
		String duc_lr = ductlion_str.get("duc_lr");
		String duc_ll = ductlion_str.get("duc_ll");
		String dip_r = ductlion_str.get("dip_r");
		String dip_p = ductlion_str.get("dip_p");
		String dip_l = ductlion_str.get("dip_l");
		String duc_rdr = ductlion_str.get("duc_rdr");
		String duc_rdl = ductlion_str.get("duc_rdl");
		String duc_ldr = ductlion_str.get("duc_ldr");
		String duc_ldl = ductlion_str.get("duc_ldl");
		String dip_dr = ductlion_str.get("dip_dr");
		String dip_d = ductlion_str.get("dip_d");
		String dip_dl = ductlion_str.get("dip_dl");
		String date_str = ductlion_str.get("date");
%>
<tr>
<td style="white-space:nowrap">
<table cellpadding="0" cellspacing="0" border="0">
<tr>
<%
		if((duc_rur.length() > 0) || (duc_rul.length() > 0) || (duc_rr.length() > 0) || (duc_rl.length() > 0) || (duc_rdr.length() > 0) || (duc_rdl.length() > 0)){
%>			
			<td>
			OD<span class="title1">(<%= date_str%>)</span>		
			
			<table>
			<tr>
				<td>
<%				
					if(duc_rur.length() > 0){
%>
					<%=duc_rur%>
<%
				}
%>
				</td>
				<td>
<%				
					if(duc_rul.length() > 0){
%>
					<%=duc_rul%>
<%
				}
%>
				</td>
			</tr>
			<tr>
				<td>
<%				
					if(duc_rr.length() > 0){
%>
					<%=duc_rr%>
<%
				}
%>
				</td>
				<td>
<%				
					if(duc_rl.length() > 0){
%>
					<%=duc_rl%>
<%
				}
%>
				</td>
			</tr>
			<tr>
				<td>
<%				
					if(duc_rdr.length() > 0){
%>
					<%=duc_rdr%>
<%
				}
%>
				</td>
				<td>
<%				
					if(duc_rdl.length() > 0){
%>
					<%=duc_rdl%>
<%
				}
%>
				</td>
			</tr>
			</table>
			</td>
<%
		}
		if((duc_lur.length() > 0) || (duc_lul.length() > 0) || (duc_lr.length() > 0) || (duc_ll.length() > 0) || (duc_ldr.length() > 0) || (duc_ldl.length() > 0)){
%>
			<td>
			OS<span class="title1">(<%= date_str%>)</span>
			<table>
			<tr>
				<td>
<%
				if(duc_lur.length() > 0){
%>
					<%=duc_lur%>
<%
				}
%>
				</td>
				<td>
<%
				if(duc_lul.length() > 0){
%>
					<%=duc_lul%>
<%
				}
%>
				</td>
			</tr>
			<tr>
				<td>
<%
				if(duc_lr.length() > 0){
%>
					<%=duc_lr%>
<%
				}
%>
				</td>
				<td>
<%
				if(duc_ll.length() > 0){
%>
					<%=duc_ll%>
<%
				}
%>
				</td>
			</tr>
			<tr>
				<td>
<%
				if(duc_ldr.length() > 0){
%>
					<%=duc_ldr%>
<%
				}
%>
				</td>
				<td>
<%
				if(duc_ldl.length() > 0){
%>
					<%=duc_ldl%>
<%
				}
%>
				</td>
			</tr>
			</table>
			</td>
<%
		}
		if((dip_ur.length() > 0) || (dip_u.length() > 0) || (dip_r.length() > 0) || (dip_p.length() > 0) || (dip_dr.length() > 0) || (dip_d.length() > 0)){
%>
			<td>
			OU<span class="title1">(<%= date_str%>)</span>
			<table>
			<tr>
				<td>
<%
				if(dip_ur.length() > 0){
%>
					<%=dip_ur%>
<%
				}
%>
				</td>
				<td>
<%
				if(dip_u.length() > 0){
%>
					<%=dip_u%>
<%
				}
%>
				</td>
			</tr>
			<tr>
				<td>
<%
				if(dip_r.length() > 0){
%>
					<%=dip_r%>
<%
				}
%>
				</td>
				<td>
<%
				if(dip_p.length() > 0){
%>
					<%=dip_p%>
<%
				}
%>
				</td>
			</tr>
			<tr>
				<td>
<%
				if(dip_dr.length() > 0){
%>
					<%=dip_dr%>
<%
				}
%>
				</td>
				<td>
<%
				if(dip_d.length() > 0){
%>
					<%=dip_d%>
<%
				}
%>
				</td>
			</tr>
			</table>
			</td>
<%
		}
%>
			</tr>
			</table>
			</td>
</tr>
<%}}%>
<tr><td>&nbsp;</td></tr>
<%
}
//if(fieldList.contains("Primary gaze")){
if(fieldList1.contains("DEVIATION MEASUREMENT")){
%>
<tr>
	<td class="title">DEVIATION MEASUREMENT :</td>
</tr>
<%
	ArrayList<Map<String,String>>  primary = new ArrayList<Map<String,String>>(); 
	primary = (ArrayList<Map<String,String>>)request.getAttribute("primary");
	if(primary != null){
	for(int i=0;i < primary.size(); i ++ ){
		Map<String,String> primary_str = primary.get(i);
		String date_str = primary_str.get("date");
%>
<tr><td style="word-break:break-all"><span class="title1">(<%= date_str%>)&nbsp; &nbsp;</span>
<%
	if(fieldList.contains("Primary gaze")){
		String dev_p = primary_str.get("dev_p");
		if(dev_p.length() > 0){
%>
		<span class="title1">Primary gaze </span>
			<%=dev_p%>.
<%   	}
	}
%>
<%
	if(fieldList.contains("Up gaze")){
		String dev_u = primary_str.get("dev_u");
		if(dev_u.length() > 0){
%>
		<span class="title1">Up gaze </span>
			<%=dev_u%>.
<%   	}
	}
%>
<%
	if(fieldList.contains("Down gaze")){
		String dev_d = primary_str.get("dev_d");
		if(dev_d.length() > 0){
%>
		<span class="title1">Down gaze </span>
			<%=dev_d%>.
<%   	}
	}
%>
<%
	if(fieldList.contains("Right gaze")){
		String dev_r = primary_str.get("dev_r");
		if(dev_r.length() > 0){
%>
		<span class="title1">Right gaze </span>
			<%=dev_r%>.
<%   	}
	}
%>
<%
	if(fieldList.contains("Left gaze")){
		String dev_l = primary_str.get("dev_l");
		if(dev_l.length() > 0){
%>
		<span class="title1">Left  gaze </span>
			<%=dev_l%>.
<%   	}
	}
%>
<%
	if(fieldList.contains("Right head tilt")){
		String dev_rt = primary_str.get("dev_rt");
		if(dev_rt.length() > 0){
%>
		<span class="title1">Right head tilt </span>
			<%=dev_rt%>.
<%   	}
	}
%>
<%
	if(fieldList.contains("Left head tilt")){
		String dev_lt = primary_str.get("dev_lt");
		if(dev_lt.length() > 0){
%>
		<span class="title1">Left head tilt </span>
			<%=dev_lt%>.
<%   	}
	}
%>
<%
	if(fieldList.contains("Near")){
		String dev_near = primary_str.get("dev_near");
		if(dev_near.length() > 0){
%>
		<span class="title1">Near </span>
			<%=dev_near%>.
<%   	}
	}
%>
<%
	if(fieldList.contains("Near with +3D add")){
		String dev_plus3 = primary_str.get("dev_plus3");
		if(dev_plus3.length() > 0){
%>
		<span class="title1">Near with +3D add </span>
			<%=dev_plus3%>.
<%   	}
	}
%>
<%
	if(fieldList.contains("Far distance")){
		String dev_far = primary_str.get("dev_far");
		if(dev_far.length() > 0){
%>
		<span class="title1">Far distance </span>
			<%=dev_far%>.
<%   	}
	}
%>
</td>
</tr>
<%}}%>
<tr><td>&nbsp;</td></tr>
<%
}
//if(fieldList.contains("Retropulsion")){
if(fieldList1.contains("EXTERNAL/ORBIT")){
%>
<tr>
	<td class="title">EXTERNAL/ORBIT :</td>
</tr>
<%
	ArrayList<Map<String,String>>  retropulsion = new ArrayList<Map<String,String>>(); 
	retropulsion = (ArrayList<Map<String,String>>)request.getAttribute("retropulsion");
	if(retropulsion != null){
	for(int i=0;i < retropulsion.size(); i ++ ){
		Map<String,String> retropulsion_str = retropulsion.get(i);
		String date_str = retropulsion_str.get("date");
%>
<tr><td style="word-break:break-all"><span class="title1">(<%= date_str%>)&nbsp; &nbsp;</span>
<%
	if(fieldList.contains("Face")){
		String ext_rface = retropulsion_str.get("ext_rface");
		String ext_lface = retropulsion_str.get("ext_lface");	
		if((ext_rface.length() > 0) || (ext_lface.length() > 0)){
%>
		<span class="title1">Face </span>
<%
			if(ext_rface.length() > 0){
%>
			Right side <%=ext_rface%>;
<%   		
			}
%>
<%
			if(ext_lface.length() > 0){
%>
			Left  side <%=ext_lface%>.
<%   		}

%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Retropulsion")){
		String ext_rretro = retropulsion_str.get("ext_rretro");
		String ext_lretro = retropulsion_str.get("ext_lretro");	
		if((ext_rretro.length() > 0) || (ext_lretro.length() > 0)){
%>
		<span class="title1">Retropulsion </span>
<%
			if(ext_rretro.length() > 0){
%>
			OD <%=ext_rretro%>;
<%   		
			}
%>
<%
			if(ext_lretro.length() > 0){
%>
			OS <%=ext_lretro%>.
<%   		}

%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Hertel")){
		String ext_rhertel = retropulsion_str.get("ext_rhertel");
		String ext_lhertel = retropulsion_str.get("ext_lhertel");	
		if((ext_rhertel.length() > 0) || (ext_lhertel.length() > 0)){
%>
		<span class="title1">Hertel </span>
<%
			if(ext_rhertel.length() > 0){
%>
			OD <%=ext_rhertel%>;
<%   		
			}
%>
<%
			if(ext_lhertel.length() > 0){
%>
			OS <%=ext_lhertel%>.
<%   		}

%>
<%
		}
	}
%>
</td>
</tr>
<%}}%>
<tr><td>&nbsp;</td></tr>
<%
}
//if(fieldList.contains("Upper lid")){
if(fieldList1.contains("EYELID/NASOLACRIMAL DUCT")){
%>
<tr>
	<td class="title">EYELID/NASOLACRIMAL DUCT :</td>
</tr>
<%
	ArrayList<Map<String,String>>  upper = new ArrayList<Map<String,String>>(); 
	upper = (ArrayList<Map<String,String>>)request.getAttribute("upper");
	if(upper != null){
	for(int i=0;i < upper.size(); i ++ ){
		Map<String,String> upper_str = upper.get(i);
		String date_str = upper_str.get("date");
%>
<tr><td style="word-break:break-all"><span class="title1">(<%= date_str%>)&nbsp; &nbsp;</span>
<%
	if(fieldList.contains("Upper lid")){
		String ext_rul = upper_str.get("ext_rul");
		String ext_lul = upper_str.get("ext_lul");	
		if((ext_rul.length() > 0) || (ext_lul.length() > 0)){
%>
		<span class="title1">Upper lid </span>
<%
			if(ext_rul.length() > 0){
%>
			OD <%=ext_rul%>;
<%   		
			}
%>
<%
			if(ext_lul.length() > 0){
%>
			OS <%=ext_lul%>.
<%   		}

%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Lower lid")){
		String ext_rll = upper_str.get("ext_rll");
		String ext_lll = upper_str.get("ext_lll");	
		if((ext_rll.length() > 0) || (ext_lll.length() > 0)){
%>
		<span class="title1">Lower lid </span>
<%
			if(ext_rll.length() > 0){
%>
			OD <%=ext_rll%>;
<%   		
			}
%>
<%
			if(ext_lll.length() > 0){
%>
			OS <%=ext_lll%>.
<%   		}

%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Lacrimal lake")){
		String ext_rlake = upper_str.get("ext_rlake");
		String ext_llake = upper_str.get("ext_llake");	
		if((ext_rlake.length() > 0) || (ext_llake.length() > 0)){
%>
		<span class="title1">Lacrimal lake </span>
<%
			if(ext_rlake.length() > 0){
%>
			OD <%=ext_rlake%>;
<%   		
			}
%>
<%
			if(ext_llake.length() > 0){
%>
			OS <%=ext_llake%>.
<%   		}

%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Lacrimal irrigation")){
		String ext_rirrig = upper_str.get("ext_rirrig");
		String ext_lirrig = upper_str.get("ext_lirrig");	
		if((ext_rirrig.length() > 0) || (ext_lirrig.length() > 0)){
%>
		<span class="title1">Lacrimal irrigation </span>
<%
			if(ext_rirrig.length() > 0){
%>
			OD <%=ext_rirrig%>;
<%   		
			}
%>
<%
			if(ext_lirrig.length() > 0){
%>
			OS <%=ext_lirrig%>.
<%   		}

%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Punctum")){
		String ext_rpunc = upper_str.get("ext_rpunc");
		String ext_lpunc = upper_str.get("ext_lpunc");	
		if((ext_rpunc.length() > 0) || (ext_lpunc.length() > 0)){
%>
		<span class="title1">Punctum  </span>
<%
			if(ext_rpunc.length() > 0){
%>
			OD <%=ext_rpunc%>;
<%   		
			}
%>
<%
			if(ext_lpunc.length() > 0){
%>
			OS <%=ext_lpunc%>.
<%   		}

%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Nasolacrimal duct")){
		String ext_rnld = upper_str.get("ext_rnld");
		String ext_lnld = upper_str.get("ext_lnld");	
		if((ext_rnld.length() > 0) || (ext_lnld.length() > 0)){
%>
		<span class="title1">Nasolacrimal duct  </span>
<%
			if(ext_rnld.length() > 0){
%>
			OD <%=ext_rnld%>;
<%   		
			}
%>
<%
			if(ext_lnld.length() > 0){
%>
			OS <%=ext_lnld%>.
<%   		}

%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Dye disappearance")){
		String ext_rdye = upper_str.get("ext_rdye");
		String ext_ldye = upper_str.get("ext_ldye");	
		if((ext_rdye.length() > 0) || (ext_ldye.length() > 0)){
%>
		<span class="title1">Dye disappearance  </span>
<%
			if(ext_rdye.length() > 0){
%>
			OD <%=ext_rdye%>;
<%   		
			}
%>
<%
			if(ext_ldye.length() > 0){
%>
			OS <%=ext_ldye%>.
<%   		}

%>
<%
		}
	}
%>
</td>
</tr>
<%}}%>
<tr><td>&nbsp;</td></tr>
<%
}
//if(fieldList.contains("Margin reflex distance")){
if(fieldList1.contains("EYELID MEASUREMENT")){
%>
<tr>
	<td class="title">EYELID MEASUREMENT :</td>
</tr>
<%
	ArrayList<Map<String,String>>  margin = new ArrayList<Map<String,String>>(); 
	margin = (ArrayList<Map<String,String>>)request.getAttribute("margin");
	if(margin != null){
	for(int i=0;i < margin.size(); i ++ ){
		Map<String,String> margin_str = margin.get(i);
		String date_str = margin_str.get("date");
%>
<tr><td style="word-break:break-all"><span class="title1">(<%= date_str%>)&nbsp; &nbsp;</span>
<%
	if(fieldList.contains("Margin reflex distance")){
		String lid_rmrd = margin_str.get("lid_rmrd");
		String lid_lmrd = margin_str.get("lid_lmrd");	
		if((lid_rmrd.length() > 0) || (lid_lmrd.length() > 0)){
%>
		<span class="title1">Margin reflex distance  </span>
<%
			if(lid_rmrd.length() > 0){
%>
			OD <%=lid_rmrd%>;
<%   		
			}
%>
<%
			if(lid_lmrd.length() > 0){
%>
			OS <%=lid_lmrd%>.
<%   		}

%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Inferior scleral show")){
		String lid_riss = margin_str.get("lid_riss");
		String lid_liss = margin_str.get("lid_liss");	
		if((lid_riss.length() > 0) || (lid_liss.length() > 0)){
%>
		<span class="title1">Inferior scleral show </span> 
<%
			if(lid_riss.length() > 0){
%>
			OD <%=lid_riss%>;
<%   		
			}
%>
<%
			if(lid_liss.length() > 0){
%>
			OS <%=lid_liss%>.
<%   		}

%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Levator function")){
		String lid_rlev = margin_str.get("lid_rlev");
		String lid_llev = margin_str.get("lid_llev");	
		if((lid_rlev.length() > 0) || (lid_llev.length() > 0)){
%>
		<span class="title1">Levator function  </span>
<%
			if(lid_rlev.length() > 0){
%>
			OD <%=lid_rlev%>;
<%   		
			}
%>
<%
			if(lid_llev.length() > 0){
%>
			OS <%=lid_llev%>.
<%   		}

%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Lagophthalmos")){
		String lid_rlag = margin_str.get("lid_rlag");
		String lid_llag = margin_str.get("lid_llag");	
		if((lid_rlag.length() > 0) || (lid_llag.length() > 0)){
%>
		<span class="title1">Lagophthalmos  </span>
<%
			if(lid_rlag.length() > 0){
%>
			OD <%=lid_rlag%>;
<%   		
			}
%>
<%
			if(lid_llag.length() > 0){
%>
			OS <%=lid_llag%>.
<%   		}

%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Blink reflex")){
		String lid_rblink = margin_str.get("lid_rblink");
		String lid_lblink = margin_str.get("lid_lblink");	
		if((lid_rblink.length() > 0) || (lid_lblink.length() > 0)){
%>
		<span class="title1">Blink reflex  </span>
<%
			if(lid_rblink.length() > 0){
%>
			OD <%=lid_rblink%>;
<%   		
			}
%>
<%
			if(lid_lblink.length() > 0){
%>
			OS <%=lid_lblink%>.
<%   		}

%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Cranial Nerve VII function")){
		String lid_rcn7 = margin_str.get("lid_rcn7");
		String lid_lcn7 = margin_str.get("lid_lcn7");	
		if((lid_rcn7.length() > 0) || (lid_lcn7.length() > 0)){
%>
		<span class="title1">Cranial Nerve VII function  </span>
<%
			if(lid_rcn7.length() > 0){
%>
			OD <%=lid_rcn7%>;
<%   		
			}
%>
<%
			if(lid_lcn7.length() > 0){
%>
			OS <%=lid_lcn7%>.
<%   		}

%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Bells phenomenon")){
		String lid_rbell = margin_str.get("lid_rbell");
		String lid_lbell = margin_str.get("lid_lbell");	
		if((lid_rbell.length() > 0) || (lid_lbell.length() > 0)){
%>
		<span class="title1">Bell's phenomenon  </span>
<%
			if(lid_rbell.length() > 0){
%>
			OD <%=lid_rbell%>;
<%   		
			}
%>
<%
			if(lid_lbell.length() > 0){
%>
			OS <%=lid_lbell%>.
<%   		}

%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Schirmer test")){
		String lid_rschirm = margin_str.get("lid_rschirm");
		String lid_lschirm = margin_str.get("lid_lschirm");	
		if((lid_rschirm.length() > 0) || (lid_lschirm.length() > 0)){
%>
		<span class="title1">Schirmer test  </span>
<%
			if(lid_rschirm.length() > 0){
%>
			OD <%=lid_rschirm%>;
<%   		
			}
%>
<%
			if(lid_lschirm.length() > 0){
%>
			OS <%=lid_lschirm%>.
<%   		}

%>
<%
		}
	}
%>
</td>
</tr>
<%}}%>
<tr><td>&nbsp;</td></tr>
<%
}
//if(fieldList.contains("Cornea")){
if(fieldList1.contains("ANTERIOR SEGMENT")){
%>
<tr>
	<td class="title">ANTERIOR SEGMENT :</td>
</tr>
<%
	ArrayList<Map<String,String>>  cornea = new ArrayList<Map<String,String>>(); 
	cornea = (ArrayList<Map<String,String>>)request.getAttribute("cornea");
	if(cornea != null){
	for(int i=0;i < cornea.size(); i ++ ){
		Map<String,String> cornea_str = cornea.get(i);
		String date_str = cornea_str.get("date");
%>
<tr><td style="word-break:break-all"><span class="title1">(<%= date_str%>)&nbsp; &nbsp;</span>
<%
	if(fieldList.contains("Cornea")){
		String a_rk = cornea_str.get("a_rk");
		String a_lk = cornea_str.get("a_lk");	
		if((a_rk.length() > 0) || (a_lk.length() > 0)){
%>
		<span class="title1">Cornea  </span>
<%
			if(a_rk.length() > 0){
%>
			OD <%=a_rk%>;
<%   		
			}
%>
<%
			if(a_lk.length() > 0){
%>
			OS <%=a_lk%>.
<%   	
		}
%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Conjunctiva/Sclera")){
		String a_rconj = cornea_str.get("a_rconj");
		String a_lconj = cornea_str.get("a_lconj");	
		if((a_rconj.length() > 0) || (a_lconj.length() > 0)){
%>
		<span class="title1">Conjunctiva/Sclera  </span>
<%
			if(a_rconj.length() > 0){
%>
			OD <%=a_rconj%>;
<%   		
			}
%>
<%
			if(a_lconj.length() > 0){
%>
			OS <%=a_lconj%>.
<%   		
			}
%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Anterior chamber")){
		String a_rac = cornea_str.get("a_rac");
		String a_lac = cornea_str.get("a_lac");	
		if((a_rac.length() > 0) || (a_lac.length() > 0)){
%>
		<span class="title1">Anterior chamber  </span>
<%
			if(a_rac.length() > 0){
%>
			OD <%=a_rac%>;
<%   		
			}
%>
<%
			if(a_lac.length() > 0){
%>
			OS <%=a_lac%>.
<%   		
			}
%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Angle")){
		String a_rangle_1 = cornea_str.get("a_rangle_1");
		String a_rangle_2 = cornea_str.get("a_rangle_2");
		String a_rangle_3 = cornea_str.get("a_rangle_3");
		String a_rangle_4 = cornea_str.get("a_rangle_4");
		String a_rangle_5 = cornea_str.get("a_rangle_5");
		String a_langle_1 = cornea_str.get("a_langle_1");	
		String a_langle_2 = cornea_str.get("a_langle_2");	
		String a_langle_3 = cornea_str.get("a_langle_3");	
		String a_langle_4 = cornea_str.get("a_langle_4");	
		String a_langle_5 = cornea_str.get("a_langle_5");	
%>
		<span class="title1">Angle </span>
<%
		if((a_rangle_1.length() > 0) || (a_rangle_2.length() > 0) || (a_rangle_3.length() > 0) || (a_rangle_4.length() > 0) || (a_rangle_5.length() > 0)){
%>		
<%
			if(a_rangle_3.length() > 0){
%>
			<%=a_rangle_3%>
<%   		
			}
			if((a_rangle_1.length() > 0) || (a_rangle_2.length() > 0) || (a_rangle_4.length() > 0) || (a_rangle_5.length() > 0)){
%>
			(
<%			
				if(a_rangle_1.length() > 0){
%>
					superior <%=a_rangle_1%>
<%
				}
%>
<%
				if(a_rangle_4.length() > 0){
%>
					nasal <%=a_rangle_4%>
<%
				}
%>
<%
				if(a_rangle_5.length() > 0){
%>
					inferior <%=a_rangle_5%>
<%
				}
%>
<%
				if(a_rangle_2.length() > 0){
%>
					temporal <%=a_rangle_2%>;
<%
				}
%>
			)
<%
			}
%>
			;
<%
			}
			if((a_langle_1.length() > 0) || (a_langle_2.length() > 0) || (a_langle_3.length() > 0) || (a_langle_4.length() > 0) || (a_langle_5.length() > 0)){
%>			
			OS
<%			
				if(a_langle_3.length() > 0){
%>
					<%=a_langle_3%>
<%   		
				}
%>
<%
				if((a_langle_1.length() > 0) || (a_langle_2.length() > 0) || (a_langle_4.length() > 0) || (a_langle_5.length() > 0)){
%>
				(
<%		
					if(a_langle_1.length() > 0){
%>
						superior <%=a_langle_1%>
<%   		
					}
%>
<%
					if(a_langle_4.length() > 0){
%>
						nasal <%=a_langle_4%>
<%   		
					}
%>
<%
					if(a_langle_5.length() > 0){
%>
						inferior <%=a_langle_5%>
<%   		
					}
%>
<%
					if(a_langle_2.length() > 0){
%>
						temporal <%=a_langle_2%>
<%   		
					}
%>
				)
<%			
				}
			}
%>
				.
<%
	}
%>
<%
	if(fieldList.contains("Iris")){
		String a_riris = cornea_str.get("a_riris");
		String a_liris = cornea_str.get("a_liris");	
		if((a_riris.length() > 0) || (a_liris.length() > 0)){
%>
		<span class="title1">Iris </span>
<%
			if(a_riris.length() > 0){
%>
			OD <%=a_riris%>;
<%   		
			}
%>
<%
			if(a_liris.length() > 0){
%>
			OS <%=a_liris%>.
<%   		
			}
%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Lens")){
		String a_rlens = cornea_str.get("a_rlens");
		String a_llens = cornea_str.get("a_llens");	
		if((a_rlens.length() > 0) || (a_llens.length() > 0)){
%>
		<span class="title1">Lens </span>
<%
			if(a_rlens.length() > 0){
%>
			OD <%=a_rlens%>;
<%   		
			}
%>
<%
			if(a_llens.length() > 0){
%>
			OS <%=a_llens%>.
<%   		
			}
%>
<%
		}
	}
%>
</td>
</tr>
<%}}%>
<tr><td>&nbsp;</td></tr>
<%
}
//if(fieldList.contains("Optic disc")){
if(fieldList1.contains("POSTERIOR SEGMENT")){
%>
<tr>
	<td class="title">POSTERIOR SEGMENT :</td>
</tr>
<%
	ArrayList<Map<String,String>>  optic = new ArrayList<Map<String,String>>(); 
	optic = (ArrayList<Map<String,String>>)request.getAttribute("optic");
	if(optic != null){
	for(int i=0;i < optic.size(); i ++ ){
		Map<String,String> optic_str = optic.get(i);
		String date_str = optic_str.get("date");
%>
<tr><td style="word-break:break-all"><span class="title1">(<%= date_str%>)&nbsp; &nbsp;</span>
<%
	if(fieldList.contains("Optic disc")){
		String p_rdisc = optic_str.get("p_rdisc");
		String p_ldisc = optic_str.get("p_ldisc");	
		if((p_rdisc.length() > 0) || (p_ldisc.length() > 0)){
%>
		<span class="title1">Optic disc </span>
<%
			if(p_rdisc.length() > 0){
%>
			OD <%=p_rdisc%>;
<%   		
			}
%>
<%
			if(p_ldisc.length() > 0){
%>
			OS <%=p_ldisc%>.
<%   		
			}
%>
<%
		}
	}
%>
<%
	if(fieldList.contains("C/D ratio")){
		String p_rcd = optic_str.get("p_rcd");
		String p_lcd = optic_str.get("p_lcd");	
		if((p_rcd.length() > 0) || (p_lcd.length() > 0)){
%>
		<span class="title1">C/D ratio </span>
<%
			if(p_rcd.length() > 0){
%>
			OD <%=p_rcd%>;
<%   		
			}
%>
<%
			if(p_lcd.length() > 0){
%>
			OS <%=p_lcd%>.
<%   		
			}
%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Macula")){
		String p_rmac = optic_str.get("p_rmac");
		String p_lmac = optic_str.get("p_lmac");	
		if((p_rmac.length() > 0) || (p_lmac.length() > 0)){
%>
		<span class="title1">Macula </span>
<%
			if(p_rmac.length() > 0){
%>
			OD <%=p_rmac%>;
<%   		
			}
%>
<%
			if(p_lmac.length() > 0){
%>
			OS <%=p_lmac%>.
<%   		
			}
%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Retina")){
		String p_rret = optic_str.get("p_rret");
		String p_lret = optic_str.get("p_lret");	
		if((p_rret.length() > 0) || (p_lret.length() > 0)){
%>
		<span class="title1">Retina </span>
<%
			if(p_rret.length() > 0){
%>
			OD <%=p_rret%>;
<%   		
			}
%>
<%
			if(p_lret.length() > 0){
%>
			OS <%=p_lret%>.
<%   		
			}
%>
<%
		}
	}
%>
<%
	if(fieldList.contains("Vitreous")){
		String p_rvit = optic_str.get("p_rvit");
		String p_lvit = optic_str.get("p_lvit");	
		if((p_rvit.length() > 0) || (p_lvit.length() > 0)){
%>
		<span class="title1">Vitreous </span>
<%
			if(p_rvit.length() > 0){
%>
			OD <%=p_rvit%>;
<%   		
			}
%>
<%
			if(p_lvit.length() > 0){
%>
			OS <%=p_lvit%>.
<%   		
			}
%>
<%
		}
	}
%>
</td>
</tr>

<%}}%>
<tr><td>&nbsp;</td></tr>
<%
}
%>
</table>

<%}else if(("eyeform3".equals(eyeform)) || ("eyeform3.2".equals(eyeform))){%>
<table class="display">
<%
//if(fieldList.contains("Glasses Rx")){
if(fieldList1.contains("GLASSES HISTORY")){
%>
<tr>
<td><h5>Glasses History</h5>
	<display:table name="glasses" requestURI="/eyeform/ExaminationHistory.do" class="display"  id="glassesMap" pagesize="5">
		<display:column title="Type" style="width:30px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${glassesMap.gl_type}"/>
		</display:column>
		<display:column title="OD s" style="width:30px;white-space: nowrap;">
			<c:out value="${glassesMap.gl_rs}"/>
		</display:column>
		<display:column title="OD c" style="width:30px;white-space: nowrap;">
			<c:out value="${glassesMap.gl_rc}"/>
		</display:column>
		<display:column title="OD x" style="width:30px;white-space: nowrap;">
			<c:out value="${glassesMap.gl_rx}"/>
		</display:column>
		<display:column title="OD add" style="width:30px;white-space: nowrap;">
			<c:out value="${glassesMap.gl_ra}"/>
		</display:column>
		<display:column title="OD prism" style="width:30px;white-space: nowrap;">
			<c:out value="${glassesMap.gl_rp}"/>
		</display:column>
		<display:column title="Os s" style="width:30px;white-space: nowrap;">
			<c:out value="${glassesMap.gl_ls}"/>
		</display:column>
		<display:column title="Os c" style="width:30px;white-space: nowrap;">
			<c:out value="${glassesMap.gl_lc}"/>
		</display:column>
		<display:column title="Os x" style="width:30px;white-space: nowrap;">
			<c:out value="${glassesMap.gl_lx}"/>
		</display:column>
		<display:column title="Os add" style="width:30px;white-space: nowrap;">
			<c:out value="${glassesMap.gl_la}"/>
		</display:column>
		<display:column title="Os prism" style="width:30px;white-space: nowrap;">
			<c:out value="${glassesMap.gl_lp}"/>
		</display:column>
		<display:column title="date" style="width:30px;white-space: nowrap;">
			<c:out value="${glassesMap.gl_date}"/>
		</display:column>
		<display:column title="note" style="width:30px;white-space: nowrap;">
			<c:out value="${glassesMap.gl_note}"/>
		</display:column>
	</display:table>
</td>
</tr>
<%
}
//if(fieldList.contains("Distance vision (sc)")){
if(fieldList1.contains("VISION ASSESSMENT")){
%>
<tr>
<td><h5>VISION ASSESSMENT</h5>
	<display:table name="distance_vision" requestURI="/eyeform/ExaminationHistory.do" class="display"  id="distance_visionMap" pagesize="5">
		<display:column title="Date" style="width:60px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${distance_visionMap.date}"/>
		</display:column>
	<%if(fieldList.contains("Distance vision (sc)")){%>
		<display:column title="OD dsc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_rdsc}"/>
		</display:column>
		<display:column title="OS dsc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_ldsc}"/>
		</display:column>
		<display:column title="OU dsc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_dsc}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Distance vision (cc)")){
	%>
		<display:column title="OD dcc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_rdcc}"/>
		</display:column>
		<display:column title="OS dcc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_ldcc}"/>
		</display:column>
		<display:column title="OU dcc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_dcc}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Distance vision (ph)")){
	%>
		<display:column title="OD ph" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_rph}"/>
		</display:column>
		<display:column title="OS ph" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_lph}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Intermediate vision (sc)")){
	%>
		<display:column title="OD isc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_risc}"/>
		</display:column>
		<display:column title="OS isc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_lisc}"/>
		</display:column>
		<display:column title="OU isc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_isc}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Intermediate vision (cc)")){
	%>
		<display:column title="OD icc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_ricc}"/>
		</display:column>
		<display:column title="OS icc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_licc}"/>
		</display:column>
		<display:column title="OU icc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_icc}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Near vision (sc)")){
	%>
		<display:column title="OD nsc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_rnsc}"/>
		</display:column>
		<display:column title="OS nsc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_lnsc}"/>
		</display:column>
		<display:column title="OU nsc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_nsc}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Near vision (cc)")){
	%>
		<display:column title="OD ncc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_rncc}"/>
		</display:column>
		<display:column title="OS ncc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_lncc}"/>
		</display:column>
		<display:column title="OU ncc" style="width:30px;white-space: nowrap;">
			<c:out value="${distance_visionMap.v_ncc}"/>
		</display:column>
	<%}%>
	</display:table>
</td>
</tr>
<%
}
//if(fieldList.contains("Fly test")){
if(fieldList1.contains("STEREO VISION")){
%>
<tr>
<td><h5>STEREO VISION</h5>
	<display:table name="fly_test" requestURI="/eyeform/ExaminationHistory.do" class="display"  id="flyMap" pagesize="5">
		<display:column title="Date" style="width:30px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${flyMap.date}"/>
		</display:column>
	<%
	if(fieldList.contains("Fly test")){
	%>
		<display:column title="Fly test" style="width:30px;white-space: nowrap;">
			<c:out value="${flyMap.v_fly}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Stereo-acuity")){
	%>
		<display:column title="Stereo-acuity" style="width:30px;white-space: nowrap;">
			<c:out value="${flyMap.v_stereo}"/>
		</display:column>
	<%}%>
	</display:table>
</td>
</tr>
<%
}
//if(fieldList.contains("Keratometry")){
if(fieldList1.contains("VISION MEASUREMENT")){
%>

<tr>
<td><h5>VISION MEASUREMENT</h5>
	<display:table name="keratometry" requestURI="/eyeform/ExaminationHistory.do" class="display" id="measurementMap" pagesize="5">
		<display:column title="Date" style="width:60px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${measurementMap.date}"/>
		</display:column>
	<%if(fieldList.contains("Keratometry")){%>
		<display:column title="OD k1" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rk1}"/>
		</display:column>
		<display:column title="OD k2" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rk2}"/>
		</display:column>
		<display:column title="OD kx" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rkx}"/>
		</display:column>
		<display:column title="OS k1" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_lk1}"/>
		</display:column>
		<display:column title="OS k2" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_lk2}"/>
		</display:column>
		<display:column title="OS kx" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_lkx}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Auto-refraction")){
	%>
		<display:column title="OD as" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rs}"/>
		</display:column>
		<display:column title="OD ac" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rc}"/>
		</display:column>
		<display:column title="OD ax" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rx}"/>
		</display:column>
		<display:column title="OD ar" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rar}"/>
		</display:column>
		<display:column title="OS as" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_ls}"/>
		</display:column>
		<display:column title="OS ac" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_lc}"/>
		</display:column>
		<display:column title="OS ax" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_lx}"/>
		</display:column>
		<display:column title="OS ar" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_lar}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Manifest distance")){
	%>
		<display:column title="OD ds" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rds}"/>
		</display:column>
		<display:column title="OD dc" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rdc}"/>
		</display:column>
		<display:column title="OD dx" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rdx}"/>
		</display:column>
		<display:column title="OD dv" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rdv}"/>
		</display:column>
		<display:column title="OS ds" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_lds}"/>
		</display:column>
		<display:column title="OS dc" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_ldc}"/>
		</display:column>
		<display:column title="OS dx" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_ldx}"/>
		</display:column>
		<display:column title="OS dv" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_ldv}"/>
		</display:column>
		<display:column title="OU dv" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_dv}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Manifest near")){
	%>
		<display:column title="OD ns" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rns}"/>
		</display:column>
		<display:column title="OD nc" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rnc}"/>
		</display:column>
		<display:column title="OD nx" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rnx}"/>
		</display:column>
		<display:column title="OD nv" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rnv}"/>
		</display:column>
		<display:column title="OS ns" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_lns}"/>
		</display:column>
		<display:column title="OS nc" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_lnc}"/>
		</display:column>
		<display:column title="OS nx" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_lnx}"/>
		</display:column>
		<display:column title="OS nv" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_lnv}"/>
		</display:column>
		<display:column title="OU nv" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_nv}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Cycloplegic refraction")){
	%>
		<display:column title="OD cs" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rcs}"/>
		</display:column>
		<display:column title="OD cc" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rcc}"/>
		</display:column>
		<display:column title="OD cx" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rcx}"/>
		</display:column>
		<display:column title="OD cv" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_rcv}"/>
		</display:column>
		<display:column title="OS cs" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_lcs}"/>
		</display:column>
		<display:column title="OS cc" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_lcc}"/>
		</display:column>
		<display:column title="OS cx" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_lcx}"/>
		</display:column>
		<display:column title="OS cv" style="width:30px;white-space: nowrap;">
			<c:out value="${measurementMap.v_lcv}"/>
		</display:column>
	<%}%>
	</display:table>
</td>
</tr>
<%
}
//if(fieldList.contains("NCT")){
if(fieldList1.contains("INTRAOCULAR PRESSURE")){
%>
<tr>
<td><h5>INTRAOCULAR PRESSURE</h5>
	<display:table name="nct" requestURI="/eyeform/ExaminationHistory.do" class="display" id="nctMap" pagesize="5">
		<display:column title="Date" style="width:60px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${nctMap.date}"/>
		</display:column>
	<%if(fieldList.contains("NCT")){%>	
		<display:column title="OD n" style="width:30px;white-space: nowrap;">
			<c:out value="${nctMap.iop_rn}"/>
		</display:column>
		<display:column title="OS n" style="width:30px;white-space: nowrap;">
			<c:out value="${nctMap.iop_ln}"/>
		</display:column>
		<display:column title="ntime" style="width:30px;white-space: nowrap;">
			<c:out value="${nctMap.iop_ntime}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Applanation")){
	%>
		<display:column title="OD a" style="width:30px;white-space: nowrap;">
			<c:out value="${nctMap.iop_ra}"/>
		</display:column>
		<display:column title="OS a" style="width:30px;white-space: nowrap;">
			<c:out value="${nctMap.iop_la}"/>
		</display:column>
		<display:column title="atime" style="width:30px;white-space: nowrap;">
			<c:out value="${nctMap.iop_atime}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Central corneal thickness")){
	%>
		<display:column title="OD cct" style="width:30px;white-space: nowrap;">
			<c:out value="${nctMap.cct_r}"/>
		</display:column>
		<display:column title="OS cct" style="width:30px;white-space: nowrap;">
			<c:out value="${nctMap.cct_l}"/>
		</display:column>
	<%}%>
	</display:table>
</td>
</tr>
<%
}
//if(fieldList.contains("Dominance")){
if(fieldList1.contains("REFRACTIVE")){
%>
<tr>
<td><h5>REFRACTIVE</h5>
	<display:table name="dominance" requestURI="/eyeform/ExaminationHistory.do" class="display" id="dominanceMap" pagesize="5">
		<display:column title="Date" style="width:60px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${dominanceMap.date}"/>
		</display:column>
	<%if(fieldList.contains("Dominance")){%>
		<display:column title="OD dom" style="width:30px;white-space: nowrap;">
			<c:out value="${dominanceMap.ref_rdom}"/>
		</display:column>
		<display:column title="OS dom" style="width:30px;white-space: nowrap;">
			<c:out value="${dominanceMap.ref_ldom}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Mesopic pupil size")){
	%>
		<display:column title="OD pdim" style="width:30px;white-space: nowrap;">
			<c:out value="${dominanceMap.ref_rpdim}"/>
		</display:column>
		<display:column title="OS pdim" style="width:30px;white-space: nowrap;">
			<c:out value="${dominanceMap.ref_lpdim}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Angle Kappa")){
	%>
		<display:column title="OD kappa" style="width:30px;white-space: nowrap;">
			<c:out value="${dominanceMap.ref_rkappa}"/>
		</display:column>
		<display:column title="OS kappa" style="width:30px;white-space: nowrap;">
			<c:out value="${dominanceMap.ref_lkappa}"/>
		</display:column>
	<%}%>
	</display:table>
</td>
</tr>
<%
}
//if(fieldList.contains("Colour vision")){
if(fieldList1.contains("OTHER EXAM")){
%>
<tr>
<td><h5>OTHER EXAM</h5>
	<display:table name="colour_vision" requestURI="/eyeform/ExaminationHistory.do" class="display" id="colour_visionMap" pagesize="5">
		<display:column title="Date" style="width:60px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${colour_visionMap.date}"/>
		</display:column>
	<%if(fieldList.contains("Colour vision")){%>
		<display:column title="OD colour" style="width:30px;white-space: nowrap;">
			<c:out value="${colour_visionMap.o_rcolour}"/>
		</display:column>
		<display:column title="OS colour" style="width:30px;white-space: nowrap;">
			<c:out value="${colour_visionMap.o_lcolour}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Pupil")){
	%>
		<display:column title="OD pupil" style="width:30px;white-space: nowrap;">
			<c:out value="${colour_visionMap.o_rpupil}"/>
		</display:column>
		<display:column title="OS pupil" style="width:30px;white-space: nowrap;">
			<c:out value="${colour_visionMap.o_lpupil}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Amsler grid")){
	%>
		<display:column title="OD amsler" style="width:30px;white-space: nowrap;">
			<c:out value="${colour_visionMap.o_ramsler}"/>
		</display:column>
		<display:column title="OS amsler" style="width:30px;white-space: nowrap;">
			<c:out value="${colour_visionMap.o_lamsler}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Potential acuity meter")){
	%>
		<display:column title="OD pam" style="width:30px;white-space: nowrap;">
			<c:out value="${colour_visionMap.o_rpam}"/>
		</display:column>
		<display:column title="OS pam" style="width:30px;white-space: nowrap;">
			<c:out value="${colour_visionMap.o_lpam}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Confrontation fields")){
	%>
		<display:column title="OD conf" style="width:30px;white-space: nowrap;">
			<c:out value="${colour_visionMap.o_rconf}"/>
		</display:column>
		<display:column title="OS conf" style="width:30px;white-space: nowrap;">
			<c:out value="${colour_visionMap.o_lconf}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Maddox rod")){
	%>
		<display:column title="maddox" style="width:30px;white-space: nowrap;">
			<c:out value="${colour_visionMap.o_mad}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Bagolini test")){
	%>
		<display:column title="bagolini" style="width:30px;white-space: nowrap;">
			<c:out value="${colour_visionMap.o_bag}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Worth 4 Dot (distance)")){
	%>
		<display:column title="distance" style="width:30px;white-space: nowrap;">
			<c:out value="${colour_visionMap.o_w4dd}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Worth 4 Dot (near)")){
	%>
		<display:column title="near" style="width:30px;white-space: nowrap;">
			<c:out value="${colour_visionMap.o_w4dn}"/>
		</display:column>
	<%}%>
	</display:table>
</td>
</tr>
<%
}
//if(fieldList.contains("DUCTION/DIPLOPIA TESTING")){
if(fieldList1.contains("DUCTION/DIPLOPIA TESTING")){
%>
<tr>
<td><h5>DUCTION/DIPLOPIA TESTING</h5>
	<display:table name="ductlion" requestURI="/eyeform/ExaminationHistory.do" class="display" id="ductlionMap" pagesize="5">
		<display:column title="Date" style="width:60px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${ductlionMap.date}"/>
		</display:column>
		<display:column title="OD" headerClass="centered">
			<table style="border:0px;font-size: 10pt">
			<tr>
			<td class="inner"><c:out value="${ductlionMap.duc_rur}"/></td>
			<td class="inner"><c:out value="${ductlionMap.duc_rul}"/></td>
			</tr>
			<tr>
			<td class="inner"><c:out value="${ductlionMap.duc_rr}"/></td>
			<td class="inner"><c:out value="${ductlionMap.duc_rl}"/></td>
			</tr>
			<tr>
			<td class="inner"><c:out value="${ductlionMap.duc_rdr}"/></td>
			<td class="inner"><c:out value="${ductlionMap.duc_rdl}"/></td>
			</tr>
			</table>
		</display:column>
		<display:column title="OS" headerClass="centered">
			<table style="border:0px;font-size: 10pt">
			<tr>
			<td class="inner"><c:out value="${ductlionMap.duc_lur}"/></td>
			<td class="inner"><c:out value="${ductlionMap.duc_lul}"/></td>
			</tr>
			<tr>
			<td class="inner"><c:out value="${ductlionMap.duc_lr}"/></td>
			<td class="inner"><c:out value="${ductlionMap.duc_ll}"/></td>
			</tr>
			<tr>
			<td class="inner"><c:out value="${ductlionMap.duc_ldr}"/></td>
			<td class="inner"><c:out value="${ductlionMap.duc_ldl}"/></td>
			</tr>
			</table>
		</display:column>
		<display:column title="OU" headerClass="centered">
			<table style="border:0px;font-size: 10pt">
			<tr>
			<td class="inner"><c:out value="${ductlionMap.dip_ur}"/></td>
			<td class="inner"><c:out value="${ductlionMap.dip_u}"/></td>
			</tr>
			<tr>
			<td class="inner"><c:out value="${ductlionMap.dip_r}"/></td>
			<td class="inner"><c:out value="${ductlionMap.dip_p}"/></td>
			</tr>
			<tr>
			<td class="inner"><c:out value="${ductlionMap.dip_dr}"/></td>
			<td class="inner"><c:out value="${ductlionMap.dip_d}"/></td>
			</tr>
			</table>
		</display:column>
	</display:table>
</td>
</tr>
<%
}
//if(fieldList.contains("Primary gaze")){
if(fieldList1.contains("DEVIATION MEASUREMENT")){
%>
<tr>
<td><h5>DEVIATION MEASUREMENT</h5>
	<display:table name="primary" requestURI="/eyeform/ExaminationHistory.do" class="display" id="primaryMap" pagesize="5">
		<display:column title="Date" style="width:60px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${primaryMap.date}"/>
		</display:column>
	<%
	if(fieldList.contains("Primary gaze") || fieldList.contains("Up gaze") || fieldList.contains("Down gaze") || fieldList.contains("Right gaze") || fieldList.contains("Left gaze") || fieldList.contains("Right head tilt") || fieldList.contains("Left head tilt")){
	%>
		<display:column style="width:60px;">
			<table style="border:0px;font-size: 10pt">
			<tr>
			<td class="inner"></td>
			<td class="inner"><c:out value="${primaryMap.dev_u}"/></td>
			<td class="inner"></td>
			</tr>
			<tr>
			<td class="inner"><c:out value="${primaryMap.dev_r}"/></td>
			<td class="inner"><c:out value="${primaryMap.dev_p}"/></td>
			<td class="inner"><c:out value="${primaryMap.dev_l}"/></td>
			</tr>
			<tr>
			<td class="inner"><c:out value="${primaryMap.dev_rt}"/></td>
			<td class="inner"><c:out value="${primaryMap.dev_d}"/></td>
			<td class="inner"><c:out value="${primaryMap.dev_lt}"/></td>
			</tr>
			</table>
		</display:column>
	<%
	}
	if(fieldList.contains("Near")){
	%>
		<display:column title="Near" style="width:30px;white-space: nowrap;">
			<c:out value="${primaryMap.dev_near}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Near with +3D add")){
	%>
		<display:column title="Near with +3D add" style="width:30px;white-space: nowrap;">
			<c:out value="${primaryMap.dev_plus3}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Far distance")){
	%>
		<display:column title="Far distance" style="width:30px;white-space: nowrap;">
			<c:out value="${primaryMap.dev_far}"/>
		</display:column>
	<%}%>
	</display:table>
</td>
</tr>
<%
}
//if(fieldList.contains("Retropulsion")){
if(fieldList1.contains("EXTERNAL/ORBIT")){
%>
<tr>
<td><h5>EXTERNAL/ORBIT</h5>
	<display:table name="retropulsion" requestURI="/eyeform/ExaminationHistory.do" class="display" id="retropulsionMap" pagesize="5">
		<display:column title="Date" style="width:60px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${retropulsionMap.date}"/>
		</display:column>
	<%if(fieldList.contains("Face")){%>
		<display:column title="OD face" style="width:30px;white-space: nowrap;">
			<c:out value="${retropulsionMap.ext_rface}"/>
		</display:column>
		<display:column title="OS face" style="width:30px;white-space: nowrap;">
			<c:out value="${retropulsionMap.ext_lface}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Retropulsion")){
	%>
		<display:column title="OD retro" style="width:30px;white-space: nowrap;">
			<c:out value="${retropulsionMap.ext_rretro}"/>
		</display:column>
		<display:column title="OS retro" style="width:30px;white-space: nowrap;">
			<c:out value="${retropulsionMap.ext_lretro}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Hertel")){
	%>
		<display:column title="OD hertel" style="width:30px;white-space: nowrap;">
			<c:out value="${retropulsionMap.ext_rhertel}"/>
		</display:column>
		<display:column title="OS hertel" style="width:30px;white-space: nowrap;">
			<c:out value="${retropulsionMap.ext_lhertel}"/>
		</display:column>
	<%}%>
	</display:table>
</td>
</tr>
<%
}
//if(fieldList.contains("Upper lid")){
if(fieldList1.contains("EYELID/NASOLACRIMAL DUCT")){
%>
<tr>
<td><h5>EYELID/NASOLACRIMAL DUCT</h5>
	<display:table name="upper" requestURI="/eyeform/ExaminationHistory.do" class="display" id="upperMap" pagesize="5">
		<display:column title="Date" style="width:60px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${upperMap.date}"/>
		</display:column>
	<%if(fieldList.contains("Upper lid")){%>
		<display:column title="OD ul" style="width:30px;white-space: nowrap;">
			<c:out value="${upperMap.ext_rul}"/>
		</display:column>
		<display:column title="OS ul" style="width:30px;white-space: nowrap;">
			<c:out value="${upperMap.ext_lul}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Lower lid")){
	%>
		<display:column title="OD ll" style="width:30px;white-space: nowrap;">
			<c:out value="${upperMap.ext_rll}"/>
		</display:column>
		<display:column title="OS ll" style="width:30px;white-space: nowrap;">
			<c:out value="${upperMap.ext_lll}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Lacrimal lake")){
	%>
		<display:column title="OD lake" style="width:30px;white-space: nowrap;">
			<c:out value="${upperMap.ext_rlake}"/>
		</display:column>
		<display:column title="OS lake" style="width:30px;white-space: nowrap;">
			<c:out value="${upperMap.ext_llake}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Lacrimal irrigation")){
	%>
		<display:column title="OD irrig" style="width:30px;white-space: nowrap;">
			<c:out value="${upperMap.ext_rirrig}"/>
		</display:column>
		<display:column title="OS irrig" style="width:30px;white-space: nowrap;">
			<c:out value="${upperMap.ext_lirrig}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Punctum")){
	%>
		<display:column title="OD punc" style="width:30px;white-space: nowrap;">
			<c:out value="${upperMap.ext_rpunc}"/>
		</display:column>
		<display:column title="OS punc" style="width:30px;white-space: nowrap;">
			<c:out value="${upperMap.ext_lpunc}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Nasolacrimal duct")){
	%>
		<display:column title="OD nld" style="width:30px;white-space: nowrap;">
			<c:out value="${upperMap.ext_rnld}"/>
		</display:column>
		<display:column title="OS nld" style="width:30px;white-space: nowrap;">
			<c:out value="${upperMap.ext_lnld}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Dye disappearance")){
	%>
		<display:column title="OD dye" style="width:30px;white-space: nowrap;">
			<c:out value="${upperMap.ext_rdye}"/>
		</display:column>
		<display:column title="OS dye" style="width:30px;white-space: nowrap;">
			<c:out value="${upperMap.ext_ldye}"/>
		</display:column>
	<%}%>
	</display:table>
</td>
</tr>
<%
}
//if(fieldList.contains("Margin reflex distance")){
if(fieldList1.contains("EYELID MEASUREMENT")){
%>
<tr>
<td><h5>EYELID MEASUREMENT</h5>
	<display:table name="margin" requestURI="/eyeform/ExaminationHistory.do" class="display" id="marginMap" pagesize="5">
		<display:column title="Date" style="width:60px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${marginMap.date}"/>
		</display:column>
	<%if(fieldList.contains("Margin reflex distance")){%>
		<display:column title="OD mrd" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_rmrd}"/>
		</display:column>
		<display:column title="OS mrd" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_lmrd}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Inferior scleral show")){
	%>
		<display:column title="OD iss" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_riss}"/>
		</display:column>
		<display:column title="OS iss" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_liss}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Levator function")){
	%>
		<display:column title="OD lev" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_rlev}"/>
		</display:column>
		<display:column title="OS lev" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_llev}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Lagophthalmos")){
	%>
		<display:column title="OD lag" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_rlag}"/>
		</display:column>
		<display:column title="OS lag" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_llag}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Blink reflex")){
	%>
		<display:column title="OD blink" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_rblink}"/>
		</display:column>
		<display:column title="OS blink" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_lblink}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Cranial Nerve VII function")){
	%>
		<display:column title="OD cn7" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_rcn7}"/>
		</display:column>
		<display:column title="OS cn7" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_lcn7}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Bells phenomenon")){
	%>
		<display:column title="OD bell" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_rbell}"/>
		</display:column>
		<display:column title="OS bell" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_lbell}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Schirmer test")){
	%>
		<display:column title="OD schirm" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_rschirm}"/>
		</display:column>
		<display:column title="OS schirm" style="width:30px;white-space: nowrap;">
			<c:out value="${marginMap.lid_lschirm}"/>
		</display:column>
	<%}%>
	</display:table>
</td>
</tr>
<%
}
//if(fieldList.contains("Cornea")){
if(fieldList1.contains("ANTERIOR SEGMENT")){
%>
<tr>
<td><h5>ANTERIOR SEGMENT</h5>
	<display:table name="cornea" requestURI="/eyeform/ExaminationHistory.do" class="display" id="corneaMap" pagesize="5">
		<display:column title="Date" style="width:60px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${corneaMap.date}"/>
		</display:column>
	<%if(fieldList.contains("Cornea")){%>
		<display:column title="OD cornea" style="width:30px;white-space: nowrap;">
			<c:out value="${corneaMap.a_rk}"/>
		</display:column>
		<display:column title="OS cornea" style="width:30px;white-space: nowrap;">
			<c:out value="${corneaMap.a_lk}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Conjunctiva/Sclera")){
	%>
		<display:column title="OD conj" style="width:30px;white-space: nowrap;">
			<c:out value="${corneaMap.a_rconj}"/>
		</display:column>
		<display:column title="OS conj" style="width:30px;white-space: nowrap;">
			<c:out value="${corneaMap.a_lconj}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Anterior chamber")){
	%>
		<display:column title="OD ac" style="width:30px;white-space: nowrap;">
			<c:out value="${corneaMap.a_rac}"/>
		</display:column>
		<display:column title="OS ac" style="width:30px;white-space: nowrap;">
			<c:out value="${corneaMap.a_rac}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Angle")){
	%>
		<display:column title="OD" >
			<table border="1" style="font-size: 10pt">
			<tr>
			<td width="33%"></td>
			<td class="inner" width="34%"><c:out value="${corneaMap.a_rangle_1}"/></td>
			<td width="33%"></td>
			</tr>
			<tr>
			<td class="inner"><c:out value="${corneaMap.a_rangle_2}"/></td>
			<td class="inner"><c:out value="${corneaMap.a_rangle_3}"/></td>
			<td class="inner"><c:out value="${corneaMap.a_rangle_4}"/></td>
			</tr>
			<tr>
			<td></td>
			<td class="inner"><c:out value="${corneaMap.a_rangle_5}"/></td>
			<td></td>
			</tr>
			</table>
		</display:column>
		<display:column title="OS" >
			<table border="1" style="font-size: 10pt">
			<tr>
			<td width="33%"></td>
			<td class="inner" width="34%"><c:out value="${corneaMap.a_langle_1}"/></td>
			<td width="33%"></td>
			</tr>
			<tr>
			<td class="inner"><c:out value="${corneaMap.a_langle_2}"/></td>
			<td class="inner"><c:out value="${corneaMap.a_langle_3}"/></td>
			<td class="inner"><c:out value="${corneaMap.a_langle_4}"/></td>
			</tr>
			<tr>
			<td width="33%"></td>
			<td class="inner" width="34%"><c:out value="${corneaMap.a_langle_5}"/></td>
			<td width="33%"></td>
			</tr>
			</table>
		</display:column>
	<%
	}
	if(fieldList.contains("Iris")){
	%>
		<display:column title="OD iris" style="width:30px;white-space: nowrap;">
			<c:out value="${corneaMap.a_riris}"/>
		</display:column>
		<display:column title="OS iris" style="width:30px;white-space: nowrap;">
			<c:out value="${corneaMap.a_liris}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Lens")){
	%>
		<display:column title="OD lens" style="width:30px;white-space: nowrap;">
			<c:out value="${corneaMap.a_rlens}"/>
		</display:column>
		<display:column title="OS lens" style="width:30px;white-space: nowrap;">
			<c:out value="${corneaMap.a_llens}"/>
		</display:column>
	<%}%>
	</display:table>
</td>
</tr>
<%
}
//if(fieldList.contains("Optic disc")){
if(fieldList1.contains("POSTERIOR SEGMENT")){
%>
<tr>
<td><h5>POSTERIOR SEGMENT</h5>
	<display:table name="optic" requestURI="/eyeform/ExaminationHistory.do" class="display" id="opticMap" pagesize="5">
		<display:column title="Date" style="width:60px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${opticMap.date}"/>
		</display:column>
	<%if(fieldList.contains("Optic disc")){%>
		<display:column title="OD disc" style="width:30px;white-space: nowrap;">
			<c:out value="${opticMap.p_rdisc}"/>
		</display:column>
		<display:column title="OS disc" style="width:30px;white-space: nowrap;">
			<c:out value="${opticMap.p_ldisc}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("C/D ratio")){
	%>
		<display:column title="OD cd" style="width:30px;white-space: nowrap;">
			<c:out value="${opticMap.p_rcd}"/>
		</display:column>
		<display:column title="OS cd" style="width:30px;white-space: nowrap;">
			<c:out value="${opticMap.p_lcd}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Macula")){
	%>
		<display:column title="OD mac" style="width:30px;white-space: nowrap;">
			<c:out value="${opticMap.p_rmac}"/>
		</display:column>
		<display:column title="OS mac" style="width:30px;white-space: nowrap;">
			<c:out value="${opticMap.p_lmac}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Retina")){
	%>
		<display:column title="OD ret" style="width:30px;white-space: nowrap;">
			<c:out value="${opticMap.p_rret}"/>
		</display:column>
		<display:column title="OS ret" style="width:30px;white-space: nowrap;">
			<c:out value="${opticMap.p_lret}"/>
		</display:column>
	<%
	}
	if(fieldList.contains("Vitreous")){
	%>
		<display:column title="OD vit" style="width:30px;white-space: nowrap;">
			<c:out value="${opticMap.p_rvit}"/>
		</display:column>
		<display:column title="OS vit" style="width:30px;white-space: nowrap;">
			<c:out value="${opticMap.p_lvit}"/>
		</display:column>
	<%}%>
	</display:table>
</td>
</tr>
<%}%>
</table>
<%}else{%>
<table class="display">
  <tr>
  <td><h5>AR history</h5>
  <display:table name="ars" requestURI="/eyeform/ExaminationHistory.do" class="display" style="width:100%" id="map" pagesize="5">

			<display:column title="OD Sph" style="width:30px;white-space: nowrap;">
			<c:out value="${map.od_ar_sph}"/>
			</display:column>

			<display:column title="OD Cyl" style="width:30px;white-space: nowrap;">
			<c:out value="${map.od_ar_cyl}"/>

			</display:column>
			 <display:column title="OD Axis" style="width:30px;white-space: nowrap;">
		    <c:out value="${map.od_ar_axis}"/>
			</display:column>

			<display:column title="Date" style="width:60px;white-space: nowrap;text-align:center" headerClass="centered">
			<c:out value="${map.date}"/>
			</display:column>

			<display:column title="OS Sph" style="width:30px;white-space: nowrap;">
			<c:out value="${map.os_ar_sph}"/>
			</display:column>

			<display:column title="OS Cyl" style="width:30px;white-space: nowrap;">
			<c:out value="${map.os_ar_cyl}"/>
			</display:column>

			 <display:column title="OS Axis" style="width:30px;">
		    <c:out value="${map.os_ar_axis}"/>
			</display:column>

	</display:table>
   </td>
   </tr>

  <tr>
  <td><h5>K history</h5>
  <display:table name="ks" requestURI="/ExaminationHistory.do" class="display" style="width:100%" id="map" pagesize="5">

			<display:column title="OD K1" style="width:30px;">
			<c:out value="${map.od_k1}"/>
			</display:column>

			<display:column title="OD K2" style="width:30px;">
			<c:out value="${map.od_k2}"/>

			</display:column>
			 <display:column title="OD K2-Axis" style="width:30px;">
		    <c:out value="${map.od_k2_axis}"/>
			</display:column>

			<display:column title="Date" style="text-align:center" headerClass="centered">
			<c:out value="${map.date}"/>
			</display:column>

			<display:column title="OS K1" style="width:30px;">
			<c:out value="${map.os_k1}"/>
			</display:column>

			<display:column title="OS K2" style="width:30px;">
			<c:out value="${map.os_k2}"/>
			</display:column>

			 <display:column title="OS K2-Axis" style="width:30px;">
		    <c:out value="${map.os_k2_axis}"/>
			</display:column>

	</display:table>
   </td>
   </tr>


  <tr>
  <td><h5>Manifest Refraction history</h5>
  <display:table name="manifestRefraction" requestURI="/eyeform/ExaminationHistory.do" class="display" style="width:100%" id="map" pagesize="5">

			<display:column title="OD Sph" style="width:30px;">
			<c:out value="${map.od_manifest_refraction_sph}"/>
			</display:column>

			<display:column title="OD Cyl" style="width:30px;">
			<c:out value="${map.od_manifest_refraction_cyl}"/>

			</display:column>

			 <display:column title="OD Axis" style="width:30px;">
		    <c:out value="${map.od_manifest_refraction_axis}"/>
			</display:column>


			 <display:column title="OD Add" style="width:30px;">
		    <c:out value="${map.od_manifest_refraction_add}"/>
			</display:column>

		   <display:column title="Date" style="text-align:center" headerClass="centered">
			<c:out value="${map.date}"/>
			</display:column>

			<display:column title="OS Sph" style="width:30px;">
			<c:out value="${map.os_manifest_refraction_sph}"/>
			</display:column>

			<display:column title="OS Cyl" style="width:30px;">
			<c:out value="${map.os_manifest_refraction_cyl}"/>
			</display:column>

			 <display:column title="OS Axis" style="width:30px;">
		    <c:out value="${map.os_manifest_refraction_axis}"/>
			</display:column>

		 <display:column title="OS Add" style="width:30px;">
		    <c:out value="${map.os_manifest_refraction_add}"/>
			</display:column>

  </display:table>
   </td>
   </tr>


  <tr>
  <td><h5>Cycloplegic refraction history</h5>
  <display:table name="cycloplegicRefraction" requestURI="/eyeform/ExaminationHistory.do" class="display" style="width:100%" id="map" pagesize="5">

			<display:column title="OD Sph" style="width:30px;">
			<c:out value="${map.od_cycloplegic_refraction_sph}"/>
			</display:column>

			<display:column title="OD Cyl" style="width:30px;">
			<c:out value="${map.od_cycloplegic_refraction_cyl}"/>

			</display:column>

			 <display:column title="OD Axis" style="width:30px;">
		    <c:out value="${map.od_cycloplegic_refraction_axis}"/>
			</display:column>


			 <display:column title="OD Add" style="width:30px;">
		    <c:out value="${map.od_cycloplegic_refraction_add}"/>
			</display:column>

		   <display:column title="Date" style="text-align:center" headerClass="centered">
			<c:out value="${map.date}"/>
			</display:column>

			<display:column title="OS Sph" style="width:30px;">
			<c:out value="${map.os_cycloplegic_refraction_sph}"/>
			</display:column>

			<display:column title="OS Cyl" style="width:30px;">
			<c:out value="${map.os_cycloplegic_refraction_cyl}"/>
			</display:column>

			 <display:column title="OS Axis" style="width:30px;">
		    <c:out value="${map.os_cycloplegic_refraction_axis}"/>
			</display:column>

		 <display:column title="OS Add" style="width:30px;">
		    <c:out value="${map.os_cycloplegic_refraction_add}"/>
			</display:column>

  </display:table>
   </td>
   </tr>

  <tr>
  <td><h5>Angle history</h5>
  <display:table name="angle" requestURI="/eyeform/ExaminationHistory.do" class="display" style="width:100%" id="map" pagesize="5">

			<display:column title="OD" >
			<table style="border:0px">
			<tr>
			<td width="33%"></td>
			<td class="inner" width="34%"><c:out value="${map.od_angle_up}"/></td>
			<td width="33%"></td>
			</tr>
			<tr>
			<td class="inner"><c:out value="${map.od_angle_middle0}"/></td>
			<td class="inner"><c:out value="${map.od_angle_middle1}"/></td>
			<td class="inner"><c:out value="${map.od_angle_middle2}"/></td>
			</tr>
			<tr>
			<td></td>
			<td class="inner"><c:out value="${map.od_angle_down}"/></td>
			<td></td>
			</tr>
			</table>
			</display:column>
			<display:column title="Date" style="text-align:center" headerClass="centered">
			<c:out value="${map.date}"/>
			</display:column>

			<display:column title="OS" >
			<table border="1">
			<tr>
			<td width="33%"></td>
			<td class="inner" width="34%"><c:out value="${map.os_angle_up}"/></td>
			<td width="33%"></td>
			</tr>
			<tr>
			<td class="inner"><c:out value="${map.os_angle_middle0}"/></td>
			<td class="inner"><c:out value="${map.os_angle_middle1}"/></td>
			<td class="inner"><c:out value="${map.os_angle_middle2}"/></td>
			</tr>
			<tr>
			<td width="33%"></td>
			<td class="inner" width="34%"><c:out value="${map.os_angle_down}"/></td>
			<td width="33%"></td>
			</tr>
			</table>
			</display:column>

  </display:table>
   </td>
   </tr>

		</table>
<%}%>
	</body>
</html>
