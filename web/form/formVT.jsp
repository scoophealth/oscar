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
 <%
  if(session.getValue("user") == null) response.sendRedirect("../../logout.jsp");
%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="oscar.oscarEncounter.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.pageUtil.*"%>
<%@ page import="java.util.Vector;"%>
<%
    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", 0); //prevents caching at the proxy   
%>

<html:html locale="true">

<head>
<title>
Vascular Tracker
</title>
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
            font-size:9pt;
        }

        TH{
            font-size:10pt;
            font-weight: normal;
            text-align:left;
        }
        

        .subject {
            background-color: #000000;
            color: #FFFFFF;  
            font-size: 15pt;
            font-weight: bold;
            text-align: left;
        }

        .title {
            background-color: #486ebd;
            color: #FFFFFF;            
            font-weight: bold;
            text-align: left;
            vertical-align: top;
        }
        .subTitle {
            backgroud-color: #F2F2F2;
            font-weight: bold;
            text-align: left;
            vertical-align: top;
        }
        .note {
            font-size:80%; 
            color:#3366CC;
        }
        .data {
            background-color:#F7F7F7;
            font-size:80%;
        }
        
        

    </style>

<html:base/>

</head>

<script type="text/javascript">

    function onPrint() {
//        document.forms[0].submit.value="print";
//        var ret = checkAllDates();
//        if(ret==true)
//        {
//            ret = confirm("Do you wish to save this form and view the print preview?");
//        }
//        return ret;
        window.print();
    }
    function onSave() {
        document.forms[0].submit.value="save";
        //var ret = checkAllDates();
        if(ret==true)
        {
            ret = confirm("Are you sure you want to save this form?");
        }
        return ret;
    }
    function onExit() {
        if(confirm("Are you sure you wish to exit without saving your changes?")==true)
        {
            window.close();
        }
        return(false);
    }
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = checkAllDates();
        if(ret == true)
        {
            ret = confirm("Are you sure you wish to save and close this window?");
        }
        return ret;
    }

function write2Parent(text){
    
    self.close();
    opener.document.encForm.enTextarea.value = opener.document.encForm.enTextarea.value + text;
 }

function getDropboxValue(ctr){   
    var selectedItem = document.forms[0].value(inputMInstrc-ctr).options[document.forms[0].value(inputMInstrc-ctr).selectedIndex].value;
    alert("hello!");
}

