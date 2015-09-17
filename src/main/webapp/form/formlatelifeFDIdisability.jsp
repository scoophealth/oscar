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
    String formClass = "LateLifeFDIDisability";
    String formLink = "formlatelifeFDIdisability.jsp";

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
<title>Late Life FDI: Disability component</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">
    var choiceFormat  = new Array(6,10,11,15,16,20,21,25,26,30,31,35,36,40,41,45,
                                  46,50,51,55,56,60,61,65,66,70,71,75,76,80,81,85,
                                  86,90,91,95,96,100,101,105,106,110,111,115,116,120,121,125,
                                  126,130,131,135,136,140,141,145,146,150,151,155,156,160,161,165);
    var allNumericField = null;     
    var allMatch = null;
    var action = "/<%=project_home%>/form/formname.do";

    function goToInstructions(){
        document.getElementById('instruction').style.display = 'block';
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none';  
        document.getElementById('page3').style.display = 'none';
        document.getElementById('functionBar').style.display = 'none';
        document.getElementById('copyRight').style.display = 'block';
    }

    function goToVisualAid1(){
        var vheight = 500;
        var vwidth = 640;
        var windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
        window.open("formlatelifedisabilityvisualAid1.jsp","", windowprops);
    }

    function goToVisualAid2(){
        var vheight = 768;
        var vwidth = 640;
        var windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
        window.open("formlatelifedisabilityvisualAid2.jsp","", windowprops);
    }

    function goToPage1(){             
            document.getElementById('instruction').style.display = 'none';
            document.getElementById('page1').style.display = 'block';
            document.getElementById('page2').style.display = 'none';  
            document.getElementById('page3').style.display = 'none';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
    }
    
    function backToPage1(){             
            document.getElementById('instruction').style.display = 'none';
            document.getElementById('page1').style.display = 'block';
            document.getElementById('page2').style.display = 'none';  
            document.getElementById('page3').style.display = 'none';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
    }
    
    function goToPage2(){      
        var checkboxes = new Array(6,10,11,15,16,20,21,25,26,30,31,35,36,40,41,45,46,50,51,55,56,60,61,65);
        if (is1CheckboxChecked(0, checkboxes)==true && isFormCompleted(6,65,12,0)==true){
            document.getElementById('instruction').style.display = 'none';
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'block'; 
            document.getElementById('page3').style.display = 'none';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
        }
    }

    function backToPage2(){
        document.getElementById('instruction').style.display = 'none';
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'block'; 
        document.getElementById('page3').style.display = 'none';
        document.getElementById('functionBar').style.display = 'block';
        document.getElementById('copyRight').style.display = 'none';
    }
    
    function goToPage3(){      
        var checkboxes = new Array(66,70,71,75,76,80,81,85,86,90,91,95,96,100,101,105,106,110,111,115);        
        if (is1CheckboxChecked(0, checkboxes)==true  && isFormCompleted(66,115,10,0)==true){
            document.getElementById('instruction').style.display = 'none';
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'block';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
        }
    }    
     
    function checkBeforeSave(){                
        if(document.getElementById('page3').style.display=='block'){
            if(isFormCompleted(116,165,10,0)==true)
                return true;
        }    
        else{
            if(isFormCompleted(6,65,12,0)==true && isFormCompleted(66,115,10,0)==true && isFormCompleted(116,165,10,0)==true)
                return true;
        }            
        
        return false;
    }
</script>
<script type="text/javascript" src="formScripts.js">          
</script>


