
<%@ page language="java"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
    String formClass = "LateLifeFDIFunction";
    String formLink = "formlatelifeFDIfunction.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(demoNo, formId);

    //FrmData fd = new FrmData();    String resource = fd.getResource(); resource = resource + "ob/riskinfo/";

    //get project_home
    String project_home = request.getContextPath().substring(1);	
%>
<%
  boolean bView = false;
  if (request.getParameter("view") != null && request.getParameter("view").equals("1")) bView = true; 
%>

<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>

<head>
    <title>The Home Falls and Accidents Screening Tool (HOME FAST)</title>
    <html:base/>
        <style type="text/css">
        a:link{
            text-decoration: none;
            color:#FFFFFF;
        }

        a:active{
            text-decoration: none;
            color:#FFFFFF;
        }

        a:visited{
            text-decoration: none;
            color:#FFFFFF;
        }

        a:hover{
            text-decoration: none;
            color:#FFFFFF;
        }

	.Head {
            background-color:#BBBBBB;
            padding-top:3px;
            padding-bottom:3px;            
            height: 30px;
            font-size:12pt;
        }

        .Head INPUT {
            width: 100px;
        }

        .Head A {
            font-size:12pt;
        }

        BODY {
            font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;             
            background-color: #F2F2F2;            
        }

        TABLE {
            font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;
        }
        
        TD{
            font-size:13pt;
        }

        TH{
            font-size:14pt;            
        }

        .leftcol{
            width="100%";            
            color: #A9A9A9;              
            font-weight: bold;
            text-align: Left;            
        }

        .checkbox{
            height: 25px;
            width: 25px;    
            border: 0px solid #A9A9A9;
            background-color: #FFFFFF;              
        }

        .checkboxError{
            height: 25px;
            width: 25px;   
            border: 0px solid #A9A9A9;
            background-color: red;                   
        }
        
        .subject {
            background-color: #000000;
            color: #FFFFFF;  
            font-size: 15pt;
            font-weight: bold;
            text-align: centre;
            border-bottom:2px solid #A9A9A9
        }
        
        .lefttopCell{
            background-color: #000000;
            color: #FFFFFF;  
            font-size: 15pt;
            font-weight: bold;
            text-align: centre;
            border-bottom:2px solid #A9A9A9;
            border-right:2px solid #A9A9A9;
        }

        .title {
            background-color: #486ebd;
            color: #FFFFFF;            
            font-weight: bold;
            text-align: center;
        }
        .subTitle {
            backgroud-color: #F2F2F2;
            font-weight: bold;
            font-style: italic;
            text-align: Left;          
        }
        .question{
            text-align: left;
            font-weight: bold;
            height="10px";
        }
        .scoreHeavy{
            border-top: 2px solid #A9A9A9;
            text-align: center;
        }
        .scoreLight{
            border-top: 1px solid #A9A9A9;
            text-align: center;
        }
        .score{
            font-size=80%;
        }
        .smallTable{
            border: 2px solid #F2F2F2;
        }
        .row{
            border-bottom: 2px solid #F2F2F2;
        }
        .textbox{
            border: 1px solid #A9A9A9;
        }

        .vertContent1 {
            font-family: "Century Gothic";
            background-color: #FF6600;
            writing-mode: tb-rl;
            filter:   flipH() flipV();
            font-size: 24px;
            color: #FFFFFF;
            font-weight: bolder;
        }

    </style>
</head>


