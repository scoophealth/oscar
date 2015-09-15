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
    String formClass = "CESD";
    String formLink = "formCDSD.jsp";

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
<title>CESD</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">
    
    var choiceFormat  = new Array(6,9,10,13,14,17,18,21,22,25,26,29,30,33,34,37,38,41,42,45,46,49,50,53,54,57,58,61,62,65,66,69,70,73,74,77,78,81,82,85);
    var allNumericField = null;
    var allMatch = null;
    
    var action = "/<%=project_home%>/form/formname.do";
    
    function backToPage1(){             
            document.getElementById('page1').style.display = 'block';
            document.getElementById('page2').style.display = 'none';              
    }
    
    function goToPage2(){      
        var checkboxes = new Array(6,9,10,13,14,17,18,21,22,25,26,29,30,33,34,37,38,41,42,45,46,49,50,53);
        if (is1CheckboxChecked(0, checkboxes)==true && isFormCompleted(6,53,12,0)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'block';             
        }
    }
    
    function checkBeforeSave(){                
        if(document.getElementById('page2').style.display=='block'){
            if(isFormCompleted(54,87,8,1)==true)
                return true;
        }    
        else{
            if(isFormCompleted(6,53,12,0)==true && isFormCompleted(54,87,8,1)==true)
                return true;
        }            
        
        return false;
    }
    
    function calScore(){
        if(is1CheckboxChecked(0, choiceFormat)==true){
            //alert("calScore function called");
            var nbElements = document.forms[0].elements.length;        
            var element;
            var score = 0;
            for(var i=6; i<nbElements; i++){
                element = document.forms[0].elements[i]
                switch(i){
                    case 18:
                    case 19:
                    case 20:
                    case 21:                
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                    case 50:
                    case 51:
                    case 52:
                    case 53:    
                    case 66:
                    case 67:
                    case 68:
                    case 69:                    
                        if(element.checked == true){
                            if(element.name.match("Rare")=="Rare")
                                score = score + 3;
                            else if(element.name.match("Some")=="Some")
                                score = score + 2;
                            else if(element.name.match("Occ")=="Occ")
                                score = score + 1;
                            else if(element.name.match("Most")=="Most")
                                score = score + 0;
                        }
                        break;
                    default:                    
                        if(element.checked == true){
                            if(element.name.match("Rare")=="Rare")
                                score = score + 0;
                            else if(element.name.match("Some")=="Some")
                                score = score + 1;
                            else if(element.name.match("Occ")=="Occ")
                                score = score + 2;
                            else if(element.name.match("Most")=="Most")
                                score = score + 3;
                        }
                        break;
                    }
            }        
            document.forms[0].score.value=score;
        }
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
					<th class="subject">Center for Epidemiologic Studies
					Depression Scale (CES-D), NIMH</th>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td valign="top">
			<table border="0" cellspacing="0" cellpadding="0" height="660px"
				width="740px" id="page1">
				<tr>
					<td valign="top" colspan="2">
					<table width="100%" height="590px" border="0" cellspacing="1px"
						cellpadding="0">
						<tr class="">
							<td colspan="6">Below is a list of the ways you might have
							felt or behaved. Please tell me how often you often you have felt
							this way during the past week.</td>
						</tr>
						<tr class="title">
							<th align="center" colspan="6"><font
								style="font-size: 85%; text-align: center; font-weight: bold">During
							the Past Week</font>
						</tr>
						</th>
						<tr class="title">
							<td colspan="2"></td>
							<td width="12%" align="center"><font style="font-size: 65%;">Rarely
							or none of the time (less than 1 day)</font></td>
							<td width="12%" align="center"><font style="font-size: 65%;">Some
							or a little of the time (1-2 days)</font></td>
							<td width="12%" align="center"><font style="font-size: 65%;">Occationally
							or a moderate amount of time (3-4 days)</font></td>
							<td width="12%" align="center"><font style="font-size: 65%;">Most
							or all of the time (5-7 days)</font></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">1.</td>
							<td class="question" valign="top" width="50%">I was bothered
							by things that usually don't bother me.</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q1Rare"
								<%= props.getProperty("Q1Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q1Some"
								<%= props.getProperty("Q1Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q1Occ"
								<%= props.getProperty("Q1Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q1Most"
								<%= props.getProperty("Q1Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">2.</td>
							<td class="question" valign="top" width="50%">I did not feel
							like eating; my appetite was poor</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q2Rare"
								<%= props.getProperty("Q2Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q2Some"
								<%= props.getProperty("Q2Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q2Occ"
								<%= props.getProperty("Q2Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q2Most"
								<%= props.getProperty("Q2Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">3.</td>
							<td class="question" valign="top" width="50%">I felt that I
							could not shake off the blues even with help from my family or
							friends</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q3Rare"
								<%= props.getProperty("Q3Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q3Some"
								<%= props.getProperty("Q3Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q3Occ"
								<%= props.getProperty("Q3Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q3Most"
								<%= props.getProperty("Q3Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">4.</td>
							<td class="question" valign="top" width="50%">I felt I was
							just as good as other people.</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q4Rare"
								<%= props.getProperty("Q4Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q4Some"
								<%= props.getProperty("Q4Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q4Occ"
								<%= props.getProperty("Q4Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q4Most"
								<%= props.getProperty("Q4Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">5.</td>
							<td class="question" valign="top" width="50%">I had trouble
							keeping my mind on what I was doing</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q5Rare"
								<%= props.getProperty("Q5Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q5Some"
								<%= props.getProperty("Q5Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q5Occ"
								<%= props.getProperty("Q5Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q5Most"
								<%= props.getProperty("Q5Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">6.</td>
							<td class="question" valign="top" width="50%">I felt
							depressed.</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q6Rare"
								<%= props.getProperty("Q6Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q6Some"
								<%= props.getProperty("Q6Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q6Occ"
								<%= props.getProperty("Q6Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q6Most"
								<%= props.getProperty("Q6Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">7.</td>
							<td class="question" valign="top" width="50%">I felt that
							everything I did was an effort.</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q7Rare"
								<%= props.getProperty("Q7Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q7Some"
								<%= props.getProperty("Q7Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q7Occ"
								<%= props.getProperty("Q7Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q7Most"
								<%= props.getProperty("Q7Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">8.</td>
							<td class="question" valign="top" width="50%">I felt hopeful
							about the future.</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q8Rare"
								<%= props.getProperty("Q8Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q8Some"
								<%= props.getProperty("Q8Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q8Occ"
								<%= props.getProperty("Q8Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q8Most"
								<%= props.getProperty("Q8Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">9.</td>
							<td class="question" valign="top" width="50%">I thought my
							life had been a failure</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q9Rare"
								<%= props.getProperty("Q9Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q9Some"
								<%= props.getProperty("Q9Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q9Occ"
								<%= props.getProperty("Q9Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q9Most"
								<%= props.getProperty("Q9Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">10.</td>
							<td class="question" valign="top" width="50%">I felt fearful
							</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q10Rare"
								<%= props.getProperty("Q10Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q10Some"
								<%= props.getProperty("Q10Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q10Occ"
								<%= props.getProperty("Q10Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q10Most"
								<%= props.getProperty("Q10Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">11.</td>
							<td class="question" valign="top" width="50%">My sleep was
							restless.</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q11Rare"
								<%= props.getProperty("Q11Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q11Some"
								<%= props.getProperty("Q11Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q11Occ"
								<%= props.getProperty("Q11Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q11Most"
								<%= props.getProperty("Q11Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">12.</td>
							<td class="question" valign="top" width="50%">I was happy.</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q12Rare"
								<%= props.getProperty("Q12Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q12Some"
								<%= props.getProperty("Q12Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q12Occ"
								<%= props.getProperty("Q12Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q12Most"
								<%= props.getProperty("Q12Most", "") %> /></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr class="subject">
					<td align="left"></td>
					<td align="right"><a href="javascript: goToPage2();">Next
					Page >></a></td>
				</tr>
			</table>
			<table border="0" cellspacing="0" cellpadding="0"
				style="display: none" height="660px" width="740px" id="page2">
				<tr>
					<td valign="top" colspan="2">
					<table width="740px" height="630px" border="0" cellspacing="1"
						cellpadding="0">
						<tr class="title">
							<th align="center" colspan="6"><font
								style="font-size: 85%; text-align: center; font-weight: bold">During
							the Past Week</font>
						</tr>
						</th>
						<tr class="title">
							<td colspan="2"></td>
							<td width="12%" align="center"><font style="font-size: 65%;">Rarely
							or none of the time (less than 1 day)</font></td>
							<td width="12%" align="center"><font style="font-size: 65%;">Some
							or a little of the time (1-2 days)</font></td>
							<td width="12%" align="center"><font style="font-size: 65%;">Occationally
							or a moderate amount of time (3-4 days)</font></td>
							<td width="12%" align="center"><font style="font-size: 65%;">Most
							or all of the time (5-7 days)</font></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">13.</td>
							<td class="question" valign="top" width="50%">I talked less
							than usual.</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q13Rare"
								<%= props.getProperty("Q13Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q13Some"
								<%= props.getProperty("Q13Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q13Occ"
								<%= props.getProperty("Q13Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q13Most"
								<%= props.getProperty("Q13Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">14.</td>
							<td class="question" valign="top" width="50%">I felt lonely.
							</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q14Rare"
								<%= props.getProperty("Q14Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q14Some"
								<%= props.getProperty("Q14Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q14Occ"
								<%= props.getProperty("Q14Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q14Most"
								<%= props.getProperty("Q14Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">15.</td>
							<td class="question" valign="top" width="50%">People were
							unfriendly</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q15Rare"
								<%= props.getProperty("Q15Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q15Some"
								<%= props.getProperty("Q15Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q15Occ"
								<%= props.getProperty("Q15Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q15Most"
								<%= props.getProperty("Q15Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">16.</td>
							<td class="question" valign="top" width="50%">I enjoyed
							life.</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q16Rare"
								<%= props.getProperty("Q16Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q16Some"
								<%= props.getProperty("Q16Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q16Occ"
								<%= props.getProperty("Q16Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q16Most"
								<%= props.getProperty("Q16Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">17.</td>
							<td class="question" valign="top" width="50%">I had crying
							spells.</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q17Rare"
								<%= props.getProperty("Q17Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q17Some"
								<%= props.getProperty("Q17Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q17Occ"
								<%= props.getProperty("Q17Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q17Most"
								<%= props.getProperty("Q17Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">18.</td>
							<td class="question" valign="top" width="50%">I felt sad.</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q18Rare"
								<%= props.getProperty("Q18Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q18Some"
								<%= props.getProperty("Q18Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q18Occ"
								<%= props.getProperty("Q18Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q18Most"
								<%= props.getProperty("Q18Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">19.</td>
							<td class="question" valign="top" width="50%">I felt that
							people dislike me.</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q19Rare"
								<%= props.getProperty("Q19Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q19Some"
								<%= props.getProperty("Q19Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q19Occ"
								<%= props.getProperty("Q19Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q19Most"
								<%= props.getProperty("Q19Most", "") %> /></td>
						</tr>
						<tr>
							<td class="question" valign="top" width="2%">20.</td>
							<td class="question" valign="top" width="50%">I could not
							get "going".</td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q20Rare"
								<%= props.getProperty("Q20Rare", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q20Some"
								<%= props.getProperty("Q20Some", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q20Occ"
								<%= props.getProperty("Q20Occ", "") %> /></td>
							<td bgcolor="white" align="center"><input type="checkbox"
								class="checkbox" name="Q20Most"
								<%= props.getProperty("Q20Most", "") %> /></td>
						</tr>
						<tr class="question">
							<td colspan="6"><font style="font-size: 80%">
							SCORING: Zero for answers in the first column, 1 for answers in
							the second column, 2 for answers in the third column, 3 for
							answers in the fourth column. The scoring of positive items is
							reserved. Possible range of scores is zero to 60, with the higher
							scores indicating the presence of more sympotomatology.</font> <input
								type="button" value="Calculate Score"
								onclick="javascript:calScore();" /></td>
						</tr>
						<tr>
							<td valign="top" colspan="6">SCORE: <input type="text"
								name="score" value="<%=props.getProperty("score", "")%>" /></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr class="subject">
					<td align="left"><a href="javascript: backToPage1();"><<
					Previous Page</a></td>
					<td align="right"></td>
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