<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0"
	onload="window.resizeTo(768,748)">
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

	<table border="0" cellspacing="1" cellpadding="0" width="735px"
		height="95%">
		<tr>
			<td valign="top" colspan="2">
			<table border="0" cellspacing="0" cellpadding="0" width="735px"
				height="10%">
				<tr>
					<th class="lefttopCell" width="17%">&nbsp;</th>
					<th class="subject">Late Life FDI: Disability Component</th>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td bgcolor="#A9A9A9" valign="top" align="center" width="17%"
				style="border-right: 2px solid #A9A9A9;">
			<table>
				<tr>
					<td class="leftcol"><a href="javascript: goToInstructions();">Instructions</a></td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td class="leftcol"><a href="javascript: goToVisualAid1();"
						title="for core questions">Visual Aid 1</a></td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td class="leftcol"><a href="javascript: goToVisualAid2();"
						title="for additional device questions">Visual Aid 2</a></td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td class="leftcol"><a href="javascript: goToPage1();">Questionnaire</a></td>
				</tr>
			</table>
			</td>
			<td width="83%">
			<table border="0" cellspacing="0" cellpadding="0" height="100%"
				width="100%">
				<tr>
					<td valign="top">
					<table border="0" cellspacing="0" cellpadding="0" height="85%"
						width="100%" id="instruction">
						<tr>
							<td valign="top" colspan="2">
							<table width="100%" height="650px" border="0" cellspacing="0"
								cellpadding="0">
								<tr class="title">
									<th colspan="6">Instruction for Disability Questions</th>
								</tr>
								<tr>
									<td>In this set of questions, I will ask you about
									everyday things you do at this time in your life. <br>
									There are <font style="text-decoration: underline">two</font>
									parts to each question. <br>
									First, I will ask you <font style="font-style: italic">How
									often</font> you do a certain activity. <br>
									Next, I will ask you <font style="font-style: italic">To
									what extent do you feel limited</font> in doing this activity. <br>
									</td>
								</tr>
								<tr>
									<td>
									<table height="10px">
										<tr>
											<td></td>
										</tr>
									</table>
									</td>
								<tr>
								<tr>
									<td valign="center" align="center">
									<table style="border: 1px solid #000000" cellpadding="2"
										width="90%" height="55%">
										<tr>
											<td width="3%"></td>
											<td class="instruction" colspan="2"><font
												style="font-weight: bold">Explain each question and
											subsequent answer options:</font> <br>
											<br>
											For the first question (<font style="font-style: italic">How
											often do you do the activity?</font>), please choose from these
											answers:</td>
										</tr>
										<tr>
											<td></td>
											<td width="10%"></td>
											<td class="instruction"><font style="font-weight: bold">Very
											often</font></td>
										</tr>
										<tr>
											<td></td>
											<td width="10%"></td>
											<td class="instruction"><font style="font-weight: bold">Often</font></td>
										</tr>
										<tr>
											<td></td>
											<td width="10%"></td>
											<td class="instruction"><font style="font-weight: bold">Once
											in a while</font></td>
										</tr>
										<tr>
											<td></td>
											<td width="10%"></td>
											<td class="instruction"><font style="font-weight: bold">Almost
											never</font></td>
										</tr>
										<tr>
											<td></td>
											<td width="10%"></td>
											<td class="instruction"><font style="font-weight: bold">Never</font></td>
										</tr>
										<tr>
											<td></td>
											<td class="instruction" colspan="2">[Show visual aid to
											interviewee]</td>
										</tr>
										<tr>
											<td></td>
										</tr>
										<tr>
											<td width="3%"></td>
											<td class="instruction" colspan="2">For the second
											question (<font style="font-style: italic">To what
											extent do you feel limited in doing the activity?</font>), please
											choose from these answers:</td>
										</tr>
										<tr>
											<td></td>
											<td width="10%"></td>
											<td class="instruction"><font style="font-weight: bold">Not
											at all</font></td>
										</tr>
										<tr>
											<td></td>
											<td width="10%"></td>
											<td class="instruction"><font style="font-weight: bold">A
											little</font></td>
										</tr>
										<tr>
											<td></td>
											<td width="10%"></td>
											<td class="instruction"><font style="font-weight: bold">Somewhat</font></td>
										</tr>
										<tr>
											<td></td>
											<td width="10%"></td>
											<td class="instruction"><font style="font-weight: bold">A
											lot</font></td>
										</tr>
										<tr>
											<td></td>
											<td width="10%"></td>
											<td class="instruction"><font style="font-weight: bold">Completely</font></td>
										</tr>
										<tr>
											<td></td>
											<td class="instruction" colspan="2">[Show visual aid to
											interviewee]</td>
										</tr>
										<tr>
											<td></td>
										</tr>
										<tr>
											<td></td>
											<td class="instruction" colspan="2">For example, you
											might feel limited because of your health, or because it
											takes a lot of mental and physical energy. Please keep in
											mind that you can also feel limited by factors outside of
											yourself. Your environment could restrict you from doing the
											things; for instance, transportation issues, accessibility,
											and social or economic circumstances could limit you from
											doing things you would like to do. Think of all these factors
											when you answer this section.</td>
										</tr>
									</table>
									</td>
								</tr>
								<tr>
									<td>
									<table height="10px">
										<tr>
											<td></td>
										</tr>
									</table>
									</td>
								<tr>
								<tr>
									<td>For each question, please select the one answer that
									comes closest to the way you have been feeling. <br>
									<br>
									</td>
								</tr>
								<tr>
									<td>Let's begin...</td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					<table border="0" cellspacing="0" cellpadding="0"
						style="display: none" height="85%" width="100%" id="page1">
						<tr>
							<td valign="top" colspan="2">
							<table width="100%" height="612px" border="0" cellspacing="1px"
								cellpadding="0">
								<tr class="title">
									<th colspan="2" rowspan="2"></th>
									<th colspan="5" style="border-right: 2px solid #F2F2F2">How
									often Do you...?</th>
									<th colspan="5">To what extent do you feel limit in...?</th>
								</tr>
								<tr class="title">
									<td width="6%"><font style="font-size: 60%;">Very
									Often</font></td>
									<td width="6%"><font style="font-size: 60%;">Often</font></td>
									<td width="6%"><font style="font-size: 60%;">Once
									in a while</font></td>
									<td width="6%"><font style="font-size: 60%;">Almost
									never</font></td>
									<td width="6%" style="border-right: 2px solid #F2F2F2"><font
										style="font-size: 60%;">Never</font></td>
									<td width="6%"><font style="font-size: 60%;">Not at
									all</font></td>
									<td width="6%"><font style="font-size: 60%;">A
									little</font></td>
									<td width="6%"><font style="font-size: 60%;">Somewhat</font></td>
									<td width="6%"><font style="font-size: 60%;">A lot</font></td>
									<td width="6%"><font style="font-size: 60%;">Completely</font></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D1.</td>
									<td class="question" valign="top" width="33%">Keep
									(Keeping) in touch with others through letters, phone, or email
									</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D1VeryOften"
										<%= props.getProperty("D1VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D1Often"
										<%= props.getProperty("D1Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D1OnceInAWhile"
										<%= props.getProperty("D1OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D1AlmostNever"
										<%= props.getProperty("D1AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D1Never"
										<%= props.getProperty("D1Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D1Not"
										<%= props.getProperty("D1Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D1Little"
										<%= props.getProperty("D1Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D1Somewhat"
										<%= props.getProperty("D1Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D1Alot"
										<%= props.getProperty("D1Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D1Completely"
										<%= props.getProperty("D1Completely", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D2.</td>
									<td class="question" valign="top">Visit (Visiting)
									friends and family in their homes.</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D2VeryOften"
										<%= props.getProperty("D2VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D2Often"
										<%= props.getProperty("D2Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D2OnceInAWhile"
										<%= props.getProperty("D2OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D2AlmostNever"
										<%= props.getProperty("D2AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D2Never"
										<%= props.getProperty("D2Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D2Not"
										<%= props.getProperty("D2Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D2Little"
										<%= props.getProperty("D2Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D2Somewhat"
										<%= props.getProperty("D2Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D2Alot"
										<%= props.getProperty("D2Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D2Completely"
										<%= props.getProperty("D2Completely", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D3.</td>
									<td class="question" valign="top">Provide (Providing)
									care or assistance to others. <font class="instruction">This
									may include providing personal care, transportation, and
									running errands for family members or friends.</font></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D3VeryOften"
										<%= props.getProperty("D3VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D3Often"
										<%= props.getProperty("D3Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D3OnceInAWhile"
										<%= props.getProperty("D3OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D3AlmostNever"
										<%= props.getProperty("D3AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D3Never"
										<%= props.getProperty("D3Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D3Not"
										<%= props.getProperty("D3Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D3Little"
										<%= props.getProperty("D3Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D3Somewhat"
										<%= props.getProperty("D3Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D3Alot"
										<%= props.getProperty("D3Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D3Completely"
										<%= props.getProperty("D3Completely", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D4.</td>
									<td class="question" valign="top">Take (Taking) care of
									the inside of your home. <font class="instruction">This
									includes managing and taking responsibility for home making,
									laundry, housecleaning and minor household repairs.</font></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D4VeryOften"
										<%= props.getProperty("D4VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D4Often"
										<%= props.getProperty("D4Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D4OnceInAWhile"
										<%= props.getProperty("D4OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D4AlmostNever"
										<%= props.getProperty("D4AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D4Never"
										<%= props.getProperty("D4Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D4Not"
										<%= props.getProperty("D4Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D4Little"
										<%= props.getProperty("D4Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D4Somewhat"
										<%= props.getProperty("D4Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D4Alot"
										<%= props.getProperty("D4Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D4Completely"
										<%= props.getProperty("D4Completely", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D5.</td>
									<td class="question" valign="top" width="35%">Work
									(Working) at a volunteer job outside your home.</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D5VeryOften"
										<%= props.getProperty("D5VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D5Often"
										<%= props.getProperty("D5Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D5OnceInAWhile"
										<%= props.getProperty("D5OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D5AlmostNever"
										<%= props.getProperty("D5AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D5Never"
										<%= props.getProperty("D5Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D5Not"
										<%= props.getProperty("D5Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D5Little"
										<%= props.getProperty("D5Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D5Somewhat"
										<%= props.getProperty("D5Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D5Alot"
										<%= props.getProperty("D5Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D5Completely"
										<%= props.getProperty("D5Completely", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D6.</td>
									<td class="question" valign="top">Take (Taking) part in
									active recreation. <font class="instruction">This may
									include bowling, golf, tennis, hiking, jogging, or swimming.</font></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D6VeryOften"
										<%= props.getProperty("D6VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D6Often"
										<%= props.getProperty("D6Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D6OnceInAWhile"
										<%= props.getProperty("D6OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D6AlmostNever"
										<%= props.getProperty("D6AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D6Never"
										<%= props.getProperty("D6Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D6Not"
										<%= props.getProperty("D6Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D6Little"
										<%= props.getProperty("D6Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D6Somewhat"
										<%= props.getProperty("D6Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D6Alot"
										<%= props.getProperty("D6Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D6Completely"
										<%= props.getProperty("D6Completely", "") %> /></td>
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
						style="display: none" width="100%" height="85%" id="page2">
						<tr>
							<td valign="top" colspan="2">
							<table width="100%" height="612px" border="0" cellspacing="1px"
								cellpadding="0">
								<tr class="title">
									<th colspan="2" rowspan="2"></th>
									<th colspan="5" style="border-right: 2px solid #F2F2F2">How
									often Do you...?</th>
									<th colspan="5">To what extent do you feel limit in...?</th>
								</tr>
								<tr class="title">
									<td width="6%"><font style="font-size: 60%;">Very
									Often</font></td>
									<td width="6%"><font style="font-size: 60%;">Often</font></td>
									<td width="6%"><font style="font-size: 60%;">Once
									in a while</font></td>
									<td width="6%"><font style="font-size: 60%;">Almost
									never</font></td>
									<td width="6%" style="border-right: 2px solid #F2F2F2"><font
										style="font-size: 60%;">Never</font></td>
									<td width="6%"><font style="font-size: 60%;">Not at
									all</font></td>
									<td width="6%"><font style="font-size: 60%;">A
									little</font></td>
									<td width="6%"><font style="font-size: 60%;">Somewhat</font></td>
									<td width="6%"><font style="font-size: 60%;">A lot</font></td>
									<td width="6%"><font style="font-size: 60%;">Completely</font></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D7.</td>
									<td class="question" valign="top" width="33%">Take
									(Taking) care of household business and finances. <font
										class="instruction">This may include managing and
									taking responsibility for your money, paying bills, dealing
									with a landlord or tenants, dealing with utility companies or
									governmental agencies.</font></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D7VeryOften"
										<%= props.getProperty("D7VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D7Often"
										<%= props.getProperty("D7Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D7OnceInAWhile"
										<%= props.getProperty("D7OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D7AlmostNever"
										<%= props.getProperty("D7AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D7Never"
										<%= props.getProperty("D7Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D7Not"
										<%= props.getProperty("D7Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D7Little"
										<%= props.getProperty("D7Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D7Somewhat"
										<%= props.getProperty("D7Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D7Alot"
										<%= props.getProperty("D7Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D7Completely"
										<%= props.getProperty("D7Completely", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D8.</td>
									<td class="question" valign="top">Take (Taking) care of
									your own health. <font class="instruction">This may
									include managing daily medications, following a special diet,
									scheduling doctor's appointments.</font></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D8VeryOften"
										<%= props.getProperty("D8VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D8Often"
										<%= props.getProperty("D8Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D8OnceInAWhile"
										<%= props.getProperty("D8OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D8AlmostNever"
										<%= props.getProperty("D8AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D8Never"
										<%= props.getProperty("D8Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D8Not"
										<%= props.getProperty("D8Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D8Little"
										<%= props.getProperty("D8Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D8Somewhat"
										<%= props.getProperty("D8Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D8Alot"
										<%= props.getProperty("D8Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D8Completely"
										<%= props.getProperty("D8Completely", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D9.</td>
									<td class="question" valign="top">Travel (Traveling) out
									of town for at least an overnight stay.</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D9VeryOften"
										<%= props.getProperty("D9VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D9Often"
										<%= props.getProperty("D9Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D9OnceInAWhile"
										<%= props.getProperty("D9OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D9AlmostNever"
										<%= props.getProperty("D9AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D9Never"
										<%= props.getProperty("D9Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D9Not"
										<%= props.getProperty("D9Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D9Little"
										<%= props.getProperty("D9Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D9Somewhat"
										<%= props.getProperty("D9Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D9Alot"
										<%= props.getProperty("D9Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D9Completely"
										<%= props.getProperty("D9Completely", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D10.</td>
									<td class="question" valign="top">Take (Taking) part in a
									regular fitness program. <font class="instruction">This
									may include walking for exercise, stationary biking, weight
									lifting, or exercise classes.</font></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D10VeryOften"
										<%= props.getProperty("D10VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D10Often"
										<%= props.getProperty("D10Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D10OnceInAWhile"
										<%= props.getProperty("D10OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D10AlmostNever"
										<%= props.getProperty("D10AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D10Never"
										<%= props.getProperty("D10Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D10Not"
										<%= props.getProperty("D10Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D10Little"
										<%= props.getProperty("D10Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D10Somewhat"
										<%= props.getProperty("D10Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D10Alot"
										<%= props.getProperty("D10Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D10Completely"
										<%= props.getProperty("D10Completely", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D11.</td>
									<td class="question" valign="top">Invite (Inviting)
									people into your home for a meal or entertainment.</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D11VeryOften"
										<%= props.getProperty("D11VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D11Often"
										<%= props.getProperty("D11Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D11OnceInAWhile"
										<%= props.getProperty("D11OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D11AlmostNever"
										<%= props.getProperty("D11AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D11Never"
										<%= props.getProperty("D11Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D11Not"
										<%= props.getProperty("D11Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D11Little"
										<%= props.getProperty("D11Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D11Somewhat"
										<%= props.getProperty("D11Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D11Alot"
										<%= props.getProperty("D11Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D11Completely"
										<%= props.getProperty("D11Completely", "") %> /></td>
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
						style="display: none" width="100%" height="85%" id="page3">
						<tr>
							<td valign="top" colspan="2">
							<table width="100%" height="612px" border="0" cellspacing="1px"
								cellpadding="0">
								<tr class="title">
									<th colspan="2" rowspan="2"></th>
									<th colspan="5" style="border-right: 2px solid #F2F2F2">How
									often Do you...?</th>
									<th colspan="5">To what extent do you feel limit in...?</th>
								</tr>
								<tr class="title">
									<td width="6%"><font style="font-size: 60%;">Very
									Often</font></td>
									<td width="6%"><font style="font-size: 60%;">Often</font></td>
									<td width="6%"><font style="font-size: 60%;">Once
									in a while</font></td>
									<td width="6%"><font style="font-size: 60%;">Almost
									never</font></td>
									<td width="6%" style="border-right: 2px solid #F2F2F2"><font
										style="font-size: 60%;">Never</font></td>
									<td width="6%"><font style="font-size: 60%;">Not at
									all</font></td>
									<td width="6%"><font style="font-size: 60%;">A
									little</font></td>
									<td width="6%"><font style="font-size: 60%;">Somewhat</font></td>
									<td width="6%"><font style="font-size: 60%;">A lot</font></td>
									<td width="6%"><font style="font-size: 60%;">Completely</font></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D12.</td>
									<td class="question" valign="top" width="33%">Go (Going)
									out with others to public places such as restaurants or movies.
									</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D12VeryOften"
										<%= props.getProperty("D12VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D12Often"
										<%= props.getProperty("D12Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D12OnceInAWhile"
										<%= props.getProperty("D12OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D12AlmostNever"
										<%= props.getProperty("D12AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D12Never"
										<%= props.getProperty("D12Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D12Not"
										<%= props.getProperty("D12Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D12Little"
										<%= props.getProperty("D12Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D12Somewhat"
										<%= props.getProperty("D12Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D12Alot"
										<%= props.getProperty("D12Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D12Completely"
										<%= props.getProperty("D12Completely", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D13.</td>
									<td class="question" valign="top">Take (Taking) care of
									your own personal care needs. <font class="instruction">
									This includes bathing, dressing, and toileting.</font></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D13VeryOften"
										<%= props.getProperty("D13VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D13Often"
										<%= props.getProperty("D13Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D13OnceInAWhile"
										<%= props.getProperty("D13OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D13AlmostNever"
										<%= props.getProperty("D13AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D13Never"
										<%= props.getProperty("D13Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D13Not"
										<%= props.getProperty("D13Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D13Little"
										<%= props.getProperty("D13Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D13Somewhat"
										<%= props.getProperty("D13Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D13Alot"
										<%= props.getProperty("D13Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D13Completely"
										<%= props.getProperty("D13Completely", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D14.</td>
									<td class="question" valign="top">Take (Taking) part in
									organized social activities. <font class="instruction">
									This may include clubs, card playing, senior center events,
									community or religious groups.</font></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D14VeryOften"
										<%= props.getProperty("D14VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D14Often"
										<%= props.getProperty("D14Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D14OnceInAWhile"
										<%= props.getProperty("D14OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D14AlmostNever"
										<%= props.getProperty("D14AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D14Never"
										<%= props.getProperty("D14Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D14Not"
										<%= props.getProperty("D14Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D14Little"
										<%= props.getProperty("D14Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D14Somewhat"
										<%= props.getProperty("D14Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D14Alot"
										<%= props.getProperty("D14Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D14Completely"
										<%= props.getProperty("D14Completely", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D15.</td>
									<td class="question" valign="top">Take (Taking) care of
									local errands. <font class="instruction"> This may
									include managing and taking responsibility for shopping for
									food and personal items, and going to the bank, library, or dry
									cleaner.</font></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D15VeryOften"
										<%= props.getProperty("D15VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D15Often"
										<%= props.getProperty("D15Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D15OnceInAWhile"
										<%= props.getProperty("D15OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D15AlmostNever"
										<%= props.getProperty("D15AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D15Never"
										<%= props.getProperty("D15Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D15Not"
										<%= props.getProperty("D15Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D15Little"
										<%= props.getProperty("D15Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D15Somewhat"
										<%= props.getProperty("D15Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D15Alot"
										<%= props.getProperty("D15Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D15Completely"
										<%= props.getProperty("D15Completely", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">D16.</td>
									<td class="question" valign="top">Prepare (Preparing)
									meals for yourself. <font class="instruction"> This
									includes planning, cooking, serving, and cleaning up.</font></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D16VeryOften"
										<%= props.getProperty("D16VeryOften", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D16Often"
										<%= props.getProperty("D16Often", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D16OnceInAWhile"
										<%= props.getProperty("D16OnceInAWhile", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D16AlmostNever"
										<%= props.getProperty("D16AlmostNever", "") %> /></td>
									<td bgcolor="white" align="center"
										style="border-right: 2px solid #F2F2F2"><input
										type="checkbox" class="checkbox" name="D16Never"
										<%= props.getProperty("D16Never", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D16Not"
										<%= props.getProperty("D16Not", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D16Little"
										<%= props.getProperty("D16Little", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D16Somewhat"
										<%= props.getProperty("D16Somewhat", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D16Alot"
										<%= props.getProperty("D16Alot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="D16Completely"
										<%= props.getProperty("D16Completely", "") %> /></td>
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
					<table class="Head" style="display: none" width="100%" height="15%"
						id="functionBar">
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
						<tr>
							<td><font style="font-size: 70%">&copy; Copyright
							2002 Trustees of Boston University, All Right Reserved</font></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td valign="top">
					<table class="Head" valign="bottom" width="100%" height="15%"
						id="copyRight">
						<tr>
							<td><font style="font-size: 70%">&copy; Copyright
							2002 Trustees of Boston University, All Right Reserved</font></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>
</body>
</html:html>
