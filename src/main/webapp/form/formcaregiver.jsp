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
    String formClass = "Caregiver";
    String formLink = "formcaregiver.jsp";

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
<title>Caregiver Questionnaire</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">
    
    var choiceFormat  = new Array(6,7,11,16,18,19,20,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50);    
    var allNumericField = new Array(8,9,10,52);
    var a = new Array(0,9999, 8);
    var b = new Array(0,12,9);
    var c = new Array(0,31,10);
    var d = new Array(0,100,52);
    var allMatch = new Array(a,b,c,d);            
    
    var action = "/<%=project_home%>/form/formname.do";
    
    function backToPage1(){             
            document.getElementById('page1').style.display = 'block';
            document.getElementById('page2').style.display = 'none';  
            document.getElementById('page3').style.display = 'none';                
    }
    
    function goToPage2(){      
        var checkboxes = new Array(6,7,11,16,18,19,20,24);
        var numericFields = new Array(8,9,10);        
        var allInputs = new Array(a,b,c);
        if (allAreNumeric(0,numericFields)==true && is1CheckboxChecked(0, checkboxes)==true && areInRange(0, allInputs)==true && isFormCompleted(6,24,4,3)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'block'; 
            document.getElementById('page3').style.display = 'none'; 
        }
    }
    
    function backToPage2(){
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'block'; 
        document.getElementById('page3').style.display = 'none'; 
    }

    function goToPage3(){      
        var checkboxes = new Array(25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50);                
        if (is1CheckboxChecked(0, checkboxes)==true && isFormCompleted(25,51,13,1)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'block'; 
        }
    }
    
    function checkBeforeSave(){                
        if(document.getElementById('page3').style.display=='block'){
            if(isFormCompleted(52,52,0,1)==true)
                return true;
        }    
        else{
            if(isFormCompleted(6,24,4,3)==true && isFormCompleted(25,51,13,1)==true && isFormCompleted(52,52,0,1)==true)
                return true;
        }            
        
        return false;
    }
    
    function calScore(){
        var startEle = 25;
        var endEle = 50;
        var nbItems = endEle - startEle + 1;
        var scoreEle = 51;
        var score=0;
        if(nbItems!=0){
            for(var i = startEle; i<=endEle; i=i+2){
                if(document.forms[0].elements[i].checked==true)
                    score++;              
            }
           document.forms[0].elements[scoreEle].value = score;
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
					<th class="subject">Caregiver Questionnaire</th>
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
					<table width="740px" height="630px" border="0" cellspacing="0"
						cellpadding="0">
						<tr class="title">
							<th colspan="4">Caregiver Demographic Questions:</th>
						</tr>
						<tr>
						<tr class="question">
							<th class="question" width="4%">1.</th>
							<th class="question">Sex</th>
						</tr>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="checkbox" class="checkbox"
								name="sexM" <%= props.getProperty("sexM", "") %> /> M</td>
						</tr>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="checkbox" class="checkbox"
								name="sexF" <%= props.getProperty("sexF", "") %> /> F</td>
						</tr>

						<tr class="question">
							<th class="question" width="4%">2.</th>
							<th class="question">Date of Birth: (yyyy/mm/dd)</th>
						</tr>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="text" name="dobYear"
								size="5" value="<%= props.getProperty("dobYear", "") %>" />/ <input
								type="text" name="dobMonth" size="5"
								value="<%= props.getProperty("dobMonth", "") %>" />/ <input
								type="text" name="dobDay" size="5"
								value="<%= props.getProperty("dobDay", "") %>" /></td>
						</tr>

						<tr class="question">
							<th class="question" width="4%">3.</th>
							<th class="question">What is your relationship to the
							patient?</th>
						</tr>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="checkbox" class="checkbox"
								name="spouseY" <%= props.getProperty("spouseY", "") %> /> Spouse
							</td>
						</tr>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="checkbox" class="checkbox"
								name="childY" <%= props.getProperty("childY", "") %> /> Child</td>
						</tr>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="checkbox" class="checkbox"
								name="grandchildY" <%= props.getProperty("grandchildY", "") %> />
							Grandchild</td>
						</tr>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="checkbox" class="checkbox"
								name="siblingY" <%= props.getProperty("siblingY", "") %> />
							Sibling</td>
						</tr>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="checkbox" class="checkbox"
								name="friendY" <%= props.getProperty("friendY", "") %> /> Friend
							</td>
						</tr>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="checkbox" class="checkbox"
								name="otherY" <%= props.getProperty("otherY", "") %> /> Other,
							please specify: <input type="text" name="otherRelation"
								value="<%=props.getProperty("otherRelation", "")%>" /></td>
						</tr>
						<tr class="question">
							<th class="question" width="4%">4.</th>
							<th class="question">Do you reside with the patient?</th>
						</tr>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="checkbox" class="checkbox"
								name="resideY" <%= props.getProperty("resideY", "") %> /> Yes</td>
						</tr>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="checkbox" class="checkbox"
								name="resideN" <%= props.getProperty("resideN", "") %> /> No</td>
						</tr>
						<tr class="question">
							<th class="question">5.</th>
							<th class="question">In general, would you say your health
							is: (check one)</th>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="checkbox" class="checkbox"
								name="healthEx" <%= props.getProperty("healthEx", "") %> />
							Excellent</td>
						</tr>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="checkbox" class="checkbox"
								name="healthVG" <%= props.getProperty("healthVG", "") %> /> Very
							Good</td>
						</tr>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="checkbox" class="checkbox"
								name="healthG" <%= props.getProperty("healthG", "") %> /> Good</td>
						</tr>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="checkbox" class="checkbox"
								name="healthF" <%= props.getProperty("healthF", "") %> /> Fair</td>
						</tr>
						<tr class="answer">
							<td></td>
							<td class="answer"><input type="checkbox" class="checkbox"
								name="healthP" <%= props.getProperty("healthP", "") %> /> Poor</td>
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
							<th colspan="4">Caregiver Strain Index:</th>
						</tr>
						<tr>
							<th colspan="2" class="question">Below is a list of things
							that other people have found to be difficult. <br>
							Would you please check of any that apply to you?</th>
							<th>Yes (1)</th>
							<th>No (0)</th>
						</tr>
						<tr>
							<td class="question" width="2%">1.</td>
							<td class="question" width="80%">Sleep is disturbed (e.g.,
							because _____ is in and out of bed or wanders around at night)</td>
							<td class="answerYN" width="9%"><input type="checkbox"
								onchange="javascript: calScore();" class="checkbox" name="Q1Y"
								<%= props.getProperty("Q1Y", "") %> /></td>
							<td class="answerYN" width="9%"><input type="checkbox"
								class="checkbox" name="Q1N" <%= props.getProperty("Q1N", "") %> />
							</td>
						</tr>

						<tr>
							<td class="question">2.</td>
							<td class="question">It is inconvenient (e.g., because
							helping takes so much time or it's a long drive over to help)</td>
							<td class="answerYN"><input type="checkbox"
								onchange="javascript: calScore();" class="checkbox" name="Q2Y"
								<%= props.getProperty("Q2Y", "") %> /></td>
							<td class="answerYN"><input type="checkbox" class="checkbox"
								name="Q2N" <%= props.getProperty("Q2N", "") %> /></td>
						</tr>

						<tr>
							<td class="question">3.</td>
							<td class="question">It is a physical strain (e.g. because
							of lifting in and out of a chair; effort or concentration is
							required)</td>
							<td class="answerYN"><input type="checkbox"
								onchange="javascript: calScore();" class="checkbox" name="Q3Y"
								<%= props.getProperty("Q3Y", "") %> /></td>
							<td class="answerYN"><input type="checkbox" class="checkbox"
								name="Q3N" <%= props.getProperty("Q3N", "") %> /></td>
						</tr>
						<tr>
							<td class="question">4.</td>
							<td class="question">It is confining (e.g., helping
							restricts free time or canno go visiting)</td>
							<td class="answerYN"><input type="checkbox"
								onchange="javascript: calScore();" class="checkbox" name="Q4Y"
								<%= props.getProperty("Q4Y", "") %> /></td>
							<td class="answerYN"><input type="checkbox" class="checkbox"
								name="Q4N" <%= props.getProperty("Q4N", "") %> /></td>
						</tr>
						<tr>
							<td class="question">5.</td>
							<td class="question">There have been family adjustments
							(e.g., because helping has disrupted routine; there has been no
							privacy)</td>
							<td class="answerYN"><input type="checkbox"
								onchange="javascript: calScore();" class="checkbox" name="Q5Y"
								<%= props.getProperty("Q5Y", "") %> /></td>
							<td class="answerYN"><input type="checkbox" class="checkbox"
								name="Q5N" <%= props.getProperty("Q5N", "") %> /></td>
						</tr>
						<tr>
							<td class="question">6.</td>
							<td class="question">There have been changes in personal
							plans (e.g. had to turn down a job; could not go on vacation)</td>
							<td class="answerYN"><input type="checkbox"
								onchange="javascript: calScore();" class="checkbox" name="Q6Y"
								<%= props.getProperty("Q6Y", "") %> /></td>
							<td class="answerYN"><input type="checkbox" class="checkbox"
								name="Q6N" <%= props.getProperty("Q6N", "") %> /></td>
						</tr>
						<tr>
							<td class="question">7.</td>
							<td class="question">There have been other demands on my
							time (e.g., from other family members)</td>
							<td class="answerYN"><input type="checkbox"
								onchange="javascript: calScore();" class="checkbox" name="Q7Y"
								<%= props.getProperty("Q7Y", "") %> /></td>
							<td class="answerYN"><input type="checkbox" class="checkbox"
								name="Q7N" <%= props.getProperty("Q7N", "") %> /></td>
						</tr>
						<tr>
							<td class="question">8.</td>
							<td class="question">There have been emotional adjustments
							(e.g., because of severe arguments)</td>
							<td class="answerYN"><input type="checkbox"
								onchange="javascript: calScore();" class="checkbox" name="Q8Y"
								<%= props.getProperty("Q8Y", "") %> /></td>
							<td class="answerYN"><input type="checkbox" class="checkbox"
								name="Q8N" <%= props.getProperty("Q8N", "") %> /></td>
						</tr>
						<tr>
							<td class="question">9.</td>
							<td class="question">Some behaviour is upsetting (e.g.,
							because of incontinence; _____ has trouble remmebering things; or
							_____ accuses people of taking things</td>
							<td class="answerYN"><input type="checkbox"
								onchange="javascript: calScore();" class="checkbox" name="Q9Y"
								<%= props.getProperty("Q9Y", "") %> /></td>
							<td class="answerYN"><input type="checkbox" class="checkbox"
								name="Q9N" <%= props.getProperty("Q9N", "") %> /></td>
						</tr>
						<tr>
							<td class="question">10.</td>
							<td class="question">It is upsetting to find _____ has
							changed so much from his/her former self (e.g., he/she is a
							different person than he/she used to be)</td>
							<td class="answerYN"><input type="checkbox"
								onchange="javascript: calScore();" class="checkbox" name="Q10Y"
								<%= props.getProperty("Q10Y", "") %> /></td>
							<td class="answerYN"><input type="checkbox" class="checkbox"
								name="Q10N" <%= props.getProperty("Q10N", "") %> /></td>
						</tr>
						<tr>
							<td class="question">11.</td>
							<td class="question">There have been work adjustments (e.g.,
							because of having to take time off)</td>
							<td class="answerYN"><input type="checkbox"
								onchange="javascript: calScore();" class="checkbox" name="Q11Y"
								<%= props.getProperty("Q11Y", "") %> /></td>
							<td class="answerYN"><input type="checkbox" class="checkbox"
								name="Q11N" <%= props.getProperty("Q11N", "") %> /></td>
						</tr>
						<tr>
							<td class="question">12.</td>
							<td class="question">It is a financial strain</td>
							<td class="answerYN"><input type="checkbox"
								onchange="javascript: calScore();" class="checkbox" name="Q12Y"
								<%= props.getProperty("Q12Y", "") %> /></td>
							<td class="answerYN"><input type="checkbox" class="checkbox"
								name="Q12N" <%= props.getProperty("Q12N", "") %> /></td>
						</tr>
						<tr>
							<td class="question">13.</td>
							<td class="question">Feeling completely overwhelmed (e.g.,
							because of worry about _____; concerns about how you will manage)
							</td>
							<td class="answerYN"><input type="checkbox"
								onchange="javascript: calScore();" class="checkbox" name="Q13Y"
								<%= props.getProperty("Q13Y", "") %> /></td>
							<td class="answerYN"><input type="checkbox" class="checkbox"
								name="Q13N" <%= props.getProperty("Q13N", "") %> /></td>
						</tr>
						<tr>
							<td class="question" colspan="2"><font
								style="font-weight: bold">TOTAL SCORE</font> (Count yes
							responses. Any positive answer may indicate a need for
							intervention in that area. A score of 7 or higher indicates a
							high level of stress.)</td>
							<td class="answerYN" colspan="2"><input type="text"
								name="score1" size="10"
								value="<%= props.getProperty("score1", "") %>" /></td>
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
					<td valign="top" colspan='2'>
					<table width="740px" height="40%" border="0" cellspacing="0"
						cellpadding="0">
						<tr class="title">
							<th valign="top" colspan="8">Self-rate burden (SRB)</th>
						</tr>
						<tr>
							<td align="left">On the scale below '0' means that you feel
							that caring for or accompanying _____ at the moment is not hard
							at all; '100' means that you feel that caring for or accompanying
							_____ at the moment is much too hard. Please indicate with an 'X'
							one the scale <i> how burdensome you feel caring for or
							accompanying your family member/friend is at the moment.</i></td>
						</tr>
						<tr class="answer" align="center">
							<td><img src="graphics/caregiver/SRB.jpg" border="0" /></td>
						</tr>
						<tr>
							<td class="answer">SRB Score <input type="text"
								name="SRBScore" value="<%= props.getProperty("SRBScore", "")%>" />
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr class="subject">
					<td align="left"><a href="javascript: backToPage2();"><<
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
