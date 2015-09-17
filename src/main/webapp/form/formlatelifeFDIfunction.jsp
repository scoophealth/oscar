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
    String formClass = "LateLifeFDIFunction";
    String formLink = "formlatelifeFDIfunction.jsp";

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
<title>Late Life FDI: Function component</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>


<script type="text/javascript" language="Javascript">
    var choiceFormat  = new Array(6,10,11,15,16,20,21,25,26,30,31,35,36,40,41,45,
                                  46,50,51,55,56,60,61,65,66,70,71,75,76,80,81,85,
                                  86,90,91,95,96,100,101,105,106,110,111,115,116,120,121,125,
                                  126,130,131,135,136,140,141,145,146,150,151,155,156,160,161,165,
                                  167,171,172,176,177,181,182,186,187,191,192,196,197,201,202,206);
    var allNumericField = null;     
    var allMatch = null;
    var action = "/<%=project_home%>/form/formname.do";
    var totalScore = 0;

    function goToInstructions(){
        document.getElementById('instruction').style.display = 'block';
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none';  
        document.getElementById('page3').style.display = 'none';
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none';
        document.getElementById('totalScore').style.display = 'none'; 
        document.getElementById('subject2').style.display = 'none';
        document.getElementById('functionBar').style.display = 'none';
        document.getElementById('copyRight').style.display = 'block';
    }

    function goToVisualAid1(){       
        var vheight = 768;
        var vwidth = 640;
        var page = "formlatelifefunctionvisualAid1.jsp";
        windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
        window.open(page,"",windowprops);
    }

    function goToVisualAid2(){        
        var vheight = 768;
        var vwidth = 640;
        var windowprops = "height=768,width=600,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
        window.open("formlatelifefunctionvisualAid2.jsp","",windowprops);
    }

    function goToPage1(){             
            document.getElementById('instruction').style.display = 'none';
            document.getElementById('page1').style.display = 'block';
            document.getElementById('page2').style.display = 'none';  
            document.getElementById('page3').style.display = 'none';
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none'; 
            document.getElementById('totalScore').style.display = 'none';
            document.getElementById('subject2').style.display = 'none';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
    }
    
    function backToPage1(){             
            document.getElementById('instruction').style.display = 'none';
            document.getElementById('page1').style.display = 'block';
            document.getElementById('page2').style.display = 'none';  
            document.getElementById('page3').style.display = 'none';
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none'; 
            document.getElementById('totalScore').style.display = 'none';
            document.getElementById('subject2').style.display = 'none';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
    }
    
    function goToPage2(){      
        var checkboxes = new Array(6,10,11,15,16,20,21,25,26,30,31,35,36,40,41,45);
        if (is1CheckboxChecked(0, checkboxes)==true  && isFormCompleted(6,45,8,0)==true){
            document.getElementById('instruction').style.display = 'none';
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'block'; 
            document.getElementById('page3').style.display = 'none'; 
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('totalScore').style.display = 'none';
            document.getElementById('subject2').style.display = 'none';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
        }
    }

    function backToPage2(){ 
        document.getElementById('instruction').style.display = 'none';
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'block'; 
        document.getElementById('page3').style.display = 'none'; 
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none';
        document.getElementById('totalScore').style.display = 'none';
        document.getElementById('subject2').style.display = 'none';
        document.getElementById('functionBar').style.display = 'block';
        document.getElementById('copyRight').style.display = 'none';
    }
    
    function goToPage3(){      
        var checkboxes = new Array(46,50,51,55,56,60,61,65,66,70,71,75,76,80,81,85);        
        if (is1CheckboxChecked(0, checkboxes)==true  && isFormCompleted(46,85,8,0)==true){
            document.getElementById('instruction').style.display = 'none';
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'block';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('totalScore').style.display = 'none';
            document.getElementById('subject2').style.display = 'none';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
        }
    }
    
    function backToPage3(){ 
        document.getElementById('instruction').style.display = 'none';
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none'; 
        document.getElementById('page3').style.display = 'block';  
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none';
        document.getElementById('totalScore').style.display = 'none';
        document.getElementById('subject2').style.display = 'none';
        document.getElementById('functionBar').style.display = 'block';
        document.getElementById('copyRight').style.display = 'none';
    }

    function goToPage4(){    
        var checkboxes = new Array(86,90,91,95,96,100,101,105,106,110,111,115,116,120,121,125);
        if (is1CheckboxChecked(0, checkboxes)==true  && isFormCompleted(86,125,8,0)==true){
            document.getElementById('instruction').style.display = 'none';
            //document.getElementById('visualAid1').style.display = 'none';
            //document.getElementById('visualAid2').style.display = 'none';
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'block';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('totalScore').style.display = 'none';
            document.getElementById('subject2').style.display = 'none';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
        }
    }

    function backToPage4(){ 
        document.getElementById('instruction').style.display = 'none';
        //document.getElementById('visualAid1').style.display = 'none';
        //document.getElementById('visualAid2').style.display = 'none';
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none'; 
        document.getElementById('page3').style.display = 'none';  
        document.getElementById('page4').style.display = 'block';
        document.getElementById('page5').style.display = 'none';
        document.getElementById('totalScore').style.display = 'none';
        document.getElementById('subject2').style.display = 'none';
        document.getElementById('functionBar').style.display = 'block';
        document.getElementById('copyRight').style.display = 'none';
    }
    
    function goToPage5(){      
        var checkboxes = new Array(126,130,131,135,136,140,141,145,146,150,151,155,156,160,161,165);
        if (is1CheckboxChecked(0, checkboxes)==true){
            document.getElementById('instruction').style.display = 'none';
            //document.getElementById('visualAid1').style.display = 'none';
            //document.getElementById('visualAid2').style.display = 'none';
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'block';
            document.getElementById('totalScore').style.display = 'none';
            //document.getElementById('subject2').style.display = 'block';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
        }
    }
    
    function backToPage5(){ 
        document.getElementById('instruction').style.display = 'none';
        //document.getElementById('visualAid1').style.display = 'none';
        //document.getElementById('visualAid2').style.display = 'none';
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none'; 
        document.getElementById('page3').style.display = 'none';  
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'block';
        document.getElementById('totalScore').style.display = 'none';
        //document.getElementById('subject2').style.display = 'block';
        document.getElementById('functionBar').style.display = 'block';
        document.getElementById('copyRight').style.display = 'none';
    }

    function goToScorePage(){      
        var checkboxes = new Array(126,130,131,135,136,140,141,145,146,150,151,155,156,160,161,165);
        if (is1CheckboxChecked(0, checkboxes)==true  && isFormCompleted(126,165,8,0)==true){
            document.getElementById('instruction').style.display = 'none';
            //document.getElementById('visualAid1').style.display = 'none';
            //document.getElementById('visualAid2').style.display = 'none';
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('totalScore').style.display = 'block';
            //document.getElementById('subject2').style.display = 'block';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
        }
    }

    function checkBeforeSave(){                
        if(document.getElementById('page5').style.display=='block'){
            if(isFormCompleted(166,205,8,0)==true)
                return true;
        }    
        else{
            if(isFormCompleted(6,45,8,0)==true && isFormCompleted(46,85,8,0)==true && isFormCompleted(86,125,8,0)==true && isFormCompleted(126,165,8,0)==true && isFormCompleted(166,205,8,0)==true)
                return true;
        }            
        
        return false;
    }
    
    function showSubtitle(){
        if(document.getElementById('questionnaire').style.display == 'block')
            document.getElementById('questionnaire').style.display = 'none';
        else 
            document.getElementById('questionnaire').style.display = 'block';
    }

    function calculateScore(){
        var nbElements = document.forms[0].elements.length - 45;        
        var element;
        var score = 0;
        for(var i=6; i<nbElements; i++){
            element = document.forms[0].elements[i]
            if(element.checked == true){
                if(element.name.match("None")=="None")
                    score = score + 5;
                else if(element.name.match("ALittle")=="ALittle")
                    score = score + 4;
                else if(element.name.match("Some")=="Some")
                    score = score + 3;
                else if(element.name.match("ALot")=="ALot")
                    score = score + 2;
                else if(element.name.match("Cannot")=="Cannot")
                    score = score + 1;
            }
        }        
        document.forms[0].score.value=score;
        goToScorePage();
    }
