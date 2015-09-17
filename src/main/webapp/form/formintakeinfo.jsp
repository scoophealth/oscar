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
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
    String formClass = "IntakeInfo";
    String formLink = "formintakeinfo.jsp";

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
<title>Intake Information</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">
    
    var choiceFormat  = new Array(11,15,16,22,24,26,37,45,46,53,54,56,62,63,66,67,70,71,72,73,74,77,78,81,82,85,86,89,90,93,94,97,98,101,102,105,106,109,110,113,114,117,118,121,122,125,126,129,130,133,134,137,138,141,142,145,146,149,150,153);
    var allNumericField = new Array(23,57,58,59,60,64,68,154,155);
    var allMatch = null;
    var msg2 = "Are you sure that there are more than 5 people living in your household?"
    var a2 = new Array(0,5, 23);
    var allInputs2 = new Array(a2);
    var a3 = new Array(0,50, 57,58,59,60);
    var msg3 = "Are you sure that you work more than 50 hours for any of the listed activity?"
    var allInputs3 = new Array(a3);
    
    var action = "/<%=project_home%>/form/formname.do";
    
    function backToPage1(){
        document.getElementById('page1').style.display = 'block';
        document.getElementById('page2').style.display = 'none';  
        document.getElementById('page3').style.display = 'none';
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none';        
        
    }
    
    function goToPage2(){
        //check page 1
        var checkboxes = new Array(11,15,16,22,24,26);                                
        if (oneFieldIsNumeric(0,'23')==true && is1CheckboxChecked(0, checkboxes)==true && confirmRange(0, allInputs2,msg2)==true && isFormCompleted(11,36,5,1)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'block'; 
            document.getElementById('page3').style.display = 'none'; 
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
        }
    }

    function backToPage2(){    
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'block'; 
        document.getElementById('page3').style.display = 'none'; 
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none';  
    }
    
    function goToPage3(){
        //check page 2
        var checkboxes = new Array(37,45,46,53,54,56);
        var numericFields = new Array(57,58,59,60);
        
        if (allAreNumeric(0, numericFields)==true && is1CheckboxChecked(0, checkboxes)==true && confirmRange(0, allInputs3,msg3)==true && isFormCompleted(37,60,3,4)==true){
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
        //check page 3
        var checkboxes = new Array(62,63,66,67,70,71,72,73,74,77,78,81,82,85);
        numericFields = new Array(64,68);        
        if (allAreNumeric(0, numericFields)==true && is1CheckboxChecked(0, checkboxes)==true  && isFormCompleted(61,61,0,1)==true && isFormCompleted(62,85,7,0)==true){
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
        var checkboxes = new Array(86,89,90,93,94,97,98,101,102,105,106,109,110,113,114,117,118,121);
        if (is1CheckboxChecked(0, checkboxes)==true && isFormCompleted(86,121,9,0)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'block';
        }
    }

    function checkBeforeSave(){        
        if(document.getElementById('page1').style.display=='block'){
            if(confirmRange(0, allInputs2,msg2)==true && isFormCompleted(11,36,5,1)==true)                
                return true;            
        }
        else if(document.getElementById('page2').style.display=='block' && confirmRange(0, allInputs3,msg3)==true && isFormCompleted(37,60,3,4)==true){            
            return true;
        }
        
        else if(document.getElementById('page5').style.display=='block' && isFormCompleted(122,155,8,2)==true){
            return true;
        }    
        else{
            if(isFormCompleted(11,36,5,1)==true && isFormCompleted(37,60,3,4)==true && isFormCompleted(61,61,0,1)==true && isFormCompleted(62,85,8,0)==true && isFormCompleted(86,121,9,0)==true && isFormCompleted(122,155,8,2)==true)
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
	<!--input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" /-->
	<input type="hidden" name="submit" value="exit" />

	<table border="0" cellspacing="0" cellpadding="0" width="740px"
		height="710px">
		<tr>
			<td>
			<table border="0" cellspacing="0" cellpadding="0" width="740px">
				<tr>
					<th class="subject">Intake Information</th>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td valign="top">
			<table border="0" cellspacing="0" cellpadding="0" height="660px"
				width="740px" id="page1">
				<tr>
					<td colspan="2">
					<table width="740px" height="80px" border="0" cellspacing="0"
						cellpadding="0">
						<tr class="title">
							<th colspan="4">A. Personal Information</th>
						</tr>
						<tr>
							<th class="question">Sex:</th>
							<td><input type="text" name="sex" readonly="true"
								value="<%=props.getProperty("sex","")%>" /></td>
							<th class="question">Date of Birth:</th>
							<td><input type="text" name="dob" readonly="true"
								value="<%=props.getProperty("dob","")%>" /></td>
						</tr>
						<tr>
							<th class="question">Phone Number:</th>
							<td><input type="text" name="phone" readonly="true"
								value="<%=props.getProperty("phone","")%>" /></td>
							<th class="question">Contact Number:</th>
							<td><input type="text" name="contact"
								value="<%=props.getProperty("contact","")%>" /></td>
						</tr>
						<tr>
							<th colspan="3" class="question">What ethno-cultural group
							do you most identify with:</th>
							<td><input type="text" name="ethnoCulturalGr"
								value="<%=props.getProperty("ethnoCulturalGr","")%>"</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td valign="top" colspan="2">
					<table width="740px" height="550px" border="0" cellspacing="0"
						cellpadding="0">
						<tr class="title">
							<th colspan="4">B. Marital Status / Accomodation</th>
						</tr>
						<tr>
							<th colspan="4" class="question">What type of accommodations
							do you live in?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="accommodationHouse"
								<%= props.getProperty("accommodationHouse", "") %> /></td>
							<td width="45%">House</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="accommodationApt"
								<%= props.getProperty("accommodationApt", "") %> /></td>
							<td width="45%">Apartment</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="accommodationSen"
								<%= props.getProperty("accommodationSen", "") %> /></td>
							<td width="45%">Senior's Home</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="accommodationNur"
								<%= props.getProperty("accommodationNur", "") %> /></td>
							<td width="45%">Nursing Home</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="accommodationLTC"
								<%= props.getProperty("accommodationLTC", "") %> /></td>
							<td width="45%">Long Term Care</td>
							<td width="5%"></td>
							<td width="45%"></td>
						</tr>
						<tr>
							<th colspan="4" class="question">What is your present
							marital status?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="maritalStMarried"
								<%= props.getProperty("maritalStMarried", "") %> /></td>
							<td>Married (once)</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="maritalStWidowed"
								<%= props.getProperty("maritalStWidowed", "") %> /></td>
							<td>Widowed</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="maritalStLivingTogether"
								<%= props.getProperty("maritalStLivingTogether", "") %> /></td>
							<td width="45%">Living together/common-law</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="maritalStRemarried"
								<%= props.getProperty("maritalStRemarried", "") %> /></td>
							<td width="45%">Remarried (2 or more marriages)</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="maritalStSeparated"
								<%= props.getProperty("maritalStSeparated", "") %> /></td>
							<td width="45%">Seperated</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="maritalStNeverMarried"
								<%= props.getProperty("maritalStNeverMarried", "") %> /></td>
							<td width="45%">Never married</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="maritalStDivorced"
								<%= props.getProperty("maritalStDivorced", "") %> /></td>
							<td width="45%">Divorced/Annulled</td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<th colspan="2" class="question">How many people live in
							your household?</th>
							<td colspan="2"><input type="text" name="nbpplInHousehold"
								style="background-color: #FFFFFF"
								value="<%=props.getProperty("nbpplInHousehold", "")%>" /></td>
						</tr>
						<tr>
							<th colspan="4" class="question">Is there someone who lives
							with you who can help out?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="helpY"
								<%= props.getProperty("helpY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="helpNoNeed"
								<%= props.getProperty("helpNoNeed", "") %> /></td>
							<td>I do not need help</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="helpN"
								<%= props.getProperty("helpN", "") %> /></td>
							<td width="45%">No</td>
							<td width="5%" align="right"></td>
							<td width="45%"></td>
						</tr>
						<tr>
							<th colspan="4" class="question">If <font
								style="text-decoration: underline">Yes</font>, what is your
							relationship with this individual?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="helpRelationshipSpouse"
								<%= props.getProperty("helpRelationshipSpouse", "") %> /></td>
							<td>Spouse</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="helpRelationshipChildren"
								<%= props.getProperty("helpRelationshipChildren", "") %> /></td>
							<td>Children</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="helpRelationshipSibling"
								<%= props.getProperty("helpRelationshipSibling", "") %> /></td>
							<td>Sibling</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="helpRelationshipGChildren"
								<%= props.getProperty("helpRelationshipGChildren", "") %> /></td>
							<td>Grandchildren/Extened Family</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="helpRelationshipFriend"
								<%= props.getProperty("helpRelationshipFriend", "") %> /></td>
							<td>Friend</td>
							<td width="5%" align="right"></td>
							<td></td>
						</tr>
						<tr>
							<th colspan="4" class="question">Do you have others (not
							living in your home) that you can call on for support?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="otherSupportFriends"
								<%= props.getProperty("otherSupportFriends", "") %> /></td>
							<td>Friends</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="otherSupportChildren"
								<%= props.getProperty("otherSupportChildren", "") %> /></td>
							<td>Children</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="otherSupportSiblings"
								<%= props.getProperty("otherSupportSiblings", "") %> /></td>
							<td>Siblings</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="otherSupportGChildren"
								<%= props.getProperty("otherSupportGChildren", "") %> /></td>
							<td>Grandchildren/Extened Family</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="otherSupportNeighbour"
								<%= props.getProperty("otherSupportNeighbour", "") %> /></td>
							<td>Neighbour</td>
							<td width="5%" align="right"></td>
							<td></td>
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
				style="display: none" width="740px" height="660px" id="page2">
				<tr>
					<td valign="top" colspan="2">
					<table width="740px" height="630px" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td valign="top">
							<table border="0" cellspacing="0" cellpadding="0" width="100%">
								<tr class="title">
									<th colspan="4">C. Education</th>
								</tr>
								<tr>
									<th colspan="4" class="question">What is the highest grade
									<font style="text-decoration: underline">or</font> level of
									education you ever completed?</th>
								</tr>
								<tr bgcolor="white">
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="eduNoSchool"
										<%= props.getProperty("eduNoSchool", "") %> /></td>
									<td width="25%">No School</td>
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="eduSomeCommunity"
										<%= props.getProperty("eduSomeCommunity", "") %> /></td>
									<td width="65%">Some community or technical college,
									nurses' training</td>
								</tr>
								<tr bgcolor="white">
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="eduSomeElementary"
										<%= props.getProperty("eduSomeElementary", "") %> /></td>
									<td width="25%">Some elementary</td>
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="eduCompletedCommunity"
										<%= props.getProperty("eduCompletedCommunity", "") %> /></td>
									<td width="65%">Completed community or technical college,
									nurses' training</td>
								</tr>
								<tr bgcolor="white">
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="eduCompletedElementary"
										<%= props.getProperty("eduCompletedElementary", "") %> /></td>
									<td width="25%">Completed elementary</td>
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="eduSomeUni"
										<%= props.getProperty("eduSomeUni", "") %> /></td>
									<td width="65%">Some university or teacher's college</td>
								</tr>
								<tr bgcolor="white">
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="eduSomeSec"
										<%= props.getProperty("eduSomeSec", "") %> /></td>
									<td width="25%">Some secondary</td>
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="eduCompletedUni"
										<%= props.getProperty("eduCompletedUni", "") %> /></td>
									<td width="65%">Completed university or teacher's college</td>
								</tr>
								<tr bgcolor="white">
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="eduCompletedSec"
										<%= props.getProperty("eduCompletedSec", "") %> /></td>
									<td width="25%">Completed secondary</td>
									<td width="5%" align="right"></td>
									<td width="65%"></td>
								</tr>
							</table>
							</td>
						</tr>

						<tr>
							<td valign="top">
							<table border="0" cellspacing="0" cellpadding="0" width="100%">
								<tr class="title">
									<th colspan="4">D. Income</th>
								</tr>
								<tr>
									<th colspan="4" class="question">What was the approximate
									total household income in the past 12 months; before taxes? <br>
									Please include income from all sources such as as wages,
									commissions, pensions, family allowance, rental investments
									income and so forth.</th>
								</tr>
								<tr bgcolor="white">
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="incomeBelow10"
										<%= props.getProperty("incomeBelow10", "") %> /></td>
									<td width="45%">Below $10,000</td>
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="income40To50"
										<%= props.getProperty("income40To50", "") %> /></td>
									<td width="45%">$40,000 - $50,000</td>
								</tr>
								<tr bgcolor="white">
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="income10To20"
										<%= props.getProperty("income10To20", "") %> /></td>
									<td width="45%">$10,000 - $20,000</td>
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="incomeOver50"
										<%= props.getProperty("incomeOver50", "") %> /></td>
									<td width="45%">Over $50,000</td>
								</tr>
								<tr bgcolor="white">
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="income20To30"
										<%= props.getProperty("income20To30", "") %> /></td>
									<td width="45%">$20,000 - $30,000</td>
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="incomeDoNotKnow"
										<%= props.getProperty("incomeDoNotKnow", "") %> /></td>
									<td width="45%">Do not know</td>
								</tr>
								<tr bgcolor="white">
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="income30To40"
										<%= props.getProperty("income30To40", "") %> /></td>
									<td width="45%">$30,000 - $40,000</td>
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="incomeRefusedToAns"
										<%= props.getProperty("incomeRefusedToAns", "") %> /></td>
									<td width="45%">Refused to answer</td>
								</tr>
								<tr>
									<th colspan="4" class="question">Thinking about your
									financial situation, would you say that you are...</th>
								</tr>
								<tr bgcolor="white">
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="financialDifficult"
										<%= props.getProperty("financialDifficult", "") %> /></td>
									<td colspan="3">Having difficulty making ends meet?</td>
								</tr>
								<tr bgcolor="white">
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="financialEnough"
										<%= props.getProperty("financialEnough", "") %> /></td>
									<td colspan="3">Having just enough to get along?</td>
								</tr>
								<tr bgcolor="white">
									<td width="5%" align="right"><input type="checkbox"
										class="checkbox" name="financialComfortable"
										<%= props.getProperty("financialComfortable", "") %> /></td>
									<td colspan="3">Are you comfortable?</td>
								</tr>
							</table>
							</td>
						</tr>

						<tr>
							<td valign="top">
							<table border="0" cellspacing="0" cellpadding="0" width="100%">
								<tr class="title">
									<th colspan="3">E. Activity</th>
								</tr>
								<tr>
									<th colspan="3" class="question">Are you currently engaged
									in any of the following activites?</th>
								</tr>
								<tr>
									<th colspan="3" class="question">How many hours per week
									are you involved in each activity?</th>
								</tr>
								<tr class="subTitle">
									<td width="30%">Activity</td>
									<td width="30%">Hours/week</td>
									<td width="40%"></td>
								</tr>
								<tr bgcolor="white">
									<td>Paid work</td>
									<td align="center"><input type="text" name="ActPaidWk"
										value="<%= props.getProperty("ActPaidWk", "") %>" /></td>
									<td width="40%"></td>
								</tr>
								<tr bgcolor="white">
									<td>Unpaid work</td>
									<td align="center"><input type="text" name="ActUnpaidWk"
										value="<%= props.getProperty("ActUnpaidWk", "") %>" /></td>
									<td width="40%"></td>
								</tr>
								<tr bgcolor="white">
									<td>Volunteering</td>
									<td align="center"><input type="text"
										name="ActVolunteering"
										value="<%= props.getProperty("ActVolunteering", "") %>" /></td>
									<td width="40%"></td>
								</tr>
								<tr bgcolor="white">
									<td>Caregiving</td>
									<td align="center"><input type="text" name="ActCaregiving"
										value="<%= props.getProperty("ActCaregiving", "") %>" /></td>
									<td width="40%"></td>
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
				style="display: none" width="740px" height="660px" id="page3">
				<tr>
					<td valign="top" colspan="2">
					<table width="740px" height="630px" border="0" cellspacing="0"
						cellpadding="0">
						<tr class="title">
							<th colspan="4">F. Health</th>
						</tr>
						<tr>
							<th colspan="4" class="question">Have you had any health
							problems in the last 6 months?</th>
						</tr>
						<tr bgcolor="white">
							<td colspan="4"><input type="text" name="anyHealthPb"
								size="100" value="<%= props.getProperty("anyHealthPb", "") %>" />
							</td>
						</tr>
						<tr>
							<th colspan="4" class="question">Do you smoke cigarettes?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="smkY"
								<%= props.getProperty("smkY", "") %> /><br>
							<input type="checkbox" class="checkbox" name="smkN"
								<%= props.getProperty("smkN", "") %> /></td>
							<td width="15%">Yes<br>
							No</td>
							<td colspan="2">
							<table>
								<tr>
									<td>How many cigarettes per day?</td>
									<td><input type="text" name="nbCigarettes"
										value="<%= props.getProperty("nbCigarettes", "") %>" /></td>
								</tr>
								<tr>
									<td>How long have you smoked?</td>
									<td><input type="text" name="howLongSmk"
										value="<%=props.getProperty("howLongSmk","")%>" /></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<th colspan="4" class="question">Did you ever smoke
							cigarettes regularly?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="didSmkY"
								<%= props.getProperty("didSmkY", "") %> /><br>
							<input type="checkbox" class="checkbox" name="didSmkN"
								<%= props.getProperty("didSmkN", "") %> /></td>
							<td width="15%">Yes<br>
							No</td>
							<td colspan="2">
							<table>
								<tr>
									<td>How many cigarettes per day?</td>
									<td><input type="text" name="didNbCigarettes"
										value="<%= props.getProperty("didNbCigarettes", "") %>" /></td>
								</tr>
								<tr>
									<td>How long have you smoked?</td>
									<td><input type="text" name="didHowLongSmk"
										value="<%=props.getProperty("didHowLongSmk","")%>" /></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<th colspan="4" class="question">In the past 6 months, have
							you had a drink of beer, wine, liquor or other alcoholic
							beverage?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="alcoholY"
								<%= props.getProperty("alcoholY", "") %> /></td>
							<td>Yes</td>
							<td></td>
							<td></td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="alcoholN"
								<%= props.getProperty("alcoholN", "") %> /></td>
							<td>No</td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<th colspan="4" class="question">Have you had more than 12
							drinks per week in the last 6 months?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="more12DrinksY"
								<%= props.getProperty("more12DrinksY", "") %> /></td>
							<td>Yes</td>
							<td></td>
							<td></td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="more12DrinksN"
								<%= props.getProperty("more12DrinksN", "") %> /></td>
							<td>No</td>
							<td></td>
							<td></td>
						</tr>
						<tr class="title">
							<th valign="top" colspan="8">G. Diagnosis</th>
						</tr>
						<tr>
							<th class="question" colspan="8"><font
								style='text-decoration: italic'> I'm going to read you a
							list of serious illnesses and other health problems. For each
							one, please tell me if a doctor has told you that you have that
							condition since your last assessment. </font></th>
						</tr>

						<tr>
							<th>1.</th>
							<th colspan="7" class="question">Has a doctor ever told you
							that you had a heart attack or myocardial infarction?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="heartAttackY"
								<%= props.getProperty("heartAttackY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="heartAttackRefused"
								<%= props.getProperty("heartAttackRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="heartAttackN"
								<%= props.getProperty("heartAttackN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="heartAttackDoNotKnow"
								<%= props.getProperty("heartAttackDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th>2.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) angina?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="anginaY"
								<%= props.getProperty("anginaY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="anginaRefused"
								<%= props.getProperty("anginaRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="anginaN"
								<%= props.getProperty("anginaN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="anginaDoNotKnow"
								<%= props.getProperty("anginaDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th>3.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) congestive heart failure?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="heartFailureY"
								<%= props.getProperty("heartFailureY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="heartFailureRefused"
								<%= props.getProperty("heartFailureRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="heartFailureN"
								<%= props.getProperty("heartFailureN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="heartFailureDoNotKnow"
								<%= props.getProperty("heartFailureDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
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
				style="display: none" width="740px" height="660px" id="page4">
				<tr>
					<td valign="top" colspan='2'>
					<table width="740px" height="630px" border="0" cellspacing="0"
						cellpadding="0">
						<tr class="title">
							<th valign="top" colspan="8">G. Diagnosis (continue...)</th>
						</tr>
						<tr>
							<th>4.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) high blood pressure?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="highBPY"
								<%= props.getProperty("highBPY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="highBPRefused"
								<%= props.getProperty("highBPRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="highBPN"
								<%= props.getProperty("highBPN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="highBPDoNotKnow"
								<%= props.getProperty("highBPDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th>5.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) any other heart disease?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="otherHeartDiseaseY"
								<%= props.getProperty("otherHeartDiseaseY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="otherHeartDiseaseRefused"
								<%= props.getProperty("otherHeartDiseaseRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="otherHeartDiseaseN"
								<%= props.getProperty("otherHeartDiseaseN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="otherHeartDiseaseDoNotKnow"
								<%= props.getProperty("otherHeartDiseaseDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th>6.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) diabetes?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="diabetesY"
								<%= props.getProperty("diabetesY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="diabetesRefused"
								<%= props.getProperty("diabetesRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="diabetesN"
								<%= props.getProperty("diabetesN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="diabetesDoNotKnow"
								<%= props.getProperty("diabetesDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th>7.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) arthritis?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="arthritisY"
								<%= props.getProperty("arthritisY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="arthritisRefused"
								<%= props.getProperty("arthritisRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="arthritisN"
								<%= props.getProperty("arthritisN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="arthritisDoNotKnow"
								<%= props.getProperty("arthritisDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th>8.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) a stroke?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="strokeY"
								<%= props.getProperty("strokeY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="strokeRefused"
								<%= props.getProperty("strokeRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="strokeN"
								<%= props.getProperty("strokeN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="strokeDoNotKnow"
								<%= props.getProperty("strokeDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th>9.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) cancer?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="cancerY"
								<%= props.getProperty("cancerY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="cancerRefused"
								<%= props.getProperty("cancerRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="cancerN"
								<%= props.getProperty("cancerN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="cancerDoNotKnow"
								<%= props.getProperty("cancerDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th>10.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) a broken or fractured hip?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="brokenHipY"
								<%= props.getProperty("brokenHipY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="brokenHipRefused"
								<%= props.getProperty("brokenHipRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="brokenHipN"
								<%= props.getProperty("brokenHipN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="brokenHipDoNotKnow"
								<%= props.getProperty("brokenHipDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th>11.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) Parkinson's disease?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="parkinsonY"
								<%= props.getProperty("parkinsonY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="parkinsonRefused"
								<%= props.getProperty("parkinsonRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="parkinsonN"
								<%= props.getProperty("parkinsonN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="parkinsonDoNotKnow"
								<%= props.getProperty("parkinsonDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th valign="top">12.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) lung disease, such as emphysema or chronic
							bronchitis?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="lungDiseaseY"
								<%= props.getProperty("lungDiseaseY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="lungDiseaseRefused"
								<%= props.getProperty("lungDiseaseRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="lungDiseaseN"
								<%= props.getProperty("lungDiseaseN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="lungDiseaseDoNotKnow"
								<%= props.getProperty("lungDiseaseDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
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
				style="display: none" width="740px" height="660px" id="page5">
				<tr>
					<td valign="top" colspan="2">
					<table width="740px" height="630px" border="0" cellspacing="0"
						cellpadding="0">
						<tr class="title">
							<th colspan="8">G. Diagnosis (continue...)</th>
						</tr>
						<tr>
							<th>13.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) hearing problems?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="hearingPbY"
								<%= props.getProperty("hearingPbY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="hearingPbRefused"
								<%= props.getProperty("hearingPbRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="hearingPbN"
								<%= props.getProperty("hearingPbN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="hearingPbDoNotKnow"
								<%= props.getProperty("hearingPbDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th>14.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) vision problems?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="visionPbY"
								<%= props.getProperty("visionPbY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="visionPbRefused"
								<%= props.getProperty("visionPbRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="visionPbN"
								<%= props.getProperty("visionPbN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="visionPbDoNotKnow"
								<%= props.getProperty("visionPbDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th>15.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) osteoporosis?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="osteoporosisY"
								<%= props.getProperty("osteoporosisY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="osteoporosisRefused"
								<%= props.getProperty("osteoporosisRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="osteoporosisN"
								<%= props.getProperty("osteoporosisN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="osteoporosisDoNotKnow"
								<%= props.getProperty("osteoporosisDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th valign="top">16.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) fibromyalgia?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="fibromyalgiaY"
								<%= props.getProperty("fibromyalgiaY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="fibromyalgiaRefused"
								<%= props.getProperty("fibromyalgiaRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="fibromyalgiaN"
								<%= props.getProperty("fibromyalgiaN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="fibromyalgiaDoNotKnow"
								<%= props.getProperty("fibromyalgiaDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th valign="top">17.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) multiplesclerosis?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="multiplesclerosisY"
								<%= props.getProperty("multiplesclerosisY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="multiplesclerosisRefused"
								<%= props.getProperty("multiplesclerosisRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="multiplesclerosisN"
								<%= props.getProperty("multiplesclerosisN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="multiplesclerosisDoNotKnow"
								<%= props.getProperty("multiplesclerosisDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th valign="top">18.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) asthma?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="asthmaY"
								<%= props.getProperty("asthmaY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="asthmaRefused"
								<%= props.getProperty("asthmaRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="asthmaN"
								<%= props.getProperty("asthmaN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="asthmaDoNotKnow"
								<%= props.getProperty("asthmaDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th valign="top">19.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) back problem?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="backpainY"
								<%= props.getProperty("backpainY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="backpainRefused"
								<%= props.getProperty("backpainRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="backpainN"
								<%= props.getProperty("backpainN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="backpainDoNotKnow"
								<%= props.getProperty("backpainDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr>
							<th valign="top">20.</th>
							<th colspan="7" class="question">(Has a doctor ever told you
							that you had...) problems with your weight?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="weightY"
								<%= props.getProperty("weightY", "") %> /></td>
							<td>Yes</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="weightRefused"
								<%= props.getProperty("weightRefused", "") %> /></td>
							<td>Refused</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="weightN"
								<%= props.getProperty("weightN", "") %> /></td>
							<td>No</td>
							<td width="5%" align="right"><input type="checkbox"
								class="checkbox" name="weightDoNotKnow"
								<%= props.getProperty("weightDoNotKnow", "") %> /></td>
							<td>Don't Know</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"></td>
							<td>Weight: <input type="text" size="10" name="weight"
								value="<%= props.getProperty("weight", "") %>" /> Kg</td>
							<td width="5%" align="right"></td>
							<td>Height: <input type="text" size="10" name="height"
								value="<%= props.getProperty("height", "") %>" /> meter</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr class="subject">
					<td align="left"><a href="javascript: backToPage4();"><<
					Previous Page</a></td>
					<td>&nbsp;</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td valign="top">
			<table class="Head" class="hidePrint">
				<tr>
					<td align="left">
					<%
  if (!bView) {
%> <input type="submit" value="Save"
						onclick="javascript: return onSave()" /> <input type="submit"
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
