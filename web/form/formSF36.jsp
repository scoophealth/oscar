
<%@ page language="java"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
    String formClass = "SF36";
    String formLink = "formSF36.jsp";

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
    <title>Appendix K: Health Status Quetionnaire (SF36) - Health and Daily Activities</title>
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
            width:740px;
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
            font-weight: normal;            
        }

        .checkbox{
            height: 22px;
            width: 22px;     
            background-color: #FFFFFF;
        }

        .checkboxError{
            height: 22px;
            width: 22px;     
            background-color: red;
        }

        .subject {
            background-color: #000000;
            color: #FFFFFF;  
            font-size: 15pt;
            font-weight: bold;
            text-align: centre;
        }

        .title {
            background-color: #486ebd;
            color: #FFFFFF;            
            font-weight: bold;
            text-align: Center;
        }
        .subTitle {
            backgroud-color: #F2F2F2;
            font-weight: bold;
            text-align: center;             
        }
        .question{
            text-align: left;
        }
        
        

    </style>
</head>


<script type="text/javascript" language="Javascript">
    
    var choiceFormat  = new Array(6,10,12,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50,52,54,56,58,59,61,62,64,65,67,68,70,71,73,74,76,77,79,83,85,90,92,96,98,103,105,110,112,117,119,124,126,131,133,138,140,145,147,152,154,158,161,166,168,172,174,178,180,184,186,190);        
    var action = "/<%=project_home%>/form/formname.do";
    
    function goToPage1(){             
            document.getElementById('page1').style.display = 'block';
            document.getElementById('page2').style.display = 'none';  
            document.getElementById('page3').style.display = 'none';
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';        
            document.getElementById('page6').style.display = 'none';        
    }
    
    function goToPage2(){      
        var checkboxes = new Array(6,10,12,16);        
        if (is1CheckboxChecked(0, checkboxes)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'block'; 
            document.getElementById('page3').style.display = 'none'; 
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('page6').style.display = 'none';        
        }
    }

    function goToPage3(){      
        var checkboxes = new Array(18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50,52,54,56);        
        if (is1CheckboxChecked(0, checkboxes)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'block';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('page6').style.display = 'none';        
        }
    }

    function goToPage4(){    
        var checkboxes = new Array(58,59,61,62,64,65,67,68,70,71,73,74,76,77);
        if (is1CheckboxChecked(0, checkboxes)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'block';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('page6').style.display = 'none';        
        }
    }

    function goToPage5(){      
        var checkboxes = new Array(79,83,85,90,92,96);
        if (is1CheckboxChecked(0, checkboxes)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'block';
            document.getElementById('page6').style.display = 'none';        
        }
    }

    function goToPage6(){      
        var checkboxes = new Array(98,103,105,110,112,117,119,124,126,131,133,138,140,145,147,152,154,158,161,166);
        if (is1CheckboxChecked(0, checkboxes)==true){
            document.getElementById('page1').style.display = 'none';
            document.getElementById('page2').style.display = 'none'; 
            document.getElementById('page3').style.display = 'none';  
            document.getElementById('page4').style.display = 'none';
            document.getElementById('page5').style.display = 'none';
            document.getElementById('page6').style.display = 'block';        
        }
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

<table border="0" cellspacing="0" cellpadding="0" width="740px" height="95%">
<tr><td>
<table border="0" cellspacing="0" cellpadding="0" width="740px" height="10%">
    <tr>
        <th class="subject">Appendix K: Health Status Quetionnaire (SF36)</th>
    </tr>
    <tr class="title" >
        <th colspan="4">Health and Daily Activities</th>
    </tr>
</table>
</td></tr>
<tr><td valign="top">
<table border="0" cellspacing="0" cellpadding="0" height="85%" width="740px" id="page1">        
    <tr>        
        <td colspan="2">
            <table width="740px" height="620px" border="0"  cellspacing="0" cellpadding="0" >                
                <tr>
                    <td colspan="4">
                        This survey asks for your views about your health.  This information will be summarized in your medical record and will help keep track of how you feel and how well you are able to do your usual activities.
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        Answer every question by selecting the appropriate checkbox. If you unsure about how to answer a question, please give the best answer you can and make a comment in the text box below each question.
                    </td>
                </tr>
                <tr>                    
                    <th>1. </th>
                    <th colspan="3" class="question">In general, would you say your health is:</th>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="1Ex" <%= props.getProperty("1Ex", "") %>/>
                    </td>
                    <td width="45%">Excellent</td>
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="1F" <%= props.getProperty("1F", "") %>/>
                    </td>
                    <td width="45%">Fair</td>                
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="1VG" <%= props.getProperty("1VG", "") %>/>
                    </td>
                    <td width="45%">Very Good</td>                
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="1P" <%= props.getProperty("1P", "") %>/>
                    </td>
                    <td width="45%">Poor</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="1G" <%= props.getProperty("1G", "") %>/>
                    </td>
                    <td width="45%">Good</td>
                    <td width="5%"></td>
                    <td width="45%"></td>
                </tr>
                <tr bgcolor="white">
                    <td></td>
                    <td colspan="3">
                        Comments: <input type="text" size="80" name="1Cmt" value="<%= props.getProperty("1Cmt", "") %>"/>
                    </td>
                </tr>
                <tr>                    
                    <th>2. </th>
                    <th colspan="3" class="question"><font style="text-decoration:underline">Compare to one year ago</font>, how would you rate your health in general <font style="text-decoration:underline">now</font>?</th>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="2MuchBetter" <%= props.getProperty("2MuchBetter", "") %>/>
                    </td>
                    <td width="45%">Much better now than one year ago</td>
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="2Worse" <%= props.getProperty("2Worse", "") %>/>
                    </td>
                    <td width="45%">Somewhat worse now than one year ago</td>                
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="2Better" <%= props.getProperty("2Better", "") %>/>
                    </td>
                    <td width="45%">Somewhat better now than one year ago</td>                
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="2MuchWorse" <%= props.getProperty("2MuchWorse", "") %>/>
                    </td>
                    <td width="45%">Much worse now than one year ago</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="2Same" <%= props.getProperty("2Same", "") %>/>
                    </td>
                    <td width="45%">About the same</td>
                    <td width="5%"></td>
                    <td width="45%"></td>
                </tr>
                <tr bgcolor="white">
                    <td></td>
                    <td colspan="3">
                        Comments: <input type="text" size="80" name="2Cmt" value="<%= props.getProperty("2Cmt", "") %>"/>
                    </td>
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
</td></tr>
<tr><td valign="top">
<table border="0" cellspacing="0" cellpadding="0" style="display:none" width="740px" height="85%" id="page2" >    
    <tr>        
        <td colspan="2">
            <table width="740px" height="620px" border="0"  cellspacing="0" cellpadding="0" >                
                <tr>                    
                    <th width="3%" valign="top" >3. </th>
                    <th valign="top" class="question">
                        The following questions are about activities you might do during a typical day. Does <font style="text-decoration:underline">your health</font> limit you in these activities? If so, how much?</th>
                </tr>                
                <tr>
                    <td colspan="2" width="100%" align="right">
                        &nbsp;
                    </td>                                  
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
                                <td valign="top">a. </td>
                                <td>
                                    <font style='text-decoration:underline'>Vigorous activities</font>, such as running, lifting heavy objects, participating in strenuous sports
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3aYesLot" <%= props.getProperty("3aYesLot", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3aYesLittle" <%= props.getProperty("3aYesLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3aNo" <%= props.getProperty("3aNo", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <textarea cols="20" rows="2" name="3aCmt"><%= props.getProperty("3aCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td valign="top">b. </td>
                                <td>
                                    <font style='text-decoration:underline'>Moderate activities</font>, such as moving a table, pushing a vacuum cleaner, bowling, or playing golf
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3bYesLot" <%= props.getProperty("3bYesLot", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3bYesLittle" <%= props.getProperty("3bYesLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3bNo" <%= props.getProperty("3bNo", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <textarea cols="20" rows="2" name="3bCmt"><%= props.getProperty("3bCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td valign="center">c. </td>
                                <td valign="center">
                                    Lifting or carrying groceries
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3cYesLot" <%= props.getProperty("3cYesLot", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3cYesLittle" <%= props.getProperty("3cYesLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3cNo" <%= props.getProperty("3cNo", "") %>/>
                                </td>   
                                <td align="center" bgcolor="white">
                                    <textarea cols="20" rows="2" name="3cCmt"><%= props.getProperty("3cCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td valign="center">d. </td>
                                <td valign="center">
                                    Climbing <font style="text-decoration:underline">several</font> flights of stairs
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3dYesLot" <%= props.getProperty("3dYesLot", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3dYesLittle" <%= props.getProperty("3dYesLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3dNo" <%= props.getProperty("3dNo", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <textarea cols="20" rows="2" name="3dCmt"><%= props.getProperty("3dCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td valign="center">e. </td>
                                <td valign="center">
                                    Climbing <font style="text-decoration:underline">one</font> flight of stairs
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3eYesLot" <%= props.getProperty("3eYesLot", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3eYesLittle" <%= props.getProperty("3eYesLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3eNo" <%= props.getProperty("3eNo", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <textarea cols="20" rows="2" name="3eCmt"><%= props.getProperty("3eCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td valign="center">f. </td>
                                <td valign="center">
                                    Bending, kneeling, or stooping
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3fYesLot" <%= props.getProperty("3fYesLot", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3fYesLittle" <%= props.getProperty("3fYesLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3fNo" <%= props.getProperty("3fNo", "") %>/>
                                </td>   
                                <td align="center" bgcolor="white">
                                    <textarea cols="20" rows="2" name="3fCmt"><%= props.getProperty("3fCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td valign="center">g. </td>
                                <td valign="center">
                                    Walking <font style="text-decoration:underline">more than a mile</font>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3gYesLot" <%= props.getProperty("3gYesLot", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3gYesLittle" <%= props.getProperty("3gYesLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3gNo" <%= props.getProperty("3gNo", "") %>/>
                                </td>   
                                <td align="center" bgcolor="white">
                                    <textarea cols="20" rows="2" name="3gCmt"><%= props.getProperty("3gCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td valign="center">h. </td>
                                <td valign="center">
                                    Walking <font style="text-decoration:underline">several blocks</font>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3hYesLot" <%= props.getProperty("3hYesLot", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3hYesLittle" <%= props.getProperty("3hYesLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3hNo" <%= props.getProperty("3hNo", "") %>/>
                                </td>     
                                <td align="center" bgcolor="white">
                                    <textarea cols="20" rows="2" name="3hCmt"><%= props.getProperty("3hCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td valign="center">i. </td>
                                <td valign="center">
                                    Walking <font style="text-decoration:underline">one blocks</font>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3iYesLot" <%= props.getProperty("3iYesLot", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3iYesLittle" <%= props.getProperty("3iYesLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3iNo" <%= props.getProperty("3iNo", "") %>/>
                                </td>                                
                                <td align="center" bgcolor="white">
                                    <textarea cols="20" rows="2" name="3iCmt"><%= props.getProperty("3iCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td valign="center">j. </td>
                                <td valign="center">
                                    Bathing and dressing yourself
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3jesLot" <%= props.getProperty("3jYesLot", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3jYesLittle" <%= props.getProperty("3jYesLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="3jNo" <%= props.getProperty("3jNo", "") %>/>
                                </td>                                
                                <td align="center" bgcolor="white">
                                    <textarea cols="20" rows="2" name="3jCmt"><%= props.getProperty("3jCmt", "") %></textarea>
                                </td>
                            </tr>                            
                        </table>
                    </td>                    
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
</td></tr>
<tr><td valign="top">
<table border="0" cellspacing="0" cellpadding="0" style="display:none" width="740px" height="85%" id="page3" >    
    <tr>        
        <td colspan="2">
            <table width="740px" height="620px" border="0"  cellspacing="0" cellpadding="0" >                
                <tr>                    
                    <th width="3%" valign="top" >4. </th>
                    <th valign="top" class="question">
                        During the <font style="text-decoration:underline">past 4 weeks</font>, have you had any of the following problems with your work or other regular daily activities 
                        <font style="text-decoration:underline">as a result of your physical health</font>? (Please answer <font style="text-decoration:underline">YES</font> or <font style="text-decoration:underline">NO</font>
                        for each question by selecting the corresponding checkbox on each line.)
                    </th>
                </tr>                
                
                <tr>
                    <td valign="top" colspan="2">
                        <table>
                            <tr class="question">
                                <th width="3%"></th>
                                <th width="42%"></th>
                                <th width="12%" align="center">Yes</th>                                
                                <th width="12%" align="center">No</th>
                                <th width="31%" align="center">Comments</th>
                            </tr>
                            <tr>
                                <td valign="top">a. </td>
                                <td>
                                    Cut down on the <font style='text-decoration:underline'>amount of time</font> you spent on work or other activities
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="4aYes" <%= props.getProperty("4aYes", "") %>/>
                                </td>                                
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="4aNo" <%= props.getProperty("4aNo", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <textarea cols="25" rows="2" name="4aCmt"><%= props.getProperty("4aCmt", "") %></textarea>
                                </td>
                            </tr> 
                            <tr>
                                <td valign="center">b. </td>
                                <td>
                                    <font style='text-decoration:underline'>Accomplished less</font> than you would like
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="4bYes" <%= props.getProperty("4bYes", "") %>/>
                                </td>                                
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="4bNo" <%= props.getProperty("4bNo", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <textarea cols="25" rows="2" name="4bCmt"><%= props.getProperty("4bCmt", "") %></textarea>
                                </td>
                            </tr> 
                            <tr>
                                <td valign="top">c. </td>
                                <td>
                                    Were limited in the <font style='text-decoration:underline'>kind</font> of work or other activities
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="4cYes" <%= props.getProperty("4cYes", "") %>/>
                                </td>                                
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="4cNo" <%= props.getProperty("4cNo", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <textarea cols="25" rows="2" name="4cCmt"><%= props.getProperty("4cCmt", "") %></textarea>
                                </td>
                            </tr> 
                            <tr>
                                <td valign="top">d. </td>
                                <td>
                                    Had <font style='text-decoration:underline'>difficulty</font> performing the work or other activities (for example, it took extra effort)
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="4dYes" <%= props.getProperty("4dYes", "") %>/>
                                </td>                                
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="4dNo" <%= props.getProperty("4dNo", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <textarea cols="25" rows="2" name="4dCmt"><%= props.getProperty("4dCmt", "") %></textarea>
                                </td>
                            </tr> 
                        </table>
                    </td>                    
                </tr>    
                <tr>                    
                    <th width="3%" valign="top" >5. </th>
                    <th valign="top" class="question">
                        During the <font style="text-decoration:underline">past 4 weeks</font>, have you had any of the following problems with your work or other regular daily activities 
                        <font style="text-decoration:underline">as a result of any emotional problems</font> (such as feeling depressed or anxious)? (Please answer <font style="text-decoration:underline">YES</font> or <font style="text-decoration:underline">NO</font>
                        for each question by selecting the corresponding checkbox on each line.)
                    </th>
                </tr>                
                
                <tr>
                    <td valign="top" colspan="2">
                        <table>
                            <tr class="question">
                                <th width="3%"></th>
                                <th width="42%"></th>
                                <th width="12%" align="center">Yes</th>                                
                                <th width="12%" align="center">No</th>
                                <th width="31%" align="center">Comments</th>
                            </tr>
                            <tr>
                                <td valign="top">a. </td>
                                <td>
                                    Cut down on the <font style='text-decoration:underline'>amount of time</font> you spent on work or other activities
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="5aYes" <%= props.getProperty("5aYes", "") %>/>
                                </td>                                
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="5aNo" <%= props.getProperty("5aNo", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <textarea cols="25" rows="2" name="5aCmt"><%= props.getProperty("5aCmt", "") %></textarea>
                                </td>
                            </tr> 
                            <tr>
                                <td valign="center">b. </td>
                                <td>
                                    <font style='text-decoration:underline'>Accomplished less</font> than you would like
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="5bYes" <%= props.getProperty("5bYes", "") %>/>
                                </td>                                
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="5bNo" <%= props.getProperty("5bNo", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <textarea cols="25" rows="2" name="5bCmt"><%= props.getProperty("5bCmt", "") %></textarea>
                                </td>
                            </tr> 
                            <tr>
                                <td valign="top">c. </td>
                                <td>
                                    Didn't do work or other activities as <font style='text-decoration:underline'>carefully</font> as usual
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="5cYes" <%= props.getProperty("5cYes", "") %>/>
                                </td>                                
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="5cNo" <%= props.getProperty("5cNo", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <textarea cols="25" rows="2" name="5cCmt"><%= props.getProperty("5cCmt", "") %></textarea>
                                </td>
                            </tr>                             
                        </table>
                    </td>                    
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
</td></tr>
<tr><td valign="top">
<table border="0" cellspacing="0" cellpadding="0" style="display:none" width="740px" height="85%" id="page4" >    
    <tr> 
        <td colspan='2'>
            <table width="740px" height="620px" border="0"  cellspacing="0" cellpadding="0" >
                <tr>                    
                    <th valign="top">6. </th>
                    <th valign="top" colspan="3" class="question">
                        During the <font style="text-decoration:underline">past 4 weeks</font>, to what extent has your physical health or emotional 
                        problems interfered with your normal social activities with family, friends, neighbours, or groups?
                    </th>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="6NotAtAll" <%= props.getProperty("6NotAtAll", "") %>/>
                    </td>
                    <td width="45%">Not at all</td>
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="6QuiteABit" <%= props.getProperty("6QuiteABit", "") %>/>
                    </td>
                    <td width="45%">Quite a bit</td>                
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="6Slightly" <%= props.getProperty("6Slightly", "") %>/>
                    </td>
                    <td width="45%">Slightly</td>                
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="6Extremely" <%= props.getProperty("6Extremely", "") %>/>
                    </td>
                    <td width="45%">Extremely</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="6Moderately" <%= props.getProperty("6Moderately", "") %>/>
                    </td>
                    <td width="45%">Moderately</td>
                    <td width="5%"></td>
                    <td width="45%"></td>
                </tr>
                <tr bgcolor="white">
                    <td></td>
                    <td colspan="3">
                        Comments: <input type="text" size="80" name="6Cmt" value="<%= props.getProperty("6Cmt", "") %>"/>
                    </td>
                </tr>
                <tr>                    
                    <th>7. </th>
                    <th colspan="3" class="question">
                        How much <font style="text-decoration:underline">bodily</font> pain have you had during the <font style="text-decoration:underline">past 4 weeks</font>?</th>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="7None" <%= props.getProperty("7None", "") %>/>
                    </td>
                    <td width="45%">None</td>
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="7Moderate" <%= props.getProperty("7Moderate", "") %>/>
                    </td>
                    <td width="45%">Moderate</td>                
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="7VeryMild" <%= props.getProperty("7VeryMild", "") %>/>
                    </td>
                    <td width="45%">Very Mild</td>                
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="7Severe" <%= props.getProperty("7Severe", "") %>/>
                    </td>
                    <td width="45%">Severe</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="7Mild" <%= props.getProperty("7Mild", "") %>/>
                    </td>
                    <td width="45%">Mild</td>
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="7VerySevere" <%= props.getProperty("7VerySevere", "") %>/>
                    </td>
                    <td width="45%">Very Severe</td>
                </tr>
                <tr bgcolor="white">
                    <td></td>
                    <td colspan="3">
                        Comments: <input type="text" size="80" name="7Cmt" value="<%= props.getProperty("7Cmt", "") %>"/>
                    </td>
                </tr> 
                <tr>                    
                    <th valign="top">8. </th>
                    <th valign="top" colspan="3" class="question">
                        During the <font style="text-decoration:underline">past 4 weeks</font>, how much did <font style="text-decoration:underline">pain</font>
                        interfere with your normal work (including work both outside the home and housework)?
                    </th>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="8NotAtAll" <%= props.getProperty("8NotAtAll", "") %>/>
                    </td>
                    <td width="45%">Not at all</td>
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="8QuiteABit" <%= props.getProperty("8QuiteABit", "") %>/>
                    </td>
                    <td width="45%">Quite a bit</td>                
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="8Slightly" <%= props.getProperty("8Slightly", "") %>/>
                    </td>
                    <td width="45%">A little bit</td>                
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="8Extremely" <%= props.getProperty("8Extremely", "") %>/>
                    </td>
                    <td width="45%">Extremely</td>
                </tr>
                <tr bgcolor="white">
                    <td width="5%" align="right">
                        <input type="checkbox"  class="checkbox" name="8Moderately" <%= props.getProperty("8Moderately", "") %>/>
                    </td>
                    <td width="45%">Moderately</td>
                    <td width="5%"></td>
                    <td width="45%"></td>
                </tr>
                <tr bgcolor="white">
                    <td></td>
                    <td colspan="3">
                        Comments: <input type="text" size="80" name="8Cmt" value="<%= props.getProperty("8Cmt", "") %>"/>
                    </td>
                </tr>
                <tr><td><table border="0"  cellspacing="0" cellpadding="0" height="100px"><tr><td>&nbsp;</td></tr></table></td></tr>
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
</td></tr>
<tr><td valign="top">
<table border="0" cellspacing="0" cellpadding="0" style="display:none" width="740px" height="85%" id="page5" >    
    <tr>        
        <td colspan="2">
        <table width="740px" height="620px" border="0"  cellspacing="0" cellpadding="0" >
               <tr>                    
                    <th width="3%" valign="top" >9. </th>
                    <th valign="top" class="question">
                        These questions are about how you feel and how things have been with you
                        <font style="text-decoration:underline">during the past month</font>.
                        For each question, please indicate the one answer that comes closest to the way you have been feeling.
                    </th>
                </tr>                                
                <tr>
                    <td valign="top" colspan="2">
                        <table>
                            <tr class="question">
                                <th width="3%"></th>
                                <th width="26%"></th>
                                <th width="7%"  valign="top" align="center">All of the Time</th>
                                <th width="8%"  valign="top" align="center">Most of the Time</th>
                                <th width="12%"  valign="top" align="center">A Good bit of the Time</th>
                                <th width="8%"  valign="top" align="center">Some of the Time</th>
                                <th width="10%"  valign="top" align="center">A Little of the Time</th>
                                <th width="7%"  valign="top" align="center">None of the Time</th>
                                <th width="19%" valign="top" align="center">Comments</th>
                            </tr>
                            <tr>
                                <td>a. </td>
                                <td>
                                    Did you feel full of pep?
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9aAll" <%= props.getProperty("9aAll", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9aMost" <%= props.getProperty("9aMost", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9aGood" <%= props.getProperty("9aGood", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9aSome" <%= props.getProperty("9aSome", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9aLittle" <%= props.getProperty("9aLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9aNone" <%= props.getProperty("9aNone", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <textarea cols="15" rows="2" name="9aCmt"><%= props.getProperty("9aCmt", "") %></textarea>
                                </td>
                            </tr>  
                            <tr>
                                <td  valign="top">b. </td>
                                <td>
                                    Have you been a very nervous person?
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9bAll" <%= props.getProperty("9bAll", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9bMost" <%= props.getProperty("9bMost", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9bGood" <%= props.getProperty("9bGood", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9bSome" <%= props.getProperty("9bSome", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9bLittle" <%= props.getProperty("9bLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9bNone" <%= props.getProperty("9bNone", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <textarea cols="15" rows="2" name="9bCmt"><%= props.getProperty("9bCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top">c. </td>
                                <td>
                                    Have you felt so down in the dumps nothing could cheer you up?
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9cAll" <%= props.getProperty("9cAll", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9cMost" <%= props.getProperty("9cMost", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9cGood" <%= props.getProperty("9cGood", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9cSome" <%= props.getProperty("9cSome", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9cLittle" <%= props.getProperty("9cLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9cNone" <%= props.getProperty("9cNone", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <textarea cols="15" rows="2" name="9cCmt"><%= props.getProperty("9cCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top">d. </td>
                                <td>
                                    Have you felt calm and peaceful?
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9dAll" <%= props.getProperty("9dAll", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9dMost" <%= props.getProperty("9dMost", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9dGood" <%= props.getProperty("9dGood", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9dSome" <%= props.getProperty("9dSome", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9dLittle" <%= props.getProperty("9dLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9dNone" <%= props.getProperty("9dNone", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <textarea cols="15" rows="2" name="9dCmt"><%= props.getProperty("9dCmt", "") %></textarea>
                                </td>
                            </tr>
                             <tr>
                                <td  valign="top">e. </td>
                                <td>
                                    Did you have a lot of energy?
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9eAll" <%= props.getProperty("9eAll", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9eMost" <%= props.getProperty("9eMost", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9eGood" <%= props.getProperty("9eGood", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9eSome" <%= props.getProperty("9eSome", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9eLittle" <%= props.getProperty("9eLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9eNone" <%= props.getProperty("9eNone", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <textarea cols="15" rows="2" name="9eCmt"><%= props.getProperty("9eCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td  valign="top">f. </td>
                                <td>
                                    Have you felt downhearted and blue?
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9fAll" <%= props.getProperty("9fAll", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9fMost" <%= props.getProperty("9fMost", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9fGood" <%= props.getProperty("9fGood", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9fSome" <%= props.getProperty("9fSome", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9fLittle" <%= props.getProperty("9fLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9fNone" <%= props.getProperty("9fNone", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <textarea cols="15" rows="2" name="9fCmt"><%= props.getProperty("9fCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td>g. </td>
                                <td>
                                    Did you feel worn out?
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9gAll" <%= props.getProperty("9gAll", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9gMost" <%= props.getProperty("9gMost", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9gGood" <%= props.getProperty("9gGood", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9gSome" <%= props.getProperty("9gSome", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9gLittle" <%= props.getProperty("9gLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9gNone" <%= props.getProperty("9gNone", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <textarea cols="15" rows="2" name="9gCmt"><%= props.getProperty("9gCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td valign="top">h. </td>
                                <td>
                                    Have you been a happy person?
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9hAll" <%= props.getProperty("9hAll", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9hMost" <%= props.getProperty("9hMost", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9hGood" <%= props.getProperty("9hGood", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9hSome" <%= props.getProperty("9hSome", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9hLittle" <%= props.getProperty("9hLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9hNone" <%= props.getProperty("9hNone", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <textarea cols="15" rows="2" name="9hCmt"><%= props.getProperty("9hCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td>i. </td>
                                <td>
                                    Did you feel tired?
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9iAll" <%= props.getProperty("9iAll", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9iMost" <%= props.getProperty("9iMost", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9iGood" <%= props.getProperty("9iGood", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9iSome" <%= props.getProperty("9iSome", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9iLittle" <%= props.getProperty("9iLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9iNone" <%= props.getProperty("9iNone", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <textarea cols="15" rows="2" name="9iCmt"><%= props.getProperty("9iCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td valign="top">j. </td>
                                <td>
                                    Has your <font style='text-decoration:underline'>health limited your social activities </font>
                                    (like visiting with friends or close relatives)?
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9jAll" <%= props.getProperty("9jAll", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9jMost" <%= props.getProperty("9jMost", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9jGood" <%= props.getProperty("9jGood", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9jSome" <%= props.getProperty("9jSome", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9jLittle" <%= props.getProperty("9jLittle", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="9jNone" <%= props.getProperty("9jNone", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <textarea cols="15" rows="2" name="9jCmt"><%= props.getProperty("9jCmt", "") %></textarea>
                                </td>
                            </tr>
                        </table>
                    </td>                    
                </tr>          
            </table>              
        </td>
    </tr>    
    <tr class="subject">
        <td align="left">
            <a href="javascript: goToPage4();"><< Previous Page</a>
        </td>    
        <td align="right">
            <a href="javascript: goToPage6();">Next Page >></a>
        </td>
    </tr>
</table>
</td></tr>
<tr><td valign="top">
<table border="0" cellspacing="0" cellpadding="0" style="display:none" width="740px" height="85%" id="page6" >    
    <tr>        
        <td valign="top" colspan="2">
        <table width="740px" height="620px" border="0"  cellspacing="0" cellpadding="0" >
               <tr>                    
                    <th width="3%" valign="top" >10. </th>
                    <th valign="top" class="question">
                        Please choose the answer that best describes how <font style="text-decoration:underline">true</font> or
                        <font style="text-decoration:underline">false</font> each of the following statements is for you.
                    </th>
                </tr>                                
                <tr>
                    <td valign="top" colspan="2">
                        <table>
                            <tr class="question">
                                <th width="3%"></th>
                                <th width="30%"></th>
                                <th width="8%"  valign="top" align="center">Definitely True</th>
                                <th width="8%"  valign="top" align="center">Mostly True</th>
                                <th width="8%"  valign="top" align="center">Not sure</th>
                                <th width="8%"  valign="top" align="center">Mostly False</th>
                                <th width="8%"  valign="top" align="center">Definitely False</th>                                
                                <th width="27%" valign="top" align="center">Comments</th>
                            </tr>
                            <tr>
                                <td valign="top">a. </td>
                                <td>
                                    I seem to get sick a little easier than other people.
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10aDefTrue" <%= props.getProperty("10aDefTrue", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10aMostTrue" <%= props.getProperty("10aMostTrue", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10aNotSure" <%= props.getProperty("10aNotSure", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10aMostFalse" <%= props.getProperty("10aMostFalse", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10aDefFalse" <%= props.getProperty("10aDefFalse", "") %>/>
                                </td>                                
                                <td align="center" bgcolor="white">
                                    <textarea cols="20" rows="2" name="10aCmt"><%= props.getProperty("10aCmt", "") %></textarea>
                                </td>
                            </tr> 
                            <tr>
                                <td valign="top">b. </td>
                                <td>
                                    I am as healthy as anybody I know.
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10bDefTrue" <%= props.getProperty("10bDefTrue", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10bMostTrue" <%= props.getProperty("10bMostTrue", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10bNotSure" <%= props.getProperty("10bNotSure", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10bMostFalse" <%= props.getProperty("10bMostFalse", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10bDefFalse" <%= props.getProperty("10bDefFalse", "") %>/>
                                </td>                                
                                <td align="center" bgcolor="white">
                                    <textarea cols="20" rows="2" name="10bCmt"><%= props.getProperty("10bCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td valign="top">c. </td>
                                <td>
                                    I expect my health to get worse.
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10cDefTrue" <%= props.getProperty("10cDefTrue", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10cMostTrue" <%= props.getProperty("10cMostTrue", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10cNotSure" <%= props.getProperty("10cNotSure", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10cMostFalse" <%= props.getProperty("10cMostFalse", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10cDefFalse" <%= props.getProperty("10cDefFalse", "") %>/>
                                </td>                                
                                <td align="center" bgcolor="white">
                                    <textarea cols="20" rows="2" name="10cCmt"><%= props.getProperty("10cCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td>d. </td>
                                <td>
                                    My health is excellent.
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10dDefTrue" <%= props.getProperty("10dDefTrue", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10dMostTrue" <%= props.getProperty("10dMostTrue", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10dNotSure" <%= props.getProperty("10dNotSure", "") %>/>
                                </td> 
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10dMostFalse" <%= props.getProperty("10dMostFalse", "") %>/>
                                </td>
                                <td align="center" bgcolor="white">
                                    <input type="checkbox"  class="checkbox" name="10dDefFalse" <%= props.getProperty("10dDefFalse", "") %>/>
                                </td>                                
                                <td align="center" bgcolor="white">
                                    <textarea cols="20" rows="2" name="10dCmt"><%= props.getProperty("10dCmt", "") %></textarea>
                                </td>
                            </tr>
                            <tr><td><table border="0"  cellspacing="0" cellpadding="0" height="300px"><tr><td>&nbsp;</td></tr></table></td></tr>
                        </table>
                    </td>                    
                </tr>          
            </table>              
        </td>
    </tr>    
    <tr class="subject">
        <td align="left">
            <a href="javascript: goToPage5();"><< Previous Page</a>
        </td>    
        <td>
           &nbsp;
        </td>
    </tr>
</table>
</td></tr>
<tr><td valign="top">
<table class="Head" class="hidePrint" height="5%">
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
</table>
</td></tr>
</table>
</html:form>
</body>
</html:html>
