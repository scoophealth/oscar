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
    String formClass = "SelfManagement";
    String formLink = "formselfmanagement.jsp";

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
<title>Self Management Behaviors</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">
    var choiceFormat  = new Array(25,26,27,28,29,30,31,32,33,34,35,36,37,38,40,41,43,44,47,48);
    var allNumericField = new Array(22,24,39,42,45,46,49,50,54);  
    var a = new Array(0,4, 6,7,8,9,10,12);
    var b = new Array(0,180, 13);
    var c = new Array(0,900, 14);
    var d = new Array(0,5, 15,16,17,18,19,20,51,52,53);
    var allMatch = new Array(a,b,c,d);
    var action = "/<%=project_home%>/form/formname.do";
        
    
    function backToPage1(){  
            
        document.getElementById('page1').style.display = 'block';
        document.getElementById('page2').style.display = 'none';  
        document.getElementById('page3').style.display = 'none';
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none';        
        document.getElementById('page6').style.display = 'none';        
    }
    
    function goToPage2(){              
        var a = new Array(0,4, 6,7,8,9,10,12);
        var b = new Array(0,180, 13);
        var c = new Array(0,900, 14);
        var allInputs = new Array(a,b,c);
        if (areInRange(0, allInputs)==true && isFormCompleted(6,14,0,6)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'block'; 
            document.getElementById('page3').style.display = 'none'; 
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('page6').style.display = 'none';        
        }
    } 
 
    function backToPage2(){                      
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'block'; 
        document.getElementById('page3').style.display = 'none'; 
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none';
        document.getElementById('page6').style.display = 'none';                
    } 

    function goToPage3(){ 
        var a = new Array(0,5, 15,16,17,18,19,20);
        var allInputs = new Array(a);
        if (areInRange(0, allInputs)==true && isFormCompleted(15,21,0,7)==true){
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
        var numericFields = new Array(22,24);
        if (allAreNumeric(0,numericFields)==true && isFormCompleted(22,24,0,3)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'block';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('page6').style.display = 'none';        
        }
    }

    function backToPage4(){            
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none'; 
        document.getElementById('page3').style.display = 'none';  
        document.getElementById('page4').style.display = 'block';
        document.getElementById('page5').style.display = 'none';
        document.getElementById('page6').style.display = 'none';                
    }
    
    function goToPage5(){      
       var checkboxes = new Array(25,26,27,28,29,30,31,32,33,34,35,36,37,38,40,41);
       var numericFields = new Array(39,42);
       if (is1CheckboxChecked(0, checkboxes)==true&&allAreNumeric(0,numericFields)==true && isFormCompleted(25,42,8,2)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'block';
            document.getElementById('page6').style.display = 'none';        
       }
    }

    function backToPage5(){      
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none'; 
        document.getElementById('page3').style.display = 'none';  
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'block';
        document.getElementById('page6').style.display = 'none';                     
    }
    
    function goToPage6(){      
        var checkboxes = new Array(43,44,47,48);
        var numericFields = new Array(45,46,49,50);
        if (is1CheckboxChecked(0, checkboxes)==true&&allAreNumeric(0,numericFields)==true && isFormCompleted(43,50,2,2)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('page6').style.display = 'block';        
        }
    }

    function checkBeforeSave(){                
        if(document.getElementById('page6').style.display=='block'){
            if(isFormCompleted(51,54,0,4)==true)
                return true;
        }    
        else{
            if(isFormCompleted(6,14,0,6)==true && isFormCompleted(15,21,0,7)==true && isFormCompleted(22,24,0,3)==true && isFormCompleted(25,42,8,2)==true && isFormCompleted(43,50,2,2)==true && isFormCompleted(51,54,0,4)==true)
                return true;
        }            
        
        return false;
    }
    
    function exScore(itemNb){        
        var item = document.forms[0].elements[itemNb].value;
        var score = 0;
        switch (item) {
            case "0" :
                score = 0
            break
            case "1":
                score = 15
            break
            case "2" :
                score = 45
            break
            case "3":
                score = 120
            break
            case "4":
                score = 180
            break
            default : score = 0
        }
        return score;
    }
    
    function exScore1(){
        document.forms[0].elements[13].value = exScore('6');        
    }
    
    function exScore2(){
        document.forms[0].elements[14].value = exScore('7')+exScore('8')+exScore('9')+exScore('10')+exScore('12');
    }
    
    function calMentalStressScore(){        
        document.forms[0].elements[24].value = 1;
                
        var times = document.forms[0].elements[22].value;
        if(times==0)
            document.forms[0].elements[24].value = 1;
        else if (times>=1 && times<=7)
            document.forms[0].elements[24].value = 2;
        else if (times>=8)
            document.forms[0].elements[24].value = 3;            

    }
    
    function cognitiveScore(){
        var score = 0;
        var missing = 0;

        for(var i = 15; i<21; i++){
            if(document.forms[0].elements[i].value!="")
                score = eval(score) + eval(document.forms[0].elements[i].value);
            else
                missing++;
        }
        if(missing>2)
            document.forms[0].elements[21].value = "missing";
        else
            document.forms[0].elements[21].value = round_decimals(score/6,2);
    }
    
    function tangibleScore(){
        var score = 0;
        for(var i = 26; i<39; i=i+2){
            if (document.forms[0].elements[i].checked==true)
                score++;
        }
        document.forms[0].elements[39].value = score;
    }
      
    function emotionScore(){
        if (document.forms[0].elements[41].checked==true)
            document.forms[0].elements[42].value = 1;
        else
            document.forms[0].elements[42].value = 0;
    }
    
    function healthEdScore(){        
        document.forms[0].elements[46].value = 1;
        
        if (document.forms[0].elements[44].checked==true){
            var hours = document.forms[0].elements[45].value;
            if(hours>=1 && hours<=5)
                document.forms[0].elements[46].value = 2;
            else if (hours>=6 && hours<=10)
                document.forms[0].elements[46].value = 3;
            else if (hours>=11)
                document.forms[0].elements[46].value = 4;            
        }
    }
    
    function exPrgmScore(){        
        document.forms[0].elements[50].value = 1;
        
        if (document.forms[0].elements[48].checked==true){
            var hours = document.forms[0].elements[49].value;
            if(hours>=1 && hours<=18)
                document.forms[0].elements[50].value = 2;
            else if (hours>=19 && hours<=47)
                document.forms[0].elements[50].value = 3;
            else if (hours>=48)
                document.forms[0].elements[50].value = 4;            
        }
    }
    function commScore(){
        var score = 0;
        var missing = 0;

        for(var i = 51; i<54; i++){
            if(document.forms[0].elements[i].value!="")
                score = eval(score) + eval(document.forms[0].elements[i].value);
            else
                missing++;
        }
        if(missing>1)
            document.forms[0].elements[54].value = "missing";
        else
            document.forms[0].elements[54].value = round_decimals(score/3,2);
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
					<th class="subject">Self-Management Behaviors</th>
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
							<th colspan="5">Exercise</th>
						</tr>
						<tr>
							<td colspan="5"><font style="font-weight: bold">During
							the past week</font> (even if it was <font style="font-weight: bold">not</font>
							a typical week), how much <font style="font-weight: bold">total</font>
							time (for the <font style="font-weight: bold"> entire week</font>)
							did you spend on each of the following? (Please enter score in
							the box for <font style="font-weight: bold">each</font>
							question).</td>
						</tr>
						<tr>
							<td colspan="5">&nbsp;</td>
						</tr>
						<tr class="scoreLight">
							<td width="20%" class="scoreLight">None</td>
							<td width="20%" class="scoreLight">Less than 30 <br>
							minutes/week</td>
							<td width="20%" class="scoreLight">30-60 minutes/week</td>
							<td width="20%" class="scoreLight">1-3 hours/week</td>
							<td width="20%" class="scoreLight">More than 3 hours/week</td>
						</tr>
						<tr class="scoreHeavy">
							<td width="20%" class="scoreHeavy">0</td>
							<td width="20%" class="scoreHeavy">1</td>
							<td width="20%" class="scoreHeavy">2</td>
							<td width="20%" class="scoreHeavy">3</td>
							<td width="20%" class="scoreHeavy">4</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="5" class="scoreLight">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="4">1. Stretching or strengthening exercises
							(ranges of motion, using weights, etc.)</td>
							<td align="center"><input type="text" name="ex1" size="5"
								maxLength="1" onchange="javascript: exScore1();"
								value="<%= props.getProperty("ex1", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="4">2. Walk for exercise</td>
							<td align="center"><input type="text" name="ex2" size="5"
								maxLength="1" onchange="javascript: exScore2();"
								value="<%= props.getProperty("ex2", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="4">3. Swimming or aquatic exercise</td>
							<td align="center"><input type="text" name="ex3" size="5"
								maxLength="1" onchange="javascript: exScore2();"
								value="<%= props.getProperty("ex3", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="4">4. Bicycling (including stationary exercise
							bike)</td>
							<td align="center"><input type="text" name="ex4" size="5"
								maxLength="1" onchange="javascript: exScore2();"
								value="<%= props.getProperty("ex4", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="4">5. Other aerobic exercise equipment
							(stairmaster, rowing or skiing machine)</td>
							<td align="center"><input type="text" name="ex5" size="5"
								maxLength="1" onchange="javascript: exScore2();"
								value="<%= props.getProperty("ex5", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="2">6. Other aerobic exercise - specify:</td>
							<td colspan="2" align="center"><input type="text"
								name="ex6Spec" size="45" maxLength="255"
								value="<%= props.getProperty("ex6Spec", "") %>" /></td>
							<td align="center"><input type="text" name="ex6" size="5"
								maxLength="1" onchange="javascript: exScore2();"
								value="<%= props.getProperty("ex6", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="5">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="5" class="score">Scoring: Each category is
							converted to the following number of minutes spent:</td>
						</tr>
						<tr>
							<td colspan="5">&nbsp;</td>
						</tr>
						<tr class="scoreLight">
							<td width="20%" class="scoreLight">None</td>
							<td width="20%" class="scoreLight">Less than 30 <br>
							minutes/week</td>
							<td width="20%" class="scoreLight">30-60 minutes/week</td>
							<td width="20%" class="scoreLight">1-3 hours/week</td>
							<td width="20%" class="scoreLight">More than 3 hours/week</td>
						</tr>
						<tr class="scoreHeavy">
							<td width="20%" class="scoreHeavy">0</td>
							<td width="20%" class="scoreHeavy">15</td>
							<td width="20%" class="scoreHeavy">45</td>
							<td width="20%" class="scoreHeavy">120</td>
							<td width="20%" class="scoreHeavy">180</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="5" class="scoreLight">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="4">Time spent in stretching or strengthening
							exercise is the value for Item 1.</td>
							<td align="center"><input type="text" name="exTime1"
								size="5" maxLength="3" readonly="true"
								value="<%= props.getProperty("exTime1", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="4">Time spent in aerobic exercise is the sum of
							the values for Items 2 through 6.</td>
							<td align="center"><input type="text" name="exTime2To6"
								size="5" maxLength="3" readonly="true"
								value="<%= props.getProperty("exTime2To6", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="5">
							<table height="50">
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
							<th colspan="6">Cognitive Symptom Management</th>
						</tr>
						<tr>
							<td colspan="6">When you are feeling down in the dumps,
							feeling pain, or having other unpleasant symptoms, how often do
							you... (please enter score in box for <font
								style="font-weight: bold">each</font> question).</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="6">&nbsp;</td>
						</tr>
						<tr class="scoreLight">
							<td width="13%" class="scoreLight">Never</td>
							<td width="13%" class="scoreLight">Almost never</td>
							<td width="13%" class="scoreLight">Sometimes</td>
							<td width="13%" class="scoreLight">Fairly often</td>
							<td width="13%" class="scoreLight">Very often</td>
							<td width="13%" class="scoreLight">Always</td>
						</tr>
						<tr class="scoreHeavy">
							<td width="13%" class="scoreHeavy">0</td>
							<td width="13%" class="scoreHeavy">1</td>
							<td width="13%" class="scoreHeavy">2</td>
							<td width="13%" class="scoreHeavy">3</td>
							<td width="13%" class="scoreHeavy">4</td>
							<td width="13%" class="scoreHeavy">5</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="6" class="scoreLight">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="5">1. Try to feel distant from the discomfort
							and pretend that it is not part of your body?</td>
							<td align="center"><input type="text" name="cog1" size="5"
								maxLength="1" onchange="javascript: cognitiveScore()"
								value="<%= props.getProperty("cog1", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="5">2. Don't think of it as discomfort but as
							some other sensation, like a warm, numb feeling?</td>
							<td align="center"><input type="text" name="cog2" size="5"
								maxLength="1" onchange="javascript: cognitiveScore()"
								value="<%= props.getProperty("cog2", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="5">3. Play mental games or sing songs to keep
							your mind off the discomfort?</td>
							<td align="center"><input type="text" name="cog3" size="5"
								maxLength="1" onchange="javascript: cognitiveScore()"
								value="<%= props.getProperty("cog3", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="5">4. Practice progressive muscle relaxation?</td>
							<td align="center"><input type="text" name="cog4" size="5"
								maxLength="1" onchange="javascript: cognitiveScore()"
								value="<%= props.getProperty("cog4", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="5">5. Practice visualization or guided imagery,
							such as picturing yourself somewhere else?</td>
							<td align="center"><input type="text" name="cog5" size="5"
								maxLength="1" onchange="javascript: cognitiveScore()"
								value="<%= props.getProperty("cog5", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="5">6. Talk to yourself in positive ways?</td>
							<td align="center"><input type="text" name="cog6" size="5"
								maxLength="1" onchange="javascript: cognitiveScore()"
								value="<%= props.getProperty("cog6", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="6">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="6" class="score">Scoring: The score is the mean
							of the six items. If more than two items are missing answers, set
							the value of the score for this scale to missing. Scores range
							from 0 to 5, with a higher score indicating more practice these
							techniques.</td>
						</tr>
						<tr>
							<td colspan="6">Score: <input type="text" name="cogScore"
								readonly="true" value="<%= props.getProperty("cogScore", "") %>" />
							</td>
						</tr>
						<tr>
							<td colspan="6">
							<table height="150">
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
							<th colspan="2">Mental Stress Management/Relaxation</th>
						</tr>
						<tr>
							<td colspan="2">In the past week (even if it was <font
								style="font-weight: bold">not</font> a typical week), how many <font
								style="font-weight: bold">times</font> did you do mental stress
							management or relaxation technique?</td>
						</tr>
						<tr bgcolor="white">
							<td width="35%"></td>
							<td width="65%"><input type="text" name="mentalStressTimes"
								onchange="javascript:calMentalStressScore()" size="5"
								value="<%= props.getProperty("mentalStressTimes", "") %>" />
							times</td>
						</tr>
						<tr bgcolor="white">
							<td>Describe what you do to relax:</td>
							<td><input type="text" name="mentalStressToRelax" size="60"
								value="<%= props.getProperty("mentalStressToRelax", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="2">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="2" class="score">Scoring: Single item only. If
							technique described is not a cognitive stress management
							technique, code as "0". Examples of cognitive techniques include
							progressive muscle relaxation, imagery, prayer and meditation.
							Activities such as reading, listening to music, napping and deep
							breathing are not considered cognitive strategies and should
							receive a score of "0". The number of times is then categorized
							into an ordinal scale with the following categories:</td>
						</tr>
						<tr>
							<td></td>
							<td class="score">1 = None</td>
						</tr>
						<tr>
							<td></td>
							<td class="score">2 = 1 - 7 times/week</td>
						</tr>
						<tr>
							<td></td>
							<td class="score">3 = 8 or more times/week</td>
						</tr>
						<tr>
							<td colspan="2" class="score">This item can also be left as
							a continuous measure: that is, the actual times per week coded.</td>
						</tr>
						<tr>
							<td>Score: <input type="text" name="mentalStressScore"
								value="<%= props.getProperty("mentalStressScore", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="2">
							<table height="280">
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
							<th colspan="3">Use of community Services for Tangible Help</th>
						</tr>
						<tr>
							<td colspan="3">In the <font style="font-weight: bold">past
							9 months</font>, have you gotten help from resources other than friends
							or family for the following services? (Please check Yes or No for
							each category).</td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td width="40%" class="scoreLight">&nbsp;</td>
							<td width="30%" class="scoreLight">No</td>
							<td width="30%" class="scoreLight">Yes</td>
						</tr>
						<tr>
							<td colspan="3" class="scoreHeavy">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td>Housecleaning</td>
							<td align="center"><input type="checkbox" class="checkbox"
								onchange="javascript:tangibleScore()" name="tangibleHelpHouseN"
								<%= props.getProperty("tangibleHelpHouseN", "") %> /></td>
							<td align="center"><input type="checkbox" class="checkbox"
								onchange="javascript:tangibleScore()" name="tangibleHelpHouseY"
								<%= props.getProperty("tangibleHelpHouseY", "") %> /></td>
						</tr>
						<tr bgcolor="white">
							<td>Yard work</td>
							<td align="center"><input type="checkbox" class="checkbox"
								onchange="javascript:tangibleScore()" name="tangibleHelpYardN"
								<%= props.getProperty("tangibleHelpYardN", "") %> /></td>
							<td align="center"><input type="checkbox" class="checkbox"
								onchange="javascript:tangibleScore()" name="tangibleHelpYardY"
								<%= props.getProperty("tangibleHelpYardY", "") %> /></td>
						</tr>
						<tr bgcolor="white">
							<td>Home maintenance/repairs</td>
							<td align="center"><input type="checkbox" class="checkbox"
								onchange="javascript:tangibleScore()" name="tangibleHelpHomeN"
								<%= props.getProperty("tangibleHelpHomeN", "") %> /></td>
							<td align="center"><input type="checkbox" class="checkbox"
								onchange="javascript:tangibleScore()" name="tangibleHelpHomeY"
								<%= props.getProperty("tangibleHelpHomeY", "") %> /></td>
						</tr>
						<tr bgcolor="white">
							<td>Meals</td>
							<td align="center"><input type="checkbox" class="checkbox"
								onchange="javascript:tangibleScore()" name="tangibleHelpMealN"
								<%= props.getProperty("tangibleHelpMealN", "") %> /></td>
							<td align="center"><input type="checkbox" class="checkbox"
								onchange="javascript:tangibleScore()" name="tangibleHelpMealY"
								<%= props.getProperty("tangibleHelpMealY", "") %> /></td>
						</tr>
						<tr bgcolor="white">
							<td>Personal hygiene</td>
							<td align="center"><input type="checkbox" class="checkbox"
								onchange="javascript:tangibleScore()"
								name="tangibleHelpHygieneN"
								<%= props.getProperty("tangibleHelpHygieneN", "") %> /></td>
							<td align="center"><input type="checkbox" class="checkbox"
								onchange="javascript:tangibleScore()"
								name="tangibleHelpHygieneY"
								<%= props.getProperty("tangibleHelpHygieneY", "") %> /></td>
						</tr>
						<tr bgcolor="white">
							<td>Errands</td>
							<td align="center"><input type="checkbox" class="checkbox"
								onchange="javascript:tangibleScore()"
								name="tangibleHelpErrandsN"
								<%= props.getProperty("tangibleHelpErrandsN", "") %> /></td>
							<td align="center"><input type="checkbox" class="checkbox"
								onchange="javascript:tangibleScore()"
								name="tangibleHelpErrandsY"
								<%= props.getProperty("tangibleHelpErrandsY", "") %> /></td>
						</tr>
						<tr bgcolor="white">
							<td>Transportation</td>
							<td align="center"><input type="checkbox" class="checkbox"
								onchange="javascript:tangibleScore()"
								name="tangibleHelpTransportN"
								<%= props.getProperty("tangibleHelpTransportN", "") %> /></td>
							<td align="center"><input type="checkbox" class="checkbox"
								onchange="javascript:tangibleScore()"
								name="tangibleHelpTransportY"
								<%= props.getProperty("tangibleHelpTransportY", "") %> /></td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="3" class="score">Scoring: The score is the
							count of resources circled "Yes", with a possible range of 0 to
							7.</td>
						</tr>
						<tr>
							<td>Score: <input type="text" name="tangibleHelpScore"
								readonly="true"
								value="<%= props.getProperty("tangibleHelpScore", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr class="title">
							<th colspan="3">Use of community Services for Emotional
							Support</th>
						</tr>
						<tr>
							<td colspan="3">In the <font style="font-weight: bold">past
							9 months</font>, have you gotten help from resources other than friends
							or family for the following services? (Please check Yes or No for
							each category).</td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td width="40%" class="scoreLight">&nbsp;</td>
							<td width="30%" class="scoreLight">No</td>
							<td width="30%" class="scoreLight">Yes</td>
						</tr>
						<tr>
							<td colspan="3" class="scoreHeavy">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td>Emotional support or counselling</td>
							<td align="center"><input type="checkbox" class="checkbox"
								onclick="javascript:emotionScore()" name="emotionalSupportN"
								<%= props.getProperty("emotionalSupportN", "") %> /></td>
							<td align="center"><input type="checkbox" class="checkbox"
								onclick="javascript:emotionScore()" name="emotionalSupportY"
								<%= props.getProperty("emotionalSupportY", "") %> /></td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="3" class="score">Scoring: This is a single
							dischotomous item.</td>
						</tr>
						<tr>
							<td colspan="3">Score: <input type="text"
								name="emotionalSupportScore" readonly="true"
								value="<%= props.getProperty("emotionalSupportScore", "") %>" />
							</td>
						</tr>
						<tr>
							<td colspan="3">
							<table height="50">
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
							<th colspan="4">Use of community Education Services/Support
							Groups for Health Problems</th>
						</tr>
						<tr>
							<td colspan="4"><font style="font-weight: bold">Outside
							of this study</font>, have you attended any classes, lectures, or
							support groups about your health problem in the <font
								style="font-weight: bold">past 9 month</font>?</td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%">&nbsp;</td>
							<td width="15%"><input type="checkbox" class="checkbox"
								onchange="javascript: healthEdScore()" name="healthEducationN"
								<%= props.getProperty("healthEducationN", "") %> /> No</td>
							<td width="15%"><input type="checkbox" class="checkbox"
								onchange="javascript: healthEdScore()" name="healthEducationY"
								<%= props.getProperty("healthEducationY", "") %> /> Yes</td>
							<td width="65%">If Yes, how many <font
								style="font-weight: bold">total</font> hours did attend in the <font
								style="font-weight: bold">last 9 months</font>? <input
								type="text" name="healthEducationHours"
								onchange="javascript: healthEdScore()" size="10"
								value="<%= props.getProperty("healthEducationHours", "") %>" />
							</td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4" class="score">Scoring: The score is the
							hours attened that can then be categorized into an ordinal score
							with the following categories:</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td colspan="2" class="score">1 = None</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td colspan="2" class="score">2 = 1 - 5 hours</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td colspan="2" class="score">3 = 6 - 10 hours</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td colspan="2" class="score">4 = 11 or more hours</td>
						</tr>
						<tr>
							<td colspan="4" class="score">This item may also be divided
							into two separate questions: one asking about attendance at
							classes or lectures and the other asking about attendance at
							support groups. The same categorical choices from above can be
							used.</td>
						</tr>
						<tr>
							<td colspan="4">Score: <input type="text"
								name="healthEducationScore" readonly="true"
								value="<%= props.getProperty("healthEducationScore", "") %>" />
							</td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr class="title">
							<th colspan="4">Use of Organized Exercise Programs</th>
						</tr>
						<tr>
							<td colspan="4">In the <font style="font-weight: bold">past
							9 months</font>, have you attended any organized exercise programs (such
							as walking clubs, aerobic classes or water exercise programs)?.</td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%">&nbsp;</td>
							<td width="15%"><input type="checkbox" class="checkbox"
								onchange="javascript:exPrgmScore()" name="exercisePrgmN"
								<%= props.getProperty("exercisePrgmN", "") %> /> No</td>
							<td width="15%"><input type="checkbox" class="checkbox"
								onchange="javascript:exPrgmScore()" name="exercisePrgmY"
								<%= props.getProperty("exercisePrgmY", "") %> /> Yes</td>
							<td width="65%">If Yes, how many <font
								style="font-weight: bold">total</font> hours did attend in the <font
								style="font-weight: bold">last 9 months</font>? <input
								type="text" name="exercisePrgmHours"
								onchange="javascript:exPrgmScore()" size="10"
								value="<%= props.getProperty("exercisePrgmHours", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4" class="score">Scoring: The score is the
							hours attened that can then be categorized into an ordinal score
							with the following categories:</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td colspan="2" class="score">1 = None</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td colspan="2" class="score">2 = 1 - 18 hours</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td colspan="2" class="score">3 = 19 - 47 hours</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td colspan="2" class="score">4 = 48 or more hours</td>
						</tr>
						<tr>
							<td colspan="4" class="score">This item may also be divided
							into two separate questions: one asking about attendance at
							classes or lectures and the other asking about attendance at
							support groups. The same categorical choices from above can be
							used.</td>
						</tr>
						<tr>
							<td colspan="4">Score: <input type="text"
								name="exercisePrgmScore" readonly="true"
								value="<%= props.getProperty("exercisePrgmScore", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="4">
							<table height="50">
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
							<th colspan="6">Communication With Physician</th>
						</tr>
						<tr>
							<td colspan="6">When you visit your doctor, how often do you
							do the following? (Please enter score in box for <font
								style="font-weight: bold">each</font> question).</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="6">&nbsp;</td>
						</tr>
						<tr class="scoreLight">
							<td width="13%" class="scoreLight">Never</td>
							<td width="13%" class="scoreLight">Almost never</td>
							<td width="13%" class="scoreLight">Sometimes</td>
							<td width="13%" class="scoreLight">Fairly often</td>
							<td width="13%" class="scoreLight">Very often</td>
							<td width="13%" class="scoreLight">Always</td>
						</tr>
						<tr class="scoreHeavy">
							<td width="13%" class="scoreHeavy">0</td>
							<td width="13%" class="scoreHeavy">1</td>
							<td width="13%" class="scoreHeavy">2</td>
							<td width="13%" class="scoreHeavy">3</td>
							<td width="13%" class="scoreHeavy">4</td>
							<td width="13%" class="scoreHeavy">5</td>
						</tr>
						<tr class="scoreLight">
							<td colspan="6" class="scoreLight">&nbsp;</td>
						</tr>
						<tr bgcolor="white">
							<td colspan="5">1. Prepare a list of questions for your
							doctor?</td>
							<td align="center"><input type="text" name="communicate1"
								onchange="javascript: commScore()" size="5"
								value="<%= props.getProperty("communicate1", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="5">2. Ask questions about the things you want
							to know and the things you don't understand about your treatment?
							</td>
							<td align="center"><input type="text" name="communicate2"
								onchange="javascript: commScore()" size="5"
								value="<%= props.getProperty("communicate2", "") %>" /></td>
						</tr>
						<tr bgcolor="white">
							<td colspan="5">3. Discuss any personal problems that may be
							related to your illness?</td>
							<td align="center"><input type="text" name="communicate3"
								onchange="javascript: commScore()" size="5"
								value="<%= props.getProperty("communicate3", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="6">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="6" class="score">Scoring: The score is the mean
							of the three items. If more than one items is missing answers,
							set the value of the score for this scale to missing. Scores
							range from 0 to 5, with a higher score indicating better
							communication with the physician.</td>
						</tr>
						<tr>
							<td colspan="6">Score: <input type="text"
								name="communicateScore" readonly="true"
								value="<%= props.getProperty("communicateScore", "") %>" /></td>
						</tr>
						<tr>
							<td colspan="6">
							<table height="250">
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
					<td align="left"><a href="javascript: backToPage5();"><<
					Previous Page</a></td>
					<td>&nbsp;</td>
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