</script>
<script type="text/javascript" src="formScripts.js">          
</script>


<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0"
	onload="window.resizeTo(768,732)">
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
					<th class="subject">Late Life FDI: Function Component <br>
					<table style="display: none" id="subject2">
						<tr>
							<th class="subject" style="border: 0px">For those who use
							walking devices <br>
							<font style="font-size: 60%"> The following are questions
							only for people using canes, walkers, or other walking devices </font>.
							
							</td>
						</tr>
					</table>
					</th>
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
					<td class="leftcol"><a href="javascript: showSubtitle();">Questionnaire</a></td>
				</tr>
				<tr>
					<td>
					<table width="100%" style="display: none" id="questionnaire">
						<tr>
							<td width="10%"></td>
							<td><a href="javascript: goToPage1();"
								title="Core questions">&bull; <font style="font-size: 70%">Core</font></a></td>
						</tr>
						<tr>
							<td width="10%"></td>
							<td><a href="javascript: goToPage5();"
								title="Additional questions for users of assistive devices">&bull;
							<font style="font-size: 70%">Additional</font></a></td>
						</tr>
					</table>
					</td>
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
							<table width="100%" height="590px" border="0" cellspacing="0"
								cellpadding="0">
								<tr class="title">
									<th colspan="6">Instruction for Function Questions</th>
								</tr>
								<tr>
									<td>In this following section, I will ask you about your
									ability to do specific activities as part of your daily
									routines. I am interested in your <font
										style="font-style: italic">sense of your ability</font> to do
									it on a typical day. It is not important that you actually do
									the activity on a daily basis. In fact, I may mention some
									activities that you do not do at all. You can still answer
									these questions by assessing how difficult you <font
										style="text-decoration: underline"> think</font> they would be
									for you to do on an average day. <br>
									<br>
									Factors that influence the level of difficulty you have may
									include: pain, fatigue, fear, weakness, soreness, ailments,
									health conditions, or disabilities. <br>
									<br>
									I want you to know how difficult the activity would be for you
									to do <font style="text-decoration: underline"> without</font>
									the help of someone else, and <font
										style="text-decoration: underline">without</font> the use of a
									cane, walker or any other assistive walking device (or
									wheelchair or scooter). <br>
									<br>
									</td>
								</tr>
								<tr>
									<td style="border: 1px solid #000000"><br>
									<table>
										<tr>
											<td width="3%"></td>
											<td><font style="font-weight: bold">Interviewer
											personal note:</font> <br>
											For the Function items, using fixed support is acceptable
											(e.g. holding onto furniture, walls), unless otherwise
											specified in the item. <br>
											<br>
											</td>
										</tr>
									</table>
									</td>
								</tr>
								<tr>
									<td><br>
									[Show visual aid to interviewee] <br>
									<br>
									<table>
										<tr>
											<td width="10%"></td>
											<td colspan="2">Please choose from these answers:</td>
										</tr>
										<tr>
											<td></td>
											<td width="10%"></td>
											<td><font style="font-weight: bold">None</font>
										</tr>
										<tr>
											<td></td>
											<td width="10%"></td>
											<td><font style="font-weight: bold">A Little</font>
										</tr>
										<tr>
											<td></td>
											<td width="10%"></td>
											<td><font style="font-weight: bold">Some</font>
										</tr>
										<tr>
											<td></td>
											<td width="10%"></td>
											<td><font style="font-weight: bold">Quite a lot</font>
										</tr>
										<tr>
											<td></td>
											<td width="10%"></td>
											<td><font style="font-weight: bold">Cannot do</font>
										</tr>
									</table>
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
							<table width="100%" height="590px" border="0" cellspacing="1px"
								cellpadding="0">
								<tr class="title">
									<th colspan="7">
									<table border="0" cellspacing="0" cellpadding="0">
										<tr class="title">
											<th>How much difficulty do you have...?</th>
										</tr>
										<tr class="title">
											<th align="left"><font
												style="font-size: 65%; text-align: left;">(Remember
											this is without the help of someone else and without the use
											of any assistive walking device.)</font>
										</tr>
										</th>
									</table>
									</th>
								</tr>
								<tr class="title">
									<td colspan="2"></td>
									<td width="5%"><font style="font-size: 65%;">None</font></td>
									<td width="5%"><font style="font-size: 65%;">A
									little</font></td>
									<td width="5%"><font style="font-size: 65%;">Some</font></td>
									<td width="5%"><font style="font-size: 65%;">Quite
									a lot</font></td>
									<td width="5%"><font style="font-size: 65%;">Cannot
									do</font></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F1.</td>
									<td class="question" valign="top" width="45%">Unscrewing
									the lid off a previously unopened jar without using any devices
									</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F1None"
										<%= props.getProperty("F1None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F1ALittle"
										<%= props.getProperty("F1ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F1Some"
										<%= props.getProperty("F1Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F1ALot"
										<%= props.getProperty("F1ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F1Cannot"
										<%= props.getProperty("F1Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F2.</td>
									<td class="question" valign="top" width="45%">Going up &
									down a flight of stairs inside, using a handrail</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F2None"
										<%= props.getProperty("F2None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F2ALittle"
										<%= props.getProperty("F2ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F2Some"
										<%= props.getProperty("F2Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F2ALot"
										<%= props.getProperty("F2ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F2Cannot"
										<%= props.getProperty("F2Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F3.</td>
									<td class="question" valign="top" width="45%">Putting on
									and taking off long pants (including managing fasteners)</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F3None"
										<%= props.getProperty("F3None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F3ALittle"
										<%= props.getProperty("F3ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F3Some"
										<%= props.getProperty("F3Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F3ALot"
										<%= props.getProperty("F3ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F3Cannot"
										<%= props.getProperty("F3Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F4.</td>
									<td class="question" valign="top" width="45%">Running 1/2
									mile or more</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F4None"
										<%= props.getProperty("F4None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F4ALittle"
										<%= props.getProperty("F4ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F4Some"
										<%= props.getProperty("F4Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F4ALot"
										<%= props.getProperty("F4ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F4Cannot"
										<%= props.getProperty("F4Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F5.</td>
									<td class="question" valign="top">Using common utensils
									for preparing meals (e.g., can opener, potato peeler, or sharp
									knife)</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F5None"
										<%= props.getProperty("F5None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F5ALittle"
										<%= props.getProperty("F5ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F5Some"
										<%= props.getProperty("F5Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F5ALot"
										<%= props.getProperty("F5ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F5Cannot"
										<%= props.getProperty("F5Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F6.</td>
									<td class="question" valign="top">Holding a full glass of
									water in one hand</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F6None"
										<%= props.getProperty("F6None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F6ALittle"
										<%= props.getProperty("F6ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F6Some"
										<%= props.getProperty("F6Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F6ALot"
										<%= props.getProperty("F6ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F6Cannot"
										<%= props.getProperty("F6Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F7.</td>
									<td class="question" valign="top">Walking a mile, taking
									rests as necessary</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F7None"
										<%= props.getProperty("F7None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F7ALittle"
										<%= props.getProperty("F7ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F7Some"
										<%= props.getProperty("F7Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F7ALot"
										<%= props.getProperty("F7ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F7Cannot"
										<%= props.getProperty("F7Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F8.</td>
									<td class="question" valign="top">Going up & down a flight
									of stairs outside, without using a handrail</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F8None"
										<%= props.getProperty("F8None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F8ALittle"
										<%= props.getProperty("F8ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F8Some"
										<%= props.getProperty("F8Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F8ALot"
										<%= props.getProperty("F8ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F8Cannot"
										<%= props.getProperty("F8Cannot", "") %> /></td>
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
							<table width="100%" height="590px" border="0" cellspacing="1px"
								cellpadding="0">
								<tr class="title">
									<th colspan="7">
									<table border="0" cellspacing="0" cellpadding="0">
										<tr class="title">
											<th>How much difficulty do you have...?</th>
										</tr>
										<tr class="title">
											<th align="left"><font
												style="font-size: 65%; text-align: left;">(Remember
											this is without the help of someone else and without the use
											of any assistive walking device.)</font>
										</tr>
										</th>
									</table>
									</th>
								</tr>
								<tr class="title">
									<td colspan="2"></td>
									<td width="5%"><font style="font-size: 65%;">None</font></td>
									<td width="5%"><font style="font-size: 65%;">A
									little</font></td>
									<td width="5%"><font style="font-size: 65%;">Some</font></td>
									<td width="5%"><font style="font-size: 65%;">Quite
									a lot</font></td>
									<td width="5%"><font style="font-size: 65%;">Cannot
									do</font></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F9.</td>
									<td class="question" valign="top" width="45%">Running a
									short distance, such as to catch a bus</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F9None"
										<%= props.getProperty("F9None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F9ALittle"
										<%= props.getProperty("F9ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F9Some"
										<%= props.getProperty("F9Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F9ALot"
										<%= props.getProperty("F9ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F9Cannot"
										<%= props.getProperty("F9Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F10.</td>
									<td class="question" valign="top">Reaching overhead while
									standing, as if to pull a light cord</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F10None"
										<%= props.getProperty("F10None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F10ALittle"
										<%= props.getProperty("F10ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F10Some"
										<%= props.getProperty("F10Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F10ALot"
										<%= props.getProperty("F10ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F10Cannot"
										<%= props.getProperty("F10Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F11.</td>
									<td class="question" valign="top">Sitting down in and
									standing up from a low, soft couch</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F11None"
										<%= props.getProperty("F11None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F11ALittle"
										<%= props.getProperty("F11ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F11Some"
										<%= props.getProperty("F11Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F11ALot"
										<%= props.getProperty("F11ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F11Cannot"
										<%= props.getProperty("F11Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F12.</td>
									<td class="question" valign="top">Putting on and taking
									off a coat or jacket</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F12None"
										<%= props.getProperty("F12None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F12ALittle"
										<%= props.getProperty("F12ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F12Some"
										<%= props.getProperty("F12Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F12ALot"
										<%= props.getProperty("F12ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F12Cannot"
										<%= props.getProperty("F12Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F13.</td>
									<td class="question" valign="top">Reaching behind your
									back as if to put a belt through a belt loop</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F13None"
										<%= props.getProperty("F13None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F13ALittle"
										<%= props.getProperty("F13ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F13Some"
										<%= props.getProperty("F13Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F13ALot"
										<%= props.getProperty("F13ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F13Cannot"
										<%= props.getProperty("F13Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F14.</td>
									<td class="question" valign="top">Stepping up and down
									from a curb</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F14None"
										<%= props.getProperty("F14None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F14ALittle"
										<%= props.getProperty("F14ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F14Some"
										<%= props.getProperty("F14Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F14ALot"
										<%= props.getProperty("F14ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F14Cannot"
										<%= props.getProperty("F14Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F15.</td>
									<td class="question" valign="top">Opening a heavy, outside
									door</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F15None"
										<%= props.getProperty("F15None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F15ALittle"
										<%= props.getProperty("F15ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F15Some"
										<%= props.getProperty("F15Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F15ALot"
										<%= props.getProperty("F15ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F15Cannot"
										<%= props.getProperty("F15Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F16.</td>
									<td class="question" valign="top">Rip open a package of
									snack food (e.g. cellophane wrapping on crackers) using only
									your hands</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F16None"
										<%= props.getProperty("F16None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F16ALittle"
										<%= props.getProperty("F16ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F16Some"
										<%= props.getProperty("F16Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F16ALot"
										<%= props.getProperty("F16ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F16Cannot"
										<%= props.getProperty("F16Cannot", "") %> /></td>
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
							<table width="100%" height="590px" border="0" cellspacing="1px"
								cellpadding="0">
								<tr class="title">
									<th colspan="7">
									<table border="0" cellspacing="0" cellpadding="0">
										<tr class="title">
											<th>How much difficulty do you have...?</th>
										</tr>
										<tr class="title">
											<th align="left"><font
												style="font-size: 65%; text-align: left;">(Remember
											this is without the help of someone else and without the use
											of any assistive walking device.)</font>
										</tr>
										</th>
									</table>
									</th>
								</tr>
								<tr class="title">
									<td colspan="2"></td>
									<td width="5%"><font style="font-size: 65%;">None</font></td>
									<td width="5%"><font style="font-size: 65%;">A
									little</font></td>
									<td width="5%"><font style="font-size: 65%;">Some</font></td>
									<td width="5%"><font style="font-size: 65%;">Quite
									a lot</font></td>
									<td width="5%"><font style="font-size: 65%;">Cannot
									do</font></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F17.</td>
									<td class="question" valign="top" width="45%">Pouring from
									a large pitcher</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F17None"
										<%= props.getProperty("F17None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F17ALittle"
										<%= props.getProperty("F17ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F17Some"
										<%= props.getProperty("F17Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F17ALot"
										<%= props.getProperty("F17ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F17Cannot"
										<%= props.getProperty("F17Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F18.</td>
									<td class="question" valign="top">Getting into and out of
									a car/taxi (sedan)</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F18None"
										<%= props.getProperty("F18None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F18ALittle"
										<%= props.getProperty("F18ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F18Some"
										<%= props.getProperty("F18Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F18ALot"
										<%= props.getProperty("F18ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F18Cannot"
										<%= props.getProperty("F18Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F19.</td>
									<td class="question" valign="top">Hiking a couple of miles
									on uneven surfaces, including hills</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F19None"
										<%= props.getProperty("F19None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F19ALittle"
										<%= props.getProperty("F19ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F19Some"
										<%= props.getProperty("F19Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F19ALot"
										<%= props.getProperty("F19ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F19Cannot"
										<%= props.getProperty("F19Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F20.</td>
									<td class="question" valign="top">Going up and down 3
									flights of stairs inside, using a handrail</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F20None"
										<%= props.getProperty("F20None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F20ALittle"
										<%= props.getProperty("F20ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F20Some"
										<%= props.getProperty("F20Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F20ALot"
										<%= props.getProperty("F20ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F20Cannot"
										<%= props.getProperty("F20Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F21.</td>
									<td class="question" valign="top">Picking up a kitchen
									chair and moving it, in order to clean</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F21None"
										<%= props.getProperty("F21None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F21ALittle"
										<%= props.getProperty("F21ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F21Some"
										<%= props.getProperty("F21Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F21ALot"
										<%= props.getProperty("F21ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F21Cannot"
										<%= props.getProperty("F21Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F22.</td>
									<td class="question" valign="top">Using a step stool to
									reach into a high cabinet</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F22None"
										<%= props.getProperty("F22None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F22ALittle"
										<%= props.getProperty("F22ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F22Some"
										<%= props.getProperty("F22Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F22ALot"
										<%= props.getProperty("F22ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F22Cannot"
										<%= props.getProperty("F22Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F23.</td>
									<td class="question" valign="top">Making a bed, including
									spreading and tucking in bed sheets</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F23None"
										<%= props.getProperty("F23None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F23ALittle"
										<%= props.getProperty("F23ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F23Some"
										<%= props.getProperty("F23Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F23ALot"
										<%= props.getProperty("F23ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F23Cannot"
										<%= props.getProperty("F23Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F24.</td>
									<td class="question" valign="top">Carrying something in
									both arms while climbing a flight of stairs (e.g. laundry
									basket)</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F24None"
										<%= props.getProperty("F24None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F24ALittle"
										<%= props.getProperty("F24ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F24Some"
										<%= props.getProperty("F24Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F24ALot"
										<%= props.getProperty("F24ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F24Cannot"
										<%= props.getProperty("F24Cannot", "") %> /></td>
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
						style="display: none" width="100%" height="85%" id="page4">
						<tr>
							<td valign="top" colspan="2">
							<table width="100%" height="590px" border="0" cellspacing="1px"
								cellpadding="0">
								<tr class="title">
									<th colspan="7">
									<table border="0" cellspacing="0" cellpadding="0">
										<tr class="title">
											<th>How much difficulty do you have...?</th>
										</tr>
										<tr class="title">
											<th align="left"><font
												style="font-size: 65%; text-align: left;">(Remember
											this is without the help of someone else and without the use
											of any assistive walking device.)</font>
										</tr>
										</th>
									</table>
									</th>
								</tr>
								<tr class="title">
									<td colspan="2"></td>
									<td width="5%"><font style="font-size: 65%;">None</font></td>
									<td width="5%"><font style="font-size: 65%;">A
									little</font></td>
									<td width="5%"><font style="font-size: 65%;">Some</font></td>
									<td width="5%"><font style="font-size: 65%;">Quite
									a lot</font></td>
									<td width="5%"><font style="font-size: 65%;">Cannot
									do</font></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F25.</td>
									<td class="question" valign="top" width="45%">Bending over
									from a standing position to pick up a piece of clothing from
									the floor</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F25None"
										<%= props.getProperty("F25None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F25ALittle"
										<%= props.getProperty("F25ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F25Some"
										<%= props.getProperty("F25Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F25ALot"
										<%= props.getProperty("F25ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F25Cannot"
										<%= props.getProperty("F25Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F26.</td>
									<td class="question" valign="top">Walking around one floor
									of your home, taking into consideration thresholds, doors,
									furniture, and a variety of floor coverings</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F26None"
										<%= props.getProperty("F26None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F26ALittle"
										<%= props.getProperty("F26ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F26Some"
										<%= props.getProperty("F26Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F26ALot"
										<%= props.getProperty("F26ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F26Cannot"
										<%= props.getProperty("F26Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F27.</td>
									<td class="question" valign="top">Getting up from the
									floor (as if you were laying on the ground)</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F27None"
										<%= props.getProperty("F27None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F27ALittle"
										<%= props.getProperty("F27ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F27Some"
										<%= props.getProperty("F27Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F27ALot"
										<%= props.getProperty("F27ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F27Cannot"
										<%= props.getProperty("F27Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F28.</td>
									<td class="question" valign="top">Washing dishes, pots,
									and utensils by hand while standing at sink</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F28None"
										<%= props.getProperty("F28None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F28ALittle"
										<%= props.getProperty("F28ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F28Some"
										<%= props.getProperty("F28Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F28ALot"
										<%= props.getProperty("F28ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F28Cannot"
										<%= props.getProperty("F28Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F29.</td>
									<td class="question" valign="top">Walking several blocks</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F29None"
										<%= props.getProperty("F29None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F29ALittle"
										<%= props.getProperty("F29ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F29Some"
										<%= props.getProperty("F29Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F29ALot"
										<%= props.getProperty("F29ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F29Cannot"
										<%= props.getProperty("F29Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F30.</td>
									<td class="question" valign="top">Taking a 1 mile, brisk
									walk without stopping to rest</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F30None"
										<%= props.getProperty("F30None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F30ALittle"
										<%= props.getProperty("F30ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F30Some"
										<%= props.getProperty("F30Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F30ALot"
										<%= props.getProperty("F30ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F30Cannot"
										<%= props.getProperty("F30Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F31.</td>
									<td class="question" valign="top">Stepping on and off a
									bus</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F31None"
										<%= props.getProperty("F31None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F31ALittle"
										<%= props.getProperty("F31ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F31Some"
										<%= props.getProperty("F31Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F31ALot"
										<%= props.getProperty("F31ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F31Cannot"
										<%= props.getProperty("F31Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">F32.</td>
									<td class="question" valign="top">Walking on a slippery
									surface outdoors</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F32None"
										<%= props.getProperty("F32None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F32ALittle"
										<%= props.getProperty("F32ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F32Some"
										<%= props.getProperty("F32Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F32ALot"
										<%= props.getProperty("F32ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="F32Cannot"
										<%= props.getProperty("F32Cannot", "") %> /></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr class="subject">
							<td align="left"><a href="javascript: backToPage3();"><<
							Previous Page</a></td>
							<td align="right"><a href="javascript: calculateScore();">
							Calculate Total Score</a></td>
						</tr>
					</table>
					<table border="0" cellspacing="0" cellpadding="0"
						style="display: none" width="100%" height="85%" id="totalScore">
						<tr>
							<td valign="top" colspan="2">
							<table width="100%" height="590px" border="0" cellspacing="1px"
								cellpadding="0">
								<tr class="title">
									<th>
									<table border="0" cellspacing="0" cellpadding="0">
										<tr class="title">
											<th>How much difficulty do you have...?</th>
										</tr>
										<tr class="title">
											<th align="left"><font
												style="font-size: 65%; text-align: left;">(Remember
											this is without the help of someone else and without the use
											of any assistive walking device.)</font>
										</tr>
										</th>
									</table>
									</th>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">Your total
									raw score is: <input class="finalScore" type="text"
										readonly="true" name="score" /></td>
								</tr>
								<tr>
									<td>
									<table height="520px">
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
							<td align="right">&nbsp;</td>
						</tr>
					</table>
					<table border="0" cellspacing="0" cellpadding="0"
						style="display: none" width="100%" height="85%" id="page5">
						<tr>
							<td valign="top" colspan="2">
							<table width="100%" height="590px" border="0" cellspacing="1px"
								cellpadding="0">
								<tr class="title">
									<th colspan="7">
									<table border="0" cellspacing="0" cellpadding="0">
										<tr class="title">
											<th>When you use your cane, walker, or other walking
											device, how much difficulty do you have...?</th>
										</tr>
									</table>
									</th>
								</tr>
								<tr class="title">
									<td colspan="2"></td>
									<td width="5%"><font style="font-size: 65%;">None</font></td>
									<td width="5%"><font style="font-size: 65%;">A
									little</font></td>
									<td width="5%"><font style="font-size: 65%;">Some</font></td>
									<td width="5%"><font style="font-size: 65%;">Quite
									a lot</font></td>
									<td width="5%"><font style="font-size: 65%;">Cannot
									do</font></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">FD7.</td>
									<td class="question" valign="top" width="45%">Walking a
									mile, taking rests as necessary</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD7None"
										<%= props.getProperty("FD7None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD7ALittle"
										<%= props.getProperty("FD7ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD7Some"
										<%= props.getProperty("FD7Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD7ALot"
										<%= props.getProperty("FD7ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD7Cannot"
										<%= props.getProperty("FD7Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">FD8.</td>
									<td class="question" valign="top">Going up & down a
									flight of stairs outside, without using a handrail</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD8None"
										<%= props.getProperty("FD8None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD8ALittle"
										<%= props.getProperty("FD8ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD8Some"
										<%= props.getProperty("FD8Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD8ALot"
										<%= props.getProperty("FD8ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD8Cannot"
										<%= props.getProperty("FD8Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">FD14.</td>
									<td class="question" valign="top">Stepping up and down
									from a curb</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD14None"
										<%= props.getProperty("FD14None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD14ALittle"
										<%= props.getProperty("FD14ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD14Some"
										<%= props.getProperty("FD14Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD14ALot"
										<%= props.getProperty("FD14ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD14Cannot"
										<%= props.getProperty("FD14Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">FD15.</td>
									<td class="question" valign="top">Opening a heavy, outside
									door</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD15None"
										<%= props.getProperty("FD15None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD15ALittle"
										<%= props.getProperty("FD15ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD15Some"
										<%= props.getProperty("FD15Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD15ALot"
										<%= props.getProperty("FD15ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD15Cannot"
										<%= props.getProperty("FD15Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">FD26.</td>
									<td class="question" valign="top">Walking around one floor
									of your home, taking into consideration thresholds, doors,
									furniture, and a variety of floor coverings</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD26None"
										<%= props.getProperty("FD26None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD26ALittle"
										<%= props.getProperty("FD26ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD26Some"
										<%= props.getProperty("FD26Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD26ALot"
										<%= props.getProperty("FD26ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD26Cannot"
										<%= props.getProperty("FD26Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">FD29.</td>
									<td class="question" valign="top">Walking several blocks</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD29None"
										<%= props.getProperty("FD29None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD29ALittle"
										<%= props.getProperty("FD29ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD29Some"
										<%= props.getProperty("FD29Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD29ALot"
										<%= props.getProperty("FD29ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD29Cannot"
										<%= props.getProperty("FD29Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">FD30.</td>
									<td class="question" valign="top">Taking a 1 mile, brisk
									walk without stopping to rest</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD30None"
										<%= props.getProperty("FD30None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD30ALittle"
										<%= props.getProperty("FD30ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD30Some"
										<%= props.getProperty("FD30Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD30ALot"
										<%= props.getProperty("FD30ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD30Cannot"
										<%= props.getProperty("FD30Cannot", "") %> /></td>
								</tr>
								<tr>
									<td class="question" valign="top" width="5%">FD32.</td>
									<td class="question" valign="top">Walking on a slippery
									surface outdoors</td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD32None"
										<%= props.getProperty("FD32None", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD32ALittle"
										<%= props.getProperty("FD32ALittle", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD32Some"
										<%= props.getProperty("FD32Some", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD32ALot"
										<%= props.getProperty("FD32ALot", "") %> /></td>
									<td bgcolor="white" align="center"><input type="checkbox"
										class="checkbox" name="FD32Cannot"
										<%= props.getProperty("FD32Cannot", "") %> /></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr class="subject">
							<td align="left">&nbsp;</td>
							<td align="right">&nbsp;</td>
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
