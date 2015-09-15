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

<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%
    String formClass = "HomeFalls";
    String formLink = "formhomefalls.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);

    //FrmData fd = new FrmData();    String resource = fd.getResource(); resource = resource + "ob/riskinfo/";

    //get project_home
    String project_home = request.getContextPath().substring(1);	
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>The Home Falls and Accidents Screening Tool (HOME FAST)</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">
    var choiceFormat  = new Array(6,7,8,9,10,11,12,14,15,17,18,20,21,22,23,24,25,27,28,30,31,33,34,36,37,38,39,40,41,42,43,44,45,46,47,49,50,52,53,55,56,58,59,60,61,63,64,65,66,68);
    var allNumericField = null;     
    var allMatch = null;
    var action = "/<%=project_home%>/form/formname.do";
    
    function backToPage1(){             
            document.getElementById('page1').style.display = 'block';
            document.getElementById('page2').style.display = 'none';  
            document.getElementById('page3').style.display = 'none';
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';        
    }
    
    function goToPage2(){      
        var checkboxes = new Array(6,7,8,9,10,11,12,14);
        if (is1CheckboxChecked(0, checkboxes)==true && isFormCompleted(6,14,4,0)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'block'; 
            document.getElementById('page3').style.display = 'none'; 
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
        }
    }

    function bTackoPage2(){ 
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'block'; 
        document.getElementById('page3').style.display = 'none'; 
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none';
    }
    
    function goToPage3(){      
        var checkboxes = new Array(15,17,18,20,21,22,23,24,25,27);
        var numericFields = new Array(57,58,59,60);
        if (is1CheckboxChecked(0, checkboxes)==true && isFormCompleted(15,27,5,0)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'block';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
        }
    }
    
    function backToPage3(){ 
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none'; 
        document.getElementById('page3').style.display = 'block';  
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none';
    }

    function goToPage4(){    
        var checkboxes = new Array(28,30,31,33,34,36,37,38,39,40,41,42);
        if (is1CheckboxChecked(0, checkboxes)==true && isFormCompleted(28,42,6,0)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'block';
            document.getElementById('page5').style.display = 'none';
        }
    }
    
    function backToPage4(){
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none'; 
        document.getElementById('page3').style.display = 'none';  
        document.getElementById('page4').style.display = 'block';
        document.getElementById('page5').style.display = 'none';
    }

    function goToPage5(){      
        var checkboxes = new Array(43,44,45,46,47,49,50,52,53,55);
        if (is1CheckboxChecked(0, checkboxes)==true && isFormCompleted(43,55,5,0)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'block';
        }
    }
     
    function checkBeforeSave(){                
        if(document.getElementById('page5').style.display=='block'){
            if(isFormCompleted(56,68,5,0)==true)
                return true;
        }    
        else{
            if(isFormCompleted(6,14,4,0)==true && isFormCompleted(15,27,5,0)==true && isFormCompleted(28,42,6,0)==true && isFormCompleted(43,55,5,0)==true && isFormCompleted(56,68,5,0)==true)
                return true;
        }            
        
        return false;
    }
</script>
<script type="text/javascript" src="formScripts.js">          
</script>


<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0"
	onload="window.resizeTo(768,768)">