function popupPage(vheight,vwidth,page) { //open a new popup window
    
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(page, "blah", windowprops);  
}
</script>
<body class="BodyStyle" vlink="#0000FF" onload="window.focus();">
<!--  -->
    
    <html:form action="/oscarEncounter/oscarMeasurements/oscarForm/VTForm" enctype="multipart/form-data">    
    <link rel="stylesheet" type="text/css" href="../oscarEncounter/oscarMeasurements/styles/measurementStyle.css">    
    <link rel="stylesheet" type="text/css" media="print" href="print.css"/>
    <table class="Head" class="hidePrint" width="100%">
        <tr>
            <td align="left">
                <input type="hidden" name="submit" value="exit"/>
                <input type="submit" value="Save" onclick="javascript:return onSave();" />
                <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
                <input type="submit" value="Exit" onclick="javascript:return onExit();"/>
                <input type="button" value="Print" onclick="javascript:return onPrint();"/>
            </td>
        </tr>
    </table>
    <table>
        <tr>            
            <td class="subject">
                VASCULAR TRACKER
            </td>
        </tr>
        <tr>                     
            <td>
               <table border=0 cellspacing=0 >
                <tr>
                    <td>
                        <table>
                            <tr>
                                <td>
                                    <table>
                                    <html:errors/>
                                        <tr>
                                            <td>               
                                                <tr>
                                                    <td colspan="6">
                                                        <logic:present name="EctSessionBean"><bean:write name="EctSessionBean" property="patientLastName"/> <bean:write name="EctSessionBean" property="patientFirstName"/> <bean:write name="EctSessionBean" property="patientSex"/> <bean:write name="EctSessionBean" property="patientAge"/></logic:present>
                                                    </td>
                                                </tr>                                                
                                                <tr class="title">
                                                    <th align="left"  width="150">                                                        
                                                    </th>                                                    
                                                    <th align="left"  width="160">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingMeasuringInstrc"/>
                                                    </th>
                                                    <th align="left"  width="50">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingValue"/>
                                                    </th>                                                    
                                                    <th align="left"  width="150">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingObservationDate"/>
                                                    </th>
                                                    <th align="left"  width="200">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingComments"/>
                                                    </th>
                                                    <th align="left"  width="10">                                                        
                                                    </th>
                                                 </tr>
                                                <% int i = 0;%>
                                                <logic:iterate id="measurementType" name="measurementTypes" property="measurementTypeVector" indexId = "ctr" >
                                                <tr class="data">                          
                                                    <th width="150" class="subTitle"><bean:write name="measurementType" property="typeDesc" /></th>
                                                    <td><bean:write name="measurementType" property="measuringInstrc"/></td>
                                                    <td><html:text property='<%= "value(inputValue-" + ctr + ")" %>' size="5" /></td>                                                         
                                                    <td><html:text property='<%= "value(date-" + ctr + ")" %>' size="20"/></td>
                                                    <td><html:text property='<%= "value(comments-" + ctr + ")" %>' size="45"/></td>
                                                    <td width="10"></td>
                                                    <input type="hidden" name='<%= "value(inputMInstrc-" + ctr + ")" %>' value="<bean:write name="measurementType" property="measuringInstrc"/>"/>
                                                    <input type="hidden" name='<%= "value(inputType-" + ctr + ")" %>' value="<bean:write name="measurementType" property="type" />"/>
                                                    <input type="hidden" name='<%= "value(inputTypeDisplayName-" + ctr + ")" %>' value="<bean:write name="measurementType" property="typeDisplayName" />"/>                            
                                                    <input type="hidden" name='<%= "value(validation-" + ctr + ")" %>' value="<bean:write name="measurementType" property="validation" />"/>
                                                    <% i++; %>
                                                </tr>
                                                <logic:present name='measurementType' property='lastMInstrc'>
                                                <tr class="note">
                                                    <td><bean:message key="oscarEncoutner.oscarMeasurements.msgTheLastValue"/>: </td>                                                    
                                                    <td><bean:write name='measurementType' property='lastMInstrc'/></td>
                                                    <td><bean:write name='measurementType' property='lastData'/></td>
                                                    <td><bean:write name='measurementType' property='lastDateEntered'/></td>
                                                    <td><bean:write name='measurementType' property='lastComments'/></td>                                                                                                        
                                                    <td class="hidePrint"><img src="img/history.gif" title='<bean:message key="oscarEncounter.Index.oldMeasurements"/>' onClick="popupPage(300,800,'SetupDisplayHistory.do?type=<bean:write name="measurementType" property="type" />'); return false;" /></td>
                                                </tr>
                                                </logic:present>
                                                </logic:iterate>                        
                                                <input type="hidden" name="value(numType)" value="<%=String.valueOf(i)%>"/>                                                
                                                
                                            </td>
                                        </tr>
                                    </table>                                    
                                </td>   
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">

            </td>
            <td class="MainTableBottomRowRightColumn">

            </td>
        </tr>
    </table>
    <table class="Head" class="hidePrint" width="100%">
        <tr>
            <td align="left">
                <input type="hidden" name="submit" value="exit"/>
                <input type="submit" value="Save" onclick="javascript:return onSave();" />
                <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
                <input type="submit" value="Exit" onclick="javascript:return onExit();"/>
                <input type="button" value="Print" onclick="javascript:return onPrint();"/>
            </td>
        </tr>
    </table>
</html:form>
</body>
</html:html>