<script type="text/javascript" language="Javascript">
    var choiceFormat  = new Array(6,7,8,9,10,11,12,14,15,17,18,20,21,22,23,24,25,27,28,30,31,33,34,36,37,38,39,40,41,42,43,44,45,46,47,49,50,52,53,55,56,58,59,60,61,63,64,65,66,68);
    var allNumericField = null;     
    var allMatch = null;
    var action = "/<%=project_home%>/form/formname.do";

    function goToInstructions(){
        document.getElementById('instruction').style.display = 'block';
        document.getElementById('visualAid1').style.display = 'none';
        document.getElementById('visualAid2').style.display = 'none';
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none';  
        document.getElementById('page3').style.display = 'none';
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none'; 
        document.getElementById('subject2').style.display = 'none';
        document.getElementById('functionBar').style.display = 'none';
        document.getElementById('copyRight').style.display = 'block';
    }

    function goToVisualAid1(){
        document.getElementById('instruction').style.display = 'none';
        document.getElementById('visualAid1').style.display = 'block';
        document.getElementById('visualAid2').style.display = 'none';
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none';  
        document.getElementById('page3').style.display = 'none';
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none'; 
        document.getElementById('subject2').style.display = 'none';
        document.getElementById('functionBar').style.display = 'none';
        document.getElementById('copyRight').style.display = 'block';
    }

    function goToVisualAid2(){
        document.getElementById('instruction').style.display = 'none';
        document.getElementById('visualAid1').style.display = 'none';
        document.getElementById('visualAid2').style.display = 'block';
        document.getElementById('page1').style.display = 'none';
        document.getElementById('page2').style.display = 'none';  
        document.getElementById('page3').style.display = 'none';
        document.getElementById('page4').style.display = 'none';
        document.getElementById('page5').style.display = 'none'; 
        document.getElementById('subject2').style.display = 'none';
        document.getElementById('functionBar').style.display = 'none';
        document.getElementById('copyRight').style.display = 'block';
    }

    function goToPage1(){             
            document.getElementById('instruction').style.display = 'none';
            document.getElementById('visualAid1').style.display = 'none';
            document.getElementById('visualAid2').style.display = 'none';
            document.getElementById('page1').style.display = 'block';
            document.getElementById('page2').style.display = 'none';  
            document.getElementById('page3').style.display = 'none';
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none'; 
            document.getElementById('subject2').style.display = 'none';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
    }
    
    function goToPage2(){      
        //var checkboxes = new Array(6,7,8,9,10,11,12,14);
        //if (is1CheckboxChecked(0, checkboxes)==true){
            document.getElementById('instruction').style.display = 'none';
            document.getElementById('visualAid1').style.display = 'none';
            document.getElementById('visualAid2').style.display = 'none';
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'block'; 
            document.getElementById('page3').style.display = 'none'; 
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('subject2').style.display = 'none';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
        //}
    }

    function goToPage3(){      
        //var checkboxes = new Array(15,17,18,20,21,22,23,24,25,27);
        //var numericFields = new Array(57,58,59,60);
        //if (is1CheckboxChecked(0, checkboxes)==true){
            document.getElementById('instruction').style.display = 'none';
            document.getElementById('visualAid1').style.display = 'none';
            document.getElementById('visualAid2').style.display = 'none';
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'block';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('subject2').style.display = 'none';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
        //}
    }

    function goToPage4(){    
        //var checkboxes = new Array(28,30,31,33,34,36,37,38,39,40,41,42);
        //if (is1CheckboxChecked(0, checkboxes)==true){
            document.getElementById('instruction').style.display = 'none';
            document.getElementById('visualAid1').style.display = 'none';
            document.getElementById('visualAid2').style.display = 'none';
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'block';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('subject2').style.display = 'none';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
        //}
    }

    function goToPage5(){      
        //var checkboxes = new Array(43,44,45,46,47,49,50,52,53,55);
        //if (is1CheckboxChecked(0, checkboxes)==true){
            document.getElementById('instruction').style.display = 'none';
            document.getElementById('visualAid1').style.display = 'none';
            document.getElementById('visualAid2').style.display = 'none';
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'block';
            document.getElementById('subject2').style.display = 'block';
            document.getElementById('functionBar').style.display = 'block';
            document.getElementById('copyRight').style.display = 'none';
        //}
    }
    function showSubtitle(){
        if(document.getElementById('questionnaire').style.display == 'block')
            document.getElementById('questionnaire').style.display = 'none';
        else 
            document.getElementById('questionnaire').style.display = 'block';
    }
        
</script>
<script type="text/javascript" src="formScripts.js">          
</script>


<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0" onload="window.resizeTo(768,768)">
<!--
@oscar.formDB Table="formAdf" 
@oscar.formDB Field="ID" Type="int(10)" Null="NOT NULL" Key="PRI" Default="" Extra="auto_increment"
@oscar.formDB Field="demographic_no" Type="int(10)" Null="NOT NULL" Default="'0'" 
@oscar.formDB Field="provider_no" Type="int(10)" Null="" Default="NULL" 
@oscar.formDB Field="formCreated" Type="date" Null="" Default="NULL" 
@oscar.formDB Field="formEdited" Type="timestamp"  
-->
<html:form action="/form/formname">
<input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
<input type="hidden" name="formCreated" value="<%= props.getProperty("formCreated", "") %>" />
<input type="hidden" name="form_class" value="<%=formClass%>" />
<input type="hidden" name="form_link" value="<%=formLink%>" />
<input type="hidden" name="formId" value="<%=formId%>" />
<input type="hidden" name="submit" value="exit"/>

<table border="0" cellspacing="1" cellpadding="0" width="740px" height="95%">
<tr>    
    <td valign="top" colspan="2">
    <table border="0" cellspacing="0" cellpadding="0" width="740px" height="10%">
        <tr>
            <th class="lefttopCell" width="17%">&nbsp;</th>
            <th class="subject">
                Late Life FDI: Function Question <br>
                <table style="display:none" id="subject2">
                    <tr><th class="subject" style="border: 0px">
                        For those who use walking devices
                        <br><font style="font-size:60%">
                        The following are questions only for people using canes, walkers, or other walking devices
                        </font>.
                    </td></tr>
                </table>
            </th>
        </tr> 
    </table>
