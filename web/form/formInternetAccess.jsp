
<%@ page language="java"%>
<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<%
    String formClass = "InternetAccess";
    String formLink = "formInternetAccess.jsp";

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
<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>

<head>
    <title>Internet Access</title>
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
            height: 25px;
            width: 25px;     
            background-color: #FFFFFF;
        }

        .checkboxError{
            height: 25px;
            width: 25px;     
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
            text-align: left;
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
    
    var choiceFormat  = new Array(6,7);        
    var allNumericField = null;
    var allMatch = null;
    var action = "/<%=project_home%>/form/formname.do";        

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
        <th class="subject">Internet Access</th>
    </tr>    
</table>
</td></tr>
<tr><td valign="top">
<table border="0" cellspacing="0" cellpadding="0" height="85%" width="740px" id="page1">        
    <tr>        
        <td colspan="2" valign="top">
            <table width="740px" height="200px" border="0"  cellspacing="0" cellpadding="0" >                  
                <tr>
                    <td width="5%" valign="top" align="right"></td>
                    <td valign="top">                        
                        1. Do you have access to the internet?
                    </td>
                </tr>
                <tr>                                                
                <tr bgcolor="white">                                    
                    <td width="5%" align="right">                        
                    </td>
                    <td width="95%">
                        <input type="checkbox"  class="checkbox" name="internetY" <%= props.getProperty("internetY", "") %>/>
                        Yes
                    </td>                                  
                </tr>
                <tr bgcolor="white">                                    
                    <td width="5%" align="right">                        
                    </td>
                    <td width="95%">
                        <input type="checkbox"  class="checkbox" name="internetN" <%= props.getProperty("internetN", "") %>/>
                        No
                    </td>                                  
                </tr>                
                <tr>
                    <td width="5%" valign="top" align="right"></td>
                    <td valign="top">                        
                        2. If yes, where do you access the internet?
                    </td>
                </tr>
                <tr>                                                
                <tr bgcolor="white">                                    
                    <td width="5%" align="right">                        
                    </td>
                    <td width="95%">
                        <input type="checkbox"  class="checkbox" name="internetHome" <%= props.getProperty("internetHome", "") %>/>
                        at home
                    </td>                                  
                </tr>
                <tr bgcolor="white">                                    
                    <td width="5%" align="right">                        
                    </td>
                    <td width="95%">
                        <input type="checkbox"  class="checkbox" name="internetWork" <%= props.getProperty("internetWork", "") %>/>
                        at work
                    </td>                                  
                </tr>
                <tr bgcolor="white">                                    
                    <td width="5%" align="right">                        
                    </td>
                    <td width="95%">
                        <input type="checkbox"  class="checkbox" name="internetFriend" <%= props.getProperty("internetFriend", "") %>/>
                        at friend/relative's home
                    </td>                                  
                </tr>
                <tr bgcolor="white">                                    
                    <td width="5%" align="right">                        
                    </td>
                    <td width="95%">
                        <input type="checkbox"  class="checkbox" name="internetOther" <%= props.getProperty("internetOther", "") %>/>
                        other 
                        <input type="text" name="internetOtherTx" value="<%= props.getProperty("internetOtherTx", "") %>"/>
                    </td>                                  
                </tr>
            </table>            
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
            <input type="submit" value="Save and Exit" onclick="javascript: return onSaveExit();"/>
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
