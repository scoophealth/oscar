<%@ page language="java"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
    String formClass = "GripStrength";
    String formLink = "formgripstrength.jsp";

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
    <title>Grip Strength Measurements (Kgs)</title>
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
            height:15pt;
        }

        .title {
            background-color: #486ebd;
            color: #FFFFFF;            
            font-weight: bold;
            text-align: left;
            text-align: center;
        }
        .subTitle {
            backgroud-color: #F2F2F2;
            font-weight: bold;
            text-align: center;             
        }
        .question{
            text-align: left;
            vertical-align: top;
        }
        .answer{            
            vertical-align: center;
            background-color: white;
        }
        .answerYN{ 
            text-align: center;
            vertical-align: center;
            background-color: white;
        }
        .score{
            font-size=80%;
        }
    </style>
</head>


<script type="text/javascript" language="Javascript">
    
    var choiceFormat  = null;
    var allNumericField = new Array(6,7,8,9,10,11,12,13);
    var allMatch = null;
    
    var action = "/<%=project_home%>/form/formname.do";    
    var domValues = new Array(6,8,10);
    var nonDomValues = new Array(7,9,11);
    var domResult = 12;
    var nonDomResult = 13;
    
    function calScore(values, result){
        var score = 0;
        for(var i=0; i<values.length; i++){
            var item = values[i];
            score = score + eval(document.forms[0].elements[item].value);
        }
            document.forms[0].elements[result].value=score/(values.length);        
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
<!--input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" /-->
<input type="hidden" name="submit" value="exit"/>

<table border="0" cellspacing="0" cellpadding="0" width="740px" height="710px">
<tr><td>
<table border="0" cellspacing="0" cellpadding="0" width="740px" >
 <tr>
      <th class="subject">Grip Strength Measurements (Kgs)</th>
 </tr>
</table>
</td></tr>
<tr><td valign="top">
<table border="0" cellspacing="0" cellpadding="0" height="660px" width="740px" id="page1">    
    <tr>        
        <td valign="top" colspan="2">
            <table width="100%" height="300px" border="0"  cellspacing="1px" cellpadding="0" >                                        
                <tr class="title">
                    <th colspan="2">
                    Time 1
                    </th>                        
                </tr> 
                <tr class="title" >
                    <th align="center"><font style="font-size: 85%">
                    Dominant Limb
                    </font></th>
                    <th align="center"><font style="font-size: 85%">
                    Non-Dominant Limb
                    </font></th>
                </tr>                
                <tr>
                    <td bgcolor="white" align="center">
                        1 = <input type="text" onchange="javascript: calScore(domValues, domResult);" name="dom1" value="<%= props.getProperty("dom1", "") %>"/> 
                    </td>
                    <td bgcolor="white" align="center">
                        1 = <input type="text" onchange="javascript: calScore(nonDomValues, nonDomResult);" name="nonDom1" value="<%= props.getProperty("nonDom1", "") %>"/>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="white" align="center">
                        2 = <input type="text" onchange="javascript: calScore(domValues, domResult);" name="dom2" value="<%= props.getProperty("dom2", "") %>"/> 
                    </td>
                    <td bgcolor="white" align="center">
                        2 = <input type="text" onchange="javascript: calScore(nonDomValues, nonDomResult);" name="nonDom2" value="<%= props.getProperty("nonDom2", "") %>"/>
                    </td>
                </tr>                
                <tr>
                    <td bgcolor="white" align="center">
                        3 = <input type="text" onchange="javascript: calScore(domValues, domResult);" name="dom3" value="<%= props.getProperty("dom3", "") %>"/> 
                    </td>
                    <td bgcolor="white" align="center">
                        3 = <input type="text" onchange="javascript: calScore(nonDomValues, nonDomResult);" name="nonDom3" value="<%= props.getProperty("nonDom3", "") %>"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        &nbsp;
                    </td>
                </tr>
                <tr>
                    <td bgcolor="white" align="center">
                        Average = <input type="text" readonly="true" name="domAvg" value="<%= props.getProperty("domAvg", "") %>"/> 
                    </td>
                    <td bgcolor="white" align="center">
                        Average = <input type="text" readonly="true" name="nonDomAvg" value="<%= props.getProperty("nonDomAvg", "") %>"/>
                    </td>
                </tr>                               
            </table>            
        </td>
    </tr>    
</table>
</td></tr>
<tr><td valign="top">
<table class="Head" class="hidePrint">
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
        <td align="right">
            Study ID: <%= props.getProperty("studyID", "N/A") %>
            <input type="hidden" name="studyID" value="<%= props.getProperty("studyID", "N/A") %>"/>
        </td>
    </tr>
</table>
</td></tr>
</table>
</html:form>
</body>
</html:html>