</td></tr>
<tr>
    <td bgcolor="#A9A9A9" valign="top" align="center" width="17%" style="border-right:2px solid #A9A9A9;">
        <table>
            <tr>
                <td class="leftcol"><a href="javascript: goToInstructions();">Instructions</a></td>
            </tr>
            <tr>
                <td></td>
            </tr>
            <tr>
                <td class="leftcol"><a href="javascript: goToVisualAid1();" title="for core questions" >Visual Aid 1</a></td>
            </tr>
            <tr>
                <td></td>
            </tr>
            <tr>
                <td class="leftcol"><a href="javascript: goToVisualAid2();" title="for additional device questions">Visual Aid 2</a></td>
            </tr>            
            <tr>
                <td></td>
            </tr>
            <tr>
                <td class="leftcol"><a href="javascript: showSubtitle();">Questionnaire</a></td>
            </tr>
            <tr>
                <td>
                    <table width="100%" style="display:none" id="questionnaire">
                        <tr>
                            <td width="10%"></td>
                            <td><a href="javascript: goToPage1();" title="Core questions">&bull; <font style="font-size:70%">Core</font></a></td>
                        </tr>
                        <tr>
                            <td width="10%"></td>
                            <td><a href="javascript: goToPage5();" title="Additional questions for users of assistive devices">&bull; <font style="font-size:70%">Additional</font></a></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </td>
    <td width="83%"><table>
    <tr><td valign="top">
    <table border="0" cellspacing="0" cellpadding="0" height="85%" width="100%" id="instruction">        
        <tr>        
            <td valign="top" colspan="2">
                <table width="100%" height="590px" border="0" cellspacing="0" cellpadding="0" >                                        
                    <tr class="title" >
                        <th colspan="6">Instruction for Function Questions</th>
                    </tr> 
                    <tr>
                        <td>
                            In this following section, I will ask you about your ability to do specific activities as part of your daily
                            routines.  I am interested in your <font style="font-style: italic">sense of your ability</font> to do it on
                            a typical day. It is not important that you actually do the activity on a daily basis.  In fact, I may mention
                            some activities that you do not do at all. You can still answer these questions by assessing how difficult you
                            <font style="text-decoration: underline"> think they would be for you to do on an average day.</font>
                            <br>
                            <br>
                            Factors that influence the level of difficulty you have may include: pain, fatigue, fear, weakness, soreness,
                            ailments, health conditions, or disabilites.
                            <br>
                            <br>
                            I want you to know how difficult the activity would be for you to do <font style="text-decoration: underline">
                            without</font> the help of someone else, and <font style="text-decoration: underline">without</font> the use of
                            a cane, walker or any other assistive walking device (or wheelchair or scooter).
                            <br>
                            <br>
                        </td>
                    </tr>
                    <tr>
                        <td style="border:1px solid #000000">
                            <br>
                            <table>
                                <tr>
                                    <td width="3%"></td>
                                    <td>
                                        <font style="font-weight: bold">Interviewer personal note:</font>
                                        <br>For the Function items, using fixed support is acceptable (e.g. holding onto furniture, walls), unless
                                            otherwise specified in the item.
                                        <br>
                                        <br>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <br>
                            [Show visual aid to interviewee]
                            <br>
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
                        <td>
                            Let's begin...
                        </td>
                    </tr>
                </table>            
            </td>
        </tr>        
    </table>
    
    <table border="0" cellspacing="0" cellpadding="0" style="display:none" height="85%" width="100%" id="visualAid1">        
        <tr>        
            <td valign="top" colspan="2">
                <table width="100%" height="590px" border="0" cellspacing="1" cellpadding="2" >                                        
                    <tr class="title" >
                        <th colspan="5">Function Visual Aid #1</th>
                    </tr> 
                    <tr>
                        <td class="question" colspan="5">
                            Currently, how much difficulty do you have in doing the activity without the help of someone else, and without
                            the use of a cane, walker or any other assistive walking device?
                            <br>
                            <br>
                        </td>
                    </tr>                   
                    <tr bgcolor="white">
                        <td width="11%" align="center">
                            <img src="graphics/functionVisualAid/None.JPG" align="center" border='0'/>
                        </td>
                        <td width="20%" align="center">
                            <img src="graphics/functionVisualAid/little.JPG" align="center" border='0'/>
                        </td>
                        <td width="25%" align="center">
                            <img src="graphics/functionVisualAid/some.JPG" align="center" border='0'/>
                        </td>
                        <td width="30%" align="center">
                            <img src="graphics/functionVisualAid/quiteALot.JPG" align="center" border='0'/>
                        </td>
                        <td width="34%" align="center">
                            <img src="graphics/functionVisualAid/cannotDo.JPG" align="center" border='0'/>
                        </td>
                    </tr>
                    <tr bgcolor="white">
                        <td align="center" valign="top" width="10%">
                            You have no difficulty doing the activity alone
                        </td>
                        <td align="center" valign="top" width="20%">
                            You can do it alone with a little bit of difficult
                        </td>
                        <td align="center" valign="top" width="25%">
                            You can do it, but you have a moderate amount of difficulty doing it alone
                        </td>
                        <td align="center" valign="top" width="31%">
                            You can manage without help, but you have quite a lot of difficulty doing it
                        </td>
                        <td align="center" valign="top" width="34%">
                            It is so difficult, that you cannot do it unless you have help
                        </td>
                    </tr>
                    <tr><td>&nbsp;</td></tr>
                     <tr>
                        <td style="border:1px solid #000000" colspan="3">
                            <table>
                                <tr>
                                    <td width="3%"></td>
                                    <td>
                                        Factors that may influence your level of difficulty:
                                        <br>
                                        <br>Pain
                                        <br>Fatigue
                                        <br>Soreness
                                        <br>Ailments
                                        <br>Disabilities
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>            
            </td>
        </tr>        
    </table>
    
    <table border="0" cellspacing="0" cellpadding="0" style="display:none" height="85%" width="100%" id="visualAid2">        
        <tr>        
            <td valign="top" colspan="2">
                <table width="100%" height="590px" border="0" cellspacing="1" cellpadding="2" >                                        
                    <tr class="title" >
                        <th colspan="5">
                            Function Visual Aid #2 <font style="font-size:80%; font-weight:bold">(For users of canes or walkers only)
                        </th>
                    </tr> 
                    <tr>
                        <td class="question" colspan="5">
                            Currently, how much difficulty do you have in doing the activity when you use your cane, walker or any 
                            other assistive walking device?
                            <br>
                            <br>
                        </td>
                    </tr>                   
                    <tr bgcolor="white">
                        <td width="11%" align="center">
                            <img src="graphics/functionVisualAid/None.JPG" align="center" border='0'/>
                        </td>
                        <td width="20%" align="center">
                            <img src="graphics/functionVisualAid/little.JPG" align="center" border='0'/>
                        </td>
                        <td width="25%" align="center">
                            <img src="graphics/functionVisualAid/some.JPG" align="center" border='0'/>
                        </td>
                        <td width="30%" align="center">
                            <img src="graphics/functionVisualAid/quiteALot.JPG" align="center" border='0'/>
                        </td>
                        <td width="34%" align="center">
                            <img src="graphics/functionVisualAid/cannotDo.JPG" align="center" border='0'/>
                        </td>
                    </tr>
                    <tr bgcolor="white">
                        <td align="center" valign="top" width="10%">
                            You have no difficulty doing the activity alone
                        </td>
                        <td align="center" valign="top" width="20%">
                            You can do it alone with a little bit of difficult
                        </td>
                        <td align="center" valign="top" width="25%">
                            You can do it, but you have a moderate amount of difficulty doing it alone
                        </td>
                        <td align="center" valign="top" width="31%">
                            You can manage without help, but you have quite a lot of difficulty doing it
                        </td>
                        <td align="center" valign="top" width="34%">
                            It is so difficult, that you cannot do it unless you have help
                        </td>
                    </tr>
                    <tr><td>&nbsp;</td></tr>
                     <tr>
                        <td style="border:1px solid #000000" colspan="3">
                            <table>
                                <tr>
                                    <td width="3%"></td>
                                    <td>
                                        Factors that may influence your level of difficulty:
                                        <br>
                                        <br>Pain
                                        <br>Fatigue
                                        <br>Soreness
                                        <br>Ailments
                                        <br>Disabilities
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr> 
                    <tr>
                        <td colspan="3">
                            <table height="8px">
                                <tr><td></td></tr>
                            </table>
                        </td>
                    </tr> 
                </table>            
            </td>
        </tr>        
    </table>
    
    <table border="0" cellspacing="0" cellpadding="0" style="display:none" height="85%" width="100%" id="page1">        
        <tr>        
            <td valign="top" colspan="2">
                <table width="100%" height="590px" border="0"  cellspacing="0" cellpadding="0" >                                        
                    <tr class="title" >
                        <th colspan="6">How much difficulty do you have...?</th>
                    </tr> 
                    <tr class="title">
                        <td colspan="6"><font style="font-size: 70%">(Remember this is without the help of someone else and without the use of any
                        assistive walking device.)</font>
                        </td>
                    <tr>
                        <td class="question" valign="top" width="5%">F1.</td>
                        <td class="question" valign="top" colspan="5">
                        Unscrewing the lid off a previously unopened jar without using any devices
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F1None" <%= props.getProperty("F1None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F1ALittle" <%= props.getProperty("F1ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F1Some" <%= props.getProperty("F1Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F1ALot" <%= props.getProperty("F1ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F1Cannot" <%= props.getProperty("F1Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F2.</td>
                        <td class="question" valign="top" colspan="5">
                        Going up & down a flight of stairs inside, using a handrail
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F2None" <%= props.getProperty("F2None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F2ALittle" <%= props.getProperty("F2ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F2Some" <%= props.getProperty("F2Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F2ALot" <%= props.getProperty("F2ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F2Cannot" <%= props.getProperty("F2Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F3.</td>
                        <td class="question" valign="top" colspan="5">
                        Putting on and taking off long pants (including managing fasteners)
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F3None" <%= props.getProperty("F3None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F3ALittle" <%= props.getProperty("F3ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F3Some" <%= props.getProperty("F3Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F3ALot" <%= props.getProperty("F3ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F3Cannot" <%= props.getProperty("F3Cannot", "") %>/> Cannot do</td>                  
                    </tr> 
                    <tr>
                        <td class="question" valign="top" width="5%">F4.</td>
                        <td class="question" valign="top" colspan="5">
                        Running 1/2 mile or more
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F4None" <%= props.getProperty("F4None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F4ALittle" <%= props.getProperty("F4ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F4Some" <%= props.getProperty("F4Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F4ALot" <%= props.getProperty("F4ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F4Cannot" <%= props.getProperty("F4Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F5.</td>
                        <td class="question" valign="top" colspan="5">
                        Using common utensils for preparing meals (e.g., can opener, potato peeler, or sharp knife)
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F5None" <%= props.getProperty("F5None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F5ALittle" <%= props.getProperty("F5ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F5Some" <%= props.getProperty("F5Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F5ALot" <%= props.getProperty("F5ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F5Cannot" <%= props.getProperty("F5Cannot", "") %>/> Cannot do</td>                  
                    </tr> 
                    <tr>
                        <td class="question" valign="top" width="5%">F6.</td>
                        <td class="question" valign="top" colspan="5">
                        Holding a full glass of water in one hand
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F6None" <%= props.getProperty("F6None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F6ALittle" <%= props.getProperty("F6ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F6Some" <%= props.getProperty("F6Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F6ALot" <%= props.getProperty("F6ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F6Cannot" <%= props.getProperty("F6Cannot", "") %>/> Cannot do</td>                  
                    </tr> 
                    <tr>
                        <td class="question" valign="top" width="5%">F7.</td>
                        <td class="question" valign="top" colspan="5">
                        Walking a mile, taking rests as necessary
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F7None" <%= props.getProperty("F7None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F7ALittle" <%= props.getProperty("F7ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F7Some" <%= props.getProperty("F7Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F7ALot" <%= props.getProperty("F7ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F7Cannot" <%= props.getProperty("F7Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F8.</td>
                        <td class="question" valign="top" colspan="5">
                        Going up & down a flight of stairs outside, without using a handrail
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F8None" <%= props.getProperty("F8None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F8ALittle" <%= props.getProperty("F8ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F8Some" <%= props.getProperty("F8Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F8ALot" <%= props.getProperty("F8ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F8Cannot" <%= props.getProperty("F8Cannot", "") %>/> Cannot do</td>                  
                    </tr>                    
                </table>            
            </td>
        </tr>
        <tr class="subject">
            <td></td>
            <td align="right">
                <a href="javascript: goToPage2();">Next Page >></a>
            </td>
        </tr>
    </table>
    
    <table border="0" cellspacing="0" cellpadding="0" style="display:none" width="100%" height="85%" id="page2" >    
        <tr>        
            <td valign="top" colspan="2">
               <table width="100%" height="590px" border="0"  cellspacing="0" cellpadding="0" >                                   
                    <tr class="title" >
                        <th colspan="6">How much difficulty do you have...?</th>
                    </tr> 
                    <tr class="title">
                        <td colspan="6"><font style="font-size: 70%">(Remember this is without the help of someone else and without the use of any
                        assistive walking device.)</font>
                        </td>
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F9.</td>
                        <td class="question" valign="top" colspan="5">
                        Running a short distance, such as to catch a bus
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F9None" <%= props.getProperty("F9None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F9ALittle" <%= props.getProperty("F9ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F9Some" <%= props.getProperty("F9Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F9ALot" <%= props.getProperty("F9ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F9Cannot" <%= props.getProperty("F9Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F10.</td>
                        <td class="question" valign="top" colspan="5">
                        Reaching overhead while standing, as if to pull a light cord
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F10None" <%= props.getProperty("F10None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F10ALittle" <%= props.getProperty("F10ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F10Some" <%= props.getProperty("F10Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F10ALot" <%= props.getProperty("F10ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F10Cannot" <%= props.getProperty("F10Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F11.</td>
                        <td class="question" valign="top" colspan="5">
                        Sitting down in and standing up from a low, soft couch
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F11None" <%= props.getProperty("F11None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F11ALittle" <%= props.getProperty("F11ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F11Some" <%= props.getProperty("F11Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F11ALot" <%= props.getProperty("F11ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F11Cannot" <%= props.getProperty("F11Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F12.</td>
                        <td class="question" valign="top" colspan="5">
                        Putting on and taking off a coat or jacket
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F12None" <%= props.getProperty("F12None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F12ALittle" <%= props.getProperty("F12ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F12Some" <%= props.getProperty("F12Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F12ALot" <%= props.getProperty("F12ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F12Cannot" <%= props.getProperty("F12Cannot", "") %>/> Cannot do</td>                  
                    </tr> 
                    <tr>
                        <td class="question" valign="top" width="5%">F13.</td>
                        <td class="question" valign="top" colspan="5">
                        Reaching behind your back as if to put a belt through a belt loop
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F13None" <%= props.getProperty("F13None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F13ALittle" <%= props.getProperty("F13ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F13Some" <%= props.getProperty("F13Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F13ALot" <%= props.getProperty("F13ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F13Cannot" <%= props.getProperty("F13Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F14.</td>
                        <td class="question" valign="top" colspan="5">
                        Stepping up and down from a curb
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F14None" <%= props.getProperty("F14None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F14ALittle" <%= props.getProperty("F14ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F14Some" <%= props.getProperty("F14Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F14ALot" <%= props.getProperty("F14ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F14Cannot" <%= props.getProperty("F14Cannot", "") %>/> Cannot do</td>                  
                    </tr> 
                    <tr>
                        <td class="question" valign="top" width="5%">F15.</td>
                        <td class="question" valign="top" colspan="5">
                        Opening a heavy, outside door
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F15None" <%= props.getProperty("F15None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F15ALittle" <%= props.getProperty("F15ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F15Some" <%= props.getProperty("F15Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F15ALot" <%= props.getProperty("F15ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F15Cannot" <%= props.getProperty("F15Cannot", "") %>/> Cannot do</td>                  
                    </tr> 
                    <tr>
                        <td class="question" valign="top" width="5%">F16.</td>
                        <td class="question" valign="top" colspan="5">
                        Rip open a package of snack food (e.g. cellophane wrapping on crackers) using only your hands
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F16None" <%= props.getProperty("F16None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F16ALittle" <%= props.getProperty("F16ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F16Some" <%= props.getProperty("F16Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F16ALot" <%= props.getProperty("F16ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F16Cannot" <%= props.getProperty("F16Cannot", "") %>/> Cannot do</td>                  
                    </tr>                    
                </table>     
            </td>
        </tr>    
        <tr class="subject">
            <td align="left">
                <a href="javascript: goToPage1();"><< Previous Page</a>
            </td>
            <td align="right">
                <a href="javascript: goToPage3();">Next Page >></a>
            </td>
        </tr>
    </table>
    
    <table border="0" cellspacing="0" cellpadding="0" style="display:none" width="100%" height="85%" id="page3" >    
        <tr>        
            <td valign="top" colspan="2">
               <table width="100%" height="590px" border="0"  cellspacing="0" cellpadding="0" >                                   
                    <tr class="title" >
                        <th colspan="6">How much difficulty do you have...?</th>
                    </tr> 
                    <tr class="title">
                        <td colspan="6"><font style="font-size: 70%">(Remember this is without the help of someone else and without the use of any
                        assistive walking device.)</font>
                        </td>
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F17.</td>
                        <td class="question" valign="top" colspan="5">
                        Pouring from a large pitcher
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F17None" <%= props.getProperty("F17None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F17ALittle" <%= props.getProperty("F17ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F17Some" <%= props.getProperty("F17Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F17ALot" <%= props.getProperty("F17ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F17Cannot" <%= props.getProperty("F17Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F18.</td>
                        <td class="question" valign="top" colspan="5">
                        Getting into and out of a car/taxi (sedan)
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F18None" <%= props.getProperty("F18None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F18ALittle" <%= props.getProperty("F18ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F18Some" <%= props.getProperty("F18Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F18ALot" <%= props.getProperty("F18ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F18Cannot" <%= props.getProperty("F18Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F19.</td>
                        <td class="question" valign="top" colspan="5">
                        Hiking a couple of miles on uneven surfaces, including hills
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F19None" <%= props.getProperty("F19None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F19ALittle" <%= props.getProperty("F19ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F19Some" <%= props.getProperty("F19Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F19ALot" <%= props.getProperty("F19ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F19Cannot" <%= props.getProperty("F19Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F20.</td>
                        <td class="question" valign="top" colspan="5">
                        Going up and down 3 flights of stairs inside, using a handrail
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F20None" <%= props.getProperty("F20None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F20ALittle" <%= props.getProperty("F20ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F20Some" <%= props.getProperty("F20Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F20ALot" <%= props.getProperty("F20ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F20Cannot" <%= props.getProperty("F20Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F21.</td>
                        <td class="question" valign="top" colspan="5">
                        Picking up a kitchen chair and moving it, in order to clean
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F21None" <%= props.getProperty("F21None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F21ALittle" <%= props.getProperty("F21ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F21Some" <%= props.getProperty("F21Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F21ALot" <%= props.getProperty("F21ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F21Cannot" <%= props.getProperty("F21Cannot", "") %>/> Cannot do</td>                  
                    </tr> 
                    <tr>
                        <td class="question" valign="top" width="5%">F22.</td>
                        <td class="question" valign="top" colspan="5">
                        Using a step stool to reach into a high cabinet
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F22None" <%= props.getProperty("F22None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F22ALittle" <%= props.getProperty("F22ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F22Some" <%= props.getProperty("F22Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F22ALot" <%= props.getProperty("F22ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F22Cannot" <%= props.getProperty("F22Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F23.</td>
                        <td class="question" valign="top" colspan="5">
                        Making a bed, including spreading and tucking in bed sheets
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F14None" <%= props.getProperty("F14None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F14ALittle" <%= props.getProperty("F14ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F14Some" <%= props.getProperty("F14Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F14ALot" <%= props.getProperty("F14ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F14Cannot" <%= props.getProperty("F14Cannot", "") %>/> Cannot do</td>                  
                    </tr> 
                    <tr>
                        <td class="question" valign="top" width="5%">F24.</td>
                        <td class="question" valign="top" colspan="5">
                        Carrying something in both arms while climbing a flight of stairs (e.g. laundry basket)
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F15None" <%= props.getProperty("F15None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F15ALittle" <%= props.getProperty("F15ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F15Some" <%= props.getProperty("F15Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F15ALot" <%= props.getProperty("F15ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F15Cannot" <%= props.getProperty("F15Cannot", "") %>/> Cannot do</td>                  
                    </tr>                     
                </table>      
            </td>
        </tr>    
        <tr class="subject">
            <td align="left">
                <a href="javascript: goToPage2();"><< Previous Page</a>
            </td>
            <td align="right">
                <a href="javascript: goToPage4();">Next Page >></a>
            </td>
        </tr>
    </table>
    
    <table border="0" cellspacing="0" cellpadding="0" style="display:none" width="100%" height="85%" id="page4" >    
        <tr>        
            <td valign="top" colspan="2">
                <table width="100%" height="590px" border="0"  cellspacing="0" cellpadding="0" >                                   
                    <tr class="title" >
                        <th colspan="6" width="100%">How much difficulty do you have...?</th>
                    </tr> 
                    <tr class="title">
                        <td colspan="6"><font style="font-size: 70%">(Remember this is without the help of someone else and without the use of any
                        assistive walking device.)</font>
                        </td>
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F25.</td>
                        <td class="question" valign="top" colspan="5">
                        Bending over from a standing position to pick up a piece of clothing from the floor
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F25None" <%= props.getProperty("F25None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F25ALittle" <%= props.getProperty("F25ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F25Some" <%= props.getProperty("F25Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F25ALot" <%= props.getProperty("F25ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F25Cannot" <%= props.getProperty("F25Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F26.</td>
                        <td class="question" valign="top" colspan="5">
                        Walking around one floor of your home, taking into consideration thresholds, doors, furniture, and a variety of floor coverings
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F26None" <%= props.getProperty("F26None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F26ALittle" <%= props.getProperty("F26ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F26Some" <%= props.getProperty("F26Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F26ALot" <%= props.getProperty("F26ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F26Cannot" <%= props.getProperty("F26Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F27.</td>
                        <td class="question" valign="top" colspan="5">
                        Getting up from the floor (as if you were laying on the ground)
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F27None" <%= props.getProperty("F27None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F27ALittle" <%= props.getProperty("F27ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F27Some" <%= props.getProperty("F27Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F27ALot" <%= props.getProperty("F27ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F27Cannot" <%= props.getProperty("F27Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F28.</td>
                        <td class="question" valign="top" colspan="5">
                        Washing dishes, pots, and utensils by hand while standing at sink
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F28None" <%= props.getProperty("F28None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F28ALittle" <%= props.getProperty("F28ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F28Some" <%= props.getProperty("F28Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F28ALot" <%= props.getProperty("F28ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F28Cannot" <%= props.getProperty("F28Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F29.</td>
                        <td class="question" valign="top" colspan="5">
                        Walking several blocks
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F29None" <%= props.getProperty("F29None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F29ALittle" <%= props.getProperty("F29ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F29Some" <%= props.getProperty("F29Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F29ALot" <%= props.getProperty("F29ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F29Cannot" <%= props.getProperty("F29Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F30.</td>
                        <td class="question" valign="top" colspan="5">
                        Taking a 1 mile, brisk walk without stopping to rest                                                                                                  
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F30None" <%= props.getProperty("F30None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F30ALittle" <%= props.getProperty("F30ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F30Some" <%= props.getProperty("F30Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F30ALot" <%= props.getProperty("F30ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F30Cannot" <%= props.getProperty("F30Cannot", "") %>/> Cannot do</td>                  
                    </tr> 
                    <tr>
                        <td class="question" valign="top" width="5%">F31.</td>
                        <td class="question" valign="top" colspan="5">
                        Stepping on and off a bus
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F31None" <%= props.getProperty("F31None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F31ALittle" <%= props.getProperty("F31ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F31Some" <%= props.getProperty("F31Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F31ALot" <%= props.getProperty("F31ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F31Cannot" <%= props.getProperty("F31Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">F32.</td>
                        <td class="question" valign="top" colspan="5">
                        Walking on a slippery surface outdoors
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F32None" <%= props.getProperty("F32None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F32ALittle" <%= props.getProperty("F32ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F32Some" <%= props.getProperty("F32Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F32ALot" <%= props.getProperty("F32ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="F32Cannot" <%= props.getProperty("F32Cannot", "") %>/> Cannot do</td>                  
                    </tr>                    
                </table>
            </td>
        </tr>    
        <tr class="subject">
            <td align="left">
                <a href="javascript: goToPage3();"><< Previous Page</a>
            </td>
            <td align="right">
                <a href="javascript: goToPage5();">Next Page >></a>
            </td>
        </tr>
    </table>
    
    <table border="0" cellspacing="0" cellpadding="0" style="display:none" width="100%" height="85%" id="page5" >    
        <tr>        
            <td valign="top" colspan="2">
                   <table width="100%" height="530px" border="0"  cellspacing="0" cellpadding="0" >                                   
                    <tr class="title" >
                        <th colspan="6" width="100%">When you use your cane, walker, or other walking devices, 
                        <br>how much difficulty do you have...?</th>
                    </tr>                     
                    <tr>
                        <td class="question" valign="top" width="5%">FD7.</td>
                        <td class="question" valign="top" colspan="5">
                        Walking a mile, taking rests as necessary
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD7None" <%= props.getProperty("FD7None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD7ALittle" <%= props.getProperty("FD7ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD7Some" <%= props.getProperty("FD7Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD7ALot" <%= props.getProperty("FD7ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD7Cannot" <%= props.getProperty("FD7Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">FD8.</td>
                        <td class="question" valign="top" colspan="5">
                        Going up & down a flight of stairs outside, without using a handrail
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD8None" <%= props.getProperty("FD8None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD8ALittle" <%= props.getProperty("FD8ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD8Some" <%= props.getProperty("FD8Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD8ALot" <%= props.getProperty("FD8ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD8Cannot" <%= props.getProperty("FD8Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">FD14.</td>
                        <td class="question" valign="top" colspan="5">
                        Stepping up and down from a curb
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD14None" <%= props.getProperty("FD14None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD14ALittle" <%= props.getProperty("FD14ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD14Some" <%= props.getProperty("FD14Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD14ALot" <%= props.getProperty("FD14ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD14Cannot" <%= props.getProperty("FD14Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">FD15.</td>
                        <td class="question" valign="top" colspan="5">
                        Opening a heavy, outside door
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD15None" <%= props.getProperty("FD15None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD15ALittle" <%= props.getProperty("FD15ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD15Some" <%= props.getProperty("FD15Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD15ALot" <%= props.getProperty("FD15ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD15Cannot" <%= props.getProperty("FD15Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">FD26.</td>
                        <td class="question" valign="top" colspan="5">
                        Walking around one floor of your home, taking into consideration thresholds, doors, furniture, and a variety of floor coverings
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD26None" <%= props.getProperty("FD26None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD26ALittle" <%= props.getProperty("FD26ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD26Some" <%= props.getProperty("FD26Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD26ALot" <%= props.getProperty("FD26ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD26Cannot" <%= props.getProperty("FD26Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">FD29.</td>
                        <td class="question" valign="top" colspan="5">
                        Walking several blocks                                                                                            
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD29None" <%= props.getProperty("FD29None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD29ALittle" <%= props.getProperty("FD29ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD29Some" <%= props.getProperty("FD29Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD29ALot" <%= props.getProperty("FD29ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD29Cannot" <%= props.getProperty("FD29Cannot", "") %>/> Cannot do</td>                  
                    </tr> 
                    <tr>
                        <td class="question" valign="top" width="5%">FD30.</td>
                        <td class="question" valign="top" colspan="5">
                        Taking a 1 mile, brisk walk without stopping to rest
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD30None" <%= props.getProperty("FD30None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD30ALittle" <%= props.getProperty("FD30ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD30Some" <%= props.getProperty("FD30Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD30ALot" <%= props.getProperty("FD30ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD30Cannot" <%= props.getProperty("FD30Cannot", "") %>/> Cannot do</td>                  
                    </tr>
                    <tr>
                        <td class="question" valign="top" width="5%">FD32.</td>
                        <td class="question" valign="top" colspan="5">
                        Walking on a slippery surface outdoors
                        </td>                    
                    </tr>                    
                    <tr bgcolor="white">
                        <td width="5%"></td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD32None" <%= props.getProperty("FD32None", "") %>/> None</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD32ALittle" <%= props.getProperty("FD32ALittle", "") %>/> A little</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD32Some" <%= props.getProperty("FD32Some", "") %>/> Some</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD32ALot" <%= props.getProperty("FD32ALot", "") %>/> Quite a lot</td>
                        <td width="19%"><input type="checkbox" class="checkbox" name="FD32Cannot" <%= props.getProperty("FD32Cannot", "") %>/> Cannot do</td>                  
                    </tr>                    
                </table>    
            </td>
        </tr>    
        <tr class="subject">
            <td align="left">
                <a href="javascript: goToPage4();"><< Previous Page</a>
            </td>
            <td align="right">           
            </td>
        </tr>
    </table>
    </td></tr>
    <tr><td valign="top">
    <table class="Head" style="display:none" width="100%" height="15%" id="functionBar">
        <tr>
            <td align="left">
    <%
      if (!bView) {
    %>
                <input type="submit" value="Save" onclick="javascript: return onSave();" />
                <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
    <%
      }
    %>
                <input type="button" value="Exit" onclick="javascript:return onExit();"/>
                <input type="button" value="Print" onclick="javascript:window.print();"/>
            </td>
        </tr>
        <tr>
            <td><font style="font-size:70%">&copy; Copyright 2002 Trustees of Boston University, All Right Reserved</font></td>
        </tr>
    </table>
    </td></tr>
    <tr><td valign="top">
    <table class="Head" valign="bottom" width="100%" height="15%" id="copyRight">        
        <tr>
            <td><font style="font-size:70%">&copy; Copyright 2002 Trustees of Boston University, All Right Reserved</font></td>
        </tr>
    </table>
    </td></tr>
    </table>
</td></tr>
</table>
</html:form>
</body>
</html:html>