<!--
@oscar.formDB Table="formAdf" 
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'" 
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL" 
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL" 
@oscar.formDB Field="formEdited" Type="timestamp"  
-->
<html:form action="/form/formname">
	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<input type="hidden" name="submit" value="exit" />

	<table border="0" cellspacing="0" cellpadding="0" width="740px"
		height="95%">
		<tr>
			<td>
			<table border="0" cellspacing="0" cellpadding="0" width="740px"
				height="10%">
				<tr>
					<th class="subject">The Home Falls and Accidents Screening
					Tool (HOME FAST)</th>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td valign="top">
			<table border="0" cellspacing="0" cellpadding="0" height="85%"
				width="740px" id="page1">
				<tr>
					<td colspan="2">
					<table width="740px" height="620px" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td colspan="4">
							<table width="100%">
								<tr>
									<td valign="top" width="15%"><font
										style="font-weight: bold">Definition: </font></td>
									<td valign="top" width="85%">Home refers to both the
									inside and outside of a person's residential property. As the
									checklist will be used for visits during the day, answers need
									to consider the same home environment at night.</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr class="title">
							<th colspan="4">Floors</th>
						</tr>
						<tr>
							<th class="question" width="5%">1.</th>
							<th class="question" colspan="3">Are the walkways free of
							cords and other clutter?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							No cords of clutter (e.g. boxes, newspapers, objects) across or
							encroaching on walkways/doorways. Includes furniture and other
							items which obstruct doorways, or hallways, items behind doors
							preventing doors opening fully, raised thresholds in doorways.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="floor1Y" <%= props.getProperty("floor1Y", "") %> /> Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="floor1N" <%= props.getProperty("floor1N", "") %> /> No</td>
							<td width="75%"></td>
						</tr>
						<tr>
							<th class="question" width="5%">2.</th>
							<th class="question" colspan="3">Are the floor coverings in
							good condition?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Carpets/mats lie flat/no tears/not threadbare/no cracked or
							missing tiles - including coverings on stairs.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="floor2Y" <%= props.getProperty("floor2Y", "") %> /> Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="floor2N" <%= props.getProperty("floor2N", "") %> /> No</td>
							<td width="75%"></td>
						</tr>
						<tr>
							<th class="question" width="5%">3.</th>
							<th class="question" colspan="3">Are the floor surfaces
							non-slip?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Score 'no' if lino or tiles are in the kitchen, bathroom or
							laundry, in addition to other rooms, the kitchen, bathroom and
							laundry have non-slip or slip resistant floor surfaces.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="floor3Y" <%= props.getProperty("floor3Y", "") %> /> Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="floor3N" <%= props.getProperty("floor3N", "") %> /> No</td>
							<td width="75%"></td>
						</tr>
						<tr>
							<th class="question" width="5%">4.</th>
							<th class="question" colspan="3">Are loose mats securely
							fixed to the floor?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Mats have effective slip resistant backing/are taped or nailed to
							the floor.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="floor4Y" <%= props.getProperty("floor4Y", "") %> /> Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="floor4N" <%= props.getProperty("floor4N", "") %> /> No</td>
							<td width="75%"><input type="checkbox" class="checkbox"
								name="floor4NA" <%= props.getProperty("floor4NA", "") %> />N/A
							(there are no mats in the house)</td>
						</tr>
						<tr>
							<td colspan="7">
							<table height="30">
								<tr>
									<td>&nbsp;</td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr class="subject">
					<td></td>
					<td align="right"><a href="javascript: goToPage2();">Next
					Page >></a></td>
				</tr>
			</table>

			<table border="0" cellspacing="0" cellpadding="0"
				style="display: none" width="740px" height="85%" id="page2">
				<tr>
					<td colspan="2">
					<table width="740px" height="620px" border="0" cellspacing="0"
						cellpadding="0">
						<tr class="title">
							<th colspan="4">Furniture</th>
						</tr>
						<tr>
							<th class="question" width="5%">5.</th>
							<th class="question" colspan="3">Can the person get in and
							out of bed easily and safely?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Bed is of adequate height and firmness. Person does not need to
							pull self up on bedside furniture</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="furniture5Y" <%= props.getProperty("furniture5Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="furniture5N" <%= props.getProperty("furniture5N", "") %> />
							No</td>
							<td width="75%"><input type="checkbox" class="checkbox"
								name="furniture5NA" <%= props.getProperty("furniture5NA", "") %> />N/A</td>
						</tr>
						<tr>
							<th class="question" width="5%">6.</th>
							<th class="question" colspan="3">Can the person get up from
							the lounge chair easily and safely?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Chair is of adequate height, chair arms are accessible to push up
							from, seat cushion is not too soft or deep.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="furniture6Y" <%= props.getProperty("furniture6Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="furniture6N" <%= props.getProperty("furniture6N", "") %> />
							No</td>
							<td width="75%"><input type="checkbox" class="checkbox"
								name="furniture6NA" <%= props.getProperty("furniture6NA", "") %> />N/A
							(person uses wheelchair constantly)</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr class="title">
							<th colspan="4">Lighting</th>
						</tr>
						<tr>
							<th class="question" width="5%">7.</th>
							<th class="question" colspan="3">Are all the lights bright
							enough for the person to see clearly?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							No globes to be less than 75w, no shadows thrown across rooms, no
							excess glare.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="lighting7Y" <%= props.getProperty("lighting7Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="lighting7N" <%= props.getProperty("lighting7N", "") %> />
							No</td>
							<td width="75%"></td>
						</tr>
						<tr>
							<th class="question" width="5%">8.</th>
							<th class="question" colspan="3">Can the person switch a
							light on easily from his or her bed?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Person does not have to get out of bed to switch a light on at
							night - has a flashlight or bedside lamp.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="lighting8Y" <%= props.getProperty("lighting8Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="lighting8N" <%= props.getProperty("lighting8N", "") %> />
							No</td>
							<td width="75%"></td>
						</tr>
						<tr>
							<th class="question" width="5%">9.</th>
							<th class="question" colspan="3">Are the outside paths,
							steps and entrances well lit at night?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Lights exist over back and front doors, globes at least 75w,
							walkways, used exposed to light - including communal lobbies.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="lighting9Y" <%= props.getProperty("lighting9Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="lighting9N" <%= props.getProperty("lighting9N", "") %> />
							No</td>
							<td width="75%">
							<table width="100%">
								<tr>
									<td width="3%"><input type="checkbox" class="checkbox"
										name="lighting9NA" <%= props.getProperty("lighting9NA", "") %> />
									</td>
									<td width="97%">N/A (no outside path, step or entrance =
									access door opens straight onto public footpath)</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td colspan="7">
							<table height="30">
								<tr>
									<td>&nbsp;</td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr class="subject">
					<td align="left"><a href="javascript: backToPage1();"><<
					Previous Page</a></td>
					<td align="right"><a href="javascript: goToPage3();">Next
					Page >></a></td>
				</tr>
			</table>

			<table border="0" cellspacing="0" cellpadding="0"
				style="display: none" width="740px" height="85%" id="page3">
				<tr>
					<td colspan="2">
					<table width="740px" height="620px" border="0" cellspacing="0"
						cellpadding="0">
						<tr class="title">
							<th colspan="4">Bathroom</th>
						</tr>
						<tr>
							<th class="question" width="5%">10.</th>
							<th class="question" colspan="3">Is the person able to get
							on and off the toilet easily and safely?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Toilet is of adequate height, person does not need to hold on to
							sink/toilet roll holder to get up, rail exists beside toilet, if
							needed.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="bathroom10Y" <%= props.getProperty("bathroom10Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="bathroom10N" <%= props.getProperty("bathroom10N", "") %> />
							No</td>
							<td width="75%"><input type="checkbox" class="checkbox"
								name="bathroom10NA" <%= props.getProperty("bathroom10NA", "") %> />N/A
							(person uses commode constantly)</td>
						</tr>
						<tr>
							<th class="question" width="5%">11.</th>
							<th class="question" colspan="3">Is the person able to get
							in and out of the bath easily and safely?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Person is able to step over the edge of the bath without risk,
							and can lower himself or herself into the bath and get up again
							without needing to grab onto furniture (or uses bathboard, or
							stands to use shower over bath without risk).</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="bathroom11Y" <%= props.getProperty("bathroom11Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="bathroom11N" <%= props.getProperty("bathroom11N", "") %> />
							No</td>
							<td width="75%"><input type="checkbox" class="checkbox"
								name="bathroom11NA" <%= props.getProperty("bathroom11NA", "") %> />N/A
							(no bath in the home, or bath never used)</td>
						</tr>
						<tr>
							<th class="question" width="5%">12.</th>
							<th class="question" colspan="3">Is the person able to walk
							in and out of the shower recess easily and safely?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Person can step over shower hob, or screen tracks without risk
							and without having to hold onto anything for support.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="bathroom12Y" <%= props.getProperty("bathroom12Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="bathroom12N" <%= props.getProperty("bathroom12N", "") %> />
							No</td>
							<td width="75%"><input type="checkbox" class="checkbox"
								name="bathroom12NA" <%= props.getProperty("bathroom12NA", "") %> />N/A
							(no shower recess in the home)</td>
						</tr>
						<tr>
							<th class="question" width="5%">13.</th>
							<th class="question" colspan="3">Is there an
							accessible/sturdy grab rail(s) in the shower or beside the bath?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Rails which are fixed securely to the wall, which are not towel
							rails, which can be reached without leaning enough to lose
							balance.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="bathroom13Y" <%= props.getProperty("bathroom13Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="bathroom13N" <%= props.getProperty("bathroom13N", "") %> />
							No</td>
							<td width="75%"></td>
						</tr>
						<tr>
							<th class="question" width="5%">14.</th>
							<th class="question" colspan="3">Are slip resistant mats
							used in the bath/bathroom/shower recess?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Well-maintained slip resistant rubber mats, or non-slip strips in
							the base of the bath or shower recess.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="bathroom14Y" <%= props.getProperty("bathroom14Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="bathroom14N" <%= props.getProperty("bathroom14N", "") %> />
							No</td>
							<td width="75%"></td>
						</tr>
						<tr>
							<th class="question" width="5%">15.</th>
							<th class="question" colspan="3">Is the toilet in close
							proximity to the bedroom?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							No more than two doorways away (including the bedroom door) -
							does not involve going outside or unlocking doors to reach it.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="bathroom15Y" <%= props.getProperty("bathroom15Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="bathroom15N" <%= props.getProperty("bathroom15N", "") %> />
							No</td>
							<td width="75%"></td>
						</tr>
						<tr>
							<td colspan="7">
							<table height="30">
								<tr>
									<td>&nbsp;</td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr class="subject">
					<td align="left"><a href="javascript: backToPage2();"><<
					Previous Page</a></td>
					<td align="right"><a href="javascript: goToPage4();">Next
					Page >></a></td>
				</tr>
			</table>

			<table border="0" cellspacing="0" cellpadding="0"
				style="display: none" width="740px" height="85%" id="page4">
				<tr>
					<td colspan="2">
					<table width="740px" height="620px" border="0" cellspacing="0"
						cellpadding="0">
						<tr class="title">
							<th colspan="4">Storage</th>
						</tr>
						<tr>
							<th class="question" width="5%" valign="top">16.</th>
							<th class="question" colspan="3">Can the person easily reach
							items in the kitchen that are used regularly without climbing,
							bending or upsetting his or her balance?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Cupboards are accessible between shoulder and knee height - no
							chairs/stepladders are required to reach things.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="storage16Y" <%= props.getProperty("storage16Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="storage16N" <%= props.getProperty("storage16N", "") %> />
							No</td>
							<td width="75%"></td>
						</tr>
						<tr>
							<th class="question" width="5%">17.</th>
							<th class="question" colspan="3">Can the person carry meals
							easily and safely from the kitchen to the dining area?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Meals can be carried safely or transported using a trolley to
							wherever the person usually eats.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="storage17Y" <%= props.getProperty("storage17Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="storage17N" <%= props.getProperty("storage17N", "") %> />
							No</td>
							<td width="75%"></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr class="title">
							<th colspan="4">Stairways/Steps</th>
						</tr>
						<tr>
							<th class="question" width="5%" valign="top">18.</th>
							<th class="question" colspan="3">Do the <font
								style="text-decoration: underline">indoor</font> steps/stairs
							have an accessible/ sturdy grab rail extending along the full
							length of the steps/stairs?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Grab rail must be easily gripped firmly fixed, sufficiently
							robusted and available for the full length of the steps or
							stairs.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="stairway18Y" <%= props.getProperty("stairway18Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="stairway18N" <%= props.getProperty("stairway18N", "") %> />
							No</td>
							<td width="75%"><input type="checkbox" class="checkbox"
								name="stairway18NA" <%= props.getProperty("stairway18NA", "") %> />N/A
							(no steps or stairs exist inside the home)</td>
						</tr>
						<tr>
							<th class="question" width="5%" valign="top">19.</th>
							<th class="question" colspan="3">Do the <font
								style="text-decoration: underline">outdoor</font> steps have an
							accessible/ sturdy grab rail extending along the full length of
							the steps/stairs?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Steps = more than two consecutive steps (changes in floor level).
							Grab rail must be easily gripped, firmly fixed, sufficiently
							robust and available for the full length of the steps.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="stairway19Y" <%= props.getProperty("stairway19Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="stairway19N" <%= props.getProperty("stairway19N", "") %> />
							No</td>
							<td width="75%"><input type="checkbox" class="checkbox"
								name="stairway19NA" <%= props.getProperty("stairway19NA", "") %> />N/A
							(no steps or stairs exist outside the home)</td>
						</tr>
						<tr>
							<th class="question" width="5%" valign="top">20.</th>
							<th class="question" colspan="3">Can the person easily and
							safely go up and down the steps/stairs, inside or outside the
							house?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Steps are not too high, too narrow or too uneven for feet to be
							firmly placed on the steps (indoors and outdoors), person is not
							likely to become tired or breathless using the steps/stairs and
							has no medical factor likely to impact on safety on the stairs,
							e.g. foot-drop, loss of sensation in feet, impaired control of
							movement, etc.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="stairway20Y" <%= props.getProperty("stairway20Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="stairway20N" <%= props.getProperty("stairway20N", "") %> />
							No</td>
							<td width="75%"><input type="checkbox" class="checkbox"
								name="stairway20NA" <%= props.getProperty("stairway20NA", "") %> />N/A
							(no steps or stairs exist)</td>
						</tr>
						<tr>
							<td colspan="7">
							<table height="5">
								<tr>
									<td></td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr class="subject">
					<td align="left"><a href="javascript: backToPage3();"><<
					Previous Page</a></td>
					<td align="right"><a href="javascript: goToPage5();">Next
					Page >></a></td>
				</tr>
			</table>

			<table border="0" cellspacing="0" cellpadding="0"
				style="display: none" width="740px" height="85%" id="page5">
				<tr>
					<td colspan="2">
					<table width="740px" height="620px" border="0" cellspacing="0"
						cellpadding="0">
						<tr class="title">
							<th colspan="4">Stairways/Steps (continue...)</th>
						</tr>
						<tr>
							<th class="question" width="5%">21.</th>
							<th class="question" colspan="3">Are the edges of the
							steps/stairs easiliy identified?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							No patterned floor coverings, tiles or painting which could
							obscure the edge of step.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="stairway21Y" <%= props.getProperty("stairway21Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="stairway21N" <%= props.getProperty("stairway21N", "") %> />
							No</td>
							<td width="75%"><input type="checkbox" class="checkbox"
								name="stairway21NA" <%= props.getProperty("stairway21NA", "") %> />N/A
							(no steps or stairs exist)</td>
						</tr>
						<tr>
							<th class="question" width="5%">22.</th>
							<th class="question" colspan="3">Can the person use the
							entrance door(s) safely and easily?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Locks and bolts can be used without bending or over-reaching,
							there is a landing so the person does not have to balance on
							steps to open the door and/or screen door.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="stairway22Y" <%= props.getProperty("stairway22Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="stairway22N" <%= props.getProperty("stairway22N", "") %> />
							No</td>
							<td width="75%"></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr class="title">
							<th colspan="4">Mobility</th>
						</tr>
						<tr>
							<th class="question" width="5%">23.</th>
							<th class="question" colspan="3">Are the paths around the
							house in good repair, and free of clutter?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							No cracked/loose pathways, overgrowing plants/weeds, overhanging
							trees, garden hoses encroaching on walkways.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="mobility23Y" <%= props.getProperty("mobility23Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="mobility23N" <%= props.getProperty("mobility23N", "") %> />
							No</td>
							<td width="75%"><input type="checkbox" class="checkbox"
								name="mobility23NA" <%= props.getProperty("mobility23NA", "") %> />N/A
							(no garden, path or yard exists)</td>
						</tr>
						<tr>
							<th class="question" width="5%">24.</th>
							<th class="question" colspan="3">Is the person wearing well
							fitting slippers and shoes?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Person currently wearing supportive, firmly fitting shoes with
							low heels and non-slip soles or slippers which have not worn and
							support the foot in a good position.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="mobility24Y" <%= props.getProperty("mobility24Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="mobility24N" <%= props.getProperty("mobility24N", "") %> />
							No</td>
							<td width="75%"></td>
						</tr>
						<tr>
							<th class="question" width="5%" valign="top">25.</th>
							<th class="question" colspan="3">If there are pets, can the
							person care for them without bending and being at risk of falling
							over?</th>
						</tr>
						<tr>
							<td></td>
							<td colspan="3"><font style="font-style: italic">Definitions:</font>
							Pets = any animals that the person has responsibility for. Person
							does not have to feed pets when pets are jumping up or getting
							underfoot, person does not have to bend to the floor without
							available support to feed or clean pets, pets do not require a
							lot of exercise.</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="mobility25Y" <%= props.getProperty("mobility25Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="mobility25N" <%= props.getProperty("mobility25N", "") %> />
							No</td>
							<td width="75%"><input type="checkbox" class="checkbox"
								name="mobility25NA" <%= props.getProperty("mobility25NA", "") %> />N/A
							(there are no pets/animals)</td>
						</tr>
						<tr>
							<td colspan="3">
							<table height="40">
								<tr>
									<td>&nbsp;</td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr class="subject">
					<td align="left"><a href="javascript: backToPage4();"><<
					Previous Page</a></td>
					<td align="right"></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td valign="top">
			<table class="Head" class="hidePrint" height="5%">
				<tr>
					<td align="left">
					<%
  if (!bView) {
%> <input type="submit" value="Save"
						onclick="javascript: return onSave();" /> <input type="submit"
						value="Save and Exit"
						onclick="javascript:if(checkBeforeSave()==true) return onSaveExit(); else return false;" />
					<%
  }
%> <input type="button" value="Exit"
						onclick="javascript:return onExit();" /> <input type="button"
						value="Print" onclick="javascript:window.print();" /></td>
					<td align="right">Study ID: <%= props.getProperty("studyID", "N/A") %>
					<input type="hidden" name="studyID"
						value="<%= props.getProperty("studyID", "N/A") %>" /></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>
</body>
</html:html>
