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
    String formClass = "SF36Caregiver";
    String formLink = "formSF36caregiver.jsp";

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
<title>Caregiver: Health Status Quetionnaire (SF36)</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">
    var choiceFormat  = new Array(6,10,12,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50,52,54,56,58,62,64,68,70,74,76,81,82,86,88,92,94,98,100,104,106,111,113,117,119,123,125,129,131,135,137,141,143,147,149,153,155,159,161,165,167,171,173,177,179,183,185,189,191,195,197,201,204,208);
    var allNumericField = null;
    var allMatch = null;
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
        var checkboxes = new Array(6,10,12,16);        
        if (is1CheckboxChecked(0, checkboxes)==true  && isFormCompleted(6,17,2,0)==true){
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
        var checkboxes = new Array(18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50,52,54,56);        
        if (is1CheckboxChecked(0, checkboxes)==true  && isFormCompleted(18,57,10,0)==true){
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
        var checkboxes = new Array(58,62,64,68,70,74,76,81,82,86,88,92,94,98);
        if (is1CheckboxChecked(0, checkboxes)==true  && isFormCompleted(58,99,7,0)==true){
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
        var checkboxes = new Array(100,104,106,111,113,117);
        if (is1CheckboxChecked(0, checkboxes)==true  && isFormCompleted(100,118,3,0)==true){
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
        var checkboxes = new Array(119,123,125,129,131,135,137,141,143,147,149,153,155,159,161,165,167,171);
        if (is1CheckboxChecked(0, checkboxes)==true  && isFormCompleted(119,172,9,0)==true){
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
            if(isFormCompleted(173,208,6,0)==true)
                return true;
        }    
        else{
            if(isFormCompleted(6,17,2,0)==true && isFormCompleted(18,57,10,0)==true && isFormCompleted(58,99,7,0)==true && isFormCompleted(100,118,3,0)==true && isFormCompleted(119,172,9,0)==true && isFormCompleted(173,208,6,0)==true)
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
					<th class="subject">Caregiver: Health Status Quetionnaire
					(SF36)</th>
				</tr>
				<tr class="title">
					<th colspan="4">Health and Daily Activities</th>
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
							<td colspan="4">This survey asks for your views about your
							health. This information will be summarized in your medical
							record and will help keep track of how you feel and how well you
							are able to do your usual activities.</td>
						</tr>
						<tr>
							<td colspan="4">Answer every question by selecting the
							appropriate checkbox. If you unsure about how to answer a
							question, please give the best answer you can and make a comment
							in the text box below each question.</td>
						</tr>
						<tr>
							<th>1.</th>
							<th colspan="3" class="question">In general, would you say
							your health is:</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q1Ex"
								<%= props.getProperty("Q1Ex", "") %> /></td>
							<td width="45%">Excellent</td>
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q1F"
								<%= props.getProperty("Q1F", "") %> /></td>
							<td width="45%">Fair</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q1VG"
								<%= props.getProperty("Q1VG", "") %> /></td>
							<td width="45%">Very Good</td>
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q1P"
								<%= props.getProperty("Q1P", "") %> /></td>
							<td width="45%">Poor</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q1G"
								<%= props.getProperty("Q1G", "") %> /></td>
							<td width="45%">Good</td>
							<td width="5%"></td>
							<td width="45%"></td>
						</tr>
						<tr bgcolor="white">
							<td></td>
							<td colspan="3">Comments: <input type="text" class="textbox"
								size="80" name="Q1Cmt"
								value="<%= props.getProperty("Q1Cmt", "") %>" /></td>
						</tr>
						<tr>
							<th>2.</th>
							<th colspan="3" class="question"><font
								style="text-decoration: underline">Compare to one year
							ago</font>, how would you rate your health in general <font
								style="text-decoration: underline">now</font>?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q2MuchBetter"
								<%= props.getProperty("Q2MuchBetter", "") %> /></td>
							<td width="45%">Much better now than one year ago</td>
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q2Worse"
								<%= props.getProperty("Q2Worse", "") %> /></td>
							<td width="45%">Somewhat worse now than one year ago</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q2Better"
								<%= props.getProperty("Q2Better", "") %> /></td>
							<td width="45%">Somewhat better now than one year ago</td>
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q2MuchWorse"
								<%= props.getProperty("Q2MuchWorse", "") %> /></td>
							<td width="45%">Much worse now than one year ago</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q2Same"
								<%= props.getProperty("Q2Same", "") %> /></td>
							<td width="45%">About the same</td>
							<td width="5%"></td>
							<td width="45%"></td>
						</tr>
						<tr bgcolor="white">
							<td></td>
							<td colspan="3">Comments: <input type="text" class="textbox"
								size="80" name="Q2Cmt"
								value="<%= props.getProperty("Q2Cmt", "") %>" /></td>
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
							<th width="3%" valign="top">3.</th>
							<th valign="top" class="question">The following questions
							are about activities you might do during a typical day. Does <font
								style="text-decoration: underline">your health</font> limit you
							in these activities? If so, how much?</th>
						</tr>
						<tr>
							<td colspan="2" width="100%" align="right">&nbsp;</td>
						</tr>
						<tr>
							<td valign="top" colspan="2">
							<table>
								<tr class="question">
									<th width="3%"></th>
									<th width="36%"></th>
									<th width="12%" align="center">Yes, Limited a lot</th>
									<th width="12%" align="center">Yes, Limited a Little</th>
									<th width="12%" align="center">No, Not Limited at All</th>
									<th width="25%" align="center">Comments</th>
								</tr>
								<tr>
									<td valign="top">a.</td>
									<td><font style='text-decoration: underline'>Vigorous
									activities</font>, such as running, lifting heavy objects,
									participating in strenuous sports</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3aYesLot"
										<%= props.getProperty("Q3aYesLot", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3aYesLittle"
										<%= props.getProperty("Q3aYesLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3aNo"
										<%= props.getProperty("Q3aNo", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q3aCmt"><%= props.getProperty("Q3aCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="top">b.</td>
									<td><font style='text-decoration: underline'>Moderate
									activities</font>, such as moving a table, pushing a vacuum cleaner,
									bowling, or playing golf</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3bYesLot"
										<%= props.getProperty("Q3bYesLot", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3bYesLittle"
										<%= props.getProperty("Q3bYesLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3bNo"
										<%= props.getProperty("Q3bNo", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q3bCmt"><%= props.getProperty("Q3bCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="center">c.</td>
									<td valign="center">Lifting or carrying groceries</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3cYesLot"
										<%= props.getProperty("Q3cYesLot", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3cYesLittle"
										<%= props.getProperty("Q3cYesLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3cNo"
										<%= props.getProperty("Q3cNo", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q3cCmt"><%= props.getProperty("Q3cCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="center">d.</td>
									<td valign="center">Climbing <font
										style="text-decoration: underline">several</font> flights of
									stairs</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3dYesLot"
										<%= props.getProperty("Q3dYesLot", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3dYesLittle"
										<%= props.getProperty("Q3dYesLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3dNo"
										<%= props.getProperty("Q3dNo", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q3dCmt"><%= props.getProperty("Q3dCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="center">e.</td>
									<td valign="center">Climbing <font
										style="text-decoration: underline">one</font> flight of stairs
									</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3eYesLot"
										<%= props.getProperty("Q3eYesLot", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3eYesLittle"
										<%= props.getProperty("Q3eYesLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3eNo"
										<%= props.getProperty("Q3eNo", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q3eCmt"><%= props.getProperty("Q3eCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="center">f.</td>
									<td valign="center">Bending, kneeling, or stooping</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3fYesLot"
										<%= props.getProperty("Q3fYesLot", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3fYesLittle"
										<%= props.getProperty("Q3fYesLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3fNo"
										<%= props.getProperty("Q3fNo", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q3fCmt"><%= props.getProperty("Q3fCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="center">g.</td>
									<td valign="center">Walking <font
										style="text-decoration: underline">more than a mile</font></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3gYesLot"
										<%= props.getProperty("Q3gYesLot", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3gYesLittle"
										<%= props.getProperty("Q3gYesLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3gNo"
										<%= props.getProperty("Q3gNo", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q3gCmt"><%= props.getProperty("Q3gCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="center">h.</td>
									<td valign="center">Walking <font
										style="text-decoration: underline">several blocks</font></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3hYesLot"
										<%= props.getProperty("Q3hYesLot", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3hYesLittle"
										<%= props.getProperty("Q3hYesLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3hNo"
										<%= props.getProperty("Q3hNo", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q3hCmt"><%= props.getProperty("Q3hCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="center">i.</td>
									<td valign="center">Walking <font
										style="text-decoration: underline">one blocks</font></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3iYesLot"
										<%= props.getProperty("Q3iYesLot", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3iYesLittle"
										<%= props.getProperty("Q3iYesLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3iNo"
										<%= props.getProperty("Q3iNo", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q3iCmt"><%= props.getProperty("Q3iCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="center">j.</td>
									<td valign="center">Bathing and dressing yourself</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3jesLot"
										<%= props.getProperty("Q3jYesLot", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3jYesLittle"
										<%= props.getProperty("Q3jYesLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q3jNo"
										<%= props.getProperty("Q3jNo", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q3jCmt"><%= props.getProperty("Q3jCmt", "") %></textarea>
									</td>
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
						<tr>
							<th width="3%" valign="top">4.</th>
							<th valign="top" class="question">During the <font
								style="text-decoration: underline">past 4 weeks</font>, how much
							of the time have you had any of the following problems with your
							work or other regular daily activities <font
								style="text-decoration: underline">as a result of your
							physical health</font>?</th>
						</tr>

						<tr>
							<td valign="top" colspan="2">
							<table>
								<tr class="question">
									<th width="3%"></th>
									<th width="35%"></th>
									<th width="8%" align="center">All of the time</th>
									<th width="8%" align="center">Most of the time</th>
									<th width="8%" align="center">Some of the time</th>
									<th width="8%" align="center">A litte of the time</th>
									<th width="8%" align="center">None of the time</th>
									<th width="22%" align="center">Comments</th>
								</tr>
								<tr>
									<td valign="top">a.</td>
									<td>Cut down on the <font
										style='text-decoration: underline'>amount of time</font> you
									spent on work or other activities</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4aAll"
										<%= props.getProperty("Q4aAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4aMost"
										<%= props.getProperty("Q4aMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4aSome"
										<%= props.getProperty("Q4aSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4aLittle"
										<%= props.getProperty("Q4aLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4aNone"
										<%= props.getProperty("Q4aNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q4aCmt"><%= props.getProperty("Q4aCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="top">b.</td>
									<td><font style='text-decoration: underline'>Accomplished
									less</font> than you would like</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4bAll"
										<%= props.getProperty("Q4bAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4bMost"
										<%= props.getProperty("Q4bMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4bSome"
										<%= props.getProperty("Q4bSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4bLittle"
										<%= props.getProperty("Q4bLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4bNone"
										<%= props.getProperty("Q4bNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q4bCmt"><%= props.getProperty("Q4bCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="top">c.</td>
									<td>Were limited in the <font
										style='text-decoration: underline'>kind</font> of work or
									other activities</td>

									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4cAll"
										<%= props.getProperty("Q4cAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4cMost"
										<%= props.getProperty("Q4cMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4cSome"
										<%= props.getProperty("Q4cSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4cLittle"
										<%= props.getProperty("Q4cLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4cNone"
										<%= props.getProperty("Q4cNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q4cCmt"><%= props.getProperty("Q4cCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="top">d.</td>
									<td>Had <font style='text-decoration: underline'>difficulty</font>
									performing the work or other activities (for example, it took
									extra effort)</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4dAll"
										<%= props.getProperty("Q4dAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4dMost"
										<%= props.getProperty("Q4dMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4dSome"
										<%= props.getProperty("Q4dSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4dLittle"
										<%= props.getProperty("Q4dLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q4dNone"
										<%= props.getProperty("Q4dNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q4dCmt"><%= props.getProperty("Q4dCmt", "") %></textarea>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<th width="3%" valign="top">5.</th>
							<th valign="top" class="question">During the <font
								style="text-decoration: underline">past 4 weeks</font>, how much
							of the time have you had any of the following problmes with your
							work or other regular daily activities <font
								style="text-decoration: underline">as a result of any
							emotional problems</font> (such as feeling depressed or anxious)?</th>
						</tr>

						<tr>
							<td valign="top" colspan="2">
							<table>
								<tr class="question">
									<th width="3%"></th>
									<th width="35%"></th>
									<th width="8%" align="center">All of the time</th>
									<th width="8%" align="center">Most of the time</th>
									<th width="8%" align="center">Some of the time</th>
									<th width="8%" align="center">A litte of the time</th>
									<th width="8%" align="center">None of the time</th>
									<th width="22%" align="center">Comments</th>
								</tr>
								<tr>
									<td valign="top">a.</td>
									<td>Cut down on the <font
										style='text-decoration: underline'>amount of time</font> you
									spent on work or other activities</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q5aAll"
										<%= props.getProperty("Q5aAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q5aMost"
										<%= props.getProperty("Q5aMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q5aSome"
										<%= props.getProperty("Q5aSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q5aLittle"
										<%= props.getProperty("Q5aLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q5aNone"
										<%= props.getProperty("Q5aNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q5aCmt"><%= props.getProperty("Q5aCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="top">b.</td>
									<td><font style='text-decoration: underline'>Accomplished
									less</font> than you would like</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q5bAll"
										<%= props.getProperty("Q5bAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q5bMost"
										<%= props.getProperty("Q5bMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q5bSome"
										<%= props.getProperty("Q5bSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q5bLittle"
										<%= props.getProperty("Q5bLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q5bNone"
										<%= props.getProperty("Q5bNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q5bCmt"><%= props.getProperty("Q5bCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="top">c.</td>
									<td>Didn't do work or other activities as <font
										style='text-decoration: underline'>carefully</font> as usual</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q5cAll"
										<%= props.getProperty("Q5cAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q5cMost"
										<%= props.getProperty("Q5cMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q5cSome"
										<%= props.getProperty("Q5cSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q5cLittle"
										<%= props.getProperty("Q5cLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q5cNone"
										<%= props.getProperty("Q5cNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q5cCmt"><%= props.getProperty("Q5cCmt", "") %></textarea>
									</td>
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
					<td colspan='2'>
					<table width="740px" height="620px" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<th valign="top">6.</th>
							<th valign="top" colspan="3" class="question">During the <font
								style="text-decoration: underline">past 4 weeks</font>, to what
							extent has your physical health or emotional problems interfered
							with your normal social activities with family, friends,
							neighbours, or groups?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q6NotAtAll"
								<%= props.getProperty("Q6NotAtAll", "") %> /></td>
							<td width="45%">Not at all</td>
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q6QuiteABit"
								<%= props.getProperty("Q6QuiteABit", "") %> /></td>
							<td width="45%">Quite a bit</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q6Slightly"
								<%= props.getProperty("Q6Slightly", "") %> /></td>
							<td width="45%">Slightly</td>
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q6Extremely"
								<%= props.getProperty("Q6Extremely", "") %> /></td>
							<td width="45%">Extremely</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q6Moderately"
								<%= props.getProperty("Q6Moderately", "") %> /></td>
							<td width="45%">Moderately</td>
							<td width="5%"></td>
							<td width="45%"></td>
						</tr>
						<tr bgcolor="white">
							<td></td>
							<td colspan="3">Comments: <input type="text" class="textbox"
								class="textbox" size="80" name="Q6Cmt"
								value="<%= props.getProperty("Q6Cmt", "") %>" /></td>
						</tr>
						<tr>
							<th>7.</th>
							<th colspan="3" class="question">How much <font
								style="text-decoration: underline">bodily</font> pain have you
							had during the <font style="text-decoration: underline">past
							4 weeks</font>?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q7None"
								<%= props.getProperty("Q7None", "") %> /></td>
							<td width="45%">None</td>
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q7Moderate"
								<%= props.getProperty("Q7Moderate", "") %> /></td>
							<td width="45%">Moderate</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q7VeryMild"
								<%= props.getProperty("Q7VeryMild", "") %> /></td>
							<td width="45%">Very Mild</td>
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q7Severe"
								<%= props.getProperty("Q7Severe", "") %> /></td>
							<td width="45%">Severe</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q7Mild"
								<%= props.getProperty("Q7Mild", "") %> /></td>
							<td width="45%">Mild</td>
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q7VerySevere"
								<%= props.getProperty("Q7VerySevere", "") %> /></td>
							<td width="45%">Very Severe</td>
						</tr>
						<tr bgcolor="white">
							<td></td>
							<td colspan="3">Comments: <input type="text" class="textbox"
								size="80" name="Q7Cmt"
								value="<%= props.getProperty("Q7Cmt", "") %>" /></td>
						</tr>
						<tr>
							<th valign="top">8.</th>
							<th valign="top" colspan="3" class="question">During the <font
								style="text-decoration: underline">past 4 weeks</font>, how much
							did <font style="text-decoration: underline">pain</font>
							interfere with your normal work (including work both outside the
							home and housework)?</th>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q8NotAtAll"
								<%= props.getProperty("Q8NotAtAll", "") %> /></td>
							<td width="45%">Not at all</td>
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q8QuiteABit"
								<%= props.getProperty("Q8QuiteABit", "") %> /></td>
							<td width="45%">Quite a bit</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q8Slightly"
								<%= props.getProperty("Q8Slightly", "") %> /></td>
							<td width="45%">A little bit</td>
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q8Extremely"
								<%= props.getProperty("Q8Extremely", "") %> /></td>
							<td width="45%">Extremely</td>
						</tr>
						<tr bgcolor="white">
							<td width="5%" align="right"><input type="checkbox"
								class="bigCheckbox" name="Q8Moderately"
								<%= props.getProperty("Q8Moderately", "") %> /></td>
							<td width="45%">Moderately</td>
							<td width="5%"></td>
							<td width="45%"></td>
						</tr>
						<tr bgcolor="white">
							<td></td>
							<td colspan="3">Comments: <input type="text" class="textbox"
								class="textbox" size="80" name="Q8Cmt"
								value="<%= props.getProperty("Q8Cmt", "") %>" /></td>
						</tr>
						<tr>
							<td>
							<table border="0" cellspacing="0" cellpadding="0" height="100px">
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
						<tr>
							<th width="2%" valign="top">9.</th>
							<th valign="top" class="question">These questions are about
							how you feel and how things have been with you <font
								style="text-decoration: underline">during the past month</font>.
							For each question, please give the one answer that comes closest
							to the way you have been feeling. How much of the time during the
							<font style="text-decoration: underline">past 4 weeks</font>...</th>
						</tr>
						<tr>
							<td valign="top" colspan="2">
							<table>
								<tr class="question">
									<th width="2%"></th>
									<th width="35%"></th>
									<th width="8%" valign="top" align="center">All of the Time</th>
									<th width="8%" valign="top" align="center">Most of the
									Time</th>
									<th width="8%" valign="top" align="center">Some the Time</th>
									<th width="8%" valign="top" align="center">A Little of the
									Time</th>
									<th width="8%" valign="top" align="center">None of the
									Time</th>
									<th width="22%" valign="top" align="center">Comments</th>
								</tr>
								<tr>
									<td>a.</td>
									<td>Did you feel full of life?</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9aAll"
										<%= props.getProperty("Q9aAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9aMost"
										<%= props.getProperty("Q9aMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9aSome"
										<%= props.getProperty("Q9aSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9aLittle"
										<%= props.getProperty("Q9aLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9aNone"
										<%= props.getProperty("Q9aNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q9aCmt"><%= props.getProperty("Q9aCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="top">b.</td>
									<td>Have you been a very nervous person?</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9bAll"
										<%= props.getProperty("Q9bAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9bMost"
										<%= props.getProperty("Q9bMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9bSome"
										<%= props.getProperty("Q9bSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9bLittle"
										<%= props.getProperty("Q9bLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9bNone"
										<%= props.getProperty("Q9bNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q9bCmt"><%= props.getProperty("Q9bCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="top">c.</td>
									<td>Have you felt so down in the dumps nothing could cheer
									you up?</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9cAll"
										<%= props.getProperty("Q9cAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9cMost"
										<%= props.getProperty("Q9cMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9cSome"
										<%= props.getProperty("Q9cSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9cLittle"
										<%= props.getProperty("Q9cLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9cNone"
										<%= props.getProperty("Q9cNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q9cCmt"><%= props.getProperty("Q9cCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="top">d.</td>
									<td>Have you felt calm and peaceful?</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9dAll"
										<%= props.getProperty("Q9dAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9dMost"
										<%= props.getProperty("Q9dMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9dSome"
										<%= props.getProperty("Q9dSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9dLittle"
										<%= props.getProperty("Q9dLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9dNone"
										<%= props.getProperty("Q9dNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q9dCmt"><%= props.getProperty("Q9dCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td>e.</td>
									<td>Did you have a lot of energy?</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9eAll"
										<%= props.getProperty("Q9eAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9eMost"
										<%= props.getProperty("Q9eMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9eSome"
										<%= props.getProperty("Q9eSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9eLittle"
										<%= props.getProperty("Q9eLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9eNone"
										<%= props.getProperty("Q9eNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q9eCmt"><%= props.getProperty("Q9eCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="top">f.</td>
									<td>Have you felt downhearted and blue?</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9fAll"
										<%= props.getProperty("Q9fAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9fMost"
										<%= props.getProperty("Q9fMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9fSome"
										<%= props.getProperty("Q9fSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9fLittle"
										<%= props.getProperty("Q9fLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9fNone"
										<%= props.getProperty("Q9fNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q9fCmt"><%= props.getProperty("Q9fCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td>g.</td>
									<td>Did you feel worn out?</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9gAll"
										<%= props.getProperty("Q9gAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9gMost"
										<%= props.getProperty("Q9gMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9gSome"
										<%= props.getProperty("Q9gSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9gLittle"
										<%= props.getProperty("Q9gLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9gNone"
										<%= props.getProperty("Q9gNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q9gCmt"><%= props.getProperty("Q9gCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td>h.</td>
									<td>Have you been a happy person?</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9hAll"
										<%= props.getProperty("Q9hAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9hMost"
										<%= props.getProperty("Q9hMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9hSome"
										<%= props.getProperty("Q9hSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9hLittle"
										<%= props.getProperty("Q9hLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9hNone"
										<%= props.getProperty("Q9hNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q9hCmt"><%= props.getProperty("Q9hCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td>i.</td>
									<td>Did you feel tired?</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9iAll"
										<%= props.getProperty("Q9iAll", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9iMost"
										<%= props.getProperty("Q9iMost", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9iSome"
										<%= props.getProperty("Q9iSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9iLittle"
										<%= props.getProperty("Q9iLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q9iNone"
										<%= props.getProperty("Q9iNone", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="15" rows="2" name="Q9iCmt"><%= props.getProperty("Q9iCmt", "") %></textarea>
									</td>
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
						<tr>
							<th width="3%" valign="top">10.</th>
							<th valign="top" class="question"><font
								style="text-decoration: underline">During the past month</font>,
							How much of the time has your <font
								style="text-decoration: underline">physical health or
							emotional problems</font> interfered with your social activities (like
							visiting friends, relatives, etc.)?</th>
						</tr>
						<tr>
							<td valign="top" colspan="2">
							<table>
								<tr class="question">
									<th width="3%"></th>
									<th width="14%" valign="top" align="center">All of the
									Time</th>
									<th width="14%" valign="top" align="center">Most of the
									Time</th>
									<th width="14%" valign="top" align="center">Some the Time</th>
									<th width="14%" valign="top" align="center">A Little of
									the Time</th>
									<th width="14%" valign="top" align="center">None of the
									Time</th>
									<th width="27%" valign="top" align="center">Comments</th>
								</tr>
								<tr>
									<td></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q10All"
										<%= props.getProperty("Q10All", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q10Most"
										<%= props.getProperty("Q10Most", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q10Some"
										<%= props.getProperty("Q10Some", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q10Little"
										<%= props.getProperty("Q10Little", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q10None"
										<%= props.getProperty("Q10None", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q10Cmt"><%= props.getProperty("Q10Cmt", "") %></textarea>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<th width="3%" valign="top">11.</th>
							<th valign="top" class="question">Please choose the answer
							that best describes how <font style="text-decoration: underline">true</font>
							or <font style="text-decoration: underline">false</font> each of
							the following statements is for you.</th>
						</tr>
						<tr>
							<td valign="top" colspan="2">
							<table>
								<tr class="question">
									<th width="3%"></th>
									<th width="30%"></th>
									<th width="8%" valign="top" align="center">Definitely True</th>
									<th width="8%" valign="top" align="center">Mostly True</th>
									<th width="8%" valign="top" align="center">Don't Know</th>
									<th width="8%" valign="top" align="center">Mostly False</th>
									<th width="8%" valign="top" align="center">Definitely
									False</th>
									<th width="27%" valign="top" align="center">Comments</th>
								</tr>
								<tr>
									<td valign="top">a.</td>
									<td>I seem to get sick a little easier than other people.
									</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11aDefTrue"
										<%= props.getProperty("Q11aDefTrue", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11aMostTrue"
										<%= props.getProperty("Q11aMostTrue", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11aNotSure"
										<%= props.getProperty("Q11aNotSure", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11aMostFalse"
										<%= props.getProperty("Q11aMostFalse", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11aDefFalse"
										<%= props.getProperty("Q11aDefFalse", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q11aCmt"><%= props.getProperty("Q11aCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="top">b.</td>
									<td>I am as healthy as anybody I know.</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11bDefTrue"
										<%= props.getProperty("Q11bDefTrue", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11bMostTrue"
										<%= props.getProperty("Q11bMostTrue", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11bNotSure"
										<%= props.getProperty("Q11bNotSure", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11bMostFalse"
										<%= props.getProperty("Q11bMostFalse", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11bDefFalse"
										<%= props.getProperty("Q11bDefFalse", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q11bCmt"><%= props.getProperty("Q11bCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td valign="top">c.</td>
									<td>I expect my health to get worse.</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11cDefTrue"
										<%= props.getProperty("Q11cDefTrue", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11cMostTrue"
										<%= props.getProperty("Q11cMostTrue", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11cNotSure"
										<%= props.getProperty("Q11cNotSure", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11cMostFalse"
										<%= props.getProperty("Q11cMostFalse", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11cDefFalse"
										<%= props.getProperty("Q11cDefFalse", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q11cCmt"><%= props.getProperty("Q11cCmt", "") %></textarea>
									</td>
								</tr>
								<tr>
									<td>d.</td>
									<td>My health is excellent.</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11dDefTrue"
										<%= props.getProperty("Q11dDefTrue", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11dMostTrue"
										<%= props.getProperty("Q11dMostTrue", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11dNotSure"
										<%= props.getProperty("Q11dNotSure", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11dMostFalse"
										<%= props.getProperty("Q11dMostFalse", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q11dDefFalse"
										<%= props.getProperty("Q11dDefFalse", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q11dCmt"><%= props.getProperty("Q11dCmt", "") %></textarea>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<th width="3%" valign="top">12.</th>
							<th valign="top" class="question">Regardless of your current
							level of sexual activity, please answer the following question.
							If you prefer not to answer it, please check this box: <input
								type="checkbox" class="bigCheckbox" name="Q12aNotAns"
								<%= props.getProperty("Q12aNotAns", "") %> /></th>
						</tr>
						<tr>
							<td valign="top" colspan="2">
							<table>
								<tr class="question">
									<th width="3%"></th>
									<th width="30%"></th>
									<th width="8%" valign="top" align="center">Not at all</th>
									<th width="8%" valign="top" align="center">A little bit</th>
									<th width="8%" valign="top" align="center">Some what</th>
									<th width="8%" valign="top" align="center">Quite a lot</th>
									<th width="8%" valign="top" align="center">Very much</th>
									<th width="27%" valign="top" align="center">Comments</th>
								</tr>
								<tr>
									<td valign="center">a.</td>
									<td>I am satisfied with my sex life</td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q12aNot"
										<%= props.getProperty("Q12aNot", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q12aLittle"
										<%= props.getProperty("Q12aLittle", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q12aSome"
										<%= props.getProperty("Q12aSome", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q12aLot"
										<%= props.getProperty("Q12aLot", "") %> /></td>
									<td align="center" bgcolor="white"><input type="checkbox"
										class="bigCheckbox" name="Q12aMuch"
										<%= props.getProperty("Q12aMuch", "") %> /></td>
									<td align="center" bgcolor="white"><textarea
										class="textbox" cols="20" rows="2" name="Q12aCmt"><%= props.getProperty("Q12aCmt", "") %></textarea>
									</td>
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
