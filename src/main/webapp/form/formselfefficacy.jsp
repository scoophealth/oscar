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
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
    String formClass = "SelfEfficacy";
    String formLink = "formselfefficacy.jsp";

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
<title>Self Efficacy</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">
    var choiceFormat  = null;
    var allNumericField = null;  
    var a = new Array(1,10, 6,7,8,10,12,13,14,15,17,18,19,21,22,23,24,25,27,28,29,31,32,34,36,37,38,39,40,42,43,44,45,46,47);    
    var allMatch = new Array(a);
    var action = "/<%=project_home%>/form/formname.do";
    
    function backToPage1(){              
        document.getElementById('page1').style.display = 'block';
        document.getElementById('page2').style.display = 'none';  
        document.getElementById('page3').style.display = 'none';
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none';        
        document.getElementById('page6').style.display = 'none';     
        document.getElementById('page7').style.display = 'none';     
        document.getElementById('page8').style.display = 'none';     
    }
    
    function goToPage2(){              
        var a = new Array(1,10, 6,7,8,10);        
        var allInputs = new Array(a);
        if (areInRange(0, allInputs)==true && isFormCompleted(6,11,0,6)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'block'; 
            document.getElementById('page3').style.display = 'none'; 
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('page6').style.display = 'none';
            document.getElementById('page7').style.display = 'none';     
            document.getElementById('page8').style.display = 'none';                 
        }
    }

    function backToPage2(){ 
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'block'; 
        document.getElementById('page3').style.display = 'none'; 
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none';
        document.getElementById('page6').style.display = 'none';
        document.getElementById('page7').style.display = 'none';     
        document.getElementById('page8').style.display = 'none';  
    }
    
    function goToPage3(){ 
        var a = new Array(1,10, 12,13,14,15);
        var allInputs = new Array(a);
        if (areInRange(0, allInputs)==true && isFormCompleted(12,16,0,5)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'block';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('page6').style.display = 'none';        
        }
    }

    function backToPage3(){
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none'; 
        document.getElementById('page3').style.display = 'block';  
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none';
        document.getElementById('page6').style.display = 'none';
    }
    
    function goToPage4(){  
        var a = new Array(1,10, 17,18,19);
        var allInputs = new Array(a);
        if (areInRange(0, allInputs)==true && isFormCompleted(17,20,0,4)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'block';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('page6').style.display = 'none';        
            document.getElementById('page7').style.display = 'none';     
            document.getElementById('page8').style.display = 'none';         
        }
    }

    function backToPage4(){
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none'; 
        document.getElementById('page3').style.display = 'none';  
        document.getElementById('page4').style.display = 'block';
        document.getElementById('page5').style.display = 'none';
        document.getElementById('page6').style.display = 'none';        
        document.getElementById('page7').style.display = 'none';     
        document.getElementById('page8').style.display = 'none'; 
    }
    
    function goToPage5(){  
        var a = new Array(1,10, 21,22,23,24,25);
        var allInputs = new Array(a);
        if (areInRange(0, allInputs)==true && isFormCompleted(21,26,0,6)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'block';
            document.getElementById('page6').style.display = 'none';    
            document.getElementById('page7').style.display = 'none';     
            document.getElementById('page8').style.display = 'none';             
       }
    }
    
    function backToPage5(){
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none'; 
        document.getElementById('page3').style.display = 'none';  
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'block';
        document.getElementById('page6').style.display = 'none';    
        document.getElementById('page7').style.display = 'none';     
        document.getElementById('page8').style.display = 'none'; 
    }

    function goToPage6(){             
        var a = new Array(1,10, 27,28,29);
        var allInputs = new Array(a);
        if (areInRange(0, allInputs)==true  && isFormCompleted(27,30,0,4)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('page6').style.display = 'block';  
            document.getElementById('page7').style.display = 'none';     
            document.getElementById('page8').style.display = 'none';               
       }
    }
    
    function backToPage6(){
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none'; 
        document.getElementById('page3').style.display = 'none';  
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none';
        document.getElementById('page6').style.display = 'block';  
        document.getElementById('page7').style.display = 'none';     
        document.getElementById('page8').style.display = 'none'; 
    }

    function goToPage7(){ 
        var a = new Array(1,10, 31,32,34);        
        var allInputs = new Array(a);
        if (areInRange(0, allInputs)==true && isFormCompleted(31,35,0,5)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('page6').style.display = 'none';  
            document.getElementById('page7').style.display = 'block';     
            document.getElementById('page8').style.display = 'none';               
       }
    }

    function backToPage7(){
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none'; 
        document.getElementById('page3').style.display = 'none';  
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none';
        document.getElementById('page6').style.display = 'none';  
        document.getElementById('page7').style.display = 'block';     
        document.getElementById('page8').style.display = 'none'; 
    }
    
    function goToPage8(){      
        var a = new Array(1,10, 36,37,38,39,40);
        var allInputs = new Array(a);
        if (areInRange(0, allInputs)==true && isFormCompleted(36,41,0,6)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('page6').style.display = 'none';  
            document.getElementById('page7').style.display = 'none';     
            document.getElementById('page8').style.display = 'block';               
       }
    }

    function checkBeforeSave(){                
        if(document.getElementById('page8').style.display=='block'){
            if(isFormCompleted(42,48,0,7)==true)
                return true;
        }    
        else{
            if(isFormCompleted(6,11,0,6)==true && isFormCompleted(12,16,0,5)==true && isFormCompleted(17,20,0,4)==true && isFormCompleted(21,26,0,6)==true && isFormCompleted(27,30,0,4)==true && isFormCompleted(31,35,0,5)==true && isFormCompleted(36,41,0,6)==true && isFormCompleted(42,48,0,7)==true)
                return true;
        }            
        
        return false;
    }
    
    function calScore(startItem, endItem, scoreItem, nbMissing){
        var score = 0;
        var missing = 0;
        var nbItems = endItem - startItem + 1;

        if(nbItems!=0){
            for(var i = startItem; i<=endItem; i++){
                if(document.forms[0].elements[i].value!="")
                    score = eval(score) + eval(document.forms[0].elements[i].value);
                else
                    missing++;
            }
            if(missing>nbMissing)
                document.forms[0].elements[scoreItem].value = "missing";
            else
                document.forms[0].elements[scoreItem].value = round_decimals(score/nbItems,2);
        }
    }
    
    function calExerScore(){ 
        calScore("6","8","9","1");
    }
    
    function calDiseaseScore(){ 
        calScore("10","10","11","0");
    }
    
    function calHelpScore(){ 
        calScore("12","15","16","1");
    }
    
    function calCommScore(){ 
        calScore("17","19","20","1");
    }
    
    function calManDiseaseScore(){ 
        calScore("21","25","26","2");
    }
    
    function calChoresScore(){ 
        calScore("27","29","30","1");
    }
    
    function calSocialScore(){ 
        calScore("31","32","33","1");
    }
    
    function calBreathScore(){
        calScore("34","34","35","0");
    }
    
    function calManSymScore(){
        calScore("36","40","41","2");
    }
    
    function calManDprScore(){
        calScore("42","47","48","2");
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
					<th class="subject">Self Efficacy</th>
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
						<tr class="title">
							<th colspan="12">Self Efficacy to Perform Self-Management
							Behaviours</th>
						</tr>
						<tr class="subTitle">
							<th colspan="12">SE Exercise Regularly</th>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12">We would like to know <font
								style="font-weight: bold">how confident</font> you are in doing
							certain activities. For each of the following questions, please
							enter the number that corresponds to your confidence that you can
							do the tasks regularly at the present time.</td>
						</tr>
						<tr>
							<td colspan="12">How confident are you that you can...</td>
						</tr>
						<tr>
							<td width="15%" class="scoreHeavy">Not at all confident</td>
							<td width="7%" class="scoreHeavy">1</td>
							<td width="7%" class="scoreHeavy">2</td>
							<td width="7%" class="scoreHeavy">3</td>
							<td width="7%" class="scoreHeavy">4</td>
							<td width="7%" class="scoreHeavy">5</td>
							<td width="7%" class="scoreHeavy">6</td>
							<td width="7%" class="scoreHeavy">7</td>
							<td width="7%" class="scoreHeavy">8</td>
							<td width="7%" class="scoreHeavy">9</td>
							<td width="7%" class="scoreHeavy">10</td>
							<td width="15%" class="scoreHeavy">Totally Confident</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="12" class="scoreLight">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">1.</td>
									<td valign="top" width="95%">Do gentle exercises for
									muscle strength and flexibility three to four times per week
									(range of motion, using weights, etc.)?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="ex1" size="5"
								onchange="javascript:calExerScore()" maxLength="2"
								class="textbox" value="<%= props.getProperty("ex1", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">2.</td>
									<td valign="top" width="95%">Do an aerobic exercise such
									as walking, swimming or bicycling three to four times each
									week?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="ex2" size="5"
								onchange="javascript:calExerScore()" maxLength="2"
								class="textbox" value="<%= props.getProperty("ex2", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">3.</td>
									<td valign="top" width="95%">Exercise without making your
									symptoms worse?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="ex3" size="5"
								onchange="javascript:calExerScore()" maxLength="2"
								class="textbox" value="<%= props.getProperty("ex3", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12" class="score">Scoring: Score is the mean
							of the three items. If more than one item is missing, set the
							value of the score for this scale to missing. Scores range from 1
							to 10, with a higher score indicating greater self-efficacy.</td>
						</tr>
						<tr>
							<td colspan="12">Score: <input type="text" name="exerScore"
								readonly="true"
								value="<%= props.getProperty("exerScore", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr class="subTitle">
							<th colspan="12">SE Get Information About Disease</th>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12">We would like to know <font
								style="font-weight: bold">how confident</font> you are in doing
							certain activities. For each of the following questions, please
							enter the number that corresponds to your confidence that you can
							do the tasks regularly at the present time.</td>
						</tr>
						<tr>
							<td colspan="12">How confident are you that you can...</td>
						</tr>
						<tr>
							<td width="15%" class="scoreHeavy">Not at all confident</td>
							<td width="7%" class="scoreHeavy">1</td>
							<td width="7%" class="scoreHeavy">2</td>
							<td width="7%" class="scoreHeavy">3</td>
							<td width="7%" class="scoreHeavy">4</td>
							<td width="7%" class="scoreHeavy">5</td>
							<td width="7%" class="scoreHeavy">6</td>
							<td width="7%" class="scoreHeavy">7</td>
							<td width="7%" class="scoreHeavy">8</td>
							<td width="7%" class="scoreHeavy">9</td>
							<td width="7%" class="scoreHeavy">10</td>
							<td width="15%" class="scoreHeavy">Totally Confident</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="12" class="scoreLight">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">1.</td>
									<td valign="top" width="95%">Get Information about your
									disease from community resources?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="disease1"
								size="5" onchange="javascript:calDiseaseScore()" maxLength="2"
								class="textbox" value="<%= props.getProperty("disease1", "") %>" />
							</td>
						</tr>
						<tr>
							<td colspan="12" class="score">Scoring: This is a
							single-item scale; scores range from 1 to 10, with a higher score
							indicating greater self-efficacy.</td>
						</tr>
						<tr>
							<td colspan="12">Score: <input type="text"
								name="diseaseScore" readonly="true"
								value="<%= props.getProperty("diseaseScore", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12">
							<table height="2">
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
							<th colspan="12">Self Efficacy to Perform Self-Management
							Behaviours</th>
						</tr>
						<tr class="subTitle">
							<th colspan="12">SE Obtain Help From Communit, Family and
							Friends</th>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12">We would like to know <font
								style="font-weight: bold">how confident</font> you are in doing
							certain activities. For each of the following questions, please
							enter the number that corresponds to your confidence that you can
							do the tasks regularly at the present time.</td>
						</tr>
						<tr>
							<td colspan="12">How confident are you that you can...</td>
						</tr>
						<tr>
							<td width="15%" class="scoreHeavy">Not at all confident</td>
							<td width="7%" class="scoreHeavy">1</td>
							<td width="7%" class="scoreHeavy">2</td>
							<td width="7%" class="scoreHeavy">3</td>
							<td width="7%" class="scoreHeavy">4</td>
							<td width="7%" class="scoreHeavy">5</td>
							<td width="7%" class="scoreHeavy">6</td>
							<td width="7%" class="scoreHeavy">7</td>
							<td width="7%" class="scoreHeavy">8</td>
							<td width="7%" class="scoreHeavy">9</td>
							<td width="7%" class="scoreHeavy">10</td>
							<td width="15%" class="scoreHeavy">Totally Confident</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="12" class="scoreLight">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">1.</td>
									<td valign="top" width="95%">Get Family and friends to
									help you with the things you need (such as household chores
									like shopping, cooking or transport)?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="help1" size="5"
								onchange="javascript:calHelpScore()" maxLength="2"
								class="textbox" value="<%= props.getProperty("help1", "") %>" />
							</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">2.</td>
									<td valign="top" width="95%">Get emotional support from
									friends and family (such as listening or talking over your
									problems)?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="help2" size="5"
								onchange="javascript:calHelpScore()" maxLength="2"
								class="textbox" value="<%= props.getProperty("help2", "") %>" />
							</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">3.</td>
									<td valign="top" width="95%">Get emotional support from
									resources other than friends and family, if needed?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="help3" size="5"
								onchange="javascript:calHelpScore()" maxLength="2"
								class="textbox" value="<%= props.getProperty("help3", "") %>" />
							</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">4.</td>
									<td valign="top" width="95%">Get help with your daily
									tasks (such as housecleaning, yard work, meals or personal
									hygiene) from other resources other than friends and family, if
									needed?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="help4" size="5"
								onchange="javascript:calHelpScore()" maxLength="2"
								class="textbox" value="<%= props.getProperty("help4", "") %>" />
							</td>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12" class="score">Scoring: Score is the mean
							of the four items. If more than one item is missing, set the
							value of the score for this scale to missing. Scores range from 1
							to 10, with a higher score indicating greater self-efficacy.</td>
						</tr>
						<tr>
							<td colspan="12">Score: <input type="text" name="helpScore"
								readonly="true"
								value="<%= props.getProperty("helpScore", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12">
							<table height="120">
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
							<th colspan="12">Self Efficacy to Perform Self-Management
							Behaviours</th>
						</tr>
						<tr class="subTitle">
							<th colspan="12">SE Communicate with Physician</th>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12">We would like to know <font
								style="font-weight: bold">how confident</font> you are in doing
							certain activities. For each of the following questions, please
							enter the number that corresponds to your confidence that you can
							do the tasks regularly at the present time.</td>
						</tr>
						<tr>
							<td colspan="12">How confident are you that you can...</td>
						</tr>
						<tr>
							<td width="15%" class="scoreHeavy">Not at all confident</td>
							<td width="7%" class="scoreHeavy">1</td>
							<td width="7%" class="scoreHeavy">2</td>
							<td width="7%" class="scoreHeavy">3</td>
							<td width="7%" class="scoreHeavy">4</td>
							<td width="7%" class="scoreHeavy">5</td>
							<td width="7%" class="scoreHeavy">6</td>
							<td width="7%" class="scoreHeavy">7</td>
							<td width="7%" class="scoreHeavy">8</td>
							<td width="7%" class="scoreHeavy">9</td>
							<td width="7%" class="scoreHeavy">10</td>
							<td width="15%" class="scoreHeavy">Totally Confident</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="12" class="scoreLight">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">1.</td>
									<td valign="top" width="95%">Ask your doctor things about
									your illness that concern you?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text"
								name="communicateWithPhy1" size="5"
								onchange="javascript: calCommScore()" maxLength="2"
								class="textbox"
								value="<%= props.getProperty("communicateWithPhy1", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">2.</td>
									<td valign="top" width="95%">Discuss openly with your
									doctor any personal problems that maybe related to your
									illness?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text"
								name="communicateWithPhy2" size="5"
								onchange="javascript: calCommScore()" maxLength="2"
								class="textbox"
								value="<%= props.getProperty("communicateWithPhy2", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">3.</td>
									<td valign="top" width="95%">Work out differences with
									your doctor when they arise?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text"
								name="communicateWithPhy3" size="5"
								onchange="javascript: calCommScore()" maxLength="2"
								class="textbox"
								value="<%= props.getProperty("communicateWithPhy3", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12" class="score">Scoring: Score is the mean
							of the three items. If more than one item is missing, set the
							value of the score for this scale to missing. Scores range from 1
							to 10, with a higher score indicating greater self-efficacy.</td>
						</tr>
						<tr>
							<td colspan="12">Score: <input type="text" name="commScore"
								readonly="true"
								value="<%= props.getProperty("commScore", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12">
							<table height="180">
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
							<th colspan="12">Self Efficacy to Manage Disease in General</th>
						</tr>
						<tr class="subTitle">
							<th colspan="12">SE to Manage Disease in General</th>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12">We would like to know <font
								style="font-weight: bold">how confident</font> you are in doing
							certain activities. For each of the following questions, please
							enter the number that corresponds to your confidence that you can
							do the tasks regularly at the present time.</td>
						</tr>
						<tr>
							<td colspan="12">How confident are you that you can...</td>
						</tr>
						<tr>
							<td width="15%" class="scoreHeavy">Not at all confident</td>
							<td width="7%" class="scoreHeavy">1</td>
							<td width="7%" class="scoreHeavy">2</td>
							<td width="7%" class="scoreHeavy">3</td>
							<td width="7%" class="scoreHeavy">4</td>
							<td width="7%" class="scoreHeavy">5</td>
							<td width="7%" class="scoreHeavy">6</td>
							<td width="7%" class="scoreHeavy">7</td>
							<td width="7%" class="scoreHeavy">8</td>
							<td width="7%" class="scoreHeavy">9</td>
							<td width="7%" class="scoreHeavy">10</td>
							<td width="15%" class="scoreHeavy">Totally Confident</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="12" class="scoreLight">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">1.</td>
									<td valign="top" width="95%">Having an illness often means
									doing different tasks and activities to manage your condition.
									How confident are you that you can do all the things necessary
									to manage your condition on a regular basis?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="manageDisease1"
								size="5" onchange="javascript: calManDiseaseScore()"
								maxLength="2" class="textbox"
								value="<%= props.getProperty("manageDisease1", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">2.</td>
									<td valign="top" width="95%">Judge when the changes in you
									illness mean you should visit a doctor?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="manageDisease2"
								size="5" onchange="javascript: calManDiseaseScore()"
								maxLength="2" class="textbox"
								value="<%= props.getProperty("manageDisease2", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">3.</td>
									<td valign="top" width="95%">Do the different tasks and
									activities needed to manage your health condition so as to
									reduce your need to see a doctor?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="manageDisease3"
								size="5" onchange="javascript: calManDiseaseScore()"
								maxLength="2" class="textbox"
								value="<%= props.getProperty("manageDisease3", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">4.</td>
									<td valign="top" width="95%">Reduce the emotional distress
									caused by your health condition so that it does not affect your
									everyday life?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="manageDisease4"
								size="5" onchange="javascript: calManDiseaseScore()"
								maxLength="2" class="textbox"
								value="<%= props.getProperty("manageDisease4", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">5.</td>
									<td valign="top" width="95%">Do things other than just
									taking medication to reduce how much your illness affects your
									everyday life?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="manageDisease5"
								size="5" onchange="javascript: calManDiseaseScore()"
								maxLength="2" class="textbox"
								value="<%= props.getProperty("manageDisease5", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12" class="score">Scoring: Score is the mean
							of the five items. If more than two item is missing, set the
							value of the score for this scale to missing. Scores range from 1
							to 10, with a higher score indicating greater self-efficacy.</td>
						</tr>
						<tr>
							<td colspan="12">Score: <input type="text"
								name="manDiseaseScore" readonly="true"
								value="<%= props.getProperty("manDiseaseScore", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12">
							<table height="80">
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
							<th colspan="12">Self Efficacy to Achieve Outcomes</th>
						</tr>
						<tr class="subTitle">
							<th colspan="12">SE Do Chores</th>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12">We would like to know <font
								style="font-weight: bold">how confident</font> you are in doing
							certain activities. For each of the following questions, please
							enter the number that corresponds to your confidence that you can
							do the tasks regularly at the present time.</td>
						</tr>
						<tr>
							<td colspan="12">How confident are you that you can...</td>
						</tr>
						<tr>
							<td width="15%" class="scoreHeavy">Not at all confident</td>
							<td width="7%" class="scoreHeavy">1</td>
							<td width="7%" class="scoreHeavy">2</td>
							<td width="7%" class="scoreHeavy">3</td>
							<td width="7%" class="scoreHeavy">4</td>
							<td width="7%" class="scoreHeavy">5</td>
							<td width="7%" class="scoreHeavy">6</td>
							<td width="7%" class="scoreHeavy">7</td>
							<td width="7%" class="scoreHeavy">8</td>
							<td width="7%" class="scoreHeavy">9</td>
							<td width="7%" class="scoreHeavy">10</td>
							<td width="15%" class="scoreHeavy">Totally Confident</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="12" class="scoreLight">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">1.</td>
									<td valign="top" width="95%">Complete your household
									chores, such as vacuuming and yard work, despite your health
									problems?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="doChore1"
								size="5" onchange="javascript:calChoresScore()" maxLength="2"
								class="textbox" value="<%= props.getProperty("doChore1", "") %>" />
							</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">2.</td>
									<td valign="top" width="95%">Get your errands done despite
									your health problems?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="doChore2"
								size="5" onchange="javascript:calChoresScore()" maxLength="2"
								class="textbox" value="<%= props.getProperty("doChore2", "") %>" />
							</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">3.</td>
									<td valign="top" width="95%">Get your shopping done
									despite your health problems?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="doChore3"
								size="5" onchange="javascript:calChoresScore()" maxLength="2"
								class="textbox" value="<%= props.getProperty("doChore3", "") %>" />
							</td>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12" class="score">Scoring: Score is the mean
							of the three items. If more than one item is missing, set the
							value of the score for this scale to missing. Scores range from 1
							to 10, with a higher score indicating greater self-efficacy.</td>
						</tr>
						<tr>
							<td colspan="12">Score: <input type="text"
								name="choresScore" readonly="true"
								value="<%= props.getProperty("choresScore", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12">
							<table height="180">
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
					<td align="left"><a href="javascript: backToPage4();"><<
					Previous Page</a></td>
					<td align="right"><a href="javascript: goToPage6();">Next
					Page >></a></td>
				</tr>
			</table>

			<table border="0" cellspacing="0" cellpadding="0"
				style="display: none" width="740px" height="85%" id="page6">
				<tr>
					<td valign="top" colspan="2">
					<table width="740px" height="620px" border="0" cellspacing="0"
						cellpadding="0">
						<tr class="title">
							<th colspan="12">Self Efficacy to Achieve Outcomes</th>
						</tr>
						<tr class="subTitle">
							<th colspan="12">SE Social/Recreational Activities</th>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12">We would like to know <font
								style="font-weight: bold">how confident</font> you are in doing
							certain activities. For each of the following questions, please
							enter the number that corresponds to your confidence that you can
							do the tasks regularly at the present time.</td>
						</tr>
						<tr>
							<td colspan="12">How confident are you that you can...</td>
						</tr>
						<tr>
							<td width="15%" class="scoreHeavy">Not at all confident</td>
							<td width="7%" class="scoreHeavy">1</td>
							<td width="7%" class="scoreHeavy">2</td>
							<td width="7%" class="scoreHeavy">3</td>
							<td width="7%" class="scoreHeavy">4</td>
							<td width="7%" class="scoreHeavy">5</td>
							<td width="7%" class="scoreHeavy">6</td>
							<td width="7%" class="scoreHeavy">7</td>
							<td width="7%" class="scoreHeavy">8</td>
							<td width="7%" class="scoreHeavy">9</td>
							<td width="7%" class="scoreHeavy">10</td>
							<td width="15%" class="scoreHeavy">Totally Confident</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="12" class="scoreLight">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">1.</td>
									<td valign="top" width="95%">Continue to do your hobbies
									and recreation?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="social1"
								size="5" onchange="javascript:calSocialScore()" maxLength="2"
								class="textbox" value="<%= props.getProperty("social1", "") %>" />
							</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">2.</td>
									<td valign="top" width="95%">Continue to do the things you
									like to do with friends and family (such as social visits and
									recreation)?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="social2"
								size="5" onchange="javascript:calSocialScore()" maxLength="2"
								class="textbox" value="<%= props.getProperty("social2", "") %>" />
							</td>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12" class="score">Scoring: Score is the mean
							of the two items. If more than one item is missing, set the value
							of the score for this scale to missing. Scores range from 1 to
							10, with a higher score indicating greater self-efficacy.</td>
						</tr>
						<tr>
							<td colspan="12">Score: <input type="text"
								name="socialScore" readonly="true"
								value="<%= props.getProperty("socialScore", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr class="subTitle">
							<th colspan="12">SE Manage Shortness of Breath</th>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12">We would like to know <font
								style="font-weight: bold">how confident</font> you are in doing
							certain activities. For each of the following questions, please
							enter the number that corresponds to your confidence that you can
							do the tasks regularly at the present time.</td>
						</tr>
						<tr>
							<td colspan="12">How confident are you that you can...</td>
						</tr>
						<tr>
							<td width="15%" class="scoreHeavy">Not at all confident</td>
							<td width="7%" class="scoreHeavy">1</td>
							<td width="7%" class="scoreHeavy">2</td>
							<td width="7%" class="scoreHeavy">3</td>
							<td width="7%" class="scoreHeavy">4</td>
							<td width="7%" class="scoreHeavy">5</td>
							<td width="7%" class="scoreHeavy">6</td>
							<td width="7%" class="scoreHeavy">7</td>
							<td width="7%" class="scoreHeavy">8</td>
							<td width="7%" class="scoreHeavy">9</td>
							<td width="7%" class="scoreHeavy">10</td>
							<td width="15%" class="scoreHeavy">Totally Confident</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="12" class="scoreLight">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">1.</td>
									<td valign="top" width="95%">Keep your shortness of breath
									from interfering with what you want to do?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="shortBreath1"
								size="5" onchange="javascript:calBreathScore()" maxLength="2"
								class="textbox"
								value="<%= props.getProperty("shortBreath1", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12" class="score">Scoring: This is a
							single-item scale; scores range from 1 to 10, with a higher score
							indicating greater self-efficacy.</td>
						</tr>
						<tr>
							<td colspan="12">Score: <input type="text"
								name="breathScore" readonly="true"
								value="<%= props.getProperty("breathScore", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12">
							<table height="20">
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
					<td align="left"><a href="javascript: backToPage5();"><<
					Previous Page</a></td>
					<td align="right"><a href="javascript: goToPage7();">Next
					Page >></a></td>
				</tr>
			</table>

			<table border="0" cellspacing="0" cellpadding="0"
				style="display: none" width="740px" height="85%" id="page7">
				<tr>
					<td colspan="2">
					<table width="740px" height="620px" border="0" cellspacing="0"
						cellpadding="0">
						<tr class="title">
							<th colspan="12">Self Efficacy to Achieve Outcomes</th>
						</tr>
						<tr class="subTitle">
							<th colspan="12">SE Manage Symptoms</th>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12">We would like to know <font
								style="font-weight: bold">how confident</font> you are in doing
							certain activities. For each of the following questions, please
							enter the number that corresponds to your confidence that you can
							do the tasks regularly at the present time.</td>
						</tr>
						<tr>
							<td colspan="12">How confident are you that you can...</td>
						</tr>
						<tr>
							<td width="15%" class="scoreHeavy">Not at all confident</td>
							<td width="7%" class="scoreHeavy">1</td>
							<td width="7%" class="scoreHeavy">2</td>
							<td width="7%" class="scoreHeavy">3</td>
							<td width="7%" class="scoreHeavy">4</td>
							<td width="7%" class="scoreHeavy">5</td>
							<td width="7%" class="scoreHeavy">6</td>
							<td width="7%" class="scoreHeavy">7</td>
							<td width="7%" class="scoreHeavy">8</td>
							<td width="7%" class="scoreHeavy">9</td>
							<td width="7%" class="scoreHeavy">10</td>
							<td width="15%" class="scoreHeavy">Totally Confident</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="12" class="scoreLight">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">1.</td>
									<td valign="top" width="95%">Reduce your physical
									discomfort or pain?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="manageSymptoms1"
								size="5" onchange="javascript: calManSymScore()" maxLength="2"
								class="textbox"
								value="<%= props.getProperty("manageSymptoms1", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">2.</td>
									<td valign="top" width="95%">Keep the fatigue casued by
									your disease from interfering with the things you want to do?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="manageSymptoms2"
								size="5" onchange="javascript: calManSymScore()" maxLength="2"
								class="textbox"
								value="<%= props.getProperty("manageSymptoms2", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">3.</td>
									<td valign="top" width="95%">Keep the physical discomfort
									or pain of your disease from interfering with the things you
									want to do?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="manageSymptoms3"
								size="5" onchange="javascript: calManSymScore()" maxLength="2"
								class="textbox"
								value="<%= props.getProperty("manageSymptoms3", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">4.</td>
									<td valign="top" width="95%">Keep any other symptoms or
									health problems you have from interfering with the things you
									want to do?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="manageSymptoms4"
								size="5" onchange="javascript: calManSymScore()" maxLength="2"
								class="textbox"
								value="<%= props.getProperty("manageSymptoms4", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">5.</td>
									<td valign="top" width="95%">Control any symptoms or
									health problems you have so that they don't interfere with the
									things you want to do?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="manageSymptoms5"
								size="5" onchange="javascript: calManSymScore()" maxLength="2"
								class="textbox"
								value="<%= props.getProperty("manageSymptoms5", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12" class="score">Scoring: Score is the mean
							of the five items. If more than two item is missing, set the
							value of the score for this scale to missing. Scores range from 1
							to 10, with a higher score indicating greater self-efficacy.</td>
						</tr>
						<tr>
							<td colspan="12">Score: <input type="text"
								name="manSymScore" readonly="true"
								value="<%= props.getProperty("manSymScore", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12">
							<table height="80">
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
					<td align="left"><a href="javascript: backToPage6();"><<
					Previous Page</a></td>
					<td align="right"><a href="javascript: goToPage8();">Next
					Page >></a></td>
				</tr>
			</table>

			<table border="0" cellspacing="0" cellpadding="0"
				style="display: none" width="740px" height="85%" id="page8">
				<tr>
					<td colspan="2">
					<table width="740px" height="620px" border="0" cellspacing="0"
						cellpadding="0">
						<tr class="title">
							<th colspan="12">Self Efficacy to Achieve Outcomes</th>
						</tr>
						<tr class="subTitle">
							<th colspan="12">SE Control/Manage Depression</th>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12">We would like to know <font
								style="font-weight: bold">how confident</font> you are in doing
							certain activities. For each of the following questions, please
							enter the number that corresponds to your confidence that you can
							do the tasks regularly at the present time.</td>
						</tr>
						<tr>
							<td colspan="12">How confident are you that you can...</td>
						</tr>
						<tr>
							<td width="15%" class="scoreHeavy">Not at all confident</td>
							<td width="7%" class="scoreHeavy">1</td>
							<td width="7%" class="scoreHeavy">2</td>
							<td width="7%" class="scoreHeavy">3</td>
							<td width="7%" class="scoreHeavy">4</td>
							<td width="7%" class="scoreHeavy">5</td>
							<td width="7%" class="scoreHeavy">6</td>
							<td width="7%" class="scoreHeavy">7</td>
							<td width="7%" class="scoreHeavy">8</td>
							<td width="7%" class="scoreHeavy">9</td>
							<td width="7%" class="scoreHeavy">10</td>
							<td width="15%" class="scoreHeavy">Totally Confident</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="12" class="scoreLight">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">1.</td>
									<td valign="top" width="95%">Keep from getting discouraged
									when nothing you do seems to make any difference?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="controlDepress1"
								size="5" onchange="javascript:calManDprScore()" maxLength="2"
								class="textbox"
								value="<%= props.getProperty("controlDepress1", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">2.</td>
									<td valign="top" width="95%">Keep from feeling said or
									down in the dumps?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="controlDepress2"
								size="5" onchange="javascript:calManDprScore()" maxLength="2"
								class="textbox"
								value="<%= props.getProperty("controlDepress2", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">3.</td>
									<td valign="top" width="95%">Keep yourself from feeling
									lonely?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="controlDepress3"
								size="5" onchange="javascript:calManDprScore()" maxLength="2"
								class="textbox"
								value="<%= props.getProperty("controlDepress3", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">4.</td>
									<td valign="top" width="95%">Do something to make yourself
									feel better when you are feeling lonely?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="controlDepress4"
								size="5" onchange="javascript:calManDprScore()" maxLength="2"
								class="textbox"
								value="<%= props.getProperty("controlDepress4", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">5.</td>
									<td valign="top" width="95%">Do something to make yourself
									feel beeter when you are feeling discouraged?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="controlDepress5"
								size="5" onchange="javascript:calManDprScore()" maxLength="2"
								class="textbox"
								value="<%= props.getProperty("controlDepress5", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="11">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">6.</td>
									<td valign="top" width="95%">Do something to make yourself
									feel beeter when you feel sad or down in the dumps?</td>
								</tr>
							</table>
							</td>
							<td align="center"><input type="text" name="controlDepress6"
								size="5" onchange="javascript:calManDprScore()" maxLength="2"
								class="textbox"
								value="<%= props.getProperty("controlDepress6", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12" class="score">Scoring: Score is the mean
							of the six items. If more than two item is missing, set the value
							of the score for this scale to missing. Scores range from 1 to
							10, with a higher score indicating greater self-efficacy.</td>
						</tr>
						<tr>
							<td colspan="12">Score: <input type="text"
								name="manDprScore" readonly="true"
								value="<%= props.getProperty("manDprScore", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="12">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="12">
							<table height="80">
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
					<td align="left"><a href="javascript: backToPage7();"><<
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
