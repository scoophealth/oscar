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
<logic:present name="groupName"><bean:write name="groupName"/></logic:present> <bean:message key="oscarEncounter.Index.measurements"/>
</title>
<html:base/>

</head>

<script type="text/javascript">

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
    <html:errors/>
    <html:form action="/oscarEncounter/Measurements" enctype="multipart/form-data">
    <link rel="stylesheet" type="text/css" href="<bean:write name="css" />">
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <logic:present name="groupName"><bean:write name="groupName"/></logic:present>
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar" >                 
                    <tr>
                        <td width=70% class="Header" style="padding-left:2px;padding-right:2px;border-right:2px solid #003399;text-align:left;font-size:80%;font-weight:bold;width:100%;" NOWRAP >                        
                            <logic:present name="EctSessionBean"><bean:write name="EctSessionBean" property="patientLastName"/> <bean:write name="EctSessionBean" property="patientFirstName"/> <bean:write name="EctSessionBean" property="patientSex"/> <bean:write name="EctSessionBean" property="patientAge"/></logic:present>
                        </td>
                        <td style="text-align:right" NOWRAP>
                            <a href="javascript:popupStart(300,400,'Help.jsp')"  ><bean:message key="global.help" /></a> | <a href="javascript:popupStart(300,400,'About.jsp')" ><bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')" ><bean:message key="global.license" /></a>
                        </td>
                    </tr>                  
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">             
            </td>            
            <td class="MainTableRightColumn">
               <table border=0 cellspacing=0 >
                <tr>
                    <td>
                        <table>
                            <tr>
                                <td>
                                    <table>                                        
                                        <tr>
                                            <td>               
                                                <tr class="Header">
                                                    <td align="left"  width="100">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingType"/>
                                                    </td>
                                                    <td align="left"  width="200">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingTypeDesc"/>
                                                    </td>
                                                    <td align="left"  width="160">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingMeasuringInstrc"/>
                                                    </td>
                                                    <td align="left"  width="50">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingValue"/>
                                                    </td>
                                                    <td align="left"  width="140">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingComments"/>
                                                    </td>
                                                    <td align="left"  width="150">
                                                        <bean:message key="oscarEncounter.oscarMeasurements.Measurements.headingObservationDate"/>
                                                    </td>
                                                    <td align="left"  width="20">                                                        
                                                    </td>
                                                 </tr>
                                                <% int i = 0;%>
                                                <logic:iterate id="measurementType" name="measurementTypes" property="measurementTypeVector" indexId = "ctr" >
                                                <tr class="data">                          
                                                    <td width="5"><bean:write name="measurementType" property="typeDisplayName" /></td>
                                                    <td><bean:write name="measurementType" property="typeDesc" /></td>
                                                    <td>                           
                                                        <html:select property='<%= "value(inputMInstrc-" + ctr + ")" %>' style="width:160">
                                                            <html:options collection='<%="mInstrcs"+ ctr%>' property="measuringInstrc" labelProperty="measuringInstrc" style="width:160"/>
                                                        </html:select>                                
                                                    </td>
                                                    <td><html:text property='<%= "value(inputValue-" + ctr + ")" %>' size="5" /></td>     
                                                    <td><html:text property='<%= "value(comments-" + ctr + ")" %>' size="20"/></td>
                                                    <td><html:text property='<%= "value(date-" + ctr + ")" %>' size="20"/></td>
                                                    <td width="20"></td>                            
                                                    <input type="hidden" name='<%= "value(inputType-" + ctr + ")" %>' value="<bean:write name="measurementType" property="type" />"/>
                                                    <input type="hidden" name='<%= "value(inputTypeDisplayName-" + ctr + ")" %>' value="<bean:write name="measurementType" property="typeDisplayName" />"/>                            
                                                    <input type="hidden" name='<%= "value(validation-" + ctr + ")" %>' value="<bean:write name="measurementType" property="validation" />"/></td>
                                                    <% i++; %>
                                                </tr>
                                                <logic:present name='measurementType' property='lastMInstrc'>
                                                <tr class="note">
                                                    <td colspan="2"><bean:message key="oscarEncoutner.oscarMeasurements.msgTheLast"/> <bean:message key="oscarEncoutner.oscarMeasurements.msgValueWasEnteredBy"/> <bean:write name='measurementType' property='lastProviderFirstName'/> <bean:write name='measurementType' property='lastProviderLastName'/></td>                                                    
                                                    <td><bean:write name='measurementType' property='lastMInstrc'/></td>
                                                    <td><bean:write name='measurementType' property='lastData'/></td>
                                                    <td><bean:write name='measurementType' property='lastComments'/></td>
                                                    <td><bean:write name='measurementType' property='lastDateEntered'/></td>                                                    
                                                    <td><img src="img/history.gif" title='<bean:message key="oscarEncounter.Index.oldMeasurements"/>' onClick="popupPage(300,800,'SetupDisplayHistory.do?type=<bean:write name="measurementType" property="type" />'); return false;" /></td>
                                                </tr>
                                                </logic:present>
                                                </logic:iterate>                        
                                                <input type="hidden" name="value(numType)" value="<%=String.valueOf(i)%>"/>                                                
                                                <input type="hidden" name="value(groupName)" value="<bean:write name="groupName"/>"/>
                                                <input type="hidden" name="value(css)" value="<bean:write name="css"/>"/>
                                            </td>
                                        </tr>
                                    </table>
                                    <table>
                                    <tr>
                                        <td><input type="button" name="Button" value="<bean:message key="global.btnCancel"/>" onClick="window.close()"></td>
                                        <td><input type="button" name="Button" value="<bean:message key="global.btnSubmit"/>" onclick="document.forms['EctMeasurementsForm'].submit();"/></td>
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
</html:form>
</body>
</html:html>

