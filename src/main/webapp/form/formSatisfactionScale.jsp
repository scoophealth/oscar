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
    String formClass = "SatisfactionScale";
    String formLink = "formSatisfactionScale.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request), demoNo, formId);

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
<title>Patient Satisfaction Questionnaire (PSQ-18, revised)</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">
    var choiceFormat  = null;//new Array(6,7,8,9,10,11,12,14,15,17,18,20,21,22,23,24,25,27,28,30,31,33,34,36,37,38,39,40,41,42,43,44,45,46,47,49,50,52,53,55,56,58,59,60,61,63,64,65,66,68);
    var allNumericField = null;     
    var allMatch = null;
    var action = "/<%=project_home%>/form/formname.do";
    
    function backToPage1(){             
            document.getElementById('page1').style.display = 'block';
            document.getElementById('page2').style.display = 'none';  
            document.getElementById('page3').style.display = 'none';            
    }
    
    function goToPage2(){      
        var checkboxes = new Array(1,2,3,4);
        if (is1CheckboxChecked(0, checkboxes)==true && isFormCompleted(1,9,2,0)==true){
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
        var checkboxes = new Array(15,17,18,20,21,22,23,24,25,27);
        var numericFields = new Array(57,58,59,60);        
        if (isFormCompleted(15,23,0,9)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'block';              
        }
    }
    
            
   
    function checkBeforeSave(){                        
        if(document.getElementById('page3').style.display=='block'){            
            if(isFormCompleted(24,32,0,9)==true)
                return true;
        }    
        else{            
            if(isFormCompleted(15,23,0,9)==true && isFormCompleted(1,9,2,0)==true && isFormCompleted(24,32,0,9)==true)
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
					<th class="subject">Patient Satisfaction Questionnaire
					(PSQ-18, revised)</th>
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
									<td valign="top" width="5%">&nbsp;</td>
									<td valign="top" width="95%">These next questions are
									about how you feel about the rehabilitation you have received
									in the last 18 months. This would include any physiotherapy or
									occupational therapy services that you received either at
									Stonechurch Family Health Centre or through any other agency or
									service like home care, or a clinic.</td>
								</tr>
							</table>
							</td>
						</tr>

						<tr>
							<td>&nbsp;</td>
							<td colspan="3">We'll begin by getting some background
							information about the rehabilitation you have received over the
							last 18 months</td>
						</tr>

						<tr>
							<th class="question" width="5%">1.</th>
							<th class="question" colspan="3">Do you believe that you
							were in need of occupational therapy or physiotherapy services
							over the past 18 months?</th>
						</tr>

						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="believe1Y" <%= props.getProperty("believe1Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="believe1N" <%= props.getProperty("believe1N", "") %> /> No</td>
							<td width="75%"></td>
						</tr>
						<tr>
							<th class="question" width="5%">2.</th>
							<th class="question" colspan="3">Did you receive
							physiotherapy or occupational therapy in the past 18 months?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%"></td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="receive2Y" <%= props.getProperty("receive2Y", "") %> />
							Yes</td>
							<td width="10%"><input type="checkbox" class="checkbox"
								name="receive2N" <%= props.getProperty("receive2N", "") %> /> No</td>
							<td width="75%"></td>
						</tr>
						<tr>
							<th class="question" width="5%">3.</th>
							<th class="question" colspan="3">If yes, what services did
							you receive:</th>
						</tr>

						<tr bgcolor="white">
							<td width="5%">&nbsp;</dh>
							<td colspan="3"><input type="checkbox" class="checkbox"
								name="receiveOT3Y" <%= props.getProperty("receiveOT3Y", "") %> />
							Occupational therapy</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%">&nbsp;</dh>
							<td colspan="3"><input type="checkbox" class="checkbox"
								name="receiveP3Y" <%= props.getProperty("receiveP3Y", "") %> />
							Physiotherapy</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%">&nbsp;</dh>
							<td colspan="3"><input type="checkbox" class="checkbox"
								name="receiveB3Y" <%= props.getProperty("receiveB3Y", "") %> />
							Both occupational therapy and physiotherapy</td>
						</tr>

						<tr>
							<th class="question" width="5%">4.</th>
							<th class="question" colspan="3">How many treatments or
							visits would you estimate that you had with each?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%">&nbsp;</dh>
							<td colspan="3"><input type="text" name="otTreats4" size="5"
								maxLength="2" value="<%= props.getProperty("otTreats4", "") %>" />
							Occupational therapy</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%">&nbsp;</dh>
							<td colspan="3"><input type="text" name="ptTreats4" size="5"
								maxLength="2" value="<%= props.getProperty("ptTreats4", "") %>" />
							Physiotherapy</td>
						</tr>
						<!--9-->
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
						<tr>
							<td colspan="4">
							<table width="100%">
								<tr>
									<td valign="top" width="5%">&nbsp;</td>
									<td valign="top" width="95%">Below are some things people
									say about rehabilitation. Please read each one carefully,
									keeping in mind the rehabilitation you have received over the
									past 18 months. If you have not received rehabilitation, think
									about what you would expect if you needed it today. We are
									interested in your feelings, good and bad, about the
									rehabilitation you have received.</td>
								</tr>
							</table>
							</td>
						</tr>



						<tr>
							<td></td>
							<td colspan="3">How strongly do you agree or disagree with
							each of the following statements:</td>
						</tr>

						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>

						<tr>
							<td colspan="4">
							<table width="100%" cellpadding="0" cellspacing="0">
								<tr>
									<td class="scoreHeavyTopBox">Strongly<br />
									Agree</td>
									<td class="scoreHeavyTopBox">Agree</td>
									<td class="scoreHeavyTopBox">Uncertain</td>
									<td class="scoreHeavyTopBox">Disagree</td>
									<td class="scoreHeavyTopBox">Strongly<br />
									Disagree</td>
								</tr>
								<tr>
									<td class="scoreHeavyBottomBox">1</td>
									<td class="scoreHeavyBottomBox">2</td>
									<td class="scoreHeavyBottomBox">3</td>
									<td class="scoreHeavyBottomBox">4</td>
									<td class="scoreHeavyBottomBox">5</td>
								</tr>
							</table>
							</td>
						</tr>

						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>

						<tr bgcolor="white">
							<td valign="top" width="5%">1.</td>
							<td valign="top" colspan="2">Rehabilitation professionals
							are good about explaining the reason for tests and assessments</td>
							<td valign="top" align="center" width="5%"><input
								type="text" name="explaining1" size="5" maxLength="2"
								value="<%= props.getProperty("explaining1", "") %>" /></td>
						</tr>

						<tr bgcolor="white">
							<td valign="top" width="5%">2.</td>
							<td valign="top" colspan="2">I think Stonechurch family
							health centre has everything needed to provide complete
							rehabilitation</td>
							<td valign="top" align="center"><input type="text"
								name="everythingNeeded2" size="5" maxLength="2"
								value="<%= props.getProperty("everythingNeeded2", "") %>" /></td>
						</tr>


						<tr bgcolor="white">
							<td valign="top" width="5%">3.</td>
							<td valign="top" colspan="2">The rehabilitation I have been
							receiving is just about perfect</td>
							<td valign="top" align="center"><input type="text"
								name="perfect3" size="5" maxLength="2"
								value="<%= props.getProperty("perfect3", "") %>" /></td>
						</tr>


						<tr bgcolor="white">
							<td valign="top" width="5%">4.</td>
							<td valign="top" colspan="2">Sometimes rehabilitation
							professionals make me wonder if their conclusions about my
							problems are correct</td>
							<td valign="top" align="center"><input type="text"
								name="wonder4" size="5" maxLength="2"
								value="<%= props.getProperty("wonder4", "") %>" /></td>
						</tr>

						<tr bgcolor="white">
							<td valign="top" width="5%">5.</td>
							<td valign="top" colspan="2">I feel confident that I can get
							the rehabilitation I need without being set back financially</td>
							<td valign="top" align="center"><input type="text"
								name="confident5" size="5" maxLength="2"
								value="<%= props.getProperty("confident5", "") %>" /></td>
						</tr>

						<tr bgcolor="white">
							<td valign="top" width="5%">6.</td>
							<td valign="top" colspan="2">When I go for rehabilitation,
							they are careful to check everything when treating and examining
							me</td>
							<td valign="top" align="center"><input type="text"
								name="careful6" size="5" maxLength="2"
								value="<%= props.getProperty("careful6", "") %>" /></td>
						</tr>


						<tr bgcolor="white">
							<td valign="top" width="5%">7.</td>
							<td valign="top" colspan="2">I have to pay for more of my
							rehabilitation than I can afford</td>
							<td valign="top" align="center"><input type="text"
								name="afford7" size="5" maxLength="2"
								value="<%= props.getProperty("afford7", "") %>" /></td>
						</tr>


						<tr bgcolor="white">
							<td valign="top" width="5%">8.</td>
							<td valign="top" colspan="2">I have easy access to the
							rehabilitation I need</td>
							<td valign="top" align="center"><input type="text"
								name="easyaccess8" size="5" maxLength="2"
								value="<%= props.getProperty("easyaccess8", "") %>" /></td>
						</tr>


						<tr bgcolor="white">
							<td valign="top" width="5%">9.</td>
							<td valign="top" colspan="2">Where I get rehabilitation,
							people have to wait too long for treatment</td>
							<td valign="top" align="center"><input type="text"
								name="toolong9" size="5" maxLength="2"
								value="<%= props.getProperty("toolong9", "") %>" /></td>
						</tr>

						<!--18-->

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
					<table width="740px" height="520px" border="0" cellspacing="0"
						cellpadding="0">

						<tr>
							<td></td>
							<td colspan="3">How strongly do you agree or disagree with
							each of the following statements:</td>
						</tr>

						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>

						<tr>
							<td colspan="4">
							<table width="100%" cellpadding="0" cellspacing="0">
								<tr>
									<td class="scoreHeavyTopBox">Strongly<br />
									Agree</td>
									<td class="scoreHeavyTopBox">Agree</td>
									<td class="scoreHeavyTopBox">Uncertain</td>
									<td class="scoreHeavyTopBox">Disagree</td>
									<td class="scoreHeavyTopBox">Strongly<br />
									Disagree</td>
								</tr>
								<tr>
									<td class="scoreHeavyBottomBox">1</td>
									<td class="scoreHeavyBottomBox">2</td>
									<td class="scoreHeavyBottomBox">3</td>
									<td class="scoreHeavyBottomBox">4</td>
									<td class="scoreHeavyBottomBox">5</td>
								</tr>
							</table>
							</td>
						</tr>

						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>

						<tr bgcolor="white">
							<td valign="top" width="5%">10.</td>
							<td valign="top" colspan="2">Rehabilitation professionals
							act too businesslike and impersonal toward me</td>
							<td valign="top" align="center" width="5%"><input
								type="text" name="businesslike10" size="5" maxLength="2"
								value="<%= props.getProperty("businesslike10", "") %>" /></td>
						</tr>

						<tr bgcolor="white">
							<td valign="top" width="5%">11.</td>
							<td valign="top" colspan="2">Rehabilitation professionals
							treat me in a very friendly and courteous manner</td>
							<td valign="top" align="center"><input type="text"
								name="veryfriendly11" size="5" maxLength="2"
								value="<%= props.getProperty("veryfriendly11", "") %>" /></td>
						</tr>


						<tr bgcolor="white">
							<td valign="top" width="5%">12.</td>
							<td valign="top" colspan="2">Those who provide my
							rehabilitation sometimes hurry too much when they treat me</td>
							<td valign="top" align="center"><input type="text"
								name="hurrytoomuch12" size="5" maxLength="2"
								value="<%= props.getProperty("hurrytoomuch12", "") %>" /></td>
						</tr>

						<tr bgcolor="white">
							<td valign="top" width="5%">13.</td>
							<td valign="top" colspan="2">Rehabilitation professionals
							sometimes ignore what I tell them</td>
							<td valign="top" align="center"><input type="text"
								name="ignore13" size="5" maxLength="2"
								value="<%= props.getProperty("ignore13", "") %>" /></td>
						</tr>

						<tr bgcolor="white">
							<td valign="top" width="5%">14.</td>
							<td valign="top" colspan="2">I have some doubts about the
							ability of the rehabilitation professionals who treat me</td>
							<td valign="top" align="center"><input type="text"
								name="doubtability14" size="5" maxLength="2"
								value="<%= props.getProperty("doubtability14", "") %>" /></td>
						</tr>

						<tr bgcolor="white">
							<td valign="top" width="5%">15.</td>
							<td valign="top" colspan="2">Rehabilitation professionals
							usually spend plenty of time with me</td>
							<td valign="top" align="center"><input type="text"
								name="plentyoftime15" size="5" maxLength="2"
								value="<%= props.getProperty("plentyoftime15", "") %>" /></td>
						</tr>

						<tr bgcolor="white">
							<td valign="top" width="5%">16.</td>
							<td valign="top" colspan="2">I find it hard to get an
							appointment for rehabilitation right away</td>
							<td valign="top" align="center"><input type="text"
								name="hardtogetanappointment16" size="5" maxLength="2"
								value="<%= props.getProperty("hardtogetanappointment16", "") %>" />
							</td>
						</tr>

						<tr bgcolor="white">
							<td valign="top" width="5%">17.</td>
							<td valign="top" colspan="2">I am dissatisfied with some
							things about the rehabilitation I receive</td>
							<td valign="top" align="center"><input type="text"
								name="dissatisfied17" size="5" maxLength="2"
								value="<%= props.getProperty("dissatisfied17", "") %>" /></td>
						</tr>

						<tr bgcolor="white">
							<td valign="top" width="5%">18.</td>
							<td valign="top" colspan="2">I am able to get rehabilitation
							whenever I need it</td>
							<td valign="top" align="center"><input type="text"
								name="abletogetrehabilitation18" size="5" maxLength="2"
								value="<%= props.getProperty("abletogetrehabilitation18", "") %>" />
							</td>
						</tr>
						<!--27-->
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
					<td align="right">&nbsp;</td>
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
						onclick="javascript:if(checkBeforeSave()==true) return onSave(); else return false;" />
					<input type="submit" value="Save and Exit"
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
